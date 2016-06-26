package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.PartyIdent;
import com.fnis.ifx.xbo.v1_1.base.PartyKeys;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author e1050475
 */
public class GenericPartyKeysFormatDetector implements PartyKeysFormatDetector {
    private static final Logger log = Logger.getLogger(GenericPartyKeysFormatDetector.class);

    public PartyKeysFormatType detect(PartyKeysConversionContext partyKeysConversionContext, PartyKeys partyKeys) {
        log.debug("Entered detect()");
        PartyKeysFormatType format = null;

        if (StringUtils.isNotEmpty(partyKeys.getPartyId())&& partyKeys.getPartyIdent() == null) {
            format = PartyKeysFormatType.ID;
        } else if(StringUtils.isEmpty(partyKeys.getPartyId()) && (partyKeys.getPartyIdent() != null && partyKeys.getPartyIdent().getPartyIdentType() ==null)) {
            format = PartyKeysFormatType.EMPTY;
            partyKeysConversionContext.setIdentTypePresent(false);
        } else if (partyKeys.getPartyIdent() != null) {
            PartyIdent partyIdent = partyKeys.getPartyIdent();
            if (partyIdent.getPartyIdentType().equals("Profile")) {
                format = PartyKeysFormatType.PROFILE;
            } else if (partyIdent.getPartyIdentType().equals("")) {
                format = PartyKeysFormatType.EMPTY;
            }
        }

        if (format == null) {
            log.debug("PartyKeys format not recognized!");
            return PartyKeysFormatType.UNKNOWN;
        }

        log.debug("Detected PartyKeys format: " + format);
        log.debug("Leaving detect()");
        return format;
    }

}
