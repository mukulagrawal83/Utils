package com.fnis.xes.framework.filter.handlers;

import com.fnf.xes.framework.ServiceException;
import com.fnis.xes.framework.errorhandlers.ErrorHandler;
import com.fnis.xes.framework.filter.converters.ExternalLookupException;
import com.fnis.xes.framework.util.ListMapper;
import com.fnis.xes.framework.util.MappingException;
import com.sun.xml.bind.JAXBObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: lc21878
 */
public class JAXBTransformationHandler implements TransformationHandler {

    private static final String CLASS_NAME = JAXBTransformationHandler.class.getName();
    private static Logger log = Logger.getLogger(CLASS_NAME);

    private ListMapper requestMapper;
    private ListMapper responseMapper;

    private String formatToProcess;

    private String[] requestPathSelectors;
    private String[] responsePathSelectors;
    private String inputFormatRegex;

    private static final String SEPARATOR = "-";
    private String pathToFormat;

    private List<? extends ErrorHandler<Exception, ServiceException>> errorHandlers = new ArrayList();

    public void handleRequest(Object obj) throws ServiceException, Throwable {
        if (getFormatToProcess().equals(PropertyUtils.getNestedProperty(obj, getPathToFormat()))) {
            for (String pathSelector : getRequestPathSelectors()) {
                log.debug("Processing [" + pathSelector + "]");
                String originalValue = "";

                if (obj instanceof JAXBObject) {

                    try {
                        originalValue = (String) PropertyUtils.getNestedProperty(obj, pathSelector);
                        log.debug("Original value to be mapped : " + originalValue);
                        List<String> values = new ArrayList<String>();

                        Pattern p = Pattern.compile(getInputFormatRegex());
                        Matcher m = p.matcher(originalValue);

                        if (m.matches()) {
                            for (int i = 1; i <= m.groupCount(); i++) {
                                values.add(m.group(i));
                            }
                        } else {
                            throw new ExternalLookupException("Could not find corresponding value for " + pathSelector + "=" + originalValue);
                        }

                        List<String> result = getRequestMapper().map(values);

                        if (result.size() > 0) {
                            PropertyUtils.setNestedProperty(obj, pathSelector, result.get(0));

                            log.debug("New value : " + PropertyUtils.getNestedProperty(obj, pathSelector));
                        } else {
                            throw new ExternalLookupException("Could not find corresponding value for original value : " + originalValue);
                        }
                    } catch (NoSuchMethodException nsme) {
                        log.debug("Nested property not existent in current object." + nsme.toString());
                    } catch (MappingException me) {
                        throw new ExternalLookupException("Could not find corresponding value for original value : " + me.toString());
                    }
                }
            }
        }
    }

    public void handleResponse(Object obj) throws ServiceException, Throwable {
        if (getFormatToProcess().equals(PropertyUtils.getNestedProperty(obj, getPathToFormat()))) {
            for (String pathSelector : getResponsePathSelectors()) {
                log.debug("Processing [" + pathSelector + "]");
                String originalValue = "";

                if (obj instanceof JAXBObject) {
                    try {
                        originalValue = (String) PropertyUtils.getNestedProperty(obj, pathSelector);
                        List<String> values = new ArrayList<String>();
                        values.add(originalValue);

                        List<String> mappedValues = getResponseMapper().map(values);
                        if (mappedValues.size() > 0) {
                            StringBuilder mappedValue = new StringBuilder();

                            for (String s : mappedValues) {
                                mappedValue.append(s);
                                mappedValue.append(SEPARATOR);
                            }
                            PropertyUtils.setNestedProperty(obj, pathSelector, mappedValue.toString().substring(0, mappedValue.length() - 1));
                        } else {
                            throw new ExternalLookupException("Could not find corresponding value for original value : " + originalValue);
                        }
                    } catch (NoSuchMethodException nsme) {
                        log.debug("Nested property not existent in current object." + nsme.toString());
                    } catch (MappingException me) {
                        throw new ExternalLookupException("Could not find corresponding value for original value : " + me.toString());
                    }
                }
            }
        }
    }

    public ListMapper getRequestMapper() {
        return requestMapper;
    }

    public void setRequestMapper(ListMapper requestMapper) {
        this.requestMapper = requestMapper;
    }

    public ListMapper getResponseMapper() {
        return responseMapper;
    }

    public void setResponseMapper(ListMapper responseMapper) {
        this.responseMapper = responseMapper;
    }

    public String getFormatToProcess() {
        return formatToProcess;
    }

    public void setFormatToProcess(String formatToProcess) {
        this.formatToProcess = formatToProcess;
    }

    public String[] getRequestPathSelectors() {
        return requestPathSelectors;
    }

    public void setRequestPathSelectors(String[] requestPathSelectors) {
        this.requestPathSelectors = requestPathSelectors;
    }

    public String[] getResponsePathSelectors() {
        return responsePathSelectors;
    }

    public void setResponsePathSelectors(String[] responsePathSelectors) {
        this.responsePathSelectors = responsePathSelectors;
    }

    public String getInputFormatRegex() {
        return inputFormatRegex;
    }

    public void setInputFormatRegex(String inputFormatRegex) {
        this.inputFormatRegex = inputFormatRegex;
    }

    public String getPathToFormat() {
        return pathToFormat;
    }

    public void setPathToFormat(String pathToFormat) {
        this.pathToFormat = pathToFormat;
    }

    public List<? extends ErrorHandler<Exception, ServiceException>> getErrorHandlers() {
        return errorHandlers;
    }

    public void setErrorHandlers(List<? extends ErrorHandler<Exception, ServiceException>> errorHandlers) {
        this.errorHandlers = errorHandlers;
    }
}
