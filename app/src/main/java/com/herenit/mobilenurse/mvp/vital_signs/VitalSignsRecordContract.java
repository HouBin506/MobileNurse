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
public interface VitalSignsRecordContract {
    interface View extends IView {
        void onRefreshSuccess();
    }

    interface Model extends IModel {
        /**
         * 加载体征字典列表
         *
         * @param groupCode
         * @param patientId
         * @param visitId
         * @return
         */
        Observable<Result<List<VitalSignsViewItem>>> getPatientVitalSignsList(String groupCode, String patientId, String visitId);

        /**
         * 提交体征数据
         *
         * @param vitalSignsList
         * @return
         */
        Observable<Result> postVitalSignsRecord(List<VitalSignsItem> vitalSignsList);

        Observable<Result<List<VitalSignsItem>>> getVitalSignsHistoryList(VitalSignsHistoryQuery query);
    }

}
