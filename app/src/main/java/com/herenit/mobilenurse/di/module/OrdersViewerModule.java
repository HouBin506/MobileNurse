package com.herenit.mobilenurse.di.module;

import android.app.Activity;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.custom.adapter.ConditionAdapter;
import com.herenit.mobilenurse.custom.adapter.OrdersAdapter;
import com.herenit.mobilenurse.mvp.tool.order.OrdersViewerContract;
import com.herenit.mobilenurse.mvp.tool.order.OrdersViewerModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/3/6 15:54
 * desc:医嘱查看器Dagger
 */
@Module
public abstract class OrdersViewerModule {

    @Binds
    abstract OrdersViewerContract.Model bindOrdersViewerModel(OrdersViewerModel model);

    /**
     * 提供医嘱筛选条件数据
     *
     * @return
     */
    @Provides
    @ActivityScope
    static List<Conditions> provideOrdersConditionList() {
        return new ArrayList<>();
    }

    /**
     * 提供医嘱筛选条件适配器
     *
     * @return
     */
    @Provides
    @ActivityScope
    static ConditionAdapter provideOrdersConditionAdapter(OrdersViewerContract.View activity, List<Conditions> datas) {
        String eventId = EventBusUtils.obtainPrivateId(activity.toString(), CommonConstant.EVENT_INTENTION_CONDITION_CHANGED);
        return new ConditionAdapter((Activity) activity, datas, eventId);
    }

    /**
     * 提供页面医嘱列表数据
     *
     * @return
     */
    @Provides
    @ActivityScope
    static List<Order> provideOrderList() {
        return new ArrayList<>();
    }

    /**
     * 提供页面医嘱列表适配器
     *
     * @return
     */
    @Provides
    @ActivityScope
    static OrdersAdapter provideOrdersAdapter(OrdersViewerContract.View activity, List<Order> datas) {
        return new OrdersAdapter((Activity) activity, datas);
    }
}
