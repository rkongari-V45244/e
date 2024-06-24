package com.aflac.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;
import java.io.IOException;
import java.util.List;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aflac.core.experience.api.model.ApiMasterAppData;
import com.aflac.core.experience.api.model.AuditUserActivity;
import com.aflac.core.experience.api.model.MasterAppSeriesData;
import com.aflac.core.osgi.service.MasterAppService;
import com.aflac.core.osgi.service.impl.MasterAppServiceImpl;
import com.aflac.core.util.AflacAppsHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST,GET,PUT", SLING_SERVLET_EXTENSIONS + "=json",
		SLING_SERVLET_PATHS + "=/bin/MasterAppServlet" })
public class MasterAppServlet extends SlingAllMethodsServlet {
	
	public MasterAppServlet() {
	}
	
	public MasterAppServlet(MasterAppService masterAppService) {
		this.masterAppService = masterAppService;
	}
	
	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(MasterAppServlet.class);
	private final String  Dam_Path = "/content/dam/aflacapps/master-app-attchments";
	
	@Reference
	private transient MasterAppService masterAppService;

	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = null;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		try {
			if(request.getParameter("groupNumber") != null && !request.getParameter("groupNumber").isEmpty() && request.getParameter("effectiveDate") != null && !request.getParameter("effectiveDate").isEmpty()) {
				
				if(request.getParameter("mode").equalsIgnoreCase("fetch")) {
					jsonStr = Obj.writeValueAsString(masterAppService.getMasterAppData(request.getParameter("groupNumber"),request.getParameter("effectiveDate")));
					response.getWriter().write(jsonStr);
				}
				
				else if(request.getParameter("mode").equalsIgnoreCase("update")) {
					String masterAppData = Obj.writeValueAsString(masterAppService.getMasterAppData(request.getParameter("groupNumber"),request.getParameter("effectiveDate")));
					String addedProducts = request.getParameter("addedProducts");
					String deletedProducts = request.getParameter("deletedProducts");
					jsonStr = Obj.writeValueAsString(masterAppService.updateMasterAppProducts(masterAppData,addedProducts,deletedProducts));
					response.getWriter().write(jsonStr);
				}
			}
			else if(request.getParameter("situs") != null && !request.getParameter("situs").isEmpty() && request.getParameter("groupType") != null 
					&& !request.getParameter("groupType").isEmpty()) {
				if(request.getParameter("mode").equalsIgnoreCase("fetchSeries")) {
					jsonStr = Obj.writeValueAsString(masterAppService.getMasterAppSeriesData(request.getParameter("situs"),request.getParameter("groupType"),request.getParameter("census")));
					response.getWriter().write(jsonStr);
				}
			}
			else if(request.getParameter("mode").equalsIgnoreCase("addProducts")) {
				String addedProducts = request.getParameter("addedProducts");
				if(addedProducts!=null && !addedProducts.isEmpty()) {
					jsonStr = Obj.writeValueAsString(masterAppService.addProducts(addedProducts));
					response.getWriter().write(jsonStr);
				}
			}
			else if(request.getParameter("mode").equalsIgnoreCase("getOptionalFeatures")) {
				jsonStr = masterAppService.getOptionalFeatures();
				response.getWriter().write(jsonStr);
			}
			
			else if(request.getParameter("mode").equalsIgnoreCase("getFiles")) {
				List<String> assetNameList = AflacAppsHelper.getAttachmentFiles(request);
				jsonStr = Obj.writeValueAsString(assetNameList);
				response.getWriter().write(jsonStr);
			}
		}
		catch (Exception e) {
			throw new IOException("Unable to get the MaterAppData", e);
		}
	}
	
	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		String jsonStr = null;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		ObjectMapper Obj = new ObjectMapper();
		try {
			if(request.getParameter("mode").equalsIgnoreCase("save")) {
				AuditUserActivity audit = new AuditUserActivity();
				ResourceResolver resourceResolver = request.getResourceResolver();
				Session session = resourceResolver.adaptTo(Session.class);
				String userId = session.getUserID();
				audit.setUserId(userId);
				audit.setApplicationName("Master Application");
				String startDateTime = request.getParameter("startDateTime");
				String[] queryDateTime = startDateTime.split(" ");
				String startDate = queryDateTime[0] + " " + queryDateTime[1];
				String zone = queryDateTime[2];
				audit.setQueryDateTime(startDate);
				String formData = request.getParameter("formData");
				ApiMasterAppData apiMasterAppData = new MasterAppServiceImpl().getApiMasterAppData(AflacAppsHelper.ConvertMasterAppXmlToJava(formData));
				jsonStr = Obj.writeValueAsString(masterAppService.saveMasterAppData(apiMasterAppData, audit, zone));
				response.getWriter().write(jsonStr);
			}
			else if(request.getParameter("mode").equalsIgnoreCase("download")) {
				String formId = request.getParameter("formId");
				String formData = request.getParameter("formData");
				MasterAppSeriesData masterAppSeriesData = Obj.readValue(request.getParameter("seriesData"),MasterAppSeriesData.class);
				masterAppService.downloadPdf(formData, formId, request, response, masterAppSeriesData);			
			}
			
			else if(request.getParameter("mode").equalsIgnoreCase("downloadFile")) {
				String docId = request.getParameter("docId");
				masterAppService.downloadFileFromFileNet(docId,response);
			}
		} catch (Exception e) {
			throw new IOException("Unable to get the MaterAppData", e);
		}
	}
}
