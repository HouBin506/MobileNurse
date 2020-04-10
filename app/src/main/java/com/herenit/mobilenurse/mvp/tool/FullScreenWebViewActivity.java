package com.herenit.mobilenurse.mvp.tool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebView;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.mvp.base.BasicWebViewActivity;

/**
 * author: HouBin
 * date: 2019/9/4 14:49
 * desc: 全屏显示网页查看
 */
public class FullScreenWebViewActivity extends BasicWebViewActivity {

    private LoadingDialog mLoadingDialog;

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.NONE;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        setUseStatusBarView(false);
        return R.layout.activity_full_screen_webview;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        String html = getIntent().getStringExtra(KeyConstant.NAME_EXTRA_HTML);//要加载的内容（url路径，或者html文本.....）
        String mediaType = getIntent().getStringExtra(KeyConstant.NAME_EXTRA_MEDIA_TYPE);//媒体类型
        if (TextUtils.isEmpty(html) || TextUtils.isEmpty(mediaType))
            return;
        load(html, mediaType);
    }

    @Override
    protected void onPageLoadFinished(WebView view, String url) {

    }

}
