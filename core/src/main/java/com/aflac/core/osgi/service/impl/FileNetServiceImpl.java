package com.aflac.core.osgi.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.ExperienceApiConfig;
import com.aflac.core.config.FileNetApiConfig;
import com.aflac.core.osgi.service.FileNetService;
import com.aflac.core.services.RestTemplateSSLContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component(service = { FileNetService.class })
public class FileNetServiceImpl implements FileNetService , Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Reference
	private transient ExperienceApiConfig apiUrlService;
	@Reference
	private transient FileNetApiConfig fileNetConfig;

	private transient Logger log = LoggerFactory.getLogger(FileNetServiceImpl.class);
	private transient InputStream pdfStream;
	
	private static final String APPLICATION_JSON = "application/json";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String ACCEPT = "Accept";
	private static final String AFLAC_VIEW_POINTE_SAML_CREDENTIAL = "Aflac-ViewPointeSAMLCredential";
	private static final String CACERTS = "/cacerts";
	private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
	private static final String CERTIFICATES_CACERTS = "certificates/cacerts";
	private static final String PKCS12 = "PKCS12";
	private static final String TRUST_STORE = "changeit";
	private static final String USERID = "userid";
	private static final String FILENAME = "file1";
	private static final String TOKEN = "token";
	private static final String METADATA = "metadata1";
	private static final String USERNAME = "username";
	private static final String PASS = "password";
	private static final int MAX_RETRY_ATTEMPTS = 3;
	

	public FileNetServiceImpl() { }

	
	public FileNetServiceImpl(ExperienceApiConfig apiUrlService, FileNetApiConfig fileNetConfig) {
		this.apiUrlService = apiUrlService;
		this.fileNetConfig = fileNetConfig;
	}

	private SSLContext generateSSLContext() {

		SSLContext sslContext = null;

		String filenetCertPdk = fileNetConfig.getFilenetCertPdk(),
				filenetCertsPFXPath = fileNetConfig.getFilenetCredsPFXPath();

		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
			InputStream targetStream = classLoader.getResourceAsStream(filenetCertsPFXPath);
			KeyStore keyStore = KeyStore.getInstance(PKCS12);
			keyStore.load(targetStream, filenetCertPdk.toCharArray());

			InputStream targetStream2 = classLoader.getResourceAsStream(CERTIFICATES_CACERTS);
			String tempDir = System.getProperty(JAVA_IO_TMPDIR) + CACERTS;
			byte[] bytes = IOUtils.toByteArray(targetStream2);
			File file = new File(tempDir);
			try (FileOutputStream outputStream = new FileOutputStream(file)) {
				outputStream.write(bytes);
			}

			sslContext = SSLContextBuilder.create().loadTrustMaterial(file, TRUST_STORE.toCharArray())
					.loadKeyMaterial(keyStore, filenetCertPdk.toCharArray()).build();
		} catch (NoSuchAlgorithmException | KeyManagementException | UnrecoverableKeyException | KeyStoreException
				| CertificateException | IOException e) {
			log.error("Exception Error " + e);
		}
		return sslContext;

	}

	private String generateFileNetServiceToken() {
		String token = StringUtils.EMPTY, filenetTokenProviderURL = fileNetConfig.getFilenetTokenProviderURL();
		String filenetCredsUser = fileNetConfig.getFilenetCredsUser();
		String filenetCredsUserPass = fileNetConfig.getFilenetCredsUserPass();

		try {
			JsonObject credsObject = new JsonObject();
			credsObject.addProperty(USERNAME, filenetCredsUser);
			credsObject.addProperty(PASS, filenetCredsUserPass);
			StringEntity entity = new StringEntity(credsObject.toString());
			String responseToken = "";
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(CONTENT_TYPE, APPLICATION_JSON);

			RestTemplateSSLContext restTemplate = new RestTemplateSSLContext();
			try {
				responseToken = restTemplate.sendPost(filenetTokenProviderURL, headers, entity, generateSSLContext());
			} catch (IOException e1) {				
				log.error("Error", e1);
			}
			JsonReader jsonReader = new JsonReader(new StringReader(responseToken));
            JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
            token = jsonObject.get("token").toString();
			//log.info("FileNet Token Generation Response: " + responseToken);

		} catch (IOException e) {
			log.error("FileNotFoundException Error ", e);
		}

		return token;

	}
	
	private String generateFileNetServiceTokenWorkflow() throws IOException {
		String token = StringUtils.EMPTY, filenetTokenProviderURL = fileNetConfig.getFilenetTokenProviderURL();
		String filenetCredsUser = fileNetConfig.getFilenetCredsUser();
		String filenetCredsUserPass = fileNetConfig.getFilenetCredsUserPass();

		try {
			JsonObject credsObject = new JsonObject();
			credsObject.addProperty(USERNAME, filenetCredsUser);
			credsObject.addProperty(PASS, filenetCredsUserPass);
			StringEntity entity = new StringEntity(credsObject.toString());
			String responseToken = "";
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(CONTENT_TYPE, APPLICATION_JSON);

			RestTemplateSSLContext restTemplate = new RestTemplateSSLContext();
			try {
				responseToken = restTemplate.sendPost(filenetTokenProviderURL, headers, entity, generateSSLContext());
			} catch (IOException e1) {				
				log.error("Error", e1);
			}
			JsonReader jsonReader = new JsonReader(new StringReader(responseToken));
            JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
            token = jsonObject.get("token").toString();
			//log.info("FileNet Token Generation Response: " + responseToken);

		} catch (IOException e) {
			log.error("FileNotFoundException Error ", e);
			throw new IOException("Error in generateFileNetServiceTokenWorkflow");
		}

		return token;

	}
	
	@Override
	public String saveFileOnFileNet(String metaData, InputStream isPDF) {

		String responseFileNet = StringUtils.EMPTY, filenetDocumentsURL = fileNetConfig.getFilenetDocumentsURL(),
				filenetTokenUser = fileNetConfig.getFilenetTokenUser();
		String token = generateFileNetServiceToken();
		JsonObject tokenJSON = new JsonObject();
		tokenJSON.addProperty(USERID, filenetTokenUser);
		tokenJSON.addProperty(TOKEN, token);

		Map<String, String> headers = new HashMap<String, String>();
		headers.put(AFLAC_VIEW_POINTE_SAML_CREDENTIAL, tokenJSON.toString());
		headers.put(ACCEPT, APPLICATION_JSON);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addTextBody(METADATA, metaData);
		builder.addBinaryBody(FILENAME, isPDF, ContentType.APPLICATION_OCTET_STREAM, FILENAME);

		HttpEntity entity = builder.build();
		RestTemplateSSLContext restTemplate = new RestTemplateSSLContext();
		try {
			responseFileNet = restTemplate.sendPost(filenetDocumentsURL, headers, entity, generateSSLContext());
			log.info("FileNet Response: " + responseFileNet);
		} catch (IOException e1) {
			log.error("Error", e1);
		}
		return responseFileNet;
	}
	
	@Override
	public String bullUploadOnFileNet(List<String> metaData, List<InputStream> isPDFs) throws IOException {
		String responseFileNet = StringUtils.EMPTY, 
				filenetDocumentsURL = fileNetConfig.getFilenetDocumentsURL(),
				filenetTokenUser = fileNetConfig.getFilenetTokenUser();
		String token = generateFileNetServiceTokenWorkflow();
		JsonObject tokenJSON = new JsonObject();
		tokenJSON.addProperty(USERID, filenetTokenUser);
		tokenJSON.addProperty(TOKEN, token);

		Map<String, String> headers = new HashMap<String, String>();
		headers.put(AFLAC_VIEW_POINTE_SAML_CREDENTIAL, tokenJSON.toString());
		headers.put(ACCEPT, APPLICATION_JSON);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		for(int i=0; i< metaData.size(); i++) {
			builder.addTextBody("metadata"+(i+1), metaData.get(i));
			builder.addBinaryBody("file"+(i+1), isPDFs.get(i), ContentType.APPLICATION_OCTET_STREAM, FILENAME);
		}
		HttpEntity entity = builder.build();
		RestTemplateSSLContext restTemplate = new RestTemplateSSLContext();
		try {
			responseFileNet = restTemplate.sendPost(filenetDocumentsURL, headers, entity, generateSSLContext());
			log.info("FileNet Response: " + responseFileNet);
		} catch (IOException e1) {
			log.error("Error", e1);
			throw new IOException("Error in bullUploadOnFileNet");
		}
		return responseFileNet;
	}
	

	@Override
	public void getFileFromFileNet(String docID, String objectStore, SlingHttpServletResponse response) {
		String token = null;
		token = generateFileNetServiceToken();

		JsonObject tokenJSON = new JsonObject();
		tokenJSON.addProperty(USERID, fileNetConfig.getFilenetTokenUser());
		tokenJSON.addProperty(TOKEN, token);

		String url = fileNetConfig.getFilenetDocumentsRetrieveURL() + docID + "?ObjectStore=" + objectStore;
		HttpGet httpGet = new HttpGet(url);

		httpGet.addHeader(AFLAC_VIEW_POINTE_SAML_CREDENTIAL, tokenJSON.toString());
		httpGet.addHeader(ACCEPT, "application/octet-stream");
		int timeout = 60;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
		
		try{
			CloseableHttpClient httpclient = HttpClients.custom().setSslcontext(generateSSLContext()).build();
			CloseableHttpResponse outputResponse = httpclient.execute(httpGet);
			int statusCode = outputResponse.getStatusLine().getStatusCode();
			log.info("FileNet Read Status Code: " + statusCode);
			if(statusCode == 200) {
				HttpEntity resentity = outputResponse.getEntity();
				this.pdfStream = resentity.getContent();
				//this.pdfStream = responseFileNet;
				if(response != null) {
					response.setContentType("application/pdf");
					response.addHeader("Content-Disposition", "attachment; filename=output.pdf");
		
					OutputStream responseOutputStream = response.getOutputStream();
					IOUtils.copyLarge(this.pdfStream, responseOutputStream);
					responseOutputStream.flush();
					responseOutputStream.close();
					//responseFileNet.close();
				}
				
			} else {
				HttpEntity resentity = outputResponse.getEntity();
				log.error(EntityUtils.toString(resentity, "UTF-8"));
				if(response != null) {
					String fileNetError = "{\"message\": \"Failed to read file from FileNet.\"}";
					response.setHeader("Access-Control-Allow-Origin", "*");
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(HttpStatus.SC_BAD_REQUEST);
					response.getWriter().write(fileNetError);
				}
			}
			
		} catch (IOException e) {
			log.error("Error", e);
		}
	}

	@Override
	public boolean saveMetaDataToDB(String docType, String docMetaData) {
		JsonReader jsonReader = new JsonReader(new StringReader(docMetaData));
		JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
		String docId = jsonObject.get("DocumentID").toString();

		String metaData = "{\n"
				+ "  \"document-id\": " + docId + ",\n"
				+ "  \"doc-ex-id\": \"123\",\n"
				+ "  \"document-type\": \"" + docType +"\",\n"
				+ "  \"version\":" + fileNetConfig.getVersion() + ",\n"
				+ "  \"object-store\": \"" + fileNetConfig.getObjectStore() + "\",\n"
				+ "  \"modified-date\": \"" + LocalDateTime.now() + "\",\n"
				+ "  \"insert-data\": \"" + fileNetConfig.getInsertData() + "\",\n"
				+ "  \"asm-notification\": \"" + fileNetConfig.getAsmNotification() + "\",\n"
				+ "  \"filenet-metadata-data\": \"" + docMetaData.replace("\"", "\\\"") + "\"\n"
				+ "}";
		
		HttpPut httpPut = new HttpPut(apiUrlService.getSaveFilenetMetadataToDBUrl());
		CloseableHttpClient httpclient = HttpClients.createDefault();
		httpPut.setHeader("Content-type", "application/json");
		String apiResponse;
		try {
			apiResponse = apiUrlService.executeHttpPut(httpclient, httpPut,metaData);
			boolean status = false;
			if(apiResponse != null) {
				jsonReader = new JsonReader(new StringReader(apiResponse));
				jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
				status = jsonObject.get("status").getAsBoolean();
			}
			log.info("Save FileNet Metadata to DynamoDB Status: " + status);
			return status;
		} catch (IOException e) {
			log.error("Exception while saving Filenet Metadata to DynamoDB... ",e);
		}	
		return false;
	}
	
	@Override
	public InputStream getPdfStream() {
		return this.pdfStream;
	}


	@Override
	public void getFileFromFileNetWorkFlow(String docID, String objectStore, SlingHttpServletResponse response) throws IOException {
		String token = null;
		token = generateFileNetServiceTokenWorkflow();
		//int retryAttempts = 0;
		

		JsonObject tokenJSON = new JsonObject();
		tokenJSON.addProperty(USERID, fileNetConfig.getFilenetTokenUser());
		tokenJSON.addProperty(TOKEN, token);

		String url = fileNetConfig.getFilenetDocumentsRetrieveURL() + docID + "?ObjectStore=" + objectStore;
		HttpGet httpGet = new HttpGet(url);

		httpGet.addHeader(AFLAC_VIEW_POINTE_SAML_CREDENTIAL, tokenJSON.toString());
		httpGet.addHeader(ACCEPT, "application/octet-stream");
		int timeout = 60;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
		
		try{
//			while (token==null && retryAttempts < MAX_RETRY_ATTEMPTS) {
				
				CloseableHttpClient httpclient = HttpClients.custom().setSslcontext(generateSSLContext()).build();
				CloseableHttpResponse outputResponse = httpclient.execute(httpGet);
				int statusCode = outputResponse.getStatusLine().getStatusCode();
				log.info("FileNet Read Status Code: " + statusCode);
				//if(token!=null) {
					if(statusCode == 200) {
						HttpEntity resentity = outputResponse.getEntity();
						this.pdfStream = resentity.getContent();
						//this.pdfStream = responseFileNet;
						if(response != null) {
							response.setContentType("application/pdf");
							response.addHeader("Content-Disposition", "attachment; filename=output.pdf");
				
							OutputStream responseOutputStream = response.getOutputStream();
							IOUtils.copyLarge(this.pdfStream, responseOutputStream);
							responseOutputStream.flush();
							responseOutputStream.close();
							//responseFileNet.close();
						}
						
					} else {
						HttpEntity resentity = outputResponse.getEntity();
						log.error(EntityUtils.toString(resentity, "UTF-8"));
						if(response != null) {
							String fileNetError = "{\"message\": \"Failed to read file from FileNet.\"}";
							response.setHeader("Access-Control-Allow-Origin", "*");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.setStatus(HttpStatus.SC_BAD_REQUEST);
							response.getWriter().write(fileNetError);
						}
					}
//				}else {
//					retryAttempts++;
//				}
				
//			}
//		}
			
		} catch (IOException e) {
			log.error("Error", e);
			throw new IOException("Error in getFileFromFileNetWorkFlow");
		}
	}
}
