package com.herenit.mobilenurse.mvp.lab.report;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.lab.CommonLabReport;
import com.herenit.mobilenurse.criteria.entity.lab.MicroorganismLabReport;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/2/18 13:59
 * desc:“检验报告”功能  Contract
 */
public interface LabReportContract {
    interface View extends IView {
        Sickbed getCurrentSickbed();

        void loadDataSuccess();
    }

    interface Model extends IModel {
        /**
         * 获取检验报告列表
         *
         * @param patientId
         * @param visitId
         * @return
         */
        Observable<Result<List<CommonLabReport>>> getLabReportList(String patientId, String visitId);

        /**
         * 获取微生物检验数据列表
         *
         * @param patientId
         * @param visitId
         * @return
         */
        Observable<Result<List<MicroorganismLabReport>>> getMicroorganismLabReportList(String patientId, String visitId);
    }
}
