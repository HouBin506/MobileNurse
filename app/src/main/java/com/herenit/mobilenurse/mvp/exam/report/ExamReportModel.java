package com.herenit.mobilenurse.mvp.exam.report;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.ExamAndLabService;
import com.herenit.mobilenurse.criteria.entity.ExamReport;
import com.herenit.mobilenurse.criteria.entity.Result;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/8/15 10:52
 * desc:检查报告M层
 */
@FragmentScope
public class ExamReportModel extends BaseModel implements ExamReportContract.Model {

    @Inject
    public ExamReportModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<ExamReport>>> getExamReportList(String patientId, String visitId) {
        return mRepositoryManager.obtainRetrofitService(ExamAndLabService.class)
                .getExamReportList(patientId, visitId);
    }
}
