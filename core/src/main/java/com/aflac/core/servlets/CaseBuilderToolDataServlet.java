package com.aflac.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.services.CaseBuilderToolService;
import com.aflac.xml.casebuilder.models.Accident;
import com.aflac.xml.casebuilder.models.AccountInformation;
import com.aflac.xml.casebuilder.models.CaseBuilderForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=GET",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/GetCaseBuilderToolData" })
public class CaseBuilderToolDataServlet extends SlingAllMethodsServlet {
	private transient Logger log = LoggerFactory.getLogger(CaseBuilderToolDataServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Reference
	private transient ResourceResolverFactory resourceResolverFactory;

	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		AccountInformation accountInformation = new AccountInformation();
		CaseBuilderToolService caseBuilderToolService = new CaseBuilderToolService();
		CaseBuilderForm caseBuilderForm = new CaseBuilderForm();
		Accident accident = new Accident();
		ObjectMapper Obj = new ObjectMapper();  
		String jsonStr = null;
		try {
			if(request.getParameter("groupNumber")!=null && request.getParameter("effectiveDate") !=null) {
				caseBuilderToolService.getAccountInformationData(accountInformation);
				caseBuilderToolService.getAccidentData(accident);
				caseBuilderForm.setAccountInformation(accountInformation);
				caseBuilderForm.setAccident(accident);
				jsonStr = Obj.writeValueAsString(caseBuilderForm);  
				response.getWriter().write(jsonStr);
			}
			else {
				log.info("groupNumber or effectiveDate is not passed in the request");
			}
			
		} catch (JsonProcessingException e) {
			throw new IOException("Unable to get the GetCaseBuilderToolData",e);
		}
	}

}
