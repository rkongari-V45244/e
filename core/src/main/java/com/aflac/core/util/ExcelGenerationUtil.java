package com.aflac.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.aflac.core.experience.api.model.ComplianceDetail;
import com.aflac.core.osgi.service.CaseBuilderService;
import com.aflac.core.osgi.service.ComplianceService;
import com.aflac.core.osgi.service.impl.CaseBuilderServiceImpl;
import com.aflac.xml.casebuilderProducts.models.AccountInformation;
import com.aflac.xml.casebuilderProducts.models.BenefitAmount;
import com.aflac.xml.casebuilderProducts.models.CaseBuilderForm;
import com.aflac.xml.casebuilderProducts.models.FileSubmission;
import com.aflac.xml.casebuilderProducts.models.Location;
import com.aflac.xml.casebuilderProducts.models.Locations;
import com.aflac.xml.casebuilderProducts.models.Product;

public class ExcelGenerationUtil {

	static Logger log = LoggerFactory.getLogger(ExcelGenerationUtil.class);

	static String excelPath = "/content/dam/aflacapps/case-build/InstructionsExcel.xlsx";

	public XSSFWorkbook generateExcel(String formData, SlingHttpServletRequest request, CaseBuilderService caseService, ComplianceService complianceService, String excelCase) throws IOException {

//		ResourceResolver resourceResolver = request.getResourceResolver();
//		Resource excel = resourceResolver.getResource(excelPath);
//		log.info("excel ==>" + excel);
//
//		Asset asset = excel.adaptTo(Asset.class);
//		Resource original = asset.getOriginal();
//		InputStream excelIS = original.adaptTo(InputStream.class);
//		InputStream copyIS = clone(excelIS);
//		
//		XSSFWorkbook workbook;
//		if(copyIS != null)
//			workbook = new XSSFWorkbook(copyIS);
//		else
		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet accountSheet = workbook.createSheet(Products.ACCOUNT_INFORMATION.getValue());
		XSSFSheet fileSubmissionSheet = workbook.createSheet("File Submission");
		XSSFSheet PdfDirectionSheet;
		
		
		CaseBuilderForm caseForm = xmlToJava(formData);
				
		ComplianceDataUtil complianceData = new ComplianceDataUtil(
				caseService.getComplianceVerbiage(caseForm.getAccountInformation().getGroupNumber(),
						caseForm.getAccountInformation().getCoverageBillingEffDate(),
						caseForm.getAccountInformation().getPlatform(),excelCase), complianceService.getLabelSequence());

		List<Product> products = new ArrayList<>();
		if (caseForm != null && caseForm.getProducts() != null)
			products = caseForm.getProducts().getProduct();

		getAccountInfoSheet(workbook, accountSheet, caseForm.getAccountInformation());
		CaseBuilderServiceImpl caseImpl = new CaseBuilderServiceImpl();
		getFillSubmissionSheet(workbook, fileSubmissionSheet, caseForm.getFileSubmission(), caseImpl);
		if(caseForm.getLocations().getLocationSpecified().equalsIgnoreCase("Yes")) {
			XSSFSheet locationSheet = workbook.createSheet("Locations");
			getLocationSheet(workbook, locationSheet, caseForm.getLocations());
		}
		
		XSSFCellStyle helperText = ExcelSheetStyling.getHelperTextStyle(workbook);
		XSSFCellStyle rowLables = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,
				IndexedColors.DARK_BLUE.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,
				VerticalAlignment.TOP, true);
		Row row;
		Cell cell,cell2;
		int productNameCounter = 0;
		for (Product product : products) {
			XSSFSheet sheet; 
			if(workbook.getSheet(product.getProductName()) == null)
				sheet = workbook.createSheet(product.getProductName());
			else
				sheet = workbook.createSheet(product.getProductName() + "-" + ++productNameCounter);
			ExcelSheetStyling.getAflacFreezPane(workbook, sheet);
			XSSFCellStyle fieldLable = ExcelSheetStyling.getfieldLableStyle(workbook, sheet, false);
			XSSFCellStyle borderStyle = ExcelSheetStyling.getfieldLableStyle(workbook, sheet, true);
			int rowCount = 2;
			
			List<ComplianceDetail> healthQues = complianceData.getComplianceData(Labels.HEALTH_QUESTION.getValue(), product.getProductName(),product.getProductId());
			List<ComplianceDetail> disclosures = complianceData.getComplianceData(Labels.DISCLOSURE.getValue(), product.getProductName(),product.getProductId());
			List<ComplianceDetail> affirmations = complianceData.getComplianceData(Labels.AFFIRMATION.getValue(), product.getProductName(),product.getProductId());
			List<ComplianceDetail> questions = complianceData.getComplianceData(Labels.QUESTION.getValue(), product.getProductName(),product.getProductId());
			for (int i = 0; i < 30; i++) {
				switch (i) {
				case 0:
					row = getPrimaryCell(i, rowCount, sheet, workbook);
					cell = row.getCell(0);
					rowCount = row.getRowNum();
					if(product.getProductDescription()!=null && !product.getProductDescription().isEmpty())
						cell.setCellValue(product.getProductName()+"-"+product.getProductDescription());
					else
						cell.setCellValue(product.getProductName());
					sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
					sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					break;
				case 1:
					row = getPrimaryCell(i, rowCount, sheet, workbook);
					cell = row.getCell(0);
					cell2 = row.getCell(3);
					rowCount = row.getRowNum();
					cell.setCellValue("Plan Naming Convention:");
					if(product.getProductDescription()!=null && !product.getProductDescription().isEmpty())
						cell2.setCellValue(product.getPlanName()+"-"+product.getProductDescription());
					else
						cell2.setCellValue(product.getPlanName());
					cell = row.createCell(9);
					cell.setCellStyle(helperText);
					cell.setCellValue("This is how the Aflac product should be presented on the enrollment screen.");
					sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
					sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					break;
				case 2:
					row = getPrimaryCell(i, rowCount, sheet, workbook);
					cell = row.getCell(0);
					cell2 = row.getCell(3);
					rowCount = row.getRowNum();
					cell.setCellValue("Plan Series:");
					cell2.setCellValue(product.getSeries());
//					cell = row.createCell(9);
//					cell.setCellStyle(helperText);
//					cell.setCellValue("This explains , what segregation within Product , we are having, which defines different coverages");
					sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
					sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					break;
				case 3:
					if(product.getProductName().contains(Products.WORKSITE_DISABILITY.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						cell.setCellValue("Coverage Level:");
						cell2.setCellValue(product.getCoverageLevel());
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				case 4:
					row = getPrimaryCell(i, rowCount, sheet, workbook);
					cell = row.getCell(0);
					cell2 = row.getCell(3);
					rowCount = row.getRowNum();
					cell.setCellValue("Application Number:");
					cell2.setCellValue(product.getApplicationNumber());
					cell = row.createCell(9);
					cell.setCellStyle(helperText);
					cell.setCellValue("Please note that some states require Application Numbers to be presented at time of enrollment");
					sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
					sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					break;
				case 5:
					row = getPrimaryCell(i, rowCount, sheet, workbook);
					cell = row.getCell(0);
					cell2 = row.getCell(3);
					rowCount = row.getRowNum();
					cell.setCellValue("Pre/Post Tax:");
					cell2.setCellValue(product.getTaxType());
					sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 4, 8));
					cell2 = row.createCell(4);
					cell2.setCellStyle(fieldLable);
					cell2.setCellValue("This information is required for the EDI file transmission.");
					cell = row.createCell(9);
					cell.setCellStyle(helperText);
					cell.setCellValue("This information will need to be passed to Aflac on the EDI File.");
					sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
					sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					break;
				case 6:
					row = getPrimaryCell(i, rowCount, sheet, workbook);
					cell = row.getCell(0);
					rowCount = row.getRowNum();
					cell.setCellValue("Eligibility and Participation Requirements");
					cell = row.createCell(9);
					cell.setCellStyle(helperText);
					cell.setCellValue("Minimum Requirement for participation");
					sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
					sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					break;
				case 7:
					if(product.getProductName().contains(Products.WHOLE_LIFE.getValue()) || product.getProductName().contains(Products.WORKSITE_DISABILITY.getValue()) || product.getProductName().contains(Products.TERM_LIFE.getValue()) || product.getProductName().contains(Products.TERM_TO_120.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						cell.setCellValue("# of Eligible Employee:");
						cell2.setCellValue(caseForm.getAccountInformation().getEligibleEmployees());
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				case 8:
					row = getPrimaryCell(i, rowCount, sheet, workbook);
					cell = row.getCell(0);
					//cell2 = row.getCell(3);
					rowCount = row.getRowNum();
					cell.setCellValue("Minimum Hours Worked per Week:");
//					cell2.setCellValue(product.getHoursWorkedPerWeek());
					sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
					sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					break;
				case 9:
					row = getPrimaryCell(i, rowCount, sheet, workbook);
					cell = row.getCell(0);
					//cell2 = row.getCell(3);
					rowCount = row.getRowNum();
					row.setHeightInPoints((float) 32.5);
					cell.setCellValue("Eligibility Waiting Period: \n First Available Effective Date from Date of Hire");
//					cell2.setCellValue(product.getEligibilityWaitingPeriod());
//					cell = row.createCell(9);
//					cell.setCellStyle(helperText);
//					cell.setCellValue("Minimum time to get Eligible in the Plan");
					sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
					sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					break;
				case 10:
					row = getPrimaryCell(i, rowCount, sheet, workbook);
					cell = row.getCell(0);
					cell2 = row.getCell(3);
					rowCount = row.getRowNum();
					cell.setCellValue("Are Domestic Partners Eligible?");
					cell2.setCellValue(caseForm.getAccountInformation().getPartnerEligible());
					sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
					sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					break;
				case 11:
					if(product.getProductName().contains(Products.CRITICAL_ILLNESS.getValue())
							|| product.getProductName().contains(Products.WHOLE_LIFE.getValue())
							|| product.getProductName().contains(Products.WORKSITE_DISABILITY.getValue())
							|| product.getProductName().contains(Products.TERM_LIFE.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						row.setHeightInPoints((float) 29.3);
						cell.setCellValue("Age is based on:\n *Platform Driven");
						cell2.setCellValue(product.getPlatformDriven());
						
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				case 12:
					if(product.getProductName().contains(Products.CRITICAL_ILLNESS.getValue())
							|| product.getProductName().contains(Products.WHOLE_LIFE.getValue())
							|| product.getProductName().contains(Products.TERM_LIFE.getValue())
							|| product.getProductName().contains(Products.TERM_TO_120.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						cell.setCellValue("Age is calculated:");
						cell2.setCellValue(product.getAgeCalculated());
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;	
				case 13:
					row = getPrimaryCell(i, rowCount, sheet, workbook);
					cell = row.getCell(0);
					cell2 = row.getCell(3);
					rowCount = row.getRowNum();
					cell.setCellValue("Employee Issue Age:");
					cell2.setCellValue(product.getEmployeeIssueAge());
					cell = row.createCell(9);
					cell.setCellStyle(helperText);
					cell.setCellValue("Age at the time of Enrollment");
					sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
					sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					break;
				case 14:
					if(!product.getProductName().contains(Products.WORKSITE_DISABILITY.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						cell.setCellValue("Spouse Issue Age:");
//						cell = row.createCell(9);
//						cell.setCellStyle(helperText);
//						cell.setCellValue("Age at the time of Enrollment");
						cell2.setCellValue(product.getSpouseIssueAge());
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				case 15:
					if(!product.getProductName().contains(Products.WORKSITE_DISABILITY.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						cell.setCellValue("Child Issue Age:");
//						cell = row.createCell(9);
//						cell.setCellStyle(helperText);
//						cell.setCellValue("Age rating based on CED / App Sign Date");
						cell2.setCellValue(product.getChildIssueAge());
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				case 16:
					if(product.getProductName().contains(Products.CRITICAL_ILLNESS.getValue()) || product.getProductName().contains(Products.WORKSITE_DISABILITY.getValue()) || product.getProductName().contains(Products.TERM_TO_120.getValue()) || product.getProductName().contains(Products.WHOLE_LIFE.getValue()) || product.getProductName().contains(Products.TERM_LIFE.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						cell.setCellValue("Age Calculation:");
						if(product.getProductName().contains(Products.WHOLE_LIFE.getValue()) || product.getProductName().contains(Products.TERM_LIFE.getValue())) {
							cell2.setCellValue("Issue Age");
						}else {
							cell2.setCellValue(product.getAgeCalculation());
						}
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				case 17:
					if(product.getProductName().contains(Products.CRITICAL_ILLNESS.getValue()) || product.getProductName().contains(Products.WORKSITE_DISABILITY.getValue()) || product.getProductName().contains(Products.TERM_TO_120.getValue())) {
						if(product.getIssueAgeType()!=null) {
							row = getPrimaryCell(i, rowCount, sheet, workbook);
							cell = row.getCell(0);
							cell2 = row.getCell(3);
							rowCount = row.getRowNum();
							cell.setCellValue("Issue Age Type: ");
							cell2.setCellValue(product.getIssueAgeType());
							sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
							sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
						}
					}else {
						if(product.getProductName().contains(Products.WHOLE_LIFE.getValue()) || product.getProductName().contains(Products.TERM_LIFE.getValue())) {
							row = getPrimaryCell(i, rowCount, sheet, workbook);
							cell = row.getCell(0);
							cell2 = row.getCell(3);
							rowCount = row.getRowNum();
							cell.setCellValue("Issue Age Type: ");
							cell2.setCellValue("Stacked");
							sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
							sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
						}
					}
					break;	
				case 18:
					if(!product.getProductName().contains(Products.ACCIDENT.getValue()) && !product.getProductName().contains(Products.HOSPITAL_INDEMNITY.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						cell.setCellValue("Participation Requirement:");
						cell2.setCellValue(product.getParticipationRequirement());
						cell = row.createCell(9);
						cell.setCellStyle(helperText);
						cell.setCellValue("Minimum requirements to establish Guaranteed Issue for the product.");
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				case 19:
					if (product.getProductName().contains(Products.CRITICAL_ILLNESS.getValue())
						|| product.getProductName().contains(Products.TERM_LIFE.getValue())
						|| product.getProductName().contains(Products.WORKSITE_DISABILITY.getValue())
						|| product.getProductName().contains(Products.WHOLE_LIFE.getValue())
						|| product.getProductName().contains(Products.TERM_TO_120.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						row.setHeightInPoints((float) 34.5);
						cell.setCellValue("Benefit Amounts for Enrollment:");
						cell2.setCellStyle(borderStyle);
						cell2.setCellValue("Benefit Amount \r\nMinimum Amount");
						cell2 = row.createCell(4);
						cell2.setCellStyle(borderStyle);
						cell2.setCellValue("Benefit Amount \r\nMaximum Amount");
						cell2 = row.createCell(5);
						cell2.setCellStyle(borderStyle);
						cell2.setCellValue("Benefit Amount \r\nIncrements");
						cell2 = row.createCell(6);
						cell2.setCellStyle(borderStyle);
						cell2.setCellValue("Guaranteed Issue \r\nMaximum");
						
						Row row2 = sheet.createRow(++rowCount);
						sheet.addMergedRegion(new CellRangeAddress(row2.getRowNum(), row2.getRowNum(), 0, 2));
						row2.setHeightInPoints((float) 30);
						cell = row2.createCell(0);
						cell.setCellStyle(ExcelSheetStyling.getCellStyle(workbook,
								ExcelSheetStyling.getFont(workbook, IndexedColors.DARK_BLUE.getIndex(), (short) 11,
										false, false, Font.U_NONE),
								null, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, false));
						if(!product.getProductName().contains(Products.WORKSITE_DISABILITY.getValue())) {
							cell.setCellValue("Employee");
							cell2 = row2.createCell(3);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " + product.getEmployeeAmountOffered().getAmountOffered().getMinimumAmtElect());
							cell2 = row2.createCell(4);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getEmployeeAmountOffered().getAmountOffered().getMaximumAmtElect());
							cell2 = row2.createCell(5);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getEmployeeAmountOffered().getAmountOffered().getIncrements());
							cell2 = row2.createCell(6);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getEmployeeAmountOffered().getAmountOffered().getGuaranteedIssueMaximum());
							Row row3 = sheet.createRow(++rowCount);
							sheet.addMergedRegion(new CellRangeAddress(row3.getRowNum(), row3.getRowNum(), 0, 2));
							cell = row3.createCell(0);
							cell2 = row3.createCell(3);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getSpouseAmountOffered().getAmountOffered().getMinimumAmtElect());
							cell2 = row3.createCell(4);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getSpouseAmountOffered().getAmountOffered().getMaximumAmtElect());
							cell2 = row3.createCell(5);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getSpouseAmountOffered().getAmountOffered().getIncrements());
							cell2 = row3.createCell(6);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getSpouseAmountOffered().getAmountOffered().getGuaranteedIssueMaximum());
							row3.setHeightInPoints((float) 40);
							cell.setCellStyle(ExcelSheetStyling.getCellStyle(workbook,
									ExcelSheetStyling.getFont(workbook, IndexedColors.DARK_BLUE.getIndex(), (short) 11,
											false, false, Font.U_NONE),
									null, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, false));
							cell.setCellValue("Spouse");
							
							if(product.getProductName().contains(Products.WHOLE_LIFE.getValue())) {
								row3 = sheet.createRow(++rowCount);								
								cell = row3.createCell(3);
								sheet.addMergedRegion(new CellRangeAddress(row3.getRowNum(), row3.getRowNum(), 3, 8));
								cell.setCellStyle(fieldLable);
								cell.setCellValue("Child Term Rider is a flat face amount purchase");
								sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
								sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
																
								row = getPrimaryCell(i, rowCount, sheet, workbook);
								cell = row.getCell(0);
								cell2 = row.getCell(3);
								rowCount = row.getRowNum();
								cell.setCellValue("Child Coverage:");
								if(product.getIsChildCoverageOffered() != null && product.getIsChildCoverageOffered().equalsIgnoreCase("Y"))
									cell2.setCellValue(product.getChildCoverageOffered());
								else
									cell2.setCellValue("No Child Coverage offered");
																
								if(product.getIsChildCoverageOffered() != null && product.getIsChildCoverageOffered().equalsIgnoreCase("Y")) {
									sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
									sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
									row3 = sheet.createRow(++rowCount);
									sheet.addMergedRegion(new CellRangeAddress(row3.getRowNum(), row3.getRowNum(), 0, 8));
									cell = row3.createCell(0);
									cell.setCellStyle(ExcelSheetStyling.getCellStyle(workbook,
											ExcelSheetStyling.getFont(workbook, IndexedColors.DARK_BLUE.getIndex(), (short) 9,
													false, true, Font.U_NONE), null, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true));
									cell.setCellValue("Child Term Life Rider is not considered a separate policy and has $10,000 blanket coverage with Issue Ages of 16 days 26 years of age");								
								}
							}
							else if(product.getProductName().contains(Products.TERM_LIFE.getValue()) || product.getProductName().contains(Products.TERM_TO_120.getValue())) {
								row3 = sheet.createRow(++rowCount);
								sheet.addMergedRegion(new CellRangeAddress(row3.getRowNum(), row3.getRowNum(), 0, 2));
								cell = row3.createCell(0);
								cell2 = row3.createCell(3);
								cell2.setCellStyle(borderStyle);
								cell2.setCellValue("$ " +product.getChildAmountOffered().getAmountOffered().getMinimumAmtElect());
								cell2 = row3.createCell(4);
								cell2.setCellStyle(borderStyle);
								cell2.setCellValue("$ " +product.getChildAmountOffered().getAmountOffered().getMaximumAmtElect());
								cell2 = row3.createCell(5);
								cell2.setCellStyle(borderStyle);
								cell2.setCellValue("$ " +product.getChildAmountOffered().getAmountOffered().getIncrements());
								cell2 = row3.createCell(6);
								cell2.setCellStyle(borderStyle);
								cell2.setCellValue("$ " +product.getChildAmountOffered().getAmountOffered().getGuaranteedIssueMaximum());
								row3.setHeightInPoints((float) 40);
								cell.setCellStyle(ExcelSheetStyling.getCellStyle(workbook,
										ExcelSheetStyling.getFont(workbook, IndexedColors.DARK_BLUE.getIndex(), (short) 11,
												false, false, Font.U_NONE),
										null, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, false));
								cell.setCellValue("Child");
							}
						}
						else {
							cell.setCellValue("Employee");
							cell2 = row2.createCell(3);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " + product.getEmployeeAmountOffered().getAmountOffered().getMinimumAmtElect());
							cell2 = row2.createCell(4);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getEmployeeAmountOffered().getAmountOffered().getMaximumAmtElect());
							cell2 = row2.createCell(5);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getEmployeeAmountOffered().getAmountOffered().getIncrements());
							cell2 = row2.createCell(6);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getEmployeeAmountOffered().getAmountOffered().getGuaranteedIssueMaximum());
							Row row3 = sheet.createRow(++rowCount);
							sheet.addMergedRegion(new CellRangeAddress(row3.getRowNum(), row3.getRowNum(), 0, 2));
							cell = row3.createCell(0);
							cell.setCellStyle(ExcelSheetStyling.getCellStyle(workbook,
									ExcelSheetStyling.getFont(workbook, IndexedColors.DARK_BLUE.getIndex(), (short) 11,
											false, false, Font.U_NONE),
									null, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, false));
							cell.setCellValue("TDI State:");
							cell2 = row3.createCell(3);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getTdiStateAmountOffered().getAmountOffered().getMinimumAmtElect());
							cell2 = row3.createCell(4);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getTdiStateAmountOffered().getAmountOffered().getMaximumAmtElect());
							cell2 = row3.createCell(5);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getTdiStateAmountOffered().getAmountOffered().getIncrements());
							cell2 = row3.createCell(6);
							cell2.setCellStyle(borderStyle);
							cell2.setCellValue("$ " +product.getTdiStateAmountOffered().getAmountOffered().getGuaranteedIssueMaximum());
							
							row = sheet.createRow(++rowCount);
							cell2 = row.createCell(3);
							sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 3, 8));
							cell2.setCellStyle(fieldLable);
							cell2.setCellValue("TDI States are residents of:...");
						}
						if(product.getProductName().contains(Products.CRITICAL_ILLNESS.getValue())) {
							row = sheet.createRow(++rowCount);
							cell2 = row.createCell(3);
							sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 3, 8));
							cell2.setCellStyle(fieldLable);
							cell2.setCellValue("Spouse face amounts are avalaible at " + product.getBenefitAmountPercentage() + " of the employee amount.");
	
							row = sheet.createRow(++rowCount);
							row.setHeightInPoints((float) 24.7);
							cell2 = row.createCell(3);
							sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 3, 8));
							cell2.setCellStyle(fieldLable);
							cell2.setCellValue("Child(ren) are automatically covered at 50% of the employee election. No election data or plan build is required for Child coverage.");
							
							List<BenefitAmount> ee = product.getEeBenefitAmounts().getBenefitAmount();
							List<BenefitAmount> se = product.getSpBenefitAmounts().getBenefitAmount();
							if(ee.get(0).getAmount() != 0 || se.get(0).getAmount() != 0) {
								row2 = sheet.createRow(++rowCount);
								row2.setHeightInPoints((float) 34.7);
								cell = row2.createCell(0);
								cell.setCellStyle(rowLables);
								cell.setCellValue("Benefit Amounts for Selection:");
								cell2 = row2.createCell(3);
								cell2.setCellStyle(borderStyle);
								cell2.setCellValue("EE Benefit Amount");
								cell2 = row2.createCell(4);
								cell2.setCellStyle(borderStyle);
								cell2.setCellValue("SP Benefit Amount");
								int eeRowCount = rowCount;
								int seRowCount = rowCount;
								if(ee.get(0).getAmount() != 0) {
									for(BenefitAmount ba : ee) {
										row2 = sheet.createRow(++eeRowCount);
										cell2 = row2.createCell(3);
										cell2.setCellStyle(borderStyle);
										cell2.setCellValue("$ " + ba.getAmount());
									}
								}
								if(se.get(0).getAmount() != 0) {
									for(BenefitAmount ba : se) {
										row2 = sheet.getRow(++seRowCount);
										if(row2 == null)
											row2 = sheet.createRow(seRowCount);
										cell2 = row2.createCell(4);
										cell2.setCellStyle(borderStyle);
										cell2.setCellValue("$ " + ba.getAmount());
									}
								}
								rowCount = eeRowCount > seRowCount ? eeRowCount : seRowCount;
							}
						}
						
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				case 20:
					if(product.getProductName().contains(Products.WORKSITE_DISABILITY.getValue())
							|| product.getProductName().contains(Products.WHOLE_LIFE.getValue())
							|| product.getProductName().contains(Products.TERM_LIFE.getValue())
							|| product.getProductName().contains(Products.TERM_TO_120.getValue())) {
						if(product.getBenefitAmountDescription() != null) {
							row = getPrimaryCell(i, rowCount, sheet, workbook);
							cell = row.getCell(0);
							cell2 = row.getCell(3);
							rowCount = row.getRowNum();
							cell.setCellValue("Benefit Amount Description:");
							cell2.setCellValue(product.getBenefitAmountDescription());
							sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
							sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
						}
					}
					break;
				case 21:
					if(product.getProductName().contains(Products.WORKSITE_DISABILITY.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						cell.setCellValue("Benefit Type:");
						cell2.setCellValue(product.getBenefitType());
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					} 
					else if(product.getProductName().contains(Products.CRITICAL_ILLNESS.getValue())
							|| product.getProductName().contains(Products.WHOLE_LIFE.getValue())
							|| product.getProductName().contains(Products.TERM_LIFE.getValue())
							|| product.getProductName().contains(Products.TERM_TO_120.getValue())) {
						if(product.getProductName().contains(Products.TERM_LIFE.getValue())) {
							row = sheet.createRow(++rowCount);
							row.setHeightInPoints((float) 16.4);
							cell = row.createCell(0);
							cell2 = row.createCell(3);
							sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 2));
							sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 3, 8));
							cell.setCellStyle(rowLables);
							cell.setCellValue("Term:");
							cell2.setCellStyle(fieldLable);
							cell2.setCellValue(product.getTerm());
							sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
							sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
						}
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						cell.setCellValue("Tobacco Status:");
						cell2.setCellValue(product.getTobacco().getTobaccoStatus());
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				case 22:
					if(product.getProductName().contains(Products.WORKSITE_DISABILITY.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 3, 4));
						cell.setCellValue("Benefit Period:");
						cell2.setCellValue(product.getBenefitPeriod());
						
						cell = row.createCell(5);
						cell2 = row.createCell(7);
						sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 5, 6));
						sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 7, 8));
						cell.setCellStyle(rowLables);
						cell.setCellValue("Elimination Period(s):");
						cell2.setCellStyle(fieldLable);
						cell2.setCellValue(product.getEliminationPeriod());
						
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					} 
					else if(product.getProductName().contains(Products.CRITICAL_ILLNESS.getValue())
							|| product.getProductName().contains(Products.WHOLE_LIFE.getValue())
							|| product.getProductName().contains(Products.TERM_LIFE.getValue())
							|| product.getProductName().contains(Products.TERM_TO_120.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						cell.setCellValue("Tobacco Status Determind:");
						cell2.setCellStyle(fieldLable);
						cell2.setCellValue(product.getTobacco().getTobaccoStatusDetermined());
						
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				case 23:
					if(product.getOtherInstructions() != null && !product.getOtherInstructions().isEmpty()) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						cell.setCellValue("Other Instructions:");
						cell2.setCellValue(product.getOtherInstructions());
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				case 24:
					if(!affirmations.isEmpty() || !questions.isEmpty()) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						rowCount = row.getRowNum();
						if(affirmations.size() >= 1) {
							cell.setCellValue("Eligibility Statements");
						}
						if(questions.size() >= 1) {
							cell.setCellValue("Eligibility  Questions");
						}
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				case 25:
					row = getPrimaryCell(i, rowCount, sheet, workbook);
					rowCount = row.getRowNum();
					rowCount = generateEligibilityStatements(complianceData, product, sheet, workbook, rowCount);
					
					sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
					sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					break;
				case 26:
					if (!healthQues.isEmpty()) {
						XSSFCellStyle rowLablesStatic = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,IndexedColors.BLACK.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,VerticalAlignment.TOP, true);
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						rowCount = row.getRowNum();
						cell.setCellValue("Health Questions");
						
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
						
						Row staticRow = sheet.createRow(++rowCount);
						Cell staticCell = staticRow.createCell(0);
						staticCell.setCellStyle(rowLablesStatic);
						rowCount = staticRow.getRowNum();
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
						if(product.getProductName().split("-")[0].equalsIgnoreCase(Products.HOSPITAL_INDEMNITY.getValue())) {
							staticCell.setCellValue("The following questions must be asked, and responses will be required on the enrollment file.");
						}
						else {
							staticRow.setHeightInPoints((float) 50);
							staticCell.setCellValue("If the plan has a participation requirement included in the underwriting offer (example 10%), the following questions must be asked for all elections.\r\n"
						+"If there is no participation requirement the following questions must be asked for benefit amounts enrolled exceeding the guaranteed issue limits.");
						}
						
					}
					break;
				case 27:
					if (!healthQues.isEmpty()) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						rowCount = row.getRowNum();
						rowCount = generateComplianceData(healthQues, sheet, workbook, rowCount);
						
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				case 28:
					if (!disclosures.isEmpty() && !excelCase.equalsIgnoreCase(ComplianceCase.WITHOUTCOMPLIANCE.getValue())) {
						XSSFCellStyle rowLablesStatic = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,IndexedColors.BLACK.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,VerticalAlignment.TOP, true);
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						//cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						cell.setCellValue("Compliance Language To Post");
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
						
						Row staticRow = sheet.createRow(++rowCount);
						Cell staticCell = staticRow.createCell(0);
						staticCell.setCellStyle(rowLablesStatic);
						staticCell.setCellValue("The following language must be presented on the enrollment site at time of election:");
					}
					else if (!disclosures.isEmpty() && excelCase.equalsIgnoreCase(ComplianceCase.WITHOUTCOMPLIANCE.getValue())) {
						XSSFCellStyle rowLablesStatic = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,IndexedColors.BLACK.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,VerticalAlignment.TOP, true);
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						cell = row.getCell(0);
						//cell2 = row.getCell(3);
						rowCount = row.getRowNum();
						cell.setCellValue("Compliance Language To Post");
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
						
						Row staticRow = sheet.createRow(++rowCount);
						Cell staticCell = staticRow.createCell(0);
						staticCell.setCellStyle(rowLablesStatic);
						staticCell.setCellValue("The following language must be presented on the enrollment site at time of election:");
					}
					break;	
				case 29:
					if (!disclosures.isEmpty() && !excelCase.equalsIgnoreCase(ComplianceCase.WITHOUTCOMPLIANCE.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						rowCount = row.getRowNum();
						rowCount = generateComplianceData(disclosures, sheet, workbook, rowCount);
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					else if (!disclosures.isEmpty() && excelCase.equalsIgnoreCase(ComplianceCase.WITHOUTCOMPLIANCE.getValue())) {
						row = getPrimaryCell(i, rowCount, sheet, workbook);
						rowCount = row.getRowNum();
						rowCount = generateComplianceData(disclosures, sheet, workbook, rowCount);
						sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 8));
					}
					break;
				default:
					log.info("no data found..");
				}
			}
			for (int i = 0; i < 10; i++)
				sheet.autoSizeColumn(i, false);
		}
		
		if(excelCase.equalsIgnoreCase(ComplianceCase.WITHOUTCOMPLIANCE.getValue())) {
			PdfDirectionSheet  = workbook.createSheet("C-Language PDF Directions");
			getPdfDirectionSheet(workbook, PdfDirectionSheet, caseForm.getAccountInformation());
		}
		return workbook;
	}
	
	public void getPdfDirectionSheet(XSSFWorkbook workbook, XSSFSheet PdfDirectionSheet,AccountInformation accountInformation) throws IOException {
		Row row;
		Cell cell;
		int rowCount = 2;
		CellStyle style=null;
		Font font;

		ExcelSheetStyling.getAflacFreezPane(workbook, PdfDirectionSheet);
		for (int i = 0; i < 3; i++) {
			
			switch (i) {
			case 0:
				row = getPrimaryCellForPdfDirectionSheet(i, rowCount, PdfDirectionSheet, workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Compliance Language PDF Directions:");
				break;
			case 1:
				font= ExcelSheetStyling.getFont(workbook,IndexedColors.BLACK.getIndex(), (short) 9, true, false, Font.U_NONE);
				row = getPrimaryCellForPdfDirectionSheet(i, rowCount, PdfDirectionSheet, workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
//				cell.setCellValue("Please add a link to the attached PDF in the enrollment flow, above the signature line, with the following language noting its importance. \r\n"
//				+"Please click here for important enrollment disclosures and agreements.");
				cell.setCellValue(accountInformation.getPdfDirections());
				row.setHeightInPoints(35);
				style = cell.getCellStyle();
				style.setFont(font);
				break;
			case 2:
				row = getPrimaryCellForPdfDirectionSheet(i, rowCount, PdfDirectionSheet, workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("NOTE -This should be posted one time and is applicable for the entire offering.  Do not post the PDF for every product.");
				row.setHeightInPoints(35);
				break;	
			default:
				log.info("no data found..");
			}
			PdfDirectionSheet.createRow(++rowCount).setHeightInPoints((float) 8.2);	
		}
		PdfDirectionSheet.autoSizeColumn(0);
	}

	private Row getPrimaryCell(int i, int rowCount, XSSFSheet sheet, XSSFWorkbook workbook) {
		Row row = sheet.createRow(++rowCount);
		Cell cell = row.createCell(0);
		Cell cell2 = row.createCell(3);
		
		XSSFCellStyle headerLable = ExcelSheetStyling.getHeaderLableStyle(workbook);
		XSSFCellStyle rowLables = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,
				IndexedColors.DARK_BLUE.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,
				VerticalAlignment.TOP, true);
		XSSFCellStyle fieldLable = ExcelSheetStyling.getfieldLableStyle(workbook, sheet,false);
		if (i == 0 || i == 6 || i == 24 || i == 26 || i == 28) {
			cell.setCellStyle(headerLable);
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
		} else if (i < 23) {
			cell.setCellStyle(rowLables);
			cell2.setCellStyle(fieldLable);
			row.setHeightInPoints((float) 16.4);
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 2));
			if (i != 5 && i != 19 && i != 22)
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 3, 8));
		}
		
		return row;
	}
	

	private void getAccountInfoSheet(XSSFWorkbook workbook, XSSFSheet sheet, AccountInformation account)
			throws IOException {
		Row row;
		Cell cell,cell2;
		int rowCount = 2;

		ExcelSheetStyling.getAflacFreezPane(workbook, sheet);
		//XSSFCellStyle headerLable = ExcelSheetStyling.getHeaderLableStyle(workbook);
		XSSFCellStyle fieldLable = ExcelSheetStyling.getfieldLableStyle(workbook, sheet, false);
		XSSFCellStyle helperText = ExcelSheetStyling.getHelperTextStyle(workbook);
		XSSFCellStyle rowLables = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,
				IndexedColors.DARK_BLUE.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,
				VerticalAlignment.TOP, true);
		
		int mergStartRow = 0;
		for (int i = 0; i < 24; i++) {
//			Row row = sheet.createRow(++rowCount);
//			row.setHeightInPoints((float) 16.4);
//
//			Cell cell = row.createCell(0);
//			Cell cell2 = row.createCell(1);
//
//			if (i == 0 || i == 8 || i == 12 || i == 15) {
//				cell.setCellStyle(headerLable);
//				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
//			} else {
//				cell.setCellStyle(rowLables);
//				cell2.setCellStyle(fieldLable);
//			}
			
			switch (i) {
			
			case 0:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("PURPOSE");
				break;
			
			case 1:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
				cell.setCellValue("The purpose of this build requirements document is to provide all product eligibility guidelines, underwriting rules and required site language for the enrollment site configuration.");
				cell.setCellStyle(fieldLable);
				break;
			
			case 2:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Account Information");
				break;
			case 3:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Group #:");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 2));
				cell2=row.getCell(1);
				cell2.setCellValue(account.getGroupNumber());
				break;
				
			case 4:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Group Type:");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 2));
				cell2=row.getCell(1);
				cell2.setCellValue(account.getGroupType());
				break;	
			case 5:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Account Name:");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 4));
				cell2=row.getCell(1);
				cell2.setCellValue(account.getAccountName());
				cell = row.createCell(5);
				cell2 = row.createCell(6);
				cell2.setCellStyle(fieldLable);
				cell.setCellValue("Situs State:");
				cell2.setCellValue(account.getSitusState());
				break;
			case 6:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Employee Identification Method:");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 2));
				cell2=row.getCell(1);
				cell2.setCellValue(account.getSsn());
				cell = row.createCell(3);
				for(int c=3; c<9; c++) {
					row.createCell(c).setCellStyle(helperText);
				}
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 3, 8));
				row.setHeightInPoints((float) 30.5);
				cell.setCellValue("This is the unique identification number that should be used to capture employee or member enrollment and be passed on the enrollment file for policy issuance");
				break;
			case 7:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Deduction Frequency:");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 6));
				cell2=row.getCell(1);
				cell2.setCellValue(account.getDeductionFrequency());
//				cell = row.createCell(3);
//				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 3, 8));
//				row.setHeightInPoints((float) 30.5);
//				for(int c=3; c<9; c++) {
//					row.createCell(c).setCellStyle(helperText);
//				}
//				cell.setCellValue("At What Frequency, Deduction would happen? \n Monthly, Biweekly, Semi-Monthly, Weekly or Other");
				break;
			case 8:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Platform/Software");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 6));
				cell2=row.getCell(1);
				cell2.setCellValue(account.getPlatformSplitValue());
				break;
			case 9:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				row.setHeight((short)-1);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Products Enrolled on Platform");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 6));
				cell2=row.getCell(1);
				cell2.setCellValue(account.getProductSold());
				break;
			case 10:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Important Dates:");
				break;
			case 11:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("1st Coverage Effective Date on Platform:");
				cell2=row.getCell(1);
				cell2.setCellValue(account.getCoverageBillingEffDate());
				break;
			case 12:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Enrollment Event Start Date:");
				cell2=row.getCell(1);
				cell2.setCellValue(account.getOpenEnrollmentStartDate());
				cell = row.createCell(5);
				cell2 = row.createCell(6);
				cell2.setCellStyle(fieldLable);
				cell.setCellValue("Enrollment Event End Date:");
				cell.setCellStyle(rowLables);
				cell2.setCellValue(account.getOpenEnrollmentEndDate());
				break;
			case 13:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				row.setHeightInPoints((float) 110);
				cell.setCellValue("Rates & Compliance Review:");
				cell2=row.getCell(1);
				cell2.setCellValue("Aflac will require the opportunity to confirm that the system configuration follows the guidelines set forth in this requirements document. This includes validation of the rates built in the system, and a compliance review of the site language.  When the build is complete, the following may be emailed to   AGElectronicSiteTesting@aflac.com to begin the review process:\r\n"
						+ "Test User Access – Log-in Username and Password\r\n"
						+ "           or\r\n"
						+ "Screenshots & Rate Export\r\n"
						+ "System screenshots of the Aflac Group enrollment experience. Including all language and questions. Screenshots of where the Product Brochures are housed/accessed, and the form codes on the bottom left corner of each brochure. \r\n"
						+ "Rate Export – excel extract of the Aflac Group rates that are configured in the system.");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 6));
				break;
			case 14:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Enrollment Conditions");
				break;
			case 15:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Enrollment Condition Options:");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 3));
				cell2=row.getCell(1);
				cell2.setCellValue(account.getEnrollmentConditionOptions());
//				cell = row.createCell(4);
//				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 4, 8));
//				cell.setCellStyle(helperText);
//				for(int c=4; c<9; c++) {
//					row.createCell(c).setCellStyle(helperText);
//				}
//				cell.setCellValue("Expectations for File Selection");
				break;
			case 16:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("New Hires / Ongoing enrollment:");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 3));
				cell2=row.getCell(1);
				cell2.setCellValue(account.getNewHire());
				break;
			case 17:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Aflac Group Contacts:");
				break;
			case 18:
				if(account.getImplementationManager()!=null&& !account.getImplementationManager().isEmpty()) {
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Implementation Manager Name: ");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 5));
				cell2=row.getCell(1);
				cell2.setCellValue(account.getImplementationManager());
				mergStartRow = row.getRowNum();
				if(account.getImplementationManagerEmail()==null || account.getImplementationManagerEmail().isEmpty()) {
					row.setHeightInPoints((float) 25);
					cell = sheet.getRow(mergStartRow).createCell(6);
					sheet.addMergedRegion(new CellRangeAddress(mergStartRow, row.getRowNum(), 6, 8));
					for(int c=6; c<9; c++) {
						sheet.getRow(mergStartRow).createCell(c).setCellStyle(helperText);
						row.createCell(c).setCellStyle(helperText);
					}
					cell.setCellValue("Contact for Implementation-related queries");
				}
			}else {
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				mergStartRow = row.getRowNum();
			}
				break;
			case 19:
				if(account.getImplementationManagerEmail()!=null&& !account.getImplementationManagerEmail().isEmpty()) {
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				if(account.getImplementationManager()==null || account.getImplementationManager().isEmpty()) {
					row.setHeightInPoints((float) 25);
				}
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Implementation Manager Email: ");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 5));
				cell2=row.getCell(1);
				cell2.setCellValue(account.getImplementationManagerEmail());
				sheet.addMergedRegion(new CellRangeAddress(mergStartRow, row.getRowNum(), 6, 8));
				cell = sheet.getRow(mergStartRow).createCell(6);
				for(int c=6; c<9; c++) {
					sheet.getRow(mergStartRow).createCell(c).setCellStyle(helperText);
					row.createCell(c).setCellStyle(helperText);
				}
				cell.setCellValue("Contact for Implementation-related queries");
			}
				break;
			case 20:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Client Manager Name: ");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 5));
				cell2=row.getCell(1);
				cell2.setCellValue(account.getClientManager());
				mergStartRow = row.getRowNum();
				break;
			case 21:
				row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Client Manager Email: ");
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 5));
				cell2=row.getCell(1);
				cell2.setCellValue(account.getClientManagerEmail());
				sheet.addMergedRegion(new CellRangeAddress(mergStartRow, row.getRowNum(), 6, 8));
				cell = sheet.getRow(mergStartRow).createCell(6);
				for(int c=6; c<9; c++) {
					sheet.getRow(mergStartRow).createCell(c).setCellStyle(helperText);
					row.createCell(c).setCellStyle(helperText);
				}
				cell.setCellValue("Contact for ongoing servicing-related queries");
				break;
			case 22:
				if(account.getPartnerPlatformManager()!=null&& !account.getPartnerPlatformManager().isEmpty()) {
					row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
					
					rowCount = row.getRowNum();
					cell=row.getCell(0);
					cell.setCellValue("Partner Platform Manager Name: ");
					sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 5));
					cell2=row.getCell(1);
					cell2.setCellValue(account.getPartnerPlatformManager());
					mergStartRow = row.getRowNum();
					if(account.getPartnerPlatformManagerEmail()==null || account.getPartnerPlatformManagerEmail().isEmpty()) {
						row.setHeightInPoints((float) 25);
						cell = sheet.getRow(mergStartRow).createCell(6);
						sheet.addMergedRegion(new CellRangeAddress(mergStartRow, row.getRowNum(), 6, 8));
						for(int c=6; c<9; c++) {
							sheet.getRow(mergStartRow).createCell(c).setCellStyle(helperText);
							row.createCell(c).setCellStyle(helperText);
						}
						cell.setCellValue("Contact for electronic enrollment setup-related queries");
					}
					
				}else {
					row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
					mergStartRow = row.getRowNum();
				}
				break;
			case 23:
				if(account.getPartnerPlatformManagerEmail()!=null&& !account.getPartnerPlatformManagerEmail().isEmpty()) {
					row = getPrimaryCellForAccountSheet(i,rowCount,sheet,workbook);
					if(account.getPartnerPlatformManager()==null || account.getPartnerPlatformManager().isEmpty()) {
						row.setHeightInPoints((float) 25);
					}
					rowCount = row.getRowNum();
					cell=row.getCell(0);
					cell.setCellValue("Partner Platform Manager Email: ");
					sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 5));
					cell2=row.getCell(1);
					cell2.setCellValue(account.getPartnerPlatformManagerEmail());
					cell = sheet.getRow(mergStartRow).createCell(6);
					sheet.addMergedRegion(new CellRangeAddress(mergStartRow, row.getRowNum(), 6, 8));
					for(int c=6; c<9; c++) {
						sheet.getRow(mergStartRow).createCell(c).setCellStyle(helperText);
						row.createCell(c).setCellStyle(helperText);
					}
					cell.setCellValue("Contact for electronic enrollment setup-related queries");
				}
				break;
			default:
				log.info("no data found..");
			}

			sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
		}
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(5);
	}

	public Row getPrimaryCellForAccountSheet(int i, int rowCount, XSSFSheet sheet, XSSFWorkbook workbook) {
		Row row = sheet.createRow(++rowCount);
		row.setHeightInPoints((float) 16.4);
		Cell cell = row.createCell(0);
		Cell cell2 = row.createCell(1);
		
		XSSFCellStyle headerLable = ExcelSheetStyling.getHeaderLableStyle(workbook);
		XSSFCellStyle rowLables = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,
				IndexedColors.DARK_BLUE.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,
				VerticalAlignment.TOP, true);
		XSSFCellStyle fieldLable = ExcelSheetStyling.getfieldLableStyle(workbook, sheet, false);
		
		if (i == 0 || i==2 || i == 10 || i == 14 || i == 17) {
			cell.setCellStyle(headerLable);
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
		} else {
			cell.setCellStyle(rowLables);
			cell2.setCellStyle(fieldLable);
		}
		
		return row;
		
	}

	private void getFillSubmissionSheet(XSSFWorkbook workbook, XSSFSheet sheet, 
			FileSubmission fileSubmission, CaseBuilderServiceImpl caseService)
			throws IOException {
		Row row;
		Cell cell,cell2;
		int rowCount = 2;

		ExcelSheetStyling.getAflacFreezPane(workbook, sheet);
		//XSSFCellStyle headerLable = ExcelSheetStyling.getHeaderLableStyle(workbook);
		//XSSFCellStyle fieldLable = ExcelSheetStyling.getfieldLableStyle(workbook, sheet, false);
		//XSSFCellStyle rowLables = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,
		//		IndexedColors.DARK_BLUE.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,
		//		VerticalAlignment.TOP, true);
		XSSFCellStyle helperText = ExcelSheetStyling.getHelperTextStyle(workbook);
		
		for (int i = 0; i < 6; i++) {
//			Row row = sheet.createRow(++rowCount);
//			row.setHeightInPoints((float) 16.4);
//
//			Cell cell = row.createCell(0);
//			Cell cell2 = row.createCell(1);
//
//			if (i == 0) {
//				cell.setCellStyle(headerLable);
//				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
//			} else {
//				cell.setCellStyle(rowLables);
//				cell2.setCellStyle(fieldLable);
//				if(i==1 || i==2 || i==3 || i==4 || i==5)
//					sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 8));
//				else
//					sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
//			}
			
			switch (i) {
			case 0:
				row = getPrimaryCellForFileSubmissionSheet(i, rowCount, sheet, workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("File Submission Guidelines");
				break;
			case 1:
				row = getPrimaryCellForFileSubmissionSheet(i, rowCount, sheet, workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Submission Method:");
				cell2=row.getCell(1);
				cell2.setCellValue("All files must be submitted via SFTP. \r\n"
						+ "If an existing SFTP profile is in place for an enrollment vendor the log in credentials are universal and will not be sent on a case by case basis. \r\n"
						+ "If an existing SFTP profile is not already established then one will be created, and log in credentials will be sent via secure email to the EDI Point of Contact on file.");
				row.setHeightInPoints((float) 50);
				break;
			case 2:
				row = getPrimaryCellForFileSubmissionSheet(i, rowCount, sheet, workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Production File Naming:");
				cell2=row.getCell(1);
				cell2.setCellValue(fileSubmission.getProductionFileNaming());
				break;
			case 3:
				row = getPrimaryCellForFileSubmissionSheet(i, rowCount, sheet, workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Test File Naming:");
				cell2=row.getCell(1);
				cell2.setCellValue(fileSubmission.getTestFileNaming());
				break;
			case 4:
				row = getPrimaryCellForFileSubmissionSheet(i, rowCount, sheet, workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);	
				cell.setCellValue("Production File Due Date:");
				cell2=row.getCell(1);
				if(fileSubmission.getProductionFileDueDate()!=null && !fileSubmission.getProductionFileDueDate().isEmpty()) {
					cell2.setCellValue(caseService.convertDateCba(fileSubmission.getProductionFileDueDate()));
				}else {
					cell2.setCellValue("TBD");
				}
				cell = row.createCell(9);
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 9, 15));
				row.setHeightInPoints((float) 50);
				for(int c=9; c<16; c++) {
					row.createCell(c).setCellStyle(helperText);
				}
				cell.setCellValue("As a standard, Aflac requests a Production File be received 15 days before the Coverage Effective Date. If the file is received at a later date, please note that this could result in delayed policy issuance.\r\n"
						+ "Please work with your Aflac team to develop an appropriate timeline for delivery.");
				break;
			case 5:
				row = getPrimaryCellForFileSubmissionSheet(i, rowCount, sheet, workbook);
				rowCount = row.getRowNum();
				cell=row.getCell(0);
				cell.setCellValue("Test File Due Date:");
				cell2=row.getCell(1);
				if(fileSubmission.getTestFileDueDate()!=null && !fileSubmission.getTestFileDueDate().isEmpty()) {
					cell2.setCellValue(caseService.convertDateCba(fileSubmission.getTestFileDueDate()));
				}else{
					cell2.setCellValue("TBD");
				}
				cell = row.createCell(9);
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 9, 15));
				row.setHeightInPoints((float) 50);
				for(int c=9; c<16; c++) {
					row.createCell(c).setCellStyle(helperText);
				}
				cell.setCellValue("As a standard, Aflac suggests providing a Test File 1 week into the open enrollment period; however, please work with your Aflac team to develop an appropriate timeline for delivery.");
				break;	
				
//			case 6:
//				cell.setCellValue("If file name is incomplete or not present, it will be provided a later date from your Aflac Team.");
//				break;
//			case 7:
//				cell.setCellValue("If the naming conventions above are not used, the files will not route to the correct folder and this will cause a delay in receipt and processing.");
//				break;
//			case 8:
//				cell.setCellValue("NOTE: Any questions or concerns on test files, please reach out to EnrollmentIntegrations@aflac.com.");
//				break;
			default:
				log.info("no data found..");
			}
			sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
		}
		sheet.autoSizeColumn(0);
	}

	
	public Row getPrimaryCellForFileSubmissionSheet(int i, int rowCount, XSSFSheet sheet, XSSFWorkbook workbook) {
		Row row = sheet.createRow(++rowCount);
		row.setHeightInPoints((float) 16.4);

		Cell cell = row.createCell(0);
		Cell cell2 = row.createCell(1);
		
		XSSFCellStyle headerLable = ExcelSheetStyling.getHeaderLableStyle(workbook);
		XSSFCellStyle fieldLable = ExcelSheetStyling.getfieldLableStyle(workbook, sheet, false);
		XSSFCellStyle rowLables = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,
				IndexedColors.DARK_BLUE.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,
				VerticalAlignment.TOP, true);

		if (i == 0) {
			cell.setCellStyle(headerLable);
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
		} else {
			cell.setCellStyle(rowLables);
			cell2.setCellStyle(fieldLable);
			if(i==1 || i==2 || i==3 || i==4 || i==5)
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 8));
			else
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
		}
		return row;
	}
	
	public Row getPrimaryCellForPdfDirectionSheet(int i, int rowCount, XSSFSheet sheet, XSSFWorkbook workbook) {
		Row row = sheet.createRow(++rowCount);
		row.setHeightInPoints((float) 16.4);

		Cell cell = row.createCell(0);
		
		XSSFCellStyle headerLable = ExcelSheetStyling.getHeaderLableStyle(workbook);
		XSSFCellStyle fieldLable = ExcelSheetStyling.getfieldLableStyle(workbook, sheet, false);
		XSSFCellStyle rowLables = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,
				IndexedColors.DARK_BLUE.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,
				VerticalAlignment.TOP, true);

		if (i == 0) {
			cell.setCellStyle(headerLable);
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
		}
		else if(i == 2) {
			cell.setCellStyle(rowLables);
			cell.setCellStyle(fieldLable);
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 5));
		}
		else {
			cell.setCellStyle(rowLables);
			cell.setCellStyle(fieldLable);
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
		}
		return row;
	}

	private void getLocationSheet(XSSFWorkbook workbook, XSSFSheet sheet, Locations location)
			throws IOException {

		int rowCount = 2;

		ExcelSheetStyling.getAflacFreezPane(workbook, sheet);
		XSSFCellStyle headerLable = ExcelSheetStyling.getHeaderLableStyle(workbook);
				
		Font font = ExcelSheetStyling.getFont(workbook,
				IndexedColors.DARK_BLUE.getIndex(), (short) 11, false, false, Font.U_NONE);
		
		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setVerticalAlignment(VerticalAlignment.TOP);
		style.setFont(font);
		style.setWrapText(true);
				
		Row row = sheet.createRow(++rowCount);
		row.setHeightInPoints((float) 16.4);
		Cell cell = row.createCell(0);
		cell.setCellStyle(headerLable);
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
		cell.setCellValue("Locations");
		sheet.createRow(++rowCount).setHeightInPoints((float) 8.2);
		
		row = sheet.createRow(++rowCount);
		row.setHeightInPoints((float) 16.4);
		cell = row.createCell(0);
		cell.setCellStyle(headerLable);
		cell.setCellValue("Location Name");
		cell = row.createCell(1);
		cell.setCellStyle(headerLable);
		cell.setCellValue("Location State");
		cell = row.createCell(2);
		cell.setCellStyle(headerLable);
		cell.setCellValue("Location Code");
		
		for (Location loc : location.getLocation()) {
			row = sheet.createRow(++rowCount);
			row.setHeightInPoints((float) 16.4);
			cell = row.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue(loc.getLocationName());
			cell = row.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue(loc.getLocationState());
			cell = row.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue(loc.getLocationCode());			
		}
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
	}
	
	public static CaseBuilderForm xmlToJava(String formXml) throws IOException {
		try {
			Document doc = DocumentUtil.convertToDocument(formXml);
			String caseBuild = DocumentUtil.getNode("/afData/afBoundData/caseBuilderForm", doc);
			InputStream stream = new ByteArrayInputStream(caseBuild.getBytes(StandardCharsets.UTF_8));
			JAXBContext context = JAXBContext.newInstance(CaseBuilderForm.class);
			Unmarshaller um = context.createUnmarshaller();
			CaseBuilderForm caseForm = (CaseBuilderForm) um.unmarshal(stream);
			return caseForm;
		} catch (JAXBException e) {
			log.error(e.getMessage());
		}
		return new CaseBuilderForm();
	}

	public static InputStream clone(final InputStream inputStream) throws IOException {
		if(inputStream != null) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				IOUtils.copy(inputStream, outputStream);
				return new ByteArrayInputStream(outputStream.toByteArray());
			} catch (IOException ex) {
				log.error("Input Stream Error: ", ex);
			} finally {
				outputStream.flush();
				inputStream.close();
			}
		}
		return null;
	}

	private int generateEligibilityStatements(ComplianceDataUtil complianceData, Product product, XSSFSheet sheet,
			XSSFWorkbook workbook, int rowCount) {
		XSSFCellStyle rowLables = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,
				IndexedColors.DARK_BLUE.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,
				VerticalAlignment.TOP, true);
				
		List<ComplianceDetail> affirmations = complianceData.getComplianceData(Labels.AFFIRMATION.getValue(), product.getProductName(),product.getProductId());
		
		if(affirmations.size() >= 1) {
			rowCount = addQuestionAffirmation(affirmations, product.getProductName(), Labels.AFFIRMATION.getValue(), sheet, rowLables, rowCount,workbook);
		}
		List<ComplianceDetail> questions = complianceData.getComplianceData(Labels.QUESTION.getValue(), product.getProductName(),product.getProductId());
		if(questions.size() >= 1) {
			rowCount = addQuestionAffirmation(questions, product.getProductName(), Labels.QUESTION.getValue(), sheet, rowLables, rowCount,workbook);
		}
		return rowCount;
	}
	
	private int addQuestionAffirmation(List<ComplianceDetail> statements, String product, String type, XSSFSheet sheet, XSSFCellStyle rowLables, int rowCount, XSSFWorkbook workbook) {
		
		Row row = sheet.getRow(rowCount);
		Cell cell = row.createCell(0);
		cell.setCellStyle(rowLables);
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
		row.setHeightInPoints((float) 34);
		cell.setCellStyle(rowLables);
		
		if(type.equalsIgnoreCase(Labels.AFFIRMATION.getValue())) {
			XSSFCellStyle rowLablesStatic = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,IndexedColors.BLACK.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,VerticalAlignment.TOP, true);
			cell.setCellValue("The following statement(s) must be inserted in the Compliance Language to Post that is shown below. ");
			cell.setCellStyle(rowLablesStatic);
		} else if(type.equalsIgnoreCase(Labels.QUESTION.getValue())) {
			XSSFCellStyle rowLablesStatic = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,IndexedColors.BLACK.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,VerticalAlignment.TOP, true);
			cell.setCellValue("The following question(s) must be asked for this product, and responses will be required on the enrollment file.");
			cell.setCellStyle(rowLablesStatic);
		}
		
		for(ComplianceDetail statement : statements) {
			row = sheet.createRow(++rowCount);
			cell = row.createCell(0);
			cell.setCellStyle(rowLables);
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
			row.setHeightInPoints((float) 46);
			cell.setCellValue(statement.getComplianceText());
		}
		sheet.createRow(++rowCount).setHeightInPoints((float) 12);
		return rowCount;
	}
	
	private int generateComplianceData(List<ComplianceDetail> disclosures, XSSFSheet sheet,
			XSSFWorkbook workbook, int rowCount) {
		XSSFCellStyle rowLables = ExcelSheetStyling.getCellStyle(workbook, ExcelSheetStyling.getFont(workbook,
				IndexedColors.DARK_BLUE.getIndex(), (short) 11, false, false, Font.U_NONE), null, null,
				VerticalAlignment.TOP, true);
		sheet.removeRow(sheet.getRow(rowCount--));			
		for(ComplianceDetail disclosure : disclosures) {
			Row row = sheet.createRow(++rowCount);
			Cell cell = row.createCell(0);
			cell.setCellStyle(rowLables);
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));
			row.setHeightInPoints((float) 46);
			cell.setCellValue(disclosure.getComplianceText());
		}
		return rowCount;
	}
}
