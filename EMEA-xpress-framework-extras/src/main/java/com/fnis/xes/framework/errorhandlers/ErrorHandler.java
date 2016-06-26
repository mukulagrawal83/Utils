package com.fnis.xes.framework.errorhandlers;

/**
 * @Author: trojanbug
 */
public interface ErrorHandler<T,E extends Exception> {
    public void handle(T error, Object ... context) throws E;
}
