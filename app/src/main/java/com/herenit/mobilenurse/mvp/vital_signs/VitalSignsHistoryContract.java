package com.herenit.mobilenurse.mvp.vital_signs;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.submit.VitalSignsHistoryQuery;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsViewItem;

import java.util.List;

import io.reactivex.Observable;


/**
 * author: HouBin
 * date: 2019/4/10 10:05
 * desc: 体征的Contract
 */
public interface VitalSignsHistoryContract {
    interface View extends IView {
        void refreshSuccess();
    }

    interface Model extends IModel {

        /**
         * 更新体征历史数据
         *
         * @param vitalSignsList
         * @return
         */
        Observable<Result> updateVitalSigns(List<VitalSignsItem> vitalSignsList);

        /**
         * 删除体征历史数据
         *
         * @param vitalSignsList
         * @return
         */
        Observable<Result> deleteVitalSigns(List<VitalSignsItem> vitalSignsList);

        /**
         * 获取体征历史数据
         *
         * @param query
         * @return
         */
        Observable<Result<List<VitalSignsItem>>> getVitalSignsHistoryList(VitalSignsHistoryQuery query);
    }

}
