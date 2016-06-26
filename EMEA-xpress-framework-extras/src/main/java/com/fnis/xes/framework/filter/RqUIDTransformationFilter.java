package com.fnis.xes.framework.filter;

import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnis.ifx.xbo.v1_1.Payload;
import com.fnis.ifx.xbo.v1_1.Service;
import com.fnis.ifx.xbo.v1_1.base.ClientApp;
import com.fnis.ifx.xbo.v1_1.base.ClientAppImpl;
import com.fnis.ifx.xbo.v1_1.base.ContextRqHdr;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
 * Date: 15/07/14
 * Time: 11:55
 */
public class RqUIDTransformationFilter implements RequestFilter {
    //0000-00-00T00:00:00.000000-00:00
    public static DateFormat ifxDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS-00:00");

    private String custLanguagePrf;
    private String spName;
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

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
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
     * Execute the logic of the filter
     *
     * @param request
     * @param response
     * @param requestLink
     * @throws Throwable Description of the Exception
     */
    public void doFilter(RequestMessage request, ResponseMessage response, RequestLink requestLink) throws Throwable {
        // incoming message
        checkAndGenerateRqUid(request);
        requestLink.doFilter(request, response);
    }

    /**
     * Initialize the filter
     *
     * @param configProperties
     * @throws BocException Description of the Exception
     */
    public void initializeFilter(Properties configProperties) throws BocException {
    }

    /**
     * Method will check whether the input has rqUID present or not and also check rqUID is all zero [00000000-0000-0000-0000-000000000000].
     * If anyone of the condition passed then method will populate the new rqUID and also set ClientApp, ClientDt and etc if it is not present.
     *
     * @param requestMessage
     */
    public void checkAndGenerateRqUid(RequestMessage requestMessage) {
        String rqUidFromInput = null;
        Service service = null;
        if (requestMessage.getObject() instanceof Payload) {
            Payload payload = (Payload) requestMessage.getObject();
            if (payload.getProcess() instanceof Service) {
                service = (Service) payload.getProcess();
                rqUidFromInput = service.getRqUID();
            }
        } else if(requestMessage.getObject() instanceof Service) {
            service = (Service) requestMessage.getObject();
            rqUidFromInput = service.getRqUID();
        }

        if (StringUtils.isBlank(rqUidFromInput)|| isRqUidZeros(rqUidFromInput)) {
            setNewRqUidAndClientAppData(service);
        }
    }

    /**
     *  Populate the new rqUID and set the values for the ClientApp, CustLangPref, ClientDt aggregates.
     *
     * @param service
     */
    private void setNewRqUidAndClientAppData(Service service ) {
        if(null == service) {
            return;
        }

        service.setRqUID(UUID.randomUUID().toString());
        ContextRqHdr contextRqHdr = service.getRequest().getMsgRqHdr().getContextRqHdr();

        if (contextRqHdr.getClientApp() ==null)  {
            contextRqHdr.setClientApp( new ClientAppImpl());
        }

        ClientApp clientApp =  contextRqHdr.getClientApp();

        contextRqHdr.setClientDt(ifxDateFormat.format(Calendar.getInstance().getTime()));
        contextRqHdr.setCustLangPref(this.custLanguagePrf);

        if (StringUtils.isBlank(clientApp.getName())){
            clientApp.setName(this.clientAppName);
        }

        if (StringUtils.isBlank(clientApp.getOrg())){
            clientApp.setOrg(this.clientAppOrg);
        }

        if (StringUtils.isBlank(clientApp.getVersion())){
            clientApp.setVersion(this.clientAppVersion);
        }

        contextRqHdr.setClientApp(clientApp);

        service.getRequest().getMsgRqHdr().setContextRqHdr(contextRqHdr);
    }

    /**
     *  Check rqUID is all zeros.
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
}
