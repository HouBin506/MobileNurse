package com.herenit.mobilenurse.mvp.patient;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Result;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/3/4 14:57
 * desc:患者详情的Contract
 */
public interface PatientInfoContract {
    interface Model extends IModel {
        /**
         * 网络获取患者详情
         *
         * @param patientId
         * @param groupCode
         * @return
         */
        Observable<Result<PatientInfo>> getPatientInfoByNetwork(String patientId, String groupCode);

        /**
         * 保存患者详情
         *
         * @param patientInfo 数据源
         * @return
         */
        void savePatientInfo(PatientInfo patientInfo);


        /**
         * 缓存中获取患者详情
         *
         * @param patientId
         * @param visitId
         * @return
         */
        PatientInfo getPatientInfoByCache(String patientId, String visitId);
    }

    interface View extends IView {

        /**
         * 显示患者详情
         *
         * @param patient
         */
        void showPatientInfo(PatientInfo patient);
    }
}
