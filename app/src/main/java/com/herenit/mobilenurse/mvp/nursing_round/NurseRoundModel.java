package com.herenit.mobilenurse.mvp.nursing_round;

import android.appwidget.AppWidgetProviderInfo;

import com.alibaba.fastjson.JSON;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.Api;
import com.herenit.mobilenurse.api.service.NurseRoundService;
import com.herenit.mobilenurse.app.utils.FileUtils;
import com.herenit.mobilenurse.criteria.entity.NurseRoundItem;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.OrderListQuery;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewGroup;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/8/6 17:04
 * desc: 护理巡视 M层
 */
@FragmentScope
public class NurseRoundModel extends BaseModel implements NurseRoundContract.Model {

    @Inject
    public NurseRoundModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<NurseRoundViewGroup>> getNurseRoundItemList(OrderListQuery query) {
        return mRepositoryManager.obtainRetrofitService(NurseRoundService.class)
                .getNurseRoundItemList(query);
     /*   Result<NurseRoundViewGroup> result = new Result<>();
        try {
            String json = FileUtils.getAssetsToString("test_nurse_round_item_list.json");
            NurseRoundViewGroup nurseRoundView = JSON.parseObject(json, NurseRoundViewGroup.class);
            result.setCode(Api.CODE_SUCCESS);
            result.setData(nurseRoundView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Observable.just(result);*/
    }

    /**
     * 提交巡视数据
     *
     * @param dataList
     * @return
     */
    @Override
    public Observable<Result> commitNurseRoundData(List<NurseRoundItem> dataList) {
        return mRepositoryManager.obtainRetrofitService(NurseRoundService.class)
                .commitNurseRoundDataList(dataList);
    }
}
