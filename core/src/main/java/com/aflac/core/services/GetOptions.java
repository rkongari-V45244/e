package com.aflac.core.services;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class GetOptions {
static public String getPDFOptions()
{
	 ClassLoader classLoader = GetOptions.class.getClassLoader();
     InputStream credentialsFiles = classLoader.getResourceAsStream("credentials/options.json");
     JsonReader jsonReader = new JsonReader(new InputStreamReader(credentialsFiles));

     JsonElement jsonElement = JsonParser.parseReader(jsonReader);
     
     return jsonElement.getAsJsonObject().toString();

}
static public String getEmbeddFontsOptions()
{
	 ClassLoader classLoader = GetOptions.class.getClassLoader();
     InputStream credentialsFiles = classLoader.getResourceAsStream("credentials/customfonts.json");
     JsonReader jsonReader = new JsonReader(new InputStreamReader(credentialsFiles));

     JsonElement jsonElement = JsonParser.parseReader(jsonReader);
     return jsonElement.getAsJsonObject().toString();

}
}
