package com.fnf.xes.tplegacy.filter;

import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.xes.framework.IdConstants;
import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.framework.component.ComponentContext;
import com.fnf.xes.framework.filter.DmrContext;
import com.fnf.xes.framework.security.SecurityException;
import com.fnf.xes.framework.security.VirtualClientContext;
import com.fnf.xes.framework.security.trust.SubjectContext;
import com.fnf.xes.framework.security.trust.SubjectContextBuilder;
import com.fnf.xes.tplegacy.credential.SessionKey;

/**
 * Implements the RequestFilter, Ability and SecurityContextProvider interfaces.
 * Provides an abstraction over the security token received in the request.
 *
 * @author e0111296
 */
public class BetterTPSecurityFilter2 implements RequestFilter {
	private static final String SECURITY_TOKEN = "SECURITY_TOKEN";
	private static final String REQUEST_CONTEXT = "REQUEST_CONTEXT";
	private ComponentContext componentContext;
	private SubjectContextBuilder subjectContextBuilder;
	
	/**
	 * Creates a new SecurityFilter object.
	 */
	public BetterTPSecurityFilter2( SubjectContextBuilder subjectContextBuilder ) {
		this.subjectContextBuilder = subjectContextBuilder;
	}

	/**
	 * Get the component context
	 *
	 * @return the component context
	 */
	public ComponentContext getComponentContext() {
		return this.componentContext;
	}

	/**
	 * Primary method on the filter
	 *
	 * @param requestMessage Request message
	 * @param responseMessage Response message
	 * @param requestLink Next link in the chain
	 *
	 * @throws java.lang.Throwable The thrown exception
	 */
	public void doFilter(
		com.fnf.jef.boc.RequestMessage requestMessage,
		com.fnf.jef.boc.ResponseMessage responseMessage,
		com.fnf.jef.boc.filter.RequestLink requestLink)
		throws java.lang.Throwable {
         
		try {
			String token = (String) requestMessage.getProperty(SECURITY_TOKEN);

			//CVNS - Added condition to skip creating context when SMValidateUser service is called
			DmrContext dmrCtx = (DmrContext) requestMessage.getProperty(DmrContext.class.getName());
			String serviceName = dmrCtx.getExecMethod();
			VirtualClientContext ctxt = null;
                  
			if (!serviceName.equals("SMValidateUser")	&& 
                     !serviceName.equals("ServiceLookup") && !serviceName.equals("ServiceRegister")) {
                     // Create a temporary, empty subject context.  This is really a claims holder since
                     // the context has not been validated.
                     SubjectContext subjectClaims = subjectContextBuilder.createSubjectContext();
                     if( token !=null ){
	            	subjectClaims.putCredential( new SessionKey( token ));
	            	subjectClaims.setAttribute("ENDPOINT.ID",requestMessage.getMsgId());
	            	 // now assert the 'claims' and get back a validated subject context (or else an exception is thrown)
                        SubjectContext subjectContext = subjectContextBuilder.assertSubjectContext(subjectClaims,null);
	                
                        componentContext = (ComponentContext) subjectContext.getCredential(ComponentContext.class);
                        if (componentContext == null) {
                            throw new ServiceException(IdConstants.SF_SECURITY_ERROR,"Credential not set");
                        }
                        ctxt = (VirtualClientContext) subjectContext.getCredential(VirtualClientContext.class);
                     }else{
	            	throw new ServiceException(IdConstants.SF_SECURITY_ERROR,"Token is missing");
                     }
			}

			requestMessage.setProperty(REQUEST_CONTEXT, ctxt);

			// this is here to support backward compatability.
			requestMessage.setProperty(ComponentContext.class.getName(), componentContext);

                  // let the next filter process
	            requestLink.doFilter(requestMessage, responseMessage);

			//CVNS - Added condition to update the context only if the object is instantiated
			if (ctxt != null) {
				ctxt.update();
				requestMessage.setProperty(SECURITY_TOKEN, ctxt.toString());
			}
			this.componentContext = null;
		} catch (SecurityException e) {
			throw new ServiceException(IdConstants.SF_SECURITY_ERROR, e.getMessage(), e);
		} finally {
	         // cleanup the security context - this causes cached credentials to be flushed.
	         subjectContextBuilder.revokeSubjectContext();               
            }
	}

	/**
	 * Initialize this filter with parameters set in the BOC xml configuration file
	 * @param Properties the BOC config intialization parameters for this ability
	 * @throws BocException thrown when ability intialization fails
	 */
	public void initializeFilter(java.util.Properties properties) throws com.fnf.jef.boc.BocException {
	}

}
