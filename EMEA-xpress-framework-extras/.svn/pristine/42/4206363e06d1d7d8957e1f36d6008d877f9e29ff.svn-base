package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.AcctIdent;
import com.fnis.ifx.xbo.v1_1.base.AcctIdentImpl;
import com.fnis.ifx.xbo.v1_1.base.AcctKeys;
import com.fnis.xes.framework.util.ListMapper;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * User: lc29249
 * Date: 2/8/13
 */
public class AcctKeysToRefnumFromCanonical extends AcctKeysConverter {

    private static final String CUR_ACCT_IDENT_TYPE = "Refnum";
    private static final Logger log = Logger.getLogger(AcctKeysToRefnumFromCanonical.class);

    private ListMapper mapper;

    public ListMapper getMapper() {
        return mapper;
    }

    public void setMapper(ListMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public AcctKeys convert(AcctKeys acctKeys) {
        acctKeys = mapAcctId(acctKeys);
        return acctKeys;
    }

    private AcctKeys mapAcctId(AcctKeys acctKeys) {
        if (acctKeys.getAcctIdent() != null) {
            if ("".equals(acctKeys.getAcctIdent().getAcctIdentValue().trim())) {
                log.debug("Could not map blank AcctId");
                throw new ExternalLookupException("Could not map blank AcctId");
            }

            String originalValue = acctKeys.getAcctIdent().getAcctIdentValue().trim();
            log.debug("Found original AcctId=" + originalValue);
            LinkedList<String> values = new LinkedList<String>();
            values.add(originalValue);
            List<String> result = getMapper().map(values);
            if (result.size() == 1) {
                String refnum = result.get(0);

                StringBuilder mappedValue = new StringBuilder();
                mappedValue.append(refnum);

                log.debug("Substituting value of CID=" + originalValue + " with Refnum=" + mappedValue.toString());
                AcctIdent acctIdent = new AcctIdentImpl();
                acctIdent.setAcctIdentType(CUR_ACCT_IDENT_TYPE);
                acctIdent.setAcctIdentValue(mappedValue.toString());
                acctKeys.setAcctIdent(acctIdent);
            } else {
                log.debug("Could not find corresponding value for AcctId=" + originalValue);
                throw new ExternalLookupException("Could not find corresponding value for AcctId=" + originalValue);
            }
        }
        return acctKeys;
    }
}
