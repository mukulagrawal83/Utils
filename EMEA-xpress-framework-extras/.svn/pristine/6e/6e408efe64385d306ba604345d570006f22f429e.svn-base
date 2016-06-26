package com.fnis.xes.framework.errorhandlers;

import com.fnf.xes.framework.ServiceException;

public class ServiceExceptionMapper extends ExceptionMapper<ServiceException> {

    private String errorMessage = "Exception occurred in processing";
    private int errorCode = 100;

    @Override
    public void handle(Exception error, Object... context) throws ServiceException {
       if (getHandledExceptionClass().isAssignableFrom(error.getClass())) {
           throw new ServiceException(getErrorCode(), getErrorMessage(), error);
       }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
