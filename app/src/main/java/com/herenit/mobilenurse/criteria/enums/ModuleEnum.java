package com.herenit.mobilenurse.criteria.enums;


import android.support.annotation.DrawableRes;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;

/**
 * author: HouBin
 * date: 2019/2/14 11:17
 * desc: 业务相关模块的枚举，定义了每个模块的Id
 * 其中，id与服务器端返回的要显示模块id一致，用于对比判断显示
 */
public enum ModuleEnum {
    //小工具
    TOOL_CALCULATOR("计算器", "2001", R.mipmap.ic_tool_calculator),
    TOOL_STOPWATCH("秒表", "2002", R.mipmap.ic_tool_stopwatch),

    /***********************主功能***********************/
    SICKBED_LIST("0001", R.mipmap.module_sickbed),//床位列表
    ORDERS("0002", R.mipmap.module_orders),//医嘱
    VITAL_SIGNS("0003", R.mipmap.module_vital_signs),//体征
    ADMISSION_ASSESSMENT("0004", R.mipmap.module_assessment),//入院评估
    DRUG_CHECK("0005", R.mipmap.module_drug),//药品核对
    OPERATION_SCHEDULED("0006", R.mipmap.module_operation),//手术安排
    PATIENT_INFO("0007", R.mipmap.module_sickbed),//患者详情
    NURSE_ROUND("0008", R.mipmap.module_intervention),//护理巡视
    HEALTH_EDUCATION("0009", R.mipmap.module_drug),//健康宣教
    EXAM_REPORT("0010", R.mipmap.module_examin),//检查报告
    LAB_REPORT("0011", R.mipmap.module_lab),//检验报告
    MONITOR("0012", R.mipmap.module_monitor),//监护仪
    OTHER("1111", R.mipmap.module_other);//其它
    private String id;
    private int iconRes;
    private String itemName;

    ModuleEnum(String id, @DrawableRes int iconRes) {
        this.id = id;
        this.iconRes = iconRes;
    }

    ModuleEnum(String name, String id, @DrawableRes int iconRes) {
        this.itemName = name;
        this.id = id;
        this.iconRes = iconRes;
    }

    public String id() {
        return this.id;
    }

    public int iconRes() {
        return this.iconRes;
    }

    public String itemName() {
        return itemName;
    }
}
