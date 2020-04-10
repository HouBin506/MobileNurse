package com.herenit.mobilenurse.mvp.assess.health_edu;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.MultiListMenuItem;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/2/18 13:59
 * desc:“纤康宣教”功能  Contract
 */
public interface HealthEduContract {
    interface View extends IView {
        Sickbed getCurrentSickbed();

        void getHealthEduItemListSuccess();
    }

    interface Model extends IModel {
        /**
         * 获取健康宣教项目列表
         *
         * @param userId
         * @return
         */
        Observable<Result<List<MultiListMenuItem>>> getHealthEduItemList(String userId);
    }
}
