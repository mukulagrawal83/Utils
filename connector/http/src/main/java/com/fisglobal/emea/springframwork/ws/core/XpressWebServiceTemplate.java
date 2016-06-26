package com.fisglobal.emea.springframwork.ws.core;

import org.apache.http.client.HttpClient;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

/**
 * The wrapper class for {@link WebServiceTemplate} Objective it to create a
 * simple to use implementation with {@link HttpClient} which can be configured
 * to use keyStore, truststore and proxy configuration.
 * 
 * See ifx2_spring_http.xml for Spring configuration
 */
public class XpressWebServiceTemplate extends WebServiceTemplate {

    private HttpClient httpClient;

    public XpressWebServiceTemplate(HttpClient httpClient) {
        super();
        this.httpClient = httpClient;
        initDefaultMessageSenders();
    }

    public XpressWebServiceTemplate(WebServiceMessageFactory messageFactor, HttpClient httpClient) {
        super(messageFactor);
        this.httpClient = httpClient;
        initDefaultMessageSenders();
    }

    private void initDefaultMessageSenders() {
        HttpComponentsMessageSender defaultMessageSender = new HttpComponentsMessageSender(getHttpClient());
        setMessageSender(defaultMessageSender);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
    

}
