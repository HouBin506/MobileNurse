package com.herenit.mobilenurse.datastore.cache;

import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.criteria.entity.submit.Login;

import java.util.List;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.ProviderKey;
import io.rx_cache2.Reply;

/**
 * author: HouBin
 * date: 2019/1/18 10:16
 * desc: 使用RxCache对User进行缓存
 */
public interface UserCache {

    /**
     * User的缓存方法
     *
     * @param userObservable 传入的User
     * @param userIdKey      当前要缓存的User的userId包装的key 作为删除存储的标识
     * @param evictUser      是否驱逐数据，true则会直接删除userId绑定的数据，并获取新的数据存储进去，false则不会直接删除，会正常的加载数据
     * @return
     */
    @ProviderKey(KeyConstant.KEY_PROVIDER_USER)
    Observable<Reply<List<User>>> getUser(Observable<List<User>> userObservable, DynamicKey userIdKey, EvictDynamicKey evictUser);

    /**
     * Login的缓存方法，用于登录账号密码的记忆
     *
     * @param loginListObservable 登录账号信息列表
     * @param evictProvider       是否直接删除掉当前数据，重新 获取
     * @return
     */
    @ProviderKey(KeyConstant.KEY_PROVIDER_LOGIN)
    Observable<List<Login>> getLogin(Observable<List<Login>> loginListObservable, EvictProvider evictProvider);
}
