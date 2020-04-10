package com.herenit.mobilenurse.di.module;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.app.utils.FileUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.CommonNameCode;
import com.herenit.mobilenurse.criteria.common.SelectNameCode;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.submit.VitalSignsHistoryQuery;
import com.herenit.mobilenurse.custom.adapter.NameCodeAdapter;
import com.herenit.mobilenurse.custom.adapter.SelectBoxAdapter;
import com.herenit.mobilenurse.datastore.tempcache.CommonTemp;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsChartActivity;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsChartContract;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsChartModel;

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
public abstract class VitalSignsChartModule {
    @Binds
    abstract VitalSignsChartContract.Model bindVitalSignsModel(VitalSignsChartModel model);

    /**
     * 提供体征历史记录数据
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<VitalSignsItem> provideVitalSignsChartList() {
        return new ArrayList<>();
    }

    /**
     * 提供查询条件
     *
     * @return
     */
    @Provides
    @ActivityScope
    static VitalSignsHistoryQuery provideVitalSignsChartQuery() {
        VitalSignsHistoryQuery query = new VitalSignsHistoryQuery();
        Sickbed sickbed = SickbedTemp.getInstance().getCurrentSickbed();
        query.setPatientId(sickbed.getPatientId());
        query.setVisitId(sickbed.getVisitId());
        //默认显示过去三天的体征趋势图
        query.setStartDateTime(TimeUtils.getStartDateTimeByTimeCode(VitalSignsChartActivity.DEFAULT_TIME_INTERVAL_CODE));
        query.setStopDateTime(TimeUtils.getStopDateTimeByTimeCode(VitalSignsChartActivity.DEFAULT_TIME_INTERVAL_CODE));
        return query;
    }
}
