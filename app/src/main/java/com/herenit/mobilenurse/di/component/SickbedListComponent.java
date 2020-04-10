package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.di.module.SickbedListModule;
import com.herenit.mobilenurse.mvp.sickbed.SickbedListContract;
import com.herenit.mobilenurse.mvp.sickbed.SickbedListFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/2/21 14:36
 * desc: 床位列表的 dagger Component
 */
@FragmentScope
@Component(modules = SickbedListModule.class,dependencies = AppComponent.class)
public interface SickbedListComponent {

    void injec(SickbedListFragment fragment);

    @Component.Builder
    interface Builder{
        @BindsInstance
        SickbedListComponent.Builder view(SickbedListContract.View view);

        SickbedListComponent.Builder appComponent(AppComponent appComponent);

        SickbedListComponent build();
    }
}
