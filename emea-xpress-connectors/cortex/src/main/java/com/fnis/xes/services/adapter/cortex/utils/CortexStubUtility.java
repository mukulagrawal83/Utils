package com.fnis.xes.services.adapter.cortex.utils;

import com.fisglobal.cortex.*;
import org.apache.commons.lang.StringUtils;


/**
 * This program contains trade secrets that belong to Fidelity Information
 * Services, Inc. and is licensed by an agreement.  Any unauthorized access,
 * use, duplication, or disclosure is unlawful.
 * <p/>
 * Copyright (c) Fidelity Information Services, Inc.
 * 2006, All right reserved.
 * <p/>
 * User: Satheesh Kumar G - e1011705
 * Date: 12/06/14
 * Time: 15:57
 *
 * <p>
 *  Created for the interim solution for the Cortex connectivity issue.
 *  This class will be removed once the issue will be resolved in the Dev environment
 * </p>
 */
public class CortexStubUtility {

    public static CardListEnquiryResponse getCardListEnquiryResponse(CardListEnquiry request) throws Exception {
        CardListEnquiryResponse response = new CardListEnquiryResponse();
        response.setCardListEnquiryRspsInfo(new CardListEnquiryRspsInfo());
        String cardId = "7287877129533840";
        String PAN = "6332207153510539";

        if(request != null && request.getCardListEnquiryRqstInfo() != null) {
            if(StringUtils.isNotBlank(request.getCardListEnquiryRqstInfo().getCardID())) {
                cardId = request.getCardListEnquiryRqstInfo().getCardID();
            }
            if(StringUtils.isNotBlank(request.getCardListEnquiryRqstInfo().getPAN())) {
                PAN =  request.getCardListEnquiryRqstInfo().getPAN();
            }

        }
        response.getCardListEnquiryRspsInfo().setCardID(cardId);
        response.getCardListEnquiryRspsInfo().setPAN(PAN);
        response.getCardListEnquiryRspsInfo().setActionCode("000");

        ArrayOfTns1CardListInfo cardHolderCardListInfo = new ArrayOfTns1CardListInfo();

        //First set
        CardListInfo cardListInfo =  new CardListInfo();
        cardListInfo.setPAN("6332206356328921");
        cardListInfo.setEmbossName("MR JOHN WATSON");
        String expDateStr = "2018-05-31";
        cardListInfo.setExpDate(CortexUtility.populateLocalDate(expDateStr));
        cardListInfo.setCardID("7872695657471688");
        cardListInfo.setStatCode("02");
        cardListInfo.setAccno("362000004132");
        cardListInfo.setProgram("SBPR4");
        cardListInfo.setCurrCode("GBP");
        cardListInfo.setCrdProduct("SBK4");
        cardListInfo.setAvlBal(CortexUtility.convertStringToDouble("5000"));
        cardListInfo.setBlkAmt(CortexUtility.convertStringToDouble("300"));
        cardListInfo.setPinTries(0);

        cardListInfo.setAdditionalCustDet(new ArrayOfTns3AdditionalDetails());
        ArrayOfTns3AdditionalDetails arrayOfTns3AdditionalDetails = new ArrayOfTns3AdditionalDetails();

        AdditionalDetails additionalCustDetObj1 = new AdditionalDetails();
        additionalCustDetObj1.setReference("CUST_TITLE");
        additionalCustDetObj1.setValue("STUB");
        arrayOfTns3AdditionalDetails.getAdditionalDetails().add(additionalCustDetObj1);

        AdditionalDetails additionalCustDetObj11 = new AdditionalDetails();
        additionalCustDetObj11.setReference("ACN");
        additionalCustDetObj11.setValue("11");
        arrayOfTns3AdditionalDetails.getAdditionalDetails().add(additionalCustDetObj11);

        cardListInfo.setAdditionalCustDet(arrayOfTns3AdditionalDetails);
        cardHolderCardListInfo.getCardListInfos().add(cardListInfo);

        //Second set
        CardListInfo cardListInfo2 =  new CardListInfo();
        cardListInfo2.setPAN(PAN);
        cardListInfo2.setEmbossName("MR JOHN WATSON");
        cardListInfo2.setExpDate(CortexUtility.populateLocalDate("2018-06-30"));
        cardListInfo2.setCardID(cardId);
        cardListInfo2.setStatCode("00");
        cardListInfo2.setAccno("362000004132");
        cardListInfo2.setProgram("SBPR4");
        cardListInfo2.setCurrCode("GBP");
        cardListInfo2.setCrdProduct("SBK4");

        cardListInfo2.setAvlBal(CortexUtility.convertStringToDouble("6000"));
        cardListInfo2.setBlkAmt(CortexUtility.convertStringToDouble("200"));
        cardListInfo2.setPinTries(1);

        cardListInfo2.setAdditionalCustDet(new ArrayOfTns3AdditionalDetails());
        ArrayOfTns3AdditionalDetails arrayOfTns3AdditionalDetails2 = new ArrayOfTns3AdditionalDetails();

        AdditionalDetails additionalCustDetObj2 = new AdditionalDetails();
        additionalCustDetObj2.setReference("CUST_TITLE");
        additionalCustDetObj2.setValue("STUB");
        arrayOfTns3AdditionalDetails2.getAdditionalDetails().add(additionalCustDetObj2);

        AdditionalDetails additionalCustDetObj22 = new AdditionalDetails();
        additionalCustDetObj22.setReference("ACN");
        additionalCustDetObj22.setValue("10");
        arrayOfTns3AdditionalDetails2.getAdditionalDetails().add(additionalCustDetObj22);

        cardListInfo2.setAdditionalCustDet(arrayOfTns3AdditionalDetails2);
        cardHolderCardListInfo.getCardListInfos().add(cardListInfo2);

        response.getCardListEnquiryRspsInfo().setCardHolderCardList(cardHolderCardListInfo);
        return response;
    }

    public static StatusChangeResponse getStatusChangeResponse(StatusChange statusChange) throws Exception {
        StatusChangeResponse response = new StatusChangeResponse();
        response.setStatusChangeRspsInfo(new StatusChangeRspsInfo());
        if(statusChange != null && statusChange.getStatusChangeRqstInfo() != null) {
            response.getStatusChangeRspsInfo().setCardID(statusChange.getStatusChangeRqstInfo().getCardID());
        } else {
            response.getStatusChangeRspsInfo().setCardID("7287877117349134");
        }
        response.getStatusChangeRspsInfo().setPAN("6332207931667676");
        response.getStatusChangeRspsInfo().setActionCode("000");
        return response;
    }

    public static ManagePINResponse getManagePINResponse() throws Exception {

        ManagePINResponse response = new ManagePINResponse();
        response.setManagePINRspsInfo(new ManagePINRspsInfo());
        response.getManagePINRspsInfo().setPAN("6332207931667676");
        response.getManagePINRspsInfo().setCardID("7287877117349134");
        response.getManagePINRspsInfo().setActionCode("000");

        return response;
    }

    public static CardApplicationResponse getCardApplicationResponse() throws Exception{
        CardApplicationResponse response = new CardApplicationResponse();
        response.setCardApplicationRspsInfo(new CardApplicationRspsInfo());
        response.getCardApplicationRspsInfo().setActionCode("000");
        return response;
    }

    public static LimitEnquiryResponse getLimitEnquiryResponse(LimitEnquiry request) throws Exception {
        LimitEnquiryResponse response = new LimitEnquiryResponse();
        response.setLimitEnquiryRspsInfo(new LimitEnquiryRspsInfo());

        if(request != null && request.getLimitEnquiryRqstInfo() != null) {
            if(StringUtils.isNotBlank(request.getLimitEnquiryRqstInfo().getCardID())) {
              response.getLimitEnquiryRspsInfo().setCardID(request.getLimitEnquiryRqstInfo().getCardID());
            } else {
              response.getLimitEnquiryRspsInfo().setCardID("7287877129533840");
            }

            if(StringUtils.isNotBlank(request.getLimitEnquiryRqstInfo().getPAN())) {
                response.getLimitEnquiryRspsInfo().setPAN(request.getLimitEnquiryRqstInfo().getPAN());
            } else {
                response.getLimitEnquiryRspsInfo().setPAN("6332207153510539");
            }

        }

        response.getLimitEnquiryRspsInfo().setActionCode("000");

        response.getLimitEnquiryRspsInfo().setAccLimDet(new ArrayOfTns2AccountLimitInfo());

        AccountLimitInfo accountLimitInfo = new AccountLimitInfo();
        accountLimitInfo.setAccno("362000004132");
        accountLimitInfo.setCurrCode("826");
        accountLimitInfo.setLimits(new ArrayOfTns2LimitsInfo());


        ArrayOfTns2LimitInfo arrayOfTns2LimitInfo =  new ArrayOfTns2LimitInfo();
        LimitInfo limitInfo = new LimitInfo();
        limitInfo.setCycAmt(new Double(100).doubleValue());
        limitInfo.setMaxAmt(new Double(300).doubleValue());
        limitInfo.setLimRef("SBK_LIM_daily_cash");
        arrayOfTns2LimitInfo.getLimitInfos().add(limitInfo);

        LimitInfo limitInfo1 = new LimitInfo();
        limitInfo1.setCycAmt(new Double(101).doubleValue());
        limitInfo1.setMaxAmt(new Double(200).doubleValue());
        limitInfo1.setLimRef("SBK_LIM_daily_charity");

        arrayOfTns2LimitInfo.getLimitInfos().add(limitInfo1);

        LimitsInfo limitsInfo = new LimitsInfo();
        limitsInfo.setLimDet(arrayOfTns2LimitInfo);

        accountLimitInfo.getLimits().getLimitsInfos().add(limitsInfo);

        response.getLimitEnquiryRspsInfo().getAccLimDet().getAccountLimitInfos().add(accountLimitInfo);

        return response;
    }
}
