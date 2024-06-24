package com.aflac.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.jcr.query.Query;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.AdobePDFApiIntegrationConfig;
import com.aflac.core.osgi.service.FileNetService;
import com.aflac.core.services.CredentialUtilites;
import com.day.cq.dam.api.Asset;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/AssemblePDFURL" })

public class AssemblePDFURLServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private transient Logger log = LoggerFactory.getLogger(AssemblePDFURLServlet.class);
	
	@Reference
	private transient AdobePDFApiIntegrationConfig getCredentials;
	
	@Reference
	private transient FileNetService fileNetServices;


	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {

		HttpPost httpPost = new HttpPost(Utility.getDocumentAssembleURL(request.getServerName()));
		
		httpPost.addHeader("Authorization", "Bearer " + getToken());

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		String productName = request.getParameter("docURLs");
		
		String ddx = getDDX(productName.split(","));

		builder.addBinaryBody("ddx", ddx.getBytes(), ContentType.create("application/xml"), "Assemble.ddx");

		HttpEntity entity = builder.build();
		httpPost.setEntity(entity);

		
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			CloseableHttpResponse outputResponse = httpclient.execute(httpPost);

			log.info("The success code is " + outputResponse.getStatusLine().getReasonPhrase());

			InputStream generatedPDF = outputResponse.getEntity().getContent();

			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=output.pdf");

			OutputStream responseOutputStream = response.getOutputStream();
			IOUtils.copy(generatedPDF, responseOutputStream);

			responseOutputStream.flush();
			responseOutputStream.close();
		}
	}

	private String getDDX(String[] pdfLisToAssemble) {
		StringBuilder ddxXml = new StringBuilder();
		ddxXml.append("<DDX xmlns=\"http://ns.adobe.com/DDX/1.0/\">");
		ddxXml.append("<PDF result=\"output.pdf\">");
		ddxXml.append("<Footer>");
		ddxXml.append("<Center>");
		ddxXml.append("<StyledText>");
		ddxXml.append("<p>Page <_PageNumber/> of <_LastPageNumber/></p>");
		ddxXml.append("</StyledText>");
		ddxXml.append("</Center>");
		ddxXml.append("</Footer>");
		for (String pdf : pdfLisToAssemble) {
			log.info(pdf);
			ddxXml.append("<PDF source=\"");
			ddxXml.append(pdf);
			ddxXml.append("\"/>");
		}
		ddxXml.append("</PDF>");
		ddxXml.append("</DDX>");

		String ddxStr = ddxXml.toString();
		log.info(ddxStr);
		return ddxStr;
	}

	

	private String getToken() {
		CredentialUtilites cu = new CredentialUtilites(getCredentials);
		String accessToken = cu.getAccessToken();

		return accessToken;
	}

}