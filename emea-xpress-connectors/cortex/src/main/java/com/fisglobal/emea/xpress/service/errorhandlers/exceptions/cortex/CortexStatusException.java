package com.fisglobal.emea.xpress.service.errorhandlers.exceptions.cortex;

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
 * Time: 12:37
 */
public class CortexStatusException extends RuntimeException {
    private String responseCode;
    private String errorMessage;

    public CortexStatusException(String responseCode, String errorMessage) {
        this(errorMessage, responseCode, null);
    }

    public CortexStatusException(String message, String responseCode, String errorMessage) {
        super(message);
        this.responseCode = responseCode;
        this.errorMessage = errorMessage;
    }

    public CortexStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public CortexStatusException(Throwable cause) {
        super(cause);
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getErrorMessage() {
        return (null != errorMessage ? errorMessage.trim() : errorMessage);
    }
}
