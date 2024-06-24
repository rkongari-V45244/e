var CBSubmitData= "";
var CBSubmitDocCase= "";
var CBSubmitLob= "";
var CBSubmitSitus= "";
var CBSubmitFileName= "";

var ComplianceSubmitData= "";
var ComplianceSubmitDocCase= "";
var ComplianceSubmitLob= "";
var ComplianceSubmitSitus= "";
var ComplianceSubmitFileName= "";

function getCaseBuilderData(groupNumber , effectiveDate) {
    var res = JSON.parse(
        $.ajax({
            url: "/bin/GetCaseBuilderToolData",
            type: "GET",
            async: false,
             data: {
                 "groupNumber": groupNumber,
                 "effectiveDate":effectiveDate
             },
            success: function(data) {

                //setting accountInformation Data
                guideBridge.resolveNode("AI_groupNumber").value = data.accountInformation.groupNumber;
                guideBridge.resolveNode("AI_AccountName").value = data.accountInformation.accountName;
                guideBridge.resolveNode("AI_SitusState").value = data.accountInformation.situsState;
                guideBridge.resolveNode("AI_EligibleEmployees").value = data.accountInformation.eligibleEmployees;
                guideBridge.resolveNode("AI_SSN").value = data.accountInformation.ssn;
                guideBridge.resolveNode("AI_AflacEase").value = data.accountInformation.aflacEase;
                guideBridge.resolveNode("AI_DeductionFrequency").value = data.accountInformation.deductionFrequency;
                //guideBridge.resolveNode("AI_DomesticPartners").value = data.accountInformation.partnerEligible;
                guideBridge.resolveNode("AI_Locations").value = data.accountInformation.locations;
                guideBridge.resolveNode("AI_PlatformSoftware").value = data.accountInformation.platform;
                guideBridge.resolveNode("AI_ProductsSold").value = data.accountInformation.productSold;

                //setting Accident Data
                guideBridge.resolveNode("A_PlanName").value = data.accident.case.planName;
                guideBridge.resolveNode("A_PlanLevel").value = data.accident.case.planLevel;
                guideBridge.resolveNode("A_Series").value = data.accident.case.series;
                guideBridge.resolveNode("A_CoverageLevel").value = data.accident.case.coverageLevel;
                guideBridge.resolveNode("A_ApplicationNumber").value = data.accident.case.applicationNumber;
                guideBridge.resolveNode("A_Brochures").value = data.accident.case.brochuresType;
                guideBridge.resolveNode("A_TaxType").value = data.accident.case.taxType;
                guideBridge.resolveNode("A_HoursWorkedPerWeek").value = data.accident.case.hoursWorkedPerWeek;
                guideBridge.resolveNode("A_EligibilityWaitingPeriod").value = data.accident.case.eligibilityWaitingPeriod;
                guideBridge.resolveNode("A_EmployeeIssueAge").value = data.accident.case.employeeIssueAge;
                guideBridge.resolveNode("A_SpouseIssueAge").value = data.accident.case.spouseIssueAge;
                guideBridge.resolveNode("A_ChildIssueAge").value = data.accident.case.childIssueAge;
            }
        })
        .responseText);
}

function containsOnlyNumbers(str) {
  return /^\d+$/.test(str);
}

function getCaseBuilderEnrollmentData(groupNumber , effectiveDate) {
    var validGroupNo = true;
    guideBridge.resolveNode("GroupEnrollmentRecord").visible = false;
	guideBridge.resolveNode("GroupEnrollmentPlatform").visible = false;

    if(groupNumber.length == 5){
        if(containsOnlyNumbers(groupNumber)){
            groupNumber = "00000" + groupNumber;
            guideBridge.resolveNode("GroupNumber").value = groupNumber;
        }
        else {
            alert("This is not a valid Aflac Group number. Please reenter.");
            validGroupNo = false;
        }
    }
    else if(groupNumber.length > 5 && groupNumber.length < 14){
        if(groupNumber.charAt(0) != 'A'){
			alert("This is not a valid Aflac Group number. Please reenter.");
			validGroupNo = false;
        }
        else if(groupNumber.charAt(0) == 'A' && groupNumber.length < 13){
			alert("This is not a valid Aflac Group number. Please reenter.");
			validGroupNo = false;
        }
    }
    else{
        alert("This is not a valid Aflac Group number. Please reenter.");
		validGroupNo = false;
    }

    if(validGroupNo){

        var res = JSON.parse(
            $.ajax({
                url: "/bin/GetCaseBuilderToolEnrollmentDetails",
                type: "GET",
                async: false,
                data: {
                    "groupNumber": groupNumber,
                    "effectiveDate":effectiveDate
                },
                success: function(data) {

                    //setting accountInformation Data
                    guideBridge.resolveNode("GroupEnrollmentRecord").visible = true;
					guideBridge.resolveNode("GroupEnrollmentPlatform").visible = true;
                    guideBridge.resolveNode("GroupEnrollmentRecord").value = data.enrollmentRecord;
                    guideBridge.resolveNode("GroupEnrollmentPlatform").items = data.enrollmentPlatform
                }
            })
            .responseText);
    }


}

var openEnrollmentEndDate;
var openEnrollmentStartDate;
var siteTestInfoDueDate;
function getCaseBuilderToolProductsData(groupNumber , effectiveDate , enrollmentPlatform) {
    var res = JSON.parse(
        $.ajax({
            url: "/bin/GetCaseBuilderToolProductsData",
            type: "GET",
            async: false,
             data: {
                 "groupNumber": groupNumber,
                 "effectiveDate":effectiveDate,
                 "enrollmentPlatform":enrollmentPlatform,
                 "mode":"edit"
             },
            success: function(data) {
                var productArray = data.products.product;
                //setting accountInformation Data
                guideBridge.resolveNode("AI_groupNumber").value = data.accountInformation.groupNumber;
                guideBridge.resolveNode("AI_AccountName").value = data.accountInformation.accountName;
                guideBridge.resolveNode("AI_SitusState").value = data.accountInformation.situsState;
                //guideBridge.resolveNode("AI_EligibleEmployees").value = data.accountInformation.eligibleEmployees;
                guideBridge.resolveNode("AI_SSN").value = data.accountInformation.ssn;
                //guideBridge.resolveNode("AI_AflacEase").value = data.accountInformation.aflacEase;
                guideBridge.resolveNode("AI_DeductionFrequency").value = data.accountInformation.deductionFrequency;
                //guideBridge.resolveNode("AI_DomesticPartners").value = data.accountInformation.partnerEligible;
                guideBridge.resolveNode("AI_Locations").value = data.accountInformation.locations;
                guideBridge.resolveNode("AI_PlatformSoftware").value = data.accountInformation.platform;
                guideBridge.resolveNode("AI_ProductsSold").value = data.accountInformation.productSold;
                guideBridge.resolveNode("AI_CoverageBillingEffDate").value = data.accountInformation.coverageBillingEffDate;
                guideBridge.resolveNode("AI_OpenEnrollmentStartDate").value = data.accountInformation.openEnrollmentStartDate;
                openEnrollmentStartDate = data.accountInformation.openEnrollmentStartDate;
                guideBridge.resolveNode("AI_OpenEnrollmentEndDate").value = data.accountInformation.openEnrollmentEndDate;
                openEnrollmentEndDate = data.accountInformation.openEnrollmentEndDate;
                var dateOpenEnrollmentEndDate = new Date(openEnrollmentEndDate);
                var dateOpenEnrollmentStartDate = new Date(openEnrollmentStartDate);
                var enrollmentDatesValidationStatus = 'Fail';
                if(dateOpenEnrollmentEndDate > dateOpenEnrollmentStartDate) {
                    enrollmentDatesValidationStatus = 'Pass';
                    document.querySelectorAll(".enrollmentDatesValidation input")[0].style.borderColor = "#00a7e1";
                    document.querySelectorAll(".enrollmentDatesValidation input")[0].style.color = "#555";
                }
                else{
                    enrollmentDatesValidationStatus = 'Fail';
                    document.querySelectorAll(".enrollmentDatesValidation input")[0].style.borderColor = "red";
                    document.querySelectorAll(".enrollmentDatesValidation input")[0].style.color = "red";
                }
                
                guideBridge.resolveNode("enrollmentDatesValidation").value = enrollmentDatesValidationStatus;
                guideBridge.resolveNode("AI_SiteTestInfoDueDate").value = data.accountInformation.siteTestInfoDueDate;
                siteTestInfoDueDate = data.accountInformation.siteTestInfoDueDate;
                var dateSiteTestInfoDueDate = new Date(siteTestInfoDueDate);
                var currentDate = new Date();
                var complianceReviewDateValidationStatus = 'Fail';
                if(dateSiteTestInfoDueDate < dateOpenEnrollmentStartDate && currentDate < dateSiteTestInfoDueDate) {
                    complianceReviewDateValidationStatus = 'Pass';
                    document.querySelectorAll(".complianceReviewDateValidation input")[0].style.borderColor = "#00a7e1";
                    document.querySelectorAll(".complianceReviewDateValidation input")[0].style.color = "#555";
                }
                else{
                    complianceReviewDateValidationStatus = 'Fail';
                    document.querySelectorAll(".complianceReviewDateValidation input")[0].style.borderColor = "red";
                    document.querySelectorAll(".complianceReviewDateValidation input")[0].style.color = "red";
                }
                guideBridge.resolveNode("complianceReviewDateValidation").value = complianceReviewDateValidationStatus;
                guideBridge.resolveNode("AI_EnrollmentConditionOptions").value = data.accountInformation.enrollmentConditionOptions;
                //guideBridge.resolveNode("AI_OpenEnrollmentOneToOne").value = data.accountInformation.openEnrollmentOneToOne;
                //guideBridge.resolveNode("AI_OpenEnrollmentCallCenter").value = data.accountInformation.openEnrollmentCallCenter;
                //guideBridge.resolveNode("AI_NewHireSelfService").value = data.accountInformation.newHireSelfService;
                //guideBridge.resolveNode("AI_NewHireOneToOne").value = data.accountInformation.newHireOneToOne;
                //guideBridge.resolveNode("AI_NewHireOECallCenter").value = data.accountInformation.newHireOECallCenter;
                guideBridge.resolveNode("AI_ClientManager").value = data.accountInformation.clientManager;
                guideBridge.resolveNode("AI_ClientManagerEmail").value = data.accountInformation.clientManagerEmail;
                guideBridge.resolveNode("AI_ImpManager").value = data.accountInformation.implementationManager;
                guideBridge.resolveNode("AI_ImpManagerEmail").value = data.accountInformation.implementationManagerEmail;
                guideBridge.resolveNode("AI_PlatformManager").value = data.accountInformation.partnerPlatformManager;
                guideBridge.resolveNode("AI_PlatformManagerEmail").value = data.accountInformation.partnerPlatformManagerEmail;
                guideBridge.resolveNode("FS_ProdFileNaming").value = data.fileSubmission.productionFileNaming;
                guideBridge.resolveNode("FS_testFileNaming").value = data.fileSubmission.testFileNaming;
                guideBridge.resolveNode("FS_prodFileDueDate").value = data.fileSubmission.productionFileDueDate;

                var repeatPanel = guideBridge.resolveNode("Product");
                var repeatPanel2 = guideBridge.resolveNode("PlanDetails");

                //Reset Panels
				repeatPanel.instanceManager.addInstance();
            	var currentCount = repeatPanel.instanceManager.instanceCount;
				for (var m = 0; m < currentCount; m++) {
                	if(m!=0)
                	{
                	repeatPanel.instanceManager.removeInstance(0);
                	}

            	}

                for (var i = 0; i < productArray.length; i++) {
                    if(i!=0)
                   {
                       repeatPanel.instanceManager.addInstance();
                       repeatPanel2.instanceManager.addInstance();
                   }
                    // setting product panel data
                    repeatPanel.instanceManager.instances[i].title = productArray[i].productName;
                    repeatPanel.instanceManager.instances[i].summary = productArray[i].productName;
                    guideBridge.resolveNode("productName").value=productArray[i].productName;
                    //guideBridge.resolveNode("Product").title.value=productArray[i].planName;
                    guideBridge.resolveNode("PlanName").value = productArray[i].planName;
                    guideBridge.resolveNode("PlanLevel").value = productArray[i].planLevel;
                    guideBridge.resolveNode("Series").value = productArray[i].series;
                    //guideBridge.resolveNode("DomesticPartners").value = data.accountInformation.partnerEligible;

                    if((productArray[i].productName=="Accident")||(productArray[i].productName=="Hospital Indemnity")||(productArray[i].productName=="BenExtend")){
						guideBridge.resolveNode("AmtOfferedTable").visible=false;
                    }

                    if((productArray[i].productName=="Disability")||(productArray[i].productName=="Whole Life")||(productArray[i].productName=="Term Life")){
                    	guideBridge.resolveNode("EligibleEmployees").value = data.accountInformation.eligibleEmployees;
                    }else{
						guideBridge.resolveNode("EligibleEmployees").visible=false;
                    }

                    if((productArray[i].productName=="Accident")||(productArray[i].productName=="Hospital Indemnity")){
                        guideBridge.resolveNode("Participation").visible=false;
                        guideBridge.resolveNode("Participation").value="";
                    }else{
                        guideBridge.resolveNode("Participation").value = productArray[i].participationRequirement;
                    }

                    if(productArray[i].productName=="Disability"){
                        guideBridge.resolveNode("CoverageLevel").value = productArray[i].coverageLevel;
                    }else{
						guideBridge.resolveNode("CoverageLevel").visible=false;
                    }

                    // if(productArray[i].coverageLevel==null){
					// 	guideBridge.resolveNode("CoverageLevel").visible=false;
                    // }
                    // else{
                    //     guideBridge.resolveNode("CoverageLevel").value = productArray[i].coverageLevel;
                    // }

                    if(productArray[i].employeeAmountOffered==null){
                        guideBridge.resolveNode("AmtOfferedTableEmployee").visible=false;
						guideBridge.resolveNode("EmployeeIncrements").visible=false;
                        guideBridge.resolveNode("EmployeeMinAmtElect").visible=false;
                        guideBridge.resolveNode("EmployeeMaxAmtElect").visible=false;

                    }
                    else{
                        guideBridge.resolveNode("EmployeeIncrements").value = productArray[i].employeeAmountOffered.amountOffered.increments;
                        guideBridge.resolveNode("EmployeeMinAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.minimumAmtElect;
                        guideBridge.resolveNode("EmployeeMaxAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.maximumAmtElect;

                    }

                    if(productArray[i].spouseAmountOffered==null){
                        guideBridge.resolveNode("AmtOfferedTableSpouse").visible=false;
						guideBridge.resolveNode("SpouseIncrements").visible=false;
                        guideBridge.resolveNode("SpouseMinAmtElect").visible=false;
                        guideBridge.resolveNode("SpouseMaxAmtElect").visible=false;

                    }
                    else{
                        guideBridge.resolveNode("SpouseIncrements").value = productArray[i].spouseAmountOffered.amountOffered.increments;
                        guideBridge.resolveNode("SpouseMinAmtElect").value = productArray[i].spouseAmountOffered.amountOffered.minimumAmtElect;
                        guideBridge.resolveNode("SpouseMaxAmtElect").value = productArray[i].spouseAmountOffered.amountOffered.maximumAmtElect;
                    }
                    if(productArray[i].childAmountOffered==null){
                        guideBridge.resolveNode("AmtOfferedTableChild").visible=false;
						guideBridge.resolveNode("childIncrements").visible=false;
                        guideBridge.resolveNode("childMinAmtElect").visible=false;
                        guideBridge.resolveNode("childMaxAmtElect").visible=false;

                    }
                    else{
                        guideBridge.resolveNode("childIncrements").value = productArray[i].childAmountOffered.amountOffered.increments;
                        guideBridge.resolveNode("childMinAmtElect").value = productArray[i].childAmountOffered.amountOffered.minimumAmtElect;
                        guideBridge.resolveNode("childMaxAmtElect").value = productArray[i].childAmountOffered.amountOffered.maximumAmtElect;
                    }

                    if(productArray[i].tdiStateAmountOffered==null){
                        guideBridge.resolveNode("AmtOfferedTableTdiState").visible=false;
						guideBridge.resolveNode("tdiIncrements").visible=false;
                        guideBridge.resolveNode("tdiMinAmtElect").visible=false;
                        guideBridge.resolveNode("tdiMaxAmtElect").visible=false;
                    }
                    else{
                        guideBridge.resolveNode("tdiIncrements").value = productArray[i].tdiStateAmountOffered.amountOffered.increments;
                        guideBridge.resolveNode("tdiMinAmtElect").value = productArray[i].tdiStateAmountOffered.amountOffered.minimumAmtElect;
                        guideBridge.resolveNode("tdiMaxAmtElect").value = productArray[i].tdiStateAmountOffered.amountOffered.maximumAmtElect;
                    }


					if(productArray[i].childIndividualAmountOffered==null){
                    	guideBridge.resolveNode("AmtOfferedTableChildIndividual").visible=false;
						guideBridge.resolveNode("childIndividualIncrements").visible=false;
                        guideBridge.resolveNode("childIndividualMinAmtElect").visible=false;
                        guideBridge.resolveNode("childIndividualMaxAmtElect").visible=false;
                    }
                    else{
                        guideBridge.resolveNode("childIndividualIncrements").value = productArray[i].childIndividualAmountOffered.amountOffered.increments;
                        guideBridge.resolveNode("childIndividualMinAmtElect").value = productArray[i].childIndividualAmountOffered.amountOffered.minimumAmtElect;
                        guideBridge.resolveNode("childIndividualMaxAmtElect").value = productArray[i].childIndividualAmountOffered.amountOffered.maximumAmtElect;
                    }
                      

                    if(productArray[i].childTermAmountOffered==null){
                    	guideBridge.resolveNode("AmtOfferedTableChildTerm").visible=false;
						guideBridge.resolveNode("childTermIncrements").visible=false;
                        guideBridge.resolveNode("childTermMinAmtElect").visible=false;
                        guideBridge.resolveNode("childTermMaxAmtElect").visible=false;
                    }
                    else{
                        guideBridge.resolveNode("childTermIncrements").value = productArray[i].childTermAmountOffered.amountOffered.increments;
                        guideBridge.resolveNode("childTermMinAmtElect").value = productArray[i].childTermAmountOffered.amountOffered.minimumAmtElect;
                        guideBridge.resolveNode("childTermMaxAmtElect").value = productArray[i].childTermAmountOffered.amountOffered.maximumAmtElect;
                    }    


                    if(productArray[i].initialEnrollment==null){
                        guideBridge.resolveNode("initialEmployementText").visible=false;
                        guideBridge.resolveNode("initialEmployeement_employee").visible=false;
                        guideBridge.resolveNode("initialEmployeement_spouse").visible=false;
                        guideBridge.resolveNode("initialEmployeement_spouseCoverage").visible=false;

                    }
                    else if((productArray[i].initialEnrollment!=null) &&(productArray[i].productName=="Worksite Disability")){
                        guideBridge.resolveNode("initialEmployeement_spouse").visible=false;
                        guideBridge.resolveNode("initialEmployeement_spouseCoverage").visible=false;
                        guideBridge.resolveNode("initialEmployeement_employee").value = productArray[i].initialEnrollment.employee;
                        guideBridge.resolveNode("initialEmployeement_spouse").value ="";
                        guideBridge.resolveNode("initialEmployeement_spouseCoverage").value ="";
                    }
                    else{
                        guideBridge.resolveNode("initialEmployeement_employee").value = productArray[i].initialEnrollment.employee;
                        guideBridge.resolveNode("initialEmployeement_spouse").value = productArray[i].initialEnrollment.spouse;
                        guideBridge.resolveNode("initialEmployeement_spouseCoverage").value = productArray[i].initialEnrollment.spouseCoverage;
                    }

                    if(productArray[i].benefitType==null){
						guideBridge.resolveNode("BenefitType").visible=false;
                    }
                    else{
                        guideBridge.resolveNode("BenefitType").value = productArray[i].benefitType;
                    }
                    if(productArray[i].benefitPeriod==null){
						guideBridge.resolveNode("BenefitPeriod").visible=false;
                    }
                    else{
                        guideBridge.resolveNode("BenefitPeriod").value = productArray[i].benefitPeriod;
                    }
                    if(productArray[i].eliminationPeriod==null){
						guideBridge.resolveNode("EliminationPeriod").visible=false;
                    }
                    else{
                        guideBridge.resolveNode("EliminationPeriod").value = productArray[i].eliminationPeriod;
                    }
                    if(productArray[i].term==null){
						guideBridge.resolveNode("Term").visible=false;
                    }
                    else{
                        guideBridge.resolveNode("Term").value = productArray[i].term;
                    }
                    // if(productArray[i].optionalRider==null){
					// 	guideBridge.resolveNode("OptionalRider").visible=false;
                    // }
                    // else{
                    //     guideBridge.resolveNode("OptionalRider").value = productArray[i].optionalRider;
                    // }

                    // if(productArray[i].progressiveRider==null){
					// 	guideBridge.resolveNode("ProgressiveRider").visible=false;
                    // }
                    // else{
                    //     guideBridge.resolveNode("ProgressiveRider").value = productArray[i].progressiveRider;
                    // }
                    if(productArray[i].tobacco==null){
						guideBridge.resolveNode("TobaccoStatus").visible=false;
                        guideBridge.resolveNode("TobaccoStatusDetermined").visible=false;
                    }
                    else{
                        guideBridge.resolveNode("TobaccoStatus").value = productArray[i].tobacco.tobaccoStatus;
                        guideBridge.resolveNode("TobaccoStatusDetermined").value = productArray[i].tobacco.tobaccoStatusDetermined;
                    }
                    if(productArray[i].platformDriven==null){
						guideBridge.resolveNode("PlatformDriven").visible=false;
                    }
                    else{
                        guideBridge.resolveNode("PlatformDriven").value = productArray[i].platformDriven;
                    }
					if(productArray[i].ageCalculated==null){
						guideBridge.resolveNode("AgeCalculated").visible=false;
                    }
                    else{
                        guideBridge.resolveNode("AgeCalculated").value = productArray[i].ageCalculated;
                    }
                    if(productArray[i].ageCalculation==null){
						guideBridge.resolveNode("AgeCalculation").visible=false;
                    }
                    else{
                        guideBridge.resolveNode("AgeCalculation").value = productArray[i].ageCalculation;
                    }

                    if(productArray[i].employeeIssueAge==null){
						guideBridge.resolveNode("EmployeeIssueAge").visible=false;
                    }
                    else{
						guideBridge.resolveNode("EmployeeIssueAge").value = productArray[i].employeeIssueAge;
                    }

                    if(productArray[i].spouseIssueAge==null){
						guideBridge.resolveNode("SpouseIssueAge").visible=false;
                    }
                    else{
						guideBridge.resolveNode("SpouseIssueAge").value = productArray[i].spouseIssueAge;
                    }

                    if(productArray[i].childIssueAge==null){
						guideBridge.resolveNode("ChildIssueAge").visible=false;
                    }
                    else{
						guideBridge.resolveNode("ChildIssueAge").value = productArray[i].childIssueAge;
                    }

                    // if(productArray[i].participationRequirement==null){
					// 	guideBridge.resolveNode("Participation").visible=false;
                    // }
                    // else{
					// 	guideBridge.resolveNode("Participation").value = productArray[i].participationRequirement;
                    // }

                    guideBridge.resolveNode("Date").value = productArray[i].hireDate;
                    guideBridge.resolveNode("ApplicationNumber").value = productArray[i].applicationNumber;
                    //guideBridge.resolveNode("Brochures").value = productArray[i].brochuresType;
                    guideBridge.resolveNode("TaxType").value = productArray[i].taxType;
                    guideBridge.resolveNode("HoursWorkedPerWeek").value = productArray[i].hoursWorkedPerWeek;
                    guideBridge.resolveNode("EligibilityWaitingPeriod").value = productArray[i].eligibilityWaitingPeriod;

               }
            }
        })
        .responseText);
}


function submitCase(dummyInput) {

    //validation
    var errors = [];
    guideBridge.resolveNode("fileNetMsg").visible = false;
    window.guideBridge.validate(errors, guideBridge.resolveNode("AI").somExpression);
    window.guideBridge.validate(errors, guideBridge.resolveNode("AI-ImportantDates").somExpression);
    window.guideBridge.validate(errors, guideBridge.resolveNode("AI-EnrollmentConditions").somExpression);
    window.guideBridge.validate(errors, guideBridge.resolveNode("AI-GroupContacts").somExpression);
    window.guideBridge.validate(errors, guideBridge.resolveNode("FileSubmission").somExpression);
    window.guideBridge.validate(errors, guideBridge.resolveNode("Locations").somExpression); 
    window.guideBridge.validate(errors, guideBridge.resolveNode("Products").somExpression); 
    var startDateTime = guideBridge.resolveNode("startTime").value;
    if (errors.length === 0) {
     guideBridge.getDataXML({
        success: function(guideResultObject) {
            var req = new XMLHttpRequest();
            req.open("POST", "/bin/GetCaseBuilderToolProductsData", true);
            //req.responseType = "blob";
            var postParameters = new FormData();
            postParameters.append("formData", guideResultObject.data);
            postParameters.append("mode", "save");
            postParameters.append("startDateTime", startDateTime);
            req.send(postParameters);
            req.onreadystatechange = function() {
                // if (req.readyState == 4 && req.status == 200) {
                //     var blob = new Blob([this.response], {
                //             type: "application/pdf"
                //         }),
                //         newUrl = URL.createObjectURL(blob);
                //     window.open(newUrl, "_blank", "menubar=yes,resizable=yes,scrollbars=yes");
                //     document.getElementById("guideContainerForm").style.filter="blur()";
                // 	loader.setAttribute('class', 'loader-disable');
                // }
                if (req.readyState == 4 && req.status == 200) {
                    var x = JSON.parse(req.responseText);
                    if(x["status"] == true) {
                      //alert("Data added successfully!");
                      guideBridge.resolveNode("saveSuccessMessage").visible = true;
                      guideBridge.resolveNode("saveFailureMessage").visible = false;
                      guideBridge.resolveNode("coverageEffDateFailureMessage").visible = false;
                    //   guideBridge.resolveNode("GenerateWithCompliance").enabled = true;
                    //   guideBridge.resolveNode("GenerateWithoutCompliance").enabled = true;
                      guideBridge.resolveNode("GenerateCaseBuildGuide").enabled = true;
                      updateCoverageEffDateFirstScreen();
                      hideSaveMessage();
                     }
                     else if(x["status"] == false && x["validation-message"] != null) {
                        guideBridge.resolveNode("coverageEffDateFailureMessage").visible = true;
                        document.querySelectorAll(".coverageEffDateFailureMessage span")[0].children[0].innerHTML = "Unable to save data : " +  x["validation-message"];
                        guideBridge.resolveNode("saveSuccessMessage").visible = false;
                        guideBridge.resolveNode("saveFailureMessage").visible = false;
                        hideSaveMessage();
                       }
                     else{
                      //alert("Failed to add data!");
                      guideBridge.resolveNode("saveSuccessMessage").visible = false;
                      guideBridge.resolveNode("saveFailureMessage").visible = true;
                      guideBridge.resolveNode("coverageEffDateFailureMessage").visible = false;
                      hideSaveMessage();
                     }
                }
            }
        },
        error : function (guideResultObject) {
            console.error("API Failed");
            var msg = guideResultObject.getNextMessage();
            while (msg != null) {
                console.error(msg.message);
                msg = guideResultObject.getNextMessage();
            }
       }
    });
}else {
    var errorField = errors[0].getFocus();
    window.guideBridge.setFocus(errorField);
}
}

function exportExcel(exportAs, docCase,productSold,situsState) {
    
    var StartEnrollmentDate = guideBridge.resolveNode("AI_OpenEnrollmentStartDate").value;
    var StartDate = new Date(StartEnrollmentDate);
    var thresholdDate = new Date("2023-10-01");
    var docId = "";

    CBSubmitData= "";
    CBSubmitDocCase= "";
    CBSubmitLob= "";
    CBSubmitSitus= "";
    CBSubmitFileName= "";
    
    if(StartDate < thresholdDate || StartEnrollmentDate == null){
        docCase = "casebuidGuide";
    }
    else if(StartDate >= thresholdDate){
        docCase = "withoutCompliance";
    }
    exportAs = "PDF";
    guideBridge.resolveNode("fileNetMsg").visible = false;

    var errors = [];
    window.guideBridge.validate(errors, guideBridge.resolveNode("AI").somExpression);
    window.guideBridge.validate(errors, guideBridge.resolveNode("AI-ImportantDates").somExpression);
    window.guideBridge.validate(errors, guideBridge.resolveNode("AI-EnrollmentConditions").somExpression);
    window.guideBridge.validate(errors, guideBridge.resolveNode("AI-GroupContacts").somExpression);
    window.guideBridge.validate(errors, guideBridge.resolveNode("FileSubmission").somExpression);
    window.guideBridge.validate(errors, guideBridge.resolveNode("Locations").somExpression); 
    window.guideBridge.validate(errors, guideBridge.resolveNode("Products").somExpression);   

    if (errors.length === 0) {
        var loader = document.createElement('div');
        loader.setAttribute('id', 'previewLoader');
        loader.setAttribute('class', 'loader');
        loader.rel = 'stylesheet';
        loader.type = 'text/css';
        loader.href = '/css/loader.css';
        document.getElementsByTagName('BODY')[0].appendChild(loader);
        document.getElementById("guideContainerForm").style.filter = "blur(10px)";
        document.getElementById("guideContainerForm").style.pointerEvents = "none";
        
        if(guideBridge.resolveNode("AI_OpenEnrollmentStartDate").value != null && guideBridge.resolveNode("AI_OpenEnrollmentEndDate").value != null){
            
            //var fileName = guideBridge.resolveNode("AI_AccountName").value + '_' + guideBridge.resolveNode("AI_groupNumber").value + '_' + guideBridge.resolveNode("EffectiveDate").value + '_' + new Date().toJSON().slice(0, 10) + '.xlsx';
            var lob = productsSoldValues(productSold);
            
            guideBridge.getDataXML({
                success: function(guideResultObject) {
                    var req = new XMLHttpRequest();
                    req.open("POST", "/bin/CaseBuilderToolSubmitServlet", true);
                    //req.responseType = "blob";
                    var postParameters = new FormData();
                    postParameters.append("formData", guideResultObject.data);
                    postParameters.append("mode", exportAs);
                    postParameters.append("case", docCase);
                    postParameters.append("lob", lob);
                    postParameters.append("situsState", situsState);
                    postParameters.append("activity", "generate");
                    req.send(postParameters);
                    req.onreadystatechange = function() {
                        if (req.readyState == 4 && req.status == 200) {
                            guideBridge.resolveNode("fileNetMsg").visible = true;
                            docId = JSON.parse(req.responseText)["DocumentID"];
                            var msg = "<p><span class=\"greenColorText\"><b>Success : Case Build Guide is successfully generated and saved to FileNet with ID: " + docId + "</b></span></p>";
                            guideBridge.resolveNode("fileNetMsg").value = msg;
                            document.getElementById("guideContainerForm").style.filter = "blur()";
                            document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                            loader.setAttribute('class', 'loader-disable');

                            CBSubmitData= guideResultObject.data;
                            CBSubmitDocCase= docCase;
                            CBSubmitLob= lob;
                            CBSubmitSitus= situsState;
                            guideBridge.resolveNode("downloadCaseBuildGuide").visible= true;
                            //readCaseBuildFile(guideResultObject.data, docCase, lob, situsState);
                        }
                        else if (req.readyState == 4 && req.status == 400) {
                            var msg = JSON.parse(req.responseText)["message"];
                            msg = "<p><span class=\"redColorText\"><b>" + msg + "</b></span></p>";
                            guideBridge.resolveNode("fileNetMsg").visible = true;
                            guideBridge.resolveNode("fileNetMsg").value = msg;
                            document.getElementById("guideContainerForm").style.filter = "blur()";
                            document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                            loader.setAttribute('class', 'loader-disable');

                            CBSubmitData= guideResultObject.data;
                            CBSubmitDocCase= docCase;
                            CBSubmitLob= lob;
                            CBSubmitSitus= situsState;

                            guideBridge.resolveNode("downloadCaseBuildGuide").visible= true;
                            // readCaseBuildFile(guideResultObject.data, docCase, lob, situsState);
                        }
                        else if(req.readyState == 4) {
                            console.log("Failure..");
                            document.getElementById("guideContainerForm").style.filter = "blur()";
                            document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                            loader.setAttribute('class', 'loader-disable');
                        }
                    }
                }
            });
        }
        else {
            document.getElementById("guideContainerForm").style.filter = "blur()";
            document.getElementById("guideContainerForm").style.pointerEvents = "auto";
            loader.setAttribute('class', 'loader-disable');
            //alert('Please fill all the date fields.');
            window.guideBridge.setFocus(guideBridge.resolveNode("AI-ImportantDates").somExpression); 
            //guideBridge.resolveNode("emptyDateFieldsError").visible = true;
        }

    }else {
        document.getElementById("guideContainerForm").style.filter = "blur()";
        document.getElementById("guideContainerForm").style.pointerEvents = "auto";
        loader.setAttribute('class', 'loader-disable');
        var errorField = errors[0].getFocus();
        window.guideBridge.setFocus(errorField);
    }
}

function readCaseBuildFile(){
    
    var groupName = guideBridge.resolveNode('AI_AccountName').value;
    var CED = guideBridge.resolveNode('EffectiveDate').value;

    var _date = CED.split('-');
    var dateObj = {month: _date[1], day: _date[2], year: _date[0]};

    CED = dateObj.month + '-' + dateObj.day + '-' + dateObj.year;

    var formData = CBSubmitData;
    var docCase = CBSubmitDocCase;
    var lob = CBSubmitLob;
    var situs = CBSubmitSitus;
    var fileName = groupName+" "+CED+" Build Requirements Document";

    var loader = document.createElement('div');
    loader.setAttribute('id', 'previewLoader');
    loader.setAttribute('class', 'loader');
    loader.rel = 'stylesheet';
    loader.type = 'text/css';
    loader.href = '/css/loader.css';
    document.getElementsByTagName('BODY')[0].appendChild(loader);
    document.getElementById("guideContainerForm").style.filter = "blur(10px)";
    document.getElementById("guideContainerForm").style.pointerEvents = "none";
    guideBridge.getDataXML({
        success: function(guideResultObject) {
            var req = new XMLHttpRequest();
            req.open("POST", "/bin/CaseBuilderToolSubmitServlet", true);
            req.responseType = "blob";
            var postParameters = new FormData();
            postParameters.append("formData", formData);
            postParameters.append("mode", "PDF");
            postParameters.append("case", docCase);
            postParameters.append("lob", lob);
            postParameters.append("situsState", situs);
            postParameters.append("activity", "read");
            req.send(postParameters);
            req.onreadystatechange = function () {

                if (req.readyState == 4 && req.status == 200) {
                    // var blob = new Blob([this.response], {
                    //     type: "application/pdf"
                    // }),
                    // newUrl = URL.createObjectURL(blob);
                    // window.open(newUrl, "_blank", "menubar=yes,resizable=yes,scrollbars=yes");
                    var a;
                    a = document.createElement('a');
                    a.href = window.URL.createObjectURL(req.response);
                    a.download = fileName;
                    a.style.display = 'none';
                    document.body.appendChild(a);
                    a.click();
                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                }  
                else if(req.readyState == 4) {
                    console.log("Read Failure..");
                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                }  
            };
        }
    });
}

function hideAddDelIcons(input) {
    var buttons = document.querySelectorAll("div.repeatableButtons button");
    for(var val=0; val<buttons.length; val++) {
        buttons[val].style.display = 'none';
    }

}

var caseBuildPlatforms;
var platformDetails;
function fetchPlatformsData(groupNumber, effectiveDate){
    caseBuildPlatforms=[];
    platformDetails=[];

    var res = JSON.parse(
        $.ajax({
            url: "/bin/GetCaseBuilderToolProductsData",
            type: "GET",
            async: false,
            data: {
                "groupNumber": groupNumber,
                "effectiveDate":effectiveDate,
                "mode":"fetchPlatforms"
            },
            success: function (data) {
                if (data != null) {
                    caseBuildPlatforms=data.enrollmentPlatform;
                    platformDetails=data.enrollmentDetails;
                    populatePlatformData();
                }
            }
        })
    .responseText);
}

var caseBuildResponse;
function fetchCaseBuilderData(groupNumber, effectiveDate,enrollmentPlatform) {
    caseBuildResponse="";
        var res = JSON.parse(
            $.ajax({
                url: "/bin/GetCaseBuilderToolProductsData",
                type: "GET",
                async: false,
                data: {
                    "groupNumber": groupNumber,
                    "effectiveDate":effectiveDate,
                    "enrollmentPlatform":enrollmentPlatform,
                    "mode":"edit"
                },
                success: function (data) {
                    var currentdate = new Date();
					var day = currentdate.getDate()+"";
                    day = day.length == 1 ? "0"+day : day;
                    var month = (currentdate.getMonth() + 1)+"";
                    month = month.length == 1 ? "0"+month : month;
					var hours = currentdate.getHours()+"";
                    hours = hours.length == 1 ? "0"+hours : hours;
                    var min = currentdate.getMinutes()+"";
                    min = min.length == 1 ? "0"+min : min;
                    var sec = currentdate.getSeconds()+"";
                    sec = sec.length == 1 ? "0"+sec : sec;
                    var zone = Intl.DateTimeFormat().resolvedOptions().timeZone;
                    var startTime = currentdate.getFullYear() + "-" + month  + "-"  + day + " "  + hours + ":"  + min + ":" + sec + " " + zone;
                    guideBridge.resolveNode("startTime").value = startTime;
                    // data=null;
                    if (data == null) {
                        guideBridge.resolveNode("formCase").value = 'Add';
                        guideBridge.resolveNode("caseBuildVersion").value = 0;
                        guideBridge.resolveNode("AddNewCaseBuildPanel").visible = true;
                        guideBridge.resolveNode("AvailableProductsPanel").visible = false;
                        guideBridge.resolveNode("nextAdd").visible = false;
                        guideBridge.resolveNode("AccidentCheckBox").value = "";
                        guideBridge.resolveNode("HospitalIdenmityCheckbox").value = "";
                        guideBridge.resolveNode("BenExtendCheckbox").value = "";
                        guideBridge.resolveNode("CriticalIllnessCheckbox").value = "";
                        guideBridge.resolveNode("DisablityCheckbox").value = "";
                        guideBridge.resolveNode("WholeLifeCheckbox").value = "";
                        guideBridge.resolveNode("TermLifeCheckbox").value = "";
                        guideBridge.resolveNode("Termto120Checkbox").value = "";
                    }
                    else {            
                        caseBuildResponse = data;
                        guideBridge.resolveNode("formCase").value = 'Edit';
                        guideBridge.resolveNode("caseBuildVersion").value = caseBuildResponse.version;
                        guideBridge.resolveNode("deletedProducts").value = '';
                        guideBridge.resolveNode("caseBuildExistPanel").visible = true;
                        document.querySelectorAll(".caseBuildExistTest")[0].innerHTML = "Case already exist with the following products: " +data.accountInformation.productSold+ ". <br>Please select Next if no change to products is needed, otherwise modify products and continue with any other needed changes."
                        document.querySelectorAll(".caseBuildExistTest")[0].style.textAlign = "left";
                        guideBridge.resolveNode("AccidentCheckBoxEditCase").value = '';
                        guideBridge.resolveNode("HospitalIdenmityCheckboxEditCase").value = '';
                        guideBridge.resolveNode("BenExtendCheckboxEditCase").value = '';
                        guideBridge.resolveNode("CriticalIllnessCheckboxEditCase").value = '';
                        guideBridge.resolveNode("DisablityCheckboxEditCase").value = '';
                        guideBridge.resolveNode("TermLifeCheckboxEditCase").value = '';
                        guideBridge.resolveNode("WholeLifeCheckboxEditCase").value = '';
                        guideBridge.resolveNode("Termto120CheckboxEditCase").value = '';
                        setProductCheckBoxValues(data.accountInformation.productSold);
                        guideBridge.resolveNode("next").visible = true;
                        guideBridge.resolveNode("reset1662714035296").visible = true;

                    }
                }
            })
        .responseText);
}

var locationsData=null;
function getCaseBuilderToolProductsDataEdit(groupNumber, effectiveDate, enrollmentPlatform) {
    var data = caseBuildResponse;
    var productArray = data.products.product;
    locationsData=null;
    locationsData = data.locations.location;
    var newSelectedProducts = guideBridge.resolveNode("selectedProductsEditCase").value;
    var deletedProducts = guideBridge.resolveNode("deletedProducts").value;
    //formCase
    if (guideBridge.resolveNode("formCase").value == "Update"){
        var res = JSON.parse(
            $.ajax({
                url: "/bin/GetCaseBuilderToolProductsData",
                type: "GET",
                async: false,
                data: {
                    "mode": "Update",
                    "addedProducts": newSelectedProducts,
                    "deletedProducts": deletedProducts,
                    "groupNumber": groupNumber,
                    "effectiveDate":effectiveDate,
                    "enrollmentPlatform":enrollmentPlatform
                },
                success: function (response) {
                    //setting accountInformation Data
                    guideBridge.resolveNode("AI_groupNumber").value = response.accountInformation.groupNumber;
                    guideBridge.resolveNode("AI_groupType").value = response.accountInformation.groupType;
                    guideBridge.resolveNode("AI_AccountName").value = response.accountInformation.accountName;
                    guideBridge.resolveNode("AI_SitusState").value = response.accountInformation.situsState;
                    guideBridge.resolveNode("AI_SSN").value = response.accountInformation.ssn;
                    guideBridge.resolveNode("AI_DeductionFrequency").value = response.accountInformation.deductionFrequency;
                    guideBridge.resolveNode("AI_PlatformSoftware").value = response.accountInformation.platform;
                    guideBridge.resolveNode("AI_ProductsSold").value = response.accountInformation.productSold;
                    guideBridge.resolveNode("AI_CoverageBillingEffDate").value = response.accountInformation.coverageBillingEffDate;
                    guideBridge.resolveNode("AI_OpenEnrollmentStartDate").value = response.accountInformation.openEnrollmentStartDate;
                    //showGenerateButtons(data.accountInformation.openEnrollmentStartDate);
                    disableCertificationLanguagePDF(response.accountInformation.openEnrollmentStartDate);
                    guideBridge.resolveNode("AI_OpenEnrollmentEndDate").value = response.accountInformation.openEnrollmentEndDate;
                    //guideBridge.resolveNode("AI_SiteTestInfoDueDate").value = data.accountInformation.siteTestInfoDueDate;
                    guideBridge.resolveNode("AI_EnrollmentConditionOptions").value=response.accountInformation.enrollmentConditionOptions;
                    guideBridge.resolveNode("AI_NewHire").value = response.accountInformation.newHire;
                    guideBridge.resolveNode("AI_ClientManager").value = response.accountInformation.clientManager;
                    guideBridge.resolveNode("AI_ClientManagerEmail").value = response.accountInformation.clientManagerEmail;
                    guideBridge.resolveNode("AI_ImpManager").value = response.accountInformation.implementationManager;
                    guideBridge.resolveNode("AI_ImpManagerEmail").value = response.accountInformation.implementationManagerEmail;
                    guideBridge.resolveNode("AI_PlatformManager").value = response.accountInformation.partnerPlatformManager;
                    guideBridge.resolveNode("AI_PlatformManagerEmail").value = response.accountInformation.partnerPlatformManagerEmail;
                    guideBridge.resolveNode("FS_ProdFileNaming").value = response.fileSubmission.productionFileNaming;
                    guideBridge.resolveNode("FS_testFileNaming").value = response.fileSubmission.testFileNaming;
                    guideBridge.resolveNode("FS_prodFileDueDate").value = response.fileSubmission.productionFileDueDate;
                    guideBridge.resolveNode("FS_TestFileDueDate").value = response.fileSubmission.testFileDueDate;
                    guideBridge.resolveNode("AI_EligibleEmployees").value = response.accountInformation.eligibleEmployees;
                    guideBridge.resolveNode("AI_pdfDirections").value = response.accountInformation.pdfDirections;
                    guideBridge.resolveNode("HoursWorkedPerWeek").value = response.accountInformation.hoursWorkedPerWeek;
                    guideBridge.resolveNode("EligibilityWaitingPeriod").value = response.accountInformation.eligibilityWaitingPeriod;
                    guideBridge.resolveNode("DomesticPartners").value = response.accountInformation.partnerEligible;
                    guideBridge.resolveNode("AI_CoverageBillingEffDate").enabled = false;
                    guideBridge.resolveNode("validateCoverageEffDate").enabled = false;
                    guideBridge.resolveNode("IsCoverageEffDateChanged").value = "No";
        
                    if(response.locations.location[0].locationCode!=null){
                        guideBridge.resolveNode("locationRadioButton").value="Yes";
                        fillLocationsTable(response.locations.location);
                    }
                    else {
                        guideBridge.resolveNode("locationRadioButton").value="No";
                    }

                    if(guideBridge.resolveNode("AI_NewHire").value==guideBridge.resolveNode("AI_EnrollmentConditionOptions").value){
                        checkEnrollmentOptionValues(guideBridge.resolveNode("AI_EnrollmentConditionOptions").value,guideBridge.resolveNode("AI_NewHire").value);
                    }

                    var productArray = response.products.product;
                    var repeatPanel = guideBridge.resolveNode("Product");
                    var repeatPanel2 = guideBridge.resolveNode("PlanDetails");
    
                    //Reset Panels
                    repeatPanel.instanceManager.addInstance();
                    var currentCount = repeatPanel.instanceManager.instanceCount;
                    for (var m = 0; m < currentCount; m++) {
                        if (m != 0) {
                            repeatPanel.instanceManager.removeInstance(0);
                        }
    
                    }
    
                    for (var i = 0; i < productArray.length; i++) {
                        if (i != 0) {
                            repeatPanel.instanceManager.addInstance();
                            repeatPanel2.instanceManager.addInstance();
                        }

                        // setting product panel data

                        repeatPanel.instanceManager.instances[i].title = productArray[i].productName;
                        repeatPanel.instanceManager.instances[i].summary = productArray[i].productName;
                        guideBridge.resolveNode("productName").value = productArray[i].productName;
                        guideBridge.resolveNode("productId").value = (i+1);
                        guideBridge.resolveNode("SeriesApi").value = productArray[i].series;
                        guideBridge.resolveNode("Series").value = productArray[i].series;
                        guideBridge.resolveNode("Series").items = setSeries(guideBridge.resolveNode("productName").value);
                        guideBridge.resolveNode("PlanName").value = setPlanName(guideBridge.resolveNode("productName").value);
                        setPlanNameItems(guideBridge.resolveNode("productName").value);
                        guideBridge.resolveNode("ApplicationNumber").value = productArray[i].applicationNumber;
                        guideBridge.resolveNode("ApplicationNoApi").value = productArray[i].applicationNumber;
                        guideBridge.resolveNode("TaxType").value = productArray[i].taxType;

                        // For accident
                        if (accidentLOBs.includes(productArray[i].productName)){
                            guideBridge.resolveNode("AmtOfferedTable").visible = false;
                            guideBridge.resolveNode("EmployeeIssueAge").visible = true;
                            guideBridge.resolveNode("SpouseIssueAge").visible = true;
                            guideBridge.resolveNode("ChildIssueAge").visible = true;
                            guideBridge.resolveNode("isProductAro").visible = true;

                            if(productArray[i].employeeIssueAge!=null){
                                guideBridge.resolveNode("EmployeeIssueAge").value = productArray[i].employeeIssueAge;
                            }else{
                                guideBridge.resolveNode("EmployeeIssueAge").value = "";
                            }
                            if(productArray[i].spouseIssueAge!=null){
                                guideBridge.resolveNode("SpouseIssueAge").value = productArray[i].spouseIssueAge;
                            }else{
                                guideBridge.resolveNode("SpouseIssueAge").value = "";
                            }
                            if(productArray[i].childIssueAge!=null){
                                guideBridge.resolveNode("ChildIssueAge").value = productArray[i].childIssueAge;
                            }else{
                                guideBridge.resolveNode("ChildIssueAge").value = "";
                            }
                            
                            if(productArray[i].isProductAro!=null){
                                guideBridge.resolveNode("isProductAro").value = productArray[i].isProductAro;
                            }else{
                                guideBridge.resolveNode("isProductAro").value="";
                            }                           
                        }

                        // For hospitalIndemnity
                        if (hospitalIndemnityLOBs.includes(productArray[i].productName)){
                            guideBridge.resolveNode("AmtOfferedTable").visible = false;                            
                            guideBridge.resolveNode("EmployeeIssueAge").visible = true;
                            guideBridge.resolveNode("SpouseIssueAge").visible = true;
                            guideBridge.resolveNode("ChildIssueAge").visible = true;
                            guideBridge.resolveNode("isProductAro").visible = true;
                            guideBridge.resolveNode("hundredPercentGuranteed").visible = true;

                            if(productArray[i].employeeIssueAge!=null){
                                guideBridge.resolveNode("EmployeeIssueAge").value = productArray[i].employeeIssueAge;
                            }else{
                                guideBridge.resolveNode("EmployeeIssueAge").value = "";
                            }
                            if(productArray[i].spouseIssueAge!=null){
                                guideBridge.resolveNode("SpouseIssueAge").value = productArray[i].spouseIssueAge;
                            }else{
                                guideBridge.resolveNode("SpouseIssueAge").value = "";
                            }
                            if(productArray[i].childIssueAge!=null){
                                guideBridge.resolveNode("ChildIssueAge").value = productArray[i].childIssueAge;
                            }else{
                                guideBridge.resolveNode("ChildIssueAge").value = "";
                            }
                            
                            if(productArray[i].isProductAro!=null){
                                guideBridge.resolveNode("isProductAro").value = productArray[i].isProductAro;
                            }else{
                                guideBridge.resolveNode("isProductAro").value="";
                            }
                            if(productArray[i].hundredPercentGuranteed!=null){
                                guideBridge.resolveNode("hundredPercentGuranteed").value = productArray[i].hundredPercentGuranteed;
                            }else{
                                guideBridge.resolveNode("hundredPercentGuranteed").value ="";
                            }                            
                        }

                        // For benExtend
                        if (benExtendLOBs.includes(productArray[i].productName)){
                            guideBridge.resolveNode("AmtOfferedTable").visible = false;                            
                            guideBridge.resolveNode("EmployeeIssueAge").visible = true;
                            guideBridge.resolveNode("SpouseIssueAge").visible = true;
                            guideBridge.resolveNode("ChildIssueAge").visible = true;
                            guideBridge.resolveNode("isProductAro").visible = true;
                            guideBridge.resolveNode("hundredPercentGuranteed").visible = true;
                            guideBridge.resolveNode("Participation").visible = true;

                            if(productArray[i].employeeIssueAge!=null){
                                guideBridge.resolveNode("EmployeeIssueAge").value = productArray[i].employeeIssueAge;
                            }else{
                                guideBridge.resolveNode("EmployeeIssueAge").value = "";
                            }
                            if(productArray[i].spouseIssueAge!=null){
                                guideBridge.resolveNode("SpouseIssueAge").value = productArray[i].spouseIssueAge;
                            }else{
                                guideBridge.resolveNode("SpouseIssueAge").value = "";
                            }
                            if(productArray[i].childIssueAge!=null){
                                guideBridge.resolveNode("ChildIssueAge").value = productArray[i].childIssueAge;
                            }else{
                                guideBridge.resolveNode("ChildIssueAge").value = "";
                            }
                            
                            if(productArray[i].isProductAro!=null){
                                guideBridge.resolveNode("isProductAro").value = productArray[i].isProductAro;
                            }else{
                                guideBridge.resolveNode("isProductAro").value="";
                            }
                            if(productArray[i].hundredPercentGuranteed!=null){
                                guideBridge.resolveNode("hundredPercentGuranteed").value = productArray[i].hundredPercentGuranteed;
                            }else{
                                guideBridge.resolveNode("hundredPercentGuranteed").value ="";
                            }
                            if(productArray[i].participationRequirement!=null){
                                guideBridge.resolveNode("Participation").value = productArray[i].participationRequirement;
                            }else{
                                guideBridge.resolveNode("Participation").value ="";
                            }
                            
                        }
                        // for criticalIllness
                        if (criticalIllnessLOBs.includes(productArray[i].productName)){
                            guideBridge.resolveNode("AmtOfferedTable").visible = true;
                            guideBridge.resolveNode("updateBenefitAmountsDisplay").visible = true;
                            guideBridge.resolveNode("EmployeeIssueAge").visible = true;
                            guideBridge.resolveNode("SpouseIssueAge").visible = true;
                            guideBridge.resolveNode("ChildIssueAge").visible = false;
                            guideBridge.resolveNode("ChildIssueAge").value = "";
                            guideBridge.resolveNode("isProductAro").visible = true;
                            guideBridge.resolveNode("PlatformDriven").visible = true;
                            guideBridge.resolveNode("AgeCalculated").visible = true;
                            guideBridge.resolveNode("AgeCalculation").visible = true;
                            guideBridge.resolveNode("AgeCalculation").items = setAgeCalculationItems(productArray[i].productName);
                            guideBridge.resolveNode("AmtOfferedTableEmployee").visible = true;
                            guideBridge.resolveNode("AmtOfferedTableSpouse").visible = true;                        
                            guideBridge.resolveNode("benefitAmountPercentage").visible = true;
                            guideBridge.resolveNode("TobaccoStatus").visible = true;
                            guideBridge.resolveNode("TobaccoStatusDetermined").visible = true;

                            if(productArray[i].ageCalculation!=null){
                                guideBridge.resolveNode("AgeCalculation").value = productArray[i].ageCalculation;
                            }else{
                                guideBridge.resolveNode("AgeCalculation").value ="";
                            }
                            if (productArray[i].issueAgeType != null) {
                                guideBridge.resolveNode("issueAgeType").value = productArray[i].issueAgeType;
                            }
                            
                            if(productArray[i].platformDriven!=null){
                                guideBridge.resolveNode("PlatformDriven").value = productArray[i].platformDriven;
                            }else{
                                guideBridge.resolveNode("PlatformDriven").value ="";
                            }
                            if(productArray[i].ageCalculated!=null){
                                guideBridge.resolveNode("AgeCalculated").value = productArray[i].ageCalculated;
                            }else{
                                guideBridge.resolveNode("AgeCalculated").value ="";
                            }
                            if(productArray[i].employeeIssueAge!=null){
                                guideBridge.resolveNode("EmployeeIssueAge").value = productArray[i].employeeIssueAge;
                            }else{
                                guideBridge.resolveNode("EmployeeIssueAge").value = "";
                            }
                            if(productArray[i].spouseIssueAge!=null){
                                guideBridge.resolveNode("SpouseIssueAge").value = productArray[i].spouseIssueAge;
                            }else{
                                guideBridge.resolveNode("SpouseIssueAge").value = "";
                            }                           
                            if (productArray[i].employeeAmountOffered != null) {
                                guideBridge.resolveNode("EmployeeIncrements").value = productArray[i].employeeAmountOffered.amountOffered.increments;
                                guideBridge.resolveNode("EmployeeMinAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.minimumAmtElect;
                                guideBridge.resolveNode("EmployeeMaxAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.maximumAmtElect;
                                guideBridge.resolveNode("EmployeeGuaranteedMax").value = productArray[i].employeeAmountOffered.amountOffered.guaranteedIssueMaximum;
                                EEbenefitAmountDisplay();
                            }
                            else {
                                guideBridge.resolveNode("EmployeeIncrements").value = "";
                                guideBridge.resolveNode("EmployeeMinAmtElect").value = "";
                                guideBridge.resolveNode("EmployeeMaxAmtElect").value = "";
                                guideBridge.resolveNode("EmployeeGuaranteedMax").value = "";
                            }
                            if (productArray[i].spouseAmountOffered != null) {
                                guideBridge.resolveNode("SpouseIncrements").value = productArray[i].spouseAmountOffered.amountOffered.increments;
                                guideBridge.resolveNode("SpouseMinAmtElect").value = productArray[i].spouseAmountOffered.amountOffered.minimumAmtElect;
                                guideBridge.resolveNode("SpouseMaxAmtElect").value = productArray[i].spouseAmountOffered.amountOffered.maximumAmtElect;
                                guideBridge.resolveNode("SpouseGuaranteedMax").value = productArray[i].spouseAmountOffered.amountOffered.guaranteedIssueMaximum;
                                SPbenefitAmountDisplay();
                            }
                            else {
                                guideBridge.resolveNode("SpouseIncrements").value = "";
                                guideBridge.resolveNode("SpouseMinAmtElect").value = "";
                                guideBridge.resolveNode("SpouseMaxAmtElect").value = "";
                                guideBridge.resolveNode("SpouseGuaranteedMax").value = "";
                            }
                            if(productArray[i].benefitAmountPercentage != null){
                                guideBridge.resolveNode("benefitAmountPercentage").value = productArray[i].benefitAmountPercentage;
                            }
                            else{
                                guideBridge.resolveNode("benefitAmountPercentage").value = "";
                            }
                            if (productArray[i].tobacco != null) {
                                guideBridge.resolveNode("TobaccoStatus").value = productArray[i].tobacco.tobaccoStatus;
                                guideBridge.resolveNode("TobaccoStatusDetermined").value = productArray[i].tobacco.tobaccoStatusDetermined;
                            }
                            else {
                                guideBridge.resolveNode("TobaccoStatus").value = "";
                                guideBridge.resolveNode("TobaccoStatusDetermined").value = "";
                            }
                            if(productArray[i].isProductAro!=null){
                                guideBridge.resolveNode("isProductAro").value = productArray[i].isProductAro;
                            }else{
                                guideBridge.resolveNode("isProductAro").value="";
                            }
                        }

                        //for Disability
                        if (disabilityLOBs.includes(productArray[i].productName)|| disabilityLOBs.includes(productArray[i].productName.split('-')[0])){
                            guideBridge.resolveNode("AmtOfferedTable").visible = true;
                            guideBridge.resolveNode("AmtOfferedTableEmployee").visible = true;
                            guideBridge.resolveNode("AmtOfferedTableTdiState").visible = true;
                            guideBridge.resolveNode("EligibleEmployees").visible = true;
                            guideBridge.resolveNode("diHoursWorkedPerWeek").visible = true;
                            guideBridge.resolveNode("AgeCalculation").visible = true;
                            guideBridge.resolveNode("AgeCalculation").items = setAgeCalculationItems(productArray[i].productName);
                            guideBridge.resolveNode("PlatformDriven").visible = true;
                            guideBridge.resolveNode("EmployeeIssueAge").visible = true;
                            guideBridge.resolveNode("Participation").visible = true;
                            guideBridge.resolveNode("uniqueIncrementsUtilized").visible = true;
                            guideBridge.resolveNode("BenefitType").visible = true;
                            guideBridge.resolveNode("BenefitPeriod").visible = true;
                            guideBridge.resolveNode("EliminationPeriod").visible = true;
                            guideBridge.resolveNode("isProductAro").visible = true;

                            if(productArray[i].eligibleEmployees!=null){
                                guideBridge.resolveNode("EligibleEmployees").value = productArray[i].eligibleEmployees;
                            }else{
                                guideBridge.resolveNode("EligibleEmployees").value = "";
                            }
                            if(productArray[i].diHoursWorkedPerWeek!=null){
                                guideBridge.resolveNode("diHoursWorkedPerWeek").value = productArray[i].diHoursWorkedPerWeek;
                            }else{
                                guideBridge.resolveNode("diHoursWorkedPerWeek").value = "";
                            }
                            if(productArray[i].ageCalculation!=null){
                                guideBridge.resolveNode("AgeCalculation").value = productArray[i].ageCalculation;
                            }else{
                                guideBridge.resolveNode("AgeCalculation").value = "";
                            }
                            if (productArray[i].issueAgeType != null) {
                                guideBridge.resolveNode("issueAgeType").value = productArray[i].issueAgeType;
                            }
                            if(productArray[i].platformDriven!=null){
                                guideBridge.resolveNode("PlatformDriven").value = productArray[i].platformDriven;
                            }else{
                                guideBridge.resolveNode("PlatformDriven").value ="";
                            }
                            if(productArray[i].employeeIssueAge!=null){
                                guideBridge.resolveNode("EmployeeIssueAge").value = productArray[i].employeeIssueAge;
                            }else{
                                guideBridge.resolveNode("EmployeeIssueAge").value = "";
                            }
                            if(productArray[i].participationRequirement!=null){
                                guideBridge.resolveNode("Participation").value = productArray[i].participationRequirement;
                            }else{
                                guideBridge.resolveNode("Participation").value ="";
                            }
                            if (productArray[i].employeeAmountOffered != null) {
                                guideBridge.resolveNode("EmployeeIncrements").value = productArray[i].employeeAmountOffered.amountOffered.increments;
                                guideBridge.resolveNode("EmployeeMinAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.minimumAmtElect;
                                guideBridge.resolveNode("EmployeeMaxAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.maximumAmtElect;
                                guideBridge.resolveNode("EmployeeGuaranteedMax").value = productArray[i].employeeAmountOffered.amountOffered.guaranteedIssueMaximum;                                
                            }
                            else {
                                guideBridge.resolveNode("EmployeeIncrements").value = "";
                                guideBridge.resolveNode("EmployeeMinAmtElect").value = "";
                                guideBridge.resolveNode("EmployeeMaxAmtElect").value = "";
                                guideBridge.resolveNode("EmployeeGuaranteedMax").value = "";
                            }
                            if (productArray[i].tdiStateAmountOffered != null) {
                                guideBridge.resolveNode("tdiIncrements").value = productArray[i].tdiStateAmountOffered.amountOffered.increments;
                                guideBridge.resolveNode("tdiMinAmtElect").value = productArray[i].tdiStateAmountOffered.amountOffered.minimumAmtElect;
                                guideBridge.resolveNode("tdiMaxAmtElect").value = productArray[i].tdiStateAmountOffered.amountOffered.maximumAmtElect;
                                guideBridge.resolveNode("TdiGuaranteedMax").value = productArray[i].tdiStateAmountOffered.amountOffered.guaranteedIssueMaximum;                               
                            }
                            else {
                                guideBridge.resolveNode("tdiIncrements").value = '';
                                guideBridge.resolveNode("tdiMinAmtElect").value = '';
                                guideBridge.resolveNode("tdiMaxAmtElect").value = '';
                                guideBridge.resolveNode("TdiGuaranteedMax").value = ''; 
                            }
                            if(productArray[i].isUniqueIncrementsUtilized=="Y"){
                                guideBridge.resolveNode("uniqueIncrementsUtilized").value=productArray[i].isUniqueIncrementsUtilized;
                                guideBridge.resolveNode("uniqueIncrementsText").value=productArray[i].benefitAmountDescription;
                            }
                            else {
                                guideBridge.resolveNode("uniqueIncrementsUtilized").value='';
                                guideBridge.resolveNode("uniqueIncrementsText").value='';
                            }
                            if(productArray[i].benefitType!=null){
                                guideBridge.resolveNode("BenefitType").value = productArray[i].benefitType;
                            }else{
                                guideBridge.resolveNode("BenefitType").value="";
                            }
                            if(productArray[i].benefitPeriod!=null){
                                guideBridge.resolveNode("BenefitPeriod").value = productArray[i].benefitPeriod;
                            }else{
                                guideBridge.resolveNode("BenefitPeriod").value="";
                            }
                            if(productArray[i].eliminationPeriod!=null){
                                guideBridge.resolveNode("EliminationPeriod").value = productArray[i].eliminationPeriod;
                            }else{
                                guideBridge.resolveNode("EliminationPeriod").value="";
                            }                                                           
                            if(productArray[i].isProductAro!=null){
                                guideBridge.resolveNode("isProductAro").value = productArray[i].isProductAro;
                            }else{
                                guideBridge.resolveNode("isProductAro").value="";
                            }                                                    
                        }
                        // For wholeLife
                        if (wholeLifeLOBs.includes(productArray[i].productName)) {
                            guideBridge.resolveNode("AmtOfferedTable").visible = true;
                            guideBridge.resolveNode("AmtOfferedTableEmployee").visible = true;
                            guideBridge.resolveNode("AmtOfferedTableSpouse").visible = true;
                            guideBridge.resolveNode("EligibleEmployees").visible = true;
                            guideBridge.resolveNode("PlatformDriven").visible = true;
                            guideBridge.resolveNode("AgeCalculated").visible = true;
                            guideBridge.resolveNode("EmployeeIssueAge").visible = true;
                            guideBridge.resolveNode("SpouseIssueAge").visible = true;
                            guideBridge.resolveNode("ChildIssueAge").visible = true;
                            guideBridge.resolveNode("Participation").visible = true;
                            guideBridge.resolveNode("childCoverageOfferedCheckbox").visible = true;
                            guideBridge.resolveNode("childTermLifeOfferedCheckbox").visible = true;
                            guideBridge.resolveNode("uniqueIncrementsUtilized").visible = true;
                            guideBridge.resolveNode("benefitAmountPercentage").visible = true;
                            guideBridge.resolveNode("TobaccoStatus").visible = true;
                            guideBridge.resolveNode("TobaccoStatusDetermined").visible = true;
                            guideBridge.resolveNode("isProductAro").visible = false; 
                            
                            if(productArray[i].eligibleEmployees!=null){
                                guideBridge.resolveNode("EligibleEmployees").value = productArray[i].eligibleEmployees;
                            }else{
                                guideBridge.resolveNode("EligibleEmployees").value = "";
                            }
                            if(productArray[i].platformDriven!=null){
                                guideBridge.resolveNode("PlatformDriven").value = productArray[i].platformDriven;
                            }else{
                                guideBridge.resolveNode("PlatformDriven").value ="";
                            }
                            if(productArray[i].ageCalculated!=null){
                                guideBridge.resolveNode("AgeCalculated").value = productArray[i].ageCalculated;
                            }else{
                                guideBridge.resolveNode("AgeCalculated").value ="";
                            }
                            if(productArray[i].employeeIssueAge!=null){
                                guideBridge.resolveNode("EmployeeIssueAge").value = productArray[i].employeeIssueAge;
                            }else{
                                guideBridge.resolveNode("EmployeeIssueAge").value = "";
                            }
                            if(productArray[i].spouseIssueAge!=null){
                                guideBridge.resolveNode("SpouseIssueAge").value = productArray[i].spouseIssueAge;
                            }else{
                                guideBridge.resolveNode("SpouseIssueAge").value = "";
                            }
                            if(productArray[i].childIssueAge!=null){
                                guideBridge.resolveNode("ChildIssueAge").value = productArray[i].childIssueAge;
                            }else{
                                guideBridge.resolveNode("ChildIssueAge").value = "";
                            }
                            if(productArray[i].participationRequirement!=null){
                                guideBridge.resolveNode("Participation").value = productArray[i].participationRequirement;
                            }else{
                                guideBridge.resolveNode("Participation").value ="";
                            }
                            if (productArray[i].employeeAmountOffered != null) {
                                guideBridge.resolveNode("EmployeeIncrements").value = productArray[i].employeeAmountOffered.amountOffered.increments;
                                guideBridge.resolveNode("EmployeeMinAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.minimumAmtElect;
                                guideBridge.resolveNode("EmployeeMaxAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.maximumAmtElect;
                                guideBridge.resolveNode("EmployeeGuaranteedMax").value = productArray[i].employeeAmountOffered.amountOffered.guaranteedIssueMaximum;
                            }
                            else {
                                guideBridge.resolveNode("EmployeeIncrements").value = "";
                                guideBridge.resolveNode("EmployeeMinAmtElect").value = "";
                                guideBridge.resolveNode("EmployeeMaxAmtElect").value = "";
                                guideBridge.resolveNode("EmployeeGuaranteedMax").value = "";
                            }
                            if (productArray[i].spouseAmountOffered != null) {
                                guideBridge.resolveNode("SpouseIncrements").value = productArray[i].spouseAmountOffered.amountOffered.increments;
                                guideBridge.resolveNode("SpouseMinAmtElect").value = productArray[i].spouseAmountOffered.amountOffered.minimumAmtElect;
                                guideBridge.resolveNode("SpouseMaxAmtElect").value = productArray[i].spouseAmountOffered.amountOffered.maximumAmtElect;
                                guideBridge.resolveNode("SpouseGuaranteedMax").value = productArray[i].spouseAmountOffered.amountOffered.guaranteedIssueMaximum;
                            }
                            else {
                                guideBridge.resolveNode("SpouseIncrements").value = "";
                                guideBridge.resolveNode("SpouseMinAmtElect").value = "";
                                guideBridge.resolveNode("SpouseMaxAmtElect").value = "";
                                guideBridge.resolveNode("SpouseGuaranteedMax").value = "";
                            }
                            if(productArray[i].isChildCoverageOffered=="Y"){
                                guideBridge.resolveNode("childCoverageOfferedCheckbox").value=productArray[i].isChildCoverageOffered;
                                guideBridge.resolveNode("childCoverageOptions").value=productArray[i].childCoverageOffered;
                            }else{
                                guideBridge.resolveNode("childCoverageOfferedCheckbox").value="";
                                guideBridge.resolveNode("childCoverageOptions").value="";
                            }
                            if(productArray[i].isChildTermlifeRiderOffered=="Y"){
                                guideBridge.resolveNode("childTermLifeOfferedCheckbox").value=productArray[i].isChildTermlifeRiderOffered;
                            }else{
                                guideBridge.resolveNode("childTermLifeOfferedCheckbox").value="";
                            }
                            if(productArray[i].isUniqueIncrementsUtilized=="Y"){
                                guideBridge.resolveNode("uniqueIncrementsUtilized").value=productArray[i].isUniqueIncrementsUtilized;
                                guideBridge.resolveNode("uniqueIncrementsText").value=productArray[i].benefitAmountDescription;
                            }else{
                                guideBridge.resolveNode("uniqueIncrementsUtilized").value="";
                                guideBridge.resolveNode("uniqueIncrementsText").value="";
                            }
                            if(productArray[i].benefitAmountPercentage != null){
                                guideBridge.resolveNode("benefitAmountPercentage").value = productArray[i].benefitAmountPercentage;
                            }
                            else{
                                guideBridge.resolveNode("benefitAmountPercentage").value = "";
                            }
                            if (productArray[i].tobacco != null) {
                                guideBridge.resolveNode("TobaccoStatus").value = productArray[i].tobacco.tobaccoStatus;
                                guideBridge.resolveNode("TobaccoStatusDetermined").value = productArray[i].tobacco.tobaccoStatusDetermined;
                            }
                            else {
                                guideBridge.resolveNode("TobaccoStatus").value = "";
                                guideBridge.resolveNode("TobaccoStatusDetermined").value = "";
                            }
                        }

                        // For termLife
                        if (termLifeLOBs.includes(productArray[i].productName) || termLifeLOBs.includes(productArray[i].productName.split('-')[0])) {
                            guideBridge.resolveNode("AmtOfferedTable").visible = true;
                            guideBridge.resolveNode("AmtOfferedTableEmployee").visible = true;
                            guideBridge.resolveNode("AmtOfferedTableSpouse").visible = true;
                            guideBridge.resolveNode("AmtOfferedTableChild").visible = true;
                            guideBridge.resolveNode("EligibleEmployees").visible = true;
                            guideBridge.resolveNode("Term").visible = true;
                            guideBridge.resolveNode("PlatformDriven").visible = true;
                            guideBridge.resolveNode("AgeCalculated").visible = true;
                            guideBridge.resolveNode("EmployeeIssueAge").visible = true;
                            guideBridge.resolveNode("SpouseIssueAge").visible = true;
                            guideBridge.resolveNode("ChildIssueAge").visible = true;
                            guideBridge.resolveNode("Participation").visible = true;
                            guideBridge.resolveNode("uniqueIncrementsUtilized").visible = true;
                            guideBridge.resolveNode("benefitAmountPercentage").visible = true;
                            guideBridge.resolveNode("TobaccoStatus").visible = true;
                            guideBridge.resolveNode("TobaccoStatusDetermined").visible = true;
                            guideBridge.resolveNode("isProductAro").visible = false; 

                            if(productArray[i].eligibleEmployees!=null){
                                guideBridge.resolveNode("EligibleEmployees").value = productArray[i].eligibleEmployees;
                            }else{
                                guideBridge.resolveNode("EligibleEmployees").value = "";
                            }
                            if (productArray[i].term != null) {
                                guideBridge.resolveNode("Term").value = productArray[i].term;
                            }
                            else {
                                guideBridge.resolveNode("Term").value = "";
                            }
                            if(productArray[i].platformDriven!=null){
                                guideBridge.resolveNode("PlatformDriven").value = productArray[i].platformDriven;
                            }else{
                                guideBridge.resolveNode("PlatformDriven").value ="";
                            }
                            if(productArray[i].ageCalculated!=null){
                                guideBridge.resolveNode("AgeCalculated").value = productArray[i].ageCalculated;
                            }else{
                                guideBridge.resolveNode("AgeCalculated").value ="";
                            }
                            if(productArray[i].employeeIssueAge!=null){
                                guideBridge.resolveNode("EmployeeIssueAge").value = productArray[i].employeeIssueAge;
                            }else{
                                guideBridge.resolveNode("EmployeeIssueAge").value = "";
                            }
                            if(productArray[i].spouseIssueAge!=null){
                                guideBridge.resolveNode("SpouseIssueAge").value = productArray[i].spouseIssueAge;
                            }else{
                                guideBridge.resolveNode("SpouseIssueAge").value = "";
                            }
                            if(productArray[i].childIssueAge!=null){
                                guideBridge.resolveNode("ChildIssueAge").value = productArray[i].childIssueAge;
                            }else{
                                guideBridge.resolveNode("ChildIssueAge").value = "";
                            }
                            if(productArray[i].participationRequirement!=null){
                                guideBridge.resolveNode("Participation").value = productArray[i].participationRequirement;
                            }else{
                                guideBridge.resolveNode("Participation").value ="";
                            }
                            if (productArray[i].employeeAmountOffered != null) {
                                guideBridge.resolveNode("EmployeeIncrements").value = productArray[i].employeeAmountOffered.amountOffered.increments;
                                guideBridge.resolveNode("EmployeeMinAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.minimumAmtElect;
                                guideBridge.resolveNode("EmployeeMaxAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.maximumAmtElect;
                                guideBridge.resolveNode("EmployeeGuaranteedMax").value = productArray[i].employeeAmountOffered.amountOffered.guaranteedIssueMaximum;
                            }
                            else {
                                guideBridge.resolveNode("EmployeeIncrements").value = "";
                                guideBridge.resolveNode("EmployeeMinAmtElect").value = "";
                                guideBridge.resolveNode("EmployeeMaxAmtElect").value = "";
                                guideBridge.resolveNode("EmployeeGuaranteedMax").value = "";
                            }
                            if (productArray[i].spouseAmountOffered != null) {
                                guideBridge.resolveNode("SpouseIncrements").value = productArray[i].spouseAmountOffered.amountOffered.increments;
                                guideBridge.resolveNode("SpouseMinAmtElect").value = productArray[i].spouseAmountOffered.amountOffered.minimumAmtElect;
                                guideBridge.resolveNode("SpouseMaxAmtElect").value = productArray[i].spouseAmountOffered.amountOffered.maximumAmtElect;
                                guideBridge.resolveNode("SpouseGuaranteedMax").value = productArray[i].spouseAmountOffered.amountOffered.guaranteedIssueMaximum;
                            }
                            else {
                                guideBridge.resolveNode("SpouseIncrements").value = "";
                                guideBridge.resolveNode("SpouseMinAmtElect").value = "";
                                guideBridge.resolveNode("SpouseMaxAmtElect").value = "";
                                guideBridge.resolveNode("SpouseGuaranteedMax").value = "";
                            }
                            if (productArray[i].childAmountOffered != null) {
                                guideBridge.resolveNode("childIncrements").value = productArray[i].childAmountOffered.amountOffered.increments;
                                guideBridge.resolveNode("childMinAmtElect").value = productArray[i].childAmountOffered.amountOffered.minimumAmtElect;
                                guideBridge.resolveNode("childMaxAmtElect").value = productArray[i].childAmountOffered.amountOffered.maximumAmtElect;
                                guideBridge.resolveNode("ChildGuaranteedMax").value = productArray[i].childAmountOffered.amountOffered.guaranteedIssueMaximum;
                            }
                            else {
                                guideBridge.resolveNode("childIncrements").value = "";
                                guideBridge.resolveNode("childMinAmtElect").value = "";
                                guideBridge.resolveNode("childMaxAmtElect").value = "";
                                guideBridge.resolveNode("ChildGuaranteedMax").value = "";
                            }
                            if(productArray[i].isUniqueIncrementsUtilized=="Y"){
                                guideBridge.resolveNode("uniqueIncrementsUtilized").value=productArray[i].isUniqueIncrementsUtilized;
                                guideBridge.resolveNode("uniqueIncrementsText").value=productArray[i].benefitAmountDescription;
                            }else{
                                guideBridge.resolveNode("uniqueIncrementsUtilized").value="";
                                guideBridge.resolveNode("uniqueIncrementsText").value="";
                            }
                            if(productArray[i].benefitAmountPercentage != null){
                                guideBridge.resolveNode("benefitAmountPercentage").value = productArray[i].benefitAmountPercentage;
                            }
                            else{
                                guideBridge.resolveNode("benefitAmountPercentage").value = "";
                            }
                            if (productArray[i].tobacco != null) {
                                guideBridge.resolveNode("TobaccoStatus").value = productArray[i].tobacco.tobaccoStatus;
                                guideBridge.resolveNode("TobaccoStatusDetermined").value = productArray[i].tobacco.tobaccoStatusDetermined;
                            }
                            else {
                                guideBridge.resolveNode("TobaccoStatus").value = "";
                                guideBridge.resolveNode("TobaccoStatusDetermined").value = "";
                            }
                        }

                        // for termto120
                        if (termto120LOBs.includes(productArray[i].productName)) {
                            guideBridge.resolveNode("AmtOfferedTable").visible = true;
                            guideBridge.resolveNode("AmtOfferedTableEmployee").visible = true;
                            guideBridge.resolveNode("AmtOfferedTableSpouse").visible = true;
                            guideBridge.resolveNode("AmtOfferedTableChild").visible = true;
                            guideBridge.resolveNode("EligibleEmployees").visible = true;
                            guideBridge.resolveNode("AgeCalculation").visible = true;
                            guideBridge.resolveNode("AgeCalculation").items = setAgeCalculationItems(productArray[i].productName);
                            guideBridge.resolveNode("AgeCalculated").visible = true;
                            guideBridge.resolveNode("EmployeeIssueAge").visible = true;
                            guideBridge.resolveNode("SpouseIssueAge").visible = true;
                            guideBridge.resolveNode("ChildIssueAge").visible = true;
                            guideBridge.resolveNode("Participation").visible = true;
                            guideBridge.resolveNode("uniqueIncrementsUtilized").visible = true;
                            guideBridge.resolveNode("benefitAmountPercentage").visible = true;
                            guideBridge.resolveNode("TobaccoStatus").visible = true;
                            guideBridge.resolveNode("TobaccoStatusDetermined").visible = true;
                            guideBridge.resolveNode("isProductAro").visible = false; 

                            if(productArray[i].eligibleEmployees!=null){
                                guideBridge.resolveNode("EligibleEmployees").value = productArray[i].eligibleEmployees;
                            }else{
                                guideBridge.resolveNode("EligibleEmployees").value = "";
                            }
                            if(productArray[i].ageCalculation!=null){
                                guideBridge.resolveNode("AgeCalculation").value = productArray[i].ageCalculation;
                            }else{
                                guideBridge.resolveNode("AgeCalculation").value = "";
                            }
                            if (productArray[i].issueAgeType != null) {
                                guideBridge.resolveNode("issueAgeType").value = productArray[i].issueAgeType;
                            }
                            if(productArray[i].ageCalculated!=null){
                                guideBridge.resolveNode("AgeCalculated").value = productArray[i].ageCalculated;
                            }else{
                                guideBridge.resolveNode("AgeCalculated").value ="";
                            }
                            if(productArray[i].employeeIssueAge!=null){
                                guideBridge.resolveNode("EmployeeIssueAge").value = productArray[i].employeeIssueAge;
                            }else{
                                guideBridge.resolveNode("EmployeeIssueAge").value = "";
                            }
                            if(productArray[i].spouseIssueAge!=null){
                                guideBridge.resolveNode("SpouseIssueAge").value = productArray[i].spouseIssueAge;
                            }else{
                                guideBridge.resolveNode("SpouseIssueAge").value = "";
                            }
                            if(productArray[i].childIssueAge!=null){
                                guideBridge.resolveNode("ChildIssueAge").value = productArray[i].childIssueAge;
                            }else{
                                guideBridge.resolveNode("ChildIssueAge").value = "";
                            }
                            if(productArray[i].participationRequirement!=null){
                                guideBridge.resolveNode("Participation").value = productArray[i].participationRequirement;
                            }else{
                                guideBridge.resolveNode("Participation").value ="";
                            }
                            if (productArray[i].employeeAmountOffered != null) {
                                guideBridge.resolveNode("EmployeeIncrements").value = productArray[i].employeeAmountOffered.amountOffered.increments;
                                guideBridge.resolveNode("EmployeeMinAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.minimumAmtElect;
                                guideBridge.resolveNode("EmployeeMaxAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.maximumAmtElect;
                                guideBridge.resolveNode("EmployeeGuaranteedMax").value = productArray[i].employeeAmountOffered.amountOffered.guaranteedIssueMaximum;
                            }
                            else {
                                guideBridge.resolveNode("EmployeeIncrements").value = "";
                                guideBridge.resolveNode("EmployeeMinAmtElect").value = "";
                                guideBridge.resolveNode("EmployeeMaxAmtElect").value = "";
                                guideBridge.resolveNode("EmployeeGuaranteedMax").value = "";
                            }
                            if (productArray[i].spouseAmountOffered != null) {
                                guideBridge.resolveNode("SpouseIncrements").value = productArray[i].spouseAmountOffered.amountOffered.increments;
                                guideBridge.resolveNode("SpouseMinAmtElect").value = productArray[i].spouseAmountOffered.amountOffered.minimumAmtElect;
                                guideBridge.resolveNode("SpouseMaxAmtElect").value = productArray[i].spouseAmountOffered.amountOffered.maximumAmtElect;
                                guideBridge.resolveNode("SpouseGuaranteedMax").value = productArray[i].spouseAmountOffered.amountOffered.guaranteedIssueMaximum;
                            }
                            else {
                                guideBridge.resolveNode("SpouseIncrements").value = "";
                                guideBridge.resolveNode("SpouseMinAmtElect").value = "";
                                guideBridge.resolveNode("SpouseMaxAmtElect").value = "";
                                guideBridge.resolveNode("SpouseGuaranteedMax").value = "";
                            }
                            if (productArray[i].childAmountOffered != null) {
                                guideBridge.resolveNode("childIncrements").value = productArray[i].childAmountOffered.amountOffered.increments;
                                guideBridge.resolveNode("childMinAmtElect").value = productArray[i].childAmountOffered.amountOffered.minimumAmtElect;
                                guideBridge.resolveNode("childMaxAmtElect").value = productArray[i].childAmountOffered.amountOffered.maximumAmtElect;
                                guideBridge.resolveNode("ChildGuaranteedMax").value = productArray[i].childAmountOffered.amountOffered.guaranteedIssueMaximum;
                            }
                            else {
                                guideBridge.resolveNode("childIncrements").value = "";
                                guideBridge.resolveNode("childMinAmtElect").value = "";
                                guideBridge.resolveNode("childMaxAmtElect").value = "";
                                guideBridge.resolveNode("ChildGuaranteedMax").value = "";
                            }
                            if(productArray[i].isUniqueIncrementsUtilized=="Y"){
                                guideBridge.resolveNode("uniqueIncrementsUtilized").value=productArray[i].isUniqueIncrementsUtilized;
                                guideBridge.resolveNode("uniqueIncrementsText").value=productArray[i].benefitAmountDescription;
                            }else{
                                guideBridge.resolveNode("uniqueIncrementsUtilized").value="";
                                guideBridge.resolveNode("uniqueIncrementsText").value="";
                            }
                            if(productArray[i].benefitAmountPercentage != null){
                                guideBridge.resolveNode("benefitAmountPercentage").value = productArray[i].benefitAmountPercentage;
                            }
                            else{
                                guideBridge.resolveNode("benefitAmountPercentage").value = "";
                            }
                            if (productArray[i].tobacco != null) {
                                guideBridge.resolveNode("TobaccoStatus").value = productArray[i].tobacco.tobaccoStatus;
                                guideBridge.resolveNode("TobaccoStatusDetermined").value = productArray[i].tobacco.tobaccoStatusDetermined;
                            }
                            else {
                                guideBridge.resolveNode("TobaccoStatus").value = "";
                                guideBridge.resolveNode("TobaccoStatusDetermined").value = "";
                            }
                        }

                        // for AgeCalculated,TobaccoStatusDetermined default values 
                        if(guideBridge.resolveNode("AgeCalculated").visible === true){
                            if(enrollmentDetail['spouse-tobacco-rate-status'] === true){
                                guideBridge.resolveNode("AgeCalculated").value = "Employee & Spouse Ages Captured Separately";
                                guideBridge.resolveNode("TobaccoStatusDetermined").value = "Employee & Spouse Tobacco Status Captured Separately";
                            }
                            else {
                                guideBridge.resolveNode("AgeCalculated").value = "Spouse Age based off of Employee Age";
                                guideBridge.resolveNode("TobaccoStatusDetermined").value = "Spouse Tobacco Status based off of Employee";
                            }
                        }

                        // for other instructions checkbox and textbox
                        if(productArray[i].otherInstructions==null){
                            guideBridge.resolveNode("instructionsNeededCheckbox").value="No";
                            guideBridge.resolveNode("otherInstructions").visible = false;
                        }
                        else{
                            guideBridge.resolveNode("instructionsNeededCheckbox").value="Yes";
                            guideBridge.resolveNode("otherInstructions").value=productArray[i].otherInstructions;
                        }
                    }
                }
            }).responseText);
    }
    else if (guideBridge.resolveNode("formCase").value == "Edit"){

        //setting accountInformation Data
    
        guideBridge.resolveNode("AI_groupNumber").value = data.accountInformation.groupNumber;
        guideBridge.resolveNode("AI_groupType").value = data.accountInformation.groupType;
        guideBridge.resolveNode("AI_AccountName").value = data.accountInformation.accountName;
        guideBridge.resolveNode("AI_SitusState").value = data.accountInformation.situsState;
        guideBridge.resolveNode("AI_SSN").value = data.accountInformation.ssn;
        guideBridge.resolveNode("AI_DeductionFrequency").value = data.accountInformation.deductionFrequency;
        guideBridge.resolveNode("AI_PlatformSoftware").value = data.accountInformation.platform;
        guideBridge.resolveNode("AI_ProductsSold").value = data.accountInformation.productSold;
        guideBridge.resolveNode("AI_CoverageBillingEffDate").value = data.accountInformation.coverageBillingEffDate;
        guideBridge.resolveNode("AI_OpenEnrollmentStartDate").value = data.accountInformation.openEnrollmentStartDate;
        //showGenerateButtons(data.accountInformation.openEnrollmentStartDate);
        disableCertificationLanguagePDF(data.accountInformation.openEnrollmentStartDate);
        guideBridge.resolveNode("AI_OpenEnrollmentEndDate").value = data.accountInformation.openEnrollmentEndDate;
        //guideBridge.resolveNode("AI_SiteTestInfoDueDate").value = data.accountInformation.siteTestInfoDueDate;
        guideBridge.resolveNode("AI_EnrollmentConditionOptions").value=data.accountInformation.enrollmentConditionOptions;
        guideBridge.resolveNode("AI_NewHire").value = data.accountInformation.newHire;
        guideBridge.resolveNode("AI_ClientManager").value = data.accountInformation.clientManager;
        guideBridge.resolveNode("AI_ClientManagerEmail").value = data.accountInformation.clientManagerEmail;
        guideBridge.resolveNode("AI_ImpManager").value = data.accountInformation.implementationManager;
        guideBridge.resolveNode("AI_ImpManagerEmail").value = data.accountInformation.implementationManagerEmail;
        guideBridge.resolveNode("AI_PlatformManager").value = data.accountInformation.partnerPlatformManager;
        guideBridge.resolveNode("AI_PlatformManagerEmail").value = data.accountInformation.partnerPlatformManagerEmail;
        guideBridge.resolveNode("FS_ProdFileNaming").value = data.fileSubmission.productionFileNaming;
        guideBridge.resolveNode("FS_testFileNaming").value = data.fileSubmission.testFileNaming;
        guideBridge.resolveNode("FS_prodFileDueDate").value = data.fileSubmission.productionFileDueDate;
        guideBridge.resolveNode("FS_TestFileDueDate").value = data.fileSubmission.testFileDueDate;
        guideBridge.resolveNode("AI_EligibleEmployees").value = data.accountInformation.eligibleEmployees;
        guideBridge.resolveNode("AI_pdfDirections").value = data.accountInformation.pdfDirections;
        guideBridge.resolveNode("HoursWorkedPerWeek").value = data.accountInformation.hoursWorkedPerWeek;
        guideBridge.resolveNode("EligibilityWaitingPeriod").value = data.accountInformation.eligibilityWaitingPeriod;
        guideBridge.resolveNode("DomesticPartners").value = data.accountInformation.partnerEligible;
        guideBridge.resolveNode("AI_CoverageBillingEffDate").enabled = false;
        guideBridge.resolveNode("validateCoverageEffDate").enabled = false;
        guideBridge.resolveNode("IsCoverageEffDateChanged").value = "No";
        
        if(data.locations.location[0].locationCode!=null){
            guideBridge.resolveNode("locationRadioButton").value="Yes";
            fillLocationsTable(locationsData);
        }
        else {
            guideBridge.resolveNode("locationRadioButton").value="No";
        }

        if(guideBridge.resolveNode("AI_NewHire").value==guideBridge.resolveNode("AI_EnrollmentConditionOptions").value){
            checkEnrollmentOptionValues(guideBridge.resolveNode("AI_EnrollmentConditionOptions").value,guideBridge.resolveNode("AI_NewHire").value);
        }
        
        var repeatPanel = guideBridge.resolveNode("Product");
        var repeatPanel2 = guideBridge.resolveNode("PlanDetails");

        //Reset Panels
        repeatPanel.instanceManager.addInstance();
        var currentCount = repeatPanel.instanceManager.instanceCount;
        for (var m = 0; m < currentCount; m++) {
            if (m != 0) {
                repeatPanel.instanceManager.removeInstance(0);
            }

        }

        for (var i = 0; i < productArray.length; i++) {
            if (i != 0) {
                repeatPanel.instanceManager.addInstance();
                repeatPanel2.instanceManager.addInstance();
            }
            // setting product panel data
            repeatPanel.instanceManager.instances[i].title = productArray[i].productName;
            repeatPanel.instanceManager.instances[i].summary = productArray[i].productName;
            guideBridge.resolveNode("productName").value = productArray[i].productName;
            guideBridge.resolveNode("productId").value = productArray[i].productId;
            guideBridge.resolveNode("PlanName").value = productArray[i].planName;

            setPlanNameItems(guideBridge.resolveNode("productName").value);

            guideBridge.resolveNode("planDescription").value = productArray[i].productDescription;
            planDescriptionChange(productArray[i].productDescription);

            //guideBridge.resolveNode("PlanLevel").value = productArray[i].planLevel;
            if(disabilityLOBs.includes(productArray[i].productName) || disabilityLOBs.includes(productArray[i].productName.split('-')[0])){
                guideBridge.resolveNode("Series").items = setSeries(productArray[i].productName.split('-')[0]);

                guideBridge.resolveNode("diHoursWorkedPerWeek").visible = true;
                guideBridge.resolveNode("diHoursWorkedPerWeek").value = productArray[i].diHoursWorkedPerWeek;
            }
            else{
                guideBridge.resolveNode("Series").items = setSeries(guideBridge.resolveNode("productName").value);

                guideBridge.resolveNode("diHoursWorkedPerWeek").value = '';
                guideBridge.resolveNode("diHoursWorkedPerWeek").visible = false;
            }

            if(termLifeLOBs.includes(productArray[i].productName) || termLifeLOBs.includes(productArray[i].productName.split('-')[0])){
                guideBridge.resolveNode("Series").items = setSeries(productArray[i].productName.split('-')[0]);
            }
            else{
                guideBridge.resolveNode("Series").items = setSeries(guideBridge.resolveNode("productName").value);
            }

            guideBridge.resolveNode("Series").value = productArray[i].series;
            guideBridge.resolveNode("SeriesApi").value = productArray[i].series;
            

            // var checkPlanNameDisability = checkDisabilityPlanName(productArray[i].productName);
            // if (checkPlanNameDisability != null) {
            //     var disabilityPlanName = checkPlanNameDisability;
            // } 
            // else {
            // disabilityPlanName = null;
            // }

            if ((accidentLOBs.includes(productArray[i].productName)) || (hospitalIndemnityLOBs.includes(productArray[i].productName)) || (benExtendLOBs.includes(productArray[i].productName))) {
                guideBridge.resolveNode("AmtOfferedTable").visible = false;
                // guideBridge.resolveNode("updateBenefitAmountsDisplay").visible = false;
            }

            if (criticalIllnessLOBs.includes(productArray[i].productName)) {
                guideBridge.resolveNode("updateBenefitAmountsDisplay").visible = true;
            } else {
                guideBridge.resolveNode("updateBenefitAmountsDisplay").visible = false;
            }
            

            if (disabilityLOBs.includes(productArray[i].productName) || disabilityLOBs.includes(productArray[i].productName.split('-')[0]) || (wholeLifeLOBs.includes(productArray[i].productName)) || (termLifeLOBs.includes(productArray[i].productName)) || termLifeLOBs.includes(productArray[i].productName.split('-')[0]) || (termto120LOBs.includes(productArray[i].productName))) {
                guideBridge.resolveNode("EligibleEmployees").visible = true;
                guideBridge.resolveNode("EligibleEmployees").value = data.accountInformation.eligibleEmployees;
            } else {
                guideBridge.resolveNode("EligibleEmployees").visible = false;
            }

            if ((accidentLOBs.includes(productArray[i].productName)) || (hospitalIndemnityLOBs.includes(productArray[i].productName)) || criticalIllnessLOBs.includes(productArray[i].productName)) {
                guideBridge.resolveNode("Participation").visible = false;
                guideBridge.resolveNode("Participation").value = "";
            } else {
                guideBridge.resolveNode("Participation").visible = true;
                guideBridge.resolveNode("Participation").value = productArray[i].participationRequirement;

                // if(disabilityLOBs.includes(productArray[i].productName) || disabilityLOBs.includes(productArray[i].productName.split('-')[0])){
                //     guideBridge.resolveNode("Participation").value = setParticipation(productArray[i].participationRequirement, data.accountInformation.eligibleEmployees);
                // }
                // else 
                // {
                //     guideBridge.resolveNode("Participation").value = productArray[i].participationRequirement;
                // }
            }

            if (productArray[i].coverageLevel!=null) {
                guideBridge.resolveNode("CoverageLevel").visible = true;
                guideBridge.resolveNode("CoverageLevel").value = productArray[i].coverageLevel;
            } else {
                guideBridge.resolveNode("CoverageLevel").visible = false;
            }

            if (disabilityLOBs.includes(productArray[i].productName) || disabilityLOBs.includes(productArray[i].productName.split('-')[0]) || termLifeLOBs.includes(productArray[i].productName) || termLifeLOBs.includes(productArray[i].productName.split('-')[0]) || wholeLifeLOBs.includes(productArray[i].productName) || termto120LOBs.includes(productArray[i].productName)) {
                guideBridge.resolveNode("uniqueIncrementsUtilized").visible = true;
            } else {
                guideBridge.resolveNode("uniqueIncrementsUtilized").visible = false;
            }

            if (wholeLifeLOBs.includes(productArray[i].productName)) {
                guideBridge.resolveNode("childCoverageOfferedCheckbox").visible = true;
                guideBridge.resolveNode("childTermLifeOfferedCheckbox").visible = true;
            } else {
                guideBridge.resolveNode("childCoverageOfferedCheckbox").visible = false;
                guideBridge.resolveNode("childTermLifeOfferedCheckbox").visible = false;
            }

            if (productArray[i].progressiveRider==null || !criticalIllnessLOBs.includes(productArray[i].productName)) {
                guideBridge.resolveNode("progressiveRider").visible = false;
                guideBridge.resolveNode("progressiveRider").value="";
            }
            else {
                guideBridge.resolveNode("progressiveRider").visible = true;
                guideBridge.resolveNode("progressiveRider").value = productArray[i].progressiveRider;
            }

            if (productArray[i].optionalRider==null || !criticalIllnessLOBs.includes(productArray[i].productName)) {
                guideBridge.resolveNode("optionalRider").visible = false;
                guideBridge.resolveNode("optionalRider").value = "";
            }
            else {
                guideBridge.resolveNode("optionalRider").visible = true;
                guideBridge.resolveNode("optionalRider").value = productArray[i].optionalRider;
            }

            // if (productArray[i].acceleratedDeathBenefitOffered == null) {
            //     guideBridge.resolveNode("AcceleratedDeathBenefitOffered").visible = false;
            // }
            // else {
            //     guideBridge.resolveNode("AcceleratedDeathBenefitOffered").visible = true;
            //     guideBridge.resolveNode("AcceleratedDeathBenefitOffered").value = productArray[i].acceleratedDeathBenefitOffered;
            // }

            if (productArray[i].employeeAmountOffered == null) {
                guideBridge.resolveNode("AmtOfferedTableEmployee").visible = false;
                guideBridge.resolveNode("EmployeeIncrements").visible = false;
                guideBridge.resolveNode("EmployeeMinAmtElect").visible = false;
                guideBridge.resolveNode("EmployeeMaxAmtElect").visible = false;
                guideBridge.resolveNode("EmployeeGuaranteedMax").visible = false;

            }
            else {
                guideBridge.resolveNode("AmtOfferedTableEmployee").visible = true;
                guideBridge.resolveNode("EmployeeIncrements").visible = true;
                guideBridge.resolveNode("EmployeeMinAmtElect").visible = true;
                guideBridge.resolveNode("EmployeeMaxAmtElect").visible = true;
                guideBridge.resolveNode("EmployeeGuaranteedMax").visible = true;
                guideBridge.resolveNode("EmployeeIncrements").value = productArray[i].employeeAmountOffered.amountOffered.increments;
                guideBridge.resolveNode("EmployeeMinAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.minimumAmtElect;
                guideBridge.resolveNode("EmployeeMaxAmtElect").value = productArray[i].employeeAmountOffered.amountOffered.maximumAmtElect;
                guideBridge.resolveNode("EmployeeGuaranteedMax").value = productArray[i].employeeAmountOffered.amountOffered.guaranteedIssueMaximum;

                EEbenefitAmountDisplay();
            }

            if (productArray[i].spouseAmountOffered == null) {
                guideBridge.resolveNode("AmtOfferedTableSpouse").visible = false;
                guideBridge.resolveNode("SpouseIncrements").visible = false;
                guideBridge.resolveNode("SpouseMinAmtElect").visible = false;
                guideBridge.resolveNode("SpouseMaxAmtElect").visible = false;
                guideBridge.resolveNode("SpouseGuaranteedMax").visible = false;

            }
            else {
                guideBridge.resolveNode("AmtOfferedTableSpouse").visible = true;
                guideBridge.resolveNode("SpouseIncrements").visible = true;
                guideBridge.resolveNode("SpouseMinAmtElect").visible = true;
                guideBridge.resolveNode("SpouseMaxAmtElect").visible = true;
                guideBridge.resolveNode("SpouseGuaranteedMax").visible = true;
                guideBridge.resolveNode("SpouseIncrements").value = productArray[i].spouseAmountOffered.amountOffered.increments;
                guideBridge.resolveNode("SpouseMinAmtElect").value = productArray[i].spouseAmountOffered.amountOffered.minimumAmtElect;
                guideBridge.resolveNode("SpouseMaxAmtElect").value = productArray[i].spouseAmountOffered.amountOffered.maximumAmtElect;
                guideBridge.resolveNode("SpouseGuaranteedMax").value = productArray[i].spouseAmountOffered.amountOffered.guaranteedIssueMaximum;

                SPbenefitAmountDisplay();
            }
            if (productArray[i].childAmountOffered == null) {
                guideBridge.resolveNode("AmtOfferedTableChild").visible = false;
                guideBridge.resolveNode("childIncrements").visible = false;
                guideBridge.resolveNode("childMinAmtElect").visible = false;
                guideBridge.resolveNode("childMaxAmtElect").visible = false;
                guideBridge.resolveNode("ChildGuaranteedMax").visible = false;
            }
            else {
                guideBridge.resolveNode("AmtOfferedTableChild").visible = true;
                guideBridge.resolveNode("childIncrements").visible = true;
                guideBridge.resolveNode("childMinAmtElect").visible = true;
                guideBridge.resolveNode("childMaxAmtElect").visible = true;
                guideBridge.resolveNode("ChildGuaranteedMax").visible = true;
                guideBridge.resolveNode("childIncrements").value = productArray[i].childAmountOffered.amountOffered.increments;
                guideBridge.resolveNode("childMinAmtElect").value = productArray[i].childAmountOffered.amountOffered.minimumAmtElect;
                guideBridge.resolveNode("childMaxAmtElect").value = productArray[i].childAmountOffered.amountOffered.maximumAmtElect;
                guideBridge.resolveNode("ChildGuaranteedMax").value = productArray[i].childAmountOffered.amountOffered.guaranteedIssueMaximum;

                //CHBenefitAmountDisplay();
            }

            if (productArray[i].tdiStateAmountOffered == null) {
                guideBridge.resolveNode("AmtOfferedTableTdiState").visible = false;
                guideBridge.resolveNode("tdiIncrements").visible = false;
                guideBridge.resolveNode("tdiMinAmtElect").visible = false;
                guideBridge.resolveNode("tdiMaxAmtElect").visible = false;
                guideBridge.resolveNode("TdiGuaranteedMax").visible = false;
            }
            else {
                guideBridge.resolveNode("AmtOfferedTableTdiState").visible = true;
                guideBridge.resolveNode("tdiIncrements").visible = true;
                guideBridge.resolveNode("tdiMinAmtElect").visible = true;
                guideBridge.resolveNode("tdiMaxAmtElect").visible = true;
                guideBridge.resolveNode("TdiGuaranteedMax").visible = true;
                guideBridge.resolveNode("tdiIncrements").value = productArray[i].tdiStateAmountOffered.amountOffered.increments;
                guideBridge.resolveNode("tdiMinAmtElect").value = productArray[i].tdiStateAmountOffered.amountOffered.minimumAmtElect;
                guideBridge.resolveNode("tdiMaxAmtElect").value = productArray[i].tdiStateAmountOffered.amountOffered.maximumAmtElect;
                guideBridge.resolveNode("TdiGuaranteedMax").value = productArray[i].tdiStateAmountOffered.amountOffered.guaranteedIssueMaximum;

                //TDIStatesBenefitAmountDisplay();
            }


            if (productArray[i].childIndividualAmountOffered == null) {
                guideBridge.resolveNode("AmtOfferedTableChildIndividual").visible = false;
                guideBridge.resolveNode("childIndividualIncrements").visible = false;
                guideBridge.resolveNode("childIndividualMinAmtElect").visible = false;
                guideBridge.resolveNode("childIndividualMaxAmtElect").visible = false;
                guideBridge.resolveNode("ChildIndividualGuaranteedMax").visible = false;
            }
            else {
                guideBridge.resolveNode("AmtOfferedTableChildIndividual").visible = true;
                guideBridge.resolveNode("childIndividualIncrements").visible = true;
                guideBridge.resolveNode("childIndividualMinAmtElect").visible = true;
                guideBridge.resolveNode("childIndividualMaxAmtElect").visible = true;
                guideBridge.resolveNode("ChildIndividualGuaranteedMax").visible = true;
                guideBridge.resolveNode("childIndividualIncrements").value = productArray[i].childIndividualAmountOffered.amountOffered.increments;
                guideBridge.resolveNode("childIndividualMinAmtElect").value = productArray[i].childIndividualAmountOffered.amountOffered.minimumAmtElect;
                guideBridge.resolveNode("childIndividualMaxAmtElect").value = productArray[i].childIndividualAmountOffered.amountOffered.maximumAmtElect;
                guideBridge.resolveNode("ChildIndividualGuaranteedMax").value = productArray[i].childIndividualAmountOffered.amountOffered.guaranteedIssueMaximum;

                //CHIndividualPolicyBenefitAmountDisplay();
            }


            if (productArray[i].childTermAmountOffered == null) {
                guideBridge.resolveNode("AmtOfferedTableChildTerm").visible = false;
                guideBridge.resolveNode("childTermIncrements").visible = false;
                guideBridge.resolveNode("childTermMinAmtElect").visible = false;
                guideBridge.resolveNode("childTermMaxAmtElect").visible = false;
                guideBridge.resolveNode("ChildTermGuaranteedMax").visible = false;
            }
            else {
                guideBridge.resolveNode("AmtOfferedTableChildTerm").visible = true;
                guideBridge.resolveNode("childTermIncrements").visible = true;
                guideBridge.resolveNode("childTermMinAmtElect").visible = true;
                guideBridge.resolveNode("childTermMaxAmtElect").visible = true;
                guideBridge.resolveNode("ChildTermGuaranteedMax").visible = true;
                guideBridge.resolveNode("childTermIncrements").value = productArray[i].childTermAmountOffered.amountOffered.increments;
                guideBridge.resolveNode("childTermMinAmtElect").value = productArray[i].childTermAmountOffered.amountOffered.minimumAmtElect;
                guideBridge.resolveNode("childTermMaxAmtElect").value = productArray[i].childTermAmountOffered.amountOffered.maximumAmtElect;
                guideBridge.resolveNode("ChildTermGuaranteedMax").value = productArray[i].childTermAmountOffered.amountOffered.guaranteedIssueMaximum;
            }
            
            if(productArray[i].benefitAmountPercentage == null){
                guideBridge.resolveNode("benefitAmountPercentage").visible = false;
            }
            else{
                guideBridge.resolveNode("benefitAmountPercentage").visible = true;
                guideBridge.resolveNode("benefitAmountPercentage").value = productArray[i].benefitAmountPercentage;
            }

            if (productArray[i].initialEnrollment == null) {
                guideBridge.resolveNode("initialEmployementText").visible = false;
                guideBridge.resolveNode("initialEmployeement_employee").visible = false;
                guideBridge.resolveNode("initialEmployeement_spouse").visible = false;
                guideBridge.resolveNode("initialEmployeement_spouseCoverage").visible = false;

            }
            else {
                guideBridge.resolveNode("initialEmployeement_employee").value = productArray[i].initialEnrollment.employee;
                guideBridge.resolveNode("initialEmployeement_spouse").value = productArray[i].initialEnrollment.spouse;
                guideBridge.resolveNode("initialEmployeement_spouseCoverage").value = productArray[i].initialEnrollment.spouseCoverage;
            }

            if (productArray[i].benefitType == null) {
                guideBridge.resolveNode("BenefitType").visible = false;
            }
            else {
                guideBridge.resolveNode("BenefitType").visible = true;
                guideBridge.resolveNode("BenefitType").value = productArray[i].benefitType;
            }
            if (productArray[i].benefitPeriod == null) {
                guideBridge.resolveNode("BenefitPeriod").visible = false;
            }
            else {
                guideBridge.resolveNode("BenefitPeriod").visible = true;
                guideBridge.resolveNode("BenefitPeriod").value = productArray[i].benefitPeriod;
            }
            if (productArray[i].eliminationPeriod == null) {
                guideBridge.resolveNode("EliminationPeriod").visible = false;
            }
            else {
                guideBridge.resolveNode("EliminationPeriod").visible = true;
                guideBridge.resolveNode("EliminationPeriod").value = productArray[i].eliminationPeriod;
            }
            if (productArray[i].term == null) {
                guideBridge.resolveNode("Term").visible = false;
            }
            else {
                guideBridge.resolveNode("Term").visible = true;
                guideBridge.resolveNode("Term").value = productArray[i].term;
            }
            if (productArray[i].tobacco == null) {
                guideBridge.resolveNode("TobaccoStatus").visible = false;
                guideBridge.resolveNode("TobaccoStatusDetermined").visible = false;
            }
            else {
                guideBridge.resolveNode("TobaccoStatus").visible = true;
                guideBridge.resolveNode("TobaccoStatusDetermined").visible = true;
                guideBridge.resolveNode("TobaccoStatus").value = productArray[i].tobacco.tobaccoStatus;
                guideBridge.resolveNode("TobaccoStatusDetermined").value = productArray[i].tobacco.tobaccoStatusDetermined;
            }
            if (productArray[i].platformDriven == null || termto120LOBs.includes(productArray[i].productName)) {
                guideBridge.resolveNode("PlatformDriven").visible = false;
            }
            else {
                guideBridge.resolveNode("PlatformDriven").visible = true;
                guideBridge.resolveNode("PlatformDriven").value = productArray[i].platformDriven;
            }
            if (productArray[i].ageCalculated == null) {
                guideBridge.resolveNode("AgeCalculated").visible = false;
            }
            else {
                guideBridge.resolveNode("AgeCalculated").visible = true;
                guideBridge.resolveNode("AgeCalculated").value = productArray[i].ageCalculated;
            }
            if (productArray[i].ageCalculation == null) {
                guideBridge.resolveNode("AgeCalculation").visible = false;
            }
            else {
                guideBridge.resolveNode("AgeCalculation").visible = true;
                guideBridge.resolveNode("AgeCalculation").items = setAgeCalculationItems(productArray[i].productName);
                guideBridge.resolveNode("AgeCalculation").value = productArray[i].ageCalculation;
            }
            
            if (productArray[i].issueAgeType == null) {
                guideBridge.resolveNode("issueAgeType").visible = false;
            }
            else {
                guideBridge.resolveNode("issueAgeType").visible = true;
                guideBridge.resolveNode("issueAgeType").value = productArray[i].issueAgeType;
            }

            if (productArray[i].employeeIssueAge == null) {
                guideBridge.resolveNode("EmployeeIssueAge").visible = false;
            }
            else {
                guideBridge.resolveNode("EmployeeIssueAge").visible = true;
                guideBridge.resolveNode("EmployeeIssueAge").value = productArray[i].employeeIssueAge;
            }

            if (productArray[i].spouseIssueAge == null) {
                guideBridge.resolveNode("SpouseIssueAge").visible = false;
            }
            else {
                guideBridge.resolveNode("SpouseIssueAge").visible = true;
                guideBridge.resolveNode("SpouseIssueAge").value = productArray[i].spouseIssueAge;
            }

            if (productArray[i].childIssueAge == null || criticalIllnessLOBs.includes(productArray[i].productName)) {
                guideBridge.resolveNode("ChildIssueAge").visible = false;
                guideBridge.resolveNode("ChildIssueAge").value = "";
            }
            else {
                guideBridge.resolveNode("ChildIssueAge").visible = true;
                guideBridge.resolveNode("ChildIssueAge").value = productArray[i].childIssueAge;
                //guideBridge.resolveNode("childIssueAgeUnit").visible = true;
                //setChildIssueAge(productArray[i].childIssueAge);
                //onChangingChildAgeUnit();
            }

            if(productArray[i].employeeTerminationAge==null){
                guideBridge.resolveNode("employeeTerminationAge").value = "";
            }
            else{
                guideBridge.resolveNode("employeeTerminationAge").value = productArray[i].employeeTerminationAge;
            }

            if(productArray[i].spouseTerminationAge==null){
                guideBridge.resolveNode("spouseTerminationAge").value = "";
            }
            else{
                guideBridge.resolveNode("spouseTerminationAge").value = productArray[i].spouseTerminationAge;
            }

            if(productArray[i].childTerminationAge==null || criticalIllnessLOBs.includes(productArray[i].productName)){
                guideBridge.resolveNode("childTerminationAge").value = "";
            }
            else{
                guideBridge.resolveNode("childTerminationAge").value = productArray[i].childTerminationAge;
            }


            if(!((hospitalIndemnityLOBs.includes(productArray[i].productName)) || (benExtendLOBs.includes(productArray[i].productName)))){
                guideBridge.resolveNode("hundredPercentGuranteed").visible = false;
            }
            else{
                guideBridge.resolveNode("hundredPercentGuranteed").visible = true;
                guideBridge.resolveNode("hundredPercentGuranteed").value = productArray[i].hundredPercentGuranteed;
            }

            if(productArray[i].eligibleEmployeeGuaranteedIssue==null){
                guideBridge.resolveNode("eligibleEmployeeGuaranteedIssue").value="";
                guideBridge.resolveNode("eligibleEmployeeGuaranteedIssue").visible = false;
            }
            else{
                guideBridge.resolveNode("eligibleEmployeeGuaranteedIssue").visible = true;
                guideBridge.resolveNode("eligibleEmployeeGuaranteedIssue").value=productArray[i].eligibleEmployeeGuaranteedIssue;
            }

            guideBridge.resolveNode("ApplicationNumber").value = productArray[i].applicationNumber;
            guideBridge.resolveNode("ApplicationNoApi").value = productArray[i].applicationNumber;
            guideBridge.resolveNode("TaxType").value = productArray[i].taxType;
            // guideBridge.resolveNode("HoursWorkedPerWeek").value = productArray[i].hoursWorkedPerWeek;
            // guideBridge.resolveNode("EligibilityWaitingPeriod").value = productArray[i].eligibilityWaitingPeriod;

            if(productArray[i].otherInstructions==null){
                guideBridge.resolveNode("instructionsNeededCheckbox").value="No";
                guideBridge.resolveNode("otherInstructions").visible = false;
            }
            else{
                guideBridge.resolveNode("instructionsNeededCheckbox").value="Yes";
                guideBridge.resolveNode("otherInstructions").value=productArray[i].otherInstructions;
            }

            if(productArray[i].isChildCoverageOffered=="Y"){
                guideBridge.resolveNode("childCoverageOfferedCheckbox").value=productArray[i].isChildCoverageOffered;
                guideBridge.resolveNode("childCoverageOptions").value=productArray[i].childCoverageOffered;
            }

            if(productArray[i].isChildTermlifeRiderOffered=="Y"){
                guideBridge.resolveNode("childTermLifeOfferedCheckbox").value=productArray[i].isChildTermlifeRiderOffered;
            }

            if(productArray[i].isUniqueIncrementsUtilized=="Y"){
                guideBridge.resolveNode("uniqueIncrementsUtilized").value=productArray[i].isUniqueIncrementsUtilized;
                guideBridge.resolveNode("uniqueIncrementsText").value=productArray[i].benefitAmountDescription;
            }

            // if(productArray[i].isProductAro !=null){
            //     guideBridge.resolveNode("isProductAro").value=productArray[i].isProductAro;
            // }else{
            //     guideBridge.resolveNode("isProductAro").value="";
            //     guideBridge.resolveNode("isProductAro").visible=false;
            // }

            if(wholeLifeLOBs.includes(productArray[i].productName) || termLifeLOBs.includes(productArray[i].productName) || termLifeLOBs.includes(productArray[i].productName.split('-')[0]) || termto120LOBs.includes(productArray[i].productName.split('-')[0])){
                guideBridge.resolveNode("isProductAro").value="";
                guideBridge.resolveNode("isProductAro").visible=false;
            }
            else {
                guideBridge.resolveNode("isProductAro").value=productArray[i].isProductAro;
            }
        }
        guideBridge.resolveNode("isCI22Kenabled").value = data.isCi22KEnabled;
    }
}

function getCaseBuilderToolProductsDataNew(groupNumber, effectiveDate, enrollmentPlatform, availableProducts, numberOfDisabilityMultiPlans, numberOfTermLifeMultiPlans) {
    // onChangingChildAgeUnit();
    locationsData=null;
    guideBridge.resolveNode("formCase").value = "Add";
    guideBridge.resolveNode("AI_CoverageBillingEffDate").enabled = false;
    guideBridge.resolveNode("validateCoverageEffDate").enabled = false;
    guideBridge.resolveNode("IsCoverageEffDateChanged").value = "Yes";
    setProductionTestFileNaming(enrollmentPlatform);
    var res = JSON.parse(
        $.ajax({
            url: "/bin/GetCaseBuilderToolProductsData",
            type: "GET",
            async: false,
            data: {
                "groupNumber": groupNumber,
                "effectiveDate": effectiveDate,
                "enrollmentPlatform": enrollmentPlatform,
                "mode": "add",
                "availableProducts": availableProducts,
                "numberOfDisabilityMultiPlans": numberOfDisabilityMultiPlans,
                "numberOfTermLifeMultiPlans": numberOfTermLifeMultiPlans
            },
            success: function (data) {
                var productArray = data.products.product;
                var repeatPanel = guideBridge.resolveNode("Product");
                var repeatPanel2 = guideBridge.resolveNode("PlanDetails");

                //Reset Panels
                repeatPanel.instanceManager.addInstance();
                var currentCount = repeatPanel.instanceManager.instanceCount;
                for (var m = 0; m < currentCount; m++) {
                    if (m != 0) {
                        repeatPanel.instanceManager.removeInstance(0);
                    }

                }

                for (var i = 0; i < productArray.length; i++) {
                    if (i != 0) {
                        repeatPanel.instanceManager.addInstance();
                        repeatPanel2.instanceManager.addInstance();
                    }
                    // setting product panel data
                    repeatPanel.instanceManager.instances[i].title = productArray[i].productName;
                    repeatPanel.instanceManager.instances[i].summary = productArray[i].productName;
                    guideBridge.resolveNode("productName").value = productArray[i].productName;
                    guideBridge.resolveNode("productId").value = (i+1);
                    // var checkPlanNameDisability = checkDisabilityPlanName(productArray[i].productName);
                    // if(checkPlanNameDisability!=null){
                    //     var disabilityPlanName = checkPlanNameDisability;
                    //     guideBridge.resolveNode("Series").items = setSeries(disabilityPlanName);
                    // }
                    // else{
                    //     disabilityPlanName =null;
                    //     guideBridge.resolveNode("Series").items = setSeries(guideBridge.resolveNode("productName").value);
                    // }
                    // if(disabilityLOBs.includes(productArray[i].productName) || disabilityLOBs.includes(productArray[i].productName.split('-')[0])){
                    //     guideBridge.resolveNode("Series").items = setSeries(productArray[i].productName.split('-')[0]);
                    // }
                    // else{
                    //     guideBridge.resolveNode("Series").items = setSeries(guideBridge.resolveNode("productName").value);
                    // }
                    guideBridge.resolveNode("Series").items = setSeries(guideBridge.resolveNode("productName").value);
                    
                    guideBridge.resolveNode("PlanName").value = setPlanName(guideBridge.resolveNode("productName").value);

                    setPlanNameItems(guideBridge.resolveNode("productName").value);

                    if (accidentLOBs.includes(productArray[i].productName)|| hospitalIndemnityLOBs.includes(productArray[i].productName)|| benExtendLOBs.includes(productArray[i].productName)) {
                        guideBridge.resolveNode("AmtOfferedTable").visible = false;
                        // guideBridge.resolveNode("updateBenefitAmountsDisplay").visible = false;

                    }
                    if (criticalIllnessLOBs.includes(productArray[i].productName)) {
                        guideBridge.resolveNode("updateBenefitAmountsDisplay").visible = true;
                    } else {
                        guideBridge.resolveNode("updateBenefitAmountsDisplay").visible = false;
                    }

                    if (hospitalIndemnityLOBs.includes(productArray[i].productName)|| benExtendLOBs.includes(productArray[i].productName)) {
                        guideBridge.resolveNode("hundredPercentGuranteed").visible = true;
                    }

                    if (accidentLOBs.includes(productArray[i].productName) || hospitalIndemnityLOBs.includes(productArray[i].productName) || benExtendLOBs.includes(productArray[i].productName) || criticalIllnessLOBs.includes(productArray[i].productName) || wholeLifeLOBs.includes(productArray[i].productName)) {
                        guideBridge.resolveNode("EmployeeIssueAge").visible = true;
                        guideBridge.resolveNode("EmployeeIssueAge").value = "";
                        //guideBridge.resolveNode("EmployeeIssueAge").value ="18+, no age restriction on enrollment";

                        guideBridge.resolveNode("SpouseIssueAge").visible = true;
                        guideBridge.resolveNode("SpouseIssueAge").value = "";
                        //guideBridge.resolveNode("SpouseIssueAge").value = "18+, no age restriction on enrollment";
                        //guideBridge.resolveNode("childIssueAgeUnit").visible = true;
                        guideBridge.resolveNode("ChildIssueAge").visible = true;
                        guideBridge.resolveNode("ChildIssueAge").value = "";
                       // guideBridge.resolveNode("ChildIssueAge").value = "0 to 25, Coverage Terminates the end of the month of their 26 birthday.";
                    }


                    if (benExtendLOBs.includes(productArray[i].productName) || disabilityLOBs.includes(productArray[i].productName)|| disabilityLOBs.includes(productArray[i].productName.split('-')[0]) || wholeLifeLOBs.includes(productArray[i].productName) || termLifeLOBs.includes(productArray[i].productName) || termLifeLOBs.includes(productArray[i].productName.split('-')[0]) || termto120LOBs.includes(productArray[i].productName)) {
                        guideBridge.resolveNode("Participation").visible = true;
                    }

                    if (criticalIllnessLOBs.includes(productArray[i].productName)) {
                        guideBridge.resolveNode("PlatformDriven").visible = true;
                        guideBridge.resolveNode("AgeCalculated").visible = true;
                        guideBridge.resolveNode("AgeCalculation").visible = true;
                        guideBridge.resolveNode("AgeCalculation").items = setAgeCalculationItems(productArray[i].productName);
                        // guideBridge.resolveNode("progressiveRider").visible = true;
                        // guideBridge.resolveNode("optionalRider").visible = true;
                        guideBridge.resolveNode("ChildIssueAge").visible = false;
                        guideBridge.resolveNode("ChildIssueAge").value = "";
                        guideBridge.resolveNode("AmtOfferedTable").visible = true;
                        guideBridge.resolveNode("AmtOfferedTableEmployee").visible = true;
                        guideBridge.resolveNode("AmtOfferedTableSpouse").visible = true;
                        guideBridge.resolveNode("benefitAmountPercentage").visible = true;
                        //guideBridge.resolveNode("initialEmployeement_employee").visible = true;
                        //guideBridge.resolveNode("initialEmployeement_spouse").visible = true;
                        guideBridge.resolveNode("TobaccoStatus").visible = true;
                        guideBridge.resolveNode("TobaccoStatusDetermined").visible = true;
                        
                    }

                    if (disabilityLOBs.includes(productArray[i].productName) || disabilityLOBs.includes(productArray[i].productName.split('-')[0])) {
                        guideBridge.resolveNode("EmployeeIssueAge").visible = true;
                        guideBridge.resolveNode("EmployeeIssueAge").value = "";
                        //guideBridge.resolveNode("EmployeeIssueAge").value = "18+, no age restriction on enrollment";
                        guideBridge.resolveNode("SpouseIssueAge").visible = false;
                        guideBridge.resolveNode("ChildIssueAge").visible = false;
                        guideBridge.resolveNode("PlatformDriven").visible = true;
                        guideBridge.resolveNode("PlatformDriven").value = productArray[i].platformDriven;
                        guideBridge.resolveNode("EligibleEmployees").visible = true;
                        guideBridge.resolveNode("BenefitType").visible = true;
                        guideBridge.resolveNode("BenefitPeriod").visible = true;
                        guideBridge.resolveNode("AgeCalculation").visible = true;
                        guideBridge.resolveNode("AgeCalculation").items = setAgeCalculationItems(productArray[i].productName);
                        guideBridge.resolveNode("AmtOfferedTable").visible = true;
                        guideBridge.resolveNode("AmtOfferedTableEmployee").visible = true;
                        guideBridge.resolveNode("AmtOfferedTableTdiState").visible = true;
                        guideBridge.resolveNode("uniqueIncrementsUtilized").visible = true;
                        //guideBridge.resolveNode("initialEmployeement_employee").visible = true;
                        guideBridge.resolveNode("EliminationPeriod").visible = true;
                        //guideBridge.resolveNode("CoverageLevel").visible = true;
                        //guideBridge.resolveNode("HoursWorkedPerWeek").value = 19;
                        guideBridge.resolveNode("diHoursWorkedPerWeek").visible = true;
                        guideBridge.resolveNode("Series").items = setSeries(productArray[i].productName.split('-')[0]);
                    }

                    if (wholeLifeLOBs.includes(productArray[i].productName)) {
                        guideBridge.resolveNode("isProductAro").visible = false;
                        guideBridge.resolveNode("EmployeeIssueAge").value="";
                        //guideBridge.resolveNode("EmployeeIssueAge").value="18-70, cannot enroll after age 70";

                        guideBridge.resolveNode("SpouseIssueAge").value = "";
                        //guideBridge.resolveNode("SpouseIssueAge").value = "18-70, cannot enroll after age 70";

                        guideBridge.resolveNode("ChildIssueAge").value = ""
                        //guideBridge.resolveNode("ChildIssueAge").value = "15 days - 26 years old,Coverage Terminates the end of the month of their 26 birthday."

                        guideBridge.resolveNode("PlatformDriven").visible = true;
                        guideBridge.resolveNode("PlatformDriven").value = productArray[i].platformDriven;
                        guideBridge.resolveNode("AgeCalculated").visible = true;
                        guideBridge.resolveNode("EligibleEmployees").visible = true;
                        // guideBridge.resolveNode("AcceleratedDeathBenefitOffered").visible = true; 
                        guideBridge.resolveNode("AmtOfferedTable").visible = true;
                        guideBridge.resolveNode("AmtOfferedTableEmployee").visible = true;
                        guideBridge.resolveNode("AmtOfferedTableSpouse").visible = true;
                        // guideBridge.resolveNode("AmtOfferedTableChildIndividual").visible = true;
                        // guideBridge.resolveNode("AmtOfferedTableChildTerm").visible = true;
                        guideBridge.resolveNode("childCoverageOfferedCheckbox").visible = true;
                        guideBridge.resolveNode("childTermLifeOfferedCheckbox").visible = true;
                        guideBridge.resolveNode("uniqueIncrementsUtilized").visible = true;
                        //guideBridge.resolveNode("initialEmployeement_employee").visible = true;
                        //guideBridge.resolveNode("initialEmployeement_spouse").visible = true;
                        guideBridge.resolveNode("TobaccoStatus").visible = true;
                        guideBridge.resolveNode("TobaccoStatusDetermined").visible = true;
                        guideBridge.resolveNode("benefitAmountPercentage").visible = true;
                        
                    }

                    if (termLifeLOBs.includes(productArray[i].productName) || termLifeLOBs.includes(productArray[i].productName.split('-')[0])) {
                        guideBridge.resolveNode("isProductAro").visible = false;
                        guideBridge.resolveNode("EmployeeIssueAge").visible = true;
                        guideBridge.resolveNode("EmployeeIssueAge").value = "";
                       // guideBridge.resolveNode("EmployeeIssueAge").value = "18+, no age restriction on enrollment";

                        guideBridge.resolveNode("SpouseIssueAge").visible = true;
                        guideBridge.resolveNode("SpouseIssueAge").value = "";
                        //guideBridge.resolveNode("SpouseIssueAge").value = "18+, no age restriction on enrollment";
                        //guideBridge.resolveNode("childIssueAgeUnit").visible = true;
                        guideBridge.resolveNode("ChildIssueAge").visible = true;
                        guideBridge.resolveNode("ChildIssueAge").value = "";
                        //guideBridge.resolveNode("ChildIssueAge").value = "0 to 25, Coverage Terminates the end of the month of their 26 birthday.";

                        guideBridge.resolveNode("PlatformDriven").visible = true;
                        guideBridge.resolveNode("PlatformDriven").value = productArray[i].platformDriven;
                        guideBridge.resolveNode("AgeCalculated").visible = true;
                        guideBridge.resolveNode("EligibleEmployees").visible = true;
                        guideBridge.resolveNode("AmtOfferedTable").visible = true;
                        guideBridge.resolveNode("AmtOfferedTableEmployee").visible = true;
                        guideBridge.resolveNode("AmtOfferedTableSpouse").visible = true;
                        guideBridge.resolveNode("AmtOfferedTableChild").visible = true;
                        guideBridge.resolveNode("uniqueIncrementsUtilized").visible = true;
                        //guideBridge.resolveNode("initialEmployeement_employee").visible = true;
                        //guideBridge.resolveNode("initialEmployeement_spouse").visible = true;
                        guideBridge.resolveNode("TobaccoStatus").visible = true;
                        guideBridge.resolveNode("TobaccoStatusDetermined").visible = true;
                        guideBridge.resolveNode("Term").visible = true;
                        guideBridge.resolveNode("benefitAmountPercentage").visible = true;
                        guideBridge.resolveNode("Series").items = setSeries(productArray[i].productName.split('-')[0]);
                    }

                    if (termto120LOBs.includes(productArray[i].productName)) {
                        guideBridge.resolveNode("isProductAro").visible = false;
                        guideBridge.resolveNode("EmployeeIssueAge").visible = true;
                        guideBridge.resolveNode("EmployeeIssueAge").value = "";

                        guideBridge.resolveNode("SpouseIssueAge").visible = true;
                        guideBridge.resolveNode("SpouseIssueAge").value = "";
                        
                        guideBridge.resolveNode("ChildIssueAge").visible = true;
                        guideBridge.resolveNode("ChildIssueAge").value = "";

                        guideBridge.resolveNode("AgeCalculation").visible = true;
                        guideBridge.resolveNode("AgeCalculation").items = setAgeCalculationItems(productArray[i].productName);
                        guideBridge.resolveNode("AgeCalculated").visible = true;
                        guideBridge.resolveNode("EligibleEmployees").visible = true;
                        guideBridge.resolveNode("AmtOfferedTable").visible = true;
                        guideBridge.resolveNode("AmtOfferedTableEmployee").visible = true;
                        guideBridge.resolveNode("AmtOfferedTableSpouse").visible = true;
                        guideBridge.resolveNode("AmtOfferedTableChild").visible = true;
                        guideBridge.resolveNode("uniqueIncrementsUtilized").visible = true;
                        guideBridge.resolveNode("TobaccoStatus").visible = true;
                        guideBridge.resolveNode("TobaccoStatusDetermined").visible = true;
                        guideBridge.resolveNode("benefitAmountPercentage").visible = true;

                    }

                    if(guideBridge.resolveNode("AgeCalculated").visible === true){
                        if(enrollmentDetail['spouse-tobacco-rate-status'] === true){
                            guideBridge.resolveNode("AgeCalculated").value = "Employee & Spouse Ages Captured Separately";
                            guideBridge.resolveNode("TobaccoStatusDetermined").value = "Employee & Spouse Tobacco Status Captured Separately";
                        }
                        else {
                            guideBridge.resolveNode("AgeCalculated").value = "Spouse Age based off of Employee Age";
                            guideBridge.resolveNode("TobaccoStatusDetermined").value = "Spouse Tobacco Status based off of Employee";
                        }
                    }
                }
            }
        })
            .responseText);
}

function validEndDate(startDate,endDate) {
    var start = guideBridge.resolveNode("AI_OpenEnrollmentStartDate");
    var end = guideBridge.resolveNode("AI_OpenEnrollmentEndDate");
	var InfoStartDate = new Date(startDate);
	var InfoEndDate = new Date(endDate);
    if (InfoStartDate < InfoEndDate) {
        document.getElementById(end.id).className = "guideFieldNode guideDatePicker AI_OpenEnrollmentEndDate defaultFieldLayout guideActiveField af-field-filled validation-failure ";
        var alert = document.getElementById(end.id).children[2];
        alert.setAttribute('role', 'alert');
        var alertid = "#" + alert.id;
        $(alertid).html("End date should be after Start date");
    }
    else {
        document.getElementById(end.id).className = "guideFieldNode guideDatePicker AI_OpenEnrollmentEndDate defaultFieldLayout guideActiveField af-field-filled validation-success ";
    }
}

function EndDateValStatus(startDate, endDate){
    if(startDate != null && endDate != null){
        var InfoStartDate = new Date(startDate);
        var InfoEndDate = new Date(endDate);
        if(InfoEndDate < InfoStartDate) {
            //alert('Start Date can not be after End Date');
            guideBridge.resolveNode("AI_OpenEnrollmentEndDate").value = "";
            guideBridge.resolveNode("endDateValidationMessage").visible = true;

            $(document).on('change', function() {
                guideBridge.resolveNode("endDateValidationMessage").visible = false;
            });
        }
        else {
            guideBridge.resolveNode("endDateValidationMessage").visible = false;
        }
        // var enrollmentDatesValidationStatus = 'Fail';
        // if(InfoEndDate > InfoStartDate) {
        //     enrollmentDatesValidationStatus = 'Pass';
        //     document.querySelectorAll(".enrollmentDatesValidation input")[0].style.borderColor = "#00a7e1";
        //     document.querySelectorAll(".enrollmentDatesValidation input")[0].style.color = "#555";
        // }
        // else{
        //     enrollmentDatesValidationStatus = 'Fail';
        //     document.querySelectorAll(".enrollmentDatesValidation input")[0].style.borderColor = "red";
        //     document.querySelectorAll(".enrollmentDatesValidation input")[0].style.color = "red";
        // }
        // guideBridge.resolveNode("enrollmentDatesValidation").value = enrollmentDatesValidationStatus;
    }	
}


// function DueDateValStatus(startDate, dueDate){	
// 	var InfoDueDate = new Date(dueDate);
// 	var InfoStartDate = new Date(startDate);
// 	var currentDate = new Date();
// 	var complianceReviewDateValidationStatus = 'Fail';
// 	if(InfoDueDate < InfoStartDate && currentDate < InfoDueDate) {
// 		complianceReviewDateValidationStatus = 'Pass';
// 		document.querySelectorAll(".complianceReviewDateValidation input")[0].style.borderColor = "#00a7e1";
// 		document.querySelectorAll(".complianceReviewDateValidation input")[0].style.color = "#555";
// 	}
// 	else{
// 		complianceReviewDateValidationStatus = 'Fail';
// 		document.querySelectorAll(".complianceReviewDateValidation input")[0].style.borderColor = "red";
// 		document.querySelectorAll(".complianceReviewDateValidation input")[0].style.color = "red";
// 	}
// 	guideBridge.resolveNode("complianceReviewDateValidation").value = complianceReviewDateValidationStatus	
// }

function ValidCoverageDate(CoverageDate) {
    var CovDate = new Date(CoverageDate);
    var currentDate = new Date();
    var aYearFromNow = new Date(currentDate.getFullYear() + 1, currentDate.getMonth(), currentDate.getDate());
    var Cov = guideBridge.resolveNode("EffectiveDate");
    if (CovDate > aYearFromNow) {
        document.getElementById(Cov.id).className = "guideFieldNode guideDatePicker EffectiveDate defaultFieldLayout guideActiveField af-field-filled validation-failure ";
        var alert = document.getElementById(Cov.id).children[2];
        alert.setAttribute('role', 'alert');
        var alertid = "#" + alert.id;
        $(alertid).html("Coverage Date should not exceed 1 year after today's date.");
        Cov.value="";
    } else {
        document.getElementById(Cov.id).className = "guideFieldNode guideDatePicker EffectiveDate defaultFieldLayout guideActiveField af-field-filled validation-success ";
    }
}

function ValidFirstCoverageEffectiveDate(CoverageDate) {
    var CovDate = new Date(CoverageDate);
    var currentDate = new Date();
    var aYearFromNow = new Date(currentDate.getFullYear() + 1, currentDate.getMonth(), currentDate.getDate());
    var Cov = guideBridge.resolveNode("AI_CoverageBillingEffDate");
    if (CovDate > aYearFromNow) {
        document.getElementById(Cov.id).className = "guideFieldNode guideDatePicker AI_CoverageBillingEffDate defaultFieldLayout guideActiveField af-field-filled validation-failure ";
        Cov.value="";
        var alert = document.getElementById(Cov.id).children[2];
        alert.setAttribute('role', 'alert');
        var alertid = "#" + alert.id;
        $(alertid).html("Coverage Date should not exceed 1 year after today's date.");
    } else {
        document.getElementById(Cov.id).className = "guideFieldNode guideDatePicker AI_CoverageBillingEffDate defaultFieldLayout guideActiveField af-field-filled validation-success ";
    }
}
function setSeries(productName) {
    for (var i = 0; i < LOBseries.length; i++) {
        if (LOBseries[i].LOB.includes(productName)) {
            return LOBseries[i].Series;
        }
    }
}

function setAgeCalculationItems(productName) {
    for (var i = 0; i < LOBseries.length; i++) {
        if (LOBseries[i].LOB.includes(productName) || LOBseries[i].LOB.includes(productName.split('-')[0])) {
            return LOBseries[i].ageCalculation;
        }
    }
}

function getApplicationNumber(situsState,series,groupType,formCase,ApplicationNumberSOM,seriesSom,seriesApiSom){
    var field = guideBridge.resolveNode(ApplicationNumberSOM);
    var formCase = guideBridge.resolveNode("formCase").value;
    if(formCase=="Add"){
        $.ajax({
            url: "/bin/GetCaseBuilderToolProductsData",
            type: 'GET',
            data: { mode: "applicationNo", situs: situsState, series: series , groupType: groupType},
            dataType: 'json', // added data type
            success: function (res) {
                if ( res["form-id"] == null){
                    //window.alert("Application no. not found.");
                    guideBridge.resolveNode(ApplicationNumberSOM).value = "";
                    document.getElementById(field.id).className = "guideFieldNode guideTextBox ApplicationNumber defaultFieldLayout af-field-filled validation-failure";
                    setTimeout(function() {
                    var alert = document.getElementById(field.id).children[3];
                    alert.setAttribute('role', 'alert');
                    var alertid = "#" + alert.id;
                    $(alertid).html("Application number not found.");
                    },10);
                }
                else{
                    guideBridge.resolveNode(ApplicationNumberSOM).value = res["form-id"];
                    document.getElementById(field.id).className = "guideFieldNode guideTextBox ApplicationNumber defaultFieldLayout af-field-filled validation-success ";
                }
            }
        });
    }
    else if(formCase=="Edit" || formCase=="Update"){
        var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;
        var seriesApiValue=getSeriesApiValue(productName);
        if(situsState!=null && series!=null && groupType!=null){
            if(situsState!= caseBuildResponse.accountInformation.situsState || groupType!=caseBuildResponse.accountInformation.groupType || guideBridge.resolveNode(seriesSom).value!=seriesApiValue){
                $.ajax({
                    url: "/bin/GetCaseBuilderToolProductsData",
                    type: 'GET',
                    data: { mode: "applicationNo", situs: situsState, series: series , groupType: groupType},
                    dataType: 'json', // added data type
                    success: function (res) {
                        if ( res["form-id"] == null){
                            //window.alert("Application no. not found.");
                            guideBridge.resolveNode(ApplicationNumberSOM).value = "";
                            document.getElementById(field.id).className = "guideFieldNode guideTextBox ApplicationNumber defaultFieldLayout af-field-filled validation-failure";
                            setTimeout(function() {
                            var alert = document.getElementById(field.id).children[3];
                            alert.setAttribute('role', 'alert');
                            var alertid = "#" + alert.id;
                            $(alertid).html("Application number not found.");
                            },10);
                        }
                        else{
                            guideBridge.resolveNode(ApplicationNumberSOM).value = res["form-id"];
                            document.getElementById(field.id).className = "guideFieldNode guideTextBox ApplicationNumber defaultFieldLayout af-field-filled validation-success ";
                        }
                    }
                });
            }
        }
    }
    
}

function getPlatformDriven(lob,platform,ageCal){
    if(guideBridge.resolveNode("formCase").value == 'Add' || guideBridge.resolveNode("formCase").value == 'Update'){
        lob =  guideBridge.resolveNode("Product")._children[1].PlanName.value;
        var field = guideBridge.resolveNode("PlatformDriven");
        $.ajax({
            url: "/bin/GetCaseBuilderToolProductsData",
            type: 'GET',
            data: { mode: "ageBasedOn", lob: lob, platform: platform , ageCal: ageCal},
            dataType: 'json',
            success: function (res) {
                if ( res["issue-age-type"] == null){
                    guideBridge.resolveNode("issueAgeType").value = "";
                }
                else{
                    guideBridge.resolveNode("issueAgeType").value = res["issue-age-type"];
                }
                if ( res["age-method"] == null){
                    guideBridge.resolveNode("Product")._children[3].PlatformDriven.value = "";
                    for(var i=0; i<document.querySelectorAll(".PlatformDriven").length; i++){
                        if(document.querySelectorAll(".PlatformDriven")[i].id == guideBridge.resolveNode("Product")._children[3].PlatformDriven.id){
                            document.querySelectorAll(".PlatformDriven select")[i].value = '';
                        }
                    }
                }
                else{
                    guideBridge.resolveNode("Product")._children[3].PlatformDriven.value = res["age-method"];
                }
            }
        });
    }
}

function getIssueAges(situsState,series,product){
    var term = guideBridge.resolveNode("Term").value;
    if((termLifeLOBs.includes(product) || termLifeLOBs.includes(product.split('-')[0])) && series == 91000){
        term = "";
    }
    $.ajax({
        url: "/bin/GetCaseBuilderToolProductsData",
        type: 'GET',
        data: { mode: "issueAge", situs: situsState, series: series , product: product, term: term, lob: product},
        dataType: 'json', // added data type
        success: function (res) {
			var issueAges = res;
            if (accidentLOBs.includes(product) || hospitalIndemnityLOBs.includes(product) || benExtendLOBs.includes(product) || criticalIllnessLOBs.includes(product) || wholeLifeLOBs.includes(product) || termLifeLOBs.includes(product) || termLifeLOBs.includes(product.split('-')[0]) || termto120LOBs.includes(product)) {

                guideBridge.resolveNode("EmployeeIssueAge").value = issueAges["emp-issue-age"];

                guideBridge.resolveNode("SpouseIssueAge").value = issueAges["spouse-issue-age"];

                guideBridge.resolveNode("ChildIssueAge").value = issueAges["child-issue-age"];

                guideBridge.resolveNode("employeeTerminationAge").value = issueAges["termination-age"]["emp-termination-age"];

                guideBridge.resolveNode("spouseTerminationAge").value = issueAges["termination-age"]["spouse-termination-age"];

                guideBridge.resolveNode("childTerminationAge").value = issueAges["termination-age"]["child-termination-age"];

            }
            if(disabilityLOBs.includes(product) || disabilityLOBs.includes(product.split('-')[0])){
				guideBridge.resolveNode("EmployeeIssueAge").value = issueAges["emp-issue-age"];
                
                guideBridge.resolveNode("employeeTerminationAge").value = issueAges["termination-age"]["emp-termination-age"];
            }  
            if(criticalIllnessLOBs.includes(product)){       
                guideBridge.resolveNode("ChildIssueAge").value = "";         

                guideBridge.resolveNode("childTerminationAge").value = "";
            }   
        }
    });
}



function ValidEmpAge(EmployeeIssueAge) {
    var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;
    var EmpAge = guideBridge.resolveNode("EmployeeIssueAge");
    if (wholeLifeLOBs.includes(productName)) {
        if (EmployeeIssueAge > 70 || EmployeeIssueAge < 18) {
            guideBridge.resolveNode("EmployeeIssueAge").value = "";
            document.getElementById(EmpAge.id).className = "guideFieldNode guideNumericBox EmployeeIssueAge defaultFieldLayout af-field-filled validation-failure";
            setTimeout(function() {
                var alert = document.getElementById(EmpAge.id).children[3];
                alert.setAttribute('role', 'alert');
                var alertid = "#" + alert.id;
                $(alertid).html("Employee issue Age should be between 18-70.");
            }, 10);
        } else {
            document.getElementById(EmpAge.id).className = "guideFieldNode guideNumericBox EmployeeIssueAge defaultFieldLayout af-field-filled validation-success ";
        }
    }
}

function ValidSpouseAge(SpouseIssueAge) {
    var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;
    var SpAge = guideBridge.resolveNode("SpouseIssueAge");
    if (wholeLifeLOBs.includes(productName)) {
        if (SpouseIssueAge > 70 || SpouseIssueAge < 18) {
            guideBridge.resolveNode("SpouseIssueAge").value = "";
            document.getElementById(SpAge.id).className = "guideFieldNode guideNumericBox SpouseIssueAge defaultFieldLayout af-field-filled validation-failure";
            setTimeout(function() {
                var alert = document.getElementById(SpAge.id).children[3];
                alert.setAttribute('role', 'alert');
                var alertid = "#" + alert.id;
                $(alertid).html("Spouse issue Age should be between 18-70.");
            }, 10);
        } else {
            document.getElementById(SpAge.id).className = "guideFieldNode guideNumericBox SpouseIssueAge defaultFieldLayout af-field-filled validation-success ";
        }
    }
}

function addMonthSuffix(BenefitPeriod) {
    var benPer = guideBridge.resolveNode("BenefitPeriod");
    if (BenefitPeriod.endsWith('-months') || BenefitPeriod.endsWith('-months')) {
        var Newben = BenefitPeriod.split('-months');
        BenefitPeriod = Newben[0];
    }
    if (BenefitPeriod < 0 || isNaN(BenefitPeriod) == true || BenefitPeriod.includes('+')) {
        guideBridge.resolveNode("BenefitPeriod").value = "";
        document.getElementById(benPer.id).className = "guideFieldNode guideTextBox BenefitPeriod defaultFieldLayout af-field-filled validation-failure";
        setTimeout(function() {
            var alert = document.getElementById(benPer.id).children[2];
            alert.setAttribute('role', 'alert');
            var alertid = "#" + alert.id;
            $(alertid).html("Not a valid value.");
        }, 10);
    } else {
        document.getElementById(benPer.id).className = "guideFieldNode guideTextBox BenefitPeriod defaultFieldLayout af-field-filled validation-success ";
        guideBridge.resolveNode("BenefitPeriod").value = "";
        var value = BenefitPeriod + "-months";
        guideBridge.resolveNode("BenefitPeriod").value = value;
    }
}

function ValidParticipation(Participation) {
    var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;
    var par = guideBridge.resolveNode("Participation");
    if (Participation.endsWith('%')) {
        var partval = Participation.split('%');
        Participation = partval[0];
    }
    if (isNaN(Participation) || Participation < 0 || Participation.includes('+')) {
        guideBridge.resolveNode("Participation").value = "";
        document.getElementById(par.id).className = "guideFieldNode guideTextBox Participation defaultFieldLayout af-field-filled validation-failure";
        setTimeout(function() {
            var alert = document.getElementById(par.id).children[3];
            alert.setAttribute('role', 'alert');
            var alertid = "#" + alert.id;
            $(alertid).html("Not a valid value.");
        }, 10);
    } else {
        if (criticalIllnessLOBs.includes(productName) && Participation > 100) {
            guideBridge.resolveNode("Participation").value = "";
            document.getElementById(par.id).className = "guideFieldNode guideTextBox Participation defaultFieldLayout af-field-filled validation-failure";
            setTimeout(function() {
                var alert = document.getElementById(par.id).children[3];
                alert.setAttribute('role', 'alert');
                var alertid = "#" + alert.id;
                $(alertid).html("Participation range should be 0-100%.");
            }, 10);
        // } else if (disabilityLOBs.includes(productName) || disabilityLOBs.includes(productName.split('-')[0])) {
        //     var Emp = guideBridge.resolveNode("EligibleEmployees").value;
        //     guideBridge.resolveNode("Participation").value = Math.ceil(Emp * Participation) + "%";
        //     document.getElementById(par.id).className = "guideFieldNode guideTextBox Participation defaultFieldLayout af-field-filled validation-success ";
        } else {
            guideBridge.resolveNode("Participation").value = Participation + "%";
            document.getElementById(par.id).className = "guideFieldNode guideTextBox Participation defaultFieldLayout af-field-filled validation-success ";
        }
    }
}

function setParticipation(Participation, EligibleEmployees){
    if (Participation.endsWith('%')) {
        var partval = Participation.split('%');
        Participation = partval[0];
    }
    if (EligibleEmployees != null){
        Participation = Participation/EligibleEmployees;
    }
    else {
        Participation = null;
    }
    return Participation;
}

function setPlanName(productName) {
    var planName;
    var productNameValue = productName.split('-');
    var product = productNameValue[0];
    var level = productNameValue[1];
    levelValue="-"+level
    if(productNameValue.length==1){
        levelValue='';
    }
    if(productNameValue.length == 3){
        levelValue = "-"+productNameValue[1] + "-" + productNameValue[2];
    }
    if (accidentLOBs.includes(product)) {
        return planName = "Group Accident Insurance"+ levelValue;
    }
    if (hospitalIndemnityLOBs.includes(product)) {
        return planName = "Group Hospital Indemnity Insurance"+ levelValue;
    }
    if (benExtendLOBs.includes(product)) {
        return planName = "Group BenExtend Insurance"+ levelValue;
    }
    if (criticalIllnessLOBs.includes(product)) {
        return planName = "Group Critical Illness Insurance"+ levelValue;
    }
    if (disabilityLOBs.includes(product)) {
        return planName = "Group Disability Insurance"+ levelValue;
    }
    if (wholeLifeLOBs.includes(product)) {
        return planName = "Group Whole Life Insurance"+ levelValue;
    }
    if (termLifeLOBs.includes(product)) {
        return planName = "Group Term Life Insurance"+ levelValue;
    }
    if (termto120LOBs.includes(product)) {
        return planName = "Group Term to 120 Insurance"+ levelValue;
    }
}

function validChidIssueAge(unit, age) {
    var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;
    var field = guideBridge.resolveNode("ChildIssueAge");
    if (age.endsWith('-Years') || age.endsWith('-Months') || age.endsWith('-Days')) {
        var newAge = age.split('-');
        age = newAge[0];
    }
    if (!isNaN(age) && age > 0 && !age.includes('+')) {
        if (unit == "Years") {
            if (age > 26) {
                guideBridge.resolveNode("ChildIssueAge").value = "";
                document.getElementById(field.id).className = "guideFieldNode guideTextBox ChildIssueAge defaultFieldLayout af-field-filled validation-failure";
                setTimeout(function() {
                    var alert = document.getElementById(field.id).children[3];
                    alert.setAttribute('role', 'alert');
                    var alertid = "#" + alert.id;
                    $(alertid).html("Max value for age in years can be 26");
                }, 10);
            } else {
                guideBridge.resolveNode("ChildIssueAge").value = "";
                var value = age + "-Years";
                guideBridge.resolveNode("ChildIssueAge").value = value;
                document.getElementById(field.id).className = "guideFieldNode guideTextBox ChildIssueAge defaultFieldLayout af-field-filled validation-success ";
            }
        }
        if (unit == "Months") {
            if (age > 312) {
                guideBridge.resolveNode("ChildIssueAge").value = "";
                document.getElementById(field.id).className = "guideFieldNode guideTextBox ChildIssueAge defaultFieldLayout af-field-filled validation-failure";
                setTimeout(function() {
                    var alert = document.getElementById(field.id).children[3];
                    alert.setAttribute('role', 'alert');
                    var alertid = "#" + alert.id;
                    $(alertid).html("Max value for age in months can be 312");
                }, 10);
            } else {
                guideBridge.resolveNode("ChildIssueAge").value = "";
                var value = age + "-Months";
                guideBridge.resolveNode("ChildIssueAge").value = value;
                document.getElementById(field.id).className = "guideFieldNode guideTextBox ChildIssueAge defaultFieldLayout af-field-filled validation-success ";
            }
        }
        if (unit == "Days") {
            if (age > 9500) {
                guideBridge.resolveNode("ChildIssueAge").value = "";
                document.getElementById(field.id).className = "guideFieldNode guideTextBox ChildIssueAge defaultFieldLayout af-field-filled validation-failure";
                setTimeout(function() {
                    var alert = document.getElementById(field.id).children[3];
                    alert.setAttribute('role', 'alert');
                    var alertid = "#" + alert.id;
                    $(alertid).html("Max value for age in days can be 9500");
                }, 10);
            } else if (wholeLifeLOBs.includes(productName) && age < 15) {
                guideBridge.resolveNode("ChildIssueAge").value = "";
                document.getElementById(field.id).className = "guideFieldNode guideTextBox ChildIssueAge defaultFieldLayout af-field-filled validation-failure";
                setTimeout(function() {
                    var alert = document.getElementById(field.id).children[3];
                    alert.setAttribute('role', 'alert');
                    var alertid = "#" + alert.id;
                    $(alertid).html("Min value for age in days for whole life can be 15");
                }, 10);
            } else {
                guideBridge.resolveNode("ChildIssueAge").value = "";
                var value = age + "-Days";
                guideBridge.resolveNode("ChildIssueAge").value = value;
                document.getElementById(field.id).className = "guideFieldNode guideTextBox ChildIssueAge defaultFieldLayout af-field-filled validation-success ";
            }
        }
    } else {
        guideBridge.resolveNode("ChildIssueAge").value = "";
        document.getElementById(field.id).className = "guideFieldNode guideTextBox ChildIssueAge defaultFieldLayout af-field-filled validation-failure";
        setTimeout(function() {
            var alert = document.getElementById(field.id).children[3];
            alert.setAttribute('role', 'alert');
            var alertid = "#" + alert.id;
            $(alertid).html("Not a valid value.");
        }, 10);
    }
}

function fillLocationsTable(locationsData) {
    var repeatrow = guideBridge.resolveNode("locationsDataRow");
    var currentCount = repeatrow.instanceManager.instanceCount;
    for (var m = 0; m < currentCount; m++) {
        if (m != 0) {
            repeatrow.instanceManager.removeInstance(0);
        }
    }
    for (var i = 0; i < locationsData.length; i++) {
        if (i != 0) {
            repeatrow.instanceManager.addInstance();
        }
    }
    for (var i = 0; i < repeatrow.instanceManager.instanceCount; i++) {
        repeatrow.instanceManager.instances[i].locationNameValue.value = locationsData[i].locationName;
        repeatrow.instanceManager.instances[i].LocationStateValue.value = locationsData[i].locationState;
        repeatrow.instanceManager.instances[i].LocationCodeValue.value = locationsData[i].locationCode;
    }
}

function clearLocationsTable() {
    var repeatrow = guideBridge.resolveNode("locationsDataRow");
    repeatrow.instanceManager.instances[0].locationNameValue.value = "";
    repeatrow.instanceManager.instances[0].LocationStateValue.value = "";
    repeatrow.instanceManager.instances[0].LocationCodeValue.value = "";
    var delCount = repeatrow.instanceManager.instanceCount;
    for (var i = 0; i <= delCount; i++) {           
        if (i != 0) {
            repeatrow.instanceManager.removeInstance(0);
        }
    }
}

function checkEnrollmentOptionValues(enrollmentCondtion,newHire){
    if(enrollmentCondtion==newHire){
        guideBridge.resolveNode("commonEnrollmentValue").value=enrollmentCondtion;
    }

}

function setChildIssueAge(ChildeAge){
    var ChildeAgeValue = ChildeAge.split('-');
    guideBridge.resolveNode("childIssueAgeUnit").value = ChildeAgeValue[1];
    guideBridge.resolveNode("ChildIssueAge").value = ChildeAge;
}

function onChangingChildAgeUnit(){
    $(document).on('change', '.childIssueAgeUnit select', function() {
        guideBridge.resolveNode("ChildIssueAge").value = "";
    });
}

function populateSitusData() {
    var selectedSitus = "";
    $(".AI_SitusState input").autocomplete({
        minLength: 0,
        source: situsStates,
        delay: 0,
        select: function(event, ui) {
            selectedSitus = ui.item.label;
            guideBridge.resolveNode("AI_SitusState").value = selectedSitus;
        },
    }).focus(function() {
        $(this).autocomplete('search', $(this).val())
    });
}

function populatePlatformData() {
    var selectedPlatform = "";
    $(".GroupEnrollmentPlatform input").autocomplete({
        minLength: 0,
        source: caseBuildPlatforms,
        delay: 0,
        select: function(event, ui) {
            selectedPlatform = ui.item.label;
            guideBridge.resolveNode("GroupEnrollmentPlatform").value = selectedPlatform;
        },
        open: function() {
            $('.ui-autocomplete').css('max-height', '40%');
            $('.ui-autocomplete').css('width', '21%');
            $('.ui-autocomplete').css('overflow-y', 'auto');
            $('.ui-autocomplete').css('overflow-x', 'hidden');
        },
        select: function(event, ui) {
            guideBridge.resolveNode("Fetch").enabled = true;
        }
    }).focus(function() {
        $(this).autocomplete('search', $(this).val())
    });
}

function setAvailaibleProducts(accidentValues,hospitalValues,benExtendValues,crticalValues,disabilitySingle,disabilityMulti,wholeLifeValues,termLifeValues,termLifeMulti,termto120){
    var values="";
    if(accidentValues!=null){
        values+=accidentValues+",";
    }
    if(hospitalValues!=null){
        values+=hospitalValues+",";
    }
    if(benExtendValues!=null){
        values+=benExtendValues+",";
    }
    if(crticalValues!=null){
        values+=crticalValues+",";
    }
    if(disabilitySingle!=null){
        values+=disabilitySingle+",";
    }
    if(disabilityMulti!=null){
        values+=disabilityMulti+",";
    }
    if(wholeLifeValues!=null){
        values+=wholeLifeValues+",";
    }
    if(termLifeValues!=null && termLifeValues == 'S'){
        values+= "Term Life"+",";
    }
    if(termLifeMulti!=null && termLifeMulti == 'M'){
        values+= "Term Life-MultiPlan"+",";
    }
    if(termto120!=null && termto120 == 'T'){
        values+=termto120LOBs[0]+",";
    }

    return values;
}

function ValidBenifitAmount(benefitAmount){    
    var par = guideBridge.resolveNode("benefitAmountPercentage");
   
    if (benefitAmount.endsWith('%')) {
        var partval = benefitAmount.split('%');
        benefitAmount = partval[0];
    }
    
    if(isNaN(benefitAmount) || benefitAmount<0 || benefitAmount.includes('+')){
        guideBridge.resolveNode("benefitAmountPercentage").value = "";
        document.getElementById(par.id).className = "guideFieldNode guideTextBox benefitAmountPercentage defaultFieldLayout af-field-filled validation-failure";
        setTimeout(function() {
        var alert = document.getElementById(par.id).children[2];
        alert.setAttribute('role','alert');
        var alertid = "#" + alert.id;
        $(alertid).html("Not a valid value.");
        },10);
    }
    else{
        guideBridge.resolveNode("benefitAmountPercentage").value = benefitAmount + "%";
        document.getElementById(par.id).className = "guideFieldNode guideTextBox benefitAmountPercentage defaultFieldLayout af-field-filled validation-success ";
    }
}

function validCasebuildSitus(situs) {
    var field = guideBridge.resolveNode("AI_SitusState");
    if (!situsStates.includes(situs)) {
        guideBridge.resolveNode("AI_SitusState").value = '';
        document.getElementById(field.id).className = "guideFieldNode guideTextBox AI_SitusState defaultFieldLayout af-field-filled validation-failure";
        setTimeout(function() {
            var alert = document.getElementById(field.id).children[2];
            alert.setAttribute('role', 'alert');
            var alertid = "#" + alert.id;
            $(alertid).html("This is not a valid Situs.");
        }, 10);
    } else {
        document.getElementById(field.id).className = "guideFieldNode guideTextBox AI_SitusState defaultFieldLayout af-field-filled validation-success ";
    }
}

function validCasebuildGroupNo(groupNumber) {
    var field = guideBridge.resolveNode("GroupNumber");
    if (groupNumber.length > 0 && groupNumber.length < 10 && !isNaN(groupNumber)) {
        groupNumber = groupNumber.padStart(10, '0')
        guideBridge.resolveNode("GroupNumber").value = groupNumber;
    }
    
    if (!isNaN(groupNumber) && groupNumber.length == 10 && !groupNumber.includes('+') && !groupNumber.includes('-') && !groupNumber.includes('.') && groupNumber != '0000000000') {
        document.getElementById(field.id).className = "guideFieldNode guideTextBox GroupNumber defaultFieldLayout af-field-filled validation-success ";
    } else if (groupNumber.length == 13 && groupNumber.startsWith("AGC") && !isNaN(groupNumber.slice(3, 13)) && !groupNumber.includes('+') && !groupNumber.includes('-') && !groupNumber.includes('.')) {
        document.getElementById(field.id).className = "guideFieldNode guideTextBox GroupNumber defaultFieldLayout af-field-filled validation-success ";
    } else {
        guideBridge.resolveNode("GroupNumber").value = "";
        document.getElementById(field.id).className = "guideFieldNode guideTextBox GroupNumber defaultFieldLayout af-field-filled validation-failure";
        setTimeout(function() {
        var alert = document.getElementById(field.id).children[2];
        alert.setAttribute('role', 'alert');
        var alertid = "#" + alert.id;
        $(alertid).html("This is not a valid Aflac Group number. Please re-enter.");
        },10);
    }
}

function getLocationsValues(){
    if(locationsData!=null){
        fillLocationsTable(locationsData);
    }
}

function setdedfreqvalue(deductionFrequencyValue) {
    var valuesArray = deductionFrequencyValue.split(',');
    var valuesArrayLowerCase = [];
    for (var j = 0; j < valuesArray.length; j++) {
        valuesArrayLowerCase[j] = valuesArray[j].toLowerCase().trim();
    }
    var splicearray = [];
    for (var k = 0; k < valuesArrayLowerCase.length - 1; k++) {
        for (var l = k + 1; l < valuesArrayLowerCase.length; l++) {
            if (valuesArrayLowerCase[k] == valuesArrayLowerCase[l]) {
                if (!splicearray.includes(l)) {
                    splicearray.push(l);
                }
            }
        }
    }
    for (var i = splicearray.length - 1; i >= 0; i--) {
        valuesArray.splice(splicearray[i], 1);
    }
    for (var p = 0; p < dedFreqDefaultOptions.length; p++) {
        for (var q = 0; q < valuesArray.length; q++) {
            if (dedFreqDefaultOptions[p].toLowerCase().trim() == valuesArray[q].toLowerCase().trim()) {
                valuesArray[q] = dedFreqDefaultOptions[p];
            }
        }
    }
    for (var j = 0; j < valuesArray.length; j++) {
        valuesArray[j] = valuesArray[j].trim();
    }
    deductionFrequencyValue = valuesArray.join(", ");
    if (deductionFrequencyValue.trim().endsWith(',')) {
        deductionFrequencyValue = deductionFrequencyValue.trim();
        deductionFrequencyValue = deductionFrequencyValue.slice(0, deductionFrequencyValue.length - 1);
    }
    guideBridge.resolveNode("AI_DeductionFrequency").value = deductionFrequencyValue;
}

function deductionFrequencyAutoComplete() {
    $(function() {
        function split(val) {
            return val.split(/,\s*/);
        }

        function extractLast(term) {
            return split(term).pop();
        }
        $(".AI_DeductionFrequency input").bind("keydown", function(event) {
            if (event.keyCode === $.ui.keyCode.TAB && $(this).autocomplete("instance").menu.active) {
                event.preventDefault();
            }
        }).autocomplete({
            minLength: 0,
            source: function(request, response) {
                response($.ui.autocomplete.filter(dedFreqDefaultOptions, extractLast(request.term)));
            },
            select: function(event, ui) {
                var currentValue = split(this.value);
                currentValue.pop();
                if (currentValue.includes(ui.item.value) != true) {
                    currentValue.push(ui.item.value);
                }
                currentValue.push("");
                this.value = currentValue.join(", ");
                (function() {
                    $(this).autocomplete('search', $(this).val())
                })();
                return false;
            }
        }).focus(function() {
            $(this).autocomplete('search', $(this).val())
        });
    });
}

// function checkDisabilityPlanName(planName){
//     var lobName = planName.split("-");
//     if(disabilityLOBs.includes(lobName[0])){
//         return lobName[0];
//     }
//     else{
//         return null;
//     }
// }

function checkAddbuton(){
    if(guideBridge.resolveNode("selectedProducts").value==null){
        guideBridge.resolveNode("nextAdd").visible=false;
    }else{
        guideBridge.resolveNode("nextAdd").visible=true;
    }
}

function setProductsSold(selectedProducts) {
    if (selectedProducts.trim().endsWith(',')) {
        selectedProducts = selectedProducts.trim();
        selectedProducts = selectedProducts.slice(0, selectedProducts.length - 1);
    }
    return selectedProducts;
}

var enrollmentDetail = {};
function validPlatform(platform) {
    var field = guideBridge.resolveNode("GroupEnrollmentPlatform");
    if (!caseBuildPlatforms.includes(platform)) {
        guideBridge.resolveNode("GroupEnrollmentPlatform").value = '';
        document.getElementById(field.id).className = "guideFieldNode guideTextBox GroupEnrollmentPlatform defaultFieldLayout af-field-filled validation-failure";
        setTimeout(function() {
            var alert = document.getElementById(field.id).children[2];
            alert.setAttribute('role', 'alert');
            var alertid = "#" + alert.id;
            $(alertid).html("This is not a valid Platform.");
        }, 10);
    } else {
        document.getElementById(field.id).className = "guideFieldNode guideTextBox GroupEnrollmentPlatform defaultFieldLayout af-field-filled validation-success ";
        for(var i=0; i< platformDetails.length; i++){
            var tmp = platformDetails[i];
            if(tmp.platform === platform)
                enrollmentDetail = tmp;
        }
        if(enrollmentDetail['contact-integration'] === true){
            //alert("Integration need to be contacted regarding this case build.");
            guideBridge.resolveNode("contactIntegrationError").visible = true;

            $(document).on('change', function() {
                guideBridge.resolveNode("contactIntegrationError").visible = false;
            });

            $(document).on('click', 'button', function () {
                guideBridge.resolveNode("contactIntegrationError").visible = false;
            });

            guideBridge.resolveNode("contactIntegrationNo").visible=false;
            guideBridge.resolveNode("contactIntegrationYes").visible=true;
        }
        else {
            guideBridge.resolveNode("contactIntegrationNo").visible=true;
            guideBridge.resolveNode("contactIntegrationYes").visible=false;
        }
    }
}

function radioToChk() {
    document.querySelectorAll(".DisablityCheckbox")[1].children[0].children[0].type = 'checkbox';
    document.querySelectorAll(".DisablityCheckbox")[1].children[0].children[0].style.top = '2px';
    document.querySelectorAll(".DisablityCheckbox")[2].children[0].children[0].type = 'checkbox';
    document.querySelectorAll(".DisablityCheckbox")[2].children[0].children[0].style.top = '2px';
}

function removeApplicationNumberValidation(){
    var field = guideBridge.resolveNode("ApplicationNumber");
    var fieldClassName = document.getElementById(field.id).className;
    if(fieldClassName.includes("validation-failure")){
        document.getElementById(field.id).className = fieldClassName.replace("validation-failure", "");
    }
}

function setPlatformNameOnAccountInfo(platformValue){
    var newPlatformValue='';
    if(platformValue!=null){
        newPlatformValue = platformValue.split('|')[1];
    }
    return newPlatformValue;
}

function setProductionDueDate(effectiveDate) {
    var date = new Date(effectiveDate);
    var day = date.getDate() - 15;
    date.setDate(day);
    var newDate = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
    return newDate;
}

function setFilesubmissionLabel() {
    document.querySelectorAll("#guideContainer-rootPanel-panel_628055537_copy-panel_1625559233-panel1679917013897___guide-item-nav > a")[0].innerHTML = "File Submission<br>Guidelines";
    document.querySelectorAll("#guideContainer-rootPanel-panel_628055537_copy-panel_1625559233-panel1679917013897___guide-item-nav > a")[0].style.paddingTop = "6px";
    document.querySelectorAll("#guideContainer-rootPanel-panel_628055537_copy-panel_1625559233-panel1679917013897___guide-item-nav > a")[0].style.paddingBottom = "6px";
}

function setTestDueDate(effectiveDate) {
    var date = new Date(effectiveDate);
    var day = date.getDate() + 7;
    date.setDate(day);
    var newDate = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
    return newDate;
}

function EEbenefitAmountDisplay() {
    
    var minAmount = guideBridge.resolveNode("Product")._children[3].AmtOfferedTable._children[3]._children[1].value;
    var maxAmount = guideBridge.resolveNode("Product")._children[3].AmtOfferedTable._children[3]._children[2].value;
    var amountIncrement = guideBridge.resolveNode("Product")._children[3].AmtOfferedTable._children[3]._children[3].value;

    var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;

    if (minAmount != null && maxAmount != null && amountIncrement != null && criticalIllnessLOBs.includes(productName)) {
        guideBridge.resolveNode("Product")._children[3].EEbenefitTable.visible = true;

        var tableRows
        if(amountIncrement == 0){
            tableRows = 1;
        }
        else {
            tableRows = maxAmount / amountIncrement;
        }

        var repeatrow = guideBridge.resolveNode("Product")._children[3].EEbenefitTable._children[3];

        var currentCount = repeatrow.instanceManager.instanceCount;
        for (var m = 0; m < currentCount; m++) {
            if (m != 0) {
                repeatrow.instanceManager.removeInstance(0);
            }
        }

        var benefitValue = minAmount;
        for (var i = 0; i < tableRows; i++) {
            if (i != 0) {
                repeatrow.instanceManager.addInstance();
            }
            //EEbenefit
            repeatrow._instanceManager._instances[i]._children[0].value = benefitValue;     
            benefitValue += amountIncrement;
            if (benefitValue > maxAmount) {
                break;
            }
        }

        //To add a row in the end for maxAmount
        if(benefitValue != (maxAmount + amountIncrement)){
            repeatrow.instanceManager.addInstance();
            currentCount = repeatrow.instanceManager.instanceCount;
            repeatrow._instanceManager._instances[currentCount-1]._children[0].value = maxAmount;
        }
    }
    else if (minAmount == null || maxAmount == null || amountIncrement == null) {
        //alert("Please Enter all values of Employee Benefit Row");
        guideBridge.resolveNode("Product")._children[3].tableEmptyEBRowError.visible = true;

        $(document).on('change', function() {
            guideBridge.resolveNode("Product")._children[3].tableEmptyEBRowError.visible = false;
        });
        
        var repeatrow = guideBridge.resolveNode("Product")._children[3].EEbenefitTable._children[3];
        for (var i = 0; i < repeatrow._instanceManager.instanceCount; i++) {
            repeatrow._instanceManager._instances[i]._children[0].value = "";

        }
        guideBridge.resolveNode("Product")._children[3].EEbenefitTable.visible = false;
    }
    else {
        guideBridge.resolveNode("Product")._children[3].EEbenefitTable.visible = false;
    }
}

function SPbenefitAmountDisplay() {

    var minAmount = guideBridge.resolveNode("Product")._children[3].AmtOfferedTable._children[5]._children[1].value;
    var maxAmount = guideBridge.resolveNode("Product")._children[3].AmtOfferedTable._children[5]._children[2].value;
    var amountIncrement = guideBridge.resolveNode("Product")._children[3].AmtOfferedTable._children[5]._children[3].value;

    var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;

    if (minAmount != null && maxAmount != null && amountIncrement != null && criticalIllnessLOBs.includes(productName)) {
        guideBridge.resolveNode("Product")._children[3].SPbenefitTable.visible = true;

        var tableRows
        if(amountIncrement == 0){
            tableRows = 1;
        }
        else {
            tableRows = maxAmount / amountIncrement;
        }

        var repeatrow = guideBridge.resolveNode("Product")._children[3].SPbenefitTable._children[3];
        

        var currentCount = repeatrow.instanceManager.instanceCount;
        for (var m = 0; m < currentCount; m++) {
            if (m != 0) {
                repeatrow.instanceManager.removeInstance(0);
            }
        }

        var benefitValue = minAmount;
        for (var i = 0; i < tableRows; i++) {
            if (i != 0) {
                repeatrow.instanceManager.addInstance();
            }
            //SPbenefit
            repeatrow._instanceManager._instances[i]._children[0].value = benefitValue;     
            benefitValue += amountIncrement;
            if (benefitValue > maxAmount) {
                break;
            }
        }

        //To add a row in the end for maxAmount
        if(benefitValue != (maxAmount + amountIncrement)){
            repeatrow.instanceManager.addInstance();
            currentCount = repeatrow.instanceManager.instanceCount;
            repeatrow._instanceManager._instances[currentCount-1]._children[0].value = maxAmount;
        }
    }
    else if ((minAmount == null || maxAmount == null || amountIncrement == null) && (criticalIllnessLOBs.includes(productName) || wholeLifeLOBs.includes(productName) || termLifeLOBs.includes(productName) || termLifeLOBs.includes(productName.split('-')[0]))) {
        //alert("Please Enter all values of Spouse Benefit Row");
        guideBridge.resolveNode("Product")._children[3].tableEmptySBRowError.visible = true;

        $(document).on('change', function() {
            guideBridge.resolveNode("Product")._children[3].tableEmptySBRowError.visible = false;
        });

        guideBridge.resolveNode("Product")._children[3].SPbenefitTable.visible = false;

        var repeatrow = guideBridge.resolveNode("Product")._children[3].SPbenefitTable._children[3];
        for (var i = 0; i < repeatrow._instanceManager.instanceCount; i++) {
            repeatrow._instanceManager._instances[i]._children[0].value = "";
        }    
    }
    else {
        guideBridge.resolveNode("Product")._children[3].SPbenefitTable.visible = false;
    }

}

// function TDIStatesBenefitAmountDisplay() {

//     var minAmount = guideBridge.resolveNode("Product")._children[3]._children[19]._children[9]._children[1].value;
//     var maxAmount = guideBridge.resolveNode("Product")._children[3]._children[19]._children[9]._children[2].value;
//     var amountIncrement = guideBridge.resolveNode("Product")._children[3]._children[19]._children[9]._children[3].value;

//     var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;

//     if (minAmount != null && maxAmount != null && amountIncrement != null && (disabilityLOBs.includes(productName) || disabilityLOBs.includes(productName.split('-')[0]))) {
//         guideBridge.resolveNode("Product")._children[3]._children[26].visible = true;
        
//         var tableRows
//         if(amountIncrement == 0){
//             tableRows = 1;
//         }
//         else {
//             tableRows = maxAmount / amountIncrement;
//         }

//         var repeatrow = guideBridge.resolveNode("Product")._children[3]._children[26]._children[3];
        

//         var currentCount = repeatrow.instanceManager.instanceCount;
//         for (var m = 0; m < currentCount; m++) {
//             if (m != 0) {
//                 repeatrow.instanceManager.removeInstance(0);
//             }
//         }
//         var benefitValue = minAmount;
//         for (var i = 0; i < tableRows; i++) {
//             if (i != 0) {
//                 repeatrow.instanceManager.addInstance();
//             }
//             //TDIStatesBenefit
//             repeatrow._instanceManager._instances[i]._children[0].value = benefitValue;     
//             benefitValue += amountIncrement;
//             if (benefitValue > maxAmount) {
//                 break;
//             }
//         }

//         //To add a row in the end for maxAmount
//         if(benefitValue != (maxAmount + amountIncrement)){
//             repeatrow.instanceManager.addInstance();
//             currentCount = repeatrow.instanceManager.instanceCount;
//             repeatrow._instanceManager._instances[currentCount-1]._children[0].value = maxAmount;
//         }
//     }
//     else if ((minAmount == null || maxAmount == null || amountIncrement == null) && (disabilityLOBs.includes(productName) || disabilityLOBs.includes(productName.split('-')[0]))) {
//         alert("Please Enter all values of TDI States Benefit Row");
//         guideBridge.resolveNode("Product")._children[3]._children[26].visible = false;
        
//         var repeatrow = guideBridge.resolveNode("Product")._children[3]._children[26]._children[3];
//         for (var i = 0; i < repeatrow._instanceManager.instanceCount; i++) {
//             repeatrow._instanceManager._instances[i]._children[0].value = "";
//         }
//     }
//     else {
//         guideBridge.resolveNode("Product")._children[3]._children[26].visible = false;
//     }
// }

// function CHIndividualPolicyBenefitAmountDisplay() {

//     var minAmount = guideBridge.resolveNode("Product")._children[3]._children[19]._children[11]._children[1].value;
//     var maxAmount = guideBridge.resolveNode("Product")._children[3]._children[19]._children[11]._children[2].value;
//     var amountIncrement = guideBridge.resolveNode("Product")._children[3]._children[19]._children[11]._children[3].value;

//     var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;

//     if (minAmount != null && maxAmount != null && amountIncrement != null && wholeLifeLOBs.includes(productName)) {
//         guideBridge.resolveNode("Product")._children[3]._children[28].visible = true;

//         var tableRows
//         if(amountIncrement == 0){
//             tableRows = 1;
//         }
//         else {
//             tableRows = maxAmount / amountIncrement;
//         }

//         var repeatrow = guideBridge.resolveNode("Product")._children[3]._children[28]._children[3];
        

//         var currentCount = repeatrow.instanceManager.instanceCount;
//         for (var m = 0; m < currentCount; m++) {
//             if (m != 0) {
//                 repeatrow.instanceManager.removeInstance(0);
//             }
//         }
//         var benefitValue = minAmount;
//         for (var i = 0; i < tableRows; i++) {
//             if (i != 0) {
//                 repeatrow.instanceManager.addInstance();
//             }
//             //CHIndividualPolicybenefit
//             repeatrow._instanceManager._instances[i]._children[0].value = benefitValue;     
//             benefitValue += amountIncrement;
//             if (benefitValue > maxAmount) {
//                 break;
//             }
//         }

//         //To add a row in the end for maxAmount
//         if(benefitValue != (maxAmount + amountIncrement)){
//             repeatrow.instanceManager.addInstance();
//             currentCount = repeatrow.instanceManager.instanceCount;
//             repeatrow._instanceManager._instances[currentCount-1]._children[0].value = maxAmount;
//         }
//     }
//     else if ((minAmount == null || maxAmount == null || amountIncrement == null) && wholeLifeLOBs.includes(productName)) {
//         alert("Please Enter all values of Child Individual Policy Benefit Row");
//         guideBridge.resolveNode("Product")._children[3]._children[28].visible = false;
        
//         var repeatrow = guideBridge.resolveNode("Product")._children[3]._children[28]._children[3];
//         for (var i = 0; i < repeatrow._instanceManager.instanceCount; i++) {
//             repeatrow._instanceManager._instances[i]._children[0].value = "";
//         }

//     }
//     else {
//         guideBridge.resolveNode("Product")._children[3]._children[28].visible = false;
//     }
// }

// function CHBenefitAmountDisplay() {

//     var minAmount = guideBridge.resolveNode("Product")._children[3]._children[19]._children[7]._children[1].value;
//     var maxAmount = guideBridge.resolveNode("Product")._children[3]._children[19]._children[7]._children[2].value;
//     var amountIncrement = guideBridge.resolveNode("Product")._children[3]._children[19]._children[7]._children[3].value;

//     var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;

//     if (minAmount != null && maxAmount != null && amountIncrement != null && termLifeLOBs.includes(productName)) {
//         guideBridge.resolveNode("Product")._children[3]._children[30].visible = true;

//         var tableRows
//         if(amountIncrement == 0){
//             tableRows = 1;
//         }
//         else {
//             tableRows = maxAmount / amountIncrement;
//         }

//         var repeatrow = guideBridge.resolveNode("Product")._children[3]._children[30]._children[3];
        

//         var currentCount = repeatrow.instanceManager.instanceCount;
//         for (var m = 0; m < currentCount; m++) {
//             if (m != 0) {
//                 repeatrow.instanceManager.removeInstance(0);
//             }
//         }
//         var benefitValue = minAmount;
//         for (var i = 0; i < tableRows; i++) {
//             if (i != 0) {
//                 repeatrow.instanceManager.addInstance();
//             }
//             //CHBenefit
//             repeatrow._instanceManager._instances[i]._children[0].value = benefitValue;     
//             benefitValue += amountIncrement;
//             if (benefitValue > maxAmount) {
//                 break;
//             }
//         }

//         //To add a row in the end for maxAmount
//         if(benefitValue != (maxAmount + amountIncrement)){
//             repeatrow.instanceManager.addInstance();
//             currentCount = repeatrow.instanceManager.instanceCount;
//             repeatrow._instanceManager._instances[currentCount-1]._children[0].value = maxAmount;
//         }
//     }
//     else if ((minAmount == null || maxAmount == null || amountIncrement == null) && termLifeLOBs.includes(productName)) {
//         alert("Please Enter all values of Child Benefit Row");
//         guideBridge.resolveNode("Product")._children[3]._children[30].visible = false;
        
//         var repeatrow = guideBridge.resolveNode("Product")._children[3]._children[30]._children[3];
//         for (var i = 0; i < repeatrow._instanceManager.instanceCount; i++) {
//             repeatrow._instanceManager._instances[i]._children[0].value = "";
//         }

//     }
//     else {
//         guideBridge.resolveNode("Product")._children[3]._children[30].visible = false;
//     }
// }

function validateAmountIncrement(maxAmount, amountIncrement){
    if(amountIncrement != null && maxAmount != null){
        if(amountIncrement > maxAmount){
            //alert("Increment Amount cannot be greater than Maximum amount.");
            guideBridge.resolveNode("Product")._children[3].tableMaxIncrementAmountError.visible = true;

            $(document).on('change', function() {
                guideBridge.resolveNode("Product")._children[3].tableMaxIncrementAmountError.visible = false;
            });

            amountIncrement = "";
        }
    }
    return amountIncrement;
}

function validateMaxAmount(maxAmount, minAmount, amountIncrement){
    if(maxAmount != null && amountIncrement != null){
        if(maxAmount < amountIncrement){
            //alert("Maximum amount cannot be lesser than Increment Amount.");
            guideBridge.resolveNode("Product")._children[3].tableMaxIncrementAmountError.visible = true;

            $(document).on('change', function() {
                guideBridge.resolveNode("Product")._children[3].tableMaxIncrementAmountError.visible = false;
            });

            maxAmount = "";
        }
    }
    
    if(minAmount != null && maxAmount != null){
        if(minAmount > maxAmount){
            //alert("Minimum Amount cannot be greater than Maximum amount.");
            guideBridge.resolveNode("Product")._children[3].tableMinMaxAmountError.visible = true;

            $(document).on('change', function() {
                guideBridge.resolveNode("Product")._children[3].tableMinMaxAmountError.visible = false;
            });

            maxAmount = "";
        }
    }
    return maxAmount;
}

function validateMinAmount(maxAmount, minAmount){
     if(minAmount != null && maxAmount != null){
        if(minAmount > maxAmount){
            //alert("Minimum Amount cannot be greater than Maximum amount.");
            guideBridge.resolveNode("Product")._children[3].tableMinMaxAmountError.visible = true;

            $(document).on('change', function() {
                guideBridge.resolveNode("Product")._children[3].tableMinMaxAmountError.visible = false;
            });

            minAmount = "";
        }
    }
    return minAmount;
}

var disabilityArray = ["Group Disability Insurance"];
var termLifeArray = ["Group Term Life Insurance"];
function setPlanNameItems(productName) {
    var productNameValue = productName.split('-');
    var product = productNameValue[0];

    if (accidentLOBs.includes(product)) {
        guideBridge.resolveNode("PlanName").items = ["Group Accident Insurance-High", "Group Accident Insurance-Mid", "Group Accident Insurance-Low", "Group Accident Insurance-High-LT", "Group Accident Insurance-Mid-LT", "Group Accident Insurance-Low-LT"];
    } else if (hospitalIndemnityLOBs.includes(product)) {
        guideBridge.resolveNode("PlanName").items = ["Group Hospital Indemnity Insurance-High", "Group Hospital Indemnity Insurance-Mid", "Group Hospital Indemnity Insurance-Low", "Group Hospital Indemnity Insurance-High-LT", "Group Hospital Indemnity Insurance-Mid-LT", "Group Hospital Indemnity Insurance-Low-LT"];
    } else if (benExtendLOBs.includes(product)) {
        guideBridge.resolveNode("PlanName").items = ["Group BenExtend Insurance-High", "Group BenExtend Insurance-Mid", "Group BenExtend Insurance-Low", "Group BenExtend Insurance-High-LT", "Group BenExtend Insurance-Mid-LT", "Group BenExtend Insurance-Low-LT"];
    } else if (criticalIllnessLOBs.includes(product)) {
        guideBridge.resolveNode("PlanName").items = ["Group Critical Illness Insurance-With Cancer", "Group Critical Illness Insurance-Without Cancer", "Group Critical Illness Insurance-With Health Screening Benefit", "Group Critical Illness Insurance-Without Health Screening Benefit"];
    } else if (disabilityLOBs.includes(product)) {
        if (productNameValue[1] != null || productNameValue[1] != undefined) {
            var disabilityOption = "Group " + productNameValue[0] + " Insurance-" + productNameValue[1];
            if (!disabilityArray.includes(disabilityOption)) {
                disabilityArray.push(disabilityOption);
            }
        }
        setTimeout(function() {
            var repeatPanel = guideBridge.resolveNode("Product");
            for (var i = 0; i < repeatPanel.instanceManager.instanceCount; i++) {
                var prod = repeatPanel.instanceManager.instances[i]._children[1]._children[0].value;
                if (prod.split('-')[0] == 'Disability') {
                    repeatPanel.instanceManager.instances[i]._children[1]._children[2].items = disabilityArray;
                }
            }
        }, 100);

    } else if (wholeLifeLOBs.includes(product)) {
        guideBridge.resolveNode("PlanName").items = ["Group Whole Life Insurance"];
    } else if (termLifeLOBs.includes(product)) {
        if (productNameValue[1] != null || productNameValue[1] != undefined) {
            var termLifeOption = "Group " + productNameValue[0] + " Insurance-" + productNameValue[1];
            if (!termLifeArray.includes(termLifeOption)) {
                termLifeArray.push(termLifeOption);
            }
        }
        setTimeout(function() {
            var repeatPanel = guideBridge.resolveNode("Product");
            for (var i = 0; i < repeatPanel.instanceManager.instanceCount; i++) {
                var prod = repeatPanel.instanceManager.instances[i]._children[1]._children[0].value;
                if (prod.split('-')[0] == 'Term Life') {
                    repeatPanel.instanceManager.instances[i]._children[1]._children[2].items = termLifeArray;
                }
            }
        }, 100);
    } else if (termto120LOBs.includes(product)) {
        guideBridge.resolveNode("PlanName").items = ["Group Term to 120 Insurance"];
    }
}

function clearDisabilityArray(){
    disabilityArray = ["Group Disability Insurance"];
    termLifeArray = ["Group Term Life Insurance"];
}

function planNameChange(planName){
    var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;

    var planNameValue = planName.split('-');
    //var groupPlanName = planNameValue[0];
    var NewLevel = planNameValue[1];
    var NewLevelLTHT = planNameValue[2];
    var productNameValue = productName.split('-');
    var NewProductName = productNameValue[0] + '-' + NewLevel + '-' + NewLevelLTHT;

    if(NewLevelLTHT == null || NewLevelLTHT == undefined){
        NewLevelLTHT = '';
        NewProductName = productNameValue[0] + '-' + NewLevel;
    }

    if(NewLevel == null || NewLevel == undefined){
        NewLevel = '';
        NewProductName = productNameValue[0];
    }
    
    guideBridge.resolveNode("Product")._children[1]._children[0].value = NewProductName;
    guideBridge.resolveNode("Product").title = NewProductName;
    guideBridge.resolveNode("Product").summary = NewProductName;

    updateProductsSold();
}

function planDescriptionChange(planDescription){
    var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;

    if(planDescription != null){
        guideBridge.resolveNode("Product").title = productName + '-' + planDescription;
        guideBridge.resolveNode("Product").summary = productName + '-' + planDescription;    
    } 
    else{
        guideBridge.resolveNode("Product").title = productName;
        guideBridge.resolveNode("Product").summary = productName;    
    }
    updateProductsSold();
}

function updateProductsSold(){
    var productsSold = [];
        var repeatPanel = guideBridge.resolveNode("Product");
        for (var i = 0; i < repeatPanel.instanceManager.instanceCount; i++) {
            productsSold.push(repeatPanel.instanceManager.instances[i]._children[1]._children[0].value);
            //productsSold.push(repeatPanel.instanceManager.instances[i].title);  
        }
        guideBridge.resolveNode("AI_ProductsSold").value = '';
        guideBridge.resolveNode("AI_ProductsSold").value = productsSold.join(',');
}

function clearDropdownValues(){
    document.querySelectorAll(".AI_groupType select")[0].value = "";
    document.querySelectorAll(".AI_SSN select")[0].value = "";

}

function hideOptionalRider(){
    var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;

    var EmpMaxamount = guideBridge.resolveNode("Product")._children[3].AmtOfferedTable._children[3]._children[2].value;
    var EmpGuaranteedIssueMax = guideBridge.resolveNode("Product")._children[3].AmtOfferedTable._children[3]._children[4].value;
    var Series = guideBridge.resolveNode("Product")._children[1].Series.value;
    //_children[1]._children[3]
    

    if(EmpGuaranteedIssueMax != null && EmpMaxamount != null){
        if(criticalIllnessLOBs.includes(productName) && EmpGuaranteedIssueMax < EmpMaxamount){
            guideBridge.resolveNode("Product")._children[3].progressiveRider.visible = true;
            if(Series != 22000){
                guideBridge.resolveNode("Product")._children[3].optionalRider.visible = true;
            }
            else {
                guideBridge.resolveNode("Product")._children[3].optionalRider.visible = false;
                guideBridge.resolveNode("Product")._children[3].optionalRider.value = '';
            }
        }
        else if(criticalIllnessLOBs.includes(productName) && EmpGuaranteedIssueMax >= EmpMaxamount){
            guideBridge.resolveNode("Product")._children[3].progressiveRider.visible = false;
            guideBridge.resolveNode("Product")._children[3].progressiveRider.value = '';
            guideBridge.resolveNode("Product")._children[3].optionalRider.visible = false;
            guideBridge.resolveNode("Product")._children[3].optionalRider.value = '';
        }
    }
 }

function RiderMaxGICondition(){
    var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;

    var EmpMaxamount = guideBridge.resolveNode("Product")._children[3].AmtOfferedTable._children[3]._children[2].value;
    var EmpGuaranteedIssueMax = guideBridge.resolveNode("Product")._children[3].AmtOfferedTable._children[3]._children[4].value;
    var Series = guideBridge.resolveNode("Product")._children[1].Series.value;

    if(EmpGuaranteedIssueMax != null && EmpMaxamount != null){
        if(criticalIllnessLOBs.includes(productName) && EmpGuaranteedIssueMax < EmpMaxamount){
            guideBridge.resolveNode("Product")._children[3].progressiveRider.visible = true;
            if(Series != 22000){
                guideBridge.resolveNode("Product")._children[3].optionalRider.visible = true;
            }
            else {
                guideBridge.resolveNode("Product")._children[3].optionalRider.visible = false;
                guideBridge.resolveNode("Product")._children[3].optionalRider.value = '';
            }
        }
        else if(criticalIllnessLOBs.includes(productName) && EmpGuaranteedIssueMax >= EmpMaxamount){
            guideBridge.resolveNode("Product")._children[3].progressiveRider.visible = false;
            guideBridge.resolveNode("Product")._children[3].progressiveRider.value = '';
            guideBridge.resolveNode("Product")._children[3].optionalRider.visible = false;
            guideBridge.resolveNode("Product")._children[3].optionalRider.value = '';
        }
    }
}

function setNumberOfEligibleEmployeeValue(eligibleEmployeeValue){
    var productName = guideBridge.resolveNode("Product")._children[1]._children[0].value;
    if(disabilityLOBs.includes(productName) || disabilityLOBs.includes(productName.split('-')[0]) || wholeLifeLOBs.includes(productName)|| termLifeLOBs.includes(productName) || termLifeLOBs.includes(productName.split('-')[0]) || termto120LOBs.includes(productName)){
        return eligibleEmployeeValue;
    }else{
        return null;
    }
}

function clearTobaccoStatusDetermined(TobaccoStatusDeterminedSOM){
    guideBridge.resolveNode(TobaccoStatusDeterminedSOM).value = '';
    for(var i=0; i<document.querySelectorAll(".TobaccoStatusDetermined").length; i++){
        if(document.querySelectorAll(".TobaccoStatusDetermined")[i].id == guideBridge.resolveNode(TobaccoStatusDeterminedSOM).id){
            document.querySelectorAll(".TobaccoStatusDetermined select")[i].value = '';
        }
    }
}

function getPdfDirections(situs,formCase){
    if((formCase == "Add" || !(caseBuildResponse.accountInformation.situsState == situs)) && situs !=null && !situs==""){
        $.ajax({
            url: "/bin/GetCaseBuilderToolProductsData",
            type: 'GET',
            data: { mode: "fetchPdfDirections",situs: situs},
            dataType: 'json',
            success: function (res) {
                if ( res["pdf-directions"] != null){
                    guideBridge.resolveNode("AI_pdfDirections").value = res["pdf-directions"];
                }
            }
        });
    }
}

function productsSoldValues(productsSold){
    var NumericValues = [];
    var productsSoldArray = productsSold.split(',');
    for(var i=0; i<productsSoldArray.length; i++){
        if(accidentLOBs.includes(productsSoldArray[i].split('-')[0]) && !NumericValues.includes('1')){
            NumericValues.push('1');
        }
        else if(hospitalIndemnityLOBs.includes(productsSoldArray[i].split('-')[0]) && !NumericValues.includes('3')){
            NumericValues.push('3');
        }
        else if(benExtendLOBs.includes(productsSoldArray[i].split('-')[0]) && !NumericValues.includes('4')){
            NumericValues.push('4');
        }
        else if(criticalIllnessLOBs.includes(productsSoldArray[i].split('-')[0]) && !NumericValues.includes('2')){
            NumericValues.push('2');
        }
        else if(disabilityLOBs.includes(productsSoldArray[i].split('-')[0]) && !NumericValues.includes('5')){
            NumericValues.push('5');
        }
        else if(wholeLifeLOBs.includes(productsSoldArray[i].split('-')[0]) && !NumericValues.includes('6')){
            NumericValues.push('6');
        }
        else if(termLifeLOBs.includes(productsSoldArray[i].split('-')[0]) && !NumericValues.includes('7')){
            NumericValues.push('7');
        }
        else if(termto120LOBs.includes(productsSoldArray[i].split('-')[0]) && !NumericValues.includes('8')){
            NumericValues.push('8');
        }
    }
    return NumericValues.join(',');
}

function hideSaveMessage(){
    $(document).on('change', function() {
        guideBridge.resolveNode("saveSuccessMessage").visible = false;
        guideBridge.resolveNode("saveFailureMessage").visible = false;
        guideBridge.resolveNode("fileNetMsg").visible = false;
        guideBridge.resolveNode("coverageEffDateFailureMessage").visible = false;
    });
}

function showGenerateButtons(StartEnrollmentDate) {
    var StartDate = new Date(StartEnrollmentDate);
    var thresholdDate = new Date("2023-10-01");
    
    if(StartDate < thresholdDate || StartEnrollmentDate == null){
        guideBridge.resolveNode('GenerateWithCompliance').visible = true;
        guideBridge.resolveNode('GenerateWithComplianceText').visible = true;
        guideBridge.resolveNode('GenerateWithoutCompliance').visible = false;
        guideBridge.resolveNode('GenerateWithoutComplianceText').visible = false;
        guideBridge.resolveNode('GenerateCompliance').visible = false;
        guideBridge.resolveNode('GenerateComplianceText').visible = false;
    }
    else if(StartDate >= thresholdDate){
        guideBridge.resolveNode('GenerateWithCompliance').visible = false;
        guideBridge.resolveNode('GenerateWithComplianceText').visible = false;
        guideBridge.resolveNode('GenerateWithoutCompliance').visible = true;
        guideBridge.resolveNode('GenerateWithoutComplianceText').visible = true;
        guideBridge.resolveNode('GenerateCompliance').visible = true;
        guideBridge.resolveNode('GenerateComplianceText').visible = true;
    }

    if(StartDate > thresholdDate && guideBridge.resolveNode('R_exportAs').value == 'PDF'){
        guideBridge.resolveNode('GenerateCompliance').visible = true;
        guideBridge.resolveNode('GenerateComplianceText').visible = true;
    }
    else {
        guideBridge.resolveNode('GenerateCompliance').visible = false;
        guideBridge.resolveNode('GenerateComplianceText').visible = false;
    }
}

function showGenerateCompliadncePDF(exportAs, StartEnrollmentDate){
    var StartDate = new Date(StartEnrollmentDate);
    var thresholdDate = new Date("2023-10-01");

    if(exportAs == 'PDF'){
        guideBridge.resolveNode('GenerateCaseBuildGuide').visible = true;
        guideBridge.resolveNode('GenerateCaseBuildGuideText').visible = true;
    }
    else {
        guideBridge.resolveNode('GenerateCaseBuildGuide').visible = false;
        guideBridge.resolveNode('GenerateCaseBuildGuideText').visible = false;
    }

    if(StartDate >= thresholdDate && exportAs == 'PDF'){
        guideBridge.resolveNode('GenerateCompliance').visible = true;
        guideBridge.resolveNode('GenerateComplianceText').visible = true;
    }
    else {
        guideBridge.resolveNode('GenerateCompliance').visible = false;
        guideBridge.resolveNode('GenerateComplianceText').visible = false;
    }
}

function disableCertificationLanguagePDF(StartEnrollmentDate){
    var StartDate = new Date(StartEnrollmentDate);
    var thresholdDate = new Date("2023-10-01");
    var condition = false;
    
    if(StartDate < thresholdDate || StartEnrollmentDate == null){
        //guideBridge.resolveNode('GenerateCompliance').enabled = false;
        condition = false;
    }
    else if(StartDate >= thresholdDate){
        //guideBridge.resolveNode('GenerateCompliance').enabled = true;
        condition = true;
    }
    return condition;
}

function UpdateTerm120ApplicationNumber(){
    var situsState = guideBridge.resolveNode("AI_SitusState").value;
    var groupType = guideBridge.resolveNode("AI_groupType").value;
    var formCase = guideBridge.resolveNode("formCase").value;
    var series = guideBridge.resolveNode("Product")._children[1].Series.value;
    var eeBenefitMaxAmt = guideBridge.resolveNode("Product")._children[3].AmtOfferedTable._children[3]._children[2].value;
    var giMaxAmt =guideBridge.resolveNode("Product")._children[3].AmtOfferedTable._children[3]._children[4].value;
    
    if(situsState != null && groupType != null && series != null && eeBenefitMaxAmt != null && giMaxAmt != null){
        var product = guideBridge.resolveNode("Product")._children[1]._children[0].value;

        var giAMount = false;
        if(eeBenefitMaxAmt>giMaxAmt){
            giAMount=true;
        }
        if(termto120LOBs.includes(product) && formCase =="Add"){
            $.ajax({
                url: "/bin/GetCaseBuilderToolProductsData",
                type: 'GET',
                data: { mode: "applicationNo", situs: situsState, series: series , groupType: groupType,giAMount:giAMount},
                dataType: 'json', // added data type
                success: function (res) {
                    
                    if ( res["form-id"] == null){
                        //window.alert("Application no. not found.");
                        guideBridge.resolveNode("Product")._children[1].ApplicationNumber.value = "";
                        document.getElementById(field.id).className = "guideFieldNode guideTextBox ApplicationNumber defaultFieldLayout af-field-filled validation-failure";
                        setTimeout(function() {
                        var alert = document.getElementById(field.id).children[3];
                        alert.setAttribute('role', 'alert');
                        var alertid = "#" + alert.id;
                        $(alertid).html("Application number not found.");
                        },10);
                    }
                    else{
                        guideBridge.resolveNode("Product")._children[1].ApplicationNumber.value = res["form-id"];
                        document.getElementById(field.id).className = "guideFieldNode guideTextBox ApplicationNumber defaultFieldLayout af-field-filled validation-success ";
                    }
                }
            });
        }else if(termto120LOBs.includes(product) && (formCase =="Edit" || formCase=="Update") && (eeBenefitMaxAmt!=eeBenefitMaxAmtApi || giMaxAmt!=giMaxAmtApi)){
            var data = caseBuildResponse;
            var productArray = data.products.product;
            for (var i = 0; i < productArray.length; i++) {
                if(product==productArray[i].productName){
                    var eeBenefitMaxAmtApi = productArray[i].employeeAmountOffered.amountOffered.maximumAmtElect;
                    var giMaxAmtApi = productArray[i].employeeAmountOffered.amountOffered.guaranteedIssueMaximum;
                }
            }
            $.ajax({
                url: "/bin/GetCaseBuilderToolProductsData",
                type: 'GET',
                data: { mode: "applicationNo", situs: situsState, series: series , groupType: groupType,giAMount:giAMount},
                dataType: 'json', // added data type
                success: function (res) {
                    
                    if ( res["form-id"] == null){
                        //window.alert("Application no. not found.");
                        guideBridge.resolveNode("Product")._children[1].ApplicationNumber.value = "";
                        document.getElementById(field.id).className = "guideFieldNode guideTextBox ApplicationNumber defaultFieldLayout af-field-filled validation-failure";
                        setTimeout(function() {
                        var alert = document.getElementById(field.id).children[3];
                        alert.setAttribute('role', 'alert');
                        var alertid = "#" + alert.id;
                        $(alertid).html("Application number not found.");
                        },10);
                    }
                    else{
                        guideBridge.resolveNode("Product")._children[1].ApplicationNumber.value = res["form-id"];
                        document.getElementById(field.id).className = "guideFieldNode guideTextBox ApplicationNumber defaultFieldLayout af-field-filled validation-success ";
                    }
                }
            });
        }   
    }
}

function generateCompliancePDF(exportAs, docCase,productSold,situsState, isCI22Kenabled){

    ComplianceSubmitData= "";
    ComplianceSubmitDocCase= "";
    ComplianceSubmitLob= "";
    ComplianceSubmitSitus= "";
    ComplianceSubmitFileName= "";
    
    isCI22Kenabled = guideBridge.resolveNode("isCI22Kenabled").value;
    var CI22Kcondition = false;
    if(isCI22Kenabled == "Yes"){
        CI22Kcondition = true;
    }
    else {
        CI22Kcondition = false;
    }

    exportAs = "PDF";
    var format = exportAs;
    var fileName = guideBridge.resolveNode("AI_AccountName").value + '_' + guideBridge.resolveNode("AI_groupNumber").value + '_' + guideBridge.resolveNode("EffectiveDate").value + '_' + new Date().toJSON().slice(0, 10) + '.xlsx';
    var lob = productsSoldValues(productSold);
    var loader = document.createElement('div');
    loader.setAttribute('id', 'previewLoader');
    loader.setAttribute('class', 'loader');
    loader.rel = 'stylesheet';
    loader.type = 'text/css';
    loader.href = '/css/loader.css';
    document.getElementsByTagName('BODY')[0].appendChild(loader);
    document.getElementById("guideContainerForm").style.filter = "blur(10px)";
    document.getElementById("guideContainerForm").style.pointerEvents = "none";
    guideBridge.getDataXML({
        success: function(guideResultObject) {
            var req = new XMLHttpRequest();
            req.open("POST", "/bin/CaseBuilderToolSubmitServlet", true);
            //req.responseType = "blob";
            var postParameters = new FormData();
            postParameters.append("formData", guideResultObject.data);
            postParameters.append("mode", exportAs);
            postParameters.append("case", docCase);
            postParameters.append("lob", lob);
            postParameters.append("situsState", situsState);
            postParameters.append("CI22Kcondition", CI22Kcondition);
            postParameters.append("activity", "generate");
            req.send(postParameters);
            req.onreadystatechange = function() {
                if (req.readyState == 4 && req.status == 200) {
                    guideBridge.resolveNode("fileNetMsg").visible = true;
                    docId = JSON.parse(req.responseText)["DocumentID"];
                    var msg = "<p><span class=\"greenColorText\"><b>Success : Certification language pdf file is successfully generated and saved to FileNet with ID: " + docId + "</b></span></p>";
                    guideBridge.resolveNode("fileNetMsg").value = msg;
                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                    //readCaseBuildFile(guideResultObject.data, docCase, lob, situsState);
                    ComplianceSubmitData= guideResultObject.data;
                    ComplianceSubmitDocCase= docCase;
                    ComplianceSubmitLob= lob;
                    ComplianceSubmitSitus= situsState;

                    guideBridge.resolveNode("downloadCompliance").visible= true;
                }
                else if (req.readyState == 4 && req.status == 400) {
                    var msg = JSON.parse(req.responseText)["message"];
                    msg = "<p><span class=\"redColorText\"><b>" + msg + "</b></span></p>";
                    guideBridge.resolveNode("fileNetMsg").visible = true;
                    guideBridge.resolveNode("fileNetMsg").value = msg;
                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                    //readCaseBuildFile(guideResultObject.data, docCase, lob, situsState);
                    ComplianceSubmitData= guideResultObject.data;
                    ComplianceSubmitDocCase= docCase;
                    ComplianceSubmitLob= lob;
                    ComplianceSubmitSitus= situsState;

                    guideBridge.resolveNode("downloadCompliance").visible= true;
                }
                else if(req.readyState == 4) {
                    console.log("Failure..");
                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                }
            }
        }
    });
}

function readCertifcationLanguageFile(){

    var groupName = guideBridge.resolveNode('AI_AccountName').value;
    var CED = guideBridge.resolveNode('EffectiveDate').value;

    var _date = CED.split('-');
    var dateObj = {month: _date[1], day: _date[2], year: _date[0]};

    CED = dateObj.month + '-' + dateObj.day + '-' + dateObj.year;
    
    var formData = ComplianceSubmitData;
    var docCase = ComplianceSubmitDocCase;
    var lob = ComplianceSubmitLob;
    var situs = ComplianceSubmitSitus;
    var fileName = groupName+" "+CED+" Certification Language";

    var loader = document.createElement('div');
    loader.setAttribute('id', 'previewLoader');
    loader.setAttribute('class', 'loader');
    loader.rel = 'stylesheet';
    loader.type = 'text/css';
    loader.href = '/css/loader.css';
    document.getElementsByTagName('BODY')[0].appendChild(loader);
    document.getElementById("guideContainerForm").style.filter = "blur(10px)";
    document.getElementById("guideContainerForm").style.pointerEvents = "none";
    guideBridge.getDataXML({
        success: function(guideResultObject) {
            var req = new XMLHttpRequest();
            req.open("POST", "/bin/CaseBuilderToolSubmitServlet", true);
            req.responseType = "blob";
            var postParameters = new FormData();
            postParameters.append("formData", formData);
            postParameters.append("mode", "PDF");
            postParameters.append("case", docCase);
            postParameters.append("lob", lob);
            postParameters.append("situsState", situs);
            postParameters.append("activity", "read");
            req.send(postParameters);
            req.onreadystatechange = function () {

                if (req.readyState == 4 && req.status == 200) {
                    // var blob = new Blob([this.response], {
                    //     type: "application/pdf"
                    // }),
                    // newUrl = URL.createObjectURL(blob);
                    // window.open(newUrl, "_blank", "menubar=yes,resizable=yes,scrollbars=yes");
                    var a;
                    a = document.createElement('a');
                    a.href = window.URL.createObjectURL(req.response);
                    a.download = fileName;
                    a.style.display = 'none';
                    document.body.appendChild(a);
                    a.click();
                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                }  
                else if(req.readyState == 4) {
                    console.log("Read Failure..");
                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                }  
            };
        }
    });
}

function getSeriesApiValue(productNamevalue){
    var data = caseBuildResponse;
    var productArray = data.products.product;
    var apiSeriesValue = null;
    for (var i = 0; i < productArray.length; i++) {
        if(productNamevalue==productArray[i].productName){
            apiSeriesValue = productArray[i].series;
        }
    }
    return apiSeriesValue;
}

function getAmtApiValue(productNamevalue,propertyName){
    var data = caseBuildResponse;
    var productArray = data.products.product;
    var apiValue = null;
    for (var i = 0; i < productArray.length; i++) {
        if(productNamevalue==productArray[i].productName){
            apiValue = productArray[i].employeeAmountOffered.amountOffered[propertyName];
        }
    }
    return apiValue;
}

function CI22KCertLangCondition(){
    var situs = guideBridge.resolveNode("AI_SitusState").value;
    var nonRefiledStates = ["CA", "NH", "NJ", "VA", "ID", "PA"];
    guideBridge.resolveNode("isCI22Kenabled").visible = false;
    guideBridge.resolveNode("isCI22Kenabled").value = "";

    var repeatPanel = guideBridge.resolveNode("Product");
    for (var i = 0; i < repeatPanel.instanceManager.instanceCount; i++) {
        var prod = repeatPanel.instanceManager.instances[i]._children[1]._children[0].value;
        var series = repeatPanel.instanceManager.instances[i]._children[1].Series.value;
        if (nonRefiledStates.includes(situs) && prod.split('-')[0] == 'Critical Illness' && series == "22000") {
            guideBridge.resolveNode("isCI22Kenabled").visible = true;
            guideBridge.resolveNode("isCI22Kenabled").value = "Yes";
            break;
        }
    }
}

function showisCI22KEnabled(){
    var situs = guideBridge.resolveNode("AI_SitusState").value;
    var nonRefiledStates = ["CA", "NH", "NJ", "VA", "ID", "PA"];
    var productsSold = guideBridge.resolveNode("AI_ProductsSold").value;
    var productsSoldArray = productsSold.split(',');
    guideBridge.resolveNode("isCI22Kenabled").visible = false;
    guideBridge.resolveNode("isCI22Kenabled").value = "";

    for(var i=0; i<productsSoldArray.length; i++){
        if(criticalIllnessLOBs.includes(productsSoldArray[i].split('-')[0]) && nonRefiledStates.includes(situs)){
            guideBridge.resolveNode("isCI22Kenabled").visible = true;
            break;
        }
    }
}

function clearEligibleEmployeeGuaranteedIssue(eligibleEmployeeGuaranteedIssueSOM){
    guideBridge.resolveNode(eligibleEmployeeGuaranteedIssueSOM).value = '';
    for(var i=0; i<document.querySelectorAll(".eligibleEmployeeGuaranteedIssue").length; i++){
        if(document.querySelectorAll(".eligibleEmployeeGuaranteedIssue")[i].id == guideBridge.resolveNode(eligibleEmployeeGuaranteedIssueSOM).id){
            document.querySelectorAll(".eligibleEmployeeGuaranteedIssue select")[i].value = '';
        }
    }
}

function FillPlatformGuideTable(platform){
    
    for (var j=0; j<platformGuide.length; j++){
        if(platform.includes(platformGuide[j].platformName)){
            guideBridge.resolveNode("platformGuideTable").visible = true;
            if(platformGuide[j].files.length > 1){
                document.querySelectorAll(".platformGuideTableHeader")[0].children[0].innerHTML = "Platform Specific Guides for " + platform ;
            }
            else{
                document.querySelectorAll(".platformGuideTableHeader")[0].children[0].innerHTML = "Platform Specific Guide for " + platform ;
            }

            var repeatrow = guideBridge.resolveNode("platformGuideTableRow");
            var currentCount = repeatrow.instanceManager.instanceCount;
            for (var m = 0; m < currentCount; m++) {
                if (m != 0) {
                    repeatrow.instanceManager.removeInstance(0);
                }
            }
            for (var i = 0; i < platformGuide[j].files.length; i++) {
                if (i != 0) {
                    repeatrow.instanceManager.addInstance();
                }
            }
            for (var i = 0; i < repeatrow.instanceManager.instanceCount; i++) {
                repeatrow._instanceManager._instances[i].platformFileName.value = platformGuide[j].files[i].fileName;
            }
            break;
        }
        else {
            guideBridge.resolveNode("platformGuideTable").visible = false;
        }
    }
}

function downloadPlatformGuide(fileName){
    var loader = document.createElement('div');
    loader.setAttribute('id', 'previewLoader');
    loader.setAttribute('class', 'loader');
    loader.rel = 'stylesheet';
    loader.type = 'text/css';
    loader.href = '/css/loader.css';
    document.getElementsByTagName('BODY')[0].appendChild(loader);
    document.getElementById("guideContainerForm").style.filter = "blur(10px)";
    document.getElementById("guideContainerForm").style.pointerEvents = "none";
    
    var ext = fileName.substring(fileName.lastIndexOf(".")+1);
    // console.log(ext);
    // console.log(fileName)    

    guideBridge.getDataXML({
        success: function (guideResultObject) {
            var req = new XMLHttpRequest();

            req.open("POST", "/bin/GetCaseBuilderToolProductsData", true);
            req.responseType = "blob";
            var postParameters = new FormData();
            postParameters.append("mode","platformGuide");
            postParameters.append("file",fileName);
            req.send(postParameters);
            req.onreadystatechange = function () {

                if (req.readyState == 4 || req.status == 200) {
                    var a;
                    a = document.createElement('a');
                    a.href = window.URL.createObjectURL(req.response);
                    a.download = fileName;
                    a.style.display = 'none';
                    document.body.appendChild(a);
                    a.click();

                    document.getElementById("guideContainerForm").style.filter = "blur()";
                    document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                    loader.setAttribute('class', 'loader-disable');
                }
                else {
                   console.log("File Not Found");

                   document.getElementById("guideContainerForm").style.filter = "blur()";
                   document.getElementById("guideContainerForm").style.pointerEvents = "auto";
                   loader.setAttribute('class', 'loader-disable');
                }
            };
        }
    });
}

function setRateFormatValues(platform){
    for (var i=0; i<platformDetails.length; i++){
        if(platform == platformDetails[i].platform){
            guideBridge.resolveNode("rateCalculationMethod").value = platformDetails[i]["rate-calculation-method"];
            guideBridge.resolveNode("rateBasis").value = platformDetails[i]["rate-basis"];
        }
    }
}

function setProductCheckBoxValues(productsSold){
    var acCheckBoxValues = [];
    var hiCheckBoxValues = [];
    var beCheckBoxValues = [];
    var ciCheckBoxValues = [];
    var diCheckBoxValues = [];
    var wlCheckBoxValues = [];
    var tlCheckBoxValues = [];
    var t120CheckBoxValues = [];
    var productsSoldArray = productsSold.split(',');
    for(var i=0; i<productsSoldArray.length; i++){
        if(accidentLOBs.includes(productsSoldArray[i])){
            acCheckBoxValues.push(productsSoldArray[i]);
            var acValue = acCheckBoxValues.join(',');
            console.log("Accident checkbox values :"+acValue)
            guideBridge.resolveNode("AccidentCheckBoxEditCase").value = acValue;
        }
        if(hospitalIndemnityLOBs.includes(productsSoldArray[i])){
            hiCheckBoxValues.push(productsSoldArray[i]);
            var hiValues = hiCheckBoxValues.join(',');
            console.log("hospital checkbox values :"+hiValues)
            guideBridge.resolveNode("HospitalIdenmityCheckboxEditCase").value = hiValues;
        }
        if(benExtendLOBs.includes(productsSoldArray[i])){
            beCheckBoxValues.push(productsSoldArray[i]);
            var beValues = beCheckBoxValues.join(',');
            console.log("benExtend checkbox values :"+beValues)
            guideBridge.resolveNode("BenExtendCheckboxEditCase").value = beValues;
        }
        if(criticalIllnessLOBs.includes(productsSoldArray[i])){
            ciCheckBoxValues.push(productsSoldArray[i]);
            var ciValues = ciCheckBoxValues.join(',');
            console.log("Critical checkbox values :"+ciValues)
            guideBridge.resolveNode("CriticalIllnessCheckboxEditCase").value = ciValues;
        }
        if(disabilityLOBs.includes(productsSoldArray[i])){
            diCheckBoxValues.push(productsSoldArray[i]);
            var diValues = diCheckBoxValues.join(',');
            console.log("Disablity checkbox values :"+diValues)
            guideBridge.resolveNode("DisablityCheckboxEditCase").value = diValues;
        }
        if(wholeLifeLOBs.includes(productsSoldArray[i])){
            wlCheckBoxValues.push(productsSoldArray[i]);
            var wlValues = wlCheckBoxValues.join(',');
            console.log("wholeLife checkbox values :"+wlValues)
            guideBridge.resolveNode("WholeLifeCheckboxEditCase").value = wlValues;
        }
        if(termLifeLOBs.includes(productsSoldArray[i])){
            tlCheckBoxValues.push(productsSoldArray[i]);
            var tlValues = tlCheckBoxValues.join(',');
            console.log("termLife checkbox values :"+tlValues)
            guideBridge.resolveNode("TermLifeCheckboxEditCase").value = tlValues;
        }
        if(termto120LOBs.includes(productsSoldArray[i])){
            t120CheckBoxValues.push(productsSoldArray[i]);
            var t120Values = t120CheckBoxValues.join(',');
            console.log("termto120 checkbox values :"+t120Values)
            guideBridge.resolveNode("Termto120CheckboxEditCase").value = t120Values;
        }
    }
}

function setFormCaseUpdate(){
    var data = caseBuildResponse;
    guideBridge.resolveNode("formCase").value = "Edit";
    var selectedProductsEditCase = guideBridge.resolveNode("selectedProductsEditCase").value;
    if(selectedProductsEditCase.endsWith(',')){
        selectedProductsEditCase = selectedProductsEditCase.slice(0, selectedProductsEditCase.length - 1);
    }

    var selectedProductsArray = selectedProductsEditCase.split(',');
    var productsSoldArray = data.accountInformation.productSold.split(',');

    if(selectedProductsArray.length != productsSoldArray.length){
        guideBridge.resolveNode("formCase").value = "Update";
    } else {
        for(var i=0; i<productsSoldArray.length; i++){
            if(!selectedProductsArray.includes(productsSoldArray[i])){
                guideBridge.resolveNode("formCase").value = "Update";
                break;
            }
        }
    }
}

function setDeletedProducts(){
    var data = caseBuildResponse;
    var selectedProductsEditCase = guideBridge.resolveNode("selectedProductsEditCase").value;
    var selectedProductsArray = selectedProductsEditCase.split(',');
    var productsSoldArray = data.accountInformation.productSold.split(',');
    var deletedProductsArray = [];

    for(var i=0; i<productsSoldArray.length; i++){
        if((!selectedProductsArray.includes(productsSoldArray[i])) && !productsSoldArray[i].includes("Disability-") && !productsSoldArray[i].includes("Term Life-")){
            deletedProductsArray.push(productsSoldArray[i]);
        }
    }

    guideBridge.resolveNode("deletedProducts").value = deletedProductsArray.join(',');
}

function setAvailaibleProductsEditCase(){
    accidentValues = guideBridge.resolveNode("AccidentCheckBoxEditCase").value;
    hospitalValues = guideBridge.resolveNode("HospitalIdenmityCheckboxEditCase").value;
    benExtendValues = guideBridge.resolveNode("BenExtendCheckboxEditCase").value;
    crticalValues = guideBridge.resolveNode("CriticalIllnessCheckboxEditCase").value;
    disabilitySingle = guideBridge.resolveNode("DisablityCheckboxEditCase").value;
    wholeLifeValues = guideBridge.resolveNode("WholeLifeCheckboxEditCase").value;
    termLifeValues = guideBridge.resolveNode("TermLifeCheckboxEditCase").value;
    termto120 = guideBridge.resolveNode("Termto120CheckboxEditCase").value;

    var data = caseBuildResponse;
    var productsSoldArray = data.accountInformation.productSold.split(',');

    var values="";
    if(accidentValues!=null){
        values+=accidentValues+",";
    }
    if(hospitalValues!=null){
        values+=hospitalValues+",";
    }
    if(benExtendValues!=null){
        values+=benExtendValues+",";
    }
    if(crticalValues!=null){
        values+=crticalValues+",";
    }
    if(disabilitySingle!=null){
        values+=disabilitySingle+",";
    }
    if(wholeLifeValues!=null){
        values+=wholeLifeValues+",";
    }
    if(termLifeValues!=null ){
        values+= termLifeValues+",";
    }
    if(termto120!=null){
        values+=termto120LOBs[0]+",";
    }

    for(var i=0; i<productsSoldArray.length; i++){
        if(productsSoldArray[i].includes("Disability-") || productsSoldArray[i].includes("Term Life-")){
            values+=productsSoldArray[i]+",";
        }
    }

    return values;
}

function hideExtraCheckbox(){
    document.querySelectorAll('.DisablityCheckboxEditCase')[2].style.display = 'none';
    document.querySelectorAll('.TermLifeCheckboxEditCase')[2].style.display = 'none';
    document.querySelectorAll('.WholeLifeCheckboxEditCase')[2].style.display = 'none';
    document.querySelectorAll('.Termto120CheckboxEditCase')[2].style.display = 'none';
}

function setProductionTestFileNaming(platform){
    for (var i=0; i<platformDetails.length; i++){
        if(platform == platformDetails[i].platform){
            guideBridge.resolveNode("FS_ProdFileNaming").value = platformDetails[i]["production-file-naming"];
            guideBridge.resolveNode("FS_testFileNaming").value = platformDetails[i]["test-file-naming"];
        }
    }
}

function isCoverageEffDateChanged(coverageDate,ai_coverageDate){
    var formCase = guideBridge.resolveNode("formCase").value;
    if(coverageDate!=ai_coverageDate && (formCase=="Edit" || formCase=="Update")){
        guideBridge.resolveNode("IsCoverageEffDateChanged").value = "Yes";
        guideBridge.resolveNode("validateCoverageEffDate").enabled = true;
    }else if(coverageDate!=ai_coverageDate && (formCase=="Add")){
        guideBridge.resolveNode("IsCoverageEffDateChanged").value = "Yes";
        guideBridge.resolveNode("validateCoverageEffDate").enabled = true;
    } 
    else{
        guideBridge.resolveNode("IsCoverageEffDateChanged").value = "No";
        guideBridge.resolveNode("validateCoverageEffDate").enabled = false;
    }
}

function updateCoverageEffDateFirstScreen(){
    var coverageDate = guideBridge.resolveNode("EffectiveDate").value;
    var ai_coverageDate = guideBridge.resolveNode("AI_CoverageBillingEffDate").value;
    if(coverageDate!=ai_coverageDate){
        guideBridge.resolveNode("EffectiveDate").value = ai_coverageDate;
    }
}

function CoverageDateValidation(){
    var groupNumber = guideBridge.resolveNode("GroupNumber").value;
    var effectiveDate = guideBridge.resolveNode("AI_CoverageBillingEffDate").value;
    var platform = guideBridge.resolveNode("GroupEnrollmentPlatform").value;
    var res = JSON.parse(
        $.ajax({
            url: "/bin/GetCaseBuilderToolProductsData",
            type: "GET",
            async: false,
            data: {
                "groupNumber": groupNumber,
                "effectiveDate":effectiveDate,
                "enrollmentPlatform":platform,
                "mode":"checkEffectiveDate"
            },
            success: function (data) {
                if (data.status != true) {
                    guideBridge.resolveNode("CoverageEffDateMessage").visible = true;
                    guideBridge.resolveNode("coverageDateExistsError").visible = false;
                    $(document).on('change', function() {
                        guideBridge.resolveNode("CoverageEffDateMessage").visible = false;
                    });
                }
                else {
                    guideBridge.resolveNode("coverageDateExistsError").visible = true;
                    guideBridge.resolveNode("CoverageEffDateMessage").visible = false;
                    $(document).on('change', function() {
                        guideBridge.resolveNode("coverageDateExistsError").visible = false;
                    });
                    guideBridge.resolveNode("AI_CoverageBillingEffDate").value = guideBridge.resolveNode("EffectiveDate").value;
                }
            }
        })
    .responseText);
}

function hideAllErrorMessages(){
    guideBridge.resolveNode("saveSuccessMessage").visible = false;
    guideBridge.resolveNode("saveFailureMessage").visible = false;
    guideBridge.resolveNode("fileNetMsg").visible = false;
    guideBridge.resolveNode("coverageEffDateFailureMessage").visible = false;

    guideBridge.resolveNode("endDateValidationMessage").visible = false;
    guideBridge.resolveNode("endDateValidationMessage").visible = false;
    guideBridge.resolveNode("contactIntegrationError").visible = false;

    guideBridge.resolveNode("tableEmptyEBRowError").visible = false;
    guideBridge.resolveNode("tableEmptySBRowError").visible = false;
    guideBridge.resolveNode("tableMaxIncrementAmountError").visible = false;
    guideBridge.resolveNode("tableMinMaxAmountError").visible = false;

    guideBridge.resolveNode("coverageDateExistsError").visible = false;
    guideBridge.resolveNode("CoverageEffDateMessage").visible = false;
}