package com.herenit.mobilenurse.mvp.tool.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herenit.arms.base.adapter.rv.MultiItemTypeAdapter;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.adapter.ConditionAdapter;
import com.herenit.mobilenurse.custom.adapter.OrdersAdapter;
import com.herenit.mobilenurse.di.component.DaggerOrdersViewerComponent;
import com.herenit.mobilenurse.mvp.base.ConditionWithListActivity;
import com.herenit.mobilenurse.mvp.orders.OrderListFragment;
import com.herenit.mobilenurse.mvp.orders.OrdersInfoActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/9/20 13:52
 * desc:医嘱选择器
 */
public class OrdersViewerActivity extends ConditionWithListActivity<OrdersViewerPresenter> implements OrdersViewerContract.View {

    //筛选条件Adapter
    @Inject
    ConditionAdapter mConditionAdapter;
    //医嘱列表Adapter
    @Inject
    OrdersAdapter mOrdersAdapter;
    //筛选条件数据
    @Inject
    List<Conditions> mConditionList = new ArrayList<>();
    //医嘱列表数据
    @Inject
    List<Order> mOrderList = new ArrayList<>();
    //刷新控件
    @BindView(R2.id.srl_orderListPager_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    //底部“全选”、“执行”布局
    @BindView(R2.id.ll_orderListPager_bottom)
    LinearLayout mLL_bottom;
    @BindView(R2.id.tv_orderListPager_selectAll)
    TextView mTv_selectAll;//全选
    @BindView(R2.id.tv_orderListPager_execute)
    TextView mTv_execute;//执行

    //默认操作类型为查看
    private int mOperationType = CommonConstant.OPERATION_TYPE_QUERY;//操作类型
    private Sickbed mSickbed;//患者
    private boolean mEditable;//当前界面是否可编辑（医嘱是否可选择）

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerOrdersViewerComponent
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
    protected int layoutId() {
        return R.layout.activity_orders_selector;
    }

    protected void initView() {
        initTitleBar();
        //获取当前页面是否处于编辑状态，并初始化设置给Adapter
        mOrdersAdapter.changeEditable(mEditable);
        selectedItemChanged();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//canScrollVertically方法返回false，可解决ScrollView嵌套RecyclerView造成的滑动卡顿问题
                return false;
            }
        };
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mOrdersAdapter);
        //刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                mPresenter.loadPatientOrderList(mSickbed, true);
            }
        });
        mOrdersAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {//点击单条医嘱，进入医嘱详情界面
                if (mEditable) {//处于编辑状态，点击Item修改选中状态
                    mOrdersAdapter.editOrderListBySelectPosition(position);
                    selectedItemChanged();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mTv_selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrdersAdapter.isSelectedAll()) {//已经全选，则清空选择
                    mOrdersAdapter.clearDataListSelect();
                } else {//没有全选，则全选
                    mOrdersAdapter.selectAllCanExecuteOrders();
                }
                selectedItemChanged();
            }
        });
        mTv_execute.setText(ArmsUtils.getString(mContext, R.string.btn_sure));
        mTv_execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeSelectOrders();//完成医嘱选择
            }
        });
    }

    /**
     * 完成选择医嘱
     */
    private void completeSelectOrders() {
        List<Order> selectOrders = mOrdersAdapter.getSelectedOrders();
        if (mOperationType == CommonConstant.OPERATION_TYPE_SELECT) {//如果做的是选择医嘱的操作，则将选择医嘱返回
            Intent intent = new Intent();
            intent.putExtra(KeyConstant.NAME_EXTRA_ORDER_LIST, (Serializable) selectOrders);
            setResult(CommonConstant.RESULT_CODE_SELECT_ORDERS, intent);
            finish();
        }
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.common_order));
        setTitleBarLeftOnClickListener(new View.OnClickListener() {//点击返回
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (mSickbed != null) {
            setTitleBarCenterText(mSickbed.getPatientTitle());
        }
        setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.common_order));
    }

    /**
     * 选中的医嘱列表发生改变，界面做出改变（是否全选状态、是否可以执行）
     */
    private void selectedItemChanged() {
        ViewUtils.setViewVisibility(mLL_bottom, mEditable);
        mLL_bottom.setVisibility(View.VISIBLE);
        if (mOrdersAdapter.isSelectedAll()) {//全选状态
            mTv_selectAll.setText(R.string.btn_selectAllCancel);
        } else {
            mTv_selectAll.setText(R.string.btn_selectAll);
        }
        List<Order> selectOrders = mOrdersAdapter.getSelectedOrders();
        if (selectOrders == null || selectOrders.isEmpty()) {
            mTv_execute.setTextColor(ArmsUtils.getColor(mContext, R.color.royalBlueFuzzy));
            mTv_execute.setText(R.string.btn_sure);
        } else {
            String execute = ArmsUtils.getString(mContext, R.string.btn_sure) + "（" + selectOrders.size() + "）";
            mTv_execute.setText(execute);
            mTv_execute.setTextColor(ArmsUtils.getColor(mContext, R.color.bg_royalBlue));
        }
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            mOperationType = intent.getIntExtra(KeyConstant.NAME_EXTRA_OPERATION_TYPE, CommonConstant.OPERATION_TYPE_QUERY);
            mSickbed = (Sickbed) intent.getSerializableExtra(KeyConstant.NAME_EXTRA_SICKBED);
            if (mOperationType == CommonConstant.OPERATION_TYPE_QUERY) {//普通查看不可编辑
                mEditable = false;
            } else if (mOperationType == CommonConstant.OPERATION_TYPE_SELECT) {//选择操作可编辑
                mEditable = true;
            }
        }
        initView();
        mPresenter.loadData(mSickbed, false);
    }

    /**
     * 筛选条件发生改变
     *
     * @return
     */
    @Override
    public ConditionAdapter conditionAdapter() {
        return mConditionAdapter;
    }

    @Override
    public void conditionChanged() {
        mPresenter.loadPatientOrderList(mSickbed, false);
        //展示条件
        mTv_selectCondition.setText(Conditions.getSelectedConditionString(mConditionList));
    }

    @Override
    public void showConditionUI() {
        mConditionAdapter.notifyDataSetChanged();
        mTv_selectCondition.setText(Conditions.getSelectedConditionString(mConditionList));
    }

    @Override
    public void showOrderListUI() {
        mOrdersAdapter.clearDataListSelect();
    }

    @Override
    public void showLoading() {
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showError(@NonNull String message) {
        showToast(message);
    }
}
