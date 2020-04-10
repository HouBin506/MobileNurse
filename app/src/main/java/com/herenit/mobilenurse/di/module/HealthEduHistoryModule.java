package com.herenit.mobilenurse.di.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.criteria.entity.HealthEduHistoryItem;
import com.herenit.mobilenurse.custom.adapter.HealthEduHistoryAdapter;
import com.herenit.mobilenurse.mvp.assess.health_edu.HealthEduContract;
import com.herenit.mobilenurse.mvp.assess.health_edu.HealthEduModel;
import com.herenit.mobilenurse.mvp.assess.health_edu.history.HealthEduHistoryContract;
import com.herenit.mobilenurse.mvp.assess.health_edu.history.HealthEduHistoryModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/1/11 16:36
 * desc:健康宣教历史记录功能的dagger
 */
@Module
public abstract class HealthEduHistoryModule {

    @Binds
    abstract HealthEduHistoryContract.Model bindHealthEduHistoryModel(HealthEduHistoryModel model);

    /**
     * 提供健康宣教历史记录数据
     *
     * @return
     */
    @Provides
    @ActivityScope
    static List<HealthEduHistoryItem> provideHealthEduHistoryItem() {
        return new ArrayList<>();
    }

    /**
     * 提供健康教育历史记录列表适配器
     *
     * @param activity
     * @param data
     * @return
     */
    @Provides
    @ActivityScope
    static HealthEduHistoryAdapter provideHealthEduHistoryAdapter(HealthEduHistoryContract.View activity, List<HealthEduHistoryItem> data) {
        return new HealthEduHistoryAdapter((Activity) activity, data);
    }

}
