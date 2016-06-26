package com.fnis.xes.ifx.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.fnf.xes.framework.ServiceException;
import com.fnis.ifx.xbo.v1_1.Data;
import com.fnis.ifx.xbo.v1_1.MalformedDataException;
import com.fnis.xes.services.IdConstants;
import com.fnis.xes.services.XESServicesConstants;
import com.fnis.xes.services.errormapping.XesHostMessageWrapper;
import com.fnis.xes.services.template.ValidationException;
import com.fnis.xes.services.template.XBOStatus;
import com.fnis.xes.services.template.XCommonBusinessService;
import com.fnis.xes.services.util.DateUtil;
import com.fnis.xes.services.util.ModXPathUtils;

import de.odysseus.el.util.SimpleContext;

/**
 * Super validator class having common methods being used by all validator objects
 * @author e1002388
 *
 */
public abstract class IFXValidator {

    protected SimpleContext elContext;
    protected ExpressionFactory elFactory;
    protected XCommonBusinessService ifxService;

    protected static final Pattern numericPattern = Pattern.compile("^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$");
    protected static final Pattern ifxPhonePattern =  Pattern.compile("(\\+)?(\\d{1,2}-\\d{1,3}-\\d{1,7})(\\+)?(\\d{1,4})?");
    protected static Pattern booleanPattern  = Pattern.compile("true|false|1|0|y|n|yes|no",Pattern.CASE_INSENSITIVE);
    List currSelElements = new ArrayList();

    public IFXValidator(XCommonBusinessService ifxService){
        this.ifxService = ifxService;
        elFactory = new de.odysseus.el.ExpressionFactoryImpl();
        elContext = new de.odysseus.el.util.SimpleContext();
    }

    /**
     * Retrieves the value from the incoming payload for the given XPath
     *
     * @param elementPath - XPath element path
     *
     * @return value if exists otherwise null
     */
    protected Object getElementValue(Object obj, String elementPath) {

        elContext.setVariable("obj", elFactory.createValueExpression(obj,
                obj.getClass()));

        StringBuffer expr = new StringBuffer("${obj.");
        expr.append(elementPath).append("}");

        ValueExpression e1 = elFactory.createValueExpression(elContext, expr
                .toString(), String.class);

        return e1.getValue(elContext);
    }

    /**
     * Retrieves the boolean value from the incoming payload for the given XPath using method expression
     * @param obj
     * @param elementPath
     * @return
     */
    protected Object getBooleanElementValue(Object obj, String elementPath) {
        elContext.setVariable("obj", elFactory.createValueExpression(obj,
                obj.getClass()));
        StringBuffer expr = new StringBuffer("${obj.");
        expr.append(elementPath).append("}");
        // Create empty array for parameter types
        Class[] paramTypes = new Class[0];
        MethodExpression mexp = elFactory.createMethodExpression(elContext, expr
                .toString(), Boolean.class, paramTypes);
        return mexp.invoke(elContext, null);
    }

    /**
     * Validates if the element is null
     *
     * @param elementPath - XPath element path
     *
     * @return - True if null or empty otherwise false
     */

    protected boolean isNull(Object obj, String elementPath) {
        if (getElementValue(obj, elementPath) == null){
            ifxService.setBeanStatus(new XBOStatus(new XesHostMessageWrapper(
                    IdConstants.IFX_REQUIRED_DATA_MISSING, elementPath)));
            return true;
        }
        else
            return false;
    }

    /**
     * Validates if the element is null or empty
     *
     * @param elementPath - XPath element path
     *
     * @return - True if null or empty otherwise false
     */
    protected boolean isNullOrEmpty(Object obj, String elementPath){
        String value = (String) getElementValue(obj, elementPath);
        return StringUtils.isEmpty(value);
    }

    /**
     * Validates the string element and its length
     *
     * @param elementPath - XPath element path
     * @param minLength - minimum length the element
     * @param maxLength - maximum length the element
     * @param bRequired - flag indicating if the element is required or optional
     *
     * @throws ValidationException - Exception if the validation fails
     */
    protected void validateString(Object obj, String elementPath, int minLength, int maxLength, boolean bRequired)
            throws ValidationException{
        String value = (String) getElementValue(obj, elementPath);
        if(StringUtils.isEmpty(value)) {
            if (bRequired) 	throw new ValidationException(IdConstants.IFX_REQUIRED_DATA_MISSING, elementPath+" is required");
        }
        else{
            if(value.length() < minLength)
                throw new ValidationException(IdConstants.IFX_INVALID_VALUE, "Below the minimum length of "+minLength+" for the element "+elementPath);
            if (value.length() > maxLength)
                throw new ValidationException(IdConstants.FIELD_MAX_LENGTH_EXCEEDED, "Max length of "+maxLength+" exceeded for the element "+elementPath);
        }
    }

    /**
     * Validates the string element and its length
     *
     * @param elementPath - XPath element path
     * @param minLength - minimum length the element
     * @param maxLength - maximum length the element
     * @param bRequired - flag indicating if the element is required or optional
     *
     * @throws ValidationException - Exception if the validation fails
     */
    protected void validateNumeric(Object obj, String elementPath, boolean bRequired)
            throws ValidationException{
        String value = (String) getElementValue(obj, elementPath);
        if (value == null || value.trim().length() == 0){
            if(bRequired)
                throw new ValidationException(IdConstants.IFX_REQUIRED_DATA_MISSING, elementPath+" is required");
        }else{
            Matcher matcher = numericPattern.matcher(value);
            if(!matcher.matches())
                throw new ValidationException(IdConstants.IFX_INVALID_NUMERIC_VALUE, elementPath + " should be numeric");
        }
    }

    protected void validateNumeric(Object obj,String elementPath,boolean bRequired,int size)
            throws ValidationException{
        validateNumeric(obj,elementPath,bRequired);
        String value = (String) getElementValue(obj, elementPath);
        if(value.trim().length() > size){
            throw new ValidationException(IdConstants.FIELD_MAX_LENGTH_EXCEEDED,"Max length of "+size+" exceeded for the element "+elementPath);
        }
    }
    /**
     * Validates the date element and its length
     *
     * @param elementPath - XPath element path
     * @param minLength - minimum length the element
     * @param maxLength - maximum length the element
     * @param bRequired - flag indicating if the element is required or optional
     *
     * @throws ValidationException - Exception if the validation fails
     */
    protected void validateDateString(Object obj, String elementPath, boolean bRequired)
            throws ValidationException{
        String value = null;
        value = (String) getElementValue(obj, elementPath);
        if (value == null || value.trim().length() == 0){
            if(bRequired)
                throw new ValidationException(IdConstants.IFX_REQUIRED_DATA_MISSING, elementPath+" is required");
        }else{
            try{
                DateUtil.getSqlDate(value, DateUtil.IFX_DATETIME_FORMAT_TZ);
            }catch(ServiceException e){
                throw new ValidationException(IdConstants.IFX_INVALID_DATETIME,
                        "Invalid date format for " + elementPath);
            }
        }
    }


    /**
     * Validates the boolean element
     * @param obj - The payload 
     * @param elementPath - XPath element path
     * @param bRequired - flag indicating if the element is required or optional
     *
     * @throws ValidationException - Exception if the validation fails
     */
    protected void validateBoolean(Object obj, String elementPath, boolean bRequired)
            throws ValidationException{
        String value = null;
        try {
            value = (String) getElementValue(obj, elementPath);
        } catch(ELException ele) {
            if (ele.getCause() instanceof MalformedDataException)
                throw new ValidationException(IdConstants.IFX_INVALID_VALUE, elementPath + " should be boolean");
        }

        if (value == null || value.trim().length() == 0) {
            if (bRequired)
                throw new ValidationException(
                        IdConstants.IFX_REQUIRED_DATA_MISSING, elementPath
                        + " is required");
        }

        if(StringUtils.isNotEmpty(value)) {
            Matcher matcher = booleanPattern.matcher(value);
            if (!matcher.matches())
                throw new ValidationException(IdConstants.IFX_INVALID_VALUE, elementPath + " should be boolean");
        }
    }

    protected void validateTimeFrame(Object obj,String startElementPath,String endElementPath) throws ValidationException{
        Date startDt,endDt;
        try{
            if(!isNullOrEmpty(obj, startElementPath) && !isNullOrEmpty(obj,endElementPath)){
                String strStartDate = (String) getElementValue(obj, startElementPath);
                String strEndDate = (String) getElementValue(obj, endElementPath);
                if(strStartDate != null && strEndDate != null) {
                    startDt = DateUtil.getDate(strStartDate, DateUtil.IFX_DATE_FORMAT);
                    endDt = DateUtil.getDate(strEndDate,  DateUtil.IFX_DATE_FORMAT);
                    if(endDt.compareTo(startDt)< 0)
                        throw new ValidationException(IdConstants.IFX_INVALID_DATETIME_RANGE," End Date in Date Range should not be less than Start Date");

                }
            }
        }

        catch(ValidationException ve){
            throw new ValidationException(ve.getErrorCode(),ve.getMessage());
        }
        catch(ServiceException se){
            throw new ValidationException(se.getErrorCode(),se.getMessage());
        }
    }
    /**
     * Retrieves the value from the incoming payload for the given XPath
     *
     * @param elementPath - XPath element path
     *
     * @return value if exists otherwise null
     */
    protected Calendar getDateElementValue(Object obj, String elementPath) {

        elContext.setVariable("obj", elFactory.createValueExpression(obj,
                obj.getClass()));

        StringBuffer expr = new StringBuffer("${obj.");
        expr.append(elementPath).append("}");

        ValueExpression e1 = elFactory.createValueExpression(elContext, expr
                .toString(), Calendar.class);

        return (Calendar)e1.getValue(elContext);
    }
    /**
     * Validates the date element and its length
     *
     * @param elementPath - XPath element path
     * @param minLength - minimum length the element
     * @param maxLength - maximum length the element
     * @param bRequired - flag indicating if the element is required or optional
     *
     * @throws ValidationException - Exception if the validation fails
     */
    protected void validateDate(Object obj, String elementPath, boolean bRequired)
            throws ValidationException{
        Calendar dateValue = null;
        dateValue = getDateElementValue(obj, elementPath);
        if (dateValue == null){
            if(bRequired)
                throw new ValidationException(IdConstants.IFX_REQUIRED_DATA_MISSING, elementPath+" is required");
        }
    }

    /**
     * Validates a Rate field
     * @param elementPath - XPath element path
     * @param maxValue - maximum value of the element
     * @param maxDecimalPrecision - maximum length of the decimal part
     * @param bRequired - flag indicating if the element is required or optional
     */
    public void validateRate(Object obj,String elementPath,int minVal,int maxValue, int maxDecimalPrecision, boolean required) throws ValidationException {
        validateNumeric(obj, elementPath, required);
        String value = (String) getElementValue(obj, elementPath);
        if (value == null || value.trim().length() == 0){
            if(required)
                throw new ValidationException(IdConstants.IFX_REQUIRED_DATA_MISSING, elementPath+" is required");
        }else{
            BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(value));
            int rateIntVal = rate.intValue();
            if(rateIntVal < minVal)
                throw new ValidationException(IdConstants.IFX_INVALID_VALUE, elementPath+" cannot be less than "+minVal);
            if(rateIntVal > maxValue)
                throw new ValidationException(IdConstants.IFX_INVALID_VALUE, elementPath+" exceedes "+maxValue);
            if(value.indexOf(".") > -1){
                String decimalPart = value.substring(value.indexOf(".")+1, value.length());
                if(decimalPart.length() > maxDecimalPrecision)
                    throw new ValidationException(IdConstants.IFX_INVALID_VALUE,"Decimal Precision for "+elementPath+" should be less than or equal to "+maxDecimalPrecision);
            }
        }
    }


    public void validateNumericRange(Object obj,String elementPath,BigDecimal minValue, BigDecimal maxValue, boolean required) throws ValidationException {
        validateNumeric(obj, elementPath, required);
        String value = (String) getElementValue(obj, elementPath);
        if(StringUtils.isNotEmpty(value)) {
            BigDecimal val = BigDecimal.valueOf(Double.parseDouble(value));
            if(val.compareTo(minValue)<0 || val.compareTo(maxValue)>0) {
                throw new ValidationException(IdConstants.IFX_INVALID_VALUE, elementPath + " should be between [" +minValue+"-"+maxValue+"]");
            }
        }
    }

    public void validateRate(Object obj,String elementPath, int maxDecimalPrecision, boolean required) throws ValidationException {
        validateNumeric(obj, elementPath, required);
        String value = (String) getElementValue(obj, elementPath);
        if (value == null || value.trim().length() == 0){
            if(required)
                throw new ValidationException(IdConstants.IFX_REQUIRED_DATA_MISSING, elementPath+" is required");
        }else{
            BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(value));
            int rateIntVal = rate.intValue();

            if(value.indexOf(".") > -1){
                String decimalPart = value.substring(value.indexOf(".")+1, value.length());
                if(decimalPart.length() > maxDecimalPrecision)
                    throw new ValidationException(IdConstants.IFX_INVALID_VALUE, "Decimal Precision for "+elementPath+" should be less than or equal to "+maxDecimalPrecision);
            }
        }
    }

    protected void validateDateStringForALS(Object obj, String elementPath, boolean bRequired)
            throws ValidationException{
        String value = null;
        value = (String) getElementValue(obj, elementPath);
        if (value == null || value.trim().length() == 0){
            if(bRequired)
                throw new ValidationException(IdConstants.IFX_REQUIRED_DATA_MISSING, elementPath+" is required");
        }
        else{
            try{
                DateUtil.getSqlDate(value, DateUtil.IFX_DATETIME_FORMAT_TZ);
            }catch(ServiceException e){
                throw new ValidationException(IdConstants.IFX_INVALID_DATETIME,
                        "Invalid date format for " + elementPath);
            }
        }
    }
    /**
     * Validates the string element and its length
     *
     * @param elementPath - XPath element path
     * @param bRequired - flag indicating if the element is required or optional
     * @param size - Exact length of the element
     *
     * @throws ValidationException - Exception if the validation fails
     */
    protected void validateNumericForExactLength(Object obj,String elementPath,boolean bRequired,int size) throws ValidationException {
        validateNumeric(obj,elementPath,bRequired);
        String value = (String) getElementValue(obj, elementPath);
        if(value.trim().length() != size){
            throw new ValidationException(IdConstants.IFX_INVALID_VALUE,"Length for the element "+elementPath+" should be "+size);
        }

    }

    protected void validateStringForExactLength(Object obj, String elementPath, int size,  boolean bRequired)
            throws ValidationException{
        String value = (String) getElementValue(obj, elementPath);
        validateString(obj, elementPath, 0, size, bRequired);
        if(value.trim().length() != size){
            throw new ValidationException(IdConstants.IFX_INVALID_VALUE,"Length for the element "+elementPath+" should be "+size);
        }
    }
    /**
     * Validates the string element and its length
     *
     * @param elementPath - XPath element path
     *
     * @throws ValidationException - Exception if the validation fails
     */
    public String getStringValue(Object obj, String elementPath)
            throws ValidationException{
        return (String) getElementValue(obj, elementPath);
    }

    /**
     * Validates the string element and its length
     *
     * @param value - value of the element
     * @param maxLength - maximum length the element
     * @param bRequired - flag indicating if the element is required or optional
     *
     * @throws ValidationException - Exception if the validation fails
     */
    public void validateLength(String value, int maxLength, String fieldName,boolean bRequired)
            throws ValidationException{
        if (value == null || value.trim().length() == 0){
            if(bRequired)
                throw new ValidationException(IdConstants.IFX_REQUIRED_DATA_MISSING, fieldName+" is required");
        }
        else{
            if(value.length() > maxLength )
                throw new ValidationException(IdConstants.FIELD_MAX_LENGTH_EXCEEDED, "Size of " + fieldName + " should be less than or equal to " + maxLength);
        }
    }

    /**
     * Validates the date element and its length
     *
     * @param elementPath - XPath element path
     * @param minLength - minimum length the element
     * @param maxLength - maximum length the element
     * @param bRequired - flag indicating if the element is required or optional
     *
     * @throws ServiceException - Exception if the validation fails
     */
    public void validateDateStringValue(Object obj, String elementPath, int maxLength,boolean bRequired)
            throws ValidationException{
        String value = (String) getElementValue(obj, elementPath);

        try {
            if (value == null || value.trim().length() == 0){
                if(bRequired)
                    throw new ValidationException(IdConstants.IFX_REQUIRED_DATA_MISSING, elementPath+" value is required");
            }else{
                if(value.length() > maxLength )
                    throw new ValidationException(IdConstants.FIELD_MAX_LENGTH_EXCEEDED, "Size of " + elementPath + " should be less than or equal to " + maxLength);

                if (DateUtil.convertDate(value,DateUtil.IFX_DATE_FORMAT, DateUtil.YYYYMMDD) == null)
                    throw new ValidationException(IdConstants.ERR_INVALID_DATE,
                            "Unable to parse date " + elementPath + "- " + value);
            }
        } catch (ServiceException exp) {
            throw new ValidationException(IdConstants.ERR_INVALID_DATE,
                    "Unable to parse date " + elementPath + "- " + value);
        }
    }

    /**
     * Validates the string element and its length
     *
     * @param elementPath - XPath element path
     * @param minLength - minimum length the element
     * @param maxLength - maximum length the element
     * @param bRequired - flag indicating if the element is required or optional
     *
     * @throws ValidationException - Exception if the validation fails
     */
    public void validateNumeric(Object obj, String elementPath, int maxLength, boolean bRequired)
            throws ValidationException{
        String value = (String) getElementValue(obj, elementPath);
        if (value == null || value.trim().length() == 0){
            if(bRequired)
                throw new ValidationException(IdConstants.IFX_REQUIRED_DATA_MISSING, elementPath+" value is required");
        }else{
            if(!NumberUtils.isNumber(value))
                throw new ValidationException(IdConstants.IFX_INVALID_NUMERIC_VALUE, elementPath + " should be numeric");
            if(value.length() > maxLength )
                throw new ValidationException(IdConstants.FIELD_MAX_LENGTH_EXCEEDED, "Size of " + elementPath + " should be less than or equal to " + maxLength);
        }
    }

    public void validateNumericWithDecimal(Object obj,String elementPath, int maxDecimalPrecision, boolean required) throws ValidationException {
        validateNumeric(obj, elementPath, required);
        String value = (String) getElementValue(obj, elementPath);
        if (value == null || value.trim().length() == 0){
            if(required)
                throw new ValidationException(IdConstants.IFX_REQUIRED_DATA_MISSING, elementPath+" is required");
        }else{
            BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(value));
            int rateIntVal = rate.intValue();

            if(value.indexOf(".") > -1){
                String decimalPart = value.substring(value.indexOf(".")+1, value.length());
                if(decimalPart.length() > maxDecimalPrecision)
                    throw new ValidationException(IdConstants.IFX_INVALID_VALUE, "Decimal Precision for "+elementPath+" should be less than or equal to "+maxDecimalPrecision);
            }
        }
    }

    /**
     * Validates whether value is containing any special characters
     * @param value
     * @param fieldName
     * @throws ValidationException
     */
    public void validateSplChars(String value, String fieldName) throws ValidationException {
        String iChars = XESServicesConstants.SPECIAL_CHARECTERS;
        for (int i = 0; i < value.length(); i++) {
            if (iChars.indexOf(value.charAt(i)) != -1) {
                throw new ValidationException(IdConstants.IFX_GENERAL_ERROR_DATA, fieldName+" is invalid. Special characters found.");
            }
        }
    }

    public void validateNumericWithDecimal(Object obj,String elementPath, int maxDecimalPrecision, boolean required,int size) throws ValidationException {
        validateNumeric(obj, elementPath, required,size);
        String value = (String) getElementValue(obj, elementPath);
        if (value == null || value.trim().length() == 0){
            if(required)
                throw new ValidationException(IdConstants.IFX_REQUIRED_DATA_MISSING, elementPath+" is required");
        }else{
            BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(value));
            int rateIntVal = rate.intValue();

            if(value.indexOf(".") > -1){
                String decimalPart = value.substring(value.indexOf(".")+1, value.length());
                if(decimalPart.length() > maxDecimalPrecision)
                    throw new ValidationException(IdConstants.IFX_INVALID_VALUE, "Decimal Precision for "+elementPath+" should be less than or equal to "+maxDecimalPrecision);
            }
        }
    }


    public void validatePhoneNumber(Object obj, String elementPath, boolean bRequired, int minLength, int maxLength) throws ValidationException {
        validateString(obj, elementPath, minLength, maxLength, bRequired);
        String value = (String) getElementValue(obj, elementPath);
        if(StringUtils.isNotEmpty(value)) {
            Matcher m = ifxPhonePattern.matcher(value);
            if (!m.matches()) {
                throw new ValidationException(IdConstants.IFX_INVALID_VALUE, elementPath + " does not confirm to IFX phone format");
            }
        }

    }

    /**
     * Validates the input data from XQO NoteSel for Same elements
     *
     * @throws ValidationException -
     *             Exception if the validation fails
     */
    public void validateSelElements(List selList) throws ValidationException
    {
        List<String> previousSelElements = new ArrayList<String>();
        if(selList == null || !(selList.size() > 1))
            return;
        for(int i=0;i<selList.size();i++){
            Data selData = (Data)selList.get(i);
            JSONObject json = selData.getData();
            getKeys(json);
            if(previousSelElements.size()>0 && (!previousSelElements.containsAll(currSelElements) || !currSelElements.containsAll(previousSelElements))){
                throw new ValidationException(IdConstants.IFX_GENERAL_ERROR_DATA, "Same elements need to be used for each Sel aggregate");
            }
            previousSelElements.clear();
            previousSelElements.addAll(currSelElements);
            currSelElements.clear();
        }
    }
    
    public <T> boolean isNullOrEmpty(Collection<T> collection){
        return collection == null || collection.isEmpty();
    }
    
    private void getKeys(JSONObject json){
        Iterator keysIterate = json.keys();
        while(keysIterate.hasNext()){
            Object iterat = keysIterate.next();
            try {
                Object objKey = json.get((String)iterat);
                if(ModXPathUtils.instanceOfSimpleType(objKey)){
                    currSelElements.add(iterat.toString());
                }else if(objKey instanceof JSONObject){
                    JSONObject jsonMap = (JSONObject)json.get((String)iterat);
                    currSelElements.add(iterat.toString());
                    getKeys(jsonMap);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
