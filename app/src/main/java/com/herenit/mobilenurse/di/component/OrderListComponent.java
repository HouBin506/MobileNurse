package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.di.module.OrderListModule;
import com.herenit.mobilenurse.mvp.orders.OrderListContract;
import com.herenit.mobilenurse.mvp.orders.OrderListFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/3/4 15:27
 * desc:医嘱列表的Dagger
 */
@FragmentScope
@Component(modules = OrderListModule.class,dependencies = AppComponent.class)
public interface OrderListComponent {
    void inject(OrderListFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        OrderListComponent.Builder view(OrderListContract.View view);

        OrderListComponent.Builder appComponent(AppComponent component);

        OrderListComponent build();
    }
}
