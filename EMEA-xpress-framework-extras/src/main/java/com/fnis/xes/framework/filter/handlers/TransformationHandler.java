package com.fnis.xes.framework.filter.handlers;

import com.fnf.xes.framework.ServiceException;

/**
 * User: lc21878
 */
public interface TransformationHandler {

    public void handleRequest(Object obj) throws ServiceException, Throwable;

    public void handleResponse(Object obj) throws ServiceException, Throwable;
}
