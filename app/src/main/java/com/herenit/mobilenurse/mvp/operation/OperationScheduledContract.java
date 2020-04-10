package com.herenit.mobilenurse.mvp.operation;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.OperationScheduled;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.OperationScheduledQuery;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/2/18 13:59
 * desc:手术安排 Contract
 */
public interface OperationScheduledContract {
    interface View extends IView {
        void onRefreshSuccess();

        void remindEmergencyAndUnconfirmedOperation(List<OperationScheduled> emergencyOperations);
    }

    interface Model extends IModel {
        /**
         * 查询手术安排列表
         *
         * @param query
         * @return
         */
        Observable<Result<List<OperationScheduled>>> loadOperationScheduledList(OperationScheduledQuery query);
    }
}
