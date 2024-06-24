package com.aflac.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.aflac.core.osgi.service.BrochureMaintenanceService;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST,GET,PUT",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/BrochureMaintenance" })
public class BrochureMaintenanceServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	@Reference
	private transient BrochureMaintenanceService brochureService;
	
	public BrochureMaintenanceServlet() { }
	
	public BrochureMaintenanceServlet(BrochureMaintenanceService brochureService) {
		this.brochureService = brochureService;
	}
	
	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		String situs = request.getParameter("situs");
		String series = request.getParameter("series");
		String lob = request.getParameter("lob");
		String lang = request.getParameter("language");
		String brochureData = brochureService.getBrochureData(lob, series, situs, lang);
		response.getWriter().write(brochureData);
	}
	
	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		String data = request.getParameter("data");
		String res = brochureService.saveBrochureData(data);
		response.getWriter().write(res);
	}
	
}
