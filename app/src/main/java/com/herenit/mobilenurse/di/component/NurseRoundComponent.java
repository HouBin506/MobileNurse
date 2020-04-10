package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.di.module.NurseRoundModule;
import com.herenit.mobilenurse.mvp.nursing_round.NurseRoundContract;
import com.herenit.mobilenurse.mvp.nursing_round.NurseRoundFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/3/4 15:27
 * desc:护理巡视的Dagger
 */
@FragmentScope
@Component(modules = NurseRoundModule.class, dependencies = AppComponent.class)
public interface NurseRoundComponent {
    void inject(NurseRoundFragment activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        NurseRoundComponent.Builder view(NurseRoundContract.View view);

        NurseRoundComponent.Builder appComponent(AppComponent component);

        NurseRoundComponent build();
    }
}
