package com.fisglobal.xpress.jms;

/**
 *
 * @author trojanbug
 */
public class StaticMessageSelectorProvider implements MessageSelectorProvider {
    
    private String messageSelector;

    public String getMessageSelector() {
        return messageSelector;
    }

    public void setMessageSelector(String messageSelector) {
        this.messageSelector = messageSelector;
    }
    
}
