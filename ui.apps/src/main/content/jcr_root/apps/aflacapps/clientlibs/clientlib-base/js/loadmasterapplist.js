function populateCheckbox() {
    var repeatPanel = guideBridge.resolveNode("Row1");
    var delCount = repeatPanel.instanceManager.instanceCount;

    for (var k = 0; k < delCount; k++) {
        repeatPanel.instanceManager.instances[k].tableItem11.items = [repeatPanel.instanceManager.instances[k].tableItem12.value + "=" + repeatPanel.instanceManager.instances[k].tableItem12.value];
    }
}

function populateCheckBoxValue(val){
    var values = val.split(',');
    return values;
}