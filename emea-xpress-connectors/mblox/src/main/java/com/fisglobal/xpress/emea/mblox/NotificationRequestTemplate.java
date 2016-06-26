package com.fisglobal.xpress.emea.mblox;

public interface NotificationRequestTemplate {

    String
            PARTNER_NAME_PLACEHOLDER = "${partnerName}",
            PARTNER_PASSWORD_PLACEHOLDER = "${partnerPassword}",
            MESSAGE_ID_PLACEHOLDER = "${messageId}",
            MESSAGE_PLACEHOLDER = "${message}",
            PROFILEID_PLACEHOLDER = "${profileId}",
            SENDER_ID_PLACEHOLDER = "${senderId}",
            SUBSCRIBER_NUMBER_PLACEHOLDER = "${subscriberNumber}";

    String getTemplate();
}
