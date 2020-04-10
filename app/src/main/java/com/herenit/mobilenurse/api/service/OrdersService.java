package com.herenit.mobilenurse.api.service;

import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.OrderPerformLabel;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.dict.ExecuteResultDict;
import com.herenit.mobilenurse.criteria.entity.dict.OrderClassDict;
import com.herenit.mobilenurse.criteria.entity.submit.OrderListQuery;
import com.herenit.mobilenurse.criteria.entity.submit.OrdersExecute;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * author: HouBin
 * date: 2019/3/6 16:14
 * desc:医嘱接口API
 */
public interface OrdersService {

    /**
     * 查询医嘱类别列表
     *
     * @return
     */
    @GET("mobileNurse/orderHandler/getOrderClassDict")
    Observable<Result<List<OrderClassDict>>> getOrderClassList();

    /**
     * 查询医嘱执行结果字典列表
     *
     * @return
     */
    @GET("mobileNurse/orderHandler/getExecuteResultDict")
    Observable<Result<List<ExecuteResultDict>>> getOrderExecuteResultList();

    /**
     * 获取患者医嘱列表
     *
     * @return
     */
    @POST("mobileNurse/orderHandler/getPerformOrdersBasicInformation")
    Observable<Result<List<Order>>> getPatientOrderList(@Body OrderListQuery query);

    /**
     * 根据医嘱条码，获取对应的要执行的医嘱列表
     *
     * @param patientId 患者ID
     * @param barCode   医嘱码
     * @param type      医嘱码类别
     * @return
     */
    @GET("mobileNurse/orderHandler/getPatientPerformOrdersByBarCode")
    Observable<Result<List<Order>>> getPatientPerformOrdersByBarCode(@Query(CommonConstant.FIELD_NAME_PATIENT_ID) String patientId,
                                                                     @Query(CommonConstant.FIELD_NAME_BAR_CODE) String barCode,
                                                                     @Query(CommonConstant.FIELD_NAME_TYPE) String type);

    /**
     * 执行医嘱
     *
     * @param executeList
     * @return
     */
    @PUT("mobileNurse/orderHandler/performOrders")
    Observable<Result> executeOrders(@Body List<OrdersExecute> executeList);

    /**
     * 医嘱撤回，不管医嘱处于什么状态，将其改变成未执行状态
     *
     * @param ordersExecute
     * @return
     */
    @PUT("mobileNurse/orderHandler/undoPerformOrders")
    Observable<Result> revokeOrders(@Body OrdersExecute ordersExecute);

    @GET("mobileNurse/orderHandler/getPerformLabelDict")
    Observable<Result<List<OrderPerformLabel>>> getOrderPerformLabelList(@Query(CommonConstant.FIELD_NAME_GROUP_CODE) String groupCode);
}
