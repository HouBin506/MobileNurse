package com.herenit.mobilenurse.datastore.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.herenit.mobilenurse.app.MobileNurseApplication;

import java.util.Set;

/**
 * SharedPreferences 存取的封装
 *
 * @author HouBin
 */
public class CommonSp {
    protected Context mContext;
    private SharedPreferences mSharePre;

    protected CommonSp(String spName) {
        mContext = MobileNurseApplication.getInstance();
        mSharePre = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    /* get and put String value */
    public void setValue(String key, String value) {
        mSharePre.edit().putString(key, value).commit();
    }

    public String getValue(String key, String defValue) {
        return mSharePre.getString(key, defValue);
    }

    /* get and put boolean value */
    public void setValue(String key, boolean value) {
        mSharePre.edit().putBoolean(key, value).commit();
    }

    public boolean getValue(String key, boolean defValue) {
        return mSharePre.getBoolean(key, defValue);
    }

    /* get and put int value */
    public void setValue(String key, int value) {
        mSharePre.edit().putInt(key, value).commit();
    }

    public int getValue(String key, int defValue) {
        return mSharePre.getInt(key, defValue);
    }

    /* get and put int value */
    public void setValue(String key, float value) {
        mSharePre.edit().putFloat(key, value).commit();
    }

    public float getValue(String key, float defValue) {
        return mSharePre.getFloat(key, defValue);
    }

    /* get and put long value */
    public void setValue(String key, long value) {
        mSharePre.edit().putLong(key, value).commit();
    }

    public long getValue(String key, long defValue) {
        return mSharePre.getLong(key, defValue);
    }

    /* get and put string set value */
    public void setValue(String key, Set<String> value) {
        mSharePre.edit().putStringSet(key, value).commit();
    }

    public Set<String> getValue(String key, Set<String> defValue) {
        Set<String> value = mSharePre.getStringSet(key, defValue);
        return value;
    }

    public void removeValue(String key) {
        mSharePre.edit().remove(key);
    }

    /* 添加和移除sp改变的监听 */
    public void registerSpChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mSharePre.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterSpChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mSharePre.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
