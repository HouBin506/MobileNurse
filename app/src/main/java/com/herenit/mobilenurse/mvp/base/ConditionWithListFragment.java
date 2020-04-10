package com.herenit.mobilenurse.mvp.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.herenit.arms.mvp.BasePresenter;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.adapter.ConditionAdapter;
import com.herenit.mobilenurse.custom.listener.OnScrollListener;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.custom.widget.layout.MNScrollView;
import com.herenit.mobilenurse.custom.widget.layout.SimulateListView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * author: HouBin
 * date: 2019/2/26 13:58
 * desc: 封装了具条件+数据列表的页面的一些公共部分和基本功能，常用于
 * 床位列表、医嘱列表之类的可根据条件筛选显示数据列表
 * 使用时Fragment需要集成ConditionWithListFragment，同时布局文件应该为 include_list_with_condition
 */
public abstract class ConditionWithListFragment<P extends BasePresenter> extends BasicCommonFragment<P> {

    @BindView(R2.id.sv_listWithCondition_scrollView)
    protected MNScrollView mScrollView;//滚动布局，该布局包裹了条件列表和数据列表
    /**
     * 显示条件的列表的ListView
     */
    @BindView(R2.id.lv_listWithCondition_condition)
    protected SimulateListView mLv_condition;
    @BindView(R2.id.rv_listWithCondition_data)
    protected RecyclerView mRecyclerView;//展示数据的列表View

    @BindView(R2.id.tv_sickbedList_selectedCondition)
    protected TextView mTv_selectCondition;

    private View mContentView;

    protected LoadingDialog mLoadingDialog;


    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mContentView = inflater.inflate(layoutId(), container, false);
        return mContentView;
    }

    public void setConditionScroll() {

        mScrollView.setOnScrollListener(new OnScrollListener() {
            /**
             *
             * @param view    正在发生滑动的View
             * @param originX 截止当前 view所处的坐标位置x轴的值
             * @param originY 截止当前 view所处的坐标位置y轴的值
             */
            @Override
            public void onScroll(View view, int originX, int originY) {
                int topHeight = mLv_condition.getMeasuredHeight() + (getResources().getDimensionPixelOffset(R.dimen.view_horizontal_margin_parent) * 1);
                if (originY <= 0) {//处于最顶端
                    mTv_selectCondition.setVisibility(View.GONE);
                    mTv_selectCondition.setBackgroundColor(Color.argb((int) 0, 128, 214, 64));
                } else if (topHeight >= originY) {//顶端布局可见
                    mTv_selectCondition.setVisibility(View.VISIBLE);
                    float scale = (float) originY / topHeight;
                    float alpha = (255 * scale);
                    mTv_selectCondition.setTextColor(Color.argb((int) alpha, 255, 255, 255));
                    mTv_selectCondition.setBackgroundColor(Color.argb((int) alpha, 128, 214, 64));
                } else {//顶端布局可见
                    mTv_selectCondition.setVisibility(View.VISIBLE);
                    mTv_selectCondition.setBackgroundColor(Color.argb((int) 255, 128, 214, 64));
                }
            }
        });

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mLv_condition.setAdapter(conditionAdapter());
        mLoadingDialog = createLoadingDialog();
        initView(mContentView);
        setConditionScroll();
    }

    /**
     * 返回页面布局
     *
     * @return
     */
    @LayoutRes
    protected abstract int layoutId();

    /**
     * 在{@link #initView(LayoutInflater, ViewGroup, Bundle)}
     * return之前，会先调用{@link #initView(View)}
     * 这里可以做一些页面UI的初始化工作
     *
     * @param contentView
     */
    protected abstract void initView(View contentView);

    /**
     * 提供显示条件的Adapter
     *
     * @return
     */
    public abstract ConditionAdapter conditionAdapter();

    /**
     * 筛选条件发生改变，此时需要根据新的条件去查询、显示数据
     */
    public abstract void conditionChanged();


    @Subscribe
    public void onEventReceived(@NonNull Event event) {
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (!TextUtils.isEmpty(intention)) {//私有事件消费
            if (intention.equals(CommonConstant.EVENT_INTENTION_CONDITION_CHANGED)) {//筛选条件发生改变
                conditionChanged();
            }
        }
    }
}
