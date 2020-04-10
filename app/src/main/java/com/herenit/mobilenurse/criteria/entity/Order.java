package com.herenit.mobilenurse.criteria.entity;


import android.content.Context;
import android.support.annotation.ColorInt;
import android.text.TextUtils;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.entity.view.MultiItemDelegate;
import com.herenit.mobilenurse.criteria.enums.ExecuteResultEnum;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.jetbrains.annotations.NotNull;

/**
 * author: HouBin
 * Date: 2019/3/6 16:43
 * desc:医嘱实体类
 */
//@Entity(indexes = {@Index(value = "recordId DESC", unique = true)})
public class Order implements Serializable, MultiItemDelegate {

    public static final long serialVersionUID = 22222222L;


    /**
     * 表示上述同一个手术间的手术台次，由手术室分配
     */
    private Integer operationSequence;

    private String recordId;


    /**
     *
     */
    private String patientId;

    /**
     * 住院次数
     */
    private String visitId;

    /**
     * 住院流水号
     */
    private String visitNo;

    /**
     * 手术间
     */
    private String operationRoomNo;

    /**
     * 给药途径
     */
    private String administration;

    /**
     * 停止护士姓名
     */
    private String stopNurseName;

    /**
     * 备注
     */
    private String memo;

    /**
     * 执行频率描述
     */
    private String frequency;


    /**
     * 执行剂量
     */
    private Double executeDosage;


    /**
     * 执行结果：0-未执行、1-部分执行、2-已执行
     */
    private int executeResult;


    /**
     * 医嘱组内序号
     */
    private Integer groupSubNo;


    /**
     * 医嘱正文
     */
    private String orderText;


    /**
     * 患者姓名
     */
    private String name;

    /**
     * 持续执行标记:由医嘱分解时判断，为空时表示医嘱为非持续执行类型。持续执行的医嘱标记为“1”
     */
    private Integer continuedType;

    /**
     * 本医嘱是否长期医嘱，使用代码， 1-长期， 0-临时
     */
    private Integer repeatIndicator;

    /**
     * 关联皮试医嘱号
     */
    private String skinTestOrderId;

    /**
     * 手术医嘱手术室名称
     */
    private String operationDeptName;


    /**
     * 计划执行时间
     */
    private Long planDateTime;

    /**
     * 剂量:药品一次使用剂量
     */
    private String dosage;

    /**
     * 检查医嘱信息(执行科室)
     */
    private String examPerformDept;


    /**
     * 结束持续执行医嘱的护士ID
     */
    private String stopNurseId;

    /**
     * 医嘱类别医嘱类别字典（西成药、草药、检查、检验、手术、用血、病理、处置、其他等）
     */
    private String orderClass;

    /**
     * 持续时间
     */
    private String duration;

    /**
     * 标本名称
     */
    private String specimanName;

    /**
     * 医嘱停止时间
     */
    private Long stopDateTime;

    /**
     * 开单医生
     */
    private String doctorName;

    /**
     * 剂量单位
     */
    private String doseUnits;

    /**
     * 执行该次医嘱的护士员工ID
     */
    private String processingNurseId;

    /**
     * 标本注意事项
     */
    private String notesForSpcm;

    /**
     * 是否高危药:0 普通 1 高危
     */
    private Integer dangerIndicator;

    private boolean isDangerIndicator;


    /**
     * 医嘱组号:0 表示非成组医嘱
     */
    private String groupNo;

    /**
     * 医嘱的实际执行时间
     */
    private Long executeDateTime;

    /**
     * 持续时间单位
     */
    private String durationUnits;

    /**
     * 皮试结果:阴性或阳性
     */
    private String performResult;

    /**
     * 医嘱开始生效时间
     */
    private Long startDateTime;

    /**
     * 申请单号
     */
    private String applyNo;

    /**
     * 标本条码号
     */
    private String barCode;

    /**
     * 床标号
     */
    private String bedLabel;

    /**
     * 执行该次医嘱的护士员工姓名
     */
    private String processingNurseName;

    /**
     * 核对人
     */
    private String verifyNurseName;
    /**
     * 核对人Id
     */
    private String verifyNurseId;
    //核对时间
    private Long verifyDateTime;

    /**
     * 医嘱状态: 0-开立未提交、1-新开、2-执行、3-停止、4-作废、7-审核未通过
     */
    private String orderStatus;

    /**
     * 表示某一组医嘱在某一次执行时，的唯一标识。可作为护士执行拆分后的医嘱的选择依据
     * patientId+visitId+orderNo+planDateTime拼接而成
     */
    private String recordGroupId;

    private boolean isST;//是否是皮试处置医嘱

    private boolean isInfusion;//是否是输液医嘱

    /**
     * 一般非输液类型的药疗医嘱，需要显示执行剂量
     */
    private boolean isShowExecuteDosage;//是否显示执行剂量
    //    @Transient
    private boolean isChecked = false;

    private boolean isGroupStart = false;

    private Integer skinTestFlag;

    /**
     * 是否需要双签名
     */
    private boolean isDoubleSignature;

    /**
     * 表示有没有超时，超时则为红色
     */
    private String color;

    private Integer doubledSignIndicator;


    private String orderId;

    /**
     * 用法描述
     */
    private String userDescription;
    //    @Transient
    private boolean newExecute;


    /**
     * 新生儿编号
     */
    private String babyNo;

//    @Generated(hash = 537078003)
//    public Order(Integer operationSequence, String recordId, String patientId, String visitId, String visitNo, String operationRoomNo,
//            String administration, String stopNurseName, String memo, String frequency, Double executeDosage, int executeResult, Integer groupSubNo,
//            String orderText, String name, Integer continuedType, Integer repeatIndicator, String skinTestOrderId, String operationDeptName,
//            Long planDateTime, String dosage, String examPerformDept, String stopNurseId, String orderClass, String duration, String specimanName,
//            Long stopDateTime, String doctorName, String doseUnits, String processingNurseId, String notesForSpcm, Integer dangerIndicator,
//            boolean isDangerIndicator, String groupNo, Long executeDateTime, String durationUnits, String performResult, Long startDateTime,
//            String applyNo, String barCode, String bedLabel, String processingNurseName, String verifyNurseName, String verifyNurseId,
//            Long verifyDateTime, String orderStatus, String recordGroupId, boolean isST, boolean isInfusion, boolean isShowExecuteDosage,
//            boolean isGroupStart, Integer skinTestFlag, boolean isDoubleSignature, String color, Integer doubledSignIndicator, String orderId,
//            String userDescription) {
//        this.operationSequence = operationSequence;
//        this.recordId = recordId;
//        this.patientId = patientId;
//        this.visitId = visitId;
//        this.visitNo = visitNo;
//        this.operationRoomNo = operationRoomNo;
//        this.administration = administration;
//        this.stopNurseName = stopNurseName;
//        this.memo = memo;
//        this.frequency = frequency;
//        this.executeDosage = executeDosage;
//        this.executeResult = executeResult;
//        this.groupSubNo = groupSubNo;
//        this.orderText = orderText;
//        this.name = name;
//        this.continuedType = continuedType;
//        this.repeatIndicator = repeatIndicator;
//        this.skinTestOrderId = skinTestOrderId;
//        this.operationDeptName = operationDeptName;
//        this.planDateTime = planDateTime;
//        this.dosage = dosage;
//        this.examPerformDept = examPerformDept;
//        this.stopNurseId = stopNurseId;
//        this.orderClass = orderClass;
//        this.duration = duration;
//        this.specimanName = specimanName;
//        this.stopDateTime = stopDateTime;
//        this.doctorName = doctorName;
//        this.doseUnits = doseUnits;
//        this.processingNurseId = processingNurseId;
//        this.notesForSpcm = notesForSpcm;
//        this.dangerIndicator = dangerIndicator;
//        this.isDangerIndicator = isDangerIndicator;
//        this.groupNo = groupNo;
//        this.executeDateTime = executeDateTime;
//        this.durationUnits = durationUnits;
//        this.performResult = performResult;
//        this.startDateTime = startDateTime;
//        this.applyNo = applyNo;
//        this.barCode = barCode;
//        this.bedLabel = bedLabel;
//        this.processingNurseName = processingNurseName;
//        this.verifyNurseName = verifyNurseName;
//        this.verifyNurseId = verifyNurseId;
//        this.verifyDateTime = verifyDateTime;
//        this.orderStatus = orderStatus;
//        this.recordGroupId = recordGroupId;
//        this.isST = isST;
//        this.isInfusion = isInfusion;
//        this.isShowExecuteDosage = isShowExecuteDosage;
//        this.isGroupStart = isGroupStart;
//        this.skinTestFlag = skinTestFlag;
//        this.isDoubleSignature = isDoubleSignature;
//        this.color = color;
//        this.doubledSignIndicator = doubledSignIndicator;
//        this.orderId = orderId;
//        this.userDescription = userDescription;
//    }

    //    @Generated(hash = 1105174599)
    public Order() {
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

    public Integer getDoubledSignIndicator() {
        return doubledSignIndicator;
    }

    public void setDoubledSignIndicator(Integer doubledSignIndicator) {
        this.doubledSignIndicator = doubledSignIndicator;
    }

    public Integer getOperationSequence() {
        return operationSequence;
    }

    public void setOperationSequence(Integer operationSequence) {
        this.operationSequence = operationSequence;
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

    public String getAdministration() {
        return administration;
    }

    public void setAdministration(String administration) {
        this.administration = administration;
    }

    public String getStopNurseName() {
        return stopNurseName;
    }

    public void setStopNurseName(String stopNurseName) {
        this.stopNurseName = stopNurseName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Double getExecuteDosage() {
        return executeDosage;
    }

    public void setExecuteDosage(Double executeDosage) {
        this.executeDosage = executeDosage;
    }

    public int getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(int executeResult) {
        this.executeResult = executeResult;
    }

    public Integer getGroupSubNo() {
        return groupSubNo;
    }

    public void setGroupSubNo(Integer groupSubNo) {
        this.groupSubNo = groupSubNo;
    }


    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getContinuedType() {
        return continuedType;
    }

    public void setContinuedType(Integer continuedType) {
        this.continuedType = continuedType;
    }

    public Integer getRepeatIndicator() {
        return repeatIndicator;
    }

    public void setRepeatIndicator(Integer repeatIndicator) {
        this.repeatIndicator = repeatIndicator;
    }

    public String getSkinTestOrderId() {
        return skinTestOrderId;
    }

    public void setSkinTestOrderId(String skinTestOrderId) {
        this.skinTestOrderId = skinTestOrderId;
    }

    public String getOperationDeptName() {
        return operationDeptName;
    }

    public void setOperationDeptName(String operationDeptName) {
        this.operationDeptName = operationDeptName;
    }


    public Long getPlanDateTime() {
        return planDateTime;
    }

    public void setPlanDateTime(Long planDateTime) {
        this.planDateTime = planDateTime;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getExamPerformDept() {
        return examPerformDept;
    }

    public void setExamPerformDept(String examPerformDept) {
        this.examPerformDept = examPerformDept;
    }


    public String getStopNurseId() {
        return stopNurseId;
    }

    public void setStopNurseId(String stopNurseId) {
        this.stopNurseId = stopNurseId;
    }

    public String getOrderClass() {
        return orderClass;
    }

    public void setOrderClass(String orderClass) {
        this.orderClass = orderClass;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSpecimanName() {
        return specimanName;
    }

    public void setSpecimanName(String specimanName) {
        this.specimanName = specimanName;
    }

    public Long getStopDateTime() {
        return stopDateTime;
    }

    public void setStopDateTime(Long stopDateTime) {
        this.stopDateTime = stopDateTime;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoseUnits() {
        return doseUnits;
    }

    public void setDoseUnits(String doseUnits) {
        this.doseUnits = doseUnits;
    }

    public String getProcessingNurseId() {
        return processingNurseId;
    }

    public void setProcessingNurseId(String processingNurseId) {
        this.processingNurseId = processingNurseId;
    }

    public String getNotesForSpcm() {
        return notesForSpcm;
    }

    public void setNotesForSpcm(String notesForSpcm) {
        this.notesForSpcm = notesForSpcm;
    }

    public Integer getDangerIndicator() {
        return dangerIndicator;
    }

    public void setDangerIndicator(Integer dangerIndicator) {
        this.dangerIndicator = dangerIndicator;
    }


    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public Long getExecuteDateTime() {
        return executeDateTime;
    }

    public void setExecuteDateTime(Long executeDateTime) {
        this.executeDateTime = executeDateTime;
    }

    public String getDurationUnits() {
        return durationUnits;
    }

    public void setDurationUnits(String durationUnits) {
        this.durationUnits = durationUnits;
    }

    public String getPerformResult() {
        return performResult;
    }

    public void setPerformResult(String performResult) {
        this.performResult = performResult;
    }

    public Long getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Long startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getBedLabel() {
        return bedLabel;
    }

    public void setBedLabel(String bedLabel) {
        this.bedLabel = bedLabel;
    }

    public String getProcessingNurseName() {
        return processingNurseName;
    }

    public void setProcessingNurseName(String processingNurseName) {
        this.processingNurseName = processingNurseName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public boolean isGroupStart() {
        return isGroupStart;
    }

    public void setGroupStart(boolean groupStart) {
        isGroupStart = groupStart;
    }

    public boolean isST() {
        return isST;
    }

    public void setIsST(boolean ST) {
        isST = ST;
    }

    public boolean isInfusion() {
        return isInfusion;
    }

    public void setIsInfusion(boolean infusion) {
        isInfusion = infusion;
    }

    public void setIsShowExecuteDosage(boolean isShowExecuteDosage) {
        this.isShowExecuteDosage = isShowExecuteDosage;
    }

    public String getRecordGroupId() {
        return recordGroupId;
    }

    public void setRecordGroupId(String recordGroupId) {
        this.recordGroupId = recordGroupId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return
                recordGroupId.equals(order.recordGroupId) &&
                        groupSubNo.equals(order.groupSubNo);

    }

    /**
     * 是否为持续性医嘱
     *
     * @return
     */
    public boolean isContinue() {
        if (continuedType != null && continuedType == 1)
            return true;
        return false;
    }

    /**
     * 给医嘱列表分组
     *
     * @param data
     */
    public static void setOrderListGroup(List<Order> data) {
        if (data == null || data.isEmpty())
            return;
        for (int x = 0; x < data.size(); x++) {
            Order order = data.get(x);
            if (x == 0) {
                order.setGroupStart(true);
            } else {
                Order last = data.get(x - 1);
                order.setGroupStart(!(order.getRecordGroupId().equals(last.getRecordGroupId())));
            }
        }
    }

    public boolean getIsST() {
        return this.isST;
    }

    public boolean getIsInfusion() {
        return this.isInfusion;
    }

    public boolean getIsShowExecuteDosage() {
        return this.isShowExecuteDosage;
    }

    public boolean getIsGroupStart() {
        return this.isGroupStart;
    }

    public void setIsGroupStart(boolean isGroupStart) {
        this.isGroupStart = isGroupStart;
    }


    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public boolean getIsDoubleSignature() {
        return this.isDoubleSignature;
    }

    public void setIsDoubleSignature(boolean isDoubleSignature) {
        this.isDoubleSignature = isDoubleSignature;
    }

    public String getRecordId() {
        return this.recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public boolean getIsDangerIndicator() {
        return isDangerIndicator;
    }

    public void setIsDangerIndicator(boolean isDangerIndicator) {
        this.isDangerIndicator = isDangerIndicator;
    }

    public String getVerifyNurseName() {
        return verifyNurseName;
    }

    public void setVerifyNurseName(String verifyNurseName) {
        this.verifyNurseName = verifyNurseName;
    }

    public String getVerifyNurseId() {
        return verifyNurseId;
    }

    public void setVerifyNurseId(String verifyNurseId) {
        this.verifyNurseId = verifyNurseId;
    }

    public Long getVerifyDateTime() {
        return verifyDateTime;
    }

    public void setVerifyDateTime(Long verifyDateTime) {
        this.verifyDateTime = verifyDateTime;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSkinTestFlag() {
        return skinTestFlag;
    }

    public void setSkinTestFlag(Integer skinTestFlag) {
        this.skinTestFlag = skinTestFlag;
    }

    public boolean isNewExecute() {
        return newExecute;
    }

    public void setNewExecute(boolean newExecute) {
        this.newExecute = newExecute;
    }

    public String getBabyNo() {
        return babyNo;
    }

    public void setBabyNo(String babyNo) {
        this.babyNo = babyNo;
    }

    /**
     * 判断该医嘱是否需要双签
     *
     * @return
     */
    public boolean needVerify() {
        if (ExecuteResultEnum.isExecuted(executeResult))
            return false;
        if (isST) {
            return !TextUtils.isEmpty(processingNurseId) && !TextUtils.isEmpty(performResult) && TextUtils.isEmpty(verifyNurseId);
        }
        return isDoubleSignature && !TextUtils.isEmpty(processingNurseId) && TextUtils.isEmpty(verifyNurseId);
    }

    /**
     * 判断该条医嘱，成功执行一次之后，下一次执行是否需要双签名
     *
     * @return
     */
    public boolean nextNeedVerify() {
        if (ExecuteResultEnum.isExecuted(executeResult))
            return false;
        if (isST) {
            return !TextUtils.isEmpty(processingNurseId) && TextUtils.isEmpty(performResult) && TextUtils.isEmpty(verifyNurseId);
        }
        return isDoubleSignature && TextUtils.isEmpty(processingNurseId) && TextUtils.isEmpty(verifyNurseId);
    }

    /**
     * 判断该医嘱是否需要录入皮试结果
     *
     * @return
     */
    public boolean needExecuteSkinTestResult() {
        return !ExecuteResultEnum.isExecuted(executeResult) && isST && !TextUtils.isEmpty(processingNurseId) && TextUtils.isEmpty(performResult);
    }

    @ColorInt
    public int getPlanDateTimeColor(@NotNull Context context) {
        @ColorInt int planDateTimeColor = ArmsUtils.getColor(context, R.color.bg_royalBlue);
        if (TextUtils.isEmpty(color))
            return planDateTimeColor;
        switch (color) {
            case "red":
                planDateTimeColor = ArmsUtils.getColor(context, R.color.red);
                break;
            case "blue":
                planDateTimeColor = ArmsUtils.getColor(context, R.color.bg_royalBlue);
                break;
            case "green":
                planDateTimeColor = ArmsUtils.getColor(context, R.color.dark_green);
                break;
        }
        return planDateTimeColor;
    }

    public String getUserDescription() {
        return this.userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }
}
