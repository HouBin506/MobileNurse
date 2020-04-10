package com.herenit.mobilenurse.criteria.entity.view;

import java.io.Serializable;

/**
 * author: HouBin
 * date: 2019/8/5 10:58
 * desc: 护理巡视页面控制类
 */
public class NurseRoundViewItem implements Serializable {
    private static final long serialVersionUID = -26834761389881453L;

    //层级关系
    private String path;

    /**
     * 当前条目类型，是Group还是Value或者ValueGroup...,
     * Group仅代表组的Item，没有实质的数据意义
     * Value表是该Item是数据操作条目。
     * ValueGroup表示该Item既有数据操作的意义，也算是组
     */
    private String itemType;


    /**
     * 巡视项目ID，巡视项目唯一标识
     */
    private String itemCode;
    /**
     * 巡视项目名称
     */
    private String itemName;
    /**
     * 巡视项目描述
     */
    private String itemDescription;
    /**
     * 是否可编辑，默认为true，可编辑
     */
    private boolean editable;
    /**
     * 巡视项录入的数据的控件类型，有输入框、单选列表等
     */
    private String valueViewType;

    /**
     * 数据控件修饰规则，用于修饰当前控件，方便显示。
     * 比如具备前缀、后缀的输入框，则可以通过“氧流量|L/min”分别表示前缀、后缀
     */
    private String valueViewRule;
    /**
     * 最终从控件取值的规则。比如“※mmHg”,意思就是最后使用Value替换☆作为值，比如Value为108
     * 则最终的值，结果为“108mmHg”
     */
    private String valueDataRule;

    /**
     * 巡视项的值的数据类型，Number、Date、Character等
     */
    private String valueDataType;
    /**
     * 针对列表选择类型的巡视项，需要提供选择的数据列表，使用“|”符号隔开。比如“在位情况”巡视的数据“患者在位|治疗或检查外出|请假外出|擅自外出”
     */
    private String valueDataList;

    /**
     * 服务器端排序使用
     */
    private Integer itemSortNo;

    /*********************************************由客户端操作录入*********************************************************/
    //填写的数值
    private String value;

    /**
     * 选择的录入时间点
     */
    private Long timePoint;
    /**
     * 只有选中的巡视项目，才可以被提交到服务器
     */
    private boolean checked;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getValueViewType() {
        return valueViewType;
    }

    public void setValueViewType(String valueViewType) {
        this.valueViewType = valueViewType;
    }

    public String getValueDataType() {
        return valueDataType;
    }

    public void setValueDataType(String valueDataType) {
        this.valueDataType = valueDataType;
    }


    public String getValueDataList() {
        return valueDataList;
    }

    public void setValueDataList(String valueDataList) {
        this.valueDataList = valueDataList;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(Long timePoint) {
        this.timePoint = timePoint;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getItemSortNo() {
        return itemSortNo;
    }

    public void setItemSortNo(Integer itemSortNo) {
        this.itemSortNo = itemSortNo;
    }

    public String getValueViewRule() {
        return valueViewRule;
    }

    public void setValueViewRule(String valueViewRule) {
        this.valueViewRule = valueViewRule;
    }

    public String getValueDataRule() {
        return valueDataRule;
    }

    public void setValueDataRule(String valueDataRule) {
        this.valueDataRule = valueDataRule;
    }
}
