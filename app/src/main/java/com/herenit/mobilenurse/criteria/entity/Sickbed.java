package com.herenit.mobilenurse.criteria.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.greenrobot.greendao.annotation.Generated;

/**
 * author: HouBin
 * date: 2019/2/18 16:59
 * desc: 床卡实体类 bedNo+patientId+visitId为联合主键
 */
//@Entity(indexes = {@Index(value = "bedNo DESC,patientId DESC,visitId DESC", unique = true)})
public class Sickbed implements Serializable {

    /**
     * babyNo:
     * bedNo : 1
     * bedLabel : 01
     * bedStatus : 占床
     * patientId : 18036324
     * visitId : 1
     * patientName : 吕文婧
     * patientAge : 33
     * patientSex : 女
     * patientCondition : null
     * operatingDataCount : 0
     * diagnosis : 妊娠状态 NOS;妊娠41+1周
     * fastingSign : null
     * isolationSign : null
     * nursingClass : 重症护理
     */

    public static final long serialVersionUID = 888888L;
//    @Id
//    private Long id;

    /**
     * 住院流水号
     */
    private String visitNo;

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

    /**
     * 入院时间
     */
    private Long visitTime;

    /**
     * 是否绑定监护仪
     */
    private boolean isBindMonitor;


    /**
     * 新生儿列表
     */
    private List<Newborn> babyList;

    /**
     * 当前宝宝所在的新生儿列表的角标
     */
    private int currentBabyIndex = -1;

    //    @Generated(hash = 559876606)
//    public Sickbed(Long id, String visitNo, String deptCode, int bedNo, String bedLabel,
//            String bedStatus, String babyNo, String patientId, String visitId,
//            String patientName, String patientAge, String patientSex, String patientCondition,
//            String diagnosis, String fastingSign, String isolationSign, String nursingClass,
//            Long visitTime, boolean isBindMonitor) {
//        this.id = id;
//        this.visitNo = visitNo;
//        this.deptCode = deptCode;
//        this.bedNo = bedNo;
//        this.bedLabel = bedLabel;
//        this.bedStatus = bedStatus;
//        this.babyNo = babyNo;
//        this.patientId = patientId;
//        this.visitId = visitId;
//        this.patientName = patientName;
//        this.patientAge = patientAge;
//        this.patientSex = patientSex;
//        this.patientCondition = patientCondition;
//        this.diagnosis = diagnosis;
//        this.fastingSign = fastingSign;
//        this.isolationSign = isolationSign;
//        this.nursingClass = nursingClass;
//        this.visitTime = visitTime;
//        this.isBindMonitor = isBindMonitor;
//    }
//
//    @Generated(hash = 475836778)
    public Sickbed() {
    }


//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getBabyNo() {
        return babyNo;
    }

    public void setBabyNo(String babyNo) {
        this.babyNo = babyNo;
    }

    public int getBedNo() {
        return bedNo;
    }

    public void setBedNo(int bedNo) {
        this.bedNo = bedNo;
    }

    public String getBedLabel() {
        return bedLabel;
    }

    public void setBedLabel(String bedLabel) {
        this.bedLabel = bedLabel;
    }

    public String getBedStatus() {
        return bedStatus;
    }

    public void setBedStatus(String bedStatus) {
        this.bedStatus = bedStatus;
    }

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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public String getPatientCondition() {
        return patientCondition;
    }

    public void setPatientCondition(String patientCondition) {
        this.patientCondition = patientCondition;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getFastingSign() {
        return fastingSign;
    }

    public void setFastingSign(String fastingSign) {
        this.fastingSign = fastingSign;
    }

    public String getIsolationSign() {
        return isolationSign;
    }

    public void setIsolationSign(String isolationSign) {
        this.isolationSign = isolationSign;
    }

    public String getNursingClass() {
        return nursingClass;
    }

    public void setNursingClass(String nursingClass) {
        this.nursingClass = nursingClass;
    }

    public Long getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Long visitTime) {
        this.visitTime = visitTime;
    }

    public boolean isEmptyBed() {
        return TextUtils.isEmpty(patientId);
    }

    public String getVisitNo() {
        return visitNo;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }

    public boolean getIsBindMonitor() {
        return isBindMonitor;
    }

    public void setIsBindMonitor(boolean isBindMonitor) {
        this.isBindMonitor = isBindMonitor;
    }

    public List<Newborn> getBabyList() {
        return babyList;
    }

    public void setBabyList(List<Newborn> babyList) {
        this.babyList = babyList;
    }

    public int getCurrentBabyIndex() {
        return currentBabyIndex;
    }

    public void setCurrentBabyIndex(int currentBabyIndex) {
        this.currentBabyIndex = currentBabyIndex;
    }

    /**
     * 获取当前选中的新生儿
     *
     * @return
     */
    public Newborn getCurrentBaby() {
        if (currentBabyIndex < 0 || babyList == null || babyList.isEmpty())
            return null;
        if (currentBabyIndex > babyList.size() - 1)
            return null;
        return babyList.get(currentBabyIndex);
    }

    /**
     * 那一个新生儿对象与患者当前的新生儿比较，是否同一人
     *
     * @param newborn
     * @return
     */
    public boolean isCurrentBaby(Newborn newborn) {
        Newborn currentBaby = getCurrentBaby();
        if (currentBaby == null)
            return false;
        if (newborn == null)
            return false;
        if (!newborn.getBabyId().equals(currentBaby.getBabyId()))
            return false;
        return true;
    }

    /**
     * 获取患者标题，一般用于标题栏的显示信息
     *
     * @return
     */
    public String getPatientTitle() {
        return bedLabel + " " + patientName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sickbed sickbed = (Sickbed) o;
        return bedNo == sickbed.bedNo &&
                patientId.equals(sickbed.patientId);
    }
}
