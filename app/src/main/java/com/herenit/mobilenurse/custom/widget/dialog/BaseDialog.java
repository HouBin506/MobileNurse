package com.herenit.mobilenurse.custom.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.arms.utils.Preconditions;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.extra.WidgetExtra;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: HouBin
 * date: 2019/1/9 16:51
 * desc: Dialog基类
 */
public abstract class BaseDialog implements WidgetExtra {

    protected Unbinder mUnbinder;
    protected Dialog mDialog;
    protected Context mContext;

    @BindView(R2.id.ll_dialog_title)
    LinearLayout mLl_title;
    @BindView(R2.id.tv_dialog_title)
    TextView mTv_title;

    @BindView(R2.id.ll_dialog_bottom)
    LinearLayout mLL_bottom;
    @BindView(R2.id.tv_dialog_positive)
    TextView mTv_positive;
    @BindView(R2.id.tv_dialog_negative)
    TextView mTv_negative;
    @BindView(R2.id.view_dialog_bottomCenter)
    View mView_centerLine;

    private View mContentView;

    protected String mPositiveText;
    protected String mNegativeText;
    protected String mTitle;
    private boolean mTitleVisibility;
    protected boolean mPositiveVisibility;
    protected boolean mNegativeVisibility;
    protected boolean mBottomVisibility;

    protected BaseDialog(Builder builder) {
        this.mContext = builder.context;
        this.mBottomVisibility = builder.bottomVisibility;
        this.mNegativeText = builder.negativeText;
        this.mPositiveText = builder.positiveText;
        this.mNegativeVisibility = builder.negativeVisibility;
        this.mPositiveVisibility = builder.positiveVisibility;
        this.mTitle = builder.title;
        this.mTitleVisibility = builder.titleVisibility;
        buildDialog(builder);
        setView(initLayout(builder.layoutId));
        setTitleBottomView();
    }

    /**
     * 设置底部布局，确认取消按钮
     */
    private void setTitleBottomView() {
        if (mTitleVisibility) {
            mLl_title.setVisibility(View.VISIBLE);
        } else {
            mLl_title.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mTitle))
            mTv_title.setText(mTitle);
        mTv_positive.setText(mPositiveText);
        mTv_negative.setText(mNegativeText);
        mTv_positive.setVisibility(View.VISIBLE);
        mTv_negative.setVisibility(View.VISIBLE);
        mLL_bottom.setVisibility(View.VISIBLE);
        mView_centerLine.setVisibility(View.VISIBLE);
        if (!mBottomVisibility || (!mPositiveVisibility && !mNegativeVisibility)) {//底部按钮均不显示
            mLL_bottom.setVisibility(View.GONE);
        } else if (!mNegativeVisibility) {//只显示确定按钮
            mView_centerLine.setVisibility(View.GONE);
            mTv_negative.setVisibility(View.GONE);
        } else if (!mPositiveVisibility) {//只显示取消按钮
            mView_centerLine.setVisibility(View.GONE);
            mTv_positive.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化布局
     *
     * @param layoutId
     */
    private View initLayout(@IdRes int layoutId) {
        mContentView = LayoutInflater.from(mContext).inflate(Preconditions.checkIntGreaterThan(layoutId, 0, "layoutId"), null);
        mUnbinder = ButterKnife.bind(this, mContentView);
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(mContentView);
        return mContentView;
    }


    protected abstract <T extends Builder> void buildDialog(T builder);

    /**
     * 设置一些控件的属性、监听等内容，由子类实现
     */
    protected abstract void setView(View layout);


    public void show() {
        setDialogSize(mContentView);
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    /**
     * 设置监听,具体要实现Listener的哪几个方法，要看Dialog的子类是否传递了参数
     * {@link PositiveNegativeListener}的每一个方法都不强制要求实现，请根据
     * {@link BaseDialog#setView(View)}方法中针对弹窗的按钮设置的监听方法，来具体
     * 实现{@link PositiveNegativeListener}的方法
     *
     * @param listener
     * @return
     */
    public abstract void setPositiveNegativeListener(PositiveNegativeListener listener);

    @Override
    public void release() {
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
    }

    public static abstract class Builder<T extends Builder> {
        private Context context;
        @IdRes
        private int layoutId;
        private String title;
        private boolean titleVisibility;
        private boolean bottomVisibility;
        private String negativeText;
        private String positiveText;
        private boolean negativeVisibility;
        private boolean positiveVisibility;
        @NonNull
        private T mBuilder;

        public Builder(Context context, @IdRes int layoutId) {
            mBuilder = (T) this;
            this.context = context;
            this.layoutId = layoutId;
            positiveText = ArmsUtils.getString(context, R.string.btn_sure);
            negativeText = ArmsUtils.getString(context, R.string.btn_cancel);
            titleVisibility = true;
            positiveVisibility = true;
            negativeVisibility = true;
            bottomVisibility = true;
        }

        /**
         * 设置弹窗标题
         *
         * @param title
         * @return
         */
        public T title(String title) {
            this.title = title;
            return mBuilder;
        }

        public T titleVisibility(boolean titleVisibility) {
            this.titleVisibility = titleVisibility;
            return mBuilder;
        }

        /**
         * 设置底部布局是否显示
         *
         * @param bottomVisibility
         * @return
         */
        public T bottomVisibility(boolean bottomVisibility) {
            this.bottomVisibility = bottomVisibility;
            return mBuilder;
        }

        /**
         * 设置负极按钮的Text
         *
         * @param negativeText
         * @return
         */
        public T negativeText(String negativeText) {
            this.negativeText = negativeText;
            return mBuilder;
        }

        /**
         * 设置正极按钮Text
         *
         * @param positiveText
         * @return
         */
        public T positiveText(String positiveText) {
            this.positiveText = positiveText;
            return mBuilder;
        }

        /**
         * 设置负极按钮显示
         *
         * @param negativeVisibility
         * @return
         */
        public T negativeVisibility(boolean negativeVisibility) {
            this.negativeVisibility = negativeVisibility;
            return mBuilder;
        }

        /**
         * 设置正极按钮显示
         *
         * @param positiveVisibility
         * @return
         */
        public T positiveVisibility(boolean positiveVisibility) {
            this.positiveVisibility = positiveVisibility;
            return mBuilder;
        }


        public abstract <T extends BaseDialog> T build();
    }

    /**
     * 在dialog.show()前调用此方法
     *
     * @param mView dialog要显示的view
     */
    private void setDialogSize(final View mView) {
        mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                int heightNow = v.getHeight();//dialog当前的高度
                int widthNow = v.getWidth();//dialog当前的宽度
                int needWidth = (int) (((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth() * 0.7);//最小宽度为屏幕的0.7倍
                int needHeight = (int) (((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight() * 0.6);//最大高度为屏幕的0.6倍
                if (widthNow < needWidth || heightNow > needHeight) {
                    if (widthNow > needWidth) {
                        needWidth = FrameLayout.LayoutParams.WRAP_CONTENT;
                    }
                    if (heightNow < needHeight) {
                        needHeight = FrameLayout.LayoutParams.WRAP_CONTENT;
                    }
                    mView.setLayoutParams(new FrameLayout.LayoutParams(needWidth,
                            needHeight));
                }
            }
        });
    }

    public boolean isShowing() {
        if (mDialog == null)
            return false;
        return mDialog.isShowing();
    }
}
