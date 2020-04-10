package com.herenit.mobilenurse.mvp.nursing_round;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.herenit.arms.integration.AppManager;
import com.herenit.arms.mvp.IView;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.ValueConstant;
import com.herenit.mobilenurse.criteria.entity.NurseRoundItem;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewGroup;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewItem;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.adapter.NurseRoundItemAdapter;
import com.herenit.mobilenurse.custom.adapter.OrdersAdapter;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.dialog.NoticeDialog;
import com.herenit.mobilenurse.custom.widget.layout.MNScrollView;
import com.herenit.mobilenurse.custom.widget.layout.SpacesItemDecoration;
import com.herenit.mobilenurse.mvp.base.BasicCommonFragment;
import com.herenit.mobilenurse.mvp.orders.OrdersHelp;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/8/5 10:02
 * desc:护理巡视页面
 */
public class NurseRoundPagerFragment extends BasicCommonFragment implements IView {

    @BindView(R2.id.srl_nurseRound_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R2.id.tv_nurseRoundPager_recordDateTime)
    TextView mTv_RecordDateTime;//记录时间
    @BindView(R2.id.tv_nurseRoundPager_history)
    TextView mTv_roundHistory;//历史记录

    //普通巡视
    @BindView(R2.id.rv_nurseRoundPager_commonRound)
    RecyclerView mRv_commonRound;
    List<NurseRoundViewItem> mCommonRoundList = new ArrayList<>();//普通巡视
    NurseRoundItemAdapter mCommonRoundAdapter;

    /**
     * 输液巡视界面
     */
    @BindView(R2.id.mnSv_nurseRoundPager_infusionRound)
    MNScrollView mMnSv_infusionRound;
    //输液巡视
    @BindView(R2.id.rv_nurseRoundPager_infusionRound)
    RecyclerView mRv_infusionRound;
    List<NurseRoundViewItem> mInfusionRoundList = new ArrayList<>();//输液巡视
    NurseRoundItemAdapter mInfusionRoundAdapter;

    /**
     * 正在执行的输液医嘱
     */
    @BindView(R2.id.rv_nurseRoundPager_orderList)
    RecyclerView mRv_orderList;
    List<Order> mOrderList = new ArrayList<>();
    OrdersAdapter mOrdersAdapter;

    @BindView(R2.id.rg_nurseRoundPager_roundType)
    RadioGroup mRg_roundType;

    public final String ROUND_TYPE_COMMON = "common_round";//普通巡视
    public final String ROUND_TYPE_INFUSION = "common_infusion";//输液巡视

    private String mCurrentRoundType;

    private Date mRecordDateTime = TimeUtils.getCurrentDate();

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_nurse_round_pager, container, false);
        return contentView;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();
        loadData(false);
    }

    private void initView() {
        //普通巡视
        mCommonRoundAdapter = new NurseRoundItemAdapter(mContext, mCommonRoundList);
        LinearLayoutManager commonLayoutManager = new LinearLayoutManager(mContext);
        mRv_commonRound.setLayoutManager(commonLayoutManager);
        mRv_commonRound.setAdapter(mCommonRoundAdapter);
        //输液巡视
        mInfusionRoundAdapter = new NurseRoundItemAdapter(mContext, mInfusionRoundList);
        LinearLayoutManager infusionLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//canScrollVertically方法返回false，可解决ScrollView嵌套RecyclerView造成的滑动卡顿问题
                return false;
            }
        };
        mRv_infusionRound.setLayoutManager(infusionLayoutManager);
        mRv_infusionRound.setAdapter(mInfusionRoundAdapter);
        //输液医嘱列表
        mOrdersAdapter = new OrdersAdapter(mContext, mOrderList);
        LinearLayoutManager orderLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//canScrollVertically方法返回false，可解决ScrollView嵌套RecyclerView造成的滑动卡顿问题
                return false;
            }
        };
        mRv_orderList.setLayoutManager(orderLayoutManager);
        mRv_orderList.setAdapter(mOrdersAdapter);

        //设置刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND);
                //刷新时修改页面时间
                mRecordDateTime = TimeUtils.getCurrentDate();
                mTv_RecordDateTime.setText(TimeUtils.getYYYYMMDDHHMMString(mRecordDateTime));
                loadData(true);
            }
        });
        //巡视类型切换（输液巡视、普通巡视）
        mRg_roundType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_nurseRoundPager_commonRound) {//普通巡视
                    mCurrentRoundType = ROUND_TYPE_COMMON;
                    mRv_commonRound.setVisibility(View.VISIBLE);
                    mMnSv_infusionRound.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb_nurseRoundPager_infusionRound) {//输液巡视
                    mCurrentRoundType = ROUND_TYPE_INFUSION;
                    mRv_commonRound.setVisibility(View.GONE);
                    mMnSv_infusionRound.setVisibility(View.VISIBLE);
                }
            }
        });
        //默认显示输液巡视模式
        mRg_roundType.check(R.id.rb_nurseRoundPager_infusionRound);
        //时间选择
        mTv_RecordDateTime.setOnClickListener(new View.OnClickListener() {//时间选择
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mRecordDateTime);
                new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        mRecordDateTime = date;
                        mTv_RecordDateTime.setText(TimeUtils.getYYYYMMDDHHMMString(mRecordDateTime));
                    }
                }).setType(new boolean[]{true, true, true, true, true, false})
                        .setDate(calendar)
                        .isDialog(false)
                        .build().show();
            }
        });
        mTv_RecordDateTime.setText(TimeUtils.getYYYYMMDDHHMMString(mRecordDateTime));
        //历史记录
        mTv_roundHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(new Intent(mContext, NurseRoundHistoryActivity.class));
            }
        });
    }

    private void loadData(boolean refresh) {
        String id = EventBusUtils.obtainPrivateId(getParentFragment().toString(), CommonConstant.EVENT_INTENTION_LOAD_DATA);
        EventBusUtils.post(id, refresh);
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    /**
     * 刷新巡视列表
     *
     * @param nurseRoundView
     */
    private void updateNurseRoundList(NurseRoundViewGroup nurseRoundView) {
        if (mRefreshLayout != null)
            mRefreshLayout.finishRefresh();
        mCommonRoundList.clear();
        mInfusionRoundList.clear();
        mOrderList.clear();
        if (nurseRoundView != null) {
            List<NurseRoundViewItem> commonRoundList = nurseRoundView.getCommonRoundList();
            if (commonRoundList != null)
                mCommonRoundList.addAll(commonRoundList);
            List<NurseRoundViewItem> infusionRoundList = nurseRoundView.getInfusionRoundList();
            if (infusionRoundList != null)
                mInfusionRoundList.addAll(infusionRoundList);
            List<Order> orderList = nurseRoundView.getOrderList();
            if (orderList != null)
                mOrderList.addAll(orderList);
        }
        mCommonRoundAdapter.notifyDataSetChanged();
        mInfusionRoundAdapter.notifyDataSetChanged();
        mOrdersAdapter.notifyDataSetChanged();
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
            if (intention.equals(CommonConstant.EVENT_INTENTION_UPDATE_NURSE_ROUND)) {//刷新巡视列表
                updateNurseRoundList((NurseRoundViewGroup) event.getMessage());
            } else if (intention.equals(CommonConstant.EVENT_INTENTION_INFUSION_ROUND)) {//输液巡视
                //此消息一般是由扫描药品瓶贴发送而接收到的
                //如果当前页面处于输液巡视模式，则根据医嘱条码与当前页面医嘱列表做对比，做出错误提示或者直接提交输液巡视内容。
                //如果当前页面处于普通巡视模式，则要自动切换到输液巡视模式，并判断扫描的医嘱是否与页面医嘱列表相匹配；再次扫描则会提交
                ScanResult result = (ScanResult) event.getMessage();
                executeInfusionRound(result);
            } else if (intention.equals(CommonConstant.EVENT_INTENTION_ON_TITLEBAR_RIGHT_CLICK)) {//保存按钮被点击，提交所有巡视数据
                List<NurseRoundItem> commonRoundData = NurseRoundHelper.buildNurseRoundDataList(mCommonRoundList, mRecordDateTime, null);
                List<NurseRoundItem> infusionRoundData = NurseRoundHelper.buildNurseRoundDataList(mInfusionRoundList, mRecordDateTime, mOrderList);
                List<NurseRoundItem> commitData = new ArrayList<>();
                if (commonRoundData != null && !commonRoundData.isEmpty())
                    commitData.addAll(commonRoundData);
                if (infusionRoundData != null && !infusionRoundData.isEmpty())
                    commitData.addAll(infusionRoundData);
                if (commitData != null && !commitData.isEmpty())
                    commitNurseRound(commitData);
            }
        } else {//公共事件

        }
    }

    /**
     * 执行输液巡视，此方法一般是在扫描输液瓶贴时调用的
     * 如果当前界面处于普通巡视模式，则要切换到输液巡视模式，并根据参数recordGroupId查询当前正在执行的输液医嘱，
     * 如果找到该条医嘱：当前界面如果处于输液巡视模式并且“输液”巡视已经选中，则直接提交；当前界面未处于输液巡
     * 视模式或者处于输液巡视模式但是未选中输液巡视，则切换到输液巡视模式，并选中“输液巡视”，
     * 下次扫描成功则提交。
     * 如果未找到该条医嘱：切换到输液巡视模式，并弹出提示信息即可
     *
     * @param result
     */
    private void executeInfusionRound(ScanResult result) {
        if (mInfusionRoundList == null || mInfusionRoundList.isEmpty())//输液巡视页面未加载到数据
            return;
        if (TextUtils.isEmpty(mCurrentRoundType) || !mCurrentRoundType.equals(ROUND_TYPE_INFUSION)) {//切换到输液巡视模式
            mRg_roundType.check(R.id.rb_nurseRoundPager_infusionRound);
        }
        List<Order> executeOrderList = OrdersHelp.getExecutingOrderListByOrderCode(mOrderList, result);
        if (executeOrderList == null || executeOrderList.isEmpty()) {//未找到扫描到的医嘱,给出错误提示
            NoticeDialog dialog = createErrorNoNegativeNoticeDialog(ArmsUtils.getString(mContext, R.string.title_dialog_error)
                    , ArmsUtils.getString(mContext, R.string.message_notFoundPatientInfusionOrder));
            dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
                @Override
                public void onPositive() {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return;
        }
        if (!mInfusionRoundList.get(0).isChecked()) {//未选中输液巡视，则先选中
            for (NurseRoundViewItem item : mInfusionRoundList) {
                item.setChecked(true);
                item.setEditable(true);
            }
            mInfusionRoundAdapter.notifyDataSetChanged();
            return;
        }
        //Todo 走到这一步，就需要将输液巡视数据提交到服务器端
        List<NurseRoundItem> infusionRoundDataList = NurseRoundHelper.buildNurseRoundDataList(mInfusionRoundList, mRecordDateTime, mOrderList);
        if (infusionRoundDataList != null && !infusionRoundDataList.isEmpty()) {
            commitNurseRound(infusionRoundDataList);
        }
    }

    /**
     * 提交护理巡视数据
     *
     * @param nurseRoundDataList
     */
    private void commitNurseRound(List<NurseRoundItem> nurseRoundDataList) {
        String id = EventBusUtils.obtainPrivateId(getParentFragment().toString(), CommonConstant.EVENT_INTENTION_COMMIT_NURSE_ROUND);
        EventBusUtils.post(id, nurseRoundDataList);
    }

}
