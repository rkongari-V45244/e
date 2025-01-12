package com.aflac.core.util;

public enum MasterAppFormIds {
	
	C03204("C03204,C03204AL,C03204AR,C03204AZ,C03204CO,C03204CT,C03204DC,C03204DE,C03204FL,C03204GA,C03204HI,C03204IL,C03204KS,C03204KY,C03204LA,C03204MA,C03204MD,C03204ME,C03204MI,C03204MN,C03204MO,C03204MT,C03204NC,C03204ND,C03204NE,C03204NV,C03204OH,C03204NJ,C03204OK,C03204OR,C03204RI,C03204SD,C03204TN,C03204TX,C03204UT,C03204VT,C03204WV,C03204WY"),
	C02204("C02204CA,C02204HI,C02204IN,C02204MT,C02204PA"),
	C01204("C01204,C01204CA,C01204ID,C01204NM,C01204VA"),
	C22201("C22201CA,C22201ID,C22201MS,C22201NH,C22201PA,C22201VA"),
	C60201("C60201CA,C60201ID,C60201NH,C60201NM,C60201VA,C60201WA"),
	WL9800_MA("WL9800-MA,WL9800-MA NM,WL9800-MA (WA)"),
	CAI9110("CAI9110,CAI9110NM,CAI9110VA"),
	C81201("C81201,C81201VA"),
	C80201("C80201.2NH,C80201WA"),
	C70201("C70201.2NH,C70201WA"),
	C50201("C50201NH,C50201WA"),
	C21201("C21201.1NH,C21201WA"),
	GP5000_MA("GP5000-MA (D2/CI),GP-5000-MA-OH");
	
	private String value;
	
	MasterAppFormIds(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

}
