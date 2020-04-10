package com.herenit.mobilenurse.criteria.enums;

import android.support.annotation.DrawableRes;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;

/**
 * author: HouBin
 * date: 2019/3/28 14:33
 * desc:执行类型枚举，列举了医嘱执行的操作类型
 */
public enum ExecuteClassEnum {
    EXECUTE("001", "执行", R.mipmap.ic_pencil_small),
    PAUSE("002", "暂停", R.mipmap.ic_pause_small),
    FINISH("003", "结束", R.mipmap.ic_finish_small),
    CONTINUE("004", "继续",R.mipmap.ic_continue_small),
    STOP("004", "中断", R.mipmap.ic_stop_small),
    REVOKE("005", "撤回", R.mipmap.ic_revoke_small);

    private String code;
    private String name;
    @DrawableRes
    private int iconResId;

    ExecuteClassEnum(String code, String name, @DrawableRes int iconResId) {
        this.code = code;
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
