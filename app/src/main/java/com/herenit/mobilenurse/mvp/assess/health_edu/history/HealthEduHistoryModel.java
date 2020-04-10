package com.herenit.mobilenurse.mvp.assess.health_edu.history;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.Api;
import com.herenit.mobilenurse.api.service.HealthEduService;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.HealthEduHistoryItem;
import com.herenit.mobilenurse.criteria.entity.MultiListMenuItem;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.DaoSession;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.MultiListMenuItemDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * author: HouBin
 * date: 2019/8/15 10:52
 * desc:健康宣教M层
 */
@ActivityScope
public class HealthEduHistoryModel extends BaseModel implements HealthEduHistoryContract.Model {


    @Inject
    public HealthEduHistoryModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    /**
     * 获取健康教育历史记录数据
     *
     * @param patientId
     * @param visitId
     * @return
     */
    @Override
    public Observable<Result<List<HealthEduHistoryItem>>> loadHealthEduHistory(String patientId, String visitId) {
        return mRepositoryManager.obtainRetrofitService(HealthEduService.class)
                .getHealthEduHistoryList(patientId, visitId).map(new Function<Result<List<HealthEduHistoryItem>>, Result<List<HealthEduHistoryItem>>>() {
                    @Override
                    public Result<List<HealthEduHistoryItem>> apply(Result<List<HealthEduHistoryItem>> listResult) throws Exception {
                        //返回之前，给历史数据赋值（设置选中项的名称显示字符串）
                        setSelectedItemNameList(listResult.getData());
                        return listResult;
                    }
                });
    }

    /**
     * 删除宣教历史记录
     *
     * @param docId
     * @return
     */
    @Override
    public Observable<Result> deleteHealthEduHistory(String docId) {
        return mRepositoryManager.obtainRetrofitService(HealthEduService.class)
                .deleteHealthEduHistory(docId);
    }


    /**
     * 给历史记录数据赋值选中的项目列表字符串
     *
     * @param itemList
     */
    private void setSelectedItemNameList(List<HealthEduHistoryItem> itemList) {
        if (itemList == null || itemList.isEmpty())
            return;
        for (HealthEduHistoryItem item : itemList) {
            item.setSelectedItemNameList(MultiListMenuItem.buildMultiLevelItemName(item.getSelectedItemList()));
        }
    }
}
