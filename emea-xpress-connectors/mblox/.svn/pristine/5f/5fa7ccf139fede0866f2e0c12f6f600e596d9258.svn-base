package com.fisglobal.xpress.emea.mblox;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MbloxHttpClient implements MbloxClient {
    private static final Logger log = Logger.getLogger(MbloxHttpClient.class);

    private List<URI> mbloxUris;

    private RestTemplate restTemplate;

    public MbloxHttpClient() {
    }

    public HttpConnectionStatus sendNotificationRequest(String notificationRequestStr) {
        boolean connectionSuccess = false;
        boolean responseStatusSuccess = false;

        Integer responseStatusCode = null;
        String responseStatusText = null;
        String responseContent = null;
        Date timestamp = null;

        ResponseEntity<String> response = null;

        for (URI mbloxUri : mbloxUris) {
            try {
                log.debug("Sending request to Mblox server: " + mbloxUri.toString());
                timestamp = new Date();
                response = sendNotificationRequest(mbloxUri, notificationRequestStr);
                log.debug("Request successfuly sent to Mblox server: " + mbloxUri.toString());
                log.debug("Response from server " + response);

                connectionSuccess = true;
                responseStatusCode = response.getStatusCode().value();
                responseStatusText = response.getStatusCode().getReasonPhrase();
                responseContent = response.getBody();

                if ((responseStatusCode >= 200) && (responseStatusCode <= 299)) {
                    responseStatusSuccess = true;
                    log.debug("Response status: " + responseStatusCode);
                    break;    // OK, no problems occured
                } else {
                    log.warn("Mblox response status contains server error: " + responseStatusCode);
                }

            } catch (HttpClientErrorException httpException) {
                log.warn("Could not connect to Mblox server: " + mbloxUri.toString());
                responseStatusCode = httpException.getStatusCode().value();
                responseStatusText = httpException.getStatusCode().getReasonPhrase();
            } catch (Exception exp){
                responseStatusCode = 500;
                responseStatusText = "Unknown exception";
            }
            log.info("Trying next server...");
        }

        if (!connectionSuccess) {
            String msg = "Could not connect to any Mblox server!";
            log.error(msg);
        } else if (!responseStatusSuccess) {
            String msg = "All Mblox servers responded with server error!";
            log.error(msg);
        }

        HttpConnectionStatus connectionStatus = new HttpConnectionStatus();

        connectionStatus.setTimestamp(timestamp);
        connectionStatus.setConnectionSuccess(connectionSuccess);
        connectionStatus.setResponseStatusCode(responseStatusCode);
        connectionStatus.setResponseStatusText(responseStatusText);
        connectionStatus.setRequestContent(notificationRequestStr);
        connectionStatus.setResponseContent(responseContent);

        return connectionStatus;
    }

    ResponseEntity<String> sendNotificationRequest(URI mbloxUri, String notificationRequestStr) {
        LinkedMultiValueMap requestParamsMap = new LinkedMultiValueMap();
        requestParamsMap.put("XMLDATA", Collections.singletonList(notificationRequestStr));
        ResponseEntity<String> response = restTemplate.postForEntity(mbloxUri, requestParamsMap, String.class);
        return response;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<URI> getMbloxUris() {
        return mbloxUris;
    }

    public void setMbloxUris(List<URI> mbloxUris) {
        this.mbloxUris = mbloxUris;
    }

}
