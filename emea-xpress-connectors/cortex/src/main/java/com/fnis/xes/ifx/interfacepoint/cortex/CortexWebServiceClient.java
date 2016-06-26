package com.fnis.xes.ifx.interfacepoint.cortex;


import com.fisglobal.cortex.CardApplication;
import com.fisglobal.cortex.CardDetailsType;
import com.fnis.xes.ifx.interfacepoint.cortex.handler.CortexRequestResponseHandler;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.jdom2.transform.JDOMResult;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * This program contains trade secrets that belong to Fidelity Information
 * Services, Inc. and is licensed by an agreement.  Any unauthorized access,
 * use, duplication, or disclosure is unlawful.
 * <p/>
 * Copyright (c) Fidelity Information Services, Inc.
 * 2006, All right reserved.
 * <p/>
 * User: Satheesh Kumar G - e1011705
 * Date: 10/02/14
 * Time: 16:12
 */
public class CortexWebServiceClient implements CortexWebService {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private WebServiceTemplate cortexWebServiceTemplate;
    private JAXBContext cortexJAXBContext = null;
    private String cid;

    public CortexWebServiceClient () {
        try {
            cortexJAXBContext = JAXBContext.newInstance("com.fisglobal.cortex");
        } catch (JAXBException e) {
            throw new RuntimeException("Can't initialize JAXBContext(com.fisglobal.cortex)");
        }
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public WebServiceTemplate getCortexWebServiceTemplate() {
        return cortexWebServiceTemplate;
    }

    public void setCortexWebServiceTemplate(WebServiceTemplate cortexWebServiceTemplate) {
        this.cortexWebServiceTemplate = cortexWebServiceTemplate;
    }

    /**
     *  This method will send and receives the Cortex - request and response.
     *  if there is any error or exception it will be added to the stack and thrown in the service.
     *
     * @param handler
     * @return
     * @throws Exception
     */
    public Object sendAndReceiveCortexRequest(CortexRequestResponseHandler handler) throws Exception {

        JAXBSource source = new JAXBSource(cortexJAXBContext, handler.getRequest());
       /* if(logger.isDebugEnabled()) {
            XMLHelper x = new XMLHelper(handler.getRequest().getClass().getPackage().getName());
            String xml = x.marshall(handler.getRequest(), "UTF-8");
            logger.debug ("Generated request" + xml);
        }*/

        JDOMResult result = new JDOMResult();
        getCortexWebServiceTemplate().sendSourceAndReceiveToResult(handler.getURL(), source, result);
        // transform JDOMResult to Response
        Element root = result.getDocument().getRootElement();
        //handler
        return handler.process(root);
    }

    /**
     *  This method will send and receives the Cortex - request and response with the CRI attachment.
     *  if there is any error or exception it will be added to the stack and thrown in the service.
     *
     * @param handler
     * @return
     * @throws Exception
     */
    public Object sendAndReceiveCortexRequestWithAttachment(CortexRequestResponseHandler handler, final String criAttachment) throws Exception {
        CardDetailsType detailsType = new CardDetailsType();
        detailsType.setHref(cid);
        ((CardApplication) handler.getRequest()).setCardDetails(detailsType);

        JAXBSource source = new JAXBSource(cortexJAXBContext, handler.getRequest());
        JDOMResult result = new JDOMResult();

        getCortexWebServiceTemplate().sendSourceAndReceiveToResult(handler.getURL(), source, new WebServiceMessageCallback() {

            public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
                SoapMessage soapMessage = (SoapMessage)message;
                soapMessage.addAttachment(cid, new ByteArrayResource(criAttachment.getBytes("UTF-8")), "text/xml");

                if(logger.isDebugEnabled()) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    message.writeTo(out);
                    logger.debug("SOAP Request Payload: " + new String(out.toByteArray()));
                }
            }
        }, result);

        // transform JDOMResult to Response
        Element root = result.getDocument().getRootElement();
        //handler
        return handler.process(root);
    }
}
