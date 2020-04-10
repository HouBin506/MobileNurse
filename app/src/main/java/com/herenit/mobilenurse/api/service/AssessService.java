package com.herenit.mobilenurse.api.service;

import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.AssessModel;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.AssessParam;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * author: HouBin
 * date: 2019/8/19 14:30
 * desc:评估功能API
 */
public interface AssessService {

    /**
     * 获取入院评估数据（界面配置+历史数据）
     *
     * @param patientId
     * @param visitId
     * @return
     */
    @GET("mobileNurse/admissionAssessmentHandler/getAdmissionAssessmentStyleAndRecord")
    Observable<Result<AssessModel>> getAdmissionAssessModel(@Query(CommonConstant.FIELD_NAME_PATIENT_ID) String patientId,
                                                            @Query(CommonConstant.FIELD_NAME_VISIT_ID) String visitId);

    @POST("mobileNurse/admissionAssessmentHandler/saveOrUpdateAdmissionAssessmentRecord")
    Observable<Result> commitAdmissionAssessData(@Body AssessParam param);
}
