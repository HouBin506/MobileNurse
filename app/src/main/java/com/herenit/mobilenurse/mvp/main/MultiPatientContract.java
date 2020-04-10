package com.herenit.mobilenurse.mvp.main;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.criteria.entity.submit.SickbedListQuery;

import java.util.List;

import io.reactivex.Observable;
import io.rx_cache2.Reply;

/**
 * author: HouBin
 * date: 2019/1/29 16:45
 * desc:主界面 Contract
 */
public interface MultiPatientContract {

    interface View extends IView {
        /**
         * 界面展示
         */
        void showView();

        /**
         * 显示模块页面
         */
        void showModuleView();

        /**
         * 切换科室（或者病区、护理单元）
         */
        void switchGroup();
    }

    interface Model extends IModel {
        /**
         * 根据用户Id获取缓存中的User
         *
         * @param userId
         * @return
         */
        User getCacheUser(String userId);


        List<Sickbed> getCacheSickbedList(String groupId);

        /**
         * 获取床位列表
         *
         * @param query
         * @return
         */
        Observable<Result<List<Sickbed>>> getSickbedList(SickbedListQuery query);


        /**
         * 更新本地缓存的某科室、护理单元的床位列表
         *
         * @param sickbedList
         * @param groupCode
         */
        void updateCacheGroupSickbedList(List<Sickbed> sickbedList, String groupCode);
    }
}
