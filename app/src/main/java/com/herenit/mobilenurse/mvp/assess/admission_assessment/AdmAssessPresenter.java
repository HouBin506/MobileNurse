package com.herenit.mobilenurse.mvp.assess.admission_assessment;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.criteria.entity.AssessModel;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.AssessParam;

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
 * desc:“入院评估”功能  Presenter
 */
@ActivityScope
public class AdmAssessPresenter extends BasePresenter<AdmAssessContract.Model, AdmAssessContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    AdmAssessPresenter(AdmAssessContract.Model model, AdmAssessContract.View view) {
        super(model, view);
    }

    /**
     * 加载数据
     *
     * @param refresh
     */
    public void getAdmissionAssessModel(String patientId, String visitId, boolean refresh) {
        mModel.getAdmissionAssessModel(patientId, visitId)
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
                .subscribe(new ErrorHandleSubscriber<Result<AssessModel>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<AssessModel> result) {
                        if (result.isSuccessful()) {
                            mRootView.getAdmissionAssessModelSuccess(result.getData());
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }

    /**
     * 提交入院评估数据
     */
    public void commitAdmissionAssessmentData(AssessParam param) {
        mModel.commitAdmissionAssessData(param)
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
                            mRootView.saveAdmissionAssessDataSuccess();
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }
}
