package com.herenit.mobilenurse.mvp.setting;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.mvp.IModel;
import com.herenit.mobilenurse.api.Api;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.constant.ValueConstant;
import com.herenit.mobilenurse.criteria.entity.submit.Login;
import com.herenit.mobilenurse.datastore.sp.ConfigSp;
import com.herenit.mobilenurse.mvp.login.LoginActivity;

import javax.inject.Inject;

/**
 * author: HouBin
 * date: 2019/1/28 10:39
 * desc: 系统设置的Presenter
 */
@ActivityScope
public class SystemSettingsPresenter extends BasePresenter<IModel, SystemSettingsContract.View> {
    @Inject
    Application mApplication;

    @Inject
    public SystemSettingsPresenter(SystemSettingsContract.View view) {
        super(view);
    }

    /**
     * 修改密码
     */
    public void changePassword() {
        Intent intent = new Intent(mApplication, LoginActivity.class);
        intent.putExtra(KeyConstant.NAME_EXTRA_PAGE_TYPE, ValueConstant.VALUE_PAGE_TYPE_CHANGE_PASSWORD);
        mRootView.launchActivity(intent);
    }

    /**
     * 设置床位范围列表
     */
    public void bedRange() {
        mRootView.launchActivity(new Intent(mApplication, BedRangeActivity.class));
    }

    /**
     * 切换账号
     */
    public void switchAccount() {
        Intent intent = new Intent(mApplication, LoginActivity.class);
        intent.putExtra(KeyConstant.NAME_EXTRA_PAGE_TYPE, ValueConstant.VALUE_PAGE_TYPE_SWITCH_ACCOUNT);
        mRootView.launchActivity(intent);
    }

    /**
     * 设置是否买使用摄像头扫描
     */
    public void setCameraScan() {
        boolean isUse = ConfigSp.getInstance().getCameraScan();
        ConfigSp.getInstance().setCameraScan(!isUse);
    }

    /**
     * 设置服务器地址，修改了服务器地址之后，需要重启APP
     *
     * @param address
     */
    public void setServiceAddress(String address) {
        ConfigSp.getInstance().setServiceAddress(address);
        mRootView.setSystemAddressSuccess();
    }

    /**
     * 获取服务器地址
     *
     * @return
     */
    public String getServiceAddress() {
        String address = ConfigSp.getInstance().getServiceAddress();
        return TextUtils.isEmpty(address) ? Api.IP_PORT : address;
    }
}
