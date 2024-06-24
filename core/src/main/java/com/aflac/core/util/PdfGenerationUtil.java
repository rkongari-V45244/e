package com.aflac.core.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.experience.api.model.ComplianceDetail;
import com.aflac.xml.casebuilderProducts.models.CaseBuilderForm;
import com.aflac.xml.casebuilderProducts.models.Product;

public class PdfGenerationUtil {

	static Logger log = LoggerFactory.getLogger(PdfGenerationUtil.class);

	public static CaseBuilderForm xmlToJava(String formXml) throws IOException {
		try {
			InputStream stream = new ByteArrayInputStream(formXml.getBytes(StandardCharsets.UTF_8));
			JAXBContext context = JAXBContext.newInstance(CaseBuilderForm.class);
			Unmarshaller um = context.createUnmarshaller();
			CaseBuilderForm caseForm = (CaseBuilderForm) um.unmarshal(stream);
			return caseForm;
		} catch (JAXBException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public static void generateEligibilityStatements(ComplianceDataUtil complianceData, Product product,
			CaseBuilderForm caseForm) {
		List<String> disclosureList = new ArrayList<String>();
		List<String> questionsList = new ArrayList<String>();
		List<String> affirmationList = new ArrayList<String>();
		List<String> healthQuestionList = new ArrayList<String>();
//		String[] productNameValues = null;
//		String productName=null;
//		if(product.getProductName()!=null) {
//			productNameValues=product.getProductName().split("-");
//			productName = productNameValues[0];
//		}
		

		// filtering disclosures based on the ProductName
		List<ComplianceDetail> disclosures = complianceData.getComplianceData(Labels.DISCLOSURE.getValue(), product.getProductName(),product.getProductId());

		// for adding disclosure values in disclosureList
		for (ComplianceDetail disclosure : disclosures) {
			disclosureList.add(disclosure.getComplianceText());
		}

		// filtering questions based on the ProductName
		List<ComplianceDetail> questions = complianceData.getComplianceData(Labels.QUESTION.getValue(), product.getProductName(),product.getProductId());

		// for adding question values in questionsList
		for (ComplianceDetail question : questions) {
			questionsList.add(question.getComplianceText());
		}
		
		// filtering affirmations based on the ProductName
		List<ComplianceDetail> affirmations = complianceData.getComplianceData(Labels.AFFIRMATION.getValue(), product.getProductName(),product.getProductId());

		// for adding affirmation values in affirmationList
		for (ComplianceDetail affirmation : affirmations) {
			affirmationList.add(affirmation.getComplianceText());
		}
		
		// filtering healthQuestions based on the ProductName
		List<ComplianceDetail> healthQuestions = complianceData.getComplianceData(Labels.HEALTH_QUESTION.getValue(), product.getProductName(),product.getProductId());

		// for adding healthQuestion values in affirmationList
		for (ComplianceDetail healthQuestion : healthQuestions) {
			healthQuestionList.add(healthQuestion.getComplianceText());
		}
		
		product.getQuestions().setComplianceText(questionsList);
		product.getDisclosures().setComplianceText(disclosureList);
		product.getAffirmations().setComplianceText(affirmationList);
		product.getHealthQuestions().setComplianceText(healthQuestionList);
	}

	public static String convertObjectToXML(Object obj) throws javax.xml.bind.JAXBException {
		// create JAXB context and instantiate marshaller
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		StringWriter sw = new StringWriter();
		m.marshal(obj, sw);
		String xmlString = sw.toString();
		log.info("PDF XML:"+xmlString);
		return xmlString;

	}
}
