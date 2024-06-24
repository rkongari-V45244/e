package com.aflac.core.services;

import java.io.IOException;
import java.io.InputStream;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.forms.common.service.ContentType;
import com.adobe.forms.common.service.DataOptions;
import com.adobe.forms.common.service.DataProvider;
import com.adobe.forms.common.service.FormsException;
import com.adobe.forms.common.service.PrefillData;
import com.aflac.core.util.AflacAppsHelper;
import com.day.cq.dam.api.Asset;

@Component
public class PrefillAdaptiveForm implements DataProvider {
	private Logger logger = LoggerFactory.getLogger(PrefillAdaptiveForm.class);
	// private GroupMasterAppService service = new GroupMasterAppService();

	public String getServiceName() {
		return "Aflac Prefill Service";
	}

	public String getServiceDescription() {
		return "Aflac Prefill Service";
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

		String groupProposalNo = dataOptions.getExtras().get("groupProposalNo").toString();
		// if edit model

		Object mode = dataOptions.getExtras().get("mode");
		if (mode != null && mode.toString().equalsIgnoreCase("edit")) {
			Resource aemFormContainer = dataOptions.getFormResource();
			ResourceResolver resolver = aemFormContainer.getResourceResolver();

			Resource res = resolver.getResource(AflacAppsHelper.ASSETPATH + groupProposalNo + "/datajson.xml");
			Asset asset = res.adaptTo(Asset.class);
			Resource original = asset.getOriginal();
			InputStream inputStream = original.adaptTo(InputStream.class);
			return inputStream;
		} else {
			logger.info("GroupProposalNo: " + groupProposalNo);

			ClassLoader classLoader = getClass().getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream(groupProposalNo + ".xml");

			return inputStream;
		}
	}
}
