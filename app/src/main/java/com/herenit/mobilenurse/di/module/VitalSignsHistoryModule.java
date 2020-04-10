package com.herenit.mobilenurse.di.module;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.app.utils.FileUtils;
import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.CommonNameCode;
import com.herenit.mobilenurse.criteria.common.SelectNameCode;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.submit.VitalSignsHistoryQuery;
import com.herenit.mobilenurse.custom.adapter.NameCodeAdapter;
import com.herenit.mobilenurse.custom.adapter.SelectBoxAdapter;
import com.herenit.mobilenurse.custom.adapter.VitalSignsHistoryAdapter;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsHistoryActivity;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsHistoryContract;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsHistoryModel;


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
public abstract class VitalSignsHistoryModule {
    @Binds
    abstract VitalSignsHistoryContract.Model bindVitalSignsModel(VitalSignsHistoryModel model);

    /**
     * 提供体征历史记录数据
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<VitalSignsItem> provideVitalSignsHistoryList() {
        return new ArrayList<>();
    }

    /**
     * 提供体征历史记录Adapter
     *
     * @param activity
     * @param data
     * @return
     */
    @ActivityScope
    @Provides
    static VitalSignsHistoryAdapter provideVitalSignsHistoryAdapter(VitalSignsHistoryContract.View activity, List<VitalSignsItem> data) {
        return new VitalSignsHistoryAdapter((Activity) activity, data);
    }

    /**
     * 提供选择体征类型的列表
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<SelectNameCode> provideSelectVitalItemList() {
        return new ArrayList<>();
    }

    /**
     * 提供选择体征列表的Adapter
     *
     * @param activity
     * @param data
     * @return
     */
    @ActivityScope
    @Provides
    static SelectBoxAdapter provideSelectVitalItemAdapter(VitalSignsHistoryContract.View activity, List<SelectNameCode> data) {
        return new SelectBoxAdapter((Activity) activity, data);
    }

    /**
     * 提供选择时间的列表
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<CommonNameCode> provideSelectDateList() {
        List<CommonNameCode> dateList = new ArrayList<>();
        String json = FileUtils.getAssetsToString(FileUtils.FILE_NAME_UI_CONDITION_COMMON_TIME_INTERVAL);
        if (!TextUtils.isEmpty(json)) {
            List<CommonNameCode> list = JSON.parseArray(json, CommonNameCode.class);
            if (list != null)
                dateList.addAll(list);
        }
        return dateList;
    }


    /**
     * 提供选择时间列表的Adapter
     *
     * @param activity
     * @param data
     * @return
     */
    @ActivityScope
    @Provides
    static NameCodeAdapter<CommonNameCode> provideSelectDateAdapter(VitalSignsHistoryContract.View activity, List<CommonNameCode> data) {
        return new NameCodeAdapter<>((Activity) activity, data);
    }

    /**
     * 提供查询条件
     *
     * @return
     */
    @Provides
    @ActivityScope
    static VitalSignsHistoryQuery provideVitalSignsHistoryQuery() {
        VitalSignsHistoryQuery query = new VitalSignsHistoryQuery();
        Sickbed sickbed = SickbedTemp.getInstance().getCurrentSickbed();
        query.setPatientId(sickbed.getPatientId());
        query.setVisitId(sickbed.getVisitId());
        //默认查询当日的体征数据
        query.setStartDateTime(TimeUtils.getStartDateTimeByTimeCode(VitalSignsHistoryActivity.DEFAULT_TIME_INTERVAL_CODE));
        query.setStopDateTime(TimeUtils.getStopDateTimeByTimeCode(VitalSignsHistoryActivity.DEFAULT_TIME_INTERVAL_CODE));
        return query;
    }
}
