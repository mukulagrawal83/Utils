package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.ToAcctKeys;

public interface ToAcctKeysFormatDetector {
    public AcctKeysFormatType detect(ToAcctKeys acctKeys);
}
