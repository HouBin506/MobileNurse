package com.herenit.mobilenurse.datastore.tempcache;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.criteria.common.NameCode;
import com.herenit.mobilenurse.criteria.constant.ValueConstant;
import com.herenit.mobilenurse.criteria.entity.dict.AckIndicatorDict;
import com.herenit.mobilenurse.criteria.entity.dict.EmergencyIndicatorDict;
import com.herenit.mobilenurse.criteria.entity.dict.ExecuteResultDict;
import com.herenit.mobilenurse.criteria.entity.dict.ModuleDict;
import com.herenit.mobilenurse.criteria.entity.dict.NurseRoundDict;
import com.herenit.mobilenurse.criteria.entity.dict.NursingClassDict;
import com.herenit.mobilenurse.criteria.entity.dict.OrderClassDict;
import com.herenit.mobilenurse.criteria.entity.dict.PatientConditionDict;
import com.herenit.mobilenurse.criteria.enums.ModuleEnum;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.DaoSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * author: HouBin
 * date: 2019/4/4 9:51
 * desc:常用字典表的全局临时存储
 */
public class DictTemp {
    /**
     * 多患者界面本来具有的所有功能模块，由客户端开发拥有，不一定要显示出来，具体要看服务器端的返回
     */
    private static Map<String, ModuleEnum> multiPatientModuleAll;
    /**
     * 多患者界面要显示的功能模块，根据服务器端返回的模块列表，结合客户端现有的功能模块来决定显示哪些功能模块
     */
    private Map<String, ModuleDict> multiPatientModuleShow = new LinkedHashMap<>();

    /**
     * 多患者界面要显示的功能模块，根据服务器端返回的模块列表，结合客户端现有的功能模块来决定显示哪些功能模块
     * 这里添加的是次要模块，也就是说不是在主界面底部切换的模块
     */
    private Map<String, ModuleDict> secondMultiPatientModuleShow = new LinkedHashMap<>();

    /**
     * 单患者界面本来具有的所有功能模块，由客户端开发拥有，不一定要显示出来，具体要看服务器端的返回
     */
    private static Map<String, ModuleEnum> singlePatientModuleAll;
    /**
     * 单患者界面要显示的功能模块，根据服务器端返回的模块列表，结合客户端现有的功能模块来决定显示哪些功能模块
     */
    private Map<String, ModuleDict> singlePatientModuleShow = new LinkedHashMap<>();
    /**
     * 单患者界面要显示的功能模块，根据服务器端返回的模块列表，结合客户端现有的功能模块来决定显示哪些功能模块
     * 这里添加的是次要模块，也就是说不是在主界面底部切换的模块
     */
    private Map<String, ModuleDict> secondSinglePatientModuleShow = new LinkedHashMap<>();

    static {//本地添加所有模块，最终与服务器端返回作对比显示
        multiPatientModuleAll = new LinkedHashMap<>();
        multiPatientModuleAll.put(ModuleEnum.SICKBED_LIST.id(), ModuleEnum.SICKBED_LIST);//床位一览
        multiPatientModuleAll.put(ModuleEnum.OPERATION_SCHEDULED.id(), ModuleEnum.OPERATION_SCHEDULED);//手术安排
        multiPatientModuleAll.put(ModuleEnum.OTHER.id(), ModuleEnum.OTHER);//其他

        singlePatientModuleAll = new LinkedHashMap<>();
        singlePatientModuleAll.put(ModuleEnum.PATIENT_INFO.id(), ModuleEnum.PATIENT_INFO);//患者详情
        singlePatientModuleAll.put(ModuleEnum.ORDERS.id(), ModuleEnum.ORDERS);//医嘱
        singlePatientModuleAll.put(ModuleEnum.VITAL_SIGNS.id(), ModuleEnum.VITAL_SIGNS);//体征
        singlePatientModuleAll.put(ModuleEnum.ADMISSION_ASSESSMENT.id(), ModuleEnum.ADMISSION_ASSESSMENT);//入院评估
        singlePatientModuleAll.put(ModuleEnum.DRUG_CHECK.id(), ModuleEnum.DRUG_CHECK);//药品核对
        singlePatientModuleAll.put(ModuleEnum.NURSE_ROUND.id(), ModuleEnum.NURSE_ROUND);//护理巡视
        singlePatientModuleAll.put(ModuleEnum.HEALTH_EDUCATION.id(), ModuleEnum.HEALTH_EDUCATION);//健康宣教
        singlePatientModuleAll.put(ModuleEnum.EXAM_REPORT.id(), ModuleEnum.EXAM_REPORT);//检查报告
        singlePatientModuleAll.put(ModuleEnum.LAB_REPORT.id(), ModuleEnum.LAB_REPORT);//检验报告
        singlePatientModuleAll.put(ModuleEnum.MONITOR.id(), ModuleEnum.MONITOR);//监护仪绑定
    }


    private static DictTemp mInstance;
    private DaoSession mSession;


    private DictTemp() {
        mSession = MobileNurseApplication.getInstance().getDaoSession();
    }

    public static DictTemp getInstance() {
        if (mInstance == null)
            mInstance = new DictTemp();
        return mInstance;
    }

    /**********************************************************************ModuleDict***************************************************************/

    //主界面可切换的模块个数，按照顺序，超出这个限制的模块，入口不会出现在主界面底部，而是安排在侧滑抽屉中
    private final int mainModuleCount = 4;

    /**
     * 模块控制是否已经初始化
     */
    private boolean isModuleInitialized = false;

    /**
     * 初始化界面要显示的功能模块
     */
    private void initModule() {
        try {
            if (isModuleInitialized)
                return;
            List<ModuleDict> moduleDictList = mSession.loadAll(ModuleDict.class);
            multiPatientModuleShow.clear();
            singlePatientModuleShow.clear();
            for (ModuleDict module : moduleDictList) {
                String id = module.getModuleId();
                if (multiPatientModuleAll.containsKey(id)) {//多患者
                    module.setIconRes(multiPatientModuleAll.get(id).iconRes());
                    if (multiPatientModuleShow.size() < mainModuleCount)
                        multiPatientModuleShow.put(id, module);
                    else//主界面模块个数已经够了，剩下的添加到次要模块显示
                        secondMultiPatientModuleShow.put(id, module);
                }
                if (singlePatientModuleAll.containsKey(id)) {//单患者
                    module.setIconRes(singlePatientModuleAll.get(id).iconRes());
                    if (singlePatientModuleShow.size() < mainModuleCount)
                        singlePatientModuleShow.put(id, module);
                    else//主界面模块个数已经够了，剩下的添加到次要模块显示
                        secondSinglePatientModuleShow.put(id, module);
                }
            }
            //此处一旦为true，说明已经完成了要显示的模块功能初始化，initModule
            isModuleInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 返回要显示的主要模块列表
     *
     * @param moduleType
     * @return
     */
    public Map<String, ModuleDict> getShowModule(@NonNull String moduleType) {
        if (!isModuleInitialized)
            initModule();
        if (moduleType.equals(ValueConstant.MODULE_TYPE_MULTI_PATIENT)) {//返回多患者类型的功能模块列表
            return Collections.unmodifiableMap(multiPatientModuleShow);
        } else if (moduleType.equals(ValueConstant.MODULE_TYPE_SINGLE_PATIENT)) {//返回单患者类型的功能模块列表
            return Collections.unmodifiableMap(singlePatientModuleShow);
        }
        return null;
    }

    /**
     * 返回要显示的次要模块列表
     *
     * @param moduleType
     * @return
     */
    public List<ModuleDict> getSecondShowModuleList(@NonNull String moduleType) {
        if (!isModuleInitialized)
            initModule();
        if (moduleType.equals(ValueConstant.MODULE_TYPE_MULTI_PATIENT)) {//返回多患者类型的功能模块列表
            return getModuleListByModuleMap(secondMultiPatientModuleShow);
        } else if (moduleType.equals(ValueConstant.MODULE_TYPE_SINGLE_PATIENT)) {//返回单患者类型的功能模块列表
            return getModuleListByModuleMap(secondSinglePatientModuleShow);
        }
        return null;
    }

    private List<ModuleDict> getModuleListByModuleMap(Map<String, ModuleDict> moduleMap) {
        if (moduleMap == null)
            return null;
        List<ModuleDict> moduleDictList = new ArrayList<>();
        for (Map.Entry<String, ModuleDict> entry : moduleMap.entrySet()) {
            moduleDictList.add(entry.getValue());
        }
        return moduleDictList;
    }

    /**
     * 根据模块Id获取模块名称
     *
     * @param moduleId
     * @return
     */
    public String getModuleNameById(String moduleId) {
        if (!isModuleInitialized)
            initModule();
        ModuleDict module = multiPatientModuleShow.get(moduleId);
        if (module == null)
            module = singlePatientModuleShow.get(moduleId);
        if (module != null)
            return module.getModuleName();
        return "";
    }

    /********************************************************OrderClass**********************************************************************/
    private List<OrderClassDict> mOrderClassList = new ArrayList<>();
    private boolean isOrderClassInitialized = false;

    public List<OrderClassDict> getOrderClassList() {
        if (!isOrderClassInitialized)
            initOrderClass();
        return mOrderClassList;
    }

    /**
     * 初始化医嘱类别列表
     */
    private void initOrderClass() {
        try {
            mOrderClassList.clear();
            List<OrderClassDict> list = mSession.loadAll(OrderClassDict.class);
            if (list != null)
                mOrderClassList.addAll(list);
            isOrderClassInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /********************************************************ExecuteResult**********************************************************************/
    private List<ExecuteResultDict> mExecuteResultList = new ArrayList<>();
    private boolean isExecuteResultInitialized = false;

    public List<ExecuteResultDict> getExecuteResultList() {
        if (!isExecuteResultInitialized)
            initExecuteResult();
        return mExecuteResultList;
    }

    /**
     * 根据执行结果Code，获取执行结果内容
     *
     * @param executeResultCode
     * @return
     */
    public String getExecuteResultNameByCode(int executeResultCode) {
        if (mExecuteResultList == null || mExecuteResultList.isEmpty())
            return "";
        for (ExecuteResultDict executeResult : mExecuteResultList) {
            int resultCode = Integer.parseInt(executeResult.getCode());
            if (executeResultCode == resultCode)
                return executeResult.getName();
        }
        return "";
    }

    /**
     * 根据执行结果Code，获取执行结果内容
     *
     * @param executeResultName
     * @return
     */
    public Integer getExecuteResultCodeByName(String executeResultName) {
        if (mExecuteResultList == null || mExecuteResultList.isEmpty())
            return null;
        for (ExecuteResultDict executeResult : mExecuteResultList) {
            String resultName = executeResult.getExecuteResultName();
            if (resultName.equals(executeResultName))
                return Integer.parseInt(executeResult.getCode());
        }
        return null;
    }

    /**
     * 初始化执行结果
     */
    private void initExecuteResult() {
        try {
            mExecuteResultList.clear();
            List<ExecuteResultDict> list = mSession.loadAll(ExecuteResultDict.class);
            if (list != null)
                mExecuteResultList.addAll(list);
            isExecuteResultInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /********************************************************PatientCondition*********************************************************************/
    private List<PatientConditionDict> mPatientConditionList = new ArrayList<>();
    private boolean isPatientConditionInitialized = false;

    public List<PatientConditionDict> getPatientConditionList() {
        if (!isPatientConditionInitialized)
            initPatientCondition();
        return mPatientConditionList;
    }

    /**
     * 根据患者病情等级Code，查询患者病情等级
     *
     * @param patientConditionCode
     * @return
     */
    public String getPatientConditionNameByCode(String patientConditionCode) {
        if (TextUtils.isEmpty(patientConditionCode))
            return "";
        if (mPatientConditionList == null || mPatientConditionList.isEmpty())
            return "";
        for (PatientConditionDict patientCondition : mPatientConditionList) {
            if (patientConditionCode.equals(patientCondition.getCode()))
                return patientCondition.getName();
        }
        return "";
    }

    /**
     * 初始病情等级
     */
    private void initPatientCondition() {
        try {
            mPatientConditionList.clear();
            List<PatientConditionDict> list = mSession.loadAll(PatientConditionDict.class);
            if (list != null)
                mPatientConditionList.addAll(list);
            isPatientConditionInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /********************************************************NursingClass*********************************************************************/
    private List<NursingClassDict> mNursingClassList = new ArrayList<>();
    private boolean isNursingClassInitialized = false;

    public List<NursingClassDict> getNursingClassList() {
        if (!isNursingClassInitialized)
            initNursingClass();
        return mNursingClassList;
    }

    /**
     * 根据患者病情等级Code，查询患者病情等级
     *
     * @param nursingClassCode
     * @return
     */
    public String getNursingClassNameByCode(String nursingClassCode) {
        if (TextUtils.isEmpty(nursingClassCode))
            return "";
        if (mNursingClassList == null || mNursingClassList.isEmpty())
            return "";
        for (NursingClassDict nursingClass : mNursingClassList) {
            if (nursingClassCode.equals(nursingClass.getCode()))
                return nursingClass.getName();
        }
        return "";
    }

    /**
     * 初始化护理等级
     */
    private void initNursingClass() {
        try {
            mNursingClassList.clear();
            List<NursingClassDict> list = mSession.loadAll(NursingClassDict.class);
            if (list != null)
                mNursingClassList.addAll(list);
            isNursingClassInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**********************************************************************AckIndicator手术确认标识*******************************************************/
    private List<AckIndicatorDict> mAckIndicatorList = new ArrayList<>();
    private boolean isAckIndicatorInitialized = false;

    public List<AckIndicatorDict> getAckIndicatorList() {
        if (!isAckIndicatorInitialized)
            initAckIndicator();
        return mAckIndicatorList;
    }

    /**
     * 初始化手术确认标识
     */
    private void initAckIndicator() {
        try {
            mAckIndicatorList.clear();
            List<AckIndicatorDict> list = mSession.loadAll(AckIndicatorDict.class);
            if (list != null)
                mAckIndicatorList.addAll(list);
            isAckIndicatorInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**********************************************************************EmergencyIndicator手术紧急标识*******************************************************/
    private List<EmergencyIndicatorDict> mEmergencyIndicatorList = new ArrayList<>();
    private boolean isEmergencyIndicatorInitialized = false;

    public List<EmergencyIndicatorDict> getEmergencyIndicatorList() {
        if (!isEmergencyIndicatorInitialized)
            initEmergencyIndicator();
        return mEmergencyIndicatorList;
    }

    /**
     * 初始化手术紧急标识
     */
    private void initEmergencyIndicator() {
        try {
            mEmergencyIndicatorList.clear();
            List<EmergencyIndicatorDict> list = mSession.loadAll(EmergencyIndicatorDict.class);
            if (list != null)
                mEmergencyIndicatorList.addAll(list);
            isEmergencyIndicatorInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**********************************************************************NurseRound护理巡视项目*******************************************************/
    private List<NurseRoundDict> mNurseRoundList = new ArrayList<>();
    private boolean isNurseRoundInitialized = false;

    public List<NurseRoundDict> getNurseRoundList() {
        if (!isNurseRoundInitialized)
            initNurseRound();
        return mNurseRoundList;
    }

    /**
     * 初始初始化护理巡视项目列表
     */
    private void initNurseRound() {
        try {
            mNurseRoundList.clear();
            List<NurseRoundDict> list = mSession.loadAll(NurseRoundDict.class);
            if (list != null)
                mNurseRoundList.addAll(list);
            isNurseRoundInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
