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
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/AssemblePDF" })

public class AssemblePDFServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private transient Logger log = LoggerFactory.getLogger(AssemblePDFServlet.class);
	
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

		String planName = StringUtils.toRootLowerCase(request.getParameter("planName").replace(" ", "-"));
		String[] productNames = request.getParameterValues("products");
		
		productNames = productNames[0].split(",");
		for(int i=0; i<productNames.length; i++) {
			productNames[i] = productNames[i].replace(" ", "");
		}
		

		String query = "SELECT * FROM [dam:Asset] WHERE ISDESCENDANTNODE ([/content/dam/aflacapps/" + planName + "])";

		// Fetch Data from CRX
		ResourceResolver resolver = request.getResourceResolver();
		Iterator<Resource> result = resolver.findResources(query, Query.JCR_SQL2);
		ArrayList<String> pdfList = new ArrayList<String>();

		while (result.hasNext()) {
			Resource res = result.next();
			for(String prod : productNames) {
				prod += ".pdf";
				if(prod.equalsIgnoreCase(res.getName())) {
					pdfList.add(res.getName());
					log.info("Added  " + res.getName());
				}
			}
			
			Asset asset = res.adaptTo(Asset.class);
			Resource original = asset.getOriginal();
			InputStream pdf = original.adaptTo(InputStream.class);		

			builder.addBinaryBody(res.getName(), pdf, ContentType.APPLICATION_OCTET_STREAM, res.getName());
		}

		String ddx = getDDX(productNames); //pdfList.toArray(new String[pdfList.size()])

		builder.addBinaryBody("ddx", ddx.getBytes(), ContentType.create("application/xml"), "Assemble.ddx");

		HttpEntity entity = builder.build();
		httpPost.setEntity(entity);

		
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			CloseableHttpResponse outputResponse = httpclient.execute(httpPost);

			log.info("The success code is " + outputResponse.getStatusLine().getReasonPhrase());

			InputStream generatedPDF = outputResponse.getEntity().getContent();
			fileNetServices.saveFileOnFileNet(request.getParameter("metaData"), generatedPDF);

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
			System.out.println(pdf + ".pdf");
			ddxXml.append("<PDF source=\"");
			ddxXml.append(pdf+".pdf");
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