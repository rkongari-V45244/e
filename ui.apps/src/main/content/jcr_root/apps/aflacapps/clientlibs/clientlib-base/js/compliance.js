function closePanel() {
  var repeatPanel2 = guideBridge.resolveNode("rowComplianceTable");
  
  //Reset Panels
  repeatPanel2.instanceManager.addInstance();
  var currentCount2 = repeatPanel2.instanceManager.instanceCount;
  //table add
  for (var m = 0; m < currentCount2; m++) {
     //console.log("m is: ", m);
    if (m != 0) {
      repeatPanel2.instanceManager.removeInstance(0);
    }
  }

}

function addPanel() {

  console.log("responseFetch: ", responseFetch);

  //panel identification
  var repeatPanel2 = guideBridge.resolveNode("rowComplianceTable");

  var maxHeight = 0;
  // //console.log("dummyData.question.length: ", dummyData.question.length);
  var len = responseFetch["compliance-verbiage-list"].length; //dummyData.question.length ["compliance-verbiage-list"]

  if(len >0){
    guideBridge.resolveNode("noDataReceived").visible = false;
    guideBridge.resolveNode("tableCompliance").visible = true;
  }
  else{
    guideBridge.resolveNode("noDataReceived").visible = true;
    guideBridge.resolveNode("tableCompliance").visible = false;
    guideBridge.resolveNode("noDataReceived").value = "No Language has been supplied with entered combination";
  }
    var addRows = len -1;
    guideBridge.resolveNode("rowComplianceTable").instanceManager.addInstances({instanceCount:addRows});

  for (var k = 0; k < len; k++) {
    //  console.log("K is: ", k);

    //console.log("Question textarea");
    document.querySelectorAll(".tableCompliance table")[0].style.marginLeft = "0px";
    document.querySelectorAll(".tableCompliance table")[0].style.marginRight = "0px";
    var tableQuestionCol = document.querySelectorAll(".tableCompliance table .question textarea");
    
    //console.log("tableQuestionCol", tableQuestionCol);
     tableQuestionCol[k].style.border = "none";
    tableQuestionCol[k].style.outline = "none";
    // tableQuestionCol[k].style.cursor = "none";
    tableQuestionCol[k].style.overflow = "hidden";
    tableQuestionCol[k].style.height = "auto";


    tableQuestionCol[k].style.resize= "none";
    // tableQuestionCol[k].style.background= "rgb(236, 250, 255)";
    // tableQuestionCol[k].style.fontFamily= "Arial"; 
    // tableQuestionCol[k].style.fontSize= "14px"; 
    // tableQuestionCol[k].style.fontWeight= "400";  

    var editButton = document.querySelectorAll(".tableCompliance table .edit button");
    //console.log("editButton: ", editButton);
    editButton[k].style.background = "url(../../../../../content/dam/formsanddocuments-themes/aflacapps/canvas-3-0/assets/Table-Edit-Default.svg) center center / 1.2rem 1.2rem no-repeat";
  

    //state
  //   document.querySelectorAll(".tableCompliance table .state input")[k].style.border = "none";
    document.querySelectorAll(".tableCompliance table .state input")[k].style.outline = "none";
    document.querySelectorAll(".tableCompliance table .state input")[k].style.textOverflow = "ellipsis";
    document.querySelectorAll(".tableCompliance table .state input")[k].style.whiteSpace = "nowrap";
    document.querySelectorAll(".tableCompliance table .state input")[k].style.overflow= "hidden";
  //   document.querySelectorAll(".tableCompliance table .state input")[k].style.cursor = "none";
  //   document.querySelectorAll(".tableCompliance table .state input")[k].style.padding = "0px";
     //console.log("state");
    //lob
  //   document.querySelectorAll(".tableCompliance table .lob input")[k].style.border = "none";
    document.querySelectorAll(".tableCompliance table .lob input")[k].style.outline = "none";
    document.querySelectorAll(".tableCompliance table .lob input")[k].style.textOverflow = "ellipsis";
    document.querySelectorAll(".tableCompliance table .lob input")[k].style.whiteSpace = "nowrap";
    document.querySelectorAll(".tableCompliance table .lob input")[k].style.overflow= "hidden";
  //   document.querySelectorAll(".tableCompliance table .lob input")[k].style.cursor = "none";
  //    //console.log("lob");
  //   //type
  //   document.querySelectorAll(".tableCompliance table .type input")[k].style.border = "none";
    document.querySelectorAll(".tableCompliance table .type input")[k].style.outline = "none";
  //   document.querySelectorAll(".tableCompliance table .type input")[k].style.cursor = "none";
  //   //label
  //   document.querySelectorAll(".tableCompliance table .label input")[k].style.border = "none";
    // console.log("Label tag: ", document.querySelectorAll(".tableCompliance table .label input")[k]);
    document.querySelectorAll(".tableCompliance table .label input")[k].style.outline = "none";
    document.querySelectorAll(".tableCompliance table .label input")[k].style.textOverflow = "ellipsis";
    document.querySelectorAll(".tableCompliance table .label input")[k].style.whiteSpace = "nowrap";
    document.querySelectorAll(".tableCompliance table .label input")[k].style.overflow= "hidden";
  
  //   document.querySelectorAll(".tableCompliance table .label input")[k].style.cursor = "none";
     //console.log("label");
    //question //table add
    var verbiageType = "";
    if(responseFetch["compliance-verbiage-list"][k]["verbiage-type"].value == 'Question' || responseFetch["compliance-verbiage-list"][k]["verbiage-type"].value == 'Questions') {
      verbiageType = "Q";
    }
    else if(responseFetch["compliance-verbiage-list"][k]["verbiage-type"].value == 'Affirmation' || responseFetch["compliance-verbiage-list"][k]["verbiage-type"].value == 'Affirmations'){
      verbiageType = "A";
    }
    else if(responseFetch["compliance-verbiage-list"][k]["verbiage-type"].value == 'Health Question' || responseFetch["compliance-verbiage-list"][k]["verbiage-type"].value == 'Health Questions'){
      verbiageType = "HQ";
    }
    else if(responseFetch["compliance-verbiage-list"][k]["verbiage-type"].value == 'Disclosure' || responseFetch["compliance-verbiage-list"][k]["verbiage-type"].value == 'Disclosures') {
      verbiageType = "D";
    }
    else{
      verbiageType = responseFetch["compliance-verbiage-list"][k]["verbiage-type"].value;
    }

    var quesText = responseFetch["compliance-verbiage-list"][k]["compliance-text"]; 
    // console.log("Question text: ", quesText);
    var quesTextLength =quesText.length;

    if(quesText.includes('\n') == true){
      var quesTextSplitArray = quesText.split('\n');
      var lenQuesSplitText = quesTextSplitArray.length;
      var rowNum = Math.ceil((quesTextLength) /55); 
      if(rowNum > lenQuesSplitText){
        maxHeight = rowNum+2;
      }
      else{
        maxHeight  = lenQuesSplitText+2;
      }
    }
    else{
      var rowNum = Math.ceil((quesTextLength) /55);
    
      maxHeight = rowNum+1;
    
    }

     tableQuestionCol[k].rows = maxHeight;
     //console.log("Row Num: ", rowNum);
      //  console.log("Max height: ", maxHeight);
    repeatPanel2.instanceManager.instances[k].question.value = "[" + verbiageType + "] " + responseFetch["compliance-verbiage-list"][k]["compliance-text"]; //dummyData.question[k];
    //console.log("responseFetch[k]['compliance-text']: ", responseFetch["compliance-verbiage-list"][k]["compliance-text"]);
    
     //type
     var dynIDType = repeatPanel2.instanceManager.instances[k].question.id;
     var dynaLabIDType = "#" + dynIDType + "_guideFieldShortDescription > p";
     var typeListForThisQuestionArray = [];
     var typeListTooltip = "";
     
       typeListForThisQuestionArray.push(responseFetch["compliance-verbiage-list"][k]["verbiage-type"].value);
       typeListTooltip = typeListForThisQuestionArray.toString();
    
     $(dynaLabIDType).html(typeListTooltip);
     //type end

    var lobListForThisQuestionArray = [];
    var lobListForThisQuestion = "";
    var cuurentLobArr= responseFetch["compliance-verbiage-list"][k].lob;
    // cuurentLobArr.map(item =>(
    // lobListForThisQuestionArray.push(item.value)
    // )); //remove arrow fun
    for(var x=0; x<responseFetch["compliance-verbiage-list"][k].lob.length; x++) {
      //console.log("x is: ", x);
      lobListForThisQuestionArray.push(responseFetch["compliance-verbiage-list"][k]["lob"][x].value);
    }

    // console.log("lobListForThisQuestionArray: ", lobListForThisQuestionArray);
    lobListForThisQuestion = lobListForThisQuestionArray.toString();
    // console.log("lobListForThisQuestion: ", lobListForThisQuestion);
    repeatPanel2.instanceManager.instances[k].lob.value = lobListForThisQuestion;
    
    //lob tooltip
    var dynIDlobTooltip = repeatPanel2.instanceManager.instances[k].lob.id;
    var dynaLabIDlobTooltip = "#" + dynIDlobTooltip + "_guideFieldShortDescription > p";
    // console.log("LOB tooltip")
    $(dynaLabIDlobTooltip).html(lobListForThisQuestion);
    //lob end

  
    
    var stateListForThisQuestionArray = [];
    var stateListForThisQuestion = "";
  
    for(var x=0; x<responseFetch["compliance-verbiage-list"][k].state.length; x++) {
      //console.log("x is: ", x);
      stateListForThisQuestionArray.push(responseFetch["compliance-verbiage-list"][k]["state"][x].value);
    }

    // var cuurentStateArr= responseFetch["compliance-verbiage-list"][k].state;
    // cuurentStateArr.map(item =>(
    //   stateListForThisQuestionArray.push(item.value)
    // ));

    if(stateListForThisQuestionArray.includes('All') == true){
      stateListForThisQuestion = "All";
    }
    else{
      stateListForThisQuestion = stateListForThisQuestionArray.toString();
    }
    //console.log("stateListForThisQuestion: ", stateListForThisQuestion);
    repeatPanel2.instanceManager.instances[k].state.value = stateListForThisQuestion;

    //state tooltip
    var dynIDStateTooltip = repeatPanel2.instanceManager.instances[k].state.id;
    var dynaLabIDStateTooltip = "#" + dynIDStateTooltip  + "_guideFieldShortDescription > p";
    
    var state = "";
    state = stateListForThisQuestionArray.toString();
    $(dynaLabIDStateTooltip).html(state);
    //state end tooltip

    // console.log("K is: ", k);
    // console.log("responseFetch label: ", repeatPanel2.instanceManager.instances[k].label);
    repeatPanel2.instanceManager.instances[k].label.value = responseFetch["compliance-verbiage-list"][k].label.value;
    // console.log("Setting label tooltip");
    var dynIDlabelTooltip = repeatPanel2.instanceManager.instances[k].label.id;
    var dynaLabIDlabelTooltip = "#" + dynIDlabelTooltip + "_guideFieldShortDescription > p";
    $(dynaLabIDlabelTooltip).html(responseFetch["compliance-verbiage-list"][k].label.value);

    repeatPanel2.instanceManager.instances[k].type.value = responseFetch["compliance-verbiage-list"][k]["verbiage-type"].value;

    //here add for recored id
    repeatPanel2.instanceManager.instances[k].recoredId.value = responseFetch["compliance-verbiage-list"][k]["record-id"];

    // status
    var dynIDstatusTooltip = repeatPanel2.instanceManager.instances[k].status.id;
    var dynaLabIDstatusTooltip = "#" + dynIDstatusTooltip + "_guideFieldShortDescription > p";

    if(responseFetch["compliance-verbiage-list"][k]["active-status"] == true){
      repeatPanel2.instanceManager.instances[k].status.value = "Active";
      tableQuestionCol[k].style.background= "rgb(236, 250, 255)";
      $(dynaLabIDstatusTooltip).html("Active for Case Build automation");
    }
    else {
      repeatPanel2.instanceManager.instances[k].status.value = "Disabled";
      tableQuestionCol[k].style.background= "lightgray";
      $(dynaLabIDstatusTooltip).html("Disabled for Case Build automation");
    }

      // console.log("0");
      document.querySelectorAll("#guideContainer-rootPanel-panel-table-HeaderRow-headerItem1668082478644___guide-item")[0].style.width = "11%";
      // console.log("1");
      document.querySelectorAll("#guideContainer-rootPanel-panel-table-HeaderRow-headerItem1668082485197___guide-item")[0].style.width = "19%";
    //  document.querySelectorAll("#guideContainer-rootPanel-panel-table-HeaderRow-headerItem1668082488763___guide-item")[0].style.width = "9.5%";
    // console.log("2");  
    document.querySelectorAll("#guideContainer-rootPanel-panel-table-HeaderRow-headerItem1668082491809___guide-item")[0].style.width = "15%";
    // console.log("3"); 
    document.querySelectorAll("#guideContainer-rootPanel-panel-table-HeaderRow-headerItem1668165485436___guide-item")[0].style.width = "4%";
    document.querySelectorAll("#guideContainer-rootPanel-panel-table-HeaderRow-headerItem1673856784288___guide-item")[0].style.width = "9%";
    // console.log("4");  
    // document.querySelectorAll("#guideContainer-rootPanel-panel-table-HeaderRow-headerItem1668166681966___guide-item")[0].style.width = "5%";
    //  console.log("End");
  }
  //  setToolTip();
  // styleLabels(); 
}


function styleLabels() {
  // console.log("Style labels");
 var labels = document.querySelectorAll(".guideFieldLabel.top label");
 console.log("labels: ", labels);

document.querySelectorAll(".titleStateInput p")[0].style.padding = "2px 6px";
document.querySelectorAll(".titleStateAdd p")[0].style.padding = "2px 6px";

document.querySelectorAll(".Addtype")[0].style.paddingLeft = "0px";
document.querySelectorAll(".addQuestion")[0].style.paddingLeft = "0px";
document.querySelectorAll(".addQuestion")[0].style.paddingRight = "0px";
document.querySelectorAll(".addLabel")[0].style.paddingRight = "0px";

document.querySelectorAll(".editType")[0].style.paddingLeft = "0px";
document.querySelectorAll(".editQuestion")[0].style.paddingLeft = "0px";
document.querySelectorAll(".editQuestion")[0].style.paddingRight = "0px";
document.querySelectorAll(".editLabel")[0].style.paddingRight = "0px";

console.log("Style label add change");

 for( var i=0; i<labels.length; i++) {
   labels[i].style.background = "#00A7E1";
   labels[i].style.color = "#fff";
  //  labels[i].style.borderRadius = "26px";
   labels[i].style.textAlign = "center";
  //  labels[i].style.width= "77px";
   labels[i].style.padding= "2px 6px";

 }
 
  console.log("0 Label");

 document.querySelectorAll("#guideContainer-rootPanel-panel_18801104-guidecheckbox_copy_1_2053070957___guide-item")[0].style.marginTop="30px";
 document.querySelectorAll("#guideContainer-rootPanel-panel_18801104-guidecheckbox_copy_1___guide-item")[0].style.marginTop="30px";
 document.querySelectorAll("#guideContainer-rootPanel-panel_18801104-guidecheckbox_copy_c_1545859356___guide-item")[0].style.marginTop="30px";
 document.querySelectorAll("#guideContainer-rootPanel-panel_18801104-guidecheckbox_copy_c___guide-item")[0].style.marginTop="30px";
 console.log("1 Label");

 document.querySelectorAll("#guideContainer-rootPanel-panel_18801104-guidedropdownlist_722896294___widget")[0].style.marginTop = "7px";
 document.querySelectorAll("#guideContainer-rootPanel-panel_18801104-guidedropdownlist_722896294___widget")[0].style.padding= "0px 15px";
 document.querySelectorAll("#guideContainer-rootPanel-panel_18801104-guidedropdownlist_722896294___widget")[0].style.height= "32px";
  console.log("2 Label");
 document.querySelectorAll("#guideContainer-rootPanel-panel_1109364196-guidedropdownlist_257471037___widget")[0].style.marginTop = "7px";
 document.querySelectorAll("#guideContainer-rootPanel-panel_1109364196-guidedropdownlist_257471037___widget")[0].style.padding= "0px 15px";
 document.querySelectorAll("#guideContainer-rootPanel-panel_1109364196-guidedropdownlist_257471037___widget")[0].style.height= "32px";
  console.log("3 Label");
 document.querySelectorAll("#guideContainer-rootPanel-panel_1109364196-guidebutton___widget")[0].style.background= "#F89627";
 document.querySelectorAll("#guideContainer-rootPanel-panel_1109364196-guidetextbox___label")[0].style.width = "189px";
 document.querySelectorAll("#guideContainer-rootPanel-panel_1109364196-guidetextbox___label")[0].style.marginBottom = "8px";
 console.log("After Q/A");


//  document.querySelectorAll(".addLobInput .guideFieldLabel.top label")[0].style.width = "12%";

}

function setToolTip()
{
    	var repeatPanel2 = guideBridge.resolveNode("rowComplianceTable");

      // console.log("Count: ", repeatPanel2.instanceManager.instanceCount);
    for (var k = 0; k < repeatPanel2.instanceManager.instanceCount; k++) {

       //state
		    var dynID = repeatPanel2.instanceManager.instances[k].state.id;
        var dynaLabID = "#" + dynID + "_guideFieldShortDescription > p";
        var stateListForThisQuestionArray = [];
        var state = "";
        for(var x=0; x<responseFetch["compliance-verbiage-list"][k].state.length; x++) {
          // console.log("x in tooltip is: ", x);
          stateListForThisQuestionArray.push(responseFetch["compliance-verbiage-list"][k]["state"][x].value);
        }
        state = stateListForThisQuestionArray.toString();
        // console.log("state: ", state);
	    	$(dynaLabID).html(state);
        //state end

        //lob
        var dynIDlob = repeatPanel2.instanceManager.instances[k].lob.id;
        var dynaLabIDlob = "#" + dynIDlob + "_guideFieldShortDescription > p";
        var lobListForThisQuestionArray = [];
        var lobList = "";
        for(var x=0; x<responseFetch["compliance-verbiage-list"][k].lob.length; x++) {
          lobListForThisQuestionArray.push(responseFetch["compliance-verbiage-list"][k]["lob"][x].value);
        }
        lobList = lobListForThisQuestionArray.toString();
        // console.log("loblist: ", lobList);
	    	$(dynaLabIDlob).html(lobList);
        //lob end

        //type
        var dynIDType = repeatPanel2.instanceManager.instances[k].question.id;
        var dynaLabIDType = "#" + dynIDType + "_guideFieldShortDescription > p";
        var typeListForThisQuestionArray = [];
        var typeList = "";
        
          typeListForThisQuestionArray.push(responseFetch["compliance-verbiage-list"][k]["verbiage-type"].value);
        typeList = typeListForThisQuestionArray.toString();
       
	    	$(dynaLabIDType).html(typeList);
        //type end

    }

}

function setCBDynamic() {
  // console.log("Fun 1");
  var stateList = ["All", "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Dist. of Columbia", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas"
  , "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
  "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"];
  var repeatPanel = guideBridge.resolveNode("testCbPanel");
  var count=0;
  for (var x=0; x<stateList.length; x++) {
    count = x;
    if (x != 0) {
      repeatPanel.instanceManager.addInstance(); //add panel
    }
  }
  // console.log("Fun 1 end");
  //  setTitleCB();
    setTimeout(function() {
      //start
      // console.log("Fun 2");
     
      var repeatPanel = guideBridge.resolveNode("testCbPanel");
      console.log("Panel len: ", repeatPanel.instanceManager.instanceCount);
      for (var k = 0; k < repeatPanel.instanceManager.instanceCount; k++) {
        var state = stateList[k]; 
        var divDynamicID = repeatPanel.instanceManager.instances[k].cbTest.id;
        var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
      
        var lab = document.querySelectorAll(dynID);
        // console.log("Lab: ", lab)
        lab[0].style.margin = "0px";
        $(dynID).html(state);
        //setLabel(dynID, state, k);
        document.querySelectorAll(".guideCheckBoxGroup.cbTest")[k].style.paddingTop = "0px";
        document.querySelectorAll(".guideCheckBoxGroup.cbTest")[k].style.marginTop = "0px";
        document.querySelectorAll(".guideCheckBoxGroup.cbTest")[k].style.marginBottom = "0px";
      }
      //end
    }, 100);
}
//testCbPanelAdd
function setCBAddDynamic() {
  console.log("Set CB State");
  var repeatPanel = guideBridge.resolveNode("testCbPanelAdd");
  for (var x=0; x<stateListAll.length; x++) {
    if (x != 0) {
      repeatPanel.instanceManager.addInstance(); //add panel
    }
  }
    setTimeout(function() {
      //start
      console.log("Fun 2");
     
      var repeatPanel = guideBridge.resolveNode("testCbPanelAdd");
      console.log("Panel len: ", repeatPanel.instanceManager.instanceCount);
      for (var k = 0; k < repeatPanel.instanceManager.instanceCount; k++) {
        var state = stateListAll[k].state; 
        var divDynamicID = repeatPanel.instanceManager.instances[k].cbTestAdd.id;
        var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
      
        var lab = document.querySelectorAll(dynID);
        // console.log("Lab: ", lab)
        lab[0].style.margin = "0px";
        $(dynID).html(state);
        //setLabel(dynID, state, k);
        // console.log("CB:",document.querySelectorAll(".guideCheckBoxGroup.cbTestAdd"));
        document.querySelectorAll(".guideCheckBoxGroup.cbTestAdd")[k].style.paddingTop = "0px";
        document.querySelectorAll(".guideCheckBoxGroup.cbTestAdd")[k].style.marginTop = "0px";
        document.querySelectorAll(".guideCheckBoxGroup.cbTestAdd")[k].style.marginBottom = "0px";
      }
      //end
    }, 100);
}
//end





