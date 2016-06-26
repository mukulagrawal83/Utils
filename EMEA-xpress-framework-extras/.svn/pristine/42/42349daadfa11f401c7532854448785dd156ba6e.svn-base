package com.fnis.xes.framework.filter.converters;

public class FromAcctKeysConversionContext {
    private AcctKeysFormatType incomingAcctKeysFormat;
    private ContextMessageType messageType;

    public FromAcctKeysConversionContext() {
    }

    public AcctKeysFormatType getIncomingAcctKeysFormat() {
        return incomingAcctKeysFormat;
    }

    public void setIncomingAcctKeysFormat(AcctKeysFormatType incomingAcctKeysFormat) {
        if(this.incomingAcctKeysFormat != null) {
            throw new IllegalStateException("Trying to set already set incoming AcctKeys format type context.");
        }
        this.incomingAcctKeysFormat = incomingAcctKeysFormat;
    }

    public ContextMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(ContextMessageType messageType) {
        this.messageType = messageType;
    }
}
