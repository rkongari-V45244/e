package com.aflac.core.osgi.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.ExperienceApiConfig;
import com.aflac.core.experience.api.model.ComplianceDetail;
import com.aflac.core.experience.api.model.ComplianceVerbiageDetail;
import com.aflac.core.experience.api.model.LabelSequence;
import com.aflac.core.osgi.service.ComplianceService;
import com.aflac.core.util.ExcelSheetStyling;
import com.aflac.xml.casebuilderProducts.models.Compliance;
import com.aflac.xml.casebuilderProducts.models.EmploymentEnrollmentDisclosure;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(service = { ComplianceService.class })
public class ComplianceServiceImpl implements ComplianceService,Serializable {

	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(CaseBuilderServiceImpl.class);
	private transient CloseableHttpClient httpclient;
	@Reference
	private transient ExperienceApiConfig apiUrlService;

	public ComplianceServiceImpl() {
		this.httpclient = HttpClients.createDefault();
	}

	public ComplianceServiceImpl(CloseableHttpClient httpclient,ExperienceApiConfig apiUrlService) {
		this.httpclient = httpclient;
		this.apiUrlService = apiUrlService;
	}
	
	@Override
	public String getComplianceData(String situsState, String lob, String type, String label) {
		ObjectMapper objectMapper = new ObjectMapper();
		HttpGet http = new HttpGet(apiUrlService.getComplianceDataUrl(situsState, lob, type, label,false));
		String apiResponse = null;
		try {
			String data = apiUrlService.executeHttpGet(httpclient, http);
			ComplianceVerbiageDetail complianceVerbiageDetail = objectMapper.readValue(data,ComplianceVerbiageDetail.class);
			apiResponse = objectMapper.writeValueAsString(complianceVerbiageDetail);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return apiResponse;
	}

	@Override
	public String getInitComplianceData(String referenceDataItem) {
		HttpGet http = new HttpGet(apiUrlService.getComplianceReferenceDataUrl(referenceDataItem));
		String apiResponse = null;
		try {
			apiResponse = apiUrlService.executeHttpGet(httpclient, http);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return apiResponse;
	}

	@Override
	public String addComplianceData(String formData) {
		HttpPut httpPut = new HttpPut(apiUrlService.getComplianceSaveUrl());
		httpPut.setHeader("Content-type", "application/json");
		String apiResponse = null;
		try {
			apiResponse = apiUrlService.executeHttpPut(httpclient, httpPut,formData);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return apiResponse;
	}

	@Override
	public String updateComplianceData(String formData) {
		HttpPut httpPut = new HttpPut(apiUrlService.getComplianceSaveUrl());
		httpPut.setHeader("Content-type", "application/json");
		String apiResponse = null;
		try {
			apiResponse = apiUrlService.executeHttpPut(httpclient, httpPut,formData);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return apiResponse;
	}
	
	@Override
	public XSSFWorkbook exportComplianceData(String formData) {
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> headers = Arrays.asList("Type","Question","State","LOB","Label","Status");
		int rowCount = 0, cellCount = 0;
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			ComplianceVerbiageDetail complianceVerbiageDetail = objectMapper.readValue(formData,ComplianceVerbiageDetail.class);
			List<ComplianceDetail> records = complianceVerbiageDetail.getComplianceVerbiageList();
						
			XSSFSheet sheet = workbook.createSheet("Compliance Data");
			XSSFCellStyle headerLable = ExcelSheetStyling.getHeaderLableStyle(workbook);
			XSSFCellStyle rowLables = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,
					IndexedColors.DARK_BLUE.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,
					VerticalAlignment.TOP, false);
			XSSFCellStyle wrappedRow = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,
					IndexedColors.DARK_BLUE.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,
					VerticalAlignment.TOP, true);
			Row row = sheet.createRow(rowCount++);
			sheet.setDefaultColumnWidth(35);
			Cell cell;
			for(String header : headers) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(headerLable);
				cell.setCellValue(header);
			}
			for(ComplianceDetail record : records) {
				row = sheet.createRow(rowCount++);
				cellCount = 0;
				cellCount = setCellData(cellCount, record.getVerbiageType().getValue(), row, wrappedRow);
				
				cellCount = setCellData(cellCount, record.getComplianceText(), row, wrappedRow);
				
				String states = record.getState().stream().map(state -> state.getValue()).collect(Collectors.joining(","));
				cellCount = setCellData(cellCount, states, row, rowLables);
				
				String lobs = record.getLob().stream().map(lob -> lob.getValue()).collect(Collectors.joining(","));
				cellCount = setCellData(cellCount, lobs, row, wrappedRow);
				
				cellCount = setCellData(cellCount, record.getLabel().getValue(), row, wrappedRow);
				
				setCellData(cellCount, record.getActiveStatus() ? "Active" : "Disabled", row, rowLables);
			}
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(5);
		} catch (JsonProcessingException e) {
			log.error("Error ", e);
		}
		return workbook;
	}
	
	private int setCellData(int cellCount, String data, Row row, XSSFCellStyle rowLables) {
		
		Cell cell = row.createCell(cellCount++);
		cell.setCellStyle(rowLables);
		cell.setCellValue(data);
		return cellCount;
	}

	@Override
	public String getEditComplianceData(String recordId) {
		HttpGet http = new HttpGet(apiUrlService.getComplianceEditDataUrl(recordId));
		String apiResponse = null;
		try {
			apiResponse = apiUrlService.executeHttpGet(httpclient, http);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return apiResponse;
	}

	@Override
	public LabelSequence getLabelSequence() {
		HttpGet http = new HttpGet(apiUrlService.getComplianceReferenceDataUrl("LABEL_SEQ"));
		LabelSequence seq = new LabelSequence();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String apiResponse = apiUrlService.executeHttpGet(httpclient, http);
			LabelSequence[] sequence = objectMapper.readValue(apiResponse, LabelSequence[].class);
			List<LabelSequence> list = Arrays.asList(sequence);
			seq = list.stream().filter(s -> s.getId().equalsIgnoreCase("LABEL_SEQ")).collect(Collectors.toList()).get(0);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return seq;
	}
	
	public EmploymentEnrollmentDisclosure getComplianceDataRefill(String situsState, String lob, String type, String label,Boolean isCi22kEnabled) {
		ObjectMapper objectMapper = new ObjectMapper();
		HttpGet http = new HttpGet(apiUrlService.getComplianceDataUrl(situsState, lob, type, label,isCi22kEnabled));
		EmploymentEnrollmentDisclosure empd = new EmploymentEnrollmentDisclosure();
		try {
			String data = apiUrlService.executeHttpGet(httpclient, http);
			List<String> coverageSeq = Arrays.asList("18","19","20");
			List<String> termSeq = Arrays.asList("18","19","22","26","27");
			List<String> CI22kCoverage = null;
			List<String> allCoverage = null;
			ComplianceVerbiageDetail complianceVerbiageDetail = objectMapper.readValue(data,ComplianceVerbiageDetail.class);
			List<ComplianceDetail> complianceVerbiageList = complianceVerbiageDetail.getComplianceVerbiageList().stream()
																.filter(cd -> (!cd.getLabel().getId().equalsIgnoreCase("24") && !cd.getLabel().getId().equalsIgnoreCase("25")))
																.collect(Collectors.toList());
			List<String> healthCoverages = complianceVerbiageList.stream()
												.filter(cd -> cd.getLabel().getId().equalsIgnoreCase("21"))
												.distinct()
												.map(cd -> cd.getComplianceText())
												.collect(Collectors.toList());
			List<String> wholeLifeCoverage = complianceVerbiageList.stream()
												.filter(cd -> cd.getLob().stream().anyMatch(l -> l.getId().equalsIgnoreCase("6")))
												.filter(cd -> cd.getLabel().getId().equalsIgnoreCase("22"))
												.distinct()
												.map(cd -> cd.getComplianceText())
												.collect(Collectors.toList());
			if(isCi22kEnabled) {
				CI22kCoverage = complianceVerbiageList.stream()
												.filter(cd -> cd.getLob().stream().anyMatch(l -> l.getId().equalsIgnoreCase("2")))
												.sorted(Comparator.comparingInt(complaince -> coverageSeq.indexOf((complaince.getLabel().getId()))))
												.distinct()
												.map(cd -> cd.getComplianceText())
												.collect(Collectors.toList());
				allCoverage = complianceVerbiageList.stream()
												.filter(cd -> cd.getLob().stream().anyMatch(l -> !l.getId().equalsIgnoreCase("8") && !l.getId().equalsIgnoreCase("2")))
												.filter(cd -> (!cd.getLabel().getId().equalsIgnoreCase("22") && !cd.getLabel().getId().equalsIgnoreCase("21") && !cd.getLabel().getId().equalsIgnoreCase("2")))
												.sorted(Comparator.comparingInt(complaince -> coverageSeq.indexOf((complaince.getLabel().getId()))))
												.distinct()
												.map(cd -> cd.getComplianceText())
												.collect(Collectors.toList());
			} else {
				allCoverage = complianceVerbiageList.stream()
												.filter(cd -> cd.getLob().stream().anyMatch(l -> !l.getId().equalsIgnoreCase("8")))
												.filter(cd -> (!cd.getLabel().getId().equalsIgnoreCase("22") && !cd.getLabel().getId().equalsIgnoreCase("21") && !cd.getLabel().getId().equalsIgnoreCase("2")))
												.sorted(Comparator.comparingInt(complaince -> coverageSeq.indexOf((complaince.getLabel().getId()))))
												.distinct()
												.map(cd -> cd.getComplianceText())
												.collect(Collectors.toList());
			}
			List<String> majorMedical = complianceVerbiageList.stream()
												.filter(cd -> cd.getLob().stream().anyMatch(l -> l.getId().equalsIgnoreCase("2") || l.getId().equalsIgnoreCase("3") || l.getId().equalsIgnoreCase("4")))
												.filter(cd -> cd.getLabel().getId().equalsIgnoreCase("2"))
												.distinct()
												.map(cd -> cd.getComplianceText())
												.collect(Collectors.toList());
			List<String> termCoverage = complianceVerbiageList.stream()
												.filter(cd -> cd.getLob().stream().anyMatch(l -> l.getId().equalsIgnoreCase("8")))
												.filter(cd -> (!cd.getLabel().getId().equalsIgnoreCase("26") && !cd.getLabel().getId().equalsIgnoreCase("27")))
												.sorted(Comparator.comparingInt(complaince -> termSeq.indexOf((complaince.getLabel().getId()))))
												.distinct()
												.map(cd -> cd.getComplianceText())
												.collect(Collectors.toList());
			
			List<Compliance> compl = new ArrayList<>();
			
			if(healthCoverages != null && !healthCoverages.isEmpty()) {
				Compliance comp = new Compliance();
				comp.setLable("HEALTH COVERAGE:");
				comp.setVerbiage(healthCoverages);
				compl.add(comp);
			}
			if(allCoverage != null && !allCoverage.isEmpty()) {
				Compliance comp = new Compliance();
				comp.setLable("ALL COVERAGES:");
				comp.setVerbiage(allCoverage);
				compl.add(comp);
			}
			if(CI22kCoverage != null && !CI22kCoverage.isEmpty()) {
				Compliance comp = new Compliance();
				comp.setLable("CRITICAL ILLNESS 22000 COVERAGE:");
				comp.setVerbiage(CI22kCoverage);
				compl.add(comp);
			}
			if(majorMedical != null && !majorMedical.isEmpty()) {
				Compliance comp = new Compliance();
				comp.setLable("CALIFORNIA MAJOR MEDICAL STATEMENTS:");
				comp.setVerbiage(majorMedical);
				compl.add(comp);
			}
			if(wholeLifeCoverage != null && !wholeLifeCoverage.isEmpty()) {
				Compliance comp = new Compliance();
				comp.setLable("WHOLE LIFE COVERAGE:");
				comp.setVerbiage(wholeLifeCoverage);
				compl.add(comp);
			}
			if(termCoverage != null && !termCoverage.isEmpty()) {	
				Compliance comp = new Compliance();
				comp.setLable("TERM LIFE 93000 COVERAGE:");
				comp.setVerbiage(termCoverage);
				compl.add(comp);
			}
			empd.setCompliance(compl);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return empd;
	}

}
