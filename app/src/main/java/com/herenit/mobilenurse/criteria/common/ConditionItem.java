package com.herenit.mobilenurse.criteria.common;

/**
 * 筛选条件ITEM
 * Created by HouBin on 2017/12/1.
 */

public class ConditionItem {
    private String id;//条件ID
    private String name;//条件名
    private boolean isAll;//该条件是否表示该行全选
    private boolean isChecked;//该条件是否被选中
    private String format;//格式，当条件比较特殊，比如name为“Calendar”日历时，需要通过format来说明日期的格式

    public ConditionItem() {
    }

    public ConditionItem(String id, String name, boolean isAll) {
        this.id = id;
        this.name = name;
        this.isAll = isAll;
    }


    public ConditionItem(String id, String name, boolean isAll, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.isAll = isAll;
        this.isChecked = isChecked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}