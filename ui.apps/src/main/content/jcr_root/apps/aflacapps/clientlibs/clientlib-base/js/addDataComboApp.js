  var lobList = [];
  function getAllLobComboApp(){
    for(var p=0; p<res[0].products.length; p++){
        lobList[p] = res[0].products[p].name;
    }
        console.log("lob: ", lobList);
    
  }

  function getDataCombo(state, form, value, lob){
    
    $(document).off().on('change', '.checkboxAdd input[type="checkbox"]', function() {
        console.log("Add cb change");
        for(var l=0; l<lobList.length; l++){
          if(lob == lobList[l]){
              console.log("Value: ", value);
              
              var finalValue = []; 
              if(value != null && value != '') {
                  finalValue = value.split(",");
                  console.log("Final val: ", finalValue);
              }
              console.log("finalValue: ", finalValue);
              for(var i=0; i<res[0].products.length; i++){
                  if(res[0].products[i].name == lob) {
                     for(var j=0; j<res[0].products[i]["series-details"].length; j++) {
                      var x = res[0].products[i]["series-details"][j]["series-name"];
                      console.log("X lob is: ", x);

                      if(finalValue.includes(x)){
                        console.log("True add: ", finalValue.includes(x))
                        res[0].products[i]["series-details"][j]["availability"] = true;
                    }
                    else if(!finalValue.includes(x)) {
                      res[0].products[i]["series-details"][j]["availability"] = false;
                        console.log(x + " " + "Else add")
                    }
                      // for(var k=0; k<finalValue.length; k++) {
                      //     if(finalValue[k] == x) {
                      //         console.log("Final val lob: ",finalValue);
                      //         res[0].products[i]["series-details"][j]["availability"] = true;
                      //     }
                      // }
                     } 
                  }
              }
            }
      }

      setTimeout(function() {
        console.log("Data: ", res[0]);
    }, 100);
    });    
   
  }

  function submitDataComboApp(state, form){

    if(form!=null)
    {
    console.log("RES submit: ", res[0]);
    res[0]["situs-state"] = state;
    res[0]["form-id"] = form;
    console.log("Data form: ", res[0]);
    const formData = new FormData();
    formData.append("formData", JSON.stringify(res[0]));

    var counter = 0;
    for(var l=0; l<res[0].products.length; l++) {
        for(k=0; k<res[0].products[l]["series-details"].length; k++) {
            if(res[0].products[l]["series-details"][k].availability == true) {
                counter++;
            }
        }
    }
    console.log("Counter add: ", counter);

    if(counter > 0){
      var xhttp = new XMLHttpRequest();
      xhttp.open("POST", "/bin/planData?appName=comboApp", true);
      // xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
      xhttp.send(formData);
  
      xhttp.onreadystatechange = function() {
  
        console.log("status: ", xhttp.status)
        if (xhttp.readyState == 4 && xhttp.status == 200) {
          
          var x = JSON.parse(xhttp.responseText);
            console.log("RESP: ",this.response, "+", x);
            var mesg = "Thank You!" 
            if(x['form-found-status'] == true) {
              console.log(['form-found-status'] == true);
               mesg = "Failed to save data! Already found the series added in form " + x['available-form-list'];
            }
            else if(x['form-found-status'] == false && x['update-status']==true){
              console.log(["form-found-status"] == false);  
              mesg = "Data saved successfully!"
            }
            alert(mesg)
            guideBridge.resolveNode("FetchFormIds").visible=true;
			guideBridge.resolveNode("ComboApp_FormId_text").visible=false;
            guideBridge.resolveNode("ComboApp_FormId_text").value="";

            }
          }
  
      var currentCount = guideBridge.resolveNode("panelProductsCombo").instanceManager.instanceCount;
          for (var m = 0; m < currentCount; m++) {
            if (m != 0) {
              console.log("m: ", m);
              guideBridge.resolveNode("panelProductsCombo").instanceManager.removeInstance(0);
            }
          }
          guideBridge.resolveNode("invalidDataPanelCombo").visible=false;
          console.log("After post", state, form);
    }
    else {
      alert("No series selected!");
    }
    }else{
        alert("Form id is mandatory, please fill to proceed.");
    }
    
  
  }
  
  var res= [];
  function addDataCombo(state, form){
    console.log("addDataCombo");
    guideBridge.resolveNode("invalidMessagePanel").visible=false;
    $.ajax({
        url: "/bin/planData",
        type: 'GET',
        data: {situs:state,appName:'comboApp', formID: form, methodType: 'Add'},
        dataType: 'json', // added data type
        success: function(dataset) { 
      console.log("dataset call: ", dataset);
      var datares = [];
      // var dataset = {
      //   "metadata": {
      //     "status": "success",
      //     "code": "OK",
      //     "descriptions": [
      //       {
      //         "context": "success",
      //         "type": "info",
      //         "code": "200 OK",
      //         "short-description": "generic.success.message",
      //         "long-description": "Request completed successfully."
      //       }
      //     ]
      //   },
      //   "data": [
      //     {
      //       "product-id": "CI",
      //       "product-name": "Critical Illness",
      //       "series-list": [
      //         "5300",
      //         "5400",
      //         "5500"
      //       ]
      //     },
      //     {
      //       "product-id": "HI",
      //       "product-name": "Hospital Indemnity",
      //       "series-list": [
      //         "7300",
      //         "7400",
      //         "7500",
      //         "7600"
      //       ]
      //     },
      //     {
      //       "product-id": "AC",
      //       "product-name": "Accident",
      //       "series-list": [
      //         "1200",
      //         "2400",
      //         "22"
      //       ]
      //     }
      //   ]
      // };
        var dataNew= [];
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
        guideBridge.resolveNode("invalidDataPanelCombo").visible=true;


        for(var i=0; i<dataset.data.length; i++){
            
            var series= [];
            for(var j=0; j<dataset.data[i]["series-list"].length; j++) {
                var x = dataset.data[i]["series-list"][j]
                console.log("Prod: ", dataset.data[i]["product-name"], "Ser: ", dataset.data[i]["series-list"][j]);
                
                series.push({ "series-name": dataset.data[i]["series-list"][j]})
                
            }
            dataNew.push({"id": dataset.data[i]["product-id"], "name": dataset.data[i]["product-name"], "series-details": series})
        }
        res.push({"products" : dataNew, "situs-state": state, "form-id": form});
        console.log("res: ", res);

            datares = res[0];
            console.log("Datares : ", datares);
      guideBridge.resolveNode("panelProductsCombo").visible=true;
    setTimeout(function() {
       
        console.log("Vis");
        var count;
        var maxCount = 0;
  
        for(var s=0; s<datares.products.length; s++) {
          count = datares.products[s]["series-details"].length;
          if(count > maxCount) {
              maxCount = count;
          }
      }
  
      var panelHeightCalc = (80*maxCount);
      var panelHeight = panelHeightCalc + "px";
  
      var repeatPanelCB = guideBridge.resolveNode("panelProductsCombo");
  
      var lengthDS = datares.products.length;
  
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
            // console.log("panelTag: ", panelTag);
  
             panelTag[0].parentElement.style.marginRight = "7px";
             panelTag[0].parentElement.style.marginLeft = "1px";
             panelTag[0].parentElement.style.marginTop = "4px";
            //  console.log("1");
             panelTag[0].parentElement.style.marginBottom = "4px";
            //  console.log("2");
             panelTag[0].style.borderRadius = "12px";
             panelTag[0].style.borderColor = "#00A7E1";
             panelTag[0].style.borderWidth = "2px";
             panelTag[0].style.borderStyle = "solid";
             panelTag[0].style.minHeight = panelHeight;
         //end
  
            //addDataTextBox
            var innerCbPanel = guideBridge.resolveNode("addCbPanelCombo");
            var checkBoxesLen = datares.products[p]["series-details"].length;
            // console.log("checkBoxesLen: ", checkBoxesLen);
            //start
            var divLavTextDynamicID = repeatPanelCB.instanceManager.instances[p].addDataTextBox.id;
            repeatPanelCB.instanceManager.instances[p].addDataTextBox.value = datares.products[p].name;
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
                 var textLabel = datares.products[p]["series-details"][q]["series-name"];
                 datares.products[p]["series-details"][q]["availability"] = false;
                 console.log("textLabel: ", textLabel);
                 $(dynLabID).html(textLabel);
            }
            console.log("RES: ", datares);
      }      
    }, 100)
  //uncomment
        }
      
    });      

}