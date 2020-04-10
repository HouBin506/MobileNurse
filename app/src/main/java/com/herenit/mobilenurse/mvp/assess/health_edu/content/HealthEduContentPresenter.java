package com.herenit.mobilenurse.mvp.assess.health_edu.content;

import android.app.Application;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.entity.AssessModel;
import com.herenit.mobilenurse.criteria.entity.HealthEduAssessModel;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.HealthEduAssessParam;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;

import java.util.List;

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
 * date: 2019/2/18 14:00
 * desc:“健康宣教”功能  Presenter
 */
@ActivityScope
public class HealthEduContentPresenter extends BasePresenter<HealthEduContentContract.Model, HealthEduContentContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    Application mApplication;


    @Inject
    HealthEduContentPresenter(HealthEduContentContract.Model model, HealthEduContentContract.View view) {
        super(model, view);
    }

    /**
     * 获取健康宣教结果列表样式数据
     */
    public void getHealthEduResult(boolean refresh,String docId) {
        mModel.getHealthEduResult(docId)
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
                .subscribe(new ErrorHandleSubscriber<Result<HealthEduAssessModel>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<HealthEduAssessModel> result) {
                        if (result.isSuccessful()) {
                            mRootView.showHealthEduResult(result.getData());
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }

    public void commitHealthEduContent(HealthEduAssessParam param) {
        mModel.saveOrUpdateHealthEduContent(param)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .subscribe(new ErrorHandleSubscriber<Result>(mErrorHandler) {
                    @Override
                    public void onNext(Result result) {
                        if (result.isSuccessful()) {
                            mRootView.showMessage(ArmsUtils.getString(mApplication, R.string.message_saveSuccess));
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }
}
