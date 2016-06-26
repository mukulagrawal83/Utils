package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.AcctIdent;
import com.fnis.ifx.xbo.v1_1.base.AcctKeys;
import com.fnis.ifx.xbo.v1_1.base.ToAcctKeys;
import org.apache.log4j.Logger;

public class GenericToAcctKeysFormatDetector implements ToAcctKeysFormatDetector {
    private static final Logger log = Logger.getLogger(GenericToAcctKeysFormatDetector.class);


    // TODO: this is only proof of concept code. proper business req. analysis should occur to provide more information
    public AcctKeysFormatType detect(ToAcctKeys acctKeys) {
        log.debug("Entered detect()");
        AcctKeysFormatType format = null;

        if(acctKeys.getAcctId() != null && acctKeys.getAcctIdent() == null) {
            format = AcctKeysFormatType.ID;
        } else if (acctKeys.getAcctId() == null && acctKeys.getAcctIdent() != null) {
            AcctIdent acctIdent = acctKeys.getAcctIdent();
            if(acctIdent.getAcctIdentType().equals("Profile")) {
                format = AcctKeysFormatType.IDENT_VALUE_PROFILE;
            } else if (acctIdent.getAcctIdentType().equals("AgreementID")) {
                format = AcctKeysFormatType.IDENT_VALUE_AGREEMENT_ID;
            } else if (acctIdent.getAcctIdentType().equals("SortCodeAcctNo")) {
                format = AcctKeysFormatType.SORTCODEACCTNO;
            } else if (acctIdent.getAcctIdentType().equals("SortCodeProfileCid")) {
                format = AcctKeysFormatType.SORTCODEPROFILECID;
            }
        } else if(acctKeys.getIBAN() != null) {
            format = AcctKeysFormatType.OTHER;
        }

        if(format == null) {
            log.debug("ToAcctKeys format not recognized!");
            return AcctKeysFormatType.UNKNOWN;
        }

        log.debug("Detected ToAcctKeys format: " + format);
        log.debug("Leaving detect()");
        return format;
    }

}
