package com.herenit.mobilenurse.api.service;

import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.MonitorBind;
import com.herenit.mobilenurse.criteria.entity.Result;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 监护仪Api
 */
public interface MonitorService {

    /**
     * 获取患者监护仪绑定信息
     *
     * @param visitNo
     * @return
     */
    @GET("mobileNurse//monitorHandler/getMonitorInformation")
    Observable<Result<MonitorBind>> loadMonitorInfo(@Query(CommonConstant.FIELD_NAME_VISIT_NO) String visitNo);

    /**
     * 监护仪绑定之前的核对
     *
     * @param monitor
     * @return
     */
    @POST("mobileNurse/monitorHandler/checkMonitor")
    Observable<Result<MonitorBind>> monitorBindVerify(@Body MonitorBind monitor);

    /**
     * 监护仪绑定（绑定、解绑、先解绑后绑定）
     *
     * @param monitor
     * @return
     */
    @POST("mobileNurse/monitorHandler/bindMonitor")
    Observable<Result> monitorBind(@Body MonitorBind monitor);
}
