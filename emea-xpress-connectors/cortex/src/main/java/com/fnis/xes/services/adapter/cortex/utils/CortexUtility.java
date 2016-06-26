package com.fnis.xes.services.adapter.cortex.utils;

import com.fnf.xes.framework.codetables.CodeTableKey;
import com.fnf.xes.framework.codetables.TableLookupException;
import com.fnf.xes.framework.codetables.ability.CodeTableLookupAbility;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
 * Time: 11:30
 */
public class CortexUtility {

    private static final String EMPTY_STRING = "";

    public static String swapNull(String str) {
        if(null == str) {
          return EMPTY_STRING;
        }
        if(null != str && str.trim().length() == 0) {
            return EMPTY_STRING;
       }
       return str;
    }

    public static String trimString(String str)	{
        return (str!=null && str.trim().length() > 0) ? str.trim() : str ;
    }

    public static String ctuLookup(String tableName, String srcDomain, String srcPropertyValue, String targetDomain, CodeTableLookupAbility ctuAbility) throws TableLookupException {
        CodeTableKey ctuKey = new CodeTableKey(tableName, srcDomain, srcPropertyValue, targetDomain);
        String lookupValue = null;

        try{
            lookupValue = ctuAbility.lookup(ctuKey);
        } catch (TableLookupException e){
            throw new TableLookupException("Value is not configured in CTU Lookup");
        }
        return lookupValue;
    }


    public static String ctuLookup(String tableName, String srcDomain, String parentPropertyValue, String srcPropertyValue,String targetDomain, CodeTableLookupAbility ctuAbility) throws TableLookupException {
        CodeTableKey ctuKey = new CodeTableKey(tableName, srcDomain, parentPropertyValue, srcPropertyValue, targetDomain);
        String lookupValue = null;

        try{
            lookupValue = ctuAbility.lookup(ctuKey);
        } catch (TableLookupException e){
            throw new TableLookupException("Value is not configured in CTU Lookup");
        }

        return lookupValue.substring(0, lookupValue.indexOf("|"));
    }

    public static String ctuLookupReverse(String tableName, String srcDomain, String parentPropertyValue, String srcPropertyValue,String targetDomain, CodeTableLookupAbility ctuAbility) throws TableLookupException {

        if(null == parentPropertyValue)  {   //simple lookup
            return ctuLookup(tableName, srcDomain, srcPropertyValue, targetDomain, ctuAbility);
        }  else {        //Parent - Child table lookup
            CodeTableKey ctuKey = new CodeTableKey(tableName, srcDomain, parentPropertyValue, srcPropertyValue, targetDomain);
            String lookupValue = null;

            try{
                lookupValue = ctuAbility.lookup(ctuKey);
            } catch (TableLookupException e){
                throw new TableLookupException("Value is not configured in CTU Lookup");
            }

            return lookupValue.substring(lookupValue.indexOf("|")+1);
        }
    }

    public static String ctuLookupForValidation(String tableName, String srcDomain, String parentPropertyValue, String srcPropertyValue, String targetDomain, CodeTableLookupAbility ctuAbility){
        try {
            CodeTableKey ctuKey = new CodeTableKey(tableName, srcDomain, parentPropertyValue, srcPropertyValue, targetDomain);
//            ctuKey.setBusinessLocaleId("en_US");
            String lookupValue = ctuAbility.lookup(ctuKey);
            return lookupValue.substring(0, lookupValue.indexOf("|"));
        } catch (TableLookupException e) {
            return null;
        }
    }

    public static Calendar populateLocalDate(String expDateStr) throws Exception{
        DateFormat ifxReturnFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = ifxReturnFormat.parse(expDateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    public static int populateLocalTime(Calendar calendar) throws Exception {
        DateFormat cortexLocalTimeFormat = new SimpleDateFormat("HHmmss");
        return Integer.parseInt(cortexLocalTimeFormat.format(calendar.getTime()));
    }


    //Added new method support Calendar to String
    public static String populateLocalDateToString(Calendar expDateStr) throws Exception{
        DateFormat ifxReturnFormat = new SimpleDateFormat("yyyy-MM-dd");
        return ifxReturnFormat.format(expDateStr.getTime());
    }

    public static double convertStringToDouble(String input) throws Exception{
        NumberFormat numberFormat = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
        return numberFormat.parse(StringUtils.defaultString(input,"0").trim()).doubleValue();
    }


    public static BigDecimal convertDoubleToBigDecimal(double input) throws Exception {
        // PS-12423 changed to always include 2 decimal places in results. Prior version truncated 0 cents amount
        return BigDecimal.valueOf(input).setScale(2);
    }

}
