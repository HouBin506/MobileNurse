package com.herenit.mobilenurse.custom.widget.layout;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.dialog.SingleInputDialog;

/**
 * author: HouBin
 * date: 2019/8/9 10:49
 * desc:移动护理特有的单输入框弹窗。基础组件是“TextView+TextView+TextView”,
 * 当点击中间的TextView则弹出单输入框的弹窗，输入完毕点击确定按钮，就会完成输入
 */
public class MNSingleInputDialog extends LinearLayout {

    private Context mContext;
    private String mPrefix;//前缀
    private String mSuffix;//后缀
    private String mContent;

    private TextView mTv_prefix;//前缀
    private TextView mTv_suffix;//后缀
    private TextView mTv_content;
    private SingleInputDialog mSingleInputDialog;

    private PositiveNegativeListener mPositiveNegativeListener;


    public MNSingleInputDialog(MNSingleInputDialog.Builder builder) {
        super(builder.context);
        this.mContext = builder.context;
        this.mPrefix = builder.prefix;
        this.mSuffix = builder.suffix;
        this.mContent = builder.content;
        initView(builder);
    }

    private void initView(MNSingleInputDialog.Builder builder) {
        View contentView = LayoutInflater.from(mContext).inflate(builder.layoutId, this);
        mTv_prefix = contentView.findViewById(R.id.tv_mnSingleInputDialog_prefix);
        mTv_suffix = contentView.findViewById(R.id.tv_mnSingleInputDialog_suffix);
        mTv_content = contentView.findViewById(R.id.tv_mnSingleInputDialog_content);
        if (!TextUtils.isEmpty(mContent))
            mTv_content.setText(mContent);
        else
            mTv_content.setText("");
        if (!TextUtils.isEmpty(mPrefix)) {
            mTv_prefix.setVisibility(VISIBLE);
            mTv_prefix.setText(mPrefix);
        } else {
            mTv_prefix.setText("");
            mTv_prefix.setVisibility(INVISIBLE);
        }
        if (!TextUtils.isEmpty(mSuffix)) {
            mTv_suffix.setVisibility(VISIBLE);
            mTv_suffix.setText(mSuffix);
        } else {
            mTv_suffix.setText("");
            mTv_suffix.setVisibility(INVISIBLE);
        }
        mTv_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(builder);
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        mTv_content.setEnabled(enabled);
    }

    public void setPositiveNegativeListener(PositiveNegativeListener listener) {
        this.mPositiveNegativeListener = listener;
    }

    private void showInputDialog(Builder builder) {
        if (mSingleInputDialog == null) {
            mSingleInputDialog = new SingleInputDialog.Builder(mContext)
                    .content(builder.content)
                    .descVisibility(false)
                    .title(builder.inputTitle)
                    .build();
            mSingleInputDialog.setPositiveNegativeListener(mPositiveNegativeListener);
        }
        mSingleInputDialog.show();
    }

    public void show() {
        if (mSingleInputDialog != null) {
            mSingleInputDialog.show();
        }
    }

    public void dismiss() {
        if (mSingleInputDialog != null)
            mSingleInputDialog.dismiss();
    }

    public static class Builder {
        private Context context;
        private String prefix;//前缀
        private String suffix;//后缀

        private String content;
        private String hint;
        private String inputTitle;

        private int layoutId;

        public Builder(Context context) {
            this.context = context;
            this.layoutId = R.layout.mn_single_input_dialog;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder hint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder inputTitle(String inputTitle) {
            this.inputTitle = inputTitle;
            return this;
        }

        public MNSingleInputDialog build() {
            return new MNSingleInputDialog(this);
        }
    }
}
