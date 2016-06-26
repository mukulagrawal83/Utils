package com.fnis.xes.framework.filter.handlers;

import com.fnf.xes.framework.ServiceException;

/**
 * User: lc21878
 */
public interface ReplicationHandler {

    public void handle(Object obj) throws ServiceException, Throwable;
}
