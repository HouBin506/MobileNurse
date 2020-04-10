package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.di.module.HealthEduContentModule;
import com.herenit.mobilenurse.mvp.assess.health_edu.content.HealthEduContentActivity;
import com.herenit.mobilenurse.mvp.assess.health_edu.content.HealthEduContentContract;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/1/11 16:40
 * desc: 检查报告的dagger桥梁Component
 */
@ActivityScope
@Component(modules = HealthEduContentModule.class, dependencies = AppComponent.class)
public interface HealthEduContentComponent {
    void inject(HealthEduContentActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        HealthEduContentComponent.Builder view(HealthEduContentContract.View view);

        HealthEduContentComponent.Builder appComponent(AppComponent component);

        HealthEduContentComponent build();
    }
}
