package com.fnis.xes.framework.filter;

import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.services.msgs.ifx.impl.*;
import com.fnis.xes.framework.errorhandlers.ErrorHandler;
import com.fnis.xes.framework.filter.handlers.TransformationHandler;
import com.fnis.xes.framework.util.ListMapper;
import com.sun.xml.bind.JAXBObject;
import com.sun.xml.bind.util.ListImpl;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author lc21878
 */
public class GenericJAXBTransformationFilter implements RequestFilter {

    private static final String CLASS_NAME = GenericJAXBTransformationFilter.class.getName();
    private static Logger log = Logger.getLogger(CLASS_NAME);

    private String elemToProcessInCompositeRq = "";

    private TransformationHandler handler;

    private List<? extends ErrorHandler<Exception, ServiceException>> errorHandlers = new ArrayList();

    public void doFilter(RequestMessage requestMessage, ResponseMessage responseMessage, RequestLink requestLink) throws Throwable {

        if (requestMessage.getObject() instanceof IFXImpl) {
            ListImpl list = (ListImpl) PropertyUtils.getNestedProperty(requestMessage.getObject(), "rqServices");
            if (list.get(0) instanceof BaseSvcRqImpl || list.get(0) instanceof BankSvcRqImpl) {
                /*BaseSvcRqImpl baseSvcRq = (BaseSvcRqImpl) list.get(0);
                ListImpl list2 = (ListImpl) baseSvcRq.getRqMessages();*/

                ListImpl list2 = (ListImpl) PropertyUtils.getNestedProperty(list.get(0), "rqMessages");

                for (Object o : list2) {
                    try {
                        getHandler().handleRequest(o);
                    } catch (Exception e) {
                        for (ErrorHandler<Exception, ServiceException> eh : getErrorHandlers()) {
                            eh.handle(e, requestMessage);
                        }
                    }
                }
            }
        } else if (requestMessage.getObject() instanceof JAXBObject) {
            try {
                getHandler().handleRequest(requestMessage.getObject());
            } catch (Exception e) {
                for (ErrorHandler<Exception, ServiceException> eh : getErrorHandlers()) {
                    eh.handle(e, requestMessage);
                }
            }
        }

        requestLink.doFilter(requestMessage, responseMessage);

        if (responseMessage.getObject() instanceof IFXImpl) {
            ListImpl list = (ListImpl) PropertyUtils.getNestedProperty(responseMessage.getObject(), "rsServices");

            if (list.get(0) instanceof BaseSvcRsImpl || list.get(0) instanceof BankSvcRsImpl) {
/*                BaseSvcRsImpl baseSvcRs = (BaseSvcRsImpl) list.get(0);
                ListImpl list2 = (ListImpl) baseSvcRs.getRsMessages();*/

                ListImpl list2 = (ListImpl) PropertyUtils.getNestedProperty(list.get(0), "rsMessages");

                for (Object o : list2) {
                    ListImpl list3 = null;

                    try {
                        list3 = (ListImpl) PropertyUtils.getNestedProperty(o, getElemToProcessInCompositeRq());
                    } catch (NoSuchMethodException nsme) {
                        log.debug("Nested property not existent or null in current object." + nsme.toString());
                    }
                    if (list3 != null) {
                        for (Object o2 : list3) {
                            try {
                                getHandler().handleResponse(o2);
                            } catch (Exception e) {
                                for (ErrorHandler<Exception, ServiceException> eh : getErrorHandlers()) {
                                    eh.handle(e, responseMessage);
                                }
                            }

                        }
                    }
                }
            }
        } else if (responseMessage.getObject() instanceof JAXBObject) {
            try {
                getHandler().handleResponse(responseMessage.getObject());
            } catch (Exception e) {
                for (ErrorHandler<Exception, ServiceException> eh : getErrorHandlers()) {
                    eh.handle(e, responseMessage);
                }
            }
        }
    }

    public void initializeFilter(Properties props) throws com.fnf.jef.boc.BocException {
    }

    protected List<? extends ErrorHandler<Exception, ServiceException>> getErrorHandlers() {
        return errorHandlers;
    }

    public void setErrorHandlers(List<? extends ErrorHandler<Exception, ServiceException>> errorHandlers) {
        this.errorHandlers = errorHandlers;
    }

    public String getElemToProcessInCompositeRq() {
        return elemToProcessInCompositeRq;
    }

    public void setElemToProcessInCompositeRq(String elemToProcessInCompositeRq) {
        this.elemToProcessInCompositeRq = elemToProcessInCompositeRq;
    }

    public TransformationHandler getHandler() {
        return handler;
    }

    public void setHandler(TransformationHandler handler) {
        this.handler = handler;
    }
}
