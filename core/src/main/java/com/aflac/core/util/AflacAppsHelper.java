package com.aflac.core.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.aflac.core.experience.api.model.ApiMasterAppData;
import com.aflac.core.experience.api.model.MasterAppReviewTableApi;
import com.aflac.core.experience.api.model.MasterAppReviewTableDataApi;
import com.aflac.core.experience.api.model.SaveCasebuildResponse;
import com.aflac.core.osgi.service.FileNetService;
import com.aflac.core.osgi.service.MasterAppService;
import com.aflac.core.osgi.service.impl.MasterAppServiceImpl;
import com.aflac.xml.casebuilderProducts.models.CaseBuilderForm;
import com.aflac.xml.masterApp.models.MasterApp;
import com.day.cq.dam.api.Asset;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component(service = { AflacAppsHelper.class })
public class AflacAppsHelper {
	public static final String ASSETPATH = "/content/dam/formsanddocuments/aflac/output/";
	
	static Logger log = LoggerFactory.getLogger(AflacAppsHelper.class);
	
	@Reference
	private transient MasterAppService masterAppService;
		
	public AflacAppsHelper() {}

	public AflacAppsHelper(MasterAppService masterAppService) {
		this.masterAppService = masterAppService;
	}

	public String convertDate(String signedDT , SimpleDateFormat existingDateFormat ,SimpleDateFormat convertingDateFormat) throws ParseException {
		String formattedDate = null;
			if(signedDT!=null && !signedDT.isEmpty()) {
		        Date date = existingDateFormat.parse(signedDT);
		        formattedDate = convertingDateFormat.format(date);
			}
		return formattedDate;
	}
	
	public static MasterApp ConvertMasterAppXmlToJava(String formXml) throws IOException {
		try {
			Document doc = DocumentUtil.convertToDocument(formXml);
			String masterAppData = DocumentUtil.getNode("/afData/afBoundData/masterApp", doc);
			InputStream stream = new ByteArrayInputStream(masterAppData.getBytes(StandardCharsets.UTF_8));
			JAXBContext context = JAXBContext.newInstance(MasterApp.class);
			Unmarshaller um = context.createUnmarshaller();
			MasterApp masterApp = (MasterApp) um.unmarshal(stream);
			return masterApp;
		} catch (JAXBException e) {
			log.error(e.getMessage());
		}
		return new MasterApp();
	}

	public static List<String> getAttachmentFiles(SlingHttpServletRequest request) {
		List<String> fileNameList = new ArrayList<String>();
		try {
			Map<String, String> map = new HashMap<>();
			if (request.getParameter("appType").equalsIgnoreCase("Master App")) {
				map.put("path", "/content/dam/aflacapps/master-app-attchments");
				map.put("1_property", "AppUsed");
				map.put("1_property.value", String.valueOf(request.getParameter("appType")));
				map.put("2_property", "AttachmentGroup");
				map.put("2_property.value", String.valueOf(request.getParameter("attachmentGroup")));
				map.put("3_property", "DocType");
				map.put("3_property.value", String.valueOf(request.getParameter("attachmentType")));
			}
			getDataFromQuery(map, request, fileNameList);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return fileNameList;

	}
	
	public static void getDataFromQuery(Map<String, String> valueMap, SlingHttpServletRequest request,
			List<String> fileNameList) {
		ResourceResolver resolver;
		Session session;
		QueryBuilder queryBuilder;
		Query query;
		SearchResult result;
		Asset asset = null;
		try {
			resolver = request.getResourceResolver();
			session = resolver.adaptTo(Session.class);
			queryBuilder = resolver.adaptTo(QueryBuilder.class);
			query = queryBuilder.createQuery(PredicateGroup.create(valueMap), session);
			result = query.getResult();

			for (Hit hit : result.getHits()) {
				String path = hit.getPath();
				Resource resource = resolver.getResource(path.split("jcr:content")[0]);
				asset = resource.adaptTo(Asset.class);
				fileNameList.add(asset.getName());
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void updateMasterAppTableData(String masterAppId, String version, String status, String[] formIds) {
		try {
			if(masterAppId!=null && version != null && !masterAppId.isEmpty() && !version.isEmpty()) {
				//saving status update
				ApiMasterAppData apiMasterAppData = new ApiMasterAppData();
				apiMasterAppData.setMasterAppEntityId(masterAppId);
				apiMasterAppData.setVersion(Integer.parseInt(version));
				apiMasterAppData.setForStatusUpdate(true);
				List<MasterAppReviewTableDataApi> masterAppReviewTableData = new ArrayList<>();
				for(int i=0; i<formIds.length; i++) {
					MasterAppReviewTableDataApi tmp = new MasterAppReviewTableDataApi();
					tmp.setFormId(formIds[i].replace(".pdf", ""));
					tmp.setSignStatus(status);
					masterAppReviewTableData.add(tmp);
				}
				MasterAppReviewTableApi table = new MasterAppReviewTableApi();
				table.setMasterAppReviewTableData(masterAppReviewTableData);
				apiMasterAppData.setMasterAppReviewTable(table);
				//apiMasterAppData.setMasterappReceivedDatetime(LocalDateTime.now().toString());
				//apiMasterAppData.setMasterappSentDatetime(LocalDateTime.now().toString());
				
				SaveCasebuildResponse saveMasterAppResponse = masterAppService.saveMasterAppData(apiMasterAppData, null, "");
				log.info("====> Status Update Response: " + saveMasterAppResponse.getMessage());
			}
		}catch (Exception e) {
			log.error(e.getMessage());
		}
		
	}
}
