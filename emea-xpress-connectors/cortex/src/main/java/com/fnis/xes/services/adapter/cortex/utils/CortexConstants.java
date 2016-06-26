package com.fnis.xes.services.adapter.cortex.utils;

/**
 * This program contains trade secrets that belong to Fidelity Information
 * Services, Inc. and is licensed by an agreement.  Any unauthorized access,
 * use, duplication, or disclosure is unlawful.
 * <p/>
 * Copyright (c) Fidelity Information Services, Inc.
 * 2006, All right reserved.
 * <p/>
 * User: Satheesh Kumar G - e1011705
 * Date: 11/02/14
 * Time: 11:39
 */
public class CortexConstants {
    public static final String SRC_DOMAIN = "IFX";
    public static final String TARGET_DOMAIN = "CORTEX";

    public static final String SRC_DOMAIN_REV = "CORTEX";
    public static final String TARGET_DOMAIN_REV = "IFX";

    public static final String CARD_STATUS_CD_TBL ="CardStatusCode";
    public static final String CARD_TYPE_TBL ="CardType";
    public static final String ACTIVITY_STATUS_CD_TBL = "ActivityStatusCode";
    public static final String CARD_CATEGORY_TBL = "CardCategory";
    public static final String CORTEX_TYPE = "Cortex";

    //Cortex webservice tag names
    public final static String STATUS_CHANGE_RETURN      = "statusChangeReturn";
    public final static String PAN                       = "PAN";
    public final static String CARD_ID                   = "cardID";
    public final static String ACTION_CD                 = "actionCode";

    public final static String CARD_LIST_ENQUIRY_RETURN  = "cardListEnquiryReturn";
    public final static String CARD_HOLDER_PRIM_LIST     = "cardHolderCardList";
    public final static String CARD_HOLDER_SEC_LIST      = "cardHolderCardList";
    public final static String STAT_CODE                 = "statCode";
    public final static String EXP_DATE                  = "expDate";
    public final static String EMBOSS_NAME               = "embossName";
    public final static String ACCT_NO                   = "accno";
    public final static String CURR_CODE                 = "currCode";
    public final static String CRD_PRODUCT               = "crdProduct";
    public final static String PROGRAM                   = "program";
    public final static String ADDITIONAL_CUST_DET       = "additionalCustDet";
    public final static String REFERENCE                 = "reference";
    public final static String VALUE                     = "value";
    public final static String AVL_BALANCE               = "avlBal";
    public final static String BLK_AMOUNT                = "blkAmt";
    public final static String FIRST_NAME                = "firstName";
    public final static String LAST_NAME                 = "lastName";
    public final static String CARD_TYPE                 = "custType";
    public final static String DESIGN_REF                = "designRef";
    public final static String PIN_TRIES                 = "pinTries";

    public final static String CARD_LIMIT_ENQUIRY_RETURN  = "limitEnquiryReturn";
    public final static String CARD_LIMIT_DET  = "cardLimDet";
    public final static String ACCT_LIMIT_DET  = "accLimDet";
    public final static String LIMITS  = "limits";
    public final static String LIMIT_DET  = "limDet";
    public final static String LIMIT_REF  = "limRef";
    public final static String MAX_AMT  = "maxAmt";
    public final static String MIN_AMT  = "minAmt";
    public final static String CYC_AMT  = "cycAmt";

    public final static String MANAGE_PIN_RETURN      = "managePINReturn";
    public final static String CARD_APPLICATION_RETURN= "cardApplicationReturn";
}
