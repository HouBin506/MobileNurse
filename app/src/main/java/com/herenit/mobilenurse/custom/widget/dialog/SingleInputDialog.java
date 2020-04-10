package com.herenit.mobilenurse.custom.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.custom.extra.WidgetExtra;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/1/29 11:27
 * desc: 单输入框的弹窗
 */
public class SingleInputDialog extends BaseDialog {

    @BindView(R2.id.tv_singleInputDialog_inputDesc)
    TextView mTv_inputDesc;
    @BindView(R2.id.et_singleInputDialog_input)
    EditText mEt_input;

    private String mInputDesc;
    private String mHint;
    private boolean mDescVisibility;
    private String mContent;

    SingleInputDialog(Builder builder) {
        super(builder);
    }


    @Override
    protected <T extends BaseDialog.Builder> void buildDialog(T builder) {
        Builder sBuilder = (Builder) builder;
        mHint = sBuilder.hint;
        mInputDesc = sBuilder.inputDesc;
        mDescVisibility = sBuilder.descVisibility;
        mContent = sBuilder.content;
    }

    @Override
    protected void setView(View layout) {
        if (!TextUtils.isEmpty(mHint))
            mEt_input.setHint(mHint);
        if (!TextUtils.isEmpty(mInputDesc))
            mTv_inputDesc.setText(mInputDesc);
        if (mDescVisibility)
            mTv_inputDesc.setVisibility(View.VISIBLE);
        else
            mTv_inputDesc.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mContent))
            mEt_input.setText(mContent);
    }

    public void setTitle(String title) {
        mTitle = title;
        if (!TextUtils.isEmpty(title))
            mTv_title.setText(title);
        else
            mTv_title.setText("");
    }

    public void setContent(String content) {
        mContent = content;
        if (!TextUtils.isEmpty(content))
            mEt_input.setText(content);
        else
            mEt_input.setText("");
    }

    public void setHint(String hint) {
        mHint = hint;
        if (!TextUtils.isEmpty(hint))
            mEt_input.setHint(hint);
        else
            mEt_input.setHint("");
    }

    /**
     * 设置确定取消按钮监听，如果确定按钮，需要将输入框中的数据返回给调用者
     *
     * @param listener
     */
    @Override
    public void setPositiveNegativeListener(PositiveNegativeListener listener) {
        mTv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEt_input.getText().toString();
                listener.onPositive(content);
            }
        });
        mTv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNegative();
            }
        });
    }

    public static final class Builder extends BaseDialog.Builder<Builder> {

        private String hint;
        //输入框描述，位于输入框左侧
        private String inputDesc;
        //是否显示输入框左侧的描述，默认不显示
        private boolean descVisibility;
        //输入框初始内容
        private String content;

        public Builder(Context context) {
            super(context, R.layout.dialog_single_input);
            descVisibility = false;
        }

        public Builder hint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder inputDesc(String inputDesc) {
            this.inputDesc = inputDesc;
            return this;
        }

        public Builder descVisibility(boolean descVisibility) {
            this.descVisibility = descVisibility;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        @Override
        public SingleInputDialog build() {
            return new SingleInputDialog(this);
        }
    }
}
