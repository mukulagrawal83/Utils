/*
 * ===================================================================
 * Copyright Notice!
 * -------------------------------------------------------------------
 * This document is protected under the trade secret and copyright
 * laws as the property of Fidelity National Information Services, Inc.
 * Copying, reproduction or distribution should be limited and only to
 * employees with a �need to know� to do their job. 
 * Any disclosure of this document to third parties 
 * is strictly prohibited.
 *
 * �  Fidelity National Information Services
 * All rights reserved worldwide.
 * ===================================================================
 */
package com.fisglobal.xpress.csf;

import com.fis.ec.base.core.commonclasses.address.Address;
import com.fis.ec.base.core.commonclasses.address.AddressComponent;
import com.fis.ec.base.core.commonclasses.exceptions.TechnicalFailureException;
import com.fis.ec.base.core.commonclasses.logicaladdress.LogicalAddress;
import com.fis.ec.base.core.commonclasses.name.Name;
import com.fis.ec.base.core.commonclasses.name.NameComponent;
import com.fis.ec.base.core.serverclasses.ThreadContext;
import com.fis.ec.correspondence.core.commonclasses.event.CorrespondenceDeliveryMethod;
import com.fis.ec.correspondence.core.commonclasses.event.CorrespondenceEvent;
import com.fis.ec.correspondence.core.commonclasses.eventdata.CorrespondenceAddress;import com.fis.ec.correspondence.core.commonclasses.eventdata.CorrespondenceEventData;import com.fis.ec.correspondence.core.commonclasses.eventdata.CorrespondenceEventDataElement;import com.fis.ec.correspondence.core.commonclasses.eventdata.CorrespondenceEventDataField;import com.fis.ec.correspondence.core.commonclasses.eventdata.CorrespondenceEventDataGroup;import com.fis.ec.correspondence.core.commonclasses.eventprocess.CustomerCorrespondenceItems;
import com.fis.ec.correspondence.core.commonclasses.eventprocess.InternalCustomerInformation;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import java.lang.Long;import java.lang.String;import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;import java.util.Date;import java.util.Iterator;import java.util.List;import java.util.Map;import java.util.logging.Logger;

public class CSFUtility {

    private static final String NAME = "NAME";
    private static final String DEFAULT_COMPANY = "001";
    private static final String DEFAULT_LOBID = "001";
    private static final String DATE_OF_DATA_CHANGE_FIELD_NAME = "Date of Data Change";
    private static final Logger LOG = Logger.getLogger(CSFUtility.class.getName());
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(CSFUtility.class);

    public String convertToCSFFormat(ThreadContext threadContext,
                                     InternalCustomerInformation customerInformation,
                                     CorrespondenceAddress address,
                                     CorrespondenceEvent correspondenceEvent,
                                     CorrespondenceEventData correspondenceEventData,
                                     long correspondenceIdentifier, List<CustomerCorrespondenceItems> allCustomers) throws TechnicalFailureException {

        LOG.entering(this.getClass().getName(), "convertToCSFFormat");
        // Create File Node
        File csfRootNode = new File();
        Element rootElement = new Element(csfRootNode.getClass()
                .getSimpleName().toUpperCase());
        rootElement.setAttribute(new Attribute(NAME, "Atom"));
        Document document = new Document(rootElement);

        // Correspondence Event
        if (correspondenceEvent != null) {
            translateCorrespondenceEvent(correspondenceEvent, document, correspondenceEventData, address, customerInformation, correspondenceIdentifier, allCustomers);
        }

        XMLOutputter xmlOutput = new XMLOutputter();
        String xmlDoc = xmlOutput.outputString(document);
        xmlDoc = xmlDoc.substring(xmlDoc.indexOf("?>") + 2).trim();
        LOG.info("CSF input : " + xmlDoc);
        LOGGER.debug("CSF input : " + xmlDoc);
        LOG.exiting(this.getClass().getName(), "convertToCSFFormat");
        return xmlDoc;
    }

    /**
     * @param deliveryMethod
     * @param document
     */
    /*private void translateDeliveryMethod(Element element, Map<String, CorrespondenceDeliveryMethodProperty> map) {
        LOG.entering(this.getClass().getName(), "translateDeliveryMethodPropertyHeader");
        Rec deliveryMethodRecord = new Rec();
        Element deliveryMethodHeaderNode = null;
        deliveryMethodHeaderNode = new Element(deliveryMethodRecord.getClass()
                .getSimpleName().toUpperCase());
        deliveryMethodHeaderNode.setAttribute(new Attribute(NAME,
                "DeliveryMethodPropertyHeader"));

        if (!map.isEmpty()) {
            Iterator iterator = map.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                CorrespondenceDeliveryMethodProperty property = (CorrespondenceDeliveryMethodProperty) entry.getValue();

                // Delivery Property Name and value
                Fld deliveryPropNameField = new Fld();
                Element deliveryPropFieldNode = new Element(deliveryPropNameField
                        .getClass().getSimpleName().toUpperCase());
                deliveryPropFieldNode.setAttribute(new Attribute(NAME,
                        property.getPropertyName()));
                deliveryPropFieldNode.setText(property.getPropertyValue());
                deliveryMethodHeaderNode.addContent(deliveryPropFieldNode);
            }

            LOG.exiting(this.getClass().getName(), "translateDeliveryMethodPropertyHeader");
        } else {
            // Company DelMethod Prop
            Fld deliveryPropNameField = new Fld();
            Element deliveryPropFieldNode = new Element(deliveryPropNameField
                    .getClass().getSimpleName().toUpperCase());
            deliveryPropFieldNode.setAttribute(new Attribute(NAME,
                    "CompanyID"));
            deliveryPropFieldNode.setText(DEFAULT_COMPANY);
            deliveryMethodHeaderNode.addContent(deliveryPropFieldNode);

            // LOBID DelMethod Prop
            Element deliveryPropFieldNode2 = new Element(deliveryPropNameField
                    .getClass().getSimpleName().toUpperCase());
            deliveryPropFieldNode2.setAttribute(new Attribute(NAME,
                    "LineOfBusinessID"));
            deliveryPropFieldNode2.setText(DEFAULT_LOBID);
            deliveryMethodHeaderNode.addContent(deliveryPropFieldNode2);
        }

        element.addContent(deliveryMethodHeaderNode);
    }*/

    /**
     * @param address
     * @param document
     */
    private void translateCorrespondenceAddress(
            CorrespondenceAddress correspondenceAddress, Element element, List<CorrespondenceAddress> addresses) {
        LOG.entering(this.getClass().getName(),
                "translateCorrespondenceAddress");
        Rec formattedAdressRecord = new Rec();
        Element formattedAdressNode = null;
        Address address = null;
        List<AddressComponent> addressComponents = null;
        formattedAdressNode = new Element(formattedAdressRecord.getClass()
                .getSimpleName().toUpperCase());
        formattedAdressNode.setAttribute(new Attribute(NAME,
                "CorrespondenceAddressHeader"));

        address = correspondenceAddress.getAddress();
        if (address != null) {
            addressComponents = address.getAddressComponents();
            for (AddressComponent addressComponent : addressComponents) {

                Fld addressField = new Fld();
                Element addressFieldNode = new Element(addressField.getClass()
                        .getSimpleName().toUpperCase());
                // Address component type and item.
                addressFieldNode.setAttribute(new Attribute(NAME, addressComponent
                        .getAddressComponentType()));
                addressFieldNode
                        .setText(addressComponent.getAddressComponentItem());
                formattedAdressNode.addContent(addressFieldNode);
            }
        } else {
            for (CorrespondenceAddress postAddr : addresses) {
                if (postAddr.getAddressType() != null && "ADDR".equals(postAddr.getAddressType())) {

                    Fld addressField = new Fld();
                    Element addressFieldNode = new Element(addressField.getClass()
                            .getSimpleName().toUpperCase());
                    // Address component type and item.
                    addressFieldNode.setAttribute(new Attribute(NAME, "AddressType"));
                    addressFieldNode.setText("ADDR");
                    formattedAdressNode.addContent(addressFieldNode);

                    for (AddressComponent addressComponent : postAddr.getAddress().getAddressComponents()) {
                        Fld addressField2 = new Fld();
                        Element addressFieldNode2 = new Element(addressField2.getClass()
                                .getSimpleName().toUpperCase());
                        // Address component type and item.
                        addressFieldNode2.setAttribute(new Attribute(NAME, addressComponent
                                .getAddressComponentType()));
                        addressFieldNode2.setText(addressComponent.getAddressComponentItem());
                        formattedAdressNode.addContent(addressFieldNode2);
                    }
                }
            }
        }
        // Logical Address Type
        if (correspondenceAddress.getLogicalAddress() != null) {
            LogicalAddress logicalAddress = correspondenceAddress
                    .getLogicalAddress();
            if (logicalAddress.getLogicalAddressType() != null) {
                Fld logicalAddressTypeField = new Fld();
                Element logicalAddressTypeFieldNode = new Element(
                        logicalAddressTypeField.getClass().getSimpleName()
                                .toUpperCase());
                logicalAddressTypeFieldNode.setAttribute(new Attribute(NAME,
                        "LogicalAddressType"));
                logicalAddressTypeFieldNode.setText(logicalAddress
                        .getLogicalAddressType());
                formattedAdressNode.addContent(logicalAddressTypeFieldNode);
            }
            // Logical Address Value
            if (logicalAddress.getAddressValue() != null) {
                Fld logicalAddressValueField = new Fld();
                Element logicalAddressValueFieldNode = new Element(
                        logicalAddressValueField.getClass().getSimpleName()
                                .toUpperCase());
                logicalAddressValueFieldNode.setAttribute(new Attribute(NAME,
                        "LogicalAddressValue"));
                logicalAddressValueFieldNode.setText(logicalAddress
                        .getAddressValue());
                formattedAdressNode.addContent(logicalAddressValueFieldNode);
            }
        }
        element.addContent(formattedAdressNode);
        LOG.exiting(this.getClass().getName(), "translateCorrespondenceAddress");
    }

    /**
     * @param formattedNames
     * @param document
     */
    private void translateCustomerNames(Element relatedCustomerInfoNode,
                                        List<Name> customerNamesList, Document document) {
        LOG.entering(this.getClass().getName(), "translateCustomerNames");
        Rec formattedNameRecord = new Rec();
        Element customerNameNode = null;
        List<NameComponent> nameComponents = null;
        customerNameNode = new Element(formattedNameRecord.getClass()
                .getSimpleName().toUpperCase());
        // Customer Name Header
        customerNameNode
                .setAttribute(new Attribute(NAME, "CustomerNameHeader"));
        for (Name customerName : customerNamesList) {
            nameComponents = customerName.getNameComponents();
            for (NameComponent nameComponent : nameComponents) {
                String nameComponentType = nameComponent.getNameComponentType();
                if (!isNullOrEmpty(nameComponentType)
                        && !isNullOrEmpty(nameComponent.getNameComponentItem())) {
                    Fld nameField = new Fld();
                    Element nameFieldNode = new Element(nameField.getClass()
                            .getSimpleName().toUpperCase());
                    // Name Component Type and Item
                    nameFieldNode.setAttribute(new Attribute(NAME,
                            nameComponentType));
                    nameFieldNode.setText(nameComponent.getNameComponentItem());
                    customerNameNode.addContent(nameFieldNode);
                }
            }
        }
        if (relatedCustomerInfoNode != null) {
            relatedCustomerInfoNode.addContent(customerNameNode);
            document.getRootElement().addContent(relatedCustomerInfoNode);
        } else {
            document.getRootElement().addContent(customerNameNode);
        }
        LOG.exiting(this.getClass().getName(), "translateCustomerNames");
    }

    /**
     * @param event
     * @param document
     */
    private void translateCorrespondenceEvent(CorrespondenceEvent event,
                                              Document document, CorrespondenceEventData correspondenceEventData,
                                              CorrespondenceAddress address, InternalCustomerInformation customerInformation,
                                              long correspondenceIdent, List<CustomerCorrespondenceItems> allCustomers) throws TechnicalFailureException {
        LOG.entering(this.getClass().getName(), "translateCorrespondenceEvent");

        List<Long> alreadyProcessedCustomers = new ArrayList<Long>();
        Element fileHeaderNode = null;

        // File Header Group
        Rec fileHeaderRecord = new Rec();
        fileHeaderNode = new Element(fileHeaderRecord.getClass().getSimpleName().toUpperCase());
        fileHeaderNode.setAttribute(new Attribute(NAME,
                "FileHeader"));
        Rec correspondenceEventRecord = new Rec();
        Element correspondenceEventNode = null;
        correspondenceEventNode = new Element(correspondenceEventRecord
                .getClass().getSimpleName().toUpperCase());
        correspondenceEventNode.setAttribute(new Attribute(NAME,
                "CorrespondenceHeader"));

        // CorrespondenceItemIdentifier
        Fld correspondenceItemIdentField = new Fld();
        Element correspondenceItemIdentFieldNode = new Element(
                correspondenceItemIdentField.getClass().getSimpleName()
                        .toUpperCase());
        correspondenceItemIdentFieldNode.setAttribute(new Attribute(NAME,
                "CorrespondenceItemIdentifier"));
        correspondenceItemIdentFieldNode.setText(String.valueOf(correspondenceIdent));
        correspondenceEventNode.addContent(correspondenceItemIdentFieldNode);

        // RunDate
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();

        Fld correspondenceRunDateField = new Fld();
        Element correspondenceRunDateFieldNode = new Element(
                correspondenceRunDateField.getClass().getSimpleName()
                        .toUpperCase());
        correspondenceRunDateFieldNode.setAttribute(new Attribute(NAME,
                "RunDate"));
        correspondenceRunDateFieldNode.setText(dateFormat.format(date));
        correspondenceEventNode.addContent(correspondenceRunDateFieldNode);

        // Correspondence Event Group
        /*Fld correspondenceEventGrpField = new Fld();
        Element correspondenceEventGrpFieldNode = new Element(
                correspondenceEventGrpField.getClass().getSimpleName()
                        .toUpperCase());
        correspondenceEventGrpFieldNode.setAttribute(new Attribute(NAME,
                "CorrespondenceEventGroup"));
        correspondenceEventGrpFieldNode.setText(event
                .getCorrespondenceEventGroup());
        correspondenceEventNode.addContent(correspondenceEventGrpFieldNode);
        */
        // System Name
        /*Fld systemNameField = new Fld();
        Element systemNameFieldNode = new Element(systemNameField.getClass()
                .getSimpleName().toUpperCase());
        systemNameFieldNode.setAttribute(new Attribute(NAME, "SystemName"));
        systemNameFieldNode.setText(event.getSystemName());
        correspondenceEventNode.addContent(systemNameFieldNode);*/
        // Event Name
        Fld eventNameField = new Fld();
        Element eventNameFieldNode = new Element(eventNameField.getClass()
                .getSimpleName().toUpperCase());
        eventNameFieldNode.setAttribute(new Attribute(NAME, "EventName"));
        eventNameFieldNode.setText(event.getEventName());
        correspondenceEventNode.addContent(eventNameFieldNode);

        // DeliveryMethod in CorrespondenceHeader
        /*Fld delMethodField = new Fld();
        Element delMethodFieldNode = new Element(delMethodField.getClass()
                      .getSimpleName().toUpperCase());
        delMethodFieldNode.setAttribute(new Attribute(NAME, "DeliveryMethod"));

        if (correspondenceEventData.getForceDeliveryMethod() != null && event.getDeliveryMethods().get(correspondenceEventData.getForceDeliveryMethod()) != null) {
            delMethod = correspondenceEventData.getForceDeliveryMethod();
            delMethodFieldNode.setText(delMethod);
            correspondenceEventNode.addContent(delMethodFieldNode);

            translateDeliveryMethod(correspondenceEventNode,
                    event.getDeliveryMethods().get(correspondenceEventData.getForceDeliveryMethod()).
                            getDeliveryMethodProperties());
        } else {
            CorrespondenceDeliveryMethod deliveryMethod = getDeliveryMethodByAddrType(event, address.getAddressType());
            delMethod = deliveryMethod.getDeliveryMethodType();

            delMethodFieldNode.setText(delMethod);
            correspondenceEventNode.addContent(delMethodFieldNode);

            if (deliveryMethod != null) {
                translateDeliveryMethod(correspondenceEventNode, deliveryMethod.getDeliveryMethodProperties());
            } else {
                throw new TechnicalFailureException("DeliveryMethod not resolvable, and ForcedDeliveryMethod not included. ");
            }
        }*/

        // Customer Identification
        translateCustomerInformation(correspondenceEventNode, customerInformation);
        alreadyProcessedCustomers.add(customerInformation.getInvolvedPartyIdentifier());

        translateCorrespondenceAddress(address, correspondenceEventNode, customerInformation.getCorrespondenceAddresses());

        // Related Customr

        for (InternalCustomerInformation internalCustomerInformation : customerInformation.getRelatedCustomers()) {
            translateCustomerInformation(correspondenceEventNode, internalCustomerInformation);
            alreadyProcessedCustomers.add(internalCustomerInformation.getInvolvedPartyIdentifier());
        }

        //Check if any customer from original list were not processed and process if so
        for (CustomerCorrespondenceItems customerCorrespondenceItem : allCustomers) {
            if (customerCorrespondenceItem.getInternalCustomerInformation() != null) {
                InternalCustomerInformation internalCust = customerCorrespondenceItem.getInternalCustomerInformation();
                if (!alreadyProcessedCustomers.contains(internalCust.getInvolvedPartyIdentifier())) {
                    translateCustomerInformation(correspondenceEventNode, internalCust);
                    alreadyProcessedCustomers.add(internalCust.getInvolvedPartyIdentifier());
                    for (InternalCustomerInformation internalCustomerInformation : internalCust.getRelatedCustomers()) {
                        if (!alreadyProcessedCustomers.contains(internalCustomerInformation.getInvolvedPartyIdentifier())) {
                            translateCustomerInformation(correspondenceEventNode, internalCustomerInformation);
                            alreadyProcessedCustomers.add(internalCustomerInformation.getInvolvedPartyIdentifier());
                        }
                    }
                }
            }
        }
        LOGGER.debug("Customer ID's processed and passed to CSF : " + alreadyProcessedCustomers);

        // Retrieve data from CorrespondenceEventData and prepare CSF request
        List<CorrespondenceEventDataElement> dataElementsList = correspondenceEventData
                .getDataElementsList();
        for (CorrespondenceEventDataElement correspondenceEventDataElement : dataElementsList) {
            if (correspondenceEventDataElement instanceof CorrespondenceEventDataGroup) {
                populateCorrespondenceEventDataGroup(correspondenceEventNode,
                        correspondenceEventDataElement, null);
            }
        }

        fileHeaderNode.addContent(correspondenceEventNode);
        document.getRootElement().addContent(fileHeaderNode);

        LOG.exiting(this.getClass().getName(), "translateCorrespondenceEvent");
    }

    public CorrespondenceDeliveryMethod getDeliveryMethodByAddrType(CorrespondenceEvent correspondenceEvent, String addrType) {
        Iterator iterator = correspondenceEvent.getDeliveryMethods().entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            CorrespondenceDeliveryMethod deliveryMethod = (CorrespondenceDeliveryMethod) entry.getValue();

            if (deliveryMethod.getAddressType().equals(addrType)) {
                return deliveryMethod;
            }
        }

        return null;
    }

    /**
     * @param element
     * @param customerInformation
     */
    private void translateCustomerInformation(Element element,
                                              InternalCustomerInformation customerInformation) {

        LOG.entering(this.getClass().getName(), "translateCustomerInformation");

        for (Name customerName : customerInformation.getCustomerNamesList()) {

            Rec formattedNameRecord = new Rec();
            Element customerHeaderNode = null;
            List<NameComponent> nameComponents = null;
            customerHeaderNode = new Element(formattedNameRecord.getClass()
                    .getSimpleName().toUpperCase());
            // Customer Header
            customerHeaderNode
                    .setAttribute(new Attribute(NAME, "CustomerHeader"));

            // Full Name Record
            Rec fullNameRecord = new Rec();
            Element fullNameNode = null;
            fullNameNode = new Element(fullNameRecord.getClass()
                    .getSimpleName().toUpperCase());
            // Customer Name Header
            fullNameNode
                    .setAttribute(new Attribute(NAME, "FullName"));
            nameComponents = customerName.getNameComponents();
            for (NameComponent nameComponent : nameComponents) {
                String nameComponentType = nameComponent.getNameComponentType();
                if (!isNullOrEmpty(nameComponentType)
                        && !isNullOrEmpty(nameComponent.getNameComponentItem())) {
                    Fld nameField = new Fld();
                    Element nameFieldNode = new Element(nameField.getClass()
                            .getSimpleName().toUpperCase());
                    // Name Component Type and Item
                    nameFieldNode.setAttribute(new Attribute(NAME,
                            nameComponentType));
                    nameFieldNode.setText(nameComponent.getNameComponentItem());
                    fullNameNode.addContent(nameFieldNode);
                }
            }
            customerHeaderNode.addContent(fullNameNode);

            // Full Name Record
            Rec customerPropertyHeaderRecord = new Rec();
            Element customerPropertyHeaderNode = null;
            customerPropertyHeaderNode = new Element(customerPropertyHeaderRecord.getClass()
                    .getSimpleName().toUpperCase());
            // Customer Name Header
            customerPropertyHeaderNode
                    .setAttribute(new Attribute(NAME, "CustomerPropertyHeader"));

            // CustomerPropertyHeader
            Iterator iterator = customerInformation.getCustomerProperties().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();


                Fld field = new Fld();
                Element nameFieldNode = new Element(field.getClass()
                        .getSimpleName().toUpperCase());
                // Name Component Type and Item
                nameFieldNode.setAttribute(new Attribute(NAME,
                        (String) entry.getKey()));
                nameFieldNode.setText((String) entry.getValue());
                customerPropertyHeaderNode.addContent(nameFieldNode);
            }

            customerHeaderNode.addContent(customerPropertyHeaderNode);
            element.addContent(customerHeaderNode);
        }

        LOG.exiting(this.getClass().getName(), "translateCustomerInformation");
    }

    /**
     * @param customerInformation
     * @param document
     */
    private void translateCustomerIdentification(
            Element relatedCustomerInfoNode,
            InternalCustomerInformation customerInformation, Document document) {

        LOG.exiting(this.getClass().getName(),
                "translateCustomerIdentification");

        Rec custIdrecord = new Rec();
        Element custIdrecordNode = null;
        custIdrecordNode = new Element(custIdrecord.getClass().getSimpleName()
                .toUpperCase());
        custIdrecordNode.setAttribute(new Attribute(NAME,
                "CustomerIdentificationHeader"));
        Fld custIdField = new Fld();
        Element custIdFieldNode = new Element(custIdField.getClass()
                .getSimpleName().toUpperCase());
        custIdFieldNode.setAttribute(new Attribute(NAME,
                "CustomerIdentification"));
        custIdFieldNode.setText(""
                + customerInformation.getInvolvedPartyIdentifier());
        custIdrecordNode.addContent(custIdFieldNode);
        if (relatedCustomerInfoNode != null) {
            relatedCustomerInfoNode.addContent(custIdrecordNode);
        } else {
            document.getRootElement().addContent(custIdrecordNode);
        }

        LOG.exiting(this.getClass().getName(),
                "translateCustomerIdentification");
    }

    /**
     * @param document
     * @param correspondenceEventDataElement
     */
    private void populateCorrespondenceEventDataGroup(Element element,
                                                      CorrespondenceEventDataElement correspondenceEventDataElement,
                                                      Element recordNode) {
        LOG.entering(this.getClass().getName(),
                "populateCorrespondenceEventDataGroup");
        CorrespondenceEventDataGroup correspondenceEventDataGroup = (CorrespondenceEventDataGroup) correspondenceEventDataElement;
        // Iterate through correspondenceEventDataGroup list
        if (correspondenceEventDataGroup.getName() instanceof String && !"Global Group Object".equals(correspondenceEventDataGroup.getName())) {
            String groupName = correspondenceEventDataGroup.getName();
            Rec record = new Rec();
            Element recNode = null;
            recNode = new Element(record.getClass().getSimpleName()
                    .toUpperCase());
            recNode.setAttribute(new Attribute(NAME, groupName));
            if (recordNode != null) {
                recordNode.setContent(recNode);
            }
            if (!correspondenceEventDataGroup.isEmpty()) {
                for (CorrespondenceEventDataElement dataElement : correspondenceEventDataGroup
                        .getDataElementList()) {
                    if (dataElement instanceof CorrespondenceEventDataField
                            && dataElement.getName() != null) {
                        //Do not sent Date of Data Change to CSF - CSF dont use it and it has different Format because we use it to query Profile
                        if (!DATE_OF_DATA_CHANGE_FIELD_NAME.equalsIgnoreCase(dataElement.getName())) {
                            CorrespondenceEventDataField correspondenceEventDataField = (CorrespondenceEventDataField) dataElement;
                            Fld field = new Fld();
                            Element fieldNode = new Element(field.getClass()
                                    .getSimpleName().toUpperCase());
                            fieldNode.setAttribute(new Attribute(NAME,
                                    correspondenceEventDataField.getName()));
                            fieldNode.setText(correspondenceEventDataField
                                    .getValue());
                            recNode.addContent(fieldNode);
                        }
                    } else {
                        populateCorrespondenceEventDataGroup(recNode,
                                (CorrespondenceEventDataGroup) dataElement,
                                recordNode);
                        if (recNode != null) {
                            element.addContent(recNode.detach());
                        }
                    }
                }
            }
            if (recordNode != null) {
                recordNode.addContent(recNode.detach());
            } else {
                element.addContent(recNode.detach());
            }
        } else {
            for (CorrespondenceEventDataElement dataElement : correspondenceEventDataGroup.getDataElementList()) {
                populateCorrespondenceEventDataGroup(element, dataElement, recordNode);
            }
        }
        LOG.exiting(this.getClass().getName(),
                "populateCorrespondenceEventDataGroup");
    }

    /**
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(final String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }
}