//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.03.22 at 11:11:49 AM IST 
//


package com.aflac.xml.masterApp.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MasterAppReviewTableData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MasterAppReviewTableData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="formId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="apiFormId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="product" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="signStatus" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="signatureCheckbox" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="masterappSentDatetime" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="masterappReceivedDatetime" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="filenetDocumentId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="agreementId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MasterAppReviewTableData", propOrder = {
    "formId",
    "apiFormId",
    "product",
    "signStatus",
    "signatureCheckbox",
    "masterappSentDatetime",
    "masterappReceivedDatetime",
    "filenetDocumentId",
    "agreementId"
})
public class MasterAppReviewTableData {

    @XmlElement(required = true)
    protected String formId;
    @XmlElement(required = true)
    protected String apiFormId;
    @XmlElement(required = true)
    protected String product;
    @XmlElement(required = true)
    protected String signStatus;
    @XmlElement(required = true)
    protected String signatureCheckbox;
    @XmlElement(required = true)
    protected String masterappSentDatetime;
    @XmlElement(required = true)
    protected String masterappReceivedDatetime;
    @XmlElement(required = true)
    protected String filenetDocumentId;
    @XmlElement(required = true)
    protected String agreementId;

    /**
     * Gets the value of the formId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormId() {
        return formId;
    }

    /**
     * Sets the value of the formId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormId(String value) {
        this.formId = value;
    }

    /**
     * Gets the value of the apiFormId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApiFormId() {
        return apiFormId;
    }

    /**
     * Sets the value of the apiFormId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApiFormId(String value) {
        this.apiFormId = value;
    }

    /**
     * Gets the value of the product property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProduct() {
        return product;
    }

    /**
     * Sets the value of the product property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProduct(String value) {
        this.product = value;
    }

    /**
     * Gets the value of the signStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignStatus() {
        return signStatus;
    }

    /**
     * Sets the value of the signStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignStatus(String value) {
        this.signStatus = value;
    }

    /**
     * Gets the value of the signatureCheckbox property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignatureCheckbox() {
        return signatureCheckbox;
    }

    /**
     * Sets the value of the signatureCheckbox property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignatureCheckbox(String value) {
        this.signatureCheckbox = value;
    }

    /**
     * Gets the value of the masterappSentDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMasterappSentDatetime() {
        return masterappSentDatetime;
    }

    /**
     * Sets the value of the masterappSentDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMasterappSentDatetime(String value) {
        this.masterappSentDatetime = value;
    }

    /**
     * Gets the value of the masterappReceivedDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMasterappReceivedDatetime() {
        return masterappReceivedDatetime;
    }

    /**
     * Sets the value of the masterappReceivedDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMasterappReceivedDatetime(String value) {
        this.masterappReceivedDatetime = value;
    }

    /**
     * Gets the value of the filenetDocumentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilenetDocumentId() {
        return filenetDocumentId;
    }

    /**
     * Sets the value of the filenetDocumentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilenetDocumentId(String value) {
        this.filenetDocumentId = value;
    }

    /**
     * Gets the value of the agreementId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgreementId() {
        return agreementId;
    }

    /**
     * Sets the value of the agreementId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgreementId(String value) {
        this.agreementId = value;
    }

}
