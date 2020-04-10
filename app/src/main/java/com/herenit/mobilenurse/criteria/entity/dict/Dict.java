package com.herenit.mobilenurse.criteria.entity.dict;

import org.greenrobot.greendao.annotation.Entity;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/4/3 15:59
 * desc:常用的一些字典，比如医嘱类别、执行结果、护理等级、病情等级、模块等
 */
public class Dict {
    private List<ModuleDict> mnModuleDictTOBaseList;
    private List<NursingClassDict> mnNursingClassDictVPOJOList;
    private List<PatientConditionDict> mnPatientConditionDictVPOJOList;
    private List<OrderClassDict> mnOrderClassDictVPOJOList;
    private List<ExecuteResultDict> mnExecuteResultDictTOList;
    private List<AckIndicatorDict> mnAckIndicatorDictVPOJOList;
    private List<EmergencyIndicatorDict> mnEmergencyIndicatorDictVPOJOList;
    private List<NurseRoundDict> mnPatrolDictTOTwoList;

    public List<ModuleDict> getMnModuleDictTOBaseList() {
        return mnModuleDictTOBaseList;
    }

    public void setMnModuleDictTOBaseList(List<ModuleDict> mnModuleDictTOBaseList) {
        this.mnModuleDictTOBaseList = mnModuleDictTOBaseList;
    }

    public List<NursingClassDict> getMnNursingClassDictVPOJOList() {
        return mnNursingClassDictVPOJOList;
    }

    public void setMnNursingClassDictVPOJOList(List<NursingClassDict> mnNursingClassDictVPOJOList) {
        this.mnNursingClassDictVPOJOList = mnNursingClassDictVPOJOList;
    }

    public List<PatientConditionDict> getMnPatientConditionDictVPOJOList() {
        return mnPatientConditionDictVPOJOList;
    }

    public void setMnPatientConditionDictVPOJOList(List<PatientConditionDict> mnPatientConditionDictVPOJOList) {
        this.mnPatientConditionDictVPOJOList = mnPatientConditionDictVPOJOList;
    }

    public List<OrderClassDict> getMnOrderClassDictVPOJOList() {
        return mnOrderClassDictVPOJOList;
    }

    public void setMnOrderClassDictVPOJOList(List<OrderClassDict> mnOrderClassDictVPOJOList) {
        this.mnOrderClassDictVPOJOList = mnOrderClassDictVPOJOList;
    }

    public List<ExecuteResultDict> getMnExecuteResultDictTOList() {
        return mnExecuteResultDictTOList;
    }

    public void setMnExecuteResultDictTOList(List<ExecuteResultDict> mnExecuteResultDictTOList) {
        this.mnExecuteResultDictTOList = mnExecuteResultDictTOList;
    }

    public List<AckIndicatorDict> getMnAckIndicatorDictVPOJOList() {
        return mnAckIndicatorDictVPOJOList;
    }

    public void setMnAckIndicatorDictVPOJOList(List<AckIndicatorDict> mnAckIndicatorDictVPOJOList) {
        this.mnAckIndicatorDictVPOJOList = mnAckIndicatorDictVPOJOList;
    }

    public List<EmergencyIndicatorDict> getMnEmergencyIndicatorDictVPOJOList() {
        return mnEmergencyIndicatorDictVPOJOList;
    }

    public void setMnEmergencyIndicatorDictVPOJOList(List<EmergencyIndicatorDict> mnEmergencyIndicatorDictVPOJOList) {
        this.mnEmergencyIndicatorDictVPOJOList = mnEmergencyIndicatorDictVPOJOList;
    }

    public List<NurseRoundDict> getMnPatrolDictTOTwoList() {
        return mnPatrolDictTOTwoList;
    }

    public void setMnPatrolDictTOTwoList(List<NurseRoundDict> mnPatrolDictTOTwoList) {
        this.mnPatrolDictTOTwoList = mnPatrolDictTOTwoList;
    }
}
