package com.aflac.core.osgi.service;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ComboAppMatrixService {
	
	public String getComboAppData(String situs, String formID);
	
	public String getMasterAppData(String situs, String formID);
	
	public String getComboAppAddData();

	public String saveComboAppData(String formData);

	public String getFormIds(String situs);
}
