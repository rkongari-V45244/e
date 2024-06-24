//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.07.12 at 03:55:57 PM IST 
//


package com.aflac.xml.casebuilderProducts.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fileSubmission complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fileSubmission"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="productionFileNaming" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="testFileNaming" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="productionFileDueDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="testFileDueDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fileSubmission", propOrder = {
    "productionFileNaming",
    "testFileNaming",
    "productionFileDueDate",
    "testFileDueDate"
})
public class FileSubmission {

    @XmlElement(required = true)
    protected String productionFileNaming;
    @XmlElement(required = true)
    protected String testFileNaming;
    protected String productionFileDueDate;
    protected String testFileDueDate;

    /**
     * Gets the value of the productionFileNaming property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductionFileNaming() {
        return productionFileNaming;
    }

    /**
     * Sets the value of the productionFileNaming property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductionFileNaming(String value) {
        this.productionFileNaming = value;
    }

    /**
     * Gets the value of the testFileNaming property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTestFileNaming() {
        return testFileNaming;
    }

    /**
     * Sets the value of the testFileNaming property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTestFileNaming(String value) {
        this.testFileNaming = value;
    }

    /**
     * Gets the value of the productionFileDueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductionFileDueDate() {
        return productionFileDueDate;
    }

    /**
     * Sets the value of the productionFileDueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductionFileDueDate(String value) {
        this.productionFileDueDate = value;
    }

    /**
     * Gets the value of the testFileDueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTestFileDueDate() {
        return testFileDueDate;
    }

    /**
     * Sets the value of the testFileDueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTestFileDueDate(String value) {
        this.testFileDueDate = value;
    }

}