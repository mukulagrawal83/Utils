package com.fnis.xes.framework.errorhandlers;

public abstract class ExceptionMapper<E extends Exception> implements ErrorHandler<Exception,E> {

    private Class handledExceptionClass = Exception.class;

    public abstract void handle(Exception error, Object... context) throws E;

    public Class getHandledExceptionClass() {
        return handledExceptionClass;
    }

    public void setHandledExceptionClass(Class handledExceptionClass) {
        this.handledExceptionClass = handledExceptionClass;
    }
}
