package com.herenit.mobilenurse.criteria.entity.lab;

import java.io.Serializable;

/**
 * author: HouBin
 * date: 2019/9/6 17:23
 * desc:检验实体基类
 */
public class LabReport implements Serializable {
    private static final long serialVersionUID = 6015367945564851038L;
    /**
     * 检验单号
     */
    private String labNo;

    /**
     * 申请时间
     */
    private Long reqDateTime;

    public String getLabNo() {
        return labNo;
    }

    public void setLabNo(String labNo) {
        this.labNo = labNo;
    }

    public Long getReqDateTime() {
        return reqDateTime;
    }

    public void setReqDateTime(Long reqDateTime) {
        this.reqDateTime = reqDateTime;
    }
}
