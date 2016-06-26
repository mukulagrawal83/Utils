package com.fisglobal.xpress.emea.mblox;

public class NotificationRequestResultDetails {
    // Results related to the processing of the header information submitted in Notification Request
    private Integer requestResultCode;
    private String requestResultText;

    // Result information associated with the <Notification> element in submitted Notification Request.
    private Integer notificationResultCode;    // A numeric code indicating the result of a Notification Request
    private String notificationResultText; // explanation of the code

    private Integer subscriberResultCode;    // code which identifies the subscriber-related result
    private String subscriberResultText;

    private Boolean retry; // specifies whether the submission of this message should be tried again

    public int getRequestResultCode() {
        return requestResultCode;
    }

    public void setRequestResultCode(Integer requestResultCode) {
        this.requestResultCode = requestResultCode;
    }

    public String getRequestResultText() {
        return requestResultText;
    }

    public void setRequestResultText(String requestResultText) {
        this.requestResultText = requestResultText;
    }

    public int getNotificationResultCode() {
        return notificationResultCode;
    }

    public void setNotificationResultCode(Integer notificationResultCode) {
        this.notificationResultCode = notificationResultCode;
    }

    public String getNotificationResultText() {
        return notificationResultText;
    }

    public void setNotificationResultText(String notificationResultText) {
        this.notificationResultText = notificationResultText;
    }

    public int getSubscriberResultCode() {
        return subscriberResultCode;
    }

    public void setSubscriberResultCode(Integer subscriberResultCode) {
        this.subscriberResultCode = subscriberResultCode;
    }

    public String getSubscriberResultText() {
        return subscriberResultText;
    }

    public void setSubscriberResultText(String subscriberResultText) {
        this.subscriberResultText = subscriberResultText;
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(Boolean retry) {
        this.retry = retry;
    }

    @Override
    public String toString() {
        return "NotificationRequestResultDetails [\nrequestResultCode="
                + requestResultCode + ",\nrequestResultText="
                + requestResultText + ",\nnotificationResultCode="
                + notificationResultCode + ",\nnotificationResultText="
                + notificationResultText + ",\nsubscriberResultCode="
                + subscriberResultCode + ",\nsubscriberResultText="
                + subscriberResultText + ",\nretry=" + retry + "]";
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + notificationResultCode;
        result = prime
                * result
                + ((notificationResultText == null) ? 0
                : notificationResultText.hashCode());
        result = prime * result + requestResultCode;
        result = prime
                * result
                + ((requestResultText == null) ? 0 : requestResultText
                .hashCode());
        result = prime * result + (retry ? 1231 : 1237);
        result = prime * result + subscriberResultCode;
        result = prime
                * result
                + ((subscriberResultText == null) ? 0 : subscriberResultText
                .hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NotificationRequestResultDetails other = (NotificationRequestResultDetails) obj;
        if (!notificationResultCode.equals(other.notificationResultCode))
            return false;
        if (notificationResultText == null) {
            if (other.notificationResultText != null)
                return false;
        } else if (!notificationResultText.equals(other.notificationResultText))
            return false;
        if (!requestResultCode.equals(other.requestResultCode))
            return false;
        if (requestResultText == null) {
            if (other.requestResultText != null)
                return false;
        } else if (!requestResultText.equals(other.requestResultText))
            return false;
        if (retry != other.retry)
            return false;
        if (!subscriberResultCode.equals(other.subscriberResultCode))
            return false;
        if (subscriberResultText == null) {
            if (other.subscriberResultText != null)
                return false;
        } else if (!subscriberResultText.equals(other.subscriberResultText))
            return false;
        return true;
    }
}
