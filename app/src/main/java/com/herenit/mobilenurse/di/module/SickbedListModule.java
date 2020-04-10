package com.herenit.mobilenurse.di.module;

import android.support.v4.app.Fragment;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.submit.SickbedListQuery;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.custom.adapter.ConditionAdapter;
import com.herenit.mobilenurse.custom.adapter.SickbedAdapter;
import com.herenit.mobilenurse.mvp.sickbed.SickbedListContract;
import com.herenit.mobilenurse.mvp.sickbed.SickbedListModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/2/21 14:31
 * desc: 床位列表的dagger Module
 */
@Module
public abstract class SickbedListModule {
    @Binds
    abstract SickbedListContract.Model bindSickbedListModel(SickbedListModel model);

    /**
     * 提供筛选条件列表
     *
     * @return
     */
    @FragmentScope
    @Provides
    static List<Conditions> provideSickbedConditionList() {
        return new ArrayList<>();
    }

    /**
     * 提供页面条件列表的Adapter
     *
     * @param fragment
     * @param conditionList
     * @return
     */
    @FragmentScope
    @Provides
    static ConditionAdapter provideConditionAdapter(SickbedListContract.View fragment, List<Conditions> conditionList) {

        return new ConditionAdapter(((Fragment) fragment).getContext(), conditionList,
                EventBusUtils.obtainPrivateId(fragment.toString(), CommonConstant.EVENT_INTENTION_CONDITION_CHANGED));
    }

    /**
     * 提供床位列表
     *
     * @return
     */
    @FragmentScope
    @Provides
    static List<Sickbed> provideSickbedList() {
        return new ArrayList<>();
    }

    /**
     * 提供床位列表Adapter
     *
     * @param fragment
     * @param sickbedList
     * @return
     */
    @FragmentScope
    @Provides
    static SickbedAdapter provideSickbedAdapter(SickbedListContract.View fragment, List<Sickbed> sickbedList) {
        return new SickbedAdapter(((Fragment) fragment).getContext(), sickbedList);
    }

    /**
     * 提供查询患者列表要向服务器端发送的查询条件
     *
     * @return
     */
    @FragmentScope
    @Provides
    static SickbedListQuery provideSickbedListQuery() {
        return SickbedListQuery.createQueryAllSickbed(UserTemp.getInstance().getGroupCode());
    }
}
