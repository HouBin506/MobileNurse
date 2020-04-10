package com.herenit.mobilenurse.criteria.entity.lab;

import java.io.Serializable;

/**
 * author: HouBin
 * date: 2019/9/6 15:55
 * desc:微生物检验实体类
 */
public class MicroorganismLabReport extends LabReport {

    public MicroorganismLabReport() {
        super();
    }

    //检验项目名称（微生物标本）
    private String specimen;


    //结果类型
    private String resultType;

    //细菌名称
    private String bioName;

    //检验子项序号
    private String labItemNo;

    //耐药类型
    private String spectrum;

    //申请医生名称
    private String reqPhysicianName;


    //执行时间
    private Long executeDateTime;

    public String getSpecimen() {
        return specimen;
    }

    public void setSpecimen(String specimen) {
        this.specimen = specimen;
    }


    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getBioName() {
        return bioName;
    }

    public void setBioName(String bioName) {
        this.bioName = bioName;
    }

    public String getLabItemNo() {
        return labItemNo;
    }

    public void setLabItemNo(String labItemNo) {
        this.labItemNo = labItemNo;
    }

    public String getSpectrum() {
        return spectrum;
    }

    public void setSpectrum(String spectrum) {
        this.spectrum = spectrum;
    }

    public String getReqPhysicianName() {
        return reqPhysicianName;
    }

    public void setReqPhysicianName(String reqPhysicianName) {
        this.reqPhysicianName = reqPhysicianName;
    }


    public Long getExecuteDateTime() {
        return executeDateTime;
    }

    public void setExecuteDateTime(Long executeDateTime) {
        this.executeDateTime = executeDateTime;
    }
}
