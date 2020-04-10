package com.herenit.mobilenurse.datastore.sp;

import android.app.Application;
import android.content.Context;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * author: HouBin
 * date: 2019/1/18 9:45
 * desc: 文件名为{@link KeyConstant#NAME_SP_CONFIG}的{@link android.content.SharedPreferences}存储
 */
public class ConfigSp extends CommonSp {

    private static ConfigSp mInstance;

    private ConfigSp() {
        super(KeyConstant.NAME_SP_CONFIG);
    }

    public static final ConfigSp getInstance() {
        if (mInstance == null) {
            mInstance = new ConfigSp();
        }
        return mInstance;
    }

    /**
     * 设置是否记住密码
     *
     * @param remember
     */
    public void setRememberPassword(boolean remember) {
        setValue(KeyConstant.KEY_SP_CONFIG_REMEMBER_PASSWORD, remember);
    }

    /**
     * 获取是否记住密码
     *
     * @return 默认为false
     */
    public boolean getRememberPassword() {
        return getValue(KeyConstant.KEY_SP_CONFIG_REMEMBER_PASSWORD, false);
    }

    /**
     * 存储Cookie值
     *
     * @param cookieList
     */
    public void setCookie(List<String> cookieList) {
        Set<String> cookieSet = new TreeSet<>();
        if (cookieList == null || cookieList.isEmpty())
            return;
        for (String cookie : cookieList) {
            cookieSet.add(cookie);
        }
        setValue(KeyConstant.KEY_SP_CONFIG_COOKIE, cookieSet);
    }

    /**
     * 获取cookie值
     *
     * @return
     */
    public List<String> getCookie() {
        Set<String> cookieSet = getValue(KeyConstant.KEY_SP_CONFIG_COOKIE, new TreeSet<>());
        if (cookieSet == null || cookieSet.isEmpty())
            return null;
        List<String> cookieList = new ArrayList<>();
        for (String cookie : cookieSet) {
            cookieList.add(cookie);
        }
        return cookieList;
    }

    /**
     * 设置是否使用摄像头扫描
     *
     * @param isUse
     */
    public void setCameraScan(boolean isUse) {
        setValue(KeyConstant.KEY_SP_CONFIG_CAMERA_SCAN, isUse);
    }

    /**
     * 是否使用摄像头扫描
     * 默认不使用
     */
    public boolean getCameraScan() {
        return getValue(KeyConstant.KEY_SP_CONFIG_CAMERA_SCAN, false);
    }

    /**
     * 设置服务器地址
     *
     * @param address
     */
    public void setServiceAddress(String address) {
        setValue(KeyConstant.KEY_SP_CONFIG_SERVICE_ADDRESS, address);
    }

    /**
     * 获取服务器地址
     */
    public String getServiceAddress() {
        return getValue(KeyConstant.KEY_SP_CONFIG_SERVICE_ADDRESS, "");
    }
}
