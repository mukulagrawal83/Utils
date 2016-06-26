package com.fisglobal.xpress.emea.mblox;

import org.apache.log4j.Logger;

/**
 * @author e1050475
 */
public class MBloxReseponseProcessor {

    private static final Logger LOGGER = Logger.getLogger(MBloxReseponseProcessor.class);

    public void processResponse(NotificationStatus status) {

        LOGGER.debug("Response got for status " + status.getNotificationDetails().getMessageId());
        if (status.getMbloxConnectionStatus().getResponseStatusCode() != 200) {
            LOGGER.debug("Response is not success for message ID " + status.getNotificationDetails().getMessageId());
        }
    }
}
