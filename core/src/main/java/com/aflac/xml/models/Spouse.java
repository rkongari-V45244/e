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
 * <p>Java class for spouse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="spouse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="isEligible">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Yes"/>
 *               &lt;enumeration value="No"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="empAgeRange" type="{http://www.aflac.com/GroupMasterApplication}empAgeRange" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "spouse", propOrder = {
    "isEligible",
    "empAgeRange"
})
public class Spouse {

    @XmlElement(required = true)
    protected String isEligible;
    protected EmpAgeRange empAgeRange;

    /**
     * Gets the value of the isEligible property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsEligible() {
        return isEligible;
    }

    /**
     * Sets the value of the isEligible property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsEligible(String value) {
        this.isEligible = value;
    }

    /**
     * Gets the value of the empAgeRange property.
     * 
     * @return
     *     possible object is
     *     {@link EmpAgeRange }
     *     
     */
    public EmpAgeRange getEmpAgeRange() {
        return empAgeRange;
    }

    /**
     * Sets the value of the empAgeRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmpAgeRange }
     *     
     */
    public void setEmpAgeRange(EmpAgeRange value) {
        this.empAgeRange = value;
    }

}
