package com.herenit.mobilenurse.mvp.tool;

import com.herenit.arms.mvp.IModel;
import com.herenit.arms.mvp.IView;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * author: HouBin
 * date: 2019/2/18 13:59
 * desc:“查看工具”  Contract
 */
public interface ViewToolContract {
    interface View extends IView {
        void downloadSuccess(String url);
    }

    interface Model extends IModel {
        /**
         * 文件下载
         *
         * @return
         */
        Observable<ResponseBody> downloadFile(String url);
    }
}
