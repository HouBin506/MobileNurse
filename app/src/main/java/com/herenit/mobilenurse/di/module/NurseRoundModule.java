package com.herenit.mobilenurse.di.module;

import android.support.v4.app.Fragment;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewItem;
import com.herenit.mobilenurse.custom.adapter.NurseRoundItemAdapter;
import com.herenit.mobilenurse.mvp.nursing_round.NurseRoundContract;
import com.herenit.mobilenurse.mvp.nursing_round.NurseRoundModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/3/6 15:54
 * desc:护理巡视的Dagger
 */
@Module
public abstract class NurseRoundModule {

    @Binds
    abstract NurseRoundContract.Model bindNurseRoundModel(NurseRoundModel model);

    /**
     * 提供护理巡视页面Item列表数据
     *
     * @return
     */
    @FragmentScope
    @Provides
    static List<NurseRoundViewItem> provideNurseRoundViewItemList() {
        return new ArrayList<>();
    }

    /**
     * 提供巡视列表Adapter
     *
     * @param fragment
     * @param datas
     * @return
     */
    @FragmentScope
    @Provides
    static NurseRoundItemAdapter provideNurseRoundItemAdapter(NurseRoundContract.View fragment, List<NurseRoundViewItem> datas) {
        return new NurseRoundItemAdapter(((Fragment) fragment).getContext(), datas);
    }
}
