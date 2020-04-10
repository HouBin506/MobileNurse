package com.herenit.mobilenurse.custom.widget.dialog;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.enums.NoticeLevelEnum;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;


import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/1/10 9:31
 * desc: 提示弹窗，专门用来做弹窗提示的
 */
public class NoticeDialog extends BaseDialog {
    //弹框message
    @BindView(R2.id.tv_noticeDialog_message)
    TextView mTv_message;

    private String mMessage;
    private boolean mCanceledOnTouchOutside;

    /**
     * 弹窗类型
     */
    private NoticeLevelEnum mLevel;

    NoticeDialog(Builder builder) {
        super(builder);
    }

    @Override
    protected void setView(View layout) {
        mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        if (mLevel == NoticeLevelEnum.NORMAL) {
            mTv_title.setText(ArmsUtils.getString(mContext, R.string.title_dialog_notice));
        } else if (mLevel == NoticeLevelEnum.WARNING) {
            mTv_title.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_notice_warning, 0, 0, 0);
            mTv_title.setText(ArmsUtils.getString(mContext, R.string.title_dialog_warning));
        } else if (mLevel == NoticeLevelEnum.ERROR) {
            mTv_title.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_notice_error, 0, 0, 0);
            mTv_title.setText(ArmsUtils.getString(mContext, R.string.title_dialog_error));
        }
        if (!TextUtils.isEmpty(mMessage)) {
            mTv_message.setVisibility(View.VISIBLE);
            mTv_message.setText(mMessage);
        } else {
            mTv_message.setVisibility(View.GONE);
        }
    }

    @Override
    protected <T extends BaseDialog.Builder> void buildDialog(T builder) {
        Builder nBuilder = (Builder) builder;
        this.mMessage = nBuilder.message;
        this.mCanceledOnTouchOutside = nBuilder.canceledOnTouchOutside;
        this.mLevel = nBuilder.level;
    }

    /**
     * @param listener 需要实现{@link PositiveNegativeListener#onNegative()}
     *                 和{@link PositiveNegativeListener#onPositive()}方法
     */
    @Override
    public void setPositiveNegativeListener(PositiveNegativeListener listener) {
        if (listener != null) {
            mTv_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPositive();
                }
            });
            mTv_negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNegative();
                }
            });
        }
    }

    /**
     * 设置提示信息
     */
    public void setMessage(String message) {
        mMessage = TextUtils.isEmpty(message) ? "" : message;
        if (mTv_message != null)
            mTv_message.setText(mMessage);
    }

    public void setTitle(String title) {
        mTitle = title;
        if (!TextUtils.isEmpty(title))
            mTv_title.setText(title);
        else
            mTv_title.setText("");
    }

    public void setLevel(NoticeLevelEnum level) {
        if (level == NoticeLevelEnum.NORMAL) {
            mTv_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (level == NoticeLevelEnum.WARNING) {
            mTv_title.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_notice_warning, 0, 0, 0);
        } else if (level == NoticeLevelEnum.ERROR) {
            mTv_title.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_notice_error, 0, 0, 0);
        }
    }

    /**
     * 构建NoticeDialog
     */
    public static final class Builder extends BaseDialog.Builder<Builder> {
        private String message;
        //点击弹窗外部是否取消弹窗
        private boolean canceledOnTouchOutside;
        /**
         * 弹窗类型
         */
        private NoticeLevelEnum level;

        public Builder(Context context) {
            super(context, R.layout.dialog_notice);
            canceledOnTouchOutside = false;
            level = NoticeLevelEnum.NORMAL;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder level(NoticeLevelEnum level) {
            this.level = level;
            return this;
        }

        public Builder canceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        @Override
        public NoticeDialog build() {
            return new NoticeDialog(this);
        }
    }
}
