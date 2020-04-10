package com.herenit.mobilenurse.mvp.vital_signs;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.arms.mvp.IModel;
import com.herenit.mobilenurse.api.Api;
import com.herenit.mobilenurse.api.service.VitalSignsService;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.submit.VitalSignsHistoryQuery;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsChartData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * author: HouBin
 * date: 2019/6/3 11:18
 * desc:体征趋势图Model
 */
@ActivityScope
public class VitalSignsChartModel extends BaseModel implements VitalSignsChartContract.Model {

    @Inject
    public VitalSignsChartModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    /**
     * 加载体征趋势图显示的数据
     *
     * @param query
     * @return
     */
    @Override
    public Observable<Result<VitalSignsChartData>> loadVitalSignsChartData(VitalSignsHistoryQuery query) {
        return mRepositoryManager.obtainRetrofitService(VitalSignsService.class)
                .getVitalSignsHistoryList(query)
                .map(new Function<Result<List<VitalSignsItem>>, Result<VitalSignsChartData>>() {
                    @Override
                    public Result<VitalSignsChartData> apply(Result<List<VitalSignsItem>> listResult) throws Exception {
                        Result<VitalSignsChartData> result = new Result();
                        result.setData(VitalSignsHelper.convertVitalToChartData(listResult.getData()));
                        result.setCode(Api.CODE_SUCCESS);
                        return result;
                    }
                });
    }
}
