package com.fnis.xes.framework.filter.converters;

/**
 * @author e1050475
 */
public enum PartyKeysFormatType {
    ID,
    // PartyIdent/[PartyIdentType=Profile]
    PROFILE,
    // PartyIdent/[PartyIdentType=Profile]
    IDENT_VALUE_PROFILE,
    // PartyIdent/[PartyIdentType=RGB]
    CANONICAL,
    //meta type - ident type empty
    EMPTY,
    // meta type - groups recognized but non-modifiable types
    OTHER,
    // meta type - other groups which are not be handled (usually generate TransformError Exception)
    UNKNOWN
}
