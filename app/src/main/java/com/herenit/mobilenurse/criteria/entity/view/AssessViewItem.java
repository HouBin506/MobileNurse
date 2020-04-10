package com.herenit.mobilenurse.criteria.entity.view;

import android.text.TextUtils;

import com.herenit.mobilenurse.app.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 评估界面的配置实体类
 * <p>
 * Created by HouBin on 2018/9/5.
 */

public class AssessViewItem implements Serializable {

    public static final String DATA_TYPE_NUMBER = "数值";
    public static final String DATA_TYPE_DATE = "日期";
    public static final String DATA_TYPE_CHARACTER = "字符";
    public static final String DATA_TYPE_BOOLEAN = "布尔";

    public static final String DATA_VALUE_TRUE = "True";

    /**
     * 表示 成组评估的节点，其下面的Item都属于这个组，比如“一般资料”
     */
    public static final String MODEL_TYPE_GROUP = "Group";
    /**
     * 表示某条评估项，比如"费用支付方式"这一项评估；
     */
    public static final String MODEL_TYPE_ITEM = "Item";
    /**
     * 表示 评估操作项，该项评估的值，就是这个操作项的操作结果；
     */
    public static final String MODEL_TYPE_VALUE = "Value";
    /**
     * 表示评估操作项的子项，用于对评估某操作的补充操作，比如“部位”、“面积”等
     */
    public static final String MODEL_TYPE_SUBVALUE = "SubValue";

    /**
     * 评估输入框，（前缀，比如输入框内容描述）TextView+EditText+TextView(后缀，比如单位)
     */
    public static final String ASSESS_VIEW_TEXT_INPUT_VIEW = "AssessTextInputView";
    /**
     * 普通输入框
     */
    public static final String ASSESS_VIEW_EDIT_TEXT = "EditText";
    /**
     * 评估日期控件，点击可选择日期，选择之后可将如期填写到TextView上，TextView+日历控件
     */
    public static final String ASSESS_VIEW_CALENDAR = "AssessCalendar";
    /**
     * 普通的CheckBox选择框
     */
    public static final String ASSESS_VIEW_CKECK_BOX = "CheckBox";
    /**
     * 评估单选
     */
    public static final String ASSESS_VIEW_RADIO_GROUP = "AssessRadioGroup";
    public static final String ASSESS_VIEW_RADIO_BUTTON = "AssessRadioButton";
    /**
     * 评估 普通文本框
     */
    public static final String ASSESS_VIEW_TEXT_VIEW = "TextView";
    /**
     * 评估  两个输入框  血压输入框 （专门针对血压等项目），当时用此控件时，字段“unit”表示“单位 连接符 单位”，将两个输入框后面的单位以及连接符用空格分开
     * 比如" | mmHg" 表示血压，第一个输入框后面无单位，两个输入框用“|”符号连接，第二个输入框后面有单位“mmHg”。
     * 再比如"cm x cm"表示两个数相乘，前后两个输入框后面跟着的单位均为“cm”，两输入框用“x”连接   最后得到的value（dataValue）：第一个输入框的值x第二个输入框的值
     */
    public static final String ASSESS_VIEW_DOUBLE_INPUT_VIEW = "AssessDoubleInputView";

    public AssessViewItem() {
        super();
    }

    //评估项目的id，可作为存入数据库的key
    private String id;
    //控件名称，可决定该项目加载哪个控件，目前涉及的控件类型，见下文
    private String viewName;
    //当前项目的数据类型  有 “日期”、“数值”、“字符”、“布尔”
    private String dataType;
    //当前项目名称，一般在控件上显示
    private String text;
    //当前项目的评估数值   用户填写或者选择操作的结果，可作为数据库的Value
    private String dataValue;
    //文字的尺寸   “Large”、“Normal”、“small”
    private String textSize;
    //评估项目的单位  比如“kg”、“℃”等
    private String unit;
    //是否处于被选中状态  一般用于CheckBox等可选择的控件
    private boolean checked;
    /**
     * model类型，四种情况
     * “Item”表示某条评估项，比如"费用支付方式"这一项评估；
     * “Value”表示 评估操作项，该项评估的值，就是这个操作项的操作结果；
     * “Group” 表示 成组评估的节点，其下面的Item都属于这个组，比如“一般资料”
     * “SubValue” 表示评估操作项的子项，用于对评估某操作的补充操作，比如“部位”、“面积”等
     */
    private String modelType;
    //是否是只读状态，如果是只读的，则该控件不可编辑
    private boolean readOnly;

    /**
     * 针对某些选择类型列表，可以将选择项以“|”符号隔开写入
     */
    private String valueDataList;

    /**
     * 公式，有些控件上面的数据，是通过计算获得。比如“ 体重指数BMI_1=体重（w）_1/(身高（H）_1*身高（H）_1)”
     * 定义规则：比如 体重指数BMI_1=体重（w）_1/(身高（H）_1*身高（H）_1)
     * 1，必须有“=”符号，并且等号左边是值，等号右边是计算公式，理论上只允许拥有一个“=”
     * 2，计算公式目前只支持加、减、乘、除，使用“()”将需要优先计算的数据包装起来，注意：括号必须为英文格式
     */
    private String formula;
    //当 viewName 为单选或者多选列表时，checkList有值，就是子选项
    private List<AssessViewItem> subModel;

    //用于列表显示的展示给用户的数据
    private String showDataValue;

    public String getValueDataList() {
        return valueDataList;
    }

    public void setValueDataList(String valueDataList) {
        this.valueDataList = valueDataList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getTextSize() {
        return textSize;
    }

    public void setTextSize(String textSize) {
        this.textSize = textSize;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public List<AssessViewItem> getSubModel() {
        return subModel;
    }

    public void setSubModel(List<AssessViewItem> subModel) {
        this.subModel = subModel;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }


    public String getShowDataValue() {
        return showDataValue;
    }

    public void setShowDataValue(String showDataValue) {
        this.showDataValue = showDataValue;
    }

    /**
     * 构建Mode要显示到列表上的数据
     */
    public void buildItemValue() {
        if (subModel == null || subModel.size() == 0)
            return;
        if (MODEL_TYPE_ITEM.equals(modelType))
            setShowDataValue("");
        if (MODEL_TYPE_VALUE.equals(modelType)) {
            if (!TextUtils.isEmpty(showDataValue) && showDataValue.contains("(") && showDataValue.contains(")")) {
                String subValue = showDataValue.substring(showDataValue.indexOf("("), showDataValue.indexOf(")") + 1);
                showDataValue = showDataValue.replace(subValue, "");
            }
        }
        for (int x = 0; x < subModel.size(); x++) {
            AssessViewItem sub = subModel.get(x);
            String type = sub.getModelType();
            if (MODEL_TYPE_GROUP.equals(modelType)) {//当modeType表示组时候，不设置，继续设置其子项（Item）
                sub.buildItemValue();
            } else if (MODEL_TYPE_ITEM.equals(modelType)) {//当modeType为Item，将modeType为Value的dateType设置给Item
                if (MODEL_TYPE_VALUE.equals(type)) {
                    String value = sub.getShowDataValue();
                    if (TextUtils.isEmpty(value))
                        continue;
                    setShowDataValue(TextUtils.isEmpty(getShowDataValue()) ? value : getShowDataValue() + "," + value);
                } else {
                    sub.buildItemValue();
                }
            } else if (MODEL_TYPE_VALUE.equals(modelType)) {//当modeType为Value，将modeType为SubValue的dateType设置给Value
                if (MODEL_TYPE_SUBVALUE.equals(type)) {
                    String value = sub.getShowDataValue();
                    if (TextUtils.isEmpty(value))
                        continue;
                    if (TextUtils.isEmpty(getShowDataValue()) || !getShowDataValue().contains("(") || !getShowDataValue().contains(")"))
                        setShowDataValue(getShowDataValue() + "()");
                    String subStrStart = getShowDataValue().substring(0, getShowDataValue().indexOf("(") + 1);
                    String subStrValue = getShowDataValue().substring(getShowDataValue().indexOf("(") + 1, getShowDataValue().indexOf(")"));
                    String subStrEnd = getShowDataValue().substring(getShowDataValue().indexOf(")"));
                    subStrValue = TextUtils.isEmpty(subStrValue) ? value : subStrValue + "," + value;
                    setShowDataValue(subStrStart + subStrValue + subStrEnd);
                } else {
                    sub.buildItemValue();
                }
            } else if (MODEL_TYPE_SUBVALUE.equals(modelType)) {//默认的modeType的最内层只能到SubValue
                return;
            }
        }
    }

    /**
     * 克隆一个新的实例
     *
     * @param model
     * @return
     */
    public static AssessViewItem clone(AssessViewItem model) {
        AssessViewItem clone = new AssessViewItem();
        clone.setId(model.getId());
        clone.setViewName(model.getViewName());
        clone.setDataType(model.getDataType());
        clone.setText(model.getText());
        clone.setDataValue(model.getDataValue());
        clone.setTextSize(model.getTextSize());
        clone.setUnit(model.getUnit());
        clone.setChecked(model.isChecked());
        clone.setReadOnly(model.isReadOnly());
        clone.setFormula(model.getFormula());
        clone.setModelType(model.getModelType());
        clone.setShowDataValue(model.getShowDataValue());
        clone.setValueDataList(model.getValueDataList());
        if (model.getSubModel() != null) {
            List<AssessViewItem> subModel = new ArrayList<>();
            for (AssessViewItem assessModel : model.getSubModel()) {
                subModel.add(AssessViewItem.clone(assessModel));
            }
            clone.setSubModel(subModel);
        }
        return clone;
    }

    /**
     * 将某AssessModel上的值赋给另一个AssessModel对象
     *
     * @param destModel  要被赋值的对象
     * @param sourceMode 赋值的对象（数据从此对象来，赋值给另一个对象 destModel）
     * @return 返回被赋值后的对象  destModel
     */
    public static void assignModelValue(AssessViewItem destModel, AssessViewItem sourceMode) {
        if (!destModel.getId().equals(sourceMode.getId()))
            return;
        destModel.setChecked(sourceMode.isChecked());
        destModel.setDataValue(sourceMode.getDataValue());
        destModel.setShowDataValue(sourceMode.getShowDataValue());
        List<AssessViewItem> destSubModel = destModel.getSubModel();
        List<AssessViewItem> sourceSubModel = sourceMode.getSubModel();
        if (destSubModel != null && sourceSubModel != null && destSubModel.size() > 0 && destSubModel.size() == sourceSubModel.size()) {
            for (int x = 0; x < destSubModel.size(); x++) {
                assignModelValue(destSubModel.get(x), sourceSubModel.get(x));
            }
        }
    }

    /**
     * 清除AssessMode数据
     */
    public void clearData() {
        setDataValue("");
        setShowDataValue("");
        setChecked(false);
        if (getSubModel() != null) {
            for (AssessViewItem model : getSubModel()) {
                model.clearData();
            }
        }
    }

    public String parseShowDataValue() {
        String unit = getUnit();
        String value = getDataValue();
        if (TextUtils.isEmpty(value))
            return "";
        if (TextUtils.isEmpty(unit))
            return value;
        if (!unit.contains(" "))
            return value + " " + unit;
        String[] units = unit.split(" ");
        if (units == null || units.length != 3)
            return value + " " + unit;
        String unit1 = units[0];
        String connector = units[1];
        String unit2 = units[2];
        String value1 = "";
        String value2 = "";
        if (TextUtils.isEmpty(connector) || !value.contains(connector))
            return value + " " + unit;
        int index = value.indexOf(connector);
        char[] valueArr = value.toCharArray();
        for (int x = 0; x < valueArr.length; x++) {
            char c = valueArr[x];
            if (x < index)
                value1 = value1 + c;
            else if (x > index)
                value2 = value2 + c;
        }
        return (TextUtils.isEmpty(value1) ? "" : value1 + " " + unit1) + connector + (TextUtils.isEmpty(value2) ? "" : value2 + " " + unit2);
    }

    /**
     * 根据计算公式设置一些需要自动计算的值
     *
     * @param modelList
     */
    public void setDataValueByFormula(List<AssessViewItem> modelList) {
        if (getSubModel() != null && getSubModel().size() > 0) {
            for (AssessViewItem model : getSubModel()) {
                model.setDataValueByFormula(modelList);
            }
        } else {
            if (TextUtils.isEmpty(formula) || modelList == null || modelList.size() == 0)
                return;
            //说明计算公式不规则或者与当前控件的值无关
            if (!formula.contains(id) || !formula.contains("="))//要求计算公式必须有“=”号
                return;
            String[] strArr = formula.split("=");
            if (strArr == null || strArr.length != 2)
                return;
            String resultId = strArr[0];
            String formulaStr = strArr[1];
            String result = calculate(formulaStr, modelList);
            setDataValueById(resultId, Utils.setNumberScale(result, 2), modelList);
            for (AssessViewItem model : modelList) {
                model.buildItemValue();
            }
        }
    }

    /**
     * 根据给出的计算公式
     *
     * @param formulaStr
     * @param modelList
     * @return
     */
    public String calculate(String formulaStr, List<AssessViewItem> modelList) {
        if (TextUtils.isEmpty(formulaStr))
            return null;
        String result = "";
        formulaStr = formulaStr.trim().replaceAll(" ", "");
        int[] indexArr = getParenthesisIndex(formulaStr);
        if (indexArr == null) {
            result = calculateSimpleFormula(formulaStr, modelList);
        } else if (indexArr.length == 2) {
            int startIndex = indexArr[0];
            int endIndex = indexArr[1];
            String start = formulaStr.substring(0, startIndex);
            String end = formulaStr.substring(endIndex + 1);
            String pValue = formulaStr.substring(startIndex + 1, endIndex);
            result = start + calculateSimpleFormula(pValue, modelList) + end;
        } else {
            return null;
        }
        if (!TextUtils.isEmpty(result) && !Utils.isIntOrFloat(result))
            result = calculate(result, modelList);
        return result;
    }


    /**
     * 计算简单的公式 不包含小括号的，只有 + - * / 连接符
     *
     * @param formulaStr
     * @return
     */
    public String calculateSimpleFormula(String formulaStr, List<AssessViewItem> modelList) {
        String result = "";
        if (!formulaStr.contains("(") && !formulaStr.contains(")")) {
            if (!(formulaStr.contains("+") || formulaStr.contains("-") || formulaStr.contains("*") || formulaStr.contains("/")))
                return null;
            List<Float> params = new ArrayList<>();//每一个参数
            List<String> signs = new ArrayList<>();//每一个运算符号
            char[] charArr = formulaStr.toCharArray();
            String temp = "";
            for (int x = 0; x < charArr.length; x++) {
                String str = String.valueOf(charArr[x]);
                if (!TextUtils.isEmpty(str) && (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/"))) {
                    if (!TextUtils.isEmpty(temp)) {
                        if (Utils.isIntOrFloat(temp) && Utils.string2Float(temp) != null) {
                            params.add(Utils.string2Float(temp));
                        } else {
                            String valueStr = getDataValueById(temp, modelList);
                            if (!TextUtils.isEmpty(valueStr) && Utils.isIntOrFloat(valueStr) && Utils.string2Float(valueStr) != null)
                                params.add(Utils.string2Float(valueStr));
                        }
                        signs.add(str);
                    }
                    temp = "";
                } else if (!TextUtils.isEmpty(str)) {
                    temp += str;
                }
                if (x == charArr.length - 1 && !TextUtils.isEmpty(temp)) {
                    if (Utils.isIntOrFloat(temp) && Utils.string2Float(temp) != null) {
                        params.add(Utils.string2Float(temp));
                    } else {
                        String valueStr = getDataValueById(temp, modelList);
                        if (!TextUtils.isEmpty(valueStr) && Utils.isIntOrFloat(valueStr) && Utils.string2Float(valueStr) != null)
                            params.add(Utils.string2Float(valueStr));
                    }
                }
            }
            Float value = getResult(params, signs);
            if (value != null)
                result = String.valueOf(value);
        } else {
            return null;
        }
        return result;
    }


    /**
     * 根据计算公式，获取小括号的角标，每次只能获取一对小括号的角标
     *
     * @param formulaStr
     * @return
     */
    public int[] getParenthesisIndex(String formulaStr) {
        int[] indexArr = null;
        if (formulaStr.contains("(") && formulaStr.contains(")")) {
            int start = formulaStr.lastIndexOf("(");
            String subStr = formulaStr.substring(start + 1);
            int end = start + subStr.indexOf(")") + 1;
            if (start >= 0 && end >= 0 && end > start) {
                indexArr = new int[2];
                indexArr[0] = start;
                indexArr[1] = end;
            }
        }
        return indexArr;
    }

    /**
     * 根据给出的参数和运算符号，获取计算结果
     *
     * @param params 参数
     * @param signs  运算符号（只有+ - * /）
     * @return
     */
    private Float getResult(List<Float> params, List<String> signs) {
        if (params == null || params.size() == 0)
            return null;
        if (signs == null || signs.size() == 0)
            return null;
        if (params.size() != signs.size() + 1)
            return null;
        while (signs.contains("*") || signs.contains("/")) {
            int index = signs.indexOf("*");
            if (index >= 0) {
                float value = params.get(index) * params.get(index + 1);
                params.remove(index);
                params.remove(index);
                signs.remove(index);
                params.add(index, value);
            }
            index = signs.indexOf("/");
            if (index >= 0) {
                float value = params.get(index) / params.get(index + 1);
                params.remove(index);
                params.remove(index);
                signs.remove(index);
                params.add(index, value);
            }
        }
        Float result = null;
        if (params.size() != 0)
            result = params.get(0);
        if (params.size() != signs.size() + 1)
            return result;
        for (int x = 0; x < signs.size(); x++) {
            String sign = signs.get(x);
            if (sign.equals("+")) {
                result += params.get(x + 1);
            } else if (sign.equals("-")) {
                result -= params.get(x + 1);
            }
        }
        return result;
    }


    /**
     * 根据Id给某条数据赋值
     *
     * @param resultId
     * @param result
     */
    private void setDataValueById(String resultId, String result, List<AssessViewItem> modelList) {
        if (TextUtils.isEmpty(resultId) || modelList == null || modelList.size() == 0)
            return;
        for (AssessViewItem model : modelList) {
            if (resultId.equals(model.getId())) {
                model.setDataValue(result);
                model.setShowDataValue(result);
                return;
            } else if (model.getSubModel() != null && model.getSubModel().size() > 0) {
                setDataValueById(resultId, result, model.getSubModel());
            } else {
                return;
            }
        }
    }

    /**
     * 根据Id获取对应的值
     *
     * @param id
     * @return
     */
    private String getDataValueById(String id, List<AssessViewItem> modelList) {
        if (TextUtils.isEmpty(id) || modelList == null || modelList.size() <= 0)
            return null;
        for (AssessViewItem model : modelList) {
            if (model.getSubModel() != null && model.getSubModel().size() > 0) {
                String value = getDataValueById(id, model.getSubModel());
                if (!TextUtils.isEmpty(value))
                    return value;
            }
            if (id.equals(model.getId())) {
                return model.getDataValue();
            }
        }
        return "";
    }


    @Override
    public String toString() {
        return "AssessViewItem{" +
                "id='" + id + '\'' +
                ", viewName='" + viewName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", text='" + text + '\'' +
                ", dataValue='" + dataValue + '\'' +
                ", textSize='" + textSize + '\'' +
                ", unit='" + unit + '\'' +
                ", checked=" + checked +
                ", modelType='" + modelType + '\'' +
                ", readOnly=" + readOnly +
                ", formula='" + formula + '\'' +
                ", subModel=" + subModel +
                ", showDataValue='" + showDataValue + '\'' +
                ", valueDataList='" + valueDataList + '\'' +
                '}';
    }
}
