package com.herenit.mobilenurse.mvp.sickbed;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.dict.NursingClassDict;
import com.herenit.mobilenurse.criteria.entity.dict.PatientConditionDict;
import com.herenit.mobilenurse.criteria.entity.submit.SickbedListQuery;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/2/16 16:45
 * desc: 床位列表的Contract
 */
public interface SickbedListContract {

    interface View extends IView {
        void loadConditionsFinish();

        /**
         * 完成刷新
         */
        void finishRefresh();

        /**
         * 扫描结果
         *
         * @param scanResult
         */
        void onScanResult(ScanResult scanResult);

        /**
         * 显示患者数量
         *
         * @param num
         */
        void showPatientNum(int num);
    }

    interface Model extends IModel {
        /**
         * 获取病情类型
         *
         * @return
         */
        Observable<Result<List<PatientConditionDict>>> getPatientConditionList();

        /**
         * 获取护理等级
         *
         * @return
         */
        Observable<Result<List<NursingClassDict>>> getNursingClassList();

        /**
         * 网络请求获取患者列表
         *
         * @param query
         * @return
         */
        Observable<Result<List<Sickbed>>> getSickbedList(SickbedListQuery query);

        /**
         * 读取缓存中的床位列表
         *
         * @param userId  当前账号
         * @param groupId 当前科室
         * @return 根据userId作为key， groupId作为子Key 返回缓存中对应的床位列表
         */
        Observable<List<Sickbed>> getCacheSickbedList(String userId, String groupId);

        /**
         * 根据科室代码，获取本地床位列表
         *
         * @param groupId
         * @return
         */
        List<Sickbed> getCacheSickbedList(String groupId);

        /**
         * 更新本地数据库床位列表
         *
         * @param data
         */
        void updateSickbedList(List<Sickbed> data);
    }
}
