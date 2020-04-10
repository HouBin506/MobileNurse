package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.di.module.PatientInfoModule;
import com.herenit.mobilenurse.mvp.patient.PatientInfoContract;
import com.herenit.mobilenurse.mvp.patient.PatientInfoFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/3/4 15:27
 * desc:患者详情的Dagger
 */
@FragmentScope
@Component(modules = PatientInfoModule.class,dependencies = AppComponent.class)
public interface PatientInfoComponent {
    void inject(PatientInfoFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PatientInfoComponent.Builder view(PatientInfoContract.View view);

        PatientInfoComponent.Builder appComponent(AppComponent component);

        PatientInfoComponent build();
    }
}
