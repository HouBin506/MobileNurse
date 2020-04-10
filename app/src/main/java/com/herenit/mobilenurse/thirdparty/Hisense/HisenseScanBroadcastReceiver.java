package com.herenit.mobilenurse.thirdparty.Hisense;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.thirdparty.ScanBroadcastReceiver;

/**
 * author: HouBin
 * date: 2019/5/24 16:14
 * desc:海信设备扫码监听
 */
public class HisenseScanBroadcastReceiver extends ScanBroadcastReceiver {

    public static HisenseScanBroadcastReceiver mInstance;

    private HisenseScanBroadcastReceiver() {

    }

    public static HisenseScanBroadcastReceiver getInstance() {
        if (mInstance == null)
            mInstance = new HisenseScanBroadcastReceiver();
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
        return intent.getStringExtra(HisenseConstant.SCAN_BROADCAST_EXTRA_NAME_HISENSE);
    }
}
