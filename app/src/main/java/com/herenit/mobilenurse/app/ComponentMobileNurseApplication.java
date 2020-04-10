package com.herenit.mobilenurse.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import com.herenit.arms.base.App;
import com.herenit.arms.base.BaseApplication;
import com.herenit.arms.base.IComponentApplication;
import com.herenit.arms.base.delegate.AppDelegate;
import com.herenit.arms.base.delegate.AppLifecycles;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.DeviceUtils;
import com.herenit.arms.utils.Preconditions;
import com.herenit.mobilenurse.R;
import com.herenit.mobilenurse.app.utils.ScreenUtils;
import com.herenit.mobilenurse.app.utils.SoundPlayUtils;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.DaoMaster;
import com.herenit.mobilenurse.datastore.orm.greendao.daopackage.DaoSession;
import com.herenit.mobilenurse.thirdparty.Hisense.HisenseConstant;
import com.herenit.mobilenurse.thirdparty.Hisense.HisenseScanBroadcastReceiver;
import com.herenit.mobilenurse.thirdparty.Lonbon.LonbonConstant;
import com.herenit.mobilenurse.thirdparty.Neusoft.CamusScanBroadcastReceiver;
import com.herenit.mobilenurse.thirdparty.Neusoft.NeusoftConstant;
import com.herenit.mobilenurse.thirdparty.Seuic.CruiseScanBroadcastReceiver;
import com.herenit.mobilenurse.thirdparty.Seuic.SeuicConstant;
import com.herenit.mobilenurse.thirdparty.newlond.NLSConstant;
import com.herenit.mobilenurse.thirdparty.newlond.NLSScanBroadcastReceiver;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import org.greenrobot.greendao.database.Database;

/**
 * author: HouBin
 * date: 2019/1/15 11:26
 * desc: Application
 */
public class ComponentMobileNurseApplication implements App, IComponentApplication {

    private Application mApplicationContext;

    public ComponentMobileNurseApplication() {
        super();
        MobileNurseApplicationDelegate.getInstance().setComponentMobileNurseApplication(this);
    }

    public Application getApplicationContext() {
        return mApplicationContext;
    }

    private AppLifecycles mAppDelegate;

    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    public void attachBaseContext(Context base) {
        mApplicationContext = (Application) base;
        if (mAppDelegate == null)
            this.mAppDelegate = new AppDelegate(base);
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate(Application application) {
        if (mAppDelegate != null)
            this.mAppDelegate.onCreate(application);
        businessOnCreate(application);
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate(Application application) {
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate(application);
        businessOnTerminate(application);
    }

    /**
     * 将 {@link AppComponent} 返回出去, 供其它地方使用, {@link AppComponent} 接口中声明的方法所返回的实例, 在 {@link #getAppComponent()} 拿到对象后都可以直接使用
     *
     * @return AppComponent
     * @see ArmsUtils#obtainAppComponentFromContext(Context) 可直接获取 {@link AppComponent}
     */
    @NonNull
    @Override
    public AppComponent getAppComponent() {
        Preconditions.checkNotNull(mAppDelegate, "%s cannot be null", AppDelegate.class.getName());
        Preconditions.checkState(mAppDelegate instanceof App, "%s must be implements %s", mAppDelegate.getClass().getName(), App.class.getName());
        return ((App) mAppDelegate).getAppComponent();
    }

    /***********************************************Business*****************************************************/

    static {
        //设置下拉刷新默认风格
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.white);
                ClassicsHeader header = new ClassicsHeader(context);
                if (DeviceUtils.hasBigScreen(context))
                    //设置字体大小，为了多端屏幕适配大小
                    header.setTextSizeTime(ScreenUtils.px2sp(context, ArmsUtils.getDimens(context, R.dimen.text_content_level_6)));
                return header; //经典风格
            }
        });
    }


    private void businessOnCreate(Application application) {
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate(application);
        initOrm(application);
        registerCommonReceiver(application);
        SoundPlayUtils.init(application);
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    private void businessOnTerminate(Application application) {
        unRegisterCommonReceiver(application);
    }

    private DaoSession daoSession;

    /**
     * 初始化数据库
     */
    private void initOrm(Application application) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application, "mobilenurse.db");
        Database database = helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    /**
     * 取消常用广播注册
     */
    private void unRegisterCommonReceiver(Application application) {
        //取消注册海信设备
        application.unregisterReceiver(HisenseScanBroadcastReceiver.getInstance());
        //取消注册东软Camus设备
        application.unregisterReceiver(CamusScanBroadcastReceiver.getInstance());
        //取消注册来邦设备
        application.unregisterReceiver(CamusScanBroadcastReceiver.getInstance());
        //取消注册东大集成小码哥设备
        application.unregisterReceiver(CruiseScanBroadcastReceiver.getInstance());
        //取消注册新大陆设备
        application.unregisterReceiver(NLSScanBroadcastReceiver.getInstance());
    }

    /**
     * 动态注册常用广播
     */
    private void registerCommonReceiver(Application application) {
        //注册海信设备
        application.registerReceiver(HisenseScanBroadcastReceiver.getInstance(), HisenseConstant.SCAN_BROADCAST_INTENT_FILTER_HISENSE);
        //注册东软Camus设备
        application.registerReceiver(CamusScanBroadcastReceiver.getInstance(), NeusoftConstant.SCAN_BROADCAST_INTENT_FILTER_CAMUS);
        //注册来邦设备
        application.registerReceiver(CamusScanBroadcastReceiver.getInstance(), LonbonConstant.SCAN_BROADCAST_INTENT_FILTER_LONBON);
        //注册东集小码哥
        IntentFilter cruiseFilter = SeuicConstant.SCAN_BROADCAST_INTENT_FILTER_CRUISE;
        cruiseFilter.setPriority(Integer.MAX_VALUE);
        application.registerReceiver(CruiseScanBroadcastReceiver.getInstance(), cruiseFilter);
        //注册新大陆设备 注册及扫描设置
        //设置扫描为广播输出模式
        Intent intent = new Intent("ACTION_BAR_SCANCFG");
        intent.putExtra("EXTRA_SCAN_MODE", 3);
        application.sendBroadcast(intent);
        //注册扫描接受广播
        application.registerReceiver(NLSScanBroadcastReceiver.getInstance(), NLSConstant.SCAN_BROADCAST_INTENT_FILTER_CRUISE);
    }
}
