var masterAppResponse;
function fetchMasterAppData(){
    masterAppResponse = "";
    var GroupID = guideBridge.resolveNode("masterAppGroupNo").value;
    var effectiveDate = guideBridge.resolveNode("coverageEffDateMasterApp").value;
	var optionalFeaturesTextboxValue;
    var fetchDropdown = [];

    var errors = [];
    guideBridge.validate(errors, guideBridge.resolveNode("masterAppGroupNo").somExpression);
    guideBridge.validate(errors, guideBridge.resolveNode("coverageEffDateMasterApp").somExpression);
    
    if (errors.length === 0) {
        if(GroupID != null && effectiveDate != null){
            var res = JSON.parse(
                $.ajax({
                    url: "/bin/MasterAppServlet",
                    type: "GET",
                    async: false,
                    data: {
                        "groupNumber": GroupID,
                        "effectiveDate":effectiveDate,
                        "mode":"fetch"
                    },
                    success: function (data) {
                        var currentdate = new Date();
                        var day = currentdate.getDate()+"";
                        day = day.length == 1 ? "0"+day : day;
                        var month = (currentdate.getMonth() + 1)+"";
                        month = month.length == 1 ? "0"+month : month;
                        var hours = currentdate.getHours()+"";
                        hours = hours.length == 1 ? "0"+hours : hours;
                        var min = currentdate.getMinutes()+"";
                        min = min.length == 1 ? "0"+min : min;
                        var sec = currentdate.getSeconds()+"";
                        sec = sec.length == 1 ? "0"+sec : sec;
                        var zone = Intl.DateTimeFormat().resolvedOptions().timeZone;
                        var startTime = currentdate.getFullYear() + "-" + month  + "-"  + day + " "  + hours + ":"  + min + ":" + sec + " " + zone;
                        guideBridge.resolveNode("startTime").value = startTime;
                        if (data != null) {
                            masterAppResponse = data;
                            guideBridge.resolveNode("masterAppFormCase").value = "Edit";
                            guideBridge.resolveNode("masterAppFound").visible = true;
                        }
                        else {
                            guideBridge.resolveNode("masterAppFormCase").value = "Add";
                            guideBridge.resolveNode("masterAppNotFound").visible = true;
                        }
                    }
                })
            .responseText);
            // console.log("bala",res);
        }
    }
}

function validateMasterAppGroupNo(){
    var GroupID = guideBridge.resolveNode("masterAppGroupNo").value;
    var field = guideBridge.resolveNode("masterAppGroupNo");
    
    if(guideBridge.resolveNode('GroupNoRadioButton').value=="Group Number"){
        if (GroupID.length > 0 && GroupID.length < 10 && !isNaN(GroupID)) {
            GroupID = GroupID.padStart(10, '0')
            guideBridge.resolveNode("masterAppGroupNo").value = GroupID;
        }

        if (!isNaN(GroupID) && GroupID.length == 10 && !GroupID.includes('+') && !GroupID.includes('-') && !GroupID.includes('.') && GroupID != '0000000000') {
            document.getElementById(field.id).className = "guideFieldNode guideTextBox masterAppGroupNo defaultFieldLayout af-field-filled validation-success ";
        } 
        else if (GroupID.length == 13 && GroupID.startsWith("AGC") && !isNaN(GroupID.slice(3, 13)) && !GroupID.includes('+') && !GroupID.includes('-') && !GroupID.includes('.')) {
            document.getElementById(field.id).className = "guideFieldNode guideTextBox masterAppGroupNo defaultFieldLayout af-field-filled validation-success ";
        }
        else {
            guideBridge.resolveNode("masterAppGroupNo").value = "";
            document.getElementById(field.id).className = "guideFieldNode guideTextBox masterAppGroupNo defaultFieldLayout af-field-filled validation-failure";
            setTimeout(function() {
                var alert = document.getElementById(field.id).children[3];
                alert.setAttribute('role', 'alert');
                var alertid = "#" + alert.id;
                $(alertid).html("This is not a valid Aflac Group number. Please re-enter.");
            },10);
        }
    }
    else{
        if (GroupID.length > 0 && GroupID.length < 10 && !isNaN(GroupID)) {
            guideBridge.resolveNode("masterAppGroupNo").value = "GP-"+GroupID;
            GroupID = "GP-"+GroupID;
        }
        if (GroupID.length > 3 && GroupID.length <= 11 && GroupID.startsWith("GP-") && !isNaN(GroupID.slice(3, 11)) && !GroupID.includes('+') && !GroupID.slice(3, 11).includes('-') && !GroupID.includes('.') && !GroupID.slice(3, 11).startsWith('0')) {
            document.getElementById(field.id).className = "guideFieldNode guideTextBox masterAppGroupNo defaultFieldLayout af-field-filled validation-success ";
        } 
        else {
            guideBridge.resolveNode("masterAppGroupNo").value = "";
            document.getElementById(field.id).className = "guideFieldNode guideTextBox masterAppGroupNo defaultFieldLayout af-field-filled validation-failure";
            setTimeout(function() {
                var alert = document.getElementById(field.id).children[3];
                alert.setAttribute('role', 'alert');
                var alertid = "#" + alert.id;
                $(alertid).html("This is not a valid GP-ID. Please re-enter.");
            },10);
        }
    }
}

function onChangingGPIDRadio(){
    var field = guideBridge.resolveNode("masterAppGroupNo");
    guideBridge.resolveNode("masterAppGroupNo").value = null;
    document.getElementById(field.id).className = "guideFieldNode guideTextBox masterAppGroupNo defaultFieldLayout ";
    guideBridge.resolveNode("coverageEffDateMasterApp").value = "";
    guideBridge.resolveNode("masterAppNotFound").visible = false;
    guideBridge.resolveNode("masterAppFound").visible = false;
}

function populateMasterAppSitus() {
    var selectedSitus = "";
    $(".masterAppSitus input").autocomplete({
        minLength: 0,
        source: situsStates,
        delay: 0,
        select: function(event, ui) {
            selectedSitus = ui.item.label;
            guideBridge.resolveNode("masterAppSitus").value = selectedSitus;
        },open: function() {
            $('.ui-autocomplete').css('max-height', '40%');
            $('.ui-autocomplete').css('overflow-y', 'auto');
            $('.ui-autocomplete').css('overflow-x', 'hidden');
        },
    }).focus(function() {
        $(this).autocomplete('search', $(this).val())
    });
}

function validMasterAppSitus() {
    var situs = guideBridge.resolveNode("masterAppSitus").value;
    var field = guideBridge.resolveNode("masterAppSitus");
    if (!situsStates.includes(situs)) {
        guideBridge.resolveNode("masterAppSitus").value = '';
        clearProductCheckbox();
        guideBridge.resolveNode("failMsg_NoLOBsFound").visible = false;
        document.getElementById(field.id).className = "guideFieldNode guideTextBox masterAppSitus defaultFieldLayout af-field-filled validation-failure";
        setTimeout(function() {
            var alert = document.getElementById(field.id).children[2];
            alert.setAttribute('role', 'alert');
            var alertid = "#" + alert.id;
            $(alertid).html("This is not a valid Situs.");
        }, 10);
    } else {
        document.getElementById(field.id).className = "guideFieldNode guideTextBox masterAppSitus defaultFieldLayout af-field-filled validation-success ";
    }
}

var masterAppSeriesData;
function setApplicableSeriesItems(){
    guideBridge.resolveNode("failMsg_NoLOBsFound").visible = false;
    var formCase = guideBridge.resolveNode("masterAppFormCase").value;

    var situs = guideBridge.resolveNode("masterAppSitus").value;
    var groupType = guideBridge.resolveNode("masterAppGroupType").value;
    var census = guideBridge.resolveNode("masterAppCensus").value; 
    clearProductCheckbox(); 

    if(situs != null && groupType != null){
        var res = JSON.parse(
            $.ajax({
                url: "/bin/MasterAppServlet",
                type: "GET",
                async: false,
                data: {
                    "situs": situs,
                    "groupType":groupType,
                    "census":census,
                    "mode":"fetchSeries"
                },
                success: function (data) {
                    if (data != null) {
                        guideBridge.resolveNode("failMsg_NoLOBsFound").visible = false;
                        guideBridge.resolveNode("MasterAppFormIds").value = data.formids;
                        masterAppSeriesData = data.masterAppSeriesData;  

                        guideBridge.resolveNode("productSelectionCheckboxPanel").resetData();
                        guideBridge.resolveNode("selectLOBtext").visible = true;
                        var accidentArray = [];
                        var hospitalIndemnityArray = [];
                        var criticalIllnessArray = [];
                        var disabilityArray = [];
                        var wholeLifeArray = [];
                        var termLifeArray = [];
                        var dentalArray = [];

                        for (var i=0; i<data.eligibleSeries.length; i++){
                            for (var j=0; j<masterAppLOBdata.length; j++){
                                if(masterAppLOBdata[j].Series.includes(data.eligibleSeries[i])){
                                    if(masterAppLOBdata[j].ProductPrefix == "AC"){
                                        accidentArray.push(data.eligibleSeries[i]);
                                    }
                                    if(masterAppLOBdata[j].ProductPrefix == "HI"){
                                        hospitalIndemnityArray.push(data.eligibleSeries[i]);                                       
                                    }
                                    if(masterAppLOBdata[j].ProductPrefix == "CI"){
                                        criticalIllnessArray.push(data.eligibleSeries[i]);                                    
                                    }
                                    if(masterAppLOBdata[j].ProductPrefix == "DI"){
                                        disabilityArray.push(data.eligibleSeries[i]);                                        
                                    }
                                    if(masterAppLOBdata[j].ProductPrefix == "WL"){
                                        wholeLifeArray.push(data.eligibleSeries[i]);                                        
                                    }
                                    if(masterAppLOBdata[j].ProductPrefix == "TL"){
                                        termLifeArray.push(data.eligibleSeries[i]);                                                                                    
                                    }
                                    if(masterAppLOBdata[j].ProductPrefix == "D"){
                                        dentalArray.push(data.eligibleSeries[i]);                                        
                                    }
                                }
                            }
                        }
                        setProductsChecbox("accidentCheckbox", accidentArray);
                        setProductsChecbox("hospitalIndemnityCheckbox", hospitalIndemnityArray);
                        setProductsChecbox("criticalIllnessCheckbox", criticalIllnessArray);
                        setProductsChecbox("disabilityCheckbox", disabilityArray);
                        setProductsChecbox("wholeLifeCheckbox", wholeLifeArray);
                        setProductsChecbox("termLifeCheckbox", termLifeArray);
                        setProductsChecbox("dentalCheckbox", dentalArray);

                        if(formCase == "Edit"){
                            setMAProductCheckBoxValues();
                        }
                    }
                    else {
                        clearProductCheckbox();
                        guideBridge.resolveNode("failMsg_NoLOBsFound").visible = true;                        
                    }
                }
            })
        .responseText);
    }
}

function setProductsChecbox(fieldname, value){
    if(value.length > 0){
        guideBridge.resolveNode(fieldname).visible = true;
        guideBridge.resolveNode(fieldname).items = value;
    }
    else{
        guideBridge.resolveNode(fieldname).visible = false;
    }
}

function setApplicableSeries(){
    accidentValues = guideBridge.resolveNode("accidentCheckbox").value;
    hospitalValues = guideBridge.resolveNode("hospitalIndemnityCheckbox").value;
    crticalValues = guideBridge.resolveNode("criticalIllnessCheckbox").value;
    disabilityValues = guideBridge.resolveNode("disabilityCheckbox").value;
    wholeLifeValues = guideBridge.resolveNode("wholeLifeCheckbox").value;
    termLifeValues = guideBridge.resolveNode("termLifeCheckbox").value;
    dentalValues = guideBridge.resolveNode("dentalCheckbox").value;
    
    var values="";
    if(accidentValues!=null){
        values+=accidentValues+",";
    }
    if(crticalValues!=null){
        values+=crticalValues+",";
    }
    if(hospitalValues!=null){
        values+=hospitalValues+",";
    }
    if(dentalValues!=null){
        values+= dentalValues+",";
    }
    if(disabilityValues!=null){
        values+=disabilityValues+",";
    }
    if(termLifeValues!=null){
        values+= termLifeValues+",";
    }
    if(wholeLifeValues!=null){
        values+=wholeLifeValues+",";
    }
    return values;
}

function clearProductCheckbox(){
    guideBridge.resolveNode("productSelectionCheckboxPanel").resetData();
    guideBridge.resolveNode("masterAppApplicableSeries").value = "";
    guideBridge.resolveNode("accidentCheckbox").items = "";
    guideBridge.resolveNode("hospitalIndemnityCheckbox").items = "";
    guideBridge.resolveNode("criticalIllnessCheckbox").items = "";
    guideBridge.resolveNode("disabilityCheckbox").items = "";
    guideBridge.resolveNode("wholeLifeCheckbox").items = "";
    guideBridge.resolveNode("termLifeCheckbox").items = "";
    guideBridge.resolveNode("dentalCheckbox").items = "";

    guideBridge.resolveNode("selectLOBtext").visible = false;
    guideBridge.resolveNode("accidentCheckbox").visible = false;
    guideBridge.resolveNode("hospitalIndemnityCheckbox").visible = false;
    guideBridge.resolveNode("criticalIllnessCheckbox").visible = false;
    guideBridge.resolveNode("disabilityCheckbox").visible = false;
    guideBridge.resolveNode("wholeLifeCheckbox").visible = false;
    guideBridge.resolveNode("termLifeCheckbox").visible = false;
    guideBridge.resolveNode("dentalCheckbox").visible = false;
}

function hideProductSelectionPanel(){
    guideBridge.resolveNode("productSelection").resetData();
    document.querySelectorAll(".masterAppGroupType select")[0].value = "";
    guideBridge.resolveNode("masterAppCensus").visible = false;
    guideBridge.resolveNode("productSelection").visible = false;
    clearProductCheckbox(); 
}

function hideProductSelectionPanelSave(){
    var groupTypeBeforeClear = document.querySelectorAll(".masterAppGroupType select")[0].value;
    //guideBridge.resolveNode("productSelection").resetData();
    guideBridge.resolveNode("masterAppGroupType").value = groupTypeBeforeClear;
    guideBridge.resolveNode("masterAppCensus").visible = false;
    guideBridge.resolveNode("productSelection").visible = false;
    clearProductCheckbox(); 
}

function setAccountsAndEligibilityLabel() {
    document.querySelectorAll("#guideContainer-rootPanel-panel-panel_193158218-panel___guide-item-nav > a")[0].innerHTML = "Account and<br>Eligibility";
    document.querySelectorAll("#guideContainer-rootPanel-panel-panel_193158218-panel___guide-item-nav > a")[0].style.paddingTop = "6px";
    document.querySelectorAll("#guideContainer-rootPanel-panel-panel_193158218-panel___guide-item-nav > a")[0].style.paddingBottom = "6px";
}

var masterAppProductsArray =[];
function addMasterAppProducts(){
    var applicableLOBs = guideBridge.resolveNode("masterAppApplicableSeries").value;
    guideBridge.resolveNode("Version").value = "";
    guideBridge.resolveNode("MasterAppEntityId").value = "";
    if (applicableLOBs.trim().endsWith(',')) {
        applicableLOBs = applicableLOBs.trim();
        applicableLOBs = applicableLOBs.slice(0, applicableLOBs.length - 1);
    }
    masterAppProductsArray =[];

    if(applicableLOBs.includes("93000") && applicableLOBs.split(",").length == 1){          
        guideBridge.resolveNode("accountSection").visible = false;
        guideBridge.resolveNode("accountSection").resetData();
        guideBridge.resolveNode("eligibleEmployees").visible=false;
        guideBridge.resolveNode("eligibleEmployees").value="";
        guideBridge.resolveNode("subsidiariesOrAffiliates").visible=false;
        guideBridge.resolveNode("subsidiariesOrAffiliates").value="";
          
        // document.querySelectorAll(".certHolders select")[0].value = "";
        
        //guideBridge.resolveNode("ApplicationRequestPurpose").visible = true;
        //guideBridge.resolveNode("policyNumbers").visible = true;
    }
    else if(applicableLOBs.includes("93000") && applicableLOBs.split(",").length > 1){
        guideBridge.resolveNode("accountSection").visible = true;
        guideBridge.resolveNode("eligibleEmployees").visible=true;
        guideBridge.resolveNode("subsidiariesOrAffiliates").visible=true;
    }
    else{
        guideBridge.resolveNode("accountSection").visible = true;
        guideBridge.resolveNode("eligibleEmployees").visible=true;
        guideBridge.resolveNode("subsidiariesOrAffiliates").visible=true;

        //guideBridge.resolveNode("ApplicationRequestPurpose").value= "";
        //guideBridge.resolveNode("classes_AddedRemoved").value= "";
        //guideBridge.resolveNode("policyNumbers").value= "";

        //guideBridge.resolveNode("ApplicationRequestPurpose").visible = false;
        //guideBridge.resolveNode("classes_AddedRemoved").visible = false;
        //guideBridge.resolveNode("policyNumbers").visible = false;
    }
    
    

    if(applicableLOBs != null && applicableLOBs != "" && guideBridge.resolveNode("masterAppFormCase").value == "Add"){
        var res = JSON.parse(
            $.ajax({
                url: "/bin/MasterAppServlet",
                type: "GET",
                async: false,
                data: {
                    "addedProducts":applicableLOBs,
                    "mode":"addProducts"
                },
                success: function (data) {
                    if (data != null) {
                        var productsArray = data.product;
                        masterAppProductsArray = productsArray;

                        var repeatPanel = guideBridge.resolveNode("masterAppProducts");

                        //Reset Panels
                        repeatPanel.instanceManager.addInstance();
                        var currentCount = repeatPanel.instanceManager.instanceCount;
                        for (var m = 0; m < currentCount; m++) {
                            if (m != 0) {
                                repeatPanel.instanceManager.removeInstance(0);
                            }
                        }

                        for (var i = 0; i < productsArray.length; i++) {
                            if (i != 0) {
                                repeatPanel.instanceManager.addInstance();
                            }

                            // setting product panel data
                            repeatPanel.instanceManager.instances[i].title = productsArray[i].productName;
                            repeatPanel.instanceManager.instances[i].summary = productsArray[i].productName;

                            guideBridge.resolveNode("productName").value = productsArray[i].productName;
                            guideBridge.resolveNode("Series").value = productsArray[i]["series"];

                            setOptionalFeaturesItems();

                            guideBridge.resolveNode("EmployeePremiumPercentage").value = 100;
                            guideBridge.resolveNode("EmployerPremiumPercentage").value = 0;

                            //Accident
                            if(productsArray[i].productName == "AC7700" || productsArray[i].productName == "AC7800" || productsArray[i].productName == "AC70000"){
                                guideBridge.resolveNode("benefitType").visible = true;
                                guideBridge.resolveNode("Plan").visible = true;
                                guideBridge.resolveNode("exceptionRadio").visible = true;
                                guideBridge.resolveNode("productSequence").value = 1;
                            }

                            //Hospital Indemnity
                            if(productsArray[i].productName == "HI8500" || productsArray[i].productName == "HI8800" || productsArray[i].productName == "HI80000" || productsArray[i].productName == "HI81000"){
                                if (productsArray[i].productName == "HI8500" || productsArray[i].productName == "HI8800"){
                                    guideBridge.resolveNode("Plan").visible = true;
                                }
                                guideBridge.resolveNode("exceptionRadio").visible = true;
                                guideBridge.resolveNode("minEnrolledEmployees_HI").visible = true;
                                guideBridge.resolveNode("minEnrolledEmployees_HI").value = "25";
                                guideBridge.resolveNode("productSequence").value = 3;
                                guideBridge.resolveNode("PlanYearMax").visible = true;
                                
                               
                                    if(productsArray[i].productName == "HI80000"){
								    optionalFeaturesTextboxValue = guideBridge.resolveNode("masterAppProducts").selectedOptionalFeaturesValues.value = fetchDropdown.filter(item => item === 'Dependent Spouse Rider' || item === 'Dependent Child Rider');
                                    fillOptionalFeatureTable(optionalFeaturesTextboxValue);
                                }
                            }

                            //Critical Illness
                            if(productsArray[i].productName == "CI20000" || productsArray[i].productName == "CI2100" || productsArray[i].productName == "CI2800" || productsArray[i].productName == "CI21000" || productsArray[i].productName == "CI22000"){
                                guideBridge.resolveNode("exceptionRadio").visible = true;
                                guideBridge.resolveNode("productSequence").value = 2;
                            }

                            //Dental
                            if(productsArray[i].productName == "D1100"){
                                guideBridge.resolveNode("Plan").visible = true;
                                guideBridge.resolveNode("exceptionRadio").visible = false;
                                guideBridge.resolveNode("productSequence").value = 4;
                            }

                            //Term Life
                            if(productsArray[i].productName == "TL9100"){
                                guideBridge.resolveNode("Plan").visible = true;
                                guideBridge.resolveNode("planPremiumPeriod").visible = true;
                                guideBridge.resolveNode("optionalFeaturesDropdown").visible = false;
                                guideBridge.resolveNode("AddOptionalFeatureButton").visible = false;
                                guideBridge.resolveNode("optionalFeaturesOther").visible = false;
                                guideBridge.resolveNode("optionalFeaturesTextbox").visible = true;

                                guideBridge.resolveNode("employeeType").visible = false
                                guideBridge.resolveNode("eligibleEmployeesClass_Emp").visible = false
                                guideBridge.resolveNode("exceptionRadio").visible = false;
                                guideBridge.resolveNode("eligibleEmployeesClass_Spouse").visible = false;
                                guideBridge.resolveNode("otherEligibleEmpClass").visible = false;
                                guideBridge.resolveNode("eligibleEmployeesClass_termLife").visible = true;
                                guideBridge.resolveNode("productSequence").value = 6;
                            }

                            //Disability
                            if(productsArray[i].productName == "DI5000" || productsArray[i].productName == "DI50000"){
                                guideBridge.resolveNode("productclass").visible = true;
                                //guideBridge.resolveNode("eligibleEmpClassChkbox").visible = true;
                                guideBridge.resolveNode("eliminationPeriod").visible = true;
                                guideBridge.resolveNode("benefitPeriod").visible = true;
                                guideBridge.resolveNode("incomeReplacementPercentage").visible = true;
                                guideBridge.resolveNode("incomeReplacementPercentage").value = "60%";
                                guideBridge.resolveNode("productSequence").value = 5;
                                guideBridge.resolveNode("eligibleEmployeesClass_Spouse").visible = false;
                                guideBridge.resolveNode("eligibleEmployeesClass_Spouse").value = "";
                            }

                            //Whole Life
                            if(productsArray[i].productName == "WL9800" || productsArray[i].productName == "WL60000"){
                                guideBridge.resolveNode("exceptionRadio").visible = true;
                                guideBridge.resolveNode("planFeatures").visible = true;
                                guideBridge.resolveNode("optionalFeaturesDropdown").visible = false;
                                guideBridge.resolveNode("AddOptionalFeatureButton").visible = false;
                                guideBridge.resolveNode("optionalFeaturesOther").visible = false;
                                guideBridge.resolveNode("optionalFeaturesTextbox").visible = true;
                                guideBridge.resolveNode("duePremiumRadio").visible = true;
                                guideBridge.resolveNode("productSequence").value = 7;
                            }

                            //Term to 120
                            if(productsArray[i].productName == "TL93000"){
                                guideBridge.resolveNode("benefitType").visible = false;
                                guideBridge.resolveNode("applicationReason").visible = false;
                                guideBridge.resolveNode("exceptionRadio").visible = true;
                                guideBridge.resolveNode("eligibleEmpClassChkbox").visible = false;
                                guideBridge.resolveNode("planFeatures").visible = false;
                                guideBridge.resolveNode("optionalFeaturesDropdown").visible = false;
                                guideBridge.resolveNode("AddOptionalFeatureButton").visible = false;
                                guideBridge.resolveNode("optionalFeaturesOther").visible = false;
                                guideBridge.resolveNode("optionalFeaturesTextbox").visible = false;
                                guideBridge.resolveNode("eliminationPeriod").visible = false;  
                                guideBridge.resolveNode("benefitPeriod").visible = false; 
                                guideBridge.resolveNode("incomeReplacementPercentage").visible = false;  
                                guideBridge.resolveNode("Plan").visible = false;  
                                guideBridge.resolveNode("EmployeePremiumPercentage").visible = false;  
                                guideBridge.resolveNode("EmployerPremiumPercentage").visible = false;  
                                guideBridge.resolveNode("otherEligibleEmpClass").visible = true;
                                guideBridge.resolveNode("groupPolicySitusState").visible = true;  
                                guideBridge.resolveNode("optionalTermLifeCoverages").visible = true; 
                                //guideBridge.resolveNode("proposedEffectiveDate").visible = true; 
                                //guideBridge.resolveNode("onDate").visible = true; 
                                guideBridge.resolveNode("minimumNoOfEmployeesEnrolled").visible = true; 
                                guideBridge.resolveNode("minimumNoOfEmployeesEnrolled").value = 25; 
                                guideBridge.resolveNode("minimumPerEligibleEmployees").visible = true; 
                                //guideBridge.resolveNode("premiumPaidWithApplication").visible = true; 
                                //guideBridge.resolveNode("premiumPaymentMode").visible = true; 
                                guideBridge.resolveNode("replacingWithCaic").visible = true; 
                                //guideBridge.resolveNode("employeeCostOfInsurance").visible = true;
                                //guideBridge.resolveNode("RatesGuaranteeLength").visible = true;
                                guideBridge.resolveNode("productSequence").value = 6; 
                                
                            }
                        }
                        setContactTitleName();
                    }
                }
            })
        .responseText);
    }
    fillMasterAppReviewTable();
    guideBridge.resolveNode("eSignCheckbox").value="Yes";
    setTimeout(function() {
        guideBridge.resolveNode("eSignCheckbox").enabled = false;
        },10);
    guideBridge.resolveNode("lastSaveTimestamp").visible=false;
    guideBridge.resolveNode("refreshTimestamp").visible = false;
}

function getLOB(series) {
    var LoB;
    for (var i = 0; i < LOBseries.length; i++) {
        if (LOBseries[i].Series.includes(series)) {
            LoB = LOBseries[i].LOB.split(',')[0];
        }
        if (series == "81000"){
            LoB = "Hospital Indemnity";
        }
    }
    return LoB;
}

function setEligibleEmpClass(employeeType, Series, ElgEmpSOM, ElgEmpSpouseSOM){
    var certHolders = guideBridge.resolveNode("certHolders").value;

    var masterAppSitus = guideBridge.resolveNode("masterAppSitus").value;
    var LOB = getLOB(Series);
    
    if(employeeType != null & certHolders != null && Series != null){

        $.ajax({
            url: "/bin/GetCaseBuilderToolProductsData",
            type: 'GET',
            data: { mode: "issueAge", situs: masterAppSitus, series: Series, product: LOB, term: "", lob: LOB },
            dataType: 'json', // added data type
            success: function (res) {
                guideBridge.resolveNode(ElgEmpSOM).value = "Regular " + employeeType + " " + certHolders +"s" + " " + res["emp-issue-age-master-app"];
                
                if(LOB!="Disability"){
                    guideBridge.resolveNode(ElgEmpSpouseSOM).value = "Spouses of Eligible " + certHolders +"s" + " " + res["spouse-issue-age-master-app"];
                }else{
                    guideBridge.resolveNode(ElgEmpSpouseSOM).value="";
                    guideBridge.resolveNode(ElgEmpSpouseSOM).visible=false;
                }
                
            }
        });
    }
}

var masterAppIssueAgeRes;
function onChangingCertHolders(){
    masterAppIssueAgeRes="";
    
    var certHolders = guideBridge.resolveNode("certHolders").value;
    var masterAppSitus = guideBridge.resolveNode("masterAppSitus").value;

    var repeatPanel = guideBridge.resolveNode("masterAppProducts");
    var productArray = guideBridge.resolveNode("masterAppApplicableSeries").value.split(",");
    for (var i = 0; i < productArray.length; i++) {
        var employeeType = repeatPanel.instanceManager.instances[i].employeeType.value;
        var Series = repeatPanel.instanceManager.instances[i].Series.value; 
        var LOB = getLOB(Series);

        if(employeeType != null & certHolders != null && Series != null){
            var res = JSON.parse(
                $.ajax({
                    url: "/bin/GetCaseBuilderToolProductsData",
                    type: 'GET',
                    async: false,
                    data: { mode: "issueAge", situs: masterAppSitus, series: Series, product: LOB, term: "", lob: LOB},
                    dataType: 'json', // added data type
                    success: function (res) {
                        masterAppIssueAgeRes=res;
                    }
                })
            .responseText);
            
            repeatPanel.instanceManager.instances[i]["eligibleEmployeesClass_Emp"].value =  "Regular " + employeeType + " " + certHolders +"s" + " " + masterAppIssueAgeRes["emp-issue-age-master-app"];
            if(LOB!="Disability"){
                repeatPanel.instanceManager.instances[i]["eligibleEmployeesClass_Spouse"].value =   "Spouses of Eligible " + certHolders +"s" + " " + masterAppIssueAgeRes["spouse-issue-age-master-app"];
            }else{
                repeatPanel.instanceManager.instances[i]["eligibleEmployeesClass_Spouse"].value ="";
                repeatPanel.instanceManager.instances[i]["eligibleEmployeesClass_Spouse"].visible=false;
            }
            
        }
        else{
            repeatPanel.instanceManager.instances[i]["eligibleEmployeesClass_Emp"].value =  "";
            repeatPanel.instanceManager.instances[i]["eligibleEmployeesClass_Spouse"].value =   "";
        }
    }
}

var optionalFeatures;
function getOptionalFeatures(){
    var res = JSON.parse(
        $.ajax({
            url: "/bin/MasterAppServlet",
            type: "GET",
            async: false,
            data: {
                "mode":"getOptionalFeatures"
            },
            success: function (data) {
                if (data != null) {
                    optionalFeatures = data;
                }
                else {
                    console.log("No data found for getOptionalFeatures")                        
                }
            }
        })
    .responseText);
}
function setOptionalFeaturesItems(){
    var Series = guideBridge.resolveNode("masterAppProducts").Series.value;
    var LoB = getLOB(Series);
    var optionalFeaturesItems = [];
    if(optionalFeatures!=null && optionalFeatures!=""){
        for (var i=0; i<optionalFeatures.length; i++){
            if(LoB == optionalFeatures[i]["product-name"]){
                optionalFeaturesItems = optionalFeatures[i]["product-optional-features"]
                fetchDropdown = optionalFeaturesItems
            }
        }
        guideBridge.resolveNode("optionalFeaturesDropdown").items = optionalFeaturesItems;
    }
}

// function optionalFeaturesOtherVisibility(){
//     var optionalFeatures = guideBridge.resolveNode("masterAppProducts").optionalFeaturesCheckbox.value;
//     var otherValue = guideBridge.resolveNode("masterAppProducts").optionalFeaturesOther.value;
//     if(optionalFeatures!=null){
//         var optionalFeaturesArray = optionalFeatures.split(',');
//         for (var i=0; i<optionalFeaturesArray.length; i++){
//             if (optionalFeaturesArray[i] == "Other"){
//                 guideBridge.resolveNode("masterAppProducts").optionalFeaturesOther.visible = true;
//                 guideBridge.resolveNode("masterAppProducts").optionalFeaturesOther.value = otherValue;
//                 break;
//             }
//             else{
//                 guideBridge.resolveNode("masterAppProducts").optionalFeaturesOther.visible = false;
//                 guideBridge.resolveNode("masterAppProducts").optionalFeaturesOther.value = "";
//             }
//         }
//     }
//     else{
//         guideBridge.resolveNode("masterAppProducts").optionalFeaturesOther.visible = false;
//         guideBridge.resolveNode("masterAppProducts").optionalFeaturesOther.value = "";
//     }
// }

// function planYearMaxVisibility(){
//     var optionalFeatures = guideBridge.resolveNode("masterAppProducts").optionalFeaturesCheckbox.value;
//     if(optionalFeatures!=null){
//         if (optionalFeatures.includes("Plan Year Maximum, Per Insured")){
//             guideBridge.resolveNode("masterAppProducts").PlanYearMax.visible = true;
//         }
//         else{
//             guideBridge.resolveNode("masterAppProducts").PlanYearMax.visible = false;
//             guideBridge.resolveNode("masterAppProducts").PlanYearMax.value = "";
//         }
//     }
//     else{
//         guideBridge.resolveNode("masterAppProducts").PlanYearMax.visible = false;
//         guideBridge.resolveNode("masterAppProducts").PlanYearMax.value = "";
//     }
// }

function addMonthSuffix(fieldname) {
    var field = guideBridge.resolveNode(fieldname);
    var monthsFieldValue = guideBridge.resolveNode(fieldname).value;
    if (monthsFieldValue.endsWith('-months') || monthsFieldValue.endsWith('-Months') || monthsFieldValue.endsWith('-month') || monthsFieldValue.endsWith('-Month')) {
        var NewValue = monthsFieldValue.split('-months');
        monthsFieldValue = NewValue[0];
    }
    if (monthsFieldValue < 0 || isNaN(monthsFieldValue) == true || monthsFieldValue.includes('+')) {
        guideBridge.resolveNode(fieldname).value = "";
        document.getElementById(field.id).className = "guideFieldNode guideTextBox " + fieldname + "defaultFieldLayout af-field-filled validation-failure";
        setTimeout(function() {
            var alert = document.getElementById(field.id).children[2];
            alert.setAttribute('role', 'alert');
            var alertid = "#" + alert.id;
            $(alertid).html("Not a valid value.");
        }, 10);
    } else {
        document.getElementById(field.id).className = "guideFieldNode guideTextBox " + fieldname + "defaultFieldLayout af-field-filled validation-success ";
        guideBridge.resolveNode(fieldname).value = "";
        if(monthsFieldValue > 1){
            var value = monthsFieldValue + "-months";
        }
        else {
            var value = monthsFieldValue + "-month";
        }
        guideBridge.resolveNode(fieldname).value = value;
    }
}

function planFeaturesOtherVisibility(){
    var planFeatures = guideBridge.resolveNode("masterAppProducts").planFeatures.value;

    if(planFeatures!=null){
        if (planFeatures.includes("Other")){
            guideBridge.resolveNode("masterAppProducts").otherPlanFeatures.visible = true;
        }
        else{
            guideBridge.resolveNode("masterAppProducts").otherPlanFeatures.visible = false;
            guideBridge.resolveNode("masterAppProducts").otherPlanFeatures.value = "";
        }
    }
    else{
        guideBridge.resolveNode("masterAppProducts").otherPlanFeatures.visible = false;
        guideBridge.resolveNode("masterAppProducts").otherPlanFeatures.value = "";
    }
}

function setPhysicalAddress(){
    var isPhysicalAddressSame = guideBridge.resolveNode("PhysicalAddressRadio").value;

    if(isPhysicalAddressSame == "Yes"){
        guideBridge.resolveNode("PhysicalAddress").value = guideBridge.resolveNode("Address").value;
        guideBridge.resolveNode("PhysicalNumberStreet").value = guideBridge.resolveNode("NumberStreet").value;
        guideBridge.resolveNode("PhysicalCity").value = guideBridge.resolveNode("City").value;
        guideBridge.resolveNode("PhysicalState").value = guideBridge.resolveNode("State").value;
        guideBridge.resolveNode("PhysicalZip").value = guideBridge.resolveNode("Zip").value;
        
        guideBridge.resolveNode("PhysicalAddress").enabled = false;
        guideBridge.resolveNode("PhysicalNumberStreet").enabled = false;
        guideBridge.resolveNode("PhysicalCity").enabled = false;
        guideBridge.resolveNode("PhysicalState").enabled = false;
        guideBridge.resolveNode("PhysicalZip").enabled = false;
    }
    else{
        guideBridge.resolveNode("PhysicalAddress").value = "";
        guideBridge.resolveNode("PhysicalNumberStreet").value = "";
        guideBridge.resolveNode("PhysicalCity").value = "";
        guideBridge.resolveNode("PhysicalState").value = "";
        guideBridge.resolveNode("PhysicalZip").value = "";
        
        guideBridge.resolveNode("PhysicalAddress").enabled = true;
        guideBridge.resolveNode("PhysicalNumberStreet").enabled = true;
        guideBridge.resolveNode("PhysicalCity").enabled = true;
        guideBridge.resolveNode("PhysicalState").enabled = true;
        guideBridge.resolveNode("PhysicalZip").enabled = true;
    }
}

function clearWaitingPeriodDays(waitingDaysSOM){
    guideBridge.resolveNode(waitingDaysSOM).value = '';
    for(var i=0; i<document.querySelectorAll(".waitingPeriodDays").length; i++){
        if(document.querySelectorAll(".waitingPeriodDays")[i].id == guideBridge.resolveNode(waitingDaysSOM).id){
            document.querySelectorAll(".waitingPeriodDays select")[i].value = '';
        }
    }
}

function saveMasterApp(){

    //validation
    var errors = [];
    guideBridge.validate(errors, guideBridge.resolveNode("accountEligibilitySection").somExpression);
    var repeatPanel = guideBridge.resolveNode("masterAppProducts");    
    var currentCount = repeatPanel.instanceManager.instanceCount;
    var startDateTime = guideBridge.resolveNode("startTime").value;
    for (var i = 0; i < currentCount; i++) {
        guideBridge.validate(errors, repeatPanel._instanceManager._instances[i].somExpression);
    }

    if (errors.length === 0) {
        var isOptionalFeatureEmpty = false;
        for (var i = 0; i < currentCount; i++) {
            var field = repeatPanel.instanceManager.instances[i].optionalFeaturesDropdown;
            var  opFeatureDropdownVisibility = repeatPanel.instanceManager.instances[i].optionalFeaturesDropdown.visible;
            if(repeatPanel.instanceManager.instances[i].selectedOptionalFeaturesValues.value == null && opFeatureDropdownVisibility == true) {
                isOptionalFeatureEmpty = true;
                guideBridge.setFocus(field.somExpression);
                document.getElementById(field.id).className = "guideFieldNode guideDropDownList optionalFeaturesDropdown defaultFieldLayout af-field-filled validation-failure";
                setTimeout(function() {
                    var alert = document.getElementById(field.id).children[2];
                    alert.setAttribute('role', 'alert');
                    var alertid = "#" + alert.id;
                    $(alertid).html("Add atleast one Optional Feature");
                },10);
                break;
            }
            else{
                isOptionalFeatureEmpty = false;
            }
        }

        if(isOptionalFeatureEmpty == false){
            guideBridge.getDataXML({
                success: function(guideResultObject) {
                    var req = new XMLHttpRequest();
                    req.open("POST", "/bin/MasterAppServlet", true);
                    var postParameters = new FormData();
                    postParameters.append("formData", guideResultObject.data);
                    postParameters.append("mode", "save");
                    postParameters.append("startDateTime", startDateTime);
                    req.send(postParameters);
                    req.onreadystatechange = function() {
                        if (req.readyState == 4 && req.status == 200) {
                            var x = JSON.parse(req.responseText);
                            if(x["status"] == true) {

                                var masterAppEntityIdValue = guideBridge.resolveNode("masterAppGroupNo").value + "#" + guideBridge.resolveNode("coverageEffDateMasterApp").value;
                                guideBridge.resolveNode("MasterAppEntityId").value = masterAppEntityIdValue;

                                guideBridge.resolveNode("saveMasterAppSuccessMessage").visible = true;
                                guideBridge.resolveNode("saveMasterAppFailureMessage").visible = false;
                                guideBridge.resolveNode("lastSaveTimestamp").visible=false;
                                hideSaveMasterAppMessage();
                                hideProductSelectionPanelSave();
                                //guideBridge.resolveNode("previewMasterAppPDF").enabled = true;

                                guideBridge.resolveNode("Version").value = x["version"];

                                var repeatrow = guideBridge.resolveNode("MasterAppReviewTableRow");
                                for (var i = 0; i < repeatrow.instanceManager.instanceCount; i++) {
                                    repeatrow._instanceManager._instances[i].previewMasterAppPDF.enabled = true;
                                    repeatrow._instanceManager._instances[i].ReviewTableSignCheckbox.enabled = true;
                                }

                                guideBridge.resolveNode("eSignCheckbox").enabled = true;
                            }
                            else{
                                guideBridge.resolveNode("saveMasterAppSuccessMessage").visible = false;
                                guideBridge.resolveNode("saveMasterAppFailureMessage").visible = false;
                                hideSaveMasterAppMessage();
                            }
                        }
                        else if (req.readyState == 4 && req.status == 400) {
                            guideBridge.resolveNode("saveMasterAppSuccessMessage").visible = false;
                            guideBridge.resolveNode("saveMasterAppFailureMessage").visible = false;
                            hideSaveMasterAppMessage();
                        }
                    }
                }
            });
        }
    } 
    else {
        var errorField = errors[0].getFocus();
        guideBridge.setFocus(errorField);
    }   
}

function hideSaveMasterAppMessage(){
    $(document).on('click', 'button', function () {
        guideBridge.resolveNode("saveMasterAppSuccessMessage").visible = false;
        guideBridge.resolveNode("saveMasterAppFailureMessage").visible = false;
    });

    $(document).on('change', function() {
        guideBridge.resolveNode("saveMasterAppSuccessMessage").visible = false;
        guideBridge.resolveNode("saveMasterAppFailureMessage").visible = false;
    });
}

function showCensusEnrollment(){
    var situs = guideBridge.resolveNode("masterAppSitus").value;
    var formCase = guideBridge.resolveNode("masterAppFormCase").value;
    if(situs == "CA"){
        guideBridge.resolveNode("masterAppCensus").visible = true;
        if(formCase == "Add"){
            guideBridge.resolveNode("masterAppCensus").value="N/A";
        }
        else{
            guideBridge.resolveNode("masterAppCensus").value = masterAppResponse.censusEnrollment;
        }
    }else{
        guideBridge.resolveNode("masterAppCensus").visible = false;
        guideBridge.resolveNode("masterAppCensus").value="";
    }
}

function fillMasterAppReviewTable(){
    var selectedSeries = guideBridge.resolveNode("masterAppApplicableSeries").value;
    var selectedSeriesArray = selectedSeries.split(',');
    var MasterAppReviewData = [];

    for (var i=0; i<masterAppSeriesData.length; i++) {
        var productSeriesArray = [];
        var newObj = {
            "form-id": "",
            "apiFormId": "",
            "product-series": [],
            }
        for (var j=0; j<selectedSeriesArray.length; j++) {
            if(masterAppSeriesData[i]["product-series"].includes(selectedSeriesArray[j])){
                productSeriesArray.push(selectedSeriesArray[j]);
            }
        }
        newObj["form-id"] = masterAppSeriesData[i]["form-id"];
        newObj["apiFormId"] = masterAppSeriesData[i]["apiFormId"];
        newObj["product-series"] = productSeriesArray;

        if(productSeriesArray.length > 0){
            MasterAppReviewData.push(newObj);
        }
    }

    var repeatrow = guideBridge.resolveNode("MasterAppReviewTableRow");
    var currentCount = repeatrow.instanceManager.instanceCount;
    for (var m = 0; m < currentCount; m++) {
        repeatrow.resetData();
        if (m != 0) {
            repeatrow.instanceManager.removeInstance(0);
        }
    }
    for (var i = 0; i < MasterAppReviewData.length; i++) {
        if (i != 0) {
            repeatrow.instanceManager.addInstance();
        }
    }
    for (var i = 0; i < repeatrow.instanceManager.instanceCount; i++) {
        repeatrow._instanceManager._instances[i].ReviewTableFormID.value = MasterAppReviewData[i]["form-id"];
        repeatrow._instanceManager._instances[i].apiFormId.value = MasterAppReviewData[i]["apiFormId"];
        repeatrow._instanceManager._instances[i].ReviewTableProducts.value = addProductPrefix(MasterAppReviewData[i]["product-series"]).join(', ');
        //repeatrow._instanceManager._instances[i].ReviewTableStatus.value = "Ready To Preview";
        repeatrow._instanceManager._instances[i].ReviewTableSignStatus.value = "Doc Not Generated";
        repeatrow._instanceManager._instances[i].ReviewTableSignCheckbox.value ="-100";
        repeatrow._instanceManager._instances[i].ReviewTableSignCheckbox.enabled = false;
        repeatrow._instanceManager._instances[i].previewMasterAppPDF.enabled = false;
        repeatrow._instanceManager._instances[i].agreementID.value = "";
        repeatrow._instanceManager._instances[i].docID.value = "";
    }
}

function fillMasterAppReviewTableEdit(masterAppReviewTableData){

    var repeatrow = guideBridge.resolveNode("MasterAppReviewTableRow");
    var currentCount = repeatrow.instanceManager.instanceCount;
    for (var m = 0; m < currentCount; m++) {
        repeatrow.resetData();
        if (m != 0) {
            repeatrow.instanceManager.removeInstance(0);
        }
    }
    for (var i = 0; i < masterAppReviewTableData.length; i++) {
        if (i != 0) {
            repeatrow.instanceManager.addInstance();
        }
    }

    for (var i = 0; i < repeatrow.instanceManager.instanceCount; i++) {
        repeatrow._instanceManager._instances[i].ReviewTableFormID.value = masterAppReviewTableData[i].formId;
        repeatrow._instanceManager._instances[i].apiFormId.value =  masterAppReviewTableData[i].apiFormId;
        repeatrow._instanceManager._instances[i].ReviewTableProducts.value = masterAppReviewTableData[i].product;
        repeatrow._instanceManager._instances[i].ReviewTableSignCheckbox.value = masterAppReviewTableData[i].signatureCheckbox;
        repeatrow._instanceManager._instances[i].ReviewTableSignStatus.value = masterAppReviewTableData[i].signStatus;
        repeatrow._instanceManager._instances[i].ReviewTableSignCheckbox.enabled = true;
        repeatrow._instanceManager._instances[i].previewMasterAppPDF.enabled = true;
        repeatrow._instanceManager._instances[i].agreementID.value = masterAppReviewTableData[i].agreementId;
        repeatrow._instanceManager._instances[i].docID.value = masterAppReviewTableData[i].filenetDocumentId;
    }
    reviewTableAdobeSignCheckbox();
}

function fillMasterAppReviewTableUpdate(masterAppAPIdata){

    var selectedSeries = guideBridge.resolveNode("masterAppApplicableSeries").value;
    var selectedSeriesArray = selectedSeries.split(',');
    var MasterAppReviewData = [];

    for (var i=0; i<masterAppSeriesData.length; i++) {
        var productSeriesArray = [];
        var newObj = {
            "form-id": "",
            "apiFormId": "",
            "product-series": [],
            }
        for (var j=0; j<selectedSeriesArray.length; j++) {
            if(masterAppSeriesData[i]["product-series"].includes(selectedSeriesArray[j])){
                productSeriesArray.push(selectedSeriesArray[j]);
            }
        }
        newObj["form-id"] = masterAppSeriesData[i]["form-id"];
        newObj["apiFormId"] = masterAppSeriesData[i]["apiFormId"];
        newObj["product-series"] = productSeriesArray;

        if(productSeriesArray.length > 0){
            MasterAppReviewData.push(newObj);
        }
    }

    var repeatrow = guideBridge.resolveNode("MasterAppReviewTableRow");
    var currentCount = repeatrow.instanceManager.instanceCount;
    for (var m = 0; m < currentCount; m++) {
        repeatrow.resetData();
        if (m != 0) {
            repeatrow.instanceManager.removeInstance(0);
        }
    }
    for (var i = 0; i < MasterAppReviewData.length; i++) {
        if (i != 0) {
            repeatrow.instanceManager.addInstance();
        }
    }

    for (var i = 0; i < repeatrow.instanceManager.instanceCount; i++) {
        repeatrow._instanceManager._instances[i].ReviewTableFormID.value = MasterAppReviewData[i]["form-id"];
        repeatrow._instanceManager._instances[i].apiFormId.value = MasterAppReviewData[i]["apiFormId"];
        repeatrow._instanceManager._instances[i].ReviewTableProducts.value = addProductPrefix(MasterAppReviewData[i]["product-series"]).join(', ');

        var existingDoc = false;

        for (var j = 0; j < masterAppAPIdata.length; j++) {
            if(MasterAppReviewData[i]["form-id"] == masterAppAPIdata[j].formId){
                console.log("same formID");
                var products = repeatrow._instanceManager._instances[i].ReviewTableProducts.value;
                var productsArray = products.split(',');
                var apiProductsArray = masterAppAPIdata[i].product.split(',');

                if(productsArray.length != apiProductsArray.length){
                    console.log("different products");
                    existingDoc = false;
                } else {
                    for(var k=0; k<apiProductsArray.length; k++){
                        if(!productsArray.includes(apiProductsArray[k])){
                            console.log("different products");
                            existingDoc = false;
                            break;
                        }
                        else{
                            existingDoc = true;
                        }
                    }
                }       
            }
        }

        if(existingDoc == false){
            repeatrow._instanceManager._instances[i].ReviewTableSignStatus.value = "Doc Not Generated";
            repeatrow._instanceManager._instances[i].ReviewTableSignCheckbox.value ="-100";
            repeatrow._instanceManager._instances[i].ReviewTableSignCheckbox.enabled = false;
            repeatrow._instanceManager._instances[i].previewMasterAppPDF.enabled = false;
        }
        else{
            repeatrow._instanceManager._instances[i].ReviewTableSignCheckbox.value = masterAppAPIdata[i].signatureCheckbox;
            repeatrow._instanceManager._instances[i].ReviewTableSignStatus.value = masterAppAPIdata[i].signStatus;
            repeatrow._instanceManager._instances[i].ReviewTableSignCheckbox.enabled = true;
            repeatrow._instanceManager._instances[i].previewMasterAppPDF.enabled = true;
            repeatrow._instanceManager._instances[i].agreementID.value = MasterAppReviewData[i].agreementId;
            repeatrow._instanceManager._instances[i].docID.value = MasterAppReviewData[i].filenetDocumentId;
        }
    }
    reviewTableAdobeSignCheckbox();
}

function hideExtraCheckboxMasterApp(){
    document.querySelectorAll('.eSignCheckbox')[2].style.display = 'none';
}

function addProductPrefix(seriesArray){
    var reviewTableProductArray = [];
    for (var i = 0; i < seriesArray.length; i++) {
        for (var j = 0; j < masterAppLOBdata.length; j++) {
            if(masterAppLOBdata[j].Series.includes(seriesArray[i])){
                reviewTableProductArray.push(masterAppLOBdata[j].ProductPrefix + seriesArray[i]);
                break;
            }
        }
    }
    return reviewTableProductArray;
}

function setContactTitleName(){
    var contactTitleValue="";
    var groupName = guideBridge.resolveNode("organizationName").value;
    var formcase = guideBridge.resolveNode("masterAppFormCase").value;
    var productsArray =[];
    if(formcase=="Edit" || formcase=="Update"){
        productsArray= masterAppProductsUpdateArray;
		productsArray.filter(function(products){
            if((optionalFeaturesTextboxValue == null || optionalFeaturesTextboxValue == "") && (products.productName === 'HI80000')){
			    optionalFeaturesTextboxValue = guideBridge.resolveNode("masterAppProducts").selectedOptionalFeaturesValues.value = fetchDropdown.filter(item => item === 'Dependent Spouse Rider' || item === 'Dependent Child Rider');
                fillOptionalFeatureTable(optionalFeaturesTextboxValue);
            }
        });
    }else if(formcase=="Add"){
        productsArray = masterAppProductsArray;
    }
    if(groupName!="" && groupName!=null){
        if(productsArray.length>1){
            contactTitleValue = groupName+" Master Application";
        }else{
            contactTitleValue = groupName+" Master Application "+productsArray[0].productName;
        }
        guideBridge.resolveNode("masterAppContractTitle").value = contactTitleValue;
    }
}

function setFirstScreenDataEditCase(){
    guideBridge.resolveNode("masterAppSitus").value = masterAppResponse.situsState;
    guideBridge.resolveNode("masterAppGroupType").value = masterAppResponse.groupType;        
}

function setMAProductCheckBoxValues(){
    var eligibleSeries = masterAppResponse.eligibleSeries.split(',');

    var accidentArray = [];
    var hospitalIndemnityArray = [];
    var criticalIllnessArray = [];
    var disabilityArray = [];
    var wholeLifeArray = [];
    var termLifeArray = [];
    var dentalArray = [];

    for (var i=0; i<eligibleSeries.length; i++){
        for (var j=0; j<masterAppLOBdata.length; j++){
            if(masterAppLOBdata[j].Series.includes(eligibleSeries[i])){
                if(masterAppLOBdata[j].ProductPrefix == "AC"){
                    accidentArray.push(eligibleSeries[i]);
                }
                if(masterAppLOBdata[j].ProductPrefix == "HI"){
                    hospitalIndemnityArray.push(eligibleSeries[i]);                                        
                }
                if(masterAppLOBdata[j].ProductPrefix == "CI"){
                    criticalIllnessArray.push(eligibleSeries[i]);                                    
                }
                if(masterAppLOBdata[j].ProductPrefix == "DI"){
                    disabilityArray.push(eligibleSeries[i]);                                        
                }
                if(masterAppLOBdata[j].ProductPrefix == "WL"){
                    wholeLifeArray.push(eligibleSeries[i]);                                        
                }
                if(masterAppLOBdata[j].ProductPrefix == "TL"){
                    termLifeArray.push(eligibleSeries[i]);                                                                                    
                }
                if(masterAppLOBdata[j].ProductPrefix == "D"){
                    dentalArray.push(eligibleSeries[i]);                                        
                }
            }
        }
    }

    guideBridge.resolveNode("accidentCheckbox").value = accidentArray;
    guideBridge.resolveNode("hospitalIndemnityCheckbox").value = hospitalIndemnityArray;
    guideBridge.resolveNode("criticalIllnessCheckbox").value = criticalIllnessArray;
    guideBridge.resolveNode("disabilityCheckbox").value = disabilityArray;
    guideBridge.resolveNode("wholeLifeCheckbox").value = wholeLifeArray;
    guideBridge.resolveNode("termLifeCheckbox").value = termLifeArray;
    guideBridge.resolveNode("dentalCheckbox").value = dentalArray;
}

function setMasterAppDeletedProducts(){
    var selectedProducts = guideBridge.resolveNode("masterAppApplicableSeries").value;
    var selectedProductsArray = addProductPrefix(selectedProducts.split(','));
    var productsSoldArray = addProductPrefix(masterAppResponse.eligibleSeries.split(','));
    var deletedProductsArray = [];

    for(var i=0; i<productsSoldArray.length; i++){
        if((!selectedProductsArray.includes(productsSoldArray[i]))){
            deletedProductsArray.push(productsSoldArray[i]);
        }
    }

    guideBridge.resolveNode("masterAppDeletedProducts").value = deletedProductsArray.join(',');
}

function setMasterAppAddedProducts(){
    var selectedProducts = guideBridge.resolveNode("masterAppApplicableSeries").value;
    if(selectedProducts != null){
        var selectedProductsArray = addProductPrefix(selectedProducts.split(','));
        guideBridge.resolveNode("masterAppAddedProducts").value = selectedProductsArray.join(',');
    }
    else{
        guideBridge.resolveNode("masterAppAddedProducts").value = null;
    }
}

function setMAFormCaseUpdate(){
    var selectedProductsEditCase = guideBridge.resolveNode("masterAppAddedProducts").value;

    var selectedProductsArray = selectedProductsEditCase.split(',');
    var productsSoldArray = addProductPrefix(masterAppResponse.eligibleSeries.split(','));

    if(selectedProductsArray.length != productsSoldArray.length){
        guideBridge.resolveNode("masterAppFormCase").value = "Update";
    } else {
        for(var i=0; i<productsSoldArray.length; i++){
            if(!selectedProductsArray.includes(productsSoldArray[i])){
                guideBridge.resolveNode("masterAppFormCase").value = "Update";
                break;
            }
            else{
                guideBridge.resolveNode("masterAppFormCase").value = "Edit";
            }
        }
    }
}

var masterAppProductsUpdateArray =[];
var signatureStatus ="";
function updateMasterAppProducts(){
    var addedProducts = guideBridge.resolveNode("masterAppAddedProducts").value;
    var effectiveDate = guideBridge.resolveNode("coverageEffDateMasterApp").value;
    var groupNumber = guideBridge.resolveNode("masterAppGroupNo").value;
    var deletedProducts = guideBridge.resolveNode("masterAppDeletedProducts").value;

    masterAppProductsUpdateArray =[];

    var applicableLOBs = guideBridge.resolveNode("masterAppApplicableSeries").value;

    if (applicableLOBs.trim().endsWith(',')) {
        applicableLOBs = applicableLOBs.trim();
        applicableLOBs = applicableLOBs.slice(0, applicableLOBs.length - 1);
    }

    if(applicableLOBs.includes("93000") && applicableLOBs.split(",").length == 1){          
        guideBridge.resolveNode("accountSection").visible = false;
        guideBridge.resolveNode("accountSection").resetData();
        guideBridge.resolveNode("eligibleEmployees").visible=false;
        guideBridge.resolveNode("eligibleEmployees").value="";
        guideBridge.resolveNode("subsidiariesOrAffiliates").visible=false;
        guideBridge.resolveNode("subsidiariesOrAffiliates").value="";
        // document.querySelectorAll(".certHolders select")[0].value = "";
        
        //guideBridge.resolveNode("ApplicationRequestPurpose").visible = true;
        //guideBridge.resolveNode("policyNumbers").visible = true;
    }
    else if(applicableLOBs.includes("93000") && applicableLOBs.split(",").length > 1){
        guideBridge.resolveNode("accountSection").visible = true;
        guideBridge.resolveNode("eligibleEmployees").visible=true;
        guideBridge.resolveNode("subsidiariesOrAffiliates").visible=true;
    }
    else{
        guideBridge.resolveNode("accountSection").visible = true;
        guideBridge.resolveNode("eligibleEmployees").visible=true;
        guideBridge.resolveNode("subsidiariesOrAffiliates").visible=true;

        //guideBridge.resolveNode("ApplicationRequestPurpose").value= "";
        //guideBridge.resolveNode("classes_AddedRemoved").value= "";
        //guideBridge.resolveNode("policyNumbers").value= "";

        //guideBridge.resolveNode("ApplicationRequestPurpose").visible = false;
        //guideBridge.resolveNode("classes_AddedRemoved").visible = false;
        //guideBridge.resolveNode("policyNumbers").visible = false;
    }

    if(guideBridge.resolveNode("masterAppFormCase").value == "Update" || guideBridge.resolveNode("masterAppFormCase").value == "Edit"){
        var res = JSON.parse(
            $.ajax({
                url: "/bin/MasterAppServlet",
                type: "GET",
                async: false,
                data: {
                    "effectiveDate": effectiveDate,
                    "groupNumber": groupNumber,
                    "addedProducts":addedProducts,
                    "deletedProducts":deletedProducts,
                    "mode":"update"
                },
                success: function (data) {
                    if (data != null) {
                        guideBridge.resolveNode("Version").value = data.version;
                        guideBridge.resolveNode("MasterAppEntityId").value = data.masterAppEntityId;

                        if(guideBridge.resolveNode("masterAppFormCase").value == "Edit"){
                            signatureStatus = data.signatureStatus;
                        }
                        setAccountInfoData(data);

                        var productsArray = data.product;
                        masterAppProductsUpdateArray = productsArray;

                        var repeatPanel = guideBridge.resolveNode("masterAppProducts");

                        //Reset Panels
                        repeatPanel.instanceManager.addInstance();
                        var currentCount = repeatPanel.instanceManager.instanceCount;
                        for (var m = 0; m < currentCount; m++) {
                            if (m != 0) {
                                repeatPanel.instanceManager.removeInstance(0);
                            }
                        }

                        for (var i = 0; i < productsArray.length; i++) {
                            if (i != 0) {
                                repeatPanel.instanceManager.addInstance();
                            }

                            // setting product panel data
                            repeatPanel.instanceManager.instances[i].title = productsArray[i].productName;
                            repeatPanel.instanceManager.instances[i].summary = productsArray[i].productName;

                            guideBridge.resolveNode("productName").value = productsArray[i].productName;
                            guideBridge.resolveNode("Series").value = productsArray[i].series;

                            setOptionalFeaturesItems();

                            guideBridge.resolveNode("applicationReason").value = productsArray[i].applicationReason;
                            if(productsArray[i].applicationReason == "Change to Existing Policy Number"){
                                guideBridge.resolveNode("existingPolicyNumber").value = productsArray[i].existingPolicyNumber;
                            }

                            if(productsArray[i].effectiveDate != null){
                                guideBridge.resolveNode("productLevel_CED").value = productsArray[i].effectiveDate;
                            }
                            else{
                                guideBridge.resolveNode("productLevel_CED").value = guideBridge.resolveNode("coverageEffDateMasterApp").value;
                            }
                            guideBridge.resolveNode("RatesGuaranteeLength").value = productsArray[i].rates;
                            // guideBridge.resolveNode("ProductContribution").value = productsArray[i].productContribution;
                            if(productsArray[i].productContribution != null){
                                guideBridge.resolveNode("ProductContribution").value = productsArray[i].productContribution;
                                guideBridge.resolveNode("EmployeePremiumPercentage").value = productsArray[i].employeePremium;
                                guideBridge.resolveNode("EmployerPremiumPercentage").value = productsArray[i].employerPremium;
                            }
                            else{
                                guideBridge.resolveNode("ProductContribution").value = "100% Voluntary";
                                guideBridge.resolveNode("EmployeePremiumPercentage").value = 100;
                                guideBridge.resolveNode("EmployerPremiumPercentage").value = 0;
                            }

                            if(productsArray[i].productContribution =="Other"){
                                guideBridge.resolveNode("EmployeePremiumPercentage").visible = true;
                                guideBridge.resolveNode("EmployerPremiumPercentage").visible = true;
                            }  

                            //Accident
                            if(masterAppAccident.includes(productsArray[i].productName)){
                                guideBridge.resolveNode("benefitType").visible = true;
                                guideBridge.resolveNode("employeeType").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Emp").visible = true;
                                guideBridge.resolveNode("exceptionRadio").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Spouse").visible = true;
                                guideBridge.resolveNode("otherEligibleEmpClass").visible = true;
                                guideBridge.resolveNode("optionalFeaturesDropdown").visible = true;
                                guideBridge.resolveNode("AddOptionalFeatureButton").visible = true;
                                guideBridge.resolveNode("optionalFeaturesOther").visible = true;
                                guideBridge.resolveNode("Plan").visible = true;
                                guideBridge.resolveNode("productSequence").value = 1;
                                
                                if(productsArray[i].benefitType != null){
                                    guideBridge.resolveNode("benefitType").value = productsArray[i].benefitType;
                                }
                                if(productsArray[i].eligibleEmployee != null){
                                    guideBridge.resolveNode("eligibleEmployeesClass_Emp").value = productsArray[i].eligibleEmployee;
                                    guideBridge.resolveNode("eligibleEmployeesClass_Spouse").value = productsArray[i].eligibleSpouse;

                                    if(productsArray[i].eligibleEmployee.includes("full-time")){
                                        guideBridge.resolveNode("employeeType").value = "full-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("part-time")){
                                        guideBridge.resolveNode("employeeType").value = "part-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("full and part-time")){
                                        guideBridge.resolveNode("employeeType").value = "full and part-time";
                                    }
                                }
                                if(productsArray[i].eligibleEmployeeExcept != null){
                                    guideBridge.resolveNode("exceptionRadio").value = "Yes"
                                    guideBridge.resolveNode("eligibleClassException").value = productsArray[i].eligibleEmployeeExcept;
                                }
                                else{
                                    guideBridge.resolveNode("exceptionRadio").value = "No";
                                }
                                if(productsArray[i].eligibleEmployeeOther != null){
                                    guideBridge.resolveNode("otherEligibleEmpClass").value = productsArray[i].eligibleEmployeeOther;
                                }
                                if(productsArray[i].optionalFeatures != null){
                                    guideBridge.resolveNode("selectedOptionalFeaturesValues").value = productsArray[i].optionalFeatures;
                                }
                                if(productsArray[i].optionalFeatures.length != 0){
                                    fillOptionalFeatureTable(productsArray[i].optionalFeatures);
                                }
                                
                                if(productsArray[i].optionalFeaturesOther != null){
                                    guideBridge.resolveNode("optionalFeaturesOther").value = productsArray[i].optionalFeaturesOther;
                                }
                                if(productsArray[i].plan != null){
                                    guideBridge.resolveNode("Plan").value = productsArray[i].plan;
                                }
                            }

                            //Critical Illness
                            if(masterCriticalIllness.includes(productsArray[i].productName)){
                                guideBridge.resolveNode("employeeType").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Emp").visible = true;
                                guideBridge.resolveNode("exceptionRadio").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Spouse").visible = true;
                                guideBridge.resolveNode("otherEligibleEmpClass").visible = true;
                                guideBridge.resolveNode("optionalFeaturesDropdown").visible = true;
                                guideBridge.resolveNode("AddOptionalFeatureButton").visible = true;
                                guideBridge.resolveNode("optionalFeaturesOther").visible = true;
                                guideBridge.resolveNode("productSequence").value = 2;
                                
                                if(productsArray[i].eligibleEmployee != null){
                                    guideBridge.resolveNode("eligibleEmployeesClass_Emp").value = productsArray[i].eligibleEmployee;
                                    guideBridge.resolveNode("eligibleEmployeesClass_Spouse").value = productsArray[i].eligibleSpouse;

                                    if(productsArray[i].eligibleEmployee.includes("full-time")){
                                        guideBridge.resolveNode("employeeType").value = "full-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("part-time")){
                                        guideBridge.resolveNode("employeeType").value = "part-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("full and part-time")){
                                        guideBridge.resolveNode("employeeType").value = "full and part-time";
                                    }
                                }
                                if(productsArray[i].eligibleEmployeeExcept != null){
                                    guideBridge.resolveNode("exceptionRadio").value = "Yes"
                                    guideBridge.resolveNode("eligibleClassException").value = productsArray[i].eligibleEmployeeExcept;
                                }
                                else{
                                    guideBridge.resolveNode("exceptionRadio").value = "No";
                                }
                                if(productsArray[i].eligibleEmployeeOther != null){
                                    guideBridge.resolveNode("otherEligibleEmpClass").value = productsArray[i].eligibleEmployeeOther;
                                }
                                if(productsArray[i].optionalFeatures != null){
                                    guideBridge.resolveNode("selectedOptionalFeaturesValues").value = productsArray[i].optionalFeatures;
                                }
                                if(productsArray[i].optionalFeatures.length != 0){
                                    fillOptionalFeatureTable(productsArray[i].optionalFeatures);
                                }
                                
                                if(productsArray[i].optionalFeaturesOther != null){
                                    guideBridge.resolveNode("optionalFeaturesOther").value = productsArray[i].optionalFeaturesOther;
                                }
                            }

                            //Hospital Indemnity
                            if(masterHospitalIndemnity.includes(productsArray[i].productName)){
                                guideBridge.resolveNode("employeeType").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Emp").visible = true;
                                guideBridge.resolveNode("exceptionRadio").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Spouse").visible = true;
                                guideBridge.resolveNode("otherEligibleEmpClass").visible = true;
                                guideBridge.resolveNode("optionalFeaturesDropdown").visible = true;
                                guideBridge.resolveNode("AddOptionalFeatureButton").visible = true;
                                guideBridge.resolveNode("optionalFeaturesOther").visible = true;
                                guideBridge.resolveNode("minEnrolledEmployees_HI").visible = true;
                                guideBridge.resolveNode("PlanYearMax").visible = true;
                                guideBridge.resolveNode("Plan").visible = true;
                                guideBridge.resolveNode("productSequence").value = 3;
                                
                                if(productsArray[i].eligibleEmployee != null){
                                    guideBridge.resolveNode("eligibleEmployeesClass_Emp").value = productsArray[i].eligibleEmployee;
                                    guideBridge.resolveNode("eligibleEmployeesClass_Spouse").value = productsArray[i].eligibleSpouse;

                                    if(productsArray[i].eligibleEmployee.includes("full-time")){
                                        guideBridge.resolveNode("employeeType").value = "full-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("part-time")){
                                        guideBridge.resolveNode("employeeType").value = "part-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("full and part-time")){
                                        guideBridge.resolveNode("employeeType").value = "full and part-time";
                                    }
                                }
                                if(productsArray[i].eligibleEmployeeExcept != null){
                                    guideBridge.resolveNode("exceptionRadio").value = "Yes"
                                    guideBridge.resolveNode("eligibleClassException").value = productsArray[i].eligibleEmployeeExcept;
                                }
                                else{
                                    guideBridge.resolveNode("exceptionRadio").value = "No";
                                }
                                if(productsArray[i].eligibleEmployeeOther != null){
                                    guideBridge.resolveNode("otherEligibleEmpClass").value = productsArray[i].eligibleEmployeeOther;
                                }
                                if(productsArray[i].optionalFeatures != null){
                                    guideBridge.resolveNode("selectedOptionalFeaturesValues").value = productsArray[i].optionalFeatures;
                                }
                                if(productsArray[i].optionalFeatures.length != 0){
                                    fillOptionalFeatureTable(productsArray[i].optionalFeatures);
                                }
                                
                                if(productsArray[i].optionalFeaturesOther != null){
                                    guideBridge.resolveNode("optionalFeaturesOther").value = productsArray[i].optionalFeaturesOther;
                                }
                                if(productsArray[i].hiEnrollEmployeeCount != null){
                                    guideBridge.resolveNode("minEnrolledEmployees_HI").value = productsArray[i].hiEnrollEmployeeCount;
                                }
                                if(productsArray[i].planYearPerInsured != null){
                                    guideBridge.resolveNode("PlanYearMax").value = productsArray[i].planYearPerInsured;
                                }
                                if(productsArray[i].plan != null){
                                    guideBridge.resolveNode("Plan").value = productsArray[i].plan;
                                }
                            }

                            //Dental
                            if(masterDental.includes(productsArray[i].productName)){
                                guideBridge.resolveNode("employeeType").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Emp").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Spouse").visible = true;
                                guideBridge.resolveNode("otherEligibleEmpClass").visible = true;
                                guideBridge.resolveNode("optionalFeaturesDropdown").visible = true;
                                guideBridge.resolveNode("AddOptionalFeatureButton").visible = true;
                                guideBridge.resolveNode("optionalFeaturesOther").visible = true;
                                guideBridge.resolveNode("Plan").visible = true;
                                guideBridge.resolveNode("exceptionRadio").visible = false;
                                guideBridge.resolveNode("productSequence").value = 4;
                               
                                if(productsArray[i].eligibleEmployee != null){
                                    guideBridge.resolveNode("eligibleEmployeesClass_Emp").value = productsArray[i].eligibleEmployee;
                                    guideBridge.resolveNode("eligibleEmployeesClass_Spouse").value = productsArray[i].eligibleSpouse;
 
                                    if(productsArray[i].eligibleEmployee.includes("full-time")){
                                        guideBridge.resolveNode("employeeType").value = "full-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("part-time")){
                                        guideBridge.resolveNode("employeeType").value = "part-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("full and part-time")){
                                        guideBridge.resolveNode("employeeType").value = "full and part-time";
                                    }
                                }
                                if(productsArray[i].eligibleEmployeeOther != null){
                                    guideBridge.resolveNode("otherEligibleEmpClass").value = productsArray[i].eligibleEmployeeOther;
                                }
                                if(productsArray[i].optionalFeatures != null){
                                    guideBridge.resolveNode("selectedOptionalFeaturesValues").value = productsArray[i].optionalFeatures;
                                }
                                if(productsArray[i].optionalFeatures.length != 0){
                                    fillOptionalFeatureTable(productsArray[i].optionalFeatures);
                                }
                                
                                if(productsArray[i].optionalFeaturesOther != null){
                                    guideBridge.resolveNode("optionalFeaturesOther").value = productsArray[i].optionalFeaturesOther;
                                }
                                if(productsArray[i].plan != null){
                                    guideBridge.resolveNode("Plan").value = productsArray[i].plan;
                                }
                            }

                            //Disability
                            if(masterDisability.includes(productsArray[i].productName)){
                                guideBridge.resolveNode("productclass").visible = true;
                                guideBridge.resolveNode("employeeType").visible = true;
                                guideBridge.resolveNode("exceptionRadio").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Emp").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Spouse").visible = false;
                                //guideBridge.resolveNode("eligibleEmpClassChkbox").visible = true;
                                guideBridge.resolveNode("otherEligibleEmpClass").visible = true;
                                guideBridge.resolveNode("optionalFeaturesDropdown").visible = true;
                                guideBridge.resolveNode("AddOptionalFeatureButton").visible = true;
                                guideBridge.resolveNode("optionalFeaturesOther").visible = true;
                                guideBridge.resolveNode("eliminationPeriod").visible = true;
                                guideBridge.resolveNode("benefitPeriod").visible = true;
                                guideBridge.resolveNode("incomeReplacementPercentage").visible = true;
                                guideBridge.resolveNode("productSequence").value = 5;
                                if(productsArray[i].productClass!=null){
                                    guideBridge.resolveNode("productclass").value = productsArray[i].productClass;
                                }

                                if(productsArray[i].eligibleEmployee != null){
                                    guideBridge.resolveNode("eligibleEmployeesClass_Emp").value = productsArray[i].eligibleEmployee;
                                    //guideBridge.resolveNode("eligibleEmployeesClass_Spouse").value = productsArray[i].eligibleSpouse;
 
                                    if(productsArray[i].eligibleEmployee.includes("full-time")){
                                        guideBridge.resolveNode("employeeType").value = "full-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("part-time")){
                                        guideBridge.resolveNode("employeeType").value = "part-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("full and part-time")){
                                        guideBridge.resolveNode("employeeType").value = "full and part-time";
                                    }
                                }

                                if(productsArray[i].eligibleEmployeeExcept != null){
                                    guideBridge.resolveNode("exceptionRadio").value = "Yes"
                                    guideBridge.resolveNode("eligibleClassException").value = productsArray[i].eligibleEmployeeExcept;
                                }
                                else{
                                    guideBridge.resolveNode("exceptionRadio").value = "No";
                                }

                                if(productsArray[i].eligibleEmployeeClass !=null){
                                    guideBridge.resolveNode("eligibleEmpClassChkbox").value = productsArray[i].eligibleEmployeeClass;
                                }

                                if(productsArray[i].eligibleEmployeeOther != null){
                                    guideBridge.resolveNode("otherEligibleEmpClass").value = productsArray[i].eligibleEmployeeOther;
                                }

                                if(productsArray[i].optionalFeatures != null){
                                    guideBridge.resolveNode("selectedOptionalFeaturesValues").value = productsArray[i].optionalFeatures;
                                }
                                if(productsArray[i].optionalFeatures.length != 0){
                                    fillOptionalFeatureTable(productsArray[i].optionalFeatures);
                                }

                                if(productsArray[i].optionalFeaturesOther != null){
                                    guideBridge.resolveNode("optionalFeaturesOther").value = productsArray[i].optionalFeaturesOther;
                                }
                                
                                if(productsArray[i].benefitPeriod!=null){
                                    guideBridge.resolveNode("benefitPeriod").value = productsArray[i].benefitPeriod;
                                }

                                if(productsArray[i].eliminationPeriod!=null){
                                    guideBridge.resolveNode("eliminationPeriod").value = productsArray[i].eliminationPeriod; 
                                }

                                if(productsArray[i].incomeReplacement!=null){
                                    guideBridge.resolveNode("incomeReplacementPercentage").value = productsArray[i].incomeReplacement;
                                }

                            }

                            //Term Life
                            if(masterTermLife.includes(productsArray[i].productName)){
                                guideBridge.resolveNode("planPremiumPeriod").visible = true;
                                guideBridge.resolveNode("optionalFeaturesTextbox").visible = true;
                                guideBridge.resolveNode("Plan").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_termLife").visible = true;

                                guideBridge.resolveNode("exceptionRadio").visible = false;
                                guideBridge.resolveNode("employeeType").visible = false;
                                guideBridge.resolveNode("eligibleEmployeesClass_Emp").visible = false;
                                guideBridge.resolveNode("eligibleEmployeesClass_Spouse").visible = false;
                                guideBridge.resolveNode("otherEligibleEmpClass").visible = false;
                                guideBridge.resolveNode("optionalFeaturesDropdown").visible = false;
                                guideBridge.resolveNode("AddOptionalFeatureButton").visible = false;
                                guideBridge.resolveNode("optionalFeaturesOther").visible = false;
                                guideBridge.resolveNode("productSequence").value = 6;

                                if(productsArray[i].planLevelPremium != null){
                                    guideBridge.resolveNode("planPremiumPeriod").value = productsArray[i].planLevelPremium;
                                }

                                if(productsArray[i].optionalFeaturesText != null){
                                    guideBridge.resolveNode("optionalFeaturesTextbox").value = productsArray[i].optionalFeaturesText;
                                }
                                if(productsArray[i].plan != null){
                                    guideBridge.resolveNode("Plan").value = productsArray[i].plan;
                                }

                                if(productsArray[i].activelyAtWork != null){
                                    var repeatrow = guideBridge.resolveNode("ActivelyAtWorkTableRow");
                                    var currentCount = repeatrow.instanceManager.instanceCount;
                                    for (var m = 0; m < currentCount; m++) {
                                        if (m != 0) {
                                            repeatrow.instanceManager.removeInstance(0);
                                        }
                                    }
                                    for (var j = 0; j < productsArray[i].activelyAtWork.activeAtWorkData.length; j++) {
                                        if (j != 0) {
                                            repeatrow.instanceManager.addInstance();
                                        }
                                    }
                                    for (var k = 0; k < repeatrow.instanceManager.instanceCount; k++) {
                                        repeatrow._instanceManager._instances[k].ActivelyAtWorkClassName.value = productsArray[i].activelyAtWork.activeAtWorkData[k].className;
                                        repeatrow._instanceManager._instances[k].ActivelyAtWorkHoursPerWeek.value = productsArray[i].activelyAtWork.activeAtWorkData[k].activelyAtWorkHours;
                                    }
                                }

                                if(productsArray[i].waitingPeriodEligibility != null){
                                    var repeatrow = guideBridge.resolveNode("WaitingPeriodsTableRow");
                                    var currentCount = repeatrow.instanceManager.instanceCount;
                                    for (var m = 0; m < currentCount; m++) {
                                        if (m != 0) {
                                            repeatrow.instanceManager.removeInstance(0);
                                        }
                                    }
                                    for (var j = 0; j < productsArray[i].waitingPeriodEligibility.waitingPeriodEligibilityData.length; j++) {
                                        if (j != 0) {
                                            repeatrow.instanceManager.addInstance();
                                        }
                                    }
                                    for (var k = 0; k < repeatrow.instanceManager.instanceCount; k++) {
                                        repeatrow._instanceManager._instances[k].WaitingPeriodsClassName.value = productsArray[i].waitingPeriodEligibility.waitingPeriodEligibilityData[k].className
                                        repeatrow._instanceManager._instances[k].waitingPeriod.value = productsArray[i].waitingPeriodEligibility.waitingPeriodEligibilityData[k].waitingPeriod
                                        repeatrow._instanceManager._instances[k].waitingPeriodDays.value = productsArray[i].waitingPeriodEligibility.waitingPeriodEligibilityData[k].numberOfDays
                                    }
                                }
                            }

                            //Whole Life
                            if(masterWholeLife.includes(productsArray[i].productName)){
                                guideBridge.resolveNode("employeeType").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Emp").visible = true;
                                guideBridge.resolveNode("exceptionRadio").visible = true;
                                guideBridge.resolveNode("duePremiumRadio").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Spouse").visible = true;
                                guideBridge.resolveNode("otherEligibleEmpClass").visible = true;
                                guideBridge.resolveNode("planFeatures").visible = true;
                                guideBridge.resolveNode("optionalFeaturesTextbox").visible = true;

                                guideBridge.resolveNode("optionalFeaturesDropdown").visible = false;
                                guideBridge.resolveNode("AddOptionalFeatureButton").visible = false;
                                guideBridge.resolveNode("optionalFeaturesOther").visible = false;
                                guideBridge.resolveNode("productSequence").value = 7;
                                if(productsArray[i].eligibleEmployee != null){
                                    guideBridge.resolveNode("eligibleEmployeesClass_Emp").value = productsArray[i].eligibleEmployee;
                                    guideBridge.resolveNode("eligibleEmployeesClass_Spouse").value = productsArray[i].eligibleSpouse;

                                    if(productsArray[i].eligibleEmployee.includes("full-time")){
                                        guideBridge.resolveNode("employeeType").value = "full-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("part-time")){
                                        guideBridge.resolveNode("employeeType").value = "part-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("full and part-time")){
                                        guideBridge.resolveNode("employeeType").value = "full and part-time";
                                    }
                                }

                                if(productsArray[i].eligibleEmployeeExcept != null){
                                    guideBridge.resolveNode("exceptionRadio").value = "Yes"
                                    guideBridge.resolveNode("eligibleClassException").value = productsArray[i].eligibleEmployeeExcept;
                                }
                                else{
                                    guideBridge.resolveNode("exceptionRadio").value = "No";
                                }

                                if(productsArray[i].premiumDue != null){
                                    guideBridge.resolveNode("duePremiumRadio").value = "Yes"
                                    guideBridge.resolveNode("duePremiumOptions").value = productsArray[i].premiumDue;
                                }
                                else{
                                    guideBridge.resolveNode("duePremiumRadio").value = "No";
                                }
                                if(productsArray[i].eligibleEmployeeOther != null){
                                    guideBridge.resolveNode("otherEligibleEmpClass").value = productsArray[i].eligibleEmployeeOther;
                                }
                                if(productsArray[i].planFeatures != null){
                                    guideBridge.resolveNode("planFeatures").value = productsArray[i].planFeatures;
                                }
                                if(productsArray[i].planFeaturesOther != null){
                                    guideBridge.resolveNode("otherPlanFeatures").value = productsArray[i].planFeaturesOther;
                                }
                                if(productsArray[i].optionalFeaturesText != null){
                                    guideBridge.resolveNode("optionalFeaturesTextbox").value = productsArray[i].optionalFeaturesText;
                                }
                            }

                            //TermTo120
                            if(masterTermTo120.includes(productsArray[i].productName)){
                                guideBridge.resolveNode("employeeType").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Emp").visible = true;
                                guideBridge.resolveNode("eligibleEmployeesClass_Spouse").visible = true;
                                guideBridge.resolveNode("otherEligibleEmpClass").visible = true;
                                guideBridge.resolveNode("groupPolicySitusState").visible = true;
                                guideBridge.resolveNode("optionalFeaturesDropdown").visible = false;
                                guideBridge.resolveNode("AddOptionalFeatureButton").visible = false;
                                guideBridge.resolveNode("optionalFeaturesOther").visible = false;
                                guideBridge.resolveNode("optionalTermLifeCoverages").visible = true;
                                //guideBridge.resolveNode("proposedEffectiveDate").visible = true;
                                //guideBridge.resolveNode("onDate").visible = true;
                                guideBridge.resolveNode("minimumNoOfEmployeesEnrolled").visible = true;
                                guideBridge.resolveNode("minimumPerEligibleEmployees").visible = true;
                                //guideBridge.resolveNode("premiumPaidWithApplication").visible = true;
                                //guideBridge.resolveNode("premiumPaymentMode").visible = true;
                                guideBridge.resolveNode("replacingWithCaic").visible = true;
                                //guideBridge.resolveNode("employeeCostOfInsurance").visible = true;

                                if(productsArray[i].eligibleEmployeeExcept != null){
                                    guideBridge.resolveNode("exceptionRadio").value = "Yes"
                                    guideBridge.resolveNode("eligibleClassException").value = productsArray[i].eligibleEmployeeExcept;
                                }
                                else{
                                    guideBridge.resolveNode("exceptionRadio").value = "No";
                                }

                                guideBridge.resolveNode("applicationReason").visible = false;
                                guideBridge.resolveNode("productSequence").value = 6;
                                if(productsArray[i].eligibleEmployee != null){
                                    guideBridge.resolveNode("eligibleEmployeesClass_Emp").value = productsArray[i].eligibleEmployee;
                                    guideBridge.resolveNode("eligibleEmployeesClass_Spouse").value = productsArray[i].eligibleSpouse;
 
                                    if(productsArray[i].eligibleEmployee.includes("full-time")){
                                        guideBridge.resolveNode("employeeType").value = "full-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("part-time")){
                                        guideBridge.resolveNode("employeeType").value = "part-time";
                                    }
                                    else if(productsArray[i].eligibleEmployee.includes("full and part-time")){
                                        guideBridge.resolveNode("employeeType").value = "full and part-time";
                                    }
                                }

                                if(productsArray[i].eligibleEmployeeOther != null){
                                    guideBridge.resolveNode("otherEligibleEmpClass").value = productsArray[i].eligibleEmployeeOther;
                                }

                                if(productsArray[i].groupPolicyState !=null){
                                    guideBridge.resolveNode("groupPolicySitusState").value = productsArray[i].groupPolicyState;
                                }

                                if(productsArray[i].optionalTermlifeCoverages!=null){
                                    guideBridge.resolveNode("optionalTermLifeCoverages").value = productsArray[i].optionalTermlifeCoverages;
                                }

                                if(productsArray[i].proposedEffectiveDate!=null){
                                    guideBridge.resolveNode("proposedEffectiveDate").value = productsArray[i].proposedEffectiveDate;
                                }

                                // if(productsArray[i].onDate!=null){
                                //     guideBridge.resolveNode("onDate").value = productsArray[i].onDate;
                                // }

                                if(productsArray[i].minimumNoOfEmployees!=null){
                                    guideBridge.resolveNode("minimumNoOfEmployeesEnrolled").value = productsArray[i].minimumNoOfEmployees;
                                }
                                else{
                                    guideBridge.resolveNode("minimumNoOfEmployeesEnrolled").value = 25; 
                                }

                                if(productsArray[i].percentileOfEligibleEmployees!=null){
                                    guideBridge.resolveNode("minimumPerEligibleEmployees").value = productsArray[i].percentileOfEligibleEmployees;
                                }

                                // if(productsArray[i].premiumPaid!=null){
                                //     guideBridge.resolveNode("premiumPaidWithApplication").value = true;
                                // }

                                // if(productsArray[i].premiumPaymentMode!=null){
                                //     guideBridge.resolveNode("premiumPaymentMode").value = productsArray[i].premiumPaymentMode;
                                // }

                                if(productsArray[i].ciac!=null){
                                    guideBridge.resolveNode("replacingWithCaic").value = productsArray[i].ciac;
                                }

                                // if(productsArray[i].employeeCostOfInsurance!=null){
                                //     guideBridge.resolveNode("employeeCostOfInsurance").value = productsArray[i].employeeCostOfInsurance;
                                // }
                            }
                        }
                        setContactTitleName();
                        if(guideBridge.resolveNode("masterAppFormCase").value == "Edit" && data.masterAppReviewTable != null){
                            fillMasterAppReviewTableEdit(data.masterAppReviewTable.masterAppReviewTableData);
                        }else if(guideBridge.resolveNode("masterAppFormCase").value == "Update"  && data.masterAppReviewTable != null){
                            fillMasterAppReviewTableUpdate(data.masterAppReviewTable.masterAppReviewTableData);
                        }else{
                            fillMasterAppReviewTable();
                        }

                        guideBridge.resolveNode("lastSaveTimestamp").visible=true;
                        var timestamp = data.dataStatus.split("#")[1].trim();
                        timestamp = new Date(timestamp);
                        document.querySelectorAll(".lastSaveTimestamp > p > b")[0].innerHTML =  "This Master App was last saved on " + timestamp;
                        
                    }
                }
            }).responseText);
    }
}

function setAccountInfoData(masterAppData){
    guideBridge.resolveNode("masterAppGPID").value = masterAppData.accountAndEligiblity.groupNumberOrGpId;
    guideBridge.resolveNode("accountLevel_CED").value = masterAppData.accountAndEligiblity.effectiveDate;
    guideBridge.resolveNode("organizationName").value = masterAppData.accountAndEligiblity.organizationName;
    guideBridge.resolveNode("certHolders").value = masterAppData.accountAndEligiblity.certHolders;
    
    guideBridge.resolveNode("fullTimeHoursperWeek").value = masterAppData.accountAndEligiblity.hourPerWeekFullTime;
    guideBridge.resolveNode("partTimeHoursperWeek").value = masterAppData.accountAndEligiblity.hourPerWeekpartTime;

    guideBridge.resolveNode("firstOftheMonth").value = masterAppData.accountAndEligiblity.firstOftheMonth;
    guideBridge.resolveNode("fullTimeOrPartTime").value = masterAppData.accountAndEligiblity.fullTimeOrPartTime;

    guideBridge.resolveNode("fullTimeEligibleCoverageDuration").value = masterAppData.accountAndEligiblity.fullTimeEligibleCoverageDuration;
    guideBridge.resolveNode("fullTimeEligibleCoverageMonth").value = masterAppData.accountAndEligiblity.fullTimeEligibleCoverageMonth;
    guideBridge.resolveNode("partTimeEligibleCoverageDuration").value = masterAppData.accountAndEligiblity.partTimeEligibleCoverageDuration;
    guideBridge.resolveNode("partTimeEligibleCoverageMonth").value = masterAppData.accountAndEligiblity.partTimeEligibleCoverageMonth;

    guideBridge.resolveNode("cityOfHQ").value = masterAppData.accountAndEligiblity.cityOfHq;
    guideBridge.resolveNode("subsidiariesOrAffiliates").value = masterAppData.accountAndEligiblity.subsidiariesAffiliates;
    guideBridge.resolveNode("minEnrolledEmployees").value = masterAppData.accountAndEligiblity.enrollEmployeeCount;
    guideBridge.resolveNode("eligibleEmployees").value = masterAppData.accountAndEligiblity.eligibleEmployeeCount;
    guideBridge.resolveNode("otherEligibilityRequirements").value = masterAppData.accountAndEligiblity.otherRequirement;

    var applicableLOBs = guideBridge.resolveNode("masterAppApplicableSeries").value;
    if (applicableLOBs.trim().endsWith(',')) {
        applicableLOBs = applicableLOBs.trim();
        applicableLOBs = applicableLOBs.slice(0, applicableLOBs.length - 1);
    }

    if(applicableLOBs.includes("93000") && applicableLOBs.split(",").length == 1){
        guideBridge.resolveNode("accountSection").visible = false;
        guideBridge.resolveNode("accountSection").resetData();
        guideBridge.resolveNode("eligibleEmployees").visible=false;
        guideBridge.resolveNode("eligibleEmployees").value="";
        document.querySelectorAll(".certHolders select")[0].value = "";
        guideBridge.resolveNode("subsidiariesOrAffiliates").visible=false;
        guideBridge.resolveNode("subsidiariesOrAffiliates").value="";
        
    }
    else if(applicableLOBs.includes("93000") && applicableLOBs.split(",").length > 1){
        guideBridge.resolveNode("accountSection").visible = true;
        guideBridge.resolveNode("eligibleEmployees").visible=true;
        guideBridge.resolveNode("subsidiariesOrAffiliates").visible=true;
    }
    else{
        guideBridge.resolveNode("accountSection").visible = true;
        guideBridge.resolveNode("eligibleEmployees").visible=true;
        guideBridge.resolveNode("subsidiariesOrAffiliates").visible=true;
    }
}

function setPhysicalAddressRadioValue(accountData){
    if(accountData.address == accountData.physicalAddress 
        && accountData.numberOrStreet == accountData.physicalNumberOrStreet 
        && accountData.city == accountData.physicalCity 
        && accountData.state == accountData.physicalState
        && accountData.zip == accountData.physicalZip){
            guideBridge.resolveNode("PhysicalAddressRadio").value = "Yes"
    }
    else{
        guideBridge.resolveNode("PhysicalAddressRadio").value = "No"
        guideBridge.resolveNode("PhysicalAddress").value = accountData.physicalAddress;
        guideBridge.resolveNode("PhysicalNumberStreet").value = accountData.physicalNumberOrStreet;
        guideBridge.resolveNode("PhysicalCity").value = accountData.physicalCity;
        guideBridge.resolveNode("PhysicalState").value = accountData.physicalState;
        guideBridge.resolveNode("PhysicalZip").value = accountData.physicalZip;
    }
}

function clearPremiumValues(){
    var ProductContribution = guideBridge.resolveNode("ProductContribution").value;
    var formCase = guideBridge.resolveNode("masterAppFormCase").value;
    if(ProductContribution =="Other" && formCase=="Add"){
        guideBridge.resolveNode("EmployeePremiumPercentage").value = "";
        guideBridge.resolveNode("EmployerPremiumPercentage").value = "";
    }
}

function clearAccountInfoTab(){
    if(guideBridge.resolveNode("masterAppApplicableSeries").value == null){
        guideBridge.resolveNode("NextToMADetails").visible = false;
    }
    var formCase = guideBridge.resolveNode("masterAppFormCase").value;
    if(formCase=="Edit" || formCase=="Update"){
        guideBridge.resolveNode("accountEligibilitySection").resetData();
        guideBridge.resolveNode("masterAppGPID").value = guideBridge.resolveNode("masterAppGroupNo").value;
        guideBridge.resolveNode("accountLevel_CED").value = guideBridge.resolveNode("coverageEffDateMasterApp").value;
        //document.querySelectorAll(".certHolders select")[0].value = "";
        document.querySelectorAll(".ApplicantType select")[0].value = "";
        document.querySelectorAll(".partTimeEligibleCoverageMonth select")[0].value = "";
        document.querySelectorAll(".fullTimeEligibleCoverageMonth select")[0].value = "";

        //ReviewScreen
        guideBridge.resolveNode("SelectedFormId").value = "";
        guideBridge.resolveNode("SelectedAPIFormID").value = "";
        guideBridge.resolveNode("refreshTimestamp").visible = false;
        guideBridge.resolveNode("adobeSignPanel").resetData();
    }
}

function setFileNetDocumentId(filenetDocId){
    
}

// function setClassesAddedOrRemovedValue(){
//     var applicationRequest = guideBridge.resolveNode("ApplicationRequestPurpose").value;
//     if(applicationRequest!=null){
//       if(applicationRequest.includes("Remove Eligible Classes") || applicationRequest.includes("Add Eligible Classes")){
//             guideBridge.resolveNode("classes_AddedRemoved").visible=true;
//         }else{
//             guideBridge.resolveNode("classes_AddedRemoved").value="";
//             guideBridge.resolveNode("classes_AddedRemoved").visible=false;
//         }
//     }else{
//         guideBridge.resolveNode("classes_AddedRemoved").value="";
//         guideBridge.resolveNode("classes_AddedRemoved").visible=false;
//     }
// }
   
// function setPolicyNumbers(){
//     var applicationRequest = guideBridge.resolveNode("ApplicationRequestPurpose").value;
//     if(applicationRequest!=null){
//         if(!applicationRequest.includes("Issuance of new coverage")){
//             guideBridge.resolveNode("policyNumbers").visible=true;
//         }else{
//             guideBridge.resolveNode("policyNumbers").visible=false;
//             guideBridge.resolveNode("policyNumbers").value="";
//         }
//     }else{
//         guideBridge.resolveNode("policyNumbers").visible=true; 
//     }
// }

function downloadMasterAppPdf(formID){
    
    var groupName = guideBridge.resolveNode("organizationName").value;
    var productsArray = guideBridge.resolveNode("ReviewTableProducts").value.split(",");
    var pdfTitleName = "";
    if(groupName!="" && groupName!=null){
        if(productsArray.length>1){
            pdfTitleName = groupName+" Master Application";
        }else{
            pdfTitleName = groupName+" Master Application "+productsArray[0];
        }
    }
    var signType = guideBridge.resolveNode("eSignTextbox").value == "Yes" ? " ESign" : "";
    var fileName = pdfTitleName + signType + ".pdf";
    
    var loader = document.createElement('div');
        loader.setAttribute('id', 'previewLoader');
        loader.setAttribute('class', 'loader');
        loader.rel = 'stylesheet';
        loader.type = 'text/css';
        loader.href = '/css/loader.css';
        document.getElementsByTagName('BODY')[0].appendChild(loader);
        document.getElementById("guideContainerForm").style.filter = "blur(10px)";
        document.getElementById("guideContainerForm").style.pointerEvents = "none";
    
    guideBridge.getDataXML({
        success: function(guideResultObject) {
            var req = new XMLHttpRequest();
            req.open("POST", "/bin/MasterAppServlet", true);
            req.responseType = "blob";
            var postParameters = new FormData();
            postParameters.append("formData", guideResultObject.data);
            postParameters.append("formId", formID);
            postParameters.append("seriesData", JSON.stringify(masterAppSeriesData));
            postParameters.append("mode", "download");
            req.send(postParameters);

            req.onreadystatechange = function () {
                if (req.readyState == 4 && req.status == 200) {
                    var a;
                    a = document.createElement('a');
                    a.href = window.URL.createObjectURL(req.response);
                    a.download = fileName;
                    a.style.display = 'none';
                    document.body.appendChild(a);
                    a.click();

                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                }  
                else if(req.readyState == 4) {
                    console.log("Error in generating pdf file....");
                   
                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                }
                
            };
        }
    });
}

function clearPartTimeCoverageMonth(){
    guideBridge.resolveNode("partTimeEligibleCoverageMonth").value=""; 
    document.querySelectorAll(".partTimeEligibleCoverageMonth select")[0].value = "";
}

function ValidIncomeReplacement(incomeReplacement){    
    var par = guideBridge.resolveNode("incomeReplacementPercentage"); 
    if (incomeReplacement.endsWith('%')) {
        var partval = incomeReplacement.split('%');
        incomeReplacement = partval[0];
    }
    if(isNaN(incomeReplacement) || incomeReplacement<0 || incomeReplacement.includes('+')){
        guideBridge.resolveNode("incomeReplacementPercentage").value = "";
        document.getElementById(par.id).className = "guideFieldNode guideTextBox incomeReplacementPercentage defaultFieldLayout af-field-filled validation-failure";
        setTimeout(function() {
        var alert = document.getElementById(par.id).children[2];
        alert.setAttribute('role','alert');
        var alertid = "#" + alert.id;
        $(alertid).html("Not a valid value.");
        },10);
    }
    else{
        guideBridge.resolveNode("incomeReplacementPercentage").value = incomeReplacement + "%";
        document.getElementById(par.id).className = "guideFieldNode guideTextBox incomeReplacementPercentage defaultFieldLayout af-field-filled validation-success ";
    }
}

function ValidPercentageOfEligibleEmployee(percentageOfEligibleEmployee){    
    var par = guideBridge.resolveNode("minimumPerEligibleEmployees"); 
    if (percentageOfEligibleEmployee.endsWith('%')) {
        var partval = percentageOfEligibleEmployee.split('%');
        percentageOfEligibleEmployee = partval[0];
    }
    if(isNaN(percentageOfEligibleEmployee) || percentageOfEligibleEmployee<0 || percentageOfEligibleEmployee.includes('+')){
        guideBridge.resolveNode("minimumPerEligibleEmployees").value = "";
        document.getElementById(par.id).className = "guideFieldNode guideTextBox minimumPerEligibleEmployees defaultFieldLayout af-field-filled validation-failure";
        setTimeout(function() {
        var alert = document.getElementById(par.id).children[2];
        alert.setAttribute('role','alert');
        var alertid = "#" + alert.id;
        $(alertid).html("Not a valid value.");
        },10);
    }
    else{
        guideBridge.resolveNode("minimumPerEligibleEmployees").value = percentageOfEligibleEmployee + "%";
        document.getElementById(par.id).className = "guideFieldNode guideTextBox minimumPerEligibleEmployees defaultFieldLayout af-field-filled validation-success ";
    }
}

//selectedOptionalFeaturesTable
//selectedOptionalFeaturesTableRow
//selectedOptionalFeature
//AddOptionalFeatureButton
//clearSelectedOptionalFeaturesButton
//selectedOptionalFeaturesValues
//opFeatureExistsError

function addOptionalFeature(){
    var optionalFeature = guideBridge.resolveNode("masterAppProducts").optionalFeaturesDropdown.value.trim();
    var repeatrow = guideBridge.resolveNode("masterAppProducts").selectedOptionalFeaturesTable.selectedOptionalFeaturesTableRow;
   
    var optionalFeaturesTextboxValue = guideBridge.resolveNode("masterAppProducts").selectedOptionalFeaturesValues.value;
    
    if((optionalFeaturesTextboxValue == null || optionalFeaturesTextboxValue == "") && optionalFeature != null){
        repeatrow._instanceManager._instances[0].selectedOptionalFeature.value = optionalFeature;
        guideBridge.resolveNode("selectedOptionalFeaturesTable").visible = true;
        guideBridge.resolveNode("clearSelectedOptionalFeaturesButton").visible = true;
        guideBridge.resolveNode("masterAppProducts").opFeatureExistsError.visible = false;

        field = guideBridge.resolveNode("masterAppProducts").optionalFeaturesDropdown;
        document.getElementById(field.id).className = "guideFieldNode guideDropDownList optionalFeaturesDropdown defaultFieldLayout af-field-filled validation-success ";
    }
    else if(optionalFeaturesTextboxValue != null && optionalFeature != null){
        var optionalFeaturesTextboxArray = optionalFeaturesTextboxValue.split(",");
        for(var i=0; i<optionalFeaturesTextboxArray.length; i++){
            var featureAlreadyExists = false;
            if(optionalFeaturesTextboxArray[i].trim() == optionalFeature){
                featureAlreadyExists = true;
                break;
            }
            else{
                featureAlreadyExists = false;
            }	
        }
        if(featureAlreadyExists == false){
            repeatrow.instanceManager.addInstance();
            var currentCount = repeatrow.instanceManager.instanceCount;
            repeatrow._instanceManager._instances[currentCount-1].selectedOptionalFeature.value = optionalFeature;
            guideBridge.resolveNode("masterAppProducts").opFeatureExistsError.visible = false;  
        }
        else{
            guideBridge.resolveNode("masterAppProducts").opFeatureExistsError.visible = true;
            $(document).on('change', function() {
                guideBridge.resolveNode("masterAppProducts").opFeatureExistsError.visible = false;
            });
        }
    }
    setOptionalFeaturesValue();
}

function setOptionalFeaturesValue(){
    var repeatrow = guideBridge.resolveNode("masterAppProducts").selectedOptionalFeaturesTable.selectedOptionalFeaturesTableRow;
    var opFeatureArray = [];
   
    for (var i = 0; i < repeatrow.instanceManager.instanceCount; i++) {
        opFeatureArray.push(repeatrow._instanceManager._instances[i].selectedOptionalFeature.value.trim());  
    }
    guideBridge.resolveNode("masterAppProducts").selectedOptionalFeaturesValues.value = opFeatureArray.join(',');
}

function clearOptionalFeatures(){
    var repeatrow = guideBridge.resolveNode("masterAppProducts").selectedOptionalFeaturesTable.selectedOptionalFeaturesTableRow;
    var currentCount = repeatrow.instanceManager.instanceCount;
    for (var m = 0; m < currentCount; m++) {
        if (m != 0) {
            repeatrow.instanceManager.removeInstance(0);
        }
    }
    repeatrow._instanceManager._instances[0].selectedOptionalFeature.value = null;
    guideBridge.resolveNode("masterAppProducts").selectedOptionalFeaturesValues.value = '';
    guideBridge.resolveNode("masterAppProducts").selectedOptionalFeaturesTable.visible = false;
    guideBridge.resolveNode("masterAppProducts").clearSelectedOptionalFeaturesButton.visible = false;
    guideBridge.resolveNode("masterAppProducts").opFeatureExistsError.visible = false;

    field = guideBridge.resolveNode("masterAppProducts").optionalFeaturesDropdown;
    document.getElementById(field.id).className = "guideFieldNode guideDropDownList optionalFeaturesDropdown defaultFieldLayout af-field-filled validation-failure";
    setTimeout(function() {
        var alert = document.getElementById(field.id).children[2];
        alert.setAttribute('role', 'alert');
        var alertid = "#" + alert.id;
        $(alertid).html("Add atleast one Optional Feature");
    },10);
}

function fillOptionalFeatureTable(optionalFeatures){
    guideBridge.resolveNode("selectedOptionalFeaturesTable").visible = true;
    guideBridge.resolveNode("clearSelectedOptionalFeaturesButton").visible = true;
    var repeatrow = guideBridge.resolveNode("masterAppProducts").selectedOptionalFeaturesTable.selectedOptionalFeaturesTableRow;
    
    var currentCount = repeatrow.instanceManager.instanceCount;
    for (var m = 0; m < currentCount; m++) {
        if (m != 0) {
            repeatrow.instanceManager.removeInstance(0);
        }
    }

    for (var i = 0; i < optionalFeatures.length; i++) {
        if (i != 0) {
            repeatrow.instanceManager.addInstance();
        }
    }

    for (var i = 0; i < repeatrow.instanceManager.instanceCount; i++) {
        repeatrow._instanceManager._instances[i].selectedOptionalFeature.value = optionalFeatures[i];
    }
}

//For removing optional features
function onOptionalFeatureRowDelete(){
    $(document).on('click', '.selectedOptionalFeaturesTable .selectedOptionalFeaturesTableRow span.guideTableRuntimeDeleteControl', function () {
        setOptionalFeaturesValue();
    });
}

function reviewTableAdobeSignCheckbox(){
    var repeatrow = guideBridge.resolveNode("MasterAppReviewTableRow");
    var selectedFormIDs = [];
    var selectedAPIFormIDs = [];

    for (var i = 0; i < repeatrow.instanceManager.instanceCount; i++) {
        if(repeatrow._instanceManager._instances[i].ReviewTableSignCheckbox.value == 0){
            guideBridge.resolveNode("adobeSignPanel").visible = true;
            guideBridge.resolveNode("eSignCheckbox").value = "Yes";
            guideBridge.resolveNode("eSignCheckbox").enabled = false;
            guideBridge.resolveNode("policyholderRepresentative").enabled = true;
            guideBridge.resolveNode("SecondSignerEmail").enabled = true;
            guideBridge.resolveNode("reviewSend").enabled = true;
            break;
        }
        else{
            guideBridge.resolveNode("adobeSignPanel").visible = false;
            guideBridge.resolveNode("enrollmentAdminGuideSelection").value = "";
            guideBridge.resolveNode("dataSharingAgreementSelection").value = "";
            guideBridge.resolveNode("eSignCheckbox").enabled = true;
            guideBridge.resolveNode("adobeSignPanel").resetData();
        }
    }

    for (var i = 0; i < repeatrow.instanceManager.instanceCount; i++) {
        if(repeatrow._instanceManager._instances[i].ReviewTableSignCheckbox.value == 0){
            var formID = repeatrow._instanceManager._instances[i].ReviewTableFormID.value;
            var apiFormId = repeatrow._instanceManager._instances[i].apiFormId.value;
            selectedFormIDs.push(formID);
            selectedAPIFormIDs.push(apiFormId);
        }
    }
    guideBridge.resolveNode("SelectedFormId").value = selectedFormIDs.join(',');
    guideBridge.resolveNode("SelectedAPIFormID").value = selectedAPIFormIDs.join(',');
}

function showSecondSigner(){
    var selectedFormIds = guideBridge.resolveNode("SelectedAPIFormID").value;
    if(selectedFormIds.includes("C03204FL")||selectedFormIds.includes("C03204LA")||selectedFormIds.includes("ICC22 C93201")||selectedFormIds.includes("C93201")||selectedFormIds.includes("C50201NH")||selectedFormIds.includes("C80201WA")||selectedFormIds.includes("C70201WA")||selectedFormIds.includes("C60201NH")){
        guideBridge.resolveNode("SecondSignerEmail").visible = true;
    }else{
        guideBridge.resolveNode("SecondSignerEmail").visible = false;
        guideBridge.resolveNode("SecondSignerEmail").value = "";
    }
}

function setAttchmentSelectionItems(attachmentGroup, fieldsname , appType , attachmentType){
    document.querySelectorAll("#guideContainer-rootPanel-panel-panel_193158218-panel_219177938-panel-guideradiobutton___label")[0].style.fontWeight = "bold";
    document.querySelectorAll("#guideContainer-rootPanel-panel-panel_193158218-panel_219177938-panel-guidecheckbox_832267___label")[0].style.fontWeight = "bold";
    var res = JSON.parse(
        $.ajax({
            url: "/bin/MasterAppServlet",
            type: "GET",
            async: false,
            data: {
                "mode":"getFiles",
                "appType":appType,
                "attachmentGroup":attachmentGroup,
                "attachmentType":attachmentType
            },
            success: function (data) {
                if (data != null && data.length > 0) {
                    console.log(data);
                    guideBridge.resolveNode(fieldsname).items = data;
                    guideBridge.resolveNode(fieldsname).visible = true;
                    guideBridge.resolveNode(fieldsname+"_Msg").visible = false;
                }
                else {
                    console.log("No data found");
                    guideBridge.resolveNode(fieldsname).visible = false;
                    guideBridge.resolveNode(fieldsname+"_Msg").visible = true;
                }
            }
        })
    .responseText);
}

function voidAgreement(masterAppId, coverageEffectiveDate, formId, agreementId){
    var masterAppVersion = guideBridge.resolveNode("Version").value;
    var res = JSON.parse(
        $.ajax({
            url: "/bin/VoidAgreement",
            type: "GET",
            async: false,
            data: {
                "mode":"voidAgreement",
                "masterAppId":masterAppId,
                "coverageEffectiveDate":coverageEffectiveDate,
                "formId":formId,
                "agreementId":agreementId,
                "masterAppVersion":masterAppVersion
            },
            success: function (data) {
                console.log("workflow called");
                console.log(data);
            }
        })
    .responseText);
}

function downloadFilenetDoc(docId){

    //File name for the downloaded document
    var groupName = guideBridge.resolveNode("organizationName").value;
    var productsArray = guideBridge.resolveNode("ReviewTableProducts").value.split(",");
    var pdfTitleName = "";
    if(groupName!="" && groupName!=null){
        if(productsArray.length>1){
            pdfTitleName = groupName+" Master Application";
        }else{
            pdfTitleName = groupName+" Master Application "+productsArray[0];
        }
    }
    var signType = guideBridge.resolveNode("eSignTextbox").value == "Yes" ? " ESign" : "";
    var fileName = pdfTitleName + signType + ".pdf";


    //loader or spinner  object for loading screen
    var loader = document.createElement('div');
        loader.setAttribute('id', 'previewLoader');
        loader.setAttribute('class', 'loader');
        loader.rel = 'stylesheet';
        loader.type = 'text/css';
        loader.href = '/css/loader.css';
        document.getElementsByTagName('BODY')[0].appendChild(loader);
        document.getElementById("guideContainerForm").style.filter = "blur(10px)";
        document.getElementById("guideContainerForm").style.pointerEvents = "none";
    
    guideBridge.getDataXML({
        success: function() {
            var req = new XMLHttpRequest();
            req.open("POST", "/bin/MasterAppServlet", true);
            req.responseType = "blob";
            var postParameters = new FormData();
            postParameters.append("docId", docId);
            postParameters.append("mode", "downloadFile");
            req.send(postParameters);

            req.onreadystatechange = function () {
                if (req.readyState == 4 && req.status == 200) {
                    var a;
                    a = document.createElement('a');
                    a.href = window.URL.createObjectURL(req.response);
                    a.download = fileName;
                    a.style.display = 'none';
                    document.body.appendChild(a);
                    a.click();

                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                }  
                else if(req.readyState == 4) {
                    console.log("Error in generating pdf file....");
                   
                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                }
                
            };
        }
    });
}

function showVoidAlert(voidButtonSOM, masterAppId, coverageEffectiveDate, formId, agreementId){
    var modal = document.getElementById('myModal');
    modal.style.display = "block";

    var modal = document.getElementById('myModal');
    
    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];
    
    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
        masterAppId = "";
        coverageEffectiveDate = "";
        formId = "";
        agreementId = "";
        modal.style.display = "none";
    };
    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {
            masterAppId = "";
            coverageEffectiveDate = "";
            formId = "";
            agreementId = "";
            modal.style.display = "none";
        }
    };

    var YesBtn = document.getElementById("voidYes");
    YesBtn.onclick = function() {
        modal.style.display = "none";
        guideBridge.resolveNode(voidButtonSOM).enabled = false;
        voidAgreement(masterAppId, coverageEffectiveDate, formId, agreementId);
        
        
    };

    var NoBtn = document.getElementById("voidNo");
    NoBtn.onclick = function() {
        masterAppId = "";
        coverageEffectiveDate = "";
        formId = "";
        agreementId = "";
        modal.style.display = "none";
    };
}

function refreshSignStatus(){
    var addedProducts = guideBridge.resolveNode("masterAppAddedProducts").value;
    var effectiveDate = guideBridge.resolveNode("coverageEffDateMasterApp").value;
    var groupNumber = guideBridge.resolveNode("masterAppGroupNo").value;
    var deletedProducts = guideBridge.resolveNode("masterAppDeletedProducts").value;
    guideBridge.resolveNode("refreshTimestamp").visible = true;
    var timestamp = new Date();
    document.querySelectorAll(".refreshTimestamp > p")[0].innerHTML =  "Last refreshed: " + timestamp;

    var res = JSON.parse(
        $.ajax({
            url: "/bin/MasterAppServlet",
            type: "GET",
            async: false,
            data: {
                "effectiveDate": effectiveDate,
                "groupNumber": groupNumber,
                "addedProducts":addedProducts,
                "deletedProducts":deletedProducts,
                "mode":"update"
            },
            success: function (data) {
                if (data != null) {

                    masterAppAPIdata = data.masterAppReviewTable.masterAppReviewTableData;
                    console.log(masterAppAPIdata);

                    var repeatrow = guideBridge.resolveNode("MasterAppReviewTableRow");

                    for (var i = 0; i < repeatrow.instanceManager.instanceCount; i++) {
                        for (var j = 0; j < masterAppAPIdata.length; j++) {
                            if(repeatrow._instanceManager._instances[i].ReviewTableFormID.value == masterAppAPIdata[j].formId){
                                repeatrow._instanceManager._instances[i].ReviewTableSignStatus.value = masterAppAPIdata[j].signStatus;
                                repeatrow._instanceManager._instances[i].agreementID.value = masterAppAPIdata[j].agreementId;
                                repeatrow._instanceManager._instances[i].docID.value = masterAppAPIdata[j].filenetDocumentId;
                            }
                        }
                    }
                }
            }
        }).responseText);
}

//for invoking adobe Sign workflow through servlet
function invokeAdobeSignWorkflow(groupNumber, coverageEffectiveDate){
    var errors = [];
    guideBridge.validate(errors, guideBridge.resolveNode("adobeSignPanel").somExpression);

    if (errors.length === 0) {
        guideBridge.getDataXML({
            success: function(guideResultObject) {
                var req = new XMLHttpRequest();
                req.open("POST", "/bin/AdobeSignWorkflow", true);
                var postParameters = new FormData();
                postParameters.append("formData", guideResultObject.data);
                postParameters.append("groupNumber", groupNumber);
                postParameters.append("coverageEffectiveDate", coverageEffectiveDate);
                req.send(postParameters);
                req.onreadystatechange = function() {
                    if (req.readyState == 4 && req.status == 200) {
                        var x = JSON.parse(req.responseText);
                        if(x["status"] == true) {
                            guideBridge.resolveNode("sendWorkflowSuccess").visible = true;
                            guideBridge.resolveNode("sendWorkflowError").visible = false;
                            guideBridge.resolveNode("policyholderRepresentative").enabled = false;
                            guideBridge.resolveNode("SecondSignerEmail").enabled = false;
                            guideBridge.resolveNode("reviewSend").enabled = false;
                        }
                        else {
                            guideBridge.resolveNode("sendWorkflowSuccess").visible = false;
                            guideBridge.resolveNode("sendWorkflowError").visible = true;
                        }
                    }
                }
            },
            error : function (guideResultObject) {
                console.error("Workflow failed");
                guideBridge.resolveNode("sendWorkflowSuccess").visible = false;
                guideBridge.resolveNode("sendWorkflowError").visible = true;
           }
        });
    
        $(document).on('change', function() {
            guideBridge.resolveNode("sendWorkflowError").visible = false;
            guideBridge.resolveNode("sendWorkflowSuccess").visible = false;
        });
    }
    else {
        var errorField = errors[0].getFocus();
        guideBridge.setFocus(errorField);
    } 
}