package com.fisglobal.xpress.emea.mblox;



// refactor to mbloxProtocol ???

import org.apache.log4j.Logger;

public class NotificationRequestMarshaller {
    private static final Logger log = Logger.getLogger(NotificationRequestMarshaller.class);

    private NotificationRequestTemplate notificationRequestTemplate;

    public NotificationRequestMarshaller(NotificationRequestTemplate notificationRequestTemplate) {
        if (Utils.isNull(notificationRequestTemplate))
            throw new IllegalArgumentException("notificationRequestTemplate is null");

        this.notificationRequestTemplate = notificationRequestTemplate;
    }

    public String marshallToString(NotificationRequestDetails notificationRequestDetails) {

        if (Utils.isNull(notificationRequestDetails))
            throw new IllegalArgumentException("notificationRequestDetails is null");

        NotificationHeaderDetails notificationHeaderDetails = notificationRequestDetails.getNotificationHeaderDetails();
        NotificationDetails notificationDetails = notificationRequestDetails.getNotificationDetails();

        NotificationRequestBuilder builder = new NotificationRequestBuilder(notificationRequestTemplate)
                .withPartnerName(notificationHeaderDetails.getPartnerName())
                .withPartnerPassword(notificationHeaderDetails.getPartnerPassword())
                .withMessageId(notificationDetails.getMessageId())
                .withMessage(notificationDetails.getMessage())
                .withProfileId(notificationHeaderDetails.getProfileId())
                .withSenderId(notificationDetails.getSenderId())
                .withSubscriberNumber(notificationDetails.getSubscriberNumber());

        String notificationRequestStr = builder.asString();

        log.debug("Marshalled notificationRequestDetails:\n" + notificationRequestStr);

        return notificationRequestStr;
    }
}
