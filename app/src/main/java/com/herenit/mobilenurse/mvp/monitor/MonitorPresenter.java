package com.herenit.mobilenurse.mvp.monitor;

import android.app.Application;
import android.support.annotation.NonNull;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.NetworkUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.entity.MonitorBind;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;

import java.sql.Time;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * author: HouBin
 * date: 2019/3/4 14:59
 * desc:监护仪绑定的P层
 */
@FragmentScope
public class MonitorPresenter extends BasePresenter<MonitorContract.Model, MonitorContract.View> {

    @Inject
    Application mApplication;

    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    MonitorPresenter(MonitorContract.Model model, MonitorContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 加载数据
     *
     * @param currentSickbed
     * @param refresh
     */
    public void loadData(Sickbed currentSickbed, boolean refresh) {
        loadPatientInfo(currentSickbed, refresh, true);
//        loadMonitorInfo(currentSickbed, refresh);
    }

    /**
     * 加载监护仪绑定信息
     *
     * @param sickbed
     * @param refresh
     */
    private void loadMonitorInfo(Sickbed sickbed, boolean refresh) {
        mModel.loadMonitorInfo(sickbed.getVisitNo())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (!refresh)
                            mRootView.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (!refresh)
                            mRootView.hideLoading();
                    }
                })
                .subscribe(new ErrorHandleSubscriber<Result<MonitorBind>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<MonitorBind> result) {
                        if (result.isSuccessful()) {
                            mRootView.showMonitorInfo(result.getData());
                        }else {
                            mRootView.showError(result.getMessage());
                        }
                        mRootView.loadFinish();
                    }
                });
    }

    /**
     * 加载患者详情信息
     *
     * @param sickbed
     * @param refresh
     * @param multiLoad 多加载
     */
    private void loadPatientInfo(Sickbed sickbed, boolean refresh, boolean multiLoad) {
        mModel.loadPatientInfo(sickbed.getPatientId(), UserTemp.getInstance().getGroupCode())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (!refresh)
                            mRootView.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (!refresh)
                            mRootView.hideLoading();
                    }
                })
                .subscribe(new ErrorHandleSubscriber<Result<PatientInfo>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<PatientInfo> result) {
                        if (result.isSuccessful()) {
                            mRootView.showPatientInfo(result.getData());
                        }
                        if (multiLoad)//如果是多重加载，则加载完患者详情，还需要加载监护仪绑定信息
                            loadMonitorInfo(sickbed, refresh);
                    }
                });
    }

    /**
     * 监护仪绑定前的核对
     *
     * @param monitor
     */
    public void monitorBindVerify(MonitorBind monitor) {
        mModel.monitorBindVerify(monitor)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .subscribe(new ErrorHandleSubscriber<Result<MonitorBind>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<MonitorBind> result) {
                        if (result.isSuccessful()) {
                            mRootView.showBindVerifyNotice(result.getData());
                        } else {
                            mRootView.showError(ArmsUtils.getString(mApplication, R.string.error_bindingUnbindError), result.getMessage());
                            return;
                        }
                    }
                });
    }

    /**
     * 监护仪绑定
     * 此操作，可以是绑定操作，也可以是解绑操作，也可以是先解绑后绑定操作，关键要看入参 {@link MonitorBind#getOperationType()}
     *
     * @param monitor
     */
    public void monitorBind(MonitorBind monitor, Sickbed sickbed) {
        monitor.setOperatingDateTime(TimeUtils.getCurrentDate().getTime());
        mModel.monitorBind(monitor)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .subscribe(new ErrorHandleSubscriber<Result>(mErrorHandler) {
                    @Override
                    public void onNext(Result result) {
                        if (result.isSuccessful()) {
                            mRootView.bindSuccess();
                            loadMonitorInfo(sickbed, false);
                        } else {
                            mRootView.showError(ArmsUtils.getString(mApplication, R.string.error_bindingUnbindError), result.getMessage());
                            return;
                        }
                    }
                });
    }
}
