package com.fisglobal.xpress.emea.mblox;

import com.fisglobal.xpress.emea.mblox.MbloxConnectionStatus;

import java.util.Date;

public class HttpConnectionStatus implements MbloxConnectionStatus {
    private Date timestamp;
    private boolean connectionSuccess;
    private Integer responseStatusCode;
    private String responseStatusText;
    private String requestContent;
    private String responseContent;

    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = new Date(timestamp.getTime());
    }

    public boolean isConnectionSuccess() {
        return connectionSuccess;
    }

    public void setConnectionSuccess(boolean connectionSuccess) {
        this.connectionSuccess = connectionSuccess;
    }

    public Integer getResponseStatusCode() {
        return responseStatusCode;
    }

    public void setResponseStatusCode(Integer responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public String getResponseStatusText() {
        return responseStatusText;
    }

    public void setResponseStatusText(String responseStatusText) {
        this.responseStatusText = responseStatusText;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    @Override
    public String toString() {
        return "ServerConnectionStatus [timestamp=" + timestamp
                + ", connectionSuccess=" + connectionSuccess
                + ", responseStatusCode=" + responseStatusCode
                + ", responseStatusText=" + responseStatusText
                + ", requestContent=" + requestContent + ", responseContent="
                + responseContent + "]";
    }
}
