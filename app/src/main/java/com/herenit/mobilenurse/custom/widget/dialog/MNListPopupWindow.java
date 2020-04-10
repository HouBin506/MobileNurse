package com.herenit.mobilenurse.custom.widget.dialog;

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
import com.herenit.mobilenurse.app.utils.ScreenUtils;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.enums.ChoiceTypeEnum;

/**
 * author: HouBin
 * date: 2019/8/6 10:22
 * desc: 下拉列表选择框（单选）
 */
public class MNListPopupWindow extends LinearLayout {
    private Context mContext;
    private String mPrefix;//前缀
    private String mSuffix;//后缀
    private String mContent;

    private TextView mTv_prefix;//前缀
    private TextView mTv_suffix;//后缀
    private TextView mTv_content;
    private ListPopupWindow mListPopupWindow;
    private AdapterView.OnItemClickListener mItemClickListener;


    public MNListPopupWindow(Builder builder) {
        super(builder.context);
        this.mContext = builder.context;
        this.mPrefix = builder.prefix;
        this.mSuffix = builder.suffix;
        this.mContent = builder.content;
        initView(builder);
    }

    private void initView(Builder builder) {
        View contentView = LayoutInflater.from(mContext).inflate(builder.layoutId, this);
        mTv_prefix = contentView.findViewById(R.id.tv_mnListPopupWindow_prefix);
        mTv_suffix = contentView.findViewById(R.id.tv_mnListPopupWindow_suffix);
        mTv_content = contentView.findViewById(R.id.tv_mnListPopupWindow_content);
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
                showListPopupWindow(builder);
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        mTv_content.setEnabled(enabled);
    }

    private void showListPopupWindow(Builder builder) {
        if (mListPopupWindow == null) {
            mListPopupWindow = ViewUtils.createListPopupWindow(mContext, builder.adapter,
                    mTv_content.getWidth(), LayoutParams.WRAP_CONTENT, mTv_content, 0, 0);
            mListPopupWindow.setOnItemClickListener(mItemClickListener);
        }
        mListPopupWindow.show();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void show() {
        if (mListPopupWindow != null) {
            mListPopupWindow.show();
        }
    }

    public void dismiss() {
        if (mListPopupWindow != null)
            mListPopupWindow.dismiss();
    }

    public static class Builder {
        private Context context;
        private String prefix;//前缀
        private String suffix;//后缀

        private String content;

        private ListAdapter adapter;
        //弹出列表单选或者多选
        private ChoiceTypeEnum choiceType;

        private int layoutId;

        public Builder(Context context) {
            this.context = context;
            this.layoutId = R.layout.mn_list_popup_window;
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

        public Builder adapter(ListAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public MNListPopupWindow build() {
            return new MNListPopupWindow(this);
        }
    }
}
