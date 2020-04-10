package com.herenit.mobilenurse.criteria.enums;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.text.TextUtils;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.MobileNurseApplication;

import org.w3c.dom.Text;

/**
 * author: HouBin
 * date: 2019/2/25 14:33
 * desc: 患者病床一些标记，比如病情、护理等级、隔离、禁食、是否母婴....
 */
public enum SickbedFlagEnum {
    /****************************病情等级***********************/
    CONDITION_DANGER("1", "危"),
    CONDITION_INTENSIVE("2", "重"),
    CONDITION_GENERAL("", "一般"),

    /*****************************护理等级************************************/

    NURSING_CLASS_INTENSIVE("4", "重症护理"),
    NURSING_CLASS_SPECIAL("0", "特别"),
    NURSING_CLASS_FIRST("1", "Ⅰ级"),
    NURSING_CLASS_SECOND("2", "II级"),
    NURSING_CLASS_THIRD("3", "Ⅲ级");

    private String code;
    private String name;

    private SickbedFlagEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }


    /**
     * 是否隔离
     *
     * @param code
     * @return
     */
    public static boolean isIsolationSign(String code) {
        if (TextUtils.isEmpty(code))
            return false;
        return code.equals("01");
    }


    /**
     * 是否禁食
     *
     * @param code
     * @return
     */
    public static boolean isFastingSign(String code) {
        if (TextUtils.isEmpty(code))
            return false;
        return code.equals("01");
    }

    /**
     * 根据病情等级名称判断是否为病危
     *
     * @param name
     * @return
     */
    public static boolean isDangerCondition(String name) {
        if (!TextUtils.isEmpty(name))
            return name.contains(CONDITION_DANGER.getName());
        return false;
    }

    /**
     * 根据病情等级名称，判断是否为病重
     *
     * @param name
     * @return
     */
    public static boolean isIntensiveCondition(String name) {
        if (!TextUtils.isEmpty(name))
            return name.contains(CONDITION_INTENSIVE.getName());
        return false;
    }

    /**
     * 根据护理等级名称，获取对应显示的颜色
     *
     * @param name
     * @return
     */
    @ColorInt
    public static int getNursingClassColorByName(String name) {
        @ColorRes int colorRes;
        if (TextUtils.isEmpty(name))
            colorRes = R.color.white;
        else if (name.contains(NURSING_CLASS_INTENSIVE.getName()))
            colorRes = R.color.nursingClass_intensive;
        else if (name.contains(NURSING_CLASS_SPECIAL.getName()))
            colorRes = R.color.nursingClass_special;
        else if (name.contains(NURSING_CLASS_FIRST.getName()))
            colorRes = R.color.nursingClass_first;
        else if (name.contains(NURSING_CLASS_SECOND.getName()))
            colorRes = R.color.nursingClass_second;
        else if (name.contains(NURSING_CLASS_THIRD.getName()))
            colorRes = R.color.nursingClass_third;
        else
            colorRes = R.color.white;
        return ArmsUtils.getColor(MobileNurseApplication.getInstance(), colorRes);
    }

    /**
     * 根据护理等级名称，获取对应文字显示的颜色
     *
     * @param name
     * @return
     */
    @ColorInt
    public static int getNursingClassTextColorByName(String name) {
        @ColorRes int colorRes;
        if (TextUtils.isEmpty(name))
            colorRes = R.color.black;
        else if (name.contains(NURSING_CLASS_INTENSIVE.getName()))
            colorRes = R.color.white;
        else if (name.contains(NURSING_CLASS_SPECIAL.getName()))
            colorRes = R.color.white;
        else if (name.contains(NURSING_CLASS_FIRST.getName()))
            colorRes = R.color.white;
        else if (name.contains(NURSING_CLASS_SECOND.getName()))
            colorRes = R.color.black;
        else if (name.contains(NURSING_CLASS_THIRD.getName()))
            colorRes = R.color.white;
        else
            colorRes = R.color.black;
        return ArmsUtils.getColor(MobileNurseApplication.getInstance(), colorRes);
    }


    /**
     * 根据护理等级名称，获取对应显示的颜色的资源Id
     *
     * @param name
     * @return
     */
    @ColorRes
    public static int getNursingClassColorResByName(String name) {
        @ColorRes int colorRes;
        if (TextUtils.isEmpty(name))
            colorRes = R.color.white;
        else if (name.contains(NURSING_CLASS_INTENSIVE.getName()))
            colorRes = R.color.nursingClass_intensive;
        else if (name.contains(NURSING_CLASS_SPECIAL.getName()))
            colorRes = R.color.nursingClass_special;
        else if (name.contains(NURSING_CLASS_FIRST.getName()))
            colorRes = R.color.nursingClass_first;
        else if (name.contains(NURSING_CLASS_SECOND.getName()))
            colorRes = R.color.nursingClass_second;
        else if (name.contains(NURSING_CLASS_THIRD.getName()))
            colorRes = R.color.nursingClass_third;
        else
            colorRes = R.color.white;
        return colorRes;
    }
}
