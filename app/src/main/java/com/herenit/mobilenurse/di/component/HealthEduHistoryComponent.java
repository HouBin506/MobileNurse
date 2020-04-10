package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.di.module.HealthEduHistoryModule;
import com.herenit.mobilenurse.mvp.assess.health_edu.history.HealthEduHistoryActivity;
import com.herenit.mobilenurse.mvp.assess.health_edu.history.HealthEduHistoryContract;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/1/11 16:40
 * desc: 健康宣教dagger桥梁Component
 */
@ActivityScope
@Component(modules = HealthEduHistoryModule.class, dependencies = AppComponent.class)
public interface HealthEduHistoryComponent {
    void inject(HealthEduHistoryActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        HealthEduHistoryComponent.Builder view(HealthEduHistoryContract.View view);

        HealthEduHistoryComponent.Builder appComponent(AppComponent component);

        HealthEduHistoryComponent build();
    }
}
