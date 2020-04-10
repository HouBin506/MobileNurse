package com.herenit.mobilenurse.mvp.login;

/**
 * author: HouBin
 * date: 2019/1/4 13:47
 * desc:
 */

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.criteria.entity.dict.Dict;
import com.herenit.mobilenurse.criteria.entity.submit.ChangePassword;
import com.herenit.mobilenurse.criteria.entity.submit.Login;
import com.herenit.mobilenurse.criteria.entity.submit.UserGroup;
import com.herenit.mobilenurse.custom.adapter.CommonTextImageAdapter;

import java.util.List;

import io.reactivex.Observable;

public interface LoginContract {

    interface View extends IView {
        /**
         * 设置登陆
         *
         * @param login
         */
        void autoCompleteLogin(Login login);

        /**
         * 获取Activity
         *
         * @return
         */
        Activity getActivity();


        void loginSuccess();

        /**
         * 退出登录
         */
        void logoutSuccess();

        /**
         * 解锁完成
         */
        void unlockSuccess();

        /**
         * 打开登录
         */
        void openLogin();

        /**
         * 关闭登录
         */
        void closeLogin();

        /**
         * 设置要登录的工作站
         *
         * @param application
         */
        void setApplication(User.MnUserVsGroupVPOJOListBean application);

        /**
         * 免密登录
         */
        void loginWithoutPassword();
    }

    interface Model extends IModel {
        /**
         * 登录账号
         *
         * @param login 登录信息
         * @return
         */
        Observable<Result<User>> login(Login login);


        /**
         * 修改密码
         *
         * @param changePassword 封装了账号、新旧密码
         * @return
         */
        Observable<Result> changePassword(ChangePassword changePassword);

        /**
         * 账号密码验证
         *
         * @param verify 封装了账号、密码
         * @return
         */
        Observable<Result> verify(Login verify);

        /**
         * 登出
         *
         * @return
         */
        Observable<Result> logout();

        /**
         * 获取历史登录记录
         *
         * @return
         */
        List<Login> getHistoryLogin();

        /**
         * 更新登录记录
         *
         * @param loginList
         */
        void updateHistoryLogin(List<Login> loginList);

        /**
         * 数据库新增User对象
         *
         * @param user
         */
        void insertOrUpdateUser(User user);


        /**
         * 根据UserId从缓存中获取User
         *
         * @param userId
         */
        User getCacheUser(String userId);

        /**
         * 获取常用的字典表
         *
         * @param userGroup
         * @return
         */
        Observable<Result<Dict>> getAllDict(UserGroup userGroup);

        /**
         * 存储字典数据
         *
         * @param dict
         */
        void saveDict(Dict dict);

        /**
         * 获取某账号下的工作站列表
         *
         * @param loginName
         */
        Observable<Result<List<User.MnUserVsGroupVPOJOListBean>>> getApplicationList(String loginName);
    }
}
