package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.di.module.VitalSignsHistoryModule;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsHistoryActivity;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsHistoryContract;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/3/4 15:27
 * desc:生命体征的Dagger
 */
@ActivityScope
@Component(modules = VitalSignsHistoryModule.class, dependencies = AppComponent.class)
public interface VitalSignsHistoryComponent {
    void inject(VitalSignsHistoryActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        VitalSignsHistoryComponent.Builder view(VitalSignsHistoryContract.View view);

        VitalSignsHistoryComponent.Builder appComponent(AppComponent component);

        VitalSignsHistoryComponent build();
    }
}
