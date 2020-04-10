package com.herenit.mobilenurse.custom.widget.progressbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/1/8 9:45
 * desc: 加载的ProgressBar
 */
public class LoadingProgressBar extends BaseProgressBar {

    @BindView(R2.id.pb_progressBar_loading)
    ProgressBar mPb_loading;
    @BindView(R2.id.tv_progressBar_loadingDesc)
    TextView mTv_loadingDesc;

    public LoadingProgressBar(Context context) {
        super(context);
    }

    public LoadingProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.progressbar_loading;
    }

    public void setMessage(String message) {
        mTv_loadingDesc.setText(message);
    }

    /**
     * 调用此方法时，已经完成了页面加载，此方法主要设置ProgressBar的一些特性监听
     *
     * @param layout
     */
    @Override
    public void setView(View layout) {

    }
}
