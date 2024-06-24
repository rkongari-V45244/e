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
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/AdobeSignWorkflow" })
public class AdobeSignWorkflowServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(AdobeSignWorkflowServlet.class);

	@Reference
	private transient ResourceResolverFactory resolverFactory;
	@Reference
	private transient AdobePDFApiIntegrationConfig getCredentials;

	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		log.info("----Entering to AdobeSignWorkflowServlet----");
		String jsonStr = null;
		Boolean status =false;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			
			ResourceResolver resourceResolver = request.getResourceResolver();
			Session session = resourceResolver.adaptTo(Session.class);
			WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);
			ObjectMapper Obj = new ObjectMapper();
			String groupNumber = request.getParameter("groupNumber");
			String coverageEffectiveDate = request.getParameter("coverageEffectiveDate");
			String server = request.getServerName();
			log.info("------ Request Params received => Group no: " + groupNumber + " | CoverageEffDt: " + coverageEffectiveDate + " | Server: " + server);
			// Invoking workflow
			WorkflowModel wfModel = workflowSession.getModel("/var/workflow/models/masterapp-signature-wf1");
			log.info("----Workflow Model: " + wfModel);
			
			if((server.contains("localhost") || server.contains("author")) && wfModel!=null) {
				log.info("-----Workflow triggered on Author Instance: {} ------", server);
				log.info("JCR_PATH : "+PayloadInfo.PAYLOAD_TYPE.JCR_PATH.name());
				log.info("Resource Path :"+ request.getResource().getPath().split("/bin")[0]);
				log.info("WorkfFlow Model Id:"+wfModel.getId());
				
				WorkflowData wfData = workflowSession.newWorkflowData(PayloadInfo.PAYLOAD_TYPE.JCR_PATH.name(),request.getResource().getPath().split("/bin")[0] + wfModel.getId());
				
				// Getting variable names from the workflow Model
				String formXmlData = wfModel.getVariableTemplates().get("formXmlData").getName();
				MetaDataMap metaData = wfData.getMetaDataMap();
				
				// Setting metaData variables along with the values
				metaData.put(formXmlData, request.getParameter("formData"));
				metaData.put("workflowTitle", wfModel.getTitle()+"#"+groupNumber+"#"+coverageEffectiveDate+"#"+System.currentTimeMillis());
				
				String metaDataStr = Obj.writeValueAsString(metaData);
				log.info("Workflow MetaData :"+ metaDataStr);
				log.info("----Adobe Sign Workflow Started----");
				
				workflowSession.startWorkflow(wfModel, wfData, metaData);
				status = true;
				jsonStr = "{\"status\":" + status +"}";
			}else if(server.contains("publish")) {
				log.info("-----Workflow triggered on Publish Instance: {} ------", server);
				String postUrl = "https://" + server.replace("publish", "author") + "/bin/AdobeSignWorkflow";
				log.info("---- API call to Author instance: {} ---", postUrl);
				HttpPost httpPost = new HttpPost(postUrl);
				httpPost.addHeader("Authorization", "Bearer " + getToken());
				
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				builder.addTextBody("groupNumber", groupNumber);
				builder.addTextBody("coverageEffectiveDate", coverageEffectiveDate);
				builder.addTextBody("formData", request.getParameter("formData"));
				httpPost.setEntity(builder.build());
								
				CloseableHttpResponse outputResponse = httpclient.execute(httpPost);
				int statusCode = outputResponse.getStatusLine().getStatusCode();
				log.info("----Author Workflow Servlet response: " + statusCode);
				log.info(outputResponse.getStatusLine().getReasonPhrase());
				if(statusCode == 200)
					status = true;
				jsonStr = "{\"status\":" + status +"}";
			} else {
				log.info("----Workflow Model Not Found----");
				jsonStr = "{\"status\":" + status +"}";
			}
			session.save();
			session.logout();
			log.info("----Exiting from AdobeSignWorkflowServlet----");
			response.getWriter().write(jsonStr);
			
		} catch (Exception e) {
			jsonStr = "{\"status\":" + status +"}";
			log.info("Info Error in AdobeSignWorkflowServlet: ", e);
			log.error("Error in AdobeSignWorkflowServlet: ", e);
			response.getWriter().write(jsonStr);
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
