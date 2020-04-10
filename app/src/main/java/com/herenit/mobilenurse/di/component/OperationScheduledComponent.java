package com.herenit.mobilenurse.di.component;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.di.module.OperationScheduledModule;
import com.herenit.mobilenurse.mvp.operation.OperationScheduledContract;
import com.herenit.mobilenurse.mvp.operation.OperationScheduledFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * author: HouBin
 * date: 2019/2/21 14:36
 * desc: 床位列表的 dagger Component
 */
@FragmentScope
@Component(modules = OperationScheduledModule.class,dependencies = AppComponent.class)
public interface OperationScheduledComponent {

    void injec(OperationScheduledFragment fragment);

    @Component.Builder
    interface Builder{
        @BindsInstance
        OperationScheduledComponent.Builder view(OperationScheduledContract.View view);

        OperationScheduledComponent.Builder appComponent(AppComponent appComponent);

        OperationScheduledComponent build();
    }
}
