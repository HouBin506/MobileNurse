package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.di.module.LoadingModule;
import com.herenit.mobilenurse.mvp.launch.LoadingContract;
import com.herenit.mobilenurse.mvp.launch.LoadingActivity;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/1/11 16:40
 * desc: Loading的dagger桥梁Component
 */
@ActivityScope
@Component(modules = LoadingModule.class, dependencies = AppComponent.class)
public interface LoadingComponent {
    void inject(LoadingActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LoadingComponent.Builder view(LoadingContract.View view);

        LoadingComponent.Builder appComponent(AppComponent component);

        LoadingComponent build();
    }
}
