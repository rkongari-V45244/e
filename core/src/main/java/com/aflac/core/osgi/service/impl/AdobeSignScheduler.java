package com.aflac.core.osgi.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.serviceusermapping.ServiceUserMapped;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.converter.Converters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.collection.util.ResultSet;
import com.adobe.granite.workflow.exec.InboxItem;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.aflac.core.config.AdobeSignConfig;
import com.aflac.core.osgi.service.MasterAppService;
import com.aflac.core.util.AflacAppsHelper;
import com.aflac.core.util.DocumentStatus;
import com.aflac.xml.masterApp.models.MasterApp;
import com.aflac.xml.masterApp.models.MasterAppReviewTableData;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component(
//		reference = {
//				@Reference(
//					name = "aflac-service-user",
//					service = ServiceUserMapped.class,
//					target = "(subServiceName=aflac-service-user)"
//				)
//		},
		service = Runnable.class, 
		immediate = true)
public class AdobeSignScheduler implements Runnable {

	private transient Logger log = LoggerFactory.getLogger(AdobeSignScheduler.class);
	//private static final String SERVICE_USER_ACCOUNT = "aflac-service-user";
	
	private String schedulerName;
	private String cronExpression;
	private boolean isConcurrent; 
	private boolean enable;
	private String userName;
	private String password;
	private String runMode;
	
	@Reference
	private transient ResourceResolverFactory resolverFactory;
	@Reference
	private transient Scheduler scheduler;
	@Reference
	private transient AdobeSignConfig adobeSignConfig;
	@Reference
	private transient MasterAppService masterAppService;
	
	@Activate
	protected void activate(Map<String, Object> properties) {
		this.schedulerName = Converters.standardConverter().convert(properties.get("schedulerName")).defaultValue("AdobeSign").to(String.class);
		this.cronExpression = Converters.standardConverter().convert(properties.get("cronExpression")).defaultValue("0 0/2 * 1/1 * ? *").to(String.class);
		this.isConcurrent = Converters.standardConverter().convert(properties.get("isConcurrent")).defaultValue(false).to(Boolean.class);
		this.enable = Converters.standardConverter().convert(properties.get("enable")).defaultValue(true).to(Boolean.class);
		this.userName = Converters.standardConverter().convert(properties.get("userName")).defaultValue("admin").to(String.class);
		this.password = Converters.standardConverter().convert(properties.get("password")).defaultValue("admin").to(String.class);
		this.runMode = Converters.standardConverter().convert(properties.get("runMode")).defaultValue("").to(String.class);
		
		if(this.runMode.equalsIgnoreCase("author"))
			addScheduler();
	}
	
	public void addScheduler() {
		if(this.enable) {
			ScheduleOptions scheduleOptions = scheduler.EXPR(this.cronExpression);
			scheduleOptions.name(this.schedulerName);
			scheduleOptions.canRunConcurrently(this.isConcurrent);
			
			scheduler.schedule(this, scheduleOptions);
		}
	}
	
	@Deactivate
	public void deactivate() {
		unscheduler();
	}
	
	public void unscheduler() {
		scheduler.unschedule(this.schedulerName);
	}
	
	@Override
	public void run() {
		log.info("---Scheduler is started---");
		executeWorkflowQuery();
	}
	
	private void executeWorkflowQuery() {
		
		Map<String, String> valueMap = new HashMap<>();
		valueMap.put("path", "/var/workflow/instances");
		valueMap.put("1_property", "status");
		valueMap.put("1_property.value", "RUNNING");
		valueMap.put("p.limit", "-1");  // Adding the following parameter allows the servlet to display all query results
		
		ResourceResolver resolver = getResourceResolver();
		Session session = resolver.adaptTo(Session.class);
		QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);
		Query query = queryBuilder.createQuery(PredicateGroup.create(valueMap), session);
		SearchResult result = query.getResult();
		log.info("===> Query Results: " + result.getHits().size());
		try {
			for (Hit hit : result.getHits()) {
				WorkflowSession workflowSession = resolver.adaptTo(WorkflowSession.class);
				Workflow workflow = workflowSession.getWorkflow(hit.getPath());
				WorkflowData data = workflow.getWorkflowData();
				MetaDataMap metaData = data.getMetaDataMap();
				String agreementId = metaData.get("agreementId", String.class);
				if(agreementId != null && !agreementId.isEmpty()) {
					log.info("Checking Sign status for agreementId: " + agreementId);
					String formXml = metaData.get("formXmlData", String.class);
					String dbStatus = getMasterAppFormStatus(formXml, agreementId);
					log.info("----- DynamoDB Status: " + dbStatus);
					String status = "";
					if(dbStatus.equalsIgnoreCase(DocumentStatus.VOIDED.getValue()))
						status = DocumentStatus.VOIDED.getValue();
					else
						status = getAgreementStatus(agreementId);
					if(!status.equalsIgnoreCase(DocumentStatus.OUT_FOR_SIGNATURE.getValue())) {
						if(!status.equalsIgnoreCase(DocumentStatus.SIGNED.getValue()))
							status = DocumentStatus.VOIDED.getValue();
						WorkItem item = getWorkItem(workflow.getId(), workflowSession);
						if(item != null) {
							item.getWorkflowData().getMetaDataMap().put("signatureStatus",status);
							completeWorkflow(workflowSession, item);
						}
						else {
							log.error("-----WorkFlow Item is NULL-----");
						}
					}
				}
			}
		} catch (WorkflowException | RepositoryException e) {
			log.error("WorkFlow Exception in AdobeSignScheduler: ", e);
		} finally {
			resolver.close();
		}
	}
	
	private void completeWorkflow(WorkflowSession workflowSession, WorkItem workItem) {
		if(workItem != null) {
			try {
				List<Route> routes = workflowSession.getRoutes(workItem, false);
				Route route = null;
				if(routes.size() > 0) {
					for(Route r : routes) {
						if(r.hasDefault()) {
							route = r;
							break;
						}
					}
					if(route == null)
						route = routes.get(0);
					workflowSession.complete(workItem, route);
					log.info("----- Workflow \"" + workItem.getId() + "\" send for complete. -----");
				} else {
					log.error("---- No valid Routes found for given workflow " + workItem.getId());
					throw new WorkflowException("No Valid Routes Found.");
				}
			} catch (WorkflowException e) {
				log.error("Exception occured in completeWorkflow(): ", e);
			}
		}
	}
	
	private ResourceResolver getResourceResolver() {
		ResourceResolver resolver = null;
		try {
			//Map<String, Object> serviceUserSession = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,SERVICE_USER_ACCOUNT);
			Map<String, Object> serviceUserSession = new HashMap<>();
			serviceUserSession.put(ResourceResolverFactory.USER, userName);
            serviceUserSession.put(ResourceResolverFactory.PASSWORD, password.toCharArray());
			resolver = resolverFactory.getResourceResolver(serviceUserSession);
		} catch (LoginException e) {
			log.error("Login Exception in getResourceResolver of AdobeSignScheduler: ", e);
		}
		return resolver;
	}
	
	private WorkItem getWorkItem(String workflowId, WorkflowSession workflowSession) {
		WorkItem item = null;
		try {
			ResultSet<WorkItem> activeItems = workflowSession.getActiveWorkItems(0, -1);
			log.info("----- Total Active work items found: " + activeItems.getTotalSize());
			
			for(InboxItem it : activeItems.getItems()) {
				if(((WorkItem) it).getWorkflow().getId().equals(workflowId))
					item = (WorkItem) it;
			}
			
		} catch (WorkflowException e) {
			log.error("Exception in getting workItems: {}", e);
		}
		return item;
	}
	
	public String getAgreementStatus(String agreementId) {
		String status = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(adobeSignConfig.getAdobeSignApiUrl() +"/agreements/"+ agreementId);
		httpGet.addHeader("Authorization", "Bearer " + adobeSignConfig.getAccessToken());
		CloseableHttpResponse outputResponse;
		try {
			outputResponse = httpclient.execute(httpGet);
			int statusCode = outputResponse.getStatusLine().getStatusCode();
			log.info("Get Agreement Status API HTTP code: " + statusCode);
			HttpEntity entity = outputResponse.getEntity();
			String result = EntityUtils.toString(entity);
			if (statusCode == 200) {
				JsonReader jsonReader = new JsonReader(new StringReader(result));
				JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
				status = jsonObject.get("status").getAsString();
				log.info("====> Agreement Status: " + status);
			} else {
				log.info("getAgreementStatus API Error Response: " + result);
			}
		} catch (IOException e) {
			log.error("Exception in getting Agreement Status: ", e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				log.error("http error: ", e);
			}
		}
		
		return status;
	}
	
	public String getMasterAppFormStatus(String formXml, String agreementId) {
		String status = "";
		try {
			MasterApp masterAppFromXml = AflacAppsHelper.ConvertMasterAppXmlToJava(formXml);
			MasterApp masterAppFromDb = masterAppService.getMasterAppData(masterAppFromXml.getAccountAndEligiblity().getGroupNumberOrGpId(), masterAppFromXml.getAccountAndEligiblity().getEffectiveDate());
			status = masterAppFromDb.getMasterAppReviewTable().getMasterAppReviewTableData()
					.stream().filter(r -> r.getAgreementId()!= null ? r.getAgreementId().equals(agreementId) : false).map(MasterAppReviewTableData::getSignStatus).findAny().get();
			
		} catch (IOException e) {
			log.error("Error: " + e);
		}
		return status;
	}
}
