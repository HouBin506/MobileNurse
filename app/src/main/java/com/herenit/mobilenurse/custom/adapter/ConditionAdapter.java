package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.common.ConditionItem;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.custom.widget.layout.MeasuredGridLayoutManager;
import com.herenit.mobilenurse.custom.widget.layout.SpacesItemDecoration;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * author: HouBin
 * date: 2019/2/26 9:53
 * desc:
 */
public class ConditionAdapter extends CommonAdapter<Conditions> {
    private int totalHeight = 0;

    private String eventId;

    public ConditionAdapter(Context context, List<Conditions> datas, @NonNull String eventId) {
        super(context, datas, R.layout.item_common_recyclerview_horizontal);
        this.eventId = eventId;
    }

    @Override
    protected void convert(ViewHolder holder, Conditions item, int position) {
        if (!holder.isViewInit(R.id.rv_item_common_recyclerViewHorizontal)) {
            MeasuredGridLayoutManager gridLayoutManager = new MeasuredGridLayoutManager(mContext, 1);
            gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            RecyclerView recyclerView = holder.getView(R.id.rv_item_common_recyclerViewHorizontal);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addItemDecoration(
                    new SpacesItemDecoration(mContext.getResources().getDimensionPixelSize(R.dimen.dp_2)));
            ConditionItemAdapter adapter = new ConditionItemAdapter(mContext,
                    R.layout.item_condition_checkbox_text, item.getConditions(), position);
            recyclerView.setAdapter(adapter);
        } else {
            RecyclerView recyclerView = holder.getView(R.id.rv_item_common_recyclerViewHorizontal);
            ConditionItemAdapter conditionItemAdapter = (ConditionItemAdapter) recyclerView.getAdapter();
            List<ConditionItem> data = conditionItemAdapter.getDatas();
            if (data != null && data.equals(item.getConditions())) {//当子项数据列表对象没变时，直接刷新即可
                conditionItemAdapter.notifyDataSetChanged();
            } else {//当子项数据发生变化，则之前的旧数据不可使用，要重新设置Adapter，否则会出现条件未切换的情况
                conditionItemAdapter = new ConditionItemAdapter(mContext,
                        R.layout.item_condition_checkbox_text, item.getConditions(), position);
                recyclerView.setAdapter(conditionItemAdapter);
            }
//            ((RecyclerView) holder.getView(R.id.rv_item_common_recyclerViewHorizontal)).getAdapter().notifyDataSetChanged();
        }
    }


    class ConditionItemAdapter extends com.herenit.arms.base.adapter.rv.CommonAdapter<ConditionItem> {
        private int currentLine = 0;//当前操作的行

        public ConditionItemAdapter(Context context, int layoutId, List<ConditionItem> datas, int currentLine) {
            super(context, layoutId, datas);
            this.currentLine = currentLine;
        }

        @Override
        protected void convert(com.herenit.arms.base.adapter.rv.ViewHolder holder, ConditionItem item, int position) {

            CheckBox checkBox = holder.getView(R.id.cb_item_conditionCheckBox);
            boolean isChecked = item.isChecked();
            checkBox.setChecked(isChecked);
            if (!TextUtils.isEmpty(item.getId()) && item.getId().equals(CommonConstant.VIEW_TYPE_CALENDAR)) {//日历，此处应显示日历控件
                String text = item.getName();
                if (TextUtils.isEmpty(text)) {
                    text = TimeUtils.getDateStringByFormat(TimeUtils.getCurrentDate(), item.getFormat());
                    item.setName(text);
                }
                if (!TextUtils.isEmpty(text)) {
                    checkBox.setText(text);
                    checkBox.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.selector_checkbox_calendar, 0);
                    checkBox.setOnClickListener(v -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(TimeUtils.getDateByFormat(item.getFormat(), item.getName()));
                        new TimePickerBuilder(mContext, (date, v1) -> {
                            String dateStr = TimeUtils.getYYYYMMDDString(date);
                            item.setName(dateStr);
                            checkBox.setText(dateStr);
                            conditionChanged(currentLine, position, checkBox.isChecked());
                            EventBusUtils.post(eventId, null);
                        }).setType(new boolean[]{true, true, true, false, false, false})
                                .isDialog(true)
                                .setDate(calendar)
                                .build()
                                .show();
                    });
                }
            } else {
                checkBox.setOnClickListener(v -> {
                    conditionChanged(currentLine, position, checkBox.isChecked());
                    EventBusUtils.post(eventId, null);
                });
                checkBox.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                checkBox.setText(item.getName());
            }
        }

        @Override
        protected void convert(com.herenit.arms.base.adapter.rv.ViewHolder holder, Map<String, Object> stringObjectMap, int position) {

        }
    }

    /**
     * 选择条件发生改变
     *
     * @param line
     * @param column
     * @param isChecked
     */
    private void conditionChanged(int line, int column, boolean isChecked) {
        //获取当前操作的行
        Conditions conditions = mDatas.get(line);
        if (conditions.isMultipleChoice()) {//多选
            conditions.getConditions().get(column).setChecked(isChecked);
            boolean isAll = conditions.getConditions().get(column).isAll();
            if (isAll) {
                setOnlyOneChecked(conditions, column);
            } else {
                if (isChecked)//如果选中的是其他选项，那就先把全选取消掉
                    conditions.getConditions().get(0).setChecked(false);
                int checkCount = 0;
                for (int x = 0; x < conditions.getConditions().size(); x++) {
                    ConditionItem item = conditions.getConditions().get(x);
                    if (item.isChecked())
                        checkCount++;
                }
                if (checkCount == 0 || checkCount >= conditions.getConditions().size() - 1) {//如果全选或者全不选，那就按照全选来
                    setOnlyOneChecked(conditions, 0);//默认第一项为全选
                }
            }
        } else {//单选
            if (isChecked)
                setOnlyOneChecked(conditions, column);
        }
        setSingleLines(line, conditions);
        this.notifyDataSetChanged();
    }

    /**
     * 将一行选项只选择一项
     *
     * @param conditions
     * @param column     被选中的列
     */
    private void setOnlyOneChecked(Conditions conditions, int column) {
        for (int x = 0; x < conditions.getConditions().size(); x++) {
            ConditionItem item = conditions.getConditions().get(x);
            if (x == column)
                item.setChecked(true);
            else
                item.setChecked(false);
        }
    }

    /**
     * 设置不共存的行
     * 如果当前操作的是不共存的行，那要把其他行的选择置为全选
     * 如果当前操作的是其他可共存的行，就找出不共存的行，并去掉该行的所有选择
     *
     * @param line
     * @param conditions
     */
    private void setSingleLines(int line, Conditions conditions) {
        if (conditions.isSingle()) {//与其他行条件不共存
            for (int x = 0; x < mDatas.size(); x++) {
                if (x == line)
                    continue;
                Conditions condition = mDatas.get(x);
                //要将其他行都置为全选状态
                for (int y = 0; y < condition.getConditions().size(); y++) {
                    ConditionItem item = condition.getConditions().get(y);
                    item.setChecked(false);
//                    if (item.isAll()) {
//                        if (condition.isSingle())
//                            item.setChecked(false);
//                        else
//                            item.setChecked(true);
//                    } else {
//                        item.setChecked(false);
//                    }
                }
            }
        } else {//与其他行条件共存
            for (int x = 0; x < mDatas.size(); x++) {
                if (x == line)
                    continue;
                Conditions condition = mDatas.get(x);
                if (condition.isSingle()) {//找到不共存行
                    for (int y = 0; y < condition.getConditions().size(); y++) {
                        ConditionItem item = condition.getConditions().get(y);
                        item.setChecked(false);
                    }
                }
            }
        }
    }
}
