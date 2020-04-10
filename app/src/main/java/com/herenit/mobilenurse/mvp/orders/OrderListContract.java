package com.herenit.mobilenurse.mvp.orders;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.OrderPerformLabel;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.dict.ExecuteResultDict;
import com.herenit.mobilenurse.criteria.entity.dict.OrderClassDict;
import com.herenit.mobilenurse.criteria.entity.submit.OrderListQuery;
import com.herenit.mobilenurse.criteria.entity.submit.OrdersExecute;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/3/6 15:49
 * desc:医嘱列表的Contract
 */
public interface OrderListContract {

    interface Model extends IModel {
        /**
         * 通过网络查询患者医嘱列表
         *
         * @param query
         * @return
         */
        Observable<Result<List<Order>>> getOrderListByNetwork(OrderListQuery query);


//        /**
//         * 本地获取医嘱列表
//         *
//         * @param query
//         * @return
//         */
//        List<Order> getCacheOrderList(OrderListQuery query);
//

//        /**
//         * 根据查询条件更新当前查询到的医嘱列表
//         *
//         * @param orderList 数据源
//         * @param query     查询条件
//         * @return
//         */
//        void updateQueryOrderList(List<Order> orderList, OrderListQuery query);

        /**
         * 查询医嘱类型列表
         *
         * @return
         */
        Observable<Result<List<OrderClassDict>>> getOrderClassList();


        /**
         * 执行医嘱
         *
         * @return
         */
        Observable<Result> executeOrders(List<OrdersExecute> executeList);

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
         * 获取医嘱执行单类别列表
         *
         * @param groupCode
         * @return
         */
        Observable<Result<List<OrderPerformLabel>>> getOrderPerformLabelList(String groupCode);
    }

    interface View extends IView {
        /**
         * 显示筛选条件
         */
        void showConditionUI();

        /**
         * 显示医嘱列表
         */
        void showOrderListUI();

        /**
         * 执行双签名医嘱
         *
         * @param executeList
         */
        void executeDoubleSignatureOrders(List<OrdersExecute> executeList, List<String> verifyList);

        /**
         * 执行皮试医嘱
         *
         * @param skinTestOrders
         */
        void executeSkinTestOrder(List<Order> skinTestOrders);

        void clearVerifyList();

        void executeFailed(String errorMessage);

    }
}
