package com.herenit.mobilenurse.mvp.vital_signs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsViewItem;
import com.herenit.mobilenurse.custom.adapter.VitalSignsItemAdapter;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.custom.widget.layout.SpacesItemDecoration;
import com.herenit.mobilenurse.di.component.DaggerVitalSignsRecordComponent;
import com.herenit.mobilenurse.mvp.base.BasicCommonFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/4/10 10:22
 * desc:体征页面
 */
public class VitalSignsRecordPagerFragment extends BasicCommonFragment<VitalSignsRecordPresenter> implements VitalSignsRecordContract.View {
    /**
     * 体征列表
     */
    @BindView(R2.id.rv_vitalSigns_vitalSignsList)
    RecyclerView mRv_VitalSignsList;
    @Inject
    List<VitalSignsViewItem> mVitalItemList;
    @Inject
    VitalSignsItemAdapter mVitalListAdapter;

    /**
     * 记录时间
     */
    @BindView(R2.id.tv_vitalSigns_recordDateTime)
    TextView mTv_recordDateTime;
    /**
     * 历史记录
     */
    @BindView(R2.id.tv_vitalSigns_history)
    TextView mTv_history;
    /**
     * 下拉刷新
     */
    @BindView(R2.id.srl_vitalSigns_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private LoadingDialog mLoadingDialog;

    private Date mRecordDateTime = TimeUtils.getCurrentDate();

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerVitalSignsRecordComponent
                .builder()
                .view(this)
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_vital_signs_record_pager, container, false);
        return contentView;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();
        mPresenter.loadVitalSignsList(false);
    }

    private void initView() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                //页面刷新时，重新获取当前时间
                mRecordDateTime = TimeUtils.getCurrentDate();
                mTv_recordDateTime.setText(TimeUtils.getYYYYMMDDHHMMString(mRecordDateTime));
                mPresenter.loadVitalSignsList(true);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRv_VitalSignsList.setLayoutManager(layoutManager);
        mRv_VitalSignsList.setAdapter(mVitalListAdapter);
        mRv_VitalSignsList.addItemDecoration(new SpacesItemDecoration(ArmsUtils.getDimens(mContext, R.dimen.dp_5)));
        if (mRecordDateTime != null)
            mTv_recordDateTime.setText(TimeUtils.getYYYYMMDDHHMMString(mRecordDateTime));
        else
            mTv_recordDateTime.setVisibility(View.GONE);
        mTv_recordDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mRecordDateTime);
                new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        mRecordDateTime = date;
                        mTv_recordDateTime.setText(TimeUtils.getYYYYMMDDHHMMString(mRecordDateTime));
                    }
                }).setType(new boolean[]{true, true, true, true, true, false})
                        .setDate(calendar)
                        .isDialog(false)
                        .build().show();
            }
        });
        mTv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VitalSignsHistoryActivity.class);
                intent.putExtra(KeyConstant.NAME_EXTRA_VITAL_ITEM_LIST, (Serializable) mVitalItemList);
                launchActivity(intent);
            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    /**
     * 保存体征数据
     */
    public void saveVitalSignsData() {
        mPresenter.saveVitalSignsData(mRecordDateTime);
    }

    @Override
    public void showMessage(@NonNull String message) {
        showNotAttachToast(message);
    }

    @Override
    public void showError(@NonNull String message) {
        showNotAttachErrorToast(message);
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
    public void onRefreshSuccess() {
        mRefreshLayout.finishRefresh();
    }
}
