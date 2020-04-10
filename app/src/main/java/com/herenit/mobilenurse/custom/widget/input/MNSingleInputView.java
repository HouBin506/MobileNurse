package com.herenit.mobilenurse.custom.widget.input;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.MNUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.enums.IndicatorStatus;


/**
 * author: HouBin
 * date: 2019/4/18 9:45
 * desc: 移动护理特有的单输入框，
 */
public class MNSingleInputView extends MNInputView {
    private TextView mTv_prefix;//前缀
    private TextView mTv_suffix;//后缀
    private EditText mEt_input;//输入框

    private MNSingleInputView(Builder builder) {
        super(builder.context);
        initView(builder);
    }

    private void initView(Builder builder) {
        View view = LayoutInflater.from(mContext).inflate(builder.layoutId, this);
        mTv_prefix = view.findViewById(R.id.tv_mnSingleInput_prefix);
        mTv_suffix = view.findViewById(R.id.tv_mnSingleInput_suffix);
        mEt_input = view.findViewById(R.id.et_mnSingleInput_input);
        if (!TextUtils.isEmpty(builder.prefix)) {
            mTv_prefix.setVisibility(VISIBLE);
            mTv_prefix.setText(builder.prefix);
        } else {
            mTv_prefix.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(builder.suffix)) {
            mTv_suffix.setVisibility(VISIBLE);
            mTv_suffix.setText(builder.suffix);
        } else {
            mTv_suffix.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(builder.content)) {
            mEt_input.setText(builder.content);
        }
        if (!TextUtils.isEmpty(builder.inputType) && CommonConstant.DATA_TYPE_NAME_NUMBER.equals(builder.inputType)) {
            //输入数字类型数据
            mEt_input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            mEt_input.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        if (builder.useIndicatorStatus) {//使用指示牌提醒
            setInputRangeAction(builder);
        } else {
            mEt_input.setBackgroundResource(R.drawable.shape_bg_edittext_black);
        }
        setInputListener(builder);
    }

    @Override
    public void setEnabled(boolean enabled) {
        mEt_input.setEnabled(enabled);
    }

    public void setContent(String content) {
        if (mEt_input == null)
            return;
        if (TextUtils.isEmpty(content))
            mEt_input.setText("");
        else
            mEt_input.setText(content);
    }

    /**
     * 设置输入框监听
     *
     * @param builder
     */
    private void setInputListener(Builder builder) {
        mEt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if (builder.useIndicatorStatus) {
                    IndicatorStatus status = setInputRangeAction(builder);
                    builder.onTextChangedListener.onTextChanged(status, content);
                } else {
                    builder.onTextChangedListener.onTextChanged(null, content);
                }
            }
        });
    }

    /**
     * 根据正常值范围、可输入值的范围，对输入框背景、字体做出相应的提示
     *
     * @param builder
     */
    private IndicatorStatus setInputRangeAction(Builder builder) {
        IndicatorStatus status;
        String content = mEt_input.getText().toString();
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(content.trim())) {//输入框为空
            status = IndicatorStatus.NORMAL;
            setInputStatus(mEt_input, IndicatorStatus.NORMAL);
            return status;//空的没必要去做操作
        }
        String inputType = builder.inputType;
        if (TextUtils.isEmpty(inputType) || !inputType.equals(CommonConstant.DATA_TYPE_NAME_NUMBER)) {//不是数字类型的输入框，不需要判断，均为正常
            status = IndicatorStatus.NORMAL;
            setInputStatus(mEt_input, IndicatorStatus.NORMAL);
            return status;//
        }
        //将输入框中的数据变成double
        Double value;
        try {
            value = Double.valueOf(content);
        } catch (Exception e) {//字符串解析为double时出错
            status = IndicatorStatus.ERROR;
            setInputStatus(mEt_input, IndicatorStatus.ERROR);
            return status;
        }
        if (value == null) {//非空字符串转化为double为空，则说明输入有误
            status = IndicatorStatus.ERROR;
            setInputStatus(mEt_input, IndicatorStatus.ERROR);
            return status;
        }
        status = MNUtils.getValueIndicator(value, builder.inputValueLimitUpper, builder.inputValueLimitLower,
                builder.normalValueLimitUpper, builder.normalValueLimitLower);
        setInputStatus(mEt_input, status);
        return status;
    }


    public static class Builder {
        private Context context;
        private String prefix;//前缀
        private String suffix;//后缀
        private String content;//输入框内容
        private String inputType;//输入类型，Number、Character等
        //正常值范围上限
        private Double normalValueLimitUpper;
        //正常值范围下限
        private Double normalValueLimitLower;
        //可输入值的上限
        private Double inputValueLimitUpper;
        //可输入值的下限
        private Double inputValueLimitLower;
        @LayoutRes
        private int layoutId;
        //是否使用执行牌或者指示灯相关的标记，就是说当输入框的值处于“正常”、“警告”、“错误”
        //时候，控件是否做出相应的提示
        private boolean useIndicatorStatus;
        //输入框监听
        private OnTextChangedListener onTextChangedListener;

        public Builder(Context context) {
            this.context = context;
            layoutId = R.layout.mn_single_input;
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

        public Builder inputType(String inputType) {
            this.inputType = inputType;
            return this;
        }

        public Builder normalValueLimitUpper(Double normalValueLimitUpper) {
            this.normalValueLimitUpper = normalValueLimitUpper;
            return this;
        }

        public Builder normalValueLimitLower(Double normalValueLimitLower) {
            this.normalValueLimitLower = normalValueLimitLower;
            return this;
        }

        public Builder inputValueLimitUpper(Double inputValueLimitUpper) {
            this.inputValueLimitUpper = inputValueLimitUpper;
            return this;
        }

        public Builder inputValueLimitLower(Double inputValueLimitLower) {
            this.inputValueLimitLower = inputValueLimitLower;
            return this;
        }

        public Builder useIndicatorStatus(boolean useIndicatorStatus) {
            this.useIndicatorStatus = useIndicatorStatus;
            return this;
        }

        public Builder onTextChangedListener(OnTextChangedListener onTextChangedListener) {
            this.onTextChangedListener = onTextChangedListener;
            return this;
        }

        public MNSingleInputView build() {
            return new MNSingleInputView(this);
        }
    }
}
