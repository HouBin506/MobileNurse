package com.herenit.mobilenurse.api.service;

import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.VersionInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * author: HouBin
 * date: 2019/1/9 13:55
 * desc: 版本管理的接口API，包括获取版本信息，下载安装包等接口
 */
public interface VersionManagerService {
    @GET("mobileNurse/versionHandler/getVersionInformation")
    Observable<Result<VersionInfo>> getVersionInfo();

}
