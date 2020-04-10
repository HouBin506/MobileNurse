package com.herenit.mobilenurse.mvp.assess.health_edu;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.Api;
import com.herenit.mobilenurse.api.service.HealthEduService;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
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
@FragmentScope
public class HealthEduModel extends BaseModel implements HealthEduContract.Model {

    private DaoSession daoSession;

    @Inject
    public HealthEduModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        daoSession = MobileNurseApplication.getInstance().getDaoSession();
    }

    /**
     * 获取健康教育项目列表
     *
     * @param userId
     * @return
     */
    @Override
    public Observable<Result<List<MultiListMenuItem>>> getHealthEduItemList(String userId) {
        return mRepositoryManager.obtainRetrofitService(HealthEduService.class)
                .getHealthEduItemStyle();
//        return getHealthEduItemListTestData(userId).map(new Function<Result<List<MultiListMenuItem>>, Result<List<MultiListMenuItem>>>() {
//            @Override
//            public Result<List<MultiListMenuItem>> apply(Result<List<MultiListMenuItem>> listResult) throws Exception {
//                List<MultiListMenuItem> dataList = listResult.getData();
//                updateCacheHealthEduMultiList(dataList);
//                return listResult;
//            }
//        });
    }

    /**
     * 获取测试数据
     *
     * @param userId
     * @return
     */
    private Observable<Result<List<MultiListMenuItem>>> getHealthEduItemListTestData(String userId) {
        List<MultiListMenuItem> data = new ArrayList<>();
        for (int x = 0; x < 15; x++) {
            MultiListMenuItem level1Item = new MultiListMenuItem();
            String level1ItemCode = (x + 100) + "";
            level1Item.setCode(level1ItemCode);
            level1Item.setPath(level1ItemCode);
            level1Item.setContent(level1Item.getPath() + "一级菜单内容" + x);
            if (x % 5 == 0) {
                level1Item.setName(x + "一级菜单....这个菜单稍微有点长");
            } else {
                level1Item.setName(x + "一级菜单");
            }
            data.add(level1Item);
            for (int y = 0; y < (x % 5) * 5; y++) {
                level1Item.setHasSubItem(true);
                MultiListMenuItem level2Item = new MultiListMenuItem();
                String level2ItemCode = x + "-" + (y + 1000) + "";
                level2Item.setCode(level2ItemCode);
                level2Item.setPath(level1ItemCode + "/" + level2ItemCode);
                level2Item.setContent(level2Item.getPath() + "二级菜单内容" + y);
                if (y % 3 == 0) {
                    level2Item.setName(x + "->" + y + "二级菜单....这个菜单稍微有点长");
                } else {
                    level2Item.setName(x + "->" + y + "二级菜单");
                }
                data.add(level2Item);
                for (int z = 0; z < (y % 3) * 6; z++) {
                    level2Item.setHasSubItem(true);
                    MultiListMenuItem level3Item = new MultiListMenuItem();
                    String level3ItemCode = x + "-" + y + "-" + (z + 10000) + "";
                    level3Item.setCode(level3ItemCode);
                    level3Item.setPath(level1ItemCode + "/" + level2ItemCode + "/" + level3ItemCode);
                    level3Item.setContent(level3Item.getPath() + "三级菜单内容" + z);
                    level3Item.setSelectable(true);
                    if (y % 4 == 0) {
                        level3Item.setName(x + "->" + y + "->" + z + "三级菜单....这个菜单稍微有点长");
                    } else {
                        level3Item.setName(x + "->" + y + "->" + z + "三级菜单");
                    }
                    data.add(level3Item);
                }
                if (level2Item.isHasSubItem()) {//有子列表，不可选中
                    level2Item.setSelectable(true);
                } else {
                    level2Item.setSelectable(false);
                }
            }
            if (level1Item.isHasSubItem()) {//有子列表，不可选中
                level1Item.setSelectable(true);
            } else {
                level1Item.setSelectable(false);
            }
        }
        Result<List<MultiListMenuItem>> result = new Result<>();
        result.setCode(Api.CODE_SUCCESS);
        result.setData(data);
        return Observable.just(result);

    }

    /**
     * 更新本地缓存的健康教育多级列表
     */
    private void updateCacheHealthEduMultiList(List<MultiListMenuItem> itemList) {
        //先删除本地健康教育的多级列表
        QueryBuilder<MultiListMenuItem> deleteBuilder = daoSession.queryBuilder(MultiListMenuItem.class);
        deleteBuilder.where(MultiListMenuItemDao.Properties.DocType.eq(CommonConstant.DOC_TYPE_HEALTH_EDU))
                .buildDelete().executeDeleteWithoutDetachingEntities();
        //添加新的列表
        if (itemList != null && !itemList.isEmpty()) {
            for (MultiListMenuItem item : itemList) {
                item.setDocType(CommonConstant.DOC_TYPE_HEALTH_EDU);
                daoSession.insertOrReplace(item);
            }
        }
    }

}
