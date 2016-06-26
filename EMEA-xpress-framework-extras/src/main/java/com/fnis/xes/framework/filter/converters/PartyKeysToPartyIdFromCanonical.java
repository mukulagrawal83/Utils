package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.*;
import com.fnis.xes.framework.util.ListMapper;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * @author e1050475
 */
public class PartyKeysToPartyIdFromCanonical extends PartyKeysConverter {

    private static final Logger log = Logger.getLogger(PartyKeysToPartyIdFromCanonical.class);

    private ListMapper mapper;

    @Override
    public PartyKeys convert(PartyKeysConversionContext partyKeysConversionContext, PartyKeys partyKeys) {
        return mapPartyId(partyKeys);
    }

    public ListMapper getMapper() {
        return mapper;
    }

    public void setMapper(ListMapper mapper) {
        this.mapper = mapper;
    }

    private PartyKeys mapPartyId(PartyKeys partyKeys) {
        if (partyKeys.getPartyIdent() != null) {
            if ("".equals(partyKeys.getPartyIdent().getPartyIdentValue().trim())) {
                log.debug("Could not map blank partyId");
                throw new ExternalLookupException("Could not map blank partyId");
            }
            String originalValue = partyKeys.getPartyIdent().getPartyIdentValue();
            log.debug("Found original partyId=" + originalValue);
            LinkedList<String> values = new LinkedList<String>();
            values.add(originalValue);
            List<String> results = mapper.map(values);
            if (results.size() == 1) {
                String mappedValue = results.get(0);
                log.debug("Substituting value of partyId=" + originalValue + " with " + mappedValue);
                partyKeys.setPartyIdent(null);
                partyKeys.setPartyId(mappedValue);
            } else {
                log.debug("Could not find corresponding value for partyId=" + originalValue);
                throw new ExternalLookupException("Could not find corresponding value for partyId=" + originalValue);
            }
        }
        return partyKeys;
    }


}
