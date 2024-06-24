var statesValue = [];
var selected = [];
var selectedStateId = [];
var id;
var data = [];
var responseFetch = [];

var lobListFromAPI = [];
var labelListFromAPI = [];
var typeListFromAPI = [];

var stateListAll = [{ "state": "All", "id": "All" }, { "state": "Alabama", "id": "AL" }, { "state": "Alaska", "id": "AK" }, { "state": "Arizona", "id": "AZ" }, { "state": "Arkansas", "id": "AR" }, { "state": "California", "id": "CA" }, { "state": "Colorado", "id": "CO" }, { "state": "Connecticut", "id": "CT" }, { "state": "Delaware", "id": "DE" }, { "state": "Dist. of Columbia", "id": "DC" }, { "state": "Florida", "id": "FL" }, { "state": "Georgia", "id": "GA" }, { "state": "Hawaii", "id": "HI" }, { "state": "Idaho", "id": "ID" }, { "state": "Illinois", "id": "IL" }, { "state": "Indiana", "id": "IN" }, { "state": "Iowa", "id": "IA" }, { "state": "Kansas", "id": "KS" }, { "state": "Kentucky", "id": "KY" }, { "state": "Louisiana", "id": "LA" }, { "state": "Maine", "id": "ME" }, { "state": "Maryland", "id": "MD" }, { "state": "Massachusetts", "id": "MA" }, { "state": "Michigan", "id": "MI" }, { "state": "Minnesota", "id": "MN" }, { "state": "Mississippi", "id": "MS" }, { "state": "Missouri", "id": "MO" }, { "state": "Montana", "id": "MT" }, { "state": "Nebraska", "id": "NE" }, { "state": "Nevada", "id": "NV" }, { "state": "New Hampshire", "id": "NH" }, { "state": "New Jersey", "id": "NJ" }, { "state": "New Mexico", "id": "NM" }, { "state": "New York", "id": "NY" }, { "state": "North Carolina", "id": "NC" }, { "state": "North Dakota", "id": "ND" }, { "state": "Ohio", "id": "OH" }, { "state": "Oklahoma", "id": "OK" }, { "state": "Oregon", "id": "OR" }, { "state": "Pennsylvania", "id": "PA" }, { "state": "Rhode Island", "id": "RI" }, { "state": "South Carolina", "id": "SC" }, { "state": "South Dakota", "id": "SD" }, { "state": "Tennessee", "id": "TN" }, { "state": "Texas", "id": "TX" }, { "state": "Utah", "id": "UT" }, { "state": "Vermont", "id": "VT" }, { "state": "Virginia", "id": "VA" }, { "state": "Washington", "id": "WA" }, { "state": "West Virginia", "id": "WV" }, { "state": "Wisconsin", "id": "WI" }, { "state": "Wyoming", "id": "WY" }];

function setAllTrueFetch(value) {



  var checkboxes = guideBridge.resolveNode("addLob");
  var lobList;
  var finalLobSelected;

  if (value != null && value != undefined) {
    finalLobSelected = value.split(",");
  }

  $(document).on('change', '.inputDataPanel .addLobInput input[type="checkbox"]', function () {
    if (this.value == 'All' && this.id == "guideContainer-rootPanel-panel_1109364196-guidedropdownlist_2092245669___1_widget") {
      
      checkboxes.value = "All,Accident,BenExtend,Critical Illness,Hopital Idemnity,Worksite Disability,Whole Life,Term Life";
      var list = checkboxes.value;
      lobList = list.split(",");
      
      finalLobSelected = lobList;
    }
    else if (this.value == -100 && this.id != "guideContainer-rootPanel-panel_1109364196-guidedropdownlist_2092245669___1_widget") {

      

      var list = checkboxes.value;

      
      if (list != null && list != undefined && list.length > 0) {
        var lobListItems = list.split(",");
        
        finalLobSelected = lobListItems; //check

        if (lobListItems[0] == "All") {
          var removed = lobListItems.splice(0, 1);
          var str = "";
          for (var x = 0; x < lobListItems.length; x++) {
            if (x == 0) {
              str = str + lobListItems[x];
            }
            else {
              str = str + "," + lobListItems[x];
            }

          }
          
          finalLobSelected = lobListItems; //check
          
          checkboxes.value = str;
        }
      }
      else {
        
        // finalLobSelected = lobListItems; //check
      }
    }
  });
  if (finalLobSelected === undefined) {
    finalLobSelected = [];
  }
  
}

function setLOBfromFetchDyn() {
  
  //   data= [
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
  //       }
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

  // typeListFromAPI = [{"id":"0","value":"All"},{"id":"1","value":"Question"},{"id":"2","value":"Affirmation"},{"id":"3","value":"Health Question"},{"id":"4","value":"Disclosure"}];

  // lobListFromAPI= [{"id":"0","value":"All"},{"id":"1","value":"Accident"},{"id":"2","value":"Critical Illness 21000"},{"id":"3","value":"Hospital Indemnity"},{"id":"4","value":"BenExtend"},{"id":"5","value":"Disability"},{"id":"6","value":"Whole Life"},{"id":"7","value":"Term Life"}];
  // labelListFromAPI =  [{"id":"0","value":"All"},{"id":"1","value":"Actively At Work"},{"id":"2","value":"Major Medical"},{"id":"3","value":"Tobacco"},{"id":"4","value":"Spouse Tobacco"},{"id":"5","value":"DI Eligibility Question"},{"id":"6","value":"Spouse Disabled"},{"id":"7","value":"General Replacement"},{"id":"8","value":"General Replacement Continuation"},{"id":"9","value":"WL ACD"},{"id":"10","value":"Health Question 1"},{"id":"11","value":"Health Question 2"},{"id":"12","value":"Health Question 3"},{"id":"13","value":"Health Question 4"},{"id":"14","value":"Health Question 5"},{"id":"15","value":"Health Question 6"},{"id":"16","value":"Health Question 7"},{"id":"17","value":"Health Question 8"},{"id":"18","value":"Certification Language"},{"id":"19","value":"Fraud Language"},{"id":"20","value":"Additional Language"},{"id":"21","value":"Suitability Statements"}];

// //comment above
// 

  //labelForLob
  // document.querySelectorAll(".labelForLob p")[0].style.color = "#fff";
  // document.querySelectorAll(".labelForLob p")[0].style.background = "rgb(0, 167, 225)";
  // document.querySelectorAll(".labelForLob p")[0].style.borderTopRightRadius = "26px";
  // document.querySelectorAll(".labelForLob p")[0].style.borderBottomLeftRadius = "26px";
  // document.querySelectorAll(".labelForLob p")[0].style.borderBottomRightRadius = "26px";
  // document.querySelectorAll(".labelForLob p")[0].style.borderTopLeftRadius = "26px";
  // document.querySelectorAll(".labelForLob p")[0].style.borderStyle = "hidden";
  // document.querySelectorAll(".labelForLob p")[0].style.width = "77px";
  // document.querySelectorAll(".labelForLob p")[0].style.textAlign = "center";
  // document.querySelectorAll(".labelForLob p")[0].style.marginBottom = "0px";
  // document.querySelectorAll(".labelForLob p")[0].style.padding = "2px 6px";

  // document.querySelectorAll(".labelForLabel p")[0].style.color = "#fff";
  // document.querySelectorAll(".labelForLabel p")[0].style.background = "rgb(0, 167, 225)";
  // document.querySelectorAll(".labelForLabel p")[0].style.borderTopRightRadius = "26px";
  // document.querySelectorAll(".labelForLabel p")[0].style.borderBottomLeftRadius = "26px";
  // document.querySelectorAll(".labelForLabel p")[0].style.borderBottomRightRadius = "26px";
  // document.querySelectorAll(".labelForLabel p")[0].style.borderTopLeftRadius = "26px";
  // document.querySelectorAll(".labelForLabel p")[0].style.borderStyle = "hidden";
  // document.querySelectorAll(".labelForLabel p")[0].style.width = "77px";
  // document.querySelectorAll(".labelForLabel p")[0].style.textAlign = "center";
  // document.querySelectorAll(".labelForLabel p")[0].style.marginBottom = "0px";
  // document.querySelectorAll(".labelForLabel p")[0].style.padding = "2px 6px";

  // document.querySelectorAll(".labelForType p")[0].style.color = "#fff";
  // document.querySelectorAll(".labelForType p")[0].style.background = "rgb(0, 167, 225)";
  // document.querySelectorAll(".labelForType p")[0].style.borderTopRightRadius = "26px";
  // document.querySelectorAll(".labelForType p")[0].style.borderBottomLeftRadius = "26px";
  // document.querySelectorAll(".labelForType p")[0].style.borderBottomRightRadius = "26px";
  // document.querySelectorAll(".labelForType p")[0].style.borderTopLeftRadius = "26px";
  // document.querySelectorAll(".labelForType p")[0].style.borderStyle = "hidden";
  // document.querySelectorAll(".labelForType p")[0].style.width = "77px";
  // document.querySelectorAll(".labelForType p")[0].style.textAlign = "center";
  // document.querySelectorAll(".labelForType p")[0].style.marginBottom = "0px";
  // document.querySelectorAll(".labelForType p")[0].style.padding = "2px 6px";

  ///bin/complianceServlet

  
  var lobList = [];
  var labelList = [];
  var typeList = [];
  $.ajax({
    url: "/bin/complianceServlet",
    type: 'GET',
    data: { referenceDataItem: "LABEL,LOB,TYPE", methodType: "init" },
    dataType: 'json', // added data type
    success: function (resdata) {

      data = resdata;

      
      for (var i = 0; i < data.length; i++) {
        if (data[i].id == 'LOB') {
          lobList = data[i].items;
          lobListFromAPI = data[i].items;
          

          var repeatPanel = guideBridge.resolveNode("panelLOB");
          for (var x = 0; x < lobList.length; x++) {
            if (x != 0) {
              repeatPanel.instanceManager.addInstance(); //add panel
            }
          }

          setTimeout(function () {
            //  

            var repeatPanel = guideBridge.resolveNode("panelLOB");

            for (var k = 0; k < repeatPanel.instanceManager.instanceCount; k++) {
              var lob = lobList[k].value;
              var divDynamicID = repeatPanel.instanceManager.instances[k].checkboxLob.id;
              var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";

              var lab = document.querySelectorAll(dynID);

              lab[0].style.margin = "0px";
              $(dynID).html(lob);

              document.querySelectorAll(".guideCheckBoxGroup.checkboxLob")[k].style.paddingTop = "0px";
              document.querySelectorAll(".guideCheckBoxGroup.checkboxLob")[k].style.marginTop = "0px";
              document.querySelectorAll(".guideCheckBoxGroup.checkboxLob")[k].style.marginBottom = "0px";
            }

            //end
          }, 100);
        }
        if (data[i].id == 'TYPE') {
          typeList = data[i].items;
          typeListFromAPI = data[i].items;
          

          var repeatPanelType = guideBridge.resolveNode("panelType");
          
          for (var x = 0; x < typeList.length; x++) {
            if (x != 0) {
              repeatPanelType.instanceManager.addInstance(); //add panel
            }
          }

          setTimeout(function () {
            //  

            var repeatPanelType = guideBridge.resolveNode("panelType");

            for (var k = 0; k < repeatPanelType.instanceManager.instanceCount; k++) {
              var type = typeList[k].value;
              var divDynamicID = repeatPanelType.instanceManager.instances[k].checkboxType.id;
              var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";

              var lab = document.querySelectorAll(dynID);

              lab[0].style.margin = "0px";
              $(dynID).html(type);

              document.querySelectorAll(".guideCheckBoxGroup.checkboxType")[k].style.paddingTop = "0px";
              document.querySelectorAll(".guideCheckBoxGroup.checkboxType")[k].style.marginTop = "0px";
              document.querySelectorAll(".guideCheckBoxGroup.checkboxType")[k].style.marginBottom = "0px";
            }
            //end
          }, 100);
        }
        if (data[i].id == 'LABEL') {
          
          labelList = data[i].items;
          labelListFromAPI = data[i].items;
          //panelLabel

          var repeatPanelLab = guideBridge.resolveNode("panelLabel");
          for (var y = 0; y < labelList.length; y++) {
            if (y != 0) {
              repeatPanelLab.instanceManager.addInstance(); //add panel
            }
          }

          setTimeout(function () {
            

            var repeatPanelLab = guideBridge.resolveNode("panelLabel");
            
            for (var k = 0; k < repeatPanelLab.instanceManager.instanceCount; k++) {
              var label = labelList[k].value;
              // 
              var divDynamicID = repeatPanelLab.instanceManager.instances[k].checkboxLabel.id;
              var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";

              var lab = document.querySelectorAll(dynID);

              lab[0].style.margin = "0px";
              $(dynID).html(label);

              document.querySelectorAll(".guideCheckBoxGroup.checkboxLabel")[k].style.paddingTop = "0px";
              document.querySelectorAll(".guideCheckBoxGroup.checkboxLabel")[k].style.marginTop = "0px";
              document.querySelectorAll(".guideCheckBoxGroup.checkboxLabel")[k].style.marginBottom = "0px";
            }
            //end
          }, 100);

        }
      }

    }
  });







  //end

}


var selectedTypeID = [];
var allcbFetchTypeAll = [];
var allcbFetchType = [];
var typeID;
function getTypeFromFetch() {
  
  var allcbTypeFetch = document.querySelectorAll('.checkboxType input[type="checkbox"]');

  $(document).off().on('change', '.checkboxType input[type="checkbox"]', function () {

    typeID = "#" + this.id;
    
    var typeLabelChanged = document.querySelectorAll(typeID)[0].parentElement.parentElement.children[1].children[0].innerHTML;
    
    for (var i = 0; i < data.length; i++) {
      if (data[i].id == 'TYPE') {
        var types = data[i].items;
        for (var j = 0; j < types.length; j++) {
          if (types[j].value == typeLabelChanged) {
            var selectedID = types[j].id;
            
            if (document.querySelectorAll(typeID)[0].checked == true) {

              if (selectedTypeID.includes(selectedID) != true) {
                selectedTypeID.push(selectedID);
              }

              //start copy

              if (typeLabelChanged == 'All') {
                
                for (var x = 0; x < allcbTypeFetch.length; x++) {
                  
                  allcbTypeFetch[x].value = "0";
                  allcbTypeFetch[x].checked = true;
                  allcbTypeFetch[x].ariaChecked = true;
                  allcbTypeFetch[x].setAttribute("checked", "checked");
                  allcbTypeFetch[x].setAttribute("aria-checked", true);
                  allcbTypeFetch[x].setAttribute("value", "0");
                  

                  allcbTypeFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment guideItemSelected");
                  
                  //guideItemSelected
                  var itemCB = guideBridge.resolveNode('panelType');
                  
                  for (var l = 0; l < itemCB._instanceManager._instances.length; l++) {
                    itemCB._instanceManager._instances[l].children[0].value = "0";
                  }
                  //_instanceManager._instances[2].children[0].value
                  
                }

                for (var k = 0; k < types.length; k++) {
                  if (selectedTypeID.includes(types[k].id) == false) {
                    
                    selectedTypeID.push(types[k].id);
                  }
                }
              }
              //end copy
            }
            else if (document.querySelectorAll(typeID)[0].checked == false) {
              
              
              indexId = selectedTypeID.indexOf(selectedID);
              
              if (indexId >= 0) {
                selectedTypeID.splice(indexId, 1);
              }


              
              for (var k = 0; k < types.length; k++) {
                if (types[k].value == "All" || types[k].value == "all") {
                  
                  idOfAllType = types[k].id;
                  if (selectedTypeID.includes(idOfAllType)) {
                    
                    var indexOfAllType = selectedTypeID.indexOf(idOfAllType);
                    if (indexOfAllType >= 0) {
                      selectedTypeID.splice(indexOfAllType, 1);
                    }

                  }

                  for (var x = 0; x < allcbTypeFetch.length; x++) {

                    if (allcbTypeFetch[x].parentElement.parentElement.children[1].children[0].innerHTML == 'All') {
                      
                      allcbTypeFetch[x].value = "-100";
                      allcbTypeFetch[x].checked = false;
                      allcbTypeFetch[x].ariaChecked = false;
                      allcbTypeFetch[x].setAttribute("value", "-100");
                      allcbTypeFetch[x].removeAttribute("checked");
                      // allcbTypeFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment");
                      
                      var itemCB = guideBridge.resolveNode('panelType');
                      
                      // itemCB._instanceManager._instances[0].children[0].value = "-100";

                      for (var l = 0; l < itemCB._instanceManager.instances.length; l++) {
                        var divDynamicID = itemCB._instanceManager.instances[l].checkboxType.id;
                        var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
                        
                        var ele = document.querySelector(dynID);
                        if (ele.innerHTML == 'All') {
                          
                          itemCB._instanceManager._instances[l].children[0].value = "-100";
                          
                        }
                        
                      }



                    }

                  }


                }
              }

            }
          }
        }
      }
    }

    
  });


}


var selectedLobID = [];
var allcbFetchLobAll = [];
var allcbFetchLob = [];
var LobID;
function getLobFromFetch() {
  
  var allcbLobFetch = document.querySelectorAll('.checkboxLob input[type="checkbox"]');

  $(document).off().on('change', '.checkboxLob input[type="checkbox"]', function () {

    LobID = "#" + this.id;
    

    var LobLabelChanged = document.querySelectorAll(LobID)[0].parentElement.parentElement.children[1].children[0].innerHTML;
    

    for (var i = 0; i < data.length; i++) {
      if (data[i].id == 'LOB') {
        var Lobs = data[i].items;
        for (var j = 0; j < Lobs.length; j++) {
          if (Lobs[j].value == LobLabelChanged) {
            var selectedID = Lobs[j].id;
            
            if (document.querySelectorAll(LobID)[0].checked == true) {

              if (selectedLobID.includes(selectedID) != true) {
                selectedLobID.push(selectedID);
              }

              //start copy

              if (LobLabelChanged == 'All') {
                
                for (var x = 0; x < allcbLobFetch.length; x++) {
                  
                  allcbLobFetch[x].value = "0";
                  allcbLobFetch[x].checked = true;
                  allcbLobFetch[x].ariaChecked = true;
                  allcbLobFetch[x].setAttribute("checked", "checked");
                  allcbLobFetch[x].setAttribute("aria-checked", true);
                  allcbLobFetch[x].setAttribute("value", "0");
                  

                  allcbLobFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment guideItemSelected");
                  
                  //guideItemSelected
                  var itemCB = guideBridge.resolveNode('panelLOB');
                  
                  for (var l = 0; l < itemCB._instanceManager._instances.length; l++) {
                    itemCB._instanceManager._instances[l].children[0].value = "0";
                  }
                  //_instanceManager._instances[2].children[0].value
                  
                }

                for (var k = 0; k < Lobs.length; k++) {
                  if (selectedLobID.includes(Lobs[k].id) == false) {
                    
                    selectedLobID.push(Lobs[k].id);
                  }
                }
              }
              //end copy
            }
            else if (document.querySelectorAll(LobID)[0].checked == false) {
              
              
              indexId = selectedLobID.indexOf(selectedID);
              
              if (indexId >= 0) {
                selectedLobID.splice(indexId, 1);
              }


              
              for (var k = 0; k < Lobs.length; k++) {
                if (Lobs[k].value == "All" || Lobs[k].value == "all") {
                  
                  idOfAllLob = Lobs[k].id;
                  if (selectedLobID.includes(idOfAllLob)) {
                    
                    var indexOfAllLob = selectedLobID.indexOf(idOfAllLob);
                    if (indexOfAllLob >= 0) {
                      selectedLobID.splice(indexOfAllLob, 1);
                    }

                  }

                  for (var x = 0; x < allcbLobFetch.length; x++) {

                    if (allcbLobFetch[x].parentElement.parentElement.children[1].children[0].innerHTML == 'All') {
                      
                      allcbLobFetch[x].value = "-100";
                      allcbLobFetch[x].checked = false;
                      allcbLobFetch[x].ariaChecked = false;
                      allcbLobFetch[x].setAttribute("value", "-100");
                      allcbLobFetch[x].removeAttribute("checked");
                      // allcbLobFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment");
                      
                      var itemCB = guideBridge.resolveNode('panelLOB');
                      
                      // itemCB._instanceManager._instances[0].children[0].value = "-100";

                      for (var l = 0; l < itemCB._instanceManager.instances.length; l++) {
                        var divDynamicID = itemCB._instanceManager.instances[l].checkboxLob.id;
                        var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
                        
                        var ele = document.querySelector(dynID);
                        if (ele.innerHTML == 'All') {
                          
                          itemCB._instanceManager._instances[l].children[0].value = "-100";
                          
                        }
                        
                      }



                    }

                  }


                }
              }

            }
          }
        }
      }
    }

    
  });


}

var selectedLabelID = [];
var allcbFetchLabelAll = [];
var allcbFetchLabel = [];
var LabelID;
function getLabelFromFetch() {
  
  var allcbLabelFetch = document.querySelectorAll('.checkboxLabel input[type="checkbox"]');

  $(document).off().on('change', '.checkboxLabel input[type="checkbox"]', function () {

    LabelID = "#" + this.id;
    

    var LabelLabelChanged = document.querySelectorAll(LabelID)[0].parentElement.parentElement.children[1].children[0].innerHTML;
    

    for (var i = 0; i < data.length; i++) {
      if (data[i].id == 'LABEL') {
        var Labels = data[i].items;
        for (var j = 0; j < Labels.length; j++) {
          if (Labels[j].value == LabelLabelChanged) {
            var selectedID = Labels[j].id;
            
            if (document.querySelectorAll(LabelID)[0].checked == true) {

              if (selectedLabelID.includes(selectedID) != true) {
                selectedLabelID.push(selectedID);
              }

              //start copy

              if (LabelLabelChanged == 'All') {
                
                for (var x = 0; x < allcbLabelFetch.length; x++) {
                  
                  allcbLabelFetch[x].value = "0";
                  allcbLabelFetch[x].checked = true;
                  allcbLabelFetch[x].ariaChecked = true;
                  allcbLabelFetch[x].setAttribute("checked", "checked");
                  allcbLabelFetch[x].setAttribute("aria-checked", true);
                  allcbLabelFetch[x].setAttribute("value", "0");
                  

                  allcbLabelFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment guideItemSelected");
                  
                  //guideItemSelected
                  var itemCB = guideBridge.resolveNode('panelLabel');
                  
                  for (var l = 0; l < itemCB._instanceManager._instances.length; l++) {
                    itemCB._instanceManager._instances[l].children[0].value = "0";
                  }
                  //_instanceManager._instances[2].children[0].value
                  
                }

                for (var k = 0; k < Labels.length; k++) {
                  if (selectedLabelID.includes(Labels[k].id) == false) {
                    
                    selectedLabelID.push(Labels[k].id);
                  }
                }
              }
              //end copy
            }
            else if (document.querySelectorAll(LabelID)[0].checked == false) {
              
              
              indexId = selectedLabelID.indexOf(selectedID);
              
              if (indexId >= 0) {
                selectedLabelID.splice(indexId, 1);
              }


              
              for (var k = 0; k < Labels.length; k++) {
                if (Labels[k].value == "All" || Labels[k].value == "all") {
                  
                  idOfAllLabel = Labels[k].id;
                  if (selectedLabelID.includes(idOfAllLabel)) {
                    
                    var indexOfAllLabel = selectedLabelID.indexOf(idOfAllLabel);
                    if (indexOfAllLabel >= 0) {
                      selectedLabelID.splice(indexOfAllLabel, 1);
                    }

                  }

                  for (var x = 0; x < allcbLabelFetch.length; x++) {

                    if (allcbLabelFetch[x].parentElement.parentElement.children[1].children[0].innerHTML == 'All') {
                      
                      allcbLabelFetch[x].value = "-100";
                      allcbLabelFetch[x].checked = false;
                      allcbLabelFetch[x].ariaChecked = false;
                      allcbLabelFetch[x].setAttribute("value", "-100");
                      allcbLabelFetch[x].removeAttribute("checked");
                      // allcbLabelFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment");
                      
                      var itemCB = guideBridge.resolveNode('panelLabel');
                      
                      // itemCB._instanceManager._instances[0].children[0].value = "-100";

                      for (var l = 0; l < itemCB._instanceManager.instances.length; l++) {
                        var divDynamicID = itemCB._instanceManager.instances[l].checkboxLabel.id;
                        var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
                        
                        var ele = document.querySelector(dynID);
                        if (ele.innerHTML == 'All') {
                          
                          itemCB._instanceManager._instances[l].children[0].value = "-100";
                          
                        }
                        
                      }



                    }

                  }


                }
              }

            }
          }
        }
      }
    }

    
  });


}

//new state function used
var selectedStateID = [];
var allcbFetchStateAll = [];
var allcbFetchState = [];
var StateID;
var stateListAll = [{ "state": "All", "id": "All" }, { "state": "Alabama", "id": "AL" }, { "state": "Alaska", "id": "AK" }, { "state": "Arizona", "id": "AZ" }, { "state": "Arkansas", "id": "AR" }, { "state": "California", "id": "CA" }, { "state": "Colorado", "id": "CO" }, { "state": "Connecticut", "id": "CT" }, { "state": "Delaware", "id": "DE" }, { "state": "Dist. of Columbia", "id": "DC" }, { "state": "Florida", "id": "FL" }, { "state": "Georgia", "id": "GA" }, { "state": "Hawaii", "id": "HI" }, { "state": "Idaho", "id": "ID" }, { "state": "Illinois", "id": "IL" }, { "state": "Indiana", "id": "IN" }, { "state": "Iowa", "id": "IA" }, { "state": "Kansas", "id": "KS" }, { "state": "Kentucky", "id": "KY" }, { "state": "Louisiana", "id": "LA" }, { "state": "Maine", "id": "ME" }, { "state": "Maryland", "id": "MD" }, { "state": "Massachusetts", "id": "MA" }, { "state": "Michigan", "id": "MI" }, { "state": "Minnesota", "id": "MN" }, { "state": "Mississippi", "id": "MS" }, { "state": "Missouri", "id": "MO" }, { "state": "Montana", "id": "MT" }, { "state": "Nebraska", "id": "NE" }, { "state": "Nevada", "id": "NV" }, { "state": "New Hampshire", "id": "NH" }, { "state": "New Jersey", "id": "NJ" }, { "state": "New Mexico", "id": "NM" }, { "state": "New York", "id": "NY" }, { "state": "North Carolina", "id": "NC" }, { "state": "North Dakota", "id": "ND" }, { "state": "Ohio", "id": "OH" }, { "state": "Oklahoma", "id": "OK" }, { "state": "Oregon", "id": "OR" }, { "state": "Pennsylvania", "id": "PA" }, { "state": "Rhode Island", "id": "RI" }, { "state": "South Carolina", "id": "SC" }, { "state": "South Dakota", "id": "SD" }, { "state": "Tennessee", "id": "TN" }, { "state": "Texas", "id": "TX" }, { "state": "Utah", "id": "UT" }, { "state": "Vermont", "id": "VT" }, { "state": "Virginia", "id": "VA" }, { "state": "Washington", "id": "WA" }, { "state": "West Virginia", "id": "WV" }, { "state": "Wisconsin", "id": "WI" }, { "state": "Wyoming", "id": "WY" }];

function getStateFromFetch() {
  
  var allcbStateFetch = document.querySelectorAll('.cbTest input[type="checkbox"]');

  $(document).off().on('change', '.cbTest input[type="checkbox"]', function () {

    StateID = "#" + this.id;
    

    var StateLabelChanged = document.querySelectorAll(StateID)[0].parentElement.parentElement.children[1].children[0].innerHTML;
    

    //   for(var i=0; i<data.length; i++) {
    // if(data[i].id == 'LABEL') {
    //   var States = data[i].items;
    for (var j = 0; j < stateListAll.length; j++) {
      if (stateListAll[j].state == StateLabelChanged) {
        var selectedID = stateListAll[j].id;
        
        if (document.querySelectorAll(StateID)[0].checked == true) {

          if (selectedStateID.includes(selectedID) != true) {
            selectedStateID.push(selectedID);
          }

          //start copy

          if (StateLabelChanged == 'All') {
            
            for (var x = 0; x < allcbStateFetch.length; x++) {
              
              allcbStateFetch[x].value = "0";
              allcbStateFetch[x].checked = true;
              allcbStateFetch[x].ariaChecked = true;
              allcbStateFetch[x].setAttribute("checked", "checked");
              allcbStateFetch[x].setAttribute("aria-checked", true);
              allcbStateFetch[x].setAttribute("value", "0");
              

              allcbStateFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment guideItemSelected");
              
              //guideItemSelected
              var itemCB = guideBridge.resolveNode('testCbPanel');
              
              for (var l = 0; l < itemCB._instanceManager._instances.length; l++) {
                itemCB._instanceManager._instances[l].children[0].value = "0";
              }
              //_instanceManager._instances[2].children[0].value
              
            }

            for (var k = 0; k < stateListAll.length; k++) {
              if (selectedStateID.includes(stateListAll[k].id) == false) {
                
                selectedStateID.push(stateListAll[k].id);
              }
            }
          }
          //end copy
        }
        else if (document.querySelectorAll(StateID)[0].checked == false) {
          
          
          indexId = selectedStateID.indexOf(selectedID);
          
          if (indexId >= 0) {
            selectedStateID.splice(indexId, 1);
          }


          
          for (var k = 0; k < stateListAll.length; k++) {
            if (stateListAll[k].state == "All" || stateListAll[k].state == "all") {
              
              idOfAllState = stateListAll[k].id;
              if (selectedStateID.includes(idOfAllState)) {
                
                var indexOfAllState = selectedStateID.indexOf(idOfAllState);
                if (indexOfAllState >= 0) {
                  selectedStateID.splice(indexOfAllState, 1);
                }

              }

              for (var x = 0; x < allcbStateFetch.length; x++) {

                if (allcbStateFetch[x].parentElement.parentElement.children[1].children[0].innerHTML == 'All') {
                  
                  allcbStateFetch[x].value = "-100";
                  allcbStateFetch[x].checked = false;
                  allcbStateFetch[x].ariaChecked = false;
                  allcbStateFetch[x].setAttribute("value", "-100");
                  allcbStateFetch[x].removeAttribute("checked");
                  allcbStateFetch[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment");
                  
                  var itemCB = guideBridge.resolveNode('testCbPanel');
                  
                  // itemCB._instanceManager._instances[0].children[0].value = "-100";

                  for (var l = 0; l < itemCB._instanceManager.instances.length; l++) {
                    var divDynamicID = itemCB._instanceManager.instances[l].cbTest.id;
                    var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
                    
                    var ele = document.querySelector(dynID);
                    if (ele.innerHTML == 'All') {
                      
                      itemCB._instanceManager._instances[l].children[0].value = "-100";
                      
                    }
                    
                  }



                }

              }


            }
          }

        }
      }
    }
    // }
    //   }

    
  });


}


function submitFetchData() {
  
  
  
  

  var state = selectedStateID.toString();
  var lob = selectedLobID.toString();
  var label = selectedLabelID.toString();
  var type = selectedTypeID.toString();
  
  var loader = "";
  loader = document.createElement('div');
  loader.rel = 'stylesheet';
  loader.type = 'text/css';
  loader.href = '/css/loader.css';

  
  loader.setAttribute('id', 'previewLoaderN');
  
  // loader.setAttribute('class', 'loaderdisplay');
  loader.className = "loader";
  // loader.innerHTML = "Loading";
  // 
  
  document.getElementsByTagName('BODY')[0].appendChild(loader);
  
  document.getElementById("guideContainerForm").style.filter="blur(10px)";

  
  
  $.ajax({
    url: "/bin/complianceServlet",
    type: 'GET',
    data: { situsState: state, lob: lob, type: type, label: label },
    dataType: 'json', // added data type
    success: function (resdata) {
     responseFetch = resdata;
      
      // ["compliance-verbiage-list"]
      // responseFetch=newresponseFetch;
        
      //  addPanel();
       fillData();
       document.getElementById("guideContainerForm").style.filter="blur()";
       loader.setAttribute('class', 'loaderhide');
       document.getElementById("previewLoaderNew").style.display ="none";
    //  
  }
 });

  
//   responseFetch = {
//   "compliance-verbiage-list": [
//       {
//           "record-id": "12",
//           "compliance-text": "Does the person to be insured have comprehensive health benefits from an insurance policy, an HMO plan, an employer health benefit plan, or other coverage that satisfies minimum essential coverage under the Affordable Care Act.Persons without such comprehensive coverage are not eligible for this Group Hospital Indemnity coverage.",
//           "verbiage-type": {
//               "id": "1",
//               "value": "Question"
//           },
//           "state": [
//               {
//                   "id": "CA",
//                   "value": "California"
//               }
//           ],
//           "lob": [
//               {
//                   "id": "3",
//                   "value": "Hospital Indemnity"
//               },
//               {
//                   "id": "4",
//                   "value": "BenExtend"
//               }
//           ],
//           "label": {
//               "id": "2",
//               "value": "Major Medical"
//           },
//           "active-status": true
//       },
//       {
//             "record-id": "12",
//             "compliance-text": "Are you actively at work?",
//             "verbiage-type": {
//               "id": "1",
//               "value": "Question"
//             },
//             "state": [
//               {
//                 "id": "AR",
//                 "value": "Arizona"
//               }
//             ],
//             "lob": [
//               {
//                 "id": "2",
//                 "value": "Critical Illness"
//               },
//               {
//                 "id": "3",
//                 "value": "Accident"
//               }
//             ],
//             "label": {
//               "id": "1",
//               "value": "Actively At Work"
//             },
//             "active-status": true
//           },
//           {
//             "record-id": "12",
//             "compliance-text": "Does this coverage replace any existing Aflac individual policy?",
//             "verbiage-type": {
//               "id": "1",
//               "value": "Question"
//             },
//             "state": [
//               {
//                 "id": "AZ",
//                 "value": "Arizona"
//               },
//               {
//                 "id": "AL",
//                 "value": "Alaska"
//               }
//             ],
//             "lob": [
//               {
//                 "id": "2",
//                 "value": "Critical Illness"
//               }
//             ],
//             "label": {
//               "id": "1",
//               "value": "Actively At Work"
//             },
//             "active-status": true
//           }
//   ]
// };

 
// addPanel();
}

function exportComplianceData() {
  
  var fileName = "Compliance_Data" + '_' + new Date().toJSON().slice(0, 10) + '.xlsx';
  const formData = new FormData();
  formData.append("formData", JSON.stringify(responseFetch));
  formData.append("methodType","export");
  var xhttp = new XMLHttpRequest();
  xhttp.open("POST", "/bin/complianceServlet", true);
  xhttp.responseType = "blob";
  xhttp.send(formData);

  xhttp.onreadystatechange = function() {
    if (xhttp.readyState == 4 && xhttp.status == 200) {
        var a;
        a = document.createElement('a');
        a.href = window.URL.createObjectURL(xhttp.response);
        a.download = fileName;
        a.style.display = 'none';
        document.body.appendChild(a);
        a.click();
    }
  }
}
function clearData(){
  clearDataState();
  clearDataLob();
  clearDataType();
  clearDataLabel();
}

function clearDataState() {
  
  //state clear
  

  var allCbState = document.querySelectorAll('.cbTest input[type="checkbox"]');

  for (var x = 0; x < allCbState.length; x++) {

    if (allCbState[x].checked == true) {

      allCbState[x].value = "-100";
      allCbState[x].checked = false;
      allCbState[x].ariaChecked = false;
      allCbState[x].setAttribute("value", "-100");
      allCbState[x].removeAttribute("checked");

      

    }

  }

  var itemCB = guideBridge.resolveNode('testCbPanel');
  
  itemCB._instanceManager._instances[0].children[0].value = "-100";

  for (var l = 0; l < itemCB._instanceManager.instances.length; l++) {
    var divDynamicID = itemCB._instanceManager.instances[l].cbTest.id;
    var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
    
    var ele = document.querySelector(dynID);
      itemCB._instanceManager._instances[l].children[0].value = "-100";
    
  }

  selectedStateID = [];
  //state clear end

}

function clearDataLob() {
  
  //Lob clear
  

  var allCbLob = document.querySelectorAll('.checkboxLob input[type="checkbox"]');

  for (var x = 0; x < allCbLob.length; x++) {

    if (allCbLob[x].checked == true) {

      allCbLob[x].value = "-100";
      allCbLob[x].checked = false;
      allCbLob[x].ariaChecked = false;
      allCbLob[x].setAttribute("value", "-100");
      allCbLob[x].removeAttribute("checked");

      

    }

  }

  var itemCB = guideBridge.resolveNode('panelLOB');
  

  for (var l = 0; l < itemCB._instanceManager.instances.length; l++) {
    if(itemCB._instanceManager._instances[l].children[0].value != "-100"){
      itemCB._instanceManager._instances[l].children[0].value = "-100";
    }
  }

  selectedLobID = [];
  //Lob clear end
}

function clearDataLabel() {
  
  //Label clear
  

  var allCbLabel = document.querySelectorAll('.checkboxLabel input[type="checkbox"]');

  for (var x = 0; x < allCbLabel.length; x++) {

    if (allCbLabel[x].checked == true) {

      allCbLabel[x].value = "-100";
      allCbLabel[x].checked = false;
      allCbLabel[x].ariaChecked = false;
      allCbLabel[x].setAttribute("value", "-100");
      allCbLabel[x].removeAttribute("checked");

      

    }

  }

  var itemCB = guideBridge.resolveNode('panelLabel');
  

  for (var l = 0; l < itemCB._instanceManager.instances.length; l++) {
    if(itemCB._instanceManager._instances[l].children[0].value != "-100"){
      itemCB._instanceManager._instances[l].children[0].value = "-100";
    }
  }

  selectedLabelID = [];
  //Label clear end
}

function clearDataType() {
  
  //Type clear
  

  var allCbType = document.querySelectorAll('.checkboxType input[type="checkbox"]');

  for (var x = 0; x < allCbType.length; x++) {

    if (allCbType[x].checked == true) {

      allCbType[x].value = "-100";
      allCbType[x].checked = false;
      allCbType[x].ariaChecked = false;
      allCbType[x].setAttribute("value", "-100");
      allCbType[x].removeAttribute("checked");

      

    }

  }

  var itemCB = guideBridge.resolveNode('panelType');
  

  for (var l = 0; l < itemCB._instanceManager.instances.length; l++) {
    if(itemCB._instanceManager._instances[l].children[0].value != "-100"){
      itemCB._instanceManager._instances[l].children[0].value = "-100";
    }
  }

  selectedTypeID = [];
  //Type clear end
}