function btn1_OnClick() {
    open("nur_mark_ui");
}

function OnResult(msg) {
    perform("te1", "SetText", msg);
}