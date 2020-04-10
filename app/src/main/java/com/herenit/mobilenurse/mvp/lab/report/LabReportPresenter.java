package com.herenit.mobilenurse.mvp.lab.report;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.criteria.entity.lab.CommonLabReport;
import com.herenit.mobilenurse.criteria.entity.lab.MicroorganismLabReport;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.custom.adapter.LabReportAdapter;
import com.herenit.mobilenurse.custom.adapter.delegate.MicroorganismLabReportAdapter;

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
 * desc:“检验报告”功能  Presenter
 */
@FragmentScope
public class LabReportPresenter extends BasePresenter<LabReportContract.Model, LabReportContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    List<CommonLabReport> mLabReportList;//普通检验列表数据
    @Inject
    LabReportAdapter mLabReportAdapter;//普通检验列表Adapter

    @Inject
    List<MicroorganismLabReport> mMicroorganismLabReportList;//微生物检验列表数据
    @Inject
    MicroorganismLabReportAdapter mMicroorganismLabReportAdapter;//微生物检验列表Adapter


    @Inject
    LabReportPresenter(LabReportContract.Model model, LabReportContract.View view) {
        super(model, view);
    }


    public void loadLabReportList(boolean refresh) {
        Sickbed sickbed = mRootView.getCurrentSickbed();
        if (sickbed == null)
            return;
        mModel.getLabReportList(sickbed.getPatientId(), sickbed.getVisitId())
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
                .subscribe(new ErrorHandleSubscriber<Result<List<CommonLabReport>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<CommonLabReport>> result) {
                        if (result.isSuccessful()) {
                            mLabReportList.clear();
                            if (result.getData() != null)
                                mLabReportList.addAll(result.getData());
                            mLabReportAdapter.notifyDataSetChanged();
                            mRootView.loadDataSuccess();
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }

    public void loadMicroorganismLabReportList(boolean refresh) {
        Sickbed sickbed = mRootView.getCurrentSickbed();
        if (sickbed == null)
            return;
        mModel.getMicroorganismLabReportList(sickbed.getPatientId(), sickbed.getVisitId())
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
                .subscribe(new ErrorHandleSubscriber<Result<List<MicroorganismLabReport>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<MicroorganismLabReport>> result) {
                        if (result.isSuccessful()) {
                            mMicroorganismLabReportList.clear();
                            if (result.getData() != null)
                                mMicroorganismLabReportList.addAll(result.getData());
                            mMicroorganismLabReportAdapter.notifyDataSetChanged();
                            mRootView.loadDataSuccess();
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }
}
