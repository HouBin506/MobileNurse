package com.herenit.mobilenurse.mvp.assess.health_edu.content;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.Api;
import com.herenit.mobilenurse.api.service.HealthEduService;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.app.utils.FileUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.AssessModel;
import com.herenit.mobilenurse.criteria.entity.HealthEduAssessModel;
import com.herenit.mobilenurse.criteria.entity.MultiListMenuItem;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.AssessParam;
import com.herenit.mobilenurse.criteria.entity.submit.HealthEduAssessParam;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;
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
public class HealthEduContentModel extends BaseModel implements HealthEduContentContract.Model {

    DaoSession daoSession;

    @Inject
    public HealthEduContentModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        daoSession = MobileNurseApplication.getInstance().getDaoSession();
    }

    /**
     * 获取健康教育结果
     *
     * @return
     */
    @Override
    public Observable<Result<HealthEduAssessModel>> getHealthEduResult(String docId) {
        return mRepositoryManager.obtainRetrofitService(HealthEduService.class)
                .getHealthEduResult(docId);
//        return getHealthEduResultStyleTestData();
    }

    /**
     * 加载缓存中的宣教项目列表
     *
     * @return
     */
    private List<MultiListMenuItem> loadCacheHealthEduItemList() {
        QueryBuilder<MultiListMenuItem> queryBuilder = daoSession.queryBuilder(MultiListMenuItem.class)
                .where(MultiListMenuItemDao.Properties.DocType.eq(CommonConstant.DOC_TYPE_HEALTH_EDU));

        return queryBuilder.build().list();
    }

    /**
     * 保存健康教育数据
     *
     * @param param
     * @return
     */
    @Override
    public Observable<Result> saveOrUpdateHealthEduContent(HealthEduAssessParam param) {
        return mRepositoryManager.obtainRetrofitService(HealthEduService.class)
                .saveOrUpdateHealthEduContent(param);
    }

    /**
     * 获取健康教育结果样式 测试数据
     *
     * @return
     */
    private Observable<Result<List<AssessViewItem>>> getHealthEduResultStyleTestData() {
        List<AssessViewItem> data = new ArrayList<>();
        String json = FileUtils.getAssetsToString("test_health_edu_content.json");
        if (!TextUtils.isEmpty(json)) {
            List<AssessViewItem> dataList = JSON.parseArray(json, AssessViewItem.class);
            if (dataList != null)
                data.addAll(dataList);
        }
        Result<List<AssessViewItem>> result = new Result<>();
        result.setCode(Api.CODE_SUCCESS);
        result.setData(data);
        return Observable.just(result);
    }
}
