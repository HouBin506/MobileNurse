package com.herenit.mobilenurse.Interaction.js;

import android.webkit.JavascriptInterface;

import com.herenit.mobilenurse.api.ApiManager;

/**
 * author: HouBin
 * date: 2019/8/30 10:05
 * desc:常用的、通用的JS交互接口.JS调用此接口的方法，WebView实现此接口，即可实现JS调用Android原生的方法
 */
public class CommonJsInterface implements JSInterface {

    private CommonJsInterfaceListener listener;

    public CommonJsInterface(CommonJsInterfaceListener listener) {
        this.listener = listener;
    }

    /**
     * 打开PDF
     *
     * @param url
     */
    @JavascriptInterface
    public void openPDF(String url) {
        listener.openPDF(url);
    }

    /**
     * 打开全屏的WebView
     *
     * @param html
     * @param mediaType
     */
    @JavascriptInterface
    public void openFullWebView(String html, String mediaType) {
        listener.openFullWebView(html, mediaType);
    }

    /**
     * 常用的回调方法
     */
    public interface CommonJsInterfaceListener {
        default void openPDF(String url) {

        }

        default void openFullWebView(String html, String mediaType) {

        }
    }
}
