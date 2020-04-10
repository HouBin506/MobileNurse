package com.herenit.mobilenurse.mvp.launch;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.PermissionUtil;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.MNUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.constant.ValueConstant;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.dialog.NoticeDialog;
import com.herenit.mobilenurse.custom.widget.progressbar.DownloadProgressBar;
import com.herenit.mobilenurse.custom.widget.progressbar.LoadingProgressBar;
import com.herenit.mobilenurse.di.component.DaggerLoadingComponent;
import com.herenit.mobilenurse.mvp.base.BasicCommonActivity;
import com.herenit.mobilenurse.mvp.login.LoginActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;


import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import timber.log.Timber;

/**
 * author: HouBin
 * date: 2019/1/9 13:53
 * desc: App Loading页面，做一些导航页显示、版本检测更新、系统初始化等工作
 * 此页面可以作为App首页，Loading模式，做数据初始化，版本检测等工作。也可以作为about模式页面，查看App相关信息
 */
public class LoadingActivity extends BasicCommonActivity<LoadingPresenter> implements LoadingContract.View {

    private static String[] appPermissions;

    /**
     * 如果项目需要增加权限，这里添加6.0系统申请权限
     */
    static {
        appPermissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    @BindView(R2.id.pb_loading_loading)
    LoadingProgressBar mPb_loading;

    @BindView(R2.id.pb_loading_download)
    DownloadProgressBar mPb_download;
    private String mPageType;

    @Inject
    RxPermissions mPermissions;
    @Inject
    RxErrorHandler mErrorHandler;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoadingComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        setUseStatusBarView(false);
        return R.layout.activity_loading;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //TODO 此处代码别无它用，只是为了计算设备最小宽度dp值
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int heightPixels = dm.heightPixels;
//        int widthPixels = dm.widthPixels;
//        float density = dm.density;
//        float heightDP = heightPixels / density;
//        float widthDP = widthPixels / density;
//        float smallestWidthDP;
//        if (widthDP < heightDP) {
//            smallestWidthDP = widthDP;
//        } else {
//            smallestWidthDP = heightDP;
//        }
//        Log.e(TAG, "设备宽高：" + widthDP + "*" + heightDP);
        requestAppPermissions();
//        TimeUtils.setTimeZone("GMT+08:00");
    }

    /**
     * 申请App使用的权限
     */
    private void requestAppPermissions() {
        PermissionUtil.requestPermission(new PermissionUtil.RequestPermission() {
            /**
             * 权限请求成功
             */
            @Override
            public void onRequestPermissionSuccess() {
                loadServerTime();
            }

            /**
             * 用户拒绝了权限请求, 权限请求失败, 但还可以继续请求该权限
             *
             * @param permissions 请求失败的权限名
             */
            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                loadServerTime();
            }

            /**
             * 用户拒绝了权限请求并且用户选择了以后不再询问, 权限请求失败, 这时将不能继续请求该权限, 需要提示用户进入设置页面打开该权限
             *
             * @param permissions 请求失败的权限名
             */
            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                loadServerTime();
            }
        }, mPermissions, mErrorHandler, appPermissions);
    }

    @Override
    public void showDownloadProgressBar(int progress) {
        mPb_download.show();
        mPb_download.setMessage(ArmsUtils.getString(mContext, R.string.message_completed) + "" + progress + "%");
        mPb_download.setProgress(progress);
    }

    @Override
    public void dismissDownloadProgressBar() {
        mPb_download.dismiss();
    }

    /**
     * 显示 系统更新弹窗
     *
     * @param updateNow 是否强制更新
     */
    @Override
    public void showUpdateNotice(boolean updateNow) {
        Timber.tag("有新版本，提示客户更新");
        String title;
        String message;
        String positiveText;
        String negativeText;
        title = ArmsUtils.getString(mContext, R.string.title_dialog_appUpdate);
        if (updateNow) {
            message = ArmsUtils.getString(mContext, R.string.message_appUpdate_now);
            positiveText = ArmsUtils.getString(mContext, R.string.btn_update);
            negativeText = ArmsUtils.getString(mContext, R.string.btn_exit);
        } else {
            message = ArmsUtils.getString(mContext, R.string.message_appUpdate);
            positiveText = ArmsUtils.getString(mContext, R.string.btn_update);
            negativeText = ArmsUtils.getString(mContext, R.string.btn_do_later);
        }
        NoticeDialog dialog = createNoticeDialog(title, message, positiveText, negativeText);
        dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
            @Override
            public void onPositive() {
                dialog.dismiss();
                mPresenter.updateApp();
            }

            @Override
            public void onNegative() {
                dialog.dismiss();
                if (updateNow) {
                    ArmsUtils.exitApp();
                } else {
                    toLogin();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void toLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(KeyConstant.NAME_EXTRA_PAGE_TYPE, ValueConstant.VALUE_PAGE_TYPE_LOGIN);
        launchActivity(intent);
        finish();
    }

    @Override
    public void loadServerTime() {
        //获取服务器时间，做本地时间校对
        mPresenter.getServerTime();
    }

    @Override
    public void checkVersion() {
//设置当前页面类型，是loading还是about
        mPageType = ValueConstant.VALUE_PAGE_TYPE_LOADING;
        Intent intent = getIntent();
        if (intent != null) {
            String pageType = intent.getStringExtra(KeyConstant.NAME_EXTRA_PAGE_TYPE);
            if (!TextUtils.isEmpty(pageType))
                mPageType = pageType;
        }
        if (ValueConstant.VALUE_PAGE_TYPE_LOADING.equals(mPageType)) {//Loading模式
            mPresenter.checkVersionUpdate();
        } else {//about模式
            hideLoading();
            dismissDownloadProgressBar();
        }
    }


    @Override
    public void showLoading() {
        mPb_loading.show();
    }

    @Override
    public void hideLoading() {
        mPb_loading.dismiss();
    }

    @Override
    public void showError(@NonNull String message) {
        showErrorToast(message);
    }

    @Override
    public void showMessage(@NonNull String message) {
        mPb_loading.setMessage(message);
    }

    @Override
    protected void onDestroy() {
        mPb_loading.release();
        mPb_download.release();
        super.onDestroy();
    }
}
