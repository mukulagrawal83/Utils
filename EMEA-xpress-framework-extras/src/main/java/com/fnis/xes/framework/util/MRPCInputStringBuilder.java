package com.fnis.xes.framework.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MRPCInputStringBuilder {

    private String separator = ",";
    private static final String EQUALS = "=";
    private Map<String, String> paramMap = new HashMap();

    public MRPCInputStringBuilder() {

    }

    public MRPCInputStringBuilder(String separator) {
        this.separator = separator;
    }

    public void setPreserveOrder(boolean preserveOrder) {
        if (preserveOrder && !(paramMap instanceof TreeMap))  {
            TreeMap<String, String> newMap= new TreeMap<String, String>();
            newMap.putAll(paramMap);
            paramMap = newMap;
        }
    }

    public void reset() {
        paramMap.clear();
    }

    public MRPCInputStringBuilder addInputParam(String paramName, String paramValue) {
        validateName(paramName);
        paramValue = sanitizeValue(paramValue);
        paramMap.put(paramName, paramValue);
        return this;
    }

    private String sanitizeValue(String paramValue) {
        return paramValue.replace("\"", "\"\"");
    }

    private void validateName(String paramName) {
        // placeholder to encode name validations, although it should be covered by other tests anyway
        // for now empty
    }

    public String getInputString() {
        StringBuilder builder = new StringBuilder();

        Iterator<Map.Entry<String, String>> iterator = paramMap.entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            builder.append(entry.getKey()).append(EQUALS).append('"').append(entry.getValue()).append('"');
            while (iterator.hasNext()) {
                entry = iterator.next();
                builder.append(separator);
                builder.append(entry.getKey()).append(EQUALS).append('"').append(entry.getValue()).append('"');
            }
        }

        return builder.toString();
    }
}
