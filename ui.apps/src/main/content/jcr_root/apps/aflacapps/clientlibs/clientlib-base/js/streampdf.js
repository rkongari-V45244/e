// dummyInput otherwise on rules screen this function will not drag.
var agreementFilePath ="";
function loadData(dummyInput) {
    var searchField = guideBridge.resolveNode("gpsearchNumber");
    var repeatPanel = guideBridge.resolveNode("repPanel");
    var inpData = searchField.value;
    //Remove all data
    var delCount = repeatPanel.instanceManager.instanceCount;

   // console.log(delCount);

    for (var m = 0; m < delCount; m++) {
        if (m != 0) {
            repeatPanel.instanceManager.removeInstance(1);
        }
    }
    repeatPanel.instanceManager.instances[0].tableRowPolicyHolder.value = "";
    repeatPanel.instanceManager.instances[0].tableRowGpNumber.value = "";
    repeatPanel.instanceManager.instances[0].tableRowSignedOn.value = "";
    repeatPanel.instanceManager.instances[0].tableRowEffectiveDate.value = "";

    //Ends
    var res = JSON.parse(
        $.ajax({
            url: "/bin/GetApplicationData",
            type: "GET",
            async: false,
            data: {
                "searchParam": inpData
            },
            success: function(data) {
             //   console.log(data);
                repeatPanel.instanceManager.addInstance();
                repeatPanel.instanceManager.instances[0].tableRowPolicyHolder.value = data.organizationName;
                repeatPanel.instanceManager.instances[0].tableRowGpNumber.value = data.groupProposalNo;
                repeatPanel.instanceManager.instances[0].tableRowEffectiveDate.value = data.coverageEffectiveDate;
                repeatPanel.instanceManager.instances[0].tableRowSignedOn.value = data.SignedDateTime;
                agreementFilePath = data.agreementFilePath;
            }
        })
        .responseText);
}

// dummyInput otherwise on rules screen this function will not drag.
//
function previewPDF(dummyInput) {
    guideBridge.getDataXML({
        success: function(guideResultObject) { 
            var req = new XMLHttpRequest();

            //code for loader
            var loader = document.createElement('div');
            loader.setAttribute('id', 'previewLoader');
            loader.setAttribute('class', 'loader');
            loader.rel = 'stylesheet';
            loader.type = 'text/css';
            loader.href = '/css/loader.css';
            document.getElementsByTagName('BODY')[0].appendChild(loader);
            console.log(loader);
            document.getElementById("guideContainerForm").style.filter="blur(10px)";
            //code for loader

            req.open("POST", "/bin/CreatePDF", true);
            req.responseType = "blob"; 
            var postParameters = new FormData();
            postParameters.append("formData", guideResultObject.data);
            postParameters.append("mode", "preview");
            req.send(postParameters);
            req.onreadystatechange = function() {

                if (req.readyState == 4 && req.status == 200) {
                    var blob = new Blob([this.response], {
                            type: "application/pdf"
                        }),
                        newUrl = URL.createObjectURL(blob);
                    window.open(newUrl, "_blank", "menubar=yes,resizable=yes,scrollbars=yes");
                    document.getElementById("guideContainerForm").style.filter="blur()";
                    loader.setAttribute('class', 'loader-disable');
                }
            }
        }
    });
}

function downloadPDF(dummyInput) {
	var searchField = guideBridge.resolveNode("gpsearchNumber");
    var inpData = searchField.value;

   guideBridge.getDataXML({
        success: function(guideResultObject) {
            var req = new XMLHttpRequest();

            req.open("POST", "/bin/CreatePDF", true);
            req.responseType = "blob"; 
           var postParameters = new FormData();
            postParameters.append("formData", guideResultObject.data);
            postParameters.append("mode", "download");
            postParameters.append("searchParam", inpData);
            postParameters.append("agreementFilePath", agreementFilePath);
            req.send(postParameters);
            req.onreadystatechange = function() {

                if (req.readyState == 4 && req.status == 200) {
                    var blob = new Blob([this.response], {
                            type: "application/pdf"
                       }),
                        newUrl = URL.createObjectURL(blob);
                    window.open(newUrl, "_blank", "menubar=yes,resizable=yes,scrollbars=yes");
                }
            };
        }
    });
}

// dummyInput otherwise on rules screen this function will not drag.
function publishPDF(dummyInput) {

    //validation
    var errors = [];
    window.guideBridge.validate(errors, "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].editGroupMasterAppInput[0].wrapperPanel[0].policyHolderDetail[0]");
    window.guideBridge.validate(errors, "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].editGroupMasterAppInput[0].wrapperPanel[0].generalRequirement[0]");
    window.guideBridge.validate(errors, "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].editGroupMasterAppInput[0].wrapperPanel[0].groupAccident_copy_1[0]");
    window.guideBridge.validate(errors, "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].editGroupMasterAppInput[0].wrapperPanel[0].groupCriticalIllness[0]");
    window.guideBridge.validate(errors, "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].editGroupMasterAppInput[0].wrapperPanel[0].groupHospitalIndemnity[0]");
    window.guideBridge.validate(errors, "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].editGroupMasterAppInput[0].wrapperPanel[0].groupDental[0]");
    window.guideBridge.validate(errors, "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].editGroupMasterAppInput[0].wrapperPanel[0].groupDisabilityIncome[0]");
    window.guideBridge.validate(errors, "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].editGroupMasterAppInput[0].wrapperPanel[0].groupTermLife[0]");
    window.guideBridge.validate(errors, "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].editGroupMasterAppInput[0].wrapperPanel[0].groupWholeLife[0]");
    // console.log("The errors are "+ errors.length);
    if (errors.length === 0) {
        guideBridge.getDataXML({
            success: function(guideResultObject) {
                var req = new XMLHttpRequest();

                //code for loader
            	var loader = document.createElement('div');
            	loader.setAttribute('id', 'publishLoader');
            	loader.setAttribute('class', 'loader');
            	loader.rel = 'stylesheet';
            	loader.type = 'text/css';
            	loader.href = '/css/loader.css';
            	document.getElementsByTagName('BODY')[0].appendChild(loader);
            	console.log(loader);
            	document.getElementById("guideContainerForm").style.filter="blur(10px)";
            //code for loader

                req.open("POST", "/bin/CreatePDF", true);
                req.responseType = "blob";
                var postParameters = new FormData();
                postParameters.append("formData", guideResultObject.data);
                postParameters.append("mode", "save");
                req.send(postParameters);
                req.onreadystatechange = function() {

                    if (req.readyState == 4 && req.status == 200) {
                        var blob = new Blob([this.response], {
                                type: "application/pdf"
                            }),
                            newUrl = URL.createObjectURL(blob);
                        window.open(newUrl, "_blank", "menubar=yes,resizable=yes,scrollbars=yes");
                        document.getElementById("guideContainerForm").style.filter="blur()";
                    	loader.setAttribute('class', 'loader-disable');
                    }
                }
            }
        });
    } else {
        var errorField = errors[0].getFocus();
        window.guideBridge.setFocus(errorField);
    }
}

function editForm(groupProposalNo){
	window.location.href="/content/dam/formsanddocuments/aflacapps/GroupMasterApplication/jcr:content?wcmmode=disabled&dataRef=crx:///content/dam/formsanddocuments/aflac/output/"+ groupProposalNo +"/datajson.xml/jcr:content/renditions/original";
}

function brochurePrefill(groupPlanId){
	window.location.href="/content/dam/formsanddocuments/aflacapps/brochure-generation-tool/jcr:content?wcmmode=disabled&groupPlanId="+ groupPlanId;
}
