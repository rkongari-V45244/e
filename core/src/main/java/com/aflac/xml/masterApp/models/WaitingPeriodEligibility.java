//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.12.12 at 03:05:36 PM IST 
//


package com.aflac.xml.masterApp.models;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WaitingPeriodEligibility complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WaitingPeriodEligibility"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="WaitingPeriodEligibilityData" type="{}WaitingPeriodEligibilityData" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WaitingPeriodEligibility", propOrder = {
    "waitingPeriodEligibilityData"
})
public class WaitingPeriodEligibility {

    @XmlElement(name = "WaitingPeriodEligibilityData")
    protected List<WaitingPeriodEligibilityData> waitingPeriodEligibilityData;

    /**
     * Gets the value of the waitingPeriodEligibilityData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the waitingPeriodEligibilityData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWaitingPeriodEligibilityData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WaitingPeriodEligibilityData }
     * 
     * 
     */
    public List<WaitingPeriodEligibilityData> getWaitingPeriodEligibilityData() {
        if (waitingPeriodEligibilityData == null) {
            waitingPeriodEligibilityData = new ArrayList<WaitingPeriodEligibilityData>();
        }
        return this.waitingPeriodEligibilityData;
    }

}
