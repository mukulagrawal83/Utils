package com.fnis.xes.ifx.interfacepoint.cortex.handler;

import com.fisglobal.cortex.CardApplication;
import com.fisglobal.cortex.CardApplicationResponse;
import com.fisglobal.cortex.CardApplicationRspsInfo;
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
 * Date: 24/04/14
 * Time: 17:52
 */
public class CardApplicationCortexHandler implements CortexRequestResponseHandler<CardApplication, CardApplicationResponse> {
    private CardApplication request;
    private String URL;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public CardApplicationResponse process(Element rootElement) throws Exception{
        if(logger.isDebugEnabled()) {
            logger.debug("CardApplicationResponse - process starts");
        }
        CardApplicationResponse response = null;
        Element cardApplicationReturn = null;
        if(null != rootElement)
            cardApplicationReturn = rootElement.getChild(CortexConstants.CARD_APPLICATION_RETURN);

        if(null != cardApplicationReturn) {
            response = new CardApplicationResponse();
            response.setCardApplicationRspsInfo(new CardApplicationRspsInfo());
            /*response.getCardApplicationRspsInfo().setPAN(cardApplicationReturn.getChildText(CortexConstants.PAN));
            response.getCardApplicationRspsInfo().setCardID(cardApplicationReturn.getChildText(CortexConstants.CARD_ID));*/
            response.getCardApplicationRspsInfo().setActionCode(cardApplicationReturn.getChildText(CortexConstants.ACTION_CD));

            if(logger.isDebugEnabled()) {
                logger.debug("statusChangeReturn - ActionCd "+response.getCardApplicationRspsInfo().getActionCode());
            }
        }
        if(logger.isDebugEnabled()) {
            logger.debug("CardApplicationResponse - process ends");
        }
        return response;
    }

    public void setRequest(CardApplication request) {
       this.request = request;
    }

    public CardApplication getRequest() {
        return request;
    }

    public void setURL(String url) {
        this.URL = url;
    }

    public String getURL() {
        return URL;
    }
}
