package com.herenit.mobilenurse.mvp.orders;

import android.text.TextUtils;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.dict.ExecuteResultDict;
import com.herenit.mobilenurse.criteria.entity.submit.OrdersExecute;
import com.herenit.mobilenurse.criteria.entity.view.RvController;

import com.herenit.mobilenurse.criteria.enums.ExecuteResultEnum;
import com.herenit.mobilenurse.criteria.enums.OrderClassEnum;

import com.herenit.mobilenurse.datastore.tempcache.DictTemp;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医嘱帮助类，可以提供一些构建医嘱特有的数据
 */
public class OrdersHelp {

    /**
     * 根据一条医嘱，获取该医嘱的详情列表控制Item的List，用于界面显示
     *
     * @param order
     * @return
     */
    public static List<RvController> createItemControllerByOrder(Order order) {
        if (order == null)
            return null;
        List<RvController> result = new ArrayList<>();
        //计划执行时间Item
        result.add(buildPlanExecuteTimeController(order));
        //是否双签名未核对
        boolean notVerify = !ExecuteResultEnum.isExecuted(order.getExecuteResult()) && !TextUtils.isEmpty(order.getProcessingNurseName()) && order.getIsDoubleSignature();
        //执行时间Item
        result.add(buildExecuteTimeController(order, notVerify));
        String orderClass = order.getOrderClass();
        if (OrderClassEnum.EXAM.getCode().equals(orderClass)) {//检查医嘱
            //TODO 空军口腔医院Orders表中没找到检查科室相关数据，不需要添加检查科室
//            result.addAll(buildExamControllerList(order));
        }
        if ("C".equals(orderClass)) {//检验医嘱
            //TODO 空军口腔医院Orders表中没找到检验医嘱相关的标本字段，按普通医嘱处理，不需要添加特有的控件（标本说明、标本码等）
            result.addAll(buildLabControllerList(order));
        }
        if (OrderClassEnum.OPERATION.getCode().equals(orderClass)) {//手术医嘱
            //TODO 空军口腔医院Orders表中没找到手术相关的字段，不添加手术医嘱特有的信息（手术室、手术间、手术台次）
//            result.addAll(buildOperationControllerList(order));
        }
        if (order.isST()) {//皮试或者关联了皮试的医嘱
            result.add(buildSTController(order));
        }
        if (order.getIsShowExecuteDosage()) {//药品医嘱，需要显示剂量百分数
            result.add(buildDosageController(order, notVerify));
        }
        result.add(buildExecuteResultController(order, notVerify));//添加执行结果
        RvController stopTimeController = buildStopTimeController(order);
        if (stopTimeController != null)//添加结束时间
            result.add(stopTimeController);
        result.add(buildProcessingNurseController(order));
        if (order.getIsDoubleSignature()) {
            result.add(buildVerifyNurseController(order));
        }
        result.add(buildMemoController(order, notVerify));
        return result;
    }

    /**
     * 构建一个Controller，需要显示医嘱执行的和对人
     *
     * @param order
     * @return
     */
    private static RvController buildVerifyNurseController(Order order) {
        RvController controller = new RvController();
        controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_verifyNurse));
        controller.setReadOnly(true);
        controller.setEditable(false);
        controller.setValue(order.getVerifyNurseName());
        controller.setValueType(CommonConstant.DATA_TYPE_NAME_CHARACTER);
        return controller;
    }


    /**
     * 构建一个Controller，需要显示医嘱的执行人
     *
     * @param order
     * @return
     */
    private static RvController buildProcessingNurseController(Order order) {
        RvController controller = new RvController();
        controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_processingNurse));
        controller.setReadOnly(true);
        controller.setEditable(false);
        controller.setValue(order.getProcessingNurseName());
        controller.setValueType(CommonConstant.DATA_TYPE_NAME_CHARACTER);
        return controller;
    }

    /**
     * 构建一个Controller，需要显示备注的医嘱特有的界面
     *
     * @param order
     * @param notVerify
     * @return
     */
    private static RvController buildMemoController(Order order, boolean notVerify) {
        RvController controller = new RvController();
        controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_memo));
        controller.setReadOnly(false);
        int executeResult = order.getExecuteResult();
        if (ExecuteResultEnum.isExecuted(executeResult) || notVerify) {//已执行或者未核对的 不允许编写备注
            controller.setValue(order.getMemo());
            controller.setEditable(false);
        } else {
            controller.setValue(order.getMemo());
            controller.setEditable(true);
        }
        controller.setValueType(CommonConstant.DATA_TYPE_NAME_CHARACTER);
        controller.setActionViewType(CommonConstant.VIEW_TYPE_SINGLE_INPUT_DIALOG);
        Map<String, Object> viewData = new HashMap<>();
        viewData.put(CommonConstant.FIELD_NAME_TITLE, ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_memo));
        viewData.put(CommonConstant.FIELD_NAME_HINT, ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.message_inputMemo));
        viewData.put(CommonConstant.FIELD_NAME_DATA, order.getMemo());
        controller.setViewData(viewData);
        return controller;
    }


    /**
     * 构建一个Controller，需要显示结束时间的医嘱特有的界面
     *
     * @param order
     * @return
     */
    private static RvController buildStopTimeController(Order order) {
        if (!order.isContinue())
            return null;
        int executeResult = order.getExecuteResult();
//        if (!(executeResult == ExecuteResultEnum.EXECUTING.getCode() || executeResult == ExecuteResultEnum.EXECUTED.getCode()))
        if (!ExecuteResultEnum.isExecuted(executeResult))
            return null;
        RvController controller = new RvController();
        controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_finishTime));
        controller.setReadOnly(false);
        controller.setActionViewType(CommonConstant.VIEW_TYPE_CALENDAR);
        controller.setValueType(CommonConstant.DATA_TYPE_NAME_DATE);
        controller.setValueFormat(TimeUtils.FORMAT_YYYYMMDDHHMM);
        Date stopDateTime = null;
        if (executeResult == ExecuteResultEnum.EXECUTED.getCode()) {//已执行，显示上传过的结束时间，不可改变
            if (order.getStopDateTime() != null)
                stopDateTime = new Date(order.getStopDateTime());
            controller.setEditable(false);
        } else {//否则，显示当前时间，可编辑
            stopDateTime = TimeUtils.getCurrentDate();
            controller.setEditable(true);
        }
        controller.setValue(TimeUtils.getDateStringByFormat(stopDateTime, controller.getValueFormat()));
        Map<String, Object> viewData = new HashMap<>();
        viewData.put(CommonConstant.FIELD_NAME_DATA, stopDateTime);
        controller.setViewData(viewData);
        return controller;
    }

    /**
     * 构建一个Controller，需要显示执行结果的医嘱特有的界面
     *
     * @param order
     * @param notVerify
     * @return
     */
    private static RvController buildExecuteResultController(Order order, boolean notVerify) {
        RvController controller = new RvController();
        controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_executeResult));
        controller.setValueType(CommonConstant.DATA_TYPE_NAME_CHARACTER);

        Map<String, Object> viewData = new HashMap<>();
        viewData.put(CommonConstant.FIELD_NAME_TITLE, ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_executeResult));
        List<String> data = new ArrayList<>();
        List<ExecuteResultDict> resultList = DictTemp.getInstance().getExecuteResultList();
        if (resultList != null && !resultList.isEmpty()) {
            for (ExecuteResultDict result : resultList)
                data.add(result.getExecuteResultName());
        }
        viewData.put(CommonConstant.FIELD_NAME_DATA, data);
        controller.setViewData(viewData);
        controller.setActionViewType(CommonConstant.VIEW_TYPE_LIST_DIALOG);
        if (ExecuteResultEnum.isExecuted(order.getExecuteResult()) || notVerify) {//已执行的医嘱或者未核对的医嘱
            controller.setReadOnly(false);
            controller.setEditable(false);
            controller.setValue(DictTemp.getInstance().getExecuteResultNameByCode(order.getExecuteResult()));
        } else {//未执行的医嘱可以选择执行结果
            controller.setReadOnly(false);
            controller.setEditable(true);
            //默认为执行
            controller.setValue(DictTemp.getInstance().getExecuteResultNameByCode(ExecuteResultEnum.EXECUTED.getCode()));
        }
        return controller;
    }

    /**
     * 构建一个Controller列表，需要显示执行剂量的医嘱特有的界面Item列表
     *
     * @param order
     * @param notVerify
     * @return
     */
    private static RvController buildDosageController(Order order, boolean notVerify) {
        RvController controller = new RvController();
        controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_executeDosage));
        controller.setReadOnly(false);
        controller.setValueType(CommonConstant.DATA_TYPE_NAME_PERCENT);
        int executeResult = order.getExecuteResult();
        Double dosage;
        if (ExecuteResultEnum.isExecuted(executeResult) || notVerify)
            dosage = order.getExecuteDosage();
        else
            dosage = 100.0;
        if (dosage == null)
            dosage = 0.0;
        controller.setValue("" + StringUtils.getInt(dosage) + "%");
        //TODO 省人民没有执行中的概念
//        if (order.isContinue()) {//持续性医嘱
//            if (executeResult == ExecuteResultEnum.EXECUTING.getCode()) {//执行中的持续性医嘱，执行剂量可修改，默认为100%
//                controller.setEditable(true);
//            } else {//持续性医嘱，只要不是执行中的状态，则执行剂量不可编辑
//                controller.setEditable(false);
//            }
//        } else {//非持续性医嘱
        if (!ExecuteResultEnum.isExecuted(executeResult) && !notVerify) {//未执行的医嘱，并且并非未核对的，可编辑执行剂量，默认为100%
            controller.setEditable(true);
        } else {//已执行的持续性医嘱，不可修改执行剂量，执行剂量显示为之前提交过的值
            controller.setEditable(false);
        }
//        }
        controller.setActionViewType(CommonConstant.VIEW_TYPE_SEEK_BAR_DIALOG);
        Map<String, Object> viewData = new HashMap<>();
        viewData.put(CommonConstant.FIELD_NAME_TITLE, ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_executeDosage));
        viewData.put(CommonConstant.FIELD_NAME_DATA, StringUtils.getIntegerNumFromPercent(controller.getValue()));
        controller.setViewData(viewData);
        return controller;
    }

    /**
     * 构建一个Controller列表，手术医嘱特有的界面Item列表
     *
     * @param order
     * @return
     */
    private static Collection<? extends RvController> buildOperationControllerList(Order order) {
        List<RvController> controllerList = new ArrayList<>();
        /**************************************************手术室***************************************************/
        RvController operationDept = new RvController();
        operationDept.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_operationDeptName));
        operationDept.setValue(order.getOperationDeptName());
        operationDept.setReadOnly(true);
        operationDept.setValueType(CommonConstant.DATA_TYPE_NAME_CHARACTER);
        controllerList.add(operationDept);
        /**************************************************手术间***************************************************/
        RvController operationRoom = new RvController();
        operationRoom.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_operationRoom));
        operationRoom.setValue(order.getOperationRoomNo());
        operationRoom.setReadOnly(true);
        operationRoom.setValueType(CommonConstant.DATA_TYPE_NAME_CHARACTER);
        controllerList.add(operationRoom);
        controllerList.add(operationDept);
        /**************************************************手术台次***************************************************/
        RvController operationSequence = new RvController();
        operationSequence.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_operationSequence));
        if (order.getOperationSequence() != null)
            operationSequence.setValue(String.valueOf(order.getOperationSequence()));
        else
            operationSequence.setValue("");
        operationSequence.setReadOnly(true);
        operationSequence.setValueType(CommonConstant.DATA_TYPE_NAME_CHARACTER);
        controllerList.add(operationRoom);
        return controllerList;
    }

    /**
     * 构建一个Controller列表，皮试、关联了皮试医嘱特有的界面Item列表
     *
     * @param order
     * @return
     */
    private static RvController buildSTController(Order order) {
        RvController controller = new RvController();
        controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_STResult));
        controller.setValueType(CommonConstant.DATA_TYPE_NAME_CHARACTER);
        controller.setReadOnly(false);
        int executeResult = order.getExecuteResult();
        if (order.needExecuteSkinTestResult()) {//需要录入皮试结果的皮试医嘱
            controller.setEditable(true);//可修改皮试结果
            controller.setValue(CommonConstant.COMMON_VALUE_NEGATIVE);//默认为阴性
        } else {//已经录入的或者还不可以录入皮试结果的医嘱
            controller.setEditable(false);
            controller.setValue(order.getPerformResult());
        }
        Map<String, Object> viewData = new HashMap<>();
        viewData.put(CommonConstant.FIELD_NAME_TITLE, ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_STResult));
        List<String> data = new ArrayList<>();
        data.add(CommonConstant.COMMON_VALUE_NEGATIVE);
        data.add(CommonConstant.COMMON_VALUE_POSITIVE);
        viewData.put(CommonConstant.FIELD_NAME_DATA, data);
        controller.setViewData(viewData);
        controller.setActionViewType(CommonConstant.VIEW_TYPE_LIST_DIALOG);
        return controller;
    }

    /**
     * 构建一个Controller列表，检验医嘱特有的界面Item列表
     *
     * @param order
     * @return
     */
    private static Collection<? extends RvController> buildLabControllerList(Order order) {
        List<RvController> controllerList = new ArrayList<>();
        /********************************************申请单号********************************************************/
        RvController specimenCode = new RvController();
        specimenCode.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_applyNo));
        if (!TextUtils.isEmpty(order.getApplyNo()))
            specimenCode.setValue(order.getApplyNo());
        else
            specimenCode.setValue("");
        specimenCode.setReadOnly(true);
        specimenCode.setValueType(CommonConstant.DATA_TYPE_NAME_CHARACTER);
//        if (ExecuteResultEnum.NONEXECUTION.getCode() == order.getExecuteResult())//未执行的状态下，可修改标本码
//            specimenCode.setEditable(true);
//        else//如果不是未执行状态，则不可以修改标本码
        //TODO 省人民的检验执行，标本号不可修改
        specimenCode.setEditable(false);
        Map<String, Object> viewData = new HashMap<>();
        viewData.put(CommonConstant.FIELD_NAME_TITLE, ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.title_specimenCollection));
        viewData.put(CommonConstant.FIELD_NAME_HINT, ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.message_inputSpecimenCode));
        viewData.put(CommonConstant.FIELD_NAME_DATA, order.getApplyNo());
        specimenCode.setViewData(viewData);
        specimenCode.setActionViewType(CommonConstant.VIEW_TYPE_SINGLE_INPUT_DIALOG);
        controllerList.add(specimenCode);
        /********************************************标本********************************************************/
        RvController specimenName = new RvController();
        specimenName.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_specimen));
        specimenName.setValue(order.getSpecimanName());
        specimenName.setReadOnly(true);
        specimenName.setValueType(CommonConstant.DATA_TYPE_NAME_CHARACTER);
        controllerList.add(specimenName);
        /********************************************标本说明********************************************************/
        RvController specimenNotes = new RvController();
        specimenNotes.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_specimenNotes));
        specimenNotes.setValue(order.getNotesForSpcm());
        specimenNotes.setReadOnly(true);
        specimenNotes.setValueType(CommonConstant.DATA_TYPE_NAME_CHARACTER);
        controllerList.add(specimenNotes);
        return controllerList;
    }

    /**
     * 构建一个Controller列表，检查医嘱特有的界面Item列表
     *
     * @param order
     * @return
     */
    private static Collection<? extends RvController> buildExamControllerList(Order order) {
        List<RvController> controllerList = new ArrayList<>();
        RvController controller = new RvController();
        controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_examDept));
        controller.setValue(order.getExamPerformDept());
        controller.setReadOnly(true);
        controller.setValueType(CommonConstant.DATA_TYPE_NAME_CHARACTER);
        controllerList.add(controller);
        return controllerList;
    }

    /**
     * 构建执行时间的Item
     *
     * @param order
     * @param notVerify
     * @return
     */
    private static RvController buildExecuteTimeController(Order order, boolean notVerify) {
        RvController controller = new RvController();
        String orderClass = order.getOrderClass();
        if (OrderClassEnum.EXAM.getCode().equals(orderClass) || OrderClassEnum.OPERATION.getCode().equals(orderClass)) {
            //检查预约、手术医嘱，应该叫为确认时间
            controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_confirmTime));
            //TODO 空军口腔医院Orders表中，没有检验医嘱的标本相关字段，所以不叫采集时间
        }/* else if (OrderClassEnum.LAB.getCode().equals(orderClass)) {//检验医嘱应该叫为采集时间
            controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_collectionTime));
        }*/ else {//其它的叫做执行时间
            controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_executeTime));
        }
        controller.setActionViewType(CommonConstant.VIEW_TYPE_CALENDAR);
        controller.setReadOnly(false);
        controller.setValueType(CommonConstant.DATA_TYPE_NAME_DATE);
        controller.setValueFormat(TimeUtils.FORMAT_YYYYMMDDHHMM);
        Long executeTime = order.getExecuteDateTime();
        if (executeTime == null)
            executeTime = TimeUtils.getCurrentDate().getTime();
        Date executeDateTime = new Date(executeTime);
        controller.setValue(TimeUtils.getDateStringByFormat(executeDateTime, controller.getValueFormat()));
        if (!ExecuteResultEnum.isExecuted(order.getExecuteResult()) && !notVerify) {//未执行的医嘱，或者未核对的医嘱，执行时间可编辑
            controller.setEditable(true);
        } else {//不是未执行状态，则执行时间不可修改
            controller.setEditable(false);
        }
        Map<String, Object> viewData = new HashMap<>();
        viewData.put(CommonConstant.FIELD_NAME_DATA, executeDateTime);
        controller.setViewData(viewData);
        return controller;
    }

    /**
     * 构建计划执行时间Item的OrderInfoItemController
     *
     * @param order
     * @return
     */
    private static RvController buildPlanExecuteTimeController(Order order) {
        RvController controller = new RvController();
        String orderClass = order.getOrderClass();
        if (OrderClassEnum.EXAM.getCode().equals(orderClass)) {//检查预约的医嘱，应该叫为预约时间
            controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_appointmentTime));
        } else if (OrderClassEnum.OPERATION.getCode().equals(orderClass)) {//手术医嘱应该叫为手术时间
            controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_operationTime));
        } else {//其它的叫做计划时间
            controller.setName(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_planTime));
        }
        controller.setReadOnly(true);
        controller.setValueType(CommonConstant.DATA_TYPE_NAME_DATE);
        controller.setValueFormat(TimeUtils.FORMAT_YYYYMMDDHHMM);
        controller.setValue(TimeUtils.getDateStringByFormat(new Date(order.getPlanDateTime()), controller.getValueFormat()));
        return controller;
    }


    /**
     * 根据医嘱和界面控制列表，获取最终要填写的执行时间
     * 这里获取的可能是非持续性医嘱的执行时间，也可能是持续性医嘱的执行时间或结束时间
     * 这里只获取单组医嘱，所以长度为1，只有一组医嘱要执行
     *
     * @param order          要执行的医嘱
     * @param controllerList 要执行的医嘱的操作结果
     * @return
     */
    public static OrdersExecute buildGroupOrderExecuteByController(Order order, List<RvController> controllerList, boolean isSkinTestResult) {
        //医嘱伟空或者医嘱已执行，不需要继续构建
        if (order == null || ExecuteResultEnum.isExecuted(order.getExecuteResult()))
            return null;
        OrdersExecute execute = new OrdersExecute();
        execute.setContinuedType(order.getContinuedType());
        execute.setDoubledSignIndicator(order.getDoubledSignIndicator());
        int skinTestIndicator = 0;
        if (order.getSkinTestFlag() != null && order.getSkinTestFlag() >= 1 && order.getSkinTestFlag() <= 4) {
            skinTestIndicator = 1;
        }
        execute.setSkinTestIndicator(skinTestIndicator);
        execute.setOrderId(order.getOrderId());
        execute.setPatientId(order.getPatientId());
        execute.setRecordId(order.getRecordId());
        execute.setVisitNo(order.getVisitNo());
        execute.setExecuteDateTime(order.getExecuteDateTime());
        execute.setExecuteResult(order.getExecuteResult());
        execute.setProcessingNurseId(order.getProcessingNurseId());
        execute.setProcessingNurseName(order.getProcessingNurseName());
        execute.setStopDateTime(order.getStopDateTime());
        execute.setStopNurseId(order.getStopNurseId());
        execute.setStopNurseName(order.getStopNurseName());
        execute.setExecuteDosage(order.getExecuteDosage());
        for (RvController controller : controllerList) {
            if (controller.isReadOnly() || !controller.isEditable())//不是编辑的属性，不需要填写
                continue;
            String name = controller.getName();
            if ((name.equals(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_executeTime)))
                    || name.equals(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_collectionTime))
                    || name.equals(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_confirmTime))
                    || name.equals(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_finishTime))) {//执行时间、结束时间
                if (CommonConstant.DATA_TYPE_NAME_DATE.equals(controller.getValueType())) {
                    Date executeDate = TimeUtils.getDateByFormat(controller.getValueFormat(), controller.getValue());
                    if (executeDate != null) {
                        if (isSkinTestResult) {//皮试结果录入
                            execute.setSkinTestDateTime(executeDate.getTime());
                        } else {//普通结果录入
                            execute.setExecuteDateTime(executeDate.getTime());
                            execute.setStopDateTime(executeDate.getTime());
                        }
                    }
                }
            } else if (name.equals(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_executeDosage))) {//执行剂量
                execute.setExecuteDosage(StringUtils.getNumFromPercent(controller.getValue()));
            } else if (name.equals(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_memo))) {//备注
                execute.setMemo(controller.getValue());
            } else if (name.equals(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_executeResult))) {//执行结果
                int executeResult = DictTemp.getInstance().getExecuteResultCodeByName(controller.getValue());
                if (ExecuteResultEnum.isExecuted(executeResult)) {//执行完成，才填写停止时间
                    execute.setStopNurseId(UserTemp.getInstance().getUserId());
                    execute.setStopNurseName(UserTemp.getInstance().getUserName());
                    execute.setStopDateTime(TimeUtils.getCurrentDate().getTime());
                }
                execute.setExecuteResult(executeResult);
            } else if (name.equals(ArmsUtils.getString(MobileNurseApplication.getInstance(), R.string.common_STResult))) {//皮试结果
                execute.setSkinTestResult(getExecuteResultCodeByName(controller.getValue()));
                execute.setSkinTestDateTime(TimeUtils.getCurrentDate().getTime());
                execute.setSkinTestNurseId(UserTemp.getInstance().getUserId());
                execute.setSkinTestNurseName(UserTemp.getInstance().getUserName());
            }
        }
        if (TextUtils.isEmpty(execute.getProcessingNurseId())) {
            execute.setProcessingNurseId(UserTemp.getInstance().getUserId());
            execute.setProcessingNurseName(UserTemp.getInstance().getUserName());
            execute.setExecuteDateTime(TimeUtils.getCurrentDate().getTime());
        }
        return execute;
    }


    /**
     * 构建医嘱执行对象
     *
     * @param order
     * @param skinTestResult
     * @param executeResult
     * @return
     */
    public static OrdersExecute buildGroupOrderExecute(Order order, Integer skinTestResult, Integer executeResult) {
        //已执行或者医嘱为空，则不构建
        if (order == null || ExecuteResultEnum.isExecuted(order.getExecuteResult()))
            return null;
        OrdersExecute execute = new OrdersExecute();
        execute.setContinuedType(order.getContinuedType());
        execute.setDoubledSignIndicator(order.getDoubledSignIndicator());
        int skinTestIndicator = 0;
        if (order.getSkinTestFlag() != null && order.getSkinTestFlag() >= 1 && order.getSkinTestFlag() <= 4) {
            skinTestIndicator = 1;
        }
        execute.setSkinTestIndicator(skinTestIndicator);
        execute.setOrderId(order.getOrderId());
        execute.setPatientId(order.getPatientId());
        execute.setRecordId(order.getRecordId());
        execute.setVisitNo(order.getVisitNo());
        execute.setExecuteDateTime(order.getExecuteDateTime());
        execute.setExecuteResult(order.getExecuteResult());
        execute.setProcessingNurseId(order.getProcessingNurseId());
        execute.setProcessingNurseName(order.getProcessingNurseName());
        execute.setStopDateTime(order.getStopDateTime());
        execute.setStopNurseId(order.getStopNurseId());
        execute.setStopNurseName(order.getStopNurseName());
        execute.setExecuteDosage(order.getExecuteDosage());
        if (TextUtils.isEmpty(execute.getProcessingNurseId())) {
            execute.setProcessingNurseId(UserTemp.getInstance().getUserId());
            execute.setProcessingNurseName(UserTemp.getInstance().getUserName());
            execute.setExecuteDateTime(TimeUtils.getCurrentDate().getTime());
        }
        if (executeResult != null && ExecuteResultEnum.isExecuted(executeResult)) {
            execute.setStopNurseId(UserTemp.getInstance().getUserId());
            execute.setStopNurseName(UserTemp.getInstance().getUserName());
            execute.setStopDateTime(TimeUtils.getCurrentDate().getTime());
        }
        if (execute.getExecuteDosage() == null && order.getIsShowExecuteDosage()) {//默认执行计量为100
            execute.setExecuteDosage(100.0);
        }
        execute.setExecuteResult(executeResult);
        if (skinTestResult != null) {
            execute.setSkinTestResult(skinTestResult);
            execute.setSkinTestDateTime(TimeUtils.getCurrentDate().getTime());
            execute.setSkinTestNurseId(UserTemp.getInstance().getUserId());
            execute.setSkinTestNurseName(UserTemp.getInstance().getUserName());
        }
        return execute;
    }

    /**
     * 根据皮试数据获取皮试Code
     *
     * @param value
     * @return
     */
    private static Integer getExecuteResultCodeByName(String value) {
        if (TextUtils.isEmpty(value))
            return null;
        return value.equals(CommonConstant.COMMON_VALUE_POSITIVE) ? 1 : value.equals(CommonConstant.COMMON_VALUE_NEGATIVE) ? 2 : null;
    }

    /**
     * 根据groupRecordId 获取到医嘱列表
     *
     * @param orderList
     * @param groupRecordId 请参照{@link Order#recordGroupId}
     * @return
     */
    public static List<Order> getOrderListByGroupRecordId(List<Order> orderList, String
            groupRecordId) {
        if (orderList == null || orderList.isEmpty() || TextUtils.isEmpty(groupRecordId))
            return null;
        List<Order> result = new ArrayList<>();
        for (int x = 0; x < orderList.size(); x++) {
            Order order = orderList.get(x);
            if (order.getRecordGroupId().equals(groupRecordId)) {
                result.add(order);
            } else {
                if (!result.isEmpty())//因为医嘱列表是已经排好序的，所以当拿到了我们要的一组医嘱，就没必要遍历下去
                    return result;
            }
        }
        return result;
    }

    /**
     * 根据扫描到的医嘱条码，在医嘱列表中找到可执行的医嘱，如果未找到可执行的医嘱，则返回找到的第一组医嘱
     *
     * @param orderCode
     * @return 列表中如果找到了该组医嘱的可执行医嘱，则返回可执行医嘱，否则返回首次遍历到的医嘱
     */
    public static List<Order> getExecuteOrderListByOrderCode
    (List<Order> orderList, ScanResult orderCode) {
        if (orderList == null || orderList.isEmpty() || orderCode == null || orderCode.getCodeType() != ScanResult.CODE_TYPE_ORDER)
            return null;
        List<Order> firstOrderList = new ArrayList<>();
        List<Order> canExecuteOrderList = new ArrayList<>();
        String firstRecordGroupId = "";
        String canExecuteRecordGroupId = "";
        for (Order order : orderList) {
            if (order.getPatientId().equals(orderCode.getPatientId()) &&
                    order.getVisitId().equals(orderCode.getVisitId()) &&
                    order.getGroupNo().equals(orderCode.getOrderNo())) {
                if (TextUtils.isEmpty(firstRecordGroupId))//第一组的Id
                    firstRecordGroupId = order.getRecordGroupId();
                if (firstRecordGroupId.equals(order.getRecordGroupId()))
                    firstOrderList.add(order);
                int executeResult = order.getExecuteResult();
                if (ExecuteResultEnum.canExecute(executeResult)) {//该条医嘱可执行
                    if (TextUtils.isEmpty(canExecuteRecordGroupId))//记录可执行医嘱的Id
                        canExecuteRecordGroupId = order.getRecordGroupId();
                    if (order.getRecordGroupId().equals(canExecuteRecordGroupId)) {//是我们要找的可执行医嘱
                        canExecuteOrderList.add(order);
                    }
                }
            }
        }
        if (canExecuteOrderList.isEmpty())
            return firstOrderList;
        return canExecuteOrderList;
    }

    /**
     * 根据扫描的医嘱条码，获取该组医嘱中正在执行的医嘱
     *
     * @param orderList
     * @param orderCode
     * @return
     */
    public static List<Order> getExecutingOrderListByOrderCode
    (List<Order> orderList, ScanResult orderCode) {
        //TODO 省人民暂时不做执行中状态
//        if (orderList == null || orderList.isEmpty() || orderCode == null || orderCode.getCodeType() != ScanResult.CODE_TYPE_ORDER)
//            return null;
//        List<Order> executingOrderList = new ArrayList<>();
//        for (Order order : orderList) {
//            if (order.getPatientId().equals(orderCode.getPatientId()) &&
//                    order.getVisitId().equals(orderCode.getVisitId()) &&
//                    order.getGroupNo().equals(orderCode.getOrderNo()) &&
//                    order.getExecuteResult() == ExecuteResultEnum.EXECUTING.getCode()) {
//                executingOrderList.add(order);
//            }
//        }
//        return executingOrderList;
        return null;
    }

    /**
     * 根据医嘱列表，构建医嘱内容字符串
     *
     * @param orderList
     * @return
     */
    public static String buildOrderTextByOrderList(List<Order> orderList) {
        StringBuilder orderText = new StringBuilder();
        for (int x = 0; x < orderList.size(); x++) {
            Order order = orderList.get(x);
            if (order.isGroupStart() && x != 0) {//不是开头项，并且是一组的开头，要与前一组区分开
                orderText.append("。|" + order.getOrderText());
            } else if (x == 0) {
                orderText.append(order.getOrderText());
            } else {
                orderText.append("；" + order.getOrderText());
                if (x == orderList.size() - 1)
                    orderText.append("。");
            }
        }
        return orderText.toString();
    }

    /**
     * 判断当前执行的医嘱是否需要双签名
     *
     * @param orderList
     * @return
     */
    public static boolean isDoubleSignature(List<Order> orderList) {
        if (orderList == null)
            return false;
        for (Order order : orderList) {
            if (order.getIsDoubleSignature())
                return true;
        }
        return false;
    }

    /**
     * 查找双签名的医嘱
     *
     * @param orderList
     * @return
     */
    public static Order getDoubleSignature(List<Order> orderList) {
        if (orderList == null)
            return null;
        for (Order order : orderList) {
            if (order.getIsDoubleSignature())
                return order;
        }
        return null;
    }
}
