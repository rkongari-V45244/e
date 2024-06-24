package com.aflac.xml.casebuilderProducts.models;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Compliance {

	@XmlElement(required = true)
	private String lable;
	@XmlElement(required = true)
	private List<String> verbiage;
	
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public List<String> getVerbiage() {
		return verbiage;
	}
	public void setVerbiage(List<String> verbiage) {
		this.verbiage = verbiage;
	}	
}
