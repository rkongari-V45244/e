package com.aflac.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.day.cq.dam.api.Asset;

public class DocumentUtil {
	
	static Logger log = LoggerFactory.getLogger(DocumentUtil.class);

	public static String getNode(String path, Document doc) throws IOException {
		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xpath = xpathfactory.newXPath();

		String value = null;

		try {
			XPathExpression expr = xpath.compile(path);
			Node grpMasterDataNode = (Node) expr.evaluate(doc, XPathConstants.NODE);

			StringWriter sw = new StringWriter();
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "no");
			t.transform(new DOMSource(grpMasterDataNode), new StreamResult(sw));
			value = sw.getBuffer().toString();

		} catch (XPathExpressionException | TransformerException e) {
			throw new IOException("Can't extract data at|" + path, e);
		}
		return value;
	}
	
	public static String getValue(String path, Document doc) throws IOException {
		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xpath = xpathfactory.newXPath();

		String value = null;
		try {
			XPathExpression expr = xpath.compile(path);
			value = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			throw new IOException("Can't find value of path|" + path, e);
		}

		return value;
	}
	
	public static Document convertToDocument(String formData) throws IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder docBuilder;
		Document doc = null;
		try {
			docBuilder = factory.newDocumentBuilder();
			doc = docBuilder.parse(IOUtils.toInputStream(formData, StandardCharsets.UTF_8));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new IOException("Can't parse xml", e);
		}
		return doc;
	}
	
	public static void downloadDamDocument(String fileName, SlingHttpServletRequest request,
			SlingHttpServletResponse response,String damPath) {
		
		ResourceResolver resourceResolver = request.getResourceResolver();
		log.info("File: " + damPath + fileName);
		Resource resourseXDP = resourceResolver.getResource(damPath + fileName);
		log.info("File ==>" + resourseXDP);

		Asset asset = resourseXDP.adaptTo(Asset.class);
		Resource original = asset.getOriginal();
		InputStream fileIS = original.adaptTo(InputStream.class);
		
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "attachment; filename=output.pdf");

		OutputStream responseOutputStream;
		try {
			responseOutputStream = response.getOutputStream();
			IOUtils.copyLarge(fileIS, responseOutputStream);
			responseOutputStream.flush();
			responseOutputStream.close();
			fileIS.close();
		} catch (IOException e) {
			log.error("Exception in Downloading file :" + e);
		}
	}
	
}
