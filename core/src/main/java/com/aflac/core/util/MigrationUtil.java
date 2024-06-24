package com.aflac.core.util;

import java.io.IOException;
import java.io.InputStream;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MigrationUtil {

	public static String createComplianceAsset(InputStream docFile, String dataJSONString, String fileCreateName,
			ResourceResolver resourceResolver) throws IOException {

		JsonObject dataJSON = new JsonParser().parse(dataJSONString).getAsJsonObject();

		AssetManager am = resourceResolver.adaptTo(AssetManager.class);
		Asset assetFormData = am.createAsset(
				"/content/dam/compliance/" + dataJSON.get("documentType").getAsString() + "/" + fileCreateName, docFile,
				"application/PDF", true);
		String path = assetFormData.getPath() + "/jcr:content/metadata";
		Resource resource = resourceResolver.getResource(path);
		javax.jcr.Node node = resource.adaptTo(javax.jcr.Node.class);
		try {
			node.setProperty("name", dataJSON.get("name").getAsString());
			node.setProperty("documentType", dataJSON.get("documentType").getAsString());
			node.setProperty("dc:title", dataJSON.get("name").getAsString());
			node.setProperty("product", dataJSON.get("product").getAsString());
			node.setProperty("product2", dataJSON.get("product2").getAsString());
			node.setProperty("notes", dataJSON.get("notes").getAsString());
			node.setProperty("formNumber", dataJSON.get("formNumber").getAsString());
			node.setProperty("comments", dataJSON.get("comments").getAsString());
			node.setProperty("modifiedDate", dataJSON.get("modifiedDate").getAsString());
			node.setProperty("modifiedBy", dataJSON.get("modifiedBy").getAsString());
			node.setProperty("itemType2", dataJSON.get("itemType2").getAsString());
			node.setProperty("old", dataJSON.get("old").getAsString());
			node.setProperty("itemType", dataJSON.get("itemType").getAsString());
			node.setProperty("path", dataJSON.get("path").getAsString());
			node.getSession().save();
		} catch (RepositoryException e) {
			throw new IOException("ValueFormatException:" + e);
		}

		resourceResolver.commit();

		docFile.close();
		return assetFormData.getPath();
	}

	public static String createStandardAsset(InputStream docFile, String dataJSONString, String fileCreateName,
			ResourceResolver resourceResolver) throws IOException {

		JsonObject dataJSON = new JsonParser().parse(dataJSONString).getAsJsonObject();

		AssetManager am = resourceResolver.adaptTo(AssetManager.class);
		// Split Product Name with Numericals
		String str = dataJSON.get("productName").getAsString();
		StringBuffer alpha = new StringBuffer(), num = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i)))
				num.append(str.charAt(i));
			else
				alpha.append(str.charAt(i));
		}
		String writePath = null;
		if (num.length() > 0) {
			writePath = alpha.toString().replace("  ", " ").trim() + "/" + num.toString().trim();
		} else {
			writePath = str;
		}
		String planLevel = "";
		if (dataJSON.get("planDesign").getAsString() != null && !dataJSON.get("planDesign").getAsString().isEmpty()) {
			if (dataJSON.get("planDesign").getAsString().toLowerCase().contains("low")) {
				planLevel = "Low";
			}
			if (dataJSON.get("planDesign").getAsString().toLowerCase().contains("mid")) {
				planLevel = "Mid";
			}
			if (dataJSON.get("planDesign").getAsString().toLowerCase().contains("high")) {
				planLevel = "High";
			}
		}

		// Add metadata to Low if part of planDesign
		Asset assetFormData = am.createAsset("/content/dam/Standard/" + writePath + "/"
				+ dataJSON.get("language").getAsString() + "/" + fileCreateName, docFile, "application/PDF", true);
		String path = assetFormData.getPath() + "/jcr:content/metadata";
		Resource resource = resourceResolver.getResource(path);
		javax.jcr.Node node = resource.adaptTo(javax.jcr.Node.class);
		try {

			node.setProperty("id", dataJSON.get("id").getAsString());
			node.setProperty("dc:language", dataJSON.get("language").getAsString());
			node.setProperty("dc:title", dataJSON.get("productName").getAsString());
			node.setProperty("formNumber", dataJSON.get("formNumber").getAsString());
			node.setProperty("itemType", dataJSON.get("itemType").getAsString());
			node.setProperty("planDesign", dataJSON.get("planDesign").getAsString());
			node.setProperty("planLevel", planLevel);
			node.setProperty("coverageName", dataJSON.get("planDesign").getAsString());
			node.setProperty("state", dataJSON.get("state").getAsString());
			node.setProperty("modifiedDate", dataJSON.get("modifiedDate").getAsString());
			node.setProperty("modifiedBy", dataJSON.get("modifiedBy").getAsString());
			node.setProperty("itemType2", dataJSON.get("itemType2").getAsString());
			node.setProperty("path", dataJSON.get("path").getAsString());
			node.getSession().save();
		} catch (RepositoryException e) {
			throw new IOException("ValueFormatException:" + e);
		}
		resourceResolver.commit();

		docFile.close();
		return assetFormData.getPath();
	}
}
