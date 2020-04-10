package com.herenit.mobilenurse.mvp.patient;

import android.app.Application;
import android.support.annotation.NonNull;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.app.utils.NetworkUtils;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * author: HouBin
 * date: 2019/3/4 14:59
 * desc:患者详情的P层
 */
@FragmentScope
public class PatientInfoPresenter extends BasePresenter<PatientInfoContract.Model, PatientInfoContract.View> {

    @Inject
    Application mApplication;

    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    PatientInfoPresenter(PatientInfoContract.Model model, PatientInfoContract.View rootView) {
        super(model, rootView);

    }

    /**
     * 加载患者详情
     *
     * @param refresh
     */
    public void loadPatientInfo(@NonNull Sickbed sickbed, boolean refresh) {
        if (sickbed == null)
            return;
        if (NetworkUtils.isNetworkConnected(mApplication)) {//没网
            loadPatientInfoByNetwork(sickbed, refresh);
        } else {//有网
            loadPatientInfoByCache(sickbed);
        }
    }

    /**
     * 缓存中加载患者详情
     *
     * @param sickbed 当前患者
     */
    private void loadPatientInfoByCache(Sickbed sickbed) {
        PatientInfo patientInfo = mModel.getPatientInfoByCache(sickbed.getPatientId(), String.valueOf(sickbed.getVisitId()));
        mRootView.showPatientInfo(patientInfo);

    }

    /**
     * 网络加载患者详情
     *
     * @param sickbed 当前患者
     * @param refresh 是否刷新
     */
    private void loadPatientInfoByNetwork(Sickbed sickbed, boolean refresh) {
        mModel.getPatientInfoByNetwork(sickbed.getPatientId(), UserTemp.getInstance().getGroupCode())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (!refresh)
                            mRootView.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (!refresh)
                            mRootView.hideLoading();
                    }
                })
                .subscribe(new ErrorHandleSubscriber<Result<PatientInfo>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<PatientInfo> result) {
                        if (result.isSuccessful()) {
                            mRootView.showPatientInfo(result.getData());
                            savePatientInfo(result.getData());
                        }
                    }
                });
    }

    private void savePatientInfo(PatientInfo patientInfo) {
        mModel.savePatientInfo(patientInfo);
    }
}
