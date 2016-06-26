package com.fnf.xes.framework.pa;

import com.fnf.jef.boc.Configuration;
import com.fnf.jef.boc.Container;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;

/**
 *
 * @author lc21878
 */
public class BocConfigPoolFactory extends BasePooledObjectFactory<Container> {
    private static Logger log = Logger.getLogger(BocConfigPoolFactory.class);
    private String bocConfigName;
    private static HashMap map = new HashMap();
    ClassPathXmlApplicationContext parent = null;

    /**
     * Creates a new instance of BocConfigPoolFactory
     *
     * @param bocConfigName DOCUMENT ME!
     */
    public BocConfigPoolFactory(String bocConfigName) {
        this.bocConfigName = bocConfigName;
    }


    public BocConfigPoolFactory(String bocConfigName, String basicBocConfigName) {
        this.bocConfigName = bocConfigName;
        if (basicBocConfigName == null) return;

        if (map.get(basicBocConfigName) == null){
            ClassPathXmlApplicationContext basicContext = new ClassPathXmlApplicationContext(basicBocConfigName);
            map.put(basicBocConfigName, basicContext);
            parent = basicContext;
        }
        else parent = (ClassPathXmlApplicationContext)map.get(basicBocConfigName);
    }

    @Override
    public Container create() throws Exception {
        if ( log.isInfoEnabled() )
            log.info("BocConfigPoolFactory.makeObje()");
        Container container = Configuration.buildContainer(bocConfigName, parent);
        return container;
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
