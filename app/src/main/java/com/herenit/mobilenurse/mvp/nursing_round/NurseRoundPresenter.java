package com.herenit.mobilenurse.mvp.nursing_round;

import android.app.Application;

import com.alibaba.fastjson.JSON;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.FileUtils;
import com.herenit.mobilenurse.app.utils.NetworkUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.NurseRoundItem;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.submit.OrderListQuery;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewGroup;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewItem;
import com.herenit.mobilenurse.criteria.enums.ExecuteResultEnum;
import com.herenit.mobilenurse.custom.adapter.NurseRoundItemAdapter;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;

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
 * desc:“护理巡视”功能  Presenter
 */
@FragmentScope
public class NurseRoundPresenter extends BasePresenter<NurseRoundContract.Model, NurseRoundContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    Application mApplication;

    private NurseRoundViewGroup mCurrentUIData;

    @Inject
    NurseRoundPresenter(NurseRoundContract.Model model, NurseRoundContract.View view) {
        super(model, view);
    }


    /**
     * 获取护理巡视列表
     *
     * @param refresh
     */
    public void loadNurseRoundItemList(boolean refresh) {
        loadNurseRoundItemListByNetwork(refresh);
    }

    private void loadNurseRoundItemListByNetwork(boolean refresh) {
        Sickbed sickbed = SickbedTemp.getInstance().getCurrentSickbed();
        OrderListQuery query = new OrderListQuery();
        query.setPatientId(sickbed.getPatientId());
        query.setVisitId(sickbed.getVisitId());
        //TODO 省人民没有执行中的状态，也就是暂时不做护理巡视功能，将此处隐藏掉，以免报错
//        query.setExecuteResult(ExecuteResultEnum.EXECUTING.getCode() + "");
        query.setStartDateTime(TimeUtils.getStartDateTimeByTimeCode(CommonConstant.TIME_CODE_YESTERDAY));
        query.setStopDateTime(TimeUtils.getStartDateTimeByTimeCode(CommonConstant.TIME_CODE_TOMORROW));
        mModel.getNurseRoundItemList(query)
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
                .subscribe(new ErrorHandleSubscriber<Result<NurseRoundViewGroup>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<NurseRoundViewGroup> result) {
                        if (result.isSuccessful()) {//请求成功
                            List<Order> orderList = result.getData().getOrderList();
                            if (orderList != null && !orderList.isEmpty())
                                Order.setOrderListGroup(orderList);//医嘱列表分组设置
                            mRootView.loadNurseRoundListSuccess(result.getData());
                            mCurrentUIData = result.getData();
                        }
                    }
                });
    }

    /**
     * 提交护理巡视数据
     *
     * @param nurseRoundData
     */
    public void commitNurseRoundData(List<NurseRoundItem> nurseRoundData) {
        if (nurseRoundData == null || nurseRoundData.isEmpty())
            return;
        mModel.commitNurseRoundData(nurseRoundData)
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
                        if (result.isSuccessful()) {//请求成功
                            mRootView.showMessage(ArmsUtils.getString(mApplication, R.string.message_saveSuccess));
                            //提交成功之后，将页面数据操作历史清空
                            clearRecordingData();
                        } else {
                            mRootView.showError(ArmsUtils.getString(mApplication, R.string.error_saveError), result.getMessage());
                        }
                    }
                });
    }

    /**
     * 清空页面记录状态
     */
    private void clearRecordingData() {
        if (mCurrentUIData == null)
            return;
        List<NurseRoundViewItem> commonRoundList = mCurrentUIData.getCommonRoundList();
        List<NurseRoundViewItem> infusionRoundList = mCurrentUIData.getInfusionRoundList();
        clearNurseRoundData(commonRoundList);
        clearNurseRoundData(infusionRoundList);
        mRootView.loadNurseRoundListSuccess(mCurrentUIData);
    }

    private void clearNurseRoundData(List<NurseRoundViewItem> nurseRoundList) {
        if (nurseRoundList == null)
            return;
        for (NurseRoundViewItem item : nurseRoundList) {
            item.setChecked(false);
            item.setEditable(false);
            item.setValue("");
            item.setTimePoint(null);
            item.setItemDescription("");
        }
    }
}
