package com.herenit.mobilenurse.mvp.orders;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.herenit.arms.base.adapter.rv.MultiItemTypeAdapter;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.mvp.IView;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.adapter.ConditionAdapter;
import com.herenit.mobilenurse.custom.adapter.OrdersAdapter;
import com.herenit.mobilenurse.custom.widget.layout.MeasuredGridLayoutManager;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.mvp.base.BasicCommonFragment;
import com.herenit.mobilenurse.mvp.base.ConditionWithListFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/3/6 15:46
 * desc:医嘱列表界面，ViewPager载体，详细数据在
 */
public class OrderListPagerFragment extends ConditionWithListFragment implements IView {

    //筛选条件Adapter
    private ConditionAdapter mConditionAdapter;
    //医嘱列表Adapter
    private OrdersAdapter mOrdersAdapter;
    //筛选条件数据
    private List<Conditions> mConditionList = new ArrayList<>();
    //医嘱列表数据
    private List<Order> mOrderList = new ArrayList<>();
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
    //要执行的医嘱列表
    private List<Order> mExecuteOrders = new ArrayList<>();


    @Override
    protected int layoutId() {
        return R.layout.fragment_order_list_pager;
    }

    @Override
    protected void initView(View contentView) {
        mOrdersAdapter = new OrdersAdapter(mContext, mOrderList);
        OrderListFragment parentFragment = (OrderListFragment) getParentFragment();
        //获取当前页面是否处于编辑状态，并初始化设置给Adapter
        mOrdersAdapter.changeEditable(parentFragment.isEditable());
        selectedItemChanged();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//canScrollVertically方法返回false，可解决ScrollView嵌套RecyclerView造成的滑动卡顿问题
                return false;
            }
        };
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mOrdersAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                //发送通知，刷新医嘱列表数据
                String id = EventBusUtils.obtainPrivateId(getParentFragment().toString(), CommonConstant.EVENT_INTENTION_LOAD_DATA);
                EventBusUtils.post(id, true);
            }
        });
        mOrdersAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {//点击单条医嘱，进入医嘱详情界面
                if (mOrdersAdapter.isEditable()) {//处于编辑状态，点击Item修改选中状态
                    mOrdersAdapter.editOrderListBySelectPosition(position);
                    selectedItemChanged();
                } else {//处于非编辑状态，点击Item跳转到详情界面
                    List<Order> currentOrders = mOrdersAdapter.getGroupOrdersByPosition(position);
                    if (currentOrders == null || currentOrders.isEmpty())
                        return;
                    Intent intent = new Intent(mContext, OrdersInfoActivity.class);
                    intent.putExtra(KeyConstant.NAME_EXTRA_ORDER_LIST, (Serializable) currentOrders);
//                    launchActivity(intent);
                    getParentFragment().startActivityForResult(intent, CommonConstant.REQUEST_CODE_ORDER_INFO);
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
        mTv_execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Order> executeOrders = mOrdersAdapter.getSelectedOrders();
                if (executeOrders == null || executeOrders.isEmpty())
                    return;
                //通知OrderListFragment执行医嘱}
                String id = EventBusUtils.obtainPrivateId(getParentFragment().toString(), CommonConstant.EVENT_INTENTION_EXECUTE_ORDERS);
                EventBusUtils.post(id, executeOrders);
            }
        });
    }

    /**
     * 选中的医嘱列表发生改变，界面做出改变（是否全选状态、是否可以执行）
     */
    private void selectedItemChanged() {
        OrderListFragment parentFragment = (OrderListFragment) getParentFragment();
        if (parentFragment == null)
            return;
        if (!parentFragment.isEditable()) {//如果当前处于不可编辑状态，则不做任何操作
            mLL_bottom.setVisibility(View.GONE);
            return;
        }
        mLL_bottom.setVisibility(View.VISIBLE);
        if (mOrdersAdapter.isSelectedAll()) {//全选状态
            mTv_selectAll.setText(R.string.btn_selectAllCancel);
        } else {
            mTv_selectAll.setText(R.string.btn_selectAll);
        }
        List<Order> selectOrders = mOrdersAdapter.getSelectedOrders();
        if (selectOrders == null || selectOrders.isEmpty()) {
            mTv_execute.setTextColor(ArmsUtils.getColor(mContext, R.color.royalBlueFuzzy));
            mTv_execute.setText(R.string.btn_execute);
        } else {
            String execute = ArmsUtils.getString(mContext, R.string.btn_execute) + "（" + selectOrders.size() + "）";
            mTv_execute.setText(execute);
            mTv_execute.setTextColor(ArmsUtils.getColor(mContext, R.color.bg_royalBlue));
        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (getUserVisibleHint()) {//当Fragment为可见状态的时候，在设置当前编辑状态，否则容易加载缓存界面出错
//            selectedItemChanged();
//        }
//    }

    /**
     * 刷新完成
     */
    public void finishRefresh() {
        if (mRefreshLayout != null)
            mRefreshLayout.finishRefresh();
    }


    /**
     * 返回筛选条件的Adapter
     *
     * @return
     */
    @Override
    public ConditionAdapter conditionAdapter() {
//        if (mConditionAdapter == null)
        mConditionAdapter = new ConditionAdapter(mContext, mConditionList,
                EventBusUtils.obtainPrivateId(this.toString(), CommonConstant.EVENT_INTENTION_CONDITION_CHANGED));
        return mConditionAdapter;
    }

    /**
     * 筛选条件发生改变
     */
    @Override
    public void conditionChanged() {
        //通知OrderListFragment 加载医嘱列表
        String id = EventBusUtils.obtainPrivateId(getParentFragment().toString(), CommonConstant.EVENT_INTENTION_LOAD_ORDER_LIST);
        EventBusUtils.post(id, null);
        //展示条件
        mTv_selectCondition.setText(Conditions.getSelectedConditionString(mConditionList));
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //发送通知，加载数据
        String id = EventBusUtils.obtainPrivateId(getParentFragment().toString(), CommonConstant.EVENT_INTENTION_LOAD_DATA);
        EventBusUtils.post(id, false);
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    /**
     * 更新条件UI
     *
     * @param conditionsList
     */
    private void updateConditionsUI(List<Conditions> conditionsList) {
        mConditionList.clear();
        if (conditionsList != null)
            mConditionList.addAll(conditionsList);
        mConditionAdapter.notifyDataSetChanged();
        mTv_selectCondition.setText(Conditions.getSelectedConditionString(mConditionList));
    }

    /**
     * 更新医嘱UI
     *
     * @param orderList
     */
    private void updateOrdersUI(List<Order> orderList) {
        mOrderList.clear();
        if (orderList != null)
            mOrderList.addAll(orderList);
        mOrdersAdapter.clearDataListSelect();
        if (!mOrderList.isEmpty())
            mScrollView.scrollTo(0, 0);
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
            if (intention.equals(CommonConstant.EVENT_INTENTION_UPDATE_CONDITION)) {//刷新条件UI
                updateConditionsUI((List<Conditions>) event.getMessage());
            } else if (intention.equals(CommonConstant.EVENT_INTENTION_UPDATE_ORDERS)) {//刷新医嘱列表UI
                updateOrdersUI((List<Order>) event.getMessage());
            } else if (intention.equals(CommonConstant.EVENT_INTENTION_EDITABLE_CHANGED)) {//编辑状态发生改变
                boolean editable = (boolean) event.getMessage();
                if (mOrdersAdapter != null)
                    mOrdersAdapter.changeEditable(editable);//调用Adapter改变当前是否可编辑状态
                selectedItemChanged();
            }
        }
    }
}
