package com.herenit.mobilenurse.di.module;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.app.utils.FileUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.CommonNameCode;
import com.herenit.mobilenurse.criteria.common.SelectNameCode;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.NurseRoundItem;
import com.herenit.mobilenurse.criteria.entity.dict.NurseRoundDict;
import com.herenit.mobilenurse.criteria.entity.submit.CommonPatientItemQuery;
import com.herenit.mobilenurse.custom.adapter.NameCodeAdapter;
import com.herenit.mobilenurse.custom.adapter.NurseRoundHistoryAdapter;
import com.herenit.mobilenurse.custom.adapter.SelectBoxAdapter;
import com.herenit.mobilenurse.datastore.tempcache.DictTemp;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.mvp.nursing_round.NurseRoundHistoryActivity;
import com.herenit.mobilenurse.mvp.nursing_round.NurseRoundHistoryContract;
import com.herenit.mobilenurse.mvp.nursing_round.NurseRoundHistoryModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/4/10 10:12
 * desc: 护理巡视历史记录Dagger
 */
@Module
public abstract class NurseRoundHistoryModule {
    @Binds
    abstract NurseRoundHistoryContract.Model bindNurseRoundModel(NurseRoundHistoryModel model);

    /**
     * 提供巡视历史记录数据
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<NurseRoundItem> provideNurseRoundHistoryList() {
        return new ArrayList<>();
    }

    /**
     * 提供巡视历史记录Adapter
     *
     * @param activity
     * @param data
     * @return
     */
    @ActivityScope
    @Provides
    static NurseRoundHistoryAdapter provideNurseRoundHistoryAdapter(NurseRoundHistoryContract.View activity, List<NurseRoundItem> data) {
        return new NurseRoundHistoryAdapter((Activity) activity, data);
    }

    /**
     * 提供选择巡视类型的列表
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<SelectNameCode> provideSelectNurseRoundItemList() {
        List<SelectNameCode> selectNameCodeList = new ArrayList<>();
        List<NurseRoundDict> nurseRoundDictList = DictTemp.getInstance().getNurseRoundList();
        if (nurseRoundDictList != null) {
            for (NurseRoundDict nurseRound : nurseRoundDictList) {
                selectNameCodeList.add(new SelectNameCode(nurseRound.getItemName(), nurseRound.getItemCode(), false));
            }
        }
        return selectNameCodeList;
    }

    /**
     * 提供选择巡视列表的Adapter
     *
     * @param activity
     * @param data
     * @return
     */
    @ActivityScope
    @Provides
    static SelectBoxAdapter provideSelectNurseRoundItemAdapter(NurseRoundHistoryContract.View activity, List<SelectNameCode> data) {
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
    static NameCodeAdapter<CommonNameCode> provideSelectDateAdapter(NurseRoundHistoryContract.View activity, List<CommonNameCode> data) {
        return new NameCodeAdapter<>((Activity) activity, data);
    }

    /**
     * 提供查询条件
     *
     * @return
     */
    @Provides
    @ActivityScope
    static CommonPatientItemQuery provideNurseRoundHistoryQuery() {
        CommonPatientItemQuery query = new CommonPatientItemQuery();
        Sickbed sickbed = SickbedTemp.getInstance().getCurrentSickbed();
        query.setPatientId(sickbed.getPatientId());
        query.setVisitId(sickbed.getVisitId());
        //默认查询当日的
        query.setStartDateTime(TimeUtils.getStartDateTimeByTimeCode(NurseRoundHistoryActivity.DEFAULT_TIME_INTERVAL_CODE));
        query.setStopDateTime(TimeUtils.getStopDateTimeByTimeCode(NurseRoundHistoryActivity.DEFAULT_TIME_INTERVAL_CODE));
        return query;
    }
}
