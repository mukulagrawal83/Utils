package com.fisglobal.xpress.emea.mblox;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class NotificationRequestBuilder {

    private static Logger logger = Logger.getLogger("com.fisglobal.xpress.emea.sms.mblox");

    private String notificationRequestStr;

    public NotificationRequestBuilder(NotificationRequestTemplate notificationRequestTemplate) {
        if (Utils.isNull(notificationRequestTemplate)) {
            throw new IllegalArgumentException("notificationRequestTemplate is null");
        }

        notificationRequestStr = notificationRequestTemplate.getTemplate();
    }

    public NotificationRequestBuilder withPartnerName(String partnerName) {
        if (StringUtils.isBlank(partnerName)) {
            throw new IllegalArgumentException("partnerName is blank");
        }

        notificationRequestStr = notificationRequestStr.replace(NotificationRequestTemplate.PARTNER_NAME_PLACEHOLDER, partnerName);
        return this;
    }

    public NotificationRequestBuilder withPartnerPassword(String partnerPassword) {
        if (StringUtils.isBlank(partnerPassword)) {
            throw new IllegalArgumentException("partnerPassword is blank");
        }

        notificationRequestStr = notificationRequestStr.replace(NotificationRequestTemplate.PARTNER_PASSWORD_PLACEHOLDER, partnerPassword);
        return this;
    }

    public NotificationRequestBuilder withMessageId(String messageId) {
        if (StringUtils.isBlank(messageId)) {
            throw new IllegalArgumentException("messageId is blank");
        }

        notificationRequestStr = notificationRequestStr.replace(NotificationRequestTemplate.MESSAGE_ID_PLACEHOLDER, messageId);
        return this;
    }

    public NotificationRequestBuilder withMessage(String message) {
        if (StringUtils.isBlank(message)) {
            throw new IllegalArgumentException("message is blank");
        }

        notificationRequestStr = notificationRequestStr.replace(NotificationRequestTemplate.MESSAGE_PLACEHOLDER, "<![CDATA["+message+"]]>");
        return this;
    }

    public NotificationRequestBuilder withProfileId(String profileId) {
        if (StringUtils.isBlank(profileId)) {
            throw new IllegalArgumentException("profileId is blank");
        }

        notificationRequestStr = notificationRequestStr.replace(NotificationRequestTemplate.PROFILEID_PLACEHOLDER, profileId);
        return this;
    }

    public NotificationRequestBuilder withSenderId(String senderId) {
        if (StringUtils.isBlank(senderId)) {
            throw new IllegalArgumentException("senderId is blank");
        }

        notificationRequestStr = notificationRequestStr.replace(NotificationRequestTemplate.SENDER_ID_PLACEHOLDER, senderId);
        return this;
    }

    public NotificationRequestBuilder withSubscriberNumber(String subscriberNumber) {
        notificationRequestStr = notificationRequestStr.replace(NotificationRequestTemplate.SUBSCRIBER_NUMBER_PLACEHOLDER, subscriberNumber);
        return this;
    }

    public String asString() {
        return notificationRequestStr;
    }
}
