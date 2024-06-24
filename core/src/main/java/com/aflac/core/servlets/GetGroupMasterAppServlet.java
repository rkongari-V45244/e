package com.aflac.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.aflac.core.util.AflacAppsHelper;
import com.day.cq.dam.api.Asset;
import com.google.gson.JsonObject;

@Component(service = { Servlet.class }, property = { SLING_SERVLET_METHODS + "=GET", SLING_SERVLET_EXTENSIONS + "=json",
		SLING_SERVLET_PATHS + "=/bin/GetApplicationData" })
public class GetGroupMasterAppServlet extends SlingAllMethodsServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient Logger log = LoggerFactory.getLogger(GetGroupMasterAppServlet.class);

	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);

	}

	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String appID = request.getParameter("searchParam");
		String formattedDate = null;
		log.info("searchParam|" + appID);
		AflacAppsHelper aflacAppsHelper = new AflacAppsHelper();
		
		// Fetch Data from CRX
		ResourceResolver resolver = request.getResourceResolver();

		Resource res = resolver.getResource(AflacAppsHelper.ASSETPATH + appID + "/datajson.xml");
		String groupProposalPath = AflacAppsHelper.ASSETPATH + appID;// + "/jcr:content";
		Resource proposalRes = resolver.getResource(groupProposalPath);
		Asset asset = res.adaptTo(Asset.class);
		log.info("found some asset" + asset);
		Resource original = asset.getOriginal();
		InputStream xdpIS = original.adaptTo(InputStream.class);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder docBuilder;
		Document doc = null;
		try {
			docBuilder = factory.newDocumentBuilder();

			doc = docBuilder.parse(xdpIS);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new IOException("Can't parse xml", e);
		}
		final ValueMap properties = proposalRes.getValueMap();
		String agreementID = properties.get("AgreementID", String.class);
		String status = properties.get("Status", String.class);
		String signedDT = properties.get("SignedDateTime", String.class);
		SimpleDateFormat existingDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat convertingDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			formattedDate = aflacAppsHelper.convertDate(signedDT, existingDateFormat, convertingDateFormat);
		}
		catch (ParseException e) {
			throw new IOException("Can't format date", e);
		}
		String agreementFilePath = properties.get("AgreementFilePath", String.class);
		String organizationName = getValue("/afData/afBoundData/groupMasterData/organizationName", doc);
		String coverageEffectiveDate = getValue("/afData/afBoundData/groupMasterData/coverageEffectiveDate", doc);
		String groupProposalNo = getValue("/afData/afBoundData/groupMasterData/groupProposalNo", doc);

		JsonObject id = new JsonObject();
		id.addProperty("groupProposalNo", groupProposalNo);
		id.addProperty("organizationName", organizationName);
		id.addProperty("coverageEffectiveDate", coverageEffectiveDate);
		id.addProperty("agreementID", agreementID);
		id.addProperty("status", status);
		id.addProperty("agreementFilePath", agreementFilePath);
		id.addProperty("SignedDateTime", formattedDate);

		log.info("data from asset" + id.toString());
		response.setContentType("application/json");
		response.getWriter().write(id.toString());
	}

	private String getValue(String path, Document doc) throws IOException {
		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xpath = xpathfactory.newXPath();

		String value = null;
		try {
			XPathExpression expr = xpath.compile(path);
			value = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			throw new IOException("Can't retrieve value from path|"+path, e);
		}

		return value;
	}

}