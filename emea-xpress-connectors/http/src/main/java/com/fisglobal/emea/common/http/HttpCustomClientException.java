package com.fisglobal.emea.common.http;

public class HttpCustomClientException extends Exception {

    private static final long serialVersionUID = 1L;

    public HttpCustomClientException(){
        super();
    }
    
    public HttpCustomClientException(final String msg) {
        super(msg);
    }
    
    public HttpCustomClientException(final Throwable cause) {
        initCause(cause);
    }
    
    public HttpCustomClientException(final String msg, final Throwable cause) {
        super(msg);
        initCause(cause);
    }
    
}
