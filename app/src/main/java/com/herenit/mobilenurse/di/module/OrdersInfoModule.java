package com.herenit.mobilenurse.di.module;

import android.app.Activity;
import android.content.Context;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.view.ImageText;
import com.herenit.mobilenurse.criteria.entity.view.RvController;
import com.herenit.mobilenurse.criteria.enums.ExecuteClassEnum;
import com.herenit.mobilenurse.custom.adapter.CommonImageTextAdapter;
import com.herenit.mobilenurse.custom.adapter.OrdersInfoAdapter;
import com.herenit.mobilenurse.mvp.orders.OrdersInfoContract;
import com.herenit.mobilenurse.mvp.orders.OrdersInfoModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/3/6 15:54
 * desc:医嘱详情的Dagger
 */
@Module
public abstract class OrdersInfoModule {

    @Binds
    abstract OrdersInfoContract.Model bindOrdersInfoModel(OrdersInfoModel model);

    /**
     * 提供当前页面要显示的单组医嘱列表
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<Order> provideGroupOrders() {
        return new ArrayList<>();
    }

    /**
     * 提供医嘱相亲信息列表数据
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<RvController> provideOrdersInfoItemEditList() {
        return new ArrayList<>();
    }

    /**
     * 提供医嘱详情界面中医嘱列表+医嘱信息的列表Adapter
     *
     * @param activity
     * @return
     */
    @ActivityScope
    @Provides
    static OrdersInfoAdapter provideOrdersInfoAdapter(OrdersInfoContract.View activity) {
        return new OrdersInfoAdapter((Context) activity, new ArrayList<>());
    }

    /**
     * 提供医嘱执行的操作类别列表，通过选择列表中的某一选项，对该组医嘱做出对应操作
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<ImageText> provideExecuteClassList() {
        return new ArrayList<>();
    }
    /**
     * 提供医嘱执行的操作类别列表，通过选择列表中的某一选项，对该组医嘱做出对应操作
     *
     * @return
     */
    @ActivityScope
    @Provides
    static CommonImageTextAdapter provideExecuteClassAdapter(OrdersInfoContract.View activity, List<ImageText> datas) {
        return new CommonImageTextAdapter((Activity) activity, datas);
    }
}
