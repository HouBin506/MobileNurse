package com.herenit.mobilenurse.mvp.vital_signs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.ScreenUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.CommonNameCode;
import com.herenit.mobilenurse.criteria.common.NameCode;
import com.herenit.mobilenurse.criteria.common.SelectNameCode;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.submit.VitalSignsHistoryQuery;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsViewItem;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.custom.adapter.NameCodeAdapter;
import com.herenit.mobilenurse.custom.adapter.SelectBoxAdapter;
import com.herenit.mobilenurse.custom.adapter.VitalSignsHistoryAdapter;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.dialog.ListDialog;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.di.component.DaggerVitalSignsHistoryComponent;
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
 * date: 2019/5/17 9:47
 * desc: 体征历史记录View
 */
public class VitalSignsHistoryActivity extends BasicBusinessActivity<VitalSignsHistoryPresenter> implements VitalSignsHistoryContract.View {

    public static final int REQUEST_CODE_VITAL_SIGHS_EDIT = 1234;
    public static final int RESULT_CODE_VITAL_SIGHS_EDIT = 2345;
    //定义默认的查询条件中的时间条件
    public static final String DEFAULT_TIME_INTERVAL_CODE = CommonConstant.TIME_CODE_TODAY;

    @BindView(R2.id.srl_vitalSignsHistory_refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R2.id.rb_vitalSignsHistory_date)
    RadioButton mRb_Date;//时间条件
    @BindView(R2.id.rb_vitalSignsHistory_signType)
    RadioButton mRb_Type;//体征类型条件
    @BindView(R2.id.lv_vitalSignsHistory_list)
    SwipeMenuListView mListView;

    @Inject
    VitalSignsHistoryAdapter mVitalSignsAdapter;//体征历史列表Adapter
    @Inject
    List<VitalSignsItem> mVitalSignsList; //体征历史数据（显示）

    /**
     * 体征历史数据查询条件
     */
    @Inject
    VitalSignsHistoryQuery mQuery;

    /**
     * 体征项目View控制列表
     */
    private List<VitalSignsViewItem> mVitalSignsViewList = new ArrayList<>();

    //体征项目选择弹窗列表
    ListDialog mSelectVitalItemListDialog;
    @Inject
    List<SelectNameCode> mSelectVitalItemList;//体征项目列表
    @Inject
    SelectBoxAdapter mSelectVitalItemAdapter;

    //时间区域选择弹窗列表
    ListDialog mSelectDateListDialog;
    @Inject
    List<CommonNameCode> mSelectDateList;//时间范围列表
    @Inject
    NameCodeAdapter<CommonNameCode> mSelectDateAdapter;

    private LoadingDialog mLoadingDialog;

    private VitalSignsItem mCurrentOperator;//当前正在操作的体征数据项


    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_TV;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVitalSignsHistoryComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_vital_signs_history;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initView();
        Intent intent = getIntent();
        if (intent != null) {
            //获取到体征项目列表
            List<VitalSignsViewItem> vitalSignsViewItemList = (List<VitalSignsViewItem>) intent.getSerializableExtra(KeyConstant.NAME_EXTRA_VITAL_ITEM_LIST);
            if (vitalSignsViewItemList != null) {
                mVitalSignsViewList.clear();
                mVitalSignsViewList.addAll(vitalSignsViewItemList);
            }
            convertVitalItemList(vitalSignsViewItemList);
        }
        mPresenter.loadVitalSignsHistory(false);
    }


    /**
     * 将相对复杂的体征项目列表，转化成Name Code组合，放到列表，更方便操作
     *
     * @param vitalSignsItemList
     * @return
     */
    private void convertVitalItemList(List<VitalSignsViewItem> vitalSignsItemList) {
        mSelectVitalItemList.clear();
        if (vitalSignsItemList == null)
            return;
        for (int x = 0; x < vitalSignsItemList.size(); x++) {//默认加载所有体征项目类型
            VitalSignsViewItem item = vitalSignsItemList.get(x);
            if (item.getItemType().equals(CommonConstant.ITEM_TYPE_GROUP))
                continue;
            SelectNameCode nameCode = new SelectNameCode();
            nameCode.setName(item.getItemName());
            nameCode.setCode(VitalSignsHelper.getCodeByItemAndClassCode(item.getItemCode(), item.getClassCode()));
            mSelectVitalItemList.add(nameCode);
        }
    }


    /**
     * 加载控件
     */
    private void initView() {
        //设置标题栏
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        setTitleBarLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleBarCenterText(ArmsUtils.getString(mContext, R.string.title_activity_vital_signs_history));
        setTitleBarRightText1(SickbedTemp.getInstance().getCurrentSickbed().getPatientTitle());
        //设置Item可侧滑的ListView
        setSwipeMenuListView();
        mRb_Type.setOnClickListener(new View.OnClickListener() {//体征类型选择
            @Override
            public void onClick(View v) {
                showVitalItemListDialog();
            }
        });
        setTimeIntervalText(null);
        mRb_Date.setOnClickListener(new View.OnClickListener() {//选择时间范围，默认为全部
            @Override
            public void onClick(View v) {
                showTimeListDialog();
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                mPresenter.loadVitalSignsHistory(true);
            }
        });
    }

    /**
     * 设置ListView的Item侧滑功能
     */
    private void setSwipeMenuListView() {
        mListView.setAdapter(mVitalSignsAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //添加“编辑”条目
                SwipeMenuItem editItem = new SwipeMenuItem(mContext);
                editItem.setBackground(R.color.bg_light_gray);
                editItem.setTitleColor(ArmsUtils.getColor(mContext, R.color.white));
                editItem.setTitle(ArmsUtils.getString(mContext, R.string.btn_edit));
                editItem.setTitleSize((int) ScreenUtils.px2sp(mContext, ArmsUtils.getDimens(mContext, R.dimen.text_content_level_3)));
                editItem.setWidth(ArmsUtils.getDimens(mContext, R.dimen.dp_70));
                menu.addMenuItem(editItem);
                //添加“删除”条目
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
                deleteItem.setBackground(R.color.red);
                deleteItem.setTitleColor(ArmsUtils.getColor(mContext, R.color.white));
                deleteItem.setTitle(ArmsUtils.getString(mContext, R.string.btn_delete));
                deleteItem.setTitleSize((int) ScreenUtils.px2sp(mContext, ArmsUtils.getDimens(mContext, R.dimen.text_content_level_3)));
                deleteItem.setWidth(ArmsUtils.getDimens(mContext, R.dimen.dp_70));
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                mCurrentOperator = mVitalSignsList.get(position);
                if (mCurrentOperator == null)
                    return false;
                switch (index) {
                    case 0://编辑
                        editVitalSigns();
                        break;
                    case 1://删除
                        deleteVitalSigns();
                        break;
                }
                return false;
            }
        });
        //设置侧滑方向
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    /**
     * 删除体征记录
     */
    private void deleteVitalSigns() {
        mPresenter.deleteVitalSigns(mCurrentOperator);
    }

    /**
     * 修改体征记录
     */
    private void editVitalSigns() {
        String itemName = mCurrentOperator.getItemName();
        String itemCode = mCurrentOperator.getItemCode();
        String classCode = mCurrentOperator.getClassCode();
        VitalSignsViewItem targetViewItem = null;
        for (VitalSignsViewItem viewItem : mVitalSignsViewList) {
            if (!TextUtils.isEmpty(itemCode) && itemCode.equals(viewItem.getItemCode()) &&
                    !TextUtils.isEmpty(itemName) && itemName.equals(viewItem.getItemName())
                    && !viewItem.getItemType().equals(CommonConstant.ITEM_TYPE_GROUP)) {//找到要操作的体征项，注意：如果是组Item，则不要，
                if (TextUtils.isEmpty(classCode) || classCode.equals(viewItem.getClassCode())) {
                    targetViewItem = viewItem;
                    break;
                }
            }
        }
        if (targetViewItem == null)
            return;
        //给targetViewItem赋值后传入Edit界面
        VitalSignsHelper.convertDataItemToViewItem(mCurrentOperator, targetViewItem);
        Intent intent = new Intent(mContext, VitalSignsEditActivity.class);
        intent.putExtra(KeyConstant.NAME_EXTRA_VITAL_SIGNS_VIEW, targetViewItem);
        startActivityForResult(intent, REQUEST_CODE_VITAL_SIGHS_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_VITAL_SIGHS_EDIT && resultCode == RESULT_CODE_VITAL_SIGHS_EDIT) {
            if (data != null) {
                VitalSignsViewItem viewItem = (VitalSignsViewItem) data.getSerializableExtra(KeyConstant.NAME_EXTRA_VITAL_SIGNS_VIEW);
                if (viewItem != null && mCurrentOperator != null) {
                    mCurrentOperator.setNewItemValue(TextUtils.isEmpty(viewItem.getValue()) ? viewItem.getSpecialValue() : viewItem.getValue());
                    mCurrentOperator.setMemo(viewItem.getMemo());
                    //设置新的时间点
                    mCurrentOperator.setNewTimePoint(VitalSignsHelper.getTimePoint(viewItem.getTimePoint(), viewItem.getFixedTimePoint()));
                    //如果时间点没有修改，则不给newTimePoint赋值
                    if (mCurrentOperator.getTimePoint().equals(mCurrentOperator.getNewTimePoint()))
                        mCurrentOperator.setNewTimePoint(null);
                    mPresenter.updateVitalSigns(mCurrentOperator);
                }
            }
        }
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
                    mPresenter.loadVitalSignsHistory(false);
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
            mRb_Date.setText(timeIntervalText);
        } else {
            for (NameCode timeInterval : mSelectDateList) {
                if (timeInterval.getCode().equals(DEFAULT_TIME_INTERVAL_CODE)) {
                    mRb_Date.setText(timeInterval.getName());
                    return;
                }
            }
        }
    }

    /**
     * 显示体征类型选择列表弹窗
     */
    private void showVitalItemListDialog() {
        if (mSelectVitalItemListDialog == null) {
            mSelectVitalItemListDialog = createMultiChoiceListDialog(ArmsUtils.getString(mContext, R.string.common_vital_signs_item), mSelectVitalItemAdapter);
            mSelectVitalItemListDialog.setPositiveNegativeListener(new PositiveNegativeListener() {
                @Override
                public void onPositive(Object... backData) {
                    mSelectVitalItemListDialog.dismiss();
                    mPresenter.resetVitalItemCondition();//先设置体征项目条件
                    mPresenter.loadVitalSignsHistory(false);
                }

                @Override
                public void onNegative(Object... backData) {
                    mSelectVitalItemListDialog.dismiss();
                }
            });
        }
        mSelectVitalItemListDialog.show();
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
        showNotAttachErrorToast(message);
    }

    @Override
    public void showMessage(@NonNull String message) {
        showToast(message);
    }

    /**
     * 刷新完成
     */
    @Override
    public void refreshSuccess() {
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void showError(@NonNull String title, @Nullable String message) {
        showErrorToast(message);
    }
}
