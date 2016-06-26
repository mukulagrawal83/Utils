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
public class PartyIdToProfileFromCanonical extends PartyKeysConverter {

    private static final String CANONICAL_IDENT_TYPE = "Profile";
    private static final Logger log = Logger.getLogger(PartyIdToProfileFromCanonical.class);

    private ListMapper mapper;

    @Override
    public PartyKeys convert(PartyKeysConversionContext partyKeysConversionContext, PartyKeys partyKeys) {
        partyKeys = mapPartyId(partyKeysConversionContext, partyKeys);
        return partyKeys;
    }

    public ListMapper getMapper() {
        return mapper;
    }

    public void setMapper(ListMapper mapper) {
        this.mapper = mapper;
    }

    private PartyKeys mapPartyId(PartyKeysConversionContext partyKeysConversionContext, PartyKeys partyKeys) {
        if(partyKeys.getPartyIdent() != null) {
            if("".equals(partyKeys.getPartyIdent().getPartyIdentValue().trim())) {
                log.debug("Could not map blank PartyId");
                throw new ExternalLookupException("Could not map blank PartyId");
            }
            String originalValue = partyKeys.getPartyIdent().getPartyIdentValue().trim();
            log.debug("Found original PartyId=" + originalValue);
            LinkedList<String> values = new LinkedList<String>();
            values.add(originalValue);
            List<String> results = mapper.map(values);
            if(results.size() == 1) {
                String mappedValue = results.get(0);
                log.debug("Substituting value of PartyId=" + originalValue + " with " + mappedValue);
                PartyIdent partyIdent = new PartyIdentImpl();
                if(partyKeysConversionContext.getIncomingPartyKeysFormat().equals(PartyKeysFormatType.EMPTY) && partyKeysConversionContext.isIdentTypePresent())
                {
                    partyIdent.setPartyIdentType("");
                }else if(partyKeysConversionContext.getIncomingPartyKeysFormat().equals(PartyKeysFormatType.PROFILE)){
                    partyIdent.setPartyIdentType(CANONICAL_IDENT_TYPE);
                }
                partyIdent.setPartyIdentValue(mappedValue);
                partyKeys.setPartyIdent(partyIdent);
                partyKeys.setPartyId("");
            } else {
                log.debug("Could not find corresponding value for PartyId=" + originalValue);
                throw new ExternalLookupException("Could not find corresponding value for PartyId=" + originalValue);
            }
        }
        return partyKeys;
    }
}
