package com.fisglobal.xpress.emea.mblox;

public class BaseMbloxProtocol implements MbloxProtocol {
    private NotificationRequestMarshaller notificationRequestMarshaller;
    private NotificationRequestResultMarshaller notificationRequestResultMarshaller;

    public BaseMbloxProtocol(NotificationRequestTemplate notificationRequestTemplate) {
        if (Utils.isNull(notificationRequestTemplate))
            throw new IllegalArgumentException("notificationRequestTemplate is null");

        notificationRequestMarshaller = new NotificationRequestMarshaller(notificationRequestTemplate);
        notificationRequestResultMarshaller = new NotificationRequestResultMarshaller();
    }

    public String createNotificationRequestContent(NotificationRequestDetails notificationRequestDetails) {
        return notificationRequestMarshaller.marshallToString(notificationRequestDetails);
    }

    public NotificationRequestResultDetails createNotificationRequestResultDetails(String notificationRequestResultXmlStr) {
        NotificationRequestResultDetails notificationRequestResultDetails = null;
        try {
            notificationRequestResultDetails = notificationRequestResultMarshaller.unmarshall(notificationRequestResultXmlStr);
        } catch (Exception e) {
            notificationRequestResultDetails = new NotificationRequestResultDetails();
            notificationRequestResultDetails.setNotificationResultCode(500);
            notificationRequestResultDetails.setRequestResultCode(500);
        }
        return notificationRequestResultDetails;
    }
}
