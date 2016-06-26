/*
 * IdConstants.java
 *
 * Created on October 21, 2004, 12:42 PM
 */
package com.fnis.xes.services;

/**
 * This class contains constants for be used for error message ids, Service Exceptions Ids and log4j ids
 *
 * @author xes team
 * 6/20/2006
 */

public class IdConstants {
    // NOTE:  See com.fnis.xes.framework.IdConstants for additional example information.

    // The domain value provides for a range of ids to be used by the services.
    private final static int XES_SERVICE_DOMAIN = 0x40000000;

    //  The domain value provides for a range of ids to be used by the services.
    private final static int SERVICE_DOMAIN = 0x4f000000;

    // Get Loan Payoff Inquiry (GLPI)
    private final static int GLPI_SOME_KIND_OF_ERROR_OR_LOG = XES_SERVICE_DOMAIN | 0x100;

    // Get Overdraft Inquiry (GODI)
    private final static int GODI_SOME_KIND_OF_ERROR_OR_LOG = XES_SERVICE_DOMAIN | 0x200;

    //Delete Stop Payment Codes

    //  Add Stop Payment Codes

    public static final int ERR_INPUT_INVALID = SERVICE_DOMAIN | 0x401;
    public static final int ERR_ACE_ERROR = SERVICE_DOMAIN | 0x405;

    // I think that you get the picture from the above examples

    // Overdraft Add error codes, using hex notation
    public static final int ERR_OBJECT_GRAPH_ERROR = XES_SERVICE_DOMAIN | 0x401;
    public static final int ERR_EMPTY_REQUEST_ERROR = XES_SERVICE_DOMAIN | 0x402;
    public static final int ERR_EMPTY_BANK_REQUEST_ERROR = XES_SERVICE_DOMAIN | 0x403;
    public static final int ERR_FUNDING_PRIORITY_NOT_NUMERIC = XES_SERVICE_DOMAIN | 0x404;
    public static final int ERR_ACE_EXCEPTION = XES_SERVICE_DOMAIN  | 0x405;
    public static final int ERR_JAXB_EXCEPTION = XES_SERVICE_DOMAIN | 0x406;
    public static final int ERR_TOO_MANY_SOURCE_ACCOUNTS = XES_SERVICE_DOMAIN | 0x407;
    public static final int ERR_FUNDING_PRIORITY_GT9 = XES_SERVICE_DOMAIN | 0x408;
    public static final int ERR_SOURCEACCT_SAME_AS_OVERDRAFTACCT = XES_SERVICE_DOMAIN | 0x409;
    public static final int ERR_OVERDRAFTACCT_NOT_A_CHECKINGACCT = XES_SERVICE_DOMAIN | 0x40A;
    public static final int ERR_DUPLICATE_FUNDING_PRIORITIES = XES_SERVICE_DOMAIN | 0x40B;
    public static final int ERR_SOURCEACCT_NOT_VALID = XES_SERVICE_DOMAIN | 0x40D;
    public static final int ERR_TOUCHPOINT_EXCEPTION = XES_SERVICE_DOMAIN | 0x40E;
    public static final int ERR_OVERDRAFTACCT_NOT_VALID = XES_SERVICE_DOMAIN | 0x40F;
    public static final int ERR_DUPLICATE_SOURCE_ACCTS = XES_SERVICE_DOMAIN | 0x410;
    public static final int ERR_MISSING_SPNAME = XES_SERVICE_DOMAIN | 0x411;
    public static final int FIELD_MAX_LENGTH_EXCEEDED = XES_SERVICE_DOMAIN | 0x412;

    public static final int ERR_OVERDRAFT_DELETE_FAILED = XES_SERVICE_DOMAIN | 0x413;
    public static final int ERR_TOO_MANY_INPUT_SOURCE_ACCOUNTS = XES_SERVICE_DOMAIN | 0x414;
    public static final int ERR_SOURCEACCT_NOT_LINKED = XES_SERVICE_DOMAIN | 0x415;
    public static final int ERR_INVALID_PRIORITY = XES_SERVICE_DOMAIN | 0x416;

    public static final int ERR_JAVA_REFLECTION_EXCEPTION=XES_SERVICE_DOMAIN | 0x417;
    public static final int ERR_UNABLE_TO_DETERMINE_SPNAME = XES_SERVICE_DOMAIN | 0x418;

    // Overdraft Inquiry error codes, using hex notation
    public static final int ERR_SOURCE_ACCT_INFO_UNAVAIL = XES_SERVICE_DOMAIN | 0x480;

    //General exception - of unknown type just in case
    public static final int ERR_UNKNOWN_EXCEPTION = XES_SERVICE_DOMAIN | 0x40C;

    //  Collateral Management Services
    public static final int ONLY_COLLATERALRECID_OR_COLLATERALID_REQUIRED_NOT_BOTH = XES_SERVICE_DOMAIN | 0x800;

    public static final int EITHER_CUSTOMERID_OR_ACCOUNTID = XES_SERVICE_DOMAIN | 0x801;
    public static final int ACCOUNTID = XES_SERVICE_DOMAIN | 0x801;
    // Stop Payment Inquiry Codes (SPI)
    public static final int SPI_ERR_UNKNOWN_HOST_NAME = XES_SERVICE_DOMAIN | 0x501;
    public static final int SPI_ERR_OBJECT_GRAPH_ERROR = XES_SERVICE_DOMAIN | 0x502;
    public static final int SPI_ERR_MISSING_ACCT_NBR = XES_SERVICE_DOMAIN | 0x503;
    public static final int SPI_ERR_ACE_EXCEPTION = XES_SERVICE_DOMAIN | 0x504;
    public static final int SPI_ERR_JAXB_EXCEPTION = XES_SERVICE_DOMAIN | 0x505;
    public static final int SPI_ERR_INVALID_OPTION_CODE = XES_SERVICE_DOMAIN | 0x506;
    public static final int STOP_PAY_INQ_INVALID_CURSOR = XES_SERVICE_DOMAIN | 0x507;
    //IFX related errors for exception filters
    public final static String IFX_SERVICE_ERRORS_OCCURRED =
            "SERVICE_ERRORS_OCCURRED";
    public final static String IFX_SERVICE_RQUID = "ifx.rquid";
    public final static int IFX_ERR_CANNOT_SET_RESPONSE_MSG =
            XES_SERVICE_DOMAIN | 0x01;
    public final static int IFX_ERR_CANNOT_GET_REQUEST_MSG =
            XES_SERVICE_DOMAIN | 0x02;
    public final static int IFX_ERR_CANNOT_DETERMINE_MSGID =
            XES_SERVICE_DOMAIN | 0x03;
    public final static int IFX_ERR_CANNOT_ADD_MESSAGE_STATUS =
            XES_SERVICE_DOMAIN | 0x04;
    public final static int IFX_ERR_CANNOT_GET_MSG_STATUS =
            XES_SERVICE_DOMAIN | 0x05;
    public final static int IFX_ERR_SERVICE_ERRORS_OCCURRED =
            XES_SERVICE_DOMAIN | 0x06;
    public final static int IFX_ERR_SERVICE_EXCEPTION_OCCURRED =
            XES_SERVICE_DOMAIN | 0x07;
    public final static int IFX_ERR_SERVICE_RT_EXCEPTION_OCCURRED =
            XES_SERVICE_DOMAIN | 0x08;
    public final static int IFX_ERR_UNKNOWN_ERROR =
            XES_SERVICE_DOMAIN | 0x09;
    public final static int IFX_ERR_UNKNOWN_CONFIG_ERROR =
            XES_SERVICE_DOMAIN | 0x0A;
    public final static int IFX_ERR_UNABLE_TO_PARSE_REQUEST =
            XES_SERVICE_DOMAIN | 0x0B;
    public final static int IFX_ERR_UNABLE_TO_SERIALIZE_RESPONSE =
            XES_SERVICE_DOMAIN | 0x0C;
    public final static int IFX_ERR_SECURITY_INVALID =
            XES_SERVICE_DOMAIN | 0x0D;
    public final static int IFX_ERR_UNKNOWN_RT_ERROR =
            XES_SERVICE_DOMAIN | 0x0E;
    public final static int IFX_ERR_INVALID_RESPONSE =
            XES_SERVICE_DOMAIN | 0x0F;
    public final static int IFX_ERR_INVALID_REQUEST_TYPE =
            XES_SERVICE_DOMAIN | 0x10;
    public final static int IFX_ERR_INVALID_IFX_ROOT_RESPONSE_TYPE =
            XES_SERVICE_DOMAIN | 0x11;
    public final static int IFX_ERR_ERROR_PROCESSING_SERVICE_WRAPPER =
            XES_SERVICE_DOMAIN | 0x12;
    public final static int IFX_ERR_NO_RESPONSE_MESSAGE_FROM_SERVICE_WRAPPER =
            XES_SERVICE_DOMAIN | 0x13;
    public final static int IFX_ERR_NO_RESPONSE_MESSAGE_FROM_SERVICE =
            XES_SERVICE_DOMAIN | 0x14;
    public final static int IFX_ERR_INVALID_CUST_LANG_PREF =
            XES_SERVICE_DOMAIN | 0x15;
    public final static int IFX_ERR_MISSING_REQUEST_HEADER =
            XES_SERVICE_DOMAIN | 0x16;
    public final static int IFX_ERR_MISSING_CREDENTIALS_HEADER =
            XES_SERVICE_DOMAIN | 0x17;


    //IFX Common Error Code
    /* Peachy!  This value is assumed if no status is returned. */
    public static final int IFX_SUCESS = 0x00;  // IFX 0 TODO: REMOVEME
    public static final int IFX_SUCCESS = 0x00;  // IFX 0
    /* Unknown error, no additional information is provided. */
    public static final int IFX_GENERAL_ERROR_NO_DATA = 0x64; // IFX 100
    /* Error code indicating that additional info is available. */
    public static final int IFX_GENERAL_ERROR_DATA = 0xFFFFFF9C;  // IFX -100
    /* catastrophic data error.  No data, bad format, wrong type, etc... */
    public static final int IFX_GENERAL_DATA_ERROR = 0xC8; // IFX 200
    public static final int IFX_SYSTEM_NOT_AVAILABLE = 0x12C;  // IFX 300
    public static final int IFX_UNSUPPORTED_MESSAGE = 0x258; // IFX 600
    public static final int IFX_UNSUPPORTED_FUNCTION = 0x2BC; // IFX 700
    public static final int IFX_REQUIRED_DATA_MISSING = 0x3FC; // IFX 1020
    /* Invalid State/City */
    public static final int IFX_INVALID_STATE_PROVINCE = 0x604; // IFX 1540
    public static final int IFX_RECORD_CONTROL_ACTIVE = 1110;   // IFX 1110

    public static final int IFX_NO_RECORD_FOUND = 0x460; // IFX 1120
    public static final int IFX_SPNAME_INVALID = 0x550; // IFX 1360
    public static final int IFX_AUTHORIZATION_FAILURE = 0x6E0; // IFX 1760
    /* Non-numeric or missing data was encountered in a single or low amount field. */
    public static final int IFX_INVALID_DATA = 0x7D0; // IFX 2000
    /* Non-numeric or missing data was encountered in a high amount field. */
    public static final int IFX_INVALID_DATA_HIGH = 0x7DA; // IFX 2010
    public static final int IFX_AMOUNT_TOO_SMALL = 0x7E4; // IFX 2020
    public static final int IFX_AMOUNT_TOO_LARGE = 0x7EE; // IFX 2030
    /* low amount is greater than high amount in amount range */
    public static final int IFX_INVALID_AMOUNT_RANGE = 0x80C; // IFX 2060
    /* single or low-range date is invalid */
    public static final int IFX_INVALID_DATETIME = 0x834; // IFX 2100
    /* high-range date is invalid */
    public static final int IFX_INVALID_DATETIME_HIGH = 0x83E; // IFX 2110
    /* date range is invalid (eg. low date exceeeds high date). */
    public static final int IFX_INVALID_DATETIME_RANGE = 0x848; // IFX 2120
    public static final int IFX_DATETIME_PASSED = 0x852; // IFX 2130
    public static final int IFX_DATETIME_TOO_SOON = 0x85C; // IFX 2140
    public static final int IFX_DATETIME_TOO_FAR_FUTURE = 0x866; // IFX 2150
    public static final int IFX_INVALID_ACCOUNT = 0x8FC; // IFX 2300
    public static final int IFX_INVALID_ACCOUNT_TYPE = 0x988; // IFX 2440 //Added for error mapping framework by Sankar LC6118
    public static final int IFX_INVALID_CHECK_NBR_RANGE = 0xA28; // IFX 2600
    public static final int IFX_INVALID_CURRENCY_CODE = 0xAB4; // IFX 2740

    public static final int IFX_INVALID_ENUM_VALUE = 0x41A; // IFX 1050
    public static final int IFX_INVALID_VALUE = 0xFFFFD7E7; // IFX -10265
    public static final int IFX_SOURCE_DESTINATION_ACCTS_IDENTICAL = 0x942; //IFX 2370

    public static final int IFX_NEGATIVE_AMOUNT = 0x19F; // IFX 415

    public static final int IFX_USER_ID_LOCKED = 0x758; // IFX 1880


    // Account Number Parser codes.
    public static final int ACCT_PARSER_NO_INPUT = XES_SERVICE_DOMAIN | 0x1001;
    public static final int ACCT_PARSER_INVALID_LENGTH = XES_SERVICE_DOMAIN | 0x1002;
    public static final int ACCT_PARSER_APP_ID_NOT_RECOGNIZED = XES_SERVICE_DOMAIN | 0x1003;
    public static final int ACCT_PARSER_ACCT_NBR_IS_REQUIRED = XES_SERVICE_DOMAIN | 0x1004;
    public static final int ACCT_PARSER_CONTROLS_REQUIRED = XES_SERVICE_DOMAIN | 0x1005;
    public static final int ACCT_PARSER_NOT_NUMERIC = XES_SERVICE_DOMAIN | 0x1006;
    public static final int ACCT_PARSER_APPID_NULL = XES_SERVICE_DOMAIN | 0x1007;

    // DateUtil codes
    public static final int DATE_UTIL_EMPTY_INPUT = XES_SERVICE_DOMAIN | 0x1011;
    public static final int DATE_UTIL_PARSE_ERROR = XES_SERVICE_DOMAIN | 0x1012;

    // TSMDA and ATSMDA Helper error codes
    public static final int TSMDA_INVALID_NUMBER = XES_SERVICE_DOMAIN | 0x1100;
    public static final int HOST_ERROR = -1000;
    public static final int HOST_WARN = -1001;
    public static final int HOST_INFO = 0;
    // IFX Bulk Service processing errors
    // Bulk Service configuration file ${0} not found
    public static final int SVCBULK_CONFIG_NOT_FOUND = XES_SERVICE_DOMAIN | 0x11A0;
    // Error parsing Bulk Service configuration file ${0}
    public static final int SVCBULK_PARSE_ERROR = XES_SERVICE_DOMAIN | 0x11A1;
    // The ability init param "ConfigFile" must point to a bulk service configuration file
    // that can be located on the classpath.
    public static final int SVCBULK_PARAMETER_REQUIRED = XES_SERVICE_DOMAIN | 0x11A2;

    //Get Loan Pay Off IFX property error
    public static final int IFX_PROP_VALUE_INVALID = XES_SERVICE_DOMAIN | 0x1200;

    // Parameters (data mapping) error codes
    public static final int PARMS_FAILED_TO_INSTANTIATE_SPC = XES_SERVICE_DOMAIN | 0x1300;
    public static final int PARMS_KEY_TYPE_INVALID = XES_SERVICE_DOMAIN | 0x1301;


    // Loan Account Info error codes.
    public static final int LAI_LOAN_ACCT_ID_REQUIRED = XES_SERVICE_DOMAIN | 0x201;
    public static final int LAI_ACCOUNT_NBR_REQUIRED = XES_SERVICE_DOMAIN | 0x202;
    public static final int LAI_OBJECT_GRAPH_ERROR = XES_SERVICE_DOMAIN | 0x203;
    public static final int LAI_ACE_PROXY_ERROR = XES_SERVICE_DOMAIN | 0x204;
    public static final int LAI_UNSUPPORTED_CPA = XES_SERVICE_DOMAIN | 0x205;

    // Transaction History Inquiry error codes.

    public static final int TRANSACTION_HISTORY_INQ_INVALID_SEARCH_KEY_TYPE = XES_SERVICE_DOMAIN | 0x1400;
    public static final int TRANSACTION_HISTORY_INQ_MISSING_SEARCH_KEY_TYPE = XES_SERVICE_DOMAIN | 0x1401;

    public static final int TRANSACTION_HISTORY_INQ_INVALID_PRODUCT_KEYWORD = XES_SERVICE_DOMAIN | 0x1402;
    public static final int TRANSACTION_HISTORY_INQ_INVALID_TRN_SEQ_NBR = XES_SERVICE_DOMAIN | 0x1403;
    public static final int TRANSACTION_HISTORY_INQ_INVALID_EFF_POST_DT_IND = XES_SERVICE_DOMAIN | 0x1404;
    public static final int TRANSACTION_HISTORY_INQ_INVALID_LAST_N_TRAN = XES_SERVICE_DOMAIN | 0x1405;
    public static final int TRANSACTION_HISTORY_INQ_INVALID_CIF_NBR = XES_SERVICE_DOMAIN | 0x1406;

    public static final int TRANSACTION_HISTORY_INQ_MULT_SEARCH_KEY_TYPE_NOT_SUPPORTED = XES_SERVICE_DOMAIN | 0x1407;
    public static final int TRANSACTION_HISTORY_INQ_LOAN_ACCT_ID_REQUIRED = XES_SERVICE_DOMAIN | 0x1408;
    public static final int TRANSACTION_HISTORY_INQ_ACCOUNT_NBR_REQUIRED = XES_SERVICE_DOMAIN | 0x1409;
    public static final int TRANSACTION_HISTORY_INQ_CHK_RANGE_NOT_SUPPORTED = XES_SERVICE_DOMAIN | 0x140A;

    public static final int TRANSACTION_HISTORY_INQ_INVALID_ACCOUNT_TYPE = XES_SERVICE_DOMAIN | 0x140B;
    public static final int TRANSACTION_HISTORY_INQ_INVALID_PAGE_SIZE = XES_SERVICE_DOMAIN | 0x140C;
    public static final int TRANSACTION_HISTORY_INQ_INVALID_CURSOR = XES_SERVICE_DOMAIN | 0x140D;

    // Get Retirement Info error codes

    public static final int PLAN_PARSER_INVALID_LENGTH = XES_SERVICE_DOMAIN | 0x1500;
    public static final int INVALID_PLAN_NUMBER = XES_SERVICE_DOMAIN | 0x1501;
    public static final int INVALID_PLAN_SEQ_NUMBER = XES_SERVICE_DOMAIN | 0x1502;
    public static final int INVALID_EMPLOYEE_ID = XES_SERVICE_DOMAIN | 0x1503;
    public static final int INVALID_PLAN_TYPE = XES_SERVICE_DOMAIN | 0x1504;
    public static final int INVALID_TAX_IDENTIFICATION_NUM = XES_SERVICE_DOMAIN | 0x1505;
    public static final int INVALID_TAX_IDENTIFICATION_TYPE = XES_SERVICE_DOMAIN | 0x1506;
    public static final int INVALID_BENEFICIARY_ID = XES_SERVICE_DOMAIN | 0x1507;
    public static final int INVALID_BENEFICIARY_NAME = XES_SERVICE_DOMAIN | 0x1508;
    public static final int INVALID_BENEFICIARY_TYPE = XES_SERVICE_DOMAIN | 0x1509;
    public static final int INVALID_BENEFICIARY_RECORDS = XES_SERVICE_DOMAIN | 0x1510;
    public static final int PLAN_NUMBER_NOT_NUMERIC = XES_SERVICE_DOMAIN | 0x150A;

    // Get Loan Facility Error codes
    public static final int LOAN_FACILITY_INQ_INVALID_FACILITY_ACCOUNT = XES_SERVICE_DOMAIN | 0x1600;
    public static final int LOAN_FACILITY_INQ_NULL_SPACES_FACILITY_ACCOUNT = XES_SERVICE_DOMAIN | 0x1601;
    public static final int LOAN_FACILITY_FACILITY_ACCOUNT_EXCEEDS_30_CHARACTERS = XES_SERVICE_DOMAIN | 0x1602;
    public static final int LOAN_FACILITY_FACILITY_CLOSED_ACCOUNT = XES_SERVICE_DOMAIN | 0x1603;
    public static final int LOAN_FACILITY_FACILITY_ACCOUNT_INVALID_DATA_TYPE = XES_SERVICE_DOMAIN | 0x1604;
    public static final int LOAN_FACILITY_LIMIT_TYPE_INVALID = XES_SERVICE_DOMAIN | 0x1605;
    public static final int LOAN_FACILITY_LIMIT_TYPE_EXCEEDS_5_CHARACTERS = XES_SERVICE_DOMAIN | 0x1606;
    public static final int LOAN_FACILITY_LIMIT_TYPE_INVALID_DATA_TYPE = XES_SERVICE_DOMAIN | 0x1607;
    public static final int LOAN_FACILITY_PORTFOLIO_ID_REQUIRED = XES_SERVICE_DOMAIN | 0x1608;
    public static final int LOAN_FACILITY_PORTFOLIO_ID_INVALID = XES_SERVICE_DOMAIN | 0x1609;
    public static final int LOAN_FACILITY_PORTFOLIO_ID_EXCEEDS_2_CHARACTERS = XES_SERVICE_DOMAIN | 0x160A;
    public static final int LOAN_FACILITY_CANCELLED = XES_SERVICE_DOMAIN | 0x160B;

    // Audit log codes
    public static final int AUDIT_MESSAGE_PARSING_ERROR=XES_SERVICE_DOMAIN | 0x1700;
    public static final int NO_PROPER_SERVICE_ELEMENT=XES_SERVICE_DOMAIN | 0x1701;
    public static final int INVALID_SECURITY_TOKEN=XES_SERVICE_DOMAIN | 0x1702;
    public static final int CANNOT_LOAD_AUDITCONF_XML=XES_SERVICE_DOMAIN | 0x1703;
    public static final int AUDITCONF_XML_FORMAT_ERROR=XES_SERVICE_DOMAIN | 0x1704;
    public static final int AUDIT_UNKNOWN_MESSAGE_TYPE=XES_SERVICE_DOMAIN | 0x1705;
    public static final int AUDIT_DATABASE_ERROR=XES_SERVICE_DOMAIN | 0x1706;
    public static final int AUDIT_JNDI_LOOKUP_ERROR=XES_SERVICE_DOMAIN | 0x1707;
    public static final int AUDIT_JMS_ERROR=XES_SERVICE_DOMAIN | 0x1708;
    public static final int AUDIT_ERROR=XES_SERVICE_DOMAIN | 0x1709;

    //Funds Transfer Message codes
    public static final int XFER_FROM_ACCT_TYPE_REQUIRED = XES_SERVICE_DOMAIN | 0x4001;
    public static final int XFER_TO_ACCT_TYPE_REQUIRED   = XES_SERVICE_DOMAIN | 0x4002;
    public static final int XFER_ERR_SAME_ACCT_TYPE      = XES_SERVICE_DOMAIN | 0x4003;
    public static final int XFER_INVALID_ACCT_TYPE       = XES_SERVICE_DOMAIN | 0x4004;
    public static final int XFER_TRANS_AMNT_NEGATIVE     = XES_SERVICE_DOMAIN | 0x4005;
    public static final int XFER_ACBS_INVALID_ACCOUNT_REVERSAL = XES_SERVICE_DOMAIN | 0x4006;
    public static final int XFER_ACBS_REVERSAL_SERVICE_ERROR = XES_SERVICE_DOMAIN | 0x4007;
    public static final int XFER_ACBS_REVERSAL_HOST_ERROR = XES_SERVICE_DOMAIN | 0x4008;
    public static final int XFER_PYMT_TYPE_INVALID = XES_SERVICE_DOMAIN | 0x4009;
    public static final int XFER_PYMT_TYPE_EXCEEDS_17_CHARACTERS = XES_SERVICE_DOMAIN | 0x400A;
    public static final int XFER_PAYEE_REQUIRED = XES_SERVICE_DOMAIN | 0x400B;
    public static final int XFER_BRANCHID_REQUIRED = XES_SERVICE_DOMAIN | 0x400C;
    public static final int XFER_NO_USERTRAN_FOUND = XES_SERVICE_DOMAIN | 0x400D;
    public static final int XFER_DBCONNECTION_ERROR = XES_SERVICE_DOMAIN | 0x400E;
    public static final int XFER_INVALID_ACCT_NUMBER = XES_SERVICE_DOMAIN | 0x400F;
    public static final int INVALID_RTP_ACCT_NUMBER = XES_SERVICE_DOMAIN | 0x401F;
    // Message codes for Error Mapping System.
    public static final int MAPPING_NO_HOST_APP_ID = XES_SERVICE_DOMAIN | 0x5001;
    public static final int MAPPING_INVALID_HOST_ERROR_OBJECT_TYPE = XES_SERVICE_DOMAIN | 0x5002;
    public static final int MAPPING_UNSUPPORTED_HOST_ERROR_OBJECT = XES_SERVICE_DOMAIN | 0x5003;
    public static final int MAPPING_NO_IFX_MAPPING_FOR_HOST = XES_SERVICE_DOMAIN | 0x5004;
    public static final int MAPPING_IFX_MSG_NOT_FOUND = XES_SERVICE_DOMAIN | 0x5005;
    public static final int MAPPING_DATA_ACCESS_ERROR = XES_SERVICE_DOMAIN | 0x5006;
    public static final int MAPPING_JAXB_ERROR = XES_SERVICE_DOMAIN | 0x5007;

    //  message codes for SystemHealthInq
    public static final int SYSCHK_NO_CONFIG        = XES_SERVICE_DOMAIN | 0x6001;
    public static final int SYSCHK_DOC_PARSE_ERROR  = XES_SERVICE_DOMAIN | 0x6002;
    public static final int SYSCHK_PARSE_CONFIG_ERR = XES_SERVICE_DOMAIN | 0x6003;
    public static final int SYSCHK_IO_ERROR         = XES_SERVICE_DOMAIN | 0x6004;
    public static final int SYSCHK_NO_REQUEST       = XES_SERVICE_DOMAIN | 0x6005;

    // boc errors
    public static final int BOC_CONFIG_ERR = XES_SERVICE_DOMAIN | 0x9001;
    public static final int BOC_EXCEPTION  = XES_SERVICE_DOMAIN | 0x9002;
    public static final int BOC_CONTAINER_ERR = XES_SERVICE_DOMAIN | 0x9003;

    // JEF errors (0x9100-0x913F inclusive)
    public static final int JEF_EXCEPTION = XES_SERVICE_DOMAIN | 0x9100;

    //  Customer Number Parser codes.
    public static final int CUST_PARSER_NO_INPUT = XES_SERVICE_DOMAIN | 0x2001;
    public static final int CUST_PARSER_INVALID_LENGTH = XES_SERVICE_DOMAIN | 0x2002;
    public static final int CUST_PARSER_APP_ID_NOT_RECOGNIZED = XES_SERVICE_DOMAIN | 0x2003;
    public static final int CUST_PARSER_CUST_NBR_IS_REQUIRED = XES_SERVICE_DOMAIN | 0x2004;
    public static final int CUST_PARSER_CONTROLS_REQUIRED = XES_SERVICE_DOMAIN | 0x2005;
    public static final int CUST_PARSER_NOT_NUMERIC = XES_SERVICE_DOMAIN | 0x2006;
    public static final int CUST_PARSER_APPID_NULL = XES_SERVICE_DOMAIN | 0x2007;

    //  Pagination and Caching Error codes
    public final static int ERR_INVALID_PAGESIZE =    XES_SERVICE_DOMAIN |0x2008;
    public final static int ERR_INVALID_STARTINDEX =  XES_SERVICE_DOMAIN |0x2009;
    public static final int ERR_CACHE_EXCEPTION = XES_SERVICE_DOMAIN | 0x2010;

    // GN Connection Exception
    public static final int ERR_GN_CON_ERROR = SERVICE_DOMAIN | 0x2011;
    public static final int ERR_UCS_CON_ERROR = SERVICE_DOMAIN | 0x2012;
    public static final int ERR_PF_CON_ERROR = SERVICE_DOMAIN | 0x2013;
    public static final int ERR_JCA_PROXY_CONVERSION_EXCEPTION = SERVICE_DOMAIN | 0x2014;

    public static final int ERR_INVALID_OBJECT =  XES_SERVICE_DOMAIN | 0x2015;
    public static final int ERR_HOST_ADAPTER_OBJECT =  XES_SERVICE_DOMAIN | 0x2016;

    public static final int ERR_NUMBER_FORMAT =  XES_SERVICE_DOMAIN | 0x2017;
    public static final int ERR_CACHE_OBJECT = -1010;
    public static final int ERR_GN_CON_ABL_OBJECT =  XES_SERVICE_DOMAIN | 0x2019;
    public static final int ERR_UCS_ABL_OBJECT =  XES_SERVICE_DOMAIN | 0x2020;
    public static final int ERR_INVALID_NUMBER =  XES_SERVICE_DOMAIN | 0x2021;
    public static final int ERR_CURSOR_OBJECT = XES_SERVICE_DOMAIN | 0x2022;
    public static final int NO_IFX_MAPPING = XES_SERVICE_DOMAIN | 0x2023;
    public static final int ERR_PRF_ABL_OBJECT =  XES_SERVICE_DOMAIN | 0x2024;
    public static final int ERR_EFX_CON_ABL_OBJECT =  XES_SERVICE_DOMAIN | 0x2601;

    //  Customer Inquiry UCS Customer Type Invalid
    public static final int CUSTOMER_TYPE_INVALID = XES_SERVICE_DOMAIN | 0x7001;

    public static final int INVALID_CUSTOMER_NUMBER = XES_SERVICE_DOMAIN | 0x7002;

    //Holds Add
    public static final int ERR_BOTH_DAYS_AND_EXPDT_ENTERED =  XES_SERVICE_DOMAIN | 0x2025;
    public static final int RECORD_ALREADY_EXISTS=XES_SERVICE_DOMAIN | 0x2026;
    public static final int ERR_AMT_OR_PERCENT =  XES_SERVICE_DOMAIN | 0x2027;

    //Date Funds Transfer
    public static final int ERR_INVALID_FREQUENCY = XES_SERVICE_DOMAIN | 0x7003;
    public static final int ERR_INVALID_REFID = XES_SERVICE_DOMAIN | 0x7004;
    public static final int ERR_INVALID_PMT_METHOD = XES_SERVICE_DOMAIN | 0x7005;
    public static final int ERR_INVALID_REFINFO = XES_SERVICE_DOMAIN | 0x7006;
    public static final int ERR_INVALID_DATE    = XES_SERVICE_DOMAIN | 0x7007;
    public static final int INVALID_LOW_OR_HIGH_INCOME_AMT  = XES_SERVICE_DOMAIN | 0x7008;
    // Login authentication and services
    public static final int ERR_JEF_SECURITY_EXCEPTION=XES_SERVICE_DOMAIN | 0x2300;
    public static final int ERR_SECURITY_AUDIT_EXCEPTION=XES_SERVICE_DOMAIN | 0x2301;
    public static final int ERR_AUTHENTICATION_FAILED=XES_SERVICE_DOMAIN | 0x2302;
    public static final int ERR_UNKNOWN_USER_ID=XES_SERVICE_DOMAIN | 0x2303;
    public static final int ERR_XES_SECURITY_EXCEPTION=XES_SERVICE_DOMAIN | 0x2304;

    //NonUseInq
    public static final int ERR_MISSING_START_DATE=XES_SERVICE_DOMAIN | 0x2310;

    // User Id Policy
    public static final int CHAR_SET_NAME_MISSING=XES_SERVICE_DOMAIN | 0x2400;
    public static final int INVALID_CHAR_SET_NAME=XES_SERVICE_DOMAIN | 0x2401;
    public static final int CHAR_SET_IS_EMPTY=XES_SERVICE_DOMAIN | 0x2402;
    public static final int USER_ID_TYPE_MISSING=XES_SERVICE_DOMAIN | 0x2403;
    public static final int INVALID_CHAR_SET_EXPRESSION=XES_SERVICE_DOMAIN | 0x2404;
    public static final int INVALID_LENGTH_FIGURE=XES_SERVICE_DOMAIN | 0x2405;
    public static final int USER_ID_POLICY_NAME_IS_MISSING=XES_SERVICE_DOMAIN | 0x2406;
    public static final int UNKNOWN_USER_ID_POLICY_NAME=XES_SERVICE_DOMAIN | 0x2407;
    public static final int INVALID_USER_ID_TYPE=XES_SERVICE_DOMAIN | 0x2408;
    public static final int CANNOT_MODIFY_USER_ID_TYPE=XES_SERVICE_DOMAIN | 0x2409;

    public static final int USER_ID_TYPE_ALREADY_EXISTS=XES_SERVICE_DOMAIN | 0x240A;
    public static final int CHAR_SET_FOR_FIRST_CHAR_ALREADY_EXISTS=XES_SERVICE_DOMAIN | 0x240B;
    public static final int CHAR_SET_ALREADY_EXISTS=XES_SERVICE_DOMAIN | 0x240C;
    public static final int CHAR_SET_NAME_ALREADY_WITH_USER_ID_TYPE=XES_SERVICE_DOMAIN | 0x240D;
    public static final int EXPRESSION_ALREADY_EXISTS=XES_SERVICE_DOMAIN | 0x240E;
    public static final int MAX_LENGTH_ALREADY_SET=XES_SERVICE_DOMAIN | 0x240F;
    public static final int MIN_LENGTH_ALREADY_SET=XES_SERVICE_DOMAIN | 0x2410;
    public static final int MAX_LENGTH_ON_THE_CHAR_SET_AREADY_SET=XES_SERVICE_DOMAIN | 0x2411;
    public static final int MIN_LENGTH_ON_THE_CHAR_SET_AREADY_SET=XES_SERVICE_DOMAIN | 0x2412;

    public static final int CHAR_SET_FOR_FIRST_CHAR_DOES_NOT_EXIST=XES_SERVICE_DOMAIN | 0x2413;
    public static final int CHAR_SET_DOES_NOT_EXIST=XES_SERVICE_DOMAIN | 0x2414;
    public static final int EXPRESSION_DOES_NOT_EXIST=XES_SERVICE_DOMAIN | 0x2415;
    public static final int MAX_LENGTH_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x2416;
    public static final int MIN_LENGTH_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x2417;
    public static final int MAX_LENGTH_ON_THE_CHAR_SET_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x2418;
    public static final int MIN_LENGTH_ON_THE_CHAR_SET_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x2419;

    //PermissionInq
    public static final int INQUIRY_CRITERIUM_MISSING=XES_SERVICE_DOMAIN | 0x2420;
    public static final int INVALID_PERMISSION_NAME=XES_SERVICE_DOMAIN | 0x2421;

    //UserMod
    public static final int USER_NOT_FOUND=XES_SERVICE_DOMAIN | 0x2422;
    public static final int USER_MOD_SERVICE_CANNOT_CHANGE_USER_ID_TYPE=XES_SERVICE_DOMAIN | 0x2428;
    //UserAdd
    public static final int USER_ALREADY_EXISTS=XES_SERVICE_DOMAIN | 0x2423;
    public static final int ERR_DATABASE_EXCEPTION=XES_SERVICE_DOMAIN | 0x2424;
    public static final int CUSTOMER_NUMBER_NOT_UNIQUE_FOR_RETAIL_USER=XES_SERVICE_DOMAIN |0x2429;
    public static final int CUSTOMER_NUMBER_IS_ALREADY_ASSIGNED_TO_RETAIL_USER=XES_SERVICE_DOMAIN |0x242A;

    //UserIdMod
    public static final int OLD_USER_DOES_NOT_EXIST=XES_SERVICE_DOMAIN | 0x2425;
    public static final int NEW_USER_ALREADY_EXISTS=XES_SERVICE_DOMAIN | 0x2426;
    //PswdMod
    public static final int INVALID_CREDENTIAL=XES_SERVICE_DOMAIN | 0x2427;

    public static final int FINE=0;
    public static final int USER_ID_IS_TOO_LONG=XES_SERVICE_DOMAIN | 0x2430;
    public static final int USER_ID_IS_TOO_SHORT=XES_SERVICE_DOMAIN | 0x2431;;
    public static final int TOO_MANY_CHARACTERS_IN_USER_ID_ARE_FROM_CHARACTER_SET=XES_SERVICE_DOMAIN | 0x2432;
    public static final int TOO_FEW_CHARACTERS_IN_USER_ID_ARE_FROM_CHARACTER_SET=XES_SERVICE_DOMAIN | 0x2433;
    public static final int USER_ID_CONTAINS_INVALID_CHARACTER=XES_SERVICE_DOMAIN | 0x2434;
    public static final int USER_ID_CONTAINS_CHARACTER_THAT_IN_INVALID_CHAR_SET=XES_SERVICE_DOMAIN | 0x2435;
    public static final int INVALID_FIRST_CHARACTER=XES_SERVICE_DOMAIN | 0x2436;
    public static final int CHARACTERS_IN_USER_ID_DO_NOT_COVER_ALL_REQUIRED_CHAR_SET=XES_SERVICE_DOMAIN | 0x2437;
    public static final int USER_ID_EMPTY=XES_SERVICE_DOMAIN | 0x2438;

    // password policy
    public static final int PASSWORD_POLICY_NAME_IS_MISSING=XES_SERVICE_DOMAIN | 0x2440;
    public static final int PASSWORD_TYPE_MISSING=XES_SERVICE_DOMAIN | 0x2441;
    public static final int PASSWORD_TYPE_ALREADY_EXISTS=XES_SERVICE_DOMAIN | 0x2442;
    public static final int INVALID_PASSWORD_TYPE=XES_SERVICE_DOMAIN | 0x2443;
    public static final int CHAR_SET_NAME_ALREADY_WITH_PASSWORD_TYPE=XES_SERVICE_DOMAIN | 0x2444;
    public static final int UNKNOWN_PASSWORD_POLICY_NAME=XES_SERVICE_DOMAIN | 0x2445;
    public static final int MAX_SUCCESSIVE_IDENTICAL_CHAR_ALREADY_EXISTS=XES_SERVICE_DOMAIN | 0x2446;
    public static final int FORBIDDEN_STRING_IS_NOT_PROVIDED=XES_SERVICE_DOMAIN | 0x2447;
    public static final int FORBIDDEN_STRING_ALREADY_EXISTS=XES_SERVICE_DOMAIN | 0x2448;
    public static final int MAX_ASCENDING_NUMBER_IS_AREADY_SET=XES_SERVICE_DOMAIN | 0x2449;
    public static final int MAX_DESCENDING_NUMBER_IS_AREADY_SET=XES_SERVICE_DOMAIN | 0x244A;
    public static final int MAX_CHAR_FROM_USER_ID_IS_AREADY_SET=XES_SERVICE_DOMAIN | 0x244B;
    public static final int MAX_CHAR_FROM_FIRST_NAME_IS_AREADY_SET=XES_SERVICE_DOMAIN | 0x244C;
    public static final int MAX_CHAR_FROM_LAST_NAME_IS_AREADY_SET=XES_SERVICE_DOMAIN | 0x244D;
    public static final int MAX_CHAR_FROM_MIDDLE_NAME_IS_AREADY_SET=XES_SERVICE_DOMAIN | 0x244E;
    public static final int MAX_CHAR_FROM_PREV_PSWD_IS_AREADY_SET=XES_SERVICE_DOMAIN | 0x244F;
    public static final int MAX_LIFE_IS_AREADY_SET=XES_SERVICE_DOMAIN | 0x2450;
    public static final int MAX_HISTORY_IS_AREADY_SET=XES_SERVICE_DOMAIN | 0x2451;
    public static final int PASSWORD_TYPE_DOES_NOT_EXIST=XES_SERVICE_DOMAIN | 0x2452;
    public static final int MAX_HISTORY_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x2453;
    public static final int MAX_LIFE_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x2454;
    public static final int MAX_CHAR_FROM_PREV_PSWD_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x2455;
    public static final int MAX_CHAR_FROM_MIDDLE_NAME_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x2456;
    public static final int MAX_CHAR_FROM_LAST_NAME_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x2457;
    public static final int MAX_CHAR_FROM_FIRST_NAME_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x2458;
    public static final int MAX_CHAR_FROM_USER_ID_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x2459;
    public static final int MAX_DESCENDING_NUMBER_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x245A;
    public static final int MAX_ASCENDING_NUMBER_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x245B;
    public static final int FORBIDDEN_STRING_CANNOT_BE_MODIFIED=XES_SERVICE_DOMAIN | 0x245C;
    public static final int MAX_SUCCESSIVE_IDENTICAL_CHAR_DOES_NOT_EXIST=XES_SERVICE_DOMAIN | 0x245D;
    public static final int PASSWORD_TYPE_CANNOT_BE_MODIFIED=XES_SERVICE_DOMAIN | 0x245E;
    public static final int FORBIDDEN_STRING_DOES_NOT_EXIST=XES_SERVICE_DOMAIN | 0x245F;
    public static final int INVALID_VALUE=XES_SERVICE_DOMAIN | 0x2460;

    public static final int PASSWORD_IS_TOO_LONG=XES_SERVICE_DOMAIN | 0x2470;
    public static final int PASSWORD_IS_TOO_SHORT=XES_SERVICE_DOMAIN | 0x2471;;
    public static final int PASSWORD_IS_EMPTY=XES_SERVICE_DOMAIN | 0x2472;
    public static final int PASSWORD_HAS_A_SUCCESSIVE_IDENTICAL_CHARACTER_SEQUENCE_TOO_LONG=XES_SERVICE_DOMAIN | 0x2473;
    public static final int TOO_MANY_CHARACTERS_IN_PASSWORD_ARE_FROM_USER_ID=XES_SERVICE_DOMAIN | 0x2474;
    public static final int TOO_MANY_CHARACTERS_IN_PASSWORD_ARE_FROM_USER_FIRST_NAME=XES_SERVICE_DOMAIN | 0x2475;
    public static final int TOO_MANY_CHARACTERS_IN_PASSWORD_ARE_FROM_USER_LAST_NAME=XES_SERVICE_DOMAIN | 0x2476;
    public static final int TOO_MANY_CHARACTERS_IN_PASSWORD_ARE_FROM_USER_MIDDLE_NAME=XES_SERVICE_DOMAIN | 0x2477;
    public static final int TOO_MANY_CHARACTERS_IN_PASSWORD_ARE_FROM_USER_PREVIOUS_PASSWORD=XES_SERVICE_DOMAIN | 0x2478;
    public static final int PASSWORD_HAS_AN_ASCENDING_SEQUENCE_TOO_LONG=XES_SERVICE_DOMAIN | 0x2479;
    public static final int PASSWORD_HAS_A_DESCENDING_SEQUENCE_TOO_LONG=XES_SERVICE_DOMAIN | 0x247A;
    public static final int PASSWORD_CONTAINS_FORBIDDEN_STRING=XES_SERVICE_DOMAIN | 0x247B;
    public static final int PASSWORD_CONTAINS_INVALID_CHARACTER=XES_SERVICE_DOMAIN | 0x247C;
    public static final int PASSWORD_CONTAINS_CHARACTER_FROM_INVALID_CHAR_SET=XES_SERVICE_DOMAIN | 0x247D;
    public static final int CHARACTERS_IN_PASSWORD_DO_NOT_COVER_ALL_REQUIRED_CHAR_SET=XES_SERVICE_DOMAIN | 0x247E;
    public static final int TOO_MANY_CHARACTERS_IN_PASSWORD_ARE_FROM_A_CHARACTER_SET=XES_SERVICE_DOMAIN | 0x247F;
    public static final int TOO_FEW_CHARACTERS_IN_PASSWORD_ARE_FROM_A_CHARACTER_SET=XES_SERVICE_DOMAIN | 0x2480;

    public static final int PASSWORD_CANNOT_BE_SAME_ITEM_IS_AREADY_SET=XES_SERVICE_DOMAIN | 0x2481;
    public static final int PASSWORD_CANNOT_BE_SAME_ITEM_IS_NOT_SET=XES_SERVICE_DOMAIN | 0x2482;
    public static final int PASSWORD_IS_THE_SAME_AS_PREVIOUS_PASSWORD=XES_SERVICE_DOMAIN | 0x2483;
    public static final int PASSWORD_IS_THE_SAME_AS_USER_ID=XES_SERVICE_DOMAIN | 0x2484;
    public static final int PASSWORD_IS_THE_SAME_AS_FIRST_NAME=XES_SERVICE_DOMAIN | 0x2485;
    public static final int PASSWORD_IS_THE_SAME_AS_LAST_NAME=XES_SERVICE_DOMAIN | 0x2486;
    public static final int PASSWORD_IS_THE_SAME_AS_MIDDLE_NAME=XES_SERVICE_DOMAIN | 0x2487;

    public static final int NOT_SUFFICIENT_AUTHORITY=XES_SERVICE_DOMAIN | 0x2488;
    public static final int UNKNOWN_ENTITY_TYPE=XES_SERVICE_DOMAIN | 0x2489;

    // User Role Add, Mod, Del, Inq
    public static final int INVALID_ROLE_NAME=XES_SERVICE_DOMAIN | 0x2500;
    public static final int UNKNOWN_ROLE=XES_SERVICE_DOMAIN | 0x2501;
    public static final int INVALID_USER_ID=XES_SERVICE_DOMAIN | 0x2502;
    public static final int UNKNOWN_USER=XES_SERVICE_DOMAIN | 0x2503;
    public static final int USER_ALREADY_ASSIGNED_TO_ROLE=XES_SERVICE_DOMAIN | 0x2504;
    public static final int USER_IS_NOT_ASSIGNED_TO_ROLE=XES_SERVICE_DOMAIN | 0x2505;
    public static final int INVALID_USER_CREDINTIAL=XES_SERVICE_DOMAIN | 0x2506;

    // Role permission Add, Del
    public static final int UNKNOWN_PERMISSION=XES_SERVICE_DOMAIN | 0x2510;
    public static final int PERMISSION_ALREADY_ASSIGNED_TO_ROLE=XES_SERVICE_DOMAIN | 0x2511;
    public static final int PERMISSION_IS_NOT_ASSIGNED_TO_ROLE=XES_SERVICE_DOMAIN | 0x2512;
    // User permission Add, Del
    public static final int PERMISSION_ALREADY_ASSIGNED_TO_USER=XES_SERVICE_DOMAIN | 0x2520;
    public static final int PERMISSION_IS_NOT_ASSIGNED_TO_USER=XES_SERVICE_DOMAIN | 0x2521;

    //UserAdd, UserMod, UserInq, UserDel new features
    public static final int EMAIL_TYPE_NOT_UNIQUE=XES_SERVICE_DOMAIN | 0x2530;
    public static final int PHONE_TYPE_NOT_UNIQUE=XES_SERVICE_DOMAIN | 0x2531;
    public static final int ADDRESS_TYPE_NOT_UNIQUE=XES_SERVICE_DOMAIN | 0x2532;
    public static final int PHONE_TYPE_NOT_VALID=XES_SERVICE_DOMAIN | 0x2533;
    public static final int ADDRESS_TYPE_NOT_VALID=XES_SERVICE_DOMAIN | 0x2534;

    //Added by Rohit 04/10/2006 - User Account Ownership validation.
    public static final int LOGGED_IN_USER_NOT_OWNER_OF_ACCOUNT=XES_SERVICE_DOMAIN | 0x2535;

    public static final int ERR_USER_ACCT_RELATION_OBJ=XES_SERVICE_DOMAIN | 0x2536;
    public static final int PERMISSION_AREADY_EXISTS=XES_SERVICE_DOMAIN | 0x2537;

    public static final int TRANSACTION_AMOUNT_EXCEEDS_LIMIT=XES_SERVICE_DOMAIN | 0x2538;

    public static final int INVALID_ROLE_COUNT=XES_SERVICE_DOMAIN | 0x2539;
    public static final int SIGNED_TRAN_NAME_IS_MISSING=XES_SERVICE_DOMAIN | 0x253A;
    public static final int TRAN_TIME_IS_NOT_VALID=XES_SERVICE_DOMAIN | 0x253B;
    public static final int TRAN_TIME_EXCEEDED=XES_SERVICE_DOMAIN | 0x253C;
    public static final int NOT_ENOUGH_SIGNATUERS_FOR_TRAN_TO_PROCEED=XES_SERVICE_DOMAIN | 0x253D;

    //  Customer Credit Rating
    public static final int INVALID_CREDIT_ANALYST = XES_SERVICE_DOMAIN | 0x4A0;
    //Cust Comm Update
    public static final int IFX_ERR_SEASONAL_PARAM_REQ = XES_SERVICE_DOMAIN | 0x27B;

    //Stop Payment Add ALS
    public static final int ERR_INVALID_HIGH_CHECK_RANGE = XES_SERVICE_DOMAIN | 0x9201;
    public static final int ERR_INVALID_LOW_CHECK_RANGE = XES_SERVICE_DOMAIN | 0x9202;
    public static final int SQL_ERROR = XES_SERVICE_DOMAIN | 0x9203;

    public static final int ACCT_OWNERSHIP_INVALID = XES_SERVICE_DOMAIN | 0x9204;

    //UserIdMod
    public static final int UNKNOWN_USER_ID=XES_SERVICE_DOMAIN | 0x9205;

    //Password Modification
    public static final int PASSWORD_NAME_IS_MISSING=XES_SERVICE_DOMAIN | 0x9206;

    //   Role Add Del and Inq
    public static final int ROLE_NAME_IS_MISSING=XES_SERVICE_DOMAIN | 0x9207;
    public static final int ROLE_NAME_DOES_NOT_EXIST=XES_SERVICE_DOMAIN | 0x9208;
    public static final int ROLE_NAME_ALREADY_EXISTS=XES_SERVICE_DOMAIN | 0x9209;
    public static final int PERMISSION_NAME_DOES_NOT_EXIST=XES_SERVICE_DOMAIN | 0x920A;

    //   Role relation Add Del
    public static final int ROLE_NAMES_EMPTY = XES_SERVICE_DOMAIN | 0x9210;
    public static final int INVALID_PARENT_ROLE_NAME = XES_SERVICE_DOMAIN | 0x9211;
    public static final int INVALID_CHILD_ROLE_NAME = XES_SERVICE_DOMAIN | 0x9212;
    public static final int PARENT_ROLE_NAME_ALREADY_EXIST = XES_SERVICE_DOMAIN | 0x9213;
    public static final int CHILD_ROLE_NAME_ALREADY_EXIST = XES_SERVICE_DOMAIN | 0x9214;
    public static final int RELATION_ALREADY_EXIST = XES_SERVICE_DOMAIN | 0x9215;
    public static final int RELATION_DOES_NOT_EXIST = XES_SERVICE_DOMAIN | 0x9216;
    public static final int INVALID_ROLE_NAMES = XES_SERVICE_DOMAIN | 0x9217;
    public static final int PARENT_ROLE_NAME_EMPTY = XES_SERVICE_DOMAIN | 0x9218;
    public static final int CHILD_ROLE_NAME_EMPTY = XES_SERVICE_DOMAIN | 0x9219;

    public static final int CHILD_ROLE_NAME_DOES_NOT_EXIST= XES_SERVICE_DOMAIN |0x9245;
    public static final int PARENT_ROLE_NAME_DOES_NOT_EXIST= XES_SERVICE_DOMAIN |0x9246;
    public static final int PARENT_ROLE_AND_CHILD_NAME_DOES_NOT_EXIST= XES_SERVICE_DOMAIN |0x9247;



    //AdminRole Add and Del
    public static final int ADMIN_ROLE_NAME_IS_MISSING=XES_SERVICE_DOMAIN |0x920B;
    public static final int ADMIN_ROLE_NAME_ALREADY_EXISTS=XES_SERVICE_DOMAIN |0x920C;
    public static final int ADMIN_ROLE_NAME_DOES_NOT_EXIST=XES_SERVICE_DOMAIN |0x920D;

    //  UserAdminRole Add and Del
    public static final int USER_ALREADY_ASSIGNED_TO_ADMIN_ROLE=XES_SERVICE_DOMAIN |0x920E;
    public static final int UNKNOWN_ADMIN_ROLE=XES_SERVICE_DOMAIN |0x920F;
    public static final int INVALID_ADMIN_ROLE_NAME=XES_SERVICE_DOMAIN |0x27C;
    public static final int USER_IS_NOT_ASSIGNED_TO_ADMIN_ROLE=XES_SERVICE_DOMAIN |0x27D;

    public static final int BEGINTIME_IS_EMPTY =XES_SERVICE_DOMAIN | 0x150B;
    public static final int ENDTIME_IS_EMPTY=XES_SERVICE_DOMAIN | 0x150C;
    public static final int DAYMASK_IS_EMPTY=XES_SERVICE_DOMAIN | 0x150D;
    public static final int INVALID_IFX_DATETIME_FORMAT=XES_SERVICE_DOMAIN | 0x150E;
    public static final int BEGINTIME_IS_GREATER_THAN_ENDTIME=XES_SERVICE_DOMAIN | 0x150F;
    public static final int  BEGINTIME_IS_EQUAL_TO_ENDTIME=XES_SERVICE_DOMAIN | 0x9242;

    public static final int DAYMASK_IS_BIG  = XES_SERVICE_DOMAIN | 0x7009;
    public static final int INVALID_DAYMASK =XES_SERVICE_DOMAIN |  0x9243;
    public static final int UNKNOWN_DAYMASK=XES_SERVICE_DOMAIN |  0x9244;
    public static final int START_DATE_IS_NOT_SMALLER_THAN_END_DATE =XES_SERVICE_DOMAIN | 0x922A;

    // Dynamic Permission
    public static final int PERMISSION_NAME_AND_DYNAMIC_ID_EMPTY = XES_SERVICE_DOMAIN |0x921A;
    public static final int PERMISSION_NAME_EMPTY = XES_SERVICE_DOMAIN |0x921B;
    public static final int DYNAMIC_ID_EMPTY = XES_SERVICE_DOMAIN |0x921C;
    public static final int INVALID_DYNAMIC_ID = XES_SERVICE_DOMAIN |0x921D;
    public static final int DYNAMIC_PERMISSION_DOES_NOT_EXIST = XES_SERVICE_DOMAIN |0x921E;
    public static final int PERMISSION_NAME_IS_INVALID = XES_SERVICE_DOMAIN |0x921F;
    public static final int THIS_ROLE_HAS_DECENDENT_ROLES=XES_SERVICE_DOMAIN | 0x9240;


    // URAPrivilegeOnRole
    public static final int ADMIN_ROLE_NAME_AND_ROLE_NAME_DOES_NOT_EXIST = XES_SERVICE_DOMAIN |0x27E ;
    public static final int ADMIN_ROLE_NAME_AND_ROLE_NAME_MISSING = XES_SERVICE_DOMAIN  |0x27F;
    public static final int NO_URA_IS_CURRENTLY_ASSIGNED_TO_ADMINROLE  = XES_SERVICE_DOMAIN |0x211;
    // OrgUnit
    public static final int ORG_UNIT_ID_IS_EMPTY = XES_SERVICE_DOMAIN |0x9220 ;
    public static final int ORG_UNIT_ID_ALREADY_EXIST = XES_SERVICE_DOMAIN |0x9221 ;
    public static final int ORG_UNIT_ID_DOES_NOT_EXIST = XES_SERVICE_DOMAIN |0x9222 ;
    public static final int INVALID_ORG_UNIT_ID = XES_SERVICE_DOMAIN |0x9223 ;
    public static final int ORG_UNIT_ID_IS_TOO_SHORT = XES_SERVICE_DOMAIN |0x9224 ;
    public static final int DESCRIPTION_IS_TOO_LONG = XES_SERVICE_DOMAIN |0x9225 ;
    public static final int ORG_IS_ASSOCIATED_TO_PERMISSIONS=XES_SERVICE_DOMAIN |0x9226;
    public static final int ORG_IS_ASSOCIATED_TO_USERS=XES_SERVICE_DOMAIN |0x9227;
    public static final int DESCENDANT_ORGS_EXIST=XES_SERVICE_DOMAIN |0x9228;
    public static final int ASCENDANT_ORGS_EXIST=XES_SERVICE_DOMAIN |0x9229;

    public static final int SEARCHSTRING_IS_EMPTY= XES_SERVICE_DOMAIN |0x9248;
    public static final int UNKNOWN_ORG_ID_NUM= XES_SERVICE_DOMAIN |0x9249;

    //  OUPrivilege
    public static final int  ADMIN_ROLE_NAME_AND_ORG_UNIT_ID_EMPTY = XES_SERVICE_DOMAIN  |0x280;
    public static final int  ADMIN_ROLE_NAME_AND_ORG_UNIT_ID_DOES_NOT_EXIST  = XES_SERVICE_DOMAIN  |0x281;
    public static final int  NO_USER_OU_IS_CURRENTLY_ASSIGNED_TO_ADMINROLE = XES_SERVICE_DOMAIN |0x286;
    public static final int  THIS_PERM_OU_IS_NOT_CURRENTLY_ASSIGNED_TO_ADMINROLE = XES_SERVICE_DOMAIN |0x287;

    //  OrgUnit Relation
    public static final int RELATION_EXISTS=XES_SERVICE_DOMAIN  |0x282;
    public static final int INVALID_CHILD_ORG_UNIT_ID= XES_SERVICE_DOMAIN  |0x284;
    public static final int INVALID_PARENT_ORG_UNIT_ID= XES_SERVICE_DOMAIN  |0x285;

    //   OrgUnitRelation.
    public static final int  PARENT_AND_CHILD_ORG_UNIT_IDS_EMPTY = XES_SERVICE_DOMAIN  |0x290;
    public static final int  PARENT_ORG_UNIT_ID_EMPTY = XES_SERVICE_DOMAIN  |0x291;
    public static final int  CHILD_ORG_UNIT_ID_EMPTY  = XES_SERVICE_DOMAIN  |0x292;
    public static final int  INVALID_CHILD_ORG_ID_NUM = XES_SERVICE_DOMAIN  |0x293;
    public static final int  INVALID_PARENT_AND_CHILD_ORG_ID_NUMS = XES_SERVICE_DOMAIN  |0x294;
    public static final int  INVALID_PARENT_ORG_ID_NUM  = XES_SERVICE_DOMAIN  |0x295;
    public static final int  NO_PARENT_CHILD_RELATION   = XES_SERVICE_DOMAIN  |0x296;

    public static final int CHILD_DESCRIPTION_IS_EMPTY = XES_SERVICE_DOMAIN  |0x29A;
    public static final int PARENT_DESCRIPTION_IS_EMPTY = XES_SERVICE_DOMAIN  |0x29B;
    public static final int PARENT_AND_CHILD_ORG_UNIT_ID_NUMS_EMPTY = XES_SERVICE_DOMAIN  |0x29C;
    public static final int PARENT_ORG_UNIT_ID_NUM_EMPTY = XES_SERVICE_DOMAIN  |0x29D;
    public static final int CHILD_ORG_UNIT_ID_NUM_EMPTY = XES_SERVICE_DOMAIN  |0x29E;


    //PRAPrivilegeOnRoleDel
    public static final int NO_PRA_ROLES_CURRENTLY_ASSIGNED_TO_ADMINROLE = XES_SERVICE_DOMAIN  |0x29F;

    //  PrimaryAcctIndicator Missing/Invalid Added By U Basavaraj (LC6383) for Incident #839
    public static final int PRIMARY_ACCT_INDICATOR_MISSING = XES_SERVICE_DOMAIN | 0x300;
    public static final int INVALID_PRIMARY_ACCT_INDICATOR = XES_SERVICE_DOMAIN | 0x301;

//   Function Does not exist or Record not found - CR65 Added for Function Definition Services by U.Basavaraj (lc6383)

    public static final int FUNCTION_DEFINITION_RECORDS_NOT_EXIST = XES_SERVICE_DOMAIN | 0x302;

    public static final int ONLY_FUNCTION_NAME_OR_FUNCTION_ID_IS_REQUIRED_NOT_BOTH = XES_SERVICE_DOMAIN | 0x303;



    //  UserAcctLock
    public static final int INVALID_USER_ACCT_STATUS = XES_SERVICE_DOMAIN  |0x297;
    public static final int UNKNOWN_USER_ACCT_STATUS = XES_SERVICE_DOMAIN  |0x298;

    public static final int USER_ACCOUNT_IS_IN_OTHER_STATUS = XES_SERVICE_DOMAIN | 0x140E;

    //  Bill Pay Source Account
    public static final int INVALID_ENROLLMENT_REQUEST = XES_SERVICE_DOMAIN | 0x299;
    //Bill Pay Enrollment
    public static final String STATUS_ENROLLMENT_PENDING = "Enrollment Pending";

    public static final String STATUS_ENROLLMENT_ACCEPTED = "Enrollment Accepted";

    public static final String STATUS_ENROLLMENT_REJECTED = "Enrollment Rejected";

    public static final String STATUS_DEENROLLMENT_PENDING = "De_Enrollment Pending";

    public static final String STATUS_DEENROLLMENT_REJECTED = "De_Enrollment Rejected";

    public static final String STATUS_DEENROLLMENT_ACCEPTED = "De_Enrollment Accepted";

    public static final String ENROLLMENT_PENDING = "0";

    public static final String ENROLLMENT_ACCEPTED = "1";

    public static final String ENROLLMENT_REJECTED = "2";

    public static final String ENROLLMENT = "0";

    public static final String DEENROLLMENT = "1";

    public static final String DEENROLLMENT_PENDING = "0";

    public static final String DEENROLLMENT_ACCEPTED = "1";

    public static final String DEENROLLMENT_REJECTED = "2";

    //Constant for ServiceInput object
    public static final int INVALID_SERVICEINPUT_DATA_OBJECT = XES_SERVICE_DOMAIN  |0x9241;

    //Constant for Special Instruction Add Service for Profile Host
    public static final int NONEXIST_ACCOUNT = XES_SERVICE_DOMAIN  |0x9250;

    //Constant for Overdraft Add Service of Profile Host
    public static final int INVALID_CHECKING_ACTIVE_ACCOUNT = XES_SERVICE_DOMAIN  |0x9252;

    //Constant for Loan PayOff Inquiry Service of Profile Host
    public static final int LOAN_ACCOUNT_INACTIVE = XES_SERVICE_DOMAIN  |0x9253;

    //constant for Stop Payment Add Service for Profile Host
    public static final int ERR_STOP_PAY_ADD_FOR_ACTIVE = XES_SERVICE_DOMAIN  | 0x9254;

    //Constant for OverDraft Protection Add Service for Profile Host
    public static final int SOURCE_ACCOUNT_INVALID = XES_SERVICE_DOMAIN  | 0x9255;

    // Constants CD Fundswithdrawal Inquiry Service for Profile Host
    public static final int WAIVE_TRAN_CHARGE_FLAG_NULL = XES_SERVICE_DOMAIN  | 0x9256;
    public static final int WAIVE_TRAN_CHARGE_FLAG_TYPE_NULL = XES_SERVICE_DOMAIN  | 0x9257;

    //constant for the service Customer Account Relationship Add - Profile host
    public static final int INVALID_CUSTID_ACCTID = XES_SERVICE_DOMAIN |0x9258;

    //constant for the service Combined Statement Add - Profile host
    public static final int CUST_INVALID_ENTRY = XES_SERVICE_DOMAIN |0x9259;



    //constant for the Checking decimal Length

    public static final int DECIMAL_LENGTH_EXCEEDED = XES_SERVICE_DOMAIN |0x9261;



    public static final int CUTOMER_MULTIPLE_ROLE_ACCOUNTS = XES_SERVICE_DOMAIN |0x9262;

//  constant for invalid decimals for the given currency code

    public static final int INVALID_DECIMAL_LENGTH = XES_SERVICE_DOMAIN |0x9263;
    //Combined Statement Inquiry
    public static final int INVALID_DATA_STMT_CYCLE_CDE = XES_SERVICE_DOMAIN |0x9264;

    //Balance Constants
    public static final String AVAILABLE_BALANCE = "Available Balance";
    public static final String OUTSTANDING_BALANCE = "Outstanding Balance";
    public static final String LAST_STATEMENT_BALANCE = "Last Statement Balance";
    public static final String ORIGINAL_BALANCE = "Original Balance";
    public static final String UNPAID_ACCRUED_INTEREST = "Unpaid Accrued Interest";
    public static final String OVERDRAFT_BALANCE = "Overdraft Balance";
    public static final String PAST_DUE_BALANCE = "Past Due Balance";
    public static final String OUTSTANDING_CASH_ADVANCE_BALANCE = "Outstanding Cash Advance Balance";
    public static final String YTD_INTEREST_BALANCE = "YTD Interest Balance";
    public static final String LAST_YEAR_INTEREST_BALANCE = "Last Year Interest Balance";
    public static final String PERIOD_FEES_BALANCE = "Period Fees Balance";


    public static final String AVAILABLE_BALANCE_AMOUNT = "Available Balance Amount";
    public static final String AVERAGE_BALANCE_YEAR_TO_DATE = "Average Balance year to date";
    public static final String AVERAGE_BALANCE_YEAR_TO_DATE_AMOUNT = "Average Balance year to date Amount";
    public static final String AVERAGE_BALANCE_MONTH_TO_DATE = "Average Balance month to date";
    public static final String AVERAGE_BALANCE_COLLECTED = "Average Balance Collected";
    public static final String AVERAGE_BALANCE_COLLECTED_AMOUNT = "Average Balance Collected Amount";
    public static final String AVERAGE_BALANCE = "Average Balance";
    public static final String CREDIT_LIMIT = "Credit Limit";
    public static final String CASH_AVAILABLE_BALANCE = "Cash Available Balance";
    public static final String COLLECTED_BALANCE = "Collected Balance";
    public static final String CASH_LINE_BALANCE = "Cash Line Balance";
    public static final String CURRENT_BALANCE = "Current_Balance";
    public static final String DDA_BALANCE = "DDA Balance";


    public static final String PREVIOUS_NIGHT_BALANCE = "Previous Night Balance Amount";
    public static final String PRINCIPAL_BALANCE = "Principal Balance";
    public static final String PAYOFF_AMOUNT = "Payoff Amount";

    //Added for defect #XES00008758
    public static final String PAYOFF_AMOUNT_BALANCE = "Payoff Amount Balance";
    public static final String LEDGE_BALANCE = "Ledger Balance";
    public static final String CURRENT_AMOUNT_BALANCE = "Current Balance";
    public static final String PREVIOUS_NIGHT_BALANCE_AMOUNT = "Previous Night Balance";
    public static final String TOTAL_TRANSFER_AMOUNT = "Total Transfer Amount";

    public static final String SAVINGS_BALANCE_AVAILABLE = "Savings Balance Available";
    public static final String SAV_BALANCE_AMOUNT = "SAV Balance Amount";


    public static final String LAST_YEAR_WITHHOLDING_BALANCE = "Last year Withholding balance";

    public static final String LEDGE_BALANCE_AMOUNT = "Ledge Balance Amount";

    public static final String TOTAL_TRANSFER = "Total Transfer";


    public static final String YTD_WITHHOLDING_BALANCE = "YTD withholding Balance";

    public static final String UNPAID_ACCRUED_INTEREST_BALANCE = "Unpaid Accrued Interest Balance";
    public static final int MORE_RECORDS_RETURNED= XES_SERVICE_DOMAIN |0x9260;
    public static final String YTD_FEES_BALANCE = "YTD Fees Balance";
    public static final String AVALIABLE_CREDIT_BALANCE = "Available Credit Balance";
    public static final String ESCROW_BALANCE = "Escrow Balance";
    public static final String INTEREST_ACCRUED_BALANCE = "Interest Accrued Balance";
    public static final String LAST_YEAR_FEES_BALANCE = "Last Year Fees Balance";

    //Added for Fidelity Check imaging interface Start
    /*
      * The following Constant is used to retrieve key from properties file
      */
    public static final String CRYPTOSERVERPROPERTIES = "crypto.server.properties";

    public static final String FIDELITY_CHECK_IMAGE_URL = "fidelitycheckimagingurl";

    public static final String DATE = "Date";
    public static final String ACCOUNTNUMBER = "Account";
    public static final String SERIAL = "Serial";
    public static final String ARCHIVECONFIG = "ArchiveConfig";
    public static final String INSTID = "InstID";
    public static final String IMAGEFB = "ImageFB";
    public static final String FRONT = "F";
    public static final String BACK = "B";
    public static final String BOTH = "Both";
    public static final String TRANSACTIONAMT = "Amount";

    //	Added for Fidelity Check imaging interface -- End

    //constants for Payee Add service
    public static final String PAYEE_TYPE_STANDARD = "STD";
    public static final String PAYEE_TYPE_TRANSFER = "XFER";
    public static final String PAYEE_TYPE_FULLYSPECIFIED = "FSPAYEE";
    public static final String PAYEE_TYPE_BILLER = "BILLER";

    public static final String DEP_ACCT_FROM_ADDRESS =  "DEPACCT_FRM_ADDR";
    public static final String CARD_ACCT_FROM_ADDRESS = "CARDACCT_FRM_ADDR";
    public static final String FSPAYEE_ADDRESS = "FSPAYEE_ADDRESS";
    public static final String FSPAYEE_ORG_MAILING_ADDRESS = "FSPAYEE_ORGMAIL_ADDR";
    public static final String XFERPAYEE_BANK_ADDRESS = "XFERPAYEE_ADDR";
    public static final String BILLERPAYEE_ADDRESS = "BILLER_ADDR";
    public static final String BILLERPAYEE_REMIT_ADDRESS = "BILLER_REMIT_ADDR";
    public static final String BILLERPAYEE_ORG_MAILING_ADDRESS = "BILLER_ORGMAIL_ADDR";

    //Added for Payment Add service
    public static final String PAYMENT_DETAILS_1 = "Payment_Details_1";
    public static final String PAYMENT_DETAILS_2 = "Payment_Details_2";
    public static final String PAYMENT_DETAILS_3 = "Payment_Details_3";
    public static final String PAYMENT_DETAILS_4 = "Payment_Details_4";
    public static final String TRANSACTION_DEBIT_ID = "Transaction_Debit_Id";
    public static final String FROM_ACCOUNT_CONTROL_NUMBER = "From_Account_Control_Number";
    public static final String TO_ACCOUNT_CONTROL_NUMBER = "To_Account_Control_Number";
    public static final String ORIGINATOR_REFERENCE_NUMBER = "Originator_Reference_Number";
    public static final String BENEFICIARY_REF = "Beneficiary_Reference";
    public static final String FROM_TRANSACTION = "From_Transaction";
    public static final String TO_TRANSACTION = "To_Transaction";
    public static final String BENEFICIARY_INFORMATION = "Beneficiary_Information";
    public static final String ADDR_TYPE_BENEFICIARY_FI = "BENEFICIARY_FI";
    public static final String BENEFICIARY_BRANCH = "BENEFICIARY_BRANCH";
    public static final String BENEFICIARY = "BENEFICIARY";
    public static final String ENTITY_TYPE_ORIGINATOR_FI = "ORIGINATOR_FI";
    public static final String ORIGINATOR_FI = "ORIGINATOR_FI";
    public static final String ORIGINATOR_TO_BENEFICIARY = "Originator_To_Beneficiary";
    public static final String ENTITY_TYPE_REMITTANCE="REMITTANCE";

    // Subject Context and credential building related errors
    // Sessionkey is not a valid TP token
    public static final int CREDBUILD_INVALID_SESSIONKEY = XES_SERVICE_DOMAIN |0x9500;
    // Credentials missing for creating component context %1=builder, %2=missing credential list
    public static final int CREDBUILD_MISSING_CREDENTIALS = XES_SERVICE_DOMAIN |0x9501;
    // Cannot find component context credential.
    public static final int CRED_COMP_CTX_NOT_FOUND = XES_SERVICE_DOMAIN |0x9502;

    //
    //Security Account Service
    public static final int ACCOUNT_ID_IS_INVALID=XES_SERVICE_DOMAIN | 0x304;
    public static final int ACCOUNT_ID_ALREADY_EXISTS=XES_SERVICE_DOMAIN | 0x305;
    public static final int ACCOUNT_ID_DOES_NOT_EXISTS=XES_SERVICE_DOMAIN | 0x306;
    public static final int ACCOUNT_TYPE_IS_INVALID=XES_SERVICE_DOMAIN | 0x307;
    public static final int ACCOUNT_TYPE_DOES_NOT_EXISTS=XES_SERVICE_DOMAIN | 0x308;
    // Added By Sameer 0n 09-03-2006
    public static final int ACCOUNT_NOT_FOUND = XES_SERVICE_DOMAIN | 0x314;
    public static final int ACCT_NAME_TOO_LONG = XES_SERVICE_DOMAIN | 0x315;
    public static final int CUR_CODE_TOO_LONG = XES_SERVICE_DOMAIN | 0x316;
    public static final int ACCT_UPDATE_FAILED = XES_SERVICE_DOMAIN | 0x317;

    //Added By vara on 09-03-2006
    public static final int ACCOUNT_NAME_IS_INVALID=XES_SERVICE_DOMAIN | 0x501;
    public static final int ACCOUNT_ALREADY_EXISTS=XES_SERVICE_DOMAIN | 0x502;
    public static final int BOTH_ACCT_ID_AND_ACCT_TYPE_INVALID=XES_SERVICE_DOMAIN | 0x503;
    public static final int ACCOUNT_SEARCH_FAILED=XES_SERVICE_DOMAIN | 0x505;

    //Company Service Added By Sameer
    public static final int BANK_ID_IS_INVALID = XES_SERVICE_DOMAIN | 0x309;
    public static final int BANK_ID_DOES_NOT_EXISTS = XES_SERVICE_DOMAIN | 0x30A;
    public static final int PARENT_ORG_UNIT_ID_DOES_NOT_EXIST = XES_SERVICE_DOMAIN | 0x30B;
    public static final int COMPANY_ID_IS_INVALID = XES_SERVICE_DOMAIN | 0x30C;
    public static final int COMPANY_ID_ALREADY_EXISTS = XES_SERVICE_DOMAIN | 0x30D;
    public static final int COMPANY_ID_DOES_NOT_EXISTS = XES_SERVICE_DOMAIN | 0x30E;
    public static final int INVALID_ADDRESS_TYPE = XES_SERVICE_DOMAIN | 0x30F;
    public static final int INVALID_PHONE_TYPE = XES_SERVICE_DOMAIN | 0x310;

    // Added By Sameer 0n 09-03-2006
    public static final int PRIMARY_SET_TWICE = XES_SERVICE_DOMAIN | 0x318;
    public static final int NO_PRIMARY_SET = XES_SERVICE_DOMAIN | 0x319;
    public static final int COMPANY_ID_TOO_LONG = XES_SERVICE_DOMAIN | 0x31A;
    public static final int URL_TOO_LONG = XES_SERVICE_DOMAIN | 0x31B;
    public static final int INVALID_URL = XES_SERVICE_DOMAIN | 0x31C;
    public static final int HOST_COMPANY_KEY_TOO_LONG = XES_SERVICE_DOMAIN | 0x31D;
    public static final int INVALID_EMAIL_ADDRESS = XES_SERVICE_DOMAIN | 0x31E;
    public static final int INVALID_PHONE_NUMBER = XES_SERVICE_DOMAIN | 0x31F;
    public static final int INVALID_ZIP_CODE = XES_SERVICE_DOMAIN | 0x320;
    public static final int INVALID_STATE = XES_SERVICE_DOMAIN | 0x321;
    public static final int INVALID_TAXID = XES_SERVICE_DOMAIN | 0x322;
    public static final int ACH_COMPANY_TOO_LONG = XES_SERVICE_DOMAIN | 0x323;
    public static final int COMPANY_CREATE_FAILED = XES_SERVICE_DOMAIN | 0x324;
    public static final int COMPANY_FIND_FAILED = XES_SERVICE_DOMAIN | 0x325;
    public static final int COMPANY_SEARCH_FAILED = XES_SERVICE_DOMAIN | 0x326;
    public static final int CAN_NOT_DELETE_COMPANY = XES_SERVICE_DOMAIN | 0x313;
    public static final int CAN_NOT_DELETE_COMPANY_A = XES_SERVICE_DOMAIN | 0x33A;
    public static final int CAN_NOT_DELETE_COMPANY_U = XES_SERVICE_DOMAIN | 0x33B;
    public static final int CAN_NOT_DELETE_COMPANY_UC = XES_SERVICE_DOMAIN | 0x33C;


    //Added By Ashok
    public static final int BANK_ID_ALREADY_EXISTS = XES_SERVICE_DOMAIN | 0x312;
    public static final int COMPANY_UPDATE_FAILED = XES_SERVICE_DOMAIN | 0x327;
    public static final int COMPANY_DELETE_FAILED  = XES_SERVICE_DOMAIN | 0x328;

    public static final int INVLD_FAX_LEN = XES_SERVICE_DOMAIN | 0x330;
    public static final int BANK_CREATE_FAILED = XES_SERVICE_DOMAIN | 0x331;
    public static final int BANK_NAME_IS_INVALID = XES_SERVICE_DOMAIN | 0x332;
    public static final int BANK_UPDATE_FAILED = XES_SERVICE_DOMAIN | 0x333;
    public static final int BANK_DELETE_FAILED = XES_SERVICE_DOMAIN | 0x334;
    public static final int BANK_SEARCH_FAILED = XES_SERVICE_DOMAIN | 0x335;
    public static final int BANK_READ_FAILED = XES_SERVICE_DOMAIN | 0x336;

    public static final int COMPANY_DOES_NOT_EXISTS = XES_SERVICE_DOMAIN | 0x337;
    public static final int INVLD_EMAIL_LEN = XES_SERVICE_DOMAIN | 0x338;
    public static final int COMPANY_NAME_IS_INVALID = XES_SERVICE_DOMAIN | 0x339;

    //Deposit Account Post Closeout Transaction
    public static final int GL_ACCT_NOTEXIST  = XES_SERVICE_DOMAIN | 0x329;

    //Added for ACH Batch User Link Add Service
    public static final int USERID_TO_BATCHID_LINK_ALREADY_EXISTS= XES_SERVICE_DOMAIN | 0x92A;

    //SecAcctTypeAdd
    public static final int ACCOUNT_TYPE_ALREADY_EXISTS= XES_SERVICE_DOMAIN | 0x311;

    //Added on 06-04-06
    public static final int CAN_NOT_DELETE_ACCOUNTTYPE= XES_SERVICE_DOMAIN | 0x607;
    public static final int ACCOUNTTYPE_DELETE_FAILED=XES_SERVICE_DOMAIN | 0x608;
    public static final int ACCOUNTTYPE_READ_FAILED=XES_SERVICE_DOMAIN | 0x605;
    public static final int ACCOUNTTYPE_SEARCH_FAILED=XES_SERVICE_DOMAIN | 0x606;

    // Added For UserClass Services By Sameer
    public static final int USERCLASS_NAME_IS_INVALID = XES_SERVICE_DOMAIN | 0x33D;
    public static final int USERCLASS_NOT_FOUND = XES_SERVICE_DOMAIN | 0x33E;
    public static final int USERCLASS_READ_FAILED = XES_SERVICE_DOMAIN | 0x33F;
    public static final int USERCLASS_SEARCH_FAILED = XES_SERVICE_DOMAIN | 0x340;

    //Added For UserClass Services by Prasad
    public static final int USERCLASS_ALREADY_EXISTS= XES_SERVICE_DOMAIN | 0x341;
    public static final int USERCLASS_CREATE_FAILED=XES_SERVICE_DOMAIN | 0x342;
    public static final int USERCLASS_DELETE_FAILED=XES_SERVICE_DOMAIN | 0x343;

    //Added For UserAccount Services by Sameer
    public static final int USER_READ_FAILED = XES_SERVICE_DOMAIN | 0x344;
    public static final int ACCOUNT_READ_FAILED = XES_SERVICE_DOMAIN | 0x345;
    public static final int USERACCOUNT_ALREADY_EXISTS = XES_SERVICE_DOMAIN | 0x346;
    public static final int USERACCOUNT_CREATE_FAILED = XES_SERVICE_DOMAIN | 0x347;

    public static final int USERACCOUNT_NOT_FOUND = XES_SERVICE_DOMAIN | 0x348;
    public static final int USERACCOUNT_READ_FAILED = XES_SERVICE_DOMAIN | 0x349;
    public static final int USERACCOUNT_DELETE_FAILED = XES_SERVICE_DOMAIN | 0x34A;

    public static final int USERACCOUNT_GET_FAILED = XES_SERVICE_DOMAIN | 0x34B;
    public static final int USERACCOUNT_SEARCH_FAILED = XES_SERVICE_DOMAIN | 0x34C;

    //added by vara on 27-04-06
    public static final int ACCOUNT_CANNOT_DELETE=XES_SERVICE_DOMAIN | 0x604;



    public static final int BANK_CANNOT_DELETE = XES_SERVICE_DOMAIN | 0x34D;

    //Added by prasad on 20-03-06
    public static final int ACCOUNT_TYPE_IS_TOO_LONG= XES_SERVICE_DOMAIN | 0x601;
    public static final int CUSTOMER_NUMBER_IS_INVALID=XES_SERVICE_DOMAIN | 0x602;
    public static final int CUSTOMER_NUMBER_DOESNOT_EXISTS = XES_SERVICE_DOMAIN | 0x603;

    //	Added by Rohit on 04-05-06
    public static final int MISSING_SMTP_HOST_SETTINGS= XES_SERVICE_DOMAIN | 0x604;
    public static final int MISSING_SMTP_HOST_USERNAME=XES_SERVICE_DOMAIN | 0x605;
    public static final int MISSING_SMTP_HOST_PASSWORD = XES_SERVICE_DOMAIN | 0x606;
    public static final int MISSING_SMTP_FROM_ADDRESS = XES_SERVICE_DOMAIN | 0x607;
    public static final int UNABLE_TO_INITALIZE_MAIL_SESSION = XES_SERVICE_DOMAIN | 0x608;
    public static final int MAIL_PROPERTIES_NOT_FOUND = XES_SERVICE_DOMAIN | 0x609;
    public static final int UNABLE_TO_READ_MAIL_PROPERTIES = XES_SERVICE_DOMAIN | 0x610;
    public static final int UNABLE_TO_SEND_MAIL = XES_SERVICE_DOMAIN | 0x611;
    public static final int NO_EMAIL_ADDRESS_FOUND = XES_SERVICE_DOMAIN | 0x612;
    public static final int IFX_SESSION_KEY_NULL = XES_SERVICE_DOMAIN | 0x613;
    public static final int ACCOUNT_NUMBER_IS_NULL = XES_SERVICE_DOMAIN | 0x614;

    // Added By Sameer For Permission Assignment Inquiry service.
    public static final int PASIGN_BANK_SEARCH_FAILED = XES_SERVICE_DOMAIN | 0x615;
    public static final int PASIGN_ACCT_SEARCH_FAILED = XES_SERVICE_DOMAIN | 0x616;
    public static final int PASIGN_ACTP_SEARCH_FAILED = XES_SERVICE_DOMAIN | 0x617;
    public static final int PASIGN_USER_SEARCH_FAILED = XES_SERVICE_DOMAIN | 0x618;
    public static final int USERACCOUNT_IS_INVALID = XES_SERVICE_DOMAIN | 0x619;
    public static final int PASIGN_USAC_SEARCH_FAILED = XES_SERVICE_DOMAIN | 0x61A;
    public static final int USERCLASS_IS_INVALID = XES_SERVICE_DOMAIN | 0x61B;
    public static final int PASIGN_UCLS_SEARCH_FAILED = XES_SERVICE_DOMAIN | 0x61C;
    public static final int PASIGN_COMP_SEARCH_FAILED = XES_SERVICE_DOMAIN | 0x61D;
    public static final int PASIGN_BANK_READ_FAILED = XES_SERVICE_DOMAIN | 0x61E;
    public static final int PASIGN_ACCT_READ_FAILED = XES_SERVICE_DOMAIN | 0x61F;
    public static final int PASIGN_ACTP_READ_FAILED = XES_SERVICE_DOMAIN | 0x620;
    public static final int PASIGN_USER_READ_FAILED = XES_SERVICE_DOMAIN | 0x621;
    public static final int PASIGN_USAC_READ_FAILED = XES_SERVICE_DOMAIN | 0x622;
    public static final int PASIGN_UCLS_READ_FAILED = XES_SERVICE_DOMAIN | 0x623;
    public static final int PASIGN_COMP_READ_FAILED = XES_SERVICE_DOMAIN | 0x624;
    public static final int PERMISION_NOT_ASSIGNED = XES_SERVICE_DOMAIN | 0x625;
    public static final int ACCOUNT_INVALID = XES_SERVICE_DOMAIN | 0x626;

    //Added By Prasad For PermissionAssignmentAdd Service.

    public static final int PERMISSIONS_ALREADY_ASSIGNED_TO_USER_FROM_USERCLASS = XES_SERVICE_DOMAIN | 0x627;
    public static final int PERMISSION_ALREADY_ASSIGNED_TO_BANK= XES_SERVICE_DOMAIN | 0x628;
    public static final int PERMISSION_ALREADY_ASSIGNED_TO_ACCOUNTTYPE= XES_SERVICE_DOMAIN | 0x629;
    public static final int PERMISSION_ALREADY_ASSIGNED_TO_ACCOUNT= XES_SERVICE_DOMAIN | 0x62A;
    public static final int PERMISSION_ALREADY_ASSIGNED_TO_USERACCOUNT= XES_SERVICE_DOMAIN | 0x62B;
    public static final int PERMISSION_ALREADY_ASSIGNED_TO_COMPANY= XES_SERVICE_DOMAIN | 0x62C;
    public static final int PERMISSION_ALREADY_ASSIGNED_TO_USERCLASS= XES_SERVICE_DOMAIN | 0x62D;
    public static final int ON_CREATE_DATE_INVALID = XES_SERVICE_DOMAIN | 0x640;
    public static final int ON_PROCESS_DATE_INVALID = XES_SERVICE_DOMAIN | 0x641;
    public static final int TRANS_AMT_INVALID = XES_SERVICE_DOMAIN | 0x643;
    public static final int DAILY_AMT_INVALID = XES_SERVICE_DOMAIN | 0x644;
    public static final int WEEKLY_AMT_INVALID = XES_SERVICE_DOMAIN | 0x645;
    public static final int MONTHLY_AMT_INVALID = XES_SERVICE_DOMAIN | 0x646;


    //added for PermissionassignmentDel Service
    public static final int PASIGN_BANK_ID_NULL = XES_SERVICE_DOMAIN | 0x351;
    public static final int PASIGN_BANK_NULL = XES_SERVICE_DOMAIN | 0x352;
    public static final int PASIGN_BANK_ID_INVLD = XES_SERVICE_DOMAIN | 0x353;
    public static final int PASIGN_BANK_PERM_NULL = XES_SERVICE_DOMAIN | 0x354;
    public static final int PASIGN_BANK_PERM_INVLD = XES_SERVICE_DOMAIN | 0x355;
    public static final int PASIGN_BANK_DELETE_FAILED = XES_SERVICE_DOMAIN | 0x356;

    public static final int PASIGN_COMP_NULL = XES_SERVICE_DOMAIN | 0x358;
    public static final int PASIGN_COMP_PERM_NULL = XES_SERVICE_DOMAIN | 0x359;
    public static final int PASIGN_COMP_PARM_NULL = XES_SERVICE_DOMAIN | 0x35B;
    public static final int PASIGN_COMP_INVLID = XES_SERVICE_DOMAIN | 0x35C;
    public static final int PASIGN_COMP_DELETE_FAILED = XES_SERVICE_DOMAIN | 0x35D;
    public static final int COMPANY_NOT_FOUND  = XES_SERVICE_DOMAIN | 0x361;

    public static final int PASIGN_ACTP_PERM_NULL = XES_SERVICE_DOMAIN | 0x363;
    public static final int PASIGN_ACTP_PARM_NULL = XES_SERVICE_DOMAIN | 0x365;
    public static final int PASIGN_ACTP_INVLID = XES_SERVICE_DOMAIN | 0x366;
    public static final int PASIGN_ACTP_DELETE_FAILED = XES_SERVICE_DOMAIN | 0x367;

    public static final int PASIGN_ACCT_PERM_NULL = XES_SERVICE_DOMAIN | 0x36A;
    public static final int PASIGN_ACCT_ACOUNT_NULL = XES_SERVICE_DOMAIN | 0x36C;
    public static final int PASIGN_ACCT_ACOUNT_INVLD = XES_SERVICE_DOMAIN | 0x36D;
    public static final int PASIGN_ACCT_DELETE_FAILED = XES_SERVICE_DOMAIN | 0x36E;

    public static final int PASIGN_UCLS_INVLD = XES_SERVICE_DOMAIN | 0x371;
    public static final int PASIGN_UCLS_PERM_NULL = XES_SERVICE_DOMAIN | 0x372;
    public static final int PASIGN_UCLS_ARG_NULL = XES_SERVICE_DOMAIN | 0x374;
    public static final int PASIGN_UCLS_DELETE_FAILED = XES_SERVICE_DOMAIN | 0x375;

    public static final int PASIGN_USER_PERM_NULL = XES_SERVICE_DOMAIN | 0x378;
    public static final int PASIGN_USER_PARM_NULL = XES_SERVICE_DOMAIN | 0x37A;
    public static final int PASIGN_USER_INVLID = XES_SERVICE_DOMAIN | 0x37B;
    public static final int PASIGN_USER_DELETE_FAILED = XES_SERVICE_DOMAIN | 0x37C;

    public static final int PASIGN_USAC_PERM_NULL = XES_SERVICE_DOMAIN | 0x37E;
    public static final int PASIGN_USAC_PARM_NULL = XES_SERVICE_DOMAIN | 0x381;
    public static final int PASIGN_USAC_INVLID = XES_SERVICE_DOMAIN | 0x382;
    public static final int PASIGN_USAC_DELETE_FAILED = XES_SERVICE_DOMAIN | 0x383;

    public static final int PASIGN_BANK_PERM_ASSIGN_NOT_FOUND = XES_SERVICE_DOMAIN | 0x385;
    public static final int PASIGN_ACCT_PERM_ASSIGN_NOT_FOUND = XES_SERVICE_DOMAIN | 0x386;
    public static final int PASIGN_UCLS_PERM_ASSIGN_NOT_FOUND = XES_SERVICE_DOMAIN | 0x387;
    public static final int PASIGN_ACTP_PERM_ASSIGN_NOT_FOUND = XES_SERVICE_DOMAIN | 0x388;
    public static final int PASIGN_USAC_PERM_ASSIGN_NOT_FOUND = XES_SERVICE_DOMAIN | 0x389;
    public static final int PASIGN_COMP_PERM_ASSIGN_NOT_FOUND = XES_SERVICE_DOMAIN | 0x38A;
    public static final int PASIGN_USER_PERM_ASSIGN_NOT_FOUND = XES_SERVICE_DOMAIN | 0x38B;

    public static final int IFX_WEEKEND_OR_HOLIDAY = 0x88E; // IFX 2190

    public static final int IFX_INVALID_ENUM_FIELD = XES_SERVICE_DOMAIN | 0x4020;
    public static final int IFX_INVALID_NUMERIC_VALUE = XES_SERVICE_DOMAIN | 0x4021;

    // Added by Sameer For PermissionAssignmentMod Service.

    public static final int PASIGN_BANK_UDATE_FAILED = XES_SERVICE_DOMAIN | 0x62E;
    public static final int PASIGN_ACCT_UDATE_FAILED = XES_SERVICE_DOMAIN | 0x62F;
    public static final int PASIGN_ACTP_UDATE_FAILED = XES_SERVICE_DOMAIN | 0x630;
    public static final int PASIGN_USER_UDATE_FAILED = XES_SERVICE_DOMAIN | 0x631;
    public static final int PASIGN_USAC_UDATE_FAILED = XES_SERVICE_DOMAIN | 0x632;
    public static final int PASIGN_UCLS_UDATE_FAILED = XES_SERVICE_DOMAIN | 0x633;
    public static final int PASIGN_COMP_UDATE_FAILED = XES_SERVICE_DOMAIN | 0x634;
    public static final int PASIGN_ACTT_ACCTTYPE_PERM_NOT_FOUND = XES_SERVICE_DOMAIN | 0x635;
    public static final int PASIGN_ACTT_COMPANY_PERM_NOT_FOUND = XES_SERVICE_DOMAIN | 0x636;
    public static final int PASIGN_UCLS_BANK_PERM_NOT_FOUND = XES_SERVICE_DOMAIN | 0x637;
    public static final int PASIGN_UCLS_COMP_PERM_NOT_FOUND = XES_SERVICE_DOMAIN | 0x638;
    public static final int PASIGN_ACTP_BANK_PERM_NOT_FOUND = XES_SERVICE_DOMAIN | 0x639;
    public static final int PASIGN_USAC_ACCOUNT_PERM_NOT_FOUND = XES_SERVICE_DOMAIN | 0x63A;
    public static final int PASIGN_USAC_USER_PERM_NOT_FOUND = XES_SERVICE_DOMAIN | 0x63B;
    public static final int PASIGN_COMP_BANK_PERM_NOT_FOUND = XES_SERVICE_DOMAIN | 0x63C;
    public static final int PASIGN_USER_COMPANY_PERM_NOT_FOUND = XES_SERVICE_DOMAIN | 0x63D;
    // Message id for IFX service type dynamic routing
    public static final int INVALID_SVC_TYPE_METADATA = XES_SERVICE_DOMAIN | 0x700;
    public static final int NO_SUCH_IFX_SERVICE_TYPE_FOUND = XES_SERVICE_DOMAIN | 0x701;
    public static final int INVALID_IFX_SERVICE_TYPE = XES_SERVICE_DOMAIN | 0x702;
    public static final int IFX_SVC_TYPE_METADATA_INIT_ERR = XES_SERVICE_DOMAIN | 0x703;
    // Added by Sameer For SecondaryOrgUnitAdd and Del service
    public static final int SEC_ORG_UNIT_ID_INVALID = XES_SERVICE_DOMAIN | 0x642;
    //Added for Frequency
    public static final int IFX_FREQ_PROC_ERR = XES_SERVICE_DOMAIN | 0x704;


    public static final int USER_AUX_ORGID_INVLD = XES_SERVICE_DOMAIN | 0x63E;

    public static final int USER_AUX_ORGID_DOES_NOT_EXIST = XES_SERVICE_DOMAIN | 0x63F;

    public static final int PKI_TOKEN_GENERATION_ERROR = XES_SERVICE_DOMAIN | 0X705;
    public static final int DUPLICATE_AUX_ORGID = XES_SERVICE_DOMAIN | 0x647;
    public static final int USER_DELETE_FAILED = XES_SERVICE_DOMAIN | 0x648;
    public static final int INVALID_REMOVAL_IND = XES_SERVICE_DOMAIN | 0x649;

    //Added By Sameer For PermissionAssignment and Account Services.
    public static final int INVALID_ENTITY_TYPE = XES_SERVICE_DOMAIN | 0x64A;
    public static final int ENFORCE_TEMPLATE_INVALID = XES_SERVICE_DOMAIN | 0x64B;
    public static final int TRAN_POWER_LIMIT_INVALID = XES_SERVICE_DOMAIN | 0x64C;
    public static final int ACCOUNTTYPE_PERM_LIMITS_CHANGED = XES_SERVICE_DOMAIN | 0x64D;
    public static final int COMPANY_PERM_LIMITS_CHANGED = XES_SERVICE_DOMAIN | 0x64E;
    public static final int ACCOUNT_PERM_LIMITS_CHANGED_ACTTYPE = XES_SERVICE_DOMAIN | 0x64F;
    public static final int ACCOUNT_PERM_LIMITS_CHANGED_COMP = XES_SERVICE_DOMAIN | 0x650;
    public static final int USER_PERM_LIMITS_CHANGED = XES_SERVICE_DOMAIN | 0x651;
    public static final int USERCLASS_PERM_LIMITS_CHANGED_BANK = XES_SERVICE_DOMAIN | 0x652;
    public static final int USERCLASS_PERM_LIMITS_CHANGED_COMP = XES_SERVICE_DOMAIN | 0x653;
    public static final int USERACCOUNT_PERM_LIMITS_CHANGED_ACCT = XES_SERVICE_DOMAIN | 0x654;
    public static final int USERACCOUNT_PERM_LIMITS_CHANGED_USER = XES_SERVICE_DOMAIN | 0x655;
    public static final int BANK_PERM_LIMITS_CHANGED_ACTTYPE = XES_SERVICE_DOMAIN | 0x656;
    public static final int BANK_PERM_LIMITS_CHANGED_COMP = XES_SERVICE_DOMAIN | 0x657;
    public static final int BANK_PERM_LIMITS_CHANGED_UCLS = XES_SERVICE_DOMAIN | 0x658;
    public static final int COMPANY_PERM_LIMITS_CHANGED_ACCT = XES_SERVICE_DOMAIN | 0x659;
    public static final int COMPANY_PERM_LIMITS_CHANGED_USER = XES_SERVICE_DOMAIN | 0x65A;
    public static final int COMPANY_PERM_LIMITS_CHANGED_UCLS = XES_SERVICE_DOMAIN | 0x65B;
    public static final int ACCOUNTTYPE_PERM_LIMITS_CHANGED_ACCT = XES_SERVICE_DOMAIN | 0x65C;
    public static final int ACCOUNT_PERM_LIMITS_CHANGED_USRACCT = XES_SERVICE_DOMAIN | 0x65D;
    public static final int USER_PERM_LIMITS_CHANGED_USRACCT = XES_SERVICE_DOMAIN | 0x65E;
    public static final int BANK_PERM_DEL_ACTTYPE = XES_SERVICE_DOMAIN | 0x65F;
    public static final int BANK_PERM_DEL_COMP = XES_SERVICE_DOMAIN | 0x660;
    public static final int BANK_PERM_DEL_UCLS = XES_SERVICE_DOMAIN | 0x661;
    public static final int COMPANY_PERM_DEL_ACCT = XES_SERVICE_DOMAIN | 0x662;
    public static final int COMPANY_PERM_DEL_USER = XES_SERVICE_DOMAIN | 0x663;
    public static final int COMPANY_PERM_DEL_UCLS = XES_SERVICE_DOMAIN | 0x664;
    public static final int ACCOUNTTYPE_PERM_DEL_ACCT = XES_SERVICE_DOMAIN | 0x665;
    public static final int ACCOUNT_PERM_DEL_USRACCT = XES_SERVICE_DOMAIN | 0x666;
    public static final int USER_PERM_DEL_USRACCT = XES_SERVICE_DOMAIN | 0x667;
    //For Account Services
    public static final int ACCOUNT_ID_IS_TOO_LONG = XES_SERVICE_DOMAIN | 0x668;
    public static final int BANK_ID_IS_TOO_LONG = XES_SERVICE_DOMAIN | 0x669;
    public static final int CUST_ID_IS_TOO_LONG = XES_SERVICE_DOMAIN | 0x66A;
    public static final int ACCOUNT_GROUP_IS_TOO_LONG = XES_SERVICE_DOMAIN | 0x66B;
    public static final int ACCOUNT_GROUP_NOT_FOUND = XES_SERVICE_DOMAIN | 0x66C;
    public static final int ACCT_CREATE_FAILED = XES_SERVICE_DOMAIN | 0x66D;
    public static final int ACCT_DELETE_FAILED = XES_SERVICE_DOMAIN | 0x66E;
    public static final int ACCT_READ_FAILED = XES_SERVICE_DOMAIN | 0x66F;
    public static final int INVALID_SEARCH_CRITERIA = XES_SERVICE_DOMAIN | 0x670;

    //Added By Sameer For ImportPermAssignAdd Services.
    public static final int BANK_PERM_IMPORT_DUP = XES_SERVICE_DOMAIN | 0x671;
    public static final int COMPANY_PERM_IMPORT_DUP = XES_SERVICE_DOMAIN | 0x672;
    public static final int ACCOUNTTYPE_PERM_IMPORT_DUP = XES_SERVICE_DOMAIN | 0x673;
    public static final int ACCOUNT_PERM_IMPORT_DUP = XES_SERVICE_DOMAIN | 0x674;
    public static final int USER_PERM_IMPORT_DUP = XES_SERVICE_DOMAIN | 0x675;
    public static final int USERCLASS_PERM_IMPORT_DUP = XES_SERVICE_DOMAIN | 0x676;
    public static final int USERACCOUNT_PERM_IMPORT_DUP = XES_SERVICE_DOMAIN | 0x677;
    public static final int INVALID_SRC_ENTITY_TYPE = XES_SERVICE_DOMAIN | 0x678;
    public static final int INVALID_TRG_ENTITY_TYPE = XES_SERVICE_DOMAIN | 0x679;

    //Added By Sameer For AccountGroup Services.
    public static final int ACCOUNT_GROUP_IS_INVALID = XES_SERVICE_DOMAIN | 0x67A;
    public static final int DESC_IS_TOO_LONG = XES_SERVICE_DOMAIN | 0x67B;
    public static final int ACCOUNT_GROUP_ALREADY_EXISTS = XES_SERVICE_DOMAIN | 0x67C;
    public static final int ACCOUNT_GROUP_CREATE_FAILED = XES_SERVICE_DOMAIN | 0x67D;
    public static final int ACCOUNT_GROUP_READ_FAILED = XES_SERVICE_DOMAIN | 0x67E;
    public static final int ACCOUNT_GROUP_DELETE_FAILED = XES_SERVICE_DOMAIN | 0x67F;
    public static final int ACCOUNT_GROUP_SEARCH_FAILED = XES_SERVICE_DOMAIN | 0x680;


    public static final int IFX_CANNOT_MODIFY_ELEMENT = 0x424; // IFX 1060


    //Added for ESL IFX
    public static final int INVALID_POSITIVE_NUMERIC_VALUE = XES_SERVICE_DOMAIN | 0x681;

    //Added For User services.
    public static final int INVALID_REL_AUTH = XES_SERVICE_DOMAIN | 0x682;
    public static final int INVALID_NOTIFY_USER = XES_SERVICE_DOMAIN | 0x683;

    //Added For PermAssignMod and Del Service.
    public static final int BANK_PERM_DEL = XES_SERVICE_DOMAIN | 0x684;
    public static final int ACCOUNTTYPE_PERM_DEL = XES_SERVICE_DOMAIN | 0x685;
    public static final int COMPANY_PERM_DEL = XES_SERVICE_DOMAIN | 0x686;
    public static final int ACCOUNT_PERM_DEL = XES_SERVICE_DOMAIN | 0x687;
    public static final int USER_PERM_DEL = XES_SERVICE_DOMAIN | 0x688;
    public static final int USER_ACCOUNT_PERM_DEL = XES_SERVICE_DOMAIN | 0x689;
    public static final int USER_CLASS_PERM_DEL = XES_SERVICE_DOMAIN | 0x68A;

    public static final int BANK_PERM_LMTS_CHNGED = XES_SERVICE_DOMAIN | 0x68B;
    public static final int ACCOUNTTYPE_PERM_LMTS_CHNGED = XES_SERVICE_DOMAIN | 0x68C;
    public static final int COMPANY_PERM_LMTS_CHNGED = XES_SERVICE_DOMAIN | 0x68D;
    public static final int ACCOUNT_PERM_LMTS_CHNGED = XES_SERVICE_DOMAIN | 0x68E;
    public static final int USER_PERM_LMTS_CHNGED = XES_SERVICE_DOMAIN | 0x68F;
    public static final int USERACCOUNT_PERM_LMTS_CHNGED = XES_SERVICE_DOMAIN | 0x690;
    public static final int USERCLASS_PERM_LMTS_CHNGED = XES_SERVICE_DOMAIN | 0x691;

    public static final int TRN_MAP_NO_FOUND_MULTIPLE_MAP = XES_SERVICE_DOMAIN | 0x695;
    public static final int TRN_MAP_DATABASE_EXCEPTION = XES_SERVICE_DOMAIN | 0x693;
    public static final int RECORD_NOT_FOUND = XES_SERVICE_DOMAIN | 0x6B5;
    public static final int INVALID_RECSELECT_REQUEST = XES_SERVICE_DOMAIN | 0x6B6;
    public static final int APPLICATION_ID_NOT_EXISTS = XES_SERVICE_DOMAIN | 0x6B7;

    public static final String CHANNEL_ID = "CHANNEL_ID";
    public static final String PERFORMANCE_LOGGER = "jmxPerformanceLogger";

    //Recurring funds Transfer
    public static final int INVALID_EXPIRATION_DATE=XES_SERVICE_DOMAIN | 0x6063;

    // COntrol Number Filter
    public static final int CNTRLGEN_MISSING_DATA=XES_SERVICE_DOMAIN | 0x8001;
    public static final int DATA_NOT_MAPPED_TO_SOR=XES_SERVICE_DOMAIN | 0x8002;
    public static final int IFX_NO_RECORD_DELETED = XES_SERVICE_DOMAIN | 0x6B8; // IFX -11883
    public static final int IFX_ACCOUNT_NUMBER_TOO_LONG = XES_SERVICE_DOMAIN | 0x8003; //IFX -11888
    public static final int CAN_NOT_DELETE_ON_OBJECT = XES_SERVICE_DOMAIN | 0x8007; //IFX -11887
    //Param data keys
    public static final String IFX = "IFX";
    public static final int ATTACHMENTS_NOT_VALID_FOR_CALENDAR_ITEMS = XES_SERVICE_DOMAIN | 0x8006; //IFX -11891
    public static final int CHK_PRINT_DEL_FAILURE = XES_SERVICE_DOMAIN | 0x8008; //IFX -11895
    public static final int CD_RENEWAL = XES_SERVICE_DOMAIN | 0x8009;// IFX -11884
    public static final int ACCT_MOD_DEL = XES_SERVICE_DOMAIN | 0x8010;// IFX -11886
    public static final int MULTIPLE_MAPPINGS_FOUND = 0xFFFF8AD0; // IFX -30000
    /**
     * This class is not meant to be constructed.
     */
    private IdConstants() {
    }

    public static void main(String[] args){
        //System.out.println(INVALID_USER_CREDINTIAL);
    }
}