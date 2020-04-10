package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.di.module.SinglePatientModule;
import com.herenit.mobilenurse.mvp.main.SinglePatientActivity;
import com.herenit.mobilenurse.mvp.main.SinglePatientContract;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/3/1 14:40
 * desc: 单患者模块的Dagger
 */
@ActivityScope
@Component(modules = SinglePatientModule.class, dependencies = AppComponent.class)
public interface SinglePatientComponent {
    void inject(SinglePatientActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SinglePatientComponent.Builder view(SinglePatientContract.View view);

        SinglePatientComponent.Builder appComponent(AppComponent component);

        SinglePatientComponent build();
    }
}
