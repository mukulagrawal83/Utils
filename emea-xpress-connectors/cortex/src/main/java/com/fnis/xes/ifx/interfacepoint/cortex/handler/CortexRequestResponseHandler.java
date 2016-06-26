package com.fnis.xes.ifx.interfacepoint.cortex.handler;

import org.jdom2.Element;

/**
 * This program contains trade secrets that belong to Fidelity Information
 * Services, Inc. and is licensed by an agreement.  Any unauthorized access,
 * use, duplication, or disclosure is unlawful.
 * <p/>
 * Copyright (c) Fidelity Information Services, Inc.
 * 2006, All right reserved.
 * <p/>
 * User: Satheesh Kumar G - e1011705
 * Date: 04/03/14
 * Time: 17:48
 */
public interface CortexRequestResponseHandler<RequestType, ResponseType> {
    public ResponseType process(Element element) throws Exception;
    public void setRequest(RequestType request);
    public RequestType getRequest();
    public void setURL(String url);
    public String getURL();
}
