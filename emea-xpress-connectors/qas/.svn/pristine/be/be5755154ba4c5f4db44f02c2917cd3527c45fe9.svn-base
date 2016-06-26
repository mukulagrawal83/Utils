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

import org.apache.log4j.Logger;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 		:	e3003319 (Basil George)
 * @description : 	This class is used to contact web services.
 * @implements 	:
 * @extends		:	com.fnis.xes.ifx.services.qas.QASValidator
 * @date        :   June 19 2013
 */

public class WebServiceClient {

    private String username;
    private String password;

    protected WebServiceTemplate webServiceTemplate;

    private Logger log = Logger.getLogger(this.getClass().getName());

    public WebServiceClient() {
        super();
        if (log.isDebugEnabled()) {
            log.debug("WebServiceClient");
        }
    }


    /**
     * @methodName	: sendAndReceiveQARequest
     * @param 		: Object msgObj
     * @description : This Method will hit the QAS exposed webservice and get the response.
     * @exception 	:
     * @returnType 	: Object
     */
    public Object sendAndReceiveQARequest(Object msgObj) throws Exception {
        return webServiceTemplate.marshalSendAndReceive(msgObj, new WebServiceMessageCallback() {
            public void doWithMessage(WebServiceMessage message) throws java.io.IOException, javax.xml.transform.TransformerException {
                try {
                    StringBuffer sbHeader = new StringBuffer();
                    sbHeader.append("<ns2:QAQueryHeader xmlns:ns2=\"http://www.qas.com/OnDemand-2011-03\">")
                            .append("<ns2:QAAuthentication>")
                            .append("<ns2:Username>").append(username).append("</ns2:Username>")
                            .append("<ns2:Password>").append(password).append("</ns2:Password>")
                            .append("</ns2:QAAuthentication>")
                            .append("</ns2:QAQueryHeader>");
                    StringSource qaHeaderSource = new StringSource(sbHeader.toString());
                    SoapHeader soapHeader = ((SoapMessage) message).getSoapHeader();
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    transformer.transform(qaHeaderSource, soapHeader.getResult());
                    } catch (SoapFaultClientException se) {
                    SoapFaultDetail faultDetail = se.getSoapFault().getFaultDetail();
                    if (faultDetail != null) {
                        processFaultDetail((SoapMessage) se.getWebServiceMessage(), faultDetail);
                    }
                    log.debug("Fault Code"+se.getFaultCode());
                    log.debug("Fault Reason"+se.getFaultStringOrReason());
                    log.debug("Message"+se.fillInStackTrace().getLocalizedMessage());
                    throw se;


                } catch (Exception e) {
                    if (log.isDebugEnabled()) {
                        log.debug("Exception"+e.getMessage());
                    }
                    throw new TransformerException(e);
                }

            }

        });
    }
    /* Added for Soap Fault */
    private void processFaultDetail(SoapMessage soapMessage, SoapFaultDetail faultDetail) {
        try {
            List<Object> unmarshalledEntries = new ArrayList<Object>();
            Iterator<?> entries = faultDetail.getDetailEntries();
            if (entries.hasNext()) {
                SoapFaultDetailElement faultDetailElement = (SoapFaultDetailElement) entries.next();
                unmarshalledEntries.add(webServiceTemplate.getUnmarshaller().unmarshal(faultDetailElement.getSource()));
            }
            if (!unmarshalledEntries.isEmpty()) {
                throw new UnmarshalledSoapFaultClientException(soapMessage, unmarshalledEntries);
            }

        } catch (IOException ex) {
            throw new WebServiceIOException("I/O error: " + ex.getMessage(), ex);
        }
    }

    public WebServiceTemplate getWebServiceTemplate() {
        return webServiceTemplate;
    }

    public void setWebServiceTemplate(WebServiceTemplate theWebServiceTemplate) {
        webServiceTemplate = theWebServiceTemplate;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
