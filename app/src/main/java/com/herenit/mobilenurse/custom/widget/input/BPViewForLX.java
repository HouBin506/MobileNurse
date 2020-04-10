package com.herenit.mobilenurse.custom.widget.input;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.MNUtils;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.enums.IndicatorStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/10/13 12:04
 * desc:临夏州人民医院 血压录入控件
 */
public class BPViewForLX extends MNInputView {

    //当前展示的是哪一组血压，默认是第一组
    private int mCurrentSubIndex = 0;
    private ListPopupWindow mItemListView;
    private BPItemAdapter bpItemAdapter;
    private List<LinXiaVitalBPValue> mBpList = new ArrayList<>();
    private TextView mTv_itemName;//前缀
    private EditText mEt_input1;//输入框
    private EditText mEt_input2;//输入框
    private TextView mTv_suffix;//后缀

    private String mContent;

    public BPViewForLX(Builder builder) {
        super(builder.context);
        initView(builder);
        mCurrentSubIndex = builder.currentSubIndex;
        mContent = builder.content;
        initData();
    }

    /**
     * 初始化血压显示的数据
     */
    private void initData() {
        List<LinXiaVitalBPValue> bpValues = getBpListByContent(mContent);
        mBpList.clear();
        if (bpValues != null)
            mBpList.addAll(bpValues);
        mBpList.add(new LinXiaVitalBPValue());//添加一个空的血压对象
        bpItemAdapter.notifyDataSetChanged();
        setCurrentViewShow();
    }

    /**
     * 将数据库的原始血压数据，拆分成为要展示的数据列表
     *
     * @param content
     * @return
     */
    private List<LinXiaVitalBPValue> getBpListByContent(String content) {
        if (TextUtils.isEmpty(content))
            return null;
        String[] bpArray = content.trim().split(" ");
        if (bpArray == null || bpArray.length == 0)
            return null;
        List<LinXiaVitalBPValue> list = new ArrayList<>();
        for (String bpValue : bpArray) {
            if (TextUtils.isEmpty(bpValue))
                continue;
            bpValue = bpValue.trim();
            if (TextUtils.isEmpty(bpValue))
                continue;
            String[] valueArray = bpValue.split("/");
            if (valueArray == null || valueArray.length == 0 || valueArray.length > 2)//不符合规则，不要
                continue;
            LinXiaVitalBPValue value;
            if (valueArray.length == 1)
                value = new LinXiaVitalBPValue(valueArray[0], "");
            else
                value = new LinXiaVitalBPValue(valueArray[0], valueArray[1]);
            list.add(value);
        }
        return list;
    }


    private void initView(Builder builder) {
        View view = LayoutInflater.from(mContext).inflate(builder.layoutId, this);
        mTv_itemName = view.findViewById(R.id.tv_bpViewLx_itemName);
        mTv_itemName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemListView != null)
                    mItemListView.show();
            }
        });
        mTv_suffix = view.findViewById(R.id.tv_bpViewLx_suffix);
        mEt_input1 = view.findViewById(R.id.et_bpViewLx_inputLeft);
        if (!TextUtils.isEmpty(builder.suffix)) {
            mTv_suffix.setVisibility(VISIBLE);
            mTv_suffix.setText(builder.suffix);
        } else {
            mTv_suffix.setVisibility(GONE);
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
        mEt_input2 = view.findViewById(R.id.et_bpViewLx_inputRight);
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
        initItemList();
    }

    private void initItemList() {
        bpItemAdapter = new BPItemAdapter(mContext, mBpList);
        mItemListView = ViewUtils.createListPopupWindow(mContext, bpItemAdapter, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                mTv_itemName, 0, 0);
        mItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mItemListView.dismiss();
                mCurrentSubIndex = position;
                setCurrentViewShow();
            }
        });
    }

    private void setCurrentViewShow() {
        mTv_itemName.setText("血压" + (mCurrentSubIndex + 1));
        if (mBpList.size() - 1 < mCurrentSubIndex) {
            mEt_input1.setText("");
            mEt_input2.setText("");
        } else {
            LinXiaVitalBPValue bpValue = mBpList.get(mCurrentSubIndex);
            mEt_input1.setText(bpValue.getLeftValue() == null ? "" : bpValue.getLeftValue());
            mEt_input2.setText(bpValue.getRightValue() == null ? "" : bpValue.getRightValue());
        }
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
                LinXiaVitalBPValue bpValue = mBpList.get(mCurrentSubIndex);
                if (builder.useIndicatorStatus1) {
                    IndicatorStatus status = setInputRangeAction1(builder);
                    if (status != null && status == IndicatorStatus.ERROR) {//输入的值是错误的，则录入失败
                        bpValue.setLeftValue("");
                    } else {
                        bpValue.setLeftValue(content);
                    }
                } else {
                    bpValue.setLeftValue(content);
                }
                setBpValue();
                builder.onTextChangedListener.onTextChanged(getBPValueByBPValueList(mBpList), mCurrentSubIndex);
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
                LinXiaVitalBPValue bpValue = mBpList.get(mCurrentSubIndex);
                if (builder.useIndicatorStatus2) {
                    IndicatorStatus status = setInputRangeAction2(builder);
                    if (status != null && status == IndicatorStatus.ERROR) {//输入的值是错误的，则录入失败
                        bpValue.setRightValue("");
                    } else {
                        bpValue.setRightValue(content);
                    }
                } else {
                    bpValue.setRightValue(content);
                }
                setBpValue();
                builder.onTextChangedListener.onTextChanged(getBPValueByBPValueList(mBpList), mCurrentSubIndex);
            }
        });
    }

    /**
     * 将List<LinXiaVitalBPValue> 检索成为要保存的血压值
     *
     * @param vitalBPValueList
     * @return
     */
    private String getBPValueByBPValueList(List<LinXiaVitalBPValue> vitalBPValueList) {
        if (vitalBPValueList == null || vitalBPValueList.size() == 0)
            return "";
        StringBuilder sbBP = new StringBuilder();
        for (int x = 0; x < vitalBPValueList.size(); x++) {
            LinXiaVitalBPValue bpValue = vitalBPValueList.get(x);
            if (bpValue.isEmpty())
                continue;
            String bpStr = "";
            if (TextUtils.isEmpty(bpValue.leftValue))
                bpStr = bpValue.rightValue;
            else if (TextUtils.isEmpty(bpValue.rightValue))
                bpStr = bpValue.getLeftValue();
            else
                bpStr = bpValue.leftValue + "/" + bpValue.rightValue;
            if (TextUtils.isEmpty(sbBP.toString()))
                sbBP.append(bpStr);
            else
                sbBP.append(" " + bpStr);
        }
        return sbBP.toString();
    }

    /**
     * 输入框内容改变后设置血压数据
     */
    private void setBpValue() {
        Iterator<LinXiaVitalBPValue> iterator = mBpList.iterator();
        while (iterator.hasNext()) {
            LinXiaVitalBPValue bpValue = iterator.next();
            if (TextUtils.isEmpty(bpValue.getLeftValue()) && TextUtils.isEmpty(bpValue.getRightValue()))//某条血压被清空，则删除
                iterator.remove();
        }
        mBpList.add(new LinXiaVitalBPValue());
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


        private int currentSubIndex;
        //血压数据
        private String content;
        private String suffix;//后缀
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
        private OnLXBPTextChangedListener onTextChangedListener;

        public Builder(Context context) {
            this.context = context;
            layoutId = R.layout.bp_view_lx;
            this.currentSubIndex = 0;
        }

        public Builder currentSubIndex(int currentSubIndex) {
            this.currentSubIndex = currentSubIndex;
            return this;
        }


        public Builder content(String content) {
            this.content = content;
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


        public Builder suffix(String suffix) {
            this.suffix = suffix;
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

        public Builder onTextChangedListener(OnLXBPTextChangedListener onTextChangedListener) {
            this.onTextChangedListener = onTextChangedListener;
            return this;
        }

        public BPViewForLX build() {
            return new BPViewForLX(this);
        }
    }


    private class BPItemAdapter extends CommonAdapter<LinXiaVitalBPValue> {


        public BPItemAdapter(Context context, List<LinXiaVitalBPValue> datas) {
            super(context, datas, R.layout.item_common_text);
        }

        @Override
        protected void convert(ViewHolder holder, LinXiaVitalBPValue item, int position) {
            holder.setText(R.id.tv_item_text_text, "血压" + (position + 1));
        }
    }

    public static class LinXiaVitalBPValue implements Serializable {
        public String leftValue;
        public String rightValue;

        public LinXiaVitalBPValue() {
            super();
        }

        public LinXiaVitalBPValue(String leftValue, String rightValue) {
            super();
            this.leftValue = leftValue;
            this.rightValue = rightValue;
        }

        public String getLeftValue() {
            return leftValue;
        }

        public void setLeftValue(String leftValue) {
            this.leftValue = leftValue;
        }

        public String getRightValue() {
            return rightValue;
        }

        public void setRightValue(String rightValue) {
            this.rightValue = rightValue;
        }

        public boolean isEmpty() {
            return TextUtils.isEmpty(leftValue) && TextUtils.isEmpty(rightValue);
        }
    }

    public interface OnLXBPTextChangedListener {
        /**
         * 输入框监听
         *
         * @param content 输入框的内容
         */
        void onTextChanged(String content, int currentSunIndex);
    }
}
