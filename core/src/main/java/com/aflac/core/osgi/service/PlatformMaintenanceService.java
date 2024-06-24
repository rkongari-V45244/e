package com.aflac.core.osgi.service;

import org.osgi.annotation.versioning.ProviderType;

import com.aflac.core.experience.api.model.PlatformMaintenance;

@ProviderType
public interface PlatformMaintenanceService {

	public PlatformMaintenance getPlatformMaintenanceDetails(String platformId);
	
	public String savePlatformMaintenanceDetails(String formData);
	
	public String deletePlatformMaintenanceDetails(String platformId);
}
