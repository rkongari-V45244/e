package com.aflac.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
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
import org.w3c.dom.Document;

import com.aflac.core.osgi.service.GeneratePDFService;
import com.aflac.core.services.AdobeDocumentSign;
import com.aflac.core.util.AflacAppsHelper;
import com.aflac.core.util.DocumentUtil;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=POST",
		SLING_SERVLET_EXTENSIONS + "=json", SLING_SERVLET_PATHS + "=/bin/CreatePDF" })

public class CreatePDF extends SlingAllMethodsServlet {

	private static final String XDP_PATH = "/content/dam/formsanddocuments/aflacapps/group-master-application-pdf-templates/";

	private static final long serialVersionUID = 1L;

	private static final String SERVICE_USER_ACCOUNT = "aflac-service-user";

	private transient Logger log = LoggerFactory.getLogger(CreatePDF.class);

	@Reference
	private transient ResourceResolverFactory resourceResolverFactory;
	
	@Reference
	private transient GeneratePDFService pdfService;
	
	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {

		String mode = request.getParameter("mode");

		switch (mode) {
		case "preview":
			preview(request, response);
			break;
		case "download":
			download(request, response);
			break;
		case "save": {
			save(request, response);
			break;
		}
		default:
			throw new ServletException("Only 3 mode supported");
		}
	}

	private void save(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

		final Map<String, Object> serviceUserSession = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,
				SERVICE_USER_ACCOUNT);
		ResourceResolver resourceResolver = null;
		Document doc = DocumentUtil.convertToDocument(request.getParameter("formData"));
		String formId = DocumentUtil.getValue("/afData/afBoundData/groupMasterData/formID", doc);
		if (formId == null) {
			formId = "C02204";
		}

		String grpProposalNum = DocumentUtil.getValue("/afData/afBoundData/groupMasterData/groupProposalNo", doc);
		try {
			resourceResolver = resourceResolverFactory.getServiceResourceResolver(serviceUserSession);
			AssetManager am = resourceResolver.adaptTo(AssetManager.class);

			String xdpPath = XDP_PATH + formId + ".xdp";
			byte[] dataXMLBytes = request.getParameter("formData").getBytes();

			Asset assetFormData = am.createAsset(AflacAppsHelper.ASSETPATH + grpProposalNum + "/datajson.xml",
					new ByteArrayInputStream(dataXMLBytes), ContentType.APPLICATION_XML.getMimeType(), true);

			log.info("SAVING PROPOSAL|grpProposalNum|" + assetFormData.getPath());
			String groupProposalPath = AflacAppsHelper.ASSETPATH + grpProposalNum;// + "/jcr:content";
			String groupMasterData = DocumentUtil.getNode("/afData/afBoundData/groupMasterData", doc);

			pdfService.generatePDF(groupMasterData, xdpPath, request, response,"null", "read", "");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(pdfService.getPdfStream(), baos);

			log.info("save");
			Asset assetPDF = am.createAsset(AflacAppsHelper.ASSETPATH + grpProposalNum + "/output.pdf",
					new ByteArrayInputStream(baos.toByteArray()), "application/pdf", true);
			String pdfPath = assetPDF.getPath();
			// Test Sign

			AdobeDocumentSign as = new AdobeDocumentSign();
			String transientDocumentID = as.getTransientDocumentID(new ByteArrayInputStream(baos.toByteArray()));
			String recipientEmailID = DocumentUtil.getValue("/afData/afUnboundData/data/emailId", doc); // Set this for sending
																							// out for signatures
			String agreementID = as.sendDocumentforSigning(transientDocumentID, recipientEmailID);
			// Ends

			Resource resource = resourceResolver.getResource(groupProposalPath);
			javax.jcr.Node node = resource.adaptTo(javax.jcr.Node.class);
			try {
				node.setProperty("AgreementID", agreementID);
				node.setProperty("Status", "Pending");
				node.setProperty("AgreementFilePath", "");
				node.getSession().save();
			} catch (ValueFormatException e) {
				e.printStackTrace();
			} catch (VersionException e) {
				e.printStackTrace();
			} catch (LockException e) {
				e.printStackTrace();
			} catch (ConstraintViolationException e) {
				e.printStackTrace();
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
			OutputStream responseOutputStream = response.getOutputStream();
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=" + grpProposalNum + ".pdf");
			IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), responseOutputStream);
			responseOutputStream.flush();
			responseOutputStream.close();
		} catch (LoginException e) {
			throw new IOException("Can't Write to DAM", e);
		} finally {
			if (resourceResolver != null)
				resourceResolver.close();
		}
	}

	private void download(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		// Fetch Data from CRX
		ResourceResolver resolver = request.getResourceResolver();
		Resource res = null;
		String agreementFilePath = request.getParameter("agreementFilePath");
		if(agreementFilePath!=null && !agreementFilePath.isEmpty()) {
			res = resolver.getResource(request.getParameter("agreementFilePath"));
		}
		else {
			res = resolver.getResource(AflacAppsHelper.ASSETPATH + request.getParameter("searchParam") + "/output.pdf");
		}
		
		Asset asset = res.adaptTo(Asset.class);
		Resource original = asset.getOriginal();
		InputStream xdpIS = original.adaptTo(InputStream.class);

		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "attachment; filename=output.pdf");
		OutputStream responseOutputStream = response.getOutputStream();
		IOUtils.copy(xdpIS, responseOutputStream);
		responseOutputStream.flush();
		responseOutputStream.close();
	}

	private void preview(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

		Document doc = DocumentUtil.convertToDocument(request.getParameter("formData"));

		String formId = DocumentUtil.getValue("/afData/afBoundData/groupMasterData/formID", doc);
		if (formId == null)
			formId = "C02204";
		String groupMasterData = DocumentUtil.getNode("/afData/afBoundData/groupMasterData", doc);
		String xdpPath = XDP_PATH + formId + ".xdp";
		pdfService.generatePDF(groupMasterData, xdpPath, request, response,"null", "read", "CaseBuild Application");
		log.info("Preview");
	}
}