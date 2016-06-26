package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.ToAcctKeys;
import org.apache.log4j.Logger;

public abstract class ToAcctKeysConverter {
    private static final Logger log = Logger.getLogger(ToAcctKeysConverter.class);

    private AcctKeysFormatType from;
    private AcctKeysFormatType to;

    public abstract ToAcctKeys convert(ToAcctKeys acctKeys);

    public void setFrom(AcctKeysFormatType from) {
        this.from = from;
    }

    public void setTo(AcctKeysFormatType to) {
        this.to = to;
    }

    public boolean handles(AcctKeysFormatType from, AcctKeysFormatType to) {
        if(this.from.equals(from) && this.to.equals(to)) {
            log.debug("Handles conversion: " + from + " -> " + to);
            return true;
        } else {
            log.debug("Refusing to handle conversion: " + from + " -> " + to);
            return false;
        }
    }
}
