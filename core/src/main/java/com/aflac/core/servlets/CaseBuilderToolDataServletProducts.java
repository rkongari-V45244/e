package com.aflac.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.IOException;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.experience.api.model.AuditUserActivity;
import com.aflac.core.experience.api.model.SaveCasebuildResponse;
import com.aflac.core.osgi.service.CaseBuilderService;
import com.aflac.core.services.CaseBuilderToolService;
import com.aflac.core.util.DocumentUtil;
import com.aflac.xml.casebuilderProducts.models.AccountInformation;
import com.aflac.xml.casebuilderProducts.models.CaseBuilderForm;
import com.aflac.xml.casebuilderProducts.models.Products;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST,GET,PUT", SLING_SERVLET_EXTENSIONS + "=json",
		SLING_SERVLET_PATHS + "=/bin/GetCaseBuilderToolProductsData" })
public class CaseBuilderToolDataServletProducts extends SlingAllMethodsServlet {
	
	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(CaseBuilderToolDataServlet.class);
	private static String GuideDocs_DAM_Path = "/content/dam/aflacapps/case-build/guide-docs/";

	public CaseBuilderToolDataServletProducts(CaseBuilderService caseBuildService) {
		this.caseBuildService = caseBuildService;
	}
	
	public CaseBuilderToolDataServletProducts() {}

	@Reference
	private transient ResourceResolverFactory resourceResolverFactory;
	
	@Reference
	private transient CaseBuilderService caseBuildService;

	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		//AccountInformation accountInformation = new AccountInformation();
		Products products = new Products();
		//CaseBuilderToolService caseBuilderToolService = new CaseBuilderToolService();
		CaseBuilderForm caseBuilderForm = new CaseBuilderForm();
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = null;
		SaveCasebuildResponse saveCasebuildResponse = null;
		try {
			if (request.getParameter("groupNumber") != null && !request.getParameter("groupNumber").isEmpty()
					&& request.getParameter("effectiveDate") != null && !request.getParameter("effectiveDate").isEmpty()
					&& request.getParameter("enrollmentPlatform") != null
					&& !request.getParameter("enrollmentPlatform").isEmpty()) {
				
				if(request.getParameter("mode").equalsIgnoreCase("edit")) {
//					caseBuilderToolService.getAccountInformationDataProducts(accountInformation,request);
//					caseBuilderForm.setAccountInformation(accountInformation);
//					caseBuilderToolService.getProductsData(products);
//					caseBuilderForm.setAccountInformation(accountInformation);
//					caseBuilderForm.setProducts(products);
//					caseBuilderForm.setCoverageEffectiveDate("2022-08-01");
//					caseBuilderForm.setGroupNo(request.getParameter("groupNumber"));
//					caseBuilderForm.setEnrollmentPlatform("Ease");
					
					jsonStr = Obj.writeValueAsString(caseBuildService.getCaseBuilderForm(request.getParameter("groupNumber"),
					        request.getParameter("effectiveDate"), request.getParameter("enrollmentPlatform")));
				}
				else if(request.getParameter("mode").equalsIgnoreCase("update")) {
					jsonStr = Obj.writeValueAsString(caseBuildService.getCaseBuilderForm(request.getParameter("groupNumber"),
					        request.getParameter("effectiveDate"), request.getParameter("enrollmentPlatform")));
					//String caseBuildData = request.getParameter("caseBuildData");
					String addedProducts = request.getParameter("addedProducts");
					String deletedProducts = request.getParameter("deletedProducts");
					jsonStr = Obj.writeValueAsString(caseBuildService.updateProducts(jsonStr,addedProducts,deletedProducts));
				}
				else if(request.getParameter("mode").equalsIgnoreCase("checkEffectiveDate")) {
					boolean status = caseBuildService.checkExistingCase(request.getParameter("groupNumber"),request.getParameter("effectiveDate"), request.getParameter("enrollmentPlatform"));
					jsonStr = "{\"status\":" + status +"}";
				}
				else {
					products = caseBuildService.getAvailableProducts(request);
					caseBuilderForm.setProducts(products);
					jsonStr = Obj.writeValueAsString(caseBuilderForm);
				}
				//jsonStr = Obj.writeValueAsString(caseBuilderForm);
				response.getWriter().write(jsonStr);
			}
			else if(request.getParameter("mode").equalsIgnoreCase("save")) {
				AuditUserActivity audit = new AuditUserActivity();
				ResourceResolver resourceResolver = request.getResourceResolver();
				Session session = resourceResolver.adaptTo(Session.class);
				String userId = session.getUserID();
				audit.setUserId(userId);
				audit.setApplicationName("Case Build");
				String startDateTime = request.getParameter("startDateTime");
				String[] queryDateTime = startDateTime.split(" ");
				String startDate = queryDateTime[0] + " " + queryDateTime[1];
				String zone = queryDateTime[2];
				audit.setQueryDateTime(startDate);
				String formData = request.getParameter("formData");
				saveCasebuildResponse = caseBuildService.saveCaseBuilderForm(formData, audit, zone);
				jsonStr = Obj.writeValueAsString(saveCasebuildResponse);
				response.getWriter().write(jsonStr);
			} 
			else if(request.getParameter("mode").equalsIgnoreCase("applicationNo")) {
				String situs = request.getParameter("situs");
				String series = request.getParameter("series");
				String groupType = request.getParameter("groupType");
				Boolean giAmount = Boolean.parseBoolean(request.getParameter("giAMount"));
				String applicationNo = caseBuildService.getFormId(situs, series,groupType,giAmount);
				response.getWriter().write(applicationNo);
			}
			else if(request.getParameter("mode").equalsIgnoreCase("issueAge")) {
				String situs = request.getParameter("situs");
				String series = request.getParameter("series");
				String term = request.getParameter("term");
				String lob = request.getParameter("lob");
				String issueAge = caseBuildService.getIssueAges(situs, series, term,lob);
				response.getWriter().write(issueAge);
			} else if(request.getParameter("mode").equalsIgnoreCase("ageBasedOn")) {
				String lob = request.getParameter("lob");
				String platform = request.getParameter("platform");
				String ageCal = request.getParameter("ageCal");
				String ageMethod = caseBuildService.getAgeBasedOn(lob, platform, ageCal);
				response.getWriter().write(ageMethod);
			}
			else if(request.getParameter("mode").equalsIgnoreCase("fetchPlatforms")) {
				jsonStr = Obj.writeValueAsString(caseBuildService.getEnrollmentDetails());
				response.getWriter().write(jsonStr);
			}
			else if(request.getParameter("mode").equalsIgnoreCase("fetchPdfDirections")) {
				String situs = request.getParameter("situs");
				jsonStr = caseBuildService.getPdfDirections(situs);
				response.getWriter().write(jsonStr);
			}
			else if(request.getParameter("mode").equalsIgnoreCase("platformGuide")) {
				String fileName = request.getParameter("file");
				DocumentUtil.downloadDamDocument(fileName, request, response,GuideDocs_DAM_Path);
			}
			else {
				log.info("groupNumber or effectiveDate is not passed in the request");
			}

		} catch (IOException e) {
			throw new IOException("Unable to get the GetCaseBuilderToolData", e);
		}
	}

}
