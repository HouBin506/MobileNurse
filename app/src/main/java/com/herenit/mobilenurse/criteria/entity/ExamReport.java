package com.herenit.mobilenurse.criteria.entity;

import java.io.Serializable;

/**
 * author: HouBin
 * date: 2019/8/27 13:48
 * desc: 检查报告实体类
 */
public class ExamReport implements Serializable {

    private static final long serialVersionUID = -6799070217141321707L;

    /**
     * 检查单号
     */
    private String examNo;

    /**
     * 检查类别
     */
    private String examClass;

    /**
     * 检查类别名称，暂定为显示检查子类（exam_sub_class）名称
     */
    private String examSubClass;

    /**
     * 检查申请时间
     */
    private Long reqDateTime;
    /**
     * 检查结果状态
     */
    private String resultStatusName;
    /**
     * 预约时间
     */
    private Long scheduledDateTime;
    /**
     * 检查执行科室
     */
    private String performDeptName;
    /**
     * 检查项目（项目描述）
     */
    private String examItemName;

    public ExamReport() {
    }

    public String getExamNo() {
        return examNo;
    }

    public void setExamNo(String examNo) {
        this.examNo = examNo;
    }

    public String getExamClass() {
        return examClass;
    }

    public void setExamClass(String examClass) {
        this.examClass = examClass;
    }

    public String getExamSubClass() {
        return examSubClass;
    }

    public void setExamSubClass(String examSubClass) {
        this.examSubClass = examSubClass;
    }

    public Long getReqDateTime() {
        return reqDateTime;
    }

    public void setReqDateTime(Long reqDateTime) {
        this.reqDateTime = reqDateTime;
    }

    public String getResultStatusName() {
        return resultStatusName;
    }

    public void setResultStatusName(String resultStatusName) {
        this.resultStatusName = resultStatusName;
    }

    public Long getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(Long scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public String getPerformDeptName() {
        return performDeptName;
    }

    public void setPerformDeptName(String performDeptName) {
        this.performDeptName = performDeptName;
    }

    public String getExamItemName() {
        return examItemName;
    }

    public void setExamItemName(String examItemName) {
        this.examItemName = examItemName;
    }
}
