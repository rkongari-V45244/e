//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.07.29 at 12:02:24 PM IST 
//


package com.aflac.xml.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for productCoverage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="productCoverage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="productCoverageName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="productCoverageLevel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productCoverage", propOrder = {
    "productCoverageName",
    "productCoverageLevel"
})
public class ProductCoverage {

    @XmlElement(required = true)
    protected String productCoverageName;
    @XmlElement(required = true)
    protected String productCoverageLevel;

    /**
     * Gets the value of the productCoverageName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductCoverageName() {
        return productCoverageName;
    }

    /**
     * Sets the value of the productCoverageName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductCoverageName(String value) {
        this.productCoverageName = value;
    }

    /**
     * Gets the value of the productCoverageLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductCoverageLevel() {
        return productCoverageLevel;
    }

    /**
     * Sets the value of the productCoverageLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductCoverageLevel(String value) {
        this.productCoverageLevel = value;
    }

}
