package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.di.module.MultiPatientModule;
import com.herenit.mobilenurse.mvp.main.MultiPatientActivity;
import com.herenit.mobilenurse.mvp.main.MultiPatientContract;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/1/28 15:04
 * desc: 系统设置的Component
 */
@ActivityScope
@Component(modules = MultiPatientModule.class, dependencies = AppComponent.class)
public interface MultiPatientComponent {
    void inject(MultiPatientActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MultiPatientComponent.Builder view(MultiPatientContract.View view);

        MultiPatientComponent.Builder appComponent(AppComponent component);

        MultiPatientComponent build();
    }
}
