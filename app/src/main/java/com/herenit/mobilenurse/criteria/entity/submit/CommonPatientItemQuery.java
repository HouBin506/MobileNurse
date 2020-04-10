package com.herenit.mobilenurse.criteria.entity.submit;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/8/12 15:58
 * desc:常用的单患者查询条件，用来提交服务器端的条件。
 */
public class CommonPatientItemQuery extends TimeIntervalQuery {
    private String patientId;
    private String visitId;
    private List<String> itemList;

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

    public List<String> getItemList() {
        return itemList;
    }

    public void setItemList(List<String> itemList) {
        this.itemList = itemList;
    }
}
