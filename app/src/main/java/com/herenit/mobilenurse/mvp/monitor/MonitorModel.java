package com.herenit.mobilenurse.mvp.monitor;

import android.text.TextUtils;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.Api;
import com.herenit.mobilenurse.api.service.MonitorService;
import com.herenit.mobilenurse.api.service.PatientBedService;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.entity.MonitorBind;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.BindStatusEnum;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.DaoSession;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.PatientInfoDao;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: HouBin
 * date: 2019/3/4 15:01
 * desc:监护仪绑定的M层
 */
@FragmentScope
public class MonitorModel extends BaseModel implements MonitorContract.Model {


    @Inject
    public MonitorModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<PatientInfo>> loadPatientInfo(String patientId, String groupCode) {
        return mRepositoryManager.obtainRetrofitService(PatientBedService.class)
                .getPatientInfo(patientId, groupCode);
    }

    @Override
    public Observable<Result<MonitorBind>> loadMonitorInfo(String visitNo) {
//        return getMonitorBindTestData(visitNo);
        return mRepositoryManager.obtainRetrofitService(MonitorService.class)
                .loadMonitorInfo(visitNo);
    }

    @Override
    public Observable<Result<MonitorBind>> monitorBindVerify(MonitorBind monitor) {
//        return verifyTest(monitor);
        return mRepositoryManager.obtainRetrofitService(MonitorService.class)
                .monitorBindVerify(monitor);
    }

    private Observable<Result<MonitorBind>> verifyTest(MonitorBind monitor) {
        Sickbed sickbed = SickbedTemp.getInstance().getCurrentSickbed();
        int SJS = (int) (Math.random() * 10);
        switch (SJS % 5) {
            case 0:
                monitor.setOperationType(1);
                monitor.setVerifyMessage("确认将当前患者（" + sickbed.getBedLabel() + "床 " + sickbed.getPatientName() +
                        "）与当前监护仪（" + monitor.getMonitorCode() + "）进行绑定？");
                break;
            case 1:
                monitor.setOperationType(2);
                monitor.setVerifyMessage("确认将当前患者（" + sickbed.getBedLabel() + "床 " + sickbed.getPatientName() +
                        "）与当前监护仪（" + monitor.getMonitorCode() + "）解除绑定关系？");
                break;
            case 2:
                monitor.setOperationType(3);
                monitor.setVerifyMessage("当前患者（" + sickbed.getBedLabel() + "床 " + sickbed.getPatientName() +
                        "）与另一监护仪（44444）绑定，是否确认先解绑，再与当前监护仪（" + monitor.getMonitorCode() + "）进行绑定？");
                break;
            case 3:
                monitor.setOperationType(4);
                monitor.setVerifyMessage("当前监护仪（" + monitor.getMonitorCode() +
                        "）与另一患者（05床 孙某）绑定，是否确认先解绑，再与当前患者（" + sickbed.getBedLabel() + "床 " + sickbed.getPatientName() + "）进行绑定？");
                break;
            case 4:
                monitor.setOperationType(5);
                monitor.setVerifyMessage("当前患者（" + sickbed.getBedLabel() + "床 " + sickbed.getPatientName() +
                        "）与另一监护仪（44444）绑定，且当前监护仪（" + monitor.getMonitorCode() + "）与另一患者（05床 孙某）绑定。" +
                        "是否确认先分别解绑，再将当前患者（" + sickbed.getBedLabel() + "床 " + sickbed.getPatientName() +
                        "）与当前监护仪（" + monitor.getMonitorCode() + "）建立绑定？");
                break;
        }
        Result<MonitorBind> result = new Result<>();
        result.setCode(Api.CODE_SUCCESS);
        result.setData(monitor);
        return Observable.just(result);
    }

    @Override
    public Observable<Result> monitorBind(MonitorBind monitor) {
//        return bindTest();
        return mRepositoryManager.obtainRetrofitService(MonitorService.class)
                .monitorBind(monitor);
    }

    private Observable<Result> bindTest() {
        Result result = new Result();
        result.setCode(Api.CODE_SUCCESS);
        return Observable.just(result);
    }

    private Observable<Result<MonitorBind>> getMonitorBindTestData(String visitNo) {
        visitNo = SickbedTemp.getInstance().getCurrentSickbed().getPatientId();
        MonitorBind monitor = null;
        Result<MonitorBind> result = new Result<>();
        result.setCode(Api.CODE_SUCCESS);
        if (TextUtils.isEmpty(visitNo)) {
            result.setData(null);
        } else {
            monitor = new MonitorBind();
            Long pidLong = Long.parseLong(visitNo);
            monitor.setMonitorCode(System.currentTimeMillis() + "");
            monitor.setMemo("");
            monitor.setOperatingDateTime(TimeUtils.getCurrentDate().getTime());
            monitor.setVisitNo(visitNo);
            monitor.setOperatorId("3546546546546");
            monitor.setOperatorName("西门吹雪");
            if (pidLong != null && pidLong % 3 == 0) {
                monitor.setBindStatus(BindStatusEnum.UNBIND.getBindStatusCode());
                result.setData(monitor);
            } else if (pidLong != null && pidLong % 3 == 1) {
                monitor.setBindStatus(BindStatusEnum.BINDING.getBindStatusCode());
                result.setData(monitor);
            } else {
                result.setData(null);
            }
        }
        return Observable.just(result);
    }
}
