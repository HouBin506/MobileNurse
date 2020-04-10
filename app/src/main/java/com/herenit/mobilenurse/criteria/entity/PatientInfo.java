package com.herenit.mobilenurse.criteria.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;

/**
 * author: HouBin
 * long: 2019/2/18 17:01
 * desc: 患者实体类
 */
@Entity(indexes = {@Index(value = "bedNo DESC,patientId DESC,visitId DESC", unique = true)})
public class PatientInfo implements Serializable {

    /**
     * patientCondition : null
     * fastingSign : null
     * isolationSign : null
     * babyNo : null
     * wardCode : 20030202
     * wardName : 产科病区
     * deptCode : 7001
     * deptName : 产科护理单元
     * inpNo : 471189
     * visitTime : 1528446434000
     * admWardTime : 1528446614000
     * admissionDateCount : 269
     * visitNo : null
     * idNo : 62292519850405402X
     * patientClass : 1
     * patientIdentity : 一般人员
     * phoneNumber : null
     * nation : 汉族
     * birthTime : 473356800000
     * birthPlaceCode : null
     * birthPlaceName : null
     * presentAddressCode : null
     * presentAddressName : null
     * maritalStatus : null
     * nextOfKin : null
     * nextOfKinPhone : null
     * allergenList : null
     * byNurseName : null
     * chiefDoctorId : null
     * chiefDoctor : null
     * chargerDoctorId : 000639
     * chargerDoctorName : 马翠莉
     * attendingDoctorId : null
     * attendingDoctor : null
     * chargeType : 农合
     * prepayments : 4000.0
     * totalCosts : 2451.5
     * operatingDataCount : 0
     */


    public static final long serialVersionUID = 666666L;
    @Id
    private Long id;

    @NotNull
    private String deptCode;
    /**
     * 床号
     */
    private int bedNo;
    /**
     * 床标号（床上贴的那个号，就是“几床几床”）
     */
    private String bedLabel;
    /**
     * 病床状态（占用、锁床、未开展等一些状态）
     */
    private String bedStatus;

    /**
     * 母婴标识
     */
    private String babyNo;

    /**
     * 患者Id
     */
    private String patientId;
    /**
     * 住院标识（住院次数或者住院流水号）
     */
    private String visitId;
    /**
     * 患者姓名
     */
    private String patientName;
    /**
     * 患者年龄
     */
    private String patientAge;
    /**
     * 性别
     */
    private String patientSex;
    /**
     * 病情等级
     */
    private String patientCondition;
    /**
     * 诊断
     */
    private String diagnosis;
    /**
     * 是否禁食
     */
    private String fastingSign;
    /**
     * 是否隔离
     */
    private String isolationSign;
    /**
     * 护理等级
     */

    private String nursingClass;

    private String wardCode;
    private String wardName;
    private String deptName;
    private String inpNo;
    private long visitTime;
    private long admWardTime;
    private int admissionDateCount;
    private String visitNo;
    private String idNo;
    private String patientClass;
    private String patientIdentity;
    private String phoneNumber;
    private String nation;
    private long birthTime;
    private String birthPlaceCode;
    private String birthPlaceName;
    private String presentAddressCode;
    private String presentAddressName;
    private String maritalStatus;
    private String nextOfKin;
    private String nextOfKinPhone;
    private String allergenList;
    private String byNurseName;
    private String chiefDoctorId;
    private String chiefDoctor;
    private String chargerDoctorId;
    private String chargerDoctorName;
    private String attendingDoctorId;
    private String attendingDoctor;
    private String chargeType;
    private String zipCode;
    private double prepayments;
    private double totalCosts;
    private int operatingDataCount;
    /**
     * 贫困标识，贫困类型患者，该字段不为空。“建卡立档”
     */
    private String poor;








    @Generated(hash = 1485785931)
    public PatientInfo(Long id, @NotNull String deptCode, int bedNo, String bedLabel,
            String bedStatus, String babyNo, String patientId, String visitId,
            String patientName, String patientAge, String patientSex, String patientCondition,
            String diagnosis, String fastingSign, String isolationSign, String nursingClass,
            String wardCode, String wardName, String deptName, String inpNo, long visitTime,
            long admWardTime, int admissionDateCount, String visitNo, String idNo,
            String patientClass, String patientIdentity, String phoneNumber, String nation,
            long birthTime, String birthPlaceCode, String birthPlaceName,
            String presentAddressCode, String presentAddressName, String maritalStatus,
            String nextOfKin, String nextOfKinPhone, String allergenList, String byNurseName,
            String chiefDoctorId, String chiefDoctor, String chargerDoctorId,
            String chargerDoctorName, String attendingDoctorId, String attendingDoctor,
            String chargeType, String zipCode, double prepayments, double totalCosts,
            int operatingDataCount, String poor) {
        this.id = id;
        this.deptCode = deptCode;
        this.bedNo = bedNo;
        this.bedLabel = bedLabel;
        this.bedStatus = bedStatus;
        this.babyNo = babyNo;
        this.patientId = patientId;
        this.visitId = visitId;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientSex = patientSex;
        this.patientCondition = patientCondition;
        this.diagnosis = diagnosis;
        this.fastingSign = fastingSign;
        this.isolationSign = isolationSign;
        this.nursingClass = nursingClass;
        this.wardCode = wardCode;
        this.wardName = wardName;
        this.deptName = deptName;
        this.inpNo = inpNo;
        this.visitTime = visitTime;
        this.admWardTime = admWardTime;
        this.admissionDateCount = admissionDateCount;
        this.visitNo = visitNo;
        this.idNo = idNo;
        this.patientClass = patientClass;
        this.patientIdentity = patientIdentity;
        this.phoneNumber = phoneNumber;
        this.nation = nation;
        this.birthTime = birthTime;
        this.birthPlaceCode = birthPlaceCode;
        this.birthPlaceName = birthPlaceName;
        this.presentAddressCode = presentAddressCode;
        this.presentAddressName = presentAddressName;
        this.maritalStatus = maritalStatus;
        this.nextOfKin = nextOfKin;
        this.nextOfKinPhone = nextOfKinPhone;
        this.allergenList = allergenList;
        this.byNurseName = byNurseName;
        this.chiefDoctorId = chiefDoctorId;
        this.chiefDoctor = chiefDoctor;
        this.chargerDoctorId = chargerDoctorId;
        this.chargerDoctorName = chargerDoctorName;
        this.attendingDoctorId = attendingDoctorId;
        this.attendingDoctor = attendingDoctor;
        this.chargeType = chargeType;
        this.zipCode = zipCode;
        this.prepayments = prepayments;
        this.totalCosts = totalCosts;
        this.operatingDataCount = operatingDataCount;
        this.poor = poor;
    }

    @Generated(hash = 1642239073)
    public PatientInfo() {
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getInpNo() {
        return inpNo;
    }

    public void setInpNo(String inpNo) {
        this.inpNo = inpNo;
    }

    public long getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(long visitTime) {
        this.visitTime = visitTime;
    }

    public long getAdmWardTime() {
        return admWardTime;
    }

    public void setAdmWardTime(long admWardTime) {
        this.admWardTime = admWardTime;
    }

    public int getadmissionDateCount() {
        return admissionDateCount;
    }

    public void setadmissionDateCount(int admissionDateCount) {
        this.admissionDateCount = admissionDateCount;
    }

    public String getVisitNo() {
        return visitNo;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPatientClass() {
        return patientClass;
    }

    public void setPatientClass(String patientClass) {
        this.patientClass = patientClass;
    }

    public String getPatientIdentity() {
        return patientIdentity;
    }

    public void setPatientIdentity(String patientIdentity) {
        this.patientIdentity = patientIdentity;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public long getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(long birthTime) {
        this.birthTime = birthTime;
    }

    public String getBirthPlaceCode() {
        return birthPlaceCode;
    }

    public void setBirthPlaceCode(String birthPlaceCode) {
        this.birthPlaceCode = birthPlaceCode;
    }

    public String getBirthPlaceName() {
        return birthPlaceName;
    }

    public void setBirthPlaceName(String birthPlaceName) {
        this.birthPlaceName = birthPlaceName;
    }

    public String getPresentAddressCode() {
        return presentAddressCode;
    }

    public void setPresentAddressCode(String presentAddressCode) {
        this.presentAddressCode = presentAddressCode;
    }

    public String getPresentAddressName() {
        return presentAddressName;
    }

    public void setPresentAddressName(String presentAddressName) {
        this.presentAddressName = presentAddressName;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(String nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public String getNextOfKinPhone() {
        return nextOfKinPhone;
    }

    public void setNextOfKinPhone(String nextOfKinPhone) {
        this.nextOfKinPhone = nextOfKinPhone;
    }

    public String getAllergenList() {
        return allergenList;
    }

    public void setAllergenList(String allergenList) {
        this.allergenList = allergenList;
    }

    public String getByNurseName() {
        return byNurseName;
    }

    public void setByNurseName(String byNurseName) {
        this.byNurseName = byNurseName;
    }

    public String getChiefDoctorId() {
        return chiefDoctorId;
    }

    public void setChiefDoctorId(String chiefDoctorId) {
        this.chiefDoctorId = chiefDoctorId;
    }

    public String getChiefDoctor() {
        return chiefDoctor;
    }

    public void setChiefDoctor(String chiefDoctor) {
        this.chiefDoctor = chiefDoctor;
    }

    public String getChargerDoctorId() {
        return chargerDoctorId;
    }

    public void setChargerDoctorId(String chargerDoctorId) {
        this.chargerDoctorId = chargerDoctorId;
    }

    public String getChargerDoctorName() {
        return chargerDoctorName;
    }

    public void setChargerDoctorName(String chargerDoctorName) {
        this.chargerDoctorName = chargerDoctorName;
    }

    public String getAttendingDoctorId() {
        return attendingDoctorId;
    }

    public void setAttendingDoctorId(String attendingDoctorId) {
        this.attendingDoctorId = attendingDoctorId;
    }

    public String getAttendingDoctor() {
        return attendingDoctor;
    }

    public void setAttendingDoctor(String attendingDoctor) {
        this.attendingDoctor = attendingDoctor;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public double getPrepayments() {
        return prepayments;
    }

    public void setPrepayments(double prepayments) {
        this.prepayments = prepayments;
    }

    public double getTotalCosts() {
        return totalCosts;
    }

    public void setTotalCosts(double totalCosts) {
        this.totalCosts = totalCosts;
    }

    public int getOperatingDataCount() {
        return operatingDataCount;
    }

    public void setOperatingDataCount(int operatingDataCount) {
        this.operatingDataCount = operatingDataCount;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptCode() {
        return this.deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public int getBedNo() {
        return this.bedNo;
    }

    public void setBedNo(int bedNo) {
        this.bedNo = bedNo;
    }

    public String getBedLabel() {
        return this.bedLabel;
    }

    public void setBedLabel(String bedLabel) {
        this.bedLabel = bedLabel;
    }

    public String getBedStatus() {
        return this.bedStatus;
    }

    public void setBedStatus(String bedStatus) {
        this.bedStatus = bedStatus;
    }

    public String getBabyNo() {
        return this.babyNo;
    }

    public void setBabyNo(String babyNo) {
        this.babyNo = babyNo;
    }

    public String getPatientId() {
        return this.patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getVisitId() {
        return this.visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getPatientName() {
        return this.patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAge() {
        return this.patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientSex() {
        return this.patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public String getPatientCondition() {
        return this.patientCondition;
    }

    public void setPatientCondition(String patientCondition) {
        this.patientCondition = patientCondition;
    }

    public String getDiagnosis() {
        return this.diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getFastingSign() {
        return this.fastingSign;
    }

    public void setFastingSign(String fastingSign) {
        this.fastingSign = fastingSign;
    }

    public String getIsolationSign() {
        return this.isolationSign;
    }

    public void setIsolationSign(String isolationSign) {
        this.isolationSign = isolationSign;
    }

    public String getNursingClass() {
        return this.nursingClass;
    }

    public void setNursingClass(String nursingClass) {
        this.nursingClass = nursingClass;
    }

    public int getAdmissionDateCount() {
        return this.admissionDateCount;
    }

    public void setAdmissionDateCount(int admissionDateCount) {
        this.admissionDateCount = admissionDateCount;
    }

    public String getPoor() {
        return poor;
    }

    public void setPoor(String poor) {
        this.poor = poor;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
