package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.FromAcctKeys;

public interface FromAcctKeysFormatDetector {
    public AcctKeysFormatType detect(FromAcctKeys fromAcctKeys);
}
