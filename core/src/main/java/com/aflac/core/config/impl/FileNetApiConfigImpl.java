package com.aflac.core.config.impl;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.util.converter.Converters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.FileNetApiConfig;

@Component(service = { FileNetApiConfig.class })
public class FileNetApiConfigImpl implements FileNetApiConfig {
	private static final Logger log = LoggerFactory.getLogger(FileNetApiConfigImpl.class);

	private String filenetCertPdk;
	private String filenetTokenProviderURL;
	private String filenetTokenUser;
	private String filenetDocumentsURL;
	private String filenetDocumentsRetrieveURL;
	private String filenetKeyStorePass;
	private String filenetCredsPFXPath;
	private String filenetCredsUser;
	private String filenetCredsUserPass;
	private String objectStore;
	private String version;
	private String insertData;
	private String asmNotification;
	private String docClass;
	
	@Activate
	protected void activate(Map<String, Object> properties) {
		this.filenetCertPdk = Converters.standardConverter().convert(properties.get("filenetCertPdk")).defaultValue("not found")
				.to(String.class);

		this.filenetTokenProviderURL = Converters.standardConverter().convert(properties.get("filenetTokenProviderURL"))
				.defaultValue("not found").to(String.class);
		
		this.filenetKeyStorePass = Converters.standardConverter().convert(properties.get("filenetKeyStorePass"))
				.defaultValue("not found").to(String.class);

		this.filenetTokenUser = Converters.standardConverter().convert(properties.get("filenetTokenUser")).defaultValue("not found")
				.to(String.class);

		this.filenetDocumentsURL = Converters.standardConverter().convert(properties.get("filenetDocumentsURL"))
				.defaultValue("not found").to(String.class);

		this.filenetDocumentsRetrieveURL = Converters.standardConverter().convert(properties.get("filenetDocumentsRetrieveURL"))
				.defaultValue("not found").to(String.class);
		
		this.filenetCredsUser = Converters.standardConverter().convert(properties.get("filenetCredsUser"))
				.defaultValue("not found").to(String.class);
		
		this.filenetCredsUserPass = Converters.standardConverter().convert(properties.get("filenetCredsUserPass"))
				.defaultValue("not found").to(String.class);
		
		this.filenetCredsPFXPath = Converters.standardConverter().convert(properties.get("filenetCredsPFXPath"))
				.defaultValue("not found").to(String.class);
		
		this.objectStore = Converters.standardConverter().convert(properties.get("objectStore"))
				.defaultValue("ACC").to(String.class);
		
		this.version = Converters.standardConverter().convert(properties.get("version"))
				.defaultValue("1").to(String.class);
		
		this.insertData = Converters.standardConverter().convert(properties.get("insertData"))
				.defaultValue("insert").to(String.class);
		
		this.asmNotification = Converters.standardConverter().convert(properties.get("asmNotification"))
				.defaultValue("not send").to(String.class);
		this.docClass = Converters.standardConverter().convert(properties.get("docClass"))
				.defaultValue("GRP_ACCOUNT_SETUPMAINT").to(String.class);
	}
	
	@Override
	public String getFilenetCertPdk() {
		if (this.filenetCertPdk.equalsIgnoreCase("not found"))
			log.error("-----filenetCertPdk not found in config properties-----");
		return this.filenetCertPdk;
	}


	@Override
	public String getFilenetKeyStorePass() {
		if (this.filenetKeyStorePass.equalsIgnoreCase("not found"))
			log.error("-----filenetKeyStorePass not found in config properties-----");
		return this.filenetKeyStorePass;
	}

	@Override
	public String getFilenetTokenProviderURL() {
		if (this.filenetTokenProviderURL.equalsIgnoreCase("not found"))
			log.error("-----filenetTokenProviderURL not found in config properties-----");
		return this.filenetTokenProviderURL;
	}

	@Override
	public String getFilenetTokenUser() {
		if (this.filenetTokenUser.equalsIgnoreCase("not found"))
			log.error("-----filenetTokenUser not found in config properties-----");
		return this.filenetTokenUser;
	}

	@Override
	public String getFilenetDocumentsURL() {
		if (this.filenetDocumentsURL.equalsIgnoreCase("not found"))
			log.error("-----filenetDocumentsURL not found in config properties-----");
		return this.filenetDocumentsURL;
	}

	@Override
	public String getFilenetDocumentsRetrieveURL() {
		if (this.filenetDocumentsRetrieveURL.equalsIgnoreCase("not found"))
			log.error("-----filenetDocumentsRetrieveURL not found in config properties-----");
		return this.filenetDocumentsRetrieveURL;
	}
	
	@Override
	public String getFilenetCredsUser() {
		if (this.filenetCredsUser.equalsIgnoreCase("not found"))
			log.error("-----getFilenetCredsUser not found in config properties-----");
		return this.filenetCredsUser;
	}
	
	@Override
	public String getFilenetCredsUserPass() {
		if (this.filenetCredsUserPass.equalsIgnoreCase("not found"))
			log.error("-----getFilenetCredsUserPass not found in config properties-----");
		return this.filenetCredsUserPass;
	}
	
	@Override
	public String getFilenetCredsPFXPath() {
		if (this.filenetCredsPFXPath.equalsIgnoreCase("not found"))
			log.error("-----filenetCredsPDKPath not found in config properties-----");
		return this.filenetCredsPFXPath;
	}
	
	@Override
	public String getObjectStore() {
		return objectStore;
	}
	
	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getInsertData() {
		return insertData;
	}
	
	@Override
	public String getAsmNotification() {
		return asmNotification;
	}
	
	@Override
	public String getDocClass() {
		return docClass;
	}
}
