package com.herenit.mobilenurse.criteria.entity.submit;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.ConditionItem;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/2/18 17:11
 * desc: 查询病床列表的条件
 */
public class SickbedListQuery extends TimeIntervalQuery {
    /**
     * 病情等级
     */
    private String patientCondition;

    /**
     * 护理等级
     */
    private String nursingClass;

    /**
     * 是否根据手术时间查询
     */
    private Boolean isOperation;


    public String getPatientCondition() {
        return patientCondition;
    }

    public void setPatientCondition(String patientCondition) {
        this.patientCondition = patientCondition;
    }

    public String getNursingClass() {
        return nursingClass;
    }

    public void setNursingClass(String nursingClass) {
        this.nursingClass = nursingClass;
    }

    public Boolean getOperation() {
        return isOperation;
    }

    public void setOperation(Boolean operation) {
        isOperation = operation;
    }

    private void queryAllOrNew(Conditions conditions) {
        List<ConditionItem> itemList = conditions.getConditions();
        for (ConditionItem item : itemList) {
            if (item.getId().equals(CommonConstant.COMMON_CODE_ALL) && item.isChecked()) {
                return;//查询所有
            } else if (item.getId().equals(CommonConstant.COMMON_CODE_NEW) && item.isChecked()) {//查询新入院的
                setStartDateTime(TimeUtils.getFirstTimeOfDay(TimeUtils.getCurrentDate()));
                setStopDateTime(TimeUtils.getLastTimeOfDay(TimeUtils.getCurrentDate()));
            }
        }
    }

    /**
     * 根据手术时间设置条件
     *
     * @param conditions
     */
    private void queryByOperationTime(Conditions conditions) {
        List<ConditionItem> itemList = conditions.getConditions();
        if (itemList == null || itemList.isEmpty())
            return;
        for (ConditionItem item : itemList) {
            if (!item.isChecked())
                continue;
            if (CommonConstant.COMMON_CODE_ALL.equals(item.getId())) {
                //所有时间，可理解为不设置
                setOperation(true);
                return;
            }
            setStartDateTime(TimeUtils.getStartDateTimeByTimeCode(item.getId()));
            setStopDateTime(TimeUtils.getStopDateTimeByTimeCode(item.getId()));
            setOperation(true);
        }
    }

    /**
     * 根据病情等级或者护理等级来设置查询条件
     */
    private void queryByPatientConditionOrNursingClass(Conditions conditions) {
        List<ConditionItem> itemList = conditions.getConditions();
        if (itemList == null || itemList.isEmpty())
            return;
        for (ConditionItem item : itemList) {
            if (!item.isChecked())
                continue;
            if (!TextUtils.isEmpty(item.getId()) && item.getId().equals(CommonConstant.COMMON_CODE_ALL))
                return;
            else if (conditions.getConditionType().equals(CommonConstant.CONDITION_TYPE_PATIENT_CONDITION))
                setPatientCondition(item.getId());
            else if (conditions.getConditionType().equals(CommonConstant.CONDITION_TYPE_NURSING_CLASS))
                setNursingClass(item.getId());
        }
    }

    /**
     * 根据页面的筛选条件选择，获取提交服务器端的查询条件
     *
     * @param conditionList
     */
    public static SickbedListQuery createQueryByCondition(List<Conditions> conditionList) {
        if (conditionList == null || conditionList.isEmpty())
            return null;
        SickbedListQuery query = createQueryAllSickbed(UserTemp.getInstance().getGroupCode());
        for (Conditions conditions : conditionList) {
            switch (conditions.getConditionType()) {
                case CommonConstant
                        .CONDITION_TYPE_ALL_OR_NEW://所有或者新入
                    query.queryAllOrNew(conditions);
                    break;
                case CommonConstant
                        .CONDITION_TYPE_OPERATION_TIME://手术时间
                    query.queryByOperationTime(conditions);
                    break;
                case CommonConstant
                        .CONDITION_TYPE_PATIENT_CONDITION://病情
                case CommonConstant
                        .CONDITION_TYPE_NURSING_CLASS://护理等级
                    query.queryByPatientConditionOrNursingClass(conditions);
                    break;
            }
        }
        return query;
    }


    /**
     * 查询所有患者
     *
     * @param groupCode
     * @return
     */
    public static SickbedListQuery createQueryAllSickbed(@NonNull String groupCode) {
        SickbedListQuery query = new SickbedListQuery();
        query.setGroupCode(groupCode);
        query.setOperation(false);
        return query;
    }


}
