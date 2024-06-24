package com.aflac.core.config;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface FileNetApiConfig {

	String getFilenetCertPdk();

	String getFilenetTokenProviderURL();

	String getFilenetTokenUser();

	String getFilenetDocumentsURL();

	String getFilenetDocumentsRetrieveURL();

	String getFilenetKeyStorePass();

	String getFilenetCredsPFXPath();

	String getFilenetCredsUser();

	String getFilenetCredsUserPass();
	
	String getObjectStore();

	String getVersion();

	String getInsertData();
	
	String getAsmNotification();
	
	String getDocClass();
}
