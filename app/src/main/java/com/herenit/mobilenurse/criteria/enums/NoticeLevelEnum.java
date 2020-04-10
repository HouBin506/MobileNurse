package com.herenit.mobilenurse.criteria.enums;

/**
 * author: HouBin
 * date: 2019/1/11 9:48
 * desc: 通知级别，普通、警告、错误
 */
public enum NoticeLevelEnum {

    NORMAL("正常提示"),

    WARNING("警告提示"),

    ERROR("错误提示");

    private String name;

    private NoticeLevelEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
