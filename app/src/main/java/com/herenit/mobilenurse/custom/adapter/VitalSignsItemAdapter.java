package com.herenit.mobilenurse.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.herenit.arms.base.adapter.rv.CommonAdapter;
import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.MNUtils;
import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.submit.VitalSignsHistoryQuery;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsViewItem;
import com.herenit.mobilenurse.criteria.enums.IndicatorStatus;
import com.herenit.mobilenurse.custom.listener.SoftKeyBoardListener;
import com.herenit.mobilenurse.custom.widget.input.BPViewForLX;
import com.herenit.mobilenurse.custom.widget.input.MNDoubleInputView;
import com.herenit.mobilenurse.custom.widget.input.MNInputView;
import com.herenit.mobilenurse.custom.widget.input.MNSingleInputView;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsChartActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * author: HouBin
 * date: 2019/4/17 10:09
 * desc: 体征项目列表Adapter
 */
public class VitalSignsItemAdapter extends CommonAdapter<VitalSignsViewItem> {

    private int INPUT_POSITION_SINGLE = 0;//唯一一个输入框
    private int INPUT_POSITION_LEFT = 1;//左边输入框
    private int INPUT_POSITION_RIGHT = 2;//右边输入框

    private int operationType;

    public VitalSignsItemAdapter(Context context, List<VitalSignsViewItem> datas) {
        super(context, R.layout.item_vital_signs_record, datas);
        operationType = CommonConstant.OPERATION_TYPE_PERSIST;
        setSoftKeyBoardListener();
    }

    public VitalSignsItemAdapter(Context context, List<VitalSignsViewItem> datas, int operationType) {
        super(context, R.layout.item_vital_signs_record, datas);
        this.operationType = operationType;
        setSoftKeyBoardListener();
    }

    private void setSoftKeyBoardListener() {
        SoftKeyBoardListener.setListener((Activity) mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //软件盘弹出
            }

            @Override
            public void keyBoardHide(int height) {
                //软键盘隐藏，认为某项体征的输入框输入完毕，刷新界面做出控件响应
                notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void convert(ViewHolder holder, VitalSignsViewItem item, int position) {
        LinearLayout ll_group = holder.getView(R.id.ll_item_vitalSignsRecord_group);//表示组
        LinearLayout ll_item = holder.getView(R.id.ll_item_vitalSignsRecord_item);//表示条目
        View view_divider = holder.getView(R.id.view_item_vitalSignsRecord_divider);//分割线
        TextView tv_groupName = holder.getView(R.id.tv_item_vitalSignsRecord_groupName);//组名，比如“血压”、“体温”
        CheckBox cb_itemName = holder.getView(R.id.cb_item_vitalSignsRecord_itemName);//体征项目名
        TextView tv_itemName = holder.getView(R.id.tv_item_vitalSignsRecord_itemName);//体征项目名
        LinearLayout ll_value = holder.getView(R.id.ll_item_vitalSignsRecord_value);//体征项的值，客户端录入的
        LinearLayout ll_specialValue = holder.getView(R.id.ll_item_vitalSignsRecord_specialValue);//例外值，客户端录入
        ImageView img_chart = holder.getView(R.id.img_item_vitalSignsRecord_chart);//趋势图标记，有历史记录且值为数字的，可以画趋势图
        LinearLayout ll_DateView = holder.getView(R.id.ll_item_vitalSignsRecord_itemDate);//时间
        LinearLayout ll_fixedTimePoint = holder.getView(R.id.ll_item_vitalSignsRecord_fixedTimePoint);//使用固定时间点时。显示
        TextView tv_timePoint = holder.getView(R.id.tv_item_vitalSignsRecord_timePoint);//该条体征录入的时间点，默认为当前时间
        TextView tv_fixed = holder.getView(R.id.tv_item_vitalSignsRecord_fixedTimePoint);//固定时间点，针对某些医院要求录入固定时间点
        ImageView img_fixedClean = holder.getView(R.id.img_item_vitalSignsRecord_fixedTimeClear);//删除固定时间点
        LinearLayout ll_memo = holder.getView(R.id.ll_item_vitalSignsRecord_memo);//备注，有的医院需要录入备注信息
        EditText et_memo = holder.getView(R.id.et_item_vitalSignsRecord_memo);//备注，有的医院需要录入备注信息
        LinearLayout ll_desc = holder.getView(R.id.ll_item_vitalSignsRecord_desc);//描述，显示该体征项的一些额外描述，频次、特殊说明等
        TextView tv_frequency = holder.getView(R.id.tv_item_vitalSignsRecord_frequency);//显示该项体征相关的频次描述
        TextView tv_desc = holder.getView(R.id.tv_item_vitalSignsRecord_desc);//显示该项体征相关特殊说明
        String itemType = item.getItemType();
        if (CommonConstant.ITEM_TYPE_GROUP.equals(itemType)) {//Group类型
            ll_item.setVisibility(View.GONE);
            ll_group.setVisibility(View.VISIBLE);
            view_divider.setVisibility(View.VISIBLE);
            tv_groupName.setText(item.getItemName());
        } else {
            ll_item.setVisibility(View.VISIBLE);
            ll_group.setVisibility(View.GONE);
            if (CommonConstant.ITEM_TYPE_GROUP_VALUE.equals(itemType)) {//GroupValue类型
                view_divider.setVisibility(View.VISIBLE);
            } else if (CommonConstant.ITEM_TYPE_VALUE.equals(itemType)) {//Value类型
                view_divider.setVisibility(View.GONE);
            }
            if (item.isUseValueView() || item.isUseSpecialValueView() || operationType == CommonConstant.OPERATION_TYPE_UPDATE) {//有值或者有例外值的，直接录入、提交
                cb_itemName.setVisibility(View.GONE);
                tv_itemName.setVisibility(View.VISIBLE);
                //设置体征项目名
                tv_itemName.setText(item.getItemName());
            } else {//对于不需要录入值或者例外值的，只提交时间，则需要选中才行
                cb_itemName.setVisibility(View.VISIBLE);
                tv_itemName.setVisibility(View.GONE);
                //设置体征项目名
                cb_itemName.setText(item.getItemName());
                cb_itemName.setOnCheckedChangeListener(null);//先清空之前的监听，为了解决listView滚动时再次调用CheckBox的监听器的onCheckedChanged方法，导致选中状态被改变
                cb_itemName.setChecked(item.isChecked());
                cb_itemName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        item.setChecked(isChecked);
                    }
                });
            }

            /**************************Value************************************/
            if (item.isUseValueView()) {//录入数值
                ll_value.setVisibility(View.VISIBLE);
                addValueView(ll_value, item);
            } else {//不使用数值录入
                ll_value.setVisibility(View.GONE);
            }
            /******************************例外值（特殊值）*****************************************/
            if (item.isUseSpecialValueView()) {//录入例外值（特殊值）
                ll_specialValue.setVisibility(View.VISIBLE);
                addSpecialValueView(ll_specialValue, item);
            } else {
                ll_specialValue.setVisibility(View.GONE);
            }
            /*******************************趋势图入口************************************************/
            if (item.isHasChart() && operationType != CommonConstant.OPERATION_TYPE_UPDATE) {//体征趋势图
                img_chart.setVisibility(View.VISIBLE);
                img_chart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//画趋势图
                        VitalSignsHistoryQuery.VitalItemID itemID = new VitalSignsHistoryQuery.VitalItemID();
                        itemID.setClassCode(item.getClassCode());
                        itemID.setItemCode(item.getItemCode());
                        itemID.setItemName(item.getItemName());
                        Intent intent = new Intent(mContext, VitalSignsChartActivity.class);
                        intent.putExtra(KeyConstant.NAME_EXTRA_VITAL_ITEM_ID, itemID);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                img_chart.setVisibility(View.GONE);
            }
            /*************************************录入时间点****************************************/
            if (TextUtils.isEmpty(item.getFixedTimePointList())) {//不使用固定时间点
                ll_fixedTimePoint.setVisibility(View.GONE);
            } else {//使用固定时间点
                ll_fixedTimePoint.setVisibility(View.VISIBLE);
                tv_fixed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> fixedTimeList = MNUtils.parseStringListByFormatString(item.getFixedTimePointList());
                        if (fixedTimeList == null || fixedTimeList.isEmpty())
                            return;
                        //弹出选择固定时间点的框
                        ListPopupWindow listPopupWindow = ViewUtils.createListPopupWindow(mContext, new CommonTextAdapter(mContext, fixedTimeList),
                                tv_fixed.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, tv_fixed, 0, 0);
                        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                listPopupWindow.dismiss();
                                String fixedTime = fixedTimeList.get(position);
                                item.setFixedTimePoint(fixedTime);
                                //给其他的设置固定时间点
                                setDefaultFixedTimePoint(fixedTime);
                                notifyDataSetChanged();
                            }
                        });
                        listPopupWindow.show();
                    }
                });
                img_fixedClean.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//清空固定时间点选择，不选择固定时间点，只使用实际日历控件时间
                        item.setFixedTimePoint("");
                        notifyDataSetChanged();
                    }
                });
            }
            boolean[] timeFormat;
            Long timePoint = item.getTimePoint();
            if (timePoint == null) {
                timePoint = TimeUtils.getCurrentDate().getTime();
                item.setTimePoint(timePoint);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timePoint);
            String timePointStr;
            if (!TextUtils.isEmpty(item.getFixedTimePoint())) {
                tv_fixed.setText(item.getFixedTimePoint());
                timePointStr = TimeUtils.getYYYYMMDDString(timePoint);
                timeFormat = new boolean[]{true, true, true, false, false, false};
            } else {
                tv_fixed.setText("");
                timePointStr = TimeUtils.getYYYYMMDDHHMMString(timePoint);
                timeFormat = new boolean[]{true, true, true, true, true, false};
            }
            if (item.getItemName().equals(CommonConstant.VITAL_SIGNS_ADMISSION)) {//临夏州“入院”体征项只读，包括时间不可编辑
                ll_DateView.setVisibility(View.GONE);
            } else {
                ll_DateView.setVisibility(View.VISIBLE);
            }
            tv_timePoint.setText(timePointStr);
            tv_timePoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//日历选择TimePoint
                    new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            item.setTimePoint(date.getTime());
                            notifyDataSetChanged();
                        }
                    }).isDialog(true)
                            .setDate(calendar)
                            .setType(timeFormat)
                            .build().show();
                }
            });
            /***********************************备注*******************************************/
            if (item.isUseMemoView()) {//使用录入备注
                ll_memo.setVisibility(View.VISIBLE);
                et_memo.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String memo = s.toString();
                        item.setMemo(memo);
                    }
                });
            } else {
                ll_memo.setVisibility(View.GONE);
            }
            /************************************频次、描述**************************************************/
            if (TextUtils.isEmpty(item.getFrequency()) && TextUtils.isEmpty(item.getItemDescription())) {
                ll_desc.setVisibility(View.GONE);
            } else {
                ll_desc.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(item.getFrequency())) {
                    tv_frequency.setVisibility(View.VISIBLE);
                    tv_frequency.setText(item.getFrequency());
                } else {
                    tv_frequency.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(item.getItemDescription())) {
                    tv_desc.setVisibility(View.VISIBLE);
                    tv_desc.setText(item.getItemDescription());
                } else {
                    tv_desc.setVisibility(View.GONE);
                }
            }
        }

    }

    /**
     * 设置默认固定时间点，判断，如果该条体征具备固定时间点，并且还未选择固定时间点，则给它设置固定时间点
     *
     * @param fixedTime
     */
    private void setDefaultFixedTimePoint(String fixedTime) {
        List<VitalSignsViewItem> dataList = getDatas();
        if (dataList == null)
            return;
        for (VitalSignsViewItem item : dataList) {
            if (TextUtils.isEmpty(item.getFixedTimePointList()))
                continue;
            if (TextUtils.isEmpty(item.getFixedTimePoint()))
                item.setFixedTimePoint(fixedTime);
        }
    }

    /**
     * 添加SpecialValue值的控件
     *
     * @param parent
     * @param item
     */
    private void addSpecialValueView(LinearLayout parent, VitalSignsViewItem item) {
        String viewType = item.getSpecialValueViewType();
        if (TextUtils.isEmpty(viewType))
            return;
        switch (viewType) {
            case CommonConstant.VIEW_TYPE_LIST_POPUP_WINDOW://下拉列表
                TextView specialView = new TextView(mContext);
                specialView.setBackgroundResource(R.drawable.ic_bg_drop_down_textview);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                specialView.setLayoutParams(layoutParams);
                specialView.setGravity(Gravity.CENTER);
                specialView.setTextColor(ArmsUtils.getColor(mContext, R.color.light_black));
                specialView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ArmsUtils.getDimens(mContext, R.dimen.text_content_level_3));
                parent.removeAllViews();
                parent.addView(specialView);
                if (!TextUtils.isEmpty(item.getSpecialValue()))
                    specialView.setText(item.getSpecialValue());
                else
                    specialView.setText("");
                parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> specialValueList = MNUtils.parseStringListByFormatString(item.getSpecialValueList());
                        if (specialValueList == null || specialValueList.isEmpty())
                            return;
                        ListPopupWindow listPopupWindow = ViewUtils.createListPopupWindow(mContext, new CommonTextAdapter(mContext, specialValueList),
                                parent.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, parent, 0, 0);
                        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                listPopupWindow.dismiss();
                                String value = specialValueList.get(position);
                                item.setSpecialValue(value);
                                if (item.isValueSpecialExclusive()) //值与例外值互斥
                                    item.setValue("");
                                notifyDataSetChanged();
                            }
                        });
                        listPopupWindow.show();
                    }
                });
                break;
            case CommonConstant.VIEW_TYPE_MN_SINGLE_INPUT_VIEW://输入框
                MNSingleInputView singleInputView = new MNSingleInputView.Builder(mContext)
                        .inputType(item.getValueDataType())
                        .onTextChangedListener(new MNInputView.OnTextChangedListener() {
                            @Override
                            public void onTextChanged(@Nullable IndicatorStatus status, String content) {
                                item.setSpecialValue(content);
                            }
                        }).content(item.getSpecialValue())
                        .useIndicatorStatus(false).build();
                singleInputView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                singleInputView.setGravity(Gravity.CENTER);
                parent.removeAllViews();
                parent.addView(singleInputView);
                break;
            default:
                return;
        }
    }

    /**
     * 添加Value值的控件
     *
     * @param parent
     * @param item
     */
    private void addValueView(LinearLayout parent, VitalSignsViewItem item) {
        String viewType = item.getValueViewType();
        if (TextUtils.isEmpty(viewType))
            return;
        switch (viewType) {
            case CommonConstant.VIEW_TYPE_MN_SINGLE_INPUT_VIEW://单输入框
                MNSingleInputView singleInputView = new MNSingleInputView.Builder(mContext)
                        .inputType(item.getValueDataType())
                        .onTextChangedListener(new MNInputView.OnTextChangedListener() {
                            @Override
                            public void onTextChanged(@Nullable IndicatorStatus status, String content) {
                                setItemInputValue(status, content, item, INPUT_POSITION_SINGLE);
                            }
                        }).content(item.getValue())
                        .inputValueLimitUpper(StringUtils.stringToDouble(item.getInputUpperLimit()))
                        .inputValueLimitLower(StringUtils.stringToDouble(item.getInputLowerLimit()))
                        .normalValueLimitUpper(StringUtils.stringToDouble(item.getUpperLimitValue()))
                        .normalValueLimitLower(StringUtils.stringToDouble(item.getLowerLimitValue()))
                        .suffix(item.getUnit())
                        .useIndicatorStatus(true).build();
                if (item.getItemName().equals(CommonConstant.VITAL_SIGNS_ADMISSION)) {
                    Sickbed currentSickbed = SickbedTemp.getInstance().getCurrentSickbed();
                    if (currentSickbed != null) {
                        singleInputView.setContent(TimeUtils.getYYYYMMDDHHMMString(currentSickbed.getVisitTime()));
                        singleInputView.setEnabled(false);
                    }
                }
                singleInputView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                singleInputView.setGravity(Gravity.CENTER);
                parent.removeAllViews();
                parent.addView(singleInputView);
                break;
            case CommonConstant.VIEW_TYPE_MN_DOUBLE_INPUT_VIEW://双输入框
                Double[][] limitArr = parseDoubleValueLimit(item);
                String[] unitArr = MNUtils.parseStringArrayByStringFormat(item.getUnit());
                String[] valueArr = MNUtils.parseStringArrayByStringFormat(item.getValue());
                MNDoubleInputView doubleInputView = new MNDoubleInputView.Builder(mContext)
                        .inputType1(item.getValueDataType())
                        .inputType2(item.getValueDataType())
                        .inputValueLimitUpper1(limitArr[0][0])
                        .inputValueLimitUpper2(limitArr[0][1])
                        .inputValueLimitLower1(limitArr[1][0])
                        .inputValueLimitLower2(limitArr[1][1])
                        .normalValueLimitUpper1(limitArr[2][0])
                        .normalValueLimitUpper2(limitArr[2][1])
                        .normalValueLimitLower1(limitArr[3][0])
                        .normalValueLimitLower2(limitArr[3][1])
                        .suffix1(unitArr[0])
                        .suffix2(unitArr[1])
                        .content1(valueArr[0])
                        .content2(valueArr[1])
                        .useIndicatorStatus1(true)
                        .useIndicatorStatus2(true)
                        .onTextChangedListener1(new MNInputView.OnTextChangedListener() {
                            @Override
                            public void onTextChanged(@Nullable IndicatorStatus status, String content) {//左边输入框
                                setItemInputValue(status, content, item, INPUT_POSITION_LEFT);
                            }
                        })
                        .onTextChangedListener2(new MNInputView.OnTextChangedListener() {
                            @Override
                            public void onTextChanged(@Nullable IndicatorStatus status, String content) {//右边输入框
                                setItemInputValue(status, content, item, INPUT_POSITION_RIGHT);
                            }
                        }).build();
                doubleInputView.setGravity(Gravity.CENTER);
                parent.removeAllViews();
                parent.addView(doubleInputView);
                break;
            case CommonConstant.VIEW_TYPE_BP_VIEW_FOR_LX://临夏州的血压
                limitArr = parseDoubleValueLimit(item);
                String unit = item.getUnit();
                BPViewForLX bpViewForLX = new BPViewForLX.Builder(mContext)
                        .inputType1(item.getValueDataType())
                        .inputType2(item.getValueDataType())
                        .inputValueLimitUpper1(limitArr[0][0])
                        .inputValueLimitUpper2(limitArr[0][1])
                        .inputValueLimitLower1(limitArr[1][0])
                        .inputValueLimitLower2(limitArr[1][1])
                        .normalValueLimitUpper1(limitArr[2][0])
                        .normalValueLimitUpper2(limitArr[2][1])
                        .normalValueLimitLower1(limitArr[3][0])
                        .normalValueLimitLower2(limitArr[3][1])
                        .suffix(unit)
                        .currentSubIndex(item.getCurrentSubIndex())
                        .content(item.getValue())
                        .useIndicatorStatus1(true)
                        .useIndicatorStatus2(true)
                        .onTextChangedListener(new BPViewForLX.OnLXBPTextChangedListener() {
                            @Override
                            public void onTextChanged(String content, int currentSunIndex) {
                                item.setCurrentSubIndex(currentSunIndex);
                                item.setValue(content);
                            }
                        }).build();
                bpViewForLX.setGravity(Gravity.CENTER);
                parent.removeAllViews();
                parent.addView(bpViewForLX);
                break;
        }
    }

    /**
     * 根据输入框输入的值，设置相应的Item的Value
     *
     * @param status        当前输入框的值的对应指示牌（正常、危险、错误）
     * @param input         输入框的内容
     * @param item          当前Item
     * @param inputPosition 输入框标记，是单输入框、左输入框、右输入框
     */
    private void setItemInputValue(IndicatorStatus status, String input, VitalSignsViewItem item, int inputPosition) {
        if (item.isValueSpecialExclusive() && !TextUtils.isEmpty(item.getSpecialValue())) //值与例外值互斥，则在输入Value的时候，将SpecialValue置空
            item.setSpecialValue("");
        String value;
        if (status != null && status == IndicatorStatus.ERROR)//当输入框的内容，是错误的，则将对应的value设为空
            value = "";
        else
            value = input;
        if (inputPosition == INPUT_POSITION_LEFT) {//双输入框，要分左右，两边的值用“|”符号隔开
            value = MNUtils.replaceLeftFormatString(item.getValue(), value);
        } else if (inputPosition == INPUT_POSITION_RIGHT) {//双输入框，要分左右，两边的值用“|”符号隔开
            value = MNUtils.replaceRightFormatString(item.getValue(), value);
        } else {//单输入框不作处理，后面直接设为Value的值
        }
        item.setValue(value);
    }

    /**
     * 返回长度为4的一个Double二维数组数组，分别表示，左右两个输入框的
     * //可输入值的上限
     * private Double inputValueLimitUpper1,inputValueLimitUpper2;
     * //可输入值的下限
     * private Double inputValueLimitLower1,inputValueLimitLower2;
     * //正常值范围上限
     * private Double normalValueLimitUpper1,normalValueLimitUpper2;
     * //正常值范围下限
     * private Double normalValueLimitLower1,normalValueLimitLower2;
     *
     * @param item
     * @return
     */
    private Double[][] parseDoubleValueLimit(VitalSignsViewItem item) {
        Double[][] limit = new Double[4][2];
        limit[0] = MNUtils.parseDoubleArrayByStringFormat(item.getInputUpperLimit());
        limit[1] = MNUtils.parseDoubleArrayByStringFormat(item.getInputLowerLimit());
        limit[2] = MNUtils.parseDoubleArrayByStringFormat(item.getUpperLimitValue());
        limit[3] = MNUtils.parseDoubleArrayByStringFormat(item.getLowerLimitValue());
        return limit;
    }


    @Override
    protected void convert(ViewHolder holder, Map<String, Object> stringObjectMap, int position) {

    }

}
