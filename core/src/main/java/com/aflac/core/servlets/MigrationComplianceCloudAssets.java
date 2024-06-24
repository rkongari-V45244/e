package com.aflac.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.util.MigrationUtil;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/StandardComplianceCloud" })

public class MigrationComplianceCloudAssets extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final String SERVICE_USER_ACCOUNT = "aflac-service-user";

	private transient Logger log = LoggerFactory.getLogger(MigrationComplianceCloudAssets.class);

	@Reference
	private transient ResourceResolverFactory resourceResolverFactory;

	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {

		final Map<String, Object> serviceUserSession = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,
				SERVICE_USER_ACCOUNT);
		ResourceResolver resourceResolver = null;
		InputStream fileStream = null;
		InputStream dataJSONStringIS = null;
		try {
			resourceResolver = resourceResolverFactory.getServiceResourceResolver(serviceUserSession);
			RequestParameterMap requestParameterMap = request.getRequestParameterMap();
			for (Map.Entry<String, RequestParameter[]> param : requestParameterMap.entrySet()) {
				RequestParameter rpm = param.getValue()[0];
				if (!rpm.isFormField()) {
					byte[] byteStream = rpm.get();
					if (rpm.getFileName().contentEquals("data")) {
						dataJSONStringIS = new ByteArrayInputStream(byteStream);
					}
					if (rpm.getFileName().contentEquals("doc")) {
						fileStream = new ByteArrayInputStream(byteStream);
					}
					

				}
			}
			String dataJSONString = IOUtils.toString(dataJSONStringIS, StandardCharsets.UTF_8);
			JsonObject dataJSON = new JsonParser().parse(dataJSONString).getAsJsonObject();
			String fileCreateName = dataJSON.get("name").getAsString();
			MigrationUtil.createComplianceAsset(fileStream, dataJSONString, fileCreateName,resourceResolver);
		} catch (LoginException e) {
			e.printStackTrace();
		}finally {
			resourceResolver.close();
		}
		
		
	}
}