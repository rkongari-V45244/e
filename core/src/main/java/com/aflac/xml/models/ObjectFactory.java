//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.07.29 at 12:02:24 PM IST 
//


package com.aflac.xml.models;

import java.util.ArrayList;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.aflac.xml.models package. 
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

    private final static QName _GroupMasterResponse_QNAME = new QName("http://www.aflac.com/GroupMasterApplication", "group-master-response");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.aflac.xml.models
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GroupMasterData }
     * 
     */
    public GroupMasterData createGroupMasterData() {
        return new GroupMasterData();
    }

    /**
     * Create an instance of {@link Product }
     * 
     */
    public Product createProduct(String prodId) {
    	Product product = new Product();
    	
    	if(prodId.equals("PLAN-111")) {
			product.setProductId(prodId);
			product.setProductName("Group Accident");
			product.setApplicationType("New Application");
			product.setProductSeries("70000");
			product.setRateGuaranteeLength("2 years");
			product.setPercentageByEmployee(100);
			product.setPercentageByEmployer(0);
			product.setProductProvisionCoverageType("24 Hour");
			product.setProductProvisionPortabilityType("Standard");
			
			ProductCoverages prods = new ProductCoverages();
			prods.productCoverage = new ArrayList<>();
			prods.productCoverage.add(createProductCoverage("Initial Accident Treatment Category","High"));
			prods.productCoverage.add(createProductCoverage("After Care Category","High"));
			prods.productCoverage.add(createProductCoverage("Organized Athletics Sports Rider","true"));
			product.setProductCoverages(prods);
			
			EligibilityReq emp = createEligibilityReq("Full Time","exception1", "", "");
			emp.setEmpAgeRange(createEmpAgeRange("Between Age", 70, 20, 0));
			emp.setSpouse(createSpouse("Yes",createEmpAgeRange("Between Age", 70, 20, 0)));
			product.setEligibilityReq(emp);
			
			product.setExistingPolicy(createExistingPolicy("Yes","789", "AIG"));
    	}
    	else if(prodId.equals("PLAN-222")) {
			product.setProductId(prodId);
			product.setProductName("Group Crical Illness");
			product.setApplicationType("New Application");
			product.setProductSeries("60000");
			product.setRateGuaranteeLength("1 years");
			product.setPercentageByEmployee(70);
			product.setPercentageByEmployer(30);
			
			ProductCoverages prods = new ProductCoverages();
			prods.productCoverage = new ArrayList<>();
			prods.productCoverage.add(createProductCoverage("Hospital Care Coverage","High"));
			prods.productCoverage.add(createProductCoverage("After Care Category","High"));
			prods.productCoverage.add(createProductCoverage("Medical Expense Coverage","true"));
			product.setProductCoverages(prods);
			
			EligibilityReq emp = createEligibilityReq("Full Time","exception1", "", "");
			emp.setEmpAgeRange(createEmpAgeRange("Between Age", 70, 20, 0));
			emp.setSpouse(createSpouse("No",createEmpAgeRange("Under Age", 0, 0, 15)));
			product.setEligibilityReq(emp);
			
			product.setExistingPolicy(createExistingPolicy("No","", ""));
    	}
    	else if(prodId.equals("PLAN-333")) {
			product.setProductId(prodId);
			product.setProductName("Group Hospital Indemnity");
			product.setApplicationType("New Application");
			product.setProductSeries("80000");
			product.setRateGuaranteeLength("2 years");
			product.setPercentageByEmployee(80);
			product.setPercentageByEmployer(20);
			product.setProductProvisionPortabilityType("Standard");
			product.setPlan("Regular");
			
			ProductCoverages prods = new ProductCoverages();
			prods.productCoverage = new ArrayList<>();
			prods.productCoverage.add(createProductCoverage("Hospitalization Category","Mid"));
			prods.productCoverage.add(createProductCoverage("Neonatal Pediatric ICU Ride","true"));
			product.setProductCoverages(prods);
			
			EligibilityReq emp = createEligibilityReq("Full Time","exception1", "", "");
			emp.setEmpAgeRange(createEmpAgeRange("Between Age", 70, 20, 0));
			emp.setSpouse(createSpouse("Yes",createEmpAgeRange("B", 0, 0, 15)));
			product.setEligibilityReq(emp);
			
			product.setExistingPolicy(createExistingPolicy("No","", ""));
    	}
    	
		return product;
    }

    /**
     * Create an instance of {@link GroupCriticalIllness }
     * 
     */
    public GroupCriticalIllness createGroupCriticalIllness() {
    	GroupCriticalIllness critical = new GroupCriticalIllness();
    	critical.setProduct(createProduct("PLAN-222"));
    	return critical;
    }

    /**
     * Create an instance of {@link GroupAccident }
     * 
     */
    public GroupAccident createGroupAccident() {
    	GroupAccident acc = new GroupAccident();
    	acc.setProduct(createProduct("PLAN-111"));
    	return acc;
    }

    /**
     * Create an instance of {@link GeneralRequirement }
     * 
     */
    public GeneralRequirement createGeneralRequirement(int workingHrs, String startDate, String units,
    							String basis, int noEligibleEmp, int noMiniEmp, String other) {
    	GeneralRequirement req = new GeneralRequirement();
		req.setWorkingHoursPerWeek(workingHrs);
		req.setStartDate(startDate);
		req.setNumberOfPerUnit(units);
		req.setPeriodBasis(basis);
		req.setNumberOfEligibleEmployee(noEligibleEmp);
		req.setMinimumEnrolledEmployee(noMiniEmp);
		req.setOtherRequirements(other);
		
    	return req;
    }

    /**
     * Create an instance of {@link ProductCoverages }
     * 
     */
    public ProductCoverages createProductCoverages() {
        return new ProductCoverages();
    }

    /**
     * Create an instance of {@link Spouse }
     * 
     */
    public Spouse createSpouse(String eligible, EmpAgeRange range) {
    	Spouse sp = new Spouse();
    	sp.setIsEligible(eligible);
    	sp.setEmpAgeRange(range);
    	return sp;
    }

    /**
     * Create an instance of {@link ExistingPolicy }
     * 
     */
    public ExistingPolicy createExistingPolicy(String isPolicy, String no, String carrier) {
    	ExistingPolicy existingPolicy = new ExistingPolicy();
    	existingPolicy.setPolicyExist(isPolicy);
    	existingPolicy.setPolicyNumber(no);
    	existingPolicy.setPolicyCarrier(carrier);
    	return existingPolicy;
    }

    /**
     * Create an instance of {@link EmpAgeRange }
     * 
     */
    public EmpAgeRange createEmpAgeRange(String range, int max, int min, int age) {
    	EmpAgeRange empAgeRange = new EmpAgeRange();
    	empAgeRange.setRange(range);
    	empAgeRange.setAge(age);
    	empAgeRange.setMaxAge(max);
    	empAgeRange.setMinAge(min);
    	return empAgeRange;
    }

    /**
     * Create an instance of {@link EligibilityReq }
     * 
     */
    public EligibilityReq createEligibilityReq(String empType, String exception, String clazz, String other) {
    	EligibilityReq req = new EligibilityReq();
    	req.setEmpType(empType);
    	req.setException(exception);
    	req.setClazz(clazz);
    	req.setOther(other);
    	return req;
    }

    /**
     * Create an instance of {@link ProductCoverage }
     * 
     */
    public ProductCoverage createProductCoverage(String name, String level) {
    	ProductCoverage coverage = new ProductCoverage();
    	coverage.setProductCoverageName(name);
    	coverage.setProductCoverageLevel(level);
    	return coverage;
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GroupMasterData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.aflac.com/GroupMasterApplication", name = "group-master-response")
    public JAXBElement<GroupMasterData> createGroupMasterResponse(GroupMasterData value) {
        return new JAXBElement<GroupMasterData>(_GroupMasterResponse_QNAME, GroupMasterData.class, null, value);
    }

}
