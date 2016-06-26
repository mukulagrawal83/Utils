package com.fisglobal.xpress.services;


import com.fnf.xes.framework.ServiceException;

public interface JMSMessageService {
    Object processMessage(Object request) throws ServiceException;
    boolean accept(Object request) throws ServiceException;
}
