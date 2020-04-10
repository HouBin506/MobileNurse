package com.herenit.mobilenurse.criteria.entity;

import com.herenit.mobilenurse.criteria.entity.submit.BaseSubmit;

/**
 * 监护仪绑定实体类
 */
public class MonitorBind extends BaseSubmit {

    /**
     * 监护仪码
     */
    private String monitorCode;

    /**
     * 患者Id
     */
    private String patientId;
    /**
     * 住院流水号
     */
    private String visitNo;

    /**
     * 操作人
     */
    private String operatorId;
    /**
     * 操作人
     */
    private String operatorName;

    /**
     * 操作时间
     */
    private Long operatingDateTime;

    /**
     * 绑定状态  1 绑定，2 解绑   0 未绑定（自用）
     */
    private Integer bindStatus;

    /**
     * 备注
     */
    private String memo;

    /**
     * 当前要操作的监护仪，与之绑定的患者Id。
     * 调用核对接口时服务器端返回给客户端，客户端点击确定调用操作接口时，将该值回传给服务器端
     */
    private String monitorVsPatientId;
    /**
     * 当前要操作的监护仪，与之绑定的患者住院流水号
     * 调用核对接口时服务器端返回给客户端，客户端点击确定调用操作接口时，将该值回传给服务器端
     */
    private String monitorVsVisitNo;

    /**
     * 当前要操作的患者，与之绑定的监护仪码
     * 调用核对接口时服务器端返回给客户端，客户端点击确定调用操作接口时，将该值回传给服务器端
     */
    private String patientVsMonitorCode;

    /**
     * 在操作之前，服务器端核对患者和监护仪之后，返回给客户端的提示信息，用于客户端弹窗显示。
     * 我们分别对四种情况，拟定一个提示消息模板
     * 1，当前要操作的患者与当前要操作的监护仪均为清白之身（未绑定），monitorVsPatientId为空，patientVsMonitorCode为空
     * 此时提示绑定：“确认将当前患者（02床 周某）与当前监护仪（65485）进行绑定？“
     * 2，当前要操作的患者与当前要操作的监护仪绑定，monitorVsPatientId与patientVsMonitorCode均不为空，monitorVsPatientId与patientId相同，monitorVsVisitNo与visitNo相同，patientVsMonitorCode与monitorCode相同
     * 此时提示解绑：“确认将当前患者（02床 周某）与当前监护仪（65485）解除绑定关系？“
     * 3，当前要操作的患者与别的监护仪具有绑定关系，patientVsMonitorCode不为空，patientVsMonitorCode与monitorCode不相同，monitorVsPatientId为空
     * 此时提示先解绑后绑定：“当前患者（02床 周某）与另一监护仪（44444）绑定，是否确认先解绑，再与当前监护仪（65485）进行绑定？”
     * 4，当前要操作的监护仪与别的患者具有绑定关系，monitorVsPatientId不为空，monitorVsPatientId与patientId不相同，patientVsMonitorCode为空
     * 此时提示先解绑后绑定：“当前监护仪（65485）与另一患者（05床 孙某）绑定，是否确认先解绑，再与当前患者（02床 周某）进行绑定？”
     * 5，当前要操作的患者与另一监护仪绑定，当前要操作的监护仪与另一患者绑定，monitorVsPatientId与patientVsMonitorCode均不为空，monitorVsPatientId与patientId不相同，patientVsMonitorCode与monitorCode不相同
     * 此时提示分别各自解绑后再绑定：“当前患者（02床 周某）与另一监护仪（44444）绑定，且当前监护仪（65485）与另一患者（05床 孙某）绑定。是否确认先分别解绑，再将当前患者（02床 周某）与当前监护仪（65485）建立绑定？”
     */
    private String verifyMessage;

    /**
     * 操作类别，
     * 1 将当前患者与当前监护仪绑定；
     * 2 将当患者与当前监护仪解绑；
     * 3 将当前患者与另一监护仪解绑，再与当前监护仪绑定；
     * 4 将当前监护仪与另一患者解绑，再与当前患者绑定；
     * 5 将当前患者与当前监护仪，各自分别解绑，然后再相互绑定
     */
    private Integer operationType;

    public MonitorBind() {
    }

    public String getMonitorCode() {
        return monitorCode;
    }

    public void setMonitorCode(String monitorCode) {
        this.monitorCode = monitorCode;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getVisitNo() {
        return visitNo;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getOperatingDateTime() {
        return operatingDateTime;
    }

    public void setOperatingDateTime(Long operatingDateTime) {
        this.operatingDateTime = operatingDateTime;
    }

    public Integer getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(Integer bindStatus) {
        this.bindStatus = bindStatus;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMonitorVsPatientId() {
        return monitorVsPatientId;
    }

    public void setMonitorVsPatientId(String monitorVsPatientId) {
        this.monitorVsPatientId = monitorVsPatientId;
    }

    public String getMonitorVsVisitNo() {
        return monitorVsVisitNo;
    }

    public void setMonitorVsVisitNo(String monitorVsVisitNo) {
        this.monitorVsVisitNo = monitorVsVisitNo;
    }

    public String getPatientVsMonitorCode() {
        return patientVsMonitorCode;
    }

    public void setPatientVsMonitorCode(String patientVsMonitorCode) {
        this.patientVsMonitorCode = patientVsMonitorCode;
    }

    public String getVerifyMessage() {
        return verifyMessage;
    }

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public MonitorBind copy() {
        MonitorBind monitor = new MonitorBind();
        monitor.setMonitorCode(monitorCode);
        monitor.setPatientId(patientId);
        monitor.setVisitNo(visitNo);
        monitor.setOperatorId(operatorId);
        monitor.setOperatorName(operatorName);
        monitor.setOperatingDateTime(operatingDateTime);
        monitor.setBindStatus(bindStatus);
        monitor.setMemo(memo);
        monitor.setMonitorVsPatientId(monitorVsPatientId);
        monitor.setMonitorVsVisitNo(monitorVsVisitNo);
        monitor.setPatientVsMonitorCode(patientVsMonitorCode);
        monitor.setVerifyMessage(verifyMessage);
        monitor.setOperationType(operationType);
        return monitor;
    }
}
