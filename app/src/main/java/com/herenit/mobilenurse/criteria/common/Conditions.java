package com.herenit.mobilenurse.criteria.common;

import com.herenit.mobilenurse.criteria.constant.CommonConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/2/22 15:28
 * desc: 条件实体类，作为界面显示查询条件
 */
public class Conditions {
    /**
     * 该行条件选择类型，单选或者多选
     */
    private boolean isMultipleChoice;

    /**
     * 该行条件是否是唯一的，与其他行不共存的；
     * true：选择该行任意条件，其他行条件都将取消
     * false：该行可以与其他行的条件共存
     */
    private boolean isSingle;
    /**
     * 条件集合
     */
    private List<ConditionItem> conditions;
    /**
     * 条件类型（比如医嘱的执行分类还是执行状态；再比如是床卡的护理等级还是病情）
     */
    private String conditionType;

    public boolean isMultipleChoice() {
        return isMultipleChoice;
    }

    public void setMultipleChoice(boolean multipleChoice) {
        isMultipleChoice = multipleChoice;
    }


    public boolean isSingle() {
        return isSingle;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }

    public List<ConditionItem> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionItem> conditions) {
        this.conditions = conditions;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    /**
     * 构建一个Conditions对象，
     *
     * @param conditionType 条件类型（病情、护理等级  等）
     * @param list          具体数据
     * @return
     */
    public static Conditions createConditions(String conditionType, List<? extends NameCode> list) {
        if (list == null || list.isEmpty())
            return null;
        switch (conditionType) {
            case CommonConstant.CONDITION_TYPE_PATIENT_CONDITION://病情类型
                return createPatientConditions(list);
            case CommonConstant.CONDITION_TYPE_NURSING_CLASS://护理等级
                return createNursingClassConditions(list);
            case CommonConstant.CONDITION_TYPE_ORDER_CLASS://医嘱类别
                return createOrderClassConditions(list);
            case CommonConstant.CONDITION_TYPE_EXECUTE_RESULT://执行结果类型
                return createExecuteResultConditions(list);
            case CommonConstant.CONDITION_TYPE_ORDER_PERFORM_LABEL://执行单类别
                return createOrderPerformLabelConditions(list);
            case CommonConstant.CONDITION_TYPE_MATERNAL_AND_INFANTS://母婴选择
                return createMaternalAndInfantsConditions(list);
        }
        return null;
    }

    private static Conditions createMaternalAndInfantsConditions(List<? extends NameCode> list) {
        Conditions conditions = new Conditions();
        conditions.setConditionType(CommonConstant.CONDITION_TYPE_MATERNAL_AND_INFANTS);
        conditions.setMultipleChoice(false);
        conditions.setSingle(false);
        List<ConditionItem> itemList = new ArrayList<>();
        for (int x = 0; x < list.size(); x++) {
            NameCode nameCode = list.get(x);
            ConditionItem item = new ConditionItem();
            item.setId(nameCode.getCode());
            item.setName(nameCode.getName());
            item.setAll(false);
            itemList.add(item);
        }
        conditions.setConditions(itemList);
        return conditions;
    }

    /**
     * 创建Item是执行单类别的条件
     *
     * @param list
     * @return
     */
    private static Conditions createOrderPerformLabelConditions(List<? extends NameCode> list) {
        Conditions conditions = new Conditions();
        conditions.setConditionType(CommonConstant.CONDITION_TYPE_ORDER_PERFORM_LABEL);
        conditions.setMultipleChoice(false);
        conditions.setSingle(false);
        List<ConditionItem> itemList = new ArrayList<>();
        itemList.add(new ConditionItem(CommonConstant.COMMON_CODE_ALL, "所有执行单类别", true, true));
        for (int x = 0; x < list.size(); x++) {
            NameCode nameCode = list.get(x);
            ConditionItem item = new ConditionItem();
            item.setId(nameCode.getCode());
            item.setName(nameCode.getName());
            item.setAll(false);
            itemList.add(item);
        }
        conditions.setConditions(itemList);
        return conditions;
    }

    /**
     * 创建Item是执行结果类型的条件
     *
     * @param list
     * @return
     */
    private static Conditions createExecuteResultConditions(List<? extends NameCode> list) {
        Conditions conditions = new Conditions();
        conditions.setConditionType(CommonConstant.CONDITION_TYPE_EXECUTE_RESULT);
        conditions.setMultipleChoice(false);
        conditions.setSingle(false);
        List<ConditionItem> itemList = new ArrayList<>();
        itemList.add(new ConditionItem(CommonConstant.COMMON_CODE_ALL, "所有执行结果", true, true));
        for (int x = 0; x < list.size(); x++) {
            NameCode nameCode = list.get(x);
            ConditionItem item = new ConditionItem();
            item.setId(nameCode.getCode());
            item.setName(nameCode.getName());
            item.setAll(false);
            itemList.add(item);
        }
        conditions.setConditions(itemList);
        return conditions;
    }

    /**
     * 创建Item是医嘱类别的条件
     *
     * @param list
     * @return
     */
    private static Conditions createOrderClassConditions(List<? extends NameCode> list) {
        Conditions conditions = new Conditions();
        conditions.setConditionType(CommonConstant.CONDITION_TYPE_ORDER_CLASS);
        conditions.setMultipleChoice(false);
        conditions.setSingle(false);
        List<ConditionItem> itemList = new ArrayList<>();
        itemList.add(new ConditionItem(CommonConstant.COMMON_CODE_ALL, "所有医嘱类别", true, true));
        for (int x = 0; x < list.size(); x++) {
            NameCode nameCode = list.get(x);
            ConditionItem item = new ConditionItem();
            item.setId(nameCode.getCode());
            item.setName(nameCode.getName());
            item.setAll(false);
            itemList.add(item);
        }
        conditions.setConditions(itemList);
        return conditions;
    }

    /**
     * 创建条目是护理等级的条件
     *
     * @return
     */
    private static Conditions createNursingClassConditions(List<? extends NameCode> list) {
        Conditions conditions = new Conditions();
        conditions.setConditionType(CommonConstant.CONDITION_TYPE_NURSING_CLASS);
        conditions.setMultipleChoice(false);
        conditions.setSingle(false);
        List<ConditionItem> itemList = new ArrayList<>();
        itemList.add(new ConditionItem(CommonConstant.COMMON_CODE_ALL, "所有护理等级", true));
        for (int x = 0; x < list.size(); x++) {
            NameCode nameCode = list.get(x);
            ConditionItem item = new ConditionItem();
            item.setId(nameCode.getCode());
            item.setName(nameCode.getName());
            item.setAll(false);
            itemList.add(item);
        }
        conditions.setConditions(itemList);
        return conditions;
    }

    /**
     * 创建条目是病情类型的条件
     *
     * @param list
     * @return
     */
    private static Conditions createPatientConditions(List<? extends NameCode> list) {
        Conditions conditions = new Conditions();
        conditions.setConditionType(CommonConstant.CONDITION_TYPE_PATIENT_CONDITION);
        conditions.setMultipleChoice(false);
        conditions.setSingle(false);
        List<ConditionItem> itemList = new ArrayList<>();
        itemList.add(new ConditionItem(CommonConstant.COMMON_CODE_ALL, "所有病情", true));
        for (int x = 0; x < list.size(); x++) {
            NameCode nameCode = list.get(x);
            ConditionItem item = new ConditionItem();
            item.setId(nameCode.getCode());
            item.setName(nameCode.getName());
            item.setAll(false);
            itemList.add(item);
        }
        conditions.setConditions(itemList);
        return conditions;
    }

    /**
     * 从条件列表中获取选中的筛选条件的String表达
     *
     * @param conditionList
     * @return
     */
    public static String getSelectedConditionString(List<Conditions> conditionList) {
        if (conditionList == null || conditionList.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < conditionList.size(); x++) {
            Conditions condition = conditionList.get(x);
            for (int y = 0; y < condition.getConditions().size(); y++) {
                ConditionItem item = condition.getConditions().get(y);
                if (!item.isChecked())
                    continue;
                if (condition.isSingle()) {
                    return item.getName();
                } else {
                    sb.append(item.getName() + " ▪ ");
                }
            }
        }
        if (sb.length() > 2)
            sb.delete(sb.length() - 3, sb.length() - 1);
        return sb.toString().trim();
    }
}
