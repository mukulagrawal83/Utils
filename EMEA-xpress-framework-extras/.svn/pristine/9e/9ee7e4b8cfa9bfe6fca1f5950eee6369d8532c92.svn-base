package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.AcctIdent;
import com.fnis.ifx.xbo.v1_1.base.AcctIdentImpl;
import com.fnis.ifx.xbo.v1_1.base.ToAcctKeys;
import com.fnis.xes.framework.util.ListMapper;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * User: lc29249
 * Date: 2/8/13
 */
public class ToAcctKeysToSortCodeAcctNoFromProfile extends ToAcctKeysConverter {

    private static final String CUR_ACCT_IDENT_TYPE = "SortCodeProfileCid";
    private static final Logger log = Logger.getLogger(ToAcctKeysToSortCodeAcctNoFromProfile.class);
    private static final String SEPARATOR = "-";

    private ListMapper mapper;

    public ListMapper getMapper() {
        return mapper;
    }

    public void setMapper(ListMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ToAcctKeys convert(ToAcctKeys acctKeys) {
        acctKeys = mapAcctId(acctKeys);
        return acctKeys;
    }

    private ToAcctKeys mapAcctId(ToAcctKeys acctKeys) {
        if(acctKeys.getAcctIdent() != null) {
            if("".equals(acctKeys.getAcctIdent().getAcctIdentValue().trim())) {
                log.debug("Could not map blank AcctId");
                throw new ExternalLookupException("Could not map blank AcctId");
            }

            String originalValue = acctKeys.getAcctIdent().getAcctIdentValue().trim();
            log.debug("Found original AcctId=" + originalValue);
            LinkedList<String> values = new LinkedList<String>();
            values.add(originalValue);
            List<String> result = getMapper().map(values);
            if(result.size() == 2) {
                String sortcode = result.get(0);
                String acctno = result.get(1);

                StringBuilder mappedValue = new StringBuilder();
                mappedValue.append(sortcode);
                mappedValue.append(SEPARATOR);
                mappedValue.append(acctno);

                log.debug("Substituting value of CID=" + originalValue + " with " + mappedValue.toString());
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
