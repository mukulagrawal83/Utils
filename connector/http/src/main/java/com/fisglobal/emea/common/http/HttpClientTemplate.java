package com.fisglobal.emea.common.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.net.URI;


public class HttpClientTemplate implements HttpOperations {

    private final HttpClientConfig config;
    private final CloseableHttpClient httpClient;

    public HttpClientTemplate(HttpClientConfig config) throws HttpCustomClientException {
        this.config = config;
        httpClient = HttpClientFactory.getInstance().buildHttpClient(config);
    }

    public HttpClientResponse submit(URI uri, HttpEntity entity, Header... headers) throws HttpCustomClientException {
        HttpPost httpPost = new HttpPost(uri);

        if (config.getMaxConnectionIdleTime() == 0) {
            httpPost.addHeader(HttpHeaders.CONNECTION, "close");
        }
        
        if (StringUtils.isNotBlank(config.getAcceptEncoding())) {
            httpPost.addHeader(HttpHeaders.ACCEPT_ENCODING, config.getAcceptEncoding());
        }
        
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, config.getContentType());

        for(Header header : headers){
            httpPost.addHeader(header);
        }
        
        httpPost.setEntity(entity);
        
        try {
            HttpClientResponse response = httpClient.execute(httpPost, getResponseHandler());
            
            return response;
        } catch (ClientProtocolException ex) {
            throw new HttpCustomClientException(ex);
        } catch (IOException ex) {
            throw new HttpCustomClientException(ex);
        } finally{
            httpPost.releaseConnection();
        } 
        
    }

    private ResponseHandler<HttpClientResponse> getResponseHandler() {
        return new ResponseHandler<HttpClientResponse>() {

            @Override
            public HttpClientResponse handleResponse(HttpResponse response) throws IOException {
                return new HttpClientResponse(response);
            }
        };
    }

}
