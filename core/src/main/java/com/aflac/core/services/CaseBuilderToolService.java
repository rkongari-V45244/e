package com.aflac.core.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;

import com.aflac.xml.casebuilder.models.Accident;
import com.aflac.xml.casebuilder.models.AccountInformation;
import com.aflac.xml.casebuilder.models.Case;
import com.aflac.xml.casebuilderProducts.models.AmountOffered;
import com.aflac.xml.casebuilderProducts.models.ChildAmountOffered;
import com.aflac.xml.casebuilderProducts.models.ChildIndividualAmountOffered;
import com.aflac.xml.casebuilderProducts.models.ChildTermAmountOffered;
import com.aflac.xml.casebuilderProducts.models.EmployeeAmountOffered;
import com.aflac.xml.casebuilderProducts.models.EnrollmentDetails;
import com.aflac.xml.casebuilderProducts.models.InitialEnrollment;
import com.aflac.xml.casebuilderProducts.models.Product;
import com.aflac.xml.casebuilderProducts.models.Products;
import com.aflac.xml.casebuilderProducts.models.SpouseAmountOffered;
import com.aflac.xml.casebuilderProducts.models.TdiStateAmountOffered;
import com.aflac.xml.casebuilderProducts.models.Tobacco;

public class CaseBuilderToolService {

	public void getAccountInformationData(AccountInformation accountInformation) {

		if (accountInformation != null) {
			accountInformation.setGroupNumber("AGC0000317811");
			accountInformation.setAccountName("Kram Endeavors Inc(CA)");
			accountInformation.setSitusState("CA-California");
			accountInformation.setEligibleEmployees(350);
			accountInformation.setSsn("SSN-VALUE");
			accountInformation.setAflacEase("No");
			accountInformation.setDeductionFrequency("Biweekly,Monthly");
			accountInformation.setPartnerEligible("No");
			accountInformation.setPlatform("Ease");
			accountInformation.setProductSold("Accident");
			accountInformation.setLocations("Locations Present");
			accountInformation.setOpenEnrollmentStartDate("06/01/2022");
			accountInformation.setOpenEnrollmentEndDate("06/24/2022");
			accountInformation.setSiteTestInfoDueDate("05/18/2022");
			accountInformation.setOpenEnrollmentSelfService("No");
			accountInformation.setOpenEnrollmentOneToOne("Yes");
			accountInformation.setOpenEnrollmentCallCenter("No");
			accountInformation.setNewHireSelfService("No");
			accountInformation.setNewHireOneToOne("No");
			accountInformation.setNewHireOECallCenter("No");
			accountInformation.setClientManager("Kim Macon");
			accountInformation.setClientManagerEmail("kmacon@aflac.com");
		}
	}

	public void getAccountInformationDataProducts(com.aflac.xml.casebuilderProducts.models.AccountInformation accountInformation, SlingHttpServletRequest request) {

		if (accountInformation != null) {
			accountInformation.setGroupNumber(request.getParameter("groupNumber"));
			accountInformation.setAccountName("Kram Endeavors Inc(CA)");
			accountInformation.setSitusState("CA-California");
			accountInformation.setEligibleEmployees(350);
			accountInformation.setSsn("SSN");
			accountInformation.setAflacEase("No");
			accountInformation.setDeductionFrequency("Biweekly,Monthly");
			accountInformation.setPartnerEligible("No");
			accountInformation.setPlatform(request.getParameter("enrollmentPlatform"));
			accountInformation.setProductSold("Accident,Hospital Indemnity,Benextend,Critical Illness,Worksite Disability");
			accountInformation.setLocations("Locations Present");
			accountInformation.setOpenEnrollmentStartDate("04/29/2023");
			accountInformation.setOpenEnrollmentEndDate("04/30/2023");
			accountInformation.setSiteTestInfoDueDate("04/27/2023");
			accountInformation.setEnrollmentConditionOptions("Call Center");
			accountInformation.setNewHire("Call Center");
			accountInformation.setOpenEnrollmentSelfService("No");
			accountInformation.setOpenEnrollmentOneToOne("Yes");
			accountInformation.setOpenEnrollmentCallCenter("No");
			accountInformation.setNewHireSelfService("No");
			accountInformation.setNewHireOneToOne("No");
			accountInformation.setNewHireOECallCenter("No");
			accountInformation.setClientManager("Kim Macon");
			accountInformation.setClientManagerEmail("kmacon@aflac.com");
			accountInformation.setImplementationManager("impManager");
			accountInformation.setImplementationManagerEmail("impManager@XYZ.com");
			accountInformation.setPartnerPlatformManager("partnerManager");
			accountInformation.setPartnerPlatformManagerEmail("partnerManager@XYZ.com");
		}
	}

	public void getProductsData(Products products) {
	
		List<Product> productList = null;
		Product product = null;
		
		TdiStateAmountOffered tdiStateAmountOffered = null;
		if (products != null) {
			product = new Product();
			tdiStateAmountOffered = new TdiStateAmountOffered();
			productList = products.getProduct();
			product.setProductName("Accident");
			product.setPlanName("Aflac Group Accident Insurance Coverage-Multiple");
			product.setPlanLevel("Multiple");
			product.setSeries("7000");
			product.setApplicationNumber("C02205CA");
			product.setCoverageLevel("Non-Occupational");
			product.setBrochuresType("Standard");
			product.setTaxType("Pre-Tax");
//				product.setHoursWorkedPerWeek("16");
//				product.setEligibilityWaitingPeriod("1st of month following 180 days");
			product.setEmployeeIssueAge("18+ , no age restriction on enrollment");
			product.setSpouseIssueAge("18+ , no age restriction on enrollment");
			product.setChildIssueAge("12-Months");
			product.setHireDate("2022-01-01");
			product.setParticipationRequirement("10");
			productList.add(product);
			
			product = new Product();
			product.setProductName("Hospital Indemnity");
			product.setPlanName("Aflac Group Hospital Indemnity Insurance Coverage - Mid");
			product.setPlanLevel("Mid");
			product.setSeries("8000");
			product.setApplicationNumber("C02205CA");
			product.setBrochuresType("Standard");
			product.setTaxType("Pre-Tax");
//				product.setHoursWorkedPerWeek("16");
//				product.setEligibilityWaitingPeriod("1st of month following 180 days");
			product.setEmployeeIssueAge("18+ , no age restriction on enrollment");
			product.setSpouseIssueAge("18+ , no age restriction on enrollment");
			product.setChildIssueAge("12-Months");
			product.setHireDate("2022-01-01");
			product.setParticipationRequirement("10");
			product.setHundredPercentGuranteed("Yes");
			productList.add(product);
			
			product = new Product();
			product.setProductName("BenExtend");
			product.setPlanName("Aflac Group BenExtend Coverage");
			product.setSeries("81000");
			product.setApplicationNumber("C02205CA");
			product.setBrochuresType("Standard");
			product.setTaxType("Pre-Tax");
//				product.setHoursWorkedPerWeek("16");
//				product.setEligibilityWaitingPeriod("1st of month following 180 days");
			product.setEmployeeIssueAge("18+ , no age restriction on enrollment");
			product.setSpouseIssueAge("18+ , no age restriction on enrollment");
			product.setChildIssueAge("12-Months");
			product.setHireDate("2022-01-01");
			product.setParticipationRequirement("10");
			product.setHundredPercentGuranteed("No");
			productList.add(product);
			
			product = new Product();
			AmountOffered ci_amountOffered = new AmountOffered();
			EmployeeAmountOffered ci_employeeAmountOffered = new EmployeeAmountOffered();
			SpouseAmountOffered ci_spouseAmountOffered = new SpouseAmountOffered();
			InitialEnrollment ci_InitialEnrollment = new InitialEnrollment();
			product.setProductName("Critical Illness");
			product.setPlanName("Aflac Group Critical Illness Insurance Coverage");
			product.setSeries("21000");
			product.setApplicationNumber("C02205CA");
			product.setBrochuresType("Standard");
			product.setTaxType("Post-Tax");
//				product.setHoursWorkedPerWeek("16");
//				product.setEligibilityWaitingPeriod("1st of month following 180 days");
			product.setEmployeeIssueAge("18+ , no age restriction on enrollment");
			product.setSpouseIssueAge("18+ , no age restriction on enrollment");
			product.setChildIssueAge("12-Months");
			ci_amountOffered.setIncrements(5000);
			ci_amountOffered.setMaximumAmtElect(30000);
			ci_amountOffered.setMinimumAmtElect(300);
			ci_amountOffered.setGuaranteedIssueMaximum(25000);
			ci_employeeAmountOffered.setAmountOffered(ci_amountOffered);
			ci_spouseAmountOffered.setAmountOffered(ci_amountOffered);
			product.setEmployeeAmountOffered(ci_employeeAmountOffered);
			product.setSpouseAmountOffered(ci_spouseAmountOffered);
			ci_InitialEnrollment.setEmployee("Up to $20000");
			ci_InitialEnrollment.setSpouse("Up to $10000");
			ci_InitialEnrollment.setSpouseCoverage("Up to 50% of the face amount elected by the employee");
			product.setInitialEnrollment(ci_InitialEnrollment);
//				product.setOptionalRider("Yes");
//				product.setProgressiveRider("Yes");
			product.setBenefitAmountPercentage("10%");
			Tobacco ci_tobacco = new Tobacco();
			ci_tobacco.setTobaccoStatus("No-Uni-Tobacco Rates- Tobacco Status Not Required");
			product.setTobacco(ci_tobacco);
			product.setPlatformDriven("Plan Year Date");
			product.setAgeCalculated("Employee & Spouse Age Captured Separately");
			product.setAgeCalculation("Attained Age");
			product.setHireDate("2022-01-01");
			product.setParticipationRequirement("10");
			productList.add(product);
			
			product = new Product();
			AmountOffered wd_amountOffered = new AmountOffered();
			InitialEnrollment wd_InitialEnrollment = new InitialEnrollment();
			product.setProductName("Disability");
			product.setPlanLevel("Multiple");
			product.setCoverageLevel("Non-Occupational");
			product.setApplicationNumber("C02205CA");
			product.setPlanName("Aflac Group Worksite Disability Insurance Coverage - Multiple");
			product.setSeries("50000");
			product.setBrochuresType("Standard");
			product.setTaxType("Pre-Tax");
//				product.setHoursWorkedPerWeek("19");
//				product.setEligibilityWaitingPeriod("1st of month following 180 days");
			product.setAgeCalculation("Issue Age - Stacking");
			product.setBenefitType("Non-Occupational");
			product.setBenefitPeriod("2-Months");
			product.setEliminationPeriod("2/6");
			product.setPlatformDriven("Plan Year;Coverage Effective Date");
			product.setEmployeeIssueAge("Employee 18-74, cannot enroll after age 74");
			wd_amountOffered.setIncrements(100);
			wd_amountOffered.setMaximumAmtElect(6000);
			wd_amountOffered.setMinimumAmtElect(300);
			wd_amountOffered.setGuaranteedIssueMaximum(25);
			tdiStateAmountOffered.setAmountOffered(wd_amountOffered);
			product.setTdiStateAmountOffered(tdiStateAmountOffered);
			wd_InitialEnrollment.setEmployee("Up to $20000");
			wd_InitialEnrollment.setSpouse("Up to $10000");
			wd_InitialEnrollment.setSpouseCoverage("Up to 50% of the face amount elected by the employee");
			product.setInitialEnrollment(wd_InitialEnrollment);
			product.setHireDate("2022-01-01");
			product.setParticipationRequirement("10");
			productList.add(product);
			
			
			product = new Product();
			Tobacco wl_tobacco = new Tobacco();
			AmountOffered wl_amountOffered = new AmountOffered();
			EmployeeAmountOffered wl_employeeAmountOffered = new EmployeeAmountOffered();
			SpouseAmountOffered wl_spouseAmountOffered = new SpouseAmountOffered();
			ChildIndividualAmountOffered wl_childIndividualAmountOffered = new ChildIndividualAmountOffered();
			ChildTermAmountOffered wl_childTermAmountOffered = new ChildTermAmountOffered();
			InitialEnrollment wl_InitialEnrollment = new InitialEnrollment();
			product.setProductName("Whole Life");
			product.setPlanName("Aflac Group Whole Life Insurance Coverage - High");
			product.setPlanLevel("High");
			product.setSeries("60000");
			product.setApplicationNumber("C02205CA");
			product.setBrochuresType("Standard");
			product.setTaxType("Pre-Tax");
//				product.setHoursWorkedPerWeek("16");
//				product.setEligibilityWaitingPeriod("180");
			product.setAgeCalculated("Employee & Spouse Age Captured Separately");
			product.setAgeCalculation("Issue Age- Stacking");
			wl_tobacco.setTobaccoStatus("Tobacco Distinct - Tobacco Status Required");
			product.setTobacco(wl_tobacco);
			product.setPlatformDriven("Plan Year;Coverage Effective Date");
			product.setEmployeeIssueAge("18 to 70,cannot enroll after age 70");
			product.setSpouseIssueAge("18 to 70,cannot enroll after age 70");
			product.setChildIssueAge("12-Months");
			wl_InitialEnrollment.setEmployee("Up to $20000");
			wl_InitialEnrollment.setSpouse("Up to $10000");
			wl_InitialEnrollment.setSpouseCoverage("Up to 50% of the face amount elected by the employee");
			product.setInitialEnrollment(wl_InitialEnrollment);
			wl_amountOffered.setIncrements(1);
			wl_amountOffered.setMaximumAmtElect(1);
			wl_amountOffered.setMinimumAmtElect(1);
			wl_amountOffered.setGuaranteedIssueMaximum(250);
			wl_employeeAmountOffered.setAmountOffered(wl_amountOffered);
			wl_spouseAmountOffered.setAmountOffered(wl_amountOffered);
			wl_childIndividualAmountOffered.setAmountOffered(wl_amountOffered);
			wl_childTermAmountOffered.setAmountOffered(wl_amountOffered);
			product.setEmployeeAmountOffered(wl_employeeAmountOffered);
			product.setSpouseAmountOffered(wl_spouseAmountOffered);
			product.setChildIndividualAmountOffered(wl_childIndividualAmountOffered);
			product.setChildTermAmountOffered(wl_childTermAmountOffered);
			product.setHireDate("2022-01-01");
			product.setParticipationRequirement("10");
			productList.add(product);
			
			product = new Product();
			Tobacco tl_tobacco = new Tobacco();
			AmountOffered tl_amountOffered = new AmountOffered();
			EmployeeAmountOffered tl_employeeAmountOffered = new EmployeeAmountOffered();
			SpouseAmountOffered tl_spouseAmountOffered = new SpouseAmountOffered();
			ChildAmountOffered tl_childAmountOffered = new ChildAmountOffered();
			InitialEnrollment tl_InitialEnrollment = new InitialEnrollment();
			product.setProductName("Term Life");
			product.setPlanName("Aflac Group Term Life Insurance Coverage");
			product.setSeries("91000");
			product.setApplicationNumber("C02205CA");
			product.setBrochuresType("Standard");
			product.setTaxType("Pre-Tax");
//				product.setHoursWorkedPerWeek("16");
//				product.setEligibilityWaitingPeriod("180");
			product.setAgeCalculated("Employee & Spouse Age Captured Separately");
			product.setAgeCalculation("Issue Age- Stacking");
			tl_tobacco.setTobaccoStatus("Tobacco Distinct - Tobacco Status Required");
			product.setTobacco(tl_tobacco);
			product.setPlatformDriven("Plan Year;Coverage Effective Date");
			product.setEmployeeIssueAge("18 to 70,cannot enroll after age 70");
			product.setSpouseIssueAge("18 to 70,cannot enroll after age 70");
			product.setChildIssueAge("12-Months");
			tl_InitialEnrollment.setEmployee("Up to $20000");
			tl_InitialEnrollment.setSpouse("Up to $10000");
			tl_InitialEnrollment.setSpouseCoverage("Up to 50% of the face amount elected by the employee");
			product.setInitialEnrollment(tl_InitialEnrollment);
			tl_amountOffered.setIncrements(1);
			tl_amountOffered.setMaximumAmtElect(1);
			tl_amountOffered.setMinimumAmtElect(1);
			tl_amountOffered.setGuaranteedIssueMaximum(2500);
			tl_employeeAmountOffered.setAmountOffered(tl_amountOffered);
			tl_spouseAmountOffered.setAmountOffered(tl_amountOffered);
			tl_childAmountOffered.setAmountOffered(tl_amountOffered);
			product.setEmployeeAmountOffered(tl_employeeAmountOffered);
			product.setSpouseAmountOffered(tl_spouseAmountOffered);
			product.setChildAmountOffered(tl_childAmountOffered);
			product.setTerm("Year");
			product.setHireDate("2022-01-01");
			product.setParticipationRequirement("10");
			productList.add(product);
		}
	}

	public void getAccidentData(Accident accident) {

		if (accident != null) {
			Case caseData = new Case();
			caseData.setPlanName("Aflac Group Accident Insurance Coverage");
			caseData.setPlanLevel("Multiple");
			caseData.setSeries("7000");
			caseData.setCoverageLevel("Non-Occupational");
			caseData.setApplicationNumber("C02205CA");
			caseData.setBrochuresType("Standard");
			caseData.setTaxType("Pre-Tax");
			caseData.setHoursWorkedPerWeek("16");
			caseData.setEligibilityWaitingPeriod("1st of month following 180 days");
			caseData.setEmployeeIssueAge("18+ , no age restriction on enrollment");
			caseData.setSpouseIssueAge("18+ , no age restriction on enrollment");
			caseData.setChildIssueAge("0 to 25(Coverage terminates at the end of their 26th birthday)");
			accident.setCase(caseData);
		}
	}

	public void getEnrollmentDetails(EnrollmentDetails enrollmentDetails) {
		List<String> enrollmentPlatformList = null;
		if(enrollmentDetails!= null) {
			enrollmentDetails.setEnrollmentRecord("Kram Endeavors Inc- 7/1/2022");
			enrollmentPlatformList = new ArrayList<String>();
			enrollmentPlatformList.add("Ease");
			enrollmentPlatformList.add("Benetrac");
			enrollmentPlatformList.add("Selerix");
			enrollmentDetails.setEnrollmentPlatform(enrollmentPlatformList);
			
		}
	}
}
