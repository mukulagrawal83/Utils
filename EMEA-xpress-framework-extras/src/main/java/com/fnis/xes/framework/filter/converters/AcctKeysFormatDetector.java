package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.AcctKeys;

public interface AcctKeysFormatDetector {
    public AcctKeysFormatType detect(AcctKeys acctKeys);
}
