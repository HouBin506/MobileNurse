package com.herenit.mobilenurse.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.herenit.mobilenurse.criteria.constant.CommonConstant;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * author: HouBin
 * date: 2019/1/30 11:28
 * desc: 网络工具类
 */
public class NetworkUtils {
    private NetworkUtils() {
    }

    /**
     * 获取网络连接类型
     *
     * @param context
     * @return 返回的网络状态定义在了CommonConstant中
     */
    public static int getNetworkConnectType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return CommonConstant.NETWORK_CONNECT_TYPE_NONE;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable())
            return CommonConstant.NETWORK_CONNECT_TYPE_NONE;
        if (networkInfo.getState() != NetworkInfo.State.CONNECTED)
            return CommonConstant.NETWORK_CONNECT_TYPE_NONE;
        //判断是否为Wifi网络
        NetworkInfo wifiNet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNet != null) {
            NetworkInfo.State state = wifiNet.getState();
            if (state != null) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)
                    return CommonConstant.NETWORK_CONNECT_TYPE_WIFI;
            }
        }
        //判断是否为以太网络
        NetworkInfo ethernetNet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (ethernetNet != null) {
            NetworkInfo.State state = ethernetNet.getState();
            if (state != null) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)
                    return CommonConstant.NETWORK_CONNECT_TYPE_WIFI;
            }
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = telephonyManager.getNetworkType();
        /*
             GPRS : 2G(2.5) General Packet Radia Service 114kbps
             EDGE : 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
             UMTS : 3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
             CDMA : 2G 电信 Code Division Multiple Access 码分多址
             EVDO_0 : 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
             EVDO_A : 3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
             1xRTT : 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
             HSDPA : 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
             HSUPA : 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
             HSPA : 3G (分HSDPA,HSUPA) High Speed Packet Access
             IDEN : 2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
             EVDO_B : 3G EV-DO Rev.B 14.7Mbps 下行 3.5G
             LTE : 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
             EHRPD : 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
             HSPAP : 3G HSPAP 比 HSDPA 快些
             */
        switch (networkType) {
            // 2G网络
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return CommonConstant.NETWORK_CONNECT_TYPE_2G;
            // 3G网络
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return CommonConstant.NETWORK_CONNECT_TYPE_3G;
            // 4G网络
            case TelephonyManager.NETWORK_TYPE_LTE:
                return CommonConstant.NETWORK_CONNECT_TYPE_4G;
            default://移动网络
                return CommonConstant.NETWORK_CONNECT_TYPE_MOBILE;
        }
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return networkInfo.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    /**
     * 判断是否为Wifi连接
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                int type = networkInfo.getType();
                if (type == ConnectivityManager.TYPE_WIFI || type == ConnectivityManager.TYPE_ETHERNET)
                    return networkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * 判断当前异常是不是因为网络问题引起的
     *
     * @param t
     * @return
     */
    public static boolean isNetworkError(Throwable t) {
        if (t instanceof UnknownHostException) {
            return true;
        } else if (t instanceof SocketTimeoutException) {
            return true;
        }
        return false;
    }
}
