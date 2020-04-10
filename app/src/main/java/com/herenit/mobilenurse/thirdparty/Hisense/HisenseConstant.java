package com.herenit.mobilenurse.thirdparty.Hisense;

import android.content.IntentFilter;

/**
 * author: HouBin
 * date: 2019/5/24 16:18
 * desc: 海信设备常量
 */
public class HisenseConstant {
    //扫码广播名
    public static final String SCAN_BROADCAST_ACTION_NAME_HISENSE = "android.provider.sdlMessage";
    //扫码结果广播接收参数
    public static final String SCAN_BROADCAST_EXTRA_NAME_HISENSE = "msg";

    public static final IntentFilter SCAN_BROADCAST_INTENT_FILTER_HISENSE = new IntentFilter(SCAN_BROADCAST_ACTION_NAME_HISENSE);
}
