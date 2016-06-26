package com.fnis.xes.framework.ext.processxbo.path;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathElementMatcherFactory {
    public static final String BEAN_SELECTOR_REGEXP = "([a-zA-Z0-9]+)(\\[[0-9]+\\])?";

    private Pattern pattern;

    public PathElementMatcherFactory() {
        pattern = Pattern.compile(BEAN_SELECTOR_REGEXP);
    }

    public Matcher getMatcher(String s) {
        return pattern.matcher(s);
    }
}
