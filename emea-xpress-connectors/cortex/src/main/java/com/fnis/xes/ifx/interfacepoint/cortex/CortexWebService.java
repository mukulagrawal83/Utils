package com.fnis.xes.ifx.interfacepoint.cortex;

import com.fnis.xes.ifx.interfacepoint.cortex.handler.CortexRequestResponseHandler;

/**
 * This program contains trade secrets that belong to Fidelity Information
 * Services, Inc. and is licensed by an agreement.  Any unauthorized access,
 * use, duplication, or disclosure is unlawful.
 * <p/>
 * Copyright (c) Fidelity Information Services, Inc.
 * 2006, All right reserved.
 * <p/>
 * User: Satheesh Kumar G - e1011705
 * Date: 28/02/14
 * Time: 15:35
 */
public interface CortexWebService {
    public Object sendAndReceiveCortexRequest(CortexRequestResponseHandler handler) throws Exception;
    public Object sendAndReceiveCortexRequestWithAttachment(CortexRequestResponseHandler handler, String criAttachment) throws Exception;
}
