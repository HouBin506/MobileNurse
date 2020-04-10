package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.di.module.VitalSignsRecordModule;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsRecordContract;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsRecordPagerFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/3/4 15:27
 * desc:生命体征的Dagger
 */
@FragmentScope
@Component(modules = VitalSignsRecordModule.class,dependencies = AppComponent.class)
public interface VitalSignsRecordComponent {
    void inject(VitalSignsRecordPagerFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        VitalSignsRecordComponent.Builder view(VitalSignsRecordContract.View view);

        VitalSignsRecordComponent.Builder appComponent(AppComponent component);

        VitalSignsRecordComponent build();
    }
}
