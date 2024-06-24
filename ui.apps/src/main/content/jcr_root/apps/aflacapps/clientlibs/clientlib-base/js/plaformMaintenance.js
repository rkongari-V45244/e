
var caseBuildPlatforms;
var platformDetails;
var maintenanceScreenResponse = {};
var selectedPlatform = "";

function fetchMaintenancePlatforms(){
    caseBuildPlatforms=[];
    platformDetails=[];

    var res = JSON.parse(
        $.ajax({
            url: "/bin/GetCaseBuilderToolProductsData",
            type: "GET",
            async: false,
            data: {
                "mode":"fetchPlatforms"
            },
            success: function (data) {
                if (data != null) {
                    caseBuildPlatforms=data.enrollmentPlatform;
                    platformDetails=data.enrollmentDetails;
                    populatePlatformData();
                }
            }
        })
    .responseText);
}

function populatePlatformData() {
    $(".GroupEnrollmentPlatform input").autocomplete({
        minLength: 0,
        source: caseBuildPlatforms,
        delay: 0,
        select: function(event, ui) {
            selectedPlatform = ui.item.label;
            guideBridge.resolveNode("GroupEnrollmentPlatform").value = selectedPlatform;
            guideBridge.resolveNode("outputFieldsPanel").visible = false;
        },
        open: function() {
            $('.ui-autocomplete').css('max-height', '40%');
            $('.ui-autocomplete').css('width', '21%');
            $('.ui-autocomplete').css('overflow-y', 'auto');
            $('.ui-autocomplete').css('overflow-x', 'hidden');
        }
    }).focus(function() {
        $(this).autocomplete('search', $(this).val())
    });
}

function fetchPlatformMaintenanceData(platform){
    hideMsgs();
    var res = JSON.parse(
        $.ajax({
            url: "/bin/PlatformMaintenance",
            type: "GET",
            async: false,
            data: {
                "platformId": platform
            },
            success: function (resdata) {
                maintenanceScreenResponse= JSON.parse(resdata);                                         
                if (maintenanceScreenResponse == null) {
                    guideBridge.resolveNode("fetchFailMsg").visible = true;
                }
                else{
                    guideBridge.resolveNode("outputFieldsPanel").visible = true;
                    guideBridge.resolveNode("vendorId").value = maintenanceScreenResponse.vendorid;
                    guideBridge.resolveNode("platformName").value = maintenanceScreenResponse.platformname;
                    guideBridge.resolveNode("companyName").value = maintenanceScreenResponse.companyname;
                    guideBridge.resolveNode("healthQues").value = maintenanceScreenResponse.abletoaskhealthunderwritingquestions;
                    guideBridge.resolveNode("spSeparateFromEE").value = maintenanceScreenResponse.rateindependentlyspouseagetobacco;;
                    guideBridge.resolveNode("rateBased").value = maintenanceScreenResponse.abletoratebasedon;
                    guideBridge.resolveNode("ageRateFormat").value=maintenanceScreenResponse.agerateformat;
                    guideBridge.resolveNode("ratePer").value=maintenanceScreenResponse.rateper;
                    guideBridge.resolveNode("tobaccoStatus").value =maintenanceScreenResponse.capturetobaccostatus;
                    guideBridge.resolveNode("questionsAffirmation").value= maintenanceScreenResponse.questionsaffirmation;
                    guideBridge.resolveNode("rateCalculationMethod").value = maintenanceScreenResponse.ratecalculationmethod;
                    guideBridge.resolveNode("ediFileFormat").value = maintenanceScreenResponse.edifileformat;
                    
                }
            }
        })
    .responseText);
}

function deletePlatform(){
    hideMsgs();
    guideBridge.resolveNode("outputFieldsPanel").visible = false;
    var loader = document.createElement('div');
    loader.setAttribute('id', 'previewLoader');
    loader.setAttribute('class', 'loader');
    loader.rel = 'stylesheet';
    loader.type = 'text/css';
    loader.href = '/css/loader.css';
    document.getElementsByTagName('BODY')[0].appendChild(loader);
    document.getElementById("guideContainerForm").style.filter = "blur(10px)";
    document.getElementById("guideContainerForm").style.pointerEvents = "none";

    const formData = new FormData();
    formData.append("formData", selectedPlatform);
    formData.append("mode","delete");
    var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "/bin/PlatformMaintenance", true);
        xhttp.send(formData);

        xhttp.onreadystatechange = function() {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                var x = JSON.parse(xhttp.responseText);
                if(x["status"] == true) {
                    guideBridge.resolveNode("deleteMsg").visible = true;
                }
                else{
                    guideBridge.resolveNode("deleteFailMsg").visible = true;
                }
                fetchMaintenancePlatforms();
                guideBridge.resolveNode("GroupEnrollmentPlatform").value = "";
                clearAll();

                document.getElementById("guideContainerForm").style.filter = "blur()";
                document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                loader.setAttribute('class', 'loader-disable');
            }
        }
}

function addPlatform(){
    maintenanceScreenResponse = {};
    clearValues();
    hideMsgs();
    guideBridge.resolveNode("outputFieldsPanel").visible = true;
}

function savePlatform(){
    hideMsgs();
    var addFlag = true;
    var platformJson = JSON.stringify(maintenanceScreenResponse);
    var p = guideBridge.resolveNode("vendorId").value + "|" + guideBridge.resolveNode("platformName").value + "|" + guideBridge.resolveNode("companyName").value;
    if(platformJson === '{}'){
        if(caseBuildPlatforms.indexOf(p) > -1){
            addFlag = false;
        }
        platformJson = {
            "platform-id": guideBridge.resolveNode("vendorId").value + "|" + guideBridge.resolveNode("platformName").value + "|" + guideBridge.resolveNode("companyName").value,
            "vendor-id": guideBridge.resolveNode("vendorId").value,
            "crm-platform-naming": "",
            "platform-name": guideBridge.resolveNode("platformName").value,
            "company-name": guideBridge.resolveNode("companyName").value,
            "worksite-gi-offer-only": "",
            "worksite-accident": "",
            "worksite-critical-illness": "",
            "worksite-hospital-indemnity": "",
            "worksite-term-life": "",
            "worksite-whole-life": "",
            "worksite-std": "",
            "drms-gi-offfer-only": "",
            "drms-std": "",
            "drms-ltd": "",
            "drms-term-life": "",
            "enrollment-methods-supported": "",
            "able-to-rate-based-on": guideBridge.resolveNode("rateBased").value == undefined ? "" : guideBridge.resolveNode("rateBased").value.replace(",",";#"),
            "age-rate-format": guideBridge.resolveNode("ageRateFormat").value == undefined ? "" : guideBridge.resolveNode("ageRateFormat").value.replace(",",";#"),
            "rate-per": guideBridge.resolveNode("ratePer").value == undefined ? "" : guideBridge.resolveNode("ratePer").value.replace(",",";#"),
            "capture-tobacco-status": guideBridge.resolveNode("tobaccoStatus").value,
            "rate-independently-spouse-age-tobacco": guideBridge.resolveNode("spSeparateFromEE").value,
            "questions-affirmation": guideBridge.resolveNode("questionsAffirmation").value,
            "rate-calculation-method": guideBridge.resolveNode("rateCalculationMethod").value == undefined ? "" : guideBridge.resolveNode("rateCalculationMethod").value.replace(",",";#"),
            "able-to-ask-health-underwriting-questions": guideBridge.resolveNode("healthQues").value,
            "able-to-ask-eligibility-questions": "",
            "able-to-ask-replacement-questions": "",
            "able-to-handle-ci-di-buyup": "",
            "edi-file-format": guideBridge.resolveNode("ediFileFormat").value
        }
    } else {
        platformJson = {
            "platform-id": selectedPlatform,
            "vendor-id": guideBridge.resolveNode("vendorId").value,
            "crm-platform-naming": maintenanceScreenResponse.crmplatformnaming,
            "platform-name": guideBridge.resolveNode("platformName").value,
            "company-name": guideBridge.resolveNode("companyName").value,
            "worksite-gi-offer-only": maintenanceScreenResponse.worksitegiofferonly,
            "worksite-accident": maintenanceScreenResponse.worksiteaccident,
            "worksite-critical-illness": maintenanceScreenResponse.worksitecriticalillness,
            "worksite-hospital-indemnity": maintenanceScreenResponse.worksitehospitalindemnity,
            "worksite-term-life": maintenanceScreenResponse.worksitetermlife,
            "worksite-whole-life": maintenanceScreenResponse.worksitewholelife,
            "worksite-std": maintenanceScreenResponse.worksitestd,
            "drms-gi-offfer-only": maintenanceScreenResponse.drmsgioffferonly,
            "drms-std": maintenanceScreenResponse.drmsstd,
            "drms-ltd": maintenanceScreenResponse.drmsltd,
            "drms-term-life": maintenanceScreenResponse.drmstermlife,
            "enrollment-methods-supported": maintenanceScreenResponse.enrollmentmethodssupported,
            "able-to-rate-based-on": guideBridge.resolveNode("rateBased").value == undefined ? "" : guideBridge.resolveNode("rateBased").value.replace(",",";#"),
            "age-rate-format": guideBridge.resolveNode("ageRateFormat").value == undefined ? "" : guideBridge.resolveNode("ageRateFormat").value.replace(",",";#"),
            "rate-per": guideBridge.resolveNode("ratePer").value == undefined ? "" : guideBridge.resolveNode("ratePer").value.replace(",",";#"),
            "capture-tobacco-status": guideBridge.resolveNode("tobaccoStatus").value,
            "rate-independently-spouse-age-tobacco": guideBridge.resolveNode("spSeparateFromEE").value,
            "questions-affirmation": guideBridge.resolveNode("questionsAffirmation").value,
            "rate-calculation-method": guideBridge.resolveNode("rateCalculationMethod").value == undefined ? "" : guideBridge.resolveNode("rateCalculationMethod").value.replace(",",";#"),
            "able-to-ask-health-underwriting-questions": guideBridge.resolveNode("healthQues").value,
            "able-to-ask-eligibility-questions": maintenanceScreenResponse.abletoaskeligibilityquestions,
            "able-to-ask-replacement-questions": maintenanceScreenResponse.abletoaskreplacementquestions,
            "able-to-handle-ci-di-buyup": maintenanceScreenResponse.abletohandlecidibuyup,
            "edi-file-format": guideBridge.resolveNode("ediFileFormat").value
        }
    }

    if(addFlag == true){
        var loader = document.createElement('div');
        loader.setAttribute('id', 'previewLoader');
        loader.setAttribute('class', 'loader');
        loader.rel = 'stylesheet';
        loader.type = 'text/css';
        loader.href = '/css/loader.css';
        document.getElementsByTagName('BODY')[0].appendChild(loader);
        document.getElementById("guideContainerForm").style.filter = "blur(10px)";
        document.getElementById("guideContainerForm").style.pointerEvents = "none";

        const formData = new FormData();
        formData.append("formData", JSON.stringify(platformJson));
        formData.append("mode","save");
        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "/bin/PlatformMaintenance", true);
        xhttp.send(formData);

        xhttp.onreadystatechange = function() {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                var x = JSON.parse(xhttp.responseText);
                if(x["status"] == true) {
                    guideBridge.resolveNode("updateMsg").visible = true;
                }
                else{
                    guideBridge.resolveNode("failMsg").value = "<p><span class=\"redColorText\"><b>Fail to save data. Please contact your system administrator.</b></span></p>"
                    guideBridge.resolveNode("failMsg").visible = true;
                }
                guideBridge.resolveNode("GroupEnrollmentPlatform").value = "";
                fetchMaintenancePlatforms();
                document.getElementById("guideContainerForm").style.filter = "blur()";
                document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                loader.setAttribute('class', 'loader-disable');
            }
        }
    } else {
        guideBridge.resolveNode("failMsg").value = "<p><span class=\"redColorText\"><b>Fail to save the data because platform \"" + p + "\" already exists.</b></span></p>";
        guideBridge.resolveNode("failMsg").visible = true;
    }
}

function clearAll(){
    clearValues();
    guideBridge.resolveNode("outputFieldsPanel").visible = false;
    guideBridge.resolveNode("EnrollmentDetails").visible = true;
}

function clearValues(){
    var platformJson = JSON.stringify(maintenanceScreenResponse);
    if(platformJson === '{}'){
        guideBridge.resolveNode("GroupEnrollmentPlatform").value = "";
        guideBridge.resolveNode("vendorId").value = "";
        guideBridge.resolveNode("platformName").value = "";
        guideBridge.resolveNode("companyName").value = "";
        guideBridge.resolveNode("healthQues").value = "";
        guideBridge.resolveNode("spSeparateFromEE").value = "";
        guideBridge.resolveNode("rateBased").value = "";
        guideBridge.resolveNode("ageRateFormat").value= "";
        guideBridge.resolveNode("ratePer").value= "";
        guideBridge.resolveNode("tobaccoStatus").value = "";
        guideBridge.resolveNode("questionsAffirmation").value = "";
        guideBridge.resolveNode("rateCalculationMethod").value = "";
        guideBridge.resolveNode("ediFileFormat").value = "";
    } else {
        guideBridge.resolveNode("healthQues").value = "";
        guideBridge.resolveNode("spSeparateFromEE").value = "";
        guideBridge.resolveNode("rateBased").value = "";
        guideBridge.resolveNode("ageRateFormat").value= "";
        guideBridge.resolveNode("ratePer").value= "";
        guideBridge.resolveNode("tobaccoStatus").value = "";
        guideBridge.resolveNode("questionsAffirmation").value = "";
        guideBridge.resolveNode("rateCalculationMethod").value = "";
        guideBridge.resolveNode("ediFileFormat").value = "";
    }
    
}

function hideMsgs() {
    guideBridge.resolveNode("updateMsg").visible = false;
    guideBridge.resolveNode("failMsg").visible = false;
    guideBridge.resolveNode("deleteFailMsg").visible = false;
    guideBridge.resolveNode("deleteMsg").visible = false;
    guideBridge.resolveNode("fetchFailMsg").visible = false;
}