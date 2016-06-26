package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.PartyIdent;
import com.fnis.ifx.xbo.v1_1.base.PartyIdentImpl;
import com.fnis.ifx.xbo.v1_1.base.PartyKeys;
import com.fnis.xes.framework.util.ListMapper;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * @author e1050475
 */
public class PartyKeysToCanonicalFromPartyId extends PartyKeysConverter {
    private static final Logger log = Logger.getLogger(PartyKeysToCanonicalFromPartyId.class);

    private static final String CANONICAL_IDENT_TYPE = "ProspectRef";
    private ListMapper mapper;
    public ListMapper getMapper() {
        return mapper;
    }

    public void setMapper(ListMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public PartyKeys convert(PartyKeysConversionContext partyKeysConversionContext, PartyKeys partyKeys) {
        return mapPartyId(partyKeys);
    }

    private PartyKeys mapPartyId(PartyKeys partyKeys) {
        if (partyKeys.getPartyId() != null) {
            if ("".equals(partyKeys.getPartyId().trim())) {
                log.debug("Could not map blank partyId");
                throw new ExternalLookupException("Could not map blank partyId");
            }
            String originalValue = partyKeys.getPartyId();
            log.debug("Found original partyId=" + originalValue);
            LinkedList<String> values = new LinkedList<String>();
            values.add(originalValue);
            List<String> results = mapper.map(values);
            if (results.size() == 1) {
                String mappedValue = results.get(0);
                log.debug("Substituting value of partyId=" + originalValue + " with " + mappedValue);
                PartyIdent partyIdent = new PartyIdentImpl();
                partyIdent.setPartyIdentType(CANONICAL_IDENT_TYPE);
                partyIdent.setPartyIdentValue(mappedValue);
                partyKeys.setPartyId(null);
                partyKeys.setPartyIdent(partyIdent);
            } else {
                log.debug("Could not find corresponding value for partyId=" + originalValue);
                throw new ExternalLookupException("Could not find corresponding value for partyId=" + originalValue);
            }
        }
        return partyKeys;
    }
}
