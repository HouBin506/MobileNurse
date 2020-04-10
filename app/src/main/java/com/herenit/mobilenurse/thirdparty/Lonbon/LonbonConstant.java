package com.herenit.mobilenurse.thirdparty.Lonbon;

import android.content.IntentFilter;

/**
 * author: HouBin
 * date: 2019/5/24 16:18
 * desc: 来帮设备设备常量
 */
public class LonbonConstant {
    //扫码广播名
    public static final String SCAN_BROADCAST_ACTION_NAME_LONBON = "com.lonbon.intent.action.dimension_code";
    //扫码结果广播接收参数
    public static final String SCAN_BROADCAST_EXTRA_NAME_LONBON = "value";

    public static final IntentFilter SCAN_BROADCAST_INTENT_FILTER_LONBON = new IntentFilter(SCAN_BROADCAST_ACTION_NAME_LONBON);
}
