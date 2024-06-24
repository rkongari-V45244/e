package com.aflac.core.osgi.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface FileNetService {

	public String saveFileOnFileNet(String metaData, InputStream isPDF);
	
	public String bullUploadOnFileNet(List<String> metaData, List<InputStream> isPDFs) throws IOException;

	public void getFileFromFileNet(String docID, String objectStore, SlingHttpServletResponse response);
	
	public boolean saveMetaDataToDB(String docType, String metaData);
	
	public InputStream getPdfStream();

	public void getFileFromFileNetWorkFlow(String docID, String objectStore, SlingHttpServletResponse response) throws IOException;
}
