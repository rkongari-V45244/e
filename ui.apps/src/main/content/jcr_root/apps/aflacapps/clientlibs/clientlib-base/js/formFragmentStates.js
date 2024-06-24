//creation of cb
function setCBAddDynamicFragment() {
  
    var stateList = ["All","Alaska", "Alabama", "Arkansas", "American Samoa", "Arizona", "California", "Colorado", "Connecticut", "D. Columbia", "Delaware", "Florida", "Georgia", "Guam", "Hawaii", "Iowa", "Idaho", "Illinois", "Indiana", "Kansas", "Kentucky", "Louisiana", "Massachusetts", "Maryland", "Maine", "Michigan", "Minnesota", "Missouri", "Mississippi", "Montana", "North Carolina", "North Dakota", "Nebraska", "New Hampshire", "New Jersey", "New Mexico", "Nevada", "New York", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Puerto Rico", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Virginia", "Virgin Islands", "Vermont", "Washington", "Wisconsin", "West Virginia", "Wyoming"];
    var repeatPanel = guideBridge.resolveNode("cbPanelFragment");
    console.log("repeatPanel: ", repeatPanel);
    for (var x=0; x<stateList.length; x++) {
      if (x != 0) {
        repeatPanel.instanceManager.addInstance(); //add panel
      }
    }
      setTimeout(function() {
        //start
        console.log("Fun 2");
       
        var repeatPanel = guideBridge.resolveNode("cbPanelFragment");
        console.log("Panel len: ", repeatPanel.instanceManager.instanceCount);
        for (var k = 0; k < repeatPanel.instanceManager.instanceCount; k++) {
          var state = stateList[k]; 
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

//function to change cb
var statesValue = [];
var selected = [];
var id;
function getStateFromAddFragment(){
  console.log("Fun State Add");
  var stateListAll = ["Alaska", "Alabama", "Arkansas", "American Samoa", "Arizona", "California", "Colorado", "Connecticut", "D. Columbia", "Delaware", "Florida", "Georgia", "Guam", "Hawaii", "Iowa", "Idaho", "Illinois", "Indiana", "Kansas", "Kentucky", "Louisiana", "Massachusetts", "Maryland", "Maine", "Michigan", "Minnesota", "Missouri", "Mississippi", "Montana", "North Carolina", "North Dakota", "Nebraska", "New Hampshire", "New Jersey", "New Mexico", "Nevada", "New York", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Puerto Rico", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Virginia", "Virgin Islands", "Vermont", "Washington", "Wisconsin", "West Virginia", "Wyoming"];
  
  var allcb = document.querySelectorAll('.cbPanelFragment .cbTestAdd input[type="checkbox"]');
  // console.log("All checkboxes: ", allcb);
  $(document).off().on('change', '.cbPanelFragment .cbTestAdd input[type="checkbox"]', function(){
      // console.log("this cb state: ", this.id); //cbPanelFragment //cbFragment
       id = "#"+this.id;
    console.log("Field: ", document.querySelectorAll(id)[0].parentElement.parentElement.children);
     
    if(document.querySelectorAll(id)[0].checked == true) {
        console.log("Selecting");
        var label = document.querySelectorAll(id)[0].parentElement.parentElement.children[1].children[0].innerHTML;
        console.log("Selected initially: ", selected);
        if(selected.includes(label) == false){
          selected.push(label);
          console.log("Pushed if");
          if(label == 'All'){
               console.log("All add"); 
              for(var x=0; x<allcb.length; x++) {
                allcb[x].value = "0";
                allcb[x].checked = "true";
                allcb[x].ariaChecked = true;
                allcb[x].setAttribute("checked", "checked");
                allcb[x].setAttribute("aria-checked", true);
                allcb[x].setAttribute("value", "0");
                // console.log("Checked attr set");
  
                allcb[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment guideItemSelected");
                // console.log("Check class");
                //guideItemSelected
              }
  
              for(var i=0; i<stateListAll.length; i++){
                if(selected.includes(stateListAll[i]) == false){
                    selected.push(stateListAll[i]);
                }
              }
          }
        }
        else if(selected.includes(label) == true){  //
            
            console.log("Selected: ", selected);
            console.log("False else")
            console.log("All cb len: ", allcb.length);
            for(var y=0; y<allcb.length; y++) {
                // console.log("Splice ind 1");
                
                if(allcb[y].parentElement.parentElement.children[1].children[0].innerHTML == label){
                    console.log("In html: ", allcb[y].parentElement.parentElement.children[1].children[0].innerHTML);
                    console.log("Label: ", label);
                    console.log("Splice ind");
                  allcb[y].value = "-100";
                  allcb[y].checked = false;
                  allcb[y].ariaChecked = false;
                  allcb[y].setAttribute("value", "-100");
                  allcb[y].removeAttribute("checked");
                  allcb[y].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment");
                  var indexLabel = selected.indexOf(label);
                  selected.splice(indexLabel, 1);
                  console.log("Spliced");
                  console.log("After splice sel is: ", selected);
                }
                //added
                if(selected.includes("All")){
                    var index = selected.indexOf("All");
                    selected.splice(index, 1);
                   
                        if(allcb[y].parentElement.parentElement.children[1].children[0].innerHTML == 'All'){
                          allcb[y].value = "-100";
                          allcb[y].checked = false;
                          allcb[y].ariaChecked = false;
                          allcb[y].setAttribute("value", "-100");
                          allcb[y].removeAttribute("checked");
                          allcb[y].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment");
                          console.log("All Cb after -ve: ");
                        }
                    
                }
                
            
                
            }
        }
        
        
      }
      else if(document.querySelectorAll(id)[0].checked == false) {
        for(var i=0; i<selected.length; i++) {
            if(selected[i] == document.querySelectorAll(id)[0].parentElement.parentElement.children[1].children[0].innerHTML)
            {
               console.log("Deleting");
               selected.splice(i, 1);
              if(selected.includes("All")){
                var index = selected.indexOf("All");
                console.log("All index is: ", index);
                selected.splice(index, 1)
                //allcb[x].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment guideItemSelected");
                for(var y=0; y<allcb.length; y++) {
                    if(allcb[y].parentElement.parentElement.children[1].children[0].innerHTML == 'All'){
                      allcb[y].value = "-100";
                      allcb[y].checked = false;
                      allcb[y].ariaChecked = false;
                      allcb[y].setAttribute("value", "-100");
                      allcb[y].removeAttribute("checked");
                      allcb[y].parentElement.parentElement.setAttribute("class", "guideCheckBoxItem afCheckBoxItem cbTestAdd guideFieldHorizontalAlignment");
                      console.log("All Cb after -ve: ", allcb);
                    }
                }
              }
            }
       }
   }
  
     console.log("Selected: ", selected);
  });

  setTimeout(function(){
    

  }, 1000)
  
}

//[0].children[0].checked