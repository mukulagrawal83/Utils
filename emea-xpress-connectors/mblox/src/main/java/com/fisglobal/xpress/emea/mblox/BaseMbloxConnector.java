package com.fisglobal.xpress.emea.mblox;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class BaseMbloxConnector implements MbloxMessaging {

    private static final Logger log = Logger.getLogger(BaseMbloxConnector.class);

    private MbloxProtocol mbloxProtocol;
    private MbloxClient mbloxClient;
    private MbloxCredentials mbloxCredentials;

    @Override
    public NotificationStatus sendNotification(NotificationDetails notificationDetails) {
        if (Utils.isNull(notificationDetails)) throw new IllegalArgumentException("notificationDetails is null");

        HttpConnectionStatus connectionStatus = sendNotificationRequestToMblox(notificationDetails);
        String notificationRequestResultContent = connectionStatus.getResponseContent();

        NotificationRequestResultDetails notificationRequestResultDetails = null;

        if (StringUtils.isNotBlank(notificationRequestResultContent)) {
            notificationRequestResultDetails = mbloxProtocol.createNotificationRequestResultDetails(notificationRequestResultContent);
        }

        NotificationStatus status = new NotificationStatus(connectionStatus,
                notificationDetails,
                notificationRequestResultDetails);
        return status;
    }

    HttpConnectionStatus sendNotificationRequestToMblox(NotificationDetails notificationDetails) {
        NotificationRequestDetails notificationRequestDetails = createNotificationRequestDetails(notificationDetails);
        String notificationRequestContent = mbloxProtocol.createNotificationRequestContent(notificationRequestDetails);
        HttpConnectionStatus connectionStatus = mbloxClient.sendNotificationRequest(notificationRequestContent);

        return connectionStatus;
    }

    NotificationRequestDetails createNotificationRequestDetails(NotificationDetails notificationDetails) {
        return new NotificationRequestDetails(notificationHeaderDetailsInstance(), notificationDetails);
    }

    NotificationHeaderDetails notificationHeaderDetailsInstance() {
        NotificationHeaderDetails notificationHdrDetails = new NotificationHeaderDetails();
        notificationHdrDetails.setPartnerName(mbloxCredentials.getPartnerName());
        notificationHdrDetails.setPartnerPassword(mbloxCredentials.getPartnerPassword());
        notificationHdrDetails.setProfileId(mbloxCredentials.getProfileId());

        return notificationHdrDetails;
    }

    public MbloxProtocol getMbloxProtocol() {
        return mbloxProtocol;
    }

    public void setMbloxProtocol(MbloxProtocol mbloxProtocol) {
        this.mbloxProtocol = mbloxProtocol;
    }

    public MbloxClient getMbloxClient() {
        return mbloxClient;
    }

    public void setMbloxClient(MbloxClient mbloxClient) {
        this.mbloxClient = mbloxClient;
    }

    public MbloxCredentials getMbloxCredentials() {
        return mbloxCredentials;
    }

    public void setMbloxCredentials(MbloxCredentials mbloxCredentials) {
        this.mbloxCredentials = mbloxCredentials;
    }
}
