package com.herenit.mobilenurse.mvp.operation;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.OperationService;
import com.herenit.mobilenurse.criteria.entity.OperationScheduled;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.OperationScheduledQuery;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/5/22 15:45
 * desc:手术安排M层
 */
@FragmentScope
public class OperationScheduledModel extends BaseModel implements OperationScheduledContract.Model {

    @Inject
    public OperationScheduledModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    /**
     * 查询手术安排列表
     *
     * @param query
     * @return
     */
    @Override
    public Observable<Result<List<OperationScheduled>>> loadOperationScheduledList(OperationScheduledQuery query) {
        return mRepositoryManager.obtainRetrofitService(OperationService.class)
                .loadOperationScheduledList(query);
    }
}
