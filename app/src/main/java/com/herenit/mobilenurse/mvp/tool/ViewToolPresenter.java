package com.herenit.mobilenurse.mvp.tool;

import android.text.TextUtils;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.mobilenurse.app.utils.FileUtils;

import java.io.File;
import java.io.InputStream;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.ResponseBody;

/**
 * author: HouBin
 * date: 2019/2/18 14:00
 * desc:“查看工具”功能  Presenter
 */
@ActivityScope
public class ViewToolPresenter extends BasePresenter<ViewToolContract.Model, ViewToolContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;


    @Inject
    ViewToolPresenter(ViewToolContract.Model model, ViewToolContract.View view) {
        super(model, view);
    }

    public void downloadFile(String url, String fileType) {
        if (TextUtils.isEmpty(url))
            return;
        mModel.downloadFile(url)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })
                .unsubscribeOn(Schedulers.io())//当视图消亡时，会直接取消订阅，尤其在下载文件时
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                })
                ////用于计算任务，如事件循环或和回调处理，不要用于IO操作(IO操作请使用Schedulers.io())；默认线程数等于处理器的数量
                .observeOn(Schedulers.computation())
                .doOnNext(new Consumer<InputStream>() {//doOnNext在Observer的OnNext之前被调用，一般用在subscribe之前，比如做数据的保存
                    @Override
                    public void accept(InputStream inputStream) throws Exception {
                        String fileName = FileUtils.createCommonFileName(fileType);
                        if (!TextUtils.isEmpty(fileName)) {
                            File file = new File(FileUtils.getAppDownloadSaveDirectory(), fileName);
                            FileUtils.writeStreamToFile(inputStream, file);
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .subscribe(new ErrorHandleSubscriber<InputStream>(mErrorHandler) {
                    @Override
                    public void onNext(InputStream inputStream) {
                        mRootView.downloadSuccess(FileUtils.getCommonFileNameUrl(fileType));
                    }
                });

    }
}
