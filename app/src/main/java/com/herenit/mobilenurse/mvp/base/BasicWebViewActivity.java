package com.herenit.mobilenurse.mvp.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.herenit.arms.mvp.BasePresenter;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.app.utils.WebViewUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/8/28 14:24
 * desc:加载网页的基类Activity ,
 * 子类需要引入 {@link R.layout#include_webview}布局，方可使用
 */
public abstract class BasicWebViewActivity<P extends BasePresenter> extends BasicBusinessActivity<P> {

    @BindView(R2.id.fl_webView_container)
    FrameLayout mContaner;
    //    @BindView(R2.id.webView)
    WebView mWebView;
    @BindView(R2.id.pb_webView)
    ProgressBar mProgressBar;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initWebView();
    }

    protected void initWebView() {
        mWebView = new WebView(MobileNurseApplication.getInstance());
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mWebView.setClipToPadding(true);
        mWebView.setFitsSystemWindows(true);
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mContaner.addView(mWebView);

        WebViewUtils.initNormalWebViewSetting(mWebView);
        mProgressBar.setMax(100);
        mWebView.setBackgroundColor(0); // 设置背景色
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                }
                mProgressBar.setVisibility(View.GONE);
                onPageLoadFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return false;
                }
                // Otherwise allow the OS to handle things like tel, mailto, etc.
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });
    }

    /**
     * WebView加载页面
     *
     * @param html      加载的内容，可能是url，可能是html的文本内容，要看mediaType
     * @param mediaType
     */
    protected void load(String html, String mediaType) {
        WebViewUtils.synCookies(html, mContext);
        if (CommonConstant.MEDIA_TYPE_URL.equals(mediaType))
            mWebView.loadUrl(html);
        else if (CommonConstant.MEDIA_TYPE_TEXT_HTML.equals(mediaType))
            mWebView.loadData(html, mediaType, "utf-8");
    }

    @SuppressLint("JavascriptInterface")
    protected void addJavascriptInterface(Object obj, String interfaceName) {
        mWebView.addJavascriptInterface(obj, interfaceName);
    }

    /**
     * 页面加载完成
     *
     * @param view
     * @param url
     */
    protected abstract void onPageLoadFinished(WebView view, String url);

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            ViewParent viewParent = mWebView.getParent();
            if (viewParent != null)
                ((ViewGroup) viewParent).removeView(mWebView);
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.stopLoading();
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
