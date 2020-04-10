package com.herenit.mobilenurse.di.module;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.app.utils.FileUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.CommonNameCode;
import com.herenit.mobilenurse.criteria.entity.OperationScheduled;
import com.herenit.mobilenurse.criteria.entity.dict.AckIndicatorDict;
import com.herenit.mobilenurse.criteria.entity.dict.Dict;
import com.herenit.mobilenurse.criteria.entity.dict.EmergencyIndicatorDict;
import com.herenit.mobilenurse.criteria.entity.submit.OperationScheduledQuery;
import com.herenit.mobilenurse.custom.adapter.NameCodeAdapter;
import com.herenit.mobilenurse.custom.adapter.OperationScheduledAdapter;
import com.herenit.mobilenurse.datastore.tempcache.DictTemp;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.mvp.operation.OperationScheduledContract;
import com.herenit.mobilenurse.mvp.operation.OperationScheduledFragment;
import com.herenit.mobilenurse.mvp.operation.OperationScheduledModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/2/21 14:31
 * desc: 手术安排的dagger Module
 */
@Module
public abstract class OperationScheduledModule {
    @Binds
    abstract OperationScheduledContract.Model bindOperationScheduledModel(OperationScheduledModel model);

    /**
     * 提供选择时间的列表
     *
     * @return
     */
    @FragmentScope
    @Provides
    static List<CommonNameCode> provideSelectDateList() {
        List<CommonNameCode> dateList = new ArrayList<>();
        String json = FileUtils.getAssetsToString(FileUtils.FILE_NAME_UI_OPERATION_TIME_INTERVAL);
        if (!TextUtils.isEmpty(json)) {
            List<CommonNameCode> list = JSON.parseArray(json, CommonNameCode.class);
            if (list != null)
                dateList.addAll(list);
        }
        return dateList;
    }


    /**
     * 提供手术安排列表
     *
     * @return
     */
    @FragmentScope
    @Provides
    static List<OperationScheduled> provideOperationScheduledList() {
        return new ArrayList<>();
    }

    /**
     * 提供手术安排列表Adapter
     *
     * @param fragment
     * @param data
     * @return
     */
    @FragmentScope
    @Provides
    static OperationScheduledAdapter provideOperationScheduledAdapter(OperationScheduledContract.View fragment, List<OperationScheduled> data) {
        return new OperationScheduledAdapter(((Fragment) fragment).getContext(), data);
    }

    /**
     * 提供手术查询条件
     *
     * @return
     */
    @FragmentScope
    @Provides
    static OperationScheduledQuery provideOperationScheduledQuery() {
        OperationScheduledQuery query = new OperationScheduledQuery();
        query.setGroupCode(UserTemp.getInstance().getGroupCode());
        query.setStartDateTime(TimeUtils.getStartDateTimeByTimeCode(OperationScheduledFragment.DEFAULT_TIME_INTERVAL_CODE));
        query.setStopDateTime(TimeUtils.getStopDateTimeByTimeCode(OperationScheduledFragment.DEFAULT_TIME_INTERVAL_CODE));
        query.setEmergencyIndicator(OperationScheduledFragment.DEFAULT_EMERGENCY_INDICATOR);
        return query;
    }

    /**
     * 提供紧急标识查询条件列表
     *
     * @return
     */
    @FragmentScope
    @Provides
    static List<EmergencyIndicatorDict> provideEmergencyIndicatorList() {
        List<EmergencyIndicatorDict> emergencyIndicatorList = DictTemp.getInstance().getEmergencyIndicatorList();
        EmergencyIndicatorDict dict = new EmergencyIndicatorDict();
        dict.setEmergencyIndicatorName("全部");
        dict.setEmergencyIndicatorCode("");
        emergencyIndicatorList.add(0, dict);
        return emergencyIndicatorList;
    }

    /**
     * 提供手术确认标识查询列表标识
     *
     * @return
     */
    @FragmentScope
    @Provides
    static List<AckIndicatorDict> provideAckIndicatorList() {
        return DictTemp.getInstance().getAckIndicatorList();
    }
}
