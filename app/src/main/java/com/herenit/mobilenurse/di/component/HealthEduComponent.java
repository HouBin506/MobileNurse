package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.di.module.HealthEduModule;
import com.herenit.mobilenurse.mvp.assess.health_edu.HealthEduContract;
import com.herenit.mobilenurse.mvp.assess.health_edu.HealthEduPagerFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/1/11 16:40
 * desc: 检查报告的dagger桥梁Component
 */
@FragmentScope
@Component(modules = HealthEduModule.class, dependencies = AppComponent.class)
public interface HealthEduComponent {
    void inject(HealthEduPagerFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        HealthEduComponent.Builder view(HealthEduContract.View view);

        HealthEduComponent.Builder appComponent(AppComponent component);

        HealthEduComponent build();
    }
}
