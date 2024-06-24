function clearValues(productOfferedValues){
	var GA_productName = guideBridge.resolveNode("GA_productName");
	var CI_productName = guideBridge.resolveNode("productName_1");
    var HI_productName = guideBridge.resolveNode("productName_2");
    var D_productName = guideBridge.resolveNode("productName_3");
    var DI_productName = guideBridge.resolveNode("productName_4");
    var TL_productName = guideBridge.resolveNode("productName_5");
    var WL_productName = guideBridge.resolveNode("productName_6");
    
	//console.log("productOfferedValues=",productOfferedValues);

    if(productOfferedValues.includes("Group Accident")){
         //console.log("Group Accident found");	
		 GA_productName.value = "Group Accident";
    }

    if(productOfferedValues.includes("Group Critical Illness")){
		   //console.log("Group Critical Illness found");
           CI_productName.value = "Group Critical Illness";
    }

    if(productOfferedValues.includes("Group Hospital Indemnity")){
       // console.log("Group Hospital Indemnity found");
        HI_productName.value = "Group Hospital Indemnity";
    }

    if(productOfferedValues.includes("Group Dental")){
       // console.log("Group Dental found");
        D_productName.value = "Group Dental";
    }

    if(productOfferedValues.includes("Group Disability Income")){
       // console.log("Group Disability Income found");
        DI_productName.value = "Group Disability Income";
    }

    if(productOfferedValues.includes("Group Term Life")){
       // console.log("Group Term Life found");
        TL_productName.value = "Group Term Life";
    }

    if(productOfferedValues.includes("Group Whole Life")){
       // console.log("Group Whole Life found");
        WL_productName.value = "Group Whole Life";
    }
}