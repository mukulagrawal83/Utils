package com.fnis.xes.ifx.interfacepoint.cortex.handler;

import com.fisglobal.cortex.ManagePIN;
import com.fisglobal.cortex.ManagePINResponse;
import com.fisglobal.cortex.ManagePINRspsInfo;
import com.fnis.xes.services.adapter.cortex.utils.CortexConstants;
import org.apache.log4j.Logger;
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
 * Date: 01/04/14
 * Time: 15:50
 */
public class ManagePinCortexHandler implements CortexRequestResponseHandler<ManagePIN, ManagePINResponse> {
    private ManagePIN request;
    private String URL;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public ManagePINResponse process(Element rootElement) throws Exception {
        if(logger.isDebugEnabled()) {
            logger.debug("ManagePINResponse - process starts");
        }

        ManagePINResponse response = null;
        Element managePinReturn = null;
        if(null != rootElement)
            managePinReturn = rootElement.getChild(CortexConstants.MANAGE_PIN_RETURN);

        if(null != managePinReturn) {
            response = new ManagePINResponse();
            response.setManagePINRspsInfo(new ManagePINRspsInfo());
            response.getManagePINRspsInfo().setPAN(managePinReturn.getChildText(CortexConstants.PAN));
            response.getManagePINRspsInfo().setCardID(managePinReturn.getChildText(CortexConstants.CARD_ID));
            response.getManagePINRspsInfo().setActionCode(managePinReturn.getChildText(CortexConstants.ACTION_CD));
            if(logger.isDebugEnabled()) {
                logger.debug("statusChangeReturn - CardId "+response.getManagePINRspsInfo().getCardID());
                logger.debug("statusChangeReturn - PAN "+response.getManagePINRspsInfo().getPAN());
                logger.debug("statusChangeReturn - ActionCd "+response.getManagePINRspsInfo().getActionCode());
            }
        }
        if(logger.isDebugEnabled()) {
            logger.debug("ManagePINResponse - process ends");
        }
        return response;
    }

    public void setRequest(ManagePIN request) {
       this.request = request;
    }

    public ManagePIN getRequest() {
        return request;
    }

    public void setURL(String url) {
        this.URL = url;
    }

    public String getURL() {
        return URL;
    }
}
