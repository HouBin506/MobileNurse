package com.herenit.mobilenurse.criteria.entity;

import com.herenit.mobilenurse.criteria.common.NameCode;

/**
 * 医嘱执行单
 */
public class OrderPerformLabel implements NameCode {

    /**
     * 执行单ID
     */
    private String labelId;

    /**
     * 执行单名称
     */
    private String labelName;

    /**
     * 诊疗区域代码
     */
    private String treatAreaCode;

    /**
     * 所属护理单元
     */
    private String wardCode;

    /**
     * 特殊单据标记
     */
    private Integer specialFlag;

    /**
     * 执行单描述
     */
    private String labelMemo;

    /**
     * 创建时间
     */
    private Long createDate;

    /**
     * 可用标志
     */
    private Integer usingFlag;


    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getTreatAreaCode() {
        return treatAreaCode;
    }

    public void setTreatAreaCode(String treatAreaCode) {
        this.treatAreaCode = treatAreaCode;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public Integer getSpecialFlag() {
        return specialFlag;
    }

    public void setSpecialFlag(Integer specialFlag) {
        this.specialFlag = specialFlag;
    }

    public String getLabelMemo() {
        return labelMemo;
    }

    public void setLabelMemo(String labelMemo) {
        this.labelMemo = labelMemo;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Integer getUsingFlag() {
        return usingFlag;
    }

    public void setUsingFlag(Integer usingFlag) {
        this.usingFlag = usingFlag;
    }

    @Override
    public String getName() {
        return labelName;
    }

    @Override
    public String getCode() {
        return labelId;
    }
}
