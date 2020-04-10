package com.herenit.mobilenurse.app;

import android.app.Application;

import com.herenit.mobilenurse.BuildConfig;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.DaoSession;

/**
 * Created by HouBin
 * 2020/3/31 13:59
 * Describe:  MobileNurseApplication的代理类，统一处理Application对外提供的接口，分情况处理application和library。
 */
public class MobileNurseApplicationDelegate {
    private ComponentMobileNurseApplication mComponentMobileNurseApplication;

    private static MobileNurseApplicationDelegate mInstance;

    private MobileNurseApplicationDelegate() {

    }

    public static MobileNurseApplicationDelegate getInstance() {
        if (mInstance == null)
            mInstance = new MobileNurseApplicationDelegate();
        return mInstance;
    }

    public Application getApplication() {
        if (BuildConfig.IS_MODULE) {//当前为Library模式
            if (mComponentMobileNurseApplication != null)
                return mComponentMobileNurseApplication.getApplicationContext();
            else {
                throw new RuntimeException("ComponentMobileNurseApplication is null");
            }
        } else {//当前为Application模式
            return MobileNurseApplication.getInstance();
        }
    }

    public DaoSession getDaoSession() {
        if (BuildConfig.IS_MODULE) {//当前为Library模式
            if (mComponentMobileNurseApplication != null)
                return mComponentMobileNurseApplication.getDaoSession();
            else {
                throw new RuntimeException("ComponentMobileNurseApplication is null");
            }
        } else {//当前为Application模式
            return MobileNurseApplication.getInstance().getDaoSession();
        }
    }


    public void setComponentMobileNurseApplication(ComponentMobileNurseApplication componentMobileNurseApplication) {
        this.mComponentMobileNurseApplication = componentMobileNurseApplication;
    }
}
