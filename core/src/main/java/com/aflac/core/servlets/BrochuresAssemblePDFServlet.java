package com.aflac.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.experience.api.model.AuditUserActivity;
import com.aflac.core.osgi.service.AssemblePDFService;
import com.aflac.core.util.DocumentUtil;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/BrochureAssemblePDF" })

public class BrochuresAssemblePDFServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private transient Logger log = LoggerFactory.getLogger(BrochuresAssemblePDFServlet.class);
	
	private static String Disclosure_DAM_Path = "/content/dam/aflacapps/brochure-disclosures/";

	
	@Reference
	private transient AssemblePDFService assemblerURLService;
	
	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		ResourceResolver resolver = request.getResourceResolver();
		String[] productNames = request.getParameterValues("products");
		String product = request.getParameter("product");
		String series = request.getParameter("series");
		String situs = request.getParameter("situs");
		String language = request.getParameter("language");
		String planId = request.getParameter("planId");
		String formNumber = request.getParameter("formNumber");
		String mode = request.getParameter("mode");
		String documentNames = request.getParameter("documentNames");
		
		if(mode != null && !mode.isEmpty() && mode.equalsIgnoreCase("disclosure")) {
			String fileName = request.getParameter("file");
			DocumentUtil.downloadDamDocument(fileName, request, response,Disclosure_DAM_Path);
		} else if(mode != null && !mode.isEmpty() && mode.equalsIgnoreCase("readFile")) {
			log.info("Read File mode: " + mode);
			String docId = request.getParameter("docId");
			if(docId == null || docId.isEmpty())
				assemblerURLService.generatePDFfromURLList(formNumber, request.getServerName(), response, resolver, "read", null,documentNames);
			else
				assemblerURLService.getFileFromFileNet(docId, response);
		} 
		else {
			if (productNames != null) {
				productNames = productNames[0].split(",");
				assemblerURLService.generatePDFfromCRX(productNames, request.getServerName(), resolver, response, product + " " + series,String.valueOf(language.charAt(0)));
			} else {
				AuditUserActivity audit = new AuditUserActivity();
				ResourceResolver resourceResolver = request.getResourceResolver();
				Session session = resourceResolver.adaptTo(Session.class);
				String userId = session.getUserID();
				audit.setUserId(userId);
				audit.setApplicationId(planId + "#" + product + " " + series + "|" + situs + "|" + language + "|" + LocalDateTime.now());
				audit.setApplicationName("Standard Brochure");
				
				assemblerURLService.generatePDFfromURLList(formNumber, request.getServerName(), response, resolver, "new", audit,documentNames);
			}
		}
	}
}