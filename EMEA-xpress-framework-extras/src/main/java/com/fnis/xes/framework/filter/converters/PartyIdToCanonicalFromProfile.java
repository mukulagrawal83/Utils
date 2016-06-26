package com.fnis.xes.framework.filter.converters;

import com.fnis.ifx.xbo.v1_1.base.PartyIdent;
import com.fnis.ifx.xbo.v1_1.base.PartyIdentImpl;
import com.fnis.ifx.xbo.v1_1.base.PartyKeys;
import com.fnis.xes.framework.util.ListMapper;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class PartyIdToCanonicalFromProfile extends PartyKeysConverter {
    private static final String CANONICAL_IDENT_TYPE = "ProspectRef";
    private static final Logger log = Logger.getLogger(PartyIdToCanonicalFromProfile.class);

    private ListMapper mapper;

    @Override
    public PartyKeys convert(PartyKeysConversionContext partyKeysConversionContext, PartyKeys partyKeys) {
        partyKeys = mapPartyId(partyKeys);
        partyKeys = convertPartyIdToIdent(partyKeys);
        return partyKeys;
    }

    public ListMapper getMapper() {
        return mapper;
    }

    public void setMapper(ListMapper mapper) {
        this.mapper = mapper;
    }

    private PartyKeys mapPartyId(PartyKeys partyKeys) {
        if(partyKeys.getPartyId() != null && !"".equals(partyKeys.getPartyId().trim())) {
            String originalValue = partyKeys.getPartyId();
            log.debug("Found original PartyId=" + originalValue);
            LinkedList<String> values = new LinkedList<String>();
            values.add(originalValue);
            List<String> results = mapper.map(values);
            if(results.size() == 1) {
                String mappedValue = results.get(0);
                log.debug("Substituting value of PartyId=" + originalValue + " with " + mappedValue);
                partyKeys.setPartyId(mappedValue);
            } else {
                log.debug("Could not find corresponding value for PartyId=" + originalValue);
                throw new ExternalLookupException("Could not find corresponding value for PartyId=" + originalValue);
            }
        }else if (partyKeys.getPartyIdent()!=null)
        {
            if("".equals(partyKeys.getPartyIdent().getPartyIdentValue().trim())) {
                log.debug("Could not map blank PartyId");
                throw new ExternalLookupException("Could not map blank PartyId");
            }
            String originalValue = partyKeys.getPartyIdent().getPartyIdentValue();
            log.debug("Found original PartyId=" + originalValue);
            LinkedList<String> values = new LinkedList<String>();
            values.add(originalValue);
            List<String> results = mapper.map(values);
            if(results.size() == 1) {
                String mappedValue = results.get(0);
                log.debug("Substituting value of PartyId=" + originalValue + " with " + mappedValue);
                partyKeys.setPartyId(mappedValue);
            } else {
                log.debug("Could not find corresponding value for PartyId=" + originalValue);
                throw new ExternalLookupException("Could not find corresponding value for PartyId=" + originalValue);
            }
        }
        return partyKeys;
    }

    private PartyKeys convertPartyIdToIdent(PartyKeys partyKeys) {
        String partyId = partyKeys.getPartyId();

        PartyIdent partyIdent = new PartyIdentImpl();
        partyIdent.setPartyIdentType(CANONICAL_IDENT_TYPE);
        partyIdent.setPartyIdentValue(partyId);
        setPartyIdentAsKey(partyKeys, partyIdent);
        return partyKeys;
    }

    private void setPartyIdentAsKey(PartyKeys partyKeys, PartyIdent partyIdent) {
        partyKeys.setPartyIdent(partyIdent);
        partyKeys.setPartyId(null);
    }
}
