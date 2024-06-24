function getTypeFromEdit(value) {
    console.log("Value Type: ", value);
    typeEditFinal = value;
  }
  
  function getLabelFromEdit(value) {
    console.log("Value Lab: ", value);
    labelEditFinal = value;
  }
  
  function getQuestionFromEdit(value) {
    console.log("Value Ques: ", value);
    questionEditFinal = value;
  }

var questionEditFinal = "";
var typeEditFinal = "";
var labelEditFinal = "";

var resdataEdit = [];
var typeEdit = []
var lobListEdit= [];
var LobListEditRepeatablePanel = [];
var labelsEdit = [];

var initialQuestion = ""; 
var initialState = []; 
var initialLabel = "";
var initialLabelId = ""; 
var initialLob = []; 
var initialLobId = []; 
var initialType = "";
var initialTypeId = ""; 
var recoredIdFinal = "";
function initialLoadAddEdit(questionInitial , stateInitial, labelInitial, lobInitial, typeInitial, recoredID) {
   console.log("initial load Edit");
   
   recoredIdFinal = recoredID;
     //  console.log("Element ques: ", questionInitial);
  //  initialQuestion = questionInitial;
  //  console.log("Element state: ", stateInitial);
  //  initialState = stateInitial.split(",");
  //  console.log("Element label: ", labelInitial);
  //  initialLabel = labelInitial;
  //  console.log("Element lob: ", lobInitial);
  //  initialLob = lobInitial.split(",");
  //  console.log("Element type: ", typeInitial);
  //  initialType = typeInitial;

    initialLobId = [];
    initialLob = [];

   $.ajax({
    url: "/bin/complianceServlet",
    type: 'GET',
    data: { methodType: "edit", recordId: recoredIdFinal},
    dataType: 'json', // added data type
    success: function (res) {
      resdataEdit = res;
      initialQuestion = resdataEdit["compliance-text"];
      questionEditFinal = resdataEdit["compliance-text"];
      initialLabelId = resdataEdit.label.id;
      initialTypeId = resdataEdit["verbiage-type"].id;

      activeStatusFinal = resdataEdit["active-status"];

      var disableButton = guideBridge.resolveNode("buttonDisable");
      if(activeStatusFinal == false){
        disableButton.value = 0;
      }
      else{
        disableButton.value = 1;
      }
  
      for(var v=0; v<resdataEdit.lob.length; v++){
        initialLobId.push(resdataEdit.lob[v].id);
      }

      var statesFromApi = resdataEdit["state"];
      initialState = [];
      for(var i=0; i<statesFromApi.length; i++){
        initialState.push(statesFromApi[i].value);
      }
      console.log("Initial State: ", initialState);
       selectedStateIDEdit = [];
   for(var x=0; x<stateListAll.length; x++) {
    for(var y=0; y<initialState.length; y++){
      if(initialState[y] == stateListAll[x].state){
        selectedStateIDEdit.push(stateListAll[x].id);
      }
    }
   }
     
  
    LobListEditRepeatablePanel = lobListFromAPI;
    var lobEdit = [];
    lobListEdit= [];
    selectedLobIDEdit = [];
      lobEdit = lobListFromAPI;
        for(var j=0; j<lobEdit.length; j++) {
          // console.log("Lob: ", lobEdit[j].value);
          lobListEdit.push(lobEdit[j].value);
          for(var k=0; k<initialLobId.length; k++){
              if(initialLobId[k] == lobEdit[j].id) {
                  selectedLobIDEdit.push(lobEdit[j].id);
                  initialLob.push(lobEdit[j].value);
              }
          }
        }
        console.log("lobListEdit: ", lobListEdit);
  
        
        
        
  
        var labEdit = [];
        labelsEdit = [];
        console.log("labelListFromAPI: ", labelListFromAPI);
        
         labEdit = labelListFromAPI;
        for(var j=0; j<labEdit.length; j++) {
          // console.log("Label: ", labEdit[j].value);
          if(labEdit[j].value != 'All') {
            labelsEdit.push(labEdit[j].value);
            // console.log("labEdit: ", labEdit);
          }
          if(initialLabelId == labEdit[j].id){
            initialLabel = labEdit[j].value;
          }
        }
      
      
        types = typeListFromAPI;
        typeEdit = [];
        for(var j=0; j<types.length; j++) {
          if(types[j].value != 'All') {
            typeEdit.push(types[j].value);
          }
         
          if(initialTypeId == types[j].id){
            initialType = types[j].value;
          }
        }
        
      
    
  
    console.log("labelsEdit, types: ", labelsEdit , typeEdit);
    guideBridge.resolveNode("editType").items = typeEdit;
    guideBridge.resolveNode("editType").value = initialType;
  
    guideBridge.resolveNode("editLabel").items = labelsEdit;
    guideBridge.resolveNode("editLabel").value = initialLabel;
      
    guideBridge.resolveNode("editQuestion").value = initialQuestion;
  
    // console.log("guideBridge.resolveNode('editType'): ", guideBridge.resolveNode("editType"));
  
            var repeatPanelEdit = guideBridge.resolveNode("panelLOBEdit");
  
            var currentCountLOBEdit = repeatPanelEdit.instanceManager.instanceCount;
                for (var m = 0; m < currentCountLOBEdit; m++) {
                  console.log("repeatPanelEdit.instanceManager : ", repeatPanelEdit.instanceManager);
                  
                  if(m!=0)
                  {
                    repeatPanelEdit.instanceManager.removeInstance(0);
                  }
                  

        }
  
            for (var x = 0; x < LobListEditRepeatablePanel.length; x++) {
              if (x != 0 && (repeatPanelEdit.instanceManager.instanceCount < LobListEditRepeatablePanel.length)) { //repeatPanelEdit.instanceManager.instanceCount
                repeatPanelEdit.instanceManager.addInstance();
              }
              repeatPanelEdit.instanceManager.instances[x].editLob.value = "-100";
            }
  
            
            console.log("function setCBEditDynamic(valueState)");
            setCBEditDynamic(initialState);
            // setTimeout(function () {
              var repeatPanelEdit = guideBridge.resolveNode("panelLOBEdit");
              console.log("panel lob edit count: ", repeatPanelEdit.instanceManager.instanceCount);
              console.log("LobListEditRepeatablePanel length: ", LobListEditRepeatablePanel);
  
              for (var k = 0; k < repeatPanelEdit.instanceManager.instanceCount; k++) {
                // console.log("LobListEditRepeatablePanel[k]: ", LobListEditRepeatablePanel[k]);
                var lob= ''
                if(LobListEditRepeatablePanel[k].value != undefined) {
                  lob = LobListEditRepeatablePanel[k].value;
                }
                
                var divDynamicID = repeatPanelEdit.instanceManager.instances[k].editLob.id;
                var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
            
                var lab = document.querySelectorAll(dynID);
            
                lab[0].style.margin = "0px";
                $(dynID).html(lob);
  
                //initialLob
                for(var l=0; l<initialLob.length; l++) {
                  if(initialLob[l] == lob){
                    repeatPanelEdit.instanceManager.instances[k].editLob.value = "0";
                  }
                }
            
                document.querySelectorAll(".guideCheckBoxGroup.editLob")[k].style.paddingTop = "0px";
                document.querySelectorAll(".guideCheckBoxGroup.editLob")[k].style.marginTop = "0px";
                document.querySelectorAll(".guideCheckBoxGroup.editLob")[k].style.marginBottom = "0px";
              }
            // }, 100);
    }
   });
   closePanel();
    // resdataEdit = {
    //   "record-id": "1",
    //   "compliance-text": "Are you actively at work from compliance api?",
    //   "verbiage-type": {
    //     "id": "1",
    //     "value": "Question"
    //   },
    //   "state": [
    //     {
    //       "id": "All",
    //       "value": "All"
    //     }
    //   ],
    //   "lob": [
    //     {
    //       "id": "1",
    //       "value": "Accident"
    //     },
    //     {
    //       "id": "2",
    //       "value": "Critical Illness 21000"
    //     },
    //     {
    //       "id": "3",
    //       "value": "Hospital Indemnity"
    //     },
    //     {
    //       "id": "4",
    //       "value": "BenExtend"
    //     },
    //     {
    //       "id": "7",
    //       "value": "Term Life"
    //     }
    //   ],
    //   "label": {
    //     "id": "1",
    //     "value": "Actively At Work"
    //   },
    //   "active-status": true
    // };
  
   

}

function setCBEditDynamic(valueState) {
  var statesFromFunctonValue = valueState;
  
  var repeatPanel = guideBridge.resolveNode("panelStateEdit");

      console.log("Fun 2");
   
      console.log("Panel len: ", repeatPanel.instanceManager.instanceCount);
      for (var k = 0; k < repeatPanel.instanceManager.instanceCount; k++) {
        var state = stateListAll[k].state; 

        for(var l=0; l<statesFromFunctonValue.length; l++) {
          if(state == statesFromFunctonValue[l]){
            console.log("Prepopulating states");
            repeatPanel.instanceManager.instances[k].cbStateEdit.value = "0";
            console.log("Prepopulating states done");
          }
        }

        // document.querySelectorAll(".guideCheckBoxGroup.cbStateEdit")[k].style.paddingTop = "0px";
        // document.querySelectorAll(".guideCheckBoxGroup.cbStateEdit")[k].style.marginTop = "0px";
        // document.querySelectorAll(".guideCheckBoxGroup.cbStateEdit")[k].style.marginBottom = "0px";
      }
      //end
    // }, 10);
}

//states cb functioning
var selectedStateIDEdit = [];
var StateIDEdit;

function getStateFromEdit() {
  console.log("State fun edit");
  var allcbStateEdit = document.querySelectorAll('.cbStateEdit input[type="checkbox"]');
  console.log("After query");
  $(document).off().on('change', '.cbStateEdit input[type="checkbox"]', function () {

    console.log("Before id");
    StateIDEdit = "#" + this.id;
    console.log("Field: ", document.querySelectorAll(StateIDEdit)[0].parentElement.parentElement.children);

    var StateLabelChanged = document.querySelectorAll(StateIDEdit)[0].parentElement.parentElement.children[1].children[0].innerHTML;
    console.log("Selected State: ", StateLabelChanged);

    for (var j = 0; j < stateListAll.length; j++) {
      if (stateListAll[j].state == StateLabelChanged) {
        var selectedID = stateListAll[j].id;
        console.log("Checked: ", document.querySelectorAll(StateIDEdit)[0].checked);
        if (document.querySelectorAll(StateIDEdit)[0].checked == true) {

          if (selectedStateIDEdit.includes(selectedID) != true) {
            selectedStateIDEdit.push(selectedID);
          }

          //start copy

          if (StateLabelChanged == 'All') {
            console.log("All Edit StateLabelChanged");
            for (var x = 0; x < allcbStateEdit.length; x++) {
              // console.log("All cb allcbStateEdit: ", allcbStateEdit);
              allcbStateEdit[x].value = "0";
              allcbStateEdit[x].checked = true;
              allcbStateEdit[x].ariaChecked = true;
              allcbStateEdit[x].setAttribute("checked", "checked");
              allcbStateEdit[x].setAttribute("aria-checked", true);
              allcbStateEdit[x].setAttribute("value", "0");
              console.log("Checked attr set allcbStateEdit", allcbStateEdit[x].checked);

              allcbStateEdit[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestEdit guideFieldHorizontalAlignment guideItemSelected");
              console.log("Check class allcbStateEdit");
              //guideItemSelected
              var itemCB = guideBridge.resolveNode('panelStateEdit');
              console.log("Item cb: ", itemCB);
              for (var l = 0; l < itemCB._instanceManager._instances.length; l++) {
                itemCB._instanceManager._instances[l].children[0].value = "0";
              }
              //_instanceManager._instances[2].children[0].value
              console.log("Item cb af: ", itemCB);
            }

            for (var k = 0; k < stateListAll.length; k++) {
              if (selectedStateIDEdit.includes(stateListAll[k].id) == false) {
                console.log("stateListAll[k].id: ", stateListAll[k].id);
                selectedStateIDEdit.push(stateListAll[k].id);
              }
            }
          }
          //end copy
        }
        else if (document.querySelectorAll(StateIDEdit)[0].checked == false) {
          console.log("Deleting State");
          console.log("Selected ID: ", selectedID);
          indexId = selectedStateIDEdit.indexOf(selectedID);
          console.log("Index selectedStateIDEdit: ", indexId)
          if (indexId >= 0) {
            selectedStateIDEdit.splice(indexId, 1);
          }


          console.log("All index unselect");
          for (var k = 0; k < stateListAll.length; k++) {
            if (stateListAll[k].state == "All" || stateListAll[k].state == "all") {
              console.log("stateListAll[k].id All: ", stateListAll[k].id);
              idOfAllState = stateListAll[k].id;
              if (selectedStateIDEdit.includes(idOfAllState)) {
                console.log("Remove index of all");
                var indexOfAllState = selectedStateIDEdit.indexOf(idOfAllState);
                if (indexOfAllState >= 0) {
                  selectedStateIDEdit.splice(indexOfAllState, 1);
                }

              }

              for (var x = 0; x < allcbStateEdit.length; x++) {

                if (allcbStateEdit[x].parentElement.parentElement.children[1].children[0].innerHTML == 'All') {
                  console.log("Inner html", allcbStateEdit[x].parentElement.parentElement.children[1].children[0].innerHTML);
                  allcbStateEdit[x].value = "-100";
                  allcbStateEdit[x].checked = false;
                  allcbStateEdit[x].ariaChecked = false;
                  allcbStateEdit[x].setAttribute("value", "-100");
                  allcbStateEdit[x].removeAttribute("checked");
                  allcbStateEdit[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbStateEdit guideFieldHorizontalAlignment");
                  console.log("All Cb after -ve: ");
                  var itemCB = guideBridge.resolveNode('panelStateEdit');
                  console.log("Item cb: ", itemCB._instanceManager._instances[0].children[0]);
                  // itemCB._instanceManager._instances[0].children[0].value = "-100";

                  for (var l = 0; l < itemCB._instanceManager.instances.length; l++) {
                    var divDynamicID = itemCB._instanceManager.instances[l].cbStateEdit.id;
                    var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
                    console.log("Dyn ID: ", dynID);
                    var ele = document.querySelector(dynID);
                    if (ele.innerHTML == 'All') {
                      console.log("Inner html is all");
                      itemCB._instanceManager._instances[l].children[0].value = "-100";
                      console.log("-ve val set for all");
                    }
                    console.log("Element: ", ele);
                  }



                }

              }


            }
          }

        }
      }
    }
    console.log("Selected id Edit: ", selectedStateIDEdit);
  });


}

function closeUpdate() {
  console.log("Close fun");
  selectedStateIDEdit = [];
  selectedLobIDEdit= [];

  var repeatPanel = guideBridge.resolveNode("panelStateEdit");
  for (var k = 0; k < repeatPanel.instanceManager.instanceCount; k++) {
    console.log("unselecting all cb state");
    repeatPanel.instanceManager.instances[k].cbStateEdit.value = "-100";
  }
  console.log("Closed");
}

function makeStateInstances() {
  console.log("MAking state instances");
   var switchButton = document.querySelectorAll(".guideSwitch input");
   console.log("Switch button: ", switchButton);
   switchButton[0].style.height = "25px";
  var repeatPanel = guideBridge.resolveNode("panelStateEdit");

  var currentCount = repeatPanel.instanceManager.instanceCount;
			for (var m = 0; m < currentCount; m++) {
                if(m!=0)
                {
                repeatPanel.instanceManager.removeInstance(0);
                }

      }
      console.log("current count in edit: ", currentCount);

  for (var x=0; x<stateListAll.length; x++) {
    if (x != 0) {
      repeatPanel.instanceManager.addInstance(); //add panel
    }
    console.log("adding instance");
  }
    setTimeout(function() {
      //start
      console.log("Fun 2");
     
      var repeatPanel = guideBridge.resolveNode("panelStateEdit");
      console.log("Panel len: ", repeatPanel.instanceManager.instanceCount);
      for (var k = 0; k < repeatPanel.instanceManager.instanceCount; k++) {
        var state = stateListAll[k].state; 
        var divDynamicID = repeatPanel.instanceManager.instances[k].cbStateEdit.id;
        var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
      
        var lab = document.querySelectorAll(dynID);
        // console.log("Lab: ", lab)
        lab[0].style.margin = "0px";
        $(dynID).html(state);
        
        document.querySelectorAll(".guideCheckBoxGroup.cbStateEdit")[k].style.paddingTop = "0px";
        document.querySelectorAll(".guideCheckBoxGroup.cbStateEdit")[k].style.marginTop = "0px";
        document.querySelectorAll(".guideCheckBoxGroup.cbStateEdit")[k].style.marginBottom = "0px";
      }
      //end
    }, 10);
}


var selectedLobIDEdit = [];
var LobIDEdit;
function getLobFromEdit() {
  console.log("Lob fun edit");
  var allcbLobFetch = document.querySelectorAll('.editLob input[type="checkbox"]');

  $(document).off().on('change', '.editLob input[type="checkbox"]', function () {

    LobIDEdit = "#" + this.id;
    console.log("Field: ", document.querySelectorAll(LobIDEdit)[0].parentElement.parentElement.children);

    var LobLabelChanged = document.querySelectorAll(LobIDEdit)[0].parentElement.parentElement.children[1].children[0].innerHTML;
    console.log("Selected Label: ", LobLabelChanged);

    
      
        var Lobs = lobListFromAPI;
        for (var j = 0; j < Lobs.length; j++) {
          if (Lobs[j].value == LobLabelChanged) {
            var selectedID = Lobs[j].id;
            console.log("Checked: ", document.querySelectorAll(LobIDEdit)[0].checked);
            if (document.querySelectorAll(LobIDEdit)[0].checked == true) {

              if (selectedLobIDEdit.includes(selectedID) != true) {
                selectedLobIDEdit.push(selectedID);
              }

              //start copy

              if (LobLabelChanged == 'All') {
                console.log("All Edit LobLabelChanged");
                for (var x = 0; x < allcbLobFetch.length; x++) {
                  console.log("All cb allcbLobFetch: ", allcbLobFetch);
                  allcbLobFetch[x].value = "0";
                  allcbLobFetch[x].checked = true;
                  allcbLobFetch[x].ariaChecked = true;
                  allcbLobFetch[x].setAttribute("checked", "checked");
                  allcbLobFetch[x].setAttribute("aria-checked", true);
                  allcbLobFetch[x].setAttribute("value", "0");
                  console.log("Checked attr set allcbLobFetch", allcbLobFetch[x].checked);

                  allcbLobFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem editLob guideFieldHorizontalAlignment guideItemSelected");
                  console.log("Check class allcbLobFetch");
                  //guideItemSelected
                  var itemCB = guideBridge.resolveNode('panelLOBEdit');
                  console.log("Item cb: ", itemCB);
                  for (var l = 0; l < itemCB._instanceManager._instances.length; l++) {
                    itemCB._instanceManager._instances[l].children[0].value = "0";
                  }
                  //_instanceManager._instances[2].children[0].value
                  console.log("Item cb af: ", itemCB);
                }

                for (var k = 0; k < Lobs.length; k++) {
                  if (selectedLobIDEdit.includes(Lobs[k].id) == false) {
                    console.log("Lobs[k].id: ", Lobs[k].id);
                    selectedLobIDEdit.push(Lobs[k].id);
                  }
                }
              }
              //end copy
            }
            else if (document.querySelectorAll(LobIDEdit)[0].checked == false) {
              console.log("Deleting Lob");
              console.log("Selected ID: ", selectedID);
              indexId = selectedLobIDEdit.indexOf(selectedID);
              console.log("Index selectedLobIDEdit: ", indexId)
              if (indexId >= 0) {
                selectedLobIDEdit.splice(indexId, 1);
              }


              console.log("All index unselect: ", selectedLobIDEdit.indexOf("0"));
              for (var k = 0; k < Lobs.length; k++) {
                if (Lobs[k].value == "All" || Lobs[k].value == "all") {
                  console.log("Lobs[k].id All: ", Lobs[k].id);
                  idOfAllLob = Lobs[k].id;
                  if (selectedLobIDEdit.includes(idOfAllLob)) {
                    console.log("Remove index of all");
                    var indexOfAllLob = selectedLobIDEdit.indexOf(idOfAllLob);
                    if (indexOfAllLob >= 0) {
                      selectedLobIDEdit.splice(indexOfAllLob, 1);
                    }

                  }

                  for (var x = 0; x < allcbLobFetch.length; x++) {

                    if (allcbLobFetch[x].parentElement.parentElement.children[1].children[0].innerHTML == 'All') {
                      console.log("Inner html", allcbLobFetch[x].parentElement.parentElement.children[1].children[0].innerHTML);
                      allcbLobFetch[x].value = "-100";
                      allcbLobFetch[x].checked = false;
                      allcbLobFetch[x].ariaChecked = false;
                      allcbLobFetch[x].setAttribute("value", "-100");
                      allcbLobFetch[x].removeAttribute("checked");
                      // allcbLobFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem editLob guideFieldHorizontalAlignment");
                      console.log("All Cb after -ve: ");
                      var itemCB = guideBridge.resolveNode('panelLOBEdit');
                      console.log("Item cb: ", itemCB._instanceManager._instances[0].children[0]);
                      // itemCB._instanceManager._instances[0].children[0].value = "-100";

                      for (var l = 0; l < itemCB._instanceManager.instances.length; l++) {
                        var divDynamicID = itemCB._instanceManager.instances[l].editLob.id;
                        var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
                        console.log("Dyn ID: ", dynID);
                        var ele = document.querySelector(dynID);
                        if (ele.innerHTML == 'All') {
                          console.log("Inner html is all");
                          itemCB._instanceManager._instances[l].children[0].value = "-100";
                          console.log("-ve val set for all");
                        }
                        console.log("Element: ", ele);
                      }



                    }

                  }


                }
              }

            }
          }
        }
      
    

    console.log("Selected id: ", selectedLobIDEdit);
  });


}

function submitEditedData(){
  var stateArrayEdit = [];
  var lobArrayEdit =[]; 
  var labelFinal = [];
  var question = "";
  var typeArrayFinal = [];
  
  for(var j=0; j<labelListFromAPI.length; j++) {
    if(labelEditFinal == labelListFromAPI[j].value)
    labelFinal.push({"id": labelListFromAPI[j].id, "value": labelListFromAPI[j].value}); 
  }

  for(var k=0; k<lobListFromAPI.length; k++) {
    for(var l=0; l<selectedLobIDEdit.length; l++){
      if(selectedLobIDEdit[l] == lobListFromAPI[k].id)
      lobArrayEdit.push({"id": lobListFromAPI[k].id, "value": lobListFromAPI[k].value}); 
    }
  }

  for(var m=0; m<stateListAll.length; m++) {
    for(var l=0; l<selectedStateIDEdit.length; l++){
      if(selectedStateIDEdit[l] == stateListAll[m].id)
      stateArrayEdit.push({"id": stateListAll[m].id, "value": stateListAll[m].state}); 
    }
  }

  question = questionEditFinal;

  //typeEditFinal
  for(var n=0; n<typeListFromAPI.length; n++) {
    if(typeEditFinal == typeListFromAPI[n].value)
    typeArrayFinal.push({"id": typeListFromAPI[n].id, "value": typeListFromAPI[n].value}); 
  }
  console.log("States: ", stateArrayEdit);
  console.log("lobArrayEdit: ", lobArrayEdit);
  console.log("labelFinal: ", labelFinal);
  console.log("typeArrayFinal: ", typeArrayFinal);
  console.log("question: ", question);

  if(question == null || stateArrayEdit.length == 0 || lobArrayEdit.length == 0 ) {
    // if(question == null && stateArrayEdit.length != 0 && lobArrayEdit.length != 0) {
     
    // }
    // else if(question != null && stateArrayEdit.length == 0 && lobArrayEdit.length != 0) {

    // }
    // else if(question != null && stateArrayEdit.length != 0 && lobArrayEdit.length == 0) {

    // }
    alert("All fields are mandatory. Please enter the details to proceed.");
    
  }
  else{
    var requestEdit = [];
  requestEdit.push({"record-id": recoredIdFinal, "compliance-text":question, "lob":lobArrayEdit, "label":labelFinal[0], "verbiage-type": typeArrayFinal[0],"active-status": activeStatusFinal, "state":stateArrayEdit})
   console.log("requestEdit: ", requestEdit);

   const formData = new FormData();
   formData.append("formData", JSON.stringify(requestEdit[0]));
   formData.append("methodType","update");
       var xhttp = new XMLHttpRequest();
         xhttp.open("POST", "/bin/complianceServlet", true);
         xhttp.send(formData);
     
         xhttp.onreadystatechange = function() {
             if (xhttp.readyState == 4 && xhttp.status == 200) {
                 var x = JSON.parse(xhttp.responseText);
                 console.log("RESP Edit: ", x);
                 if(x["update-status"] == true) {
                  alert("Data updated successfully!");
                 }
                 else{
                  alert("Failed to update data!");
                 }
             }
         }

// Hide editPanel
// Show inputDataPanel
    // guideBridge.resolveNode("editPanel").visible = false;
    // guideBridge.resolveNode("inputDataPanel").visible = true;
  }
}

var activeStatusFinal;
function disableVerbiage(value){
  console.log("Value: ", value);
   var disableButton = guideBridge.resolveNode("buttonDisable");
   console.log("Diable button: ", disableButton);
   var currentTitle = disableButton.value;
   console.log("active-status: ", activeStatusFinal);
    if(currentTitle == 1){ 
       activeStatusFinal = true; 
      }
    else if(currentTitle == 0){
    activeStatusFinal = false;
    }
  console.log("activeStatusFinal: ", activeStatusFinal);
}
