package com.herenit.mobilenurse.mvp.nursing_round;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.NurseRoundItem;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.CommonPatientItemQuery;
import com.herenit.mobilenurse.criteria.entity.submit.OrderListQuery;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewGroup;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/2/18 13:59
 * desc:“护理巡视”功能  Contract
 */
public interface NurseRoundHistoryContract {
    interface View extends IView {
        void loadDataSuccess();
    }

    interface Model extends IModel {
        /**
         * 获取巡视历史数据
         *
         * @param query
         * @return
         */
        Observable<Result<List<NurseRoundItem>>> getNurseRoundHistoryList(CommonPatientItemQuery query);

        /**
         * 删除巡视数据
         *
         * @param deleteDataList
         * @return
         */
        Observable<Result> deleteNurseRoundData(List<NurseRoundItem> deleteDataList);
    }
}
