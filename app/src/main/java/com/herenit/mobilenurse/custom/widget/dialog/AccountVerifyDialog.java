package com.herenit.mobilenurse.custom.widget.dialog;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/1/29 11:27
 * desc: 账号核对弹窗
 */
public class AccountVerifyDialog extends BaseDialog {

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

    private String mContent1;
    private String mContent2;
    private String mMessage;
    private boolean mMessageVisibility;

    AccountVerifyDialog(Builder builder) {
        super(builder);
    }


    @Override
    protected <T extends BaseDialog.Builder> void buildDialog(T builder) {
        Builder sBuilder = (Builder) builder;
        mContent1 = sBuilder.content1;
        mContent2 = sBuilder.content2;
        mMessage = sBuilder.message;
        mMessageVisibility = sBuilder.messageVisibility;
    }

    /**
     * 设置核对账号密码
     *
     * @param loginName
     * @param password
     */
    public void setVerifyText(String loginName, String password) {
        if (!TextUtils.isEmpty(loginName))
            mEt_input1.setText(loginName);
        else
            mEt_input1.setText("");
        if (!TextUtils.isEmpty(password))
            mEt_input2.setText(password);
        else
            mEt_input2.setText("");
    }

    @Override
    protected void setView(View layout) {
        mTv_inputDesc1.setText(ArmsUtils.getString(mContext, R.string.common_userId));
        mTv_inputDesc1.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mContent1))
            mEt_input1.setText(mContent1);

        mTv_inputDesc2.setText(ArmsUtils.getString(mContext, R.string.common_password));
        mTv_inputDesc2.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mContent2))
            mEt_input2.setText(mContent2);
        mEt_input2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        if (!TextUtils.isEmpty(mMessage))
            mTv_message.setText(mMessage);
        if (mMessageVisibility)
            mTv_message.setVisibility(View.VISIBLE);
        else
            mTv_message.setVisibility(View.GONE);
        //点击外部不允许弹窗消失
        mDialog.setCanceledOnTouchOutside(false);
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

    @Override
    public void dismiss() {
        mEt_input1.setText("");
        mEt_input2.setText("");
        super.dismiss();
    }

    public static final class Builder extends BaseDialog.Builder<Builder> {

        //输入框初始内容
        private String content1;
        //输入框初始内容
        private String content2;
        //弹窗描述
        private String message;
        //描述是否可见
        private boolean messageVisibility;

        public Builder(Context context) {
            super(context, R.layout.dialog_double_input);
            messageVisibility = false;
        }


        public Builder content1(String content) {
            this.content1 = content;
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
        public AccountVerifyDialog build() {
            return new AccountVerifyDialog(this);
        }
    }
}
