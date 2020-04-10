package com.herenit.mobilenurse.mvp.tool;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.di.component.DaggerPDFViewComponent;
import com.herenit.mobilenurse.mvp.base.BasicWebViewActivity;

/**
 * author: HouBin
 * date: 2019/9/4 14:49
 * desc: PDF文件查看
 */
public class PDFViewActivity extends BasicWebViewActivity<ViewToolPresenter> implements ViewToolContract.View {

    private LoadingDialog mLoadingDialog;

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.NONE;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPDFViewComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        setUseStatusBarView(false);
        return R.layout.activity_full_screen_webview;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        String url = getIntent().getStringExtra(KeyConstant.NAME_EXTRA_URL);//PDF路径地址
        boolean isFromService = getIntent().getBooleanExtra(KeyConstant.NAME_EXTRA_COMMON_BOOLEAN, false);//是否来自于服务器
        if (isFromService) {
            mPresenter.downloadFile(url, CommonConstant.FILE_TYPE_NAME_PDF);
        } else {
            load("file:///android_asset/pdfjs/web/viewer.html?file=" + url, CommonConstant.MEDIA_TYPE_URL);
        }
    }

    @Override
    protected void onPageLoadFinished(WebView view, String url) {

    }

    /**
     * 文件下载成功
     *
     * @param url
     */
    @Override
    public void downloadSuccess(String url) {
        load("file:///android_asset/pdfjs/web/viewer.html?file=" + url, CommonConstant.MEDIA_TYPE_URL);
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog == null)
            mLoadingDialog = createLoadingDialog();
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
    }
}
