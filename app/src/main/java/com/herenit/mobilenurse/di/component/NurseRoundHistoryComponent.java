package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.di.module.NurseRoundHistoryModule;
import com.herenit.mobilenurse.mvp.nursing_round.NurseRoundHistoryActivity;
import com.herenit.mobilenurse.mvp.nursing_round.NurseRoundHistoryContract;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/3/4 15:27
 * desc:生命体征的Dagger
 */
@ActivityScope
@Component(modules = NurseRoundHistoryModule.class, dependencies = AppComponent.class)
public interface NurseRoundHistoryComponent {
    void inject(NurseRoundHistoryActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        NurseRoundHistoryComponent.Builder view(NurseRoundHistoryContract.View view);

        NurseRoundHistoryComponent.Builder appComponent(AppComponent component);

        NurseRoundHistoryComponent build();
    }
}
