package com.herenit.mobilenurse.mvp.exam.report;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.ExamReport;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/2/18 13:59
 * desc:“检查报告”功能  Contract
 */
public interface ExamReportContract {
    interface View extends IView {
        Sickbed getCurrentSickbed();
        void loadDataSuccess();
    }

    interface Model extends IModel {
        /**
         * 获取检查报告列表
         *
         * @param patientId
         * @param visitId
         * @return
         */
        Observable<Result<List<ExamReport>>> getExamReportList(String patientId, String visitId);
    }
}
