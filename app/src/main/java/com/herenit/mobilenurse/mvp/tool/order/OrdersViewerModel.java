package com.herenit.mobilenurse.mvp.tool.order;

import android.text.TextUtils;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.OrdersService;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.dict.OrderClassDict;
import com.herenit.mobilenurse.criteria.entity.submit.OrderListQuery;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/3/12 14:47
 * desc:医嘱详情的Model层
 */
@ActivityScope
public class OrdersViewerModel extends BaseModel implements OrdersViewerContract.Model {


    DaoSession mDaoSession;

    @Inject
    public OrdersViewerModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        mDaoSession = MobileNurseApplication.getInstance().getDaoSession();
    }

    /**
     * 网络查询患者医嘱列表
     *
     * @param query
     * @return
     */
    @Override
    public Observable<Result<List<Order>>> getOrderListByNetwork(OrderListQuery query) {
        return mRepositoryManager.obtainRetrofitService(OrdersService.class)
                .getPatientOrderList(query);
    }



    /**
     * 根据条件查询本地数据库中的医嘱列表
     *
     * @param
     * @return
     */
//    @Override
//    public List<Order> getCacheOrderList(OrderListQuery query) {
//        QueryBuilder<Order> where = createQueryBuilder(query);
//        if (where == null)
//            return null;
//        else
//            return where.list();
//    }


    /**
     * 根据查询条件更新当前查询到的医嘱列表
     *
     * @param orderList 数据源
     * @param query     查询条件
     */
//    @Override
//    public void updateQueryOrderList(List<Order> orderList, OrderListQuery query) {
//        //先根据查询条件删除对应的医嘱列表
//        deleteQueryOrderList(query);
//        //插入新的列表
//        if (orderList == null || orderList.isEmpty())
//            return;
//        for (Order order : orderList) {
//            mDaoSession.insertOrReplace(order);
//        }
//    }

    /**
     * 根据查询条件删除对应的本地医嘱列表
     *
     * @param query
//     */
//    private void deleteQueryOrderList(OrderListQuery query) {
//        QueryBuilder<Order> where = createQueryBuilder(query);
//        if (where == null)
//            mDaoSession.deleteAll(Order.class);
//        else
//            where.buildDelete().executeDeleteWithoutDetachingEntities();
//    }

    /**
     * 根据查询条件，创建greendao的QueryBuilder对象
     *
     * @param query
     * @return
     */
//    private QueryBuilder<Order> createQueryBuilder(OrderListQuery query) {
//        if (query == null)
//            return null;
//        List<WhereCondition> conditionList = new ArrayList<>();
//        if (!TextUtils.isEmpty(query.getPatientId())) {
//            conditionList.add(OrderDao.Properties.PatientId.eq(query.getPatientId()));
//        }
//        if (!TextUtils.isEmpty(query.getVisitId())) {
//            conditionList.add(OrderDao.Properties.VisitId.eq(query.getVisitId()));
//        }
//        String orderClass = query.getOrderClass();
//        if (!TextUtils.isEmpty(orderClass)) {
//            conditionList.add(OrderDao.Properties.OrderClass.eq(orderClass));
//        }
//        if (!TextUtils.isEmpty(query.getExecuteResult())) {
//            conditionList.add(OrderDao.Properties.ExecuteResult.eq(Integer.valueOf(query.getExecuteResult())));
//        }
//        if (!TextUtils.isEmpty(query.getRepeatIndicator())) {
//            conditionList.add(OrderDao.Properties.RepeatIndicator.eq(Integer.valueOf(query.getRepeatIndicator())));
//        }
//        if (query.getStartDateTime() != null) {
//            conditionList.add(OrderDao.Properties.PlanDateTime.ge(query.getStartDateTime()));//ge：  >=
//        }
//        if (query.getStopDateTime() != null) {
//            conditionList.add(OrderDao.Properties.PlanDateTime.le(query.getStopDateTime()));//le：<=
//        }
//        if (conditionList == null || conditionList.isEmpty())
//            return null;
//        QueryBuilder<Order> where = mDaoSession.queryBuilder(Order.class);
//        if (conditionList.size() == 1) {
//            where.where(conditionList.get(0));
//        } else {
//            WhereCondition whereCondition0 = conditionList.remove(0);
//            where.where(whereCondition0, conditionList.toArray(new WhereCondition[conditionList.size()]));
//        }
//        return where;
//    }

    /**
     * 获取医嘱分类
     *
     * @return
     */
    @Override
    public Observable<Result<List<OrderClassDict>>> getOrderClassList() {
        return mRepositoryManager.obtainRetrofitService(OrdersService.class)
                .getOrderClassList();
    }

}
