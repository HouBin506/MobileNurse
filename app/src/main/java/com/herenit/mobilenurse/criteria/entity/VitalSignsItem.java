package com.herenit.mobilenurse.criteria.entity;


import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewItem;

import java.io.Serializable;
import java.util.Comparator;

/**
 * author: HouBin
 * date: 2019/5/16 10:10
 * desc: 体征项目实体类，用于获取体征数据，提交体征数据
 */
public class VitalSignsItem implements Serializable {
    private static final long serialVersionUID = 4085516797587415825L;
    /**
     * 患者ID
     */
    private String patientId;
    /**
     * 就诊唯一标识
     */
    private String visitId;
    /**
     * 病区、科室代码。针对某些医院，不同病区的体征项不同
     */
    private String wardCode;
    /**
     * 体征录入的记录日期
     */
    private Long recordingDate;
    /**
     * 录入者ID
     */
    private String nurse;

    /**
     * 体征项目ID，此ID一般为数据库中体征项的唯一标识，为了不同的HIS适配，服务器端也可以从中修改
     */
    private String itemCode;
    /**
     * 针对某些需要联合主键定位某项体征
     */
    private String classCode;
    /**
     * 体征项目名称
     */
    private String itemName;
    /**
     * 体征数据值
     */
    private String itemValue;

    /**
     * 修改之后新的体征数据值
     */
    private String newItemValue;
    /**
     * 例外值（特殊值）
     */
    private String specialValue;
    /**
     * 备注信息
     */
    private String memo;

    /**
     * 体征录入的时间点
     */
    private Long timePoint;

    /**
     * 对于某项体征的修改，可能会修改录入时间
     */
    private Long newTimePoint;
    /**
     * 单位
     */
    private String unit;
    /**
     * 可输入值范围上限
     */
    private String inputUpperLimit;
    /**
     * 可输入值范围下限
     */
    private String inputLowerLimit;
    /**
     * 正常值范围上限
     */
    private String upperLimitValue;
    /**
     * 正常值范围下限
     */
    private String lowerLimitValue;

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

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public Long getRecordingDate() {
        return recordingDate;
    }

    public void setRecordingDate(Long recordingDate) {
        this.recordingDate = recordingDate;
    }

    public String getNurse() {
        return nurse;
    }

    public void setNurse(String nurse) {
        this.nurse = nurse;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }


    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getSpecialValue() {
        return specialValue;
    }

    public void setSpecialValue(String specialValue) {
        this.specialValue = specialValue;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public Long getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(Long timePoint) {
        this.timePoint = timePoint;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getInputUpperLimit() {
        return inputUpperLimit;
    }

    public void setInputUpperLimit(String inputUpperLimit) {
        this.inputUpperLimit = inputUpperLimit;
    }

    public String getInputLowerLimit() {
        return inputLowerLimit;
    }

    public void setInputLowerLimit(String inputLowerLimit) {
        this.inputLowerLimit = inputLowerLimit;
    }

    public String getUpperLimitValue() {
        return upperLimitValue;
    }

    public void setUpperLimitValue(String upperLimitValue) {
        this.upperLimitValue = upperLimitValue;
    }

    public String getLowerLimitValue() {
        return lowerLimitValue;
    }

    public void setLowerLimitValue(String lowerLimitValue) {
        this.lowerLimitValue = lowerLimitValue;
    }

    public Long getNewTimePoint() {
        return newTimePoint;
    }

    public void setNewTimePoint(Long newTimePoint) {
        this.newTimePoint = newTimePoint;
    }

    public String getNewItemValue() {
        return newItemValue;
    }

    public void setNewItemValue(String newItemValue) {
        this.newItemValue = newItemValue;
    }

}
