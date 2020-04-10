package com.herenit.mobilenurse.criteria.entity.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.enums.ChoiceTypeEnum;
import com.herenit.mobilenurse.custom.adapter.CommonTextAdapter;
import com.herenit.mobilenurse.custom.listener.OnActionListener;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.dialog.ListDialog;
import com.herenit.mobilenurse.custom.widget.dialog.SeekBarDialog;
import com.herenit.mobilenurse.custom.widget.dialog.SingleInputDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * author: HouBin
 * date: 2019/3/13 13:42
 * desc: （Responsive View Model）响应式View的控制模型，用于控制某控件显示、
 * 是否可编辑、以及控件被触摸后做出的响应。比如某View，上面显示了Name、Value
 * 点击该View弹出输入框，输入完数据后点击确定则会成功修改Value的值
 */
public class RvController implements MultiItemDelegate {

    //Item要表示的名称
    private String name;
    //Item要显示的数据
    private String value;
    /**
     * 对当前条目进行操作时（填写、选择、修改），具体的控件类型
     */
    private String actionViewType;
    /**
     * 数据类型（对于非日期类型的数据，表示数据类型，比如“Number”、“Boolean”等）
     */
    private String valueType;

    /**
     * 格式，对于日期类型的数据，则表示日期格式，如“yyyy-MM-dd HH:mm”
     */
    private String valueFormat;

    /**
     * 当对条目操作时，操作的控件需要的数据
     */
    private Map<String, Object> viewData;
    //当前Item是否只读
    private boolean readOnly;
    /**
     * 是否可以编辑，当Item为只读时，不可编辑，
     * 当Item不是只读时，通过editable来控制是否可以编辑修改
     */
    private boolean editable;

    public RvController() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getActionViewType() {
        return actionViewType;
    }

    public void setActionViewType(String actionViewType) {
        this.actionViewType = actionViewType;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Object getViewData() {
        return
                this.viewData;
    }

    public void setViewData(Map<String, Object> viewData) {
        this.viewData = viewData;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getValueFormat() {
        return valueFormat;
    }

    public void setValueFormat(String valueFormat) {
        this.valueFormat = valueFormat;
    }

    /**
     * 显示操作响应，可能是弹窗列表、弹出选择框、进度条、提示、时间选择等
     *
     * @param context  上下文
     * @param listener 无法判断操作是正负极按钮还是选择返回数据
     */
    public void showAction(@NonNull Context context,
                           OnActionListener listener) {
        if (TextUtils.isEmpty(actionViewType) || context == null || listener == null)//当操作响应控件为空，则不响应
            return;
        switch (actionViewType) {
            case CommonConstant.VIEW_TYPE_CALENDAR://时间选择
                showTimePicker(context, listener);
                break;
            case CommonConstant.VIEW_TYPE_SEEK_BAR_DIALOG://可手动编辑的进度条
                showSeekBarDialog(context, listener);
                break;
            case CommonConstant.VIEW_TYPE_LIST_DIALOG://列表弹窗
                showListDialog(context, listener);
                break;
            case CommonConstant.VIEW_TYPE_SINGLE_INPUT_DIALOG://单输入框
                showSingleInputDialog(context, listener);
                break;
        }
    }

    /**
     * 显示单输入框的弹窗
     *
     * @param context
     * @param listener
     */
    private void showSingleInputDialog(Context context, OnActionListener listener) {
        String title = (String) viewData.get(CommonConstant.FIELD_NAME_TITLE);
        String hint = (String) viewData.get(CommonConstant.FIELD_NAME_HINT);
        String text = (String) viewData.get(CommonConstant.FIELD_NAME_DATA);
        SingleInputDialog dialog = new SingleInputDialog.Builder(context)
                .hint(hint)
                .content(text)
                .title(title)
                .build();
        dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
            @Override
            public void onPositive(Object... backData) {
                dialog.dismiss();
                String content = (String) backData[0];
                viewData.put(CommonConstant.FIELD_NAME_DATA, content);
                setValue(content);
                listener.onAction(content);
            }

            @Override
            public void onNegative() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 显示列表弹窗
     *
     * @param context
     * @param actionListener
     */
    private void showListDialog(Context context, OnActionListener actionListener) {
        if (actionListener == null)
            return;
        String title = (String) viewData.get(CommonConstant.FIELD_NAME_TITLE);
        List<String> data = (List<String>) viewData.get(CommonConstant.FIELD_NAME_DATA);
        ListDialog dialog = new ListDialog.Builder(context)
                .title(title)
                .bottomVisibility(false)
                .choiceType(ChoiceTypeEnum.SINGLE)
                .adapter(new CommonTextAdapter(context, data))
                .build();
        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                String result = data.get(position);
                setValue(result);
                actionListener.onAction(result);
            }
        });
        dialog.show();
    }


    /**
     * 显示可编辑的普通进度条弹窗
     *
     * @param context
     * @param listener
     */
    private void showSeekBarDialog(Context context, OnActionListener listener) {
        String title = (String) viewData.get(CommonConstant.FIELD_NAME_TITLE);
        int progress = (int) viewData.get(CommonConstant.FIELD_NAME_DATA);
        SeekBarDialog dialog = new SeekBarDialog.Builder(context)
                .progress(progress)
                .title(title)
                .build();
        dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
            @Override
            public void onPositive(Object... backData) {
                dialog.dismiss();
                int pro = (int) backData[0];
                viewData.put(CommonConstant.FIELD_NAME_DATA, pro);
                setValue(pro + "%");
                listener.onAction(pro);//将控件和选择结果一并返回
            }

            @Override
            public void onNegative(Object... backData) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 显示时间选择器
     *
     * @param context
     * @param listener
     */
    private void showTimePicker(Context context, OnActionListener listener) {
        if (listener == null)
            return;
        Date date = (Date) viewData.get(CommonConstant.FIELD_NAME_DATA);
        if (date == null)
            date = TimeUtils.getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        TimePickerBuilder builder = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//确定按钮点击
                viewData.put(CommonConstant.FIELD_NAME_DATA, date);
                setValue(TimeUtils.getDateStringByFormat(date, valueFormat));
                listener.onAction(date);
            }
        }).setDate(calendar).isDialog(true);
        switch (valueFormat) {
            case TimeUtils.FORMAT_YYYYMMDDHHMM:
                builder.setType(new boolean[]{true, true, true, true, true, false});
                break;
            case TimeUtils.FORMAT_YYYYMMDD:
                builder.setType(new boolean[]{true, true, true, false, false, false});
                break;
            default:
                builder.setType(new boolean[]{true, true, true, true, true, false});
                break;
        }
        builder.build().show();
    }
}
