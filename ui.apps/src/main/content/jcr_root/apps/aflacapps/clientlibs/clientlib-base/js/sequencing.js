var arr1 = [];

function setValue() {

   var allAdd = document.querySelectorAll("div.ProductDesignTable td span.guideTableRuntimeAddControl"); 
   var allDel = document.querySelectorAll("div.ProductDesignTable td span.guideTableRuntimeDeleteControl"); 
    //console.log("allAdd: ", allAdd);

    for(var val=0; val<allAdd.length; val++) {
		allAdd[val].style.display = 'none';
    }

    for(var v=0; v<allDel.length; v++) {
		allDel[v].style.display = 'none';
    }

    var leftAdd = document.querySelectorAll("div.table1ProductItems td span.guideTableRuntimeAddControl"); 

    for(var val2=0; val2<leftAdd.length; val2++) {
        leftAdd[val2].style.display = 'none';
    }

    deleteRows(); 

}

function sequence(){

    $(document).on('click', '.ProductDesignTable .tableItemProdName input[type="text"]', function() {

        this.style.backgroundColor = '#D3D3D3';

		var row = guideBridge.resolveNode("prodDesignRow");
		var count = row.instanceManager.instanceCount;
        for(var i=0; i<count; i++) {
            if(arr1.includes(this.value) != true){
                if(arr1.length != 0)
                    row.instanceManager.addInstance(true);
                row.instanceManager.instances[arr1.length].tableItem1ProdDesign.value = this.value;
                arr1.push(this.value);
                console.log(arr1);
            }
        }

        var allAdd2 = document.querySelectorAll("div.table1ProductItems td span.guideTableRuntimeAddControl"); 
        var allDel2 = document.querySelectorAll("div.table1ProductItems td span.guideTableRuntimeDeleteControl"); 

        for(var val=0; val<allAdd2.length; val++) {
            allAdd2[val].style.display = 'none';
        }
    
        for(var v=0; v<allDel2.length; v++) {
            allDel2[v].style.display = 'none';
        }

    });

}

function resetVal(){
	arr1 = [];
    guideBridge.resolveNode("prodDesignRow").instanceManager.instances[0].tableItem1ProdDesign.value = "";
	deleteRows();
}

function deleteRows(){
	var row1 = guideBridge.resolveNode("prodDesignRow");
    var delCount = row1.instanceManager.instanceCount;
	console.log("ROw Count: ", delCount);

	for (var m = 0; m <= delCount; m++) {
        row1.instanceManager.removeInstance(1);
    }
}

function assemblePDF(dummyInput) {
	var planName = guideBridge.resolveNode("planName");
    var inpData = planName.value;

   guideBridge.getDataXML({
        success: function(guideResultObject) {
            var req = new XMLHttpRequest();

            req.open("POST", "/bin/BrochureAssemblePDF", true);
            req.responseType = "blob"; 
           var postParameters = new FormData();
            postParameters.append("planName", inpData);
            postParameters.append("products", arr1);
            postParameters.append("formData", guideResultObject.data);
            req.send(postParameters);
            req.onreadystatechange = function() {

                if (req.readyState == 4 && req.status == 200) {
                    var blob = new Blob([this.response], {
                            type: "application/pdf"
                       }),
                        newUrl = URL.createObjectURL(blob);
                    window.open(newUrl, "_blank", "menubar=yes,resizable=yes,scrollbars=yes");
                }
            };
        }
    });
}


