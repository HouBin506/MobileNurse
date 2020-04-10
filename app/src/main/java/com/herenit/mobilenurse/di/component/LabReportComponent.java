package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.di.module.LabReportModule;
import com.herenit.mobilenurse.mvp.lab.report.LabReportContract;
import com.herenit.mobilenurse.mvp.lab.report.LabReportPagerFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/1/11 16:40
 * desc: 检验报告的dagger桥梁Component
 */
@FragmentScope
@Component(modules = LabReportModule.class, dependencies = AppComponent.class)
public interface LabReportComponent {
    void inject(LabReportPagerFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LabReportComponent.Builder view(LabReportContract.View view);

        LabReportComponent.Builder appComponent(AppComponent component);

        LabReportComponent build();
    }
}
