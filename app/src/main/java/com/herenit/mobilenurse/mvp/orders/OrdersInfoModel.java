package com.herenit.mobilenurse.mvp.orders;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.OrdersService;
import com.herenit.mobilenurse.api.service.PatientBedService;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.OrdersExecute;
import com.herenit.mobilenurse.datastore.cache.PatientBedCache;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;

/**
 * author: HouBin
 * date: 2019/3/12 14:47
 * desc:医嘱详情的Model层
 */
@ActivityScope
public class OrdersInfoModel extends BaseModel implements OrdersInfoContract.Model {

    @Inject
    public OrdersInfoModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
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
     * 获取缓存中的患者详情
     *
     * @param patientId
     * @param visitId
     * @return
     */
    @Override
    public Observable<PatientInfo> getPatientInfoByCache(String patientId, String visitId) {
        List<PatientInfo> list = new ArrayList<>();
        DynamicKey dynamicKey = new DynamicKey(patientId + "_" + visitId);
        EvictDynamicKey evictDynamicKey = new EvictDynamicKey(false);
        return mRepositoryManager.obtainCacheService(PatientBedCache.class)
                .getPatientInfo(Observable.just(list), dynamicKey, evictDynamicKey)
                .map(new Function<Reply<List<PatientInfo>>, PatientInfo>() {
                    @Override
                    public PatientInfo apply(Reply<List<PatientInfo>> listReply) throws Exception {
                        List<PatientInfo> result = listReply.getData();
                        if (result != null && !result.isEmpty())
                            return result.get(0);
                        return null;
                    }
                });
    }

    /**
     * 根据医嘱条码，获取对应的要执行的医嘱列表
     *
     * @param patientId 患者ID
     * @param barCode   医嘱码
     * @param type      医嘱码类别
     * @return
     */
    @Override
    public Observable<Result<List<Order>>> getPatientPerformOrdersByBarCode(String patientId, String barCode, String type) {
        return mRepositoryManager.obtainRetrofitService(OrdersService.class)
                .getPatientPerformOrdersByBarCode(patientId, barCode, type);
    }


    /**
     * 执行医嘱
     *
     * @param executeList
     * @return
     */
    @Override
    public Observable<Result> executeOrders(List<OrdersExecute> executeList) {
        return mRepositoryManager.obtainRetrofitService(OrdersService.class)
                .executeOrders(executeList);
    }

    /**
     * 医嘱撤回
     *
     * @param execute
     * @return
     */
    @Override
    public Observable<Result> revokeOrders(OrdersExecute execute) {
        return mRepositoryManager.obtainRetrofitService(OrdersService.class)
                .revokeOrders(execute);
    }
}
