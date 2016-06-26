package com.fisglobal.emea.xpress.service.errorhandlers.exceptions.cortex;

import com.fisglobal.emea.xpress.service.ServiceContext;
import com.fisglobal.emea.xpress.service.errorhandlers.DefaultExceptionHandler;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.xes.framework.codetables.TableLookupException;
import com.fnis.ifx.xbo.v1_1.Factory;
import com.fnis.ifx.xbo.v1_1.base.Status;
import com.fnis.xes.services.template.XBOStatus;

import java.util.List;
import java.util.Map;

/**
 * This program contains trade secrets that belong to Fidelity Information
 * Services, Inc. and is licensed by an agreement.  Any unauthorized access,
 * use, duplication, or disclosure is unlawful.
 * <p/>
 * Copyright (c) Fidelity Information Services, Inc.
 * 2006, All right reserved.
 * <p/>
 * User: Satheesh Kumar G - e1011705
 * Date: 10/03/14
 * Time: 12:20
 */
public class CortexStatusExceptionHandler extends DefaultExceptionHandler {

    private Long errorStatusCode = 100L;
    private String errorSeverity = "Error";
    private String errorSpName   = "com.fnf.xes.XES";

    @Override
    public boolean handle(Throwable throwable, Map<String, Object> exceptionProcessingContext, RequestMessage requestMessage, ResponseMessage response, List<ServiceContext> serviceContextList) {

        if(throwable instanceof CortexStatusException){
            CortexStatusException exception = (CortexStatusException)throwable;
            int statusCode = Integer.parseInt(exception.getResponseCode());

            if(statusCode > 0) {
                addAdditionalStatus(serviceContextList, errorSeverity, (long)statusCode, exception.getErrorMessage(), errorSpName);
            } else {
                addAdditionalStatus(serviceContextList, getDefaultSeverity(), getDefaultStatusCode(), exception.getErrorMessage(), getDefaultSpName());
            }

            return isContinueProcessing();
        }
        if(throwable instanceof TableLookupException){
            TableLookupException exception = (TableLookupException) throwable;
            addAdditionalStatus(serviceContextList, errorSeverity, errorStatusCode, exception.getMessage(), errorSpName);
            return isContinueProcessing();
        }
        return true;
    }

    private void addAdditionalStatus(List<ServiceContext> serviceContextList, String severity, Long statusCode, String statusDesc, String spName) {
        Status additionalStatus = (Status) Factory.create(Status.class);
        XBOStatus status = createXBOStatus(serviceContextList);
        addStatus(status, serviceContextList.get(0).getComponentContext());

        additionalStatus.setSeverity(severity);
        additionalStatus.setStatusCode(statusCode);
        additionalStatus.setStatusDesc(statusDesc);

        status.setSeverity(severity);
        status.setStatusCode(statusCode);
        status.setHostAppId(spName);
        status.setStatus(additionalStatus);
    }

    /**
     * @return the errorStatusCode
     */
    public Long getErrorStatusCode() {
        return errorStatusCode;
    }

    /**
     * @param errorStatusCode the errorStatusCode to set
     */
    public void setErrorStatusCode(Long errorStatusCode) {
        this.errorStatusCode = errorStatusCode;
    }

    /**
     * @return the errorSeverity
     */
    public String getErrorSeverity() {
        return errorSeverity;
    }

    /**
     * @param errorSeverity the errorSeverity to set
     */
    public void setErrorSeverity(String errorSeverity) {
        this.errorSeverity = errorSeverity;
    }

    /**
     * @return the errorSpName
     */
    public String getErrorSpName() {
        return errorSpName;
    }

    /**
     * @param errorSpName the errorSpName to set
     */
    public void setErrorSpName(String errorSpName) {
        this.errorSpName = errorSpName;
    }
}
