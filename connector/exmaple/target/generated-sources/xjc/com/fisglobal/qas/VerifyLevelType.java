//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.04 at 05:23:15 PM CET 
//


package com.fisglobal.qas;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VerifyLevelType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VerifyLevelType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="None"/&gt;
 *     &lt;enumeration value="Verified"/&gt;
 *     &lt;enumeration value="InteractionRequired"/&gt;
 *     &lt;enumeration value="PremisesPartial"/&gt;
 *     &lt;enumeration value="StreetPartial"/&gt;
 *     &lt;enumeration value="Multiple"/&gt;
 *     &lt;enumeration value="VerifiedPlace"/&gt;
 *     &lt;enumeration value="VerifiedStreet"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VerifyLevelType")
@XmlEnum
public enum VerifyLevelType {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Verified")
    VERIFIED("Verified"),
    @XmlEnumValue("InteractionRequired")
    INTERACTION_REQUIRED("InteractionRequired"),
    @XmlEnumValue("PremisesPartial")
    PREMISES_PARTIAL("PremisesPartial"),
    @XmlEnumValue("StreetPartial")
    STREET_PARTIAL("StreetPartial"),
    @XmlEnumValue("Multiple")
    MULTIPLE("Multiple"),
    @XmlEnumValue("VerifiedPlace")
    VERIFIED_PLACE("VerifiedPlace"),
    @XmlEnumValue("VerifiedStreet")
    VERIFIED_STREET("VerifiedStreet");
    private final String value;

    VerifyLevelType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VerifyLevelType fromValue(String v) {
        for (VerifyLevelType c: VerifyLevelType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
