package com.aflac.core.osgi.service;

import java.time.LocalDateTime;

import org.osgi.annotation.versioning.ProviderType;

import com.aflac.core.experience.api.model.AuditUserActivity;

@ProviderType
public interface AuditService {

	public boolean saveAuditEntry(AuditUserActivity audit);
	
	public String getTurnAroundTime(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
