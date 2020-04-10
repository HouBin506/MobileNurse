package com.herenit.mobilenurse.mvp.vital_signs;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.VitalSignsHistoryQuery;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsChartData;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/6/3 11:16
 * desc:体征趋势图界面 Contract
 */
public interface VitalSignsChartContract {
    interface View extends IView {
        /**
         * 显示体征趋势图
         *
         * @param data
         */
        void showChart(VitalSignsChartData data);
    }

    interface Model extends IModel {
        /**
         * 加载体征趋势图所需数据
         *
         * @param query
         * @return
         */
        Observable<Result<VitalSignsChartData>> loadVitalSignsChartData(VitalSignsHistoryQuery query);
    }
}
