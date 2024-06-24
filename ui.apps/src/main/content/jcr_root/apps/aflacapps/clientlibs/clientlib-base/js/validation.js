function ValidFetch(groupMasterNo) {
  var errors = [];
  window.guideBridge.validate(
    errors,
    "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].inputGroupProposalPanel[0].groupProposalNumber[0].groupProposalNumber[0]"
  );
  if (errors.length === 0) {
    window.location.href = "/content/dam/formsanddocuments/aflacapps/GroupMasterApplication/jcr:content?wcmmode=disabled&groupProposalNo="+groupMasterNo;  
  }
}

/*function ValidCheck(val) {
  if (val === null) {
    window.guideBridge.setFocus(
      "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].inputGroupProposalPanel[0].summary[0].productTable[0]"
    );
    alert("Select atleast one product");
  } 
    else {
    window.guideBridge.setFocus(
      "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].editGroupMasterAppInput[0]",
      "firstItem",
      true
    );
  }
}*/

function ValidCheck(val, formId) {
  if (val === null) {
    window.guideBridge.setFocus(
      "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].inputGroupProposalPanel[0].summary[0].productTable[0]"
    );
    alert("Please select atleast one product");
  }
    else if(formId == null) {
         window.guideBridge.setFocus(
      "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].inputGroupProposalPanel[0].summary[0].selectionTable[0]"
    );
        alert("Please select any product from the table");


   }
    else {
    window.guideBridge.setFocus(
      "guide[0].guide1[0].guideRootPanel[0].panel_11195218551657515460654[0].editGroupMasterAppInput[0]",
      "firstItem",
      true
    );
  }
}


function ValidSitus(Situs, TableSitus){
    if(TableSitus.includes(Situs)){
        console.log("Situs matched");
    }
    else{
        alert("Please select the appropriate form Id");
        var chkbox = guideBridge.resolveNode("formId");
        chkbox.value= "";
    }
}