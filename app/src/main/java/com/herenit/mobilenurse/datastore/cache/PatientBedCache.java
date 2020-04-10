package com.herenit.mobilenurse.datastore.cache;

import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Sickbed;

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
 * date: 2019/2/20 14:10
 * desc:病床、患者信息的缓存
 */
public interface PatientBedCache {

    /**
     * 根据UserId和GroupCode 缓存床位列表
     *
     * @param observable    数据源
     * @param keyGroup      缓存的key，由userId和科室代码group构成
     * @param evictKeyGroup 是否驱逐缓存
     * @return
     */
    @ProviderKey(KeyConstant.KEY_PROVIDER_SICKBED)
    Observable<Reply<List<Sickbed>>> getSickbedList(Observable<List<Sickbed>> observable,
                                                    DynamicKeyGroup keyGroup, EvictDynamicKeyGroup evictKeyGroup);

    /**
     * 获取患者详情
     *
     * @param observable      数据源
     * @param dynamicKey      主键 采用patientId_visitId，如：156566545_3
     * @param evictDynamicKey 是否清除缓存
     * @return
     */
    @ProviderKey(KeyConstant.KEY_PROVIDER_PATIENT)
    Observable<Reply<List<PatientInfo>>> getPatientInfo(Observable<List<PatientInfo>> observable, DynamicKey dynamicKey, EvictDynamicKey evictDynamicKey);
}
