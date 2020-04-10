package com.herenit.mobilenurse.criteria.entity.view;

import android.text.TextUtils;

import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;

import java.io.Serializable;

/**
 * author: HouBin
 * date: 2019/4/11 15:40
 * desc: 体征项目UI的Item
 */
public class VitalSignsViewItem implements Serializable, MultiItemDelegate {


    private static final long serialVersionUID = 3349752276291969756L;
    /**
     * 路径，表示该Item所处的层级关系
     */
    private String path;
    /**
     * 针对某些需要联合主键定位某项体征
     */
    private String classCode;
    /**
     * 体征项目ID，一条体征项的唯一标识
     */
    private String itemCode;
    /**
     * 体征项目名称
     */
    private String itemName;
    /**
     * 体征项描述
     */
    private String itemDescription;
    /**
     * 可输入值范围上限
     */
    private String inputUpperLimit;
    /**
     * 可输入值范围下限
     */
    private String inputLowerLimit;
    /**
     * 正常值范围上限
     */
    private String upperLimitValue;
    /**
     * 正常值范围下限
     */
    private String lowerLimitValue;
    /**
     * 特殊值或者例外值，比如“叵测、测不出、外出、不升、卧床”等
     */
    private String specialValueList;
    /**
     * 例外值控件类型（特殊值控件类型）
     */
    private String specialValueViewType;
    /**
     * 体征项单位
     */
    private String unit;
    /**
     * 是否可编辑，默认为true，可编辑
     */
    private boolean editable;
    /**
     * 体征录入频次，表示该项体征单位时间内需要记录的次数,比如（3次/日）
     */
    private String frequency;
    /**
     * 时间点集合，有些体征项目，有固定的录入时间点，比如“体温”的（02:00:00|06:00:00|10:00:00|14:00:00），"|"符号隔开固定时间录入方便绘制体温单
     */
    private String fixedTimePointList;

    /**
     * 是否有历史记录（一般情况，表示为当天该患者是否有该项体征的录入历史，根据PatientId、visitId、itemCode获得）
     */
    private boolean hasChart;
    /**
     * 体征项对应的值的操作控件类型，比如单输入框（SingleInputView），带前缀后缀的输入框（TextSingleInputView）,日历（Calendar）等，
     * 具体的需要前后端统一规定几个固定的控件名称
     */
    private String valueViewType;
    /**
     * 体征项的值的数据类型，Number、Date、Character等
     */
    private String valueDataType;
    /**
     * 是否录入备注，录入一项体征的时候，有时候会需要填写一个备注
     */
    private boolean useMemoView;

    /**
     * 是否录入值，有的体征项是不需要录入数值，只需要记录状态
     */
    private boolean useValueView;
    /**
     * 是否使用例外值（特殊值），不使用则不需要显示例外值
     */
    private boolean useSpecialValueView;


    /**
     * 当前条目类型，是Group还是Value或者ValueGroup...,
     * Group仅代表组的Item，没有实质的数据意义
     * Value表是该Item是数据操作条目。
     * ValueGroup表示该Item既有数据操作的意义，也算是组
     */
    private String itemType;
    /**
     * 正常值与特殊值（例外值）是否互斥，互斥的意思就是二者不能共存，填写了数值就不能选择例外值
     */
    private boolean isValueSpecialExclusive;
    /**
     * 是否为默认Item，如果未true，页面必须显示该项，否则，可根据用户配置来决定该项是否显示
     */
    private boolean isDefaultItem;
    /*********************************************由客户端操作录入*********************************************************/
    //填写的备注
    private String memo;
    //填写的数值
    private String value;
    //当前操作的子项，一般用于临夏州的血压
    private int currentSubIndex = 0;

    /**
     * 选择的录入时间点
     * 当 固定时间点不为空时，也就是fixedTimePoint不为空，说明此事使用固定时间点，
     * 最终提交到服务器端的录入时间点，应该是timePoint的“年月日”+fixedTimePoint的“时分”
     * 当固定时间点为空，也就是fixedTimePoint的值为空，说明此时不使用固定时间点，
     * 最终提交到服务器端的录入时间点，应该是timePoint的“年月日时分”
     */
    private Long timePoint;
    //选择的固定时间点
    private String fixedTimePoint;
    //填写的例外值（特殊值）
    private String specialValue;
    //是否提交，当为true，该条体征项的数据要提交到服务器
    private boolean checked;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getInputUpperLimit() {
        if (TextUtils.isEmpty(inputLowerLimit))
            return null;
        return inputUpperLimit;
    }

    public void setInputUpperLimit(String inputUpperLimit) {
        this.inputUpperLimit = inputUpperLimit;
    }

    public String getInputLowerLimit() {
        if (TextUtils.isEmpty(inputLowerLimit))
            return null;
        return inputLowerLimit;
    }

    public void setInputLowerLimit(String inputLowerLimit) {
        this.inputLowerLimit = inputLowerLimit;
    }

    public String getUpperLimitValue() {
        if (TextUtils.isEmpty(upperLimitValue))
            return null;
        return upperLimitValue;
    }

    public void setUpperLimitValue(String upperLimitValue) {
        this.upperLimitValue = upperLimitValue;
    }

    public String getLowerLimitValue() {
        if (TextUtils.isEmpty(lowerLimitValue))
            return null;
        return lowerLimitValue;
    }

    public void setLowerLimitValue(String lowerLimitValue) {
        this.lowerLimitValue = lowerLimitValue;
    }

    public String getSpecialValueList() {
        return specialValueList;
    }

    public void setSpecialValueList(String specialValueList) {
        this.specialValueList = specialValueList;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFixedTimePointList() {
        return fixedTimePointList;
    }

    public void setFixedTimePointList(String fixedTimePointList) {
        this.fixedTimePointList = fixedTimePointList;
    }

    public boolean isHasChart() {
        return hasChart;
    }

    public void setHasChart(boolean hasChart) {
        this.hasChart = hasChart;
    }

    public String getValueViewType() {
        return valueViewType;
    }

    public void setValueViewType(String valueViewType) {
        this.valueViewType = valueViewType;
    }

    public String getValueDataType() {
        return valueDataType;
    }

    public void setValueDataType(String valueDataType) {
        this.valueDataType = valueDataType;
    }


    public boolean isUseMemoView() {
        return useMemoView;
    }

    public void setUseMemoView(boolean useMemoView) {
        this.useMemoView = useMemoView;
    }

    public boolean isUseValueView() {
        return useValueView;
    }

    public void setUseValueView(boolean useValueView) {
        this.useValueView = useValueView;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public boolean isValueSpecialExclusive() {
        return isValueSpecialExclusive;
    }

    public void setValueSpecialExclusive(boolean valueSpecialExclusive) {
        isValueSpecialExclusive = valueSpecialExclusive;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(Long timePoint) {
        this.timePoint = timePoint;
    }

    public String getFixedTimePoint() {
        return fixedTimePoint;
    }

    public void setFixedTimePoint(String fixedTimePoint) {
        this.fixedTimePoint = fixedTimePoint;
    }

    public String getSpecialValue() {
        return specialValue;
    }

    public void setSpecialValue(String specialValue) {
        this.specialValue = specialValue;
    }

    public boolean isDefaultItem() {
        return isDefaultItem;
    }

    public void setDefaultItem(boolean defaultItem) {
        isDefaultItem = defaultItem;
    }

    public String getSpecialValueViewType() {
        return specialValueViewType;
    }

    public void setSpecialValueViewType(String specialValueViewType) {
        this.specialValueViewType = specialValueViewType;
    }

    public boolean isUseSpecialValueView() {
        return useSpecialValueView;
    }

    public void setUseSpecialValueView(boolean useSpecialValueView) {
        this.useSpecialValueView = useSpecialValueView;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getCurrentSubIndex() {
        return currentSubIndex;
    }

    public void setCurrentSubIndex(int currentSubIndex) {
        this.currentSubIndex = currentSubIndex;
    }
}
