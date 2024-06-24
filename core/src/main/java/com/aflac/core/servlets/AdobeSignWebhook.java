package com.aflac.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.query.Query;
import javax.jcr.version.VersionException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.services.AdobeDocumentSign;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST, GET",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/AdobeSignWebhook" })

public class AdobeSignWebhook extends SlingAllMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(AdobeSignWebhook.class);
	

	@Reference
	private transient ResourceResolverFactory resourceResolverFactory;

	private static final String SERVICE_USER_ACCOUNT = "aflac-service-user";

	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		// Send response for webhook activation
		String clientID = request.getHeader("X-ADOBESIGN-CLIENTID");

		JsonObject id = new JsonObject();
		id.addProperty("xAdobeSignClientId", clientID);
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(id.toString());
	}

	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		// Print all params
		log.info("Printing Webhook Response Starts ==>");
		// Start
		String body = null;
		StringBuilder stringBuilder = new StringBuilder();

		try (InputStream inputStream = request.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
			char[] charBuffer = new char[128];
			int bytesRead = -1;
			while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
				stringBuilder.append(charBuffer, 0, bytesRead);
			}
		}

		body = stringBuilder.toString();
		log.info("Adobe Sign Print POST Body ==> " + body);

		// Store the Agreement in CRX path

		JsonReader jsonReader = new JsonReader(new StringReader(body));
		JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
		String agreementStatus = jsonObject.get("event").getAsString();

		if (agreementStatus.equals("AGREEMENT_ACTION_COMPLETED")) // If signed
		{
			JsonElement agreementIDElement = jsonObject.get("agreement");
			String signedDT = jsonObject.get("eventDate").getAsString();
			String agreementID = agreementIDElement.getAsJsonObject().get("id").getAsString();

			AdobeDocumentSign as = new AdobeDocumentSign();
			// Store
			final Map<String, Object> serviceUserSession = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,
					SERVICE_USER_ACCOUNT);
			ResourceResolver resourceResolver = null;
			try {
				resourceResolver = resourceResolverFactory.getServiceResourceResolver(serviceUserSession);
				if(resourceResolver.isLive()) {
					throw new IOException("Unable to get ResourceResolver for Service User");
				}
			} catch (LoginException e) {
				log.error("Login Exception in AdobeSignWebhook():"+e);
				throw new IOException("Unable to get Login using Service User",e);
			}
			String storePath = as.saveDocumentafterSigning(agreementID,
					"/content/dam/formsanddocuments/aflac/signed-agreements", resourceResolver);
			// Search Group proposal and update document status
			
			String query = "SELECT * FROM [sling:OrderedFolder] AS node WHERE ISDESCENDANTNODE(node, '/content/dam/formsanddocuments/aflac/output') AND PROPERTY(node.[AgreementID], 'STRING') ='"
					+ agreementID + "'";
			Iterator<Resource> result = resourceResolver.findResources(query, Query.JCR_SQL2);
			
			while (result.hasNext()){
				Resource res = result.next();
				Resource resource = resourceResolver.getResource(res.getPath());
				javax.jcr.Node node = resource.adaptTo(javax.jcr.Node.class);
				try {
					node.setProperty("Status", "Signed");
					node.setProperty("AgreementFilePath", storePath);
					node.setProperty("SignedDateTime", signedDT);
					node.getSession().save();
				} catch (ValueFormatException e) {
					log.error("ValueFormatException in AdobeSignWebhook():"+e);
					throw new IOException("ValueFormatException:"+e);
				} catch (VersionException e) {
					log.error("VersionException in AdobeSignWebhook():"+e);
					throw new IOException("VersionException:"+e);
				} catch (LockException e) {
					log.error("LockException in AdobeSignWebhook():"+e);
					throw new IOException("LockException:"+e);
				} catch (ConstraintViolationException e) {
					log.error("ConstraintViolationException in AdobeSignWebhook():"+e);
					throw new IOException("ConstraintViolationException:"+e);
				} catch (RepositoryException e) {
					log.error("RepositoryException in AdobeSignWebhook():"+e);
					throw new IOException("RepositoryException:"+e);
				}
				finally {
					if(resourceResolver!=null) {
						resourceResolver.close();
					}
					
				}
			}
			// Update complete
		}

		// End

	}

}