package com.herenit.mobilenurse.thirdparty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;

/**
 * author: HouBin
 * date: 2019/5/24 16:54
 * desc: 扫描广播基类
 */
public abstract class ScanBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String content = getScanStringResult(intent);
        //扫描完成，获取到扫描结果，并将结果发送出去
        if (!TextUtils.isEmpty(content)) {
            content = content.trim();
            //对扫描结果进行解析
            ScanResult result = new ScanResult(content);
            if (result == null || result.getCodeType() == ScanResult.CODE_TYPE_UNKNOWN) {
                ArmsUtils.snackbarText(ArmsUtils.getString(context, R.string.message_unknown_scanCode));
                return;
            }
            switch (result.getCodeType()) {
                case ScanResult.CODE_TYPE_PATIENT://腕带
                    EventBusUtils.post(EventIntentionEnum.CODE_TYPE_PATIENT.getId(), result);
                    break;
                case ScanResult.CODE_TYPE_ORDER://医嘱条码
                    EventBusUtils.post(EventIntentionEnum.CODE_TYPE_ORDER.getId(), result);
                    break;
                case ScanResult.CODE_TYPE_EMP_CARD://员工牌
                    EventBusUtils.post(EventIntentionEnum.CODE_TYPE_EMP_CARD.getId(), result);
                    break;
                case ScanResult.CODE_TYPE_MONITOR://监护仪条码
                    EventBusUtils.post(EventIntentionEnum.CODE_TYPE_MONITOR.getId(), result);
                    break;
            }
        }
    }

    /**
     * 获取扫描到的String类型的数据
     *
     * @return
     */
    protected abstract String getScanStringResult(Intent intent);
}
