package com.herenit.mobilenurse.thirdparty.newlond;

import android.content.IntentFilter;

/**
 * author: HouBin
 * date: 2019/5/24 16:18
 * desc: 新大陆
 */
public class NLSConstant {
    //小码哥自定义广播名称，为了解决扫描之后自动在输入框填写扫描内容的问题，需要使用自定义广播
    public static final String SCAN_BROADCAST_ACTION_NAME_NLS = "nlscan.action.SCANNER_RESULT";
    //扫码结果广播接收参数
    public static final String SCAN_BROADCAST_EXTRA_NAME_NLS = "SCAN_BARCODE1";

    public static final IntentFilter SCAN_BROADCAST_INTENT_FILTER_CRUISE = new IntentFilter(SCAN_BROADCAST_ACTION_NAME_NLS);

}
