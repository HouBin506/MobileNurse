package com.herenit.mobilenurse.mvp.main;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.User;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/3/1 13:43
 * desc: 单患者的Contract
 */
public interface SinglePatientContract {
    interface Model extends IModel {

        /**
         * 获取本地缓存中用户所具有的科室（病区）列表
         *
         * @param userId 用户账号
         * @return
         */
        Observable<List<User.MnUserVsGroupVPOJOListBean>> getCacheUserGroupList(String userId);

        /**
         * 获取本地缓存的患者列表
         *
         * @param userId    账号
         * @param groupCode 科室代码或者病区代码
         * @return
         */
        Observable<List<Sickbed>> getCacheGroupPatientList(String userId, String groupCode);
    }

    interface View extends IView {

    }
}
