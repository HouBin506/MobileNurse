package com.herenit.mobilenurse.api.service;

import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.lab.CommonLabReport;
import com.herenit.mobilenurse.criteria.entity.ExamReport;
import com.herenit.mobilenurse.criteria.entity.lab.MicroorganismLabReport;
import com.herenit.mobilenurse.criteria.entity.Result;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * author: HouBin
 * date: 2019/8/28 14:55
 * desc:检查检验相关API
 */
public interface ExamAndLabService {

    /**
     * 获取检查列表
     *
     * @param patientId
     * @param visitId
     * @return
     */
    @GET("mobileNurse/examHandler/getExamBasicInformation")
    Observable<Result<List<ExamReport>>> getExamReportList(@Query(CommonConstant.FIELD_NAME_PATIENT_ID) String patientId,
                                                           @Query(CommonConstant.FIELD_NAME_VISIT_ID) String visitId);

    /**
     * 获取检验列表
     *
     * @param patientId
     * @param visitId
     * @return
     */
    @GET("mobileNurse/labHandler/getLabBasicInformation")
    Observable<Result<List<CommonLabReport>>> getLabReportList(@Query(CommonConstant.FIELD_NAME_PATIENT_ID) String patientId,
                                                               @Query(CommonConstant.FIELD_NAME_VISIT_ID) String visitId);

    /**
     * 获取微生物检验列表
     *
     * @param patientId
     * @param visitId
     * @return
     */
    @GET("mobileNurse/labHandler/getBioLabBasicInformation")
    Observable<Result<List<MicroorganismLabReport>>> getMicroorganismLabReportList(@Query(CommonConstant.FIELD_NAME_PATIENT_ID) String patientId,
                                                                                   @Query(CommonConstant.FIELD_NAME_VISIT_ID) String visitId);
}
