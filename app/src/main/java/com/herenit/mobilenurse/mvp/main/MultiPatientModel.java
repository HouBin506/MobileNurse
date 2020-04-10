package com.herenit.mobilenurse.mvp.main;

import android.text.TextUtils;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.PatientBedService;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.criteria.entity.submit.SickbedListQuery;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.DaoSession;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/1/29 16:46
 * desc: 主界面的Model
 */
@ActivityScope
public class MultiPatientModel extends BaseModel implements MultiPatientContract.Model {
    private DaoSession daoSession;

    @Inject
    public MultiPatientModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        daoSession = MobileNurseApplication.getInstance().getDaoSession();
    }

    /**
     * 根据UserId获取缓存中的User
     *
     * @param userId
     * @return
     */
    @Override
    public User getCacheUser(String userId) {
        if (TextUtils.isEmpty(userId))
            return null;
        List<User> oldUsers = daoSession.queryRaw(User.class, "where USER_ID = ?", userId);
        if (oldUsers == null || oldUsers.isEmpty())
            return null;
        return oldUsers.get(0);
    }

    /**
     * 获取床位列表
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
     * 获取本地数据库中缓存的床位列表
     *
     * @param groupId 科室、护理单元代码
     * @return
     */
    @Override
    public List<Sickbed> getCacheSickbedList(String groupId) {
        if (TextUtils.isEmpty(groupId))
            return null;
        return daoSession.queryRaw(Sickbed.class, "where DEPT_CODE = ?", groupId);
    }

    /**
     * 更新本地缓存的某科室、护理单元的床位列表
     *
     * @param sickbedList 数据源
     * @param groupCode   科室、护理单元代码
     */
    @Override
    public void updateCacheGroupSickbedList(List<Sickbed> sickbedList, String groupCode) {
        //TODO 先去掉本地存储，因为太乱，后面再重新实现
        //先批量删除科室的所有的床位列表
       /* QueryBuilder<Sickbed> queryBuilder = daoSession.queryBuilder(Sickbed.class)
                .where(SickbedDao.Properties.DeptCode.eq(groupCode));
        DeleteQuery<Sickbed> deleteQuery = queryBuilder.buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
        //然后存入新的床位列表
        if (sickbedList == null)
            return;
        for (Sickbed sickbed : sickbedList) {
            daoSession.insertOrReplace(sickbed);
        }*/
    }
}
