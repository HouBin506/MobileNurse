package com.herenit.mobilenurse.api.service;

import com.herenit.mobilenurse.criteria.entity.NurseRoundItem;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.CommonPatientItemQuery;
import com.herenit.mobilenurse.criteria.entity.submit.OrderListQuery;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewGroup;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

/**
 * author: HouBin
 * date: 2019/8/8 17:06
 * desc:护理巡视API接口
 */
public interface NurseRoundService {

    /**
     * 获取护理巡视列表
     *
     * @param query
     * @return
     */
    @POST("mobileNurse/patrolHandler/getPatrolDict")
    Observable<Result<NurseRoundViewGroup>> getNurseRoundItemList(@Body OrderListQuery query);

    /**
     * 提交护理巡视数据
     *
     * @param commitRoundList
     * @return
     */
    @POST("mobileNurse/patrolHandler/enterPatrol")
    Observable<Result> commitNurseRoundDataList(@Body List<NurseRoundItem> commitRoundList);

    /**
     * 删除护理巡视数据
     *
     * @param deleteData
     * @return
     */
    //TODO 此处需要注意，Retrofit框架，使用 @DELETE时候，不支持使用@Body，如果需要传递Body，需要自定义注解
//    @DELETE("mobileNurse/patrolHandler/deletePatrol")
    @HTTP(method = "DELETE", path = "mobileNurse/patrolHandler/deletePatrol", hasBody = true)
    Observable<Result> deleteNurseRoundData(@Body List<NurseRoundItem> deleteData);

    /**
     * 获取护理巡视历史数据
     *
     * @param query 查询条件
     * @return
     */
    @POST("mobileNurse/patrolHandler/getPatrol")
    Observable<Result<List<NurseRoundItem>>> getNurseRoundHistory(@Body CommonPatientItemQuery query);


}
