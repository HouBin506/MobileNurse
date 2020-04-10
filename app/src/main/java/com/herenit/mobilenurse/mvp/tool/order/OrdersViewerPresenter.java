package com.herenit.mobilenurse.mvp.tool.order;

import android.app.Application;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.FileUtils;
import com.herenit.mobilenurse.app.utils.NetworkUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.submit.OrderListQuery;
import com.herenit.mobilenurse.criteria.entity.submit.OrdersExecute;
import com.herenit.mobilenurse.criteria.enums.ExecuteResultEnum;
import com.herenit.mobilenurse.custom.adapter.ConditionAdapter;
import com.herenit.mobilenurse.custom.adapter.OrdersAdapter;
import com.herenit.mobilenurse.datastore.tempcache.DictTemp;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
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
 * date: 2019/3/12 14:46
 * desc:医嘱详情的P层
 */
@ActivityScope
public class OrdersViewerPresenter extends BasePresenter<OrdersViewerContract.Model, OrdersViewerContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;

    //筛选条件数据
    @Inject
    List<Conditions> mConditionList = new ArrayList<>();
    //医嘱列表数据
    @Inject
    List<Order> mOrderList = new ArrayList<>();


    @Inject
    OrdersViewerPresenter(OrdersViewerContract.Model model, OrdersViewerContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 加载页面数据
     *
     * @param sickbed 当前患者
     * @param refresh 是否为页面刷新
     */
    public void loadData(Sickbed sickbed, boolean refresh) {
        loadConditionList();
        loadPatientOrderList(sickbed, refresh);
    }

    /**
     * 加载页面条件
     */
    private void loadConditionList() {
        mConditionList.clear();
        String json = FileUtils.getAssetsToString(FileUtils.FILE_NAME_UI_CONDITIONS_ORDER);
        if (!TextUtils.isEmpty(json)) {
            List<Conditions> conditionsList = JSON.parseArray(json, Conditions.class);
            if (conditionsList != null)
                mConditionList.addAll(conditionsList);
        }
        Conditions orderClassConditions = Conditions.createConditions(CommonConstant.CONDITION_TYPE_ORDER_CLASS, DictTemp.getInstance().getOrderClassList());
        if (orderClassConditions != null)
            mConditionList.add(orderClassConditions);
        Conditions executeResultConditions = Conditions.createConditions(CommonConstant.CONDITION_TYPE_EXECUTE_RESULT, DictTemp.getInstance().getExecuteResultList());
        if (executeResultConditions != null)
            mConditionList.add(executeResultConditions);
        mRootView.showConditionUI();
    }

    /**
     * 加载患者的医嘱列表
     *
     * @param sickbed
     */
    public void loadPatientOrderList(Sickbed sickbed, boolean refresh) {
        if (NetworkUtils.isNetworkConnected(mApplication)) {//有网
            loadPatientOrderListByNetwork(sickbed, refresh);
        }
    }

    /**
     * 缓存中获取医嘱列表
     *
     * @param sickbed
     */
//    private void loadPatientOrderListByCache(Sickbed sickbed) {
//        List<Order> orderList = mModel.getCacheOrderList(OrderListQuery.createQuery(sickbed, mConditionList));
//        mOrderList.clear();
//        if (orderList != null)
//            mOrderList.addAll(orderList);
//        mRootView.showOrderListUI();
//    }

    /**
     * 网络获取医嘱列表
     *
     * @param sickbed
     */
    private void loadPatientOrderListByNetwork(Sickbed sickbed, boolean refresh) {
        mModel.getOrderListByNetwork(OrderListQuery.createQuery(sickbed, mConditionList))
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
                .subscribe(new ErrorHandleSubscriber<Result<List<Order>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<Order>> result) {
                        if (result.isSuccessful()) {//请求成功
                            Order.setOrderListGroup(result.getData());//设置成组关系
                            mOrderList.clear();
                            if (result.getData() != null)
                                mOrderList.addAll(result.getData());
                            mRootView.showOrderListUI();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }

    /**
     * 将医嘱列表缓存到本地
     *
     * @param sickbed
     * @param orderList 数据源
     */
//    private void savePatientOrderList(Sickbed sickbed, List<Order> orderList) {
//        mModel.updateQueryOrderList(orderList, OrderListQuery.createQuery(sickbed, mConditionList));
//    }
}
