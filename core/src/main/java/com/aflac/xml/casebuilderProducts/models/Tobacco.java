//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.01.30 at 01:01:00 PM IST 
//


package com.aflac.xml.casebuilderProducts.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tobacco complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tobacco"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="tobaccoStatus" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="tobaccoStatusDetermined" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tobacco", propOrder = {
    "tobaccoStatus",
    "tobaccoStatusDetermined"
})
public class Tobacco {

    @XmlElement(required = true)
    protected String tobaccoStatus;
    @XmlElement(required = true)
    protected String tobaccoStatusDetermined;

    /**
     * Gets the value of the tobaccoStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTobaccoStatus() {
        return tobaccoStatus;
    }

    /**
     * Sets the value of the tobaccoStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTobaccoStatus(String value) {
        this.tobaccoStatus = value;
    }

    /**
     * Gets the value of the tobaccoStatusDetermined property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTobaccoStatusDetermined() {
        return tobaccoStatusDetermined;
    }

    /**
     * Sets the value of the tobaccoStatusDetermined property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTobaccoStatusDetermined(String value) {
        this.tobaccoStatusDetermined = value;
    }

}
