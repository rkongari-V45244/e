var situsStates = ["AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "DC", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"];
var lobsBrochure = [];
var selectedLobBrouchure = "";
var productSeries = [];
var seriesAllData = [];
var seriesEachData = [];
var productSeriesSelected = "";
var situsBrochure = [];
var situsSelectedBrochure = "";
var languageSelectedBrochure = "";
var coverageBrochure = [];
var coverageSelectedBrochure = "";
var coverageAllData = [];
var coverageLevel = [];
var selectedCoverageLevel = "";
var productDesignTableData = [];
var finalBrouchureTableData = [];
var onAddDataForTable = [];
var addDataForTableUnfiltered = [];
var currentReorderData = [];
var coverageAllDataEnglish = [];
var coverageAllDataSpanish = [];
var flData = [];
var selectedFolderLanguage = "";
var displayFolderLanguage = [];

var lobForVariableSegregation = "";

var brochure = [];
var disclosures = [];
var seriesAllDis = [];
var seriesEachDis = [];
var filterDis = [];

var docId = "";

function onInitializePageBrouchure() {
    var lobsInSelect = ['Accident', 'Critical Illness', 'Hospital Indemnity', 'Dental', 'Disability', 'BenExtend', 'Whole Life', 'Term Life', 'Term to 120'];
    guideBridge.resolveNode('lineOfBusiness').items = lobsInSelect;
}
function onChangingLob(value) {
    selectedLobBrouchure = value;

    for (var i = 0; i < LOBseriesBrochure.length; i++) {
        if (LOBseriesBrochure[i].LOB.includes(selectedLobBrouchure)) {
            productSeries = LOBseriesBrochure[i].Series;
        }
    }

    if (selectedLobBrouchure == 'Accident') {
        disclosures = AccidentDisclosures;
    }
    else if (selectedLobBrouchure == 'Critical Illness') {
        disclosures = CIDisclosures;
    }
    else if (selectedLobBrouchure == 'Hospital Indemnity') {
        disclosures = HIDisclosures;
    }
    else if (selectedLobBrouchure == "Disability") {
        disclosures=DisabilityDisclosures;
    }
    else if (selectedLobBrouchure == 'BenExtend') {
        disclosures = BenExDisclosures;
    }
    else if (selectedLobBrouchure == 'Dental') {
        disclosures = DTDisclosures;
    }
    else if (selectedLobBrouchure == 'Term Life') {
        disclosures=TLDisclosures;
    }
    else if (selectedLobBrouchure == 'Whole Life') {
        disclosures=WLDisclosures;
    } 
    else if (selectedLobBrouchure == 'Term to 120') {
        disclosures=T120Disclosures;
    }

    guideBridge.resolveNode('ProductSeries').visible = false;
    guideBridge.resolveNode('coveragePanel').visible = false;
    guideBridge.resolveNode('ProductDesign').visible = false;
    guideBridge.resolveNode('errorPanel').visible = false;
    onAddDataForTable = [];
    addDataForTableUnfiltered = [];
    seriesAllData = [];
    seriesAllDis = [];
    coverageAllDataEnglish = [];
    coverageAllDataSpanish = [];
    displayFolderLanguage = [];
    resetBrochureTable();
    resetReorderTable();
    guideBridge.resolveNode('coverageNameTB').value = null;
    guideBridge.resolveNode('coverageLevel').items = [];
    guideBridge.resolveNode('coverageLevel').value = null;
    guideBridge.resolveNode('situsStatesTb').value = null;
    guideBridge.resolveNode('language').visible = false;
    guideBridge.resolveNode('language').value = null;
    guideBridge.resolveNode('Broc_Next').visible = false;

    guideBridge.resolveNode('ProductSeries').items = [];
    guideBridge.resolveNode('ProductSeries').value = null;

    for(var i = 0; i < disclosures.length; i++) {
        if (selectedLobBrouchure == disclosures[i].lob) {
            for (var j = 0; j < disclosures[i].series.length; j++) {
                seriesAllDis.push(disclosures[i].series[j]);
            }
        }
    }
        
    guideBridge.resolveNode('ProductSeries').items = productSeries;
    setTimeout(function () {
        guideBridge.resolveNode('ProductSeries').visible = true;
    }, 100);
}

function onChangingProductSeries(value) {
    productSeriesSelected = value;
    guideBridge.resolveNode('coveragePanel').visible = false;
    guideBridge.resolveNode('situsStatesTb').visible = false;
    guideBridge.resolveNode('ProductDesign').visible = false;
    guideBridge.resolveNode('errorPanel').visible = false;
    guideBridge.resolveNode("failureMsg").visible = false;
    guideBridge.resolveNode("FileNetMsg").visible = false;
    onAddDataForTable = [];
    coverageAllDataEnglish = [];
    coverageAllDataSpanish = [];
    addDataForTableUnfiltered = [];
    situsBrochure = [];
    seriesEachData = [];
    seriesEachDis = [];
    displayFolderLanguage = [];
    resetBrochureTable();
    resetReorderTable();
    guideBridge.resolveNode('coverageNameTB').value = null;
    guideBridge.resolveNode('coverageLevel').items = [];
    guideBridge.resolveNode('coverageLevel').value = null;
    guideBridge.resolveNode('situsStatesTb').value = null;
    guideBridge.resolveNode('Broc_Next').visible = false;
    guideBridge.resolveNode('language').visible = false;
    guideBridge.resolveNode('language').value = null;
    
    for(var i = 0; i < seriesAllDis.length; i++){
        if (seriesAllDis[i].name == value) {
            seriesEachDis.push(seriesAllDis[i]);
        }
    }

    guideBridge.resolveNode('situsStatesTb').visible = true;
}

function onChangingSitus() {
    guideBridge.resolveNode('coveragePanel').visible = false;
    guideBridge.resolveNode('ProductDesign').visible = false;
    guideBridge.resolveNode('addButton').visible = false;
    guideBridge.resolveNode("failureMsg").visible = false;
    guideBridge.resolveNode("FileNetMsg").visible = false;
    onAddDataForTable = [];
    coverageAllDataEnglish = [];
    coverageAllDataSpanish = [];
    addDataForTableUnfiltered = [];
    coverageAllData = [];
    coverageBrochure = [];
    displayFolderLanguage = [];
    resetBrochureTable();
    resetReorderTable();
    guideBridge.resolveNode('coverageNameTB').value = null;
    guideBridge.resolveNode('coverageLevel').items = [];
    guideBridge.resolveNode('coverageLevel').value = null;
    guideBridge.resolveNode('language').value = null;
    guideBridge.resolveNode('Broc_Next').visible = false;
    guideBridge.resolveNode('folderLanguageCB').visible = false;
}

function validBrochureSitus(situs) {
    var field = guideBridge.resolveNode("situsStatesTb");
    if (!situsStates.includes(situs)) {
        guideBridge.resolveNode("situsStatesTb").value = '';
        document.querySelectorAll(".situsStatesTb input")[0].value = '';
        situsSelectedBrochure = null;
        document.getElementById(field.id).className = "guideFieldNode guideTextBox situsStatesTb defaultFieldLayout af-field-filled validation-failure";
        setTimeout(function() {
            var alert = document.getElementById(field.id).children[2];
            alert.setAttribute('role', 'alert');
            var alertid = "#" + alert.id;
            $(alertid).html("This is not a valid Situs.");
        }, 10);
    } else {
        document.getElementById(field.id).className = "guideFieldNode guideTextBox situsStatesTb defaultFieldLayout af-field-filled validation-success ";
    }
}

function onChangeingLanguage(value) {
    languageSelectedBrochure = value;
    guideBridge.resolveNode('errorPanel').visible = false;
    guideBridge.resolveNode('coveragePanel').visible = false;
    guideBridge.resolveNode('ProductDesign').visible = false;
    guideBridge.resolveNode('folderLanguageCB').visible = false;
    guideBridge.resolveNode('addButton').visible = false;
    guideBridge.resolveNode('clearButton').visible = false;
    guideBridge.resolveNode("failureMsg").visible = false;
    guideBridge.resolveNode("FileNetMsg").visible = false;
    onAddDataForTable = [];
    coverageAllData = [];
    coverageAllDataEnglish = [];
    coverageAllDataSpanish = [];
    resetBrochureTable();
    resetReorderTable();
    guideBridge.resolveNode('coverageNameTB').value = null;
    guideBridge.resolveNode('coverageLevel').value = null;

    if (value !== undefined && value !== "" && value !== null) {
        guideBridge.resolveNode('Broc_Next').visible = true;
        disableNext();
    }

    guideBridge.resolveNode('addButton').visible = false;
    guideBridge.resolveNode('clearButton').visible = false;
    guideBridge.resolveNode('coveragePanel').visible = false;
    guideBridge.resolveNode('ProductDesign').visible = false;

    guideBridge.resolveNode('coverageNameTB').value = null;

    guideBridge.resolveNode('coverageLevel').items = [];
    guideBridge.resolveNode('coverageLevel').value = null;
    resetBrochureTable();
    resetReorderTable();

    $('.folderLanguageCB input[type="checkbox"]').prop('checked', true);
    guideBridge.resolveNode('folderLanguageCB').value = 0;

}

function disableNext(){
    if((selectedLobBrouchure==null)||(productSeriesSelected==null)||(situsSelectedBrochure==null)||(languageSelectedBrochure==null)){
        guideBridge.resolveNode('Broc_Next').enabled = false;
    }
    else{
        guideBridge.resolveNode('Broc_Next').enabled = true;
    }
}

function onClickNext(){
    guideBridge.resolveNode("planID").value = "";
    guideBridge.resolveNode("planRadio").value = "Plan ID";
    guideBridge.resolveNode('ProductDesign').visible = false;
    guideBridge.resolveNode("disclosuresTable").resetData();
    guideBridge.resolveNode("selectedDocs").value = "";
    resetBrochureTable();
    resetReorderTable();
    startLoader();
    coverageBrochure = [];
    flData = [];
    $.ajax({
        url: "/bin/BrochureMaintenance",
        type: 'GET',
        data: { lob: selectedLobBrouchure, series: productSeriesSelected, situs: situsSelectedBrochure, language: languageSelectedBrochure },
        dataType: 'json', // added data type
        success: function (resdata) {
            coverageAllData = resdata;
            console.log(coverageAllData);
            if(coverageAllData.length !=0){
                for(var i=0;i<coverageAllData.length;i++){
                    if (coverageBrochure.includes(coverageAllData[i]["coverage-name"]) != true && (coverageAllData[i]["coverage-name"] != "Folder Language")){
                        coverageBrochure.push(coverageAllData[i]["coverage-name"]);
                    }
                    if (coverageAllData[i]["coverage-name"] == "Folder Language") {
                        flData.push(coverageAllData[i]);
                        guideBridge.resolveNode('folderLanguageCB').visible = true;
                    }   
                }
                onChangeingFolderLanguageCB();
            }
            else{
                guideBridge.resolveNode('errorPanel').visible = true;
                guideBridge.resolveNode('coveragePanel').visible = false;
            }
            stopLoader();
        }
            
      });
}

function onChangingCoverage() {
    coverageLevel = [];
    productDesignTableData = [];
    guideBridge.resolveNode('addButton').visible = false;
    guideBridge.resolveNode('clearButton').visible = false;

    for (var x = 0; x < coverageAllData.length; x++) {
        if (coverageSelectedBrochure == coverageAllData[x]["coverage-name"]) {
            if (coverageLevel.includes(coverageAllData[x]["coverage-level"]) != true) {
                coverageLevel.push(coverageAllData[x]["coverage-level"]);
                productDesignTableData.push({ "level": coverageAllData[x]["coverage-level"], "formNum": coverageAllData[x]["form-number"] })
            }
        }
    }
    guideBridge.resolveNode('coverageLevel').items = [];
    guideBridge.resolveNode('coverageLevel').value = null;
    guideBridge.resolveNode('coverageLevel').items = coverageLevel;
}
function onChangeingCoverageLevel(value) {
    selectedCoverageLevel = value;
    finalBrouchureTableData = [];
    var brouchureID = "";

    for (var i = 0; i < productDesignTableData.length; i++) {
        if (productDesignTableData[i].level == selectedCoverageLevel) {
            brouchureID = productDesignTableData[i].formNum;
        }
    }
    var planDesign = coverageSelectedBrochure + "- " + selectedCoverageLevel;
    finalBrouchureTableData.push({ "formNum": brouchureID, "coverageLevel": selectedCoverageLevel, "planName": coverageSelectedBrochure, "planDesign": planDesign });

    if (selectedCoverageLevel != "" && selectedCoverageLevel != undefined && selectedCoverageLevel != null) {
        guideBridge.resolveNode('addButton').visible = true;
        guideBridge.resolveNode('clearButton').visible = true;
    }
}
function clearBrochureButton() {
    guideBridge.resolveNode('addButton').visible = false;
    guideBridge.resolveNode('clearButton').visible = false;
    guideBridge.resolveNode('coverageLevel').value = null;
    guideBridge.resolveNode('coverageLevel').items = [];
}

function onChangeingFolderLanguageCB() {
    displayFolderLanguage = [];
    console.log(flData);
    if (flData.length == 1 ) {
        $('.folderLanguageCB input[type="checkbox"]').prop('checked', true);
        guideBridge.resolveNode('folderLanguageCB').value = 0;
        guideBridge.resolveNode('folderLanguageCB').enabled = false;
    }
    else {
        guideBridge.resolveNode('folderLanguageCB').enabled = true;
    }
    if (guideBridge.resolveNode('folderLanguageCB').value == 0) {
        for (var i = 0; i < flData.length; i++) {
            if (flData[i]["coverage-level"] == "Electronic") {
                displayFolderLanguage.push({ "name": flData[i]["coverage-name"], "formNum": flData[i]["form-number"], "level": flData[i]["coverage-level"] });
            }
        }
    }
    else if (guideBridge.resolveNode('folderLanguageCB').value != 0 && (flData.length == 1)) {
        for (var i = 0; i < flData.length; i++) {
            if (flData[i]["coverage-level"] != "Electronic" && (flData.length == 1)) {
                displayFolderLanguage.push({ "name": flData[i]["coverage-name"], "formNum": flData[i]["form-number"], "level": flData[i]["coverage-level"] });
            }
        }
    }
    else if (guideBridge.resolveNode('folderLanguageCB').value != 0 && (flData.length != 1)) {
        for (var i = 0; i < flData.length; i++) {
            if (flData[i]["coverage-level"] != "Electronic") {
                displayFolderLanguage.push({ "name": flData[i]["coverage-name"], "formNum": flData[i]["form-number"], "level": flData[i]["coverage-level"] });
            }
        }
    }
}

function validGroup(){
    var planID = guideBridge.resolveNode('planID').value;
    var field = guideBridge.resolveNode("planID");
    disableGenerateButton();
    if(guideBridge.resolveNode('planRadio').value=="Group"){
        if (planID.length > 0 && planID.length < 10 && !isNaN(planID)) {
            planID = planID.padStart(10, '0')
            guideBridge.resolveNode("planID").value = planID;
        }

        if (!isNaN(planID) && planID.length == 10 && !planID.includes('+') && !planID.includes('-') && !planID.includes('.') && planID != '0000000000') {
            document.getElementById(field.id).className = "guideFieldNode guideTextBox planID defaultFieldLayout af-field-filled validation-success ";
        } else if (planID.length == 13 && planID.startsWith("AGC") && !isNaN(planID.slice(3, 13)) && !planID.includes('+') && !planID.includes('-') && !planID.includes('.')) {
            document.getElementById(field.id).className = "guideFieldNode guideTextBox planID defaultFieldLayout af-field-filled validation-success ";
        } else {
            guideBridge.resolveNode("planID").value = "";
            document.getElementById(field.id).className = "guideFieldNode guideTextBox planID defaultFieldLayout af-field-filled validation-failure";
            setTimeout(function() {
            var alert = document.getElementById(field.id).children[2];
            alert.setAttribute('role', 'alert');
            var alertid = "#" + alert.id;
            $(alertid).html("This is not a valid Aflac Group number. Please re-enter.");
            },10);
        }
    }
    else{
        if (planID.length > 0 && planID.length < 10 && !isNaN(planID)) {
            //planID = planID.padStart(10, '0')
            //console.log("Plan-"+planID);
            guideBridge.resolveNode("planID").value = "Plan-"+planID;
            planID = "Plan-"+planID;
        }
        if (planID.length > 5 && planID.length <= 14 && planID.startsWith("Plan-") && !isNaN(planID.slice(5, 14)) && !planID.includes('+') && !planID.slice(5, 14).includes('-') && !planID.includes('.') && !planID.slice(5, 14).startsWith('0')) {
            document.getElementById(field.id).className = "guideFieldNode guideTextBox planID defaultFieldLayout af-field-filled validation-success ";
        } else {
            guideBridge.resolveNode("planID").value = "";
            document.getElementById(field.id).className = "guideFieldNode guideTextBox planID defaultFieldLayout af-field-filled validation-failure";
            setTimeout(function() {
            var alert = document.getElementById(field.id).children[2];
            alert.setAttribute('role', 'alert');
            var alertid = "#" + alert.id;
            $(alertid).html("This is not a valid Plan ID. Please re-enter.");
            },10);
        }
    }
}

function planRadioChange(){
    var radio = guideBridge.resolveNode('planRadio').value;
    var field = guideBridge.resolveNode("planID");
    if(radio=="Plan ID"){
        guideBridge.resolveNode('planID').value = null;
        document.getElementById(field.id).className = "guideFieldNode guideTextBox planID defaultFieldLayout ";
    }
    else{
        guideBridge.resolveNode('planID').value = null;
        document.getElementById(field.id).className = "guideFieldNode guideTextBox planID defaultFieldLayout ";
    }
    disableGenerateButton();
}

function disableGenerateButton() {
    var planIDBroc = guideBridge.resolveNode("planID").value;
    if (currentReorderData.length > 0 && planIDBroc != null) {
        guideBridge.resolveNode('generate').enabled = true;
    }
    else {
        guideBridge.resolveNode('generate').enabled = false;
        guideBridge.resolveNode('downloadBrochure').visible=false;
    }
}

function addBrochureTableData() {

    addDataForTableUnfiltered.push(finalBrouchureTableData[0]);

    var count = 0;
    if (onAddDataForTable.length > 0) {
        for (var j = 0; j < onAddDataForTable.length; j++) {
            if (onAddDataForTable[j].formNum !== finalBrouchureTableData[0].formNum) {
                count++;
                if (count == onAddDataForTable.length) {
                    onAddDataForTable.push(finalBrouchureTableData[0]);
                }
            }
        }
    }
    else if (onAddDataForTable.length == 0) {
        onAddDataForTable.push(finalBrouchureTableData[0]);
    }

    for (var i = 0; i < onAddDataForTable.length; i++) {
        if (i != 0 && guideBridge.resolveNode('productDesignRow1').instanceManager.instanceCount < onAddDataForTable.length) {
            guideBridge.resolveNode('productDesignRow1').instanceManager.addInstance(); //add panel
        }
    }
    var count = guideBridge.resolveNode('productDesignRow1').instanceManager.instanceCount;

    guideBridge.resolveNode('productDesignRow1').instanceManager.instances[count - 1].pdCoverageName.value = onAddDataForTable[count - 1].planName;
    guideBridge.resolveNode('productDesignRow1').instanceManager.instances[count - 1].pdCoverageLevel.value = onAddDataForTable[count - 1].coverageLevel;
    guideBridge.resolveNode('productDesignRow1').instanceManager.instances[count - 1].pdBrochureId.value = onAddDataForTable[count - 1].formNum;
    guideBridge.resolveNode('productDesignRow1').instanceManager.instances[count - 1].pdPlanDesign.value = onAddDataForTable[count - 1].planDesign;
    document.querySelector(".pdCoverageLevel input").style.width = '250px';

    //PlanNam tooltip
    var dynIDPlanNameTooltip = guideBridge.resolveNode('productDesignRow1').instanceManager.instances[count - 1].pdCoverageName.id;
    var dynaLabIDPlanNameTooltip = "#" + dynIDPlanNameTooltip + "_guideFieldShortDescription > p";
    $(dynaLabIDPlanNameTooltip).html(onAddDataForTable[count - 1].planName);
    //PlanNam end
    document.querySelectorAll(".pdCoverageName input")[count - 1].style.textOverflow = 'ellipsis';
    document.querySelectorAll(".pdCoverageName input")[count - 1].style.whiteSpace = 'nowrap';
    document.querySelectorAll(".pdCoverageName input")[count - 1].style.overflow = 'hidden';
    disableGenerateButton();


    // console.log("currentReorderData",currentReorderData);
    // console.log("displayFolderLanguage",displayFolderLanguage);
    if (displayFolderLanguage.length != 0 && (currentReorderData.includes(displayFolderLanguage[0].formNum) != true)) {
        //console.log("Add button",displayFolderLanguage);//name
        // console.log("Add button",flData.levels[0].level); 
        // console.log("Add button",flData.levels[0].brochureId);
        currentReorderData = [];
        document.querySelectorAll(".brPlanDesign input")[0].style.width = '305px';
        document.querySelectorAll(".brPlanDesign input")[0].style.paddingRight = '0px';
        document.querySelectorAll(".brPlanDesign input")[0].style.paddingLeft = '0px';

        var FLplanDesign = displayFolderLanguage[0].name + "- " + displayFolderLanguage[0].level;
        guideBridge.resolveNode("brochureReorderRow1").instanceManager.instances[0].brFormNumber.value = displayFolderLanguage[0].formNum;
        guideBridge.resolveNode("brochureReorderRow1").instanceManager.instances[0].brPlanDesign.value = FLplanDesign;

        var dynIDbrPlanDesignTooltip = guideBridge.resolveNode("brochureReorderRow1").instanceManager.instances[0].brPlanDesign.id;
        var dynaLabIDbrPlanDesignTooltip = "#" + dynIDbrPlanDesignTooltip + "_guideFieldShortDescription > p";
        $(dynaLabIDbrPlanDesignTooltip).html(FLplanDesign);

        displayFolderLanguage[0].formNum = displayFolderLanguage[0].formNum + "-FLANG";
        currentReorderData.push(displayFolderLanguage[0].formNum);
        disableGenerateButton();
    }
    //console.log("after hide");
}

function reorderRows() {

    document.querySelectorAll("#guideContainer-rootPanel-panel_1872932356_cop-table_20623242-HeaderRow-tableItem___guide-item")[0].style.width = "40%";
    document.querySelectorAll("#guideContainer-rootPanel-panel_1872932356_cop-table_20623242-HeaderRow-headerItem1678702392257___guide-item")[0].style.width = "60%";
    disableGenerateButton();
    $(document).on('click', '.ProductDesignTable .productDesignRow1', function () {
        //console.log("clicked");
        //console.log("This: ", this);
        //console.log("this role: ", this.id);

        var id = "#" + this.id;
        var ele = document.querySelectorAll(id);
        // ele[0].style.backgroundColor = 'darkgray';
        //console.log("Ele: ", ele[0].children[2].children[0].children[0].children[0].children[0].value) ;
        var bIDfromClick = ele[0].children[2].children[0].children[0].children[0].children[0].value;
        var pdFromClick = ele[0].children[3].children[0].children[0].children[0].children[0].value;
        //EXTRACTING DATA FROM HIDDEN COLUMNS OF THE ROW
        // console.log("Bid: ", bIDfromClick);
        // console.log("Pd: ", pdFromClick);
        var row = guideBridge.resolveNode("brochureReorderRow1");
        var count = row.instanceManager.instanceCount;
        // console.log("currentReorderData: ", currentReorderData);

        if (displayFolderLanguage.length != 0) {
            for (var i = 1; i < count + 1; i++) {
                if (currentReorderData.includes(bIDfromClick) != true && (bIDfromClick != "")) {
                    //if(currentReorderData.length != 0)
                    row.instanceManager.addInstance(true);
                    //guideBridge.resolveNode('folderLanguage').visible=false;
                    row.instanceManager.instances[currentReorderData.length].brFormNumber.value = bIDfromClick;
                    row.instanceManager.instances[currentReorderData.length].brPlanDesign.value = pdFromClick;

                    //PlanDesign tooltip
                    var dynIDbrPlanDesignTooltip = row.instanceManager.instances[currentReorderData.length].brPlanDesign.id;
                    var dynaLabIDbrPlanDesignTooltip = "#" + dynIDbrPlanDesignTooltip + "_guideFieldShortDescription > p";
                    $(dynaLabIDbrPlanDesignTooltip).html(pdFromClick);
                    //PlanDesign tooltip end

                    // document.querySelectorAll(".brFormNumber input")[currentReorderData.length].style.width ='150px';
                    document.querySelectorAll(".brPlanDesign input")[currentReorderData.length].style.width = '305px';
                    document.querySelectorAll(".brPlanDesign input")[currentReorderData.length].style.paddingRight = '0px';
                    document.querySelectorAll(".brPlanDesign input")[currentReorderData.length].style.paddingLeft = '0px';

                    document.querySelectorAll(".brPlanDesign input")[currentReorderData.length].style.textOverflow = 'ellipsis';
                    document.querySelectorAll(".brPlanDesign input")[currentReorderData.length].style.whiteSpace = 'nowrap';
                    document.querySelectorAll(".brPlanDesign input")[currentReorderData.length].style.overflow = 'hidden';

                    currentReorderData.push(bIDfromClick);//To check duplicacy
                }
            }
        }
        else {
            for (var i = 0; i < count; i++) {
                if (currentReorderData.includes(bIDfromClick) != true && (bIDfromClick != "")) {
                    if (currentReorderData.length != 0)
                        row.instanceManager.addInstance(true);
                    row.instanceManager.instances[currentReorderData.length].brFormNumber.value = bIDfromClick;
                    row.instanceManager.instances[currentReorderData.length].brPlanDesign.value = pdFromClick;

                    //PlanDesign tooltip
                    var dynIDbrPlanDesignTooltip = row.instanceManager.instances[currentReorderData.length].brPlanDesign.id;
                    var dynaLabIDbrPlanDesignTooltip = "#" + dynIDbrPlanDesignTooltip + "_guideFieldShortDescription > p";
                    $(dynaLabIDbrPlanDesignTooltip).html(pdFromClick);
                    //PlanDesign tooltip end

                    // document.querySelectorAll(".brFormNumber input")[currentReorderData.length].style.width ='150px';
                    document.querySelectorAll(".brPlanDesign input")[currentReorderData.length].style.width = '305px';
                    document.querySelectorAll(".brPlanDesign input")[currentReorderData.length].style.paddingRight = '0px';
                    document.querySelectorAll(".brPlanDesign input")[currentReorderData.length].style.paddingLeft = '0px';

                    document.querySelectorAll(".brPlanDesign input")[currentReorderData.length].style.textOverflow = 'ellipsis';
                    document.querySelectorAll(".brPlanDesign input")[currentReorderData.length].style.whiteSpace = 'nowrap';
                    document.querySelectorAll(".brPlanDesign input")[currentReorderData.length].style.overflow = 'hidden';

                    currentReorderData.push(bIDfromClick);//To check duplicacy
                    //console.log(currentReorderData);
                }
                disableGenerateButton();
            }
        }
        // console.log("currentReorderData: ", currentReorderData);
        var allAdd2 = document.querySelectorAll(".brochureReorderRow1 td span.guideTableRuntimeAddControl");
        var allDel2 = document.querySelectorAll(".brochureReorderRow1 td span.guideTableRuntimeDeleteControl");
        for (var val = 0; val < allAdd2.length; val++) {
            allAdd2[val].style.display = 'none';
        }
        for (var v = 0; v < allDel2.length; v++) {
            allDel2[v].style.display = 'none';
        }
    });
}
function autoCompleteCoverageName() {
    clearBrochureButton();
    $(".coverageNameTB input").autocomplete({
        minLength: 0,
        source: coverageBrochure,
        delay: 0,
        select: function (event, ui) {
            coverageSelectedBrochure = ui.item.label;
            onChangingCoverage();
        },
        open: function () {
            $('.ui-autocomplete').css('width', '20%');
            $('.ui-autocomplete').css('line-height', '30px');
        },
    }).focus(function () {
        $(this).autocomplete('search', $(this).val())
    })
        .data('ui-autocomplete')._renderItem = function (ul, item) {
            return $('<li>')
                .append("<li>● " + item.label + "</li>")
                .appendTo(ul);
        }
    $(document).on('change', '.coverageNameTB input', function () {
        if (this.value == "" || this.value == undefined || this.value == null) {
            guideBridge.resolveNode('addButton').visible = false;
            guideBridge.resolveNode('clearButton').visible = false;
            guideBridge.resolveNode('coverageLevel').items = [];
            guideBridge.resolveNode('coverageLevel').value = null;
        }
    });

    // Disclosure Table...
    var disclosuresToShow = [];
    console.log(seriesEachDis);
    if(seriesEachDis.length >= 1){
        for(var j=0; j < seriesEachDis[0].data.length; j++){
            var data = seriesEachDis[0].data[j];
            for(var k=0; k < data.situs.length; k++){
                var situs = data.situs[k];
                if(situs === situsSelectedBrochure){
                    for(var i=0; i < data.disclosures.length; i++){
                        disclosuresToShow.push(data.disclosures[i]);
                    }
                }
            }
        }
    }

    console.log("disclosuresToShow ", disclosuresToShow)
    var row = guideBridge.resolveNode('disclosureRow');
    var delCount = row.instanceManager.instanceCount;
    for (var m = 0; m <= delCount; m++) {
        row.instanceManager.removeInstance(1);
    }

    if(disclosuresToShow == undefined || disclosuresToShow.length < 1){
        guideBridge.resolveNode('disclosuresTable').visible = false;
    }
    else {
        guideBridge.resolveNode('disclosuresTable').visible = true;
        for(var i=0; i < disclosuresToShow.length; i++){
            var tdis = disclosuresToShow[i];
            row.instanceManager.addInstance(true);
            row.instanceManager.instances[i].fileName.value = tdis.fileName;
            row.instanceManager.instances[i].docName.value = tdis.docName;
            row.instanceManager.instances[i].docType.value = tdis.docType;
            row.instanceManager.instances[i].formNo.value = tdis.formNo;
            row.instanceManager.instances[i].desc.value = tdis.description;
            
            var DocNameCol = document.querySelectorAll(".disclosuresTable table .docName textarea");
            DocNameCol[i].style.border = "none";
            DocNameCol[i].style.outline = "none";
           // DocNameCol[i].style.overflow = "hidden";
            DocNameCol[i].style.height = "auto";
            DocNameCol[i].style.resize = "none";
            DocNameCol[i].style.marginTop= "16px";

            var DescNameCol = document.querySelectorAll(".disclosuresTable table .desc textarea");
            DescNameCol[i].style.border = "none";
            DescNameCol[i].style.minHeight = "100px";
            DescNameCol[i].style.fontFamily = "Arial";
            DescNameCol[i].style.outline = "none";
          // DescNameCol[i].style.overflow = "hidden";
            DescNameCol[i].style.height = "auto";
            DescNameCol[i].style.resize = "none";
            //DescNameCol[i].style.opacity= 0.5;

            if(i%2==1){
                DocNameCol[i].style.background= "#ECECEC";
                DescNameCol[i].style.background= "#ECECEC";
            }

        }
        row.instanceManager.removeInstance(i);
    }
}

function downloadDisclosure(fileName){
    
    guideBridge.getDataXML({
        success: function (guideResultObject) {
            var req = new XMLHttpRequest();

            req.open("POST", "/bin/BrochureAssemblePDF", true);
            req.responseType = "blob";
            var postParameters = new FormData();
            postParameters.append("mode","disclosure");
            postParameters.append("file",fileName);
            req.send(postParameters);
            req.onreadystatechange = function () {

                if (req.readyState == 4 || req.status == 200) {
                    var a;
                    a = document.createElement('a');
                    a.href = window.URL.createObjectURL(req.response);
                    a.download = fileName;
                    a.style.display = 'none';
                    document.body.appendChild(a);
                    a.click();
                }
                else {
                   console.log("File Not Found");
                }
            };
        }
    });
}

function autoCompleteSitus() {
    // console.log("Situs Name in autocomplete: ",situsStates);
    var valueOnChange = "";
    var valueOnClick = "";

    $(document).on('change', '.situsStatesTb input', function () {
        guideBridge.resolveNode('Broc_Next').visible = false;
        document.querySelectorAll('.language select')[0].value=null;
        valueOnChange = this.value;

        if (this.value == "" || this.value == undefined || this.value == null) {

            guideBridge.resolveNode('language').value = null;
            // console.log('auto complete if');
            guideBridge.resolveNode('language').visible = false;
        }
        else {
            // console.log('auto complete else');
            guideBridge.resolveNode('language').visible = true;
            guideBridge.resolveNode('language').value = null;
            guideBridge.resolveNode('errorPanel').visible = false;
        }
        situsSelectedBrochure = valueOnChange;
    });

    $(document).on('click', '.situsStatesTb input', function () {
        //console.log("This situs is clicked: ", this);
        valueOnClick = this.value;
        //console.log("Value on change: ", valueOnChange);
        //console.log("Value on click: ", valueOnClick);
        $(".situsStatesTb input").autocomplete({
            minLength: 0,
            source: situsStates,
            delay: 0,
            select: function (event, ui) {
                situsSelectedBrochure = ui.item.label;
                if (situsBrochure.includes(situsSelectedBrochure) != true) {
                    guideBridge.resolveNode('Broc_Next').visible = false;
                    // guideBridge.resolveNode('language').visible = false;
                    guideBridge.resolveNode('errorPanel').visible = false;
                    guideBridge.resolveNode('language').value = null;
                    $(function () {
                        $('.situsStatesTb input').blur();
                    });
                    //guideBridge.resolveNode('errorPanel').visible=true;
                }
                else {
                    //console.log("Should show language");
                    $(function () {
                        $('.situsStatesTb input').blur();
                    });
                    // guideBridge.resolveNode('Broc_Next').visible=true;
                    guideBridge.resolveNode('errorPanel').visible = false;
                    guideBridge.resolveNode('language').visible = true;
                }
                onChangingSitus();
            }
        });
    });
}
function resetReorderTable() {
    //console.log("Reorder Table",reorderTableRow.instanceManager.instances);
    if (displayFolderLanguage.length != 0) {
        deleteReorderRows();
        currentReorderData.splice(1);
        disableGenerateButton();
    }
    else {
        guideBridge.resolveNode("brochureReorderRow1").instanceManager.instances[0].brFormNumber.value = "";
        guideBridge.resolveNode("brochureReorderRow1").instanceManager.instances[0].brPlanDesign.value = "";

        var dynIDbrPlanDesignTooltip = guideBridge.resolveNode("brochureReorderRow1").instanceManager.instances[0].brPlanDesign.id;
        var dynaLabIDbrPlanDesignTooltip = "#" + dynIDbrPlanDesignTooltip + "_guideFieldShortDescription > p";
        $(dynaLabIDbrPlanDesignTooltip).html(null);

        deleteReorderRows();
        currentReorderData = [];
        disableGenerateButton();
    }
}

function deleteReorderRows() {
    var row1 = guideBridge.resolveNode("brochureReorderRow1");
    var delCount = row1.instanceManager.instanceCount;
    for (var m = 0; m <= delCount; m++) {
        row1.instanceManager.removeInstance(1);
    }

}
function resetBrochureTable() {
    var reorderTableRow = guideBridge.resolveNode('productDesignRow1');
    guideBridge.resolveNode("productDesignRow1").instanceManager.instances[0].pdCoverageName.value = "";
    guideBridge.resolveNode("productDesignRow1").instanceManager.instances[0].pdCoverageLevel.value = "";
    guideBridge.resolveNode("productDesignRow1").instanceManager.instances[0].pdBrochureId.value = "";
    guideBridge.resolveNode("productDesignRow1").instanceManager.instances[0].pdPlanDesign.value = "";

    var dynIDPlanNameTooltip = guideBridge.resolveNode('productDesignRow1').instanceManager.instances[0].pdCoverageName.id;
    var dynaLabIDPlanNameTooltip = "#" + dynIDPlanNameTooltip + "_guideFieldShortDescription > p";
    $(dynaLabIDPlanNameTooltip).html(null);

    deleteBrochureRows();
    onAddDataForTable = [];
}
function deleteBrochureRows() {
    var row1 = guideBridge.resolveNode("productDesignRow1");
    var delCount = row1.instanceManager.instanceCount;
    for (var m = 0; m <= delCount; m++) {
        row1.instanceManager.removeInstance(1);
    }
}

function hideRuntimeControls() {
    var allAdd2 = document.querySelectorAll(".brochureReorderRow1 td span.guideTableRuntimeAddControl");
    var allDel2 = document.querySelectorAll(".brochureReorderRow1 td span.guideTableRuntimeDeleteControl");
    for (var val = 0; val < allAdd2.length; val++) {
        allAdd2[val].style.display = 'none';
    }
    for (var v = 0; v < allDel2.length; v++) {
        allDel2[v].style.display = 'none';
    }
}

function redirectHomePage(){
    var currentURL= window.location.origin;
    console.log(currentURL);
    window.location = currentURL+"/content/aflacapps/us/aflac-application-suite.html";
}

//add generate pdf functionality
function generatePdfOfBrochures() {
    var planIDBroc = guideBridge.resolveNode("planID").value;
    console.log(planIDBroc);
    guideBridge.resolveNode("FileNetMsg").visible = false;
    guideBridge.resolveNode("failureMsg").visible = false; 
    var loader = document.createElement('div');
    loader.setAttribute('id', 'previewLoader');
    loader.setAttribute('class', 'loader');
    loader.rel = 'stylesheet';
    loader.type = 'text/css';
    loader.href = '/css/loader.css';
    document.getElementsByTagName('BODY')[0].appendChild(loader);
    document.getElementById("guideContainerForm").style.filter = "blur(10px)";
    document.getElementById("guideContainerForm").style.pointerEvents = "none";
    docId = "";

    guideBridge.getDataXML({
        success: function (guideResultObject) {
            var req = new XMLHttpRequest();

            req.open("POST", "/bin/BrochureAssemblePDF", true);
            //req.responseType = "blob";
            var postParameters = new FormData();
            postParameters.append("formNumber", currentReorderData);
            postParameters.append("product", selectedLobBrouchure);
            postParameters.append("series", productSeriesSelected);
            postParameters.append("situs", situsSelectedBrochure);
            postParameters.append("language", guideBridge.resolveNode('language').value);
            postParameters.append("documentNames", guideBridge.resolveNode('selectedDocs').value);
            postParameters.append("planId", planIDBroc);
            req.send(postParameters);
            req.onreadystatechange = function () {
                
                if (req.readyState == 4 && req.status == 200) {

                    guideBridge.resolveNode("failureMsg").visible = false;
                    // var blob = new Blob([this.response], {
                    //     type: "application/pdf"
                    // }),
                    // newUrl = URL.createObjectURL(blob);
                    // window.open(newUrl, "_blank", "menubar=yes,resizable=yes,scrollbars=yes");
                    
                    guideBridge.resolveNode("FileNetMsg").visible = true;
                    docId = JSON.parse(req.responseText)["DocumentID"];
                    var msg = "<p><span class=\"greenColorText\"><b>Success : Brochure file is successfully generated and saved to FileNet with ID: " + docId + "</b></span></p>";
                    guideBridge.resolveNode("FileNetMsg").value = msg;
                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                    //readFile(docId);
                    guideBridge.resolveNode('downloadBrochure').visible=true;
                } 
                else if (req.readyState == 4 && req.status == 400) {
                    var msg = JSON.parse(req.responseText)["message"];
                    msg = "<p><span class=\"redColorText\"><b>" + msg + "</b></span></p>";
                    guideBridge.resolveNode("FileNetMsg").visible = true;
                    guideBridge.resolveNode("FileNetMsg").value = msg;
                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                    guideBridge.resolveNode('downloadBrochure').visible=true;
                    //readFile(docId);
                } 
                else if(req.readyState == 4) {
                    console.log("Failure..");
                    guideBridge.resolveNode("failureMsg").visible = true; 
                    guideBridge.resolveNode("failureMsg").value = "<p><span class=\"redColorText\"><b>There was an error in processing your request. Ensure selected formId PDFs available on WebOrdering. If the error persists, contact your administrator or Support Team.</b></span></p>";
                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                }
                                
            };
        }
    });
}

function readFile(){ 
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');  
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    today = mm + "-" +dd +"-" + yyyy;
    var planIDBroc = guideBridge.resolveNode("planID").value;
    var fileName = planIDBroc+"#"+selectedLobBrouchure+" "+productSeriesSelected+"|"+situsSelectedBrochure+"|"+languageSelectedBrochure+"|"+today;
    guideBridge.resolveNode("failureMsg").visible = false;
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
        success: function (guideResultObject) {
            var req = new XMLHttpRequest();

            req.open("POST", "/bin/BrochureAssemblePDF", true);
            req.responseType = "blob";
            var postParameters = new FormData();
            postParameters.append("docId", docId);
            postParameters.append("mode", "readFile");
            postParameters.append("formNumber", currentReorderData);
            postParameters.append("product", selectedLobBrouchure);
            postParameters.append("series", productSeriesSelected);
            postParameters.append("language", guideBridge.resolveNode('language').value);
            postParameters.append("documentNames", guideBridge.resolveNode('selectedDocs').value);
            req.send(postParameters);
            req.onreadystatechange = function () {

                if (req.readyState == 4 && req.status == 200) {
                    guideBridge.resolveNode("failureMsg").visible = false;
                    // var blob = new Blob([this.response], {
                    //     type: "application/pdf"
                    // }),
                    // newUrl = URL.createObjectURL(blob);
                    // window.open(newUrl, "_blank", "menubar=yes,resizable=yes,scrollbars=yes");
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
                    guideBridge.resolveNode("failureMsg").visible = true;
                    guideBridge.resolveNode("failureMsg").value = "<p><span class=\"redColorText\"><b>Fail to read file from FileNet server.</b></span></p>";                   
                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                }
                
            };
        }
    });
}

//selectedDocs
function brochureCheckbox(){
    var selectedDocsArray = [];
    var repeatrow = guideBridge.resolveNode("disclosureRow");

    for (var i = 0; i < repeatrow.instanceManager.instanceCount; i++) {

        if(repeatrow._instanceManager._instances[i].RowCheckbox.value == 0){
            selectedDocsArray.push(repeatrow._instanceManager._instances[i].docName.value);
        }
    }
    guideBridge.resolveNode("selectedDocs").value = selectedDocsArray.join(",");
}