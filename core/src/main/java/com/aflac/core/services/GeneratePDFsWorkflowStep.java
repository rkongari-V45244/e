package com.aflac.core.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.aflac.core.config.AdobePDFApiIntegrationConfig;
import com.aflac.core.config.AdobeSignConfig;
import com.aflac.core.servlets.Utility;
import com.day.cq.dam.api.Asset;

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Custom component to MASTERAPP - Generate Multple PDFs.",
		Constants.SERVICE_VENDOR + "=Adobe Systems",
		"process.label" + "=Custom component to MASTERAPP - Generate Multple PDFs" })
public class GeneratePDFsWorkflowStep implements WorkflowProcess {
	
	private static final Logger log = LoggerFactory.getLogger(GeneratePDFsWorkflowStep.class);
	
	@Reference
	private transient AdobePDFApiIntegrationConfig getCredentials;
	
	@Reference
	private transient AdobeSignConfig adobeSignConfig;
		
	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		log.info("Generate multiple pdfs method...");
		String xmlVariableName = metaDataMap.get("xmlVariableName", String.class);
		String formIdVariableName = metaDataMap.get("formIdVariableName", String.class);
		String pdfVariableName = metaDataMap.get("pdfVariableName", String.class);
		
		WorkflowData data = item.getWorkflowData();
		MetaDataMap metaData = data.getMetaDataMap();
				
		String[] xmls = metaData.get(xmlVariableName,String[].class);
		String[] formIds = metaData.get(formIdVariableName,String[].class);
		Document[] docs = new Document[formIds.length];
		
		ResourceResolver resolver = session.adaptTo(ResourceResolver.class);
		String serverName = adobeSignConfig.getServer();
		log.info("===> PDF generation server: " + serverName);
		for(int i=0; i<formIds.length; i++) {
			String xml = xmls[i];
			String formId = formIds[i];
			String xdpPath = "/content/dam/formsanddocuments/aflacapps/masterapp-designs/" + formId + ".pdf";
			Resource resourseXDP = resolver.getResource(xdpPath);
			log.info("XDP ==>" + resourseXDP);
			Asset asset = resourseXDP.adaptTo(Asset.class);
			Resource original = asset.getOriginal();
			InputStream xdpIS = original.adaptTo(InputStream.class);
			byte[] xdpTemplateBytes;
			
			try(CloseableHttpClient httpclient = HttpClients.createDefault();){
				xdpTemplateBytes = IOUtils.toByteArray(xdpIS);
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				builder.addBinaryBody("data", xml.getBytes(), ContentType.DEFAULT_BINARY, "data.xml");
				builder.addBinaryBody("template", xdpTemplateBytes, ContentType.DEFAULT_BINARY, "template.xdp");
				HttpPost httpPost = new HttpPost(Utility.getDocumentGenerationURL(serverName));
				httpPost.addHeader("Authorization", "Bearer " + getToken());
				HttpEntity entity = builder.build();
				httpPost.setEntity(entity);
				
				CloseableHttpResponse outputResponse = httpclient.execute(httpPost);
				int statusCode = outputResponse.getStatusLine().getStatusCode();
				log.info("PDF GEN API Statuscode| " + statusCode);
				if(statusCode == 200) {
					InputStream is = outputResponse.getEntity().getContent();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(is, baos);
					InputStream cp = new ByteArrayInputStream(baos.toByteArray());
					Document doc = new Document(cp);
					docs[i] = doc;
				}
			} catch (IOException e) {
				log.error("Error in xdpTemplate: ", e);
				session.terminateWorkflow(item.getWorkflow());
			}
		}
		//setting workflow variable
		metaData.put(pdfVariableName, docs);
	}
	
	private String getToken() {
		CredentialUtilites cu = new CredentialUtilites(getCredentials);
		String accessToken = cu.getAccessToken();
		return accessToken;
	}
	
}
