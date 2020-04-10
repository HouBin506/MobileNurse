package com.herenit.mobilenurse.mvp.main;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.datastore.cache.PatientBedCache;
import com.herenit.mobilenurse.datastore.cache.UserCache;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.DynamicKeyGroup;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.EvictDynamicKeyGroup;
import io.rx_cache2.Reply;

/**
 * author: HouBin
 * date: 2019/3/1 13:44
 * desc: 单患者的Model层
 */
@ActivityScope
public class SinglePatientModel extends BaseModel implements SinglePatientContract.Model {
    @Inject
    public SinglePatientModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    /**
     * 读取缓存中的科室列表（病区列表）
     *
     * @param userId 用户账号
     * @return
     */
    @Override
    public Observable<List<User.MnUserVsGroupVPOJOListBean>> getCacheUserGroupList(@NonNull String userId) {
        List<User> userList = new ArrayList<>();
        DynamicKey dynamicKey = new DynamicKey(userId);
        EvictDynamicKey evictDynamicKey = new EvictDynamicKey(false);
        return mRepositoryManager.obtainCacheService(UserCache.class)
                .getUser(Observable.just(userList), dynamicKey, evictDynamicKey)
                .map(new Function<Reply<List<User>>, List<User.MnUserVsGroupVPOJOListBean>>() {
                    @Override
                    public List<User.MnUserVsGroupVPOJOListBean> apply(Reply<List<User>> listReply) throws Exception {
                        List<User> result = listReply.getData();
                        if (result != null && result.size() > 0)
                            return result.get(0).getMnUserVsGroupVPOJOList();
                        return null;
                    }
                });
    }

    /**
     * 获取缓存中的床位列表，如果当前登录账号具有床位分组工作的概念，则需要从床位列表中筛选出分组中的床位返回，
     * 返回的床位列表中需要保证不是空床，因为空床是无法做单患者操作的，所以需要将无患者的床位筛选掉
     *
     * @param userId    账号
     * @param groupCode 科室代码或者病区代码
     * @return
     */
    @Override
    public Observable<List<Sickbed>> getCacheGroupPatientList(@NonNull String userId, @NonNull String groupCode) {
        List<Sickbed> list = new ArrayList<>();
        DynamicKeyGroup dynamicKeyGroup = new DynamicKeyGroup(userId, groupCode);
        EvictDynamicKeyGroup evictDynamicKeyGroup = new EvictDynamicKeyGroup(false);
        return mRepositoryManager.obtainCacheService(PatientBedCache.class)
                .getSickbedList(Observable.just(list), dynamicKeyGroup, evictDynamicKeyGroup)
                .map(new Function<Reply<List<Sickbed>>, List<Sickbed>>() {
                    @Override
                    public List<Sickbed> apply(Reply<List<Sickbed>> listReply) throws Exception {
                        List<Sickbed> result = listReply.getData();
                        return filterWorkSickbedList(result);
                    }
                });
    }

    /**
     * 筛选出当前登陆者需要的床位列表
     *
     * @param result
     * @return
     */
    private List<Sickbed> filterWorkSickbedList(List<Sickbed> result) {
        List<Sickbed> sickbedList = new ArrayList<>();
        if (result == null || result.isEmpty())
            return sickbedList;
//        if(如果当前账号有按组划分工作的概念){
        //TODO 获取按组划分中的床位列表
//        }
        for (Sickbed sickbed : result) {
            if (!sickbed.isEmptyBed())
                sickbedList.add(sickbed);
        }
        return sickbedList;
    }
}
