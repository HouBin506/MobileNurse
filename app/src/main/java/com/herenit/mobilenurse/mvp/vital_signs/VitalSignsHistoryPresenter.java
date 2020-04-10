package com.herenit.mobilenurse.mvp.vital_signs;

import android.app.Application;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.NameCode;
import com.herenit.mobilenurse.criteria.common.SelectNameCode;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.submit.VitalSignsHistoryQuery;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsViewItem;
import com.herenit.mobilenurse.custom.adapter.VitalSignsHistoryAdapter;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
 * date: 2019/5/17 9:49
 * desc: 体征历史记录的P层
 */
@ActivityScope
public class VitalSignsHistoryPresenter extends BasePresenter<VitalSignsHistoryContract.Model, VitalSignsHistoryContract.View> {

    @Inject
    VitalSignsHistoryAdapter mVitalSignsAdapter;//体征历史列表Adapter
    @Inject
    List<VitalSignsItem> mVitalSignsList; //体征历史数据（显示）；
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    List<SelectNameCode> mSelectVitalItemList;//体征项目列表
    @Inject
    Application mApplication;

    /**
     * 体征历史数据查询条件
     */
    @Inject
    VitalSignsHistoryQuery mQuery;

    @Inject
    VitalSignsHistoryPresenter(VitalSignsHistoryContract.Model model, VitalSignsHistoryContract.View view) {
        super(model, view);
    }

    /**
     * 加载体征历史数据
     *
     * @param refresh
     */
    public void loadVitalSignsHistory(boolean refresh) {
        mModel.getVitalSignsHistoryList(mQuery)
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
                .subscribe(new ErrorHandleSubscriber<Result<List<VitalSignsItem>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<VitalSignsItem>> result) {
                        if (refresh)
                            mRootView.refreshSuccess();
                        if (result.isSuccessful()) {
                            mVitalSignsList.clear();
                            if (!result.getData().isEmpty())
                                mVitalSignsList.addAll(result.getData());
                            mVitalSignsAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }

    /**
     * 更新体征
     *
     * @param vitalSignsItem
     */
    public void updateVitalSigns(VitalSignsItem vitalSignsItem) {
        if (vitalSignsItem == null)
            return;
        List<VitalSignsItem> vitalSignsItemList = new ArrayList<>();
        vitalSignsItemList.add(vitalSignsItem);
        mModel.updateVitalSigns(vitalSignsItemList)
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
                            mRootView.showMessage(ArmsUtils.getString(mApplication, R.string.message_updateSuccess));
                            loadVitalSignsHistory(false);

                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }

    /**
     * 删除体征
     *
     * @param vitalSignsItem
     */
    public void deleteVitalSigns(VitalSignsItem vitalSignsItem) {
        if (vitalSignsItem == null)
            return;
        List<VitalSignsItem> vitalSignsItemList = new ArrayList<>();
        vitalSignsItemList.add(vitalSignsItem);
        mModel.deleteVitalSigns(vitalSignsItemList)
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
                            mRootView.showMessage(ArmsUtils.getString(mApplication, R.string.message_deleteSuccess));
                            loadVitalSignsHistory(false);
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }

    /**
     * 重置一下体征历史数据查询条件，这里主要是针对要查询的体征项目作出设置
     */
    public void resetVitalItemCondition() {
        List<VitalSignsHistoryQuery.VitalItemID> itemList = new ArrayList<>();
        for (SelectNameCode item : mSelectVitalItemList) {
            if (!item.isChecked())
                continue;
            String code = item.getCode();
            String[] codeArr = VitalSignsHelper.getItemAndClassCode(code);
            itemList.add(new VitalSignsHistoryQuery.VitalItemID(codeArr[0], codeArr[1], item.getName()));
        }
        mQuery.setVitalItemIdList(itemList);
    }
}
