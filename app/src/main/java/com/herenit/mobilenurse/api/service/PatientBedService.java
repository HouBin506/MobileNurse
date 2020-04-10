package com.herenit.mobilenurse.api.service;

import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.dict.NursingClassDict;
import com.herenit.mobilenurse.criteria.entity.dict.PatientConditionDict;
import com.herenit.mobilenurse.criteria.entity.submit.SickbedListQuery;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * author: HouBin
 * date: 2019/2/18 16:52
 * desc: 患者相关API 查询床位列表、患者详情等接口
 */
public interface PatientBedService {

    /**
     * 获取床位列表
     *
     * @param query
     * @return
     */
    @POST("mobileNurse/bedHandler/getBedBasicInformation")
    Observable<Result<List<Sickbed>>> getSickbedList(@Body SickbedListQuery query);

    /**
     * 获取患者详细信息
     *
     * @param patientId
     * @param groupCode
     * @return
     */
    @GET("mobileNurse/bedHandler/getBedDetailedInformation")
    Observable<Result<PatientInfo>> getPatientInfo(@Query(CommonConstant.FIELD_NAME_PATIENT_ID) String patientId,
                                                   @Query(CommonConstant.FIELD_NAME_GROUP_CODE) String groupCode);

    /**
     * 获取病情种类列表
     *
     * @return
     */
    @GET("mobileNurse/bedHandler/getPatientConditionDict")
    Observable<Result<List<PatientConditionDict>>> getPatientConditions();

    /**
     * 获取护理等级列表
     *
     * @return
     */
    @GET("mobileNurse/bedHandler/getNursingClassDict")
    Observable<Result<List<NursingClassDict>>> getNursingClassList();

}
