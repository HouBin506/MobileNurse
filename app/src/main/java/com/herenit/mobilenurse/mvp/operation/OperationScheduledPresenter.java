package com.herenit.mobilenurse.mvp.operation;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.criteria.entity.OperationScheduled;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.submit.OperationScheduledQuery;
import com.herenit.mobilenurse.custom.adapter.OperationScheduledAdapter;
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
 * desc:手术安排 Presenter
 */
@FragmentScope
public class OperationScheduledPresenter extends BasePresenter<OperationScheduledContract.Model, OperationScheduledContract.View> {

    @Inject
    OperationScheduledQuery mQuery;//手术列表查询条件
    @Inject
    List<OperationScheduled> mOperationScheduledList;
    @Inject
    OperationScheduledAdapter mOperationScheduledAdapter;
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    OperationScheduledPresenter(OperationScheduledContract.Model model, OperationScheduledContract.View view) {
        super(model, view);
    }

    /**
     * 查询手术安排列表
     */
    public void loadOperationScheduledList(boolean refresh) {
        mQuery.setGroupCode(UserTemp.getInstance().getGroupCode());
        mModel.loadOperationScheduledList(mQuery)
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
                .subscribe(new ErrorHandleSubscriber<Result<List<OperationScheduled>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<OperationScheduled>> result) {
                        if (refresh)
                            mRootView.onRefreshSuccess();
                        if (result.isSuccessful()) {
                            mOperationScheduledList.clear();
                            if (result.getData() != null)
                                mOperationScheduledList.addAll(result.getData());
                            mOperationScheduledAdapter.notifyDataSetChanged();
                            if (UserTemp.getInstance().isOperation())//手术室账号，每次都要检查、提醒紧急未确认的手术
                                filterEmergencyOperation(mOperationScheduledList);
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }

    /**
     * 过滤急诊未确认的手术
     */
    private void filterEmergencyOperation(List<OperationScheduled> operationScheduledList) {
        List<OperationScheduled> emergencyOperations = new ArrayList<>();
        for (OperationScheduled model : operationScheduledList) {
            if (model.isEmergency() && model.isUnconfirmed())
                emergencyOperations.add(model);
        }
        //存在急诊未确认的手术，则进行语音+震动提醒
        if (emergencyOperations.size() > 0)
            mRootView.remindEmergencyAndUnconfirmedOperation(emergencyOperations);
    }
}
