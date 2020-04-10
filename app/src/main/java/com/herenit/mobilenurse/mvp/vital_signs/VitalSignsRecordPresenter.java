package com.herenit.mobilenurse.mvp.vital_signs;

import android.app.Application;
import android.text.TextUtils;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.NetworkUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsViewItem;
import com.herenit.mobilenurse.custom.adapter.VitalSignsItemAdapter;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;

import java.util.ArrayList;
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
 * date: 2019/4/10 10:06
 * desc:体征的P层
 */
@FragmentScope
public class VitalSignsRecordPresenter extends BasePresenter<VitalSignsRecordContract.Model, VitalSignsRecordContract.View> {
    @Inject
    Application mApplication;
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    List<VitalSignsViewItem> mVitalItemList;
    @Inject
    VitalSignsItemAdapter mVitalListAdapter;

    @Inject
    VitalSignsRecordPresenter(VitalSignsRecordContract.Model model, VitalSignsRecordContract.View view) {
        super(model, view);
    }

    /**
     * 加载体征项目列表
     */
    public void loadVitalSignsList(boolean refresh) {
        if (NetworkUtils.isNetworkConnected(mApplication)) {//有网
            loadVitalSignsListByNetwork(refresh);
        } else {//没网
            loadVitalSignsListByCache();
        }
    }

    /**
     * 本地加载体征列表
     */
    private void loadVitalSignsListByCache() {

    }

    /**
     * 网络加载体征列表
     */
    private void loadVitalSignsListByNetwork(boolean refresh) {
        mModel.getPatientVitalSignsList(UserTemp.getInstance().getGroupCode(),
                SickbedTemp.getInstance().getCurrentSickbed().getPatientId(),
                SickbedTemp.getInstance().getCurrentSickbed().getVisitId())
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
                .subscribe(new ErrorHandleSubscriber<Result<List<VitalSignsViewItem>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<VitalSignsViewItem>> result) {
                        if (refresh)
                            mRootView.onRefreshSuccess();
                        if (result.isSuccessful()) {
                            mVitalItemList.clear();
                            if (result.getData() != null)
                                mVitalItemList.addAll(result.getData());
                            mVitalListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 保存体征数据
     */
    public void saveVitalSignsData(Date recordDate) {
        if (mVitalItemList == null || mVitalItemList.size() == 0) {
            mRootView.showMessage(ArmsUtils.getString(mApplication, R.string.message_empty_commitData));
            return;
        }
        List<VitalSignsItem> recordDataList = new ArrayList<>();
        for (VitalSignsViewItem item : mVitalItemList) {
            if(item.getItemName().equals(CommonConstant.VITAL_SIGNS_ADMISSION))//“入院项”不可提交
                continue;
            if (CommonConstant.ITEM_TYPE_GROUP.equals(item.getItemType()))//组类型，无意义，不需要遍历
                continue;
            //不需要录入数据的项目，未选中，不需要提交
            if ((!item.isUseValueView() && !item.isUseSpecialValueView()) && !item.isChecked())
                continue;
            //需要录入值、例外值的项目，却没有录入值，则不提交
            if ((item.isUseValueView() || item.isUseSpecialValueView()) &&
                    (TextUtils.isEmpty(item.getValue()) && TextUtils.isEmpty(item.getSpecialValue())))
                continue;
            //构建提交的数据
            VitalSignsItem recordData = new VitalSignsItem();
            recordData.setClassCode(item.getClassCode());
            recordData.setItemCode(item.getItemCode());
            recordData.setItemName(item.getItemName());
            recordData.setItemValue(item.getValue());
            recordData.setMemo(item.getMemo());
            recordData.setNurse(UserTemp.getInstance().getUserId());
            Sickbed sickbed = SickbedTemp.getInstance().getCurrentSickbed();
            recordData.setPatientId(sickbed.getPatientId());
            recordData.setSpecialValue(item.getSpecialValue());
            if (recordDate != null)
                recordData.setRecordingDate(recordDate.getTime());
            Long timePoint;
            if (!TextUtils.isEmpty(item.getFixedTimePoint())) {//记录固定时间点的
                timePoint = TimeUtils.getDateByFormat(TimeUtils.FORMAT_YYYYMMDDHHMM,
                        TimeUtils.getYYYYMMDDString(item.getTimePoint()) + " " + item.getFixedTimePoint()).getTime();
            } else {
                timePoint = item.getTimePoint();
            }
            recordData.setTimePoint(timePoint);
            recordData.setUnit(item.getUnit());
            recordData.setVisitId(sickbed.getVisitId());
            recordData.setWardCode(UserTemp.getInstance().getGroupCode());
            recordDataList.add(recordData);
        }
        if (recordDataList.size() == 0) {
            mRootView.showMessage(ArmsUtils.getString(mApplication, R.string.message_empty_commitData));
            return;
        }
        //走到这一步，进行体征数据保存提交
        mModel.postVitalSignsRecord(recordDataList)
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
