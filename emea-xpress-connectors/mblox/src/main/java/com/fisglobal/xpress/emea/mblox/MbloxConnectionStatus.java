package com.fisglobal.xpress.emea.mblox;

import java.util.Date;

public interface MbloxConnectionStatus {
    Date getTimestamp();

    boolean isConnectionSuccess();

    Integer getResponseStatusCode();

    String getResponseStatusText();
}