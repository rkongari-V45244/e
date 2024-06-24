package com.aflac.core.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.aflac.core.experience.api.model.MasterAppFormIdsModel;
import com.aflac.core.experience.api.model.MasterAppSeriesData;
import com.aflac.core.experience.api.model.MasterAppSeriesResponseModel;
import com.aflac.core.osgi.service.MasterAppService;
import com.aflac.core.util.DocumentUtil;
import com.aflac.core.util.PdfGenerationUtil;
import com.aflac.xml.masterApp.models.MasterApp;
import com.aflac.xml.masterApp.models.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Custom component to Transform MasterApp xml based on FormID selected on Review Screen.",
		Constants.SERVICE_VENDOR + "=Adobe Systems",
		"process.label" + "=Custom component to Transform MasterApp xml based on FormID selected on Review Screen." })
public class XmlTransformationWorkflowStep implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(XmlTransformationWorkflowStep.class);
	
	@Reference
	private transient MasterAppService masterAppService;
	
	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		
		log.info("ENTERING : XmlTransformationWorkflowStep method...");
		String file = metaDataMap.get("fileName", String.class);
		log.info("------file: " + file);
		ObjectMapper Obj = new ObjectMapper();

		// Read the value from the dialogue box
		String xmlVariableName = metaDataMap.get("xmlVariableName", String.class);
		log.info("------xmlVariableName: " + xmlVariableName);
		String formIdVariableName = metaDataMap.get("formIdVariableName", String.class);
		log.info("------formIdVariableName: " + formIdVariableName);


		// Read the value from the dialogue box
		String masterXmlVariableName = metaDataMap.get("masterXmlVariableName", String.class);
		log.info("------masterXmlVariableName: " + masterXmlVariableName);

		String formData = (String) item.getWorkflowData().getMetaDataMap().get("formXmlData");

//		String formData = "";
		
		WorkflowData data = item.getWorkflowData();
		MetaDataMap metaData = data.getMetaDataMap();
		
		String xmlDocument = metaData.get("inputXML",String.class);
		if(xmlDocument!=null)
		metaData.put("xmlDocument", xmlDocument);

//		String type = data.getPayloadType();
//		String payloadPath = data.getPayload().toString();
//		String dataFilePath = payloadPath + "/" + file;
		
//		log.info("===> Type: " + type + " ====> Payload: " + payloadPath);
//		
//		try {
//			Node attachmentNode = session.adaptTo(Session.class).getNode(dataFilePath + "/jcr:content");
//			InputStream documentStream = attachmentNode.getProperty("jcr:data").getBinary().getStream();
//			formData = IOUtils.toString(documentStream, StandardCharsets.UTF_8.name());
//		} catch (RepositoryException | IOException e1) {
//			log.error("Error in fetching xml: ", e1);
//		}
		
		try {
			// This is the submitted xml data payload
			Document doc = DocumentUtil.convertToDocument(formData);
			String masterAppXml = DocumentUtil.getNode("/afData/afBoundData/masterApp", doc);
						
			InputStream stream = new ByteArrayInputStream(masterAppXml.getBytes(StandardCharsets.UTF_8));
			JAXBContext context = JAXBContext.newInstance(MasterApp.class);
			Unmarshaller um = context.createUnmarshaller();

			MasterApp masterApp = (MasterApp) um.unmarshal(stream);
			//String masterAppXml = PdfGenerationUtil.convertObjectToXML(masterApp);

			//set the WF variable with xml data.
			metaData.put(masterXmlVariableName, masterAppXml);

			String situs = masterApp.getSitusState();
			String groupType = masterApp.getGroupType();
			String census = masterApp.getCensusEnrollment();
			
			MasterAppSeriesResponseModel seriesModel = masterAppService.getMasterAppSeriesData(situs, groupType, census); 
					//Obj.readValue(getSeriesData(), MasterAppSeriesResponseModel.class); 
					
			MasterAppSeriesData masterAppSeriesData = seriesModel.getMasterAppSeriesData();
			
			String formIds = masterApp.getFormId();
			log.info("=========>>>>> First attachment: " + masterApp.getFirstAttachment());
			log.info("=========>>>>> Second attachment: " + masterApp.getSecondAttachment());
			
			metaData.put("attachment1", masterApp.getFirstAttachment());
			metaData.put("attachment2", masterApp.getSecondAttachment());
			//metaData.put("signerEmailId", masterApp.getFirstSignerEmailId());
			
			String[] forms = formIds.split(",");
			String[] xmls = new String[forms.length];
			int index = 0;
			for(String formId: forms) {

				MasterApp apps1 = Obj.readValue(Obj.writeValueAsString(masterApp), MasterApp.class);
												
				List<MasterAppFormIdsModel> filteredList = masterAppSeriesData.stream().filter(f -> f.getFormId().equalsIgnoreCase(formId)).collect(Collectors.toList());
				for(MasterAppFormIdsModel filteredModels: filteredList){
					List<String> filteredSeries = filteredModels.getSeries();
					List<Product> filteredProducts = new ArrayList<>();
					apps1.getProduct().forEach(p -> {
						if(!p.getProductName().equalsIgnoreCase("TL93000") &&
						   !p.getProductName().equalsIgnoreCase("TL9100") &&
						   !p.getProductName().equalsIgnoreCase("WL9800") &&
						   !p.getProductName().equalsIgnoreCase("WL60000")) 
						{
							List<String> opf = Arrays.asList(p.getOptionalFeatures().get(0).split(","));
							p.setOptionalFeatures(opf);
						}
						if(filteredSeries.contains(p.getSeries())) {
							filteredProducts.add(p);
						}
					});
					
					apps1.setProduct(filteredProducts);
					String xml = "";
					try {
						xml = PdfGenerationUtil.convertObjectToXML(apps1);
						xmls[index++] = xml;
					} catch (JAXBException e) {
						log.error("XML Transformation Error: ", e);
					}
				}
			}
			//set the WF variable listOfXmls.
			log.info("SETTING : xmlVariableName variable : " + xmls);
			metaData.put(xmlVariableName, xmls);
			
			//set the WF variable listOfFormIds.
			log.info("SETTING : formIdVariableName variable : " + forms);
			metaData.put(formIdVariableName, forms);
			log.info("EXITING : XmlTransformationWorkflowStep method...");
			
		} catch (JAXBException | IOException e) {
			log.error("Error XmlTransformationWorkflowStep: ", e);
		}
	}
	
	private String getSeriesData() {
		return "{\r\n"
				+ "    \"formids\": [\r\n"
				+ "        \"C03204\",\r\n"
				+ "        \"ICC22 C93201\"\r\n"
				+ "    ],\r\n"
				+ "    \"eligibleSeries\": [\r\n"
				+ "        \"7700\",\r\n"
				+ "        \"7800\",\r\n"
				+ "        \"70000\",\r\n"
				+ "        \"2800\",\r\n"
				+ "        \"20000\",\r\n"
				+ "        \"21000\",\r\n"
				+ "        \"22000\",\r\n"
				+ "        \"8500\",\r\n"
				+ "        \"80000\",\r\n"
				+ "        \"81000\",\r\n"
				+ "        \"1100\",\r\n"
				+ "        \"5000\",\r\n"
				+ "        \"50000\",\r\n"
				+ "        \"9100\",\r\n"
				+ "        \"9800\",\r\n"
				+ "        \"60000\",\r\n"
				+ "        \"93000\"\r\n"
				+ "    ],\r\n"
				+ "    \"masterAppSeriesData\": [\r\n"
				+ "        {\r\n"
				+ "            \"apiFormId\": \"C03204AL\",\r\n"
				+ "            \"form-id\": \"C03204\",\r\n"
				+ "            \"product-series\": [\r\n"
				+ "                \"7700\",\r\n"
				+ "                \"7800\",\r\n"
				+ "                \"70000\",\r\n"
				+ "                \"2800\",\r\n"
				+ "                \"20000\",\r\n"
				+ "                \"21000\",\r\n"
				+ "                \"22000\",\r\n"
				+ "                \"8500\",\r\n"
				+ "                \"80000\",\r\n"
				+ "                \"81000\",\r\n"
				+ "                \"1100\",\r\n"
				+ "                \"5000\",\r\n"
				+ "                \"50000\",\r\n"
				+ "                \"9100\",\r\n"
				+ "                \"9800\",\r\n"
				+ "                \"60000\"\r\n"
				+ "            ]\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"apiFormId\": \"ICC22 C93201\",\r\n"
				+ "            \"form-id\": \"ICC22 C93201\",\r\n"
				+ "            \"product-series\": [\r\n"
				+ "                \"93000\"\r\n"
				+ "            ]\r\n"
				+ "        }\r\n"
				+ "    ]\r\n"
				+ "}";
	}
}
