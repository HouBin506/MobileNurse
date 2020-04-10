package com.herenit.mobilenurse.di.module;

import com.herenit.mobilenurse.mvp.assess.admission_assessment.AdmAssessContract;
import com.herenit.mobilenurse.mvp.assess.admission_assessment.AdmAssessModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/1/11 16:36
 * desc:
 */
@Module
public abstract class AdmAssessModule {

    @Binds
    abstract AdmAssessContract.Model bindAdmAssessModel(AdmAssessModel model);

}
