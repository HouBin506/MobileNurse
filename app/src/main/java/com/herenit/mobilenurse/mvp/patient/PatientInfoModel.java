package com.herenit.mobilenurse.mvp.patient;

import android.text.TextUtils;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.PatientBedService;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.DaoSession;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.PatientInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/3/4 15:01
 * desc:患者详情的M层
 */
@FragmentScope
public class PatientInfoModel extends BaseModel implements PatientInfoContract.Model {

    private DaoSession daoSession;

    @Inject
    public PatientInfoModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        daoSession = MobileNurseApplication.getInstance().getDaoSession();
    }


    /**
     * 网络获取患者详情
     *
     * @param patientId
     * @param groupCode
     * @return
     */
    @Override
    public Observable<Result<PatientInfo>> getPatientInfoByNetwork(String patientId, String groupCode) {
        return mRepositoryManager.obtainRetrofitService(PatientBedService.class)
                .getPatientInfo(patientId, groupCode);
    }

    /**
     * 保存患者详细信息
     *
     * @param patientInfo 数据源
     * @return
     */
    @Override
    public void savePatientInfo(PatientInfo patientInfo) {
        //TODO 先去掉本地存储，因为太乱，后面再重新实现
      /*  if (patientInfo == null)
            return;
        daoSession.insertOrReplace(patientInfo);*/
    }

    /**
     * 获取缓存中的患者详情
     *
     * @param patientId
     * @param visitId
     * @return
     */
    @Override
    public PatientInfo getPatientInfoByCache(String patientId, String visitId) {
        if (TextUtils.isEmpty(patientId) || TextUtils.isEmpty(visitId))
            return null;
        QueryBuilder<PatientInfo> queryBuilder = daoSession.queryBuilder(PatientInfo.class)
                .where(PatientInfoDao.Properties.PatientId.eq(patientId), PatientInfoDao.Properties.VisitId.eq(visitId));
        List<PatientInfo> patientInfoList = queryBuilder.list();
        if (patientInfoList != null && !patientInfoList.isEmpty())
            return patientInfoList.get(0);
        return null;
    }
}
