package com.herenit.mobilenurse.di.module;

import android.support.v4.app.Fragment;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsViewItem;
import com.herenit.mobilenurse.custom.adapter.VitalSignsItemAdapter;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsRecordContract;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsRecordModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/4/10 10:12
 * desc: 生命体征Dagger
 */
@Module
public abstract class VitalSignsRecordModule {
    @Binds
    abstract VitalSignsRecordContract.Model bindVitalSignsModel(VitalSignsRecordModel model);

    /**
     * 提供体征录入界面，体征项目的Adapter
     *
     * @param fragment
     * @param datas
     * @return
     */
    @Provides
    @FragmentScope
    static VitalSignsItemAdapter provideVitalSignsItemAdapter(VitalSignsRecordContract.View fragment, List<VitalSignsViewItem> datas) {
        return new VitalSignsItemAdapter(((Fragment) fragment).getContext(), datas);
    }

    /**
     * 提供体征录入界面，要显示的体征项目UI控制列表
     *
     * @return
     */
    @Provides
    @FragmentScope
    static List<VitalSignsViewItem> provideVitalSignsViewItemList() {
        return new ArrayList<>();
    }
}
