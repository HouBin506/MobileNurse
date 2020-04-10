package com.herenit.mobilenurse.mvp.exam.report;

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

import com.herenit.arms.base.adapter.rv.MultiItemTypeAdapter;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.ExamReport;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.adapter.ExamReportAdapter;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.di.component.DaggerExamReportComponent;
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
 * desc:检查报告页面
 */
public class ExamReportPagerFragment extends BasicCommonFragment<ExamReportPresenter> implements ExamReportContract.View {

    @BindView(R2.id.srl_examReportPager_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R2.id.rv_examReportPager_list)
    RecyclerView mRv_examReport;
    @Inject
    List<ExamReport> mExamReportList;//检查列表数据
    @Inject
    ExamReportAdapter mExamReportAdapter;//检查列表Adapter

    private LoadingDialog mLoadingDialog;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerExamReportComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);

    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_exam_report_pager, container, false);
        return contentView;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();
        mPresenter.loadExamReportList(false);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRv_examReport.setLayoutManager(layoutManager);
        mRv_examReport.setAdapter(mExamReportAdapter);
        mExamReportAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ExamReport examReport = mExamReportList.get(position);
                Sickbed sickbed = getCurrentSickbed();
                if (sickbed == null || examReport == null)
                    return;
                Intent intent = new Intent(mContext, ExamReportInfoActivity.class);
                intent.putExtra(KeyConstant.NAME_EXTRA_SICKBED, sickbed);
                intent.putExtra(KeyConstant.NAME_EXTRA_EXAM_REPORT, examReport);
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
                mPresenter.loadExamReportList(true);
            }
        });
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
