package com.herenit.mobilenurse.mvp.sickbed;

import android.text.TextUtils;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.PatientBedService;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.dict.NursingClassDict;
import com.herenit.mobilenurse.criteria.entity.dict.PatientConditionDict;
import com.herenit.mobilenurse.criteria.entity.submit.SickbedListQuery;
import com.herenit.mobilenurse.datastore.cache.PatientBedCache;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.DaoSession;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKeyGroup;
import io.rx_cache2.EvictDynamicKeyGroup;
import io.rx_cache2.Reply;

/**
 * author: HouBin
 * date: 2019/2/20 13:47
 * desc: 床位列表Mode层
 */
@FragmentScope
public class SickbedListModel extends BaseModel implements SickbedListContract.Model {

    private DaoSession daoSession;

    @Inject
    public SickbedListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        daoSession = MobileNurseApplication.getInstance().getDaoSession();
    }

    /**
     * 获取病情类型列表
     *
     * @return
     */
    @Override
    public Observable<Result<List<PatientConditionDict>>> getPatientConditionList() {
        return mRepositoryManager.obtainRetrofitService(PatientBedService.class)
                .getPatientConditions();
    }

    /**
     * 获取护理等级列表
     *
     * @return
     */
    @Override
    public Observable<Result<List<NursingClassDict>>> getNursingClassList() {
        return mRepositoryManager.obtainRetrofitService(PatientBedService.class)
                .getNursingClassList();
    }

    /**
     * 查询患者列表
     *
     * @param query
     * @return
     */
    @Override
    public Observable<Result<List<Sickbed>>> getSickbedList(SickbedListQuery query) {
        return mRepositoryManager.obtainRetrofitService(PatientBedService.class)
                .getSickbedList(query);
    }

    /**
     * 获取缓存中的床位列表
     *
     * @param userId  当前账号
     * @param groupId 当前科室
     * @return
     */
    @Override
    public Observable<List<Sickbed>> getCacheSickbedList(String userId, String groupId) {
        List<Sickbed> sickbedList = new ArrayList<>();

        return mRepositoryManager.obtainCacheService(PatientBedCache.class)
                .getSickbedList(Observable.just(sickbedList), new DynamicKeyGroup(userId, groupId), new EvictDynamicKeyGroup(false))
                .map(new Function<Reply<List<Sickbed>>, List<Sickbed>>() {
                    @Override
                    public List<Sickbed> apply(Reply<List<Sickbed>> listReply) throws Exception {
                        return listReply.getData();
                    }
                });
    }

    /**
     * 根据科室代码获取床位列表
     *
     * @param groupId
     * @return
     */
    @Override
    public List<Sickbed> getCacheSickbedList(String groupId) {
        if (TextUtils.isEmpty(groupId))
            return null;
        return daoSession.queryRaw(Sickbed.class, "where DEPT_CODE = ?", groupId);
    }

    @Override
    public void updateSickbedList(List<Sickbed> data) {
        //TODO 先去掉本地存储，因为太乱，后面再重新实现
       /* if (data == null || data.isEmpty())
            return;
        for (Sickbed sickbed : data) {
            daoSession.insertOrReplace(sickbed);
        }*/
    }


}
