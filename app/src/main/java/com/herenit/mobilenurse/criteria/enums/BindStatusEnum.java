package com.herenit.mobilenurse.criteria.enums;


import android.support.annotation.DrawableRes;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;

/**
 * author: HouBin
 * date: 2019/2/14 11:17
 * desc: 绑定状态枚举类
 */
public enum BindStatusEnum {
    //小工具
    BINDING("已绑定", 1),
    UNBIND("已解绑", 2),
    NOT_BOUND("未绑定", 0);

    private int bindStatusCode;
    private String bindStatusName;

    BindStatusEnum(String name, int code) {
        this.bindStatusName = name;
        this.bindStatusCode = code;
    }

    public int getBindStatusCode() {
        return bindStatusCode;
    }

    public String getBindStatusName() {
        return bindStatusName;
    }
}
