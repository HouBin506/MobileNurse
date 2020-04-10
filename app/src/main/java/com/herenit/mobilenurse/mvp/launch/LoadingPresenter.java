package com.herenit.mobilenurse.mvp.launch;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.DeviceUtils;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.BuildConfig;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.api.Api;
import com.herenit.mobilenurse.app.utils.FileUtils;
import com.herenit.mobilenurse.app.utils.NetworkUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.VersionInfo;
import com.herenit.mobilenurse.datastore.tempcache.CommonTemp;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.InputStream;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.ResponseBody;
import timber.log.Timber;

/**
 * author: HouBin
 * date: 2019/1/9 14:03
 * desc: Loading的Presenter
 */
@ActivityScope
public class LoadingPresenter extends BasePresenter<LoadingContract.Model, LoadingContract.View> {
    @Inject
    Application mApplication;
    @Inject
    RxErrorHandler mErrorHandler;

    private VersionInfo mVersion;


    @Inject
    public LoadingPresenter(LoadingContract.Model model, LoadingContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取服务器时间
     */
    public void getServerTime() {
        if (!NetworkUtils.isNetworkConnected(mApplication)) {
            mRootView.checkVersion();
            return;
        }
        mModel.getServerTime()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {//当Observable终止后，会调用，无论是正常结束还是异常结束
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<Result<Long>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<Long> timeResult) {
                        if (timeResult.isSuccessful())
                            CommonTemp.getInstance().setTimeDeviation(TimeUtils.proofTime(timeResult.getData()));
                        mRootView.checkVersion();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.checkVersion();
                    }
                });
    }

    /**
     * 版本检查，软件更新
     */
    public void checkVersionUpdate() {
        mModel.getVersionInfo()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {//当Observable终止后，会调用，无论是正常结束还是异常结束
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<Result<VersionInfo>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<VersionInfo> versionInfoResult) {
                        if (versionInfoResult != null) {
                            if (versionInfoResult.isSuccessful()) {
                                mVersion = versionInfoResult.getData();
                                checkVersion();
                            } else {
                                mRootView.showError(versionInfoResult.getMessage());
                                mRootView.toLogin();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.toLogin();
                    }
                });
    }

    /**
     * 下载进度监听
     */
    ProgressListener progressListener = new ProgressListener() {
        @Override
        public void onProgress(ProgressInfo progressInfo) {
            int percent = progressInfo.getPercent();
            mRootView.showDownloadProgressBar(percent);
            if (progressInfo.isFinish()) {
                mRootView.dismissDownloadProgressBar();
            }
        }

        @Override
        public void onError(long id, Exception e) {

        }
    };

    /**
     * 检测APP版本，是否需要做版本更新
     */
    private void checkVersion() {
        if (mVersion == null)
            return;
        int currentVersionCode = DeviceUtils.getVersionCode(mApplication);
        int serviceVersionCode = mVersion.getVersionCode();
        if (currentVersionCode == serviceVersionCode) {//版本号一致，不需要更新
            mRootView.toLogin();
        } else {//版本号不一致，提示用户要更新
            mRootView.showUpdateNotice(mVersion.getUpdateNow());
        }
    }

    /**
     * 做系统版本更新
     */
    public void updateApp() {
        String url = Api.BASE_URL + Api.DOWNLOAD_APP_FILE_PATH;
        ProgressManager.getInstance().addResponseListener(url, progressListener);
        Timber.tag("开始下载安装包......");
        mModel.downloadMobileNurseApk()
                .unsubscribeOn(Schedulers.io())//当视图消亡时，会直接取消订阅，尤其在下载文件时
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                })
                ////用于计算任务，如事件循环或和回调处理，不要用于IO操作(IO操作请使用Schedulers.io())；默认线程数等于处理器的数量
                .observeOn(Schedulers.computation())
                .doOnNext(new Consumer<InputStream>() {//doOnNext在Observer的OnNext之前被调用，一般用在subscribe之前，比如做数据的保存
                    @Override
                    public void accept(InputStream inputStream) throws Exception {
                        if (!(mVersion == null || TextUtils.isEmpty(mVersion.getFileName()))) {
                            File file = new File(FileUtils.getAppDownloadSaveDirectory(), mVersion.getFileName());
                            FileUtils.writeStreamToFile(inputStream, file);
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.dismissDownloadProgressBar();
                    }
                })
                .subscribe(new ErrorHandleSubscriber<InputStream>(mErrorHandler) {
                    @Override
                    public void onNext(InputStream inputStream) {
                        mRootView.showLoading();
                        mRootView.showMessage(ArmsUtils.getString(mApplication, R.string.message_download_finish) + "," +
                                ArmsUtils.getString(mApplication, R.string.message_Installing));
                        Timber.tag("安装包下载完成，开始安装App......");
                        installApp();
                    }
                });
    }


    /**
     * 安装App
     */
    private void installApp() {
        if (mVersion == null || TextUtils.isEmpty(mVersion.getFileName()))
            return;
        Log.i(TAG, "下载完成,准备安装.");
        File apkfile = new File(FileUtils.getAppDownloadSaveDirectory(), mVersion.getFileName());
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(mApplication, BuildConfig.APPLICATION_ID + ".fileprovider", apkfile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            //兼容8.0
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                boolean hasInstallPermission = mApplication.getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    //请求安装未知应用来源的权限
                    ActivityCompat.requestPermissions((Activity) mRootView, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 6666);
//                    startInstallPermissionSettingActivity();
//                    return;
                }
            }
        } else {
            // 通过Intent安装APK文件
            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                    "application/vnd.android.package-archive");
        }
        if (mApplication.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            mApplication.startActivity(intent);
        }
        //下载完成，安装APP，退出旧的APP
        ArmsUtils.exitApp();
    }

    @Override
    public void onDestroy() {
        progressListener = null;
        super.onDestroy();
    }
}
