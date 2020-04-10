package com.herenit.mobilenurse.mvp.orders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.app.utils.SoundPlayUtils;
import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.Newborn;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.submit.OrdersExecute;
import com.herenit.mobilenurse.criteria.entity.view.ImageText;
import com.herenit.mobilenurse.criteria.entity.view.RvController;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.enums.ExecuteClassEnum;
import com.herenit.mobilenurse.criteria.enums.ExecuteResultEnum;
import com.herenit.mobilenurse.criteria.enums.OrderClassEnum;
import com.herenit.mobilenurse.criteria.enums.SickbedFlagEnum;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.adapter.CommonImageTextAdapter;
import com.herenit.mobilenurse.custom.adapter.CommonTextImageAdapter;
import com.herenit.mobilenurse.custom.adapter.OrdersInfoAdapter;
import com.herenit.mobilenurse.criteria.entity.view.MultiItemDelegate;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.bll.PatientBasicInfoView;
import com.herenit.mobilenurse.custom.widget.dialog.AccountVerifyDialog;
import com.herenit.mobilenurse.custom.widget.dialog.DoubleInputDialog;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.custom.widget.dialog.NoticeDialog;
import com.herenit.mobilenurse.datastore.tempcache.DictTemp;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.di.component.DaggerOrdersInfoComponent;
import com.herenit.mobilenurse.mvp.base.BasicBusinessActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/3/12 14:46
 * desc: 医嘱详情View层
 */
public class OrdersInfoActivity extends BasicBusinessActivity<OrdersInfoPresenter> implements OrdersInfoContract.View {

    /**
     * 患者详情
     */
    @BindView(R2.id.pbiv_ordersInfo_patientBasicInfo)
    PatientBasicInfoView mPatientBasicInfoView;

    @BindView(R2.id.srl_ordersInfo_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    //列表，包括该组医嘱以及其他执行信息
    @BindView(R2.id.rv_ordersInfo)
    RecyclerView mRecyclerView;
    //单组医嘱数据
    @Inject
    List<Order> mGroupOrders;
    //医嘱的一些执行数据信息（控制单组医嘱下面的UI显示）
    @Inject
    List<RvController> mInfoControllers;
    @Inject
    OrdersInfoAdapter mAdapter;

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

    private LoadingDialog mLoadingDialog;

    /**
     * 核对弹窗
     */
    private AccountVerifyDialog mVerifyDialog;

    /**
     * 含有签名核对的执行列表
     */
    private List<OrdersExecute> mVerifyExecuteList = new ArrayList<>();
    /**
     * 需要签名核对的recordId列表
     */
    private List<String> mVerifyList = new ArrayList<>();

    private NoticeDialog mErrorDialog;


    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_TV;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerOrdersInfoComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_orders_info;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initOrdersInfoView();
        Intent intent = getIntent();
        mGroupOrders.clear();
        if (intent != null) {
            List<Order> orders = (List<Order>) intent.getSerializableExtra(KeyConstant.NAME_EXTRA_ORDER_LIST);
            updateOrdersInfo(orders);
        }
        mPresenter.loadData(false);
    }

    private void initOrdersInfoView() {
        setTitleBarCenterText(ArmsUtils.getString(mContext, R.string.title_module_ordersExecute));
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        setTitleBarLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.btn_execute));
        setTitleBarRightBackgroundResource1(R.drawable.shape_bg_text_transparent_solid_white_stroke);
        setTitleBarRightOnClickListener1(new View.OnClickListener() {//右上角被点击，弹出非华执行操作列表
            @Override
            public void onClick(View v) {
                mPresenter.manualOrderExecute();//手动执行
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                mPresenter.loadData(true);
            }
        });

        mRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 显示患者信息
     *
     * @param data
     */
    @Override
    public void updatePatientInfo(PatientInfo data) {
        mRefreshLayout.finishRefresh();
        mPatientBasicInfoView.setPatient(data, SickbedTemp.getInstance().getCurrentSickbed().getCurrentBaby());
    }

    /**
     * 跟新医嘱详情信息
     *
     * @param groupOrders
     */
    @Override
    public void updateOrdersInfo(List<Order> groupOrders) {
        mGroupOrders.clear();
        if (groupOrders == null) {
            groupOrders = new ArrayList<>();
        }
        upDateTitleView(groupOrders.get(0));
        List<MultiItemDelegate> data = mAdapter.getDatas();
        data.clear();
        mInfoControllers.clear();
        mGroupOrders.addAll(groupOrders);
        if (!mGroupOrders.isEmpty())
            mInfoControllers.addAll(OrdersHelp.createItemControllerByOrder(mGroupOrders.get(0)));
        data.addAll(mGroupOrders);
        data.addAll(mInfoControllers);
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 执行成功  包括医嘱执行、医嘱撤回，则需要返回到医嘱列表界面，并且刷新医嘱列表
     */
    @Override
    public void executeSuccess(List<OrdersExecute> executeList) {
        showSuccessVoice();
        //执行成功，要将签名缓存清楚，同时要将签名弹窗取消
        clearVerifyList();
        if (mVerifyDialog != null)
            mVerifyDialog.dismiss();
        //将执行列表回传给医嘱列表界面，用于执行成功之后锁定刚执行的医嘱
        Intent intent = new Intent();
        intent.putExtra(KeyConstant.NAME_EXTRA_EXECUTE_LIST, (Serializable) executeList);
        setResult(CommonConstant.RESULT_CODE_ORDER_INFO, intent);
        finish();
    }

    /**
     * 执行双签医嘱
     */
    @Override
    public void executeDoubleSignatureOrders(List<OrdersExecute> executeList, List<String> verifyList) {
        if (mGroupOrders == null || executeList == null || executeList.isEmpty())
            return;
        //双签名弹窗
        if (mVerifyDialog == null) {
            mVerifyDialog = createAccountVerifyDialog(ArmsUtils.getString(mContext, R.string.title_doubleSignatureOrder),
                    ArmsUtils.getString(mContext, R.string.message_verifyNotice));
            mVerifyDialog.setPositiveNegativeListener(new PositiveNegativeListener() {
                @Override
                public void onPositive(Object... backData) {
                    String loginName = (String) backData[0];
                    String password = (String) backData[1];
                    if (TextUtils.isEmpty(loginName)) {//账号为空
                        showErrorToast(ArmsUtils.getString(mContext, R.string.error_login_emptyUserId));
                        return;
                    } else if (TextUtils.isEmpty(password)) {//密码为空
                        showErrorToast(ArmsUtils.getString(mContext, R.string.error_login_emptyPassword));
                        return;
                    }
                    mVerifyDialog.dismiss();
                    mPresenter.executeDoubleSignatureOrders(mVerifyExecuteList, mVerifyList, loginName, StringUtils.toMD5(password));
                }

                @Override
                public void onNegative() {
                    mVerifyDialog.dismiss();
                }
            });
        }
        mVerifyDialog.show();
        //弹出签名核对弹窗时，要将核对执行的医嘱缓存，用于扫描工牌核对
        clearVerifyList();
        mVerifyExecuteList.addAll(executeList);
        mVerifyList.addAll(verifyList);
    }


    /**
     * 清除核对医嘱缓存，防止扫描工牌重复核对
     */
    @Override
    public void clearVerifyList() {
        mVerifyExecuteList.clear();
        mVerifyList.clear();
    }

    /**
     * 更新标题栏
     *
     * @param order
     */
    private void upDateTitleView(Order order) {
        String title = ArmsUtils.getString(mContext, R.string.title_module_ordersExecute);
        if (order.isST()) {//皮试医嘱
            title = ArmsUtils.getString(mContext, R.string.title_activity_STOrderExecute);
        } else if (order.isInfusion()) {//输液医嘱
            title = ArmsUtils.getString(mContext, R.string.title_activity_infusionsOrderExecute);
        } else if (order.getOrderClass().equals(OrderClassEnum.EXAM.getCode())) {//检查医嘱
            title = ArmsUtils.getString(mContext, R.string.title_activity_examOrderExecute);
        } else if (order.getOrderClass().equals(OrderClassEnum.LAB.getCode())) {//检验医嘱
            title = ArmsUtils.getString(mContext, R.string.title_activity_labOrderExecute);
        } else if (order.getOrderClass().equals(OrderClassEnum.OPERATION.getCode())) {//手术医嘱
            title = ArmsUtils.getString(mContext, R.string.title_activity_operationOrderExecute);
        }
        setTitleBarCenterText(title);
        if (ExecuteResultEnum.isExecuted(order.getExecuteResult())) {//已执行
            setTitleBarRightVisibility1(false);
        } else {//未执行
            if (order.isST() && TextUtils.isEmpty(order.getProcessingNurseId())) {//皮试的，当前不可录入结果的
                setTitleBarRightVisibility1(false);
            } else {
                setTitleBarRightVisibility1(true);
                setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.btn_execute));
            }
        }
    }

    /**
     * 执行失败
     *
     * @param errorMessage
     */
    @Override
    public void executeFailed(String errorMessage) {
        showErrorVoice();
        if (mErrorDialog == null) {
            mErrorDialog = createErrorNoNegativeNoticeDialog(ArmsUtils.getString(mContext, R.string.error_executeError), errorMessage);
            mErrorDialog.setPositiveNegativeListener(new PositiveNegativeListener() {
                @Override
                public void onPositive() {
                    mErrorDialog.dismiss();
                }
            });
        } else {
            mErrorDialog.setMessage(errorMessage);
        }
        mErrorDialog.show();
    }

    @Override
    public void showSuccessVoice() {
        SoundPlayUtils.play(1);
    }

    @Override
    public void showErrorVoice() {
        SoundPlayUtils.play(2);
    }


    @Override
    public void showMessage(@NonNull String message) {
        showNotAttachToast(message);
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog == null)
            mLoadingDialog = createLoadingDialog();
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
    }


    /**
     * EventBus接收消息
     *
     * @param event
     */
    @Subscribe
    public void onEventBusReceive(Event event) {
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (!TextUtils.isEmpty(intention)) {//私有消费

        } else {//公共事件消费
            if (id.equals(EventIntentionEnum.CODE_TYPE_ORDER.getId())) {//公共事件的执行医嘱，一般是由外界扫码调用
                //上次执行错误，还未点击确认，或者当前正在进行签名核对，则不允许执行
                if ((mErrorDialog != null && mErrorDialog.isShowing()) || (mVerifyDialog != null && mVerifyDialog.isShowing())) {
                    showErrorVoice();
                    return;
                }
                ScanResult scanResult = (ScanResult) event.getMessage();
                mPresenter.scanOrderExecute(scanResult);
            } else if (id.equals(EventIntentionEnum.CODE_TYPE_PATIENT.getId())) {//患者二维码
                //上次执行错误，还未点击确认，或者当前正在进行签名核对，则不允许执行
                if ((mErrorDialog != null && mErrorDialog.isShowing()) || (mVerifyDialog != null && mVerifyDialog.isShowing())) {
                    showErrorVoice();
                    return;
                }
                ScanResult scanResult = (ScanResult) event.getMessage();
                String patientId = scanResult.getPatientId();
                Sickbed sickbed = SickbedTemp.getInstance().getCurrentSickbed();
                if (TextUtils.isEmpty(patientId)) {
                    executeFailed(ArmsUtils.getString(mContext, R.string.message_notCurrentPatient));
                    return;
                }
                if (!patientId.equals(sickbed.getPatientId())) {
                    //查询一下新生儿中有无此腕带
                    Sickbed mother = SickbedTemp.getInstance().getMother(patientId);
                    if (mother != null && sickbed.isCurrentBaby(mother.getCurrentBaby()) && mGroupOrders.get(0).needExecuteSkinTestResult()) {
                        //正是当前患者选择的当前新生儿,当前医嘱需要录入皮试结果，则扫描腕带可以执行
                        mPresenter.manualOrderExecute();
                    } else {
                        executeFailed(ArmsUtils.getString(mContext, R.string.message_notCurrentPatient));
                    }
                } else {
                    if (mGroupOrders.get(0).needExecuteSkinTestResult()) {//当前医嘱需要录入皮试结果，则扫描腕带可以执行
                        mPresenter.manualOrderExecute();
                    } else {
                        executeFailed(ArmsUtils.getString(mContext, R.string.message_canNotDoThisOperation));
                    }
                }
            } else if (id.equals(EventIntentionEnum.CODE_TYPE_EMP_CARD.getId())) {//扫描员工牌
                ScanResult scanResult = (ScanResult) event.getMessage();
                if (mVerifyDialog != null && mVerifyDialog.isShowing() && !mVerifyExecuteList.isEmpty() && !mVerifyList.isEmpty()) {//当前正处于签名状态
                    String loginName = scanResult.getLoginName();
                    if (!TextUtils.isEmpty(loginName)) {
                        //将签名信息写入弹窗，同时执行签名方法
                        mVerifyDialog.setVerifyText(loginName, "");
                        mPresenter.executeDoubleSignatureOrders(mVerifyExecuteList, mVerifyList, loginName, "");
                        return;
                    }
                }
                showError(ArmsUtils.getString(mContext, R.string.message_canNotDoThisOperation));
                return;
            } else {
                showErrorToast(ArmsUtils.getString(mContext, R.string.message_unknown_scanCode));
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPatientBasicInfoView != null)
            mPatientBasicInfoView.release();
    }
}
