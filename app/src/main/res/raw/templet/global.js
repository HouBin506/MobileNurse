function alert(msg) {
    handler.display(msg);
}

function open(ui) {
    handler.open(ui);
}

function close(data) {
    handler.close(data);
}

function close() {
    handler.close("");
}

function save() {
    handler.save();
}

function getProperty(widgetId, propName) {
    return handler.getProperty(widgetId, propName);
}

function setProperty(widgetId, propName, propValue) {
    handler.setProperty(widgetId, propName, propValue);
}

function getDataName(widgetId) {
    return handler.getDataName(widgetId);
}

function setDataName(widgetId, dataName) {
    handler.setDataName(widgetId, dataName);
}

function setDataValue(widgetId, dataValue) {
    return handler.setDataValue(widgetId, dataValue);
}

function getData(key) {
    return handler.getData(key);
}

function getValue(widgetId) {
    return handler.getProperty(widgetId, "Value");
}

function bindData() {
    handler.bindData();
}

function confirm(prompt){
    handler.confirm(prompt);
}