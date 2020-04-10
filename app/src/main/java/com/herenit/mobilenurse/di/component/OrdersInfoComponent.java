package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.di.module.OrdersInfoModule;
import com.herenit.mobilenurse.mvp.orders.OrdersInfoActivity;
import com.herenit.mobilenurse.mvp.orders.OrdersInfoContract;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/3/4 15:27
 * desc:医嘱详情的Dagger
 */
@ActivityScope
@Component(modules = OrdersInfoModule.class,dependencies = AppComponent.class)
public interface OrdersInfoComponent {
    void inject(OrdersInfoActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        OrdersInfoComponent.Builder view(OrdersInfoContract.View view);

        OrdersInfoComponent.Builder appComponent(AppComponent component);

        OrdersInfoComponent build();
    }
}
