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

import com.aflac.core.osgi.service.PlatformMaintenanceService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST,GET,PUT",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/PlatformMaintenance" })
public class PlatformMaintenanceServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	@Reference
	private transient PlatformMaintenanceService service;
	
	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		ObjectMapper obj = new ObjectMapper();
		String platformId = request.getParameter("platformId");
		String jsonStr = obj.writeValueAsString(service.getPlatformMaintenanceDetails(platformId));
		response.getWriter().write(jsonStr);
	}
	
	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		String formData = request.getParameter("formData");
		String mode = request.getParameter("mode");
		
		if(mode.equalsIgnoreCase("save")) {
			String res = service.savePlatformMaintenanceDetails(formData);
			response.getWriter().write(res);
		} else if(mode.equalsIgnoreCase("delete")) {
			String res = service.deletePlatformMaintenanceDetails(formData);
			response.getWriter().write(res);
		}
		
	}
}
