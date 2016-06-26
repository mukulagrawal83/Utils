package com.fnis.xes.ifx.interfacepoint.cortex.handler;

import com.fisglobal.cortex.StatusChange;
import com.fisglobal.cortex.StatusChangeResponse;
import com.fisglobal.cortex.StatusChangeRspsInfo;
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
 * Date: 04/03/14
 * Time: 17:52
 */
public class StatusChangeCortexHandler implements CortexRequestResponseHandler<StatusChange, StatusChangeResponse> {
    private StatusChange request;
    private String URL;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public StatusChangeResponse process(Element rootElement) throws Exception{
        if(logger.isDebugEnabled()) {
            logger.debug("StatusChangeResponse - process starts");
        }

        StatusChangeResponse response = null;
        Element statusChangeReturn = null;
        if(null != rootElement)
            statusChangeReturn = rootElement.getChild(CortexConstants.STATUS_CHANGE_RETURN);

        if(null != statusChangeReturn) {
            response = new StatusChangeResponse();
            response.setStatusChangeRspsInfo(new StatusChangeRspsInfo());
            response.getStatusChangeRspsInfo().setPAN(statusChangeReturn.getChildText(CortexConstants.PAN));
            response.getStatusChangeRspsInfo().setCardID(statusChangeReturn.getChildText(CortexConstants.CARD_ID));
            response.getStatusChangeRspsInfo().setActionCode(statusChangeReturn.getChildText(CortexConstants.ACTION_CD));

            if(logger.isDebugEnabled()) {
                logger.debug("statusChangeReturn - CardId "+response.getStatusChangeRspsInfo().getCardID());
                logger.debug("statusChangeReturn - PAN "+response.getStatusChangeRspsInfo().getPAN());
                logger.debug("statusChangeReturn - ActionCd "+response.getStatusChangeRspsInfo().getActionCode());
            }
        }

        if(logger.isDebugEnabled()) {
            logger.debug("StatusChangeResponse - process ends");
        }
        return response;
    }

    public void setRequest(StatusChange request) {
       this.request = request;
    }

    public StatusChange getRequest() {
        return request;
    }

    public void setURL(String url) {
        this.URL = url;
    }

    public String getURL() {
        return URL;
    }
}
