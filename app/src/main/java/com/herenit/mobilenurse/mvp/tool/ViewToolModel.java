package com.herenit.mobilenurse.mvp.tool;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.integration.IRepositoryManager;
import com.herenit.arms.mvp.BaseModel;
import com.herenit.mobilenurse.api.service.DownloadService;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * author: HouBin
 * date: 2019/8/15 10:52
 * desc:查看工具M层
 */
@ActivityScope
public class ViewToolModel extends BaseModel implements ViewToolContract.Model {

    @Inject
    public ViewToolModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<ResponseBody> downloadFile(String url) {
        return mRepositoryManager.obtainRetrofitService(DownloadService.class)
                .downloadFile(url);
    }
}
