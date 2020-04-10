package com.herenit.mobilenurse.custom.widget.input;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.enums.IndicatorStatus;

/**
 * author: HouBin
 * date: 2019/4/18 14:08
 * desc:
 */
public class MNInputView extends LinearLayout {
    protected Context mContext;

    public MNInputView(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * 设置输入框的状态，正常、警告、错误
     *
     * @param editText
     * @param status
     */
    protected void setInputStatus(EditText editText, IndicatorStatus status) {
        if (status == null)
            return;
        switch (status) {
            case NORMAL:
                editText.setTextColor(ArmsUtils.getColor(mContext, R.color.black));
                editText.setBackgroundResource(R.drawable.shape_bg_edittext_black);
                break;
            case WARNING:
                editText.setTextColor(ArmsUtils.getColor(mContext, R.color.black));
                editText.setBackgroundResource(R.drawable.shape_bg_yellow_solid_black_stroke);
                break;
            case ERROR:
                editText.setTextColor(ArmsUtils.getColor(mContext, R.color.red));
                editText.setBackgroundResource(R.drawable.shape_bg_gray_solid_black_stroke);
                break;
        }
    }

    public interface OnTextChangedListener {
        /**
         * 输入框监听
         *
         * @param status  当前输入框中的内容的状态（正常值、危险值、错误值）
         * @param content 输入框的内容
         */
        void onTextChanged(@Nullable IndicatorStatus status, String content);
    }
}
