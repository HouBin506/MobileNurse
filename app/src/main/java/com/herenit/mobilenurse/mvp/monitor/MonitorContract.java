package com.herenit.mobilenurse.mvp.monitor;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.MonitorBind;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Result;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/3/4 14:57
 * desc:监护仪绑定的Contract
 */
public interface MonitorContract {
    interface Model extends IModel {

        /**
         * 获取患者详情
         *
         * @param patientId
         * @param groupCode
         * @return
         */
        Observable<Result<PatientInfo>> loadPatientInfo(String patientId, String groupCode);

        /**
         * 获取监护仪绑定信息
         *
         * @param visitNo
         * @return
         */
        Observable<Result<MonitorBind>> loadMonitorInfo(String visitNo);

        /**
         * 监护仪绑定核对，在绑定监护仪之前，做一次核对。核对接下来做的操纵（绑定、解绑、先解绑后绑定、解绑+解绑+绑定）
         *
         * @param monitor
         * @return
         */
        Observable<Result<MonitorBind>> monitorBindVerify(MonitorBind monitor);

        /**
         * 监护仪绑定
         * 此操作，可以是绑定操作，也可以是解绑操作，也可以是先解绑后绑定操作，关键要看入参 {@link MonitorBind#getOperationType()}
         *
         * @param monitor
         */
        Observable<Result> monitorBind(MonitorBind monitor);
    }

    interface View extends IView {

        /**
         * 显示患者详情
         *
         * @param patient
         */
        void showPatientInfo(PatientInfo patient);

        /**
         * 显示患者监护仪绑定情况
         *
         * @param monitor
         */
        void showMonitorInfo(MonitorBind monitor);

        void loadFinish();

        /**
         * 显示监护仪绑定核对提示窗
         *
         * @param monitor
         */
        void showBindVerifyNotice(MonitorBind monitor);

        /**
         * 绑定成功
         */
        void bindSuccess();

    }
}
