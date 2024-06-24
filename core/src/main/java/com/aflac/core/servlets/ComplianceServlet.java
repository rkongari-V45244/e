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

import com.aflac.core.osgi.service.ComplianceService;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST,GET,PUT",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/complianceServlet" })
public class ComplianceServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(ComplianceServlet.class);

	@Reference
	private transient ComplianceService service;

	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		String methodType = request.getParameter("methodType");
		String referenceDataItem = request.getParameter("referenceDataItem");
		String situsState = request.getParameter("situsState");
		String lob = request.getParameter("lob");
		String type = request.getParameter("type");
		String label = request.getParameter("label");
		String recordId = request.getParameter("recordId");
		String apiResponse = null;
		if (methodType!=null && methodType.equalsIgnoreCase("init") && referenceDataItem != null && !referenceDataItem.isEmpty())
			apiResponse = service.getInitComplianceData(referenceDataItem);
		else if(methodType!=null && methodType.equalsIgnoreCase("edit") && recordId != null && !recordId.isEmpty())
			apiResponse = service.getEditComplianceData(recordId);
		else
			apiResponse = service.getComplianceData(situsState,lob,type,label);
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(apiResponse);
	}

	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		String formData = request.getParameter("formData");
		String methodType = request.getParameter("methodType");
		String apiResponse = "";
		XSSFWorkbook workBook = null;
		if(formData!=null && !formData.isEmpty()&&methodType!=null && methodType.equalsIgnoreCase("add"))
			apiResponse = service.addComplianceData(formData);
		else if(formData!=null && !formData.isEmpty()&&methodType!=null && methodType.equalsIgnoreCase("update"))
			apiResponse = service.updateComplianceData(formData);
		else if(formData!=null && !formData.isEmpty()&&methodType!=null && methodType.equalsIgnoreCase("export"))
			workBook = service.exportComplianceData(formData);
		
		if(methodType != null && methodType.equalsIgnoreCase("export")) {
			try {
				OutputStream responseOutputStream = response.getOutputStream();
				if(workBook != null) {
					workBook.write(responseOutputStream);
					workBook.close();
				}
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
				responseOutputStream.flush();
				responseOutputStream.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		} else {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(apiResponse);
		}
	}
	
//	@Override
//	public void doPut(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
//			throws ServletException, IOException {
//		String formData = request.getParameter("formData");
//		String apiResponse = null;
//		if(formData!=null && !formData.isEmpty())
//			apiResponse = service.updateComplianceData(formData);
//		
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setContentType("application/json");
//		response.setCharacterEncoding("UTF-8");
//		response.getWriter().write(apiResponse);
//	}

}
