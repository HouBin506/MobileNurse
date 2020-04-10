package com.herenit.mobilenurse.thirdparty.Seuic;

import android.content.IntentFilter;

/**
 * author: HouBin
 * date: 2019/5/24 16:18
 * desc: 东集设备常量
 */
public class SeuicConstant {
    //扫码广播名
//    public static final String SCAN_BROADCAST_ACTION_NAME_CRUISE = "com.android.server.scannerservice.broadcast";//系统默认广播
    //小码哥自定义广播名称，为了解决扫描之后自动在输入框填写扫描内容的问题，需要使用自定义广播
    public static final String SCAN_BROADCAST_ACTION_NAME_CRUISE = "com.android.server.scannerservice.srmyybroadcast";
    //扫码结果广播接收参数
    public static final String SCAN_BROADCAST_EXTRA_NAME_CRUISE = "scannerdata";

    public static final IntentFilter SCAN_BROADCAST_INTENT_FILTER_CRUISE = new IntentFilter(SCAN_BROADCAST_ACTION_NAME_CRUISE);
}
