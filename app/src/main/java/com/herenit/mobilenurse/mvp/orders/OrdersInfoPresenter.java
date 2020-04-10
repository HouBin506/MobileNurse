package com.herenit.mobilenurse.mvp.orders;

import android.app.Application;
import android.text.TextUtils;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.NetworkUtils;
import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.submit.OrdersExecute;
import com.herenit.mobilenurse.criteria.entity.view.ImageText;
import com.herenit.mobilenurse.criteria.entity.view.RvController;
import com.herenit.mobilenurse.criteria.enums.ExecuteClassEnum;
import com.herenit.mobilenurse.criteria.enums.ExecuteResultEnum;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.custom.adapter.CommonImageTextAdapter;

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
public class OrdersInfoPresenter extends BasePresenter<OrdersInfoContract.Model, OrdersInfoContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;

    //单组医嘱数据
    @Inject
    List<Order> mGroupOrders;
    //医嘱的一些执行数据信息（控制单组医嘱下面的UI显示）
    @Inject
    List<RvController> mInfoControllers;

    /**
     * 选择执行操作类型
     */
    @Inject
    List<ImageText> mExecuteClassList;
    /**
     * 选择执行操作类型
     */
    @Inject
    CommonImageTextAdapter mExecuteClassAdapter;

    @Inject
    OrdersInfoPresenter(OrdersInfoContract.Model model, OrdersInfoContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 加载数据
     *
     * @param refresh 是否刷新
     */
    public void loadData(boolean refresh) {
        loadPatientInfo(refresh);
    }

    /**
     * 加载患者详情数据
     *
     * @param refresh
     */
    private void loadPatientInfo(boolean refresh) {
        if (NetworkUtils.isNetworkConnected(mApplication)) {//有网
            loadPatientInfoByNetwork(refresh);
        } else {//没网
            loadPatientInfoByCache();
        }
    }

    /**
     * 内存获取患者详情
     */
    private void loadPatientInfoByCache() {
        Sickbed sickbed = SickbedTemp.getInstance().getCurrentSickbed();
        if (sickbed == null)
            return;
        mModel.getPatientInfoByCache(sickbed.getPatientId(), String.valueOf(sickbed.getVisitId()))
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<PatientInfo>(mErrorHandler) {
                    @Override
                    public void onNext(PatientInfo result) {
                        if (result != null) {
                            mRootView.updatePatientInfo(result);
                        }
                    }
                });
    }

    /**
     * 网络加载患者详情
     *
     * @param refresh
     */
    private void loadPatientInfoByNetwork(boolean refresh) {
        Sickbed sickbed = SickbedTemp.getInstance().getCurrentSickbed();
        if (sickbed == null)
            return;
        mModel.getPatientInfoByNetwork(sickbed.getPatientId(), UserTemp.getInstance().getGroupCode())
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
                .subscribe(new ErrorHandleSubscriber<Result<PatientInfo>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<PatientInfo> result) {
                        if (result.isSuccessful()) {
                            mRootView.updatePatientInfo(result.getData());
                        }
                    }
                });
    }


    /**
     * 执行医嘱
     *
     * @param executeList         执行的列表
     * @param isNextExecuteVerify 本次执行完成之后，如果下次执行需要签名核对，则不算完，要直接进入签名装填，让用户完成签名操作
     */
    public void execute(List<OrdersExecute> executeList, boolean isNextExecuteVerify) {
        if (executeList == null || executeList.isEmpty())
            return;
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
                        if (result.isSuccessful()) {
                            mRootView.showMessage(ArmsUtils.getString(mApplication, R.string.message_executeSuccess));
                            if (isNextExecuteVerify) {//如果是皮试医嘱执行成功，还需要继续执行双签，才算是真正完成
                                List<String> verifyList = new ArrayList<>();
                                for (OrdersExecute execute : executeList) {
                                    if (!verifyList.contains(execute.getRecordId()))
                                        verifyList.add(execute.getRecordId());
                                    mRootView.executeDoubleSignatureOrders(executeList, verifyList);
                                }
                            } else {
                                mRootView.executeSuccess(executeList);
                            }
                        } else {
                            mRootView.executeFailed(result.getMessage());
                        }
                    }
                });
    }


    /**
     * 手动医嘱执行
     */
    public void manualOrderExecute() {
        List<OrdersExecute> executeList = buildExecuteList(mGroupOrders.get(0).getRecordGroupId(), mGroupOrders);
        if (mGroupOrders.get(0).needExecuteSkinTestResult()) {//执行的是皮试医嘱
            execute(executeList, true);
        } else if (mGroupOrders.get(0).needVerify()) {//需要双签
            List<String> verifyList = new ArrayList<>();
            for (Order order : mGroupOrders) {
                verifyList.add(order.getRecordId());
            }
            mRootView.executeDoubleSignatureOrders(executeList, verifyList);
        } else if (mGroupOrders.get(0).nextNeedVerify()) {//本次执行完成之后的下一次执行，需要签名核对
            execute(executeList, true);
        } else {
            execute(executeList, false);
        }
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
                            verifyAndExecute(result.getData());
                        } else {
                            mRootView.executeFailed(result.getMessage());
                        }
                    }
                });
    }

    /**
     * 核对并且执行扫描到的医嘱
     *
     * @param scanOrderList
     */
    private void verifyAndExecute(List<Order> scanOrderList) {
        if (scanOrderList == null || scanOrderList.isEmpty()) {
            mRootView.executeFailed(ArmsUtils.getString(mApplication, R.string.message_notCurrentPatientOrdersOrCanNotExecuteOrUnrelatedWithPageOrder));
            return;
        }
        String recordId = mGroupOrders.get(0).getRecordId();
        int containPosition = -1;//当前界面显示的医嘱，在扫描得到的列表中的位置
        List<String> verifyList = new ArrayList<>();//需要核对（双签名) 。将recordGroupId存入
        List<String> nextVerifyList = new ArrayList<>();//下次执行需要双签的。将recordId存入
        for (int x = 0; x < scanOrderList.size(); x++) {//从扫描到的医嘱与当前界面的医嘱比对，如果有，则执行扫描到的医嘱
            Order order = scanOrderList.get(x);
            if (order.getRecordId().equals(recordId) && containPosition < 0)
                containPosition = x;
            if (order.needVerify()) {//未核对的
                if (!verifyList.contains(order.getRecordId()))
                    verifyList.add(order.getRecordId());
            }
            if (order.nextNeedVerify()) {//本次执行成功之后的下一次执行，需要双签名
                if (!nextVerifyList.contains(order.getRecordId()))
                    nextVerifyList.add(order.getRecordId());
            }

        }
        if (containPosition < 0) {//当前医嘱详情界面的医嘱，与扫描得到的医嘱不符
            mRootView.executeFailed(ArmsUtils.getString(mApplication, R.string.message_notCurrentPatientOrdersOrCanNotExecuteOrUnrelatedWithPageOrder));
            return;
        }
        List<OrdersExecute> executeList = buildExecuteList(mGroupOrders.get(0).getRecordGroupId(), scanOrderList);
        if (verifyList != null && !verifyList.isEmpty()) {//需要双签核对的
            mRootView.executeDoubleSignatureOrders(executeList, verifyList);
        } else {//直接执行
            //要执行的医嘱，全部都是双签名的医嘱，并且本次执行完成之后，下一次就需要双签名
            if (!nextVerifyList.isEmpty() && nextVerifyList.size() == scanOrderList.size()) {
                execute(executeList, true);
            } else {//普通不需要双签的医嘱执行
                execute(executeList, false);
            }
        }
    }

    /**
     * 构建执行列表
     *
     * @param editedRecordGroupId
     * @param executeOrderList
     * @return
     */
    public List<OrdersExecute> buildExecuteList(String editedRecordGroupId, List<Order> executeOrderList) {
        List<OrdersExecute> executeList = new ArrayList<>();
        for (Order order : executeOrderList) {
            if (ExecuteResultEnum.isExecuted(order.getExecuteResult())) {//医嘱已被执行
                mRootView.executeFailed(
                        ArmsUtils.getString(mApplication, R.string.message_executedOrder) + "：" + order.getOrderText());
                return null;
            }
            OrdersExecute execute;
            String recordGroupId = order.getRecordGroupId();
            if (recordGroupId.equals(editedRecordGroupId)) {
                boolean isSkinTest = order.isST() && !TextUtils.isEmpty(order.getProcessingNurseId()) && TextUtils.isEmpty(order.getPerformResult());
                execute = OrdersHelp.buildGroupOrderExecuteByController(order, mInfoControllers, isSkinTest);
            } else {
                Integer executeResult;
                if (order.getIsDoubleSignature() && TextUtils.isEmpty(order.getProcessingNurseId()))
                    executeResult = ExecuteResultEnum.NONEXECUTION.getCode();//需要双签的医嘱，并且一次都没执行的，则状态为未执行
                else
                    executeResult = ExecuteResultEnum.EXECUTED.getCode();
                execute = OrdersHelp.buildGroupOrderExecute(order, null, executeResult);
            }
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
                execute.setVerifyLoginName(loginName);
                execute.setVerifyPassword(password);
                execute.setVerifyDateTime(TimeUtils.getCurrentDate().getTime());
                //执行双签，要把执行状态设为已执行
                execute.setExecuteResult(ExecuteResultEnum.EXECUTED.getCode());
                execute.setStopNurseId(UserTemp.getInstance().getUserId());
                execute.setStopNurseName(UserTemp.getInstance().getUserName());
                if (execute.getStopDateTime() == null)
                    execute.setStopDateTime(TimeUtils.getCurrentDate().getTime());
            }
        }
        execute(executeList, false);
    }
}
