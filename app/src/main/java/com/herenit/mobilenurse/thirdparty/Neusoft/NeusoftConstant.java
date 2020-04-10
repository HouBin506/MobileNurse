package com.herenit.mobilenurse.thirdparty.Neusoft;

import android.content.IntentFilter;

/**
 * author: HouBin
 * date: 2019/5/24 16:18
 * desc: 东软设备常量
 */
public class NeusoftConstant {
    //扫码广播名
    public static final String SCAN_BROADCAST_ACTION_NAME_CAMUS = "com.neusoft.action.scanner.read";
    //扫码结果广播接收参数
    public static final String SCAN_BROADCAST_EXTRA_NAME_CAMUS = "scanner_value";

    public static final IntentFilter SCAN_BROADCAST_INTENT_FILTER_CAMUS = new IntentFilter(SCAN_BROADCAST_ACTION_NAME_CAMUS);
}
