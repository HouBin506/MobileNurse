package com.herenit.mobilenurse.criteria.enums;

import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Sickbed;

/**
 * author: HouBin
 * date: 2019/1/23 16:26
 * desc: 事件意图，用来表示某事件的含义、判断如何来消费等依据
 */
public enum EventIntentionEnum {
    //网络连接发生改变
    CONNECTIVITY_CHANGE("connectivity_change", CommonConstant.DATA_TYPE_NAME_NULL),
    //切换科室或者病区
    SWITCH_GROUP("switch_group", CommonConstant.DATA_TYPE_NAME_INTEGER),
    //切换患者
    SWITCH_PATIENT("switch_patient", Sickbed.class.getName()),
    //扫码结果
    CODE_TYPE_PATIENT("code_type_patient", ScanResult.class.getName()),
    CODE_TYPE_ORDER("code_type_order", ScanResult.class.getName()),
    CODE_TYPE_EMP_CARD("code_type_emp_card", ScanResult.class.getName()),
    CODE_TYPE_MONITOR("code_type_monitor", ScanResult.class.getName());
    //意图Id
    private String id;
    //代表该意图的Message数据类型
    private String valueType;

    EventIntentionEnum(String id, String valueType) {
        this.id = id;
        this.valueType = valueType;
    }

    public String getId() {
        return id;
    }

    public String getValueType() {
        return valueType;
    }
}
