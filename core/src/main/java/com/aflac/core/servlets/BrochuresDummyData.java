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

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=GET", SLING_SERVLET_EXTENSIONS + "=json",
		SLING_SERVLET_PATHS + "=/bin/GetProductCoverageData" })
public class BrochuresDummyData extends SlingAllMethodsServlet {

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
		String jsonStr = "{ \"brochureData\": { \"planId\": \"PLAN-234717\", \"language\": \"English\", \"productPlanId\": \"PLAN-234717\", \"planName\": \"Group Accident 7000\", \"productCoverages\": { \"productCoverage\": [ { \"brochureNumber\": \"example1\", \"state\":\"AR\", \"productCoverageName\": \"Nonoccupational Plan\", \"productCoverageLevel\": \"Low\" }, { \"brochureNumber\": \"example3\", \"state\":\"ID\", \"productCoverageName\": \"Mental Illness Limited Benefit Insert\", \"productCoverageLevel\": \"Mid\" }, { \"brochureNumber\": \"example4\", \"state\":\"HI\", \"productCoverageName\": \"24 Hour High with Wellness Plan\", \"productCoverageLevel\": \"High\" } ] } } }";
		response.getWriter().write(jsonStr);

	}

}
