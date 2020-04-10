package com.herenit.mobilenurse.custom.widget.progressbar;

import android.app.Application;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/1/11 16:59
 * desc:
 */
public class DownloadProgressBar extends BaseProgressBar {

    @BindView(R2.id.tv_progressBar_downloadDesc)
    TextView mTv_progress;
    @BindView(R2.id.pb_progressBar_download)
    ProgressBar mPb_progress;

    public DownloadProgressBar(Context context) {
        super(context);
    }

    public DownloadProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgress(int progress) {
        mPb_progress.setProgress(progress);
    }

    public void setMessage(String message) {
        mTv_progress.setText(message);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.progressbar_download;
    }

    @Override
    public void setView(View layout) {
//        String message = ArmsUtils.getString(mContext, R.string.message_completed) + "0";
        mTv_progress.setText("已完成");
    }
}
