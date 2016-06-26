package com.fnis.xes.framework.filter.converters;

import com.fnis.xes.framework.ext.processxbo.exceptions.TransformException;

public class ExternalLookupException extends TransformException {

    public ExternalLookupException() {
    }

    public ExternalLookupException(String message) {
        super(message);
    }

    public ExternalLookupException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalLookupException(Throwable cause) {
        super(cause);
    }

}
