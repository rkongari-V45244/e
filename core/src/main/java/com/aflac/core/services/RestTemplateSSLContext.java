package com.aflac.core.services;

import java.io.IOException;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class RestTemplateSSLContext {

	public String sendPost(final String apiUrl, final Map<String, String> headers, final HttpEntity entity,
			final SSLContext sslContext) throws ClientProtocolException, IOException {
		
		int timeout = 60;
		RequestConfig requestConfig = RequestConfig.custom()
		  .setConnectTimeout(timeout * 1000)
		  .setConnectionRequestTimeout(timeout * 1000)
		  .setSocketTimeout(timeout * 1000).build();

		CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).setDefaultRequestConfig(requestConfig).build();	
		HttpPost post = new HttpPost(apiUrl);
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			post.addHeader(entry.getKey(), entry.getValue());
		}
		post.setEntity(entity);
		HttpResponse result = httpClient.execute(post);
		HttpEntity resentity = result.getEntity();
		return EntityUtils.toString(resentity, "UTF-8");
	}

}
