package com.aflac.core.osgi.service;

import java.io.UnsupportedEncodingException;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface BrochureMaintenanceService {

	public String getBrochureData(String lob, String series, String situs, String lang) throws UnsupportedEncodingException;
	
	public String saveBrochureData(String data);
}
