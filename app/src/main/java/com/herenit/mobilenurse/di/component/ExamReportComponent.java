package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.di.module.ExamReportModule;
import com.herenit.mobilenurse.mvp.exam.report.ExamReportActivity;
import com.herenit.mobilenurse.mvp.exam.report.ExamReportContract;
import com.herenit.mobilenurse.mvp.exam.report.ExamReportPagerFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/1/11 16:40
 * desc: 检查报告的dagger桥梁Component
 */
@FragmentScope
@Component(modules = ExamReportModule.class, dependencies = AppComponent.class)
public interface ExamReportComponent {
    void inject(ExamReportPagerFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ExamReportComponent.Builder view(ExamReportContract.View view);

        ExamReportComponent.Builder appComponent(AppComponent component);

        ExamReportComponent build();
    }
}
