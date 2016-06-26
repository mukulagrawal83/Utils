package com.fnis.xes.framework.filter.handlers;

import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.services.msgs.ifxext.AcctMiscData_Type;
import com.fnf.xes.services.msgs.ifxext.ObjectFactory;
import com.fnis.xes.framework.errorhandlers.ErrorHandler;
import com.fnis.xes.framework.filter.converters.ExternalLookupException;
import com.fnis.xes.framework.util.ListMapper;
import com.fnis.xes.framework.util.MappingException;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: lc21878
 */
public class MapBasedReplicationHandler implements  ReplicationHandler{

    private static final String CLASS_NAME = MapBasedReplicationHandler.class.getName();
    private static Logger log = Logger.getLogger(CLASS_NAME);

    private ListMapper mapper;
    private ListMapper loanAcctMapper;

    private Map<Object, Object> pathSelectors;
    private String formatToProcess;

    private static final String SEPARATOR = "-";

    public void handle(Object obj) throws ServiceException, Throwable {

            Iterator it = getPathSelectors().entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();

                String pathToAcctIdentifier = (String) entry.getKey();
                String pathToAcctInfo = (String) entry.getValue();

                log.debug("Processing [" + pathToAcctIdentifier + "] and " + "[" + pathToAcctInfo + "]");

                String acctIdentifier = "";

                try {
                    acctIdentifier = (String) PropertyUtils.getNestedProperty(obj, pathToAcctIdentifier);
                } catch (NoSuchMethodException nsme) {
                    log.debug("Nested property not existent or null in current object." + nsme.toString());
                } catch (NestedNullException nne) {
                    log.debug("Nested property not existent or null in current object." + nne.toString());
                }

                if (acctIdentifier.contains("-")) {
                    replicateMiscData(obj, acctIdentifier, pathToAcctInfo);
                    break;
                } else if(StringUtils.isNotEmpty(acctIdentifier)) {

                    List<String> mappedValues = new ArrayList<String>();
                    List<String> mappedLoanValues = new ArrayList<String>();
                    try {

                        List<String> values = new ArrayList<String>();
                        values.add(acctIdentifier);

                        mappedValues = getMapper().map(values);
                        mappedLoanValues = getLoanAcctMapper().map(values);
                        StringBuilder mappedValue = new StringBuilder();

                        if (mappedValues.size() > 0) {
                            for (String s : mappedValues) {
                                mappedValue.append(s);
                                mappedValue.append(SEPARATOR);
                            }
                        } else if (mappedLoanValues.size() > 0) {
                            for (String s : mappedLoanValues) {
                                mappedValue.append(s);
                                mappedValue.append(SEPARATOR);
                            }
                        }

                        if(!mappedValue.toString().contains("null")){
                            replicateMiscData(obj, mappedValue.toString().substring(0, mappedValue.length() - 1), pathToAcctInfo);
                        }
                        break;
                    } catch (NoSuchMethodException nsme) {
                        log.debug("Nested property not existent in current object." + nsme.toString());
                    } catch (MappingException me) {
                        throw new ExternalLookupException("Could not find corresponding value for original value : " + me.toString());
                    }
                }
            }

    }

    public void replicateMiscData(Object message, String acctId, String path) throws JAXBException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ObjectFactory objectFactory = new ObjectFactory();
        AcctMiscData_Type acctMiscData = objectFactory.createAcctMiscData_Type();

        acctMiscData.setAcctMiscType(getFormatToProcess());
        acctMiscData.setMiscText(acctId);

        List miscData = (List) PropertyUtils.getNestedProperty(message, path);

        miscData.add(acctMiscData);
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

    public ListMapper getLoanAcctMapper() {
        return loanAcctMapper;
    }

    public void setLoanAcctMapper(ListMapper loanAcctMapper) {
        this.loanAcctMapper = loanAcctMapper;
    }
}
