package com.fnis.xes.framework.filter;

import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.framework.util.ErrWarnInfoMessage;
import com.fnf.xes.framework.util.OutputTypeConvertor;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;


public class BetterIFXExceptionFilter2 implements RequestFilter {
    private static final String CLASS_NAME = BetterIFXExceptionFilter2.class.getName();
    private static Logger log = Logger.getLogger(CLASS_NAME);
    private static final String XML_Oper_Or_Msg = "IFX";
    private static final String DEFAULT_XML_START = "<" + XML_Oper_Or_Msg
            + " xmlns=\"http://www.ifxforum.org/IFX_150\"" + ">";
    private static final String DEFAULT_XML_END = "</" + XML_Oper_Or_Msg + ">";
    private static final String STATUS_START = "<Status>";
    private static final String STATUS_END = "</Status>";
    private static final String STATUS_CODE_START = "<StatusCode>";
    private static final String STATUS_CODE_END = "</StatusCode>";
    private static final String SERVER_STATUS_CODE_START = "<ServerStatusCode>";
    private static final String SERVER_STATUS_CODE_END = "</ServerStatusCode>";
    private static final String STATUS_SEVERITY = "<Severity>Error</Severity>";
    private static final String STATUS_SEVERITY_INFO = "<Severity>Info</Severity>";
    private static final String STATUS_DESC_START = "<StatusDesc>";
    private static final String STATUS_DESC_END = "</StatusDesc>";
    private static final String RQUID_START = "<RqUID>";
    private static final String RQUID_END = "</RqUID>";
    private static final String ADDITIONAL_STATUS_TEMPLATE = "            <AdditionalStatus xmlns=\"http://www.fnf.com/xes\">\n" +
            "               <StatusCode xmlns=\"urn:ifxforum-org:XSD:1\">0</StatusCode>\n" +
            "               <ServerStatusCode xmlns=\"urn:ifxforum-org:XSD:1\">0</ServerStatusCode>\n" +
            "               <Severity xmlns=\"urn:ifxforum-org:XSD:1\">Info</Severity>\n" +
            "               <StatusDesc xmlns=\"urn:ifxforum-org:XSD:1\">TRANSACTION PROCESSING COMPLETE.</StatusDesc>\n" +
            "            </AdditionalStatus>\n" +
            "            <AdditionalStatus xmlns=\"http://www.fnf.com/xes\">\n" +
            "               <StatusCode xmlns=\"urn:ifxforum-org:XSD:1\">0</StatusCode>\n" +
            "               <ServerStatusCode xmlns=\"urn:ifxforum-org:XSD:1\">1120</ServerStatusCode>\n" +
            "               <Severity xmlns=\"urn:ifxforum-org:XSD:1\">Info</Severity>\n" +
            "               <StatusDesc xmlns=\"urn:ifxforum-org:XSD:1\">Success</StatusDesc>\n" +
            "            </AdditionalStatus>\n" +
            "            <AdditionalStatus xmlns=\"http://www.fnf.com/xes\">\n" +
            "               <StatusCode xmlns=\"urn:ifxforum-org:XSD:1\">-1000</StatusCode>\n" +
            "               <ServerStatusCode xmlns=\"urn:ifxforum-org:XSD:1\">1120</ServerStatusCode>\n" +
            "               <Severity xmlns=\"urn:ifxforum-org:XSD:1\">Info</Severity>\n" +
            "               <StatusDesc xmlns=\"urn:ifxforum-org:XSD:1\">No Records Found</StatusDesc>\n" +
            "            </AdditionalStatus>\n";
    private static final String DEFAULT_RQUID = "00000000-0000-0000-0000-000000000000";
    private static final int GENERAL_ERROR_CODE = 100;
    private static final int STATUS_DESC_LENGTH = 255;
    private String m_sXMLResponse;
    private ErrWarnInfoMessage m_oFormatMessage;
    //option 0 == String, 1 = dom, 2 == JAXB1
    private int messageType = 0;
    private static int DOM_TYPE = 1;
    private static int JAXB1_TYPE = 2;
    private static int STRING_TYPE = 0;
    private DocumentBuilder builder;
    private static final String JAXB_CONTEXT = "JAXBContext";

    public BetterIFXExceptionFilter2(ErrWarnInfoMessage formatmessage) {
        m_oFormatMessage = formatmessage;
    }

    /**
     * doFilter
     *
     * @param requestMessage
     * <code>com.fnf.jef.boc.RequestMessage</code>
     * @param responseMessage
     * <code>com.fnf.jef.boc.ResponseMessage</code>
     *
     * @param requestLink
     * <code>com.fnf.jef.boc.filter.RequestLink</code>
     *
     *
     * @return void
     *
     */
    public void doFilter(RequestMessage requestMessage,
                         ResponseMessage responseMessage, RequestLink requestLink)
            throws java.lang.Throwable {

        m_sXMLResponse = null;
        Object req = null;
        try {
            req = requestMessage.getObject();
            if (Node.class.isAssignableFrom(req.getClass())) {
                messageType = DOM_TYPE;
            } else if (req instanceof com.sun.xml.bind.JAXBObject) {
                messageType = JAXB1_TYPE;
            } else {
                messageType = STRING_TYPE;
            }

            requestLink.doFilter(requestMessage, responseMessage);
        } catch (ServiceException se) {
            log.debug("Service Exception occured");
			se.printStackTrace();
            createResponse(se.getErrorCode(),
                    m_oFormatMessage.format(
                            se.getErrorCode(),
                            se.getMessage()),(String)req);

        } catch (java.lang.Throwable e) {
            createResponse(GENERAL_ERROR_CODE, m_oFormatMessage.format(
                    GENERAL_ERROR_CODE,
                    "Unknown error"), (String) req);
            logme(GENERAL_ERROR_CODE, e);

        } finally {
            if (m_sXMLResponse != null) {
                try {
                    Object o = convertResponse(m_sXMLResponse);
                    responseMessage.setObject(o);
                } catch (Exception e) {
                    responseMessage.setObject(m_sXMLResponse);
                }
            }
        }
    }

    /**
     * Initialize method
     *
     * @param properties
     * @throws BocException
     */
    public void initializeFilter(java.util.Properties properties)
            throws com.fnf.jef.boc.BocException {
    }

    /**
     * Wrapper to create the IFX 2.0 error response, wraps the server error code
     *
     * @param errorCode
     * <code>String</code>
     *
     * @param errorDesc
     * <code>String</code>
     *
     * @param request
     * <code>Object</code>
     *
     * @param request
     * <code>String</code>
     *
     *
     * @return String
     *
     */
    private void createResponse(int errorCode, String errorDesc, Object request) {
        createResponse(errorCode, 0, errorDesc, request);
    }

    /**
     * Create the IFX error response, needs the message information from the
     * request
     *
     * @param errorCode
     * <code>String</code>
     *
     * @param errorDesc
     * <code>String</code>
     *
     * @param request
     * <code>Object</code>
     *
     * @return String
     *
     */
    private void createResponse(int errorCode, int serverErrorCode,
                                String errorDesc, Object oReq) {

        StringBuffer status = new StringBuffer();
        StringBuffer rqUId = new StringBuffer();

        try {
            String request = "";
            if (oReq instanceof String) {
                request = (String) oReq;
            } else if (oReq instanceof com.sun.xml.bind.JAXBObject) {
                request = OutputTypeConvertor.jaxbToString(oReq);
            } else if (Node.class.isAssignableFrom(oReq.getClass())) {
                request = OutputTypeConvertor.dom2Xml((Node) oReq);
            }

            OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();
            ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes());

            XMLStreamReader xmlreader = StAXUtils.createXMLStreamReader(input);
            StAXOMBuilder builder = new StAXOMBuilder(factory, xmlreader);
            OMElement reqOMElement = builder.getDocumentElement();
            String namespace = reqOMElement.getNamespace().getName();
            String m_strServiceName = reqOMElement.getLocalName();
            if (!m_strServiceName.endsWith("Rq") && !m_strServiceName.endsWith("IFX")) {
                throw new Exception("Invalid message:" + m_strServiceName);
            }
            if(m_strServiceName.endsWith("Rq")){
                m_strServiceName = m_strServiceName.substring(0, m_strServiceName.length() - 2) + "Rs";
            } else if (m_strServiceName.endsWith("IFX")){
                m_strServiceName = "IFX";
            }

            String namespaceUrl = "http://www.ifxforum.org/IFX_150";
            String XPathString = "//ns:RqUID";

            AXIOMXPath XPath = new AXIOMXPath(XPathString);
            XPath.addNamespace("ns", namespaceUrl);
            OMElement node = (OMElement) XPath.selectSingleNode(reqOMElement);
            String uid = node.getText();

            StringBuffer start = new StringBuffer();
            start.append("<ns:").append(m_strServiceName).append(" ").append("xmlns:ns=\"").append(namespace).append("\" ").append("xmlns=\"").append(namespaceUrl).append("\">");

            StringBuffer end = new StringBuffer();
            end.append("</ns:").append(m_strServiceName).append(">");

            String severityText = STATUS_SEVERITY;

            if(errorCode == 1120 || errorCode == 2300){
                status.append(STATUS_START + STATUS_CODE_START).append("0").append(STATUS_CODE_END);
                severityText = STATUS_SEVERITY_INFO;
            } else {
                status.append(STATUS_START + STATUS_CODE_START).append(errorCode).append(STATUS_CODE_END);
            }

            if (0 != serverErrorCode) {
                status.append(SERVER_STATUS_CODE_START + serverErrorCode
                        + SERVER_STATUS_CODE_END);
            }

            if (errorDesc.length() >= STATUS_DESC_LENGTH) {
                status.append(severityText + STATUS_DESC_START + errorDesc.substring(0, STATUS_DESC_LENGTH - 1)
                        + STATUS_DESC_END + STATUS_END);
            } else {
                status.append(severityText + STATUS_DESC_START + errorDesc
                        + STATUS_DESC_END + STATUS_END);
            }

            // Additional Status
            if (errorCode == 1120 || errorCode == 2300){
                status.append(ADDITIONAL_STATUS_TEMPLATE);
            }

            rqUId.append(RQUID_START);
            if (node != null) {
                rqUId.append(node.getText());
            } else {
                rqUId.append(this.DEFAULT_RQUID); // default RqId
            }
            rqUId.append(RQUID_END);

            m_sXMLResponse = start.toString() + status.toString()
                    + rqUId.toString() + end.toString();

        } catch (Exception e) {
            m_sXMLResponse = DEFAULT_XML_START + status.toString()
                    + rqUId.toString() + DEFAULT_XML_END;
            logme(GENERAL_ERROR_CODE, e);
        }

    }

    private void logme(int key, Throwable t) {
        logme(Integer.toString(key), null, t);
    }

    private void logme(int key, String msg, Throwable t) {
        logme(key, new String[]{msg}, t);
    }

    private void logme(int key, String[] msg, Throwable t) {
        logme(Integer.toString(key), msg, t);
    }

    private void logme(String key, String[] msg, Throwable t) {
        if ((null == msg) || (msg.length == 0)) {
            log.l7dlog(Priority.ERROR, key, t);
        } else {
            log.l7dlog(Priority.ERROR, key, msg, t);
        }
    }

    private Object convertResponse(String m_sXMLResponse) throws Exception {
        if (messageType == STRING_TYPE) {
            return m_sXMLResponse;
        } else if (messageType == DOM_TYPE) {
            return OutputTypeConvertor.xmlToDom(m_sXMLResponse);
        } else if (messageType == JAXB1_TYPE) {
            return OutputTypeConvertor.stringToJaxb(m_sXMLResponse);
        }
        return m_sXMLResponse;
    }
}
