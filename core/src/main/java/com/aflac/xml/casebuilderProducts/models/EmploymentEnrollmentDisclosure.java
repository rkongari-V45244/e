package com.aflac.xml.casebuilderProducts.models;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EmploymentEnrollmentDisclosure {

	@XmlElement(required = true)
	private List<Compliance> compliance;

	public List<Compliance> getCompliance() {
		return compliance;
	}

	public void setCompliance(List<Compliance> compliance) {
		this.compliance = compliance;
	}	
}
