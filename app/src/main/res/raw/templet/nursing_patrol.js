function btn1_OnClick() {
//    open("nur_mark_ui");
    alert("填写");
}

function btn_submit_OnClick() {
    var s1 = getValue("dl_drop");
    var s2 = getValue("dl_wlb");
    var s3 = getValue("dl_status");

    if(s1 == "" && s2 == "" && s3 == "")
        alert("请选择巡视内容");
    else {
        alert("save");
        close();
    }
}

function btn_submit_OnTitleClick() {
    close();
}
