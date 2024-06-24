package com.aflac.core.config.impl;

import java.io.Serializable;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.converter.Converters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.AdobePDFApiIntegrationConfig;

@Component(service = { AdobePDFApiIntegrationConfig.class })
public class AdobePDFApiIntegrationConfigImpl implements AdobePDFApiIntegrationConfig , Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(AdobePDFApiIntegrationConfigImpl.class);

	private String imsEndPoint;
	private String metaScopes;
	private String clientId;
	private String clientSecret;
	private String techAcc;
	private String org;
	private String email;
	private String privateKey;
	private String publicKey;
	private String env;
	private String waterMarkText;
	

	@Activate
	protected void activate(Map<String, Object> properties) {

		log.info("CredentialServiceImpl has been activated!");

		this.imsEndPoint = Converters.standardConverter().convert(properties.get("imsEndpoint"))
				.defaultValue("notFound").to(String.class);

		this.metaScopes = Converters.standardConverter().convert(properties.get("metascopes")).defaultValue("not found")
				.to(String.class);

		this.clientId = Converters.standardConverter().convert(properties.get("clientId")).defaultValue("not found")
				.to(String.class);

		this.clientSecret = Converters.standardConverter().convert(properties.get("clientSecret"))
				.defaultValue("not found").to(String.class);

		this.techAcc = Converters.standardConverter().convert(properties.get("id")).defaultValue("not found")
				.to(String.class);

		this.org = Converters.standardConverter().convert(properties.get("org")).defaultValue("not found")
				.to(String.class);

		this.email = Converters.standardConverter().convert(properties.get("email")).defaultValue("not found")
				.to(String.class);

		this.env = Converters.standardConverter().convert(properties.get("env")).defaultValue("not found")
				.to(String.class);
		
		this.waterMarkText = Converters.standardConverter().convert(properties.get("waterMarkText")).defaultValue("not found")
				.to(String.class);

		this.privateKey = Converters.standardConverter().convert(properties.get("privateKey")).defaultValue("not found")
				.to(String.class);

		this.publicKey = (String) properties.get("publicKey");
		
	}

	@Deactivate
	protected void deactivate() {
		log.info("CredentialServiceImpl has been deactivated!");
	}

	@Override
	public String getIMS_ENDPOINT() {
		if (this.imsEndPoint.equalsIgnoreCase("notFound"))
			log.error("-----imsEndpoint not found in config properties-----");
		return this.imsEndPoint;
	}

	@Override
	public String getCLIENT_SECRET() {
		if (this.clientSecret.equalsIgnoreCase("not found"))
			log.error("-----clientSecret not found in config properties-----");
		return this.clientSecret;
	}

	@Override
	public String getCLIENT_ID() {
		if (this.clientId.equalsIgnoreCase("not found"))
			log.error("-----client ID not found in config properties-----");
		return this.clientId;
	}

	@Override
	public String getMETASCOPE_ID() {
		if (this.metaScopes.equalsIgnoreCase("not found"))
			log.error("-----metaScope not found in config properties-----");
		return this.metaScopes;
	}

	@Override
	public String getTECH_ACCT() {
		if (this.techAcc.equalsIgnoreCase("not found"))
			log.error("-----Tech Account ID not found in config properties-----");
		return this.techAcc;
	}

	@Override
	public String getORG_ID() {
		if (this.org.equalsIgnoreCase("not found"))
			log.error("-----org not found in config properties-----");
		return this.org;
	}

	@Override
	public String getPRIVATE_KEY() {
		if (this.privateKey.equalsIgnoreCase("not found"))
			log.error("-----Private key not found in config properties-----");
		return this.privateKey;
	}

	@Override
	public String getEnvironment() {
		return this.env;
	}
	
	@Override
	public String getWaterMarkText() {
		return waterMarkText;
	}

	@Override
	public String getPUBLIC_KEY() {
		if (this.publicKey.equalsIgnoreCase("not found"))
			log.error("-----Public key not found in config properties-----");
		return this.publicKey;
	}

	@Override
	public String getEMAIL() {
		if (this.email.equalsIgnoreCase("not found"))
			log.error("-----email not found in config properties-----");
		return this.email;
	}	
}
