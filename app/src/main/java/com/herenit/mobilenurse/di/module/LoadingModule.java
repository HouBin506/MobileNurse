package com.herenit.mobilenurse.di.module;

import android.support.v4.app.FragmentActivity;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.mvp.launch.LoadingContract;
import com.herenit.mobilenurse.mvp.launch.LoadingModel;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/1/11 16:36
 * desc:
 */
@Module
public abstract class LoadingModule {

    @Binds
    abstract LoadingContract.Model bindLoadingModel(LoadingModel model);

    @Provides
    @ActivityScope
    static RxPermissions provideRxPermissions(LoadingContract.View activity) {
        return new RxPermissions((FragmentActivity) activity);
    }
}
