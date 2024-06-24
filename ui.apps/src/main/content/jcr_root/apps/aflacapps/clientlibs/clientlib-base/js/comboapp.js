
function checkBoxesDynamicPlans(state, formId)
{
    console.log("checkBoxesDynamicPlans masterApp");
    $.ajax({
        url: "/bin/planData",
        type: 'GET',
        data: {situs:state,appName:'masterApp', formID:formId},
        dataType: 'json', // added data type
        success: function(res) {
            console.log("REs: ", res); 
            
            var dataArray = [];
            if(res.products!= null) {
                 dataArray = res.products;
                 guideBridge.resolveNode("invalidDataPanel").visible=false;
            }

            else if(res.products == null) {
                guideBridge.resolveNode("invalidDataPanel").visible=true;
                guideBridge.resolveNode("MasterApplicationManagment.MasterApplicationManagment_tablePanel.planMaster.plans").visible=false;;
            }
            
            var count;
            var maxCount = 0;
            // console.log("dataArray: ", dataArray);
            for(var s=0; s<dataArray.length; s++) {
                count = dataArray[s]["series-details"].length;
                // console.log("Count: ", count)
                if(count > maxCount) {
                    maxCount = count;
                }
            }
            // console.log("MaxCount: ", maxCount);
            var panelHeightCalc = (80*maxCount);
            var panelHeight = panelHeightCalc + "px";

            var repeatPanel = guideBridge.resolveNode("MasterApplicationManagment.MasterApplicationManagment_tablePanel.planMaster.plans");
			//Reset Panels
			repeatPanel.instanceManager.addInstance();
            var currentCount = repeatPanel.instanceManager.instanceCount;
			for (var m = 0; m < currentCount; m++) {
                if(m!=0)
                {
                repeatPanel.instanceManager.removeInstance(0);
                }

            }

            //Ends

             for (var k = 0; k < dataArray.length; k++) {
                 	if(k!=0)
                    {
                        repeatPanel.instanceManager.addInstance();
                    }

                    repeatPanel.instanceManager.instances[k].planName.value = dataArray[k].name;

                 	//start
                		var panId = "#"+repeatPanel.instanceManager.instances[k].id
                        var panelTag = document.querySelectorAll(panId);


                 		var parEle =  panelTag[0].parentElement;

                 		panelTag[0].parentElement.style.marginRight = "7px";
                 		panelTag[0].parentElement.style.marginLeft = "1px";
                 		panelTag[0].parentElement.style.marginTop = "4px";
                 		panelTag[0].parentElement.style.marginBottom = "4px";
						panelTag[0].style.borderRadius = "12px";
                 		panelTag[0].style.borderColor = "#00A7E1";
						panelTag[0].style.borderWidth = "2px";
                 		panelTag[0].style.borderStyle = "solid";
                 		panelTag[0].style.minHeight = panelHeight; //"300px";
                        //  console.log("panel height: ", panelTag[0].style.minHeight);
                 	//end

					var divLabDynamicID = repeatPanel.instanceManager.instances[k].planNameLabel.id;
                 	var divLavTextDynamicID = repeatPanel.instanceManager.instances[k].planName.id;//added

                 	//start
                 		//console.log("divLavTextDynamicID: ", divLavTextDynamicID);
                  var dynLabTDivID = "#" + divLavTextDynamicID;
                  var dunInput = dynLabTDivID + " input";
                  var tbDivTB = document.querySelectorAll(dynLabTDivID);
                  var inputTag = document.querySelectorAll(dunInput);
                 	var tfSel = dynLabTDivID + " .textField";
                  var tf = document.querySelectorAll(tfSel);
                  tf[0].style.width = "100%";

                  //console.log("tbDivTB: ", tbDivTB);
                 	tbDivTB[0].style.margin = "0px";
                 	tbDivTB[0].style.padding = "0px";
                 	tbDivTB[0].style.background = "#00A7E1";
					tbDivTB[0].style.borderTopLeftRadius ="10px";
                 	tbDivTB[0].style.borderTopRightRadius ="10px";

                 // console.log("inputTag: ",inputTag);
					inputTag[0].style.outline = "none";
                    inputTag[0].style.fontWeight = "700"
                    inputTag[0].style.borderTopLeftRadius = "14px";
                 	inputTag[0].style.borderTopRightRadius = "14px";
					inputTag[0].style.background= "#00A7E1";
                 	inputTag[0].style.color ="white";
					//end
                    var dynLabID = "#" + divLabDynamicID + " > p";

                    var textLabel = dataArray[k].name;

					$(dynLabID).html(textLabel);

                 	// Multiply checkboxes
                // debugger;
					var checkBoxesdataArray = dataArray[k]["series-details"];
                 	var innerrepeatPanel = repeatPanel.instanceManager.instances[k].planOptions;

                 	for (var l = 0; l < checkBoxesdataArray.length; l++) {
                 	if(l!=0)
                    {
                        innerrepeatPanel.instanceManager.addInstance();
                    }

                    var divDynamicID = innerrepeatPanel.instanceManager.instances[l].planNumber.id;

                    var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
                        var cbGroupItemsID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems";
                    	var cbGroupItems = document.querySelectorAll(cbGroupItemsID);
                        cbGroupItems[0].style.textAlign = "center";
                    var checkBoxLabel = checkBoxesdataArray[l]["series-name"];
                       if(checkBoxesdataArray[l].availability)
                       {
                           innerrepeatPanel.instanceManager.instances[l].planNumber.value = "0";
                       }

					$(dynID).html(checkBoxLabel);

                    }



                }


        }
    });
$("#guideContainer-rootPanel-panel-panel_542941593-panel___guide-item-container > div").css({"display":"inline-flex"});

}

function storeData(instance, dataField, planName)
{
    var repeatPanel = guideBridge.resolveNode(instance);
	var planNameField = guideBridge.resolveNode(planName);
    var dataStoreField = guideBridge.resolveNode(dataField);
    dataStoreField.value ="";
	//Calculate Data
			var currentCount = repeatPanel.instanceManager.instanceCount;
			for (var m = 0; m < currentCount; m++) {

                if(repeatPanel.instanceManager.instances[m].planNumber.value == 0)
                {

                    var divDynamicID = repeatPanel.instanceManager.instances[m].planNumber.id;

                    var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";


					var dynamicLabel = $(dynID).html();

                    if(dataStoreField.value == "" || dataStoreField.value == null)
                    {
					dataStoreField.value = dynamicLabel;
                    } else {

					dataStoreField.value = dataStoreField.value + "," + dynamicLabel;
                    }
                }

            }


    //Ends

}

var fetchedData = [];
function checkBoxesDynamicPlansComboApplication(state, formId)
{
    console.log("checkBoxesDynamicPlansComboApplication");
    $.ajax({
        url: "/bin/planData",
        type: 'GET',
        data: {situs:state,appName:'comboApp', formID: formId, methodType: 'Fetch'},
        dataType: 'json', // added data type
        success: function(res) {
       
            // var res= null;
            // var res = {
            //       "situs-state": "CW",
            //       "form-number": "C02205CO",
            //       "products": 
            //       [
            //         {
            //           "id": "CI",
            //           "name": "Critical Illness",
            //           "series-details": [
            //             {
            //               "series-name": "2100",
            //               "availability": false
            //             },
            //             {
            //               "series-name": "2800",
            //               "availability": false
            //             },
            //             {
            //               "series-name": "20000",
            //               "availability": false
            //             },
            //             {
            //               "series-name": "21000",
            //               "availability": false
            //             }
            //           ]
            //         },
            //         {
            //           "id": "HI",
            //           "name": "Hospital Indemnity",
            //           "series-details": [
            //             {
            //               "series-name": "8500",
            //               "availability": false
            //             },
            //             {
            //               "series-name": "8800",
            //               "availability": false
            //             },
            //             {
            //               "series-name": "80000",
            //               "availability": false
            //             }
            //           ]
            //         },
            //         {
            //           "id": "AC",
            //           "name": "Accident",
            //           "series-details": [
            //             {
            //               "series-name": "7700",
            //               "availability": false
            //             },
            //             {
            //               "series-name": "7800",
            //               "availability": false
            //             },
            //             {
            //               "series-name": "70000",
            //               "availability": false
            //             }
            //           ]
            //         },
            //         {
            //           "id": "WD",
            //           "name": "Worklife Disability",
            //           "series-details": [
            //             {
            //               "series-name": "5000",
            //               "availability": false
            //             },
            //             {
            //               "series-name": "50000",
            //               "availability": false
            //             }
            //           ]
            //         },
            //         {
            //           "id": "BE",
            //           "name": "BenExtend",
            //           "series-details": [
            //             {
            //               "series-name": "81000",
            //               "availability": false
            //             },
            //             {
            //               "series-name": "82000",
            //               "availability": false
            //             }
            //           ]
            //         }
            //       ]
            //     };
              
            console.log("REs: ", res);
            var dataArray = [];
           
            if(res != null) {
                 dataArray = res.products;
                 fetchedData = res.products;
                 guideBridge.resolveNode("invalidDataPanelCombo").visible=false;
                 console.log("guideBridge invalidDataPanelCombo :", guideBridge.resolveNode("invalidDataPanelCombo"));
                 guideBridge.resolveNode("ComboApp_tablePanel").visible=true;
                 guideBridge.resolveNode("invalidMessagePanel").visible=false;
            }

            else if(res == null) {
                console.log("Hide");
                guideBridge.resolveNode("invalidDataPanelCombo").visible=true;
                guideBridge.resolveNode("ComboApp_tablePanel").visible=false;
                guideBridge.resolveNode("panelProductsCombo").visible=false;
                guideBridge.resolveNode("invalidMessagePanel").visible=true;
                console.log("panelProductsCombo get:",guideBridge.resolveNode("panelProductsCombo"));

                var currentCount = guideBridge.resolveNode("panelProductsCombo").instanceManager.instanceCount;
                console.log("currentCount: ", currentCount);
                //table reset
                
                    for (var m = 0; m < currentCount; m++) {
                        guideBridge.resolveNode("panelProductsCombo").visible=false;
                         if (m != 0) {
                            console.log("m: ", m);
                            guideBridge.resolveNode("panelProductsCombo").instanceManager.removeInstance(0);
                         }
                    }
            }
            
            //invalidDataPanel
            
            var count;
            var maxCount = 0;
            // console.log("dataArray: ", dataArray);
            for(var s=0; s<dataArray.length; s++) {
                count = dataArray[s]["series-details"].length;
                // console.log("Count: ", count)
                if(count > maxCount) {
                    maxCount = count;
                }
            }
            // console.log("MaxCount: ", maxCount);
            var panelHeightCalc = (80*maxCount);
            var panelHeight = panelHeightCalc + "px";

            var repeatPanel = guideBridge.resolveNode("ComboApplication.ComboApp_tablePanel.planCombo.comboPlans");
			//Reset Panels
			repeatPanel.instanceManager.addInstance();
            var currentCount = repeatPanel.instanceManager.instanceCount;
			for (var m = 0; m < currentCount; m++) {
                if(m!=0)
                {
                repeatPanel.instanceManager.removeInstance(0);
                }

            }

            //Ends

             for (var k = 0; k < dataArray.length; k++) {
                 	if(k!=0)
                    {
                        repeatPanel.instanceManager.addInstance();
                    }

                 	repeatPanel.instanceManager.instances[k].comboPlanName.value = dataArray[k].name;

                 	//start
                		var panId = "#"+repeatPanel.instanceManager.instances[k].id
                        var panelTag = document.querySelectorAll(panId);


                 		var parEle =  panelTag[0].parentElement;

                 		panelTag[0].parentElement.style.marginRight = "7px";
                 		panelTag[0].parentElement.style.marginLeft = "1px";
                 		panelTag[0].parentElement.style.marginTop = "4px";
                 		panelTag[0].parentElement.style.marginBottom = "4px";
						panelTag[0].style.borderRadius = "12px";
                 		panelTag[0].style.borderColor = "#00A7E1";
						panelTag[0].style.borderWidth = "2px";
                 		panelTag[0].style.borderStyle = "solid";
                 		panelTag[0].style.minHeight =panelHeight; //"300px";
                 	//end

	                var divLabDynamicID = repeatPanel.instanceManager.instances[k].planNameComboLabel.id;

                   	var divLavTextDynamicID = repeatPanel.instanceManager.instances[k].comboPlanName.id;

                 	//start
                 		//console.log("divLavTextDynamicID: ", divLavTextDynamicID);
                  var dynLabTDivID = "#" + divLavTextDynamicID;
                  var dunInput = dynLabTDivID + " input";
                  var tbDivTB = document.querySelectorAll(dynLabTDivID);
                  var inputTag = document.querySelectorAll(dunInput);
                 	var tfSel = dynLabTDivID + " .textField";
                  var tf = document.querySelectorAll(tfSel);
                  tf[0].style.width = "100%";

                  //console.log("tbDivTB: ", tbDivTB);
                 	tbDivTB[0].style.margin = "0px";
                 	tbDivTB[0].style.padding = "0px";
                 	tbDivTB[0].style.background = "#00A7E1";
					tbDivTB[0].style.borderTopLeftRadius ="10px";
                 	tbDivTB[0].style.borderTopRightRadius ="10px";

                 // console.log("inputTag: ",inputTag);
					inputTag[0].style.outline = "none";
                    inputTag[0].style.fontWeight = "700"
                    inputTag[0].style.borderTopLeftRadius = "14px";
                 	inputTag[0].style.borderTopRightRadius = "14px";
					inputTag[0].style.background= "#00A7E1";
                 	inputTag[0].style.color ="white";
					//end
                    var dynLabID = "#" + divLabDynamicID + " > p";

                    var textLabel = dataArray[k].name;

					$(dynLabID).html(textLabel);

                 	// Multiply checkboxes
                // debugger;
					var checkBoxesdataArray = dataArray[k]["series-details"];
                    var innerrepeatPanel = repeatPanel.instanceManager.instances[k].planComboOptions;

                 	for (var l = 0; l < checkBoxesdataArray.length; l++) {
                 	if(l!=0)
                    {
                        innerrepeatPanel.instanceManager.addInstance();
                    }

                    var divDynamicID = innerrepeatPanel.instanceManager.instances[l].planComboNumber.id;

                    var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
                        var cbGroupItemsID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems";
                    	var cbGroupItems = document.querySelectorAll(cbGroupItemsID);
                        cbGroupItems[0].style.textAlign = "left"; //center
                        cbGroupItems[0].style.marginLeft = "28px"; //center
                    var checkBoxLabel = checkBoxesdataArray[l]["series-name"];
                       if(checkBoxesdataArray[l].availability)
                       {
                           innerrepeatPanel.instanceManager.instances[l].planComboNumber.value = "0";
                       }

					$(dynID).html(checkBoxLabel);

                    }



                }

  //uncomment
        }
      
    });
$("#guideContainer-rootPanel-panel-panel_542941593-panel___guide-item-container > div").css({"display":"inline-flex"});

}

function storeDataCombo(instance, dataField, planName)
{
    var repeatPanel = guideBridge.resolveNode(instance);
	var planNameField = guideBridge.resolveNode(planName);
    var dataStoreField = guideBridge.resolveNode(dataField);
    dataStoreField.value ="";
	//Calculate Data
			var currentCount = repeatPanel.instanceManager.instanceCount;
			for (var m = 0; m < currentCount; m++) {

                if(repeatPanel.instanceManager.instances[m].planComboNumber.value == 0)
                {

                    var divDynamicID = repeatPanel.instanceManager.instances[m].planComboNumber.id;

                    var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";


					var dynamicLabel = $(dynID).html();

                    if(dataStoreField.value == "" || dataStoreField.value == null)
                    {
					dataStoreField.value = dynamicLabel;
                    } else {

					dataStoreField.value = dataStoreField.value + "," + dynamicLabel;
                    }
                }

            }


    //Ends

}

function storeDataComboAdd(instance, dataField, planName)
{
    var repeatPanel = guideBridge.resolveNode(instance);
	var planNameField = guideBridge.resolveNode(planName);
    var dataStoreField = guideBridge.resolveNode(dataField);
    dataStoreField.value ="";
	//Calculate Data
			var currentCount = repeatPanel.instanceManager.instanceCount;
			for (var m = 0; m < currentCount; m++) {

                if(repeatPanel.instanceManager.instances[m].checkboxAdd.value == 0)
                {

                    var divDynamicID = repeatPanel.instanceManager.instances[m].checkboxAdd.id;

                    var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";


					var dynamicLabel = $(dynID).html();

                    if(dataStoreField.value == "" || dataStoreField.value == null)
                    {
					dataStoreField.value = dynamicLabel;
                    } else {

					dataStoreField.value = dataStoreField.value + "," + dynamicLabel;
                    }
                }

            }


    //Ends

}

function addData(){
    
    var dataset = {
        "metadata": {
          "status": "success",
          "code": "OK",
          "descriptions": [
            {
              "context": "success",
              "type": "info",
              "code": "200 OK",
              "short-description": "generic.success.message",
              "long-description": "Request completed successfully."
            }
          ]
        },
        "data": [
          {
            "product-id": "CI",
            "product-name": "Critical Illness",
            "series-list": [
              "5300",
              "5400",
              "5500"
            ]
          },
          {
            "product-id": "HI",
            "product-name": "Hospital Indemnity",
            "series-list": [
              "7300",
              "7400",
              "7500",
              "7600"
            ]
          },
          {
            "product-id": "AC",
            "product-name": "Accident",
            "series-list": [
              "1200",
              "2400"
            ]
          }
        ]
      };
      var count;
      var maxCount = 0;

      for(var s=0; s<dataset.data.length; s++) {
        count = dataset.data[s]["series-list"].length;
        if(count > maxCount) {
            maxCount = count;
        }
    }

    var panelHeightCalc = (80*maxCount);
    var panelHeight = panelHeightCalc + "px";

    var repeatPanelCB = guideBridge.resolveNode("panelProducts");

    var lengthDS = dataset.data.length;

    //Reset Panels
    repeatPanelCB.instanceManager.addInstance();
    var currentCount = repeatPanelCB.instanceManager.instanceCount;
  //table add
    for (var m = 0; m < currentCount; m++) {
        if (m != 0) {
            repeatPanelCB.instanceManager.removeInstance(0);
        }
    }

    for(var p=0; p<lengthDS; p++) {
        if (p != 0) {
            repeatPanelCB.instanceManager.addInstance(); //add table
          }

          //start
          var panId = "#"+repeatPanelCB.instanceManager.instances[p].id
          var panelTag = document.querySelectorAll(panId);
        //   console.log("panelTag: ", panelTag);

           panelTag[0].parentElement.style.marginRight = "7px";
           panelTag[0].parentElement.style.marginLeft = "1px";
           panelTag[0].parentElement.style.marginTop = "4px";
        //    console.log("1");
           panelTag[0].parentElement.style.marginBottom = "4px";
        //    console.log("2");
           panelTag[0].style.borderRadius = "12px";
           panelTag[0].style.borderColor = "#00A7E1";
           panelTag[0].style.borderWidth = "2px";
           panelTag[0].style.borderStyle = "solid";
           panelTag[0].style.minHeight = panelHeight;
       //end

          //addDataTextBox
          var innerCbPanel = guideBridge.resolveNode("addCbPanel");
          var checkBoxesLen = dataset.data[p]["series-list"].length;
        //   console.log("checkBoxesLen: ", checkBoxesLen);
          //start
          var divLavTextDynamicID = repeatPanelCB.instanceManager.instances[p].addDataTextBox.id;
          repeatPanelCB.instanceManager.instances[p].addDataTextBox.value = dataset.data[p]["product-name"];
                var dynLabTDivID = "#" + divLavTextDynamicID;
                var dunInput = dynLabTDivID + " input";
                
                var tbDivTB = document.querySelectorAll(dynLabTDivID);
                var inputTag = document.querySelectorAll(dunInput);

                var tfSel = dynLabTDivID + " .textField";
                var tf = document.querySelectorAll(tfSel);
                tf[0].style.width = "100%";

                tbDivTB[0].style.margin = "0px";
                tbDivTB[0].style.padding = "0px";
                tbDivTB[0].style.background = "#00A7E1";
               tbDivTB[0].style.borderTopLeftRadius ="10px";
                tbDivTB[0].style.borderTopRightRadius ="10px";

               inputTag[0].style.outline = "none";
               inputTag[0].style.border = "none";
               inputTag[0].style.fontWeight = "700"
               inputTag[0].style.borderTopLeftRadius = "14px";
                inputTag[0].style.borderTopRightRadius = "14px";
               inputTag[0].style.background= "#00A7E1";
                inputTag[0].style.color ="white";
                
          //end
          for(var q=0; q<checkBoxesLen; q++) {

            if(q!=0)
                {
                    innerCbPanel.instanceManager.addInstance();
                }

                console.log("innerCbPanel.instanceManager.instances[q]: ", innerCbPanel.instanceManager.instances[q]);
               var divLabDynamicID = innerCbPanel.instanceManager.instances[q].checkboxAdd.id;
               var dynLabID = "#" + divLabDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
               var textLabel = dataset.data[p]["series-list"][q];
            //    console.log("textLabel: ", textLabel);
               $(dynLabID).html(textLabel);
               console.log($(dynLabID).html(textLabel));
          }
    }
}

function saveComboAfterEdit(state, form){
//  alert("Data saved successfully!");
submittedDataAfterEdit = [];
 submittedDataAfterEdit.push({"products" : fetchedData, "situs-state": state, "form-id": form});
 console.log("submittedDataAfterEdit: ", submittedDataAfterEdit);

    const formData = new FormData();
    formData.append("formData", JSON.stringify(submittedDataAfterEdit[0]));
    var counter = 0;
    for(var l=0; l<submittedDataAfterEdit[0].products.length; l++) {
        for(k=0; k<submittedDataAfterEdit[0].products[l]["series-details"].length; k++) {
            if(submittedDataAfterEdit[0].products[l]["series-details"][k].availability == true) {
                counter++;
            }
        }
    }
    console.log("Counter save: ", counter);
    if(counter > 0) {
        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "/bin/planData?appName=comboApp", true);
        xhttp.send(formData);
    
        xhttp.onreadystatechange = function() {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                var x = JSON.parse(xhttp.responseText);
                console.log("RESP: ", x);
                var mesg = "Thank You!" 
                if(x['form-found-status'] == true) {
                  console.log(['form-found-status'] == true);
                   mesg = "Form cannot be added as selected SITUS, Product(s) and Group Type are already accounted for under a different form. Please remove these options under the current form " + x['available-form-list'] + " prior to adding new filling.";
                }
                else if(x['form-found-status'] == false && x['update-status']==true){
                  console.log(["form-found-status"] == false);  
                  mesg = "Data saved successfully!"
                }
                alert(mesg);
            }
        }
    }
    else if(counter == 0) {
        alert("No series selected!");
    }
   
}

var submittedDataAfterEdit = [];
function getDataComboAfterEdit(state, form, value, lob){
    // console.log(state, form, value, lob);
    
    $(document).off().on('change', '.planComboNumber input[type="checkbox"]', function() {
        console.log("Count ", value);
        var finalValue = []; 
        if(value != null && value != '') {
            finalValue = value.split(",");
            console.log("Final val: ", finalValue);
        }
        
        console.log("LOB: ",lob + "," + finalValue);
    for(var l=0; l<fetchedData.length; l++) {
        if(lob == fetchedData[l].name){
            for(var k=0; k<fetchedData[l]["series-details"].length; k++) {
                var x = fetchedData[l]["series-details"][k]["series-name"];
                console.log("X lob in fetch is: ", x);
                if(finalValue.includes(x)){
                    console.log("True: ", finalValue.includes(x))
                    fetchedData[l]["series-details"][k]["availability"] = true;
                }
                else if(!finalValue.includes(x)) {
                    fetchedData[l]["series-details"][k]["availability"] = false;
                    console.log(x + " " + "Else")
                }
               }
        }
    }
    });
    setTimeout(function() {
        console.log("fetchedData updated: ", fetchedData);
    }, 100);
}

function getFormIds(state) {
    const states = ["AL","AK","AZ","AR","CA","CO","CT","DE","DC","FL","GA","HI","ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY"];
	if(state!=null){

        if(states.indexOf(state) != -1){
            $.ajax({
                url: "/bin/planData",
                type: 'GET',
                data: {situs:state,appName:'comboApp',methodType:'FetchFormIds'},
                dataType: 'json', // added data type
                success: function(resdata) {
                    guideBridge.resolveNode("ComboApp_FormId").items = resdata;
                    guideBridge.resolveNode("ComboApp_FormId").visible=true;
            		guideBridge.resolveNode("ComboApp_Button").visible=true;
            		guideBridge.resolveNode("Add").visible=true;
                    guideBridge.resolveNode("ComboApp_FormId_text").visible=false;
                }
            });
        } else{
            guideBridge.resolveNode("ComboApp_FormId").visible=false;
            guideBridge.resolveNode("ComboApp_Button").visible=false;
            guideBridge.resolveNode("Add").visible=false;
            alert("Invalid state. Please enter two letter valid state");

        }

    }else{
	 	alert("please enter state value");
	}
}
