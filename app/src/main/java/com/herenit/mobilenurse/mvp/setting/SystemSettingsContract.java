package com.herenit.mobilenurse.mvp.setting;

import com.herenit.arms.mvp.IView;

/**
 * author: HouBin
 * date: 2019/1/28 10:37
 * desc: 系统设置的Contract
 */
public interface SystemSettingsContract {

    interface View extends IView {

        void setSystemAddressSuccess();
    }
}
