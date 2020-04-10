package com.herenit.mobilenurse.api.service;

import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.AssessModel;
import com.herenit.mobilenurse.criteria.entity.HealthEduAssessModel;
import com.herenit.mobilenurse.criteria.entity.HealthEduHistoryItem;
import com.herenit.mobilenurse.criteria.entity.MultiListMenuItem;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.HealthEduAssessParam;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * author: HouBin
 * date: 2019/9/20 9:05
 * desc: 健康教育Api
 */
public interface HealthEduService {

    /**
     * 获取健康教育项目列表样式
     *
     * @return
     */
    @GET("mobileNurse/healthEduHandler/getHealthEduItemStyle")
    Observable<Result<List<MultiListMenuItem>>> getHealthEduItemStyle();

    /**
     * 获取健康教育结果列表样式
     *
     * @return
     */
    @GET("mobileNurse/healthEduHandler/getHealthEduResultStyleAndRecord")
    Observable<Result<HealthEduAssessModel>> getHealthEduResult(@Query(CommonConstant.FIELD_NAME_DOC_ID) String docId);

    /**
     * 提交保存健康宣教数据
     *
     * @param param
     * @return
     */
    @POST("mobileNurse/healthEduHandler/saveOrUpdateHealthEduRecord")
    Observable<Result> saveOrUpdateHealthEduContent(@Body HealthEduAssessParam param);

    /**
     * 获取健康教育历史记录列表
     *
     * @param patientId
     * @param visitId
     * @return
     */
    @GET("mobileNurse/healthEduHandler/getHealthEduRecordList")
    Observable<Result<List<HealthEduHistoryItem>>> getHealthEduHistoryList(@Query(CommonConstant.FIELD_NAME_PATIENT_ID) String patientId,
                                                                           @Query(CommonConstant.FIELD_NAME_VISIT_ID) String visitId);

    /**
     * 删除健康教育历史记录
     *
     * @param docId
     * @return
     */
    @DELETE("mobileNurse/healthEduHandler/deleteHealthEduRecord")
    Observable<Result> deleteHealthEduHistory(@Query(CommonConstant.FIELD_NAME_DOC_ID) String docId);
}
