package com.herenit.mobilenurse.mvp.launch;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.CommonService;
import com.herenit.mobilenurse.api.service.DownloadService;
import com.herenit.mobilenurse.api.service.VersionManagerService;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.VersionInfo;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * author: HouBin
 * date: 2019/1/9 13:53
 * desc: Loading页面的Model
 */
@ActivityScope
public class LoadingModel extends BaseModel implements LoadingContract.Model {

    @Inject
    public LoadingModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    /**
     * 获取版本信息
     *
     * @return
     */
    @Override
    public Observable<Result<VersionInfo>> getVersionInfo() {
        return mRepositoryManager.obtainRetrofitService(VersionManagerService.class)
                .getVersionInfo();
    }

    /**
     * 下载apk安装包
     *
     * @return
     */
    @Override
    public Observable<ResponseBody> downloadMobileNurseApk() {
        return mRepositoryManager.obtainRetrofitService(DownloadService.class)
                .downloadMobileNurseApk();
    }

    /**
     * 获取服务器时间
     *
     * @return
     */
    @Override
    public Observable<Result<Long>> getServerTime() {
        return mRepositoryManager.obtainRetrofitService(CommonService.class)
                .getServerTime();
    }
}
