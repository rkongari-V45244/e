package com.aflac.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.aflac.core.osgi.service.CaseBuilderService;
import com.aflac.core.osgi.service.ComplianceService;
import com.aflac.core.osgi.service.GeneratePDFService;
import com.aflac.core.util.ComplianceCase;
import com.aflac.core.util.DocumentUtil;
import com.aflac.core.util.ExcelGenerationUtil;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/CaseBuilderToolSubmitServlet" })
public class CaseBuilderToolSubmitServlet extends SlingAllMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient Logger log = LoggerFactory.getLogger(CaseBuilderToolSubmitServlet.class);
	
	private static final String XDP_PATH = "/content/dam/formsanddocuments/aflacapps/group-master-application-pdf-templates/";
	
	@Reference
	private transient GeneratePDFService pdfService;
	@Reference
	private transient CaseBuilderService caseService;
	@Reference
	private transient ComplianceService complianceService;
	
		
	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {

		String formData = request.getParameter("formData");
		String format = request.getParameter("mode");
		log.info("formData:"+formData);
		if(format.equalsIgnoreCase("Excel"))
			downloadExcel(formData, request, response);
		else if(format.equalsIgnoreCase("Pdf"))
			downloadPdf(formData, request, response);
	}
	
	private void downloadExcel(String formData, SlingHttpServletRequest request, SlingHttpServletResponse response) {
		ExcelGenerationUtil excel = new ExcelGenerationUtil();
		XSSFWorkbook workBook;
		try {
			String excelCase =request.getParameter("case");
			workBook = excel.generateExcel(formData, request, caseService, complianceService,excelCase);
			OutputStream responseOutputStream = response.getOutputStream();
			workBook.write(responseOutputStream);
			workBook.close();
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
			responseOutputStream.flush();
			responseOutputStream.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
	}
	
	private void downloadPdf(String formData, SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		
		String pdfCase =request.getParameter("case");
		String activity = request.getParameter("activity");
		String caseBuilderData = getCaseBuilderData(DocumentUtil.convertToDocument(formData));
		String xdpPath = XDP_PATH;
		if(pdfCase.equalsIgnoreCase(ComplianceCase.WITHOUTCOMPLIANCE.getValue())) {
			xdpPath = xdpPath + "CaseBuildGuide_WOD.xdp";
		}
		else if(pdfCase.equalsIgnoreCase(ComplianceCase.ONLYCOMPLIANCE.getValue())) {
			xdpPath = XDP_PATH + "CaseBuilderToolXDP_D.xdp";
		}
		else if(pdfCase.equalsIgnoreCase(ComplianceCase.CASEBUIDGUIDE.getValue())) {
			xdpPath = XDP_PATH + "CaseBuildGuide.xdp";
		}
		else {
			xdpPath = XDP_PATH + "CaseBuilderToolXDP.xdp";
		}
		try {
			pdfService.generatePDF(caseBuilderData, xdpPath, request, response,pdfCase, activity, "CaseBuild Application");
		} catch(IOException e) {
			log.info(e.getMessage());
		}
	}
	
	private String getCaseBuilderData(Document doc) throws IOException {
		String groupMasterData = DocumentUtil.getNode("/afData/afBoundData/caseBuilderForm", doc);
		return groupMasterData;
	}
}
