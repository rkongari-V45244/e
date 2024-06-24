// var data = [];
//new state function used
var selectedStateIDAdd = [];
var allcbFetchStateAll = [];
var allcbFetchState = [];
var StateIDAdd;

function getStateFromAdd() {
  console.log("State fun");
  var allcbStateFetch = document.querySelectorAll('.cbTestAdd input[type="checkbox"]');

  $(document).off().on('change', '.cbTestAdd input[type="checkbox"]', function () {

    StateIDAdd = "#" + this.id;
    console.log("Field: ", document.querySelectorAll(StateIDAdd)[0].parentElement.parentElement.children);

    var StateLabelChanged = document.querySelectorAll(StateIDAdd)[0].parentElement.parentElement.children[1].children[0].innerHTML;
    console.log("Selected State: ", StateLabelChanged);

    for (var j = 0; j < stateListAll.length; j++) {
      if (stateListAll[j].state == StateLabelChanged) {
        var selectedID = stateListAll[j].id;
        console.log("Checked: ", document.querySelectorAll(StateIDAdd)[0].checked);
        if (document.querySelectorAll(StateIDAdd)[0].checked == true) {

          if (selectedStateIDAdd.includes(selectedID) != true) {
            selectedStateIDAdd.push(selectedID);
          }

          //start copy

          if (StateLabelChanged == 'All') {
            console.log("All add StateLabelChanged");
            for (var x = 0; x < allcbStateFetch.length; x++) {
              console.log("All cb allcbStateFetch: ", allcbStateFetch);
              allcbStateFetch[x].value = "0";
              allcbStateFetch[x].checked = true;
              allcbStateFetch[x].ariaChecked = true;
              allcbStateFetch[x].setAttribute("checked", "checked");
              allcbStateFetch[x].setAttribute("aria-checked", true);
              allcbStateFetch[x].setAttribute("value", "0");
              console.log("Checked attr set allcbStateFetch", allcbStateFetch[x].checked);

              allcbStateFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment guideItemSelected");
              console.log("Check class allcbStateFetch");
              //guideItemSelected
              var itemCB = guideBridge.resolveNode('testCbPanelAdd');
              console.log("Item cb: ", itemCB);
              for (var l = 0; l < itemCB._instanceManager._instances.length; l++) {
                itemCB._instanceManager._instances[l].children[0].value = "0";
              }
              //_instanceManager._instances[2].children[0].value
              console.log("Item cb af: ", itemCB);
            }

            for (var k = 0; k < stateListAll.length; k++) {
              if (selectedStateIDAdd.includes(stateListAll[k].id) == false) {
                console.log("stateListAll[k].id: ", stateListAll[k].id);
                selectedStateIDAdd.push(stateListAll[k].id);
              }
            }
          }
          //end copy
        }
        else if (document.querySelectorAll(StateIDAdd)[0].checked == false) {
          console.log("Deleting State");
          console.log("Selected ID: ", selectedID);
          indexId = selectedStateIDAdd.indexOf(selectedID);
          console.log("Index selectedStateIDAdd: ", indexId)
          if (indexId >= 0) {
            selectedStateIDAdd.splice(indexId, 1);
          }


          console.log("All index unselect");
          for (var k = 0; k < stateListAll.length; k++) {
            if (stateListAll[k].state == "All" || stateListAll[k].state == "all") {
              console.log("stateListAll[k].id All: ", stateListAll[k].id);
              idOfAllState = stateListAll[k].id;
              if (selectedStateIDAdd.includes(idOfAllState)) {
                console.log("Remove index of all");
                var indexOfAllState = selectedStateIDAdd.indexOf(idOfAllState);
                if (indexOfAllState >= 0) {
                  selectedStateIDAdd.splice(indexOfAllState, 1);
                }

              }

              for (var x = 0; x < allcbStateFetch.length; x++) {

                if (allcbStateFetch[x].parentElement.parentElement.children[1].children[0].innerHTML == 'All') {
                  console.log("Inner html", allcbStateFetch[x].parentElement.parentElement.children[1].children[0].innerHTML);
                  allcbStateFetch[x].value = "-100";
                  allcbStateFetch[x].checked = false;
                  allcbStateFetch[x].ariaChecked = false;
                  allcbStateFetch[x].setAttribute("value", "-100");
                  allcbStateFetch[x].removeAttribute("checked");
                  allcbStateFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment");
                  console.log("All Cb after -ve: ");
                  var itemCB = guideBridge.resolveNode('testCbPanelAdd');
                  console.log("Item cb: ", itemCB._instanceManager._instances[0].children[0]);
                  // itemCB._instanceManager._instances[0].children[0].value = "-100";

                  for (var l = 0; l < itemCB._instanceManager.instances.length; l++) {
                    var divDynamicID = itemCB._instanceManager.instances[l].cbTestAdd.id;
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
    console.log("Selected id add: ", selectedStateIDAdd);
  });


}

var typesAdd = []
var lobListAdd= [];
var labels = [];
function initialLoadAdd() {
  closePanel();
//    data= [
//     {
//         "id": "LABEL",
//         "items": [
//             {
//                 "id": "0",
//                 "value": "All"
//             },
//             {
//                 "id": "1",
//                 "value": "Actively At Work"
//             },
//             {
//                 "id": "2",
//                 "value": "Tobacco"
//             },
//             {
//                 "id": "3",
//                 "value": "Spouse Tobacco"
//             },
//             {
//                 "id": "4",
//                 "value": "General Replacement"
//             },
//             {
//                 "id": "5",
//                 "value": "General Replacement Continuation"
//             },
//             {
//                 "id": "6",
//                 "value": "Spouse Disabled"
//             },
//             {
//                 "id": "7",
//                 "value": "Health Questions"
//             }
//         ]
//     },
//     {
//       "id": "TYPE",
//       "items": [
//           {
//               "id": "0",
//               "value": "All"
//           },
//           {
//             "id": "1",
//             "value": "Questions"
//         },
//         {
//           "id": "2",
//           "value": "Affirmations"
//       },
//       {
//         "id": "3",
//         "value": "Health Questions"
//     },
//     {
//       "id": "2",
//       "value": "Disclosures"
//   }
//         ]
//      },
//     {
//         "id": "LOB",
//         "items": [
//             {
//                 "id": "0",
//                 "value": "All"
//             },
//             {
//                 "id": "1",
//                 "value": "Accident"
//             },
//             {
//                 "id": "2",
//                 "value": "BenExtend"
//             },
//             {
//                 "id": "3",
//                 "value": "Critical Illness"
//             },
//             {
//                 "id": "4",
//                 "value": "Hospital Indemnity"
//             },
//             {
//                 "id": "5",
//                 "value": "Worksite Disability"
//             },
//             {
//                 "id": "6",
//                 "value": "Whole Life"
//             },
//             {
//                 "id": "7",
//                 "value": "Term Life"
//             }
//         ]
//     }
// ];
 
  // console.log("initial load add ques val: ", guideBridge.resolveNode("addQuestion").value);
  guideBridge.resolveNode("addQuestion").value = "";
  addedQuestion = "";
  // console.log("Data: ", data);
  
  lobListAdd = [];
      lobListAdd = lobListFromAPI;
    
    
      var lab = []
      lab =  labelListFromAPI;
      labels = [];
      for(var j=0; j<lab.length; j++) {
        console.log("Label: ", lab[j].value);
        if(lab[j].value != 'All') {
          labels.push(lab[j].value);
        }
      }
    
    
      types = [];
      types = typeListFromAPI;
      typesAdd = [];
      for(var j=0; j<types.length; j++) {
        if(types[j].value != 'All') {
          typesAdd.push(types[j].value);
        }
       
      }
    
  

  console.log("labels, types: ", labels , typesAdd);
  guideBridge.resolveNode("Addtype").items = typesAdd;
  guideBridge.resolveNode("addLabel").items = labels;

          var repeatPanelAdd = guideBridge.resolveNode("panelLOBAdd");

          var currentCountLOBAdd = repeatPanelAdd.instanceManager.instanceCount;
          for (var m = 0; m < currentCountLOBAdd; m++) {
            if(m!=0)
            {
              repeatPanelAdd.instanceManager.removeInstance(0);
            }

  }

          for (var x = 0; x < lobListAdd.length; x++) {
            if (x != 0 && (repeatPanelAdd.instanceManager.instanceCount < lobListAdd.length)) {
              repeatPanelAdd.instanceManager.addInstance();
            }
            repeatPanelAdd.instanceManager.instances[x].checkboxLobAdd.value = "-100";
          }

          setTimeout(function () {
            var repeatPanelAdd = guideBridge.resolveNode("panelLOBAdd");
            for (var k = 0; k < repeatPanelAdd.instanceManager.instanceCount; k++) {
              console.log("lobListAdd[k]: ", lobListAdd[k]);
              var lob = lobListAdd[k].value;
              var divDynamicID = repeatPanelAdd.instanceManager.instances[k].checkboxLobAdd.id;
              var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
          
              var lab = document.querySelectorAll(dynID);
          
              lab[0].style.margin = "0px";
              $(dynID).html(lob);
          
              document.querySelectorAll(".guideCheckBoxGroup.checkboxLobAdd")[k].style.paddingTop = "0px";
              document.querySelectorAll(".guideCheckBoxGroup.checkboxLobAdd")[k].style.marginTop = "0px";
              document.querySelectorAll(".guideCheckBoxGroup.checkboxLobAdd")[k].style.marginBottom = "0px";
            }
          }, 100);
}

typeSelectedIdAdd = [];
labelSelectedIdAdd = [];
function getTypeFromAdd(value) {
    
      types = typeListFromAPI;
        for(var j=0; j<types.length; j++) {
          if(types[j].value == value) {
            // typeSelectedIdAdd = types[j].id;
            typeSelectedIdAdd.push({"id": types[j].id, "value": types[j].value});
          }
         
        }
    
  
  
  console.log("Value Type: ", value, typeSelectedIdAdd);
}

function getLabelFromAdd(value) {

  
    
      lab = labelListFromAPI;
        for(var j=0; j<lab.length; j++) {
          if(lab[j].value == value) {
            // labelSelectedIdAdd = types[j].id;
            labelSelectedIdAdd.push({"id": lab[j].id, "value": lab[j].value});
          }
         
        }
    
  
  console.log("Value Lab: ", value, labelSelectedIdAdd);
}

var addedQuestion = "";
function getQuestionFromAdd(value) {
  console.log("Value Ques: ", value);
  addedQuestion = value;
}

var selectedLobIDAdd = [];
var LobIDAdd;
function getLobFromAdd() {
  console.log("Lob fun");
  var allcbLobFetch = document.querySelectorAll('.checkboxLobAdd input[type="checkbox"]');

  $(document).off().on('change', '.checkboxLobAdd input[type="checkbox"]', function () {

    LobIDAdd = "#" + this.id;
    console.log("Field: ", document.querySelectorAll(LobIDAdd)[0].parentElement.parentElement.children);

    var LobLabelChanged = document.querySelectorAll(LobIDAdd)[0].parentElement.parentElement.children[1].children[0].innerHTML;
    console.log("Selected Label: ", LobLabelChanged);

    
      
        var Lobs = lobListFromAPI;
        for (var j = 0; j < Lobs.length; j++) {
          if (Lobs[j].value == LobLabelChanged) {
            var selectedID = Lobs[j].id;
            console.log("Checked: ", document.querySelectorAll(LobIDAdd)[0].checked);
            if (document.querySelectorAll(LobIDAdd)[0].checked == true) {

              if (selectedLobIDAdd.includes(selectedID) != true) {
                selectedLobIDAdd.push(selectedID);
              }

              //start copy

              if (LobLabelChanged == 'All') {
                console.log("All add LobLabelChanged");
                for (var x = 0; x < allcbLobFetch.length; x++) {
                  console.log("All cb allcbLobFetch: ", allcbLobFetch);
                  allcbLobFetch[x].value = "0";
                  allcbLobFetch[x].checked = true;
                  allcbLobFetch[x].ariaChecked = true;
                  allcbLobFetch[x].setAttribute("checked", "checked");
                  allcbLobFetch[x].setAttribute("aria-checked", true);
                  allcbLobFetch[x].setAttribute("value", "0");
                  console.log("Checked attr set allcbLobFetch", allcbLobFetch[x].checked);

                  allcbLobFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment guideItemSelected");
                  console.log("Check class allcbLobFetch");
                  //guideItemSelected
                  var itemCB = guideBridge.resolveNode('panelLOBAdd');
                  console.log("Item cb: ", itemCB);
                  for (var l = 0; l < itemCB._instanceManager._instances.length; l++) {
                    itemCB._instanceManager._instances[l].children[0].value = "0";
                  }
                  //_instanceManager._instances[2].children[0].value
                  console.log("Item cb af: ", itemCB);
                }

                for (var k = 0; k < Lobs.length; k++) {
                  if (selectedLobIDAdd.includes(Lobs[k].id) == false) {
                    console.log("Lobs[k].id: ", Lobs[k].id);
                    selectedLobIDAdd.push(Lobs[k].id);
                  }
                }
              }
              //end copy
            }
            else if (document.querySelectorAll(LobIDAdd)[0].checked == false) {
              console.log("Deleting Lob");
              console.log("Selected ID: ", selectedID);
              indexId = selectedLobIDAdd.indexOf(selectedID);
              console.log("Index selectedLobIDAdd: ", indexId)
              if (indexId >= 0) {
                selectedLobIDAdd.splice(indexId, 1);
              }


              console.log("All index unselect: ", selectedLobIDAdd.indexOf("0"));
              for (var k = 0; k < Lobs.length; k++) {
                if (Lobs[k].value == "All" || Lobs[k].value == "all") {
                  console.log("Lobs[k].id All: ", Lobs[k].id);
                  idOfAllLob = Lobs[k].id;
                  if (selectedLobIDAdd.includes(idOfAllLob)) {
                    console.log("Remove index of all");
                    var indexOfAllLob = selectedLobIDAdd.indexOf(idOfAllLob);
                    if (indexOfAllLob >= 0) {
                      selectedLobIDAdd.splice(indexOfAllLob, 1);
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
                      // allcbLobFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment");
                      console.log("All Cb after -ve: ");
                      var itemCB = guideBridge.resolveNode('panelLOBAdd');
                      console.log("Item cb: ", itemCB._instanceManager._instances[0].children[0]);
                      // itemCB._instanceManager._instances[0].children[0].value = "-100";

                      for (var l = 0; l < itemCB._instanceManager.instances.length; l++) {
                        var divDynamicID = itemCB._instanceManager.instances[l].checkboxLobAdd.id;
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
      
    

    console.log("Selected id: ", selectedLobIDAdd);
  });


}

responseAdd = [];
var lobAddKeyVal;
var stateAddKeyVal;
var requestAddData;
function addSubmitData(){
  requestAddData = [];
  lobAddKeyVal = [];
  stateAddKeyVal = [];
  var state = selectedStateIDAdd.toString();
  console.log("State submit: ", selectedStateIDAdd + " id: ", state);

  //selectedLobIDAdd lobListAdd
  for(var i=0; i<selectedLobIDAdd.length; i++){
    for(var j=0; j<lobListAdd.length; j++){
      if(selectedLobIDAdd[i] == lobListAdd[j].id){
        lobAddKeyVal.push({"id": lobListAdd[j].id, "value": lobListAdd[j].value});
      }
    }
  }
  //stateAddKeyVal
  for(var i=0; i<selectedStateIDAdd.length; i++){
    for(var j=0; j<stateListAll.length; j++){
      if(selectedStateIDAdd[i] == stateListAll[j].id){
        stateAddKeyVal.push({"id": stateListAll[j].id, "value": stateListAll[j].state});
      }
    }
  }

  if(addedQuestion == "" || typeSelectedIdAdd[0] == undefined || labelSelectedIdAdd[0] == undefined || stateAddKeyVal.length == 0 || lobAddKeyVal.length == 0 ) {
      alert("All fields are mandatory, please fill the details to proceed.")
  }
  else{
    requestAddData.push({"active-status": true, "compliance-text": addedQuestion, "record-id": null, "verbiage-type":typeSelectedIdAdd[0], "label":labelSelectedIdAdd[0], "lob": lobAddKeyVal, "state": stateAddKeyVal});
    console.log("Request data: ", requestAddData);
    const formData = new FormData();
    formData.append("formData", JSON.stringify(requestAddData[0]));
    formData.append("methodType","add");
  
        var xhttp = new XMLHttpRequest();
          xhttp.open("POST", "/bin/complianceServlet", true);
          xhttp.send(formData);
      
          xhttp.onreadystatechange = function() {
              if (xhttp.readyState == 4 && xhttp.status == 200) {
                  var x = JSON.parse(xhttp.responseText);
                  console.log("RESP: ", x);
                  if(x["update-status"] == true) {
                    alert("Data added successfully!");
                   }
                   else{
                    alert("Failed to add data!");
                   }
              }
          }

          // Hide addDataPanel
          // Show inputDataPanel
          // guideBridge.resolveNode("addDataPanel").visible = false;
          // guideBridge.resolveNode("inputDataPanel").visible = true;
  }

}