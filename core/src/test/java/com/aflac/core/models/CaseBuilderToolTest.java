//package com.aflac.core.models;
//
//import static org.junit.jupiter.api.Assertions.*;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import javax.servlet.ServletException;
//import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
//import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.aflac.core.servlets.CaseBuilderToolDataServletProducts;
//import com.aflac.xml.casebuilderProducts.models.CaseBuilderForm;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.wcm.testing.mock.aem.junit5.AemContext;
//import io.wcm.testing.mock.aem.junit5.AemContextExtension;
//
//@ExtendWith(AemContextExtension.class)
//public class CaseBuilderToolTest {
//	
//	private transient Logger log = LoggerFactory.getLogger(CaseBuilderToolTest.class);
//
//	AemContext aemContext = null;
//	CaseBuilderForm caseBuilderForm = null;
//	ObjectMapper mapper = null;
//	MockSlingHttpServletRequest request = null;
//	MockSlingHttpServletResponse response = null;
//
//	@BeforeEach
//	public void setup() throws Exception {
//		aemContext = new AemContext();
//		caseBuilderForm = new CaseBuilderForm();
//		mapper = new ObjectMapper();
//		request = aemContext.request();
//		response = aemContext.response();
//	}
//
//	@Test
//	public void testEnrollmentPlatform() throws ServletException, IOException {
//		try {
//			Map<String, Object> params = new HashMap<>();
//			params.put("groupNumber", "123");
//			params.put("enrollmentPlatform", "Ease");
//			params.put("effectiveDate", "20-10-2022");
//			request.setParameterMap(params);
//			CaseBuilderToolDataServletProducts caseBuilderToolDataServletProducts = new CaseBuilderToolDataServletProducts();
//			caseBuilderToolDataServletProducts.doGet(request, response);
//			String json = response.getOutputAsString();
//			caseBuilderForm = mapper.readValue(json, CaseBuilderForm.class);
//			assertEquals("Ease", caseBuilderForm.getEnrollmentPlatform());
//		} catch (Exception e) {
//			throw new IOException("Unable to execute the EnrollmentPlatform Test", e);
//		}
//	}
//
//}
