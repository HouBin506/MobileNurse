package com.herenit.mobilenurse.mvp.nursing_round;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.NurseRoundService;
import com.herenit.mobilenurse.criteria.entity.NurseRoundItem;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.CommonPatientItemQuery;
import com.herenit.mobilenurse.criteria.entity.submit.OrderListQuery;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewGroup;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/8/6 17:04
 * desc: 护理巡视
 * 历史记录M层
 */
@ActivityScope
public class NurseRoundHistoryModel extends BaseModel implements NurseRoundHistoryContract.Model {

    @Inject
    public NurseRoundHistoryModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<NurseRoundItem>>> getNurseRoundHistoryList(CommonPatientItemQuery query) {
        return mRepositoryManager.obtainRetrofitService(NurseRoundService.class)
                .getNurseRoundHistory(query);
    }

    @Override
    public Observable<Result> deleteNurseRoundData(List<NurseRoundItem> deleteDataList) {
        return mRepositoryManager.obtainRetrofitService(NurseRoundService.class)
                .deleteNurseRoundData(deleteDataList);
    }
}
