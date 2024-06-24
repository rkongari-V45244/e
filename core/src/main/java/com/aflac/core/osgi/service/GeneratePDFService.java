package com.aflac.core.osgi.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface GeneratePDFService {

	public void generatePDF(String formData, String xdpPath, SlingHttpServletRequest request, SlingHttpServletResponse response, String pdfCase, String activity, String app) throws IOException;
	
	public InputStream getPdfStream();
}
