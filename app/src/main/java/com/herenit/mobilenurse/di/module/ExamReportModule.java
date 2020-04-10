package com.herenit.mobilenurse.di.module;

import android.support.v4.app.Fragment;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.criteria.entity.ExamReport;
import com.herenit.mobilenurse.custom.adapter.ExamReportAdapter;
import com.herenit.mobilenurse.mvp.exam.report.ExamReportContract;
import com.herenit.mobilenurse.mvp.exam.report.ExamReportModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/1/11 16:36
 * desc:检查报告功能的dagger
 */
@Module
public abstract class ExamReportModule {

    @Binds
    abstract ExamReportContract.Model bindExamReportModel(ExamReportModel model);

    /**
     * 提供检查列表数据
     *
     * @return
     */
    @FragmentScope
    @Provides
    static List<ExamReport> provideExamReportList() {
        return new ArrayList<>();
    }

    /**
     * 提供检查列表Adapter
     *
     * @param fragment
     * @param datas
     * @return
     */
    @FragmentScope
    @Provides
    static ExamReportAdapter provideExamReportAdapter(ExamReportContract.View fragment, List<ExamReport> datas) {
        return new ExamReportAdapter(((Fragment) fragment).getContext(), datas);
    }


}
