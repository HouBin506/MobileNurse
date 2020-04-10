package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.di.module.PDFViewModule;
import com.herenit.mobilenurse.mvp.tool.PDFViewActivity;
import com.herenit.mobilenurse.mvp.tool.ViewToolContract;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/1/11 16:40
 * desc: 检验报告的dagger桥梁Component
 */
@ActivityScope
@Component(modules = PDFViewModule.class, dependencies = AppComponent.class)
public interface PDFViewComponent {
    void inject(PDFViewActivity fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PDFViewComponent.Builder view(ViewToolContract.View view);

        PDFViewComponent.Builder appComponent(AppComponent component);

        PDFViewComponent build();
    }
}
