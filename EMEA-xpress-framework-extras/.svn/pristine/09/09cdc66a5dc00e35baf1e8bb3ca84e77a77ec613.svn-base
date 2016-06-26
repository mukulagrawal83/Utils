package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.AcctIdent;
import com.fnis.ifx.xbo.v1_1.base.AcctIdentImpl;
import com.fnis.ifx.xbo.v1_1.base.AcctKeys;
import com.fnis.ifx.xbo.v1_1.base.ToAcctKeys;
import com.fnis.xes.framework.util.ListMapper;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: lc29249
 * Date: 2/6/13
 */

public class ToAcctKeysToProfileFromSortCodeAcctNo extends ToAcctKeysConverter {

    private static final String CANONICAL_IDENT_TYPE = "Profile";
    private static final Logger log = Logger.getLogger(ToAcctKeysToProfileFromSortCodeAcctNo.class);

    private ListMapper mapper;
    private String inputFormatRegex;

    public ListMapper getMapper() {
        return mapper;
    }

    public void setMapper(ListMapper mapper) {
        this.mapper = mapper;
    }

	public String getInputFormatRegex() {
        return inputFormatRegex;
    }

    public void setInputFormatRegex(String inputFormatRegex) {
        this.inputFormatRegex = inputFormatRegex;
    }

    @Override
    public ToAcctKeys convert(ToAcctKeys acctKeys) {
        return mapCurAcctData(acctKeys);
    }

    private ToAcctKeys mapCurAcctData(ToAcctKeys acctKeys) {

        if (acctKeys.getAcctIdent() != null) {
            if ("".equals(acctKeys.getAcctIdent().getAcctIdentValue().trim())) {
                log.debug("Could not map blank AcctId");
                throw new ExternalLookupException("Could not map blank AcctId");
            }

            String originalValue = acctKeys.getAcctIdent().getAcctIdentValue().trim();
            log.debug("Found original DATA =" + originalValue);

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

            List<String> results = mapper.map(values);
            if (results.size() == 1) {
                String mappedValue = results.get(0);
                log.debug("Substituting value of DATA=" + originalValue + " with " + mappedValue);
                AcctIdent acctIdent = new AcctIdentImpl();
                acctIdent.setAcctIdentType(CANONICAL_IDENT_TYPE);
                acctIdent.setAcctIdentValue(mappedValue);
                acctKeys.setAcctIdent(acctIdent);
            } else {
                String message = "Could not find corresponding value for AcctIdentValue="+originalValue;
                log.debug(message.toString());
                throw new ExternalLookupException(message.toString());
            }
        }

        return acctKeys;
    }
}
