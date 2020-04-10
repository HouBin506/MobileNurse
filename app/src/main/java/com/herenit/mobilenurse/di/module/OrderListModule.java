package com.herenit.mobilenurse.di.module;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.OrderPerformLabel;
import com.herenit.mobilenurse.mvp.orders.OrderListContract;
import com.herenit.mobilenurse.mvp.orders.OrderListModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/3/6 15:54
 * desc:医嘱列表的Dagger
 */
@Module
public abstract class OrderListModule {

    @Binds
    abstract OrderListContract.Model bindOrderListModel(OrderListModel model);

    /**
     * 提供筛选条件列表
     *
     * @return
     */
    @FragmentScope
    @Provides
    static List<Conditions> provideOrderConditionList() {
        return new ArrayList<>();
    }

    /**
     * 提供医嘱列表
     *
     * @return
     */
    @FragmentScope
    @Provides
    static List<Order> provideOrderList() {
        return new ArrayList<>();
    }


}
