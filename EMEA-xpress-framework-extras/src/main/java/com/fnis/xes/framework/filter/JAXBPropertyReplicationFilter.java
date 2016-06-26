package com.fnis.xes.framework.filter;

import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.services.acct.acctinq.v2_0.DepAcctRec;
import com.fnf.xes.services.msgs.ifx.impl.BaseSvcRqImpl;
import com.fnf.xes.services.msgs.ifx.impl.BaseSvcRsImpl;
import com.fnf.xes.services.msgs.ifx.impl.IFXImpl;
import com.fnf.xes.services.msgs.ifxext.AcctMiscData_Type;
import com.fnf.xes.services.msgs.ifxext.ObjectFactory;
import com.fnis.xes.framework.errorhandlers.ErrorHandler;
import com.fnis.xes.framework.filter.converters.ExternalLookupException;
import com.fnis.xes.framework.filter.handlers.ReplicationHandler;
import com.fnis.xes.framework.util.ListMapper;
import com.fnis.xes.framework.util.MappingException;
import com.sun.xml.bind.JAXBObject;
import com.sun.xml.bind.util.ListImpl;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author lc21878
 */
public class JAXBPropertyReplicationFilter implements RequestFilter {

    private static final String CLASS_NAME = JAXBPropertyReplicationFilter.class.getName();
    private static Logger log = Logger.getLogger(CLASS_NAME);
    private static final String SPNAMEPATH = "custId.SPName";
    private static final String SPNAMEPROFILE = "com.fnf.xes.PRF";

    private ReplicationHandler handler;

    private String elemToProcessInCompositeRq = "";

    private List<? extends ErrorHandler<Exception, ServiceException>> errorHandlers = new ArrayList();

    public void doFilter(RequestMessage requestMessage, ResponseMessage responseMessage, RequestLink requestLink) throws Throwable {

        requestLink.doFilter(requestMessage, responseMessage);

        Object objectToHandle = responseMessage.getObject();

        if (checkSPName(requestMessage.getObject())) {
            if (requestMessage.getObject() instanceof IFXImpl) {
                ListImpl list = (ListImpl) PropertyUtils.getNestedProperty(objectToHandle, "rsServices");

                BaseSvcRsImpl baseSvcRs = (BaseSvcRsImpl) list.get(0);
                ListImpl list2 = (ListImpl) baseSvcRs.getRsMessages();

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
                                handler.handle(o2);
                            } catch (Exception e) {
                                for (ErrorHandler<Exception, ServiceException> eh : getErrorHandlers()) {
                                    eh.handle(e, responseMessage);
                                }
                            }

                        }
                    }
                }
            } else if (requestMessage.getObject() instanceof JAXBObject) {
                try {
                    handler.handle(objectToHandle);
                } catch (Exception e) {
                    for (ErrorHandler<Exception, ServiceException> eh : getErrorHandlers()) {
                        eh.handle(e, responseMessage);
                    }
                }
            }
        }
    }

    public boolean checkSPName(Object payload) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        if (payload instanceof IFXImpl) {

            ListImpl list = (ListImpl) PropertyUtils.getNestedProperty(payload, "rqServices");

            if (list.get(0) instanceof BaseSvcRqImpl) {
                BaseSvcRqImpl rqImpl = (BaseSvcRqImpl) list.get(0);

                if (rqImpl.getSPName().equals(SPNAMEPROFILE)) {
                    return true;
                }
            }
        } else if (payload instanceof JAXBObject && PropertyUtils.getNestedProperty(payload, SPNAMEPATH).equals(SPNAMEPROFILE)) {
            return true;
        }

        return false;
    }

    public void initializeFilter(Properties props) throws BocException {
    }

    public List<? extends ErrorHandler<Exception, ServiceException>> getErrorHandlers() {
        return errorHandlers;
    }

    public void setErrorHandlers(List<? extends ErrorHandler<Exception, ServiceException>> errorHandlers) {
        this.errorHandlers = errorHandlers;
    }

    public ReplicationHandler getHandler() {
        return handler;
    }

    public void setHandler(ReplicationHandler handler) {
        this.handler = handler;
    }

    public String getElemToProcessInCompositeRq() {
        return elemToProcessInCompositeRq;
    }

    public void setElemToProcessInCompositeRq(String elemToProcessInCompositeRq) {
        this.elemToProcessInCompositeRq = elemToProcessInCompositeRq;
    }
}
