package com.herenit.mobilenurse.mvp.exam.report;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.criteria.entity.ExamReport;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.custom.adapter.ExamReportAdapter;

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
 * desc:“检查报告”功能  Presenter
 */
@FragmentScope
public class ExamReportPresenter extends BasePresenter<ExamReportContract.Model, ExamReportContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    List<ExamReport> mExamReportList;//检查列表数据
    @Inject
    ExamReportAdapter mExamReportAdapter;//检查列表Adapter

    @Inject
    ExamReportPresenter(ExamReportContract.Model model, ExamReportContract.View view) {
        super(model, view);
    }


    public void loadExamReportList(boolean refresh) {
        Sickbed sickbed = mRootView.getCurrentSickbed();
        if (sickbed == null)
            return;
        mModel.getExamReportList(sickbed.getPatientId(), sickbed.getVisitId())
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
                .subscribe(new ErrorHandleSubscriber<Result<List<ExamReport>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<ExamReport>> result) {
                        if (result.isSuccessful()) {
                            mExamReportList.clear();
                            if (result.getData() != null)
                                mExamReportList.addAll(result.getData());
                            mExamReportAdapter.notifyDataSetChanged();
                            mRootView.loadDataSuccess();
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }
}
