package com.herenit.mobilenurse.datastore.cache;

import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.constant.ValueConstant;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.Result;

import java.util.List;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.DynamicKeyGroup;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.EvictDynamicKeyGroup;
import io.rx_cache2.ProviderKey;
import io.rx_cache2.Reply;

/**
 * author: HouBin
 * date: 2019/3/7 10:03
 * desc:医嘱信息缓存
 */
public interface OrderCache {


    /**
     * 获取医嘱列表
     *
     * @param observable      数据源
     * @param keyGroup        存储的主键
     * @param evictDynamicKey 是否清除缓存
     * @return
     */
    @ProviderKey(KeyConstant.KEY_PROVIDER_ORDERS)
    Observable<Reply<List<Order>>> getPatientOrderList(Observable<List<Order>> observable, DynamicKey keyGroup, EvictDynamicKey evictDynamicKey);
}
