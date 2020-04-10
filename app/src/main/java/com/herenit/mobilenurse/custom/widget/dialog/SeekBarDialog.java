package com.herenit.mobilenurse.custom.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.custom.listener.OnProgressChangeListener;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/3/19 10:24
 * desc:可拖拽的普通进度条
 */
public class SeekBarDialog extends BaseDialog {
    @BindView(R2.id.tv_seekBarDialog_progress)
    TextView mTv_progress;//实时进度
    @BindView(R2.id.tv_seekBarDialog_minProgressText)
    TextView mTv_minProgress;//最小进度
    @BindView(R2.id.tv_seekBarDialog_maxProgressText)
    TextView mTv_maxProgress;//最大进度
    @BindView(R2.id.sb_seekBarDialog_seekBar)
    SeekBar mSeekBar;//进度条

    private String mMinProgress;
    private String mMaxProgress;
    private String mCurrentProgress;
    private int mProgress;

    private OnProgressChangeListener mProgressChangeListener;


    protected SeekBarDialog(Builder builder) {
        super(builder);
    }

    @Override
    protected <T extends BaseDialog.Builder> void buildDialog(T builder) {
        if (!(builder instanceof Builder))
            return;
        Builder sBuilder = (Builder) builder;
        mMaxProgress = sBuilder.maxProgress;
        mMinProgress = sBuilder.minProgress;
        mCurrentProgress = sBuilder.currentProgress;
        mProgress = sBuilder.progress;
        mProgressChangeListener = sBuilder.progressChangeListener;
    }

    @Override
    protected void setView(View layout) {
        //如果当前进度值字符串未定义，则默认为百分数
        if (TextUtils.isEmpty(mCurrentProgress))
            mCurrentProgress = mProgress + "%";
        mTv_minProgress.setText(mMinProgress);
        mTv_maxProgress.setText(mMaxProgress);
        mTv_progress.setText(mCurrentProgress);
        mSeekBar.setProgress(mProgress);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress = progress;
                if (mProgressChangeListener == null) {
                    mCurrentProgress = progress + "%";//默认为百分数
                } else {
                    mCurrentProgress = mProgressChangeListener.onProgressChanged(progress);
                }
                mTv_progress.setText(mCurrentProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * 正负极按钮监听
     *
     * @param listener
     */
    @Override
    public void setPositiveNegativeListener(PositiveNegativeListener listener) {
        if (listener == null)
            return;
        mTv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPositive(mProgress);
            }
        });
        mTv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNegative(mProgress);
            }
        });
    }

    public static final class Builder extends BaseDialog.Builder<Builder> {
        private String minProgress;
        private String maxProgress;
        private String currentProgress;
        private int progress;

        private OnProgressChangeListener progressChangeListener;

        /**
         * 进度条默认为百分制，“0%”---“100%”
         *
         * @param context
         */
        public Builder(Context context) {
            super(context, R.layout.dialog_seek_bar);
            minProgress = "0%";
            minProgress = "100%";
            progress = 0;//默认进度为0
        }

        public Builder minProgress(String minProgress) {
            this.minProgress = minProgress;
            return this;
        }

        public Builder maxProgress(String maxProgress) {
            this.maxProgress = maxProgress;
            return this;
        }

        public Builder currentProgress(String currentProgress) {
            this.currentProgress = currentProgress;
            return this;
        }

        public Builder progress(int progress) {
            this.progress = progress;
            return this;
        }

        public Builder progressChangeListener(OnProgressChangeListener progressChangeListener) {
            this.progressChangeListener = progressChangeListener;
            return this;
        }


        @Override
        public SeekBarDialog build() {
            return new SeekBarDialog(this);
        }
    }
}
