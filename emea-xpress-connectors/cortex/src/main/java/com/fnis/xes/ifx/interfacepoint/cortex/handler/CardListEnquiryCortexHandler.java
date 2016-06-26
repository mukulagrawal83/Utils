package com.fnis.xes.ifx.interfacepoint.cortex.handler;

import com.fisglobal.cortex.*;
import com.fnis.xes.services.adapter.cortex.utils.CortexConstants;
import com.fnis.xes.services.adapter.cortex.utils.CortexUtility;
import org.apache.commons.lang.StringUtils;
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
 * Date: 05/03/14
 * Time: 10:59
 */
public class CardListEnquiryCortexHandler implements CortexRequestResponseHandler<CardListEnquiry, CardListEnquiryResponse>{
    private CardListEnquiry request;
    private String URL;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public CardListEnquiryResponse process(Element rootElement) throws Exception {
        if(logger.isDebugEnabled()) {
            logger.debug("CardListEnquiryResponse - process starts");
        }

        CardListEnquiryResponse response = null;
        Element cardListEnquiryReturn = null;

        if(null != rootElement) {
            cardListEnquiryReturn = rootElement.getChild(CortexConstants.CARD_LIST_ENQUIRY_RETURN);
        } else {
            if(logger.isDebugEnabled()) {
                logger.debug("cardListEnquiryReturn Root Elmt is NULL.... ");
            }
        }

        if(logger.isDebugEnabled()) {
            logger.debug("cardListEnquiryReturn Elmt : "+cardListEnquiryReturn);
        }

        if(null != cardListEnquiryReturn) {
            response = new CardListEnquiryResponse();
            response.setCardListEnquiryRspsInfo(new CardListEnquiryRspsInfo());

            //---------------------PAN and CardId are not required in the response as of now
            response.getCardListEnquiryRspsInfo().setPAN(cardListEnquiryReturn.getChildText(CortexConstants.PAN));
            response.getCardListEnquiryRspsInfo().setCardID(cardListEnquiryReturn.getChildText(CortexConstants.CARD_ID));
            //-----------------------

            response.getCardListEnquiryRspsInfo().setActionCode(cardListEnquiryReturn.getChildText(CortexConstants.ACTION_CD));
            Element cardHolderCardList = cardListEnquiryReturn.getChild(CortexConstants.CARD_HOLDER_PRIM_LIST);

            if(logger.isDebugEnabled()) {
                logger.debug("Check Parent CardHolderList : "+ cardListEnquiryReturn.getChild(CortexConstants.CARD_HOLDER_PRIM_LIST));
            }

            Element purchaserCardList = cardListEnquiryReturn.getChild("purchaserCardList");
            Element loaderCardList = cardListEnquiryReturn.getChild("loaderCardList");

            if(null != cardHolderCardList){
                List<Element> cardHolderCardListInfos =  cardHolderCardList.getChildren();
                ArrayOfTns1CardListInfo cardHolderCardListInfo = new ArrayOfTns1CardListInfo();
                CardListInfo cardListInfo = null;

                int i=0;
                if(null != cardHolderCardListInfos){
                    if(logger.isDebugEnabled()) {
                        logger.debug("cardHolderCardListInfos List : "+cardHolderCardListInfos.size());
                    }
                    for(Element element : cardHolderCardListInfos){
                        cardListInfo = new CardListInfo();

                        cardListInfo.setPAN(element.getChildText(CortexConstants.PAN));
                        cardListInfo.setEmbossName(element.getChildText(CortexConstants.EMBOSS_NAME));
                        String expDateStr = element.getChildText(CortexConstants.EXP_DATE);
                        if(null != expDateStr){
                            cardListInfo.setExpDate(CortexUtility.populateLocalDate(expDateStr));
                        }
                        cardListInfo.setCardID(element.getChildText(CortexConstants.CARD_ID));
                        cardListInfo.setStatCode(element.getChildText(CortexConstants.STAT_CODE));
                        cardListInfo.setAccno(element.getChildText(CortexConstants.ACCT_NO));
                        cardListInfo.setProgram(element.getChildText(CortexConstants.PROGRAM));
                        cardListInfo.setCrdProduct(element.getChildText(CortexConstants.CRD_PRODUCT));
                        cardListInfo.setAvlBal(CortexUtility.convertStringToDouble(element.getChildText(CortexConstants.AVL_BALANCE)));
                        cardListInfo.setBlkAmt(CortexUtility.convertStringToDouble(element.getChildText(CortexConstants.BLK_AMOUNT)));
                        cardListInfo.setPinTries(Integer.parseInt(StringUtils.defaultString(element.getChildText(CortexConstants.PIN_TRIES),"0")));

                        //currently these two are not mapped populating for future use
                        cardListInfo.setCurrCode(element.getChildText(CortexConstants.CURR_CODE));
                        cardListInfo.setFirstName(element.getChildText(CortexConstants.FIRST_NAME));
                        cardListInfo.setLastName(element.getChildText(CortexConstants.LAST_NAME));
                        cardListInfo.setCustType(Integer.parseInt(StringUtils.defaultString(element.getChildText(CortexConstants.CARD_TYPE),"0")));
                        cardListInfo.setDesignRef(element.getChildText(CortexConstants.DESIGN_REF));

                        if(logger.isDebugEnabled()) {
                            logger.debug("CardListInfo: ( "+i+" )");
                            logger.debug("CardId (DOM)= "+element.getChildText(CortexConstants.CARD_ID));
                            logger.debug("CardId (Obj)= "+cardListInfo.getCardID());

                            logger.debug("Status Code (DOM) = "+element.getChildText(CortexConstants.STAT_CODE));
                            logger.debug("Status Code  (Obj) = "+cardListInfo.getStatCode());

                            logger.debug("crdProduct (DOM) = "+element.getChildText(CortexConstants.CRD_PRODUCT));
                            logger.debug("crdProduct  (Obj) = "+cardListInfo.getCrdProduct());
                        }

                        Element additionalCustDetList = element.getChild(CortexConstants.ADDITIONAL_CUST_DET);
                        if(null != additionalCustDetList) {
                           cardListInfo.setAdditionalCustDet(new ArrayOfTns3AdditionalDetails());
                           List<Element> additionalCustDet = additionalCustDetList.getChildren(CortexConstants.ADDITIONAL_CUST_DET);
                            ArrayOfTns3AdditionalDetails arrayOfTns3AdditionalDetails = new ArrayOfTns3AdditionalDetails();
                            for(Element elementAddCustDet : additionalCustDet) {
                               AdditionalDetails additionalCustDetObj = new AdditionalDetails();
                               additionalCustDetObj.setReference(elementAddCustDet.getChildText(CortexConstants.REFERENCE));
                               additionalCustDetObj.setValue(elementAddCustDet.getChildText(CortexConstants.VALUE));
                               arrayOfTns3AdditionalDetails.getAdditionalDetails().add(additionalCustDetObj);
                           }
                            cardListInfo.setAdditionalCustDet(arrayOfTns3AdditionalDetails);
                        }
                        cardHolderCardListInfo.getCardListInfos().add(cardListInfo);
                    }
                    i++;
                } else {
                    if(logger.isDebugEnabled()) {
                        logger.debug("cardHolderCardListInfo is NULL ");
                    }
                }
                response.getCardListEnquiryRspsInfo().setCardHolderCardList(cardHolderCardListInfo);
            } else {
                if(logger.isDebugEnabled()) {
                    logger.debug("cardHolderCardList is NULL ");
                }
            }
            if(logger.isDebugEnabled()) {
                logger.debug("CardListEnquiryResponse - Response populated");
            }
        }
        return response;
    }

    public void setRequest(CardListEnquiry request) {
        this.request = request;
    }

    public CardListEnquiry getRequest() {
        return request;
    }

    public void setURL(String url) {
       this.URL = url;
    }

    public String getURL() {
        return URL;
    }
}
