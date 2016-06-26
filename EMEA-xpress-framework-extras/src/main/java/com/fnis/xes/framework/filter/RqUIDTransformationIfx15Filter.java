package com.fnis.xes.framework.filter;

import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.xes.framework.util.ErrWarnInfoMessage;
import com.fnf.xes.services.msgs.ifx.BankSvcRq;
import com.fnf.xes.services.msgs.ifx.BaseSvcRq;
import com.fnf.xes.services.msgs.ifx.ClientApp_Type;
import com.fnf.xes.services.msgs.ifx.IFX;
import com.fnf.xes.services.msgs.ifx.MsgRqHdr;
import com.fnf.xes.services.msgs.ifx.ObjectFactory;
import com.fnf.xes.services.msgs.ifx.SignonRq;
import com.fnf.xes.services.msgs.ifx.SignonRq_Type;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * This program contains trade secrets that belong to Fidelity Information
 * Services, Inc. and is licensed by an agreement.  Any unauthorized access,
 * use, duplication, or disclosure is unlawful.
 * <p/>
 * Copyright (c) Fidelity Information Services, Inc.
 * 2006, All right reserved.
 * <p/>
 * User: Satheesh Kumar G - e1011705
 * Date: 21/07/14
 * Time: 12:10
 */
public class RqUIDTransformationIfx15Filter implements RequestFilter {
    //0000-00-00T00:00:00.00-00:00
    public static DateFormat ifxDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS-00:00");
    public static final String GET_RQUID = "getRqUID";
    public static final String SET_RQUID = "setRqUID";
    public static final String GET_MSGRQ_HDR = "getMsgRqHdr";
    public static final String GET_SIGNON_RQ = "getSignonRq";
    public static final String GET_RQ_SERVICES = "getRqServices";

    private ErrWarnInfoMessage ewiMessage;
    private ObjectFactory ifxFactory;

    private String custLanguagePrf;
    private String clientAppName;
    private String clientAppOrg;
    private String clientAppVersion;
    private String rqUUID = "00000000-0000-0000-0000-000000000000";

    public String getCustLanguagePrf() {
        return custLanguagePrf;
    }

    public void setCustLanguagePrf(String custLanguagePrf) {
        this.custLanguagePrf = custLanguagePrf;
    }

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }

    public String getClientAppOrg() {
        return clientAppOrg;
    }

    public void setClientAppOrg(String clientAppOrg) {
        this.clientAppOrg = clientAppOrg;
    }

    public String getClientAppVersion() {
        return clientAppVersion;
    }

    public void setClientAppVersion(String clientAppVersion) {
        this.clientAppVersion = clientAppVersion;
    }

    public String getRqUUID() {
        return rqUUID;
    }

    public void setRqUUID(String rqUUID) {
        this.rqUUID = rqUUID;
    }

    /**
     *
     * @param ewiMessage
     */
    public RqUIDTransformationIfx15Filter(ErrWarnInfoMessage ewiMessage) {
        this.ewiMessage = ewiMessage;
        ifxFactory = new ObjectFactory();
    }

    /**
     * Execute the logic of the filter
     *
     * @param request
     * @param response
     * @param requestLink
     * @throws Throwable Description of the Exception
     */
    public void doFilter(RequestMessage request, ResponseMessage response, RequestLink requestLink) throws Throwable {
        checkAndGenerateRqUid(request);
        requestLink.doFilter(request, response);
    }

    /**
     * Method will check whether the input has rqUID present or not and also check rqUID is all zero [00000000-0000-0000-0000-000000000000].
     * If anyone of the condition passed then method will populate the new rqUID and also set ClientApp, ClientDt and etc if it is not present.
     *
     * @param requestMessage
     * @throws java.lang.Throwable
     */
    public void checkAndGenerateRqUid(RequestMessage requestMessage)throws java.lang.Throwable {
         //Create since it is used across the request
        String newRqUID = UUID.randomUUID().toString();

        Object ifxPayload = requestMessage.getObject();
        Class payloadClazz = ifxPayload.getClass();

        if(isIFXTagAttached(ifxPayload)) {
            Method[] methods = payloadClazz.getMethods();

            //iterate through all the methods from the root class and get the signOnRq and rqServices
            //to check the rqUID and set the values
            for(Method method : methods) {
                if(GET_SIGNON_RQ.equals(method.getName())) {
                    SignonRq signonRq = (SignonRq) method.invoke(ifxPayload, new Object[]{});
                    setClientAppData(signonRq);
                }
                if(GET_RQ_SERVICES.equals(method.getName())) {
                    List servicesList = (List) method.invoke(ifxPayload, new Object[]{});

                    for(Object svc : servicesList) {
                        List reqServiceMessages = null;
                        String rqUidFromInput = null;
                        if(svc instanceof BaseSvcRq) {
                           BaseSvcRq baseSvcRq = (BaseSvcRq) svc;
                           rqUidFromInput = baseSvcRq.getRqUID();
                           if (StringUtils.isBlank(rqUidFromInput)|| isRqUidZeros(rqUidFromInput)) {
                               baseSvcRq.setRqUID(newRqUID);
                           }
                           reqServiceMessages = baseSvcRq.getRqMessages();
                        } else if(svc instanceof BankSvcRq) {
                           BankSvcRq bankSvcRq = (BankSvcRq) svc;
                           rqUidFromInput = bankSvcRq.getRqUID();
                           if (StringUtils.isBlank(rqUidFromInput)|| isRqUidZeros(rqUidFromInput)) {
                               bankSvcRq.setRqUID(newRqUID);
                           }
                           reqServiceMessages = bankSvcRq.getRqMessages();
                        }
                        //reqServiceMessages hold services either from [BankSvc/BaseSvc].
                        //populate new rqUID, ClientAppData
                        if(null != reqServiceMessages) {
                            for (Object reqServiceMessage : reqServiceMessages) {
                               Class rqMessageClazz = reqServiceMessage.getClass();
                               checkAndSetNewReqUid(rqMessageClazz, reqServiceMessage, newRqUID);
                               setClientAppData(getSignOnRqType(reqServiceMessage));
                            }
                        }
                    }
                }
            }
        } else {  //Request from other than TP [without IFX root tag]
            checkAndSetNewReqUid(payloadClazz, requestMessage.getObject(), newRqUID);
            setClientAppData(getSignOnRqType(ifxPayload));
        }
    }

    /**
     * Check the incoming payload is wrapping up by IFX root tag.
     *
     * @param ifxPayload
     * @return
     * @throws Exception
     */
    private boolean isIFXTagAttached(Object ifxPayload) throws Exception {
        if(ifxPayload instanceof IFX) {
          return true;
        }
        return false;
    }

    /**
     * Check and Set newly created RqUID
     *
     * @param reqServiceMessage
     * @param newRqUID
     * @throws Exception
     */
    private void checkAndSetNewReqUid(Class rqMessageClazz, Object reqServiceMessage, String newRqUID) throws Exception{
        Method rqUidGetMethod = rqMessageClazz.getMethod(GET_RQUID, new Class[]{});
        String rqUidFromInput = (String) rqUidGetMethod.invoke(reqServiceMessage, new Object[]{});

        if (StringUtils.isBlank(rqUidFromInput)|| isRqUidZeros(rqUidFromInput)) {
            Method rqUidSetMethod = rqMessageClazz.getMethod(SET_RQUID, new Class [] {String.class});
            rqUidSetMethod.invoke(reqServiceMessage,  new Object[]{newRqUID});
        }
    }

    /**
     *  Get the SignOnRq from MsgHdr if present else returns null.
     *
     * @param reqMessage
     * @return
     * @throws Exception
     */
    private SignonRq_Type getSignOnRqType(Object reqMessage)throws Exception{
        if(null == reqMessage) {
            return null;
        }

        Method method = findMethod(reqMessage, GET_MSGRQ_HDR);
        if(method != null)  {
            MsgRqHdr msgRqHdr = (MsgRqHdr)method.invoke(reqMessage, new Object[]{});
            if (msgRqHdr != null) {
                return msgRqHdr.getSignonRq();
            }
        }

        return null;
    }

    /**
     * Populate the ClientApp, CustLangPref, ClientDt aggregate
     *
     * @param signOnRq
     * @throws Exception
     */
    private void setClientAppData(SignonRq_Type signOnRq)throws Exception{
        if(signOnRq == null)
            return;

        if(StringUtils.isBlank(signOnRq.getCustLangPref())) {
            signOnRq.setCustLangPref(custLanguagePrf);
        }

        if(StringUtils.isBlank(signOnRq.getClientDt())) {
            signOnRq.setClientDt(ifxDateFormat.format(Calendar.getInstance().getTime()));
        }

        if(signOnRq.getClientApp() == null) {
            signOnRq.setClientApp(ifxFactory.createClientApp());
        }

        ClientApp_Type clientApp = signOnRq.getClientApp();

        if (StringUtils.isBlank(clientApp.getName())){
            clientApp.setName(this.clientAppName);
        }

        if (StringUtils.isBlank(clientApp.getOrg())){
            clientApp.setOrg(this.clientAppOrg);
        }

        if (StringUtils.isBlank(clientApp.getVersion())){
            clientApp.setVersion(this.clientAppVersion);
        }

        signOnRq.setClientApp(clientApp);
    }

    /**
     *
     * @param rqUid
     * @return
     */
    private boolean isRqUidZeros(String rqUid) {
        if(StringUtils.isNotBlank(rqUid)) {
            if(rqUUID.equalsIgnoreCase(rqUid)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param target
     * @param name
     * @return
     * @throws Exception
     */
    private Method findMethod(Object target, String name)throws Exception{
        Method method = null;
        Class targetClass = target.getClass();
        method = targetClass.getMethod(name, new Class[]{});
        return method;
    }
    /**
     * Initialize the filter
     *
     * @param configProperties
     * @throws BocException Description of the Exception
     */
    public void initializeFilter(Properties configProperties) throws BocException {

    }
}
