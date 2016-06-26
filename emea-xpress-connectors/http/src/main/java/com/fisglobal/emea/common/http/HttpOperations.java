package com.fisglobal.emea.common.http;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

public interface HttpOperations {

    /**
     * Use it to execute POST method with request body wrapped with HttpEntity
     * @param uri
     * @param entity
     * @param headers
     * @return
     * @throws HttpCustomClientException
     */
    HttpClientResponse submit(URI uri, HttpEntity entity, Header... headers) throws HttpCustomClientException;

}
