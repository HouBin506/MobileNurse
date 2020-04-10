package com.herenit.mobilenurse.mvp.sickbed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.herenit.arms.base.adapter.rv.MultiItemTypeAdapter;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.custom.adapter.ConditionAdapter;
import com.herenit.mobilenurse.custom.adapter.SickbedAdapter;
import com.herenit.mobilenurse.di.component.DaggerSickbedListComponent;
import com.herenit.mobilenurse.mvp.base.ConditionWithListFragment;
import com.herenit.mobilenurse.mvp.main.SinglePatientActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


/**
 * author: HouBin
 * date: 2019/2/16 16:43
 * desc: 床位列表页面
 */
public class SickbedListFragment extends ConditionWithListFragment<SickbedListPresenter> implements SickbedListContract.View {

    @Inject
    List<Conditions> mConditionsList;//页面查询条件展示数据
    @Inject
    List<Sickbed> mSickbedList;//页面展示患者列表数据

    @Inject
    ConditionAdapter mConditionAdapter;//条件列表Adapter

    @Inject
    SickbedAdapter mSickbedAdapter;//床位列表Adapter

    @BindView(R2.id.srl_sickbedList_refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    /**
     * 统计当前条件下查询出来的患者数量
     */
    @BindView(R2.id.tv_sickbedList_num)
    TextView mTv_num;

    /**
     * 当前科室
     */
    private String groupCode;


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerSickbedListComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .injec(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_sickbed_list;
    }

    @Override
    protected void initView(View contentView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {//canScrollVertically方法返回false，可解决ScrollView嵌套RecyclerView造成的滑动卡顿问题
                return false;
            }
        };
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mSickbedAdapter);
        mSickbedAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Sickbed sickbed = mSickbedList.get(position);
                toSinglePatientModule(sickbed);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                mPresenter.loadData(true);
            }
        });
    }

    /**
     * 跳转到单患者模块
     */
    private void toSinglePatientModule(Sickbed sickbed) {
        if (sickbed == null || TextUtils.isEmpty(sickbed.getPatientId())) {//该床无患者
            showMessage(ArmsUtils.getString(mContext, R.string.error_emptyPatient));
        } else {//跳转
            SickbedTemp.getInstance().setCurrentSickbed(sickbed);
            Intent intent = new Intent(mContext, SinglePatientActivity.class);
            launchActivity(intent);
        }
    }

    @Override
    public ConditionAdapter conditionAdapter() {
        return mConditionAdapter;
    }

    /**
     * 筛选条件改变
     */
    @Override
    public void conditionChanged() {
        mTv_selectCondition.setText(Conditions.getSelectedConditionString(mConditionsList));
        mPresenter.loadSickbedList(false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mPresenter.loadData(false);
        groupCode = UserTemp.getInstance().getGroupCode();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showMessage(@NonNull String message) {
        showToast(message);
    }

    @Override
    public void showLoading() {
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        mLoadingDialog.dismiss();
    }


    /**
     * 完成少选条件数据加载
     */
    @Override
    public void loadConditionsFinish() {
        mTv_selectCondition.setText(Conditions.getSelectedConditionString(mConditionsList));
    }

    @Override
    public void finishRefresh() {
        mRefreshLayout.finishRefresh();
    }

    /**
     * 扫描结果
     *
     * @param scanResult
     */
    @Override
    public void onScanResult(ScanResult scanResult) {
        if (scanResult == null || scanResult.getCodeType() == ScanResult.CODE_TYPE_UNKNOWN) {
            showToast(ArmsUtils.getString(mContext, R.string.message_unknown_scanCode));
            return;
        }
        if (scanResult.getCodeType() == ScanResult.CODE_TYPE_PATIENT) {//腕带
            Sickbed sickbed = SickbedTemp.getInstance().getSickbed(scanResult.getPatientId(), scanResult.getVisitId());
            if (sickbed == null) {
                showToast(ArmsUtils.getString(mContext, R.string.message_notFoundPatient));
                return;
            } else {
                toSinglePatientModule(sickbed);
            }
        } else {//非腕带
            showToast(ArmsUtils.getString(mContext, R.string.message_pleaseScanPatientCode));
            return;
        }
    }

    /**
     * 显示患者数量
     *
     * @param num
     */
    @Override
    public void showPatientNum(int num) {
        mTv_num.setText("统计：" + num + "人");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    /**
     * 需要判断当前患者列表是否是当前科室患者列表，这样做为了避免切换科室时，
     * 页面恰好处于隐藏状态，当页面显示时，患者列表与科室不匹配的情况
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //被隐藏
        } else {
            if (!TextUtils.isEmpty(groupCode) && !groupCode.equals(UserTemp.getInstance().getGroupCode())) {
                groupCode = UserTemp.getInstance().getGroupCode();
                mPresenter.loadSickbedList(false);
            }
        }
    }

    /**
     * EventBus消息接收
     *
     * @param event
     */
    @Subscribe
    public void onEventBusReceived(Event event) {
        String id = event.getId();
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (TextUtils.isEmpty(intention)) {//公共事件消息
            if (id.equals(EventIntentionEnum.SWITCH_GROUP.getId())) {//科室（护理单元切换）
                //切换科室，需要重新加载患者列表
                mPresenter.loadSickbedList(false);
            } else if (id.equals(EventIntentionEnum.CODE_TYPE_PATIENT.getId())) {
                ScanResult scanResult = (ScanResult) event.getMessage();
                onScanResult(scanResult);
            }
        } else {//私有事件消息

        }
    }

}
