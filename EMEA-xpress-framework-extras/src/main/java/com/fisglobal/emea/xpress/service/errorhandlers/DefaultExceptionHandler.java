package com.fisglobal.emea.xpress.service.errorhandlers;

import com.fisglobal.emea.xpress.service.ServiceContext;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnis.xes.framework.component.ComponentContext;
import com.fnis.xes.services.errormapping.XesHostMessageWrapper;
import com.fnis.xes.services.template.XBOStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author morel
 */
public class DefaultExceptionHandler implements ExceptionHandler {

    private Long defaultStatusCode = 100L;
    private String defaultSeverity = "Error";
    private boolean continueProcessing = true;
    private String defaultSpName = "com.fnf.xes.XES";

    protected void addStatus(XBOStatus status, ComponentContext componentContext) {
        List<XBOStatus> xboStatusList = (List<XBOStatus>) componentContext.getAttribute("XBOStatusList");
        if (xboStatusList == null) {
            xboStatusList = new ArrayList<XBOStatus>();
            componentContext.setAttribute("XBOStatusList", xboStatusList);
        }
        xboStatusList.add(status);
    }

    public boolean handle(Throwable throwable, Map<String, Object> exceptionProcessingContext, RequestMessage requestMessage, ResponseMessage response, List<ServiceContext> serviceContextList) {
        StringBuilder causeMessage = new StringBuilder();
        causeMessage.append(throwable.getClass().getName()).append(" : ").append(throwable.getMessage());

        XBOStatus status = createXBOStatusWithCause(serviceContextList, causeMessage.toString());
        addStatus(status, serviceContextList.get(0).getComponentContext());

        status.setCause(throwable);
        status.setSeverity(getDefaultSeverity());
        status.setStatusCode(getDefaultStatusCode());
        status.setHostAppId(getDefaultSpName());

        return isContinueProcessing();
    }

    protected XBOStatus createXBOStatus(List<ServiceContext> serviceContextList) {
        // this is insane; just to maintain compatibility with stinky StatusHelper called by wierd MsgRouter!
        return serviceContextList.size() == 1 ?
                new XBOStatus(new XesHostMessageWrapper(getDefaultStatusCode().intValue(), "")) :
                new XBOStatus();
    }

    protected XBOStatus createXBOStatusWithCause(List<ServiceContext> serviceContextList, String message) {
        // PS-3333 this is another insane; just to be able to show detail message in response when there is no exception handler (uses the default)
        return serviceContextList.size() == 1 ?
                new XBOStatus(new XesHostMessageWrapper(getDefaultStatusCode().intValue(), message)) :
                new XBOStatus();
    }

    /**
     * @return the defaultStatusCode
     */
    public Long getDefaultStatusCode() {
        return defaultStatusCode;
    }

    /**
     * @param defaultStatusCode the defaultStatusCode to set
     */
    public void setDefaultStatusCode(Long defaultStatusCode) {
        this.defaultStatusCode = defaultStatusCode;
    }

    /**
     * @return the defaultSeverity
     */
    public String getDefaultSeverity() {
        return defaultSeverity;
    }

    /**
     * @param defaultSeverity the defaultSeverity to set
     */
    public void setDefaultSeverity(String defaultSeverity) {
        this.defaultSeverity = defaultSeverity;
    }

    /**
     * @return the continueProcessing
     */
    public boolean isContinueProcessing() {
        return continueProcessing;
    }

    /**
     * @param continueProcessing the continueProcessing to set
     */
    public void setContinueProcessing(boolean continueProcessing) {
        this.continueProcessing = continueProcessing;
    }

    /**
     * @return the defaultSpName
     */
    public String getDefaultSpName() {
        return defaultSpName;
    }

    /**
     * @param defaultSpName the defaultSpName to set
     */
    public void setDefaultSpName(String defaultSpName) {
        this.defaultSpName = defaultSpName;
    }
}
