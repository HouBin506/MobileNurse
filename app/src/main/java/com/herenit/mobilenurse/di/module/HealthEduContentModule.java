package com.herenit.mobilenurse.di.module;

import com.herenit.mobilenurse.mvp.assess.health_edu.content.HealthEduContentContract;
import com.herenit.mobilenurse.mvp.assess.health_edu.content.HealthEduContentModel;

import dagger.Binds;
import dagger.Module;

/**
 * author: HouBin
 * date: 2019/1/11 16:36
 * desc:健康宣教功能的dagger
 */
@Module
public abstract class HealthEduContentModule {

    @Binds
    abstract HealthEduContentContract.Model bindHealthEduContentModel(HealthEduContentModel model);
}
