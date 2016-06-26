package com.fnis.xes.framework.ext.processxbo.subject.xbo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IFX2MessageTypeParser {
    private static final String[] IFXMessageTypes = {
            "Add",
            "Mod",
            "Del",
            "Can",
            "Inq",
            "Advise",
            "StatusMod",
            "StatusInq",
            "AuthMod",
            "AuthInq",
            "Aud",
            "Rev",
            "Sync",
            // Aggregate
            "Oper"
    };

    private static final String[] IFXObjectNames = {
            "Acct",
            "AcctAcctRel",
            "AcctHold",
            "AcctPayOff",
            "AcctStmt",
            "AcctTrn",
            "AcctTrnImg",
            "Alert",
            "Ath",
            "Bill",
            "Biller",
            "Card",
            "CardAcctRel",
            "CardOrder",
            "CardUpdate",
            "ChkAccept",
            "ChkIssue",
            "ChkOrd",
            "Chksum",
            "CompRemitStmt",
            "Credit",
            "CustPayee",
            "Debit",
            "DepBkOrd",
            "Dev",
            "Disc",
            "ForExDeal",
            "ForExQuote",
            "ForExRateSheet",
            "ICCUpdate",
            "Inventory",
            "MagCardUpdate",
            "MediaAcct",
            "MediaAcctTrn",
            "Note",
            "Offer",
            "Party",
            "PartyAcctRel",
            "PartyCardRel",
            "PartyPartyRel",
            "PartySvcAcctRel",
            "PartySvcRel",
            "Passbk",
            "PassbkItem",
            "Pmt",
            "PmtBatch",
            "PmtBatchStat",
            "PmtEncl",
            "ProdIntRate",
            "PurchItem",
            "RecurChkOrd",
            "RecurPmt",
            "RecurXfer",
            "Remit",
            "Restrict",
            "SecObj",
            "StdPayee",
            "StopChk",
            "Svc",
            "SvcProvider",
            "TerminalObj",
            "TerminalSPObj",
            "Trn",
            "Xfer",
            // new
            "Collateral",
            "ApplBook",
            "AuditHist",
            "CommonData",
            "TaxDeferredPlan",
            "TellerTrn",
            "ChkPrint",
            "Application",
            "Correspdnce",
            "Notification",
            "RecXfer",
            "BusinessCase",
            "Permission",
            "UserRole",
            "HelloWorld",
            "HelloWorld_02",
            "PartySafeBoxRel",
            "Prod",
            "Service",
            "TaxDeferredPlanBenRel",
            "OutOfWallet",
            "PartyQualification",
            "PartyVerification",
            "Authent",
            "CalendarItem",
            "Case",
            "ContactHistory",
            "UserAuth",
            "User",
            "XpressSys",
            "YodleeAcct",
            // ING NL Specific
            "AcctRestrict",
            "LoanAcctActivation"
    };

    private static IFX2MessageTypeParser instance;
    Pattern pattern;

    public static IFX2MessageTypeParser getInstance() {
        if (instance == null) {
            instance = new IFX2MessageTypeParser();
        }

        return instance;
    }

    private IFX2MessageTypeParser() {
        String actionTypeMatcher = "(" + createAltRegString(IFXMessageTypes) + ")" + "(" + IFX2MessageConstants.MSG_TYPE_RQ + "|" + IFX2MessageConstants.MSG_TYPE_RS + ")";
        String objectNameMatcher = "(" + createAltRegString(IFXObjectNames) + ")";
        String strPattern = objectNameMatcher + actionTypeMatcher;

        pattern = Pattern.compile(strPattern);
    }

    public Matcher getMatcher(String s) {
        return pattern.matcher(s);
    }


    private String createAltRegString(String[] alternatives) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < alternatives.length; i++) {
            String ifxMessageBasicType = alternatives[i];
            sb.append(alternatives[i]);
            if (i < alternatives.length - 1) {
                sb.append("|");
            }
        }
        return sb.toString();
    }
}
