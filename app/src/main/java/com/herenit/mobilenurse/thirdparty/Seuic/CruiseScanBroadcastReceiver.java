package com.herenit.mobilenurse.thirdparty.Seuic;

import android.content.Intent;

import com.herenit.mobilenurse.thirdparty.Neusoft.NeusoftConstant;
import com.herenit.mobilenurse.thirdparty.ScanBroadcastReceiver;

/**
 * author: HouBin
 * date: 2019/5/24 16:14
 * desc:东大集成 小码哥设备扫描监听
 */
public class CruiseScanBroadcastReceiver extends ScanBroadcastReceiver {

    public static CruiseScanBroadcastReceiver mInstance;

    private CruiseScanBroadcastReceiver() {

    }

    public static CruiseScanBroadcastReceiver getInstance() {
        if (mInstance == null)
            mInstance = new CruiseScanBroadcastReceiver();
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
        return intent.getStringExtra(SeuicConstant.SCAN_BROADCAST_EXTRA_NAME_CRUISE);
    }
}
