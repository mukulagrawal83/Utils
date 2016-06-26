package com.fisglobal.xpress.jms;

/**
 *
 * @author trojanbug
 */
public class NullMessageSelectorProvider implements MessageSelectorProvider {

    public String getMessageSelector() {
        return null;
    }
    
}
