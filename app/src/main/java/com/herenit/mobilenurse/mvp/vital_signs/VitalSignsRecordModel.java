package com.herenit.mobilenurse.mvp.vital_signs;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.VitalSignsService;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.submit.VitalSignsHistoryQuery;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsViewItem;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/4/10 10:07
 * desc:生命体征的Model类
 */
@FragmentScope
public class VitalSignsRecordModel extends BaseModel implements VitalSignsRecordContract.Model {
    @Inject
    public VitalSignsRecordModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<VitalSignsViewItem>>> getPatientVitalSignsList(String groupCode, String patientId, String visitId) {
        return mRepositoryManager.obtainRetrofitService(VitalSignsService.class)
                .getPatientVitalSignsList(groupCode);
    }

    /**
     * 提交体征数据
     *
     * @param vitalSignsList
     * @return
     */
    @Override
    public Observable<Result> postVitalSignsRecord(List<VitalSignsItem> vitalSignsList) {
        return mRepositoryManager.obtainRetrofitService(VitalSignsService.class)
                .postVitalSignsRecord(vitalSignsList);
    }

    /**
     * 查询体征历史记录
     *
     * @param query
     * @return
     */
    @Override
    public Observable<Result<List<VitalSignsItem>>> getVitalSignsHistoryList(VitalSignsHistoryQuery query) {
        return mRepositoryManager.obtainRetrofitService(VitalSignsService.class)
                .getVitalSignsHistoryList(query);
    }


}
