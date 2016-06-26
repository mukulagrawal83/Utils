package com.fnis.xes.framework.ext.processxbo.subject.xbo;

import java.util.Arrays;
import java.util.List;

public class XBOConstants {
    public static final String IFX_RECSELECT_RECORD = "RecSelect";
    public static final String IFS_XXXSEL_AGGREGATE_SUFFIX = "Sel";
    public static final List<String> XXXREC_DIRECT_ATTRIBUTE_LIST = Arrays.asList(new String[]{"Keys", "Info", "Envr", "Status", "Auth"});
    public static final List<String> SERVICE_DIRECT_ATTRIBUTE_LIST = Arrays.asList(new String[]{"RecCtrlIn", "RecCtrlOut", "RqUID"});
    public static final List<String> xboDirectAttributes = Arrays.asList(new String[]{"UpdElements", "DelElements", "NewElements", "CTUList", "CTUValue"});
}
