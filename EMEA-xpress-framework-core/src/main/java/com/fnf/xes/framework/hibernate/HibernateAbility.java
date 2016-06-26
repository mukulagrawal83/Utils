package com.fnf.xes.framework.hibernate;

import java.util.Hashtable;
import java.util.Properties;

import javax.management.Attribute;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResourceException;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.ability.Ability;
import com.fnf.jef.boc.ability.JMXAbility;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;


public class HibernateAbility
    implements RequestFilter, Ability
{
	 

    public HibernateAbility()
    {
    }

    public Session getSession()
    {
        return hbSession;
    }

    public void setJMXAbility(JMXAbility ability){
    	this.jmxAbility= ability;
    }
    public JMXAbility  getJMXAbility(){
    	return jmxAbility;
    }
    public void initializeAbility(Properties configProperties)
        throws BocException
    {
        synchronized(initLock)
        {
            if(hbsf == null)
            {
                try
                {
                	String confFile = configProperties.getProperty("HibernateConfigFile");
                	
                    Configuration cfg = new Configuration();
                	if (confFile != null && !confFile.equals("")) {
						cfg.configure(confFile);
					}else{                	
						cfg.configure();
					}
                	
                    hbsf = cfg.buildSessionFactory();           	
                    enableHibernateStatsCollection(configProperties, cfg);
                }catch(HibernateException he){
                    throw new BocException("HibernateService Failed", he);
                }
                
            }
        }
        String hibernateManagedTxnProperty = configProperties.getProperty("HibernateManagedTransaction", "true");
        if(hibernateManagedTxnProperty.equalsIgnoreCase("true"))
        {
            logger.info("HibernateAbility will handle transacation demarcation.");
            hibernateManagedTxn = true;
        } else
        {
            hibernateManagedTxn = false;
        }
    }

	private void enableHibernateStatsCollection(Properties configProperties, Configuration cfg) {
		if(logger.isDebugEnabled())
			logger.debug("enableHibernateStatsCollection() - start");
		
		if(Boolean.parseBoolean(cfg.getProperty(Environment.GENERATE_STATISTICS))){
			try{
				String statisticsServiceMBeanName = configProperties.getProperty("HibernateStatisticsLoggerMBeanName");
				if(statisticsServiceMBeanName != null){
		    		Hashtable<String, String> props = jmxAbility.getImplSpecificKeyProperties();
		    		ObjectName name = new ObjectName(statisticsServiceMBeanName);
		    		if(props != null && !props.isEmpty()){
		    			Hashtable<String, String> p = name.getKeyPropertyList();
		    			p.putAll(props);
		    			name = new ObjectName(name.getDomain(), p);
		    			if(logger.isDebugEnabled())
		    				logger.debug("name -"+ name );
		    		}                        			
		    		Attribute attribute = new Attribute("SessionFactory", hbsf);
		    		jmxAbility.getMBeanServer().setAttribute(name, attribute);                    			
				}
		 	}catch(Exception exp){
				logger.error(exp.getMessage(), exp);
			}
		}
		if(logger.isDebugEnabled())
			logger.debug("enableHibernateStatsCollection() - end");
	}
	

    public void initializeFilter(Properties properties)
        throws BocException
    {
    }

    public void doFilter(RequestMessage requestMessage, ResponseMessage responseMessage, RequestLink requestLink)
        throws Throwable
    {
        Transaction txn = null;
        try
        {
            hbSession = hbsf.openSession();
            if(hibernateManagedTxn) {
                txn = hbSession.beginTransaction();
            }
        }
        catch(HibernateException he)
        {
            throw new ResourceException("HibernateAbility:doFilter," + he, he);
        }
        try
        {
            requestLink.doFilter(requestMessage, responseMessage);
            try
            {
                hbSession.flush();
            }
            catch(HibernateException he)
            {
                throw new ResourceException("Failed to flush the hibernate session. ", he);
            }
            try
            {
                if(hibernateManagedTxn)
                {
                   logger.info("DB command(s) successful.  Commiting the transaction.");
                   if (txn != null && txn.isActive() && !txn.wasCommitted()) {
                      txn.commit();
                   }
                }
            }
            catch(HibernateException he)
            {
                throw new ResourceException("Failed to commit the hibernate database transactions. ", he);
            }
        }
        catch(Throwable t)
        {
            try
            {
                if(hibernateManagedTxn)
                {
                    logger.info("DB command(s) failed.  Rolling back the transaction.");
                    logger.info("Reason for rollback: " + t.getMessage());
                    if (txn != null && txn.isActive() && !(txn.wasCommitted() || txn.wasRolledBack())) {
                       txn.rollback();
                    }
                }
            }
            catch(HibernateException he)
            {
                throw new ResourceException("Failed to rollback the hibernate database transactions. Original rollback reason: " + t.getMessage(), he);
            }
            throw t;
        }
        finally
        {
            try
            {
                hbSession.close();
            }
            catch(HibernateException he)
            {
                throw new ResourceException("HibernateAbility:doFilter," + he.getMessage(), he);
            }
        }
    }
    private static Logger logger = Logger.getLogger(HibernateAbility.class);
    private static SessionFactory hbsf = null;
    private static Object initLock = new Object();
    private Session hbSession;
    private JMXAbility jmxAbility = null;
    private boolean hibernateManagedTxn = false;

}
