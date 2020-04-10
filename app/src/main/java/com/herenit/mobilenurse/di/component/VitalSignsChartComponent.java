package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.di.module.VitalSignsChartModule;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsChartActivity;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsChartContract;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/3/4 15:27
 * desc:生命体征的Dagger
 */
@ActivityScope
@Component(modules = VitalSignsChartModule.class, dependencies = AppComponent.class)
public interface VitalSignsChartComponent {
    void inject(VitalSignsChartActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        VitalSignsChartComponent.Builder view(VitalSignsChartContract.View view);

        VitalSignsChartComponent.Builder appComponent(AppComponent component);

        VitalSignsChartComponent build();
    }
}
