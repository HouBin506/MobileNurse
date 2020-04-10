package com.herenit.mobilenurse.di.module;

import android.support.v4.app.Fragment;
import android.widget.SimpleAdapter;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.enums.ModuleEnum;
import com.herenit.mobilenurse.custom.adapter.ToolAdapter;
import com.herenit.mobilenurse.mvp.other.OtherContract;
import com.herenit.mobilenurse.mvp.other.OtherModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/3/6 15:54
 * desc:“其它”功能的Dagger
 */
@Module
public abstract class OtherModule {

    @Binds
    abstract OtherContract.Model bindOtherModel(OtherModel model);

    /**
     * 提供工具列表数据
     *
     * @return
     */
    @Provides
    @FragmentScope
    static List<ModuleEnum> provideOtherToolList() {
        List<ModuleEnum> toolList = new ArrayList<>();
        toolList.add(ModuleEnum.TOOL_CALCULATOR);
        toolList.add(ModuleEnum.TOOL_STOPWATCH);
        return toolList;
    }

    /**
     * 提供工具列表Adapter
     *
     * @param fragment
     * @param dataList
     * @return
     */
    @Provides
    @FragmentScope
    static ToolAdapter provideOtherToolAdapter(OtherContract.View fragment, List<ModuleEnum> dataList) {
        return new ToolAdapter(((Fragment) fragment).getContext(), dataList);
    }
}
