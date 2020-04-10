package com.herenit.mobilenurse.custom.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: HouBin
 * date: 2019/2/16 9:46
 * desc: 页面加载的弹窗
 */
public class LoadingDialog {
    Unbinder mUnbinder;
    Dialog mDialog;
    @BindView(R2.id.tv_loadingDialog_loadingDesc)
    TextView mTv_message;

    public LoadingDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        mDialog = new Dialog(context);
        mUnbinder = ButterKnife.bind(this, view);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void show() {
        if (mDialog != null)
            mDialog.show();
    }

    public void show(String message) {
        mTv_message.setText(message);
        if (mDialog != null)
            mDialog.show();
    }

    public void dismiss() {
        if (mDialog != null)
            mDialog.dismiss();
    }

    public boolean isShowing() {
        return mDialog != null && mDialog.isShowing();
    }
}
