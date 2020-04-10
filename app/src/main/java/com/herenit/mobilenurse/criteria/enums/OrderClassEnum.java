package com.herenit.mobilenurse.criteria.enums;

/**
 * author: HouBin
 * date: 2019/3/13 15:15
 * desc:医嘱类别枚举
 */
public enum OrderClassEnum {
    MEDICATION("A","药疗"),CHUZHI("E","处置"),EXAM("D","检查"),LAB("C","检验"),
    OPERATION("F","手术"),NURSE("H","护理"),DIET("I","膳食"),OTHER("Z","其它");

    private String code;
    private String name;

    OrderClassEnum(String code,String name){
        this.code = code;
        this.name = name;
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
}
