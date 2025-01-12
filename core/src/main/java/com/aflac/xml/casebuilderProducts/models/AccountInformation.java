//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.11.06 at 11:53:43 AM IST 
//


package com.aflac.xml.casebuilderProducts.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for accountInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="accountInformation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="groupNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="groupType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="situsState" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="eligibleEmployees" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="hoursWorkedPerWeek" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="eligibilityWaitingPeriod" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ssn" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="aflacEase" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="deductionFrequency" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="partnerEligible" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="locations" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="platform" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="platformSplitValue" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="productSold" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="coverageBillingEffDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="isCoverageEffDateChanged" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="openEnrollmentStartDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="openEnrollmentEndDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="siteTestInfoDueDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="enrollmentConditionOptions" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="newHire" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="openEnrollmentSelfService" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="openEnrollmentOneToOne" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="openEnrollmentCallCenter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="newHireSelfService" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="newHireOneToOne" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="newHireOECallCenter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="clientManager" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="clientManagerEmail" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="implementationManager" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="implementationManagerEmail" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="partnerPlatformManager" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="partnerPlatformManagerEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pdfDirections" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "accountInformation", propOrder = {
    "groupNumber",
    "groupType",
    "accountName",
    "situsState",
    "eligibleEmployees",
    "hoursWorkedPerWeek",
    "eligibilityWaitingPeriod",
    "ssn",
    "aflacEase",
    "deductionFrequency",
    "partnerEligible",
    "locations",
    "platform",
    "platformSplitValue",
    "productSold",
    "coverageBillingEffDate",
    "isCoverageEffDateChanged",
    "openEnrollmentStartDate",
    "openEnrollmentEndDate",
    "siteTestInfoDueDate",
    "enrollmentConditionOptions",
    "newHire",
    "openEnrollmentSelfService",
    "openEnrollmentOneToOne",
    "openEnrollmentCallCenter",
    "newHireSelfService",
    "newHireOneToOne",
    "newHireOECallCenter",
    "clientManager",
    "clientManagerEmail",
    "implementationManager",
    "implementationManagerEmail",
    "partnerPlatformManager",
    "partnerPlatformManagerEmail",
    "pdfDirections"
})
public class AccountInformation {

    @XmlElement(required = true)
    protected String groupNumber;
    @XmlElement(required = true)
    protected String groupType;
    @XmlElement(required = true)
    protected String accountName;
    @XmlElement(required = true)
    protected String situsState;
    protected int eligibleEmployees;
    @XmlElement(required = true)
    protected String hoursWorkedPerWeek;
    @XmlElement(required = true)
    protected String eligibilityWaitingPeriod;
    @XmlElement(required = true)
    protected String ssn;
    protected String aflacEase;
    @XmlElement(required = true)
    protected String deductionFrequency;
    protected String partnerEligible;
    @XmlElement(required = true)
    protected String locations;
    @XmlElement(required = true)
    protected String platform;
    @XmlElement(required = true)
    protected String platformSplitValue;
    @XmlElement(required = true)
    protected String productSold;
    @XmlElement(required = true)
    protected String coverageBillingEffDate;
    @XmlElement(required = true)
    protected String isCoverageEffDateChanged;
    @XmlElement(required = true)
    protected String openEnrollmentStartDate;
    @XmlElement(required = true)
    protected String openEnrollmentEndDate;
    @XmlElement(required = true)
    protected String siteTestInfoDueDate;
    @XmlElement(required = true)
    protected String enrollmentConditionOptions;
    @XmlElement(required = true)
    protected String newHire;
    protected String openEnrollmentSelfService;
    protected String openEnrollmentOneToOne;
    protected String openEnrollmentCallCenter;
    protected String newHireSelfService;
    protected String newHireOneToOne;
    protected String newHireOECallCenter;
    @XmlElement(required = true)
    protected String clientManager;
    @XmlElement(required = true)
    protected String clientManagerEmail;
    @XmlElement(required = true)
    protected String implementationManager;
    @XmlElement(required = true)
    protected String implementationManagerEmail;
    protected String partnerPlatformManager;
    protected String partnerPlatformManagerEmail;
    protected String pdfDirections;

    /**
     * Gets the value of the groupNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupNumber() {
        return groupNumber;
    }

    /**
     * Sets the value of the groupNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupNumber(String value) {
        this.groupNumber = value;
    }

    /**
     * Gets the value of the groupType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupType() {
        return groupType;
    }

    /**
     * Sets the value of the groupType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupType(String value) {
        this.groupType = value;
    }

    /**
     * Gets the value of the accountName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * Sets the value of the accountName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountName(String value) {
        this.accountName = value;
    }

    /**
     * Gets the value of the situsState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSitusState() {
        return situsState;
    }

    /**
     * Sets the value of the situsState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSitusState(String value) {
        this.situsState = value;
    }

    /**
     * Gets the value of the eligibleEmployees property.
     * 
     */
    public int getEligibleEmployees() {
        return eligibleEmployees;
    }

    /**
     * Sets the value of the eligibleEmployees property.
     * 
     */
    public void setEligibleEmployees(int value) {
        this.eligibleEmployees = value;
    }

    /**
     * Gets the value of the hoursWorkedPerWeek property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHoursWorkedPerWeek() {
        return hoursWorkedPerWeek;
    }

    /**
     * Sets the value of the hoursWorkedPerWeek property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHoursWorkedPerWeek(String value) {
        this.hoursWorkedPerWeek = value;
    }

    /**
     * Gets the value of the eligibilityWaitingPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEligibilityWaitingPeriod() {
        return eligibilityWaitingPeriod;
    }

    /**
     * Sets the value of the eligibilityWaitingPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEligibilityWaitingPeriod(String value) {
        this.eligibilityWaitingPeriod = value;
    }

    /**
     * Gets the value of the ssn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * Sets the value of the ssn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSsn(String value) {
        this.ssn = value;
    }

    /**
     * Gets the value of the aflacEase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAflacEase() {
        return aflacEase;
    }

    /**
     * Sets the value of the aflacEase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAflacEase(String value) {
        this.aflacEase = value;
    }

    /**
     * Gets the value of the deductionFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeductionFrequency() {
        return deductionFrequency;
    }

    /**
     * Sets the value of the deductionFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeductionFrequency(String value) {
        this.deductionFrequency = value;
    }

    /**
     * Gets the value of the partnerEligible property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartnerEligible() {
        return partnerEligible;
    }

    /**
     * Sets the value of the partnerEligible property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartnerEligible(String value) {
        this.partnerEligible = value;
    }

    /**
     * Gets the value of the locations property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocations() {
        return locations;
    }

    /**
     * Sets the value of the locations property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocations(String value) {
        this.locations = value;
    }

    /**
     * Gets the value of the platform property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Sets the value of the platform property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlatform(String value) {
        this.platform = value;
    }

    /**
     * Gets the value of the platformSplitValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlatformSplitValue() {
        return platformSplitValue;
    }

    /**
     * Sets the value of the platformSplitValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlatformSplitValue(String value) {
        this.platformSplitValue = value;
    }

    /**
     * Gets the value of the productSold property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductSold() {
        return productSold;
    }

    /**
     * Sets the value of the productSold property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductSold(String value) {
        this.productSold = value;
    }

    /**
     * Gets the value of the coverageBillingEffDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoverageBillingEffDate() {
        return coverageBillingEffDate;
    }

    /**
     * Sets the value of the coverageBillingEffDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoverageBillingEffDate(String value) {
        this.coverageBillingEffDate = value;
    }

    /**
     * Gets the value of the isCoverageEffDateChanged property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsCoverageEffDateChanged() {
        return isCoverageEffDateChanged;
    }

    /**
     * Sets the value of the isCoverageEffDateChanged property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsCoverageEffDateChanged(String value) {
        this.isCoverageEffDateChanged = value;
    }

    /**
     * Gets the value of the openEnrollmentStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpenEnrollmentStartDate() {
        return openEnrollmentStartDate;
    }

    /**
     * Sets the value of the openEnrollmentStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpenEnrollmentStartDate(String value) {
        this.openEnrollmentStartDate = value;
    }

    /**
     * Gets the value of the openEnrollmentEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpenEnrollmentEndDate() {
        return openEnrollmentEndDate;
    }

    /**
     * Sets the value of the openEnrollmentEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpenEnrollmentEndDate(String value) {
        this.openEnrollmentEndDate = value;
    }

    /**
     * Gets the value of the siteTestInfoDueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiteTestInfoDueDate() {
        return siteTestInfoDueDate;
    }

    /**
     * Sets the value of the siteTestInfoDueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiteTestInfoDueDate(String value) {
        this.siteTestInfoDueDate = value;
    }

    /**
     * Gets the value of the enrollmentConditionOptions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnrollmentConditionOptions() {
        return enrollmentConditionOptions;
    }

    /**
     * Sets the value of the enrollmentConditionOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnrollmentConditionOptions(String value) {
        this.enrollmentConditionOptions = value;
    }

    /**
     * Gets the value of the newHire property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewHire() {
        return newHire;
    }

    /**
     * Sets the value of the newHire property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewHire(String value) {
        this.newHire = value;
    }

    /**
     * Gets the value of the openEnrollmentSelfService property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpenEnrollmentSelfService() {
        return openEnrollmentSelfService;
    }

    /**
     * Sets the value of the openEnrollmentSelfService property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpenEnrollmentSelfService(String value) {
        this.openEnrollmentSelfService = value;
    }

    /**
     * Gets the value of the openEnrollmentOneToOne property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpenEnrollmentOneToOne() {
        return openEnrollmentOneToOne;
    }

    /**
     * Sets the value of the openEnrollmentOneToOne property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpenEnrollmentOneToOne(String value) {
        this.openEnrollmentOneToOne = value;
    }

    /**
     * Gets the value of the openEnrollmentCallCenter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpenEnrollmentCallCenter() {
        return openEnrollmentCallCenter;
    }

    /**
     * Sets the value of the openEnrollmentCallCenter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpenEnrollmentCallCenter(String value) {
        this.openEnrollmentCallCenter = value;
    }

    /**
     * Gets the value of the newHireSelfService property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewHireSelfService() {
        return newHireSelfService;
    }

    /**
     * Sets the value of the newHireSelfService property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewHireSelfService(String value) {
        this.newHireSelfService = value;
    }

    /**
     * Gets the value of the newHireOneToOne property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewHireOneToOne() {
        return newHireOneToOne;
    }

    /**
     * Sets the value of the newHireOneToOne property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewHireOneToOne(String value) {
        this.newHireOneToOne = value;
    }

    /**
     * Gets the value of the newHireOECallCenter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewHireOECallCenter() {
        return newHireOECallCenter;
    }

    /**
     * Sets the value of the newHireOECallCenter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewHireOECallCenter(String value) {
        this.newHireOECallCenter = value;
    }

    /**
     * Gets the value of the clientManager property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientManager() {
        return clientManager;
    }

    /**
     * Sets the value of the clientManager property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientManager(String value) {
        this.clientManager = value;
    }

    /**
     * Gets the value of the clientManagerEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientManagerEmail() {
        return clientManagerEmail;
    }

    /**
     * Sets the value of the clientManagerEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientManagerEmail(String value) {
        this.clientManagerEmail = value;
    }

    /**
     * Gets the value of the implementationManager property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImplementationManager() {
        return implementationManager;
    }

    /**
     * Sets the value of the implementationManager property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImplementationManager(String value) {
        this.implementationManager = value;
    }

    /**
     * Gets the value of the implementationManagerEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImplementationManagerEmail() {
        return implementationManagerEmail;
    }

    /**
     * Sets the value of the implementationManagerEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImplementationManagerEmail(String value) {
        this.implementationManagerEmail = value;
    }

    /**
     * Gets the value of the partnerPlatformManager property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartnerPlatformManager() {
        return partnerPlatformManager;
    }

    /**
     * Sets the value of the partnerPlatformManager property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartnerPlatformManager(String value) {
        this.partnerPlatformManager = value;
    }

    /**
     * Gets the value of the partnerPlatformManagerEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartnerPlatformManagerEmail() {
        return partnerPlatformManagerEmail;
    }

    /**
     * Sets the value of the partnerPlatformManagerEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartnerPlatformManagerEmail(String value) {
        this.partnerPlatformManagerEmail = value;
    }

    /**
     * Gets the value of the pdfDirections property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPdfDirections() {
        return pdfDirections;
    }

    /**
     * Sets the value of the pdfDirections property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPdfDirections(String value) {
        this.pdfDirections = value;
    }

}
