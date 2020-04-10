package com.herenit.mobilenurse.mvp.nursing_round;

import android.app.Application;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.SelectNameCode;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.NurseRoundItem;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.submit.CommonPatientItemQuery;
import com.herenit.mobilenurse.criteria.entity.submit.OrderListQuery;
import com.herenit.mobilenurse.criteria.entity.submit.VitalSignsHistoryQuery;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewGroup;
import com.herenit.mobilenurse.criteria.enums.ExecuteResultEnum;
import com.herenit.mobilenurse.custom.adapter.NurseRoundHistoryAdapter;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsHelper;

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
 * desc:“护理巡视历史记录”功能  Presenter
 */
@ActivityScope
public class NurseRoundHistoryPresenter extends BasePresenter<NurseRoundHistoryContract.Model, NurseRoundHistoryContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;

    @Inject
    CommonPatientItemQuery mQuery;

    @Inject
    List<SelectNameCode> mSelectNurseRoundItemList;//巡视项目列表

    @Inject
    NurseRoundHistoryAdapter mNurseRoundHistoryAdapter;//巡视历史列表Adapter
    @Inject
    List<NurseRoundItem> mNurseRoundHistoryList; //巡视历史数据（显示）

    @Inject
    NurseRoundHistoryPresenter(NurseRoundHistoryContract.Model model, NurseRoundHistoryContract.View view) {
        super(model, view);
    }


    /**
     * 加载护理巡视历史数据
     *
     * @param refresh
     */
    public void loadNurseRoundHistoryList(boolean refresh) {
        mModel.getNurseRoundHistoryList(mQuery)
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
                .subscribe(new ErrorHandleSubscriber<Result<List<NurseRoundItem>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<NurseRoundItem>> result) {
                        if (refresh)
                            mRootView.loadDataSuccess();
                        if (result.isSuccessful()) {
                            mNurseRoundHistoryList.clear();
                            if (!result.getData().isEmpty())
                                mNurseRoundHistoryList.addAll(result.getData());
                            mNurseRoundHistoryAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }

    public void resetNurseRoundItemCondition() {
        List<String> itemList = new ArrayList<>();
        for (SelectNameCode item : mSelectNurseRoundItemList) {
            if (!item.isChecked())
                continue;
            String code = item.getCode();
            itemList.add(code);
        }
        mQuery.setItemList(itemList);
    }

    /**
     * 删除巡视数据
     *
     * @param deleteList
     */
    public void deleteNurseRoundData(List<NurseRoundItem> deleteList) {
        mModel.deleteNurseRoundData(deleteList)
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
                        if (result.isSuccessful()) {//删除成功则更新数据列表
                            mRootView.showMessage(ArmsUtils.getString(mApplication, R.string.message_deleteSuccess));
                            loadNurseRoundHistoryList(false);
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }
}
