package com.fnis.xes.framework.util;

import java.util.StringTokenizer;

/**
 *
 * @author trojanbug
 */
public class SubtokenSelectionMapperWrapper implements Mapper {

    private Mapper mapper;
    private String mappedToken = "TOKEN";
    private String separators = " ,;=<>!|/&[].";

    public String map(String value) {
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(value, separators, true);
        String currentToken;
        boolean tokenFound = false;
        while (st.hasMoreTokens()) {
            currentToken = st.nextToken();
            if (!tokenFound) {
                if (mappedToken.equals(currentToken)) {
                    tokenFound = true;
                }
                sb.append(currentToken);
            } else {
                if (separators.contains(currentToken)) {
                    sb.append(currentToken);
                } else {
                    sb.append(mapper.map(currentToken));
                    tokenFound = false;
                }
            }
        }

        return sb.toString();
    }

    public Mapper getMapper() {
        return mapper;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public String getMappedToken() {
        return mappedToken;
    }

    public void setMappedToken(String mappedToken) {
        this.mappedToken = mappedToken;
    }

    public String getSeparators() {
        return separators;
    }

    public void setSeparators(String separators) {
        this.separators = separators;
    }
}
