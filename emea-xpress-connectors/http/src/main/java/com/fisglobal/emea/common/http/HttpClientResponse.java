package com.fisglobal.emea.common.http;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Locale;

public class HttpClientResponse {
    
    private final int statusCode;
    
    private final StatusLine statusLine;
    
    private final String responseBody;
    
    private final Header[] headers;
    
    private final Locale locale;
    
    public HttpClientResponse(HttpResponse response) throws IOException {
        this.statusCode = response.getStatusLine().getStatusCode();
        this.statusLine = response.getStatusLine();
        this.responseBody = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
        this.headers = response.getAllHeaders();
        this.locale = response.getLocale();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public Locale getLocale() {
        return locale;
    }
    
}
