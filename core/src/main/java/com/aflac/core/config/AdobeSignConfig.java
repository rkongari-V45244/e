package com.aflac.core.config;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface AdobeSignConfig {
	
	public String getRefreshUrl();
	
	public String getRefreshToekn();
	
	public String getGrantType();
	
	public String getClientId();
	
	public String getClientSecret();
	
	public String getAdobeSignApiUrl();
	
	public String getAccessToken();
	
	public int getWaitTime();
	
	public String getServer();
	
}
