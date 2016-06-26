package com.fnf.xes.services.filter;


import java.util.Date;

import org.apache.log4j.Logger;

import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.framework.component.ComponentContext;
import com.fnf.xes.framework.security.VirtualClientContext;
import com.fnf.xes.framework.security.trust.SubjectContext;
import com.fnf.xes.framework.security.trust.SubjectContextBuilder;
import com.fnf.xes.framework.util.ErrWarnInfoMessage;
import com.fnf.xes.framework.utiljaxb.JaxbUtils;
import com.fnf.xes.services.IdConstants;
import com.fnf.xes.services.credential.IFXSessionKey;
import com.fnf.xes.services.msgs.ifx.IFX;
import com.fnf.xes.services.msgs.ifx.ObjectFactory;
import com.fnf.xes.services.msgs.ifx.SignonPswd_Type;
import com.fnf.xes.services.msgs.ifx.SignonRs;
import com.fnf.xes.services.msgs.ifx.SignonRs_Type;
import com.fnf.xes.services.msgs.ifx.Status_Type;
import com.fnf.xes.services.util.DateUtil;


/**
 * This filter is responsible for processing the IFX signon message.  Per the
 * spec this has to be done before anyother ifx message can be processed.
 *
 * @author e0067399
 */
public class BetterIFXSignonFilter2 implements RequestFilter {


    private static Logger log = Logger.getLogger(BetterIFXSignonFilter2.class);
    private ObjectFactory ifxFactory;
    private ErrWarnInfoMessage ewiMessage;
    private SubjectContextBuilder subjectContextBuilder;
    private VirtualClientContext virtualClientContext;
    private String loginModule = null;




    /**
     * Creates a new instance of IfxSignonFilter
     *
     * @param ewiMessage DOCUMENT ME!
     * @param subjectContextBuilder DOCUMENT ME!
     */
    public BetterIFXSignonFilter2(ErrWarnInfoMessage ewiMessage,
        SubjectContextBuilder subjectContextBuilder) {

        ifxFactory = new ObjectFactory();
        this.ewiMessage = ewiMessage;
        this.subjectContextBuilder = subjectContextBuilder;

    }
    
    public BetterIFXSignonFilter2(ErrWarnInfoMessage ewiMessage,
            SubjectContextBuilder subjectContextBuilder, String loginModule) {

            ifxFactory = new ObjectFactory();
            this.ewiMessage = ewiMessage;
            this.subjectContextBuilder = subjectContextBuilder;
            this.loginModule = loginModule;

        }

    /**
     * DOCUMENT ME!
     *
     * @param requestMessage DOCUMENT ME!
     * @param responseMessage DOCUMENT ME!
     * @param requestLink DOCUMENT ME!
     *
     * @throws java.lang.Throwable DOCUMENT ME!
     */
    public void doFilter(com.fnf.jef.boc.RequestMessage requestMessage,
        com.fnf.jef.boc.ResponseMessage responseMessage,
        com.fnf.jef.boc.filter.RequestLink requestLink)
        throws java.lang.Throwable {

      IFX ifxRequest = null;
      IFX ifxResponse = ifxFactory.createIFX();
      
      // how sweet
      String requestSessionKey = null;
      
      try {
            responseMessage.setObject(ifxResponse);

            Object requestPayload = requestMessage.getObject();
            

            //NFR Assert that we are dealing with an IFX message
            if (requestPayload instanceof com.fnf.xes.services.msgs.ifx.IFX) {

                SignonRs signonRs = ifxFactory.createSignonRs();

                // set the Server Date. Since the Host does not return any date as part of the service, set it to middleware date
                String serverDate = DateUtil.getDateString(new Date(),
                        "yyyy-MM-dd'T'00:00:00.000000-00:00");
                signonRs.setServerDt(serverDate);

                // set Language  to "CustLangPref" of SignonRq. or default to "en_US"
                ifxRequest = (IFX) requestPayload;
                String lang = ifxRequest.getSignonRq().getCustLangPref();
                lang = (lang == null) ? "en_US" : lang;
                signonRs.setLanguage(lang);

                ifxResponse.setSignonRs(signonRs);

                // Create a temporary, empty subject context.  This is really a claims holder since
                // the context has not been validated.
                SubjectContext subjectClaims = subjectContextBuilder.createSubjectContext();

                populateSubjectClaims(ifxRequest, subjectClaims);
                subjectClaims.setAttribute("ENDPOINT.ID",requestMessage.getMsgId());

                // now assert the 'claims' and get back a validated subject context (or else an exception is thrown)
                SubjectContext subjectContext = subjectContextBuilder.assertSubjectContext(subjectClaims,loginModule);

                ComponentContext compCtx = (ComponentContext) subjectContext.getCredential(ComponentContext.class);
                if (compCtx == null) {
                    throw new ServiceException(IdConstants.CRED_COMP_CTX_NOT_FOUND);
                }
                compCtx.setAttribute(IdConstants.CHANNEL_ID,subjectContext.getAttribute("CHANNEL_ID"));
                // Store the componentContext in the request message
                // This is used later by Dynamic method router
                requestMessage.setProperty(ComponentContext.class.getName(), compCtx);

                // For backwards compatability with Webster code base
                virtualClientContext = null;

                try {
                    virtualClientContext = (VirtualClientContext) subjectContext.getCredential(VirtualClientContext.class);
                } catch (Exception e) {
                    log.info("Unable to create VirtualClientContext credential");
                    // ok to iqnore - may have thrown becuase the system is not
                    // configured for Webster.
                }
            } else {
                addStatusAggregate(ifxResponse,
                    IdConstants.IFX_ERR_INVALID_REQUEST_TYPE, "Error",
                    this.ewiMessage.format(IdConstants.IFX_ERR_INVALID_REQUEST_TYPE));
                addStatusAggregate(ifxResponse,
                    IdConstants.IFX_ERR_INVALID_REQUEST_TYPE, "Error",
                    this.ewiMessage.format(IdConstants.IFX_ERR_INVALID_REQUEST_TYPE));

                return;
            }

        // let the next filter process
            requestLink.doFilter(requestMessage, responseMessage);
            
        } finally {
            // cleanup the security context - this causes cached credentials to be
            // flushed.
            subjectContextBuilder.revokeSubjectContext();
        }

        // BR send back updated TP token to client
        // This code is left here for backwards compatability with Webster Bank (WB).  WB
        // uses TP TOken as the authentication and auditing identity.  Part of the WB
        // security scheme is to return a refreshed tp token to the caller.
        // This code should have been placed in a WB specific package.class but since
        // WB was the first client for XES, some of WB's business rules mede their way
        // into XES.
        if (responseMessage.getObject() instanceof com.fnf.xes.services.msgs.ifx.IFX) {
            ifxResponse = (IFX) responseMessage.getObject();

            SignonRs_Type signonRs = ifxResponse.getSignonRs();

            if ((signonRs != null) && (virtualClientContext != null)) {
                // Get TP Token from requestMessage and set it in SignonRs element. This should have been updated by Security filter
                virtualClientContext.update();

                String tpToken = virtualClientContext.toString();

                // If TP Token is null in the request Message, set whatever was sent in SignonRq
                tpToken = (tpToken == null) ? requestSessionKey : tpToken;
                signonRs.setSessKey(tpToken);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ifx DOCUMENT ME!
     * @param subjectContext DOCUMENT ME!
     *
     */
    private void populateSubjectClaims(IFX ifx, SubjectContext subjectClaims) {
        if (ifx.getSignonRq() != null) {
            // Map the clientName to the CHANNEL_ID
            String clientName = ifx.getSignonRq().getClientApp().getName();

            if (clientName != null) {
                subjectClaims.setAttribute("CHANNEL_ID", clientName);
            }

            // Get the session key
            String sessKey = ifx.getSignonRq().getSessKey();

            if ((sessKey != null)) {
                subjectClaims.putCredential(new IFXSessionKey(sessKey));
            }

            // get the SignonPswd object
            SignonPswd_Type signonPswd = ifx.getSignonRq().getSignonPswd();

            if (signonPswd != null) {
                // store the credential as the interface type not the
                subjectClaims.putCredential(JaxbUtils.getPrimaryInterfaceClass(signonPswd), signonPswd);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ifx DOCUMENT ME!
     * @param fnfErrorCode DOCUMENT ME!
     * @param severity DOCUMENT ME!
     * @param desc DOCUMENT ME!
     */
    private void addStatusAggregate(IFX ifx, long fnfErrorCode,
        String severity, String desc) throws Exception {
        Status_Type rootStatus = ifx.getStatus();

        if (rootStatus == null) {
            rootStatus = ifxFactory.createStatus_Type();
            rootStatus.setStatusCode(200);
            rootStatus.setServerStatusCode(String.valueOf(fnfErrorCode));
            rootStatus.setSeverity(severity);
            rootStatus.setStatusDesc(desc);
            ifx.setStatus(rootStatus);
        } else {
            Status_Type childStatus = ifxFactory.createStatus_Type();
            childStatus.setStatusCode(200);
            childStatus.setServerStatusCode(String.valueOf(fnfErrorCode));
            childStatus.setSeverity(severity);
            childStatus.setStatusDesc(desc);
            rootStatus.getAdditionalStatus().add(childStatus);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param properties DOCUMENT ME!
     *
     * @throws com.fnf.jef.boc.BocException DOCUMENT ME!
     */
    public void initializeFilter(java.util.Properties properties)
        throws com.fnf.jef.boc.BocException {
    }
}
