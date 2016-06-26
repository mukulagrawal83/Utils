package com.fisglobal.xpress.emea.mblox;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author e1050475
 *         Wrapper class for notification details
 */
public class NotificationMessage implements Serializable {
    public static final String BLACK_OUT_FROM = "blackOutFrom";
    public static final String BLACK_OUT_TO = "blackOutTo";
    public static final String BLACK_OUT_PERIOD = "blackOut";

    private String senderId;
    private String recipientNumber;
    private String message;
    private int messagePriority = 4;
    private String messageId;
    private Map<String, Object> properties;

    private static final Logger LOGGER = Logger.getLogger(NotificationMessage.class.getName());

    /**
     * @param messageId
     * @param senderId
     * @param recipientNumber
     * @param message
     * @param messagePriority
     */
    public NotificationMessage(String messageId, String senderId, String recipientNumber, String countryCode, String message, int messagePriority) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.recipientNumber = getMbloxFormatterNumber(recipientNumber,countryCode);
        this.message = message;
        this.messagePriority = messagePriority;
        properties = new HashMap<String, Object>();
    }

    public String getSenderId() {
        return senderId;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public String getMessage() {
        return message;
    }

    public int getMessagePriority() {
        return messagePriority;
    }

    public String getMessageId() {
        return messageId;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String key;
        builder.append("messageId=").append(this.messageId);
        builder.append("messagePriority=").append(this.messagePriority);
        builder.append("recipientNumber=").append(this.recipientNumber);
        builder.append("[");
        final Iterator<String> iterator = properties.keySet().iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            builder.append(key + " = ");
            builder.append(properties.get(key).toString());
        }
        builder.append("]");
        return builder.toString();
    }


    public String getMbloxFormatterNumber(String number, String countryCode) {
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            LOGGER.debug("looking region " + countryCode);
            String parsedCountryCode = countryCode;
            if(parsedCountryCode.startsWith("+")){
                parsedCountryCode = countryCode.substring(1);
            }
            LOGGER.debug("parsed region code " + countryCode);
            String regionCode = phoneUtil.getRegionCodeForCountryCode(Integer.parseInt(parsedCountryCode));
            LOGGER.debug("found regionCode " + regionCode);
            Phonenumber.PhoneNumber parsedNumber = phoneUtil.parse(number, regionCode);
            if (!phoneUtil.isValidNumber(parsedNumber)) {
                throw new IllegalArgumentException("Invalid phone number presented");
            }
            return new StringBuilder().append(parsedNumber.getCountryCode()).append(parsedNumber.getNationalNumber()).toString();
        } catch (NumberParseException ex) {
            LOGGER.warn("Formatting telephone number for Mblox failed number " + number + " code " + countryCode, ex);
            throw new IllegalArgumentException("Invalid phone number presented");
        }
    }
}
