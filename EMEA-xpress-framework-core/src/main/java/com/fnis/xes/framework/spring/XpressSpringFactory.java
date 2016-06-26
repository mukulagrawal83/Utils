package com.fnis.xes.framework.spring;

import com.fnf.xes.framework.ServiceException;
import com.fnis.xes.framework.IdConstants;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User: lc21878
 */
public class XpressSpringFactory {
    private static Logger log = Logger.getLogger(XpressSpringFactory.class);
    private static Properties jefConfig = new Properties();
    private static final String FACTORY_CONFIG_NAME = "jefConfig.properties";
    private static String MAX_POOL_SIZE = "maxActive";
    private static String DFLT_MAX_POOL_SIZE = "5";
    private static String DFLT_MIN_POOL_SIZE = "1";
    private static String MIN_POOL_SIZE = "minActive";

    private static Map<String, GenericObjectPool> springPools = Collections.synchronizedMap(new HashMap<String, GenericObjectPool>());

    /**
     * Load the Jef configuration file.
     */
    static {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(FACTORY_CONFIG_NAME);
        if (inputStream == null) {
            String msg = String.format("XpressSpringFactory. Configuration file not found: : %s", FACTORY_CONFIG_NAME);
            System.out.println("****** Unable to locate jefconfig.properties file");
            throw new IllegalArgumentException(msg);
        }
        try {
            jefConfig.load(inputStream);
            inputStream.close();
        } catch (Exception ex) {
            String msg = String.format("XpressSpringFactory. Error loading configuration file: %s.  Caused by: %s", FACTORY_CONFIG_NAME, ex.getMessage());
            log.error(msg, ex);
            throw new IllegalArgumentException(msg, ex);
        }
    }

    /**
     * @param springPropertyName
     * @return
     * @throws Exception
     */
    public static GenericObjectPool getContextPool(String springPropertyName) throws Exception {
        // translate from property name to spring configuration file name
        String springConfig = jefConfig.getProperty(springPropertyName);
        if (springConfig == null) {
            throw new ServiceException(IdConstants.ERR_BOC_CREATION_ERROR,String.format("No spring configuration property name: %s",springPropertyName));
        }
        GenericObjectPool springPool = (GenericObjectPool) springPools.get(springConfig);
        if (springPool == null) {
            int maxActive = 0;
            int minActive = 0;
            String maxActiveVal = null;
            String minActiveVal = null;
            try {
                maxActiveVal = jefConfig.getProperty(springPropertyName + "." + MAX_POOL_SIZE, DFLT_MAX_POOL_SIZE);
                maxActive = Integer.parseInt(maxActiveVal);
                minActiveVal = jefConfig.getProperty(springPropertyName + "." + MIN_POOL_SIZE, DFLT_MIN_POOL_SIZE);
                minActive = Integer.parseInt(minActiveVal);
            } catch (NumberFormatException nfe) {
                String msg = String.format("XpressSpringFactory: Invalid spring pool configuration. %s = %s, %s = %s",
                        springPropertyName + "." + MAX_POOL_SIZE, DFLT_MAX_POOL_SIZE, maxActiveVal,
                        springPropertyName + "." + MIN_POOL_SIZE, DFLT_MIN_POOL_SIZE, minActiveVal);
                log.error(msg);
                throw new IllegalArgumentException(msg, nfe);
            }

            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            poolConfig.setMaxTotal(maxActive);
            poolConfig.setMaxIdle(-1);
            poolConfig.setMaxWaitMillis(-1);
            poolConfig.setMinIdle(minActive);
            String parentSpringConfig = (String) jefConfig.get(springConfig + "." + "parent");
            springPool = new GenericObjectPool(new SpringPoolFactory(springConfig.trim(), parentSpringConfig), poolConfig);
            // warm up the pool
            for (int ii = 0; ii < poolConfig.getMinIdle(); ii++) {
                springPool.addObject();
            }
            // cache the pool for this configuration file.
            springPools.put(springConfig,springPool);
        }
        return springPool;
    }
}
