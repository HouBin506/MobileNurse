package com.herenit.mobilenurse.api.service;

import com.herenit.mobilenurse.criteria.entity.OperationScheduled;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.OperationScheduledQuery;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * author: HouBin
 * date: 2019/5/23 11:24
 * desc: 手术相关的API
 */
public interface OperationService {

    /**
     * 获取手术安排列表
     *
     * @param query
     * @return
     */
    @POST("mobileNurse/operationHandler/getOperationBasicInformation")
    Observable<Result<List<OperationScheduled>>> loadOperationScheduledList(@Body OperationScheduledQuery query);
}
