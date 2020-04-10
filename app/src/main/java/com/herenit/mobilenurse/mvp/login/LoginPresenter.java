package com.herenit.mobilenurse.mvp.login;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.RxLifecycleUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.NetworkUtils;
import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.criteria.constant.ValueConstant;
import com.herenit.mobilenurse.criteria.entity.dict.Dict;
import com.herenit.mobilenurse.criteria.entity.submit.UserGroup;
import com.herenit.mobilenurse.custom.adapter.CommonTextImageAdapter;
import com.herenit.mobilenurse.datastore.sp.ConfigSp;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.criteria.entity.submit.ChangePassword;
import com.herenit.mobilenurse.criteria.entity.submit.Login;
import com.herenit.mobilenurse.datastore.tempcache.TempManager;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.datastore.sp.UserSp;

import java.util.Iterator;
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
 * date: 2019/1/4 10:47
 * desc: 处理Login模块的业务
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    @Inject
    Application mApplication;

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    List<Login> mLoginList;
    @Inject
    CommonTextImageAdapter<Login> mSelectAccountAdapter;//账号选择弹窗列表Adapter

    @Inject
    List<String> mUserIdList;
    @Inject
    ArrayAdapter<String> mAutoCompleteAdapter;//输入账号输入框的自动补全弹出列表的Adapter

    @Inject
    List<User.MnUserVsGroupVPOJOListBean> mDeptList;
    @Inject
    CommonAdapter<User.MnUserVsGroupVPOJOListBean> mDeptAdapter;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View view) {
        super(model, view);
    }

    /**
     * 获取登录账号列表
     */
    public void loadHistoryLogin() {
        List<Login> loginList = mModel.getHistoryLogin();
        resetLogin(loginList);
        if (mLoginList != null && !mLoginList.isEmpty())
            mRootView.autoCompleteLogin(mLoginList.get(0));
    }

    /**
     * 根据loginList重新设置当前界面数据源（设置账号自动补全的数据列表及历史记录）
     *
     * @param loginList
     */
    private void resetLogin(List<Login> loginList) {
        mLoginList.clear();
        mUserIdList.clear();
        if (loginList != null) {
            mLoginList.addAll(loginList);
            for (Login login : loginList) {
                mUserIdList.add(login.getUserId());
            }
        }
        mSelectAccountAdapter.notifyDataSetChanged();
        mAutoCompleteAdapter.notifyDataSetChanged();
    }

    /**
     * 登录
     *
     * @param userId
     * @param password
     */
    public void login(String userId, String password, String appId) {
        if (NetworkUtils.isNetworkConnected(mApplication)) {//有网直接登录
            networkLogin(userId, password, appId);
        } else {//没网读取缓存
//            cacheLogin(userId);
        }
    }

    /**
     * 网络登录
     *
     * @param userId
     * @param password
     */
    private void networkLogin(String userId, String password, String appId) {
        Login login;
        if (TextUtils.isEmpty(password))
            login = new Login(userId, "");
        else
            login = new Login(userId, StringUtils.toMD5(password));
        login.setGroupCode(appId);
        mModel.login(login)
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
                .subscribe(new ErrorHandleSubscriber<Result<User>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<User> userResult) {
                        if (userResult != null && userResult.isSuccessful()) {
                            mRootView.showMessage(ArmsUtils.getString(mApplication, R.string.message_loginSuccess));
                            addLogin(new Login(userId, password));
                            User user = userResult.getData();
//                            user.setUserId(userId);
                            setUser(user);//设置当前账号
                            mModel.insertOrUpdateUser(user);
                            loadDict();//登录成功,加载字典之后在跳转
                        } else {
                            mRootView.showError(userResult.getMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if (NetworkUtils.isNetworkError(t)) {//因为网络连接的问题导登录失败，需要读取缓存
                            cacheLogin(userId);
                        }
                    }
                });
    }

    /**
     * 加载常用的一些字典
     */
    private void loadDict() {
        mModel.getAllDict(new UserGroup(UserTemp.getInstance().getUserId(), UserTemp.getInstance().getGroupCode()))
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
                .subscribe(new ErrorHandleSubscriber<Result<Dict>>(mErrorHandler) {
                    @Override
                    public void onNext(Result<Dict> dictResult) {//加载常用字典，不管是成功还是失败，都不应该影响正常登录
                        if (dictResult.isSuccessful()) {
                            mModel.saveDict(dictResult.getData());
                        } else {
                            mRootView.showError(dictResult.getMessage());
                        }
                        mRootView.loginSuccess();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.loginSuccess();
                    }
                });
    }

    /**
     * 设置User，设置当前登录账号、用户名、科室等信息
     *
     * @param user
     */
    private void setUser(User user) {
        String userId = user.getMnUserVPOJO().getUserId();
        user.setUserId(userId);
        UserTemp.getInstance().setUserId(userId);
        UserTemp.getInstance().setUserName(user.getMnUserVPOJO().getUserName());
        UserTemp.getInstance().setOperation(user.getMnUserVPOJO().getIsOperation());
        String groupCode = "";
        String groupName = "";
        List<User.MnUserVsGroupVPOJOListBean> groupList = user.getMnUserVsGroupVPOJOList();
        if (groupList == null || groupList.isEmpty()) {//当前账号没有任何科室的权限
            UserSp.getInstance().removeUserGroupCode(userId);//将本地保存的该账号的默认科室删除掉
        } else {//拥有科室操作权限
            groupCode = UserSp.getInstance().getUserGroupCode(userId);//获取本地存储的上次登录的科室记录
            if (TextUtils.isEmpty(groupCode)) {//没有记录
                groupCode = groupList.get(0).getGroupCode();
                groupName = groupList.get(0).getGroupName();
                UserSp.getInstance().setUserGroupCode(userId, groupCode);
            } else {
                User.MnUserVsGroupVPOJOListBean group = User.getGroupByGroupCode(groupCode, groupList);
                if (group == null) {//历史登录科室不可用,清除记录，设置列表第一项为默认科室
                    UserSp.getInstance().removeUserGroupCode(userId);
                    groupCode = groupList.get(0).getGroupCode();
                    groupName = groupList.get(0).getGroupName();
                    UserSp.getInstance().setUserGroupCode(userId, groupCode);
                } else {
                    groupName = group.getGroupName();
                }
            }
        }
        UserTemp.getInstance().setGroupCode(groupCode);
        UserTemp.getInstance().setGroupName(groupName);
    }

    /**
     * /**
     * 缓存登录
     *
     * @param userId 用户名
     */
    private void cacheLogin(String userId) {
        TempManager.clearTemp();
        User user = mModel.getCacheUser(userId);
        if (user != null) {
            setUser(user);
            mRootView.loginSuccess();
        }
    }

    /**
     * 根据UserId获取某次的登录信息
     *
     * @param userId
     * @return
     */
    public Login getLoginByUserId(String userId) {
        for (Login login : mLoginList) {
            if (login.getUserId().equals(userId)) {
                return login;
            }
        }
        return null;
    }

    /**
     * 根据UserId获取某次的登录信息
     *
     * @param position
     * @return
     */
    public Login getLoginByPosition(int position) {
        if (position >= 0 && position < mLoginList.size()) {
            return mLoginList.get(position);
        }
        return null;
    }

    /**
     * 设置登陆账号，比如登录完成，要将账号记录到本地，或者根据用户选择，不记住密码，将数据库密码删掉
     *
     * @param login
     */
    public void addLogin(Login login) {
        if (login == null || TextUtils.isEmpty(login.getUserId()))
            return;
        if (!isRememberPassword())
            login.setPassword("");
        if (mLoginList.contains(login))
            mLoginList.remove(login);
        mLoginList.add(0, login);
        String userId = login.getUserId();
        if (mUserIdList.contains(userId))
            mUserIdList.remove(userId);
        mUserIdList.add(0, userId);
        mAutoCompleteAdapter.notifyDataSetChanged();
        mSelectAccountAdapter.notifyDataSetChanged();
        //因为修改了数据量，所以要清理缓存
        mModel.updateHistoryLogin(mLoginList);
    }

    /**
     * 删除记录的账号
     *
     * @param login
     */
    public void removeLogin(Login login) {
        if (login == null || TextUtils.isEmpty(login.getUserId()))
            return;
        if (mLoginList.contains(login))
            mLoginList.remove(login);
        if (mUserIdList.contains(login.getUserId()))
            mUserIdList.remove(login.getUserId());
        mSelectAccountAdapter.notifyDataSetChanged();
        mAutoCompleteAdapter.notifyDataSetChanged();
        mModel.updateHistoryLogin(mLoginList);
    }

    /**
     * 是否记住密码
     *
     * @return
     */
    public boolean isRememberPassword() {
        return ConfigSp.getInstance().getRememberPassword();
    }

    public void setRememberPassword(boolean remember) {
        ConfigSp.getInstance().setRememberPassword(remember);
    }

    /**
     * 修改密码
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     */
    public void changePassword(String userId, String oldPassword, String newPassword) {
        ChangePassword changePassword = new ChangePassword(userId, StringUtils.toMD5(oldPassword), StringUtils.toMD5(newPassword));
        mModel.changePassword(changePassword)
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
                }).subscribe(new ErrorHandleSubscriber<Result>(mErrorHandler) {
            @Override
            public void onNext(Result result) {
                if (result.isSuccessful()) {
                    logout();
                } else {
                    mRootView.showError(result.getMessage());
                }
            }
        });
    }

    /**
     * 登出
     */
    public void logout() {
        mModel.logout()
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
                }).subscribe(new ErrorHandleSubscriber<Result>(mErrorHandler) {
            @Override
            public void onNext(Result result) {
                if (result.isSuccessful()) {
                    TempManager.clearTemp();
                    mRootView.logoutSuccess();
                }
            }
        });
    }

    /**
     * 解锁，实则是对账号密码的验证
     *
     * @param userId
     * @param password
     */
    public void unlock(String userId, String password) {
        mModel.verify(new Login(userId, StringUtils.toMD5(password)))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                }).subscribe(new ErrorHandleSubscriber<Result>(mErrorHandler) {
            @Override
            public void onNext(Result result) {
                if (result.isSuccessful()) {//密码验证成功
                    mRootView.unlockSuccess();
                } else {//密码验证失败
                    mRootView.showError(result.getMessage());
                }
            }
        });
    }

    /**
     * 获取某账号下面的工作站列表
     *
     * @param loginName
     * @param scanEmpCardLogin
     */
    public void getApplicationList(String loginName, boolean scanEmpCardLogin) {
        mModel.getApplicationList(loginName)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (scanEmpCardLogin)
                            mRootView.showLoading();
                        else
                            mRootView.closeLogin();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (scanEmpCardLogin)
                            mRootView.hideLoading();
                        else
                            mRootView.openLogin();
                    }
                }).subscribe(new ErrorHandleSubscriber<Result<List<User.MnUserVsGroupVPOJOListBean>>>(mErrorHandler) {
            @Override
            public void onNext(Result<List<User.MnUserVsGroupVPOJOListBean>> result) {
                if (result.isSuccessful()) {
                    mDeptList.clear();
                    if (result.getData() != null)
                        mDeptList.addAll(result.getData());
                    mDeptAdapter.notifyDataSetChanged();
                    setCurrentApplication(loginName, result.getData());
                    if (scanEmpCardLogin)
                        mRootView.loginWithoutPassword();
                }
            }
        });
    }

    /**
     * 设置当前工作站
     *
     * @param applicationList
     */
    private void setCurrentApplication(String loginName, List<User.MnUserVsGroupVPOJOListBean> applicationList) {
        if (applicationList == null || applicationList.isEmpty()) {
            mRootView.setApplication(null);
            return;
        }
        String appId = UserSp.getInstance().getUserGroupCode(loginName);
        User.MnUserVsGroupVPOJOListBean application = null;
        if (TextUtils.isEmpty(appId)) {//首次登录，默认选择权限下的第一个工作站
            application = applicationList.get(0);
        } else {
            for (User.MnUserVsGroupVPOJOListBean item : applicationList) {
                if (item.getGroupCode().equals(appId)) {
                    application = item;
                    break;
                }
            }
        }
        mRootView.setApplication(application);
    }

}
