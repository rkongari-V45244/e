package com.aflac.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

import com.aflac.core.osgi.service.FileNetService;
import com.aflac.core.services.DTO.Document;
import com.aflac.core.services.DTO.Properties;
import com.aflac.core.services.DTO.Property;
import com.aflac.core.services.DTO.Root;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST, GET",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/FileNetTest" })

public class FileNetSampleServlet extends SlingAllMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(FileNetSampleServlet.class);
	private static final String UTC = "UTC";

	@Reference
	private transient ResourceResolverFactory resourceResolverFactory;

	/*
	 * @Reference CredentialService getCredentials;
	 */

	@Reference
	private transient FileNetService fileNetServices;

	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		// Local Testing only

		ClassLoader classLoader = FileNetSampleServlet.class.getClassLoader();
		InputStream isPDF = classLoader.getResourceAsStream("certificates/sampleDoc.pdf");

		try {
			// FileNetServices fs = new FileNetServices(getCredentials);
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			// Set DTO and send as JSON
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
			format.setTimeZone(TimeZone.getTimeZone(UTC));
			String dt = format.format(new Date());
			Document metaData = new Document();
			metaData.objectStore = "ACC";
			metaData.documentClass = "GRP_ACCOUNT_SETUPMAINT";
			metaData.primaryDocClass = "AEM";
			metaData.secondaryDocClass = "AEM";
			metaData.contentType = "application/pdf";
			metaData.mimeType = "application/pdf";
			metaData.recCatID = "AEM";
			metaData.subRecCatID = "AEM";
			metaData.brochureID = "ABCD123";
			metaData.clientManager = "test";
			metaData.coverageEffDate = dt;
			metaData.documentLanguage = "E";
			metaData.enrollmentEndDate = dt;
			metaData.enrollmentStartDate = dt;
			metaData.ePSProposalID = "AEM";
			metaData.groupPlanID = "AEM";
			metaData.implementationManager = "AEM";
			metaData.lOB = "AEM";
			metaData.onboardingCaseID = "AEM";
			metaData.partnerPlatformManager = "AEM";
			metaData.platformName = "AEM";
			metaData.productSeries = "AEM";
			metaData.signatureStatus = "UNSIGNED";
			metaData.situsState = "OH";
			metaData.state = "OH";
			metaData.webOrderingFormIDs = "ABCD123";
			Property property = new Property();
			property.name = "createDate360";
			property.datatype = "date";
			property.value = dt;
			Properties properties = new Properties();
			ArrayList<Property> props = new ArrayList<Property>();
			props.add(property);
			properties.property = props;
			metaData.properties = properties;
			Root root = new Root();
			root.document = metaData;
			ObjectMapper mapper = new ObjectMapper();
			String finalDataObject = mapper.writeValueAsString(root);
			log.info("DTO generated : " + finalDataObject);
			// Ends
			// String metadata =
			// "{\"Document\":{\"ObjectStore\":\"ACC\",\"DocumentClass\":\"GRP_CLAIMS_CUSTOMER\",\"Title\":\"test\",\"RetrievalName\":\"test\",\"ContentType\":\"application/pdf\",\"DateCreated\":\"2023-03-23T20:55:12.456Z\",\"DateLastModified\":\"2023-03-23T20:55:12.456Z\",\"DocumentTitle\":\"test\",\"DocumentType\":\"GRP_CLAIMS_CUSTOMER\",\"MimeType\":\"application/pdf\",\"CreateDate360\":\"2023-03-23T20:55:12.456Z\",\"DataSecurityClassification\":\"ACC\",\"OnPointeID\":\"ACC\",\"PrimaryDocClass\":\"ACC\",\"RecCatID\":\"ACC\",\"RetentionTrigger\":\"ACC\",\"SecondaryDocClass\":\"ACC\",\"SubRecCatID\":\"ACC\",\"BatchID\":\"ACC\",\"CaseID\":\"ACC\",\"CorrSubTransactionID\":\"ACC\",\"CorrTransactionID\":\"ACC\",\"DocType\":\"application/pdf\",\"IsCorrespondence\":\"ACC\",\"GroupName\":null,\"GroupNumber\":null,\"CoverageEffDate\":null,\"GroupPlanID\":null,\"IsSigned\":null,\"OnboardingCaseID\":\"ACC\",\"SignatureStatus\":null,\"State\":null,\"Properties\":{\"Property\":[{\"name\":\"legacydate\",\"datatype\":\"date\",\"value\":\"2023-03-23T20:55:12.456Z\"},{\"name\":\"createDate360\",\"datatype\":\"date\",\"value\":\"2023-03-23T20:55:12.456Z\"}]}}}";
			response.getWriter().write(fileNetServices.saveFileOnFileNet(finalDataObject, isPDF));

		} catch (JsonProcessingException e) {
			e.printStackTrace();

		} finally {
			isPDF.close();

		}
	}

	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {

		// Update complete

		// End

	}

}