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
 * <p>Java class for childTermAmountOffered complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="childTermAmountOffered"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="amountOffered" type="{}amountOffered"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "childTermAmountOffered", propOrder = {
    "amountOffered"
})
public class ChildTermAmountOffered {

    @XmlElement(required = true)
    protected AmountOffered amountOffered;

    /**
     * Gets the value of the amountOffered property.
     * 
     * @return
     *     possible object is
     *     {@link AmountOffered }
     *     
     */
    public AmountOffered getAmountOffered() {
        return amountOffered;
    }

    /**
     * Sets the value of the amountOffered property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountOffered }
     *     
     */
    public void setAmountOffered(AmountOffered value) {
        this.amountOffered = value;
    }

}
