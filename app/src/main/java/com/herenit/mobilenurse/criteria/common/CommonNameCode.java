package com.herenit.mobilenurse.criteria.common;

import java.io.Serializable;

/**
 * author: HouBin
 * date: 2019/5/17 15:16
 * desc:
 */
public class CommonNameCode implements NameCode, Serializable {
    private static final long serialVersionUID = -8969775193630014189L;
    protected String name;
    protected String code;

    public CommonNameCode() {
    }

    public CommonNameCode(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

}
