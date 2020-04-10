package com.herenit.mobilenurse.mvp.other;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.OrdersService;
import com.herenit.mobilenurse.api.service.PatientBedService;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.submit.OrdersExecute;
import com.herenit.mobilenurse.datastore.cache.PatientBedCache;
import com.herenit.mobilenurse.mvp.orders.OrdersInfoContract;

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
 * desc:“其它功能”的Model层
 */
@FragmentScope
public class OtherModel extends BaseModel implements OtherContract.Model {

    @Inject
    public OtherModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
}
