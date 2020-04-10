package com.herenit.mobilenurse.api.service;

import com.herenit.mobilenurse.criteria.entity.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * author: HouBin
 * date: 2019/2/20 15:14
 * desc: 常用的公用的API
 */
public interface CommonService {
    /**
     * 获取服务器端时间
     *
     * @return
     */
    @GET("common/systemHandler/getSystemTime")
    Observable<Result<Long>> getServerTime();

}
