function setOtherFalse() {
    var id;
     function onChange(event)
    {
        id = event.srcElement.id;
       // console.log("Ele: ", event);        
    }
    window.addEventListener('change', onChange);
     var allCB = [];
//var row = [];
   setTimeout(
        function() {
            //console.log("TM Fun");
            allCB = document.querySelectorAll("div.tableOut td input[type='checkbox']");
           // row = document.querySelectorAll("div.tableOut tr");
         //  console.log("allCB: ",allCB);
            for (var i = 0; i < allCB.length; i++) {
                if (allCB[i].id != id) {
                    allCB[i].checked = false;
               } else if (allCB[i].id == id) {
                    allCB[i].checked = true;
               }
            }
        }, 10
    );
}