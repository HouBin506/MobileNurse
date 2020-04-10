package com.herenit.mobilenurse.di.module;

import com.herenit.mobilenurse.mvp.assess.health_edu.HealthEduContract;
import com.herenit.mobilenurse.mvp.assess.health_edu.HealthEduModel;

import dagger.Binds;
import dagger.Module;

/**
 * author: HouBin
 * date: 2019/1/11 16:36
 * desc:健康宣教功能的dagger
 */
@Module
public abstract class HealthEduModule {

    @Binds
    abstract HealthEduContract.Model bindHealthEduModel(HealthEduModel model);

}
