package com.fnis.xes.ifx.interfacepoint.qas;
/**
 * This program contains trade secrets that belong to Fidelity Information
 * Services, Inc. and is licensed by an agreement.  Any unauthorized access,
 * use, duplication, or disclosure is unlawful.
 *
 * Copyright (c) Fidelity Information Services, Inc.
 * 2006, All right reserved.
 * XProfileAdapter_Party.java
 *
 */
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.SoapFaultClientException;

import java.util.Collections;
import java.util.List;

/**
 * @author 		:	e3003319 (Basil George)
 * @description : 	This class handles SOAP Fault
 * @implements 	:
 * @extends		:	com.fnis.xes.ifx.services.qas.QASValidator
 * @date        :   June 19 2013
 */
public class UnmarshalledSoapFaultClientException extends SoapFaultClientException {

    private final List<Object> faultDetails;

    public UnmarshalledSoapFaultClientException(SoapMessage faultMessage, List<Object> faultDetails) {
        super(faultMessage);
        this.faultDetails = faultDetails;
    }

    public List<Object> getFaultDetails() {
        return Collections.unmodifiableList(faultDetails);
    }

    public boolean containsFaultDetailOfType(Class<?> c) {
        for (Object detail : faultDetails) {
            if (c.isAssignableFrom(detail.getClass())) {
                return true;
            }
        }
        return false;
    }

}