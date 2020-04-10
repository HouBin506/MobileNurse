package com.herenit.mobilenurse.mvp.lab.report;

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
import android.widget.RadioGroup;

import com.herenit.arms.base.adapter.rv.MultiItemTypeAdapter;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.lab.CommonLabReport;
import com.herenit.mobilenurse.criteria.entity.lab.MicroorganismLabReport;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.adapter.LabReportAdapter;
import com.herenit.mobilenurse.custom.adapter.delegate.MicroorganismLabReportAdapter;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.di.component.DaggerLabReportComponent;
import com.herenit.mobilenurse.mvp.base.BasicCommonFragment;
import com.herenit.mobilenurse.mvp.base.SwitchFragmentPagerPatientActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/8/15 14:40
 * desc:检验报告页面
 */
public class LabReportPagerFragment extends BasicCommonFragment<LabReportPresenter> implements LabReportContract.View {

    @BindView(R2.id.srl_labReportPager_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R2.id.rv_labReportPager_list)
    RecyclerView mRv_labReport;
    @Inject
    List<CommonLabReport> mLabReportList;//普通检验列表数据
    @Inject
    LabReportAdapter mLabReportAdapter;//普通检验列表Adapter

    @Inject
    List<MicroorganismLabReport> mMicroorganismLabReportList;//微生物检验列表数据
    @Inject
    MicroorganismLabReportAdapter mMicroorganismLabReportAdapter;//微生物检验列表Adapter

    @BindView(R2.id.rg_labReportPager_labType)
    RadioGroup mRg_labType;

    private LoadingDialog mLoadingDialog;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerLabReportComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);

    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_lab_report_pager, container, false);
        return contentView;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();
        mPresenter.loadLabReportList(false);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRv_labReport.setLayoutManager(layoutManager);
        mLabReportAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                CommonLabReport labReport = mLabReportList.get(position);
                Sickbed sickbed = getCurrentSickbed();
                if (sickbed == null || labReport == null)
                    return;
                Intent intent = new Intent(mContext, LabReportInfoActivity.class);
                intent.putExtra(KeyConstant.NAME_EXTRA_SICKBED, sickbed);
                intent.putExtra(KeyConstant.NAME_EXTRA_LAB_REPORT, labReport);
                launchActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mMicroorganismLabReportAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                MicroorganismLabReport labReport = mMicroorganismLabReportList.get(position);
                Sickbed sickbed = getCurrentSickbed();
                if (sickbed == null || labReport == null)
                    return;
                Intent intent = new Intent(mContext, LabReportInfoActivity.class);
                intent.putExtra(KeyConstant.NAME_EXTRA_SICKBED, sickbed);
                intent.putExtra(KeyConstant.NAME_EXTRA_LAB_REPORT, labReport);
                launchActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                int checkedRadioButtonId = mRg_labType.getCheckedRadioButtonId();
                if (checkedRadioButtonId == R.id.rb_labReportPager_common) {//当前是普通检验页面
                    mPresenter.loadLabReportList(true);
                } else if (checkedRadioButtonId == R.id.rb_labReportPager_microorganism) {//当前是微生物检验页面
                    mPresenter.loadMicroorganismLabReportList(true);
                }
            }
        });

        mRg_labType.setVisibility(View.GONE);//TODO 临夏州不现实微生物检验
        //检验类型的选择，一般检验、微生物.....
        mRg_labType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_labReportPager_common) {//普通检验
                    mRv_labReport.setAdapter(mLabReportAdapter);
                    mPresenter.loadLabReportList(false);
                } else if (checkedId == R.id.rb_labReportPager_microorganism) {//微生物检验
                    mRv_labReport.setAdapter(mMicroorganismLabReportAdapter);
                    mPresenter.loadMicroorganismLabReportList(false);
                }
            }
        });
        mRg_labType.check(R.id.rb_labReportPager_common);//默认显示普通检验
    }

    @Override
    public void setData(@Nullable Object data) {

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

    /**
     * 接收EventBus消息
     *
     * @param event
     */
    @Subscribe
    public void onReceiveEvent(Event event) {
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (!TextUtils.isEmpty(intention)) {//私有事件消费
        } else {//公共事件

        }
    }

    @Override
    public Sickbed getCurrentSickbed() {
        return ((SwitchFragmentPagerPatientActivity) getActivity()).currentSickbed();
    }

    @Override
    public void loadDataSuccess() {
        mRefreshLayout.finishRefresh();
    }
}
