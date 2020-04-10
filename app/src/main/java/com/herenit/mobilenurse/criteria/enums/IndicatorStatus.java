package com.herenit.mobilenurse.criteria.enums;

import android.support.annotation.ColorRes;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;


/**
 * author: HouBin
 * date: 2019/4/18 11:13
 * desc:指示牌、指示灯等一些指示意义的状态，比如正常、警告、错误
 */
public enum IndicatorStatus {
    NORMAL(1, "正常"),
    WARNING(2, "警告"),
    ERROR(3, "错误");


    private int code;
    private String name;

    IndicatorStatus(int code, String name) {
        this.name = name;
        this.code = code;
    }
}
