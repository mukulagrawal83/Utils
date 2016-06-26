/*
 * XMLBindingRequestFilter.java
 *
 * Created on October 8, 2004, 10:17 AM
 */

package com.fnf.xes.framework.filter;

//JDK Imports
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.Validator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.sax.SAXResult;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.xes.framework.IdConstants;

import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.framework.ability.JAXBContextFactory;
import com.fnf.xes.framework.ability.XMLBindingContentHandlerAbility;
import com.fnf.xes.framework.logging.ManagedLogAbility;
import com.fnf.xes.framework.util.FastInfoSetUtility;
import com.sun.xml.fastinfoset.sax.SAXDocumentSerializer;

/**
* This filter is responsible for marshalling XML request message and unmarshalling response
* messages that are in String format or org.w3c.dom.Node.  The {@link #doFilter doFilter()} method's javadoc
* contains more information about what this class does.
*
* @author  Steve Perry
*/
public class XMLBindingRequestFilter implements RequestFilter {

	private static final String NAMESPACE_OVERRIDE_PROPERTY = "NamespacePrefixOverrideFile";
	private static final String NAMESPACE_OVERRIDE_PROPERTY_DEFAULT = null;
	
	/**
	 * The name of the context to lookup using JAXBContextFactory.
	 */
	private static final String CONTEXT_NAME = "ContextName";

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
     * Indicates whether or not to validate against the schema.
     */
    private static final String VALIDATE_OUTPUT = "ValidateOutput";
    /**
     * The default value for validation.
     */
    private static final String DEFAULT_VALIDATE_OUTPUT = "false";
    private boolean _validateOutput;

    
    private static final String CLASS_NAME = XMLBindingRequestFilter.class.getName();
    private static Logger log = Logger.getLogger(CLASS_NAME);
    
    /**
     * Allows access to a manged Logger (optional).
     */
    private ManagedLogAbility _managedLogAbility;
    /**
     * Indicate to JAXB that the output should be formatted or unformatted.
     */
    private Boolean _formattedOutput = new Boolean(true);
    /**
     * Used by JAXB to marshal the response object.
     */
    private XMLBindingContentHandlerAbility _contentHandler = null;
    

    DocumentBuilderFactory dbFactory = null;
    DocumentBuilder docBuilder = null;
    JAXBContext jaxb = null;
    private JAXBNamespacePrefixMapper jaxbNamespacePrefixMapper = null;
    
    
    Unmarshaller u = null;
    Marshaller m = null;
    
    /**
     * Creates a new instance of XMLBindingRequestFilter
     */
    public XMLBindingRequestFilter() {
    }

    public XMLBindingRequestFilter(XMLBindingContentHandlerAbility contentHandler) {
    	_contentHandler = contentHandler;
    }

    /**
     * Creates a new instance of XMLBindingRequestFilter
     */
    public XMLBindingRequestFilter(ManagedLogAbility managedLogAbility) {
        this();
        _managedLogAbility = managedLogAbility;
    }
    
    public XMLBindingRequestFilter(ManagedLogAbility managedLogAbility, XMLBindingContentHandlerAbility contentHandler) {
        this(contentHandler);
        _managedLogAbility = managedLogAbility;
    }

    /**
     * Filter processing starts here.  This method is passed a BOC RequestMessage,
     * a ResponseMessage and a RequestLink (see below for more details about each
     * parameter).  The RequestMessage contains the payload, which is an XML document
     * in one of two formats: String, or org.w3c.Node.  No other input XML formats
     * are supported.<p>
     *
     * The document is <i><b>unmarshalled</b></i> into an object graph whose constituent
     * objects resemble their XML counterparts.  This object graph is passed to the next
     * RequestFilter in the chain (by using the <i>requestLink</i> parameter, which is
     * a reference to the next filter in the chain).  Upon successful return from all subsequent
     * filter processing, the response (also an object graph) is <i><b>marshalled</b></i>
     * back into the same XML representation upon input, and returned to the caller.<p>
     *
     * Processing the incoming and outgoing documents in this location provides a
     * significant benefit:
     * it saves the service (where the business logic resides) the trouble of having
     * to deal with XML at all, and makes the service code reusable from any number
     * of different channels.<p>
     *
     * Unmarshalling is the process of converting an XML document into a graph of Java
     * objects that resemble the document's structure, also known as its schema.  This
     * is a convenient mechanism for dealing with XML documents, in that once the document
     * is unmarshalled into an object graph (whose state mirrors that of the uncoming
     * XML document instance), its contents are processed as would any other Java object
     * graph be, separating the concern of the input format from its ultimate consumer,
     * and facilitating optimal reuse.
     * The Java API for XML Binding (JAXB) is used to perform the unmarshalling and
     * marshalling operations.<p>
     *
     * Marshalling is the opposite of unmarshalling, where a graph of objects is
     * converted into an XML document.<p>
     *
     * Three types of exceptions are caught and rethrown as ServiceException in order
     * to decorate the rethrown exception with an error code: UnmarshalException,
     * MarshalException and JAXBException.  All of these exceptions are JAXB exceptions,
     * with the lattermost provided as a "catch-all" for any unusually rare JAXB
     * exceptional conditions that may occur.
     *
     * @param request The request message.  Contains the payload, which is the
     * 		XML document from the caller. Two formats are supported for this
     * 		incoming document: String and org.w3c.Node.  No other formats are
     * 		supported.
     * @param response The response message.  Upon return from filter chain processing,
     * 		get the Object attribute (via the getObject() getter) on the ResponseMessage
     * 		object, marshal it, and set the results of the marshal operation to the
     * 		Object attribute (via the setObject() setter).
     * @param requestLink The request link.  Reference to the next filter in the chain.
     * 		The unmarshalled input document is passed to this filter as part of the
     * 		RequestMessage.
     * @throws ServiceException Thrown if:
     * <ul>
     * <li>Request payload is not a String or a DOM Node</li>
     * <li>JAXB exceptions are thrown during the marshalling/binding process.</li>
     * </ul>
     * @throws Throwable Included for Interface compliance.
     */
    public void doFilter(RequestMessage request,
                      ResponseMessage response,
                      RequestLink requestLink)  throws Throwable {
        //
        // JSP 01-09-2006 Use ManagedLogAbility (if provided) to obtain
        /// the correct Logger.
        //
        if (_managedLogAbility != null) {
            log = _managedLogAbility.getLogger(XMLBindingRequestFilter.class);
            if ( log.isDebugEnabled() )
            	log.debug("doFilter(): Obtained Logger from ManagedLogAbility.");
        }
   		request.setProperty("JAXBContext", jaxb);
        //
        // Grab the payload object and let's get started
        //
        Object payload = request.getObject();
        try {
            if (payload instanceof String) {
                //
                // Use JAXB Unmarshaller to unmarshal the String
                /// into an object graph.
                //
       	     	boolean isFastInfosetDocument = FastInfoSetUtility.isFastInfosetDocument((String)payload);
       	     	
       	     	Object rootVo=null;
       	     	
       	     	if (!isFastInfosetDocument) {
       	     		StringReader r = new StringReader((String)payload);
       	     		javax.xml.transform.Source s = new javax.xml.transform.stream.StreamSource(r);
       	     		rootVo = u.unmarshal(s);
       	     	}
       	     	else{
       	     		UnmarshallerHandler handler = u.getUnmarshallerHandler();
	              FastInfoSetUtility.xmlFastHandler(handler, (String)payload);       	     		
                rootVo = handler.getResult();

             	}
       	     	
                if (_validateInput) { // jsp 12-05-2005, QA Incident #948
                    if ( log.isDebugEnabled() )
                    	log.debug("Input validation is ON. Creating a JAXB Validator...");
                    Validator validator = jaxb.createValidator();
                    validator.validateRoot(rootVo);
                }
                //
                // Set the payload as the root object in the graph
                //
                request.setObject(rootVo);

                 //
                 // Now pass the object graph to the next link in the chain
                 //
                 requestLink.doFilter(request, response);
                 //
                 // Grab the object graph from the response message and marshal it
                 /// back into a String
                 //
                 Object newPayload = response.getObject();
                 
                 
                 if (_validateOutput) { // jsp 12-05-2005 QA Incident #948
                     Validator validator = jaxb.createValidator();
                     validator.validateRoot(newPayload);
                 }
                 
                 String xmlDoc = "";
                 if (!isFastInfosetDocument)
                 {
                 	StringWriter w = new StringWriter();
                 	m.setProperty("jaxb.formatted.output", _formattedOutput);
	                 	if (_contentHandler == null)
	                     	m.marshal(newPayload, w);
	                  else
	                     	m.marshal(newPayload, _contentHandler.getContentHandler(w));
                 	xmlDoc = w.toString();
                 }
                 else{
                 	ByteArrayOutputStream output = new ByteArrayOutputStream(); 
                 	SAXResult _result = new SAXResult();
                  SAXDocumentSerializer serializer = FastInfoSetUtility.documentSerializer();
                 	serializer.setOutputStream(output);
                 	_result.setHandler(serializer);
                    _result.setLexicalHandler(serializer); 
                 	m.marshal(newPayload, _result);
             	    xmlDoc = FastInfoSetUtility.byteToString(output.toByteArray());
             	    output.close();
                 }
                 // Now set the new String as the payload on the response
                 response.setObject(xmlDoc);
            } else if (Node.class.isAssignableFrom(payload.getClass())) {
                if ( log.isDebugEnabled() )
                	log.debug("Inside the doFilter of XMLBindingFilter");
                Node payloadNode = (Node)payload;
                //
                // Use JAXB's Unmarshaller to unmarshal the String
                /// into an object graph.
                //
                Object rootVo = u.unmarshal(payloadNode);
                if (_validateInput) { // jsp 12-05-2005, QA Incident #948
                    if ( log.isDebugEnabled() )
                    	log.debug("Input validation is ON. Creating a JAXB Validator...");
                    Validator validator = jaxb.createValidator();
                    validator.validateRoot(rootVo);
                }
                if ( log.isDebugEnabled() )
                	log.debug("successfully unmarshalled in XMLBindingFilter");
                //
                // Set the payload as the root object in the graph
                //
                request.setObject(rootVo);
                //
                // Now pass the object graph to the next link in the chain
                //
                requestLink.doFilter(request, response);
                if ( log.isDebugEnabled() )
                	log.debug("completed processing successfully in XMLBindingFilter");
                //
                // Grab the object graph from the response message and marshal it
                // set the newPayload on the response
                //
                Object newPayload = response.getObject();
                if (_validateOutput) { // jsp 12-05-2005 QA Incident #948
                    Validator validator = jaxb.createValidator();
                    validator.validateRoot(newPayload);
                }
                
                // Create a XML Document
                if (docBuilder == null)
                {
                	dbFactory =	DocumentBuilderFactory.newInstance();
                	docBuilder = dbFactory.newDocumentBuilder();
                }
            	
                Document payloadDoc = docBuilder.newDocument();
                m.marshal(newPayload,payloadDoc);
                if ( log.isDebugEnabled() )
                	log.debug("successfully marshalled in XMLBindingFilter");
                response.setObject(payloadDoc);
                
				request.setProperty("JAXBContext", null);
            } 
            else if (payload instanceof com.sun.xml.bind.JAXBObject)
            {
                 requestLink.doFilter(request, response);
            }
            else
            {
                //
                // Unsupported incoming format. Throw a service exception.
                //
                throw new ServiceException(IdConstants.ERR_UNSUPPORTED_FORMAT);
            }
        } catch (UnmarshalException e) {
            throw new ServiceException(IdConstants.ERR_INCOMING_DOCUMENT_VALIDATION_ERROR, e.getMessage(), e);
        } catch (MarshalException e) {
            // jsp 01-19-05     	e.printStackTrace();
            throw new ServiceException(IdConstants.ERR_MARSHAL_ERROR, e.getMessage(), e);
        } catch (JAXBException e) {
            throw new ServiceException(IdConstants.ERR_BINDING_ERROR, e.getMessage(), e);
        }
    }
    
    private void setNamepacePrefixOverride(Marshaller marshaller) {
    	if (this.jaxbNamespacePrefixMapper != null) {
    		try {
    			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", this.jaxbNamespacePrefixMapper);
    		} catch (PropertyException e) {
    			log.error("This version of JAXB does not support a namespace prefix mapper", e);
    		}
    	}
    }
    
    /**
     * Initializes the filter with properties specified in the BOC config XML.
     * <p>
     * The following properties are supported:<p>
     *
     *
     * @param configProperties Properties specified in the BOC config XML.
     *
     * ContextClasspath - colon-separated list of Java package names that
     *      JAXBContext will use to search for classes. Example:
     *      com.fnf.xes.services.msgs.ifx:com.fnf.xes.services.overdraft.1_1
     * formatted.output - whether or not to "pretty print" the output to make it
     *      human-readable (true or false).
     * ValidateInput - whether to validate on unmarshalling (true or false)
     * ValidateOuput - whether to validate on marshalling (true or false)
     *
     * @throws BocException Included for Interface compliance
     */
    public void initializeFilter(Properties configProperties) throws BocException {
        try {
            //
            // JSP 01-09-2006 Use ManagedLogAbility (if provided) to obtain
            /// the correct Logger.
            //
            if (_managedLogAbility != null) {
                log = _managedLogAbility.getLogger(XMLBindingRequestFilter.class);
            }
            String contextName = configProperties.getProperty(CONTEXT_NAME);
            jaxb = JAXBContextFactory.getInstance().getContext(contextName);
            String strFormattedOutput = configProperties.getProperty("formatted.output","true");
            if (strFormattedOutput.equalsIgnoreCase("true")) {
                _formattedOutput = new Boolean(true);
            } else {
                _formattedOutput = new Boolean(false);
            }
            String validateInput = configProperties.getProperty(VALIDATE_INPUT, DEFAULT_VALIDATE_INPUT);
            _validateInput = (validateInput.equalsIgnoreCase("true")) ? true : false;
            String validateOutput = configProperties.getProperty(VALIDATE_OUTPUT, DEFAULT_VALIDATE_OUTPUT);
            _validateOutput = (validateOutput.equalsIgnoreCase("true")) ? true : false;
            //
            // Additional log output to test Default Logger
            //
            if ( log.isDebugEnabled() ){
	            log.debug("Format Output = " + _formattedOutput);
	            log.debug("validateInput = " + _validateInput);
	            log.debug("validateOutput = " + _validateOutput);
            }
            
            // Check if a namespace override property was specified.  If so, build a namespace prefix
            // mapper class to override XML namespace prefixes.
            String namespacePrefixOverrideFile = configProperties.getProperty(NAMESPACE_OVERRIDE_PROPERTY, 
            		NAMESPACE_OVERRIDE_PROPERTY_DEFAULT);
            
            if (namespacePrefixOverrideFile != null) {
            	this.jaxbNamespacePrefixMapper = new JAXBNamespacePrefixMapper(namespacePrefixOverrideFile);
            }
            
            // init unmarshaller
            u = jaxb.createUnmarshaller();
            
            // init marshaller
            m = jaxb.createMarshaller();
            setNamepacePrefixOverride(m);
            
        } catch (Exception e) {
            String msg = "initializeFilter(): ERROR: ";
            if ( log.isEnabledFor(Level.ERROR) )
            	log.error(msg, e);
            throw new BocException(msg, e);
        }
    }
 }