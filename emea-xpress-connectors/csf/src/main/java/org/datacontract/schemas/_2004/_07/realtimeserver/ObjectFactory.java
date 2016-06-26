//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.21 at 02:27:23 PM CEST 
//


package org.datacontract.schemas._2004._07.realtimeserver;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.datacontract.schemas._2004._07.realtimeserver package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RenderData_QNAME = new QName("http://schemas.datacontract.org/2004/07/RealTimeServer", "RenderData");
    private final static QName _RenderDataPath_QNAME = new QName("http://schemas.datacontract.org/2004/07/RealTimeServer", "Path");
    private final static QName _RenderDataCompanionFile_QNAME = new QName("http://schemas.datacontract.org/2004/07/RealTimeServer", "CompanionFile");
    private final static QName _RenderDataCompanionData_QNAME = new QName("http://schemas.datacontract.org/2004/07/RealTimeServer", "CompanionData");
    private final static QName _RenderDataSheetCount_QNAME = new QName("http://schemas.datacontract.org/2004/07/RealTimeServer", "SheetCount");
    private final static QName _RenderDataReserved4_QNAME = new QName("http://schemas.datacontract.org/2004/07/RealTimeServer", "Reserved4");
    private final static QName _RenderDataReserved1_QNAME = new QName("http://schemas.datacontract.org/2004/07/RealTimeServer", "Reserved1");
    private final static QName _RenderDataPageCount_QNAME = new QName("http://schemas.datacontract.org/2004/07/RealTimeServer", "PageCount");
    private final static QName _RenderDataName_QNAME = new QName("http://schemas.datacontract.org/2004/07/RealTimeServer", "Name");
    private final static QName _RenderDataData_QNAME = new QName("http://schemas.datacontract.org/2004/07/RealTimeServer", "Data");
    private final static QName _RenderDataReserved3_QNAME = new QName("http://schemas.datacontract.org/2004/07/RealTimeServer", "Reserved3");
    private final static QName _RenderDataReserved2_QNAME = new QName("http://schemas.datacontract.org/2004/07/RealTimeServer", "Reserved2");
    private final static QName _RenderDataDataType_QNAME = new QName("http://schemas.datacontract.org/2004/07/RealTimeServer", "DataType");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.datacontract.schemas._2004._07.realtimeserver
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RenderData }
     * 
     */
    public RenderData createRenderData() {
        return new RenderData();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link RenderData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/RealTimeServer", name = "RenderData")
    public JAXBElement<RenderData> createRenderData(RenderData value) {
        return new JAXBElement<RenderData>(_RenderData_QNAME, RenderData.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/RealTimeServer", name = "Path", scope = RenderData.class)
    public JAXBElement<String> createRenderDataPath(String value) {
        return new JAXBElement<String>(_RenderDataPath_QNAME, String.class, RenderData.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/RealTimeServer", name = "CompanionFile", scope = RenderData.class)
    public JAXBElement<String> createRenderDataCompanionFile(String value) {
        return new JAXBElement<String>(_RenderDataCompanionFile_QNAME, String.class, RenderData.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/RealTimeServer", name = "CompanionData", scope = RenderData.class)
    public JAXBElement<String> createRenderDataCompanionData(String value) {
        return new JAXBElement<String>(_RenderDataCompanionData_QNAME, String.class, RenderData.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/RealTimeServer", name = "SheetCount", scope = RenderData.class)
    public JAXBElement<String> createRenderDataSheetCount(String value) {
        return new JAXBElement<String>(_RenderDataSheetCount_QNAME, String.class, RenderData.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/RealTimeServer", name = "Reserved4", scope = RenderData.class)
    public JAXBElement<String> createRenderDataReserved4(String value) {
        return new JAXBElement<String>(_RenderDataReserved4_QNAME, String.class, RenderData.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/RealTimeServer", name = "Reserved1", scope = RenderData.class)
    public JAXBElement<String> createRenderDataReserved1(String value) {
        return new JAXBElement<String>(_RenderDataReserved1_QNAME, String.class, RenderData.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/RealTimeServer", name = "PageCount", scope = RenderData.class)
    public JAXBElement<String> createRenderDataPageCount(String value) {
        return new JAXBElement<String>(_RenderDataPageCount_QNAME, String.class, RenderData.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/RealTimeServer", name = "Name", scope = RenderData.class)
    public JAXBElement<String> createRenderDataName(String value) {
        return new JAXBElement<String>(_RenderDataName_QNAME, String.class, RenderData.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/RealTimeServer", name = "Data", scope = RenderData.class)
    public JAXBElement<String> createRenderDataData(String value) {
        return new JAXBElement<String>(_RenderDataData_QNAME, String.class, RenderData.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/RealTimeServer", name = "Reserved3", scope = RenderData.class)
    public JAXBElement<String> createRenderDataReserved3(String value) {
        return new JAXBElement<String>(_RenderDataReserved3_QNAME, String.class, RenderData.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/RealTimeServer", name = "Reserved2", scope = RenderData.class)
    public JAXBElement<String> createRenderDataReserved2(String value) {
        return new JAXBElement<String>(_RenderDataReserved2_QNAME, String.class, RenderData.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/RealTimeServer", name = "DataType", scope = RenderData.class)
    public JAXBElement<String> createRenderDataDataType(String value) {
        return new JAXBElement<String>(_RenderDataDataType_QNAME, String.class, RenderData.class, value);
    }

}
