package com.fnis.xes.ifx.interfacepoint.cortex.handler;

import com.fisglobal.cortex.*;
import com.fnis.xes.services.adapter.cortex.utils.CortexConstants;
import com.fnis.xes.services.adapter.cortex.utils.CortexUtility;
import org.apache.log4j.Logger;
import org.jdom2.Element;

import java.util.List;

/**
 * This program contains trade secrets that belong to Fidelity Information
 * Services, Inc. and is licensed by an agreement.  Any unauthorized access,
 * use, duplication, or disclosure is unlawful.
 * <p/>
 * Copyright (c) Fidelity Information Services, Inc.
 * 2006, All right reserved.
 * <p/>
 * User: Satheesh Kumar G - e1011705
 * Date: 26/11/2014
 * Time: 11:43
 */
public class CardLimitEnquiryCortexHandler implements CortexRequestResponseHandler<LimitEnquiry, LimitEnquiryResponse>{
    private LimitEnquiry request;
    private String URL;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public LimitEnquiryResponse process(Element rootElement) throws Exception {
        if(logger.isDebugEnabled()) {
            logger.debug("LimitEnquiryResponse - process starts");
        }

        LimitEnquiryResponse response = null;
        Element cardLimitEnquiryReturn = null;

        if(null != rootElement) {
            cardLimitEnquiryReturn = rootElement.getChild(CortexConstants.CARD_LIMIT_ENQUIRY_RETURN);
        } else {
            if(logger.isDebugEnabled()) {
                logger.debug("LimitEnquiryReturn Root Elmt is NULL.... ");
            }
        }

        if(logger.isDebugEnabled()) {
            logger.debug("cardLimitEnquiryReturn Elmt : "+cardLimitEnquiryReturn);
        }

        if(null != cardLimitEnquiryReturn) {
            response = new LimitEnquiryResponse();
            response.setLimitEnquiryRspsInfo(new LimitEnquiryRspsInfo());

            response.getLimitEnquiryRspsInfo().setPAN(cardLimitEnquiryReturn.getChildText(CortexConstants.PAN));
            response.getLimitEnquiryRspsInfo().setCardID(cardLimitEnquiryReturn.getChildText(CortexConstants.CARD_ID));
            response.getLimitEnquiryRspsInfo().setActionCode(cardLimitEnquiryReturn.getChildText(CortexConstants.ACTION_CD));

            Element cardLimDetList = cardLimitEnquiryReturn.getChild(CortexConstants.CARD_LIMIT_DET);
            Element acctLimDetList = cardLimitEnquiryReturn.getChild(CortexConstants.ACCT_LIMIT_DET);

            if(logger.isDebugEnabled()) {
                logger.debug("Check Parent CardLimitList : "+ cardLimDetList);
                logger.debug("Check Parent AcctLimitList : "+ acctLimDetList);
            }

            if(acctLimDetList != null) {
                List<Element> acctLimitInfos =  acctLimDetList.getChildren();
                response.getLimitEnquiryRspsInfo().setAccLimDet(new ArrayOfTns2AccountLimitInfo());
                AccountLimitInfo accountLimitInfo = null;

                if(acctLimitInfos != null) {
                    for(Element element : acctLimitInfos) {
                        accountLimitInfo = new AccountLimitInfo();
                        accountLimitInfo.setAccno(element.getChildText(CortexConstants.ACCT_NO));
                        accountLimitInfo.setLimits(new ArrayOfTns2LimitsInfo());

                        Element limitsInfoElmt =  element.getChild(CortexConstants.LIMITS);
                        if(null != limitsInfoElmt){
                            List<Element> limitsInfos =  limitsInfoElmt.getChildren();

                            if(null != limitsInfos) {
                                for(Element elementLimitsInfo : limitsInfos) {
                                    ArrayOfTns2LimitInfo arrayOfTns2LimitInfo =  new ArrayOfTns2LimitInfo();

                                    Element limitInfoElmt =  elementLimitsInfo.getChild(CortexConstants.LIMIT_DET);

                                    if(null != limitInfoElmt) {
                                        List<Element> limitInfos =  limitInfoElmt.getChildren();
                                        if(null != limitInfos) {
                                            for(Element elementLimitInfo : limitInfos) {
                                                LimitInfo limitInfo = new LimitInfo();
                                                limitInfo.setCycAmt(CortexUtility.convertStringToDouble(elementLimitInfo.getChildText(CortexConstants.CYC_AMT)));
                                                limitInfo.setMaxAmt(CortexUtility.convertStringToDouble(elementLimitInfo.getChildText(CortexConstants.MAX_AMT)));
                                                limitInfo.setMinAmt(CortexUtility.convertStringToDouble(elementLimitInfo.getChildText(CortexConstants.MIN_AMT)));
                                                limitInfo.setLimRef(elementLimitInfo.getChildText(CortexConstants.LIMIT_REF));
                                                arrayOfTns2LimitInfo.getLimitInfos().add(limitInfo);

                                                LimitsInfo limitsInfo = new LimitsInfo();
                                                limitsInfo.setLimDet(arrayOfTns2LimitInfo);

                                                accountLimitInfo.getLimits().getLimitsInfos().add(limitsInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    response.getLimitEnquiryRspsInfo().getAccLimDet().getAccountLimitInfos().add(accountLimitInfo);
                }
            }
        }
        return response;
    }

    public void setRequest(LimitEnquiry request) {
        this.request = request;
    }

    public LimitEnquiry getRequest() {
        return request;
    }

    public void setURL(String url) {
        this.URL = url;
    }

    public String getURL() {
        return URL;
    }
}
