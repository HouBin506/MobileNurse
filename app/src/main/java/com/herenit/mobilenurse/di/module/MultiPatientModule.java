package com.herenit.mobilenurse.di.module;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.custom.adapter.CommonTextAdapter;
import com.herenit.mobilenurse.mvp.main.MultiPatientContract;
import com.herenit.mobilenurse.mvp.main.MultiPatientModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/1/28 15:04
 * desc: 主界面Dagger ModuleManager
 */
@Module
public abstract class MultiPatientModule {
    @Binds
    abstract MultiPatientContract.Model bindMainModel(MultiPatientModel model);

    /**
     * 提供科室切换列表的Text数据
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<String> provideGroupTextList() {
        return new ArrayList<>();
    }

    /**
     * 提供科室列表切换的Adapter
     *
     * @param activity
     * @param data
     * @return
     */
    @ActivityScope
    @Provides
    static CommonTextAdapter provideGroupAdapter(MultiPatientContract.View activity, List<String> data) {
        return new CommonTextAdapter((Activity) activity, data);
    }

    /**
     * 提供当前账号科室列表
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<User.MnUserVsGroupVPOJOListBean> provideGroupList() {
        return new ArrayList<>();
    }

    /**
     * 获取Fragment管理
     *
     * @param activity
     * @return
     */
    @ActivityScope
    @Provides
    static FragmentManager provideFragmentManager(MultiPatientContract.View activity) {
        return ((FragmentActivity) activity).getSupportFragmentManager();
    }
}
