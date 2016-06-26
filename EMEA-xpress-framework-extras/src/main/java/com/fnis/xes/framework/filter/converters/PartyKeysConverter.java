package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.PartyKeys;
import org.apache.log4j.Logger;

/**
 * @author e1050475
 */
public abstract class PartyKeysConverter {
    private static final Logger log = Logger.getLogger(PartyKeysConverter.class);

    private PartyKeysFormatType from;
    private PartyKeysFormatType to;

    public abstract PartyKeys convert(PartyKeysConversionContext partyKeysConversionContext, PartyKeys partyKeys);

    public void setFrom(PartyKeysFormatType from) {
        this.from = from;
    }

    public void setTo(PartyKeysFormatType to) {
        this.to = to;
    }

    public boolean handles(PartyKeysFormatType from, PartyKeysFormatType to) {
        if(this.from.equals(from) && this.to.equals(to)) {
            log.debug("Handles conversion: " + from + " -> " + to);
            return true;
        } else {
            log.debug("Refusing to handle conversion: " + from + " -> " + to);
            return false;
        }
    }
}
