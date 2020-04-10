package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.di.module.OrdersViewerModule;
import com.herenit.mobilenurse.mvp.tool.order.OrdersViewerActivity;
import com.herenit.mobilenurse.mvp.tool.order.OrdersViewerContract;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/3/4 15:27
 * desc:医嘱详情的Dagger
 */
@ActivityScope
@Component(modules = OrdersViewerModule.class, dependencies = AppComponent.class)
public interface OrdersViewerComponent {
    void inject(OrdersViewerActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        OrdersViewerComponent.Builder view(OrdersViewerContract.View view);

        OrdersViewerComponent.Builder appComponent(AppComponent component);

        OrdersViewerComponent build();
    }
}
