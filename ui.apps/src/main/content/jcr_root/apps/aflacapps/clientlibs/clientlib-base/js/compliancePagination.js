var presentPage = 1;
var countPerEachPage = 10;
var countOfPages = 0;

function fillData() {
	countPerEachPage = 10;
  if (responseFetch["compliance-verbiage-list"].length > 0) {
      if (responseFetch["compliance-verbiage-list"].length < 10){
		countPerEachPage = responseFetch["compliance-verbiage-list"].length;
      }
    guideBridge.resolveNode("noDataReceived").visible = false;
    guideBridge.resolveNode("tableCompliance").visible = true;
    var repeatrow = guideBridge.resolveNode("rowComplianceTable");
    for (var i = 0; i < countPerEachPage; i++) {
      if (i != 0) {
        
        repeatrow.instanceManager.addInstance(); //add panelÂ 
      }
    }
    
    
    // 

    loadMyPaginationList();

  }
  else {
    guideBridge.resolveNode("noDataReceived").visible = true;
    guideBridge.resolveNode("tableCompliance").visible = false;
    guideBridge.resolveNode("noDataReceived").value = "No Language has been supplied with entered combination";
  }
}


//function for creating how many how many number per each page
function getCountOfPages() {
  countOfPages = Math.ceil(responseFetch["compliance-verbiage-list"].length / countPerEachPage);
}

function validatePageCount() {
  getCountOfPages();
  
  guideBridge.resolveNode('next').enabled = presentPage == countOfPages ? false : true;
  guideBridge.resolveNode("previous").enabled = presentPage == 1 ? false : true;
    guideBridge.resolveNode('last').enabled = presentPage == countOfPages ? false : true;
  guideBridge.resolveNode("first").enabled = presentPage == 1 ? false : true;
}

//function for moving to next page
function getNextPage() {
  presentPage += 1;
  //
  loadMyPaginationList();

}
//function for moving previous page
function getPreviousPage() {
  presentPage -= 1;
  //
  loadMyPaginationList();
}
//function for moving to first page
function getFirstPage() {
  presentPage = 1;
  //
  loadMyPaginationList();
}
//function for moving last page
function getLastPage() {
  presentPage = countOfPages;
  //
  loadMyPaginationList();
}

function displayPageNo(){
	guideBridge.resolveNode('pageNo').value = "Page " + presentPage + " of " +countOfPages;
}
//function for creating how to move between the pages
function loadMyPaginationList() {
  validatePageCount();
  displayPageNo();  
  var repeatPanel2 = guideBridge.resolveNode("rowComplianceTable");
  for (var k = 0; k < countPerEachPage; k++) {
    repeatPanel2.instanceManager.instances[k].visible = true;
  }

  //
  var start = ((presentPage - 1) * countPerEachPage);
  //
  var end = start + countPerEachPage;
  //
  responseFetchSplit = responseFetch["compliance-verbiage-list"].slice(start, end);
  

  len = responseFetchSplit.length;
  var j = 0;

  var maxHeight = 0;
  for (var i = 0; i < countPerEachPage; i++) {

    //
    document.querySelectorAll(".tableCompliance table")[0].style.marginLeft = "0px";
    document.querySelectorAll(".tableCompliance table")[0].style.marginRight = "0px";
    var tableQuestionCol = document.querySelectorAll(".tableCompliance table .question textarea");
    tableQuestionCol[i].style.border = "none";
    tableQuestionCol[i].style.outline = "none";
    tableQuestionCol[i].style.overflow = "hidden";
    tableQuestionCol[i].style.height = "auto";

    tableQuestionCol[i].style.resize = "none";

    var editButton = document.querySelectorAll(".tableCompliance table .edit button");
    editButton[i].style.background = "url(../../../../../content/dam/formsanddocuments-themes/aflacapps/canvas-3-0/assets/Table-Edit-Default.svg) center center / 1.2rem 1.2rem no-repeat";

    //state
    document.querySelectorAll(".tableCompliance table .state input")[i].style.outline = "none";
    document.querySelectorAll(".tableCompliance table .state input")[i].style.textOverflow = "ellipsis";
    document.querySelectorAll(".tableCompliance table .state input")[i].style.whiteSpace = "nowrap";
    document.querySelectorAll(".tableCompliance table .state input")[i].style.overflow = "hidden";

    //lob
    document.querySelectorAll(".tableCompliance table .lob input")[i].style.outline = "none";
    document.querySelectorAll(".tableCompliance table .lob input")[i].style.textOverflow = "ellipsis";
    document.querySelectorAll(".tableCompliance table .lob input")[i].style.whiteSpace = "nowrap";
    document.querySelectorAll(".tableCompliance table .lob input")[i].style.overflow = "hidden";

    //type
    document.querySelectorAll(".tableCompliance table .type input")[i].style.outline = "none";

    //label
    document.querySelectorAll(".tableCompliance table .label input")[i].style.outline = "none";
    document.querySelectorAll(".tableCompliance table .label input")[i].style.textOverflow = "ellipsis";
    document.querySelectorAll(".tableCompliance table .label input")[i].style.whiteSpace = "nowrap";
    document.querySelectorAll(".tableCompliance table .label input")[i].style.overflow = "hidden";

    //question //table add
    var verbiageType = "";
    if (responseFetchSplit[i]["verbiage-type"].value == 'Question' || responseFetchSplit[i]["verbiage-type"].value == 'Questions') {
      verbiageType = "Q";
    }
    else if (responseFetchSplit[i]["verbiage-type"].value == 'Affirmation' || responseFetchSplit[i]["verbiage-type"].value == 'Affirmations') {
      verbiageType = "A";
    }
    else if (responseFetchSplit[i]["verbiage-type"].value == 'Health Question' || responseFetchSplit[i]["verbiage-type"].value == 'Health Questions') {
      verbiageType = "HQ";
    }
    else if (responseFetchSplit[i]["verbiage-type"].value == 'Disclosure' || responseFetchSplit[i]["verbiage-type"].value == 'Disclosures') {
      verbiageType = "D";
    }
    else {
      verbiageType = responseFetchSplit[i]["verbiage-type"].value;
    }
    var quesText = responseFetchSplit[i]["compliance-text"];
    var quesTextLength = quesText.length;

    if (quesText.includes('\n') == true) {
      var quesTextSplitArray = quesText.split('\n');
      var lenQuesSplitText = quesTextSplitArray.length;
      var rowNum = Math.ceil((quesTextLength) / 55);
      if (rowNum > lenQuesSplitText) {
        maxHeight = rowNum + 2;
      }
      else {
        maxHeight = lenQuesSplitText + 2;
      }
    }
    else {
      var rowNum = Math.ceil((quesTextLength) / 55);
      maxHeight = rowNum + 1;
    }

    tableQuestionCol[i].rows = maxHeight;


    repeatPanel2.instanceManager.instances[j].question.value = "[" + verbiageType + "] " + responseFetchSplit[i]["compliance-text"];

    //type
    var dynIDType = repeatPanel2.instanceManager.instances[i].question.id;
    var dynaLabIDType = "#" + dynIDType + "_guideFieldShortDescription > p";
    var typeListForThisQuestionArray = [];
    var typeListTooltip = "";

    typeListForThisQuestionArray.push(responseFetchSplit[i]["verbiage-type"].value);
    typeListTooltip = typeListForThisQuestionArray.toString();

    $(dynaLabIDType).html(typeListTooltip);
    //type end

    var lobListForThisQuestionArray = [];
    var lobListForThisQuestion = "";

    for (var x = 0; x < responseFetchSplit[i].lob.length; x++) {
      //
      lobListForThisQuestionArray.push(responseFetchSplit[i]["lob"][x].value);
    }

    lobListForThisQuestion = lobListForThisQuestionArray.toString();
    repeatPanel2.instanceManager.instances[j].lob.value = lobListForThisQuestion;

    //lob tooltip
    var dynIDlobTooltip = repeatPanel2.instanceManager.instances[i].lob.id;
    var dynaLabIDlobTooltip = "#" + dynIDlobTooltip + "_guideFieldShortDescription > p";
    // 
    $(dynaLabIDlobTooltip).html(lobListForThisQuestion);
    //lob end

    var stateListForThisQuestionArray = [];
    var stateListForThisQuestion = "";

    for (var x = 0; x < responseFetchSplit[i].state.length; x++) {
      stateListForThisQuestionArray.push(responseFetchSplit[i]["state"][x].value);
    }

    if (stateListForThisQuestionArray.includes('All') == true) {
      stateListForThisQuestion = "All";
    }
    else {
      stateListForThisQuestion = stateListForThisQuestionArray.toString();
    }

    repeatPanel2.instanceManager.instances[j].state.value = stateListForThisQuestion;

    //state tooltip
    var dynIDStateTooltip = repeatPanel2.instanceManager.instances[i].state.id;
    var dynaLabIDStateTooltip = "#" + dynIDStateTooltip + "_guideFieldShortDescription > p";

    var state = "";
    state = stateListForThisQuestionArray.toString();
    $(dynaLabIDStateTooltip).html(state);
    //state tooltip end

    repeatPanel2.instanceManager.instances[j].label.value = responseFetchSplit[i].label.value;

    var dynIDlabelTooltip = repeatPanel2.instanceManager.instances[i].label.id;
    var dynaLabIDlabelTooltip = "#" + dynIDlabelTooltip + "_guideFieldShortDescription > p";
    $(dynaLabIDlabelTooltip).html(responseFetchSplit[i].label.value);

    repeatPanel2.instanceManager.instances[j].type.value = responseFetchSplit[i]["verbiage-type"].value;

    repeatPanel2.instanceManager.instances[j].recoredId.value = responseFetchSplit[i]["record-id"];

    // status
    var dynIDstatusTooltip = repeatPanel2.instanceManager.instances[i].status.id;
    var dynaLabIDstatusTooltip = "#" + dynIDstatusTooltip + "_guideFieldShortDescription > p";

    if (responseFetchSplit[i]["active-status"] == true) {
      repeatPanel2.instanceManager.instances[j].status.value = "Active";
      $(dynaLabIDstatusTooltip).html("Active for Case Build automation");
    }
    else {
      repeatPanel2.instanceManager.instances[j].status.value = "Disabled";
      $(dynaLabIDstatusTooltip).html("Disabled for Case Build automation");
    }

    document.querySelectorAll("#guideContainer-rootPanel-panel-table-HeaderRow-headerItem1668082478644___guide-item")[0].style.width = "11%";
    document.querySelectorAll("#guideContainer-rootPanel-panel-table-HeaderRow-headerItem1668082485197___guide-item")[0].style.width = "19%";
    document.querySelectorAll("#guideContainer-rootPanel-panel-table-HeaderRow-headerItem1668082491809___guide-item")[0].style.width = "15%";
    document.querySelectorAll("#guideContainer-rootPanel-panel-table-HeaderRow-headerItem1668165485436___guide-item")[0].style.width = "4%";
    document.querySelectorAll("#guideContainer-rootPanel-panel-table-HeaderRow-headerItem1673856784288___guide-item")[0].style.width = "9%";

    if (len < countPerEachPage) {
      var diff = countPerEachPage - len;
      var last = countPerEachPage - 1;
      for (var k = 0; k < diff; k++) {
        repeatPanel2.instanceManager.instances[last].question.value = null;
        repeatPanel2.instanceManager.instances[last].state.value = null;
        repeatPanel2.instanceManager.instances[last].lob.value = null;
        repeatPanel2.instanceManager.instances[last].type.value = null;
        repeatPanel2.instanceManager.instances[last].label.value = null;
        repeatPanel2.instanceManager.instances[last].status.value = null;
        repeatPanel2.instanceManager.instances[last].recoredId.value = null;
        repeatPanel2.instanceManager.instances[last].visible = false;
        last--;
      }
    }

    j++;
  }
}