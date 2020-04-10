package com.herenit.mobilenurse.criteria.entity.submit;

import android.content.ClipData;
import android.text.TextUtils;

import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.ConditionItem;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.ExecuteResultEnum;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/3/7 9:36
 * desc:
 */
public class OrderListQuery extends TimeIntervalQuery {

    //患者ID
    private String patientId;

    //就诊次
    private String visitId;

    //医嘱类别，支持多选
    private String orderClass;

    //医嘱执行类别
    private String labelId;

    //医嘱执行结果
    private String executeResult;

    //医嘱种类：长期、临时、临检
    private String repeatIndicator;

    private String babyNo;

    /**
     * 根据患者、条件列表获取向服务器端提交的查询条件
     *
     * @param sickbed
     * @param conditionList
     * @return
     */
    public static OrderListQuery createQuery(Sickbed sickbed, List<Conditions> conditionList) {
        OrderListQuery query = new OrderListQuery();
        if (sickbed != null) {
            query.setPatientId(sickbed.getPatientId());
            query.setGroupCode(sickbed.getDeptCode());
        }
        if (conditionList == null || conditionList.isEmpty())
            return null;
        for (Conditions condition : conditionList) {
            switch (condition.getConditionType()) {
                case CommonConstant.CONDITION_TYPE_EXECUTE_TIME://执行时间
                    query.setTimeIntervalCondition(condition);
                    break;
                case CommonConstant.CONDITION_TYPE_EXECUTE_RESULT://执行结果
                    query.setExecuteResultCondition(condition);
                    break;
                case CommonConstant.CONDITION_TYPE_ORDER_CLASS://医嘱类别
                    query.setOrderClassCondition(condition);
                case CommonConstant.CONDITION_TYPE_ORDER_PERFORM_LABEL://医嘱执行单类别
                    query.setOrderPerformLabelCondition(condition);
                    break;
                case CommonConstant.CONDITION_TYPE_REPEAT_INDICATOR://医嘱种类（长期、临时、备用医嘱等）
                    query.setRepeatIndicatorCondition(condition);
                    break;
                case CommonConstant.CONDITION_TYPE_MATERNAL_AND_INFANTS://如果是母婴的话，要选择是查询婴儿的还是母亲的
                    query.setBabyNoCondition(condition);
                    break;
            }
        }
        return query;
    }


    public static void changeCondition(List<Conditions> conditionList, String conditionType, List<String> selectedItemCode) {
        if (conditionList == null || conditionList.isEmpty() || TextUtils.isEmpty(conditionType) || selectedItemCode == null || selectedItemCode.isEmpty())
            return;
        for (Conditions condition : conditionList) {
            if (!conditionType.equals(condition.getConditionType()))
                continue;
            for (ConditionItem item : condition.getConditions()) {
                if (selectedItemCode.contains(item.getId()))
                    item.setChecked(true);
                else
                    item.setChecked(false);
            }
        }
    }

    /**
     * 设置执行结果条件
     *
     * @param condition
     */
    private void setExecuteResultCondition(Conditions condition) {
        List<ConditionItem> itemList = condition.getConditions();
        if (itemList == null || itemList.isEmpty())
            return;
        for (ConditionItem item : itemList) {
            if (item.isChecked()) {
                setExecuteResult(item.getId());
                return;
            }
        }
    }

    /**
     * 设置医嘱状态（长期、临时）查询条件
     *
     * @param condition
     */
    private void setRepeatIndicatorCondition(Conditions condition) {
        List<ConditionItem> itemList = condition.getConditions();
        if (itemList == null || itemList.isEmpty())
            return;
        for (ConditionItem item : itemList) {
            if (item.isChecked()) {
                setRepeatIndicator(item.getId());
                return;
            }
        }
    }

    /**
     * 设置医嘱类别条件
     *
     * @param condition
     */
    private void setOrderClassCondition(Conditions condition) {
        List<ConditionItem> itemList = condition.getConditions();
        if (itemList == null || itemList.isEmpty())
            return;
        for (ConditionItem item : itemList) {
            if (item.isChecked()) {
                setOrderClass(item.getId());
                return;
            }
        }
    }

    /**
     * 设置执行单类别条件
     *
     * @param condition
     */
    private void setOrderPerformLabelCondition(Conditions condition) {
        List<ConditionItem> itemList = condition.getConditions();
        if (itemList == null || itemList.isEmpty())
            return;
        for (ConditionItem item : itemList) {
            if (item.isChecked()) {
                setLabelId(item.getId());
                return;
            }
        }
    }


    /**
     * 根据条件设置时间范围
     *
     * @param condition
     */
    private void setTimeIntervalCondition(Conditions condition) {
        List<ConditionItem> itemList = condition.getConditions();
        if (itemList == null || itemList.isEmpty())
            return;
        for (ConditionItem item : itemList) {
            if (TextUtils.isEmpty(item.getId()))
                continue;
            if (item.isChecked()) {
                if (CommonConstant.VIEW_TYPE_CALENDAR.equals(item.getId())) {//日历，标准的格式下的时间字符串
                    setStartDateTime(TimeUtils.getStartDateTimeByFormat(item.getFormat(), item.getName()));
                    setStopDateTime(TimeUtils.getStopDateTimeByFormat(item.getFormat(), item.getName()));
                } else {//非日历下的非标准字符串表示的时间，例如“今天、明天、昨天”
                    setStartDateTime(TimeUtils.getStartDateTimeByTimeCode(item.getId()));
                    setStopDateTime(TimeUtils.getStopDateTimeByTimeCode(item.getId()));
                }
                return;
            }
        }
    }

    /**
     * 设置婴儿编号
     *
     * @param condition
     */
    private void setBabyNoCondition(Conditions condition) {
        List<ConditionItem> itemList = condition.getConditions();
        if (itemList == null || itemList.isEmpty())
            return;
        for (ConditionItem item : itemList) {
            if (item.isChecked()) {
                setBabyNo(item.getId());
                return;
            }
        }
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

    public String getOrderClass() {
        return orderClass;
    }

    public void setOrderClass(String orderClass) {
        this.orderClass = orderClass;
    }

    public String getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(String executeResult) {
        this.executeResult = executeResult;
    }

    public String getRepeatIndicator() {
        return repeatIndicator;
    }

    public void setRepeatIndicator(String repeatIndicator) {
        this.repeatIndicator = repeatIndicator;
    }

    public String getBabyNo() {
        return babyNo;
    }

    public void setBabyNo(String babyNo) {
        this.babyNo = babyNo;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    @Override
    public String toString() {
        return "OrderListQuery{" +
                "patientId='" + patientId + '\'' +
                ", visitId='" + visitId + '\'' +
                ", orderClass=" + orderClass +
                ", executeResult='" + executeResult + '\'' +
                ", repeatIndicator='" + repeatIndicator + '\'' +
                ", groupCode='" + groupCode + '\'' +
                ", startDateTime=" + startDateTime +
                ", stopDateTime=" + stopDateTime +
                '}';
    }
}
