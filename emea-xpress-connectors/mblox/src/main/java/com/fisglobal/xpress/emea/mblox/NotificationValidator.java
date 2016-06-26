package com.fisglobal.xpress.emea.mblox;


import org.apache.commons.lang.StringUtils;

/**
 * @author e1050475
 */
public final class NotificationValidator {

    private NotificationValidator() {
    }


    public static void validateTextMessage(NotificationMessage textMessage) throws NotificationMessageException {
        if (StringUtils.isEmpty(textMessage.getMessage())) {
            throw new NotificationMessageException("notification.message.invalid", "Notification message is invalid");
         //TODO
        /*} else if (StringUtils.length(textMessage.getMessage()) > 140) {
            throw new NotificationMessageException("notification.message.length", "Notification message length is greater than allowed");
          //This is duplicated and not required
          */
        } else if (StringUtils.isEmpty(textMessage.getSenderId())) {
            throw new NotificationMessageException("notification.senderid.invalid", "Notification message sender ID is not defined");
        } else if (textMessage.getMessagePriority() < 0 || textMessage.getMessagePriority() > 9) {
            throw new NotificationMessageException("notification.priority.range", "Notification message priority is not in range");
        }
    }
}
