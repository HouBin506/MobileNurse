package com.herenit.mobilenurse.criteria.entity;

/**
 * author: HouBin
 * date: 2019/8/9 14:28
 * desc: 护理巡视与服务器端交互类（数据提交、数据获取）
 */
public class NurseRoundItem {

    /**
     * 患者ID
     */
    private String patientId;
    /**
     * 就诊唯一标识
     */
    private String visitId;

    //层级关系
    private String path;

    /**
     * 巡视项目ID，巡视项目唯一标识
     */
    private String itemCode;
    /**
     * 巡视项目名称
     */
    private String itemName;
    /**
     * 巡视项目描述，可以存储输液医嘱内容
     */
    private String itemDescription;

    //填写的数值
    private String itemValue;

    /**
     * 选择的录入时间点
     */
    private Long timePoint;

    /**
     * 服务器端排序使用
     */
    private Integer itemSortNo;

    /**
     * 录入者ID
     */
    private String nurse;

    /**
     * 当前条目类型，是Group还是Value或者ValueGroup...,
     * Group仅代表组的Item，没有实质的数据意义
     * Value表是该Item是数据操作条目。
     * ValueGroup表示该Item既有数据操作的意义，也算是组
     */
    private String itemType;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public Long getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(Long timePoint) {
        this.timePoint = timePoint;
    }

    public Integer getItemSortNo() {
        return itemSortNo;
    }

    public void setItemSortNo(Integer itemSortNo) {
        this.itemSortNo = itemSortNo;
    }

    public String getNurse() {
        return nurse;
    }

    public void setNurse(String nurse) {
        this.nurse = nurse;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
