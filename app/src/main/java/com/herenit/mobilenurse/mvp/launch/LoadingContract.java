package com.herenit.mobilenurse.mvp.launch;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.VersionInfo;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * author: HouBin
 * date: 2019/1/8 16:29
 * desc: Loading的一些View和Model方法
 */
public interface LoadingContract {

    interface View extends IView {
        /**
         * 显示下载进度条
         *
         * @param progress
         */
        void showDownloadProgressBar(int progress);

        /**
         * 关闭下载进度条
         */
        void dismissDownloadProgressBar();

        /**
         * 显示更新提示
         *
         * @param updateNow 是否强制更新
         */
        void showUpdateNotice(boolean updateNow);

        /**
         * 去登录
         */
        void toLogin();

        /**
         * 获取服务器时间
         */
        void loadServerTime();

        /**
         * 版本检测
         */
        void checkVersion();

    }

    interface Model extends IModel {


        /**
         * 获取系统时间
         *
         * @return
         */
        Observable<Result<Long>> getServerTime();

        /**
         * 获取当前APP版本信息，用于做更新升级
         *
         * @return
         */
        Observable<Result<VersionInfo>> getVersionInfo();

        /**
         * 下载apk安装包
         *
         * @return
         */
        Observable<ResponseBody> downloadMobileNurseApk();
    }

}
