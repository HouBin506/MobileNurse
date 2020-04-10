package com.herenit.mobilenurse.mvp.assess.health_edu.history;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.HealthEduHistoryItem;
import com.herenit.mobilenurse.criteria.entity.MultiListMenuItem;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/2/18 13:59
 * desc:“健康宣教历史”功能  Contract
 */
public interface HealthEduHistoryContract {
    interface View extends IView {
        //获取当前患者
        Sickbed sickbed();
    }

    interface Model extends IModel {
        /**
         * 获取健康宣教历史数据
         *
         * @param patientId
         * @param visitId
         * @return
         */
        Observable<Result<List<HealthEduHistoryItem>>> loadHealthEduHistory(String patientId, String visitId);

        /**
         * 删除某条健康教育记录
         *
         * @param docId
         * @return
         */
        Observable<Result> deleteHealthEduHistory(String docId);
    }
}
