package com.herenit.mobilenurse.app.utils;


import android.content.Context;
import android.content.res.AssetManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.MobileNurseApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对控件额外进行控制
 */
public class Utils {
    private static int _interval = 500;
    private static long lastClickTime;
    /**
     * 扫描间隔时间
     */
    private static int interval = 5 * 60 * 1000;

    private static long lastScanTime;

    /**
     * 是否是快速的进行双击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();

        long timeD = time - lastClickTime;

        if (0 < timeD && timeD < _interval)
            return true;
        else
            lastClickTime = time;

        return false;
    }

    /**
     * 扫描不同的输液标签低于5分钟
     *
     * @return
     */
    public static boolean isInterval5Minute() {
        long time = System.currentTimeMillis();
        long timeInterval = time - lastScanTime;
        if (0 < timeInterval && timeInterval < interval)
            return true;
        else
            lastScanTime = time;
        return false;
    }

    /**
     * 是否是快速点击物理键
     *
     * @return
     */
    public static boolean isFastClickKey() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;

        if (0 < timeD && timeD < _interval)
            return true;
        else
            lastClickTime = time;
        return false;
    }

    /**
     * 是否是快速点击指定物理键
     *
     * @return
     */
    public static boolean isFastClickKey(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long time = System.currentTimeMillis();
            long timeD = time - lastClickTime;

            if (0 < timeD && timeD < _interval)
                return true;
            else
                lastClickTime = time;
        }
        return false;
    }


    /**
     * MD5加密
     *
     * @param inStr
     * @return
     */
    public static String toMD5(String inStr) {
        StringBuffer sb = new StringBuffer();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(inStr.getBytes());
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    sb.append("0");
                sb.append(Integer.toHexString(i));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取assets目录下的数据
     *
     * @param fileName
     * @return
     */
    public static String getAssetsFileString(String fileName) {
        Context context = MobileNurseApplication.getInstance().getApplicationContext();
        StringBuilder sb = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
            br.close();
            Log.i("assets", "getJson from :" + fileName + " success");
        } catch (IOException e) {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * dp转换成px
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置输入框只读状态
     *
     * @param editText
     * @param readOnlyStatus false 只读状态，true可操作状态
     */
    public static void setEditTextReadOnlyStatus(EditText editText, boolean readOnlyStatus) {
        if (readOnlyStatus) {//可操作
            editText.setTextColor(ContextCompat.getColor(MobileNurseApplication.getInstance().getApplicationContext(), R.color.light_black));
        } else {
            editText.setTextColor(ContextCompat.getColor(MobileNurseApplication.getInstance().getApplicationContext(), R.color.gray));
        }
        editText.setCursorVisible(readOnlyStatus);      //设置输入框中的光标不可见
        editText.setFocusable(readOnlyStatus);           //无焦点
        editText.setFocusableInTouchMode(readOnlyStatus);     //触摸时也得不
    }

    /**
     * String 转化成Double
     *
     * @param dataValue
     * @return
     */
    public static Float string2Float(String dataValue) {
        Float d = null;
        try {
            d = Float.valueOf(dataValue);
        } catch (NumberFormatException e) {
            d = null;
        }
        return d;
    }

    /**
     * 判断一个字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isIntOrFloat(String str) {
        Pattern pattern = Pattern.compile("[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 将某数值（浮点型，四舍五入保留 newScale 位小数）
     *
     * @param number
     * @param newScale 小数点后保留几位
     * @return
     */
    public static String setNumberScale(String number, int newScale) {
        String result = "";
        if (!TextUtils.isEmpty(number) && isIntOrFloat(number) && string2Float(number) != null) {
            BigDecimal bigDecimal = new BigDecimal(number);
            result = bigDecimal.setScale(newScale, BigDecimal.ROUND_HALF_UP).toString();
        }
        return result;
    }
}
