function checkBoxesDynamicMasterPlans(state)
{
    $.ajax({
        url: "/bin/planData",
        type: 'GET',
        data: {planType:state},
        dataType: 'json', // added data type
        success: function(res) {
            var dataArray = res.groups;
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

					var divLabDynamicID = repeatPanel.instanceManager.instances[k].planNameLabel.id;

                    var dynLabID = "#" + divLabDynamicID + " > p";
                    var textLabel = dataArray[k].name;

					$(dynLabID).html(textLabel);

                 	// Multiply checkboxes
					var checkBoxesdataArray = dataArray[k].plans;
                 	var innerrepeatPanel = repeatPanel.instanceManager.instances[k].planOptions;

                 	for (var l = 0; l < checkBoxesdataArray.length; l++) {
                 	if(l!=0)
                    {
                        innerrepeatPanel.instanceManager.addInstance();
                    }

                    var divDynamicID = innerrepeatPanel.instanceManager.instances[l].planNumber.id;

                    var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
                    var checkBoxLabel = checkBoxesdataArray[l].plan;

					$(dynID).html(checkBoxLabel);

                    }



                }


        }
    });
$("#guideContainer-rootPanel-panel-panel_542941593-panel___guide-item-container > div").css({"display":"inline-flex"});
}

function checkBoxesDynamicComboPlans(state)
{
    $.ajax({
        url: "/bin/planComboData",
        type: 'GET',
        data: {planType:state},
        dataType: 'json', // added data type
        success: function(res) {
            var dataArray = res.groups;
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

					var divLabDynamicID = repeatPanel.instanceManager.instances[k].planNameComboLabel.id;

                    var dynLabID = "#" + divLabDynamicID + " > p";
                    var textLabel = dataArray[k].name;

					$(dynLabID).html(textLabel);

                 	// Multiply checkboxes
					var checkBoxesdataArray = dataArray[k].plans;
                 	var innerrepeatPanel = repeatPanel.instanceManager.instances[k].planComboOptions;

                 	for (var l = 0; l < checkBoxesdataArray.length; l++) {
                 	if(l!=0)
                    {
                        innerrepeatPanel.instanceManager.addInstance();
                    }

                    var divDynamicID = innerrepeatPanel.instanceManager.instances[l].planComboNumber.id;

                    var dynID = "#" + divDynamicID + " > div.guideCheckBoxGroupItems > div > div.guideWidgetLabel.right > label";
                    var checkBoxLabel = checkBoxesdataArray[l].plan;

					$(dynID).html(checkBoxLabel);

                    }



                }


        }
    });
$("#guideContainer-rootPanel-panel-panel_542941593-panel___guide-item-container > div").css({"display":"inline-flex"});
}


function ComboAppMatrixSubmit() {
    guideBridge.getDataXML({
        success: function(guideResultObject) { 
            var req = new XMLHttpRequest();
            req.open("POST", "/bin/ComboAppMatrixSubmit", true);
            req.responseType = "blob"; 
            var postParameters = new FormData();
            postParameters.append("formData", guideResultObject.data);
            postParameters.append("mode", "preview");
            req.send(postParameters);
            req.onreadystatechange = function() {

                if (req.readyState == 4 && req.status == 200) {
                    console.log("servlet called");
                }
            }
        }
    });
}
