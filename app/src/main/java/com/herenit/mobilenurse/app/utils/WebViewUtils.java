package com.herenit.mobilenurse.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.herenit.mobilenurse.Interaction.js.JSInterface;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.datastore.sp.ConfigSp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;

/**
 * author: HouBin
 * date: 2019/4/28 16:17
 * desc:WebView的工具类
 */
public class WebViewUtils {
    private WebViewUtils() {
    }

    /**
     * 初始化常用的WebView，并设置一些常用属性
     *
     * @param webView
     */
    public static void initNormalWebViewSetting(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= 19) {
//            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//加载缓存否则网络
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存
        }
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);//图片自动缩放 打开
        } else {
            webSettings.setLoadsImagesAutomatically(false);//图片自动缩放 关闭
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//软件解码
        }
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//硬件解码
   /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }*/
        // setMediaPlaybackRequiresUserGesture(boolean require) //是否需要用户手势来播放Media，默认true
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true);//设置是否允许WebView使用File协议，默认为true，即允许在File域下执行JavaScript代码；设为false，则不能加载本地html文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //是否允许通过file url加载的JS代码读取其他的本地文件，Android4.1前默认为true，4.1之后默认为false
            webSettings.setAllowFileAccessFromFileURLs(true);
            //是否允许通过file url加载的JS代码读取访问其他的源（包括http、https），Android4.1前默认为true，4.1之后默认为false
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        webSettings.setSupportZoom(true);// 设置可以支持缩放
        webSettings.setBuiltInZoomControls(true);// 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false
        webSettings.setDisplayZoomControls(false);//隐藏缩放工具
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webSettings.setUseWideViewPort(true);// 扩大比例的缩放
        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕的大小
        webSettings.setDatabaseEnabled(true);//开启 database storage API 功能
        webSettings.setSavePassword(true);//保存密码
        webSettings.setDomStorageEnabled(true);//是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        //是否支持多窗口加载页面，默认为false，不支持多窗口。当设置为true时，必须要重写WebChromeClient的onCreateWindow方法。
//        webSettings.setSupportMultipleWindows(true);//
        webView.setSaveEnabled(true);//当配置改变等情况发生时，是否保存View的状态数据
        webView.setKeepScreenOn(true);//保持屏幕常亮
//        webView.addJavascriptInterface(new AndroidToJs(), "android");//AndroidtoJS类对象映射到js的test对象
        //        webSettings.setPluginState(WebSettings.PluginState.ON);
//        webView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,webView.getHeight()));
    }

    /**
     * 将Cookie同步到WebView
     *
     * @param url WebView要加载的url
     */
    public static void synCookies(String url, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        List<String> cookieList = ConfigSp.getInstance().getCookie();
        if (cookieList == null || cookieList.isEmpty())
            return;
        for (String cookie : cookieList) {
            cookieManager.setCookie(url, cookie);
        }
        String newCookie = cookieManager.getCookie(url);
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 添加JS与Android交互的接口类
     *
     * @param jsInterfaceMap
     */
    @SuppressLint("JavascriptInterface")
    protected void addJavascriptInterface(WebView webView, HashMap<String, Object> jsInterfaceMap) {
        if (webView == null || jsInterfaceMap == null || jsInterfaceMap.size() == 0)
            return;

        for (Map.Entry<String, Object> entry : jsInterfaceMap.entrySet()) {
            String name = entry.getKey();
            Object jsInterface = entry.getValue();
            if (jsInterface == null)
                continue;
            webView.addJavascriptInterface(jsInterface, name);
        }
    }

    /**
     * 设置WebChromeClient组件
     * 主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等比如
     * 如果WebView只处理html的一些内容，则不需要设置WebChromeClient
     *
     * @param webChromeClient
     */
    protected void setWebChromeClient(WebView webView, WebChromeClient webChromeClient) {
        if (webView != null && webChromeClient != null)
            webView.setWebChromeClient(webChromeClient);
    }

    /**
     * 设置WebViewClient组件，如果不设置这个组件，WebView会根据用户选择打开浏览器加载html，设置此组件可在WebView自带浏览器打开
     * <p>
     * 主要帮助WebView处理各种通知、请求事件的，
     *
     * @param webViewClient
     */
    protected void setWebViewClient(WebView webView, WebViewClient webViewClient) {
        if (webView != null && webViewClient != null)
            webView.setWebViewClient(webViewClient);
    }

    /**
     * 设置文件下载，当碰到页面有下载链接的时候，点上去是没有反应的，WebView默认没有下载功能，
     * 只有设置了DownloadListener，通过实现自己的只有设置了DownloadListener来实现文件下载
     *
     * @Override public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
     * long contentLength) {
     * Log.i("tag", "url="+url);
     * Log.i("tag", "userAgent="+userAgent);
     * Log.i("tag", "contentDisposition="+contentDisposition);
     * Log.i("tag", "mimetype="+mimetype);
     * Log.i("tag", "contentLength="+contentLength);
     * Uri uri = Uri.parse(url);
     * Intent intent = new Intent(Intent.ACTION_VIEW, uri);
     * startActivity(intent);
     * }
     */
    protected void setDownloadListener(WebView webView, DownloadListener downloadListener) {
        if (webView != null && downloadListener != null)
            webView.setDownloadListener(downloadListener);
    }

    protected void loadUrl(WebView webView, String url) {
        if (webView != null && !TextUtils.isEmpty(url))
            webView.loadUrl(url);
    }

}

