package com.herenit.mobilenurse.app.utils;

import android.text.TextUtils;

import com.herenit.arms.utils.Preconditions;
import com.herenit.mobilenurse.criteria.enums.IndicatorStatus;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * author: HouBin
 * date: 2019/1/7 15:28
 * desc: 字符串工具类，提供加密等操作
 */
public class StringUtils {


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
     * 计算百分数，并返回字符串
     *
     * @param num   分子
     * @param total 总数（分母）
     * @return 返回字符串数值，保留两位小数
     */
    public static String countPercentString(double num, double total) {
        total = Preconditions.checkDoubleGreaterThan(total, 0, "分母");
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMaximumFractionDigits(2);//设定最多两位小数
        return numberFormat.format(num / total);
    }

    /**
     * 根据百分数，获取分子
     *
     * @param percent 百分数 要求 0~100%之间的的百分数
     */
    public static double getNumFromPercent(String percent) {
        double num = 0;
//        if (TextUtils.isEmpty(percent) || !percent.endsWith("%"))
//            return num;
        String numStr = percent.substring(0, percent.length() - 1);
        try {
            num = Double.parseDouble(numStr);
        } catch (Exception e) {
            return num;
        }
        return num;
    }

    /**
     * 去除 字符串收尾的 特殊的Unicode [ "\uFEFF" ]
     * csv 文件可能会带有该编码
     *
     * @param str
     * @return
     */
    public static String specialUnicode(String str) {
        if (str.startsWith("\uFEFF")) {
            str = str.replace("\uFEFF", "");
        } else if (str.endsWith("\uFEFF")) {
            str = str.replace("\uFEFF", "");
        }
        return str;
    }

    /**
     * 根据百分数，获取分子，四舍五入返回整数int类型,
     *
     * @param percent 百分数
     */
    public static int getIntegerNumFromPercent(String percent) {
        double numDouble = getNumFromPercent(percent);
        DecimalFormat df = new DecimalFormat("######0");
        return Integer.parseInt(df.format(numDouble));
    }

    /**
     * double转int 四舍五入
     *
     * @param number
     * @return
     */
    public static int getInt(double number) {
        BigDecimal bd = new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(bd.toString());
    }


    /**
     * 判断字符串是否为网络地址
     *
     * @param address
     * @return
     */
    public static boolean isNetworkAddress(String address) {
        if (TextUtils.isEmpty(address))
            return false;
        if (TextUtils.isEmpty(address.trim()))
            return false;
        address = address.trim();
        String[] ipAndPort = address.split(":");
        if (ipAndPort == null || ipAndPort.length != 2)
            return false;
        String ip = ipAndPort[0];
        String port = ipAndPort[1];
        if (!isNumeric(port))
            return false;
        if (!ip.contains("."))
            return false;
        String[] ipArr = ip.split("\\.");
        if (ipArr == null || ipArr.length != 4)
            return false;
        for (String vIp : ipArr) {
            if (!isNumeric(vIp))
                return false;
            int intIp = Integer.parseInt(vIp);
            if (intIp > 255)
                return false;
        }
        return true;
    }


    /**
     * 判断数组中是否包含某字符串
     *
     * @param strArr
     * @param content
     * @return
     */
    public static boolean contains(String[] strArr, String content) {
        if (strArr == null || strArr.length == 0)
            return false;
        List strList = Arrays.asList(strArr);
        return strList.contains(content);
    }

    /**
     * 是否为纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 将String字符串转Double
     *
     * @param content
     * @return 如果String字符串为空或者不能被解析为Double类型，则返回null
     */
    public static Double stringToDouble(String content) {
        if (TextUtils.isEmpty(content))
            return null;
        try {
            return Double.parseDouble(content);
        } catch (Exception e) {
            return null;
        }
    }

}
