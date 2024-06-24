package com.aflac.core.osgi.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.aflac.core.config.ExperienceApiConfig;
import com.aflac.core.experience.api.model.ApiActiveAtWorkData;
import com.aflac.core.experience.api.model.ApiActivelyAtWork;
import com.aflac.core.experience.api.model.ApiMasterAppAccountEligiblity;
import com.aflac.core.experience.api.model.ApiMasterAppData;
import com.aflac.core.experience.api.model.ApiMasterAppProduct;
import com.aflac.core.experience.api.model.ApiWaitingPeriodEligibility;
import com.aflac.core.experience.api.model.ApiWaitingPeriodEligibilityData;
import com.aflac.core.experience.api.model.MasterAppReviewTableApi;
import com.aflac.core.experience.api.model.MasterAppReviewTableDataApi;
import com.aflac.core.experience.api.model.AuditUserActivity;
import com.aflac.core.experience.api.model.MasterAppFormIdsModel;
import com.aflac.core.experience.api.model.MasterAppSeriesData;
import com.aflac.core.experience.api.model.MasterAppSeriesResponseModel;
import com.aflac.core.experience.api.model.SaveCasebuildResponse;
import com.aflac.core.osgi.service.AuditService;
import com.aflac.core.osgi.service.FileNetService;
import com.aflac.core.osgi.service.GeneratePDFService;
import com.aflac.core.osgi.service.MasterAppService;
import com.aflac.core.util.DocumentUtil;
import com.aflac.core.util.MasterAppFormIds;
import com.aflac.core.util.MasterAppProductSequence;
import com.aflac.core.util.PdfGenerationUtil;
import com.aflac.xml.masterApp.models.AccountAndEligiblity;
import com.aflac.xml.masterApp.models.ActiveAtWorkData;
import com.aflac.xml.masterApp.models.ActivelyAtWork;
import com.aflac.xml.masterApp.models.MasterApp;
import com.aflac.xml.masterApp.models.MasterAppReviewTable;
import com.aflac.xml.masterApp.models.MasterAppReviewTableData;
import com.aflac.xml.masterApp.models.Product;
import com.aflac.xml.masterApp.models.WaitingPeriodEligibility;
import com.aflac.xml.masterApp.models.WaitingPeriodEligibilityData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

@Component(
	service = { MasterAppService.class }
)
public class MasterAppServiceImpl implements MasterAppService {
	private transient Logger log = LoggerFactory.getLogger(MasterAppServiceImpl.class);
	private transient CloseableHttpClient httpclient;
	private static String objStore = "ACC";
	
	@Reference
	private transient ExperienceApiConfig apiUrlService;
	@Reference
	private transient GeneratePDFService pdfService;
	@Reference
	private transient AuditService auditService;
	
	private transient ObjectMapper objectMapper;
	
	@Reference
	private FileNetService fileNet;
	
	public MasterAppServiceImpl() {
		this.httpclient = HttpClients.createDefault();
		this.objectMapper = new ObjectMapper();
	}
	
	
	public MasterAppServiceImpl(CloseableHttpClient httpclient, ExperienceApiConfig apiUrlService, GeneratePDFService pdfService) {
		this.httpclient = httpclient;
		this.apiUrlService = apiUrlService;
		this.pdfService = pdfService;
		this.objectMapper = new ObjectMapper();
	}


	@Override
	public MasterApp getMasterAppData(String groupNumber, String effectiveDate) {
		HttpGet http = new HttpGet(apiUrlService.getMasterAppDataUrl(groupNumber, effectiveDate));
		String data = null;
		ApiMasterAppData apiMasterAppData = null;
		MasterApp masterApp = null;
		try {
			data = apiUrlService.executeHttpGet(httpclient, http);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if(data!=null && !data.isEmpty()) {
				apiMasterAppData = objectMapper.readValue(data,ApiMasterAppData.class);
				if(apiMasterAppData!=null) {
					masterApp = new MasterApp();
					masterApp.setMasterAppEntityId(apiMasterAppData.getMasterAppEntityId());
					masterApp.setVersion(String.valueOf(apiMasterAppData.getVersion()));
					masterApp.setSitusState(apiMasterAppData.getSitusState());
					masterApp.setGroupType(apiMasterAppData.getGroupType());
					masterApp.setCensusEnrollment(apiMasterAppData.getCensusEnrollment());
					masterApp.setEligibleSeries(apiMasterAppData.getEligibleSeries());
					masterApp.setFormIds(apiMasterAppData.getFormIds());
					masterApp.setAccountAndEligiblity(new AccountAndEligiblity());
					masterApp.setSignatureStatus(apiMasterAppData.getSignatureStatus());
					masterApp.setFilenetDocumentId(apiMasterAppData.getFilenetDocumentId()!=null?apiMasterAppData.getFilenetDocumentId():null);
					masterApp.setDataStatus(apiMasterAppData.getDataStatus());
					masterApp.setFirstSignerEmailId(apiMasterAppData.getFirstSignerEmailId());
					masterApp.setSecondSignerEmailId(apiMasterAppData.getSecondSignerEmailId());
					setAccountAndEligiblityData(masterApp.getAccountAndEligiblity(),apiMasterAppData.getMasterAppAccountEligiblity());
					setProductsData(masterApp,apiMasterAppData.getMasterAppProducts());
					setMasterAppReviewTableData(masterApp,apiMasterAppData.getMasterAppReviewTable());
				}
				
			}
		}catch (Exception e) {
			log.error("getMasterAppData error: ", e);
		}
		return masterApp;
	}

	private void setMasterAppReviewTableData(MasterApp masterApp, MasterAppReviewTableApi masterAppReviewTableApi) {
		try {
			if(masterAppReviewTableApi.getMasterAppReviewTableData()!=null) {
				if(masterAppReviewTableApi.getMasterAppReviewTableData().get(0).getFormId()!=null) {
					MasterAppReviewTable masterAppReviewTable = new MasterAppReviewTable();
					for(MasterAppReviewTableDataApi masterAppReviewTableDataApi: masterAppReviewTableApi.getMasterAppReviewTableData()) {
						MasterAppReviewTableData masterAppReviewTableData = new MasterAppReviewTableData();
						masterAppReviewTableData.setFormId(masterAppReviewTableDataApi.getFormId());
						masterAppReviewTableData.setApiFormId(masterAppReviewTableDataApi.getApiFormId());
						masterAppReviewTableData.setSignStatus(masterAppReviewTableDataApi.getSignStatus());
						masterAppReviewTableData.setSignatureCheckbox(masterAppReviewTableDataApi.getSignatureCheckbox());
						masterAppReviewTableData.setProduct(masterAppReviewTableDataApi.getProducts());
						masterAppReviewTableData.setFilenetDocumentId(masterAppReviewTableDataApi.getFilenetDocumentId());
						masterAppReviewTableData.setMasterappSentDatetime(masterAppReviewTableDataApi.getMasterappSentDatetime());
						masterAppReviewTableData.setMasterappReceivedDatetime(masterAppReviewTableDataApi.getMasterappReceivedDatetime());
						masterAppReviewTableData.setAgreementId(masterAppReviewTableDataApi.getAgreementId());
						masterAppReviewTable.getMasterAppReviewTableData().add(masterAppReviewTableData);
						masterApp.setMasterAppReviewTable(masterAppReviewTable);
					}
					
				}
			}else {
				masterApp.setMasterAppReviewTable(null);
			}
		}catch (Exception e) {
			log.error("setMasterAppReviewTableData error: ", e);
		}
		
	}


	private void setProductsData(MasterApp masterApp, List<ApiMasterAppProduct> apiMasterAppProducts) {
		
		List<Product> productList = new ArrayList<>();
		Product masterAppProduct = null;
		for(ApiMasterAppProduct apiMasterAppProduct : apiMasterAppProducts) {
			masterAppProduct = new Product();
			masterAppProduct.setProductName(apiMasterAppProduct.getProductName());
			masterAppProduct.setProductSequence(apiMasterAppProduct.getProductSequence());
			masterAppProduct.setSeries(apiMasterAppProduct.getSeries());
			masterAppProduct.setPlanLevelPremium(apiMasterAppProduct.getPlanLevelPremium());
			masterAppProduct.setBenefitType(apiMasterAppProduct.getBenefitType());
			masterAppProduct.setApplicationReason(apiMasterAppProduct.getApplicationReason());
			masterAppProduct.setExistingPolicyNumber(apiMasterAppProduct.getExistingPolicyNumber());
			masterAppProduct.setOtherApplicationReason(apiMasterAppProduct.getOtherApplicationReason());
			masterAppProduct.setEligibleEmployee(apiMasterAppProduct.getEligibleEmployee());
			masterAppProduct.setEligibleEmployeeClass(apiMasterAppProduct.getEligibleEmployeeClass());
			masterAppProduct.setEligibleSpouse(apiMasterAppProduct.getEligibleSpouse());
			masterAppProduct.setEligibleEmployeeExcept(apiMasterAppProduct.getEligibleEmployeeExcept());
			masterAppProduct.setEligibleEmployeeOther(apiMasterAppProduct.getEligibleEmployeeOther());
			masterAppProduct.setProductClass(apiMasterAppProduct.getProductClass());
			masterAppProduct.setPlan(apiMasterAppProduct.getPlan());
			masterAppProduct.setPlanFeatures(apiMasterAppProduct.getPlanFeatures());
			masterAppProduct.setPlanFeaturesOther(apiMasterAppProduct.getPlanFeaturesOther());
			masterAppProduct.setOptionalFeatures(apiMasterAppProduct.getOptionalFeatures());
			masterAppProduct.setOptionalFeaturesOther(apiMasterAppProduct.getOptionalFeaturesOther());
			masterAppProduct.setOptionalFeaturesText(apiMasterAppProduct.getOptionalFeaturesText());
			masterAppProduct.setEffectiveDate(apiMasterAppProduct.getEffectiveDate());
			masterAppProduct.setRates(apiMasterAppProduct.getRates());
			masterAppProduct.setProductContribution(apiMasterAppProduct.getProductContribution());
			masterAppProduct.setEmployeePremium(apiMasterAppProduct.getEmployeePremium());
			masterAppProduct.setEmployerPremium(apiMasterAppProduct.getEmployerPremium());
			masterAppProduct.setIsReplacingGroupPolicy(apiMasterAppProduct.isReplacingGroupPolicy()?"Yes":"No");
			masterAppProduct.setPolicyOrCarrierNumber(apiMasterAppProduct.getPolicyOrCarrierNumber());
			masterAppProduct.setIncomeReplacement(apiMasterAppProduct.getIncomeReplacement());
			masterAppProduct.setBenefitPeriod(apiMasterAppProduct.getBenefitPeriod());
			masterAppProduct.setEliminationPeriod(apiMasterAppProduct.getEliminationPeriod());
			masterAppProduct.setGroupPolicyState(apiMasterAppProduct.getGroupPolicyState());
			masterAppProduct.setOptionalTermlifeCoverages(apiMasterAppProduct.getOptionalTermlifeCoverages());
			masterAppProduct.setCiac(apiMasterAppProduct.getCiac());
			masterAppProduct.setProposedEffectiveDate(apiMasterAppProduct.getProposedEffectiveDate());
			masterAppProduct.setOnDate(apiMasterAppProduct.getOnDate());
			masterAppProduct.setMinimumNoOfEmployees(apiMasterAppProduct.getMinimumNoOfEmployees());
			masterAppProduct.setPercentileOfEligibleEmployees(apiMasterAppProduct.getPercentileOfEligibleEmployees());
			masterAppProduct.setPremiumPaid(apiMasterAppProduct.getPremiumPaid());
			masterAppProduct.setPremiumPaymentMode(apiMasterAppProduct.getPremiumPaymentMode());
			masterAppProduct.setEmployeeCostOfInsurance(apiMasterAppProduct.getEmployeeCostOfInsurance());
			masterAppProduct.setPlanYearPerInsured(apiMasterAppProduct.getPlanYearPerInsured());
			masterAppProduct.setHiEnrollEmployeeCount(apiMasterAppProduct.getHiEnrollEmployeeCount());
			masterAppProduct.setPremiumDue(apiMasterAppProduct.getPremiumDue());
			
			if(apiMasterAppProduct.getActivelyAtWork()!=null) {
				if(apiMasterAppProduct.getActivelyAtWork().getActiveAtWorkData().get(0).getClassName()!=null && apiMasterAppProduct.getActivelyAtWork().getActiveAtWorkData().get(0).getClassName()!=null) {
					ActivelyAtWork activelyAtWork = new ActivelyAtWork();
					for(ApiActiveAtWorkData apiActiveAtWorkData : apiMasterAppProduct.getActivelyAtWork().getActiveAtWorkData()) {
						ActiveAtWorkData activeAtWorkData = new ActiveAtWorkData();
						activeAtWorkData.setActivelyAtWorkHours(apiActiveAtWorkData.getActivelyAtWorkHours()!=null?apiActiveAtWorkData.getActivelyAtWorkHours():null);
						activeAtWorkData.setClassName(apiActiveAtWorkData.getClassName());
						activelyAtWork.getActiveAtWorkData().add(activeAtWorkData);
						masterAppProduct.setActivelyAtWork(activelyAtWork);
					}
			}
			}else {
				masterAppProduct.setActivelyAtWork(null);
			}
			
			if(apiMasterAppProduct.getWaitingPeriodEligibility()!=null) {
				if(apiMasterAppProduct.getWaitingPeriodEligibility().getWaitingPeriodEligibilityData().get(0).getClassName()!=null && !apiMasterAppProduct.getWaitingPeriodEligibility().getWaitingPeriodEligibilityData().get(0).getClassName().isEmpty()) {
					WaitingPeriodEligibility waitingPeriodEligibility = new WaitingPeriodEligibility();
					for(ApiWaitingPeriodEligibilityData apiWaitingPeriodEligibilityData : apiMasterAppProduct.getWaitingPeriodEligibility().getWaitingPeriodEligibilityData()) {
						WaitingPeriodEligibilityData waitingPeriodEligibilityData = new WaitingPeriodEligibilityData();
						waitingPeriodEligibilityData.setClassName(apiWaitingPeriodEligibilityData.getClassName());
						waitingPeriodEligibilityData.setNumberOfDays(apiWaitingPeriodEligibilityData.getNumberOfDays());
						waitingPeriodEligibilityData.setWaitingPeriod(apiWaitingPeriodEligibilityData.getWaitingPeriod());
						waitingPeriodEligibility.getWaitingPeriodEligibilityData().add(waitingPeriodEligibilityData);
						masterAppProduct.setWaitingPeriodEligibility(waitingPeriodEligibility);
						
					}
			}
			}else {
				masterAppProduct.setWaitingPeriodEligibility(null);
			}
			productList.add(masterAppProduct);
		}
		masterApp.setProduct(productList);
		
	}


	private void setAccountAndEligiblityData(AccountAndEligiblity masterAppAccountAndEligiblity,ApiMasterAppAccountEligiblity apiMasterAppAccountEligiblity) {
		masterAppAccountAndEligiblity.setGroupNumberOrGpId(apiMasterAppAccountEligiblity.getGroupNumberOrGpId());
		masterAppAccountAndEligiblity.setEffectiveDate(apiMasterAppAccountEligiblity.getEffectiveDate());
		masterAppAccountAndEligiblity.setOrganizationName(apiMasterAppAccountEligiblity.getOrganizationName());
		masterAppAccountAndEligiblity.setCityOfHq(apiMasterAppAccountEligiblity.getCityOfHq());
		masterAppAccountAndEligiblity.setSubsidiariesAffiliates(apiMasterAppAccountEligiblity.getSubsidiariesAffiliates());
		masterAppAccountAndEligiblity.setCertHolders(apiMasterAppAccountEligiblity.getCertHolders());
		masterAppAccountAndEligiblity.setHourPerWeekFullTime(apiMasterAppAccountEligiblity.getHourPerWeekFullTime());
		masterAppAccountAndEligiblity.setHourPerWeekpartTime(apiMasterAppAccountEligiblity.getHourPerWeekpartTime());
		masterAppAccountAndEligiblity.setFirstOftheMonth(apiMasterAppAccountEligiblity.getFirstOftheMonth());
		masterAppAccountAndEligiblity.setFullTimeOrPartTime(apiMasterAppAccountEligiblity.getFullTimeOrPartTime());
		masterAppAccountAndEligiblity.setFullTimeEligibleCoverageDuration(apiMasterAppAccountEligiblity.getFullTimeEligibleCoverageDuration());
		masterAppAccountAndEligiblity.setPartTimeEligibleCoverageDuration(apiMasterAppAccountEligiblity.getPartTimeEligibleCoverageDuration());
		masterAppAccountAndEligiblity.setFullTimeEligibleCoverageMonth(apiMasterAppAccountEligiblity.getFullTimeEligibleCoverageMonth());
		masterAppAccountAndEligiblity.setPartTimeEligibleCoverageMonth(apiMasterAppAccountEligiblity.getPartTimeEligibleCoverageMonth());
		masterAppAccountAndEligiblity.setEnrollEmployeeCount(apiMasterAppAccountEligiblity.getEnrollEmployeeCount());
		masterAppAccountAndEligiblity.setEligibleEmployeeCount(apiMasterAppAccountEligiblity.getEligibleEmployeeCount());
		masterAppAccountAndEligiblity.setOtherRequirement(apiMasterAppAccountEligiblity.getOtherRequirement());
		masterAppAccountAndEligiblity.setPurposeOfApplication(apiMasterAppAccountEligiblity.getPurposeOfApplication());
		masterAppAccountAndEligiblity.setClassesAddedOrRemoved(apiMasterAppAccountEligiblity.getClassesAddedOrRemoved());
		masterAppAccountAndEligiblity.setPolicyNumbers(apiMasterAppAccountEligiblity.getPolicyNumbers());
		masterAppAccountAndEligiblity.setEmployerTaxNumber(apiMasterAppAccountEligiblity.getEmployerTaxNumber());
		masterAppAccountAndEligiblity.setApplicant(apiMasterAppAccountEligiblity.getApplicant());
		masterAppAccountAndEligiblity.setApplicantOther(apiMasterAppAccountEligiblity.getApplicantOther());
		masterAppAccountAndEligiblity.setSicCode(apiMasterAppAccountEligiblity.getSicCode());
		masterAppAccountAndEligiblity.setAddress(apiMasterAppAccountEligiblity.getAddress());
		masterAppAccountAndEligiblity.setNumberOrStreet(apiMasterAppAccountEligiblity.getNumberOrStreet());
		masterAppAccountAndEligiblity.setCity(apiMasterAppAccountEligiblity.getCity());
		masterAppAccountAndEligiblity.setState(apiMasterAppAccountEligiblity.getState());
		masterAppAccountAndEligiblity.setZip(apiMasterAppAccountEligiblity.getZip());
		masterAppAccountAndEligiblity.setPhysicalAddress(apiMasterAppAccountEligiblity.getPhysicalAddress());
		masterAppAccountAndEligiblity.setPhysicalNumberOrStreet(apiMasterAppAccountEligiblity.getPhysicalNumberOrStreet());
		masterAppAccountAndEligiblity.setPhysicalCity(apiMasterAppAccountEligiblity.getPhysicalCity());
		masterAppAccountAndEligiblity.setPhysicalState(apiMasterAppAccountEligiblity.getPhysicalState());
		masterAppAccountAndEligiblity.setPhysicalZip(apiMasterAppAccountEligiblity.getPhysicalZip());
		masterAppAccountAndEligiblity.setAuthorizedPersonName(apiMasterAppAccountEligiblity.getAuthorizedPersonName());
		masterAppAccountAndEligiblity.setAuthorizedPersonTitle(apiMasterAppAccountEligiblity.getAuthorizedPersonTitle());
		masterAppAccountAndEligiblity.setContactName(apiMasterAppAccountEligiblity.getContactName());
		masterAppAccountAndEligiblity.setContactTitle(apiMasterAppAccountEligiblity.getContactTitle());
		masterAppAccountAndEligiblity.setContactPhone(apiMasterAppAccountEligiblity.getContactPhone());
		masterAppAccountAndEligiblity.setContactEmail(apiMasterAppAccountEligiblity.getContactEmail());
		masterAppAccountAndEligiblity.setErisaPlan(apiMasterAppAccountEligiblity.getErisaPlan());
		masterAppAccountAndEligiblity.setPlanNumber(apiMasterAppAccountEligiblity.getPlanNumber());
		masterAppAccountAndEligiblity.setSadToBeCovered(apiMasterAppAccountEligiblity.getSadToBeCovered());
		masterAppAccountAndEligiblity.setSadNames(apiMasterAppAccountEligiblity.getSadNames());
		
	}


	@Override
	public MasterAppSeriesResponseModel getMasterAppSeriesData(String situs, String groupType, String census) {
		HttpGet http = new HttpGet(apiUrlService.getMasterAppSeriesDataUrl(situs, groupType,census));
		List<String> seriesList = null;
		List<String> formIds = null;
		String data = null;
		MasterAppSeriesData masterAppSeriesData = null;
		MasterAppSeriesResponseModel masterAppSeriesResponseModel = null;
		try {
			data = apiUrlService.executeHttpGet(httpclient, http);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			masterAppSeriesData = objectMapper.readValue(data,MasterAppSeriesData.class);
			masterAppSeriesData.stream().forEach(m->{
				if (MasterAppFormIds.C03204.getValue().contains(m.getFormId())) {
					m.setApiFormId(m.getFormId());
					m.setFormId(MasterAppFormIds.C03204.toString());
				} else if (MasterAppFormIds.C02204.getValue().contains(m.getFormId())) {
					m.setApiFormId(m.getFormId());
					m.setFormId(MasterAppFormIds.C02204.toString());
				} else if (MasterAppFormIds.C01204.getValue().contains(m.getFormId())) {
					m.setApiFormId(m.getFormId());
					m.setFormId(MasterAppFormIds.C01204.toString());
				} else if (MasterAppFormIds.C22201.getValue().contains(m.getFormId())) {
					m.setApiFormId(m.getFormId());
					m.setFormId(MasterAppFormIds.C22201.toString());
				} else if (MasterAppFormIds.C60201.getValue().contains(m.getFormId())) {
					m.setApiFormId(m.getFormId());
					m.setFormId(MasterAppFormIds.C60201.toString());
				} else if (MasterAppFormIds.WL9800_MA.getValue().contains(m.getFormId())) {
					m.setApiFormId(m.getFormId());
					m.setFormId(MasterAppFormIds.WL9800_MA.toString());
				} else if (MasterAppFormIds.CAI9110.getValue().contains(m.getFormId())) {
					m.setApiFormId(m.getFormId());
					m.setFormId(MasterAppFormIds.CAI9110.toString());
				} else if (MasterAppFormIds.C81201.getValue().contains(m.getFormId())) {
					m.setApiFormId(m.getFormId());
					m.setFormId(MasterAppFormIds.C81201.toString());
				} else if (MasterAppFormIds.C80201.getValue().contains(m.getFormId())) {
					m.setApiFormId(m.getFormId());
					m.setFormId(MasterAppFormIds.C80201.toString());
				} else if (MasterAppFormIds.C70201.getValue().contains(m.getFormId())) {
					m.setApiFormId(m.getFormId());
					m.setFormId(MasterAppFormIds.C70201.toString());
				} else if (MasterAppFormIds.C50201.getValue().contains(m.getFormId())) {
					m.setApiFormId(m.getFormId());
					m.setFormId(MasterAppFormIds.C50201.toString());
				} else if (MasterAppFormIds.C21201.getValue().contains(m.getFormId())) {
					m.setApiFormId(m.getFormId());
					m.setFormId(MasterAppFormIds.C21201.toString());
				} else if (MasterAppFormIds.GP5000_MA.getValue().contains(m.getFormId())) {
					m.setApiFormId(m.getFormId());
					m.setFormId(MasterAppFormIds.GP5000_MA.toString());
				} else {
					m.setApiFormId(m.getFormId());
					m.setFormId(m.getFormId());
				}
			});
			seriesList = masterAppSeriesData.stream().map(m -> m.getSeries()).flatMap(List::stream).distinct().collect(Collectors.toList());
			formIds = masterAppSeriesData.stream().map(m -> m.getFormId()).distinct().collect(Collectors.toList());
			if(seriesList.size()>0 && formIds.size()>0) {
				masterAppSeriesResponseModel = new MasterAppSeriesResponseModel();
				masterAppSeriesResponseModel.setEligibleSeries(seriesList);
				masterAppSeriesResponseModel.setFormids(formIds);
				masterAppSeriesResponseModel.setMasterAppSeriesData(masterAppSeriesData);
			}
				
		}catch (Exception e) {
			log.error("getMasterAppData error: ", e);
		}
		return masterAppSeriesResponseModel;
	}

	@Override
	public MasterApp addProducts(String addedProducts) {
		MasterApp masterApp = new MasterApp();
		try {
			List<String> availableproductsList = Stream.of(addedProducts.split(",")).collect(Collectors.toList());
			List<Product> masterAppProductList = new ArrayList<>();
			availableproductsList.stream().forEach(product ->{
				if(product.equalsIgnoreCase("7700") || product.equalsIgnoreCase("7800") || product.equalsIgnoreCase("70000")) {
					Product masterAppProduct = new Product();
					masterAppProduct.setProductName("AC"+product);
					masterAppProduct.setSeries(product);
					masterAppProductList.add(masterAppProduct);
				}
				if(product.equalsIgnoreCase("2100")|| product.equalsIgnoreCase("2800") || product.equalsIgnoreCase("20000") || product.equalsIgnoreCase("21000") || product.equalsIgnoreCase("22000")) {
					Product masterAppProduct = new Product();
					masterAppProduct.setProductName("CI"+product);
					masterAppProduct.setSeries(product);
					masterAppProductList.add(masterAppProduct);
				}
				if(product.equalsIgnoreCase("8500") || product.equalsIgnoreCase("8800") || product.equalsIgnoreCase("80000") || product.equalsIgnoreCase("81000")) {
					Product masterAppProduct = new Product();
					masterAppProduct.setProductName("HI"+product);
					masterAppProduct.setSeries(product);
					masterAppProductList.add(masterAppProduct);
				}
				if(product.equalsIgnoreCase("1100")) {
					Product masterAppProduct = new Product();
					masterAppProduct.setProductName("D"+product);
					masterAppProduct.setSeries(product);
					masterAppProductList.add(masterAppProduct);
				}
				if(product.equalsIgnoreCase("5000") || product.equalsIgnoreCase("50000")) {
					Product masterAppProduct = new Product();
					masterAppProduct.setProductName("DI"+product);
					masterAppProduct.setSeries(product);
					masterAppProductList.add(masterAppProduct);
				}
				if(product.equalsIgnoreCase("9100")) {
					Product masterAppProduct = new Product();
					masterAppProduct.setProductName("TL"+product);
					masterAppProduct.setSeries(product);
					masterAppProductList.add(masterAppProduct);
				}
				if(product.equalsIgnoreCase("9800") || product.equalsIgnoreCase("60000")) {
					Product masterAppProduct = new Product();
					masterAppProduct.setProductName("WL"+product);
					masterAppProduct.setSeries(product);
					masterAppProductList.add(masterAppProduct);
				}
				if(product.equalsIgnoreCase("93000")) {
					Product masterAppProduct = new Product();
					masterAppProduct.setProductName("TL"+product);
					masterAppProduct.setSeries(product);
					masterAppProductList.add(masterAppProduct);
				}
			});
			masterApp.setProduct(masterAppProductList);
		}
		catch (Exception e) {
			log.error("addProducts error: ", e);
		}
		return masterApp;
	}


	@Override
	public String getOptionalFeatures() {
		HttpGet http = new HttpGet(apiUrlService.getMasterAppOptionalFeatureDataUrl());
		String apiResponse = null;
		try {
			apiResponse = apiUrlService.executeHttpGet(httpclient, http);
			if(apiResponse!=null && apiResponse!="null") {
				return apiResponse;
			}
		}
		catch (Exception e) {
			log.error("getOptionalFeatures error: ", e);
		}
		return apiResponse;
	}


	@Override
	public SaveCasebuildResponse saveMasterAppData(ApiMasterAppData apiMasterAppData, AuditUserActivity audit, String zone) {
		SaveCasebuildResponse saveMasterAppResponse = null;
		try {
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			String formJson = objectMapper.writeValueAsString(apiMasterAppData);
			HttpPut httpPut = new HttpPut(apiUrlService.getMasterAppSaveUrl());
			httpPut.setHeader("Content-type", "application/json");
			String apiResponse = apiUrlService.executeHttpPut(httpclient, httpPut,formJson);
			if(apiResponse != null) {
				saveMasterAppResponse = objectMapper.readValue(apiResponse, SaveCasebuildResponse.class);
				if(audit != null && saveMasterAppResponse.isStatus()) {
					ZoneId zoneId = ZoneId.of(zone);
					LocalDateTime current = LocalDateTime.now(zoneId);
					audit.setCreatedDateTime(current.toString());
				    audit.setApplicationId(apiMasterAppData.getMasterAppAccountEligiblity().getGroupNumberOrGpId() + "#" + apiMasterAppData.getMasterAppAccountEligiblity().getEffectiveDate());
				    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					LocalDateTime start = LocalDateTime.parse(audit.getQueryDateTime(), format);
				    String totalTime = auditService.getTurnAroundTime(current,start);
					audit.setTurnaroundTime(totalTime);
					audit.setApplicationData("");
					auditService.saveAuditEntry(audit);
				}
				return saveMasterAppResponse;
			}
		}
		catch (Exception e) {
			log.error("saveMasterAppData error: ", e);
		}
		return saveMasterAppResponse;
	}


	public ApiMasterAppData getApiMasterAppData(MasterApp masterApp) {
		ApiMasterAppData apiMasterAppData = null;
		try {
			if(masterApp!=null) {
				apiMasterAppData = new ApiMasterAppData();
				apiMasterAppData.setMasterAppEntityId(masterApp.getMasterAppEntityId());
				apiMasterAppData.setVersion(masterApp.getVersion()!=null && !masterApp.getVersion().isEmpty()?Integer.valueOf(masterApp.getVersion()):0);
				apiMasterAppData.setSitusState(masterApp.getSitusState());
				apiMasterAppData.setGroupType(masterApp.getGroupType());
				apiMasterAppData.setCensusEnrollment(masterApp.getCensusEnrollment());
				apiMasterAppData.setEligibleSeries(masterApp.getEligibleSeries());
				apiMasterAppData.setFormIds(masterApp.getFormIds());
				apiMasterAppData.setForStatusUpdate(false);
				apiMasterAppData.setFilenetDocumentId(masterApp.getFilenetDocumentId()!=null?masterApp.getFilenetDocumentId():null);
				apiMasterAppData.setDataStatus(masterApp.getDataStatus());
				apiMasterAppData.setFirstSignerEmailId(masterApp.getFirstSignerEmailId());
				apiMasterAppData.setSecondSignerEmailId(masterApp.getSecondSignerEmailId());
				
				// Setting Account Data
				ApiMasterAppAccountEligiblity apiMasterAppAccountEligiblity = new ApiMasterAppAccountEligiblity();
				apiMasterAppAccountEligiblity.setGroupNumberOrGpId(masterApp.getAccountAndEligiblity().getGroupNumberOrGpId());
				apiMasterAppAccountEligiblity.setEffectiveDate(masterApp.getAccountAndEligiblity().getEffectiveDate());
				apiMasterAppAccountEligiblity.setOrganizationName(masterApp.getAccountAndEligiblity().getOrganizationName());
				apiMasterAppAccountEligiblity.setSubsidiariesAffiliates(masterApp.getAccountAndEligiblity().getSubsidiariesAffiliates());
				apiMasterAppAccountEligiblity.setCertHolders(masterApp.getAccountAndEligiblity().getCertHolders());
				apiMasterAppAccountEligiblity.setHourPerWeekFullTime(masterApp.getAccountAndEligiblity().getHourPerWeekFullTime());
				apiMasterAppAccountEligiblity.setHourPerWeekpartTime(masterApp.getAccountAndEligiblity().getHourPerWeekpartTime());
				apiMasterAppAccountEligiblity.setFirstOftheMonth(masterApp.getAccountAndEligiblity().getFirstOftheMonth());
				apiMasterAppAccountEligiblity.setFullTimeOrPartTime(masterApp.getAccountAndEligiblity().getFullTimeOrPartTime());
				apiMasterAppAccountEligiblity.setFullTimeEligibleCoverageDuration(masterApp.getAccountAndEligiblity().getFullTimeEligibleCoverageDuration());
				apiMasterAppAccountEligiblity.setPartTimeEligibleCoverageDuration(masterApp.getAccountAndEligiblity().getPartTimeEligibleCoverageDuration());
				apiMasterAppAccountEligiblity.setFullTimeEligibleCoverageMonth(masterApp.getAccountAndEligiblity().getFullTimeEligibleCoverageMonth());
				apiMasterAppAccountEligiblity.setPartTimeEligibleCoverageMonth(masterApp.getAccountAndEligiblity().getPartTimeEligibleCoverageMonth());
				apiMasterAppAccountEligiblity.setEnrollEmployeeCount(masterApp.getAccountAndEligiblity().getEnrollEmployeeCount());
				apiMasterAppAccountEligiblity.setEligibleEmployeeCount(masterApp.getAccountAndEligiblity().getEligibleEmployeeCount());
				apiMasterAppAccountEligiblity.setOtherRequirement(masterApp.getAccountAndEligiblity().getOtherRequirement());
				apiMasterAppAccountEligiblity.setPurposeOfApplication(masterApp.getAccountAndEligiblity().getPurposeOfApplication());
				apiMasterAppAccountEligiblity.setClassesAddedOrRemoved(masterApp.getAccountAndEligiblity().getClassesAddedOrRemoved());
				apiMasterAppAccountEligiblity.setPolicyNumbers(masterApp.getAccountAndEligiblity().getPolicyNumbers());
				apiMasterAppAccountEligiblity.setApplicant(masterApp.getAccountAndEligiblity().getApplicant());
				apiMasterAppAccountEligiblity.setApplicantOther(masterApp.getAccountAndEligiblity().getApplicantOther());
				apiMasterAppAccountEligiblity.setSicCode(masterApp.getAccountAndEligiblity().getSicCode());
				apiMasterAppAccountEligiblity.setAddress(masterApp.getAccountAndEligiblity().getAddress());
				apiMasterAppAccountEligiblity.setNumberOrStreet(masterApp.getAccountAndEligiblity().getNumberOrStreet());
				apiMasterAppAccountEligiblity.setCity(masterApp.getAccountAndEligiblity().getCity());
				apiMasterAppAccountEligiblity.setState(masterApp.getAccountAndEligiblity().getState());
				apiMasterAppAccountEligiblity.setZip(masterApp.getAccountAndEligiblity().getZip());
				apiMasterAppAccountEligiblity.setPhysicalAddress(masterApp.getAccountAndEligiblity().getPhysicalAddress());
				apiMasterAppAccountEligiblity.setPhysicalNumberOrStreet(masterApp.getAccountAndEligiblity().getPhysicalNumberOrStreet());
				apiMasterAppAccountEligiblity.setPhysicalCity(masterApp.getAccountAndEligiblity().getPhysicalCity());
				apiMasterAppAccountEligiblity.setPhysicalState(masterApp.getAccountAndEligiblity().getPhysicalState());
				apiMasterAppAccountEligiblity.setPhysicalZip(masterApp.getAccountAndEligiblity().getPhysicalZip());
				apiMasterAppAccountEligiblity.setAuthorizedPersonName(masterApp.getAccountAndEligiblity().getAuthorizedPersonName());
				apiMasterAppAccountEligiblity.setAuthorizedPersonTitle(masterApp.getAccountAndEligiblity().getAuthorizedPersonTitle());
				apiMasterAppAccountEligiblity.setContactName(masterApp.getAccountAndEligiblity().getContactName());
				apiMasterAppAccountEligiblity.setContactTitle(masterApp.getAccountAndEligiblity().getContactTitle());
				apiMasterAppAccountEligiblity.setContactEmail(masterApp.getAccountAndEligiblity().getContactEmail());
				apiMasterAppAccountEligiblity.setContactPhone(masterApp.getAccountAndEligiblity().getContactPhone());
				apiMasterAppAccountEligiblity.setErisaPlan(masterApp.getAccountAndEligiblity().getErisaPlan());
				apiMasterAppAccountEligiblity.setPlanNumber(masterApp.getAccountAndEligiblity().getPlanNumber());
				apiMasterAppAccountEligiblity.setChurchPlan(masterApp.getAccountAndEligiblity().getChurchPlan());
				apiMasterAppAccountEligiblity.setSadToBeCovered(masterApp.getAccountAndEligiblity().getSadToBeCovered());
				apiMasterAppAccountEligiblity.setSadNames(masterApp.getAccountAndEligiblity().getSadNames());
				apiMasterAppAccountEligiblity.setCityOfHq(masterApp.getAccountAndEligiblity().getCityOfHq());
				
				apiMasterAppData.setMasterAppAccountEligiblity(apiMasterAppAccountEligiblity);
				
				List<ApiMasterAppProduct> masterAppProductList = new ArrayList<>();
				masterApp.getProduct().stream().forEach(product ->{
					ApiMasterAppProduct apiMasterAppProduct = new ApiMasterAppProduct();
					apiMasterAppProduct.setProductName(product.getProductName());
					apiMasterAppProduct.setProductSequence(product.getProductSequence());
					apiMasterAppProduct.setSeries(product.getSeries());
					apiMasterAppProduct.setPlanLevelPremium(product.getPlanLevelPremium());
					apiMasterAppProduct.setBenefitType(product.getBenefitType());
					apiMasterAppProduct.setApplicationReason(product.getApplicationReason());
					apiMasterAppProduct.setExistingPolicyNumber(product.getExistingPolicyNumber());
					apiMasterAppProduct.setOtherApplicationReason(product.getOtherApplicationReason());
					apiMasterAppProduct.setEligibleEmployee(product.getEligibleEmployee());
					apiMasterAppProduct.setEligibleEmployeeClass(product.getEligibleEmployeeClass());
					apiMasterAppProduct.setEligibleSpouse(product.getEligibleSpouse());
					apiMasterAppProduct.setEligibleEmployeeExcept(product.getEligibleEmployeeExcept());
					apiMasterAppProduct.setEligibleEmployeeOther(product.getEligibleEmployeeOther());
					apiMasterAppProduct.setProductClass(product.getProductClass());
					apiMasterAppProduct.setPlan((product.getPlan()!=null && product.getPlan().equalsIgnoreCase("N/A"))?null:product.getPlan());
					
					if(product.getPlanFeatures()!=null && !product.getPlanFeatures().isEmpty()) {
						apiMasterAppProduct.setPlanFeatures(Arrays.asList(product.getPlanFeatures().get(0).split(",")));
					}else {
						apiMasterAppProduct.setPlanFeatures(null);
					}
					
					apiMasterAppProduct.setPlanFeaturesOther(product.getPlanFeaturesOther());
					
					if(product.getOptionalFeatures()!=null && !product.getOptionalFeatures().isEmpty()) {
						apiMasterAppProduct.setOptionalFeatures(Arrays.asList(product.getOptionalFeatures().get(0).split(",")));
					}else {
						apiMasterAppProduct.setOptionalFeatures(null);
					}
					
					apiMasterAppProduct.setOptionalFeaturesOther(product.getOptionalFeaturesOther());
					apiMasterAppProduct.setOptionalFeaturesText(product.getOptionalFeaturesText());
					apiMasterAppProduct.setEffectiveDate(product.getEffectiveDate());
					apiMasterAppProduct.setRates(product.getRates());
					apiMasterAppProduct.setProductContribution(product.getProductContribution());
					apiMasterAppProduct.setEmployeePremium(product.getEmployeePremium());
					apiMasterAppProduct.setEmployerPremium(product.getEmployerPremium());
					apiMasterAppProduct.setIncomeReplacement(product.getIncomeReplacement());
					apiMasterAppProduct.setBenefitPeriod(product.getBenefitPeriod());
					apiMasterAppProduct.setEliminationPeriod(product.getEliminationPeriod());
					apiMasterAppProduct.setGroupPolicyState(product.getGroupPolicyState());
					apiMasterAppProduct.setOptionalTermlifeCoverages(product.getOptionalTermlifeCoverages());
					apiMasterAppProduct.setCiac(product.getCiac());
					apiMasterAppProduct.setProposedEffectiveDate(product.getProposedEffectiveDate());
					apiMasterAppProduct.setOnDate(product.getOnDate());
					apiMasterAppProduct.setMinimumNoOfEmployees(product.getMinimumNoOfEmployees());
					apiMasterAppProduct.setPercentileOfEligibleEmployees(product.getPercentileOfEligibleEmployees());
					apiMasterAppProduct.setPremiumPaid(product.getPremiumPaid());
					apiMasterAppProduct.setPremiumPaymentMode(product.getPremiumPaymentMode());
					apiMasterAppProduct.setEmployeeCostOfInsurance(product.getEmployeeCostOfInsurance());
					apiMasterAppProduct.setPlanYearPerInsured(product.getPlanYearPerInsured());
					apiMasterAppProduct.setHiEnrollEmployeeCount(product.getHiEnrollEmployeeCount());
					apiMasterAppProduct.setPremiumDue(product.getPremiumDue());
					
					if(product.getActivelyAtWork().getActiveAtWorkData().get(0).getClassName()!=null && !product.getActivelyAtWork().getActiveAtWorkData().get(0).getClassName().isEmpty()) {
						ApiActivelyAtWork apiActivelyAtWork = new ApiActivelyAtWork();
						if(product.getActivelyAtWork().getActiveAtWorkData()!=null && !product.getActivelyAtWork().getActiveAtWorkData().isEmpty()) {
							List<ApiActiveAtWorkData> activeAtWorkDataList = new ArrayList<>();
							product.getActivelyAtWork().getActiveAtWorkData().stream().forEach(activeAtwork->{
								ApiActiveAtWorkData apiActiveAtWorkData = new ApiActiveAtWorkData();
								apiActiveAtWorkData.setActivelyAtWorkHours(activeAtwork.getActivelyAtWorkHours());
								apiActiveAtWorkData.setClassName(activeAtwork.getClassName());
								activeAtWorkDataList.add(apiActiveAtWorkData);
							});
							apiActivelyAtWork.setActiveAtWorkData(activeAtWorkDataList);
						}
						apiMasterAppProduct.setActivelyAtWork(apiActivelyAtWork);
					}else {
						apiMasterAppProduct.setActivelyAtWork(null);
					}
					
					if(product.getWaitingPeriodEligibility().getWaitingPeriodEligibilityData().get(0).getClassName()!=null && !product.getWaitingPeriodEligibility().getWaitingPeriodEligibilityData().get(0).getClassName().isEmpty()) {
						ApiWaitingPeriodEligibility apiWaitingPeriodEligibility = new ApiWaitingPeriodEligibility();
						if(product.getWaitingPeriodEligibility().getWaitingPeriodEligibilityData()!=null && !product.getWaitingPeriodEligibility().getWaitingPeriodEligibilityData().isEmpty()) {
							List<ApiWaitingPeriodEligibilityData> waitingPeriodEligibilityDataList = new ArrayList<>();
							product.getWaitingPeriodEligibility().getWaitingPeriodEligibilityData().stream().forEach(waitingPeriodEligibility->{
								ApiWaitingPeriodEligibilityData apiWaitingPeriodEligibilityData = new ApiWaitingPeriodEligibilityData();
								apiWaitingPeriodEligibilityData.setClassName(waitingPeriodEligibility.getClassName());
								apiWaitingPeriodEligibilityData.setNumberOfDays(waitingPeriodEligibility.getNumberOfDays());
								apiWaitingPeriodEligibilityData.setWaitingPeriod(waitingPeriodEligibility.getWaitingPeriod());
								waitingPeriodEligibilityDataList.add(apiWaitingPeriodEligibilityData);
							});
							apiWaitingPeriodEligibility.setWaitingPeriodEligibilityData(waitingPeriodEligibilityDataList);
						}
						apiMasterAppProduct.setWaitingPeriodEligibility(apiWaitingPeriodEligibility);
					}else {
						apiMasterAppProduct.setWaitingPeriodEligibility(null);
					}
					masterAppProductList.add(apiMasterAppProduct);
					
				});
				apiMasterAppData.setMasterAppProducts(masterAppProductList);
				
				//Setting review table data
				if(masterApp.getMasterAppReviewTable().getMasterAppReviewTableData().get(0).getFormId()!=null && !masterApp.getMasterAppReviewTable().getMasterAppReviewTableData().get(0).getFormId().isEmpty()) {
					MasterAppReviewTableApi masterAppReviewTableApi = new MasterAppReviewTableApi();
					if(masterApp.getMasterAppReviewTable().getMasterAppReviewTableData()!=null && !masterApp.getMasterAppReviewTable().getMasterAppReviewTableData().isEmpty()) {
						List<MasterAppReviewTableDataApi> masterAppReviewTableDataList = new ArrayList<MasterAppReviewTableDataApi>();
						masterApp.getMasterAppReviewTable().getMasterAppReviewTableData().stream().forEach(reviewTableData->{
							MasterAppReviewTableDataApi masterAppReviewTableDataApi = new MasterAppReviewTableDataApi();
							masterAppReviewTableDataApi.setFormId(reviewTableData.getFormId());
							masterAppReviewTableDataApi.setApiFormId(reviewTableData.getApiFormId());
							masterAppReviewTableDataApi.setProducts(reviewTableData.getProduct());
							masterAppReviewTableDataApi.setSignStatus(reviewTableData.getSignStatus());
							masterAppReviewTableDataApi.setSignatureCheckbox(reviewTableData.getSignatureCheckbox());
							masterAppReviewTableDataApi.setFilenetDocumentId(reviewTableData.getFilenetDocumentId());
							masterAppReviewTableDataApi.setMasterappSentDatetime(reviewTableData.getMasterappSentDatetime());
							masterAppReviewTableDataApi.setMasterappReceivedDatetime(reviewTableData.getMasterappReceivedDatetime());
							masterAppReviewTableDataApi.setAgreementId(reviewTableData.getAgreementId());
							masterAppReviewTableDataList.add(masterAppReviewTableDataApi);
						});
						masterAppReviewTableApi.setMasterAppReviewTableData(masterAppReviewTableDataList);
					}
					apiMasterAppData.setMasterAppReviewTable(masterAppReviewTableApi);
				}else {
					apiMasterAppData.setMasterAppReviewTable(null);
				}
				
			}
		}
		catch (Exception e) {
			log.error("getApiMasterAppData error: ", e);
		}
		return apiMasterAppData;
	}


	@Override
	public MasterApp updateMasterAppProducts(String masterAppData, String addedProducts, String deletedProducts) throws JsonMappingException, JsonProcessingException {
		MasterApp masterApp = null;
		List<String> addedproductsList = null;
		List<String> masterAppDataProductList = null;
		Product newProduct = null;
		Product deletedProduct = null;
		try {
			if(masterAppData!=null && !masterAppData.isEmpty()) {
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				masterApp = objectMapper.readValue(masterAppData, MasterApp.class);
				List<Product> productList = masterApp.getProduct();
				if(addedProducts!=null && !addedProducts.isEmpty()) {
					addedproductsList = Stream.of(addedProducts.split(",")).collect(Collectors.toList());
					for(int i=0;i<addedproductsList.size();i++) {
						masterAppDataProductList = productList.stream().map(product -> product.getProductName()).collect(Collectors.toList());
						if(!masterAppDataProductList.contains(addedproductsList.get(i))) {
							newProduct = new Product();
							if(addedproductsList.get(i).equalsIgnoreCase("D1100")) {
								newProduct.setSeries(addedproductsList.get(i).substring(1));
							}
							else {
								newProduct.setSeries(addedproductsList.get(i).substring(2));
							}
							newProduct.setProductName(addedproductsList.get(i));
							setProductSequence(newProduct);
							productList.add(newProduct);
							
						}
					}
					productList.sort(Comparator.comparingInt(product->product.getProductSequence()));
					masterApp.setProduct(productList);
				}
				if(deletedProducts!=null && !deletedProducts.isEmpty()){
					List<String> deletedproductsList = Stream.of(deletedProducts.split(",")).collect(Collectors.toList());
					productList = masterApp.getProduct();
					List<Product> updatedList = masterApp.getProduct().stream().collect(Collectors.toList());
					for(int i=0;i<productList.size();i++) {
						if(deletedproductsList.contains(productList.get(i).getProductName())) {
							deletedProduct = productList.get(i);
							updatedList.remove(deletedProduct);
						}
					}
					updatedList.sort(Comparator.comparingInt(product->product.getProductSequence()));
					masterApp.setProduct(updatedList);
				}
			}
		} 
		catch (JsonIOException e) {
			log.error("Exception in updating products: ", e);
		} catch (JsonSyntaxException e) {
			log.error("Exception in updating products: ", e);
		}
		return masterApp;
	}
	
	private void setProductSequence(Product newProduct) {
		try {
			if(newProduct.getProductName()!=null) {
				if(newProduct.getProductName().contains(MasterAppProductSequence.AC.toString())) {
					newProduct.setProductSequence(MasterAppProductSequence.AC.getValue());
				}
				else if(newProduct.getProductName().contains(MasterAppProductSequence.CI.toString())) {
					newProduct.setProductSequence(MasterAppProductSequence.CI.getValue());
				}
				else if(newProduct.getProductName().contains(MasterAppProductSequence.HI.toString())) {
					newProduct.setProductSequence(MasterAppProductSequence.HI.getValue());
				}
				else if(newProduct.getProductName().contains(MasterAppProductSequence.D.toString())) {
					newProduct.setProductSequence(MasterAppProductSequence.D.getValue());
				}
				else if(newProduct.getProductName().contains(MasterAppProductSequence.DI.toString())) {
					newProduct.setProductSequence(MasterAppProductSequence.DI.getValue());
				}
				else if(newProduct.getProductName().contains(MasterAppProductSequence.TL.toString())) {
					newProduct.setProductSequence(MasterAppProductSequence.TL.getValue());
				}
				else if(newProduct.getProductName().contains(MasterAppProductSequence.WL.toString())) {
					newProduct.setProductSequence(MasterAppProductSequence.WL.getValue());
				}else {
					newProduct.setProductSequence(8);
				}
			}
		}catch (Exception e) {
			log.error("setProductSequence error: ", e);
		}
		
	}

	@Override
	public void downloadPdf(String formData, String formId, SlingHttpServletRequest request, 
			SlingHttpServletResponse response, MasterAppSeriesData masterAppSeriesData) {
		String xmlData = transformXml(formData, formId, masterAppSeriesData);
		String xdpPath = "/content/dam/formsanddocuments/aflacapps/masterapp-designs/" + formId + ".pdf";
		
		try {
			pdfService.generatePDF(xmlData, xdpPath, request, response, "", "read", "Master Application");
		} catch (IOException e) {
			log.error("Error in master app pdf generation: ", e);
		}
	}
	
	// This function is created to support XML transformation based on formId and it's applicable series.
	public String transformXml(String formData, String formId, MasterAppSeriesData masterAppSeriesData) {
		String xml = "";
		try {
			Document doc = DocumentUtil.convertToDocument(formData);
			String masterAppXml = DocumentUtil.getNode("/afData/afBoundData/masterApp", doc);
			
			InputStream stream = new ByteArrayInputStream(masterAppXml.getBytes(StandardCharsets.UTF_8));
			JAXBContext context = JAXBContext.newInstance(MasterApp.class);
			Unmarshaller um = context.createUnmarshaller();
			MasterApp masterApp = (MasterApp) um.unmarshal(stream);
			
			List<MasterAppFormIdsModel> filteredList = masterAppSeriesData.stream().filter(f -> f.getFormId().equalsIgnoreCase(formId)).collect(Collectors.toList());
			List<String> filteredSeries = filteredList.get(0).getSeries();
						
			List<Product> filteredProducts = new ArrayList<>();
			masterApp.getProduct().forEach(p -> {
				if(!p.getProductName().equalsIgnoreCase("TL93000") &&
				   !p.getProductName().equalsIgnoreCase("TL9100") &&
				   !p.getProductName().equalsIgnoreCase("WL9800") &&
				   !p.getProductName().equalsIgnoreCase("WL60000")) 
				{
					List<String> opf = Arrays.asList(p.getOptionalFeatures().get(0).split(","));
					p.setOptionalFeatures(opf);
				}
				if(filteredSeries.contains(p.getSeries())) {
					filteredProducts.add(p);
				}
				
				if(p.getPlan()!=null && p.getPlan().equalsIgnoreCase("N/A")) {
					p.setPlan(null);
				}
			});
			
			masterApp.setProduct(filteredProducts);
			
			xml = PdfGenerationUtil.convertObjectToXML(masterApp);
		} catch (JAXBException | IOException e) {
			log.info("XML Transformation Error: ", e);
		}
		return xml;
	}


	@Override
	public void downloadFileFromFileNet(String docId, SlingHttpServletResponse response) {
		docId = docId.replace("{", "").replace("}", "");
		log.info("DocID in aem server: " + docId);
		fileNet.getFileFromFileNet(docId, objStore, response);
		
	}
}
