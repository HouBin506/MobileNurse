package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.di.module.AdmAssessModule;
import com.herenit.mobilenurse.mvp.assess.admission_assessment.AdmAssessActivity;
import com.herenit.mobilenurse.mvp.assess.admission_assessment.AdmAssessContract;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/1/11 16:40
 * desc: AdmAssess的dagger桥梁Component
 */
@ActivityScope
@Component(modules = AdmAssessModule.class, dependencies = AppComponent.class)
public interface AdmAssessComponent {
    void inject(AdmAssessActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AdmAssessComponent.Builder view(AdmAssessContract.View view);

        AdmAssessComponent.Builder appComponent(AppComponent component);

        AdmAssessComponent build();
    }
}
