package com.aflac.core.config;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface AdobePDFApiIntegrationConfig {

	String getIMS_ENDPOINT();

	String getCLIENT_SECRET();

	String getCLIENT_ID();

	String getMETASCOPE_ID();

	String getTECH_ACCT();

	String getORG_ID();

	String getPRIVATE_KEY();

	String getPUBLIC_KEY();

	String getEMAIL();

	String getEnvironment();
	
	String getWaterMarkText();

}
