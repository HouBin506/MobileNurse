package com.herenit.mobilenurse.api.service;

import com.herenit.mobilenurse.api.Api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * author: HouBin
 * date: 2019/1/11 13:34
 * desc: 下载Api接口，比如app安装包的下载
 */
public interface DownloadService {

    /**
     * 下载移动护理安装包
     *
     * @return
     */
    @Streaming
    @GET(Api.DOWNLOAD_APP_FILE_PATH)
    Observable<ResponseBody> downloadMobileNurseApk();

    /**
     * 普通的文件下载（动态传入url）
     *
     * @param url
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);
}
