package com.herenit.mobilenurse.criteria.common;

/**
 * author: HouBin
 * date: 2019/5/17 14:16
 * desc: 普通的常用的Name  Code组合实体类,带有选择功能
 */
public class SelectNameCode extends CommonNameCode {

    private boolean checked;


    public SelectNameCode() {
        super();
    }

    public SelectNameCode(String name, String code, boolean checked) {
        super(name, code);
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
