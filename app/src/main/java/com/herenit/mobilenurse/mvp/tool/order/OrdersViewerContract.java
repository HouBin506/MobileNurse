package com.herenit.mobilenurse.mvp.tool.order;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.dict.OrderClassDict;
import com.herenit.mobilenurse.criteria.entity.submit.OrderListQuery;
import com.herenit.mobilenurse.criteria.entity.submit.OrdersExecute;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/3/12 14:46
 * desc: 医嘱选择器详情的Contract
 */
public interface OrdersViewerContract {

    interface Model extends IModel {
        /**
         * 通过网络查询患者医嘱列表
         *
         * @param query
         * @return
         */
        Observable<Result<List<Order>>> getOrderListByNetwork(OrderListQuery query);


        /**
         * 本地获取医嘱列表
         *
         * @param query
         * @return
         */
//        List<Order> getCacheOrderList(OrderListQuery query);


        /**
         * 根据查询条件更新当前查询到的医嘱列表
         *
         * @param orderList 数据源
         * @param query     查询条件
         * @return
         */
//        void updateQueryOrderList(List<Order> orderList, OrderListQuery query);

        /**
         * 查询医嘱类型列表
         *
         * @return
         */
        Observable<Result<List<OrderClassDict>>> getOrderClassList();
    }

    interface View extends IView {
        /**
         * 显示条件
         */
        void showConditionUI();

        /**
         * 显示医嘱列表
         */
        void showOrderListUI();
    }
}
