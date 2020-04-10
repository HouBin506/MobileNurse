package com.herenit.mobilenurse.criteria.entity.lab;

import java.io.Serializable;

/**
 * author: HouBin
 * date: 2019/8/27 13:48
 * desc: 检验报告实体类
 */
public class CommonLabReport extends LabReport {

    public CommonLabReport() {
        super();
    }

    /**
     * 检验项目名称
     */
    private String labItemName;


    /**
     * （标本）采集时间
     */
    private Long spcmSampleDateTime;

    /**
     * 标本
     */
    private String specimen;

    /**
     * 结果状态
     */
    private String resultStatusName;

    /**
     * 申请科室
     */
    private String reqDeptName;


    public String getLabItemName() {
        return labItemName;
    }

    public void setLabItemName(String labItemName) {
        this.labItemName = labItemName;
    }

    public Long getSpcmSampleDateTime() {
        return spcmSampleDateTime;
    }

    public void setSpcmSampleDateTime(Long spcmSampleDateTime) {
        this.spcmSampleDateTime = spcmSampleDateTime;
    }


    public String getSpecimen() {
        return specimen;
    }

    public void setSpecimen(String specimen) {
        this.specimen = specimen;
    }

    public String getResultStatusName() {
        return resultStatusName;
    }

    public void setResultStatusName(String resultStatusName) {
        this.resultStatusName = resultStatusName;
    }

    public String getReqDeptName() {
        return reqDeptName;
    }

    public void setReqDeptName(String reqDeptName) {
        this.reqDeptName = reqDeptName;
    }
}
