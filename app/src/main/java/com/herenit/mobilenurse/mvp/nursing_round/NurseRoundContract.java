package com.herenit.mobilenurse.mvp.nursing_round;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.NurseRoundItem;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.OrderListQuery;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewGroup;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewItem;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/2/18 13:59
 * desc:“护理巡视”功能  Contract
 */
public interface NurseRoundContract {
    interface View extends IView {
        void loadNurseRoundListSuccess(NurseRoundViewGroup nurseRoundView);

    }

    interface Model extends IModel {
        /**
         * 获取巡视列表
         *
         * @return
         */
        Observable<Result<NurseRoundViewGroup>> getNurseRoundItemList(OrderListQuery query);

        /**
         * 提交巡视数据
         *
         * @param dataList
         * @return
         */
        Observable<Result> commitNurseRoundData(List<NurseRoundItem> dataList);
    }
}
