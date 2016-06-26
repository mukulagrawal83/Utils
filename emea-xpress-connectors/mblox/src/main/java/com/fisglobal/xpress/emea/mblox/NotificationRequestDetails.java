package com.fisglobal.xpress.emea.mblox;

public class NotificationRequestDetails {
    private NotificationHeaderDetails notificationHeaderDetails;
    private NotificationDetails notificationDetails;

    public NotificationRequestDetails(NotificationHeaderDetails notificationHeaderDetails,
                                      NotificationDetails notificationDetails) {

        if (Utils.isNull(notificationHeaderDetails))
            throw new IllegalArgumentException("notificationHeaderDetails is null");
        if (Utils.isNull(notificationDetails)) throw new IllegalArgumentException("notificationDetails is null");

        this.notificationHeaderDetails = notificationHeaderDetails;
        this.notificationDetails = notificationDetails;
    }

    public NotificationHeaderDetails getNotificationHeaderDetails() {
        return notificationHeaderDetails;
    }

    public NotificationDetails getNotificationDetails() {
        return notificationDetails;
    }
}
