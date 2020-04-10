package com.herenit.mobilenurse.thirdparty.Neusoft;

import android.content.Intent;

import com.herenit.mobilenurse.thirdparty.Hisense.HisenseConstant;
import com.herenit.mobilenurse.thirdparty.ScanBroadcastReceiver;

/**
 * author: HouBin
 * date: 2019/5/24 16:14
 * desc:东软设备扫码监听
 */
public class CamusScanBroadcastReceiver extends ScanBroadcastReceiver {

    public static CamusScanBroadcastReceiver mInstance;

    private CamusScanBroadcastReceiver() {

    }

    public static CamusScanBroadcastReceiver getInstance() {
        if (mInstance == null)
            mInstance = new CamusScanBroadcastReceiver();
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
        return intent.getStringExtra(NeusoftConstant.SCAN_BROADCAST_EXTRA_NAME_CAMUS);
    }
}
