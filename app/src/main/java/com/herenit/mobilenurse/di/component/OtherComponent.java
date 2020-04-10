package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.di.module.OtherModule;
import com.herenit.mobilenurse.mvp.other.OtherContract;
import com.herenit.mobilenurse.mvp.other.OtherFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/3/4 15:27
 * desc:医嘱列表的Dagger
 */
@FragmentScope
@Component(modules = OtherModule.class,dependencies = AppComponent.class)
public interface OtherComponent {
    void inject(OtherFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        OtherComponent.Builder view(OtherContract.View view);

        OtherComponent.Builder appComponent(AppComponent component);

        OtherComponent build();
    }
}
