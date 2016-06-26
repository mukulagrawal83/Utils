package com.fisglobal.xpress.emea.mblox;


import org.apache.commons.lang.StringUtils;

import java.util.Collection;

public class Utils {
    public static boolean isNull(Object obj) {
        return null == obj;
    }

    public static boolean isNullOrEmpty(Collection<?> col) {
        return col == null || col.isEmpty();
    }

    public static String blankToNull(String str) {
        return StringUtils.isNotBlank(str) ? str : null;
    }

    public static Double nanToNull(Double val) {
        return val == null ? val : val.isNaN() ? null : val;
    }
}
