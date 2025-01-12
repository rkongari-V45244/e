//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.10.23 at 01:18:26 PM IST 
//


package com.aflac.xml.casebuilderProducts.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for caseBuilderForm complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="caseBuilderForm"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="groupNo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="coverageEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="enrollmentRecord" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="enrollmentPlatform"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="Ease"/&gt;
 *               &lt;enumeration value="Benetrac"/&gt;
 *               &lt;enumeration value="Selerix"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="accountInformation" type="{}accountInformation"/&gt;
 *         &lt;element name="fileSubmission" type="{}fileSubmission"/&gt;
 *         &lt;element name="locations" type="{}locations"/&gt;
 *         &lt;element name="products" type="{}products"/&gt;
 *         &lt;element name="isCi22kEnabled" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "caseBuilderForm", propOrder = {
    "groupNo",
    "coverageEffectiveDate",
    "enrollmentRecord",
    "enrollmentPlatform",
    "accountInformation",
    "fileSubmission",
    "locations",
    "products",
    "isCi22KEnabled",
    "version"
})
public class CaseBuilderForm {

    @XmlElement(required = true)
    protected String groupNo;
    @XmlElement(required = true)
    protected String coverageEffectiveDate;
    @XmlElement(required = true)
    protected String enrollmentRecord;
    @XmlElement(required = true)
    protected String enrollmentPlatform;
    @XmlElement(required = true)
    protected AccountInformation accountInformation;
    @XmlElement(required = true)
    protected FileSubmission fileSubmission;
    @XmlElement(required = true)
    protected Locations locations;
    @XmlElement(required = true)
    protected Products products;
    @XmlElement(name = "isCi22kEnabled")
    protected String isCi22KEnabled;
    protected int version;

    /**
     * Gets the value of the groupNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupNo() {
        return groupNo;
    }

    /**
     * Sets the value of the groupNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupNo(String value) {
        this.groupNo = value;
    }

    /**
     * Gets the value of the coverageEffectiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoverageEffectiveDate() {
        return coverageEffectiveDate;
    }

    /**
     * Sets the value of the coverageEffectiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoverageEffectiveDate(String value) {
        this.coverageEffectiveDate = value;
    }

    /**
     * Gets the value of the enrollmentRecord property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnrollmentRecord() {
        return enrollmentRecord;
    }

    /**
     * Sets the value of the enrollmentRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnrollmentRecord(String value) {
        this.enrollmentRecord = value;
    }

    /**
     * Gets the value of the enrollmentPlatform property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnrollmentPlatform() {
        return enrollmentPlatform;
    }

    /**
     * Sets the value of the enrollmentPlatform property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnrollmentPlatform(String value) {
        this.enrollmentPlatform = value;
    }

    /**
     * Gets the value of the accountInformation property.
     * 
     * @return
     *     possible object is
     *     {@link AccountInformation }
     *     
     */
    public AccountInformation getAccountInformation() {
        return accountInformation;
    }

    /**
     * Sets the value of the accountInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountInformation }
     *     
     */
    public void setAccountInformation(AccountInformation value) {
        this.accountInformation = value;
    }

    /**
     * Gets the value of the fileSubmission property.
     * 
     * @return
     *     possible object is
     *     {@link FileSubmission }
     *     
     */
    public FileSubmission getFileSubmission() {
        return fileSubmission;
    }

    /**
     * Sets the value of the fileSubmission property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileSubmission }
     *     
     */
    public void setFileSubmission(FileSubmission value) {
        this.fileSubmission = value;
    }

    /**
     * Gets the value of the locations property.
     * 
     * @return
     *     possible object is
     *     {@link Locations }
     *     
     */
    public Locations getLocations() {
        return locations;
    }

    /**
     * Sets the value of the locations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Locations }
     *     
     */
    public void setLocations(Locations value) {
        this.locations = value;
    }

    /**
     * Gets the value of the products property.
     * 
     * @return
     *     possible object is
     *     {@link Products }
     *     
     */
    public Products getProducts() {
        return products;
    }

    /**
     * Sets the value of the products property.
     * 
     * @param value
     *     allowed object is
     *     {@link Products }
     *     
     */
    public void setProducts(Products value) {
        this.products = value;
    }

    /**
     * Gets the value of the isCi22KEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsCi22KEnabled() {
        return isCi22KEnabled;
    }

    /**
     * Sets the value of the isCi22KEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsCi22KEnabled(String value) {
        this.isCi22KEnabled = value;
    }

    /**
     * Gets the value of the version property.
     * 
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     */
    public void setVersion(int value) {
        this.version = value;
    }

}
