package com.aflac.core.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.forms.common.service.ContentType;
import com.adobe.forms.common.service.DataOptions;
import com.adobe.forms.common.service.DataProvider;
import com.adobe.forms.common.service.FormsException;
import com.adobe.forms.common.service.PrefillData;
import com.aflac.core.servlets.CaseBuilderToolDataServlet;
import com.aflac.xml.casebuilderProducts.models.AccountInformation;
import com.aflac.xml.casebuilderProducts.models.CaseBuilderForm;
import com.aflac.xml.casebuilderProducts.models.Products;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Component
public class PrefillCaseBuilderToolProducts implements DataProvider {

	private transient Logger log = LoggerFactory.getLogger(CaseBuilderToolDataServlet.class);
	
	@Override
	public String getServiceDescription() {
		return "Aflac Products CaseBuilderTool Prefill Service";
	}

	@Override
	public String getServiceName() {
		return "Aflac Products CaseBuilderTool Prefill Service";
	}

	@Override
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
		
		InputStream stream = null;
		try {
			
			String groupNumber = dataOptions.getExtras().get("groupNumber").toString();
//			List<String> platformList = new ArrayList<String>();
//			platformList.add("Ease");
//			platformList.add("Benetrac");
			if(groupNumber!=null && !groupNumber.isEmpty()) {
				AccountInformation accountInformation = new AccountInformation();
				Products products = new Products();
				CaseBuilderToolService caseBuilderToolService = new CaseBuilderToolService();
				CaseBuilderForm caseBuilderForm = new CaseBuilderForm();
				
				caseBuilderToolService.getAccountInformationDataProducts(accountInformation,null);
				caseBuilderToolService.getProductsData(products);
//				caseBuilderToolService.getAccidentData(accident);
				caseBuilderForm.setAccountInformation(accountInformation);
				caseBuilderForm.setProducts(products);
//				caseBuilderForm.setAccident(accident); 
				caseBuilderForm.setCoverageEffectiveDate("2022-08-01");
				caseBuilderForm.setGroupNo("AGC0000317811");
				caseBuilderForm.setEnrollmentPlatform("Ease");
				XmlMapper xmlMapper = new XmlMapper();
		        String xmlString = xmlMapper.writeValueAsString(caseBuilderForm);
		        stream = new ByteArrayInputStream(xmlString.getBytes(Charset.forName("UTF-8")));
			}
		}
		catch (JsonProcessingException e) {
			log.error("Exception in CaseBuilderTool Prefill Service",e);
		}
		
		return stream;
	}

}
