package com.aflac.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;
import java.io.IOException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowModel;
import com.adobe.granite.workflow.payload.PayloadInfo;
import com.aflac.core.config.AdobePDFApiIntegrationConfig;
import com.aflac.core.services.CredentialUtilites;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST,GET,PUT",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/VoidAgreement" })
public class VoidAgreementServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(VoidAgreementServlet.class);

	@Reference
	private transient ResourceResolverFactory resolverFactory;
	@Reference
	private transient AdobePDFApiIntegrationConfig getCredentials;
	
	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {

			log.info("----Inside VoidAgreementServlet----");
			ResourceResolver resourceResolver = request.getResourceResolver();
			Session session = resourceResolver.adaptTo(Session.class);
			WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);
			ObjectMapper Obj = new ObjectMapper();
			String server = request.getServerName();
			
			// Invoking workflow
			WorkflowModel wfModel = workflowSession.getModel("/var/workflow/models/Void-Agreement-Workflow");
			log.info("----Void Workflow Model: " + wfModel);
			
			if((server.contains("localhost") || server.contains("author")) && wfModel!=null) {
				WorkflowData wfData = workflowSession.newWorkflowData(PayloadInfo.PAYLOAD_TYPE.JCR_PATH.name(),request.getResource().getPath().split("/bin")[0] + wfModel.getId());
				
				// Getting variable names from the workflow Model
				String masterAppId = wfModel.getVariableTemplates().get("masterAppId").getName();
				String coverageEffectiveDate = wfModel.getVariableTemplates().get("coverageEffectiveDate").getName();
				String formId = wfModel.getVariableTemplates().get("formId").getName();
				String agreementId = wfModel.getVariableTemplates().get("agreementId").getName();
				String masterAppVersion = wfModel.getVariableTemplates().get("masterAppVersion").getName();
				MetaDataMap metaData = wfData.getMetaDataMap();
				
				// Setting metaData variables along with the values
				metaData.put(masterAppId, request.getParameter("masterAppId"));
				metaData.put(coverageEffectiveDate, request.getParameter("coverageEffectiveDate"));
				metaData.put(formId, request.getParameter("formId"));
				metaData.put(agreementId, request.getParameter("agreementId"));
				metaData.put(masterAppVersion, request.getParameter("masterAppVersion"));
				
				String metaDataStr = Obj.writeValueAsString(metaData);
				log.info("Workflow MetaData :"+ metaDataStr);
				log.info("----Void Agreement workflow Started----");
				
				workflowSession.startWorkflow(wfModel, wfData, metaData);
			} else if(server.contains("publish")) {
				log.info("-----Void Workflow triggered on Publish Instance: {} ------", server);
				String postUrl = "https://" + server.replace("publish", "author") + "/bin/VoidAgreement";
				log.info("---- API call to Author instance: {} ---", postUrl);
				HttpPost httpPost = new HttpPost(postUrl);
				httpPost.addHeader("Authorization", "Bearer " + getToken());
				
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				builder.addTextBody("masterAppId", request.getParameter("masterAppId"));
				builder.addTextBody("coverageEffectiveDate", request.getParameter("coverageEffectiveDate"));
				builder.addTextBody("formId", request.getParameter("formId"));
				builder.addTextBody("agreementId", request.getParameter("agreementId"));
				builder.addTextBody("masterAppVersion", request.getParameter("masterAppVersion"));
				httpPost.setEntity(builder.build());
								
				CloseableHttpResponse outputResponse = httpclient.execute(httpPost);
				int statusCode = outputResponse.getStatusLine().getStatusCode();
				log.info("----Author Void Workflow Servlet response: " + statusCode);
				log.info(outputResponse.getStatusLine().getReasonPhrase());
			} else {
				log.info("----Void Workflow Model Not Found----");
			}
			session.save();
			session.logout();
			
		} catch (Exception e) {
			log.error("Error in VoidAgreementServlet: ", e);
		} finally {
			httpclient.close();
		}
	}
	
	private String getToken() {
		CredentialUtilites cu = new CredentialUtilites(getCredentials);
		String accessToken = cu.getAccessToken();
		return accessToken;
	}
}
