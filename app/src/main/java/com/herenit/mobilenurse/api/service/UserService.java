package com.herenit.mobilenurse.api.service;

import com.herenit.mobilenurse.api.Api;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.criteria.entity.dict.Dict;
import com.herenit.mobilenurse.criteria.entity.submit.ChangePassword;
import com.herenit.mobilenurse.criteria.entity.submit.Login;
import com.herenit.mobilenurse.criteria.entity.submit.UserGroup;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * author: HouBin
 * date: 2019/1/4 13:55
 * desc: 与用户相关的ApI接口，如登录、账号密码验证、密码修改等
 */
public interface UserService {
    /**
     * 登录
     *
     * @param login
     * @return
     */
    @POST(Api.LOGIN_PATH)
    Observable<Result<User>> login(@Body Login login);

    /**
     * 账号密码验证
     *
     * @param login
     * @return
     */
    @POST("mobileNurse/loginHandler/onlyValidation")
    Observable<Result> verify(@Body Login login);

    /**
     * 修改密码
     *
     * @param changePassword
     * @return
     */
    @PUT("mobileNurse/loginHandler/updatePassword")
    Observable<Result> changePassword(@Body ChangePassword changePassword);

    /**
     * 登出
     *
     * @return
     */
    @GET("common/loginHandler/logout")
    Observable<Result> logout();

    /**
     * 获取常用字典表
     *
     * @return
     */
    @POST("mobileNurse/loginHandler/getAllDict")
    Observable<Result<Dict>> getAllDict(@Body UserGroup userGroup);

    /**
     * 根据账号获取工作站列表
     *
     * @param loginName
     * @return
     */
    @GET("mobileNurse/loginHandler/getApplicationList")
    Observable<Result<List<User.MnUserVsGroupVPOJOListBean>>> getApplicationList(@Query(CommonConstant.FIELD_NAME_LOGIN_NAME) String loginName);
}
