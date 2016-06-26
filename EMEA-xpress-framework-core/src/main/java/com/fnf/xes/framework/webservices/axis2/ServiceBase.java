package com.fnf.xes.framework.webservices.axis2;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

import com.fnf.jef.boc.Container;
import com.fnf.jef.boc.Dispatcher;
import com.fnf.jef.boc.MessageDispatcher;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.xes.framework.management.monitoring.EndpointMetric;
import com.fnf.xes.framework.management.monitoring.EventManager;
import com.fnf.xes.framework.management.monitoring.MetricsPublisher;
import com.fnis.xes.framework.spring.XpressSpringFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axiom.soap.SOAPHeaderBlock;

public class ServiceBase {

	private static Logger log = Logger.getLogger(ServiceBase.class);

	protected GenericObjectPool bocConfigPool;

	static Properties jefConfig = new Properties();
	static String MAXMAX_POOL_SIZE_ENV = "bocPoolMaxActive";
	static String MIN_POOL_SIZE_ENV = "initalPoolSize";
	protected static String BOC_NAME = "ifx2-spring";
	private static Map map = Collections.synchronizedMap(new HashMap());
	
	private static String XES_MSG_ENDPOINT_PROPERTY = "XES_MSG_ENDPOINT";
	private static final String SLASH = "/";
	private static final String NDC_NS = "http://www.fis.com/NDC";
	private static final String NDC_ELE = "NDC";
	
	public ServiceBase(String bocName) throws Exception {
		bocConfigPool = XpressSpringFactory.getContextPool(bocName);
	}

	/**
	 * Processes the IFX and ESL document. The IFX or ESL document is extracted
	 * from SOAP request and the BOC container is invoked. The IFX or ESL
	 * response document is packaged within the SOAP body of the response
	 * message.
	 * 
	 * @param req
	 *            a <code>SOAPEnvelope</code> specifying the incoming SOAP
	 *            request
	 * @param resp
	 *            a <code>SOAPEnvelope</code> specifying the outgoing SOAP
	 *            response
	 */
	protected OMElement execute(String destination, OMElement param)
			throws Exception {
		String ifxResp = "", ifxReq = "";
		OMElement respOMElement = null;
        		MessageContext mc = MessageContext.getCurrentMessageContext();
		// convert IFX OMElement to String
		EndpointMetric endpointStats = new EndpointMetric("Axis");
		// Create and start the performance timers.
        endpointStats.startTime();
		
		try {
		
		 NDC.push(getNDC(mc));
            
            if (mc != null) {
            	
            	//Setting Endpoint spec property such as http:services:IFXServer2
            	
            	String endpointSpec = mc.getIncomingTransportName() + ":" 
            		+ mc.getConfigurationContext().getServicePath() + ":" 
            		+ mc.getAxisService().getName();
            	
            	if (log.isDebugEnabled()){ 
        			log.debug(ServiceBase.class.getName() + " - Setting XES_MSG_ENDPOINT property to " + endpointSpec);	
        		}
                mc.setProperty(XES_MSG_ENDPOINT_PROPERTY, endpointSpec);
            }
            
			ifxReq = param.toString();
			endpointStats.setRequestSize(ifxReq.length());
			
			ResponseMessage rs = callBocContainer(destination, ifxReq);
			ifxResp = (String) rs.getObject();
			
			endpointStats.setResponseSize(ifxResp.length());

			// convert IFX response String to OMElement
			OMFactory factory = org.apache.axiom.om.OMAbstractFactory
					.getOMFactory();
			ByteArrayInputStream input = new ByteArrayInputStream(
					ifxResp.getBytes("UTF8"));
			XMLStreamReader xmlreader = StAXUtils.createXMLStreamReader(input);
			StAXOMBuilder builder = new StAXOMBuilder(factory, xmlreader);

			respOMElement = builder.getDocumentElement();
			((org.apache.axiom.om.impl.OMNodeEx) respOMElement).setParent(null);

		} catch (Throwable e) {
			if (log.isEnabledFor(Level.ERROR))
				log.error("ERROR: " + e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}finally {
		    handleEndPointStats(endpointStats);
			if(mc != null)
				mc.removeProperty(XES_MSG_ENDPOINT_PROPERTY);
			NDC.remove();	// Needed to avoid memory leaks
           
        }

		// for java binding, below line is required, otherwise, it will throw
		// exception
		// from StAXBuilder.getPaser()
		// String s = respOMElement.toString();
		return respOMElement;
	}

	protected OMElement execute(OMElement param) throws Exception {

		return execute(null, param);
	}

	/**
	 * Invoke the XES BOC (Business Object Container)
	 * 
	 * @param ifxRq
	 *            The IFX request Document
	 * @return ifxRes The IFX response document
	 */
	protected ResponseMessage callBocContainer(Object ifxReq) throws Exception {
		return callBocContainer(null, ifxReq);
	}

	protected ResponseMessage callBocContainer(String destination, Object ifxReq)
			throws Exception {

		Container container = null;
		try {
			container = (Container) bocConfigPool.borrowObject();
			RequestMessage req = new RequestMessage();
			req.setObject(ifxReq);
			// get the dispatcher from the container

			ResponseMessage rs;
			if (destination == null) {
				MessageDispatcher md = (MessageDispatcher) container
						.getObjectEx(MessageDispatcher.class);
				rs = md.dispatch(req);
			} else {
				Dispatcher md = (Dispatcher) container
						.getObjectEx("com.fnf.jef.boc.Dispatcher");
				rs = md.dispatch(destination, req);
			}
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			if (log.isEnabledFor(Level.ERROR))
				log.error("Following execption occured in Boc execution: "
						+ e.getMessage());
			throw e;
		} finally {
			if (container != null)
				bocConfigPool.returnObject(container);
		}
	}

	public static Properties getBocConfig() {
		return jefConfig;
	}
	public static synchronized GenericObjectPool ini(String bocName) throws Exception
    {
		return XpressSpringFactory.getContextPool(bocName);
    }
	
	private void handleEndPointStats(EndpointMetric endpointStats) {
        try {
            endpointStats.stopTime();
            Boolean success = (Boolean) MetricsPublisher.getInstance().getProperty(MetricsPublisher.SUCCESSFUL);
            if (Boolean.TRUE.equals(success)) {
                endpointStats.setSuccess(true);
            } else {
                endpointStats.setSuccess(false);
            }

            EventManager em = EventManager.getInstance();
            em.processEvents();
        } catch (Throwable t) {
            log.error("Following execption occured in hanleEndPointStats:" + t.getMessage(),t);
        }

    }
    
  private String getNDC(MessageContext mc)
    {
    	String service = null;
    	String uid = null;

    	if (mc != null)
    	{
    		if (mc.getAxisService() != null)
    			service = mc.getAxisService().getName();

    		SOAPEnvelope envelope = mc.getEnvelope();
    		
    		if (envelope != null)
    		{
        		SOAPHeader header = envelope.getHeader();
        		
        		if (header != null)
        		{
            		java.util.List<?> list = header.getHeaderBlocksWithNSURI(NDC_NS);
            		if(list!=null)
            		{
	            		for(Object o : list)
	            		{
	            			SOAPHeaderBlock block = (SOAPHeaderBlock) o;
	
	            			if (block.getQName().getLocalPart().equalsIgnoreCase(NDC_ELE))
	            			{
	                			uid = block.getText();
	            			}
	            		}
            		}
        		}
    		}
    	}

    	// if client does not provide an ID then generate one.
        if (uid == null) {
            uid = com.fnf.jef.util.GuidGen.getGuid();
        }

        if (service != null)
    		return service + SLASH + uid;

   		return uid;
    }
}