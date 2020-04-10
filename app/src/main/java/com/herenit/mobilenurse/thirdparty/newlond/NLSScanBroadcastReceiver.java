package com.herenit.mobilenurse.thirdparty.newlond;

import android.content.Intent;

import com.herenit.mobilenurse.thirdparty.ScanBroadcastReceiver;

/**
 * author: HouBin
 * date: 2019/5/24 16:14
 * desc:新大陆扫描设备
 */
public class NLSScanBroadcastReceiver extends ScanBroadcastReceiver {

    public static NLSScanBroadcastReceiver mInstance;

    private NLSScanBroadcastReceiver() {

    }

    public static NLSScanBroadcastReceiver getInstance() {
        if (mInstance == null)
            mInstance = new NLSScanBroadcastReceiver();
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
        return intent.getStringExtra(NLSConstant.SCAN_BROADCAST_EXTRA_NAME_NLS);
    }
}
