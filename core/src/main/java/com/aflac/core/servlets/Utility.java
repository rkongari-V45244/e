package com.aflac.core.servlets;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.http.entity.ContentType;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;

public class Utility {

	private static final String DEFAULT_DOCASSEMBLE_ENDPOINT = "https://author-p60469-e738775.adobeaemcloud.com/adobe/forms/assembler/ddx/invoke";

	private static final String DEFAULT_DOCGEN_ENDPOINT = "https://author-p60469-e738775.adobeaemcloud.com/adobe/forms/doc/v1/generatePDFOutput";

	public static void saveXML(final SlingHttpServletRequest request) {
		ResourceResolver resourceResolver = request.getResourceResolver();
		byte[] xmlIS = request.getParameter("formData").getBytes(StandardCharsets.UTF_8);
		AssetManager am = resourceResolver.adaptTo(AssetManager.class);
		am.createAsset("/content/dam/formsanddocuments/aflac/output/datajson.xml", new ByteArrayInputStream(xmlIS),
				ContentType.APPLICATION_XML.getMimeType(), true);
	}

	public static String savePDF(final SlingHttpServletRequest request, InputStream pdfStream) {
		ResourceResolver resourceResolver = request.getResourceResolver();
		AssetManager am = resourceResolver.adaptTo(AssetManager.class);
		Asset assetPDF = am.createAsset("/content/dam/formsanddocuments/aflac/output/output.pdf", pdfStream,
				"application/pdf", true);
		String pdfPath = assetPDF.getPath();
		return pdfPath;
	}

	public static String getDocumentGenerationURL(String serverName) {
		String docGenEndpoint = DEFAULT_DOCGEN_ENDPOINT;

		if (!serverName.equalsIgnoreCase("localhost")) {
			docGenEndpoint = "https://" + serverName + "/adobe/forms/doc/v1/generatePDFOutput";
		}

		return docGenEndpoint;
	}

	public static String getDocumentAssembleURL(String serverName) {
		String docGenEndpoint = DEFAULT_DOCASSEMBLE_ENDPOINT;
		if (!serverName.equalsIgnoreCase("localhost")) {
			docGenEndpoint = "https://" + serverName + "/adobe/forms/assembler/ddx/invoke";
		}
		return docGenEndpoint;
	}
}