package com.herenit.mobilenurse.di.module;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.criteria.entity.submit.SickbedListQuery;
import com.herenit.mobilenurse.custom.adapter.CommonTextAdapter;
import com.herenit.mobilenurse.custom.adapter.SelectSickbedAdapter;
import com.herenit.mobilenurse.mvp.main.MultiPatientContract;
import com.herenit.mobilenurse.mvp.main.SinglePatientContract;
import com.herenit.mobilenurse.mvp.main.SinglePatientModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/3/1 14:28
 * desc: 单患者模块的Dagger
 */
@Module
public abstract class SinglePatientModule {
    @Binds
    abstract SinglePatientContract.Model bindSinglePatientModel(SinglePatientModel model);

    /**
     * 提供科室（病区）列表
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<User.MnUserVsGroupVPOJOListBean> provideGroupList() {
        return new ArrayList<>();
    }

    /**
     * 提供科室名（病区名）列表
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<String> provideGroupNameList() {
        return new ArrayList<>();
    }

    /**
     * 提供切换科室列表的Adapter
     *
     * @param activity
     * @param groupNameList
     * @return
     */
    @ActivityScope
    @Provides
    static CommonTextAdapter provideGroupNameAdapter(SinglePatientContract.View activity, List<String> groupNameList) {
        return new CommonTextAdapter((Activity) activity, groupNameList);
    }

    /**
     * 提供当前账号需要工作的床位列表
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<Sickbed> provideSickbedList() {
        return new ArrayList<>();
    }

    /**
     * 提供选择床位下拉列表的Adapter
     *
     * @param activity
     * @param sickbedList
     * @return
     */
    @ActivityScope
    @Provides
    static SelectSickbedAdapter provideSelectSickbedAdapter(SinglePatientContract.View activity, List<Sickbed> sickbedList) {
        return new SelectSickbedAdapter((Activity) activity, sickbedList);
    }

    /**
     * 获取Fragment管理
     *
     * @param activity
     * @return
     */
    @ActivityScope
    @Provides
    static FragmentManager provideFragmentManager(SinglePatientContract.View activity) {
        return ((FragmentActivity) activity).getSupportFragmentManager();
    }
}
