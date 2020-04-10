package com.herenit.mobilenurse.app.utils;

import android.text.TextUtils;
import android.widget.RelativeLayout;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.enums.IndicatorStatus;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/4/19 14:39
 * desc:MobileNurse（移动护理）特有的工具类，封装一些移动护理独有的功能
 */
public class MNUtils {

    private MNUtils() {
    }


    /**
     * 根据特殊格式的String，解析为Double数组
     *
     * @param content 传入的String格式为以一个“|”符号分割的字符串，两边均可以被解析为数值（Number）
     * @return 返回一个长度为2的Double数组，是将参数根据“|”符号拆分的结果,如果参数没有包含“|”符号
     * 或者参数为空，或者参数包含的|不止一个，则返回Double[]{null,null}
     */
    public static Double[] parseDoubleArrayByStringFormat(String content) {
        Double[] result = new Double[]{null, null};
        try {
            if (TextUtils.isEmpty(content))
                return result;
            if (!content.contains("|"))
                return result;
            String[] arr = content.split("\\|");
            if (arr == null || arr.length > 2)
                return result;
            Double double1 = null;
            Double double2 = null;
            if (!TextUtils.isEmpty(arr[0]))
                double1 = Double.parseDouble(arr[0]);
            if (arr.length == 2 && !TextUtils.isEmpty(arr[1]))
                double2 = Double.parseDouble(arr[1]);
            result[0] = double1;
            result[1] = double2;
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    /**
     * 将特殊格式的String解析为String[]
     *
     * @param content 传入的String格式为以一个“|”符号分割的字符串，
     * @return 返回一个长度为2的String数组，是将参数根据“|”符号拆分的结果,如果参数没有包含“|”符号或者参数为空，
     * 或者参数包含的|不止一个，则返回String[]{"",""}
     */
    public static String[] parseStringArrayByStringFormat(String content) {
        String[] result = new String[]{"", ""};
        try {
            if (TextUtils.isEmpty(content))
                return result;
            if (!content.contains("|"))
                return result;
            String[] arr = content.split("\\|");
            if (arr == null || arr.length > 2)
                return result;
            result[0] = arr[0];
            if (arr.length == 2)
                result[1] = arr[1];
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    /**
     * 将使用“|”分割的字符串，左边的替换掉
     *
     * @param value 原始值，“|”连接左右两边的值
     * @param left  左边部分的新值
     * @return 如果value为空，则直接左边加入值，如果value不为空切格式有问题，则返回value，否则替换掉返回新的值
     */
    public static String replaceLeftFormatString(String value, String left) {
        if (TextUtils.isEmpty(value)) {
            return TextUtils.isEmpty(left) ? "" : left + "|";
        }
        if (!value.contains("|"))
            return value;
        String[] valueArr = value.split("\\|");
        if (valueArr == null || valueArr.length > 2)
            return value;
        String result = "";
        if (valueArr.length == 1)
            result = left + "|";
        else
            result = left + "|" + valueArr[1];
        if (result.equals("|"))
            result = "";
        return result;
    }

    /**
     * 将使用“|”分割的字符串，右边边的替换掉
     *
     * @param value 原始值，“|”连接左右两边的值
     * @param right 左边部分的新值
     * @return 如果value为空，则直接左边加入值，如果value不为空切格式有问题，则返回value，否则替换掉返回新的值
     */
    public static String replaceRightFormatString(String value, String right) {
        if (TextUtils.isEmpty(value)) {
            return TextUtils.isEmpty(right) ? "" : "|" + right;
        }
        if (!value.contains("|"))
            return value;
        String[] valueArr = value.split("\\|");
        if (valueArr == null || valueArr.length > 2)
            return value;
        String result = valueArr[0] + "|" + right;
        if (result.equals("|"))
            result = "";
        return result;
    }

    /**
     * 将字符串解析成List<String>集合
     *
     * @param formatStr 要解析的字符串，使用“|”分割，
     * @return 使用“|”符号，将字符串分割成集合
     */
    public static List<String> parseStringListByFormatString(String formatStr) {
        if (TextUtils.isEmpty(formatStr))
            return null;
        if (!formatStr.contains("|")) {
            List<String> result = new ArrayList<>();
            result.add(formatStr);
            return result;
        }
        String[] strArr = formatStr.split("\\|");
        if (strArr == null || strArr.length <= 0)
            return null;
        return Arrays.asList(strArr);
    }

    /**
     * @param content         内容
     * @param inputUpperLimit 可输入值范围上限
     * @param inputLowerLimit 可输入值范围下限
     * @param upperLimitValue 正常值范围上限
     * @param lowerLimitValue 正常值范围下限
     * @return 返回某字符串的范围状态（正常、危险、错误）
     */
    public static IndicatorStatus parseStringStatus(String content, String inputUpperLimit, String inputLowerLimit, String upperLimitValue, String lowerLimitValue) {
        if (TextUtils.isEmpty(content))
            return IndicatorStatus.NORMAL;
        //将输入框中的数据变成double
        Double value;
        try {
            value = Double.valueOf(content);
        } catch (Exception e) {//字符串解析为double时出错
            return IndicatorStatus.ERROR;
        }
        if (value == null) {//非空字符串转化为double为空，则说明输入有误
            return IndicatorStatus.ERROR;
        }
        return getValueIndicator(value, StringUtils.stringToDouble(inputUpperLimit), StringUtils.stringToDouble(inputLowerLimit),
                StringUtils.stringToDouble(upperLimitValue), StringUtils.stringToDouble(lowerLimitValue));
    }

    /**
     * 根据当前输入框的值，与要求的输入的值的范围做比较，获取当前输入值所属指示状态：正常值、危险值、错误值
     *
     * @param value                 输入框的值
     * @param inputValueLimitUpper  可输入的值的范围上限
     * @param inputValueLimitLower  可输入的值的范围下限
     * @param normalValueLimitUpper 正常值范围上限
     * @param normalValueLimitLower 正常值范围下限
     * @return
     */
    public static IndicatorStatus getValueIndicator(Double value, Double inputValueLimitUpper, Double inputValueLimitLower,
                                                    Double normalValueLimitUpper, Double normalValueLimitLower) {
        if (value == null)
            return IndicatorStatus.NORMAL;
        if (inputValueLimitUpper == null && inputValueLimitLower == null
                && normalValueLimitUpper == null && normalValueLimitLower == null)//没有范围大小限制
            return IndicatorStatus.NORMAL;
        IndicatorStatus status = IndicatorStatus.NORMAL;
        if (normalValueLimitUpper != null && value > normalValueLimitUpper)//大于正常值上限
            status = IndicatorStatus.WARNING;
        if (normalValueLimitLower != null && value < normalValueLimitLower)//小于正常值下限
            status = IndicatorStatus.WARNING;
        if (inputValueLimitUpper != null && value > inputValueLimitUpper)//大于可输入值上限
            status = IndicatorStatus.ERROR;
        if (inputValueLimitLower != null && value < inputValueLimitLower)//小于可输入值下限
            status = IndicatorStatus.ERROR;
        return status;
    }

    /**
     * 根据字符串内容，以及给定的范围，获取对应的Html表达
     * 根据字符串中数字当前的状态（正常为白底黑色字体，危险为黄底黑色字体，错误为红色字体）
     *
     * @param content         内容
     * @param inputUpperLimit 可输入值范围上限
     * @param inputLowerLimit 可输入值范围下限
     * @param upperLimitValue 正常值范围上限
     * @param lowerLimitValue 正常值范围下限
     * @return
     */
    public static String getHtmlFontValueByLimit(String content, String inputUpperLimit, String inputLowerLimit, String upperLimitValue, String lowerLimitValue) {
        if (TextUtils.isEmpty(content))
            return null;
        String[] splitValue = content.split(" ");
        if (splitValue == null || splitValue.length == 0)
            return "<font color='#000000'>" + content + "</font>";
        String[] inputValueLimitUpperArr = parseStringArrayByStringFormatPro(inputUpperLimit);//可输入值的范围上限
        String[] inputValueLimitLowerArr = parseStringArrayByStringFormatPro(inputLowerLimit);//可输入值的范围下限
        String[] normalValueLimitUpperArr = parseStringArrayByStringFormatPro(upperLimitValue);//正常值的范围上限
        String[] normalValueLimitLowerArr = parseStringArrayByStringFormatPro(lowerLimitValue);//正常值的范围下限
        StringBuilder sbResult = new StringBuilder();
       /* <font color = '#555555' > 98 / </font ><font color = '#555555' > 78 < br / ></font >
    <font color = '#555555' > 120 / </font ><font color = '#555555' > 89 </font >*/
        for (int x = 0; x < splitValue.length; x++) {
            String value = splitValue[x];
            if (!TextUtils.isEmpty(value) && value.contains("|")) {//"|"分割的数据，一般出现在血压中
                String[] valueArr = value.split("\\|");
                if (valueArr == null || valueArr.length == 0)//格式有问题，直接返回
                    return "<font color='#000000'>" + content + "</font>";
                for (int y = 0; y < valueArr.length; y++) {//对拆分之后的值进行判断
                    String cValue = valueArr[y];
                    if (!TextUtils.isEmpty(cValue)) {//根据对应的值、取值范围，拼接带有背景色和字体色的HTML字符串
                        String color = getHtmlFontAndBgColor(cValue, y, inputValueLimitUpperArr, inputValueLimitLowerArr, normalValueLimitUpperArr, normalValueLimitLowerArr);
                        sbResult.append("<font color=\'" + color + "\'>");
                        if (y != 0)//第一项不添加“/”符号
                            sbResult.append("|" + cValue);
                        else
                            sbResult.append(cValue);
                        if (y == valueArr.length - 1 && sbResult.length() > 0 && x != splitValue.length - 1)
                            sbResult.append("<br/>");//一组数据结束换行
                        sbResult.append("</font>");
                    }
                }
            } else {
                String color = getHtmlFontAndBgColor(value, 0, inputValueLimitUpperArr, inputValueLimitLowerArr, normalValueLimitUpperArr, normalValueLimitLowerArr);
                sbResult.append("<font color=\'" + color + "\'>");
                sbResult.append(value);
                if (x != splitValue.length - 1)
                    sbResult.append("<br/>");//一组数据结束换行
                sbResult.append("</font>");
            }
        }
        return sbResult.toString();
    }

    /**
     * @param content
     * @param rangeH
     * @param rangeL
     * @return
     */
    public static String getLXHtmlFontBloodLimitValue(String content, String rangeH, String rangeL) {
        String[] splitValue;
        if (content.contains(" ")) {
            splitValue = content.split(" ");
        } else {
            splitValue = new String[1];
            splitValue[0] = content;
        }
        String rangeLeftH = "";
        String rangeRightH = "";
        String rangeLeftL = "";
        String rangeRightL = "";
        if (!TextUtils.isEmpty(rangeH) && rangeH.contains("|")) {
            //收缩压的上限
            rangeLeftH = rangeH.split("\\|")[0];
            //舒张压的上限
            rangeRightH = rangeH.split("\\|")[1];
        }
        if (!TextUtils.isEmpty(rangeL) && rangeL.contains("|")) {
            //收缩压的下限
            rangeLeftL = rangeL.split("\\|")[0];
            //舒张压
            rangeRightL = rangeL.split("\\|")[1];
        }
        StringBuilder str = new StringBuilder();
        for (int x = 0; x < splitValue.length; x++) {
            String bloodV = splitValue[x];
            String leftV = "";
            String rightV = "";
            if (!TextUtils.isEmpty(bloodV) && bloodV.contains("/")) {
                leftV = bloodV.split("/")[0];
                rightV = bloodV.split("/")[1];
            } else if (!TextUtils.isEmpty(bloodV)) {
                leftV = bloodV;
                rightV = "";
            } else {
                leftV = "";
                rightV = "";
            }
            if (!TextUtils.isEmpty(leftV)) {
                boolean isNormalValue = isValueNormal(leftV, rangeLeftH, rangeLeftL);
                if (isNormalValue)
                    str.append("<font color=\'#555555\'>");
                else
                    str.append("<font color=\'#ff3535\'>");
                str.append(leftV);
                if ((x != splitValue.length - 1) && TextUtils.isEmpty(rightV))
                    str.append("<br/>");
                else if (!TextUtils.isEmpty(rightV))
                    str.append("/");
                str.append("</font>");
            }
            if (!TextUtils.isEmpty(rightV)) {
                boolean isNormalValue = isValueNormal(rightV, rangeRightH, rangeRightL);
                if (isNormalValue)
                    str.append("<font color=\'#555555\'>");
                else
                    str.append("<font color=\'#ff3535\'>");
                str.append(rightV);
                if ((x != splitValue.length - 1))
                    str.append("<br/>");
                str.append("</font>");
            }
        }
        return str.toString();
    }

    /**
     * 根据当前体征项的正常值范围，判断值是否正常
     *
     * @param content
     * @param rangeH
     * @param rangeL
     */
    private static boolean isValueNormal(String content, String rangeH, String rangeL) {
        if (TextUtils.isEmpty(rangeH) && TextUtils.isEmpty(rangeL))
            return true;
        if (!content.matches("^[0-9]+(\\.[0-9]+)?$"))
            return false;
        float fContent = Float.valueOf(content);
        if (!TextUtils.isEmpty(rangeH)) {
            if (fContent >= Float.valueOf(rangeH))
                return false;
        }
        if (!TextUtils.isEmpty(rangeL)) {
            if (fContent <= Float.valueOf(rangeL))
                return false;
        }
        return true;
    }


    /**
     * 根据数据的值、范围，获取到数值的状态（正常、危险、错误），并且根据状态返回对应的字体背景、颜色的值
     *
     * @param value                    数值
     * @param index                    数值的角标 比如有的数据是 “100/89/99”，而传入的参数 value是100,则此时的index应该传入0
     * @param inputValueLimitUpperArr  可输入值范围上限
     * @param inputValueLimitLowerArr  可输入值范围下限
     * @param normalValueLimitUpperArr 正常值范围上限
     * @param normalValueLimitLowerArr 正常值范围下限
     * @return 返回长度为2的String数组，数组中的内容是Color的表示字符串
     * [0]表示html中的背景颜色，比如"red",[1]表示html中的额字体颜色，比如"#FF0000"
     */
    private static String getHtmlFontAndBgColor(String value, int index, String[] inputValueLimitUpperArr, String[] inputValueLimitLowerArr, String[] normalValueLimitUpperArr, String[] normalValueLimitLowerArr) {
        String inputUpperLimit = getLimitByIndex(index, inputValueLimitUpperArr);
        String inputLowerLimit = getLimitByIndex(index, inputValueLimitLowerArr);
        String upperLimitValue = getLimitByIndex(index, normalValueLimitUpperArr);
        String lowerLimitValue = getLimitByIndex(index, normalValueLimitLowerArr);
        IndicatorStatus status = parseStringStatus(value, inputUpperLimit, inputLowerLimit, upperLimitValue, lowerLimitValue);
        String color = "#000000";
        switch (status) {
            case NORMAL://正常  白底黑字
                color = "#000000";
                break;
            case ERROR://错误 灰底红字
                color = "#FF0000";
                break;
            case WARNING://危险 黄底黑字
                color = "#FFCC00";
                break;
            default:
                color = "#000000";
                break;
        }
        return color;
    }

    /**
     * 根据角标和数组，返回对应的范围值
     *
     * @param index
     * @param limitArr
     * @return
     */
    private static String getLimitByIndex(int index, String[] limitArr) {
        if (limitArr == null || limitArr.length == 0)
            return null;
        if (limitArr.length > index)
            return limitArr[index];
        else
            return limitArr[0];
    }

    /**
     * 将特殊格式的String解析为String[]
     *
     * @param content 传入的String格式为以一个“|”符号分割的字符串，
     * @return 返回一个String数组，是将参数根据“|”符号拆分的结果
     */
    public static String[] parseStringArrayByStringFormatPro(String content) {
        String[] result;
        if (TextUtils.isEmpty(content))
            return null;
        if (content.contains("|")) {
            result = content.split("\\|");
        } else {
            result = new String[1];
            result[0] = content;
        }
        return result;
    }


}
