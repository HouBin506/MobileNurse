package com.herenit.mobilenurse.mvp.login;

import android.text.TextUtils;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.UserService;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.criteria.entity.dict.AckIndicatorDict;
import com.herenit.mobilenurse.criteria.entity.dict.Dict;
import com.herenit.mobilenurse.criteria.entity.dict.EmergencyIndicatorDict;
import com.herenit.mobilenurse.criteria.entity.dict.ExecuteResultDict;
import com.herenit.mobilenurse.criteria.entity.dict.ModuleDict;
import com.herenit.mobilenurse.criteria.entity.dict.NurseRoundDict;
import com.herenit.mobilenurse.criteria.entity.dict.NursingClassDict;
import com.herenit.mobilenurse.criteria.entity.dict.OrderClassDict;
import com.herenit.mobilenurse.criteria.entity.dict.PatientConditionDict;
import com.herenit.mobilenurse.criteria.entity.submit.UserGroup;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.criteria.entity.submit.ChangePassword;
import com.herenit.mobilenurse.criteria.entity.submit.Login;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.DaoSession;

import java.nio.channels.spi.AbstractSelectionKey;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/1/4 14:05
 * desc: 登录数据处理类
 */
@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {
    private DaoSession daoSession;

    @Inject
    public LoginModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        daoSession = MobileNurseApplication.getInstance().getDaoSession();
    }

    /**
     * @param login 登录信息
     * @return
     */
    @Override
    public Observable<Result<User>> login(Login login) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .login(login);
    }


    /**
     * 修改密码
     *
     * @param changePassword 封装了账号、新旧密码
     * @return
     */
    @Override
    public Observable<Result> changePassword(ChangePassword changePassword) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .changePassword(changePassword);
    }

    /**
     * 账号密码验证
     *
     * @param verify 封装了账号、密码
     * @return
     */
    @Override
    public Observable<Result> verify(Login verify) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .verify(verify);
    }

    /**
     * 登出
     *
     * @return
     */
    @Override
    public Observable<Result> logout() {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .logout();
    }

    /**
     * 获取历史登录账号
     *
     * @return
     */
    @Override
    public List<Login> getHistoryLogin() {
        return daoSession.loadAll(Login.class);
    }

    /**
     * 更新登录记录
     *
     * @param loginList
     */
    @Override
    public void updateHistoryLogin(List<Login> loginList) {
        daoSession.deleteAll(Login.class);
        if (loginList == null || loginList.isEmpty())
            return;
        for (Login login : loginList) {
            daoSession.insert(login);
        }
    }

    /**
     * 新增、更新User
     *
     * @param user
     */
    @Override
    public void insertOrUpdateUser(User user) {
        if (user == null)
            return;
        daoSession.insertOrReplace(user);
    }

    @Override
    public User getCacheUser(String userId) {
        if (TextUtils.isEmpty(userId))
            return null;
        List<User> oldUsers = daoSession.queryRaw(User.class, "where USER_ID = ?", userId);
        if (oldUsers == null || oldUsers.isEmpty())
            return null;
        return oldUsers.get(0);
    }

    @Override
    public Observable<Result<Dict>> getAllDict(UserGroup userGroup) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getAllDict(userGroup);
    }

    @Override
    public void saveDict(Dict dict) {
        daoSession.deleteAll(ExecuteResultDict.class);
        daoSession.deleteAll(ModuleDict.class);
        daoSession.deleteAll(NursingClassDict.class);
        daoSession.deleteAll(OrderClassDict.class);
        daoSession.deleteAll(PatientConditionDict.class);
        daoSession.deleteAll(EmergencyIndicatorDict.class);
        daoSession.deleteAll(AckIndicatorDict.class);
        daoSession.deleteAll(NurseRoundDict.class);
        List<ExecuteResultDict> executeResultDictList = dict.getMnExecuteResultDictTOList();
        if (executeResultDictList != null) {
            for (ExecuteResultDict executeResultDict : executeResultDictList) {
                daoSession.insert(executeResultDict);
            }
        }
        List<ModuleDict> moduleDictList = dict.getMnModuleDictTOBaseList();
        if (moduleDictList != null) {
            for (ModuleDict moduleDict : moduleDictList) {
                daoSession.insert(moduleDict);
            }
        }
        List<NursingClassDict> nursingClassDictList = dict.getMnNursingClassDictVPOJOList();
        if (nursingClassDictList != null) {
            for (NursingClassDict nursingClassDict : nursingClassDictList) {
                daoSession.insert(nursingClassDict);
            }
        }
        List<OrderClassDict> orderClassDictList = dict.getMnOrderClassDictVPOJOList();
        if (orderClassDictList != null) {
            for (OrderClassDict orderClassDict : orderClassDictList) {
                daoSession.insert(orderClassDict);
            }
        }
        List<PatientConditionDict> patientConditionDictList = dict.getMnPatientConditionDictVPOJOList();
        if (patientConditionDictList != null) {
            for (PatientConditionDict patientConditionDict : patientConditionDictList) {
                daoSession.insert(patientConditionDict);
            }
        }
        List<AckIndicatorDict> ackIndicatorDictList = dict.getMnAckIndicatorDictVPOJOList();
        if (ackIndicatorDictList != null) {
            for (AckIndicatorDict ackIndicatorDict : ackIndicatorDictList) {
                daoSession.insert(ackIndicatorDict);
            }
        }
        List<EmergencyIndicatorDict> emergencyIndicatorDictList = dict.getMnEmergencyIndicatorDictVPOJOList();
        if (emergencyIndicatorDictList != null) {
            for (EmergencyIndicatorDict emergencyIndicatorDict : emergencyIndicatorDictList) {
                daoSession.insert(emergencyIndicatorDict);
            }
        }
        List<NurseRoundDict> nurseRoundDictList = dict.getMnPatrolDictTOTwoList();
        if (nurseRoundDictList != null) {
            for (NurseRoundDict nurseRoundDict : nurseRoundDictList) {
                daoSession.insert(nurseRoundDict);
            }
        }
    }

    @Override
    public Observable<Result<List<User.MnUserVsGroupVPOJOListBean>>> getApplicationList(String loginName) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getApplicationList(loginName);
    }
}
