package com.herenit.mobilenurse.criteria.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * author: HouBin
 * date: 2019/5/22 16:44
 * desc:手术安排信息
 */
public class OperationScheduled implements Serializable {
    private static final long serialVersionUID = 7644020427248310931L;
    //"operationSequence": null,  台次
    private String operationSequence;
    //"secondSupplyNurse": null, 供应护士2
    private String secondSupplyNurse;
    //"operatingScale": "小",     手术等级
    private String operatingScale;
    //"secondOperationNurse": null,台上护士2
    private String secondOperationNurse;
    //"orderId": "1600003741",
    private String orderId;//医嘱号、申请单号
    //"patientId": "P101102",
    private String patientId;
    //"operationRoomNo": null, 手术间
    private String operationRoomNo;
    //"scheduledDateTime": “2016-11-23”,  手术时间
    private Long scheduledDateTime;
    //"enteredBy": "张庆生",   申请医师
    private String enteredBy;
    //"threeAssistant": null, 第三助手
    private String threeAssistant;
    //"anaesthesiaMethod": "局部麻醉", 麻醉方式
    private String anaesthesiaMethod;
    //"operatorDoctor": "阚绍华",   手术医师
    private String operatorDoctor;
    //"operatorDoctorId": "2504", 手术医师ID
    private String operatorDoctorId;
    //"firstSupplyNurse": null, 供应护士1
    private String firstSupplyNurse;
    //"anesthesiaDoctor": null,    麻醉医师
    private String anesthesiaDoctor;
    //"diagBeforeOperation": "安宁中毒",  诊断
    private String diagBeforeOperation;
    //"bloodDoctor": null,            输血医师
    private String bloodDoctor;
    //"sex": "女",
    private String sex;
    //"firstAssistant": null, 第一助手
    private String firstAssistant;
    //"dateOfBirth": 977241600000,出生日期
    private String dateOfBirth;
    //"operationName": "[测试手术]",  手术内容
    private String operationName;
    //"deptStayedName": "呼吸内科病区", 手术科室、病区
    private String deptStayedName;
    //"secondAssistant": null, 第二助手
    private String secondAssistant;
    //"ackIndicator": "未确认",     手术状态
    private String ackIndicator;
    //"bedNo": null,
    private String bedNo;
    //"inpNo": "I000287",住院号
    private String inpNo;
    //"name": "洛熙玥",         姓名
    private String name;
    //"fourAssistant": null,第四助手
    private String fourAssistant;
    //"firstOperationNurse": null,台上护士1
    private String firstOperationNurse;
    //"bedLabel": "1",           床号
    private String bedLabel;
    //"age": "15岁",
    private String age;
    //"operatingRoomName": "手术室",  手术室
    private String operatingRoomName;
    //“operationNurse”:null,            台上护士
    private String operationNurse;
    //“assistantNurse”:null,             手术助手
    private String assistantNurse;
    //“supplyNurse”:null               供应护士
    private String supplyNurse;
    //“emergencyIndicator”：“急诊”   急诊标识
    private String emergencyIndicator;

    public String getOperationSequence() {
        return operationSequence;
    }

    public void setOperationSequence(String operationSequence) {
        this.operationSequence = operationSequence;
    }

    public String getSecondSupplyNurse() {
        return secondSupplyNurse;
    }

    public void setSecondSupplyNurse(String secondSupplyNurse) {
        this.secondSupplyNurse = secondSupplyNurse;
    }

    public String getOperatingScale() {
        return operatingScale;
    }

    public void setOperatingScale(String operatingScale) {
        this.operatingScale = operatingScale;
    }

    public String getSecondOperationNurse() {
        return secondOperationNurse;
    }

    public void setSecondOperationNurse(String secondOperationNurse) {
        this.secondOperationNurse = secondOperationNurse;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getOperationRoomNo() {
        return operationRoomNo;
    }

    public void setOperationRoomNo(String operationRoomNo) {
        this.operationRoomNo = operationRoomNo;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(Long scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public String getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        this.enteredBy = enteredBy;
    }

    public String getThreeAssistant() {
        return threeAssistant;
    }

    public void setThreeAssistant(String threeAssistant) {
        this.threeAssistant = threeAssistant;
    }

    public String getAnaesthesiaMethod() {
        return anaesthesiaMethod;
    }

    public void setAnaesthesiaMethod(String anaesthesiaMethod) {
        this.anaesthesiaMethod = anaesthesiaMethod;
    }

    public String getOperatorDoctor() {
        return operatorDoctor;
    }

    public void setOperatorDoctor(String operatorDoctor) {
        this.operatorDoctor = operatorDoctor;
    }

    public String getOperatorDoctorId() {
        return operatorDoctorId;
    }

    public void setOperatorDoctorId(String operatorDoctorId) {
        this.operatorDoctorId = operatorDoctorId;
    }

    public String getFirstSupplyNurse() {
        return firstSupplyNurse;
    }

    public void setFirstSupplyNurse(String firstSupplyNurse) {
        this.firstSupplyNurse = firstSupplyNurse;
    }

    public String getAnesthesiaDoctor() {
        return anesthesiaDoctor;
    }

    public void setAnesthesiaDoctor(String anesthesiaDoctor) {
        this.anesthesiaDoctor = anesthesiaDoctor;
    }

    public String getDiagBeforeOperation() {
        return diagBeforeOperation;
    }

    public void setDiagBeforeOperation(String diagBeforeOperation) {
        this.diagBeforeOperation = diagBeforeOperation;
    }

    public String getBloodDoctor() {
        return bloodDoctor;
    }

    public void setBloodDoctor(String bloodDoctor) {
        this.bloodDoctor = bloodDoctor;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFirstAssistant() {
        return firstAssistant;
    }

    public void setFirstAssistant(String firstAssistant) {
        this.firstAssistant = firstAssistant;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getDeptStayedName() {
        return deptStayedName;
    }

    public void setDeptStayedName(String deptStayedName) {
        this.deptStayedName = deptStayedName;
    }

    public String getSecondAssistant() {
        return secondAssistant;
    }

    public void setSecondAssistant(String secondAssistant) {
        this.secondAssistant = secondAssistant;
    }

    public String getAckIndicator() {
        return ackIndicator;
    }

    public void setAckIndicator(String ackIndicator) {
        this.ackIndicator = ackIndicator;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getInpNo() {
        return inpNo;
    }

    public void setInpNo(String inpNo) {
        this.inpNo = inpNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFourAssistant() {
        return fourAssistant;
    }

    public void setFourAssistant(String fourAssistant) {
        this.fourAssistant = fourAssistant;
    }

    public String getFirstOperationNurse() {
        return firstOperationNurse;
    }

    public void setFirstOperationNurse(String firstOperationNurse) {
        this.firstOperationNurse = firstOperationNurse;
    }

    public String getBedLabel() {
        return bedLabel;
    }

    public void setBedLabel(String bedLabel) {
        this.bedLabel = bedLabel;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getOperatingRoomName() {
        return operatingRoomName;
    }

    public void setOperatingRoomName(String operatingRoomName) {
        this.operatingRoomName = operatingRoomName;
    }

    public String getOperationNurse() {
        return operationNurse;
    }

    public void setOperationNurse(String operationNurse) {
        this.operationNurse = operationNurse;
    }

    public String getAssistantNurse() {
        return assistantNurse;
    }

    public void setAssistantNurse(String assistantNurse) {
        this.assistantNurse = assistantNurse;
    }

    public String getSupplyNurse() {
        return supplyNurse;
    }

    public void setSupplyNurse(String supplyNurse) {
        this.supplyNurse = supplyNurse;
    }

    public String getEmergencyIndicator() {
        return emergencyIndicator;
    }

    public void setEmergencyIndicator(String emergencyIndicator) {
        this.emergencyIndicator = emergencyIndicator;
    }

    /**
     * 是否为紧急手术
     *
     * @return
     */
    public boolean isEmergency() {
        if (TextUtils.isEmpty(emergencyIndicator))
            return false;
        return emergencyIndicator.equals("紧急");
    }

    /**
     * 是否未确认
     *
     * @return
     */
    public boolean isUnconfirmed() {
        if (TextUtils.isEmpty(ackIndicator))
            return false;
        return ackIndicator.equals("未确认");
    }
}
