package com.herenit.mobilenurse.mvp.nursing_round;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.herenit.arms.base.adapter.rv.MultiItemTypeAdapter;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.CommonNameCode;
import com.herenit.mobilenurse.criteria.common.NameCode;
import com.herenit.mobilenurse.criteria.common.SelectNameCode;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.NurseRoundItem;
import com.herenit.mobilenurse.criteria.entity.submit.CommonPatientItemQuery;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.custom.adapter.NameCodeAdapter;
import com.herenit.mobilenurse.custom.adapter.NurseRoundHistoryAdapter;
import com.herenit.mobilenurse.custom.adapter.SelectBoxAdapter;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.dialog.ListDialog;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.custom.widget.dialog.NoticeDialog;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.di.component.DaggerNurseRoundHistoryComponent;
import com.herenit.mobilenurse.mvp.base.BasicBusinessActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/8/12 11:25
 * desc:
 */
public class NurseRoundHistoryActivity extends BasicBusinessActivity<NurseRoundHistoryPresenter> implements NurseRoundHistoryContract.View {
    //默认查询今天的巡视记录
    public static final String DEFAULT_TIME_INTERVAL_CODE = CommonConstant.TIME_CODE_TODAY;

    @BindView(R2.id.srl_nurseRoundHistory_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R2.id.rg_nurseRoundHistory)
    RadioGroup mRg_nurseRoundHistory;

    @BindView(R2.id.rb_nurseRoundHistory_roundItem)
    RadioButton mRb_selectRoundItem;
    @BindView(R2.id.rb_nurseRoundHistory_date)
    RadioButton mRb_selectTime;

    @BindView(R2.id.rv_nurseRoundHistory_historyList)
    RecyclerView mRv_nurseRoundHistory;

    @Inject
    NurseRoundHistoryAdapter mNurseRoundHistoryAdapter;//巡视历史列表Adapter
    @Inject
    List<NurseRoundItem> mNurseRoundHistoryList; //巡视历史数据（显示）

    /**
     * 巡视历史数据查询条件
     */
    @Inject
    CommonPatientItemQuery mQuery;


    //巡视项目选择弹窗列表
    ListDialog mSelectNurseRoundItemListDialog;
    @Inject
    List<SelectNameCode> mSelectNurseRoundItemList;//巡视项目列表
    @Inject
    SelectBoxAdapter mSelectNurseRoundItemAdapter;

    //时间区域选择弹窗列表
    ListDialog mSelectDateListDialog;
    @Inject
    List<CommonNameCode> mSelectDateList;//时间范围列表
    @Inject
    NameCodeAdapter<CommonNameCode> mSelectDateAdapter;

    private LoadingDialog mLoadingDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerNurseRoundHistoryComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_TV;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_nurse_round_history;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initView();
        mPresenter.loadNurseRoundHistoryList(false);
    }

    private void initView() {
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        setTitleBarLeftOnClickListener(new View.OnClickListener() {//左上角返回键
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleBarCenterText(ArmsUtils.getString(mContext, R.string.title_operation_nurse_round_history));
        setTitleBarRightText1(SickbedTemp.getInstance().getCurrentSickbed().getPatientTitle());
        //巡视列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRv_nurseRoundHistory.setLayoutManager(layoutManager);
        mRv_nurseRoundHistory.setAdapter(mNurseRoundHistoryAdapter);
        mNurseRoundHistoryAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            }

            /**
             * 长按，删除巡视记录
             * @param view
             * @param holder
             * @param position
             * @return
             */
            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                deleteNurseRoundHistory(position);
                return true;
            }
        });

        //时间条件选择
        setTimeIntervalText(null);
        mRb_selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeListDialog();
            }
        });
        //巡视项目选择
        mRb_selectRoundItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNurseRoundItemListDialog();
            }
        });
        //刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                mPresenter.loadNurseRoundHistoryList(true);
            }
        });

    }

    /**
     * 删除某条（某组）巡视记录
     *
     * @param position
     */
    private void deleteNurseRoundHistory(int position) {
        NurseRoundItem item = mNurseRoundHistoryList.get(position);
        List<NurseRoundItem> deleteList = new ArrayList<>();
        String deleteMessage = "";
        if (CommonConstant.ITEM_TYPE_GROUP.equals(item.getItemType())) {//长按的正好是组项
            deleteList.add(item);
            deleteMessage = ArmsUtils.getString(mContext, R.string.message_sure_deleteNurseRoundData) +
                    "\n（" + item.getItemName() + "  " + TimeUtils.getYYYYMMDDHHMMString(item.getTimePoint()) + "）";
        } else {//长按到子项，要循环得到组项
            String group = item.getPath().split("\\/")[0];
            for (NurseRoundItem nurseRoundItem : mNurseRoundHistoryList) {
                if (group.equals(nurseRoundItem.getPath()) && CommonConstant.ITEM_TYPE_GROUP.equals(nurseRoundItem.getItemType())) {
                    deleteList.add(nurseRoundItem);
                    deleteMessage = ArmsUtils.getString(mContext, R.string.message_sure_deleteNurseRoundData) +
                            "\n（" + nurseRoundItem.getItemName() + "  " + TimeUtils.getYYYYMMDDHHMMString(nurseRoundItem.getTimePoint()) + "）";
                    break;
                }
            }
        }
        NoticeDialog dialog = createNoticeDialog(ArmsUtils.getString(mContext, R.string.title_dialog_delete), deleteMessage);
        dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
            @Override
            public void onPositive() {//确定，则删除
                dialog.dismiss();
                mPresenter.deleteNurseRoundData(deleteList);
            }

            @Override
            public void onNegative() {//取消则不删除
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 显示巡视型选择列表弹窗
     */
    private void showNurseRoundItemListDialog() {
        if (mSelectNurseRoundItemListDialog == null) {
            mSelectNurseRoundItemListDialog = createMultiChoiceListDialog(ArmsUtils.getString(mContext, R.string.common_vital_signs_item), mSelectNurseRoundItemAdapter);
            mSelectNurseRoundItemListDialog.setPositiveNegativeListener(new PositiveNegativeListener() {
                @Override
                public void onPositive(Object... backData) {
                    mSelectNurseRoundItemListDialog.dismiss();
                    mPresenter.resetNurseRoundItemCondition();//先设置巡视项目条件
                    mPresenter.loadNurseRoundHistoryList(false);
                }

                @Override
                public void onNegative(Object... backData) {
                    mSelectNurseRoundItemListDialog.dismiss();
                }
            });
        }
        mSelectNurseRoundItemListDialog.show();
    }


    /**
     * 时间选择弹窗
     */
    private void showTimeListDialog() {
        if (mSelectDateListDialog == null) {
            mSelectDateListDialog = createNoBottomListDialog(ArmsUtils.getString(mContext, R.string.common_time), mSelectDateAdapter);
            mSelectDateListDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mSelectDateListDialog.dismiss();
                    NameCode Interval = mSelectDateList.get(position);
                    setTimeIntervalText(Interval.getName());
                    mQuery.setStartDateTime(TimeUtils.getStartDateTimeByTimeCode(Interval.getCode()));
                    mQuery.setStopDateTime(TimeUtils.getStopDateTimeByTimeCode(Interval.getCode()));
                    mPresenter.loadNurseRoundHistoryList(false);
                }
            });
        }
        mSelectDateListDialog.show();
    }

    /**
     * 设置显示的时间条件,
     *
     * @param timeIntervalText 当为null时，说明刚开始没选择，此时要设置默认时间条件
     */
    private void setTimeIntervalText(@Nullable String timeIntervalText) {
        if (!TextUtils.isEmpty(timeIntervalText)) {
            mRb_selectTime.setText(timeIntervalText);
        } else {
            for (NameCode timeInterval : mSelectDateList) {
                if (timeInterval.getCode().equals(DEFAULT_TIME_INTERVAL_CODE)) {
                    mRb_selectTime.setText(timeInterval.getName());
                    return;
                }
            }
        }
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
    public void loadDataSuccess() {
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void showMessage(@NonNull String message) {
        showToast(message);
    }

    @Override
    public void showError(@NonNull String message) {
        showErrorToast(message);
    }
}
