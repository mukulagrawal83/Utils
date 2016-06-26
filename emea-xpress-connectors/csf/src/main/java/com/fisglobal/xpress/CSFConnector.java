/*
 * ===================================================================
 * Copyright Notice! 
 * ------------------------------------------------------------------- 
 * This document is protected under the trade secret and copyright 
 * laws as the property of Fidelity National Information Services, Inc. 
 * Copying, reproduction or distribution should be limited and only to
 * employees with a �need to know� to do their job. 
 * Any disclosure of this document to third parties 
 * is strictly prohibited.
 *
 * �  Fidelity National Information Services
 * All rights reserved worldwide.
 * ===================================================================
 */
package com.fisglobal.xpress;

import com.csf.documentgeneratorservice.DocumentGenerationRequest;
import com.csf.documentgeneratorservice.DocumentGenerationResponse;
import com.fis.ec.base.core.commonclasses.exceptions.TechnicalFailureException;
import com.fis.ec.correspondence.core.commonclasses.eventdata.CorrespondenceDocument;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.datacontract.schemas._2004._07.realtimeserver.RenderData;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.w3._2005._05.xmlmime.Base64Binary;

import javax.xml.bind.JAXBElement;
import java.util.UUID;


public class CSFConnector {


    private String endPointURL;
    protected WebServiceTemplate webServiceTemplate;

    private static final Logger LOGGER = Logger.getLogger(CSFConnector.class.getName());

    public CorrespondenceDocument generateDocument(String file, String templatename) throws TechnicalFailureException {
        CorrespondenceDocument correspondenceDocument = null;

        try {
            LOGGER.debug("GenerateDocument CSF call with input         : " + file);
            LOGGER.debug("GenerateDocument CSF call with template name : " + templatename);
            LOGGER.debug("GenerateDocument CSF call with url           : " + getEndPointURL());

            DocumentGenerationRequest request = prepareRequestMessage(file, templatename);
            DocumentGenerationResponse response = (DocumentGenerationResponse) ((JAXBElement) getWebServiceTemplate().marshalSendAndReceive(endPointURL, request)).getValue();
            if (response.getDocStreamArray().isEmpty()) {
                LOGGER.debug("CSF doesn't return the data, see the error message");
                LOGGER.warn("CSF Error code " + response.getErrCde());
                LOGGER.warn("CSF Error message " + response.getErrMsg());
                throw new TechnicalFailureException("Unexpected data returned from Real Time server : " + response.getErrMsg());
            }
            correspondenceDocument = new CorrespondenceDocument();
            RenderData renderData = response.getDocStreamArray().get(0);
            correspondenceDocument.setDocumentData(formatCSFOutput(renderData.getData().getValue().getBytes()));
            correspondenceDocument.setMimeType(getDocumentContentType(templatename));
            correspondenceDocument.setDocumentName(getDocumentName(templatename));
        } catch (Exception e) {
            throw new TechnicalFailureException("Error in Real Time server", e);
        }

        return correspondenceDocument;
    }

    private String getDocumentContentType(String template) {
        String format;
        if (template.toLowerCase().contains("statement") || template.toLowerCase().contains("letter") || template.toLowerCase().contains("cdit")) {
            format = "application/pdf";
        } else if (template.toLowerCase().contains("email")) {
            format = "text/html";
        } else {
            format = "text/plain";
        }
        return format;
    }

    private String getDocumentName(String templatename) {
        return getDocumentFormat(templatename) + "-" + UUID.randomUUID().toString() + "." + getDocumentFormat(templatename);
    }

    private String getDocumentFormat(String template) {
        String format;
        if (template.toLowerCase().contains("statement") || template.toLowerCase().contains("letter") || template.toLowerCase().contains("cdit")) {
            format = "pdf";
        } else if (template.toLowerCase().contains("email")) {
            format = "html";
        } else {
            format = "Text";
        }
        return format;
    }

    private byte[] formatCSFOutput(byte[] bytes) {
        return (Base64.encodeBase64(new String(Base64.decodeBase64(bytes)).replaceAll("\f", "").getBytes()));
    }

    public DocumentGenerationRequest prepareRequestMessage(String csfInput, String template) {
        DocumentGenerationRequest documentGenerationRequest = new DocumentGenerationRequest();
        documentGenerationRequest.setDsgnNme(template);
        documentGenerationRequest.setOtptTyp(getDocumentFormat(template));
        Base64Binary base64Binary = new Base64Binary();
        base64Binary.setValue(csfInput.getBytes());
        base64Binary.setContentType("application/?");
        documentGenerationRequest.getInptData().add(base64Binary);
        return documentGenerationRequest;
    }

    public String getEndPointURL() {
        return endPointURL;
    }

    public void setEndPointURL(String endPointURL) {
        this.endPointURL = endPointURL;
    }


    public WebServiceTemplate getWebServiceTemplate() {
        return webServiceTemplate;
    }

    public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }
}