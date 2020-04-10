package com.herenit.mobilenurse.di.module;

import com.herenit.mobilenurse.mvp.patient.PatientInfoContract;
import com.herenit.mobilenurse.mvp.patient.PatientInfoModel;

import dagger.Binds;
import dagger.Module;

/**
 * author: HouBin
 * date: 2019/3/4 15:25
 * desc:患者详情的Dagger
 */
@Module
public abstract class PatientInfoModule {

    @Binds
    abstract PatientInfoContract.Model bindPatientInfoModel(PatientInfoModel model);
}
