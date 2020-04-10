package com.herenit.mobilenurse.mvp.setting;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.dialog.NoticeDialog;
import com.herenit.mobilenurse.custom.widget.dialog.SingleInputDialog;
import com.herenit.mobilenurse.datastore.sp.ConfigSp;
import com.herenit.mobilenurse.di.component.DaggerSystemSettingsComponent;
import com.herenit.mobilenurse.mvp.base.BasicBusinessActivity;
import com.herenit.mobilenurse.mvp.base.BasicCommonActivity;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/1/4 13:47
 * desc:系统设置
 */
public class SystemSettingsActivity extends BasicBusinessActivity<SystemSettingsPresenter> implements SystemSettingsContract.View, View.OnClickListener {

    @BindView(R2.id.tv_systemSettings_lock)
    TextView mTv_lock;//锁频
    @BindView(R2.id.tv_systemSettings_switchAccount)
    TextView mTv_switchAccount;//切换账号
    @BindView(R2.id.tv_systemSettings_changePassword)
    TextView mTv_changePassword;//修改密码
    @BindView(R2.id.tv_systemSettings_bedRange)
    TextView mTv_bedRange;//床位范围
    @BindView(R2.id.ll_systemSettings_serviceAddress)
    LinearLayout mLl_serviceAddress;//服务器地址
    @BindView(R2.id.tv_systemSettings_ServiceAddress)
    TextView mTv_serviceAddress;//服务器地址
    @BindView(R2.id.ll_systemSettings_cameraScan)
    LinearLayout mLl_cameraScan;//扫描设置
    @BindView(R2.id.cb_systemSettings_cameraScan)
    CheckBox mCb_cameraScan;//扫描设置
    @BindView(R2.id.tv_systemSettings_exit)
    TextView mTv_exit;//退出


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSystemSettingsComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_system_settings;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        boolean isLogin = false;
        if (intent != null) {
            isLogin = intent.getBooleanExtra(KeyConstant.NAME_EXTRA_IS_LOGIN, false);
        }
        initSettings(isLogin);
    }

    /**
     * 初始化设置界面
     *
     * @param isLogin 是否登录，根据当前是否登录，来选择显示哪些设置功能
     */
    private void initSettings(boolean isLogin) {
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        setTitleBarCenterText(ArmsUtils.getString(mContext, R.string.title_activity_systemSettings));
        setTitleBarLeftOnClickListener(new View.OnClickListener() {//设置点击左上角返回
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mLl_serviceAddress.setVisibility(View.VISIBLE);
        mLl_cameraScan.setVisibility(View.GONE);
        mTv_exit.setVisibility(View.VISIBLE);
        ViewUtils.setViewVisibility(mTv_lock, isLogin);
        ViewUtils.setViewVisibility(mTv_switchAccount, isLogin);
        ViewUtils.setViewVisibility(mTv_bedRange, isLogin);
        ViewUtils.setViewVisibility(mTv_changePassword, isLogin);
        mTv_lock.setOnClickListener(this);
        mTv_changePassword.setOnClickListener(this);
        mTv_bedRange.setOnClickListener(this);
        mTv_switchAccount.setOnClickListener(this);
        mTv_exit.setOnClickListener(this);
        mLl_cameraScan.setOnClickListener(this);
        mLl_serviceAddress.setOnClickListener(this);
        mCb_cameraScan.setChecked(ConfigSp.getInstance().getCameraScan());
        mTv_serviceAddress.setText(mPresenter.getServiceAddress());
    }

    @Override
    public void showMessage(@NonNull String message) {
        showToast(message);
    }

    /**
     * 设置界面的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_systemSettings_lock) {//锁屏
            lock();
        } else if (id == R.id.tv_systemSettings_changePassword) {//修改密码
            mPresenter.changePassword();
        } else if (id == R.id.tv_systemSettings_bedRange) {//设置床位列表范围
            mPresenter.bedRange();
        } else if (id == R.id.tv_systemSettings_switchAccount) {//切换账号
            mPresenter.switchAccount();
        } else if (id == R.id.ll_systemSettings_serviceAddress) {//设置服务器地址
            setServiceAddress();
        } else if (id == R.id.ll_systemSettings_cameraScan) {//扫描设置
            setCameraScan();
        } else if (id == R.id.tv_systemSettings_exit) {//退出
            exit();
        }
    }

    /**
     * 退出程序
     */
    private void exit() {
        NoticeDialog dialog = createNoticeDialog(ArmsUtils.getString(mContext, R.string.title_dialog_exitApp),
                ArmsUtils.getString(mContext, R.string.message_sure_exitApp));
        dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
            @Override
            public void onPositive() {
                dialog.dismiss();
                exitApp();
            }

            @Override
            public void onNegative() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 设置扫描
     */
    private void setCameraScan() {
        mPresenter.setCameraScan();//修改设置状态
        boolean checked = mCb_cameraScan.isChecked();
        mCb_cameraScan.setChecked(!checked);
    }

    @Override
    public void showError(@NonNull String message) {
        showErrorToast(message);
    }

    /**
     * 设置服务器地址
     */
    private void setServiceAddress() {
        SingleInputDialog dialog = createSingInputDialog(ArmsUtils.getString(mContext, R.string.btn_serviceAddress),
                ArmsUtils.getString(mContext, R.string.message_inputServiceAddress), mTv_serviceAddress.getText().toString());
        dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
            @Override
            public void onPositive(Object... backData) {
                String address = (String) backData[0];
                if (!StringUtils.isNetworkAddress(address)) {
                    showError(ArmsUtils.getString(mContext, R.string.error_serviceAddress_formatError));
                } else {
                    mTv_serviceAddress.setText(address);
                    dialog.dismiss();
                    mPresenter.setServiceAddress(address);
                }
            }

            @Override
            public void onNegative() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 修改服务器地址成功后，要重启APP
     */
    @Override
    public void setSystemAddressSuccess() {
        restartApp();
    }

    /**
     * 设置标题栏类型，左、中、右：ImageView、TextView、无
     *
     * @return
     */
    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_NULL;
    }
}
