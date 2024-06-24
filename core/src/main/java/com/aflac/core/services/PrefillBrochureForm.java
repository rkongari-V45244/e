package com.aflac.core.services;

import java.io.InputStream;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.forms.common.service.ContentType;
import com.adobe.forms.common.service.DataOptions;
import com.adobe.forms.common.service.DataProvider;
import com.adobe.forms.common.service.FormsException;
import com.adobe.forms.common.service.PrefillData;

@Component
public class PrefillBrochureForm implements DataProvider {
	private Logger logger = LoggerFactory.getLogger(PrefillAdaptiveForm.class);
	// private GroupMasterAppService service = new GroupMasterAppService();

	public String getServiceName() {
		return "Brochure Prefill Service";
	}

	public String getServiceDescription() {
		return "Brochure Prefill Service";
	}

	public PrefillData getPrefillData(final DataOptions dataOptions) throws FormsException {
		PrefillData prefillData = new PrefillData() {
			public InputStream getInputStream() {
				return getData(dataOptions);
			}

			public ContentType getContentType() {
				return ContentType.XML;
			}
		};
		return prefillData;
	}

	private InputStream getData(DataOptions dataOptions) throws FormsException {

		String groupPlanId = dataOptions.getExtras().get("groupPlanId").toString();

		logger.info("GroupPlanId: " + groupPlanId);

		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(groupPlanId + ".xml");

		return inputStream;
	}
}
