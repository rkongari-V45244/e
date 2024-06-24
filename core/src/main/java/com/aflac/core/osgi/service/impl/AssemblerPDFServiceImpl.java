package com.aflac.core.osgi.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Stream;

import javax.jcr.query.Query;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
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
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.AdobePDFApiIntegrationConfig;
import com.aflac.core.config.FileNetApiConfig;
import com.aflac.core.experience.api.model.AuditUserActivity;
import com.aflac.core.osgi.service.AssemblePDFService;
import com.aflac.core.osgi.service.AuditService;
import com.aflac.core.osgi.service.FileNetService;
import com.aflac.core.services.CredentialUtilites;
import com.aflac.core.services.DTO.Document;
import com.aflac.core.services.DTO.Properties;
import com.aflac.core.services.DTO.Property;
import com.aflac.core.services.DTO.Root;
import com.aflac.core.servlets.Utility;
import com.day.cq.dam.api.Asset;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component(service = { AssemblePDFService.class })
public class AssemblerPDFServiceImpl implements AssemblePDFService {

	private transient Logger log = LoggerFactory.getLogger(AssemblerPDFServiceImpl.class);
	private CloseableHttpClient httpclient;
	private static String Disclosure_DAM_Path = "/content/dam/aflacapps/brochure-disclosures/";
	private static String objStore = "ACC";
	@Reference
	private transient AdobePDFApiIntegrationConfig getCredentials;
	@Reference
	private FileNetApiConfig fileNetConfig;
	@Reference
	private FileNetService fileNet;
	
	@Reference
	private transient AuditService auditService;

	public AssemblerPDFServiceImpl(){
		httpclient = HttpClients.createDefault();
	}
	
	public AssemblerPDFServiceImpl(CloseableHttpClient httpclient, AdobePDFApiIntegrationConfig getCredentials, 
			AuditService auditService){
		this.httpclient = httpclient;
		this.getCredentials = getCredentials;
		this.auditService = auditService;
	}
	
	@Override
	public int generatePDFfromURLList(String formNumber, String serverName, SlingHttpServletResponse response,
			ResourceResolver resolver, String mode, AuditUserActivity audit,String documentNames) {
		InputStream generatedPDF = null;
		int statusCode = 0;
		LocalDateTime startTime = LocalDateTime.now();
		String[] formNos = formNumber.split(",");
		HttpPost httpPost = new HttpPost(Utility.getDocumentAssembleURL(serverName));
		httpPost.addHeader("Authorization", "Bearer " + getToken());
		List<String> pdfFileSeq = new ArrayList<String>();
		List<String> pdfFilesOnWebOrdering = new ArrayList<String>();
				
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		runParallel(Arrays.stream(formNos).parallel(), pdfFilesOnWebOrdering, resolver, builder);
		boolean status = true;
		for(String formId: formNos) {
			if(!pdfFilesOnWebOrdering.contains(formId)) {
				log.info("***** Not Present FormId: " + formId);
				status = false;
				break;
			}
			if (formId.contains("-FLANG")) {
				formId = formId.split("-")[0];
			} else
				formId = formId.replace(" ", "");
			pdfFileSeq.add(formId + ".pdf");
		}
		if(status) {
			addBroucherDisclosures(documentNames,pdfFileSeq,resolver,builder);
			String ddx = getDDX(pdfFileSeq);
			builder.addBinaryBody("ddx", ddx.getBytes(), ContentType.create("application/xml"), "Assemble.ddx");
	
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);
	
			try {
				CloseableHttpResponse outputResponse = httpclient.execute(httpPost);
				statusCode = outputResponse.getStatusLine().getStatusCode();
				log.info("Aessembler API HTTP code: " + statusCode);
				log.info(outputResponse.getStatusLine().getReasonPhrase());
				String fileNetError = "";
				if (statusCode == 200) {
					generatedPDF = outputResponse.getEntity().getContent();					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(generatedPDF, baos);
					InputStream is = new ByteArrayInputStream(baos.toByteArray());
					if(mode.equalsIgnoreCase("read")) {
						response.setContentType("application/pdf");
						response.addHeader("Content-Disposition", "attachment; filename=output.pdf");
						
						OutputStream responseOutputStream = response.getOutputStream();
						IOUtils.copy(is, responseOutputStream);

						responseOutputStream.flush();
						responseOutputStream.close();
					} else {
						LocalDateTime endTime = LocalDateTime.now();
						audit.setCreatedDateTime(endTime.toString());
						audit.setQueryDateTime(startTime.toString());
						audit.setTurnaroundTime(auditService.getTurnAroundTime(endTime, startTime));
						audit.setApplicationData("");
						auditService.saveAuditEntry(audit);
						
						if(baos.size() < 50000000) {
							String metaData = "{\r\n"
									+ "    \"Document\": {\r\n"
									+ "        \"ObjectStore\": \"" + fileNetConfig.getObjectStore() + "\",\r\n"
									+ "        \"DocumentClass\": \"" + fileNetConfig.getDocClass() + "\",\r\n"
									+ "        \"Title\": \"StandardBrochure_123.pdf\",\r\n"
									+ "        \"RetrievalName\": \"StandardBrochure_123.pdf\",\r\n"
									+ "        \"ContentType\": \"application/pdf\",\r\n"
									+ "        \"Properties\": {\r\n"
									+ "            \"Property\": [\r\n"
									+ "                {\r\n"
									+ "                    \"name\": \"createDate360\",\r\n"
									+ "                    \"value\": \"" + LocalDateTime.now() + "\",\r\n"
									+ "                    \"datatype\": \"date\"\r\n"
									+ "                }\r\n"
									+ "            ]\r\n"
									+ "        }\r\n"
									+ "    }\r\n"
									+ "}";
							
							String fileNetRes = fileNet.saveFileOnFileNet(metaData, is);
							log.info("FileNet Response: " + fileNetRes);
							response.setHeader("Access-Control-Allow-Origin", "*");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							
							if(fileNetRes.isEmpty() || fileNetRes.contains("Bad") || fileNetRes.contains("errorCode") || fileNetRes.contains("faultcode")) {
								fileNetError = "{\"message\": \"Alert : Failed to save file on FileNet, Please try again or follow manual approach to save to FileNet after downloading file to your machine.\"}";
								log.info("fileNetError =====> " + fileNetError);
								response.setStatus(HttpStatus.SC_BAD_REQUEST);
								response.getWriter().write(fileNetError);
								
							} else {
								JsonReader jsonReader = new JsonReader(new StringReader(fileNetRes));
								JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
								String doc = jsonObject.get("Document").toString();
								log.info("===>>> Doc: " + doc);
								fileNet.saveMetaDataToDB("Brochure",doc);
								response.setStatus(HttpStatus.SC_OK);
								response.getWriter().write(doc);
							}
						} else {
							fileNetError = "{\"message\": \"Alert : File is too big to save on FileNet, Permitte limit is 50MB. Please follow manual approach to save to FileNet after downloading to your machine.\"}";
							response.setHeader("Access-Control-Allow-Origin", "*");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.setStatus(HttpStatus.SC_BAD_REQUEST);
							response.getWriter().write(fileNetError);
						}
					}
					generatedPDF.close();
					is.close();
				} else {
					String errorApi = "{\"message\": \"There was an error in processing your request. Ensure correct value is specified for each parameter and retry. If the error persists, contact your administrator or Support Team.\"}";
					log.error("PDF generation error: " + new String(
							IOUtils.toByteArray(outputResponse.getEntity().getContent()), StandardCharsets.UTF_8));
					response.setHeader("Access-Control-Allow-Origin", "*");
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					log.error("Status Code set to 500...");
					response.getWriter().write(errorApi);
				}
			} catch (IOException e) {
				log.error("Exception: ", e);
			}
		} 
		else {
			try {
				String errorMsg = "{\"message\": \"There was an error in processing your request. \\nEnsure selected formId PDFs available on WebOrdering. If the error persists, contact your administrator or Support Team.\"}";
				log.error("PDF File not available on Web Ordering and handled at AEM side");
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write(errorMsg);
			} catch (IOException e) {
				log.error("Exception: ", e);
			}
		}
		return statusCode;
	}
	
	public void addBroucherDisclosures(String documentNames, List<String> pdfFileSeq, ResourceResolver resolver,
			MultipartEntityBuilder builder) {
		try {
			List<String> documents = null;
			if(documentNames!=null && !documentNames.equalsIgnoreCase("null")) {
				documents = Arrays.asList(documentNames.split(","));
				documents.forEach(documentName->{
					String docPath = Disclosure_DAM_Path + documentName + ".pdf";
					log.info("Searching dam for resource: " + documentName);
					Resource resource = resolver.getResource(docPath);
					if (resource != null) {
						Asset asset = resource.adaptTo(Asset.class);
						Resource original = asset.getOriginal();
						String fileName = documentName + ".pdf";
						log.info("Folder Language File === " + fileName);
						try {
							InputStream pdf = original.adaptTo(InputStream.class);
							byte[] ba = IOUtils.toByteArray(pdf);
							builder.addBinaryBody(fileName, ba, ContentType.APPLICATION_OCTET_STREAM, fileName);
							pdfFileSeq.add(fileName);
						} catch (IOException e) {
							log.error("Exception: ", e);
						}
					}
				});
			}
		}catch (Exception e) {
			log.error("Exception: ", e);
		}
		
	}

	public void runParallel(Stream<String> formNumber, List<String> pdfFilesOnWebOrdering, ResourceResolver resolver, MultipartEntityBuilder builder) {
				
		formNumber.forEach(data -> {
			processData(data, pdfFilesOnWebOrdering, builder, resolver);
		});
	}
	
	private void processData(String formNumber, List<String> pdfFilesOnWebOrdering,
			MultipartEntityBuilder builder,	ResourceResolver resolver) {

		String formId = formNumber;

		if (formId.contains("-FLANG")) {
			formId = formId.split("-")[0];
			String planDocPath = "/content/dam/folder-language/" + formId + ".pdf";
			log.info("Searching dam for resource: " + formId);
			Resource resource = resolver.getResource(planDocPath);
			if (resource != null) {
				Asset asset = resource.adaptTo(Asset.class);
				Resource original = asset.getOriginal();
				String fileName = formId + ".pdf";
				log.info("Folder Language File === " + fileName);
				try {
					InputStream pdf = original.adaptTo(InputStream.class);
					pdfFilesOnWebOrdering.add(formNumber);
					byte[] ba = IOUtils.toByteArray(pdf);
					builder.addBinaryBody(fileName, ba, ContentType.APPLICATION_OCTET_STREAM, fileName);
				} catch (IOException e) {
					log.error("Exception: ", e);
				}
			}
		} else {
			formId = formNumber.replace(" ", "");
			String query = "https://webordering.aflac.com/PDF/" + formId + ".pdf";
			String[] path = query.split("/");
			String fileName = path[path.length - 1];
			log.info("Web Order File === " + fileName);
			InputStream input;
			try {
				input = new URL(query).openStream();
				pdfFilesOnWebOrdering.add(formNumber);
				byte[] ba = IOUtils.toByteArray(input);
				builder.addBinaryBody(fileName, ba, ContentType.APPLICATION_OCTET_STREAM, fileName);
			} catch (IOException e) {
				log.warn("**** PDF File Not found at Web Ordering ****");
			}
		}
	}

	private String getToken() {
		CredentialUtilites cu = new CredentialUtilites(getCredentials);
		String accessToken = cu.getAccessToken();
		return accessToken;
	}

	private String getDDX(List<String> pdfLisToAssemble) {
		StringBuilder ddxXml = new StringBuilder();
		String waterMark = getCredentials.getWaterMarkText();
		String env = getCredentials.getEnvironment();
		ddxXml.append("<DDX xmlns=\"http://ns.adobe.com/DDX/1.0/\">");
		ddxXml.append("<PDF result=\"output.pdf\">");
    	if(!env.equalsIgnoreCase("Production")) {
			ddxXml.append("<PageMargins left=\"0in\" top=\"0.15in\" right=\"0in\" bottom=\"0.15in\"/>\r\n"
					+ "            <Watermark rotation=\"45\" opacity=\"15%\" scale=\"150%\">\r\n"
					+ "                  <StyledText>\r\n"
					+ "                        <p font-size=\"32pt\" font=\"Arial\"> " + waterMark + "\r\n </p>\r\n"
					+ "                  </StyledText>\r\n"
					+ "            </Watermark>\r\n");
		}
		ddxXml.append("<Footer>\n" +
          "\t\t\t<Center>\n" +
          "\t\t\t\t<StyledText>\n" +
          "\t\t\t\t\t<p font-size=\"8pt\" font=\"Arial\">Page \n" +
          "\t\t\t\t\t\t<_PageNumber/> of \n" +
          "\t\t\t\t\t\t<_LastPageNumber/>\n" +
          "\t\t\t\t\t</p>\n" +
          "\t\t\t\t</StyledText>\n" +
          "\t\t\t</Center>\n" +
          "\t\t</Footer>");
        ddxXml.append("<Title value=\"Brochure\"/>");

		for (String pdf : pdfLisToAssemble) {
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

	@Override
	public void generatePDFfromCRX(String[] pdfList, String serverName, ResourceResolver resolver,
			SlingHttpServletResponse response, String productSeries, String language) {
		InputStream generatedPDF = null;
		HttpPost httpPost = new HttpPost(Utility.getDocumentAssembleURL(serverName));
		ArrayList<String> finalProductNames = new ArrayList<String>();
		httpPost.addHeader("Authorization", "Bearer " + getToken());
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		for (int i = 0; i < pdfList.length; i++) {
			String planDesign = pdfList[i].split("-")[0];
			String state = pdfList[i].split("-")[1];

			String query = "SELECT * FROM [nt:unstructured] AS node WHERE ISDESCENDANTNODE(node, '/content/dam/Standard') AND PROPERTY(node.[planDesign], 'STRING') LIKE '"
					+ planDesign + "' AND PROPERTY(node.[state], 'STRING') LIKE '" + state + "'";
			queryExecutor(query, finalProductNames, builder, resolver);
		}
		String ddx = getDDX(finalProductNames);
		log.info("DDX" + ddx);

		builder.addBinaryBody("ddx", ddx.getBytes(), ContentType.create("application/xml"), "Assemble.ddx");

		HttpEntity entity = builder.build();
		httpPost.setEntity(entity);

		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			CloseableHttpResponse outputResponse = httpclient.execute(httpPost);

			log.info("The success code is " + outputResponse.getStatusLine().getReasonPhrase());
			generatedPDF = outputResponse.getEntity().getContent();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while ((n = generatedPDF.read(buf)) >= 0)
				baos.write(buf, 0, n);
			byte[] content = baos.toByteArray();

			InputStream is1 = new ByteArrayInputStream(content);

			InputStream is2 = new ByteArrayInputStream(content);
			// String metadata =
			// "{\"Document\":{\"ObjectStore\":\"ACC\",\"DocumentClass\":\"GRP_CLAIMS_CUSTOMER\",\"Title\":\"test\",\"RetrievalName\":\"test\",\"ContentType\":\"application/pdf\",\"DateCreated\":\"2023-03-23T20:55:12.456Z\",\"DateLastModified\":\"2023-03-23T20:55:12.456Z\",\"DocumentTitle\":\"test\",\"DocumentType\":\"GRP_CLAIMS_CUSTOMER\",\"MimeType\":\"application/pdf\",\"CreateDate360\":\"2023-03-23T20:55:12.456Z\",\"DataSecurityClassification\":\"ACC\",\"OnPointeID\":\"ACC\",\"PrimaryDocClass\":\"ACC\",\"RecCatID\":\"ACC\",\"RetentionTrigger\":\"ACC\",\"SecondaryDocClass\":\"ACC\",\"SubRecCatID\":\"ACC\",\"BatchID\":\"ACC\",\"CaseID\":\"ACC\",\"CorrSubTransactionID\":\"ACC\",\"CorrTransactionID\":\"ACC\",\"DocType\":\"application/pdf\",\"IsCorrespondence\":\"ACC\",\"GroupName\":null,\"GroupNumber\":null,\"CoverageEffDate\":null,\"GroupPlanID\":null,\"IsSigned\":null,\"OnboardingCaseID\":\"ACC\",\"SignatureStatus\":null,\"State\":null,\"Properties\":{\"Property\":[{\"name\":\"legacydate\",\"datatype\":\"date\",\"value\":\"2023-03-23T20:55:12.456Z\"},{\"name\":\"createDate360\",\"datatype\":\"date\",\"value\":\"2023-03-23T20:55:12.456Z\"}]}}}";
			// Set DTO and send as JSON
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
			format.setTimeZone(TimeZone.getTimeZone("UTC"));
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
			metaData.brochureID = generateRandomBrochureID();
			metaData.clientManager = "test";
			metaData.coverageEffDate = dt;
			metaData.documentLanguage = language;
			metaData.enrollmentEndDate = dt;
			metaData.enrollmentStartDate = dt;
			metaData.ePSProposalID = "AEM";
			metaData.groupPlanID = "AEM";
			metaData.implementationManager = "AEM";
			metaData.lOB = "AEM";
			metaData.onboardingCaseID = "AEM";
			metaData.partnerPlatformManager = "AEM";
			metaData.platformName = "AEM";
			metaData.productSeries = productSeries;
			metaData.signatureStatus = "UNSIGNED";
			metaData.situsState = "OH";
			metaData.state = "OH";

			metaData.webOrderingFormIDs = pdfList[0];
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
			//fileNetService.saveFileOnFileNet(finalDataObject, is1);

			if (is2 != null) {
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=output.pdf");

				OutputStream responseOutputStream = response.getOutputStream();
				IOUtils.copy(is2, responseOutputStream);

				responseOutputStream.flush();
				responseOutputStream.close();
				is1.close();
				is2.close();
			}
		} catch (IOException e) {
			log.error("Exception: ", e);
		}
		// return generatedPDF;
	}

	private void queryExecutor(String query, ArrayList<String> finalProductNames, MultipartEntityBuilder builder,
			ResourceResolver resolver) {
		if (query.startsWith("SELECT * FROM")) {
			Iterator<Resource> result = resolver.findResources(query, Query.JCR_SQL2);

			while (result.hasNext()) {
				Resource res = result.next();
				String planDocPath = res.getPath().replace("/jcr:content/metadata", "");
				log.info("File Found");
				log.info("Found file Path " + planDocPath);
				String[] path = planDocPath.split("/");
				finalProductNames.add(path[path.length - 1]);
				Resource resource = resolver.getResource(planDocPath);
				Asset asset = resource.adaptTo(Asset.class);
				Resource original = asset.getOriginal();
				InputStream pdf = original.adaptTo(InputStream.class);
				if (pdf == null) {
					log.info("Null pdf");
				}
				builder.addBinaryBody(resource.getName(), pdf, ContentType.APPLICATION_OCTET_STREAM,
						resource.getName());
			}
		}
	}

	private String generateRandomBrochureID() {
		final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final String numerics = "0123456789";
		SecureRandom rnd = new SecureRandom();
		int len = 5;
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
		}
		int leng = 10;
		StringBuilder sb2 = new StringBuilder(leng);
		for (int i = 0; i < leng; i++) {
			sb2.append(numerics.charAt(rnd.nextInt(numerics.length())));
		}
		String finalRandomId = sb.toString() + sb2.toString();
		log.info("Generated random brochure ID: "+ finalRandomId);
		return finalRandomId;
	}

	@Override
	public void downloadDisclosure(String fileName, SlingHttpServletRequest request,
			SlingHttpServletResponse response) {
		ResourceResolver resourceResolver = request.getResourceResolver();
		Resource resourseXDP = resourceResolver.getResource(Disclosure_DAM_Path + fileName);
		log.info("File ==>" + resourseXDP);

		Asset asset = resourseXDP.adaptTo(Asset.class);
		Resource original = asset.getOriginal();
		InputStream fileIS = original.adaptTo(InputStream.class);
		
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "attachment; filename=output.pdf");

		OutputStream responseOutputStream;
		try {
			responseOutputStream = response.getOutputStream();
			IOUtils.copyLarge(fileIS, responseOutputStream);
			responseOutputStream.flush();
			responseOutputStream.close();
			fileIS.close();
		} catch (IOException e) {
			log.error("Exception in Disclosure Download :" + e);
		}
	}
	
	@Override
	public void getFileFromFileNet(String docId, SlingHttpServletResponse response) {
		docId = docId.replace("{", "").replace("}", "");
		log.info("DocID in aem server: " + docId);
		fileNet.getFileFromFileNet(docId, objStore, response);
	}

}
