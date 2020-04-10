package com.herenit.mobilenurse.mvp.assess.admission_assessment;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.AssessService;
import com.herenit.mobilenurse.criteria.entity.AssessModel;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.AssessParam;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/8/15 10:52
 * desc:入院评估M层
 */
@ActivityScope
public class AdmAssessModel extends BaseModel implements AdmAssessContract.Model {

    @Inject
    public AdmAssessModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    /**
     * 获取入院评估数据
     *
     * @param patientId
     * @param visitId
     * @return
     */
    @Override
    public Observable<Result<AssessModel>> getAdmissionAssessModel(String patientId, String visitId) {
        return mRepositoryManager.obtainRetrofitService(AssessService.class)
                .getAdmissionAssessModel(patientId, visitId);
    }

    /**
     * 提交入院评估数据
     *
     * @param param
     * @return
     */
    @Override
    public Observable<Result> commitAdmissionAssessData(AssessParam param) {
        return mRepositoryManager.obtainRetrofitService(AssessService.class)
                .commitAdmissionAssessData(param);
    }
}
