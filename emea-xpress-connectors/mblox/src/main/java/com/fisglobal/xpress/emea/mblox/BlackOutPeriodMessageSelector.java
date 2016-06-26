package com.fisglobal.xpress.emea.mblox;


import com.fisglobal.xpress.jms.MessageSelectorProvider;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

public class BlackOutPeriodMessageSelector implements MessageSelectorProvider {
    private static final Logger LOGGER = Logger.getLogger(BlackOutPeriodMessageSelector.class);
    private static final String BLANK_STRING = "";

    public String getMessageSelector() {
        String selector = BLANK_STRING;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Message selector is " + selector);
        }
        Date currentTime = Calendar.getInstance().getTime();
        selector = "NOT (" + currentTime.getTime() + "<" + NotificationMessage.BLACK_OUT_TO + " AND " + NotificationMessage.BLACK_OUT_FROM + " < " + currentTime.getTime() + ")";
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("message selector query based on dynamic time " + selector);
        }

        return selector;
    }
}
