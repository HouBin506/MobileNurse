package com.herenit.mobilenurse.custom.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.nfc.Tag;
import android.text.TextUtils;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;

import timber.log.Timber;

/**
 * author: HouBin
 * date: 2019/1/30 11:15
 * desc: 网络监听广播
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = "Network";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action) && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            //网络状态发生了改变
            Timber.tag(TAG).i("Network state Changed!");
            //发送网络状态改变的通知
            EventBusUtils.post(EventIntentionEnum.CONNECTIVITY_CHANGE.getId(), null);
        }
    }
}
