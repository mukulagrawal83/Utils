package com.fnis.xes.framework.filter;

import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.xes.framework.ServiceException;
import com.fnis.ifx.xbo.v1_1.Factory;
import com.fnis.ifx.xbo.v1_1.Payload;
import com.fnis.ifx.xbo.v1_1.Service;
import com.fnis.ifx.xbo.v1_1.XBO;
import com.fnis.ifx.xbo.v1_1.base.AcctIdent;
import com.fnis.ifx.xbo.v1_1.base.AcctKeys;
import com.fnis.xes.framework.errorhandlers.ErrorHandler;
import com.fnis.xes.framework.filter.converters.ExternalLookupException;
import com.fnis.xes.framework.util.ListMapper;
import com.fnis.xes.framework.util.MappingException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import java.util.*;

public class XBOPropertyReplicationCortexFilter implements RequestFilter {

    private static final String CLASS_NAME = XBOPropertyReplicationCortexFilter.class.getName();
    private static Logger log = Logger.getLogger(CLASS_NAME);
    private static final String SEPARATOR = "-";

    private Map<Object, Object> pathSelectors;

    private ListMapper mapper;

    private String formatToProcess;

    private List<? extends ErrorHandler<Exception, ServiceException>> errorHandlers = new ArrayList();

    public void doFilter(RequestMessage requestMessage, ResponseMessage responseMessage, RequestLink requestLink) throws Throwable {

        requestLink.doFilter(requestMessage, responseMessage);

        if (checkSPName(requestMessage)) {

            try {

                Iterator it = getPathSelectors().entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();

                    String pathToAcctIdentifier = (String) entry.getKey();
                    String pathToAcctInfo = (String) entry.getValue();
                    String accountIdentifier = "";

                    log.debug("Processing [" + pathToAcctIdentifier + "] and " + "[" + pathToAcctInfo + "]");

                    List<XBO> xbos = getXBOs(responseMessage);
                    for (XBO xbo : xbos) {
                        if (xbo != null) {

                            List<String> mappedValues = new ArrayList<String>();
                            try {

                                accountIdentifier = (String) PropertyUtils.getNestedProperty(xbo, pathToAcctIdentifier);

                                List<String> values = new ArrayList<String>();
                                values.add(accountIdentifier);

                                mappedValues = getMapper().map(values);
                                StringBuilder mappedValue = new StringBuilder();

                                if (mappedValues.size() > 0) {
                                    for (String s : mappedValues) {
                                        mappedValue.append(s);
                                        mappedValue.append(SEPARATOR);
                                    }
                                }

                                AcctKeys acctKeys = (AcctKeys) PropertyUtils.getNestedProperty(xbo, pathToAcctInfo);

                                acctKeys.setAcctIdent(buildAcctIdent(getFormatToProcess(), mappedValue.toString().substring(0, mappedValue.length() - 1)));
                            } catch (NoSuchMethodException nsme) {
                                log.debug("Nested property not existent in current object." + nsme.toString());
                            } catch (MappingException me) {
                                throw new ExternalLookupException("Could not find corresponding value for original value : " + me.toString());
                            }
                        }

                    }
                }
            } catch (Exception e) {
                for (ErrorHandler<Exception, ServiceException> eh : getErrorHandlers()) {
                    eh.handle(e, responseMessage);
                }
            }
        }
    }

    public boolean checkSPName(RequestMessage requestMessage) {
        if (requestMessage.getObject() instanceof Payload) {
            Payload payload = (Payload) requestMessage.getObject();

            if (payload.getProcess() instanceof Service) {
                Service service = (Service) payload.getProcess();

                if (service.getRequest().getMsgRqHdr().getContextRqHdr().getSPName().equals("com.fnis.xes.CORTEX")) {
                    return true;
                } else {
                    return false;
                }
            }
        } else if (requestMessage.getObject() instanceof Service) {
            Service service = (Service) requestMessage.getObject();

            if (service.getRequest().getMsgRqHdr().getContextRqHdr().getSPName().equals("com.fnis.xes.CORTEX")) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public List<XBO> getXBOs(ResponseMessage responseMessage) {

        List<XBO> xbos = null;

        if (responseMessage.getObject() instanceof Payload) {
            Payload payload = (Payload) responseMessage.getObject();

            if (payload.getProcess() instanceof Service) {
                Service service = (Service) payload.getProcess();

                xbos = service.getResponse().getXBO();
            }
        }

        return xbos;
    }

    public AcctIdent buildAcctIdent(String formatToProcess, String acctId) {
        AcctIdent acctIdent = (AcctIdent) Factory.create(AcctIdent.class);
        acctIdent.setAcctIdentType(formatToProcess);
        acctIdent.setAcctIdentValue(acctId);

        return acctIdent;
    }

    public void initializeFilter(Properties props) throws BocException {
    }


    public Map<Object, Object> getPathSelectors() {
        return pathSelectors;
    }

    public void setPathSelectors(Map<Object, Object> pathSelectors) {
        this.pathSelectors = pathSelectors;
    }

    public ListMapper getMapper() {
        return mapper;
    }

    public void setMapper(ListMapper mapper) {
        this.mapper = mapper;
    }

    public String getFormatToProcess() {
        return formatToProcess;
    }

    public void setFormatToProcess(String formatToProcess) {
        this.formatToProcess = formatToProcess;
    }

    public List<? extends ErrorHandler<Exception, ServiceException>> getErrorHandlers() {
        return errorHandlers;
    }

    public void setErrorHandlers(List<? extends ErrorHandler<Exception, ServiceException>> errorHandlers) {
        this.errorHandlers = errorHandlers;
    }
}
