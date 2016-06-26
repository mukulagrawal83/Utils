package com.fnf.xes.framework.ability;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.fnf.xes.framework.IdConstants;
import com.fnf.xes.framework.ServiceException;

public class JAXBContextFactory {

	private static JAXBContextFactory _instance = null;
	private static Object _initLock = new Object();
	private static Map _contexts = null;
	
	private static final Logger log = Logger.getLogger(JAXBContextFactory.class);

	/**
	 * Spring Singleton Constructor
	 */
	public JAXBContextFactory(Map contextNames) throws ServiceException {
		synchronized (_initLock) {
			if (_instance != null) {
            // alreadyinitalized - then it's ok!
                return;
				//throw already initialized
				//throw new ServiceException(IdConstants.JAXBCONTEXTFACTORY_ALREADY_INITIALIZED);
			} else {
				initialize(contextNames);
				_instance = this;
			}
		}
	}

	/*
	 * Singleton getter method.  It is assumed that some process during application
	 * startup has initialized this singleton factory.
	 */
	public static JAXBContextFactory getInstance() throws ServiceException {
		if (null == _instance) {
			throw new ServiceException(IdConstants.JAXBCONTEXTFACTORY_NOT_INITIALIZED);
		}
		return _instance;
	}
	
	/**
	 * This method should be called by the framework during
	 * application startup.
	 * For entry in the map a new JAXBContext is created with the classpath
	 * specified in the named property file.
	 * 
	 * Map should have the following content
	 * Context_Name=Classpath File Name
	 * 
	 * Classpath File Name should be a line seperated list of packages to load
	 * com.fnf.xes.bac.services.msgs.ifxext
	 * com.fnf.xes.bac.services.partyacctrel.partyacctrelinq.v1_0
	 * com.fnf.xes.bac.services.partyacctrel.partyacctrelinq.v2_0
	 * com.fnf.xes.mb.services.msgs.ifxext
	 * 
	 * @param contextNames
	 */
	private void initialize(Map contextNames) throws ServiceException {
		_contexts = new HashMap(contextNames.size());
		
		Iterator it = contextNames.keySet().iterator();
		String key = null;
		String value = null;
		while (it.hasNext()) {
			key = (String)it.next();
			value = (String)contextNames.get(key);
			createContext (key, value);
		}
	}
	
	/**
	 * Returns a configured JAXBContext by name.  If the named context
	 * has not been configured this method will return null.
	 * @param name String name of the context to get
	 * @return JAXBContext will return null if named context not configured
	 * @throws ServiceException 
	 * 		JAXBCONTEXTFACTORY_NOT_INITIALIZED if the factory has not been initialized yet.
	 * 		JAXBCONTEXTFACTORY_NO_CONTEXT if the context is not found by specified name.
	 */
	public JAXBContext getContext(String name) throws ServiceException {
		if (null == _contexts) {
			throw new ServiceException(IdConstants.JAXBCONTEXTFACTORY_NOT_INITIALIZED);
		}

		JAXBContext context = null;
		context = (JAXBContext)_contexts.get(name);
		if (null == context) {
			log.error(name+" context not found.");
			throw new ServiceException(IdConstants.JAXBCONTEXTFACTORY_NO_CONTEXT, name);
		}
		return context;
	}
	
	/**
	 * Creates a new JAXBContext object using the contents of the property file
	 * as the context classpath.
	 * It is expected that the property file is on the application classpath.
	 * @param name String name of the context
	 * @param propFileName String name of a property file that contains the
	 * context classpaths.  It is expected that this file is on the application
	 * classpath.
	 */
	private void createContext(String name, String propFileName) throws ServiceException {
    	String contextClasspath = null;
		//build classpath from data in property file
		InputStream is = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFileName);
		     BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		     StringBuffer buf = new StringBuffer();
		     String line = null;
		     while ((line = reader.readLine()) != null) {
		    	 //don't append empty lines or lines with comments
		    	 if ((line.trim().length() > 0) && (line.charAt(0) != '#')){
		    		 buf.append(line.trim());
		    		 buf.append(':');
		    	 }
		     }
		     contextClasspath = buf.toString();
		 } catch (Exception e) {
			 log.error("Failed to read context classpath file.", e);
			 throw new ServiceException(IdConstants.JAXBCONTEXTFACTORY_FAILED_ON_READING_CLASSPATH_FILE, propFileName, e);
		 } finally {
			 try {
				 is.close();
			 } catch(IOException ioe) {
				 log.warn("Error while closing input stream", ioe);
			 }
		 }
		 try {
			 JAXBContext context = JAXBContext.newInstance(contextClasspath, Thread.currentThread().getContextClassLoader());
			 _contexts.put(name, context);
			 if (log.isInfoEnabled()) {
				 log.info("Successfully created a context with classpath: "+contextClasspath);
			 }
		 } catch (JAXBException je) {
			 log.error("Could not create context. ", je);
			 throw new ServiceException(IdConstants.JAXBCONTEXTFACTORY_FAILED_ON_CREATING_CONTEXT, contextClasspath, je);
		 }
	}
}
