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
var selectedStateIDEdit = [];
var loader;

var LOBseriesBrochure = [
    {
        "LOB": "Accident,Accident-High,Accident-Mid,Accident-Low,Accident-High-LT,Accident-Mid-LT,Accident-Low-LT",
        "Series": ["7700", "7800", "70000"]
    }, {
        "LOB": "Hospital Indemnity,Hospital Indemnity-High,Hospital Indemnity-Mid,Hospital Indemnity-Low,Hospital Indemnity-High-LT,Hospital Indemnity-Mid-LT,Hospital Indemnity-Low-LT",
        "Series": ["8500", "8800", "80000","8500--Revised"]
    }, {
        "LOB": "BenExtend,BenExtend-High,BenExtend-Mid,BenExtend-Low,BenExtend-High-LT,BenExtend-Mid-LT,BenExtend-Low-LT",
        "Series": ["81000"]
    }, {
        "LOB": "Critical Illness,Critical Illness-With Cancer,Critical Illness-Without Cancer,Critical Illness-With Health Screening Benefit,Critical Illness-Without Health Screening Benefit",
        "Series": ["20000", "2100", "2800", "21000", "22000"]
    }, {
        "LOB": "Dental",
        "Series": ["1100"]
    }, {
        "LOB": "Disability",
        "Series": ["5000", "50000"]
    }, {
        "LOB": "Whole Life",
        "Series": ["9800", "60000"]
    }, {
        "LOB": "Term Life",
        "Series": ["9100"]
    }, {
        "LOB": "Term to 120",
        "Series": ["93000"]
    }, 
    ];

function onInitializePageBrouchureM() {
    var lobsInSelect = ['Accident', 'Critical Illness', 'Hospital Indemnity', 'Dental', 'Disability', 'BenExtend', 'Whole Life', 'Term Life', 'Term to 120'];
    guideBridge.resolveNode('lineOfBusinessM').items = lobsInSelect;
}

function onChangingLobM(value) {
    selectedLobBrouchure = value;

    for (var i = 0; i < LOBseriesBrochure.length; i++) {
        if (LOBseriesBrochure[i].LOB.includes(selectedLobBrouchure)) {
            productSeries = LOBseriesBrochure[i].Series;
        }
    }

    guideBridge.resolveNode('ProductSeriesM').visible = false;
    guideBridge.resolveNode('ProductDesignTableM').visible = false;
    guideBridge.resolveNode('editSection').visible = false;
    guideBridge.resolveNode('errorPanel').visible = false;
    guideBridge.resolveNode('situsStatesTbM').value = null;
    guideBridge.resolveNode('languageM').visible = false;
    guideBridge.resolveNode('languageM').value = null;
    guideBridge.resolveNode('Broc_Fetch').visible = false;
    guideBridge.resolveNode('ProductSeriesM').items = [];
    guideBridge.resolveNode('ProductSeriesM').value = null;
        
    guideBridge.resolveNode('ProductSeriesM').items = productSeries;
    setTimeout(function () {
        guideBridge.resolveNode('ProductSeriesM').visible = true;
    }, 100);
}

function onChangingProductSeriesM(value) {
    productSeriesSelected = value;
    guideBridge.resolveNode('editSection').visible = false;
    guideBridge.resolveNode('ProductDesignTableM').visible = false;
    guideBridge.resolveNode('situsStatesTbM').visible = false;
    guideBridge.resolveNode('errorPanel').visible = false;
    guideBridge.resolveNode('situsStatesTbM').value = null;
    guideBridge.resolveNode('Broc_Fetch').visible = false;
    guideBridge.resolveNode('languageM').visible = false;
    guideBridge.resolveNode('languageM').value = null;
    guideBridge.resolveNode('situsStatesTbM').visible = true;
}

function autoCompleteSitusM() {
    var valueOnChange = "";
    var valueOnClick = "";

    $(document).on('change', '.situsStatesTbM input', function () {
        guideBridge.resolveNode('Broc_Fetch').visible = false;
        document.querySelectorAll('.languageM select')[0].value=null;
        valueOnChange = this.value;

        if (this.value == "" || this.value == undefined || this.value == null) {

            guideBridge.resolveNode('languageM').value = null;
            guideBridge.resolveNode('languageM').visible = false;
        }
        else {
            guideBridge.resolveNode('languageM').visible = true;
            guideBridge.resolveNode('languageM').value = null;
            guideBridge.resolveNode('errorPanel').visible = false;
        }
        situsSelectedBrochure = valueOnChange;
    });

    $(document).on('click', '.situsStatesTbM input', function () {
        valueOnClick = this.value;
        $(".situsStatesTbM input").autocomplete({
            minLength: 0,
            source: situsStates,
            delay: 0,
            select: function (event, ui) {
                event.stopPropagation();
                situsSelectedBrochure = ui.item.label;
                if (situsBrochure.includes(situsSelectedBrochure) != true) {
                    guideBridge.resolveNode('Broc_Fetch').visible = false;
                    guideBridge.resolveNode('errorPanel').visible = false;
                    guideBridge.resolveNode('languageM').value = null;
                    document.querySelectorAll('.languageM select')[0].value = null;
                    $(function () {
                        $('.situsStatesTbM input').blur();
                    });
                    //guideBridge.resolveNode('errorPanel').visible=true;
                }
                else {
                    $(function () {
                        $('.situsStatesTbM input').blur();
                    });
                    guideBridge.resolveNode('Broc_Fetch').visible=true;
                    guideBridge.resolveNode('errorPanel').visible = false;
                    guideBridge.resolveNode('languageM').visible = true;
                }
                onChangingSitusM();
            }
        });
    });
}

function onChangingSitusM() {
    guideBridge.resolveNode('editSection').visible = false;
    guideBridge.resolveNode('ProductDesignTableM').visible = false;
    coverageAllData = [];
    guideBridge.resolveNode('languageM').value = null;
    document.querySelectorAll('.languageM select')[0].value=null;
    guideBridge.resolveNode('Broc_Fetch').visible = false;
}

function validBrochureSitusM(situs) {
    var field = guideBridge.resolveNode("situsStatesTbM");
    if (!situsStates.includes(situs)) {
        guideBridge.resolveNode("situsStatesTbM").value = '';
        document.querySelectorAll(".situsStatesTbM input")[0].value = '';
        situsSelectedBrochure = null;
        document.getElementById(field.id).className = "guideFieldNode guideTextBox situsStatesTbM defaultFieldLayout af-field-filled validation-failure";
        setTimeout(function() {
            var alert = document.getElementById(field.id).children[2];
            alert.setAttribute('role', 'alert');
            var alertid = "#" + alert.id;
            $(alertid).html("This is not a valid Situs.");
        }, 10);
    } else {
        document.getElementById(field.id).className = "guideFieldNode guideTextBox situsStatesTbM defaultFieldLayout af-field-filled validation-success ";
    }
}

function onChangeingLanguageM(value) {
    languageSelectedBrochure = value;
    guideBridge.resolveNode('editSection').visible = false;
    guideBridge.resolveNode('ProductDesignTableM').visible = false;
    coverageAllData = [];

    guideBridge.resolveNode('Broc_Fetch').visible=false;
    disableFetch();
    if (value !== undefined && value !== "" && value !== null) {
        //guideBridge.resolveNode('Broc_Fetch').visible = true;
        
    }
}
function disableFetch(){
    if((selectedLobBrouchure==null)||(productSeriesSelected==null)||(situsSelectedBrochure==null)||(languageSelectedBrochure==null)){
        guideBridge.resolveNode('Broc_Fetch').visible = false;
    }
    else{
        guideBridge.resolveNode('Broc_Fetch').visible = true;
    }
}

function onClickFetch(){
    //console.log(selectedLobBrouchure," ",productSeriesSelected," ",situsSelectedBrochure," ",languageSelectedBrochure);
    guideBridge.resolveNode('Broc_Fetch').visible = true;
    startLoader();
    $.ajax({
        url: "/bin/BrochureMaintenance",
        type: 'GET',
        data: { lob: selectedLobBrouchure, series: productSeriesSelected, situs: situsSelectedBrochure, language: languageSelectedBrochure },
        dataType: 'json', // added data type
        success: function (resdata) {
            coverageAllData = resdata;
            if(coverageAllData.length !=0){
                resetProductTable();
                guideBridge.resolveNode('errorPanel').visible = false;
                guideBridge.resolveNode("Details").visible = true;
                guideBridge.resolveNode('ProductDesignTableM').visible = true;
                guideBridge.resolveNode('editSection').visible = false;
                var row = guideBridge.resolveNode("productDesignRow1M");
                var k=0;
                for(var i=0;i<coverageAllData.length;i++){
                    if (k != 0)
                        row.instanceManager.addInstance(true);
                    row.instanceManager.instances[k].pdCoverageNameM.value = coverageAllData[i]["coverage-name"];
                    var dynIDPlanNameTooltip = row.instanceManager.instances[k].pdCoverageNameM.id;
                    var dynaLabIDPlanNameTooltip = "#" + dynIDPlanNameTooltip + "_guideFieldShortDescription > p";
                    $(dynaLabIDPlanNameTooltip).html(coverageAllData[i]["coverage-name"]);
                    row.instanceManager.instances[k].pdBrochureIdM.value = coverageAllData[i]["form-number"];
                    row.instanceManager.instances[k].pdCoverageLevelM.value = coverageAllData[i]["coverage-level"];
                    row.instanceManager.instances[k].situsArray.value = coverageAllData[i]["situs"];
                    row.instanceManager.instances[k].primaryKey.value = coverageAllData[i]["brochure-id"];
                    k++;
                }
            }
            else{
                guideBridge.resolveNode('errorPanel').visible = true;
            }
            stopLoader();
        }
            
      });
}

function onClickEdit(bid,lob,series,situs,language,cn,cl,pk){
    situs=situs.split(',');
    selectedStateIDEdit = [];
    guideBridge.resolveNode("maintenanceSuccess").visible = false;
    guideBridge.resolveNode("maintenanceFail").visible = false;
    guideBridge.resolveNode("Broc_Fetch").visible = false;
    guideBridge.resolveNode("Details").visible = false;
    guideBridge.resolveNode("editSection").visible = true;
    guideBridge.resolveNode("ProductDesignTableM").visible = false;
    guideBridge.resolveNode("primaryKey_edit").value = pk;
    guideBridge.resolveNode("brochureID_edit").value = bid;
    guideBridge.resolveNode("LOB_edit").value = lob;
    guideBridge.resolveNode("series_edit").value = series;
    //guideBridge.resolveNode("brochureID_edit").value = situs;
    guideBridge.resolveNode("language_edit").value = language;
    guideBridge.resolveNode("coverageName_edit").value = cn;
    guideBridge.resolveNode("coverageLevel_edit").value = cl;

    var row = guideBridge.resolveNode("situsPanel");
    var delCount = row.instanceManager.instanceCount;
    

    if(delCount>1){
        for (var m = 0; m <= delCount; m++) {
            row.instanceManager.removeInstance(1);
        }
    }
    for(var i=0;i<situsStates.length;i++){
        if (i != 0)
            row.instanceManager.addInstance(true);
        var divDynamicID = row._instanceManager.instances[i].cbState.id;
        var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
        $(dynID).html(situsStates[i]);

        document.querySelectorAll(".guideCheckBoxGroup.cbState")[i].style.paddingTop = "0px";
        document.querySelectorAll(".guideCheckBoxGroup.cbState")[i].style.marginTop = "0px";
        document.querySelectorAll(".guideCheckBoxGroup.cbState")[i].style.marginBottom = "0px";
    }
    var cbState = document.querySelectorAll('.cbState input[type="checkbox"]');
    var itemCB = guideBridge.resolveNode('situsPanel');
    for (var x = 0; x<situsStates.length; x++) {
        itemCB._instanceManager._instances[x].children[0].value = "-100";
    }
    
    for(var i=0;i<situsStates.length;i++){
        for(var j=0;j<situs.length;j++){
            if(situsStates[i]==situs[j]){
                itemCB._instanceManager._instances[i].children[0].value = "0";
            }
        }
    }
    selectedStateIDEdit = situs;
    var StateIDEdit;
    $(document).off().on('change', '.cbState input[type="checkbox"]', function () {

        StateIDEdit = "#" + this.id;
        var StateLabelChanged = document.querySelectorAll(StateIDEdit)[0].parentElement.parentElement.children[1].innerText;
        
        for (var j = 0; j < situsStates.length; j++) {
            if (situsStates[j] == StateLabelChanged) {
              if (document.querySelectorAll(StateIDEdit)[0].checked == true) {
      
                if (selectedStateIDEdit.includes(StateLabelChanged) != true) {
                  selectedStateIDEdit.push(StateLabelChanged);
                }
              }
              else if (document.querySelectorAll(StateIDEdit)[0].checked == false) {
                indexId = selectedStateIDEdit.indexOf(StateLabelChanged);
                if (indexId >= 0) {
                  selectedStateIDEdit.splice(indexId, 1);
                }
              }
            }
          }
        //console.log("selectedStateIDEdit: ", selectedStateIDEdit);
        if(selectedStateIDEdit==[] || selectedStateIDEdit=="" || selectedStateIDEdit.length==0){
            guideBridge.resolveNode("saveButton").enabled = false;
        }else{
            guideBridge.resolveNode("saveButton").enabled = true;
        }
    });

    
}

function onClickSave(){
    guideBridge.resolveNode("maintenanceSuccess").visible = false;
    guideBridge.resolveNode("maintenanceFail").visible = false;
    var primarKey = guideBridge.resolveNode("primaryKey_edit").value;
    var brochureID = guideBridge.resolveNode("brochureID_edit").value;
    var lob = guideBridge.resolveNode("LOB_edit").value;
    var series = guideBridge.resolveNode("series_edit").value;
    var language = guideBridge.resolveNode("language_edit").value;
    var coverageName = guideBridge.resolveNode("coverageName_edit").value;
    var CoverageLevel = guideBridge.resolveNode("coverageLevel_edit").value;
    var planDesign = coverageName+" - "+CoverageLevel;

    var submitData = {
        "brochure-id": primarKey,
        "lob": lob,
        "series": series,
        "language": language,
        "situs": selectedStateIDEdit,
        "form-number": brochureID,
        "coverage-name": coverageName,
        "coverage-level": CoverageLevel,
        "plan-design": planDesign
    }
    //console.log("submitData: ",submitData);
    const formData = new FormData();
    formData.append("data", JSON.stringify(submitData));
    var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/bin/BrochureMaintenance", true);
    xhttp.send(formData);
    startLoader();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            var x = JSON.parse(xhttp.responseText);
            //console.log("RESP Edit: ", x);
            if(x["status"] == true) {
                stopLoader();
                guideBridge.resolveNode("maintenanceSuccess").visible = true;
                setTimeout(function() {
                    onClickFetch();
                }, 5000);
            }
            else{
                stopLoader();
                guideBridge.resolveNode("maintenanceFail").visible = true;
            }
        }
    }
}

function resetProductTable() {
    guideBridge.resolveNode("productDesignRow1M").instanceManager.instances[0].pdCoverageNameM.value = "";
    guideBridge.resolveNode("productDesignRow1M").instanceManager.instances[0].pdCoverageLevelM.value = "";
    guideBridge.resolveNode("productDesignRow1M").instanceManager.instances[0].pdBrochureIdM.value = "";
    guideBridge.resolveNode("productDesignRow1M").instanceManager.instances[0].situsArray.value = "";
    guideBridge.resolveNode("productDesignRow1M").instanceManager.instances[0].primaryKey.value = "";

    var dynIDPlanNameTooltip = guideBridge.resolveNode('productDesignRow1M').instanceManager.instances[0].pdCoverageNameM.id;
    var dynaLabIDPlanNameTooltip = "#" + dynIDPlanNameTooltip + "_guideFieldShortDescription > p";
    $(dynaLabIDPlanNameTooltip).html(null);

    deleteProductRows();
}
function deleteProductRows() {
    var row1 = guideBridge.resolveNode("productDesignRow1M");
    var delCount = row1.instanceManager.instanceCount;
    for (var m = 0; m <= delCount; m++) {
        row1.instanceManager.removeInstance(1);
    }
}

function startLoader() {
    loader = document.createElement('div');
    loader.setAttribute('id', 'previewLoader');
    loader.setAttribute('class', 'loader');
    loader.rel = 'stylesheet';
    loader.type = 'text/css';
    loader.href = '/css/loader.css';
    document.getElementsByTagName('BODY')[0].appendChild(loader);
    document.getElementById("guideContainerForm").style.filter = "blur(10px)";
    document.getElementById("guideContainerForm").style.pointerEvents = "none";
}

function stopLoader() {
    document.getElementById("guideContainerForm").style.filter = "blur()";
    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
    loader.setAttribute('class', 'loader-disable');
}

