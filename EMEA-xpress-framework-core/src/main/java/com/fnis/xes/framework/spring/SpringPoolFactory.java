package com.fnis.xes.framework.spring;

import java.util.HashMap;

import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.Container;

import com.fnf.jef.boc.StatusMsgConstants;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: lc21878
 */
public class SpringPoolFactory  extends BasePooledObjectFactory<Container> {
    private static Logger log = Logger.getLogger(SpringPoolFactory.class);
    private String springConfigName;
    private String[] springConfigs;
    private static HashMap<String,ClassPathXmlApplicationContext> parentSpringContainers = new HashMap<String,ClassPathXmlApplicationContext>();
    ClassPathXmlApplicationContext parentContext = null;

    /**
     * Creates a new instance of BocConfigPoolFactory
     *
     * @param springConfigName DOCUMENT ME!
     */
    public SpringPoolFactory(String springConfigName) {
        this(springConfigName,null);
    }


    /**
     * Create an instance of the spring pool with a base and parent context.  The parent context
     * is a singleton which is shared across multiple child contexts.
     * @param springConfigName
     * @param springParentConfigName
     */
    public SpringPoolFactory(String springConfigName, String springParentConfigName) {
        this.springConfigName = springConfigName;
        springConfigs = springConfigName.split(",");
        for (int ii = 0; ii<springConfigs.length;ii++) {
            springConfigs[ii] = springConfigs[ii].trim();
        }

        if (log.isInfoEnabled()) {
            log.info(String.format("SpringPoolFactory configured with base spring file: %s, and parent spring file %s", springConfigName, springParentConfigName));
        }
        if (springParentConfigName != null) {
            parentContext = parentSpringContainers.get(springParentConfigName);
            if (parentContext == null) {
                ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(springParentConfigName);
                parentSpringContainers.put(springParentConfigName, context);
            }
        }
    }

    @Override
    public Container create() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug(String.format("SpringPoolFactory.makeObject().  spring config Name: %s.", springConfigName));
        }
        try {
            ClassPathXmlApplicationContext ctx = null;
            if (parentContext == null) {
                ctx = new ClassPathXmlApplicationContext(springConfigs);
            } else {
                ctx = new ClassPathXmlApplicationContext(springConfigs);
                ctx.setParent(parentContext);
                ctx.refresh();
//                ctx = new ClassPathXmlApplicationContext(new String[] { springConfigName} ,parentContext);
            }
            Container container = (Container)ctx.getBean("com.fnf.jef.boc.Container");
            return container;
        } catch(Exception ex ) {
            String msg = String.format("SpringPoolFactory: Error initializing container for Spring configuration: %s",springConfigName);
            log.fatal(msg,ex);
            throw new Exception(msg, ex);
        }
    }

    /**
     * Wrap the provided instance with an implementation of
     * {@link PooledObject}.
     *
     * @param container the instance to wrap
     *
     * @return The provided instance, wrapped by a {@link PooledObject}
     */
    @Override
    public PooledObject<Container> wrap(Container container) {
        return new DefaultPooledObject<Container>(container);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws java.lang.Exception DOCUMENT ME!
     */
    public PooledObject<Container> makeObject() throws java.lang.Exception {
        return wrap(create());
    }

}
