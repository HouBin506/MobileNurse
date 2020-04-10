package com.herenit.mobilenurse.custom.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.custom.widget.progressbar.DownloadProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: HouBin
 * date: 2019/2/16 9:46
 * desc: 页面加载的弹窗
 */
public class DownloadDialogDialog {
    Unbinder mUnbinder;
    Dialog mDialog;
    @BindView(R2.id.pb_downloadDialog_download)
    DownloadProgressBar mDpb_download;

    public DownloadDialogDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_download, null);
        mDialog = new Dialog(context);
        mUnbinder = ButterKnife.bind(this, view);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void setProgress(int progress) {
        mDpb_download.setProgress(progress);
    }

    public void setMessage(String message) {
        mDpb_download.setMessage(message);
    }

    public void show() {
        if (mDialog != null)
            mDialog.show();
    }

    public void show(String message) {
        if (!TextUtils.isEmpty(message))
            mDpb_download.setMessage(message);
        if (mDialog != null)
            mDialog.show();
    }

    public void dismiss() {
        if (mDialog != null)
            mDialog.dismiss();
    }
}
