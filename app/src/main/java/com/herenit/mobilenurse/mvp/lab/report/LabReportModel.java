package com.herenit.mobilenurse.mvp.lab.report;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.ExamAndLabService;
import com.herenit.mobilenurse.criteria.entity.lab.CommonLabReport;
import com.herenit.mobilenurse.criteria.entity.lab.MicroorganismLabReport;
import com.herenit.mobilenurse.criteria.entity.Result;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/8/15 10:52
 * desc:检验报告M层
 */
@FragmentScope
public class LabReportModel extends BaseModel implements LabReportContract.Model {

    @Inject
    public LabReportModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<CommonLabReport>>> getLabReportList(String patientId, String visitId) {
        return mRepositoryManager.obtainRetrofitService(ExamAndLabService.class)
                .getLabReportList(patientId, visitId);
    }

    @Override
    public Observable<Result<List<MicroorganismLabReport>>> getMicroorganismLabReportList(String patientId, String visitId) {
        return mRepositoryManager.obtainRetrofitService(ExamAndLabService.class)
                .getMicroorganismLabReportList(patientId, visitId);
    }
}
