package com.herenit.mobilenurse.criteria.entity.submit;


/**
 * author: HouBin
 * date: 2019/3/27 14:36
 * desc:医嘱执行提交的实体类
 */
public class OrdersExecute extends BaseSubmit {
    private String recordId;
    private String patientId;
    private String visitNo;
    private String orderId;
    private String processingNurseId;
    private String processingNurseName;
    private Long executeDateTime;
    private Integer executeResult;
    private String verifyNurseId;
    private String verifyNurseName;
    private Long verifyDateTime;
    private Long stopDateTime;
    private String stopNurseId;
    private String stopNurseName;
    private Integer continuedType;
    private Integer doubledSignIndicator;
    private Integer skinTestIndicator;
    private Integer skinTestResult;//1 阳性  2 阴性
    private String skinTestNurseId;
    private String skinTestNurseName;
    private Long skinTestDateTime;
    private String detailNo;//口服药条码号
    private String specialNo;//第三方条码号
    private Integer emergencySign = 0;//急诊表示  0否  1是

    private String verifyLoginName;//核对人的登录名
    private String verifyPassword;//核对人的登录密码

    /**
     * 执行剂量
     */
    private Double executeDosage;

    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Double getExecuteDosage() {
        return executeDosage;
    }

    public void setExecuteDosage(Double executeDosage) {
        this.executeDosage = executeDosage;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProcessingNurseId() {
        return processingNurseId;
    }

    public void setProcessingNurseId(String processingNurseId) {
        this.processingNurseId = processingNurseId;
    }

    public String getProcessingNurseName() {
        return processingNurseName;
    }

    public void setProcessingNurseName(String processingNurseName) {
        this.processingNurseName = processingNurseName;
    }

    public Long getExecuteDateTime() {
        return executeDateTime;
    }

    public void setExecuteDateTime(Long executeDateTime) {
        this.executeDateTime = executeDateTime;
    }

    public Integer getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(Integer executeResult) {
        this.executeResult = executeResult;
    }

    public String getVerifyNurseId() {
        return verifyNurseId;
    }

    public void setVerifyNurseId(String verifyNurseId) {
        this.verifyNurseId = verifyNurseId;
    }

    public String getVerifyNurseName() {
        return verifyNurseName;
    }

    public void setVerifyNurseName(String verifyNurseName) {
        this.verifyNurseName = verifyNurseName;
    }

    public Long getVerifyDateTime() {
        return verifyDateTime;
    }

    public void setVerifyDateTime(Long verifyDateTime) {
        this.verifyDateTime = verifyDateTime;
    }

    public Long getStopDateTime() {
        return stopDateTime;
    }

    public void setStopDateTime(Long stopDateTime) {
        this.stopDateTime = stopDateTime;
    }

    public String getStopNurseId() {
        return stopNurseId;
    }

    public void setStopNurseId(String stopNurseId) {
        this.stopNurseId = stopNurseId;
    }

    public String getStopNurseName() {
        return stopNurseName;
    }

    public void setStopNurseName(String stopNurseName) {
        this.stopNurseName = stopNurseName;
    }

    public Integer getContinuedType() {
        return continuedType;
    }

    public void setContinuedType(Integer continuedType) {
        this.continuedType = continuedType;
    }

    public Integer getDoubledSignIndicator() {
        return doubledSignIndicator;
    }

    public void setDoubledSignIndicator(Integer doubledSignIndicator) {
        this.doubledSignIndicator = doubledSignIndicator;
    }

    public Integer getSkinTestIndicator() {
        return skinTestIndicator;
    }

    public void setSkinTestIndicator(Integer skinTestIndicator) {
        this.skinTestIndicator = skinTestIndicator;
    }

    public Integer getSkinTestResult() {
        return skinTestResult;
    }

    public void setSkinTestResult(Integer skinTestResult) {
        this.skinTestResult = skinTestResult;
    }

    public String getSkinTestNurseId() {
        return skinTestNurseId;
    }

    public void setSkinTestNurseId(String skinTestNurseId) {
        this.skinTestNurseId = skinTestNurseId;
    }

    public String getSkinTestNurseName() {
        return skinTestNurseName;
    }

    public void setSkinTestNurseName(String skinTestNurseName) {
        this.skinTestNurseName = skinTestNurseName;
    }

    public Long getSkinTestDateTime() {
        return skinTestDateTime;
    }

    public void setSkinTestDateTime(Long skinTestDateTime) {
        this.skinTestDateTime = skinTestDateTime;
    }

    public String getDetailNo() {
        return detailNo;
    }

    public void setDetailNo(String detailNo) {
        this.detailNo = detailNo;
    }

    public String getSpecialNo() {
        return specialNo;
    }

    public void setSpecialNo(String specialNo) {
        this.specialNo = specialNo;
    }

    public Integer getEmergencySign() {
        return emergencySign;
    }

    public void setEmergencySign(Integer emergencySign) {
        this.emergencySign = emergencySign;
    }

    public String getVerifyLoginName() {
        return verifyLoginName;
    }

    public void setVerifyLoginName(String verifyLoginName) {
        this.verifyLoginName = verifyLoginName;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }
}
