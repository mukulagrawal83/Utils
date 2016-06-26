package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.AcctIdent;
import com.fnis.ifx.xbo.v1_1.base.AcctIdentImpl;
import com.fnis.ifx.xbo.v1_1.base.AcctKeys;
import com.fnis.xes.framework.util.Mapper;
import org.apache.log4j.Logger;

public class AcctKeysToCanonicalFromAcctId extends AcctKeysConverter {
    private static final String CANONICAL_IDENT_TYPE = "Profile";
    private static final Logger log = Logger.getLogger(AcctKeysToCanonicalFromAcctId.class);

    private Mapper mapper;

    public AcctKeys convert(AcctKeys acctKeys) {

        acctKeys = mapAcctId(acctKeys);
        acctKeys = convertAcctIdToIdent(acctKeys);

        return acctKeys;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    private AcctKeys mapAcctId(AcctKeys acctKeys) {
        if(acctKeys.getAcctId() != null) {
            if("".equals(acctKeys.getAcctId().trim())) {
                log.debug("Could not map blank AcctId");
                throw new ExternalLookupException("Could not map blank AcctId");
            }

            String originalValue = acctKeys.getAcctId();
            log.debug("Found original AcctId=" + originalValue);
            String mappedValue = mapper.map(acctKeys.getAcctId());
            if(mappedValue != null) {
                log.debug("Substituting value of AcctId=" + originalValue + " with " + mappedValue);
                acctKeys.setAcctId(mappedValue);
            } else {
                log.debug("Could not find corresponding value for AcctId=" + originalValue);
                throw new ExternalLookupException("Could not find corresponding value for AcctId=" + originalValue);
            }
        }
        return acctKeys;
    }

    private AcctKeys convertAcctIdToIdent(AcctKeys acctKeys) {
        String acctId = acctKeys.getAcctId();

        AcctIdent acctIdent = new AcctIdentImpl();
        acctIdent.setAcctIdentType(CANONICAL_IDENT_TYPE);
        acctIdent.setAcctIdentValue(acctId);

        setAcctIdentAsKey(acctKeys, acctIdent);
        return acctKeys;
    }

    private void setAcctIdentAsKey(AcctKeys acctKeys, AcctIdent acctIdent) {
        acctKeys.setAcctIdent(acctIdent);
        acctKeys.setAcctId(null);
    }

}
