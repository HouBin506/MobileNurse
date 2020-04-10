package com.herenit.mobilenurse.custom.widget.input;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.MNUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.enums.IndicatorStatus;

/**
 * author: HouBin
 * date: 2019/4/18 14:08
 * desc:移动护理特有双输入框
 */
public class MNDoubleInputView extends MNInputView {
    private TextView mTv_prefix;//前缀
    private EditText mEt_input1;//输入框
    private EditText mEt_input2;//输入框
    private TextView mTv_suffix1;//后缀
    private TextView mTv_suffix2;//后缀

    public MNDoubleInputView(Builder builder) {
        super(builder.context);
        initView(builder);
    }

    private void initView(Builder builder) {
        View view = LayoutInflater.from(mContext).inflate(builder.layoutId, this);
        mTv_prefix = view.findViewById(R.id.tv_mnDoubleInput_prefix);
        if (!TextUtils.isEmpty(builder.prefix)) {
            mTv_prefix.setVisibility(VISIBLE);
            mTv_prefix.setText(builder.prefix);
        } else {
            mTv_prefix.setVisibility(GONE);
        }
        mTv_suffix1 = view.findViewById(R.id.tv_mnDoubleInput_suffix1);
        mEt_input1 = view.findViewById(R.id.et_mnDoubleInput_input1);
        if (!TextUtils.isEmpty(builder.suffix1)) {
            mTv_suffix1.setVisibility(VISIBLE);
            mTv_suffix1.setText(builder.suffix1);
        } else {
            mTv_suffix1.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(builder.content1)) {
            mEt_input1.setText(builder.content1);
        }
        if (!TextUtils.isEmpty(builder.inputType1) && CommonConstant.DATA_TYPE_NAME_NUMBER.equals(builder.inputType1)) {
            //输入数字类型数据
            mEt_input1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            mEt_input1.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        if (builder.useIndicatorStatus1) {//使用指示牌提醒
            setInputRangeAction1(builder);
        } else {
            mEt_input1.setBackgroundResource(R.drawable.shape_bg_edittext_black);
        }

        mTv_suffix2 = view.findViewById(R.id.tv_mnDoubleInput_suffix2);
        mEt_input2 = view.findViewById(R.id.et_mnDoubleInput_input2);
        if (!TextUtils.isEmpty(builder.suffix2)) {
            mTv_suffix2.setVisibility(VISIBLE);
            mTv_suffix2.setText(builder.suffix2);
        } else {
            mTv_suffix2.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(builder.content2)) {
            mEt_input2.setText(builder.content2);
        }
        if (!TextUtils.isEmpty(builder.inputType2) && CommonConstant.DATA_TYPE_NAME_NUMBER.equals(builder.inputType2)) {
            //输入数字类型数据
            mEt_input2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            mEt_input2.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        if (builder.useIndicatorStatus2) {//使用指示牌提醒
            setInputRangeAction2(builder);
        } else {
            mEt_input2.setBackgroundResource(R.drawable.shape_bg_edittext_black);
        }
        setInputListener(builder);
    }

    /**
     * 设置输入框监听
     *
     * @param builder
     */
    private void setInputListener(Builder builder) {
        mEt_input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if (builder.useIndicatorStatus1) {
                    IndicatorStatus status = setInputRangeAction1(builder);
                    builder.onTextChangedListener1.onTextChanged(status, content);
                } else {
                    builder.onTextChangedListener1.onTextChanged(null, content);
                }
            }
        });
        mEt_input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if (builder.useIndicatorStatus2) {
                    IndicatorStatus status = setInputRangeAction2(builder);
                    builder.onTextChangedListener2.onTextChanged(status, content);
                } else {
                    builder.onTextChangedListener2.onTextChanged(null, content);
                }
            }
        });
    }

    /**
     * 根据正常值范围、可输入值的范围，对输入框背景、字体做出相应的提示
     *
     * @param builder
     */
    private IndicatorStatus setInputRangeAction1(Builder builder) {
        IndicatorStatus status;
        String content = mEt_input1.getText().toString();
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(content.trim())) {//输入框为空
            status = IndicatorStatus.NORMAL;
            setInputStatus(mEt_input1, IndicatorStatus.NORMAL);
            return status;//空的没必要去做操作
        }
        String inputType = builder.inputType1;
        if (TextUtils.isEmpty(inputType) || !inputType.equals(CommonConstant.DATA_TYPE_NAME_NUMBER)) {//不是数字类型的输入框，不需要判断，均为正常
            status = IndicatorStatus.NORMAL;
            setInputStatus(mEt_input1, IndicatorStatus.NORMAL);
            return status;//
        }
        //将输入框中的数据变成double
        Double value;
        try {
            value = Double.valueOf(content);
        } catch (Exception e) {//字符串解析为double时出错
            status = IndicatorStatus.ERROR;
            setInputStatus(mEt_input1, IndicatorStatus.ERROR);
            return status;
        }
        if (value == null) {//非空字符串转化为double为空，则说明输入有误
            status = IndicatorStatus.ERROR;
            setInputStatus(mEt_input1, IndicatorStatus.ERROR);
            return status;
        }
        status = MNUtils.getValueIndicator(value, builder.inputValueLimitUpper1, builder.inputValueLimitLower1,
                builder.normalValueLimitUpper1, builder.normalValueLimitLower1);
        setInputStatus(mEt_input1, status);
        return status;
    }

    /**
     * 根据正常值范围、可输入值的范围，对输入框背景、字体做出相应的提示
     *
     * @param builder
     */
    private IndicatorStatus setInputRangeAction2(Builder builder) {
        IndicatorStatus status;
        String content = mEt_input2.getText().toString();
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(content.trim())) {//输入框为空
            status = IndicatorStatus.NORMAL;
            setInputStatus(mEt_input2, IndicatorStatus.NORMAL);
            return status;//空的没必要去做操作
        }
        String inputType = builder.inputType2;
        if (TextUtils.isEmpty(inputType) || !inputType.equals(CommonConstant.DATA_TYPE_NAME_NUMBER)) {//不是数字类型的输入框，不需要判断，均为正常
            status = IndicatorStatus.NORMAL;
            setInputStatus(mEt_input2, IndicatorStatus.NORMAL);
            return status;//
        }
        //将输入框中的数据变成double
        Double value;
        try {
            value = Double.valueOf(content);
        } catch (Exception e) {//字符串解析为double时出错
            status = IndicatorStatus.ERROR;
            setInputStatus(mEt_input2, IndicatorStatus.ERROR);
            return status;
        }
        if (value == null) {//非空字符串转化为double为空，则说明输入有误
            status = IndicatorStatus.ERROR;
            setInputStatus(mEt_input2, IndicatorStatus.ERROR);
            return status;
        }
        status = MNUtils.getValueIndicator(value, builder.inputValueLimitUpper2, builder.inputValueLimitLower2,
                builder.normalValueLimitUpper2, builder.normalValueLimitLower2);
        setInputStatus(mEt_input2, status);
        return status;
    }


    public static class Builder {
        private Context context;
        private String prefix;//前缀
        private String suffix1;//后缀
        private String suffix2;//后缀
        private String content1;//输入框内容
        private String content2;//输入框内容
        private String inputType1;//输入类型，Number、Character等
        private String inputType2;//输入类型，Number、Character等
        //正常值范围上限
        private Double normalValueLimitUpper1;
        //正常值范围下限
        private Double normalValueLimitLower1;
        //可输入值的上限
        private Double inputValueLimitUpper1;
        //可输入值的下限
        private Double inputValueLimitLower1;
        //正常值范围上限
        private Double normalValueLimitUpper2;
        //正常值范围下限
        private Double normalValueLimitLower2;
        //可输入值的上限
        private Double inputValueLimitUpper2;
        //可输入值的下限
        private Double inputValueLimitLower2;
        @LayoutRes
        private int layoutId;
        //是否使用执行牌或者指示灯相关的标记，就是说当输入框的值处于“正常”、“警告”、“错误”
        //时候，控件是否做出相应的提示
        private boolean useIndicatorStatus1;
        //是否使用执行牌或者指示灯相关的标记，就是说当输入框的值处于“正常”、“警告”、“错误”
        //时候，控件是否做出相应的提示
        private boolean useIndicatorStatus2;
        //输入框监听
        private MNInputView.OnTextChangedListener onTextChangedListener1;
        //输入框监听
        private MNInputView.OnTextChangedListener onTextChangedListener2;

        public Builder(Context context) {
            this.context = context;
            layoutId = R.layout.mn_double_input;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder content1(String content1) {
            this.content1 = content1;
            return this;
        }

        public Builder content2(String content2) {
            this.content2 = content2;
            return this;
        }

        public Builder suffix1(String suffix1) {
            this.suffix1 = suffix1;
            return this;
        }

        public Builder inputType1(String inputType1) {
            this.inputType1 = inputType1;
            return this;
        }

        public Builder normalValueLimitUpper1(Double normalValueLimitUpper1) {
            this.normalValueLimitUpper1 = normalValueLimitUpper1;
            return this;
        }

        public Builder normalValueLimitLower1(Double normalValueLimitLower1) {
            this.normalValueLimitLower1 = normalValueLimitLower1;
            return this;
        }

        public Builder inputValueLimitUpper1(Double inputValueLimitUpper1) {
            this.inputValueLimitUpper1 = inputValueLimitUpper1;
            return this;
        }

        public Builder inputValueLimitLower1(Double inputValueLimitLower1) {
            this.inputValueLimitLower1 = inputValueLimitLower1;
            return this;
        }

        public Builder useIndicatorStatus1(boolean useIndicatorStatus1) {
            this.useIndicatorStatus1 = useIndicatorStatus1;
            return this;
        }

        public Builder onTextChangedListener1(MNInputView.OnTextChangedListener onTextChangedListener1) {
            this.onTextChangedListener1 = onTextChangedListener1;
            return this;
        }

        public Builder suffix2(String suffix2) {
            this.suffix2 = suffix2;
            return this;
        }

        public Builder inputType2(String inputType2) {
            this.inputType2 = inputType2;
            return this;
        }

        public Builder normalValueLimitUpper2(Double normalValueLimitUpper2) {
            this.normalValueLimitUpper2 = normalValueLimitUpper2;
            return this;
        }

        public Builder normalValueLimitLower2(Double normalValueLimitLower2) {
            this.normalValueLimitLower2 = normalValueLimitLower2;
            return this;
        }

        public Builder inputValueLimitUpper2(Double inputValueLimitUpper2) {
            this.inputValueLimitUpper2 = inputValueLimitUpper2;
            return this;
        }

        public Builder inputValueLimitLower2(Double inputValueLimitLower2) {
            this.inputValueLimitLower2 = inputValueLimitLower2;
            return this;
        }

        public Builder useIndicatorStatus2(boolean useIndicatorStatus2) {
            this.useIndicatorStatus2 = useIndicatorStatus2;
            return this;
        }

        public Builder onTextChangedListener2(MNInputView.OnTextChangedListener onTextChangedListener2) {
            this.onTextChangedListener2 = onTextChangedListener2;
            return this;
        }

        public MNDoubleInputView build() {
            return new MNDoubleInputView(this);
        }
    }
}
