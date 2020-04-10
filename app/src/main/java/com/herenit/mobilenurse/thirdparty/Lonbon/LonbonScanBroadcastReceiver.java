package com.herenit.mobilenurse.thirdparty.Lonbon;

import android.content.Intent;

import com.herenit.mobilenurse.thirdparty.Hisense.HisenseConstant;
import com.herenit.mobilenurse.thirdparty.ScanBroadcastReceiver;

/**
 * author: HouBin
 * date: 2019/5/24 16:14
 * desc:海信设备扫码监听
 */
public class LonbonScanBroadcastReceiver extends ScanBroadcastReceiver {

    public static LonbonScanBroadcastReceiver mInstance;

    private LonbonScanBroadcastReceiver() {

    }

    public static LonbonScanBroadcastReceiver getInstance() {
        if (mInstance == null)
            mInstance = new LonbonScanBroadcastReceiver();
        return mInstance;
    }

    /**
     * 获取扫描结果
     *
     * @param intent
     * @return
     */
    @Override
    protected String getScanStringResult(Intent intent) {
        return intent.getStringExtra(LonbonConstant.SCAN_BROADCAST_EXTRA_NAME_LONBON);
    }
}
