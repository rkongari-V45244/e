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

import com.aflac.core.osgi.service.CaseBuilderService;
import com.aflac.core.services.CaseBuilderToolService;
import com.aflac.xml.casebuilderProducts.models.EnrollmentDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=GET",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/GetCaseBuilderToolEnrollmentDetails" })
public class CaseBuilderToolEnrollmentDataServlet extends SlingAllMethodsServlet {

	private transient Logger log = LoggerFactory.getLogger(CaseBuilderToolEnrollmentDataServlet.class);
	
	private static final long serialVersionUID = 1L;
	
	@Reference
	private transient ResourceResolverFactory resourceResolverFactory;
	
	@Reference
	private transient CaseBuilderService caseBuildService;

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
		ObjectMapper Obj = new ObjectMapper();
		CaseBuilderToolService caseBuilderToolService = new CaseBuilderToolService();
		EnrollmentDetails enrollmentDetails = null;
		String jsonStr = null;
		try {
			if(request.getParameter("groupNumber")!=null && !request.getParameter("groupNumber").isEmpty() && request.getParameter("effectiveDate") !=null && !request.getParameter("effectiveDate").isEmpty()) {
				enrollmentDetails = new EnrollmentDetails();
				caseBuilderToolService.getEnrollmentDetails(enrollmentDetails);
				//enrollmentDetails = caseBuildService.getEnrollmentDetails(request.getParameter("groupNumber"), request.getParameter("effectiveDate"));
				jsonStr = Obj.writeValueAsString(enrollmentDetails);  
				response.getWriter().write(jsonStr);
			}
			else {
				log.info("groupNumber or effectiveDate is not passed in the request");
			}
			
		} catch (JsonProcessingException e) {
			throw new IOException("Unable to get the Enrollment Details",e);
		} catch (IOException e) {
			throw new IOException("Unable to get the Enrollment Details",e);
		}  
	}

}
