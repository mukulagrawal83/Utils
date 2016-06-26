package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.AcctIdent;
import com.fnis.ifx.xbo.v1_1.base.AcctIdentImpl;
import com.fnis.ifx.xbo.v1_1.base.FromAcctKeys;
import com.fnis.ifx.xbo.v1_1.base.ToAcctKeys;
import com.fnis.xes.framework.util.ListMapper;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: lc21878
 * Date: 2/8/13
 */
public class FromAcctKeysToSortCodeProfileCidFromSortCodeAcctNo extends FromAcctKeysConverter {

    private static final String CUR_ACCT_IDENT_TYPE = "SortCodeProfileCid";
    private static final Logger log = Logger.getLogger(FromAcctKeysToSortCodeProfileCidFromSortCodeAcctNo.class);
    private static final String SEPARATOR = "-";

    private String inputFormatRegex;
    private ListMapper mapper;

    public ListMapper getMapper() {
        return mapper;
    }

    public void setMapper(ListMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public FromAcctKeys convert(FromAcctKeys acctKeys) {
        acctKeys = mapAcctId(acctKeys);
        return acctKeys;
    }

    public String getInputFormatRegex() {
        return inputFormatRegex;
    }

    public void setInputFormatRegex(String inputFormatRegex) {
        this.inputFormatRegex = inputFormatRegex;
    }

    private FromAcctKeys mapAcctId(FromAcctKeys acctKeys) {
        if(acctKeys.getAcctIdent() != null) {
            if("".equals(acctKeys.getAcctIdent().getAcctIdentValue().trim())) {
                log.debug("Could not map blank AcctId");
                throw new ExternalLookupException("Could not map blank AcctId");
            }

            String originalValue = acctKeys.getAcctIdent().getAcctIdentValue().trim();
            log.debug("Found original AcctId=" + originalValue);
            LinkedList<String> values = new LinkedList<String>();

            try {
                Pattern p = Pattern.compile(inputFormatRegex);
                Matcher m = p.matcher(originalValue);
                if (m.matches()) {
                    values.add(m.group(1));
                    values.add(m.group(2));
                } else {
                    throw new ExternalLookupException("Could not find corresponding value for AcctIdentValue=" + originalValue);
                }
            } catch (Exception e) {
                throw new ExternalLookupException("Could not find corresponding value for AcctIdentValue=" + originalValue);
            }

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
