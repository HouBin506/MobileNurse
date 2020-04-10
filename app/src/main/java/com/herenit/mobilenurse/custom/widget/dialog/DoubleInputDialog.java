package com.herenit.mobilenurse.custom.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/1/29 11:27
 * desc: 单输入框的弹窗
 */
public class DoubleInputDialog extends BaseDialog {

    @BindView(R2.id.tv_doubleInputDialog_inputDesc1)
    TextView mTv_inputDesc1;
    @BindView(R2.id.et_doubleInputDialog_input1)
    EditText mEt_input1;
    @BindView(R2.id.tv_doubleInputDialog_inputDesc2)
    TextView mTv_inputDesc2;
    @BindView(R2.id.et_doubleInputDialog_input2)
    EditText mEt_input2;
    @BindView(R2.id.tv_doubleInputDialog_message)
    TextView mTv_message;

    private String mInputDesc1;
    private String mHint1;
    private boolean mDescVisibility1;
    private String mContent1;
    private String mInputDesc2;
    private String mHint2;
    private boolean mDescVisibility2;
    private String mContent2;
    private String mMessage;
    private boolean mMessageVisibility;

    DoubleInputDialog(Builder builder) {
        super(builder);
    }


    @Override
    protected <T extends BaseDialog.Builder> void buildDialog(T builder) {
        Builder sBuilder = (Builder) builder;
        mHint1 = sBuilder.hint1;
        mInputDesc1 = sBuilder.inputDesc1;
        mDescVisibility1 = sBuilder.descVisibility1;
        mContent1 = sBuilder.content1;
        mHint2 = sBuilder.hint2;
        mInputDesc2 = sBuilder.inputDesc2;
        mDescVisibility2 = sBuilder.descVisibility2;
        mContent2 = sBuilder.content2;
        mMessage = sBuilder.message;
        mMessageVisibility = sBuilder.messageVisibility;
    }

    @Override
    protected void setView(View layout) {
        if (!TextUtils.isEmpty(mHint1))
            mEt_input1.setHint(mHint1);
        if (!TextUtils.isEmpty(mInputDesc1))
            mTv_inputDesc1.setText(mInputDesc1);
        if (mDescVisibility1)
            mTv_inputDesc1.setVisibility(View.VISIBLE);
        else
            mTv_inputDesc1.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mContent1))
            mEt_input1.setText(mContent1);

        if (!TextUtils.isEmpty(mHint2))
            mEt_input2.setHint(mHint2);
        if (!TextUtils.isEmpty(mInputDesc2))
            mTv_inputDesc2.setText(mInputDesc2);
        if (mDescVisibility2)
            mTv_inputDesc2.setVisibility(View.VISIBLE);
        else
            mTv_inputDesc2.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mContent2))
            mEt_input2.setText(mContent2);
        if (!TextUtils.isEmpty(mMessage))
            mTv_message.setText(mMessage);
        if (mMessageVisibility)
            mTv_message.setVisibility(View.VISIBLE);
        else
            mTv_message.setVisibility(View.GONE);
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
                String content1 = mEt_input1.getText().toString();
                String content2 = mEt_input2.getText().toString();
                listener.onPositive(content1, content2);
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

        private String hint1;
        //输入框描述，位于输入框左侧
        private String inputDesc1;
        //是否显示输入框左侧的描述，默认不显示
        private boolean descVisibility1;
        //输入框初始内容
        private String content1;
        private String hint2;
        //输入框描述，位于输入框左侧
        private String inputDesc2;
        //是否显示输入框左侧的描述，默认不显示
        private boolean descVisibility2;
        //输入框初始内容
        private String content2;
        //弹窗描述
        private String message;
        //弹窗是否可见
        private boolean messageVisibility;

        public Builder(Context context) {
            super(context, R.layout.dialog_double_input);
            descVisibility1 = false;
            descVisibility2 = false;
            messageVisibility = false;
        }

        public Builder hint1(String hint) {
            this.hint1 = hint;
            return this;
        }


        public Builder inputDesc1(String inputDesc) {
            this.inputDesc1 = inputDesc;
            return this;
        }

        public Builder descVisibility1(boolean descVisibility) {
            this.descVisibility1 = descVisibility;
            return this;
        }

        public Builder content1(String content) {
            this.content1 = content;
            return this;
        }

        public Builder hint2(String hint) {
            this.hint2 = hint;
            return this;
        }

        public Builder inputDesc2(String inputDesc) {
            this.inputDesc2 = inputDesc;
            return this;
        }

        public Builder descVisibility2(boolean descVisibility) {
            this.descVisibility2 = descVisibility;
            return this;
        }

        public Builder content2(String content) {
            this.content2 = content;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder messageVisibility(boolean messageVisibility) {
            this.messageVisibility = messageVisibility;
            return this;
        }


        @Override
        public DoubleInputDialog build() {
            return new DoubleInputDialog(this);
        }
    }
}
