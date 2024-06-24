package com.aflac.core.osgi.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

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
import org.w3c.dom.Document;

import com.aflac.core.config.AdobePDFApiIntegrationConfig;
import com.aflac.core.config.FileNetApiConfig;
import com.aflac.core.osgi.service.CaseBuilderService;
import com.aflac.core.osgi.service.ComplianceService;
import com.aflac.core.osgi.service.FileNetService;
import com.aflac.core.osgi.service.GeneratePDFService;
import com.aflac.core.services.CredentialUtilites;
import com.aflac.core.servlets.Utility;
import com.aflac.core.util.ComplianceCase;
import com.aflac.core.util.ComplianceDataUtil;
import com.aflac.core.util.DocumentUtil;
import com.aflac.core.util.PdfGenerationUtil;
import com.aflac.xml.casebuilderProducts.models.CaseBuilderForm;
import com.aflac.xml.casebuilderProducts.models.EmploymentEnrollmentDisclosure;
import com.aflac.xml.casebuilderProducts.models.Product;
import com.day.cq.dam.api.Asset;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component(
	service = { GeneratePDFService.class }
)
public class GeneratePDFServiceImpl implements GeneratePDFService,Serializable{
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(GeneratePDFServiceImpl.class);
	private transient InputStream pdfStream;
	private transient CloseableHttpClient httpclient;
	
	@Reference
	private transient AdobePDFApiIntegrationConfig getCredentials;
	
	@Reference
	private transient CaseBuilderService caseService;
	@Reference
	private transient ComplianceService complianceService;
	@Reference
	private transient FileNetApiConfig fileNetConfig;
	@Reference
	private transient FileNetService fileNet;
	
	public GeneratePDFServiceImpl() { }
	
	public GeneratePDFServiceImpl(CloseableHttpClient httpclient, AdobePDFApiIntegrationConfig getCredentials,
			CaseBuilderService caseService, ComplianceService complianceService, FileNetApiConfig fileNetConfig,
			FileNetService fileNet) { 
		this.httpclient = httpclient;
		this.getCredentials = getCredentials;
		this.caseService = caseService;
		this.complianceService = complianceService;
		this.fileNet = fileNet;
		this.fileNetConfig = fileNetConfig;
	}

	@Override
	public void generatePDF(String formData, String xdpPath, SlingHttpServletRequest request, 
			SlingHttpServletResponse response, String pdfCase, String activity, String app)
			throws IOException {
		if(app.equalsIgnoreCase("CaseBuild Application")) {
			if(!pdfCase.equalsIgnoreCase(ComplianceCase.ONLYCOMPLIANCE.getValue())) {
				CaseBuilderForm caseForm = PdfGenerationUtil.xmlToJava(formData);
				ComplianceDataUtil complianceData = new ComplianceDataUtil(caseService.getComplianceVerbiage(caseForm.getAccountInformation().getGroupNumber(),
								caseForm.getAccountInformation().getCoverageBillingEffDate(),
								caseForm.getAccountInformation().getPlatform(),pdfCase), complianceService.getLabelSequence());

				List<Product> products = new ArrayList<>();
				if (caseForm != null && caseForm.getProducts() != null)
					products = caseForm.getProducts().getProduct();
				
				for (Product product : products) {
					PdfGenerationUtil.generateEligibilityStatements(complianceData, product,caseForm);
				}
				
				try {
					formData = PdfGenerationUtil.convertObjectToXML(caseForm);
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			}
			else if(pdfCase.equalsIgnoreCase(ComplianceCase.ONLYCOMPLIANCE.getValue())) {
				String lob =request.getParameter("lob");
				String situsState = request.getParameter("situsState");
				Boolean isCi22kEnabled = Boolean.parseBoolean(request.getParameter("CI22Kcondition"));
				EmploymentEnrollmentDisclosure empd = complianceService.getComplianceDataRefill(situsState, lob, "4", "",isCi22kEnabled);
				try {
					formData = PdfGenerationUtil.convertObjectToXML(empd);
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			}
		}
						
		Document doc = DocumentUtil.convertToDocument(formData);
		
		String serverName = request.getServerName();
		//log.info("request.getServerName()==>" + serverName);

		ResourceResolver resourceResolver = request.getResourceResolver();
		Resource resourseXDP = resourceResolver.getResource(xdpPath);
		//log.info("XDP ==>" + resourseXDP);

		Asset asset = resourseXDP.adaptTo(Asset.class);
		Resource original = asset.getOriginal();
		InputStream xdpIS = original.adaptTo(InputStream.class);
		byte[] xdpTemplateBytes;
		if(xdpIS != null)
			xdpTemplateBytes = IOUtils.toByteArray(xdpIS);
		else
			xdpTemplateBytes = "PDF not found".getBytes();
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addBinaryBody("data", formData.getBytes(), ContentType.DEFAULT_BINARY, "data.xml");
		builder.addBinaryBody("template", xdpTemplateBytes, ContentType.DEFAULT_BINARY, "template.xdp");
		HttpPost httpPost = new HttpPost(Utility.getDocumentGenerationURL(serverName));
		httpPost.addHeader("Authorization", "Bearer " + getToken());

		HttpEntity entity = builder.build();
		httpPost.setEntity(entity);

		try (CloseableHttpClient httpclient1 = HttpClients.createDefault();) {
			CloseableHttpResponse outputResponse = httpclient1.execute(httpPost);
			log.info("DOC GEN API Statuscode|" + outputResponse.getStatusLine().getStatusCode());
			
			InputStream is = outputResponse.getEntity().getContent();
			this.pdfStream = is;
			int statusCode = outputResponse.getStatusLine().getStatusCode();
			if(statusCode == 200 && app.equalsIgnoreCase("CaseBuild Application")) {
				HttpPost httpPost1 = addPaginationAndWaterMark(is, serverName, xdpPath);
				CloseableHttpResponse outputResponse1 = httpclient1.execute(httpPost1);
				statusCode = outputResponse1.getStatusLine().getStatusCode();
				log.info("Assembeler success code: " + statusCode);
				if(statusCode == 200)
					this.pdfStream = outputResponse1.getEntity().getContent();
			}
			
			if(statusCode == 200) {
				if(activity.equalsIgnoreCase("read")) {
					response.setContentType("application/pdf");
					response.addHeader("Content-Disposition", "attachment; filename=output.pdf");
					OutputStream responseOutputStream = response.getOutputStream();
					IOUtils.copy(this.pdfStream, responseOutputStream);
					responseOutputStream.flush();
					responseOutputStream.close();
				} else {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(this.pdfStream, baos);
					
					if(baos.size() < 50000000) {
						String metaData = "{\r\n"
								+ "    \"Document\": {\r\n"
								+ "        \"ObjectStore\": \"" + fileNetConfig.getObjectStore() + "\",\r\n"
								+ "        \"DocumentClass\": \"" + fileNetConfig.getDocClass() + "\",\r\n"
								+ "        \"Title\": \"CaseBuild.pdf\",\r\n"
								+ "        \"RetrievalName\": \"CaseBuild.pdf\",\r\n"
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
						is = new ByteArrayInputStream(baos.toByteArray());
						String fileNetRes = fileNet.saveFileOnFileNet(metaData, is);
						log.info("FileNet Response: " + fileNetRes);
						response.setHeader("Access-Control-Allow-Origin", "*");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						if(fileNetRes.isEmpty() || fileNetRes.contains("Bad") || fileNetRes.contains("errorCode") || fileNetRes.contains("faultcode")) {
							String fileNetError = "{\"message\": \"Alert : Failed to save file on FileNet, Please try again or follow manual approach to save to FileNet after downloading file to your machine.\"}";
							response.setStatus(HttpStatus.SC_BAD_REQUEST);
							response.getWriter().write(fileNetError);
							
						} else {
							JsonReader jsonReader = new JsonReader(new StringReader(fileNetRes));
							JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
							String docId = jsonObject.get("Document").toString();
							log.info("===>>> Doc: " + docId);
							fileNet.saveMetaDataToDB("Case Build", docId);
							response.setStatus(HttpStatus.SC_OK);
							response.getWriter().write(docId);
						}
					} else {
						String fileNetError = "{\"message\": \"Alert : File is too big to save on FileNet, Permitte limit is 50MB. Please follow manual approach to save to FileNet after downloading to your machine.\"}";
						response.setHeader("Access-Control-Allow-Origin", "*");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.setStatus(HttpStatus.SC_BAD_REQUEST);
						response.getWriter().write(fileNetError);
					}
					baos.close();
					is.close();
				}
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
		}
	}
	
	private HttpPost addPaginationAndWaterMark(InputStream is, String serverName, String xdpPath) throws IOException {
		byte[] ba = IOUtils.toByteArray(is);
		String fileName = "certificate.pdf";
		String waterMark = getCredentials.getWaterMarkText();
		String env = getCredentials.getEnvironment();
		HttpPost httpPost1 = new HttpPost(Utility.getDocumentAssembleURL(serverName));
		httpPost1.addHeader("Authorization", "Bearer " + getToken());
		MultipartEntityBuilder builder1 = MultipartEntityBuilder.create();
		builder1.addBinaryBody(fileName, ba, ContentType.APPLICATION_OCTET_STREAM, fileName);
		StringBuilder ddxXml = new StringBuilder();
		ddxXml.append("<DDX xmlns=\"http://ns.adobe.com/DDX/1.0/\">");
		ddxXml.append("<PDF result=\"output.pdf\">");
		ddxXml.append("<PDF source=\"");
		ddxXml.append(fileName);
		ddxXml.append("\"/>");
		if(!env.equalsIgnoreCase("Production")) {
			ddxXml.append("<PageMargins left=\"0in\" top=\"0.15in\" right=\"0in\" bottom=\"0.15in\"/>\r\n"
					+ "            <Watermark rotation=\"45\" opacity=\"15%\" scale=\"150%\">\r\n"
					+ "                  <StyledText>\r\n"
					+ "                        <p font-size=\"32pt\" font=\"Arial\"> " + waterMark + "\r\n </p>\r\n"
					+ "                  </StyledText>\r\n"
					+ "            </Watermark>\r\n");
		}
		if(xdpPath.contains("CaseBuilderToolXDP_D.xdp")){
			ddxXml.append("<Footer>\r\n"
				+ "		<Center>\r\n"
				+ "		<StyledText>\r\n"
				+ "			<p>Page <_PageNumber/> of <_LastPageNumber/></p>\r\n"
				+ "		</StyledText>\r\n"
				+ "		</Center>\r\n"
				+ "	</Footer>");
			ddxXml.append("<Title value=\"Certificate Language Pdf\"/>");
		}
		else{
			ddxXml.append("<Footer>\r\n"
				+ "		<Center>\r\n"
				+ "		<StyledText>\r\n"
				+ "			<p></p>\r\n"
				+ "		</StyledText>\r\n"
				+ "		</Center>\r\n"
				+ "	</Footer>");
			ddxXml.append("<Title value=\"Case Build Pdf\"/>");
		}
		ddxXml.append("</PDF>");
		ddxXml.append("</DDX>");

		String ddxStr = ddxXml.toString();
		builder1.addBinaryBody("ddx", ddxStr.getBytes(), ContentType.create("application/xml"), "Assemble.ddx");
		HttpEntity entity1 = builder1.build();
		httpPost1.setEntity(entity1);
		
		return httpPost1;
	}
	
	private String getToken() {
		CredentialUtilites cu = new CredentialUtilites(getCredentials);
		String accessToken = cu.getAccessToken();
		return accessToken;
	}
	
	@Override
	public InputStream getPdfStream() {
		return this.pdfStream;
	}
}
