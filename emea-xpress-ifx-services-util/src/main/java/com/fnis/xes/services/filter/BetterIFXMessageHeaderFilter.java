package com.fnis.xes.services.filter;

import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.framework.security.XESSecurityPrincipal;
import com.fnf.xes.framework.security.trust.SubjectContext;
import com.fnis.ifx.xbo.v1_1.Operation;
import com.fnis.ifx.xbo.v1_1.Payload;
import com.fnis.ifx.xbo.v1_1.PayloadImpl;
import com.fnis.ifx.xbo.v1_1.ResponseImpl;
import com.fnis.ifx.xbo.v1_1.Service;
import com.fnis.ifx.xbo.v1_1.base.ContextRqHdr;
import com.fnis.ifx.xbo.v1_1.base.CredentialsRqHdr;
import com.fnis.ifx.xbo.v1_1.base.MsgRqHdr;
import com.fnis.ifx.xbo.v1_1.base.OperRqHdr;
import com.fnis.ifx.xbo.v1_1.base.SecTokenLogin;
import com.fnis.ifx.xbo.v1_1.base.Status;
import com.fnis.xes.framework.component.ComponentContext;
import com.fnis.xes.framework.security.trust.SubjectContextBuilder;
import com.fnis.xes.framework.util.ErrWarnInfoMessage;
import com.fnis.xes.services.IdConstants;
import com.fnis.xes.services.credential.IFXSessionKey;
import com.fnis.xes.services.util.StatusHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * This filter is responsible for processing <b>Operation</b> level header 
 * <OprRqHdr> or <b>Message</b> level header <MsgRqHdr> object. 
 * 
 * Prior to processing actual IFX Message, headers should be processed to see if the
 * requesting client has sufficient privileges to execute messages that are 
 * part of an operation or an individual message (i.e not as part of an operation)  
 * 
 * General Rules
 * 
 * </p>
 * Headers are required elements in operations and optional elements of messages. 
 * This means a request operation must contain a request header and a response 
 * operation must contain a response header. 
 * 
 * </p>
 * Messages that are transmitted on their own (i.e. not as part of an operation) 
 * must contain a message request header. Messages that form part of an operation 
 * may contain a message request header, in which case the elements of the header 
 * aggregate that are present in the message override the corresponding elements 
 * in the operation request header.  
 * 
 * @author e0101486
 */
public class BetterIFXMessageHeaderFilter implements RequestFilter {

	private static Logger log = Logger.getLogger(IFXMessageHeaderFilter.class);

	private ErrWarnInfoMessage ewiMessage;

	private SubjectContextBuilder subjectContextBuilder;

	private String CRED_KEY = XESSecurityPrincipal.class.toString();

	/**
	 * Creates a new instance of IFXMessageHeaderFilter
	 *
	 * @param ewiMessage - ErrWarnInfoMessage object
	 * @param subjectContextBuilder - SubjectContext builder
	 */
	public BetterIFXMessageHeaderFilter(ErrWarnInfoMessage ewiMessage,
			SubjectContextBuilder subjectContextBuilder) {
		this.ewiMessage = ewiMessage;
		this.subjectContextBuilder = subjectContextBuilder;

	}
	
	/**
	 * This filter processes <MsgRqHdr> aggregate provided as part of the
	 * request message. 
	 * 
	 * @param RequestMessage - object containing request payload 
	 * @param ResponseMessage - object contining response payload
	 *  
	 * @see RequestFilter#doFilter(RequestMessage,ResponseMessage, RequestLink)
	 */
	public void doFilter(RequestMessage requestMessage, ResponseMessage responseMessage,
			RequestLink requestLink) throws java.lang.Throwable {

         Payload ifxPayload = null;
         try {
            
               if (requestMessage.getObject() instanceof Payload) {

                     ifxPayload = (Payload) requestMessage.getObject();

                     if (ifxPayload.getProcess() instanceof Operation){
                           List<ComponentContext> compCtxList = processOperation(requestMessage);
                           if(compCtxList == null){
                                 responseMessage.setObject(ifxPayload);
                                 return;
                           }
                           // Store the componentContext in the request message
                           // This is used later by Dynamic method router
                           requestMessage.setProperty(ComponentContext.class.getName() + "List",
                                       compCtxList);

                     }else if (ifxPayload.getProcess() instanceof Service){
                           ComponentContext compCtx = processMessage(requestMessage);
                           if(compCtx == null){
                                 responseMessage.setObject(ifxPayload);
                                 return;
                           }
                           // Store the componentContext in the request message
                           // This is used later by Dynamic method router
                           requestMessage.setProperty(ComponentContext.class.getName(),
                                       compCtx);
                     }else{
                           log.error(ewiMessage.getString(IdConstants.IFX_ERR_INVALID_REQUEST_TYPE));
                           throw new ServiceException(IdConstants.IFX_ERR_INVALID_REQUEST_TYPE);
                     }
                     // Set the ifx payload as the root object
                     requestMessage.setObject(ifxPayload);

               } else {
                     Payload resPayload = new PayloadImpl();
                     Status status = StatusHelper.addStatus(IdConstants.IFX_ERR_INVALID_REQUEST_TYPE, StatusHelper.IFX_SEV_ERROR, 
                                 this.ewiMessage.format(IdConstants.IFX_ERR_INVALID_REQUEST_TYPE), null);
                     resPayload.getProcess().setStatus(status);
                     responseMessage.setObject(resPayload);
                     log.error(ewiMessage.getString(IdConstants.IFX_ERR_INVALID_REQUEST_TYPE) + resPayload.toString());
                     return;
               }

		// let the next filter process
               requestLink.doFilter(requestMessage, responseMessage);
               
		} finally {
			// cleanup the security context - this causes cached credentials to be
			// flushed.
			subjectContextBuilder.revokeSubjectContext();
		}
		responseMessage.setObject(ifxPayload);
	}

	/**
	 * Processes message request header <MsgRqHdr> from the IFX Message that are 
	 * transmitted on their own (i.e. not as part of an operation) to authenticate/
	 * authorize user credentials 
	 * 
	 * Users security token information is extracted from <SecToken> Object to validated
	 * against configured credential login modules
	 * 
	 * @param requestMessage - RequestMessage containing IFX Payload
	 * @return ComponentContext -  Subject Context object
	 * 
	 * @throws ServiceException - Error while validating security token
	 * 
	 */
	private ComponentContext processMessage(RequestMessage requestMessage) throws Exception{
		
		//Get the IFX Message
		Payload ifxPayload = (Payload)requestMessage.getObject();
		Service svcMsg = (Service) ifxPayload.getProcess();
		try{
			// Extract the <MsgRqHdr> information from the request message
			MsgRqHdr msgRqHdr = svcMsg.getRequest().getMsgRqHdr();
			if (msgRqHdr == null) {
            throw new ServiceException(IdConstants.IFX_ERR_MISSING_REQUEST_HEADER);
         }
	
			// Validate service credentials
			Object creds = requestMessage.getProperty(CRED_KEY);
			
			ComponentContext compCtx = validateSubjectCredentials(msgRqHdr.getCredentialsRqHdr(), msgRqHdr.getContextRqHdr(), creds);
			
			//Set the credentialInfo in the component context
			compCtx.setAttribute(ComponentContext.SIGNON_INFO, msgRqHdr.getCredentialsRqHdr());
			//Set the Service Provider (CPA) in the component context
			compCtx.setAttribute(ComponentContext.SP_NAME, msgRqHdr.getContextRqHdr().getSPName());
			// Set the original String message
			try{
				String channelID = msgRqHdr.getContextRqHdr().getClientApp().getName();
				compCtx.setAttribute(IdConstants.CHANNEL_ID, channelID);
			}catch(Exception e){}
			
		
			compCtx.setAttribute(ComponentContext.STRING_MESSAGE, requestMessage.getProperty(com.fnis.xes.framework.IdConstants.ORIG_MESSAGE));
         
         // set ifx action on the component context
         compCtx.setAttribute(ComponentContext.ACTION, svcMsg.getAction().toLowerCase());
			
			return compCtx;
		}catch(ServiceException exp){
			String errMsg = ewiMessage.format(IdConstants.IFX_ERR_SECURITY_INVALID,
					exp.getMessage());
			
			ResponseImpl svcRes = new ResponseImpl();
			svcMsg.setResponse(svcRes);

			Status status = StatusHelper.addStatus(IdConstants.IFX_ERR_SECURITY_INVALID, 
											StatusHelper.IFX_SEV_ERROR,	errMsg, null);
			
			svcMsg.setStatus(status);
			log.error("IFXMessageHeaderFilter" + errMsg);
			return null;
		}
	}
	
	/**
	 * Processes operation request header <OprRqHdr> and <MsgRqHdr> from the messages
	 * (that form part of an operation) to authenticate/authorize users credentials 
	 *  
	 * Users security token information is extracted from <SecToken> Object to validated
	 * against configured credential login modules
	 * 
	 * @param requestMessage - RequestMessage containing IFX Payload
	 * @return List<ComponentContext> - Subject Context object
	 * 
	 * @throws ServiceException - Error while validating security token
	 * 
	 */
	private List<ComponentContext> processOperation(RequestMessage requestMessage) throws Exception {
		
		// Get the IFX Operation Message
		Payload ifxPayload = (Payload)requestMessage.getObject();
		Operation operMsg = (Operation) ifxPayload.getProcess();

		// Extract the <OprRqHdr> information from the request message
		OperRqHdr operRqHdr = operMsg.getOperRqHdr();

		//Operation request should contain <OprRqHdr> 
		if(operRqHdr == null) {
         throw new ServiceException(IdConstants.IFX_ERR_MISSING_REQUEST_HEADER);
      }

		requestMessage.setProperty(OperRqHdr.class.getName(), operRqHdr);

		// Get the credential object from the request message
		Object creds = requestMessage.getProperty(CRED_KEY);

		List<ComponentContext> compCtxList = new ArrayList<ComponentContext>();
		
		ComponentContext compCtx = null;
		Iterator<Service> svcIter = operMsg.getServices().iterator();
		while (svcIter.hasNext()) {
			Service svcMsg = svcIter.next();

			try{
				MsgRqHdr msgRqHdr = svcMsg.getRequest().getMsgRqHdr();
				
				if(msgRqHdr != null){ 
					//Service level message header <MsgRqHdr> takes precedence over <OprRqHdr>
					compCtx = validateSubjectCredentials(msgRqHdr.getCredentialsRqHdr(), msgRqHdr.getContextRqHdr(), creds);
					//Set the credentialInfo in the component context
					compCtx.setAttribute(ComponentContext.SIGNON_INFO, msgRqHdr.getCredentialsRqHdr());
					//Set the Service Provider (CPA) in the component context
					compCtx.setAttribute(ComponentContext.SP_NAME, msgRqHdr.getContextRqHdr().getSPName());
				}else{ 
					//Utilize the operation level message header <OprRqHdr> if service level <MsgRqHdr> is missing
					compCtx = validateSubjectCredentials(operRqHdr.getCredentialsRqHdr(), operRqHdr.getContextRqHdr(), creds);
					//Set the credentialInfo in the component context
					compCtx.setAttribute(ComponentContext.SIGNON_INFO, operRqHdr.getCredentialsRqHdr());
					//Set the Service Provider (CPA) in the component context
					compCtx.setAttribute(ComponentContext.SP_NAME, operRqHdr.getContextRqHdr().getSPName());
				}
				compCtxList.add(compCtx);
			}catch(ServiceException exp){
				String errMsg = ewiMessage.format(IdConstants.IFX_ERR_SECURITY_INVALID,
						exp.getMessage());
				
				Status status = StatusHelper.addStatus(IdConstants.IFX_ERR_SECURITY_INVALID, 
						StatusHelper.IFX_SEV_ERROR,	errMsg, null);

				ResponseImpl svcRes = new ResponseImpl();
				svcMsg.setResponse(svcRes);
				svcMsg.setStatus(status);
				log.error("IFXMessageHeaderFilter" + errMsg);

				//Populate empty response for all the services in the operation list
				svcIter = operMsg.getServices().iterator();
				while (svcIter.hasNext()) {
					svcMsg = svcIter.next();
					svcMsg.setResponse(new ResponseImpl());
				}
				return null;
			}
		}
		return compCtxList;
	}

	/**
	 * Validates Subject's credentials 
	 * 
	 * @param credentialsHdrList - Vector containing CredentialRqHdr object
	 * @param ctxRqHdr - Message Context header
	 * @param creds - credential object
	 * @return - ComponentConxtext containing SubjectContext information
	 * 
	 * @throws ServiceException - if subject credential validation fails 
	 */
	private ComponentContext validateSubjectCredentials(List<CredentialsRqHdr> credentialsHdrList, ContextRqHdr ctxRqHdr, Object creds)
		throws ServiceException{

		ComponentContext compCtx = null;
		SubjectContext subjectContext = subjectContextBuilder.createSubjectContext();

		//<CredentialsRqHdr> is a repeating object however current implementation assumes that 
		//only single instance of this object will be passed in the request message
		
		if (credentialsHdrList != null && credentialsHdrList.size() > 0) {
			CredentialsRqHdr credentialsRqHdr = (CredentialsRqHdr) credentialsHdrList.get(0);			
			
			if (credentialsRqHdr.getSecTokenLogin() != null && credentialsRqHdr.getSecTokenLogin().size() > 0) {
				subjectContext.putCredential(SecTokenLogin.class, credentialsRqHdr.getSecTokenLogin().get(0));
			} else if (credentialsRqHdr.getSecTokenKey() != null && credentialsRqHdr.getSecTokenKey().size() > 0) {
				String sessKey = credentialsRqHdr.getSecTokenKey().get(0).getSessKey();
				if ((sessKey != null)) {
					subjectContext.putCredential(new IFXSessionKey(sessKey));
				}
			}

			//Add credential object to SubjectContext
			if (creds != null) {
            subjectContext.putCredential(creds);
         }
			
			//Get the clientName from Context request header  
			String clientName = ctxRqHdr.getClientApp().getName();
			if (clientName != null) {
				subjectContext.setAttribute(IdConstants.CHANNEL_ID, clientName);
			}
			
			//added to support nutiOrgLoginModule
			String org = ctxRqHdr.getClientApp().getOrg();
			if (org!=null){
				subjectContext.setAttribute("ORG", org);
			}
			
			subjectContext = subjectContextBuilder.assertSubjectContext(subjectContext);

			compCtx = (ComponentContext) subjectContext.getCredential(ComponentContext.class);
			if (compCtx == null) {
				throw new ServiceException(IdConstants.CRED_COMP_CTX_NOT_FOUND, ewiMessage.format(IdConstants.CRED_COMP_CTX_NOT_FOUND));
			}
			compCtx.setAttribute(IdConstants.CHANNEL_ID, clientName);
		}else {
            throw new ServiceException(IdConstants.CRED_COMP_CTX_NOT_FOUND, ewiMessage.format(IdConstants.IFX_ERR_MISSING_CREDENTIALS_HEADER));
         }
		
		return compCtx;
	}
	public void initializeFilter(java.util.Properties properties)
						throws com.fnf.jef.boc.BocException {
	}
}
