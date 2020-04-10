package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.mvp.setting.SystemSettingsActivity;
import com.herenit.mobilenurse.mvp.setting.SystemSettingsContract;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/1/28 15:04
 * desc: 系统设置的Component
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface SystemSettingsComponent {
    void inject(SystemSettingsActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SystemSettingsComponent.Builder view(SystemSettingsContract.View view);

        SystemSettingsComponent.Builder appComponent(AppComponent component);

        SystemSettingsComponent build();
    }
}
