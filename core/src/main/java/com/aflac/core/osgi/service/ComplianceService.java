package com.aflac.core.osgi.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.osgi.annotation.versioning.ProviderType;

import com.aflac.core.experience.api.model.LabelSequence;
import com.aflac.xml.casebuilderProducts.models.EmploymentEnrollmentDisclosure;

@ProviderType
public interface ComplianceService {

	String getComplianceData(String situsState, String lob, String type, String label);

	String getInitComplianceData(String referenceDataItem);

	String addComplianceData(String formData);

	String updateComplianceData(String formData);
	
	XSSFWorkbook exportComplianceData(String formData);

	String getEditComplianceData(String recordId);
	
	LabelSequence getLabelSequence();
	
	EmploymentEnrollmentDisclosure getComplianceDataRefill(String situsState, String lob, String type, String label, Boolean isCi22kEnabled);

}
