package com.herenit.mobilenurse.mvp.assess.health_edu.history;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.criteria.entity.HealthEduHistoryItem;
import com.herenit.mobilenurse.criteria.entity.MultiListMenuItem;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.custom.adapter.HealthEduHistoryAdapter;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;

import java.util.ArrayList;
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
public class HealthEduHistoryPresenter extends BasePresenter<HealthEduHistoryContract.Model, HealthEduHistoryContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;

    //列表数据
    @Inject
    List<HealthEduHistoryItem> mHistoryItemList;
    //列表适配器
    @Inject
    HealthEduHistoryAdapter mHistoryAdapter;

    @Inject
    HealthEduHistoryPresenter(HealthEduHistoryContract.Model model, HealthEduHistoryContract.View view) {
        super(model, view);
    }

    public void loadHealthEduHistory(Sickbed sickbed, boolean refresh) {
        if (sickbed == null)
            return;
        mModel.loadHealthEduHistory(sickbed.getPatientId(), sickbed.getVisitId())
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
                .subscribe(new ErrorHandleSubscriber<Result<List<HealthEduHistoryItem>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<HealthEduHistoryItem>> result) {
                        if (result.isSuccessful()) {
                            mHistoryItemList.clear();
                            if (result.getData() != null)
                                mHistoryItemList.addAll(result.getData());
                            mHistoryAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }

    /**
     * 删除宣教历史记录
     *
     * @param docId
     */
    public void deleteHealthEduHistory(String docId) {
        mModel.deleteHealthEduHistory(docId)
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
                            loadHealthEduHistory(mRootView.sickbed(), false);
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }
}
