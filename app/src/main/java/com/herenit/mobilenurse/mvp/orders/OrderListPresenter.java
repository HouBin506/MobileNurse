package com.herenit.mobilenurse.mvp.orders;

import android.app.Application;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.FileUtils;
import com.herenit.mobilenurse.app.utils.NetworkUtils;
import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.ConditionItem;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Newborn;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.OrderPerformLabel;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.criteria.entity.submit.OrderListQuery;
import com.herenit.mobilenurse.criteria.entity.submit.OrdersExecute;
import com.herenit.mobilenurse.criteria.enums.ExecuteClassEnum;
import com.herenit.mobilenurse.criteria.enums.ExecuteResultEnum;
import com.herenit.mobilenurse.datastore.tempcache.DictTemp;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;

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
 * date: 2019/3/6 15:51
 * desc:医嘱列表的P层
 */
@FragmentScope
public class OrderListPresenter extends BasePresenter<OrderListContract.Model, OrderListContract.View> {

    //    private Sickbed sickbed;
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;

    @Inject
    List<Conditions> mConditionList;

    @Inject
    List<Order> mOrderList;
    /**
     * 当前操作列表，存储当前正在操作的医嘱,存储recordId
     */
    private List<OrdersExecute> mCurrentOperationList = new ArrayList<>();

    @Inject
    OrderListPresenter(OrderListContract.Model model, OrderListContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 加载页面数据
     *
     * @param sickbed 当前患者
     * @param refresh 是否为页面刷新
     */
    public void loadData(Sickbed sickbed, boolean refresh) {
        loadConditionList(sickbed, refresh);
    }


    /**
     * 加载页面条件
     */
    private void loadConditionList(Sickbed sickbed, boolean refresh) {
        mConditionList.clear();
        String json = FileUtils.getAssetsToString(FileUtils.FILE_NAME_UI_CONDITIONS_ORDER);
        if (!TextUtils.isEmpty(json)) {
            List<Conditions> conditionsList = JSON.parseArray(json, Conditions.class);
            if (conditionsList != null)
                mConditionList.addAll(conditionsList);
        }

        //母婴床，要显示母婴筛选条件
        if (sickbed != null && sickbed.getBabyList() != null && !sickbed.getBabyList().isEmpty()) {
            List<Newborn> babyList = sickbed.getBabyList();
            Conditions maternalAndInfantsConditions = Conditions.createConditions(CommonConstant.CONDITION_TYPE_MATERNAL_AND_INFANTS, babyList);
            if (maternalAndInfantsConditions != null) {
                List<ConditionItem> itemList = maternalAndInfantsConditions.getConditions();
                if (itemList != null && !itemList.isEmpty()) {
                    //把新生儿妈妈也添加进去
                    itemList.add(0, new ConditionItem("0", sickbed.getPatientName(), false));
                    int babyIndex = sickbed.getCurrentBabyIndex();
                    if (babyIndex < 0)
                        itemList.get(0).setChecked(true);
                    else
                        itemList.get(babyIndex + 1).setChecked(true);
                }
            }
            mConditionList.add(0, maternalAndInfantsConditions);
        }

        Conditions orderClassConditions = Conditions.createConditions(CommonConstant.CONDITION_TYPE_ORDER_CLASS, DictTemp.getInstance().getOrderClassList());
        if (orderClassConditions != null)
            mConditionList.add(mConditionList.size() - 1, orderClassConditions);
        getOrderPerformLabelList(sickbed, refresh);
    }

    /**
     * 获取执行单类别
     */
    private void getOrderPerformLabelList(Sickbed sickbed, boolean refresh) {
        mModel.getOrderPerformLabelList(UserTemp.getInstance().getGroupCode())
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
                .subscribe(new ErrorHandleSubscriber<Result<List<OrderPerformLabel>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<OrderPerformLabel>> result) {
                        if (result.isSuccessful()) {//请求成功
                            List<OrderPerformLabel> performLabelList = result.getData();
                            if (performLabelList != null && !performLabelList.isEmpty()) {
                                mConditionList.add(mConditionList.size() - 1, Conditions.createConditions(CommonConstant.CONDITION_TYPE_ORDER_PERFORM_LABEL, performLabelList));
                            }
                        }
                        mRootView.showConditionUI();
                        loadPatientOrderList(sickbed, refresh, false, false);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }


    /**
     * 加载患者医嘱列表
     *
     * @param sickbed             床位
     * @param refresh             是否刷新
     * @param skinTestCheck       是否做皮试医嘱的检测（查询到需要录入结果的皮试医嘱，则执行）
     * @param afterSuccessExecute 是否是刚执行成功后的一次调用，如果是刚执行成功之后的一次调用，则要更改
     */
    public void loadPatientOrderList(Sickbed sickbed, boolean refresh, boolean skinTestCheck, boolean afterSuccessExecute) {
        if (NetworkUtils.isNetworkConnected(mApplication)) {//有网
            loadPatientOrderListByNetwork(sickbed, refresh, skinTestCheck, afterSuccessExecute);
        }
    }

//    /**
//     * 缓存中获取医嘱列表
//     *
//     * @param sickbed
//     */
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
    private void loadPatientOrderListByNetwork(Sickbed sickbed, boolean refresh, boolean skinTestCheck, boolean afterSuccessExecute) {
        mModel.getOrderListByNetwork(buildConditionList(sickbed, afterSuccessExecute))
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
                            if (skinTestCheck) {//检查是否含有需要皮试的医嘱，如果包含，则先提示执行录入皮试结果
                                skinTestCheck(result.getData());
                            } else {
                                List<Order> orderList = result.getData();
                                if (afterSuccessExecute) {
                                    orderList = setNewExecute(orderList);
                                }
                                Order.setOrderListGroup(orderList);//设置成组关系
                                mOrderList.clear();
                                if (orderList != null)
                                    mOrderList.addAll(orderList);
                                mRootView.showOrderListUI();
                            }
                        }
                        //界面数据更新完成，清除掉正在执行列表中的数据
                        mCurrentOperationList.clear();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }

    /**
     * 将新执行的医嘱设置到医嘱列表的最前排
     *
     * @param orderList
     * @return
     */
    private List<Order> setNewExecute(List<Order> orderList) {
        if (mCurrentOperationList.isEmpty() || orderList == null || orderList.isEmpty())
            return orderList;
        List<String> newExecuteRecordIdList = new ArrayList<>();
        for (OrdersExecute execute : mCurrentOperationList) {
            if (!newExecuteRecordIdList.contains(execute.getRecordId()))
                newExecuteRecordIdList.add(execute.getRecordId());
        }
        List<Order> newExecute = new ArrayList<>();
        List<Order> otherList = new ArrayList<>();
        for (Order order : orderList) {
            if (newExecuteRecordIdList.contains(order.getRecordId())) {
                order.setNewExecute(true);
                newExecute.add(order);
            } else {
                order.setNewExecute(false);
                otherList.add(order);
            }
        }
        if (!otherList.isEmpty())
            newExecute.addAll(otherList);
        return newExecute;
    }

    /**
     * 构建查询列表
     *
     * @param sickbed             当前患者
     * @param afterSuccessExecute 是否刚执行完医嘱之后的加载
     * @return
     */
    private OrderListQuery buildConditionList(Sickbed sickbed, boolean afterSuccessExecute) {
        if (!afterSuccessExecute || mCurrentOperationList.isEmpty())
            return OrderListQuery.createQuery(sickbed, mConditionList);
//        OrdersExecute execute = mCurrentOperationList.get(0);
        List<String> selectedItemCode = new ArrayList<>();
        selectedItemCode.add(CommonConstant.COMMON_CODE_ALL);
        //已执行
       /* if (ExecuteResultEnum.isExecuted(execute.getExecuteResult()) && !TextUtils.isEmpty(execute.getStopNurseId())) {
            selectedItemCode.add(String.valueOf(ExecuteResultEnum.EXECUTED.getCode()));
        } else if (!ExecuteResultEnum.isExecuted(execute.getExecuteResult()) && !TextUtils.isEmpty(execute.getProcessingNurseId()) && TextUtils.isEmpty(execute.getVerifyNurseId())) {
            selectedItemCode.add(String.valueOf(ExecuteResultEnum.NOT_VERIFY.getCode()));
        } else {
            selectedItemCode.add(String.valueOf(ExecuteResultEnum.NONEXECUTION.getCode()));
        }*/
        OrderListQuery.changeCondition(mConditionList, CommonConstant.CONDITION_TYPE_EXECUTE_RESULT, selectedItemCode);
        mRootView.showConditionUI();
        return OrderListQuery.createQuery(sickbed, mConditionList);
    }

    /**
     * 扫码执行医嘱
     *
     * @param scanResult 扫码结果
     */
    public void scanOrderExecute(ScanResult scanResult) {
        mModel.getPatientPerformOrdersByBarCode(SickbedTemp.getInstance().getCurrentSickbed().getPatientId(),
                scanResult.getSuffix(), scanResult.getPrefix())
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
                .subscribe(new ErrorHandleSubscriber<Result<List<Order>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<Order>> result) {
                        if (result.isSuccessful()) {
                            //先检查母婴关系，不要执行错了
                            if (checkMaternalAndInfants(result.getData()))
                                executeNotSkinTestOrders(result.getData());
                        } else {
                            mRootView.executeFailed(
                                    result.getMessage());
                        }
                    }
                });
    }


    /**
     * 母婴医嘱的核对检测  为了避免再查看新生儿医嘱列表的时候，扫描母亲的医嘱，或者查看母亲的医嘱的时候，扫描新生儿的医嘱
     *
     * @param orderList
     * @return
     */
    private boolean checkMaternalAndInfants(List<Order> orderList) {
        if (orderList == null || orderList.isEmpty())
            return true;//检测的医嘱列表为空，直接通过检测
        Sickbed sickbed = SickbedTemp.getInstance().getCurrentSickbed();
        if (sickbed == null)
            return true;
        for (Order order : orderList) {
            String babyNo = order.getBabyNo();
            String patientId = order.getPatientId();
            if (!patientId.equals(sickbed.getPatientId())) {
                mRootView.executeFailed(
                        ArmsUtils.getString(mApplication, R.string.message_notCurrentPatientOrders));
                return false;
            }
            if (TextUtils.isEmpty(babyNo) || "0".equals(babyNo)) {//是大人的医嘱，并非婴儿的医嘱
                if (sickbed.getCurrentBaby() != null) {
                    mRootView.executeFailed(
                            ArmsUtils.getString(mApplication, R.string.message_notCurrentPatientOrders));
                    return false;
                }
            } else {//是婴儿的医嘱
                Newborn newborn = sickbed.getCurrentBaby();
                if (newborn == null || !babyNo.equals(String.valueOf(newborn.getBabyNo()))) {
                    mRootView.executeFailed(
                            ArmsUtils.getString(mApplication, R.string.message_notCurrentPatientOrders));
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 执行医嘱
     *
     * @param executeList  执行的列表
     * @param isNextVerify 完成这一步执行，下一步是否要执行双签，如果下一步要执行签名核对吗，则此执行不算完，要进入签名执行
     */
    public void execute(List<OrdersExecute> executeList, boolean isNextVerify) {
        if (executeList == null || executeList.isEmpty())
            return;
        //更新当前正在执行的列表，用于后面界面显示
        setCurrentOperation(executeList);
        mModel.executeOrders(executeList)
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
                        if (result.isSuccessful()) {//执行成功
                            mRootView.showMessage(ArmsUtils.getString(mApplication, R.string.message_executeSuccess));
                            mRootView.clearVerifyList();
                            loadPatientOrderList(SickbedTemp.getInstance().getCurrentSickbed(), false, false, true);
                            if (isNextVerify) {//如果是皮试医嘱执行成功，则接下来还需要继续执行签名核对核对
                                List<String> verifyList = new ArrayList<>();
                                for (OrdersExecute execute : executeList) {
                                    if (!verifyList.contains(execute.getRecordId()))
                                        verifyList.add(execute.getRecordId());
                                    mRootView.executeDoubleSignatureOrders(executeList, verifyList);
                                }
                            } else {
                                mRootView.showSuccessVoice();
                            }
                        } else {
                            mRootView.executeFailed(result.getMessage());
                        }
                    }
                });
    }


    /**
     * 检测是否有待录入结果的皮试医嘱，如果有则提示执行
     *
     * @param orderList
     */
    private void skinTestCheck(List<Order> orderList) {
        if (orderList == null || orderList.isEmpty())
            return;
        String skinTestRecordGroupId = "";
        List<Order> skinTestOrders = new ArrayList<>();
        for (Order order : orderList) {
            if (order.needExecuteSkinTestResult()) {
                if (TextUtils.isEmpty(skinTestRecordGroupId))
                    skinTestRecordGroupId = order.getRecordGroupId();
                if (skinTestRecordGroupId.equals(order.getRecordGroupId()))
                    skinTestOrders.add(order);
            }
        }
        if (!skinTestOrders.isEmpty()) {//有皮试医嘱要执行
            mRootView.executeSkinTestOrder(skinTestOrders);
        }
    }

//    /**
//     * 将医嘱列表缓存到本地
//     *
//     * @param sickbed
//     * @param orderList 数据源
//     */
//    private void savePatientOrderList(Sickbed sickbed, List<Order> orderList) {
//        mModel.updateQueryOrderList(orderList, OrderListQuery.createQuery(sickbed, mConditionList));
//    }


    /**
     * 执行非皮试医嘱
     *
     * @param orderList
     */
    public void executeNotSkinTestOrders(List<Order> orderList) {
        if (orderList == null || orderList.isEmpty()) {
            mRootView.executeFailed(
                    ArmsUtils.getString(mApplication, R.string.message_notCurrentPatientOrdersOrCanNotExecute));
            return;
        }
        List<String> nextVerifyList = new ArrayList<>();//下次执行需要双签的。将recordId存入
        List<String> verifyList = new ArrayList<>();//需要核对（双签名) 。将recordId存入
        List<Order> skinTestList = new ArrayList<>();
        for (int x = 0; x < orderList.size(); x++) {//从扫描到的医嘱与当前界面的医嘱比对，如果有，则执行扫描到的医嘱
            Order order = orderList.get(x);
            if (ExecuteResultEnum.isExecuted(order.getExecuteResult())) {//医嘱已被执行
                mRootView.executeFailed(
                        ArmsUtils.getString(mApplication, R.string.message_executedOrder) + "：" + order.getOrderText());
                return;
            }
            if (order.isST() && TextUtils.isEmpty(order.getPerformResult())) {//执行列表包含未执行的皮试处置医嘱，则
                skinTestList.add(order);
            }
            if (order.needVerify()) {//未核对的
                if (!verifyList.contains(order.getRecordId()))
                    verifyList.add(order.getRecordId());
            }
            if (order.nextNeedVerify()) {//本次执行成功之后的下一次执行，需要双签名
                if (!nextVerifyList.contains(order.getRecordId()))
                    nextVerifyList.add(order.getRecordId());
            }
        }
        if (!skinTestList.isEmpty()) {//要执行的医嘱中，包含了未执行的皮试处置医嘱，但不全是皮试医嘱，不可执行
            if (orderList.size() > skinTestList.size()) {
                mRootView.executeFailed(
                        ArmsUtils.getString(mApplication, R.string.message_skinTestExecuteWithOtherTogether));
                return;
            } else {
                for (Order order : skinTestList) {
                    if (!order.needExecuteSkinTestResult()) {//皮试医嘱中，包含有暂时不需要录入结果的皮试医嘱，也就是说皮试药品还未执行
                        mRootView.executeFailed(
                                ArmsUtils.getString(mApplication, R.string.message_pleaseExecuteSkinTestDrugFirst));
                        return;
                    }
                }
                //满足条件，则限制性皮试医嘱，其它医嘱不执行
                mRootView.executeSkinTestOrder(skinTestList);
                return;
            }
        }
        List<OrdersExecute> executeList = buildExecuteList(orderList);
        //要执行的医嘱，全部都是双签名的医嘱，并且本次执行完成之后，下一次就需要双签名
        if (!nextVerifyList.isEmpty() && nextVerifyList.size() == orderList.size()) {
            execute(executeList, true);
            return;
        }
        if (verifyList != null && !verifyList.isEmpty()) {//需要双签核对的
            mRootView.executeDoubleSignatureOrders(executeList, verifyList);
        } else {//直接执行
            execute(executeList, false);
        }
    }

    /**
     * 构建执行列表
     *
     * @param executeOrderList
     * @return
     */
    public List<OrdersExecute> buildExecuteList(List<Order> executeOrderList) {
        List<OrdersExecute> executeList = new ArrayList<>();
        for (Order order : executeOrderList) {
            if (ExecuteResultEnum.isExecuted(order.getExecuteResult()))
                continue;
            OrdersExecute execute;
            Integer executeResult;
            if (order.getIsDoubleSignature() && TextUtils.isEmpty(order.getProcessingNurseId()))
                executeResult = ExecuteResultEnum.NONEXECUTION.getCode();//需要双签的医嘱，并且一次都没执行的，则状态为未执行
            else
                executeResult = ExecuteResultEnum.EXECUTED.getCode();
            execute = OrdersHelp.buildGroupOrderExecute(order, null, executeResult);
            if (execute != null)
                executeList.add(execute);
        }
        return executeList;
    }


    /**
     * 执行双签名的医嘱
     *
     * @param executeList
     * @param verifyList
     * @param loginName
     * @param password
     */
    public void executeDoubleSignatureOrders(List<OrdersExecute> executeList, List<String> verifyList, String loginName, String password) {
        //将签名附上去
        for (OrdersExecute execute : executeList) {
            if (verifyList.contains(execute.getRecordId())) {
                //执行双签，要把执行状态设为已执行
                execute.setExecuteResult(ExecuteResultEnum.EXECUTED.getCode());
                execute.setStopNurseId(UserTemp.getInstance().getUserId());
                execute.setStopNurseName(UserTemp.getInstance().getUserName());
                execute.setStopDateTime(TimeUtils.getCurrentDate().getTime());
                execute.setVerifyLoginName(loginName);
                execute.setVerifyPassword(password);
                execute.setVerifyDateTime(TimeUtils.getCurrentDate().getTime());
            }
        }
        execute(executeList, false);
    }

    /**
     * 执行皮试医嘱 注意，只能执行一组的，不能执行多组的皮试医嘱
     *
     * @param skinTestOrders
     * @param skinResult
     */
    public void executeSkinTestOrder(List<Order> skinTestOrders, int skinResult) {
        if (skinTestOrders == null || skinTestOrders.isEmpty())
            return;
        List<OrdersExecute> executeList = new ArrayList<>();
        String performRecordGroupId = "";
        boolean multiGroupSkinTest = false;
        for (Order order : skinTestOrders) {
            if (TextUtils.isEmpty(performRecordGroupId))
                performRecordGroupId = order.getRecordGroupId();
            if (performRecordGroupId.equals(order.getRecordGroupId())) {
                OrdersExecute execute = OrdersHelp.buildGroupOrderExecute(order, skinResult, ExecuteResultEnum.NONEXECUTION.getCode());
                if (execute != null)
                    executeList.add(execute);
            } else if (!TextUtils.isEmpty(performRecordGroupId)) {
                multiGroupSkinTest = true;
            }
        }
        if (multiGroupSkinTest) {//多组皮试处置医嘱不可一起执行
            mRootView.executeFailed(
                    ArmsUtils.getString(mApplication, R.string.message_canNotExecuteMultiGroupSkinTest));
            return;
        }
        execute(executeList, true);
    }

    public void setCurrentOperation(List<OrdersExecute> executeList) {
        mCurrentOperationList.clear();
        mCurrentOperationList.addAll(executeList);
    }
}
