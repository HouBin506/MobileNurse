package com.herenit.mobilenurse.api.service;

import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.submit.VitalSignsHistoryQuery;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsViewItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * author: HouBin
 * date: 2019/5/15 11:05
 * desc:体征Api
 */
public interface VitalSignsService {
    /**
     * 获取体征界面显示的项目
     *
     * @param groupCode
     * @return
     */
    @GET("mobileNurse/vitalSignsHandler/getVitalSignsDictByDeptCode")
    Observable<Result<List<VitalSignsViewItem>>> getPatientVitalSignsList(@Query(CommonConstant.FIELD_NAME_GROUP_CODE) String groupCode);

    /**
     * 提交体征记录数据
     *
     * @param vitalSignsList
     * @return
     */
    @POST("mobileNurse/vitalSignsHandler/enterVitalSigns")
    Observable<Result> postVitalSignsRecord(@Body List<VitalSignsItem> vitalSignsList);

    /**
     * 更新体征历史数据
     *
     * @param vitalSignsList
     * @return
     */
    @PUT("mobileNurse/vitalSignsHandler/updateVitalSigns")
    Observable<Result> updateVitalSigns(@Body List<VitalSignsItem> vitalSignsList);

    /**
     * 删除体征历史数据
     *
     * @param vitalSignsList
     * @return
     */
    //TODO 此处需要注意，Retrofit框架，使用 @DELETE时候，不支持使用@Body，如果需要传递Body，需要自定义注解
//    @DELETE("mobileNurse/vitalSignsHandler/deleteVitalSigns")
    @HTTP(method = "DELETE", path = "mobileNurse/vitalSignsHandler/deleteVitalSigns", hasBody = true)
    Observable<Result> deleteVitalSigns(@Body List<VitalSignsItem> vitalSignsList);

    /**
     * 获取体征历史记录
     *
     * @param query
     * @return
     */
    @POST("mobileNurse/vitalSignsHandler/getVitalSigns")
    Observable<Result<List<VitalSignsItem>>> getVitalSignsHistoryList(@Body VitalSignsHistoryQuery query);
}
