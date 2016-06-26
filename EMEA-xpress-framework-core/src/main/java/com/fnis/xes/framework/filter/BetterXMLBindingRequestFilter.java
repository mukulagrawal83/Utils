package com.fnis.xes.framework.filter;

import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.xes.framework.ServiceException;
import com.fnis.ifx.xbo.v1_1.Factory;
import com.fnis.ifx.xbo.v1_1.Marshal;
import com.fnis.ifx.xbo.v1_1.Payload;
import com.fnis.ifx.xbo.v1_1.UnMarshal;
import com.fnis.xes.framework.IdConstants;
import com.fnis.xes.framework.util.FastInfoSetUtility;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import java.util.Properties;

/**
 * This filter is responsible for marshalling XML request message and
 * unmarshalling response messages that are in String format or
 * org.w3c.dom.Node. The {@link #doFilter doFilter()} method's javadoc contains
 * more information about what this class does.
 * <p/>
 * One of the base filters for the GoTO Architecture
 *
 * @author Modified by GoTO Arch Team
 */
public class BetterXMLBindingRequestFilter implements RequestFilter {

    private enum MessageType {
        XML_RQ_MSG,
        JSON_RQ_MSG,
        FASTINFOSET_RQ_MSG,
        DOM_RQ_MSG;
    }


    /**
     * The literal string for the Context Class Path to put in the BOC config.
     */
    private static final String CONTEXT_CLASSPATH = "ContextClasspath";

    /**
     * The Default Context Classpath. If there is none set from the BOC
     * configuration, then use this one as the default.
     */
    private static final String DEFAULT_CONTEXT_CLASSPATH = "http://www.fnf.com/xes";

    /**
     * Indicates whether or not to validate against the schema.
     */
    private static final String VALIDATE_INPUT = "ValidateInput";

    /**
     * The default value for validation.
     */
    private static final String DEFAULT_VALIDATE_INPUT = "false";

    private boolean _validateInput;
    /**
     * Schema Location for validating input XML
     */
    private static final String SCHEMA_LOCATION = "SchemaLocation";

    private String _schemaLocation;

    private String _schemaPath;

    /**
     * Indicates whether or not to validate against the schema.
     */
    private static final String VALIDATE_OUTPUT = "ValidateOutput";

    /**
     * The default value for validation.
     */
    private static final String DEFAULT_VALIDATE_OUTPUT = "false";

    private boolean _validateOutput;

    /**
     * The XBO Context Classpath (i.e., which packages to search in the
     * CLASSPATH to find XBO classes to load).
     */
    private String _contextClasspath;

    private static final String CLASS_NAME = BetterXMLBindingRequestFilter.class
            .getName();

    private static Logger log = Logger.getLogger(CLASS_NAME);

    DocumentBuilderFactory dbFactory = null;

    DocumentBuilder docBuilder = null;

    SAXParser saxParser = null;

    Transformer _transformer;

    public static final String JSON_START_TAG = "<json>";
    public static final String JSON_END_TAG = "</json>";

    /**
     * Creates a new instance of XMLBindingRequestFilter
     */
    public BetterXMLBindingRequestFilter() {
    }

    /**
     * Filter processing starts here. This method is passed a BOC
     * RequestMessage, a ResponseMessage and a RequestLink (see below for more
     * details about each parameter). The RequestMessage contains the payload,
     * which is an XML document in one of two formats: String, or org.w3c.Node.
     * No other input XML formats are supported.
     * <p/>
     * <p/>
     * The document is <i><b>unmarshalled</b></i> into an object graph whose
     * constituent objects resemble their XML counterparts. This object graph is
     * passed to the next RequestFilter in the chain (by using the
     * <i>requestLink</i> parameter, which is a reference to the next filter in
     * the chain). Upon successful return from all subsequent filter processing,
     * the response (also an object graph) is <i><b>marshalled</b></i> back
     * into the same XML representation upon input, and returned to the caller.
     * <p/>
     * <p/>
     * Processing the incoming and outgoing documents in this location provides
     * a significant benefit: it saves the service (where the business logic
     * resides) the trouble of having to deal with XML at all, and makes the
     * service code reusable from any number of different channels.
     * <p/>
     * <p/>
     * Unmarshalling is the process of converting an XML document into a graph
     * of Java objects that resemble the document's structure, also known as its
     * schema. This is a convenient mechanism for dealing with XML documents, in
     * that once the document is unmarshalled into an object graph (whose state
     * mirrors that of the uncoming XML document instance), its contents are
     * processed as would any other Java object graph be, separating the concern
     * of the input format from its ultimate consumer, and facilitating optimal
     * reuse. The XBO Framework is used to perform the
     * unmarshalling and marshalling operations.
     * <p/>
     * <p/>
     * Marshalling is the opposite of unmarshalling, where a graph of objects is
     * converted into an XML document.
     * <p/>
     * <p/>
     * Three types of exceptions are caught and rethrown as ServiceException in
     * order to decorate the rethrown exception with an error code:
     * UnmarshalException, MarshalException and XBOException. All of these
     * exceptions are XBO exceptions, with the lattermost provided as a
     * "catch-all" for any unusually rare XBO exceptional conditions that may
     * occur.
     *
     * @param request     The request message. Contains the payload, which is the XML
     *                    document from the caller. Two formats are supported for this
     *                    incoming document: String and org.w3c.Node. No other formats
     *                    are supported.
     * @param response    The response message. Upon return from filter chain
     *                    processing, get the Object attribute (via the getObject()
     *                    getter) on the ResponseMessage object, marshal it, and set the
     *                    results of the marshal operation to the Object attribute (via
     *                    the setObject() setter).
     * @param requestLink The request link. Reference to the next filter in the chain.
     *                    The unmarshalled input document is passed to this filter as
     *                    part of the RequestMessage.
     * @throws ServiceException Thrown if:
     *                          <ul>
     *                          <li>Request payload is not a String or a DOM Node</li>
     *                          <li>XBO exceptions are thrown during the
     *                          marshalling/binding process.</li>
     *                          </ul>
     * @throws Throwable        Included for Interface compliance.
     */
    public void doFilter(RequestMessage request, ResponseMessage response,
                         RequestLink requestLink) throws Throwable {
        if (saxParser == null) {
            saxParser = SAXParserFactory.newInstance().newSAXParser();
        }

        if (log.isDebugEnabled())
            log.debug("Inside the doFilter of XMLBindingFilter");
        //
        // Grab the Request Message object and let's get started
        Object rqMessage = request.getObject();
        try {

            Payload rootVo = null;
            if (rqMessage instanceof String) {
                String xmlDoc = null;
                switch (getRqPayloadType((String) rqMessage)) {
                    case XML_RQ_MSG:

                        //validate input XML
                        rootVo = validateInput(rqMessage.toString());

                        if (log.isDebugEnabled())
                            log.debug("successfully unmarshalled in XMLBindingFilter");

                        //route the request to downstream filter chain
                        rootVo = (Payload) dispatchPayloadMsg((String) rqMessage, rootVo, request, response, requestLink);

                        //marshal the XML Response message
                        xmlDoc = Marshal.marshal(rootVo, false, false, "http://www.fnf.com/xes");

                        //validate output XML
                        rootVo = validateOutput(xmlDoc);


                        if (log.isDebugEnabled())
                            log.debug("successfully marshalled in XMLBindingFilter");

                        response.setObject(xmlDoc);
                        break;

                    case JSON_RQ_MSG:
                        //unmarshall the JSON Request Message
                        String jsonMsg = rqMessage.toString();
                        boolean bJSONElement = false;

                        if (jsonMsg.startsWith(JSON_START_TAG)) {
                            jsonMsg = jsonMsg.substring(JSON_START_TAG.length(), jsonMsg.length() - JSON_END_TAG.length() + 1);
                            bJSONElement = true;
                        }

                        rootVo = UnMarshal.unmarshal(jsonMsg);

                        if (log.isDebugEnabled())
                            log.debug("successfully unmarshaled in XMLBindingFilter");

                        //route the request to downstream filter chain
                        rootVo = (Payload) dispatchPayloadMsg(jsonMsg, rootVo, request, response, requestLink);

                        //marshal the XML Response message
                        xmlDoc = Marshal.marshal(rootVo, false, true, "http://www.fnf.com/xes");

                        if (log.isDebugEnabled())
                            log.debug("successfully marshalled in XMLBindingFilter");

                        if (bJSONElement)
                            xmlDoc = JSON_START_TAG + "<![CDATA[" + xmlDoc + "]]>" + JSON_END_TAG;

                        response.setObject(xmlDoc);
                        break;
                }
            } else if (rqMessage instanceof Payload) {
                rootVo = (Payload) dispatchPayloadMsg(rqMessage.toString(), (Payload) rqMessage, request, response, requestLink);
                response.setObject(rootVo);
            }
            // TODO: report this as a bug
            // e1003423: if service exception occurs after filter, it would be wrapped around Exception - even Service Exception
            // So IFXExceptionFilter would be fooled about the code returned...
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(IdConstants.ERR_BINDING_ERROR, e
                    .getMessage(), e);
        }
    }

    /**
     * Method to get the Schema path to enable schema validation
     *
     * @throws Exception
     */
    private String getSchemaPath() throws Exception {
        String schemaPath = null;
        //For Schema validation true
        if (_validateInput || _validateOutput) {

            if (_schemaLocation != null && !_schemaLocation.trim().equals("")) {
                //Schema validation path from Spring Hard Coded Schema location
                schemaPath = _schemaLocation;
            } else {
                log.warn("Warning message from XMLBindingRequestFilter " +
                        "Schema Location not specified in ifx2_spring xml for either validate input or output true condition");
                log.warn("Schema Validation Could not be performed");

                /*//Schema validation path from deployed Application Schema location - WebSphere server
                    try{
                        AdminService adminService = AdminServiceFactory.getAdminService();
                        ObjectName queryName = new ObjectName( "WebSphere:*,type=AdminOperations" );
                        Set objs = adminService.queryNames( queryName, null );
                        Object appPath = null;
                        Object cellPath = null;
                        if ( !objs.isEmpty() )
                        {
                            ObjectName thisObj = (ObjectName)objs.iterator().next();
                            String opName = "expandVariable";
                            String appSignature[] = { "java.lang.String" };
                            String appParams[] = { "${APP_INSTALL_ROOT}" } ;
                            appPath = adminService.invoke( thisObj, opName, appParams, appSignature );

                            String cellSignature[] = { "java.lang.String" };
                            String cellParams[] = { "${WAS_CELL_NAME}" } ;
                            cellPath = adminService.invoke( thisObj, opName, cellParams, cellSignature );
                        }
                        schemaPath = appPath.toString()+"/"+cellPath.toString()+"/xpress.ear/xesModelSvcs-dev.war/wsdl/schema";

                    }catch(Throwable e){
                        log.error("Exception occurred in XMLBindingRequestFilter " +
                                "while getting Schema Location from websphere server constants:: ERROR :: "+e.getMessage());
                        log.error("Schema Validation Could not be performed for the Schema path :: "+schemaPath);
                    }*/
            }
        }
        return schemaPath;
    }

    /**
     * Method to enable schema validation
     *
     * @throws Exception
     */
    private Payload validateInput(String rqMessage) throws Exception {
        Payload rootVo = null;
        //For Schema validation true
        if (_validateInput && _schemaPath != null) {
            //Schema Validation
            Factory.setSchemaLocation(_schemaPath);
            //validate and unmarshal the XML Request Message
            rootVo = UnMarshal.validateUnmarshal(rqMessage, _contextClasspath);
        } else {
            //unmarshal the XML Request Message
            rootVo = UnMarshal.unmarshal(rqMessage.toString());
        }
        return rootVo;
    }

    /**
     * Method to enable schema validation
     *
     * @throws Exception
     */
    private Payload validateOutput(String rsMessage) throws Exception {
        Payload rootVo = null;
        //For Schema validation true
        if (_validateOutput && _schemaPath != null) {
            //Schema Validation
            Factory.setSchemaLocation(_schemaPath);
            //validate and unmarshal the XML Response Message
            rootVo = UnMarshal.validateUnmarshal(rsMessage, _contextClasspath);
        }
        return rootVo;
    }


    /**
     * Initializes the filter with properties specified in the BOC config XML.
     * <p/>
     * The following properties are supported:
     * <p/>
     *
     * @param configProperties Properties specified in the BOC config XML.
     *                         <p/>
     *                         ContextClasspath - colon-separated list of Java package names that
     *                         XBOContext will use to search for classes. Example:
     *                         com.fnis.xes.services.msgs.ifx
     *                         formatted.output - whether or not to "pretty print" the output to make it
     *                         human-readable (true or false). ValidateInput - whether to validate on
     *                         unmarshalling (true or false) ValidateOuput - whether to validate on
     *                         marshalling (true or false)
     * @throws BocException Included for Interface compliance
     */
    public void initializeFilter(Properties configProperties)
            throws BocException {
        try {
            String contextClassPath = configProperties.getProperty(
                    CONTEXT_CLASSPATH, DEFAULT_CONTEXT_CLASSPATH);
            _contextClasspath = contextClassPath;
            String validateInput = configProperties.getProperty(VALIDATE_INPUT,
                    DEFAULT_VALIDATE_INPUT);
            _validateInput = (validateInput.equalsIgnoreCase("true")) ? true
                    : false;
            String validateOutput = configProperties.getProperty(
                    VALIDATE_OUTPUT, DEFAULT_VALIDATE_OUTPUT);
            _validateOutput = (validateOutput.equalsIgnoreCase("true")) ? true
                    : false;

            //Schema Location
            _schemaLocation = configProperties.getProperty(SCHEMA_LOCATION);

            // Schema path to enable schema validation
            _schemaPath = getSchemaPath();

            // Additional log output to test Default Logger
            if (log.isDebugEnabled()) {
                log.debug("ContextClasspath = \'" + _contextClasspath + "\'");
                log.debug("validateInput = " + _validateInput);
                log.debug("validateOutput = " + _validateOutput);
            }

        } catch (Exception e) {
            String msg = "initializeFilter(): ERROR: ";
            if (log.isEnabledFor(Level.ERROR))
                log.error(msg, e);
            throw new BocException(msg, e);
        }
    }

    /**
     * Determines the Payload type of the client request message
     *
     * @param rqMsg - Incoming Request Message
     * @return int - Returns Request Message type
     * @throws ServiceException - If unsupported request format is sent
     *                          by the client application
     */
    public static MessageType getRqPayloadType(Object rqMsg) throws ServiceException {
        if (rqMsg instanceof String) {
            boolean isFastInfosetDocument = FastInfoSetUtility.isFastInfosetDocument((String) rqMsg);
            if (!isFastInfosetDocument) {
                String msg = (String) rqMsg;
                if (msg.startsWith("{") || msg.startsWith(JSON_START_TAG))
                    return MessageType.JSON_RQ_MSG;
                else
                    return MessageType.XML_RQ_MSG;
            } else
                return MessageType.FASTINFOSET_RQ_MSG;
        } else if (Node.class.isAssignableFrom(rqMsg.getClass())) {
            return MessageType.DOM_RQ_MSG;
        } else {
            // Unsupported incoming format. Throw a service exception.
            throw new ServiceException(IdConstants.ERR_UNSUPPORTED_FORMAT);
        }
    }

    /**
     * Dispatches PayLoad message to the downstream filter chain for further processing
     *
     * @param rqMessage   - RequestMessage
     * @param rootVo      - Payload Object
     * @param request     - RequestMessage Object
     * @param response    - ResponseMessage Object
     * @param requestLink - RequestLink containing list of filters
     * @return Response Object processed by the service
     * @throws Throwable - Error processing the client request message
     */
    private Object dispatchPayloadMsg(String rqMessage, Payload rootVo, RequestMessage request, ResponseMessage response,
                                      RequestLink requestLink) throws Throwable {
        // Throw an exception if parser was unable to parse the request message
        if (rootVo == null) {
            if (log.isDebugEnabled())
                log.debug("Error Parsing request Message: " + rqMessage);
            throw new ServiceException(IdConstants.ERR_MARSHAL_ERROR);
        }
        // store the original String message
        request.setProperty(IdConstants.ORIG_MESSAGE, rqMessage);


        // Set the payload as the root object in the graph
        request.setObject(rootVo);

        // Now pass the object graph to the next link in the chain
        requestLink.doFilter(request, response);

        // Grab the object graph from the response message and marshal it
        // back into a String
        return response.getObject();
    }
}