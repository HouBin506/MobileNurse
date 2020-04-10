function te_nurmark_OnFocusChange(focus) {
//    if(focus)
//        open("nur_mark_ui");
}

function OnResult(msg) {
//    setProperty("te_nurmark", "Value", msg);
}

function OnLoaded() {
    if(handler.mode == "View"){
        bindata();
        setEnabled();
    }
}

function setEnabled() {
    setProperty("te_temp", "Enabled", "false");
    setProperty("te_pulse", "Enabled", "false");
    setProperty("de_pressure", "Enabled", "false");
    setProperty("rg_smoke", "Enabled", "false");
    setProperty("te_temp", "Enabled", "false");
    setProperty("te_pulse", "Enabled", "false");
    setProperty("de_pressure", "Enabled", "false");
    setProperty("te_weight", "Enabled", "false");
    setProperty("dl_aware", "Enabled", "false");
    setProperty("dl_face", "Enabled", "false");
    setProperty("dl_tongkong", "Enabled", "false");
    setProperty("dl_light", "Enabled", "false");
    setProperty("dl_nutrition", "Enabled", "false");
    setProperty("te_water", "Enabled", "false");
    setProperty("dl_bodyloc", "Enabled", "false");
    setProperty("dl_listen", "Enabled", "false");
    setProperty("dl_eye", "Enabled", "false");
    setProperty("dl_mouth", "Enabled", "false");
    setProperty("dl_teeth", "Enabled", "false");
    setProperty("dl_tee", "Enabled", "false");
    setProperty("dl_tongue", "Enabled", "false");
    setProperty("dl_skin", "Enabled", "false");
    setProperty("rg_yachuang", "Enabled", "false");
    setProperty("rg_yinliu", "Enabled", "false");
    setProperty("dl_lang", "Enabled", "false");
    setProperty("rg_keshou", "Enabled", "false");
    setProperty("rg_pain", "Enabled", "false");
    setProperty("dl_eat", "Enabled", "false");
    setProperty("dl_food", "Enabled", "false");
    setProperty("dl_sleep", "Enabled", "false");
    setProperty("dl_activity", "Enabled", "false");
    setProperty("dl_mess", "Enabled", "false");
    setProperty("dl_piss", "Enabled", "false");
    setProperty("dl_self", "Enabled", "false");
    setProperty("dl_health", "Enabled", "false");
    setProperty("rg_smoke", "Enabled", "false");
    setProperty("rg_drink", "Enabled", "false");
    setProperty("rg_drug", "Enabled", "false");
    setProperty("dl_mood", "Enabled", "false");
    setProperty("dl_religen", "Enabled", "false");
    setProperty("dl_attitude", "Enabled", "false");
    setProperty("dl_society", "Enabled", "false");
    setProperty("te_info", "Enabled", "false");
    setProperty("te_risk", "Enabled", "false");
    setProperty("te_nurmark", "Enabled", "false");
}

function btn_save_OnClick() {

    if(handler.mode != "View"){

        setValue("te_temp");
        setValue("te_pulse");
        setValue("de_pressure");
        setValue("rg_smoke");
        setValue("te_temp");
        setValue("te_pulse");
        setValue("de_pressure");
        setValue("te_weight");
        setValue("dl_aware");
        setValue("dl_face");
        setValue("dl_tongkong");
        setValue("dl_light");
        setValue("dl_nutrition");
        setValue("te_water");
        setValue("dl_bodyloc");
        setValue("dl_listen");
        setValue("dl_eye");
        setValue("dl_mouth");
        setValue("dl_teeth");
        setValue("dl_tee");
        setValue("dl_tongue");
        setValue("dl_skin");
        setValue("rg_yachuang");
        setValue("rg_yinliu");
        setValue("dl_lang");
        setValue("rg_keshou");
        setValue("rg_pain");
        setValue("dl_eat");
        setValue("dl_food");
        setValue("dl_sleep");
        setValue("dl_activity");
        setValue("dl_mess");
        setValue("dl_piss");
        setValue("dl_self");
        setValue("dl_health");
        setValue("rg_smoke");
        setValue("rg_drink");
        setValue("rg_drug");
        setValue("dl_mood");
        setValue("dl_religen");
        setValue("dl_attitude");
        setValue("dl_society");
        setValue("te_info");
        setValue("te_risk");
        setValue("te_nurmark");

        save();
        alert("保存成功！");
    }

    close();
}

function setValue(widgetId){
    setDataValue(widgetId, getValue(widgetId));
}

function OnConfirmResult(result) {
    if(result){
        alert("save data");
        close();
    }
    else
        close();
}

function OnDataSaved(result){
}

function te_name1_OnValueChanged(data){
}

function dl_aware_OnValueChanged(data){
}

function de_OnValueChanged(data){
}

function te_bed1_OnValueChanged(data){
}

function OnBack(){
    if(handler.mode != "View")
        confirm("数据已修改，是否保存？");
}