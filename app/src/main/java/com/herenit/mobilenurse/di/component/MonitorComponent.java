package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.di.module.MonitorModule;
import com.herenit.mobilenurse.mvp.monitor.MonitorContract;
import com.herenit.mobilenurse.mvp.monitor.MonitorFragment;
import com.herenit.mobilenurse.mvp.monitor.MonitorPagerFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/3/4 15:27
 * desc:监护仪绑定的Dagger
 */
@FragmentScope
@Component(modules = MonitorModule.class, dependencies = AppComponent.class)
public interface MonitorComponent {
    void inject(MonitorPagerFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MonitorComponent.Builder view(MonitorContract.View view);

        MonitorComponent.Builder appComponent(AppComponent component);

        MonitorComponent build();
    }
}
