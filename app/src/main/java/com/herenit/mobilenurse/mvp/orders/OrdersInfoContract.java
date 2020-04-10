package com.herenit.mobilenurse.mvp.orders;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.OrdersExecute;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/3/12 14:46
 * desc: 医嘱详情的Contract
 */
public interface OrdersInfoContract {

    interface Model extends IModel {
        /**
         * 网络获取患者详情
         *
         * @param patientId
         * @param groupCode
         * @return
         */
        Observable<Result<PatientInfo>> getPatientInfoByNetwork(String patientId, String groupCode);

        /**
         * 缓存中获取患者详情
         *
         * @param patientId
         * @param visitId
         * @return
         */
        Observable<PatientInfo> getPatientInfoByCache(String patientId, String visitId);

        /**
         * 根据医嘱条码，获取对应的要执行的医嘱列表
         *
         * @param patientId 患者ID
         * @param barCode   医嘱码
         * @param type      医嘱码类别
         * @return
         */
        Observable<Result<List<Order>>> getPatientPerformOrdersByBarCode(String patientId, String barCode, String type);

        /**
         * 执行医嘱
         *
         * @return
         */
        Observable<Result> executeOrders(List<OrdersExecute> executeList);

        /**
         * 撤回执行（不管该条医嘱处于什么状态，将其撤回到未执行状态）
         *
         * @param execute
         * @return
         */
        Observable<Result> revokeOrders(OrdersExecute execute);
    }

    interface View extends IView {

        /**
         * 显示患者信息
         *
         * @param data
         */
        void updatePatientInfo(PatientInfo data);

        /**
         * 跟新医嘱详情
         *
         * @param groupOrders
         */
        void updateOrdersInfo(List<Order> groupOrders);


        void executeSuccess(List<OrdersExecute> executeList);

        /**
         * 执行双签名医嘱
         */
        void executeDoubleSignatureOrders(List<OrdersExecute> executeList, List<String> verifyList);


        void clearVerifyList();

        /**
         * 执行失败
         *
         * @param errorMessage
         */
        void executeFailed(String errorMessage);
    }
}
