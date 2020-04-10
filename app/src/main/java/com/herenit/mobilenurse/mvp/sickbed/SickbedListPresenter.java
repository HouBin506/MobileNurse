package com.herenit.mobilenurse.mvp.sickbed;

import android.app.Application;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.app.utils.FileUtils;
import com.herenit.mobilenurse.app.utils.NetworkUtils;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.submit.SickbedListQuery;
import com.herenit.mobilenurse.datastore.tempcache.DictTemp;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.custom.adapter.ConditionAdapter;
import com.herenit.mobilenurse.custom.adapter.SickbedAdapter;

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
 * date: 2019/2/16 16:45
 * desc: 床位列表Presenter
 */
@FragmentScope
public class SickbedListPresenter extends BasePresenter<SickbedListContract.Model, SickbedListContract.View> {
    @Inject
    Application mApplication;

    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    List<Conditions> mConditionList;

    @Inject
    SickbedListQuery mQuery;

    @Inject
    List<Sickbed> mSickbedList;

    @Inject
    ConditionAdapter mConditionAdapter;//条件列表Adapter

    @Inject
    SickbedAdapter mSickbedAdapter;//床位列表Adapter


    @Inject
    public SickbedListPresenter(SickbedListContract.Model model, SickbedListContract.View rootView) {
        super(model, rootView);
    }

    public void loadData(boolean refresh) {
        initQueryCondition(refresh);
        loadSickbedList(refresh);
    }

    /**
     * 初始化查询条件，时间、所有、护理等级、病情等级、手术条件等
     */
    private void initQueryCondition(boolean refresh) {
        mConditionList.clear();
        //先从获取一部分查询条件
        String json = FileUtils.getAssetsToString(FileUtils.FILE_NAME_UI_CONDITIONS_SICKBED);
        if (!TextUtils.isEmpty(json)) {
            List<Conditions> list = JSON.parseArray(json, Conditions.class);
            if (list != null)
                mConditionList.addAll(list);
        }
        //获取病情等级条件
        Conditions patientConditionConditions = Conditions.createConditions(CommonConstant.CONDITION_TYPE_PATIENT_CONDITION, DictTemp.getInstance().getPatientConditionList());
        if (patientConditionConditions != null)
            mConditionList.add(patientConditionConditions);
        //获取护理等级条件
        Conditions nursingClassConditions = Conditions.createConditions(CommonConstant.CONDITION_TYPE_NURSING_CLASS, DictTemp.getInstance().getNursingClassList());
        if (nursingClassConditions != null)
            mConditionList.add(nursingClassConditions);
        mConditionAdapter.notifyDataSetChanged();
        mRootView.loadConditionsFinish();
    }

    /**
     * 获取床位列表
     */
    public void loadSickbedList(boolean refresh) {
        if (NetworkUtils.isNetworkConnected(mApplication)) {//有网
            getSickbedListByNetwork(SickbedListQuery.createQueryByCondition(mConditionList), refresh);
        } else {//没网
            getSickbedListByCache();
        }
    }

    /**
     * 缓存中获取床位列表
     */
    private void getSickbedListByCache() {
        List<Sickbed> sickbedList = mModel.getCacheSickbedList(UserTemp.getInstance().getGroupCode());
        SickbedTemp.getInstance().setSickbedList(sickbedList);
        mSickbedList.clear();
        if (sickbedList != null)
            mSickbedList.addAll(sickbedList);
        mSickbedAdapter.notifyDataSetChanged();
        mRootView.showPatientNum(mSickbedList.size());
    }

    /**
     * 网络获取床位列表
     *
     * @param query
     */
    private void getSickbedListByNetwork(SickbedListQuery query, boolean refresh) {
        mModel.getSickbedList(query)
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
                        else
                            mRootView.finishRefresh();
                    }
                })
                .subscribe(new ErrorHandleSubscriber<Result<List<Sickbed>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<Sickbed>> result) {
                        if (result.isSuccessful()) {
                            SickbedTemp.getInstance().setSickbedList(result.getData());
                            mModel.updateSickbedList(result.getData());
                            mSickbedList.clear();
                            if (result.getData() != null)
                                mSickbedList.addAll(result.getData());
                            mSickbedAdapter.notifyDataSetChanged();
                            mRootView.showPatientNum(mSickbedList.size());
                        }
                    }
                });
    }
}
