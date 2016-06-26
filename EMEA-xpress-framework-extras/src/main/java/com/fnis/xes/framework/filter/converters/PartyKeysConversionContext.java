package com.fnis.xes.framework.filter.converters;

/**
 * @author e1050475
 */
public class PartyKeysConversionContext {
    private PartyKeysFormatType incomingPartyKeysFormat;
    private ContextMessageType messageType;
    private boolean IdentTypePresent=true;

    public PartyKeysConversionContext() {
    }

    public PartyKeysFormatType getIncomingPartyKeysFormat() {
        return incomingPartyKeysFormat;
    }

    public void setIncomingPartyKeysFormat(PartyKeysFormatType incomingPartyKeysFormat) {
        if(this.incomingPartyKeysFormat != null) {
            throw new IllegalStateException("Trying to set already set incoming PartyKeys format type context.");
        }
        this.incomingPartyKeysFormat = incomingPartyKeysFormat;
    }

    public ContextMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(ContextMessageType messageType) {
        this.messageType = messageType;
    }

    public boolean isIdentTypePresent() {
        return IdentTypePresent;
    }

    public void setIdentTypePresent(boolean identTypePresent) {
        IdentTypePresent = identTypePresent;
    }
}
