package com.herenit.mobilenurse.mvp.assess.admission_assessment;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.AssessModel;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.AssessParam;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/2/18 13:59
 * desc:“入院评估”功能  Contract
 */
public interface AdmAssessContract {
    interface View extends IView {
        void getAdmissionAssessModelSuccess(AssessModel model);

        void saveAdmissionAssessDataSuccess();
    }

    interface Model extends IModel {
        /**
         * 获取入院评估数据（界面配置+历史数据）
         *
         * @param patientId
         * @param visitId
         * @return
         */
        Observable<Result<AssessModel>> getAdmissionAssessModel(String patientId, String visitId);

        /**
         * 提交入院评估数据
         *
         * @param param
         * @return
         */
        Observable<Result> commitAdmissionAssessData(AssessParam param);
    }
}
