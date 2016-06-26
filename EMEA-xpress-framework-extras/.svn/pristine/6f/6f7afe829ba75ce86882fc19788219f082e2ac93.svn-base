package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.AcctKeys;
import com.fnis.xes.framework.util.Mapper;
import org.apache.log4j.Logger;

public class AcctKeysToAcctIdFromCanonical extends AcctKeysConverter {
    private static final Logger log = Logger.getLogger(AcctKeysToAcctIdFromCanonical.class);

    private Mapper mapper;

    public AcctKeys convert(AcctKeys acctKeys) {

        acctKeys = convertAcctIdentToId(acctKeys);
        acctKeys = mapAcctId(acctKeys);

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

    private AcctKeys convertAcctIdentToId(AcctKeys acctKeys) {
        String acctId = acctKeys.getAcctIdent().getAcctIdentValue();

        setAcctIdAsKey(acctKeys, acctId);
        return acctKeys;
    }

    private void setAcctIdAsKey(AcctKeys acctKeys, String acctId) {
        acctKeys.setAcctIdent(null);
        acctKeys.setAcctId(acctId);
    }


}
