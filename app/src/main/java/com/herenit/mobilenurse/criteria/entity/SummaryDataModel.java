package com.herenit.mobilenurse.criteria.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by luyudan on 2017/6/15.
 */
public class SummaryDataModel implements Serializable {
    private String docId;//文档id号
    private String dataName;//数据名
    private String dataCode;//数据编码
    private String dataValue;//数据值
    private String dataType;//数据类型
    private String dataUnit;//数据值单位
    //服务器端可识别的格式类型："yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "EEE, dd MMM yyyy HH:mm:ss zzz", "yyyy-MM-dd"))
    private Long dataTime;//数据时间
    private String patientId;//病人id号
    private String visitId;//就诊次数
    private String subId;//子id号
    private String wardCode;//病区代码
    private String recordId;//记录id
    private Integer category;//数据类型
    private String remarks;//关键数据备注
    private Boolean containsTime;//体征项目保存时是否包含时间
    private String docTypeId;//文档类型id号

    public SummaryDataModel() {
        super();
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataUnit() {
        return dataUnit;
    }

    public void setDataUnit(String dataUnit) {
        this.dataUnit = dataUnit;
    }

    public Long getDataTime() {
        return dataTime;
    }

    public void setDataTime(Long dataTime) {
        this.dataTime = dataTime;
    }

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

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getContainsTime() {
        return containsTime;
    }

    public void setContainsTime(Boolean containsTime) {
        this.containsTime = containsTime;
    }

    public String getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(String docTypeId) {
        this.docTypeId = docTypeId;
    }
}
