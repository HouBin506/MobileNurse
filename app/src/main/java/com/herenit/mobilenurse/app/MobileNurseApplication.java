package com.herenit.mobilenurse.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.herenit.arms.base.BaseApplication;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.DeviceUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
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

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.rx.RxDao;

/**
 * author: HouBin
 * date: 2019/1/15 11:26
 * desc: Application
 */
public class MobileNurseApplication extends BaseApplication {
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


    private static MobileNurseApplication mInstance;

    @Override
    public void onCreate() {
        if (mInstance == null)
            mInstance = this;
        super.onCreate();
        initOrm();
        registerCommonReceiver();
        SoundPlayUtils.init(this);
    }


    private DaoSession daoSession;

    /**
     * 初始化数据库
     */
    private void initOrm() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "mobilenurse.db");
        Database database = helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static MobileNurseApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onTerminate() {
        unRegisterCommonReceiver();
        super.onTerminate();
    }

    /**
     * 取消常用广播注册
     */
    private void unRegisterCommonReceiver() {
        //取消注册海信设备
        unregisterReceiver(HisenseScanBroadcastReceiver.getInstance());
        //取消注册东软Camus设备
        unregisterReceiver(CamusScanBroadcastReceiver.getInstance());
        //取消注册来邦设备
        unregisterReceiver(CamusScanBroadcastReceiver.getInstance());
        //取消注册东大集成小码哥设备
        unregisterReceiver(CruiseScanBroadcastReceiver.getInstance());
        //取消注册新大陆设备
        unregisterReceiver(NLSScanBroadcastReceiver.getInstance());
    }

    /**
     * 动态注册常用广播
     */
    private void registerCommonReceiver() {
        //注册海信设备
        registerReceiver(HisenseScanBroadcastReceiver.getInstance(), HisenseConstant.SCAN_BROADCAST_INTENT_FILTER_HISENSE);
        //注册东软Camus设备
        registerReceiver(CamusScanBroadcastReceiver.getInstance(), NeusoftConstant.SCAN_BROADCAST_INTENT_FILTER_CAMUS);
        //注册来邦设备
        registerReceiver(CamusScanBroadcastReceiver.getInstance(), LonbonConstant.SCAN_BROADCAST_INTENT_FILTER_LONBON);
        //注册东集小码哥
        IntentFilter cruiseFilter = SeuicConstant.SCAN_BROADCAST_INTENT_FILTER_CRUISE;
        cruiseFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(CruiseScanBroadcastReceiver.getInstance(), cruiseFilter);
        //注册新大陆设备 注册及扫描设置
        //设置扫描为广播输出模式
        Intent intent = new Intent("ACTION_BAR_SCANCFG");
        intent.putExtra("EXTRA_SCAN_MODE", 3);
        sendBroadcast(intent);
        //注册扫描接受广播
        registerReceiver(NLSScanBroadcastReceiver.getInstance(), NLSConstant.SCAN_BROADCAST_INTENT_FILTER_CRUISE);
    }
}
