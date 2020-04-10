package com.herenit.mobilenurse.criteria.entity.submit;

import com.herenit.mobilenurse.criteria.entity.SummaryDataModel;

import java.io.Serializable;
import java.util.List;

/**
 * 评估传参（提交给服务器端的数据）
 * <p>
 * Created by HouBin on 2018/9/28.
 */

public class AssessParam implements Serializable {
    private String docId;//文档id号
    //服务器端可识别的格式类型："yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "EEE, dd MMM yyyy HH:mm:ss zzz", "yyyy-MM-dd"))
    private Long recordTime;//记录时间
    private String userId;
    private String userName;
    private String patientId;
    private String patientName;
    private String visitId;
    private Long visitTime;
    private String wardCode;
    private String wardName;
    private String recordId;//记录id
    private String docTypeId;//文档类型id号
    private String subId;


    private List<SummaryDataModel> docSummaryDataPOJOList;

    public AssessParam() {
        super();
    }

    public Long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Long recordTime) {
        this.recordTime = recordTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public Long getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Long visitTime) {
        this.visitTime = visitTime;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(String docTypeId) {
        this.docTypeId = docTypeId;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public List<SummaryDataModel> getDocSummaryDataPOJOList() {
        return docSummaryDataPOJOList;
    }

    public void setDocSummaryDataPOJOList(List<SummaryDataModel> docSummaryDataPOJOList) {
        this.docSummaryDataPOJOList = docSummaryDataPOJOList;
    }

    public void clear() {
        if (docSummaryDataPOJOList != null)
            docSummaryDataPOJOList.clear();
        docSummaryDataPOJOList = null;
    }
}
