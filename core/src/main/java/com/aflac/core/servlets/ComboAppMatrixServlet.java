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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aflac.core.osgi.service.ComboAppMatrixService;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST, GET",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/planData" })
public class ComboAppMatrixServlet extends SlingAllMethodsServlet{

	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(ComboAppMatrixServlet.class);
	
	@Reference
	private transient ComboAppMatrixService service;

	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		String appName = request.getParameter("appName");
		String formID = request.getParameter("formID");
		String situs = request.getParameter("situs");
		String methodType = request.getParameter("methodType");
		String sampleData = null;
		if(appName.equalsIgnoreCase("comboApp")) {
			if(methodType!=null && methodType.equalsIgnoreCase("Add"))
				sampleData = service.getComboAppAddData();
			else if(methodType!=null && methodType.equalsIgnoreCase("FetchFormIds"))
				sampleData = service.getFormIds(situs);
			else
				sampleData = service.getComboAppData(situs, formID);
		}else {
			sampleData = service.getMasterAppData(situs, formID);
		}
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(sampleData);
	}

	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		String appName = request.getParameter("appName");
		String formData = request.getParameter("formData");
		//String formData = "{\"situs-state\":\"CZ\",\"form-id\":\"C02205CO3\",\"products\":[{\"id\":\"AC\",\"name\":\"string\",\"series-details\":[{\"series-name\":\"7001\",\"availability\":false},{\"series-name\":\"7002\",\"availability\":true}]},{\"id\":\"CI\",\"name\":\"string\",\"series-details\":[{\"series-name\":\"8002\",\"availability\":true}]},{\"id\":\"HI\",\"name\":\"string\",\"series-details\":[{\"series-name\":\"9002\",\"availability\":true}]}]}";
		String servletResponse = null;

		if (appName.equalsIgnoreCase("comboApp")) {
			servletResponse = service.saveComboAppData(formData);
		}
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(servletResponse);
	}

}
