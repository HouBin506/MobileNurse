package com.herenit.mobilenurse.di.module;

import android.support.v4.app.Fragment;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.criteria.entity.lab.CommonLabReport;
import com.herenit.mobilenurse.criteria.entity.lab.MicroorganismLabReport;
import com.herenit.mobilenurse.custom.adapter.LabReportAdapter;
import com.herenit.mobilenurse.custom.adapter.delegate.MicroorganismLabReportAdapter;
import com.herenit.mobilenurse.mvp.lab.report.LabReportContract;
import com.herenit.mobilenurse.mvp.lab.report.LabReportModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/1/11 16:36
 * desc:检验报告功能的dagger
 */
@Module
public abstract class LabReportModule {

    @Binds
    abstract LabReportContract.Model bindLabReportModel(LabReportModel model);

    /**
     * 提供检验列表数据
     *
     * @return
     */
    @FragmentScope
    @Provides
    static List<CommonLabReport> provideLabReportList() {
        return new ArrayList<>();
    }

    /**
     * 提供检验列表Adapter
     *
     * @param fragment
     * @param datas
     * @return
     */
    @FragmentScope
    @Provides
    static LabReportAdapter provideLabReportAdapter(LabReportContract.View fragment, List<CommonLabReport> datas) {
        return new LabReportAdapter(((Fragment) fragment).getContext(), datas);
    }


    /**
     * 提供微生物检验列表数据
     *
     * @return
     */
    @FragmentScope
    @Provides
    static List<MicroorganismLabReport> provideMicroorganismLabReportList() {
        return new ArrayList<>();
    }

    /**
     * 提供微生物检验列表Adapter
     *
     * @param fragment
     * @param datas
     * @return
     */
    @FragmentScope
    @Provides
    static MicroorganismLabReportAdapter provideMicroorganismLabReportAdapter(LabReportContract.View fragment, List<MicroorganismLabReport> datas) {
        return new MicroorganismLabReportAdapter(((Fragment) fragment).getContext(), datas);
    }
}
