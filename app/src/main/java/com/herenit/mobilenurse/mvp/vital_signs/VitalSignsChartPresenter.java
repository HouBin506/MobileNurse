package com.herenit.mobilenurse.mvp.vital_signs;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.submit.VitalSignsHistoryQuery;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsChartData;

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
 * date: 2019/6/3 11:20
 * desc:体征趋势图 P层
 */
@ActivityScope
public class VitalSignsChartPresenter extends BasePresenter<VitalSignsChartContract.Model, VitalSignsChartContract.View> {
    @Inject
    VitalSignsHistoryQuery mQuery;
    @Inject
    RxErrorHandler mErrorHandler;


    @Inject
    VitalSignsChartPresenter(VitalSignsChartContract.Model model, VitalSignsChartContract.View view) {
        super(model, view);
    }

    /**
     * @param vitalItemID
     */
    public void loadVitalSignsChartData(VitalSignsHistoryQuery.VitalItemID vitalItemID) {
        if (vitalItemID == null)
            return;
        List<VitalSignsHistoryQuery.VitalItemID> idList = new ArrayList<>();
        idList.add(vitalItemID);
        mQuery.setVitalItemIdList(idList);
        mModel.loadVitalSignsChartData(mQuery)
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
                .subscribe(new ErrorHandleSubscriber<Result<VitalSignsChartData>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<VitalSignsChartData> result) {
                        if (result.isSuccessful()) {
                            mRootView.showChart(result.getData());
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }
}
