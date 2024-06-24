package com.aflac.core.osgi.service;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.annotation.versioning.ProviderType;

import com.aflac.core.experience.api.model.AuditUserActivity;

@ProviderType
public interface AssemblePDFService {

	public int generatePDFfromURLList(String pdfURLList, String serverName, SlingHttpServletResponse response, ResourceResolver resolver, String mode, AuditUserActivity audit, String documentNames);

	public void generatePDFfromCRX(String[] pdfList, String serverName, ResourceResolver resolver, SlingHttpServletResponse response, String productSeries, String language);

	public void downloadDisclosure(String fileName, SlingHttpServletRequest request, SlingHttpServletResponse response);

	public void getFileFromFileNet(String docId, SlingHttpServletResponse response);
}
