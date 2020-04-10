package com.herenit.mobilenurse.mvp.monitor;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.app.utils.SoundPlayUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.MonitorBind;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.BindStatusEnum;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.enums.MonitorOperationTypeEnum;
import com.herenit.mobilenurse.criteria.enums.NoticeLevelEnum;
import com.herenit.mobilenurse.criteria.enums.SickbedFlagEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.bll.PatientBasicInfoView;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.custom.widget.dialog.NoticeDialog;
import com.herenit.mobilenurse.custom.widget.dialog.SingleInputDialog;
import com.herenit.mobilenurse.custom.widget.layout.MNSingleInputDialog;
import com.herenit.mobilenurse.datastore.tempcache.DictTemp;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.di.component.DaggerMonitorComponent;
import com.herenit.mobilenurse.mvp.base.BasicCommonFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rx_cache2.Migration;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.herenit.mobilenurse.criteria.enums.MonitorOperationTypeEnum.*;

/**
 * author: HouBin
 * date: 2019/3/4 15:06
 * desc: 监护仪绑定页面
 */
public class MonitorPagerFragment extends BasicCommonFragment<MonitorPresenter> implements MonitorContract.View {

    @BindView(R2.id.srl_monitorPager_refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    /**
     * 患者基本信息
     */
    @BindView(R2.id.pbiv_monitorPager_patientBasicInfo)
    PatientBasicInfoView mPatientBasicInfoView;

    /**
     * 监护仪条码
     */
    @BindView(R2.id.tv_monitorPager_monitorCode)
    TextView mTv_monitorCode;
    /**
     * 绑定时间
     */
    @BindView(R2.id.tv_monitorPager_bindingDateTime)
    TextView mTv_bindingDateTime;
    /**
     * 绑定人
     */
    @BindView(R2.id.tv_monitorPager_bindingNurse)
    TextView mTv_bindingNurse;
    /**
     * 解绑时间
     */
    @BindView(R2.id.tv_monitorPager_unbindDateTime)
    TextView mTv_unbindDateTime;
    /**
     * 解绑人
     */
    @BindView(R2.id.tv_monitorPager_unbindNurse)
    TextView mTv_unbindNurse;
    /**
     * 当前状态
     */
    @BindView(R2.id.tv_monitorPager_currentStatus)
    TextView mTv_currentStatus;
    /**
     * 备注
     */
    @BindView(R2.id.tv_monitorPager_memo)
    TextView mTv_memo;

    /**
     * 数据加载弹窗
     */
    private LoadingDialog mLoadingDialog;
    /**
     * 输入监护仪码框弹窗
     */
    private SingleInputDialog mInputMonitorCodeDialog;
    /**
     * 输入备注框弹窗
     */
    private SingleInputDialog mInputMemoDialog;

    /**
     * 提示弹窗，在绑定的时候弹出提示信息
     */
    private NoticeDialog mNoticeDialog;
    /**
     * 错误弹窗
     */
    private NoticeDialog mErrorDialog;


    /**
     * 当前患者监护仪绑定信息
     */
    private MonitorBind mMonitor;


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMonitorComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_monitor_pager, container, false);
        return contentView;
    }


    public void finishRefresh() {
        if (mRefreshLayout != null)
            mRefreshLayout.finishRefresh();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        resetMonitorBindInfo();
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                mPresenter.loadData(SickbedTemp.getInstance().getCurrentSickbed(), true);
            }
        });
        mTv_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMemo();
            }
        });
        mPresenter.loadData(SickbedTemp.getInstance().getCurrentSickbed(), false);
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    /**
     * EventBus事件接收
     *
     * @param event
     */
    @Subscribe
    public void onEventBusReceive(Event event) {
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (!TextUtils.isEmpty(intention)) {//私有事件消费
            if (intention.equals(CommonConstant.EVENT_INTENTION_ON_TITLEBAR_RIGHT_CLICK)) {//右上角按钮被点击（绑定或者解绑）
                //已绑定，则点击就是要解绑此时不需要手动输入监护仪码
                if (mMonitor != null && BindStatusEnum.BINDING.getBindStatusCode() == mMonitor.getBindStatus()) {
                    mPresenter.monitorBindVerify(buildMonitorBind(mMonitor.getMonitorCode()));
                } else {//非绑定状态则点击右上角，就是要绑定，此时需要手动输入要绑定的监护仪码
                    inputMonitorCodeBind();
                }
            }
        } else {
            if (id.equals(EventIntentionEnum.CODE_TYPE_MONITOR.getId())) {//扫描到监护仪条码
                if (isWaitForConfirm()) {//当前界面还有某项操作正在等待确认，则不可做其它操作
                    showErrorVoice();
                    return;
                }
                ScanResult scanResult = (ScanResult) event.getMessage();
                mPresenter.monitorBindVerify(buildMonitorBind(scanResult.getMonitorCode()));
            } else if (id.equals(EventIntentionEnum.CODE_TYPE_ORDER.getId()) || id.equals(EventIntentionEnum.CODE_TYPE_EMP_CARD.getId())) {
                showErrorVoice();
                return;
            }
        }
    }


    /**
     * 输入监护仪码绑定
     */
    private void inputMonitorCodeBind() {
        if (mInputMonitorCodeDialog == null) {
            mInputMonitorCodeDialog = createSingInputDialog(ArmsUtils.getString(mContext, R.string.common_monitorBinding),
                    ArmsUtils.getString(mContext, R.string.message_inputMonitorCode), "");
            mInputMonitorCodeDialog.setPositiveNegativeListener(new PositiveNegativeListener() {
                @Override
                public void onPositive(Object... backData) {
                    String code = (String) backData[0];
                    if (TextUtils.isEmpty(code)) {
                        showErrorToast(ArmsUtils.getString(mContext, R.string.error_login_emptyMonitorCode));
                        return;
                    }
                    mInputMonitorCodeDialog.dismiss();
                    mPresenter.monitorBindVerify(buildMonitorBind(code));
                }

                @Override
                public void onNegative() {
                    mInputMonitorCodeDialog.dismiss();
                }
            });
        }
        mInputMonitorCodeDialog.show();
    }

    /**
     * 输入监护仪码绑定
     */
    private void inputMemo() {
        if (mInputMemoDialog == null) {
            mInputMemoDialog = createSingInputDialog(ArmsUtils.getString(mContext, R.string.common_memo),
                    ArmsUtils.getString(mContext, R.string.message_inputMemo), mTv_memo.getText().toString());
            mInputMemoDialog.setPositiveNegativeListener(new PositiveNegativeListener() {
                @Override
                public void onPositive(Object... backData) {
                    String memo = (String) backData[0];
                    mInputMemoDialog.dismiss();
                    if (TextUtils.isEmpty(memo))
                        mTv_memo.setText("");
                    else
                        mTv_memo.setText(memo);
                }

                @Override
                public void onNegative() {
                    mInputMemoDialog.dismiss();
                }
            });
        }
        mInputMemoDialog.show();
    }

    /**
     * 构建监护仪绑定对象
     *
     * @param monitorCode
     * @return
     */
    private MonitorBind buildMonitorBind(String monitorCode) {
        MonitorBind monitor = new MonitorBind();
        monitor.setOperatorId(UserTemp.getInstance().getUserId());
        monitor.setOperatorName(UserTemp.getInstance().getUserName());
        Sickbed sickbed = SickbedTemp.getInstance().getCurrentSickbed();
        if (sickbed != null) {
            monitor.setPatientId(sickbed.getPatientId());
            monitor.setVisitNo(sickbed.getVisitNo());
        }
        monitor.setOperatingDateTime(TimeUtils.getCurrentDate().getTime());
        monitor.setMonitorCode(monitorCode);
        monitor.setMemo(mTv_memo.getText().toString());
        return monitor;
    }


    /**
     * 重置绑定信息
     */
    private void resetMonitorBindInfo() {
        mMonitor = null;
        mTv_monitorCode.setText("");
        mTv_bindingDateTime.setText("");
        mTv_bindingNurse.setText("");
        mTv_unbindDateTime.setText("");
        mTv_unbindNurse.setText("");
        mTv_memo.setText("");
        mTv_currentStatus.setText(BindStatusEnum.NOT_BOUND.getBindStatusName());
        //设置按钮为绑定
        ((MonitorFragment) getParentFragment()).setTitleBarRightText(ArmsUtils.getString(mContext, R.string.btn_binding));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPatientBasicInfoView != null)
            mPatientBasicInfoView.release();
    }

    @Override
    public void showPatientInfo(PatientInfo patient) {
        if (mPatientBasicInfoView != null)
            mPatientBasicInfoView.setPatient(patient,null);
    }

    @Override
    public void showMonitorInfo(MonitorBind monitor) {
        try {
            resetMonitorBindInfo();
            mMonitor = monitor;
            if (monitor == null)
                return;
            Integer status = monitor.getBindStatus();
            if (status == null) {
                mTv_currentStatus.setText("");
            } else if (status == BindStatusEnum.BINDING.getBindStatusCode()) {//已绑定
                //设置按钮为解绑
                ((MonitorFragment) getParentFragment()).setTitleBarRightText(ArmsUtils.getString(mContext, R.string.btn_unbind));
                mTv_currentStatus.setText(BindStatusEnum.BINDING.getBindStatusName());
                if (monitor.getOperatingDateTime() != null)
                    mTv_bindingDateTime.setText(TimeUtils.getYYYYMMDDHHMMString(monitor.getOperatingDateTime()));
                if (!TextUtils.isEmpty(monitor.getOperatorName()))
                    mTv_bindingNurse.setText(monitor.getOperatorName());
            } else if (status == BindStatusEnum.UNBIND.getBindStatusCode()) {//已解绑
                //设置按钮为解绑
                ((MonitorFragment) getParentFragment()).setTitleBarRightText(ArmsUtils.getString(mContext, R.string.btn_binding));
                mTv_currentStatus.setText(BindStatusEnum.UNBIND.getBindStatusName());
                if (monitor.getOperatingDateTime() != null)
                    mTv_unbindDateTime.setText(TimeUtils.getYYYYMMDDHHMMString(monitor.getOperatingDateTime()));
                if (!TextUtils.isEmpty(monitor.getOperatorName()))
                    mTv_unbindNurse.setText(monitor.getOperatorName());
            }
            if (!TextUtils.isEmpty(monitor.getMonitorCode()))
                mTv_monitorCode.setText(monitor.getMonitorCode());
            if (!TextUtils.isEmpty(monitor.getMemo()))
                mTv_memo.setText(monitor.getMemo());
        } catch (Exception e) {
            showErrorToast(e.getMessage());
        }

    }

    /**
     * 数据加载完成
     */
    @Override
    public void loadFinish() {
        mRefreshLayout.finishRefresh();
    }

    /**
     * 显示监护仪绑定核对提示窗
     *
     * @param monitor
     */
    @Override
    public void showBindVerifyNotice(final MonitorBind monitor) {
        if (monitor == null || monitor.getOperationType() == null) {
            showErrorVoice();
            showErrorToast(ArmsUtils.getString(mContext, R.string.error_bindingUnbindError));
            return;
        }
        int operationType = monitor.getOperationType();
        //简单的绑定或者解绑给出普通提示
        switch (matchMonitorOperationType(operationType)) {
            case BINDING:
                mNoticeDialog = createNoticeDialog(ArmsUtils.getString(mContext, R.string.common_monitorBinding), monitor.getVerifyMessage());
                break;
            case UNBIND:
                mNoticeDialog = createNoticeDialog(ArmsUtils.getString(mContext, R.string.common_monitorUnbind), monitor.getVerifyMessage());
                break;
            default:
                mNoticeDialog = createWarningNoticeDialog(ArmsUtils.getString(mContext, R.string.common_monitorBindingUnbind), monitor.getVerifyMessage());
                break;
        }
        mNoticeDialog.setPositiveNegativeListener(new PositiveNegativeListener() {
            @Override
            public void onPositive() {
                mNoticeDialog.dismiss();
                mPresenter.monitorBind(monitor, SickbedTemp.getInstance().getCurrentSickbed());
            }

            @Override
            public void onNegative() {
                mNoticeDialog.dismiss();
            }
        });
        mNoticeDialog.show();
    }

    /**
     * 绑定或者解绑成功
     */
    @Override
    public void bindSuccess() {
        showSuccessVoice();
        showNotAttachToast(ArmsUtils.getString(mContext, R.string.message_monitorBindingOrUnbindSuccess));
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

    @Override
    public void showError(@NonNull String title, @Nullable String message) {
        showErrorVoice();
        if (mErrorDialog == null) {
            mErrorDialog = createErrorNoNegativeNoticeDialog(title, message);
            mErrorDialog.setPositiveNegativeListener(new PositiveNegativeListener() {
                @Override
                public void onPositive() {
                    mErrorDialog.dismiss();
                }
            });
        }
        mErrorDialog.show();
    }

    @Override
    public void showError(@NonNull String message) {
        showErrorToast(message);
    }

    @Override
    public void showErrorVoice() {
        SoundPlayUtils.play(2);
    }

    @Override
    public void showSuccessVoice() {
        SoundPlayUtils.play(3);
    }

    /**
     * 当前页面是否处于等待确认的状态，比如正在有编辑弹窗弹出，正在有错误弹窗等待点击确认
     *
     * @return
     */
    private boolean isWaitForConfirm() {
        return (mInputMonitorCodeDialog != null && mInputMonitorCodeDialog.isShowing()) || (mInputMemoDialog != null && mInputMemoDialog.isShowing()) ||
                (mNoticeDialog != null && mNoticeDialog.isShowing()) || (mErrorDialog != null && mErrorDialog.isShowing()) || (mLoadingDialog != null && mLoadingDialog.isShowing());
    }
}
