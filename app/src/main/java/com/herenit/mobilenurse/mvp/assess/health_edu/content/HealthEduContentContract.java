package com.herenit.mobilenurse.mvp.assess.health_edu.content;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.AssessModel;
import com.herenit.mobilenurse.criteria.entity.HealthEduAssessModel;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.AssessParam;
import com.herenit.mobilenurse.criteria.entity.submit.HealthEduAssessParam;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/2/18 13:59
 * desc:“健康宣教”功能  Contract
 */
public interface HealthEduContentContract {
    interface View extends IView {
        /**
         * 显示健康教育结果
         *
         * @param assessModel
         */
        void showHealthEduResult(HealthEduAssessModel assessModel);
    }

    interface Model extends IModel {
        /**
         * 获取健康教育结果样式
         *
         * @return
         */
        Observable<Result<HealthEduAssessModel>> getHealthEduResult(String docId);

        /**
         * 保存或更新
         *
         * @return
         */
        Observable<Result> saveOrUpdateHealthEduContent(HealthEduAssessParam param);
    }
}
