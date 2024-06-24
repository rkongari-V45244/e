package com.aflac.core.osgi.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.ExperienceApiConfig;
import com.aflac.core.experience.api.model.AgeDetails;
import com.aflac.core.experience.api.model.ApiAccountInformation;
import com.aflac.core.experience.api.model.ApiCaseBuilderForm;
import com.aflac.core.experience.api.model.ApiEnrollmentDetails;
import com.aflac.core.experience.api.model.ApiProducts;
import com.aflac.core.experience.api.model.AuditUserActivity;
import com.aflac.core.experience.api.model.ComplianceVerbiageDetail;
import com.aflac.core.experience.api.model.FaceAmountOffered;
import com.aflac.core.experience.api.model.LocationDetail;
import com.aflac.core.experience.api.model.LocationDetails;
import com.aflac.core.experience.api.model.Manager;
import com.aflac.core.experience.api.model.OpenEnrollmentDetails;
import com.aflac.core.experience.api.model.SaveCasebuildResponse;
import com.aflac.core.osgi.service.AuditService;
import com.aflac.core.osgi.service.CaseBuilderService;
import com.aflac.core.util.ExcelGenerationUtil;
import com.aflac.xml.casebuilderProducts.models.AccountInformation;
import com.aflac.xml.casebuilderProducts.models.AmountOffered;
import com.aflac.xml.casebuilderProducts.models.CaseBuilderForm;
import com.aflac.xml.casebuilderProducts.models.ChildAmountOffered;
import com.aflac.xml.casebuilderProducts.models.ChildIndividualAmountOffered;
import com.aflac.xml.casebuilderProducts.models.ChildTermAmountOffered;
import com.aflac.xml.casebuilderProducts.models.EmployeeAmountOffered;
import com.aflac.xml.casebuilderProducts.models.EnrollmentDetails;
import com.aflac.xml.casebuilderProducts.models.FileSubmission;
import com.aflac.xml.casebuilderProducts.models.Location;
import com.aflac.xml.casebuilderProducts.models.Locations;
import com.aflac.xml.casebuilderProducts.models.Product;
import com.aflac.xml.casebuilderProducts.models.Products;
import com.aflac.xml.casebuilderProducts.models.SpouseAmountOffered;
import com.aflac.xml.casebuilderProducts.models.TdiStateAmountOffered;
import com.aflac.xml.casebuilderProducts.models.Tobacco;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

@Component(
	service = { CaseBuilderService.class }
)
public class CaseBuilderServiceImpl implements CaseBuilderService , Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(CaseBuilderServiceImpl.class);
	private transient CloseableHttpClient httpclient;
	private transient ObjectMapper objectMapper;
	@Reference
	private transient ExperienceApiConfig apiUrlService;
	@Reference
	private transient AuditService auditService;
	
	public CaseBuilderServiceImpl() { 
		this.httpclient = HttpClients.createDefault();
		this.objectMapper = new ObjectMapper();
	}

	public CaseBuilderServiceImpl(CloseableHttpClient httpclient,ExperienceApiConfig apiUrlService, AuditService auditService) {
		this.httpclient = httpclient;
		this.objectMapper = new ObjectMapper();
		this.apiUrlService = apiUrlService;
		this.auditService = auditService;
	}

	@Override
	public CaseBuilderForm getCaseBuilderForm(String groupNo, String effDate, String platform) {
		
		CaseBuilderForm caseBuild = null;
		HttpGet http = new HttpGet(apiUrlService.getCaseBuildAccountsUrl(groupNo, effDate, platform));
		log.info("Requested Group Number:"+groupNo);
		log.info("Requested Effective Date:"+effDate);
		log.info("Requested Platform:"+platform);
		try{
			String data = apiUrlService.executeHttpGet(httpclient, http);
		    if(data != null) {
		    	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				ApiCaseBuilderForm apiCaseBild = objectMapper.readValue(data, ApiCaseBuilderForm.class);
				if(apiCaseBild != null) {
					caseBuild = new CaseBuilderForm();
					if(apiCaseBild.getIsCi22KEnabled() != null)
						caseBuild.setIsCi22KEnabled((boolean)apiCaseBild.getIsCi22KEnabled() ? "Yes" : "No");
					else
						caseBuild.setIsCi22KEnabled("No");
					caseBuild.setAccountInformation(new AccountInformation());
				    caseBuild.setProducts(new Products());
				    caseBuild.setFileSubmission(new FileSubmission());
				    caseBuild.setLocations(new Locations());
				    caseBuild.setVersion(apiCaseBild.getVersion());
				    setAccounInfo(caseBuild.getAccountInformation(), apiCaseBild, groupNo);
				    setFileSubmission(caseBuild.getFileSubmission(),apiCaseBild);
				    setProducts(caseBuild.getProducts(), apiCaseBild.getProducts());
				    setLocations(caseBuild.getLocations(), apiCaseBild);
				}
			    return caseBuild;
		    }
		} catch (IOException e1) {
			log.error("getCaseBuild error: ", e1);
		}
		return caseBuild;
	}
	
	@Override
	public SaveCasebuildResponse saveCaseBuilderForm(String formData, AuditUserActivity audit, String zone) {
		SaveCasebuildResponse saveCasebuildResponse = null;
		try {
			ApiCaseBuilderForm apiCase = getApiCaseBuildForm(ExcelGenerationUtil.xmlToJava(formData));
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			String formJson = objectMapper.writeValueAsString(apiCase);
			HttpPut httpPut = new HttpPut(apiUrlService.getCaseBuildSaveUrl());
			httpPut.setHeader("Content-type", "application/json");
			String apiResponse = apiUrlService.executeHttpPut(httpclient, httpPut,formJson);
			boolean status = false;
			if(apiResponse != null) {
				saveCasebuildResponse = objectMapper.readValue(apiResponse, SaveCasebuildResponse.class);
    			status = saveCasebuildResponse.isStatus();
			}
			log.info("CaseBuild Save Status: " + status);
			if(status) {
				ZoneId zoneId = ZoneId.of(zone);
				LocalDateTime current = LocalDateTime.now(zoneId);
				audit.setCreatedDateTime(current.toString());
			    audit.setApplicationId(apiCase.getAccountinformation().getGroupNumber()+"#"+apiCase.getCoveragebillingeffectivedate()+"#"+apiCase.getAccountinformation().getPlatform());
			    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime start = LocalDateTime.parse(audit.getQueryDateTime(), format);
			    String totalTime = auditService.getTurnAroundTime(current,start);
				audit.setTurnaroundTime(totalTime);
				audit.setApplicationData("");
				auditService.saveAuditEntry(audit);
			}
			return saveCasebuildResponse;
		} catch (IOException e) {
			log.error("saveCaseBuilder Api error: ", e);
		}
		return saveCasebuildResponse;
	}
	
	@Override
	public String getFormId(String situs, String series,String groupType,Boolean giAmount) {
		HttpGet http = new HttpGet(apiUrlService.getFormIdUrl(situs, series,groupType,giAmount));
		String apiResponse = null;
		try {
			apiResponse = apiUrlService.executeHttpGet(httpclient, http);
		} catch (IOException e) {
			log.error("FormId Get Api Error: ", e);
		}		
		return apiResponse;
	}
	
	@Override
	public String getAgeBasedOn(String lob, String platform,String ageCalculation) {
		HttpGet http = new HttpGet(apiUrlService.getAgeBasedOnUrl(lob, platform, ageCalculation));
		String apiResponse = "";
		try {
			apiResponse = apiUrlService.executeHttpGet(httpclient, http);
		} catch (IOException e) {
			log.error("getAgeBasedOn Api Error: ", e);
		}		
		return apiResponse;
	}
	
	@Override
	public String getIssueAges(String situs, String series,String term,String lob) {
		HttpGet http = new HttpGet(apiUrlService.getIssueAgesUrl(situs, series,term,lob));
		String apiResponse = null;
		try {
			apiResponse = apiUrlService.executeHttpGet(httpclient, http);
		} catch (IOException e) {
			log.error("Issue Ages Get Api Error: ", e);
		}
		return apiResponse;
	}
	
	private ApiCaseBuilderForm getApiCaseBuildForm(CaseBuilderForm caseBuild) {
		ApiCaseBuilderForm apiCase = new ApiCaseBuilderForm();
		apiCase.setCoveragebillingeffectivedate(caseBuild.getAccountInformation().getCoverageBillingEffDate());
		apiCase.setCoverageEffectiveDateChanged((caseBuild.getAccountInformation().getIsCoverageEffDateChanged()!=null && caseBuild.getAccountInformation().getIsCoverageEffDateChanged().equalsIgnoreCase("Yes")) ? true : false);
		apiCase.setSitetestinfoduedate(caseBuild.getAccountInformation().getSiteTestInfoDueDate());
		apiCase.setNewHire(caseBuild.getAccountInformation().getNewHire());
		apiCase.setProductionFileNaming(caseBuild.getFileSubmission().getProductionFileNaming());
		apiCase.setProductionFileDueDate(convertDateCba(caseBuild.getFileSubmission().getProductionFileDueDate()));
		apiCase.setTestFileNaming(caseBuild.getFileSubmission().getTestFileNaming());
		apiCase.setTestFileDueDate(convertDateCba(caseBuild.getFileSubmission().getTestFileDueDate()));
		apiCase.setIsCi22KEnabled(caseBuild.getIsCi22KEnabled()!=null && caseBuild.getIsCi22KEnabled().equalsIgnoreCase("Yes") ? true : false);
		apiCase.setVersion(caseBuild.getVersion());
		
		ApiAccountInformation apiAcc = new ApiAccountInformation();
		apiAcc.setAccountName(caseBuild.getAccountInformation().getAccountName());
		apiAcc.setGroupNumber(caseBuild.getAccountInformation().getGroupNumber());
		apiAcc.setGroupType(caseBuild.getAccountInformation().getGroupType());
		apiAcc.setSitusstate(caseBuild.getAccountInformation().getSitusState());
		apiAcc.setNoofeligibleemployee(caseBuild.getAccountInformation().getEligibleEmployees());
		apiAcc.setSsnoreeid(caseBuild.getAccountInformation().getSsn());
		if(caseBuild.getAccountInformation().getAflacEase() != null)
			apiAcc.setAflacease(caseBuild.getAccountInformation().getAflacEase().equalsIgnoreCase("Yes") ? true : false);
		apiAcc.setDeductionfrequency(Arrays.asList(caseBuild.getAccountInformation().getDeductionFrequency()));
		if(caseBuild.getAccountInformation().getPartnerEligible() != null)
			apiAcc.setAredemesticpartnerseligible(caseBuild.getAccountInformation().getPartnerEligible().equalsIgnoreCase("Yes") ? true : false);
		if(caseBuild.getAccountInformation().getLocations() != null)
			apiAcc.setLocations(caseBuild.getAccountInformation().getLocations());
		apiAcc.setPlatform(caseBuild.getAccountInformation().getPlatform());
		if(caseBuild.getAccountInformation().getProductSold() != null)
			apiAcc.setProductsold(Arrays.asList(caseBuild.getAccountInformation().getProductSold().split(",")));
		log.info("Enrollment Options : " + caseBuild.getAccountInformation().getEnrollmentConditionOptions());
		if(caseBuild.getAccountInformation().getEnrollmentConditionOptions() != null && !caseBuild.getAccountInformation().getEnrollmentConditionOptions().isEmpty()) {
		    
			apiAcc.setEnrollmentConditionOptions(Arrays.asList(caseBuild.getAccountInformation().getEnrollmentConditionOptions().split(",")));
		} else {
		    apiAcc.setEnrollmentConditionOptions(null);
		}
//		apiAcc.setPdfDirections(caseBuild.getAccountInformation().getPdfDirections());
		apiAcc.setHoursworkedperweek(Integer.parseInt(caseBuild.getAccountInformation().getHoursWorkedPerWeek()));
		apiAcc.setEligibilitywaitingperiod(caseBuild.getAccountInformation().getEligibilityWaitingPeriod());
		apiCase.setAccountinformation(apiAcc);
				
		OpenEnrollmentDetails enrollment = new OpenEnrollmentDetails();
		enrollment.setStartdate(caseBuild.getAccountInformation().getOpenEnrollmentStartDate());
		enrollment.setEnddate(caseBuild.getAccountInformation().getOpenEnrollmentEndDate());
		
//		enrollment.setCallcenter(caseBuild.getAccountInformation().getOpenEnrollmentCallCenter().equalsIgnoreCase("Yes") ? true : false);
//		enrollment.setOnetoone(caseBuild.getAccountInformation().getOpenEnrollmentOneToOne().equalsIgnoreCase("Yes") ? true : false);
//		enrollment.setSelfservice(caseBuild.getAccountInformation().getOpenEnrollmentSelfService().equalsIgnoreCase("Yes") ? true : false);
		apiCase.setOpenenrollmentdetails(enrollment);
		
//		NewHireDetails hire = new NewHireDetails();
//		hire.setCallcenter(caseBuild.getAccountInformation().getNewHireOECallCenter().equalsIgnoreCase("Yes") ? true : false);
//		hire.setOnetoone(caseBuild.getAccountInformation().getNewHireOneToOne().equalsIgnoreCase("Yes") ? true : false);
//		hire.setSelfservice(caseBuild.getAccountInformation().getNewHireSelfService().equalsIgnoreCase("Yes") ? true : false);
//		apiCase.setNewhiredetails(hire);
		
		Manager impl = new Manager();
		impl.setName(caseBuild.getAccountInformation().getImplementationManager());
		impl.setEmail(caseBuild.getAccountInformation().getImplementationManagerEmail());
		apiCase.setImplementationManager(impl);
		
		Manager client = new Manager();
		client.setName(caseBuild.getAccountInformation().getClientManager());
		client.setEmail(caseBuild.getAccountInformation().getClientManagerEmail());
		apiCase.setClientManager(client);
		
		Manager partner = new Manager();
		partner.setName(caseBuild.getAccountInformation().getPartnerPlatformManager());
		partner.setEmail(caseBuild.getAccountInformation().getPartnerPlatformManagerEmail());
		apiCase.setPartnerManager(partner);
		
		LocationDetails loc = new LocationDetails();
		if(caseBuild.getLocations().getLocationSpecified() != null)
			loc.setAreLocationsSpecified(caseBuild.getLocations().getLocationSpecified().equalsIgnoreCase("Yes") ? true : false);
		List<LocationDetail> locs = new ArrayList<>();
		if(caseBuild.getLocations().getLocation() != null) {
			caseBuild.getLocations().getLocation().stream().forEach(l -> {
				LocationDetail ld = new LocationDetail();
				ld.setName(l.getLocationName());
				ld.setCode(l.getLocationCode());
				ld.setState(l.getLocationState());
				locs.add(ld);
			});
		}
		loc.setLocationDetail(locs);
		apiCase.setLocationDetails(loc);
		
		List<ApiProducts> apiProducts = new ArrayList<>();
		caseBuild.getProducts().getProduct().stream().forEach(prod -> {
			ApiProducts apiProd = new ApiProducts();
			apiProd.setProductname(prod.getProductName());
			apiProd.setProductDescription(prod.getProductDescription());
			apiProd.setProductId(prod.getProductId());
			apiProd.setPlanname(prod.getPlanName());
			apiProd.setPlanlevel(prod.getPlanLevel());
			apiProd.setSeries(Integer.parseInt(prod.getSeries()));
			apiProd.setCoveragelevel(prod.getCoverageLevel());
			apiProd.setApplicationnumber(prod.getApplicationNumber());
			apiProd.setBrochuretype(prod.getBrochuresType());
			apiProd.setTaxtype(prod.getTaxType());
			if(prod.getDiHoursWorkedPerWeek()!=null)
				apiProd.setDiHoursWorkedPerWeek(Integer.parseInt(prod.getDiHoursWorkedPerWeek()));
//			apiProd.setHoursworkedperweek(Integer.parseInt(prod.getHoursWorkedPerWeek()));
//			apiProd.setEligibilitywaitingperiod(prod.getEligibilityWaitingPeriod());
			apiProd.setEmployeeissueage(prod.getEmployeeIssueAge());
			apiProd.setSpouseissueage(prod.getSpouseIssueAge());
			apiProd.setChildissueage(prod.getChildIssueAge());
			apiProd.setEmployeeTerminationAge(prod.getEmployeeTerminationAge());
			apiProd.setSpouseTerminationAge(prod.getSpouseTerminationAge());
			apiProd.setChildTerminationAge(prod.getChildTerminationAge());
			
			FaceAmountOffered faceAmt = new FaceAmountOffered();
			if(prod.getBenefitAmountPercentage() != null) {
				String[] benefitAmountValues = prod.getBenefitAmountPercentage().split("%");
				String benefitAmount = benefitAmountValues[0];
				faceAmt.setBenefitAmt(Integer.valueOf(benefitAmount));
			}
				
			com.aflac.core.experience.api.model.AmountOffered emp = new com.aflac.core.experience.api.model.AmountOffered();
			if(prod.getEmployeeAmountOffered() != null) {
				emp.setIncrements(prod.getEmployeeAmountOffered().getAmountOffered().getIncrements());
				emp.setMaxamount(prod.getEmployeeAmountOffered().getAmountOffered().getMaximumAmtElect());
				emp.setMinamount(prod.getEmployeeAmountOffered().getAmountOffered().getMinimumAmtElect());
				emp.setGuaranteedAmt(prod.getEmployeeAmountOffered().getAmountOffered().getGuaranteedIssueMaximum());
				faceAmt.setEmpoyeefaceamountoffered(emp);
			}
//			faceAmt.setEmpoyeefaceamountoffered(emp);
			
			com.aflac.core.experience.api.model.AmountOffered sp = new com.aflac.core.experience.api.model.AmountOffered();
			if(prod.getSpouseAmountOffered() != null) {
				sp.setIncrements(prod.getSpouseAmountOffered().getAmountOffered().getIncrements());
				sp.setMaxamount(prod.getSpouseAmountOffered().getAmountOffered().getMaximumAmtElect());
				sp.setMinamount(prod.getSpouseAmountOffered().getAmountOffered().getMinimumAmtElect());
				sp.setGuaranteedAmt(prod.getSpouseAmountOffered().getAmountOffered().getGuaranteedIssueMaximum());
				faceAmt.setSpousefaceamountoffered(sp);
			}
			//faceAmt.setSpousefaceamountoffered(sp);
			
			com.aflac.core.experience.api.model.AmountOffered ch = new com.aflac.core.experience.api.model.AmountOffered();
			if(prod.getChildAmountOffered() != null) {
				ch.setIncrements(prod.getChildAmountOffered().getAmountOffered().getIncrements());
				ch.setMaxamount(prod.getChildAmountOffered().getAmountOffered().getMaximumAmtElect());
				ch.setMinamount(prod.getChildAmountOffered().getAmountOffered().getMinimumAmtElect());
				ch.setGuaranteedAmt(prod.getChildAmountOffered().getAmountOffered().getGuaranteedIssueMaximum());
				faceAmt.setChildfaceamountoffered(ch);
			}
			//faceAmt.setChildfaceamountoffered(ch);
			
			com.aflac.core.experience.api.model.AmountOffered tdi = new com.aflac.core.experience.api.model.AmountOffered();
			if(prod.getTdiStateAmountOffered() != null) {
				tdi.setIncrements(prod.getTdiStateAmountOffered().getAmountOffered().getIncrements());
				tdi.setMaxamount(prod.getTdiStateAmountOffered().getAmountOffered().getMaximumAmtElect());
				tdi.setMinamount(prod.getTdiStateAmountOffered().getAmountOffered().getMinimumAmtElect());
				tdi.setGuaranteedAmt(prod.getTdiStateAmountOffered().getAmountOffered().getGuaranteedIssueMaximum());
				faceAmt.setTdistateamountoffered(tdi);
			}
			//faceAmt.setTdistateamountoffered(tdi);
			
			com.aflac.core.experience.api.model.AmountOffered childInd = new com.aflac.core.experience.api.model.AmountOffered();
			if(prod.getChildIndividualAmountOffered() != null) {
				childInd.setIncrements(prod.getChildIndividualAmountOffered().getAmountOffered().getIncrements());
				childInd.setMaxamount(prod.getChildIndividualAmountOffered().getAmountOffered().getMaximumAmtElect());
				childInd.setMinamount(prod.getChildIndividualAmountOffered().getAmountOffered().getMinimumAmtElect());
				childInd.setGuaranteedAmt(prod.getChildIndividualAmountOffered().getAmountOffered().getGuaranteedIssueMaximum());
				faceAmt.setChildIndividualAmountOffered(childInd);
			}
			//faceAmt.setChildIndividualAmountOffered(childInd);
			
			com.aflac.core.experience.api.model.AmountOffered childTerm = new com.aflac.core.experience.api.model.AmountOffered();
			if(prod.getChildTermAmountOffered() != null) {
				childTerm.setIncrements(prod.getChildTermAmountOffered().getAmountOffered().getIncrements());
				childTerm.setMaxamount(prod.getChildTermAmountOffered().getAmountOffered().getMaximumAmtElect());
				childTerm.setMinamount(prod.getChildTermAmountOffered().getAmountOffered().getMinimumAmtElect());
				childTerm.setGuaranteedAmt(prod.getChildTermAmountOffered().getAmountOffered().getGuaranteedIssueMaximum());
				faceAmt.setChildTermAmountoffered(childTerm);
			}
			//faceAmt.setChildTermAmountoffered(childTerm);
			apiProd.setFaceamountoffered(faceAmt);
			
			apiProd.setProgressiverider(prod.getProgressiveRider() != null && prod.getProgressiveRider().equalsIgnoreCase("Yes") ? true : false);
			apiProd.setOptionalrider(prod.getOptionalRider() != null && prod.getOptionalRider().equalsIgnoreCase("Yes") ? true : false);
			
			com.aflac.core.experience.api.model.Tobacco tb = new com.aflac.core.experience.api.model.Tobacco();
			if(prod.getTobacco() != null) {
				tb.setStatus(prod.getTobacco().getTobaccoStatus());
				tb.setStatusdetermined(prod.getTobacco().getTobaccoStatusDetermined());
			}
			apiProd.setTobacco(tb);
			
			AgeDetails age = new AgeDetails();
			age.setAgebasedon("");
			age.setAgecalculated(prod.getAgeCalculated());
			age.setAgecalculation(prod.getAgeCalculation());
			apiProd.setAgedetails(age);
			
			apiProd.setParticipationrequirement(prod.getParticipationRequirement());
			apiProd.setAcceleratedDeathBenefitOffered(prod.getAcceleratedDeathBenefitOffered());
			apiProd.setNewhireddate(prod.getHireDate());
			apiProd.setHealthquestionrequired(prod.getHealthQueReq() != null && prod.getHealthQueReq().equalsIgnoreCase("Yes") ? true : false);
			apiProd.setBenefitperiod(prod.getBenefitPeriod());
			apiProd.setBenefittype(prod.getBenefitType());
			apiProd.setEliminationperiod(prod.getEliminationPeriod());
			apiProd.setTerm(prod.getTerm());
			apiProd.setPlatformdriven(prod.getPlatformDriven());
			apiProd.setHundredPercentageGuranteed(prod.getHundredPercentGuranteed().equalsIgnoreCase("Yes") ? true : false);
			apiProd.setOtherInstructions(prod.getOtherInstructions()!=null?prod.getOtherInstructions():null);
			
			if(prod.getIsChildCoverageOffered()!=null)
				apiProd.setIsChildCoverageOffered(prod.getIsChildCoverageOffered().equalsIgnoreCase("Y")? true : false);
			
			if(prod.getIsChildTermlifeRiderOffered()!=null)
				apiProd.setIsChildTermlifeRiderOffered(prod.getIsChildTermlifeRiderOffered().equalsIgnoreCase("Y")? true : false);
			
			if(prod.getIsUniqueIncrementsUtilized()!=null)
				apiProd.setIsUniqueIncrementsUtilized(prod.getIsUniqueIncrementsUtilized().equalsIgnoreCase("Y")? true : false);
			
			apiProd.setChildCoverageOffered(prod.getChildCoverageOffered());
			apiProd.setBenefitAmountDescription(prod.getBenefitAmountDescription());
			apiProd.setIsProductAro(prod.getIsProductAro() !=null && prod.getIsProductAro().equalsIgnoreCase("Yes") ? true : false);
			apiProd.setIssueAgeType(prod.getIssueAgeType());
			apiProd.setEligibleEmployeeGuaranteedIssue(prod.getEligibleEmployeeGuaranteedIssue());
			apiProducts.add(apiProd);
		});
		apiCase.setProducts(apiProducts);
		
		return apiCase;
	}

	private void setFileSubmission(FileSubmission fileSubmission, ApiCaseBuilderForm apiCaseBild) {
		fileSubmission.setProductionFileDueDate(apiCaseBild.getProductionFileDueDate());
		fileSubmission.setProductionFileNaming(apiCaseBild.getProductionFileNaming());
		fileSubmission.setTestFileNaming(apiCaseBild.getTestFileNaming());
		fileSubmission.setTestFileDueDate(apiCaseBild.getTestFileDueDate());
	}
	
	private void setLocations(Locations locations, ApiCaseBuilderForm apiCaseBild) {
		locations.setLocationSpecified(apiCaseBild.getLocationDetails().isAreLocationsSpecified() ? "Yes" : "No");
		if(apiCaseBild.getLocationDetails().getLocationDetail() != null) {
			for(LocationDetail loc : apiCaseBild.getLocationDetails().getLocationDetail()) {
				Location lt = new Location();
				lt.setLocationName(loc.getName());
				lt.setLocationCode(loc.getCode());
				lt.setLocationState(loc.getState());
				locations.getLocation().add(lt);
			}
		} else {
			Location lt = new Location();
			lt.setLocationName(null);
			lt.setLocationCode(null);
			lt.setLocationState(null);
			locations.getLocation().add(lt);
		}
	}

	@Override
	public EnrollmentDetails getEnrollmentDetails() {
		EnrollmentDetails enrollmentDetails = new EnrollmentDetails();
		HttpGet http = new HttpGet(apiUrlService.getCaseBuildPlaformsUrl());
		try{
			String data = apiUrlService.executeHttpGet(httpclient, http);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			ApiEnrollmentDetails apiEnrollment = objectMapper.readValue(data, ApiEnrollmentDetails.class);
			enrollmentDetails.setEnrollmentPlatform(apiEnrollment.getEnrollmentPlatform().stream().map(e -> e.getPlatform()).collect(Collectors.toList()));
			enrollmentDetails.setEnrollmentDetails(apiEnrollment.getEnrollmentPlatform());
		} catch (IOException e1) {
			log.error("Api error: ", e1);
		}
		return enrollmentDetails;
	}
	
	private void setAccounInfo(AccountInformation formAccInfo, ApiCaseBuilderForm apiCase, String groupNo) {
		formAccInfo.setAccountName(apiCase.getAccountinformation().getAccountName());
		formAccInfo.setGroupNumber(apiCase.getAccountinformation().getGroupNumber());
		formAccInfo.setGroupType(apiCase.getAccountinformation().getGroupType());
		formAccInfo.setAflacEase(apiCase.getAccountinformation().isAflacease() ? "Yes" : "No");
		formAccInfo.setClientManager(apiCase.getClientManager().getName());
		formAccInfo.setClientManagerEmail(apiCase.getClientManager().getEmail());
		formAccInfo.setImplementationManager(apiCase.getImplementationManager().getName());
		formAccInfo.setImplementationManagerEmail(apiCase.getImplementationManager().getEmail());
		formAccInfo.setPartnerPlatformManager(apiCase.getPartnerManager().getName());
		formAccInfo.setPartnerPlatformManagerEmail(apiCase.getPartnerManager().getEmail());
		formAccInfo.setCoverageBillingEffDate(apiCase.getCoveragebillingeffectivedate());
		formAccInfo.setDeductionFrequency(apiCase.getAccountinformation().getDeductionfrequency().get(0));
		formAccInfo.setEligibleEmployees(apiCase.getAccountinformation().getNoofeligibleemployee());
		formAccInfo.setGroupNumber(groupNo);
		formAccInfo.setLocations(apiCase.getAccountinformation().getLocations());
		formAccInfo.setNewHire(apiCase.getNewHire());
		if(apiCase.getNewhiredetails() != null) {
    		formAccInfo.setNewHireOECallCenter(apiCase.getNewhiredetails().isCallcenter() ? "Yes" : "No");
    		formAccInfo.setNewHireOneToOne(apiCase.getNewhiredetails().isOnetoone() ? "Yes" : "No");
    		formAccInfo.setNewHireSelfService(apiCase.getNewhiredetails().isSelfservice() ? "Yes" : "No");
		}
		formAccInfo.setOpenEnrollmentCallCenter(apiCase.getOpenenrollmentdetails().isCallcenter() ? "Yes" : "No");
		formAccInfo.setOpenEnrollmentEndDate(apiCase.getOpenenrollmentdetails().getEnddate());
		formAccInfo.setOpenEnrollmentOneToOne(apiCase.getOpenenrollmentdetails().isOnetoone() ? "Yes" : "No");
		formAccInfo.setOpenEnrollmentSelfService(apiCase.getOpenenrollmentdetails().isSelfservice() ? "Yes" : "No");
		formAccInfo.setOpenEnrollmentStartDate(apiCase.getOpenenrollmentdetails().getStartdate());
		formAccInfo.setPartnerEligible(apiCase.getAccountinformation().isAredemesticpartnerseligible() ? "Yes" : "No");
		formAccInfo.setPlatform(apiCase.getAccountinformation().getPlatform());
		formAccInfo.setProductSold(apiCase.getAccountinformation().getProductsold().stream().collect(Collectors.joining(",")));
		formAccInfo.setSiteTestInfoDueDate(apiCase.getSitetestinfoduedate());
		formAccInfo.setSitusState(apiCase.getAccountinformation().getSitusstate());
		formAccInfo.setSsn(apiCase.getAccountinformation().getSsnoreeid());
		formAccInfo.setEnrollmentConditionOptions(apiCase.getAccountinformation().getEnrollmentConditionOptions().stream().collect(Collectors.joining(",")));
		formAccInfo.setPdfDirections(apiCase.getAccountinformation().getPdfDirections());
		formAccInfo.setHoursWorkedPerWeek(String.valueOf(apiCase.getAccountinformation().getHoursworkedperweek()));
		formAccInfo.setEligibilityWaitingPeriod(apiCase.getAccountinformation().getEligibilitywaitingperiod());
	}
	
	private void setProducts(Products formProduct, List<ApiProducts> apiProducts) {
		List<Product> formProducts = formProduct.getProduct();
		
		for(ApiProducts apiProd : apiProducts) {
			Product prod = new Product();
			prod.setProductName(apiProd.getProductname());
			prod.setProductId(apiProd.getProductId());
			prod.setProductDescription(apiProd.getProductDescription());
			
			if(apiProd.getAgedetails() != null) {
				prod.setAgeCalculated(apiProd.getAgedetails().getAgecalculated());
				prod.setAgeCalculation(apiProd.getAgedetails().getAgecalculation());
			}
			else {
			    prod.setAgeCalculated(null);
                prod.setAgeCalculation(null);
			}
			prod.setIssueAgeType(apiProd.getIssueAgeType());
			prod.setApplicationNumber(apiProd.getApplicationnumber());
			prod.setBrochuresType(apiProd.getBrochuretype());
			prod.setChildIssueAge(apiProd.getChildissueage());
			prod.setCoverageLevel(apiProd.getCoveragelevel());
//			prod.setEligibilityWaitingPeriod(apiProd.getEligibilitywaitingperiod());
			
			if(apiProd.getFaceamountoffered() != null) {
			    //prod.setBenefitAmountPercentage(String.valueOf(apiProd.getFaceamountoffered().getBenefitAmt()));
			    prod.setBenefitAmountPercentage(apiProd.getFaceamountoffered().getBenefitAmt()==null ? null: String.valueOf(apiProd.getFaceamountoffered().getBenefitAmt()));
                AmountOffered amt = new AmountOffered();
                if(apiProd.getFaceamountoffered().getEmpoyeefaceamountoffered() != null && 
                	apiProd.getFaceamountoffered().getEmpoyeefaceamountoffered().getIncrements() != null) {
	                EmployeeAmountOffered empAmt = new EmployeeAmountOffered();
	                amt.setIncrements(apiProd.getFaceamountoffered().getEmpoyeefaceamountoffered().getIncrements());
	                amt.setMaximumAmtElect(apiProd.getFaceamountoffered().getEmpoyeefaceamountoffered().getMaxamount());
	                amt.setMinimumAmtElect(apiProd.getFaceamountoffered().getEmpoyeefaceamountoffered().getMinamount());
	                amt.setGuaranteedIssueMaximum(apiProd.getFaceamountoffered().getEmpoyeefaceamountoffered().getGuaranteedAmt());
	                empAmt.setAmountOffered(amt);
	                prod.setEmployeeAmountOffered(empAmt);
                }
                if(apiProd.getFaceamountoffered().getSpousefaceamountoffered() != null && 
                	apiProd.getFaceamountoffered().getSpousefaceamountoffered().getIncrements() != null) {
                    SpouseAmountOffered spAmt = new SpouseAmountOffered();
                    amt = new AmountOffered();
                    amt.setIncrements(apiProd.getFaceamountoffered().getSpousefaceamountoffered().getIncrements());
                    amt.setMaximumAmtElect(apiProd.getFaceamountoffered().getSpousefaceamountoffered().getMaxamount());
                    amt.setMinimumAmtElect(apiProd.getFaceamountoffered().getSpousefaceamountoffered().getMinamount());
                    amt.setGuaranteedIssueMaximum(apiProd.getFaceamountoffered().getSpousefaceamountoffered().getGuaranteedAmt());
                    spAmt.setAmountOffered(amt);
                    prod.setSpouseAmountOffered(spAmt);
                }
                if(apiProd.getFaceamountoffered().getTdistateamountoffered() != null && 
                	apiProd.getFaceamountoffered().getTdistateamountoffered().getIncrements() != null) {
                    TdiStateAmountOffered td = new TdiStateAmountOffered();
                    amt = new AmountOffered();
                    amt.setIncrements(apiProd.getFaceamountoffered().getTdistateamountoffered().getIncrements());
                    amt.setMaximumAmtElect(apiProd.getFaceamountoffered().getTdistateamountoffered().getMaxamount());
                    amt.setMinimumAmtElect(apiProd.getFaceamountoffered().getTdistateamountoffered().getMinamount());
                    amt.setMinimumAmtElect(apiProd.getFaceamountoffered().getTdistateamountoffered().getMinamount());
                    amt.setGuaranteedIssueMaximum(apiProd.getFaceamountoffered().getTdistateamountoffered().getGuaranteedAmt());
                    td.setAmountOffered(amt);
                    prod.setTdiStateAmountOffered(td);
                }
                if(apiProd.getFaceamountoffered().getChildfaceamountoffered() != null && 
                	apiProd.getFaceamountoffered().getChildfaceamountoffered().getIncrements() != null) {
                	ChildAmountOffered td = new ChildAmountOffered();
                    amt = new AmountOffered();
                    amt.setIncrements(apiProd.getFaceamountoffered().getChildfaceamountoffered().getIncrements());
                    amt.setMaximumAmtElect(apiProd.getFaceamountoffered().getChildfaceamountoffered().getMaxamount());
                    amt.setMinimumAmtElect(apiProd.getFaceamountoffered().getChildfaceamountoffered().getMinamount());
                    amt.setMinimumAmtElect(apiProd.getFaceamountoffered().getChildfaceamountoffered().getMinamount());
                    amt.setGuaranteedIssueMaximum(apiProd.getFaceamountoffered().getChildfaceamountoffered().getGuaranteedAmt());
                    td.setAmountOffered(amt);
                    prod.setChildAmountOffered(td);
                }
                if(apiProd.getFaceamountoffered().getChildIndividualAmountOffered() != null &&
                		apiProd.getFaceamountoffered().getChildIndividualAmountOffered().getIncrements() != null) {
                	ChildIndividualAmountOffered td = new ChildIndividualAmountOffered();
                    amt = new AmountOffered();
                    amt.setIncrements(apiProd.getFaceamountoffered().getChildIndividualAmountOffered().getIncrements());
                    amt.setMaximumAmtElect(apiProd.getFaceamountoffered().getChildIndividualAmountOffered().getMaxamount());
                    amt.setMinimumAmtElect(apiProd.getFaceamountoffered().getChildIndividualAmountOffered().getMinamount());
                    amt.setMinimumAmtElect(apiProd.getFaceamountoffered().getChildIndividualAmountOffered().getMinamount());
                    amt.setGuaranteedIssueMaximum(apiProd.getFaceamountoffered().getChildIndividualAmountOffered().getGuaranteedAmt());
                    td.setAmountOffered(amt);
                    prod.setChildIndividualAmountOffered(td);
                }
                if(apiProd.getFaceamountoffered().getChildTermAmountoffered() != null && 
                	apiProd.getFaceamountoffered().getChildTermAmountoffered().getIncrements() != null) {
                	ChildTermAmountOffered td = new ChildTermAmountOffered();
                    amt = new AmountOffered();
                    amt.setIncrements(apiProd.getFaceamountoffered().getChildTermAmountoffered().getIncrements());
                    amt.setMaximumAmtElect(apiProd.getFaceamountoffered().getChildTermAmountoffered().getMaxamount());
                    amt.setMinimumAmtElect(apiProd.getFaceamountoffered().getChildTermAmountoffered().getMinamount());
                    amt.setMinimumAmtElect(apiProd.getFaceamountoffered().getChildTermAmountoffered().getMinamount());
                    amt.setGuaranteedIssueMaximum(apiProd.getFaceamountoffered().getChildTermAmountoffered().getGuaranteedAmt());
                    td.setAmountOffered(amt);
                    prod.setChildTermAmountOffered(td);
                }
            }
			prod.setEmployeeTerminationAge(apiProd.getEmployeeTerminationAge());
			prod.setSpouseTerminationAge(apiProd.getSpouseTerminationAge());
			prod.setChildTerminationAge(apiProd.getChildTerminationAge());
			prod.setEmployeeIssueAge(apiProd.getEmployeeissueage());
			prod.setHealthQueReq(apiProd.isHealthquestionrequired() ? "Yes" : "No");
			prod.setHireDate(apiProd.getNewhireddate());
			prod.setDiHoursWorkedPerWeek(String.valueOf(apiProd.getDiHoursWorkedPerWeek()).equalsIgnoreCase("null")?null: String.valueOf(apiProd.getDiHoursWorkedPerWeek()));
//			prod.setHoursWorkedPerWeek(apiProd.getHoursworkedperweek() + "");
			//prod.setInitialEnrollment(apiProd.getInitialenrollment());
			if(apiProd.isOptionalrider() != null)
                prod.setOptionalRider((boolean)apiProd.isOptionalrider() ? "Yes" : "No");
            if(apiProd.isProgressiverider() != null)
                prod.setProgressiveRider((boolean)apiProd.isProgressiverider() ? "Yes" : "No");
			prod.setParticipationRequirement(apiProd.isParticipationrequirement());
			prod.setAcceleratedDeathBenefitOffered(apiProd.getAcceleratedDeathBenefitOffered());
			prod.setPlanLevel(apiProd.getPlanlevel());
			prod.setPlanName(apiProd.getPlanname());
			prod.setPlatformDriven(apiProd.getPlatformdriven());
			prod.setSeries(apiProd.getSeries() + "");
			prod.setSpouseIssueAge(apiProd.getSpouseissueage());			
			prod.setTaxType(apiProd.getTaxtype());			    
			prod.setBenefitType(apiProd.getBenefittype());
			prod.setBenefitPeriod(apiProd.getBenefitperiod());
			prod.setEliminationPeriod(apiProd.getEliminationperiod());
			prod.setHundredPercentGuranteed((boolean)apiProd.getHundredPercentageGuranteed()? "Yes" : "No");
			
			if(apiProd.getTerm() != null)
			    prod.setTerm(apiProd.getTerm());
			else
			    prod.setTerm(null);
			if(apiProd.getTobacco() != null && apiProd.getTobacco().getStatus() != null) {
				Tobacco tb = new Tobacco();
				tb.setTobaccoStatus(apiProd.getTobacco().getStatus());
				tb.setTobaccoStatusDetermined(apiProd.getTobacco().getStatusdetermined());
				prod.setTobacco(tb);
			}
			prod.setOtherInstructions(apiProd.getOtherInstructions()!=null?apiProd.getOtherInstructions():null);
			prod.setIsChildCoverageOffered((boolean)apiProd.getIsChildCoverageOffered()? "Y" : null);
			prod.setIsChildTermlifeRiderOffered((boolean)apiProd.getIsChildTermlifeRiderOffered()? "Y" : null);
			prod.setIsUniqueIncrementsUtilized((boolean)apiProd.getIsUniqueIncrementsUtilized()? "Y" : null);
			prod.setChildCoverageOffered(apiProd.getChildCoverageOffered()!=null? apiProd.getChildCoverageOffered() : null);
			prod.setBenefitAmountDescription(apiProd.getBenefitAmountDescription()!=null? apiProd.getBenefitAmountDescription() : null);
			prod.setIsProductAro((boolean)apiProd.getIsProductAro()? "Yes" : "No");
			prod.setEligibleEmployeeGuaranteedIssue(apiProd.getEligibleEmployeeGuaranteedIssue());
			formProducts.add(prod);
		}
	}

	@Override
	public ComplianceVerbiageDetail getComplianceVerbiage(String groupNo, String effDate, String platform, String pdfCase) {
		HttpGet http = new HttpGet(apiUrlService.getCaseBuildComplianceVerbiage(groupNo, effDate, platform,pdfCase));
		ComplianceVerbiageDetail complianceVerbiageDetail = null;
		try {
			String data = apiUrlService.executeHttpGet(httpclient, http);
			complianceVerbiageDetail = objectMapper.readValue(data,ComplianceVerbiageDetail.class);
		} catch (IOException e) {
			log.error("Api error: ", e);
		}
		return complianceVerbiageDetail;
	}

	@Override
	public Products getAvailableProducts(SlingHttpServletRequest request) {
		List<Product> productsList = null;
		Products products = null;
		Product product = null;
		String availableProducts = request.getParameter("availableProducts");
		String platform = request.getParameter("enrollmentPlatform");
		String ageMethod = "";
		Integer numberOfDisabilityMultiPlans =null;
		Integer numberOfTermLifeMultiPlans =null;
		try {
			if(availableProducts!=null && !availableProducts.isEmpty()) {
				List<String> availableproductsList = Stream.of(availableProducts.split(",")).collect(Collectors.toList());
				productsList = new ArrayList<Product>();
				products = new Products();
				for(int i=0;i<availableproductsList.size();i++) {
					numberOfDisabilityMultiPlans = Integer.valueOf(request.getParameter("numberOfDisabilityMultiPlans"));
					numberOfTermLifeMultiPlans = Integer.valueOf(request.getParameter("numberOfTermLifeMultiPlans"));
					if(availableproductsList.get(i).equalsIgnoreCase("Disability-MultiPlan")&&numberOfDisabilityMultiPlans!=null) {
						for(int j=0;j<numberOfDisabilityMultiPlans;j++) {
							product = new Product();
							product.setProductName("Disability-"+(j+1));
							JsonReader jsonReader = new JsonReader(new StringReader(getAgeBasedOn("Disability", platform, "")));
			    			JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
			    			ageMethod= jsonObject.get("age-method").getAsString();
							product.setPlatformDriven(ageMethod);
							productsList.add(product);
						}
					} 
					else if(availableproductsList.get(i).equalsIgnoreCase("Term Life-MultiPlan")&&numberOfTermLifeMultiPlans!=null) {
						for(int j=0;j<numberOfTermLifeMultiPlans;j++) {
							product = new Product();
							product.setProductName("Term Life-"+(j+1));
							JsonReader jsonReader = new JsonReader(new StringReader(getAgeBasedOn("Term Life", platform, "")));
			    			JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
			    			ageMethod= jsonObject.get("age-method").getAsString();
							product.setPlatformDriven(ageMethod);
							productsList.add(product);
						}
					} 
					else 
					{
						product = new Product();
						product.setProductName(availableproductsList.get(i));
						if(availableproductsList.get(i).contains("Disability") || availableproductsList.get(i).contains("Whole Life") || availableproductsList.get(i).contains("Term Life")) {
							JsonReader jsonReader = new JsonReader(new StringReader(getAgeBasedOn(availableproductsList.get(i), platform, "")));
			    			JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
			    			ageMethod= jsonObject.get("age-method").getAsString();
			    			product.setPlatformDriven(ageMethod);
						}
						productsList.add(product);
					}
				}
			}else {
				log.info("available Products is not selected");
			}
			products.setProduct(productsList);
		} catch (JsonSyntaxException e) {
			log.error("Error ", e);
		} catch (JsonIOException e) {
			log.error("Error ", e);
		}
		return products;
	}

	public String convertDateCba(String existingDateValue) {
		String convertedDate = null;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date da = (Date) formatter.parse(existingDateValue);
			convertedDate = formatter.format(da);
		} catch (ParseException e) {
			log.info("error in convertDateCba method:" + e);
		}

		return convertedDate;

	}

	@Override
	public String getPdfDirections(String situs) {
		HttpGet http = new HttpGet(apiUrlService.getPdfDirectionsUrl(situs));
		String apiResponse = null;
		try {
			apiResponse = apiUrlService.executeHttpGet(httpclient, http);
		} catch (IOException e) {
			log.error("PdfDirections Get Api Error: ", e);
		}		
		return apiResponse;
	}

	@Override
	public CaseBuilderForm updateProducts(String caseBuildData, String addedProducts, String deletedProducts) throws JsonMappingException, JsonProcessingException {
		CaseBuilderForm caseBuild = null;
		List<Product> productList = null;
		List<String> addedproductsList = null;
		List<String> deletedproductsList = null;
		List<String> caseBuildDataProductList = null;
		Product newProduct = null;
		Product deletedProduct = null;
		String ageMethod = "";
		String platform = null;
		try {
			if(caseBuildData!=null && !caseBuildData.isEmpty()) {
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				caseBuild = objectMapper.readValue(caseBuildData, CaseBuilderForm.class);
				productList = caseBuild.getProducts().getProduct();
				caseBuildDataProductList = productList.stream().map(product -> product.getProductName()).collect(Collectors.toList());
				if(addedProducts!=null && !addedProducts.isEmpty()) {
					addedproductsList = Stream.of(addedProducts.split(",")).collect(Collectors.toList());
					for(int i=0;i<addedproductsList.size();i++) {
						if(!caseBuildDataProductList.contains(addedproductsList.get(i))) {
							newProduct = new Product();
							newProduct.setProductName(addedproductsList.get(i));
							if(addedproductsList.get(i).contains("Disability") || addedproductsList.get(i).contains("Whole Life") || addedproductsList.get(i).contains("Term Life")) {
								platform = caseBuild.getAccountInformation().getPlatform();
								JsonReader jsonReader = new JsonReader(new StringReader(getAgeBasedOn(addedproductsList.get(i), platform, "")));
				    			JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
				    			ageMethod= jsonObject.get("age-method").getAsString();
				    			newProduct.setPlatformDriven(ageMethod);
							}
							productList.add(newProduct);
							caseBuild.getProducts().setProduct(productList);
						}
					}
				}
				if(deletedProducts!=null && !deletedProducts.isEmpty()){
					deletedproductsList = Stream.of(deletedProducts.split(",")).collect(Collectors.toList());
					for(int i=0;i<productList.size();i++) {
						if(deletedproductsList.contains(productList.get(i).getProductName())) {
                            deletedProduct = productList.get(i);
                            productList.remove(deletedProduct);
                        }
					}
				}
			}
		} 
		catch (JsonIOException e) {
			log.error("Exception in updating products: ", e);
		} catch (JsonSyntaxException e) {
			log.error("Exception in updating products: ", e);
		}
		return caseBuild;
	}

	@Override
	public boolean checkExistingCase(String groupNo, String effDate, String platform) {
		HttpGet http = new HttpGet(apiUrlService.getCaseBuildAccountsUrl(groupNo, effDate, platform));
		log.info("Requested Group Number:"+groupNo);
		log.info("Requested Effective Date:"+effDate);
		log.info("Requested Platform:"+platform);
		try{
			String data = apiUrlService.executeHttpGet(httpclient, http);
		    if(!data.equalsIgnoreCase("null") && data!=null) {
		    	return true;
		    }
		} catch (IOException e1) {
			log.error("checkExistingCase error: ", e1);
		}
		return false;
	}
	
}
