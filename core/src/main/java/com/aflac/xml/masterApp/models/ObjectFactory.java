//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.01.15 at 04:00:04 PM IST 
//


package com.aflac.xml.masterApp.models;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.aflac.xml.masterApp.models package. 
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

    private final static QName _MasterApp_QNAME = new QName("", "masterApp");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.aflac.xml.masterApp.models
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MasterApp }
     * 
     */
    public MasterApp createMasterApp() {
        return new MasterApp();
    }

    /**
     * Create an instance of {@link MetaData }
     * 
     */
    public MetaData createMetaData() {
        return new MetaData();
    }

    /**
     * Create an instance of {@link AccountAndEligiblity }
     * 
     */
    public AccountAndEligiblity createAccountAndEligiblity() {
        return new AccountAndEligiblity();
    }

    /**
     * Create an instance of {@link Product }
     * 
     */
    public Product createProduct() {
        return new Product();
    }

    /**
     * Create an instance of {@link ActivelyAtWork }
     * 
     */
    public ActivelyAtWork createActivelyAtWork() {
        return new ActivelyAtWork();
    }

    /**
     * Create an instance of {@link ActiveAtWorkData }
     * 
     */
    public ActiveAtWorkData createActiveAtWorkData() {
        return new ActiveAtWorkData();
    }

    /**
     * Create an instance of {@link WaitingPeriodEligibility }
     * 
     */
    public WaitingPeriodEligibility createWaitingPeriodEligibility() {
        return new WaitingPeriodEligibility();
    }

    /**
     * Create an instance of {@link WaitingPeriodEligibilityData }
     * 
     */
    public WaitingPeriodEligibilityData createWaitingPeriodEligibilityData() {
        return new WaitingPeriodEligibilityData();
    }

    /**
     * Create an instance of {@link MasterAppReviewTableData }
     * 
     */
    public MasterAppReviewTableData createMasterAppReviewTableData() {
        return new MasterAppReviewTableData();
    }

    /**
     * Create an instance of {@link MasterAppReviewTable }
     * 
     */
    public MasterAppReviewTable createMasterAppReviewTable() {
        return new MasterAppReviewTable();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterApp }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MasterApp }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "masterApp")
    public JAXBElement<MasterApp> createMasterApp(MasterApp value) {
        return new JAXBElement<MasterApp>(_MasterApp_QNAME, MasterApp.class, null, value);
    }

}