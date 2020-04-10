package com.herenit.mobilenurse.mvp.operation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.herenit.arms.base.BaseFragment;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.CommonNameCode;
import com.herenit.mobilenurse.criteria.common.NameCode;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.OperationScheduled;
import com.herenit.mobilenurse.criteria.entity.dict.EmergencyIndicatorDict;
import com.herenit.mobilenurse.criteria.entity.submit.OperationScheduledQuery;
import com.herenit.mobilenurse.custom.adapter.NameCodeAdapter;
import com.herenit.mobilenurse.custom.adapter.OperationScheduledAdapter;
import com.herenit.mobilenurse.custom.widget.dialog.ListDialog;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.di.component.DaggerOperationScheduledComponent;
import com.herenit.mobilenurse.mvp.base.BasicCommonFragment;
import com.herenit.mobilenurse.mvp.operation.operation_room.OperationRoomFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/2/18 13:58
 * desc: 手术安排Fragment
 */
public class OperationScheduledFragment extends OperationRoomFragment<OperationScheduledPresenter> implements OperationScheduledContract.View {

    //默认的时间范围（查询条件），手术室账号，默认查询当日手术，非手术室账号默认查询三日内手术
    public static final String DEFAULT_TIME_INTERVAL_CODE = UserTemp.getInstance().isOperation() ? CommonConstant.TIME_CODE_TODAY : CommonConstant.TIME_CODE_IN_THREE_DAYS;
    //默认的紧急状态查询条件，手术室账号，默认查询紧急状态手术，非手术室账号默认查询全部
    public static final String DEFAULT_EMERGENCY_INDICATOR = UserTemp.getInstance().isOperation() ? "1" : "";

    //刷新
    @BindView(R2.id.srl_operationScheduled_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R2.id.tv_operationScheduled_time)
    TextView mTv_time;//时间范围
    @Inject
    List<CommonNameCode> mTimeIntervalList;//时间筛选条件
    private NameCodeAdapter<CommonNameCode> mTimeIntervalAdapter;
    private ListDialog mTimeIntervalDialog;

    @BindView(R2.id.tv_operationScheduled_operationType)
    TextView mTv_operationType;//手术类型（急诊、非急诊、全部）
    @Inject
    List<EmergencyIndicatorDict> mEmergencyIndicatorList;//紧急状态选择Adapter
    private NameCodeAdapter<EmergencyIndicatorDict> mEmergencyIndicatorAdapter;
    private ListDialog mEmergencyIndicatorDialog;

    @BindView(R2.id.lv_operationScheduled)
    ListView mListView;//手术列表
    @Inject
    List<OperationScheduled> mOperationScheduledList;
    @Inject
    OperationScheduledAdapter mOperationScheduledAdapter;

    @Inject
    OperationScheduledQuery mQuery;//手术列表查询条件


    private LoadingDialog mLoadingDialog;

    //定时器，手术室账号登录，需要定时查询手术
    private Timer mTimer;
    /**
     * 手术室刷新时间间隔(单位为秒)
     */
    private static final int OPERATION_REFRESH_TIME = 60;


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerOperationScheduledComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .injec(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_operation_scheduled, container, false);
        return contentView;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();
        setDefaultUI();
        if (UserTemp.getInstance().isOperation()) {//手术室账号，开启刷新手术室的任务
            startRefreshOperationRoomTask();
        } else {
            mPresenter.loadOperationScheduledList(false);
        }
    }

    /**
     * 开启刷新手术室的任务
     */
    private void startRefreshOperationRoomTask() {
        if (mTimer == null)
            mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mPresenter.loadOperationScheduledList(false);
            }
        }, 0, OPERATION_REFRESH_TIME * 1000);

    }

    /**
     * 设置默认数据
     */
    private void setDefaultUI() {
        String defaultOperationType = mEmergencyIndicatorList.get(0).getEmergencyIndicatorName();
        if (mEmergencyIndicatorList != null && !TextUtils.isEmpty(DEFAULT_EMERGENCY_INDICATOR))
            for (EmergencyIndicatorDict emergency : mEmergencyIndicatorList) {
                if (DEFAULT_EMERGENCY_INDICATOR.equals(emergency.getEmergencyIndicatorCode())) {
                    defaultOperationType = emergency.getEmergencyIndicatorName();
                    break;
                }
            }
        mTv_operationType.setText(defaultOperationType);
        for (CommonNameCode nameCode : mTimeIntervalList) {
            if (TextUtils.isEmpty(DEFAULT_TIME_INTERVAL_CODE) && TextUtils.isEmpty(nameCode.getCode())) {
                mTv_time.setText(nameCode.getName());
                return;
            } else if (!TextUtils.isEmpty(DEFAULT_TIME_INTERVAL_CODE)) {
                if (DEFAULT_TIME_INTERVAL_CODE.equals(nameCode.getCode())) {
                    mTv_time.setText(nameCode.getName());
                }
            }
        }
    }

    private void initView() {
        //刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.loadOperationScheduledList(true);
                refreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
            }
        });
        //手术安排数据库列表
        mListView.setAdapter(mOperationScheduledAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//点击某条手术，进入详情界面
                OperationScheduled operationScheduled = mOperationScheduledList.get(position);
                Intent intent = new Intent(mContext, OperationScheduledInfoActivity.class);
                intent.putExtra(KeyConstant.NAME_EXTRA_OPERATION_SCHEDULED, operationScheduled);
                launchActivity(intent);
            }
        });
        //时间范围筛选
        mTimeIntervalAdapter = new NameCodeAdapter<>(mContext, mTimeIntervalList);
        mTimeIntervalDialog = createNoBottomListDialog(ArmsUtils.getString(mContext, R.string.common_operationTime), mTimeIntervalAdapter);
        mTimeIntervalDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//选择时间
                mTimeIntervalDialog.dismiss();
                NameCode timeInterval = mTimeIntervalList.get(position);
                mTv_time.setText(timeInterval.getName());
                mQuery.setStartDateTime(TimeUtils.getStartDateTimeByTimeCode(timeInterval.getCode()));
                mQuery.setStopDateTime(TimeUtils.getStopDateTimeByTimeCode(timeInterval.getCode()));
                mPresenter.loadOperationScheduledList(false);
            }
        });
        mTv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeIntervalDialog.show();
            }
        });
        //急诊标识筛选
        mEmergencyIndicatorAdapter = new NameCodeAdapter<>(mContext, mEmergencyIndicatorList);
        mEmergencyIndicatorDialog = createNoBottomListDialog(ArmsUtils.getString(mContext, R.string.common_emergencyStatus), mEmergencyIndicatorAdapter);
        mEmergencyIndicatorDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {//选择紧急状态
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mEmergencyIndicatorDialog.dismiss();
                EmergencyIndicatorDict emergencyIndicator = mEmergencyIndicatorList.get(position);
                mTv_operationType.setText(emergencyIndicator.getName());
                mQuery.setEmergencyIndicator(emergencyIndicator.getEmergencyIndicatorCode());
                mPresenter.loadOperationScheduledList(false);
            }
        });
        mTv_operationType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmergencyIndicatorDialog.show();
            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void onRefreshSuccess() {
        mRefreshLayout.finishRefresh();
    }

    /**
     * 紧急未确认手术提醒
     *
     * @param emergencyOperations
     */
    @Override
    public void remindEmergencyAndUnconfirmedOperation(List<OperationScheduled> emergencyOperations) {
        remindEmergencyOperation(emergencyOperations);
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
    public void showError(@NonNull String message) {
        showErrorToast(message);
    }
}
