package com.fnis.xes.framework.util;

public class MappingException extends RuntimeException {
    public MappingException() {
    }

    public MappingException(String s) {
        super(s);
    }

    public MappingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MappingException(Throwable throwable) {
        super(throwable);
    }
}
