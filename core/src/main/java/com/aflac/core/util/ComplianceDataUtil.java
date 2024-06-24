package com.aflac.core.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.aflac.core.experience.api.model.ComplianceDetail;
import com.aflac.core.experience.api.model.ComplianceVerbiageDetail;
import com.aflac.core.experience.api.model.LabelSequence;

public class ComplianceDataUtil {

	private ComplianceVerbiageDetail verbiage;
	private LabelSequence sequence;

	public ComplianceDataUtil() {
	}

	public ComplianceDataUtil(ComplianceVerbiageDetail verbiage, LabelSequence sequence) {
		this.verbiage = verbiage;
		this.sequence = sequence;
	}

	public List<ComplianceDetail> getComplianceData(String type, String productName,String productId) {
		List<ComplianceDetail> statements = new ArrayList<>();
		if(sequence != null && sequence.getSequence() != null && verbiage != null && verbiage.getComplianceVerbiageList() != null) {
			List<String> labelSeq = sequence.getSequence().stream()
					.sorted(Comparator.comparingInt(s -> Integer.parseInt(s.getValue()))).map(s -> s.getId())
					.collect(Collectors.toList());
			statements = verbiage.getComplianceVerbiageList().stream()
					.filter(complaince -> (complaince.getVerbiageType().getValue().equalsIgnoreCase(type)))
					.filter(obj -> (obj.getLob().stream().anyMatch(
							lob -> lob.getValue().equalsIgnoreCase(productName) || lob.getValue().equalsIgnoreCase("All"))))
					.filter(complaince -> (complaince.getProductId().equalsIgnoreCase(productId)))
					.sorted(Comparator.comparingInt(complaince -> labelSeq.indexOf((complaince.getLabel().getId()))))
					.collect(Collectors.toList());
		}
		
		return statements;
	}

}
