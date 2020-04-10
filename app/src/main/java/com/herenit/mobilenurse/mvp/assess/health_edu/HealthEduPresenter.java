package com.herenit.mobilenurse.mvp.assess.health_edu;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.criteria.entity.MultiListMenuItem;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;

import java.util.ArrayList;
import java.util.List;

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
 * date: 2019/2/18 14:00
 * desc:“健康宣教”功能  Presenter
 */
@FragmentScope
public class HealthEduPresenter extends BasePresenter<HealthEduContract.Model, HealthEduContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;

    private List<MultiListMenuItem> mData = new ArrayList<>();


    @Inject
    HealthEduPresenter(HealthEduContract.Model model, HealthEduContract.View view) {
        super(model, view);
    }

    /**
     * 获取健康宣教条目列表
     */
    public void getHealthEduItemList(boolean refresh) {
        mModel.getHealthEduItemList(UserTemp.getInstance().getUserId())
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
                .subscribe(new ErrorHandleSubscriber<Result<List<MultiListMenuItem>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<MultiListMenuItem>> result) {
                        if (result.isSuccessful()) {
                            mData.clear();
                            if (result.getData() != null)
                                mData.addAll(result.getData());
                            mRootView.getHealthEduItemListSuccess();
                        } else {
                            mRootView.showError(result.getMessage());
                        }
                    }
                });
    }

    /**
     * 获取下一级列表
     *
     * @param path 当前级的路径
     * @return
     */
    public List<MultiListMenuItem> getNextLevelList(String path) {
        return MultiListMenuItem.getNextLevelList(path, mData);
    }

    /**
     * 获取一级目录下的列表
     *
     * @return
     */
    public List<MultiListMenuItem> getLevel1List() {
        return MultiListMenuItem.getLevel1List(mData);
    }

    /**
     * 获取选中的条目
     *
     * @return
     */
    public List<MultiListMenuItem> getSelectItemList() {
        if (mData == null || mData.isEmpty())
            return null;
        List<MultiListMenuItem> selectItemList = new ArrayList<>();
        for (MultiListMenuItem item : mData) {
            if (item.isChecked())
                selectItemList.add(item);
        }
        return selectItemList;
    }

    /**
     * 获取最大层级数
     *
     * @return
     */
    public int getMaxLevelNum() {
        return MultiListMenuItem.getMaxLevelNum(mData);
    }
}
