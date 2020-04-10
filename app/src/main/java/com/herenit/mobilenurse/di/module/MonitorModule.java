package com.herenit.mobilenurse.di.module;

import com.herenit.mobilenurse.mvp.monitor.MonitorContract;
import com.herenit.mobilenurse.mvp.monitor.MonitorModel;

import dagger.Binds;
import dagger.Module;

/**
 * author: HouBin
 * date: 2019/3/4 15:25
 * desc:监护仪绑定的Dagger
 */
@Module
public abstract class MonitorModule {

    @Binds
    abstract MonitorContract.Model bindMonitorModel(MonitorModel model);
}
