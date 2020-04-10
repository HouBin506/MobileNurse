package com.herenit.mobilenurse.mvp.main;

import android.app.Application;
import android.text.TextUtils;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.app.utils.NetworkUtils;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.criteria.entity.submit.SickbedListQuery;
import com.herenit.mobilenurse.datastore.cache.UserCache;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.custom.adapter.CommonTextAdapter;
import com.herenit.mobilenurse.datastore.sp.UserSp;

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
 * date: 2019/1/29 16:47
 * desc: 主界面的Presenter
 */

@ActivityScope
public class MultiPatientPresenter extends BasePresenter<MultiPatientContract.Model, MultiPatientContract.View> {
    @Inject
    Application mApplication;
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    CommonTextAdapter mGroupListAdapter;
    @Inject
    List<String> mGroupTextList;
    @Inject
    List<User.MnUserVsGroupVPOJOListBean> mGroupList;

    @Inject
    public MultiPatientPresenter(MultiPatientContract.Model model, MultiPatientContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取User
     */
    public void getUser() {
        String userId = UserTemp.getInstance().getUserId();
        User user = mModel.getCacheUser(userId);
        if (user != null) {
            setGroupList(user.getMnUserVsGroupVPOJOList());
            mRootView.showView();
        }
    }

    /**
     * 设置当前科室列表或者是病区列表
     *
     * @param groupList 可切换的科室或者病区列表
     */
    private void setGroupList(List<User.MnUserVsGroupVPOJOListBean> groupList) {
        mGroupTextList.clear();
        mGroupList.clear();
        int selectPosition = -1;
        String selectGroupCode = UserTemp.getInstance().getGroupCode();
        if (TextUtils.isEmpty(selectGroupCode))
            return;
        if (groupList != null && !groupList.isEmpty()) {
            mGroupList.addAll(groupList);
            for (int x = 0; x < groupList.size(); x++) {
                User.MnUserVsGroupVPOJOListBean group = groupList.get(x);
                mGroupTextList.add(group.getGroupName());
                if (selectGroupCode.equals(group.getGroupCode()))
                    selectPosition = x;//找到当前要加载的科室所处的科室列表的角标位置
            }
        }
        if (selectPosition >= 0)
            mGroupListAdapter.setSelectItem(selectPosition);
        mGroupListAdapter.notifyDataSetChanged();
    }

    /**
     * 切换科室
     *
     * @param position
     */
    public void switchGroup(int position) {
        String groupCode = mGroupList.get(position).getGroupCode();
        UserTemp.getInstance().setGroupCode(groupCode);
        UserTemp.getInstance().setGroupName(mGroupList.get(position).getGroupName());
        UserSp.getInstance().setUserGroupCode(UserTemp.getInstance().getUserId(), groupCode);
        loadSickbedList(true);
    }

    /**
     * 获取当前科室（病区）所在列表的角标
     *
     * @return
     */
    public int getCurrentGroupPosition() {
        String groupCode = UserTemp.getInstance().getGroupCode();
        for (int x = 0; x < mGroupList.size(); x++) {
            String code = mGroupList.get(x).getGroupCode();
            if (groupCode.equals(code))
                return x;
        }
        return 0;
    }

    /**
     * 获取床位列表 主页面要加载当前科室患者列表
     */
    public void loadSickbedList(boolean switchGroup) {
        if (!NetworkUtils.isNetworkConnected(mApplication)) { //没网
            loadSickbedListByCache(switchGroup);
        } else {
            //有网
            loadSickbedListByNetwork(switchGroup);
        }
    }

    /**
     * 读取缓存中的床位列表
     *
     * @param switchGroup 是否为切换科室操作
     */
    private void loadSickbedListByCache(boolean switchGroup) {
        List<Sickbed> sickbedList = mModel.getCacheSickbedList(UserTemp.getInstance().getGroupCode());
        loadSickbedListSucceed(sickbedList, switchGroup);
    }

    /**
     * 网络加载患者列表
     *
     * @param switchGroup 是否为切换科室操作
     */
    private void loadSickbedListByNetwork(boolean switchGroup) {
        SickbedListQuery query = SickbedListQuery.createQueryAllSickbed(UserTemp.getInstance().getGroupCode());
        mModel.getSickbedList(query)
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
                .subscribe(new ErrorHandleSubscriber<Result<List<Sickbed>>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<List<Sickbed>> result) {
                        if (result.isSuccessful()) {
                            //加载患者列表成功后，缓存到本地
                            mModel.updateCacheGroupSickbedList(result.getData(), UserTemp.getInstance().getGroupCode());
                            loadSickbedListSucceed(result.getData(), switchGroup);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.showModuleView();
                    }
                });
    }

    /**
     * 加载床位列表成功
     *
     * @param sickbedList
     */
    private void loadSickbedListSucceed(List<Sickbed> sickbedList, boolean switchGroup) {
        //缓存到内存
        SickbedTemp.getInstance().setSickbedList(sickbedList);
        if (switchGroup) {//如果是切换科室操作，则做出切换科室的反应
            mRootView.switchGroup();
        } else {//非切换科室操作，则加载页面
            mRootView.showModuleView();//患者列表加载缓存完成，显示模块页面
        }
    }

}
