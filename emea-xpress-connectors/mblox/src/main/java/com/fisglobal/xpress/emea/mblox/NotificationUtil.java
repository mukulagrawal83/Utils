package com.fisglobal.xpress.emea.mblox;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

/**
 * @author e1050475
 */
public final class NotificationUtil {

    private NotificationUtil() {
    }

    public static Message<NotificationMessage> getMessage(NotificationMessage notificationMessage) {
        return MessageBuilder.withPayload(notificationMessage)
                .setPriority(notificationMessage.getMessagePriority())
                .build();
    }
}
