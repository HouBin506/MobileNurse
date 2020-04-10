package com.herenit.mobilenurse.custom.adapter.delegate;

import android.text.TextUtils;
import android.view.View;


import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.arms.base.adapter.rv.base.ItemViewDelegate;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by HouBin on 2018/9/6.
 */

public class AssessCalendarDelegate implements ItemViewDelegate<AssessViewItem> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_assess_dialog_calendar;
    }

    @Override
    public boolean isForViewType(AssessViewItem item, int position) {
        return AssessViewItem.ASSESS_VIEW_CALENDAR.equals(item.getViewName());
    }

    @Override
    public void convert(final ViewHolder holder, final AssessViewItem model, int position) {
        final String value = model.getDataValue();
        if (!TextUtils.isEmpty(value))
            holder.setText(R.id.tv_item_Assess_dialog_calendar_time, value);
        if (!model.isReadOnly()) {
            holder.setOnClickListener(R.id.tv_item_Assess_dialog_calendar_time, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date date = TimeUtils.getDateByFormat(TimeUtils.FORMAT_YYYYMMDDHHMM, value);
                    if (date == null)
                        date = TimeUtils.getCurrentDate();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    new TimePickerBuilder(holder.getConvertView().getContext(), new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            String time = TimeUtils.getYYYYMMDDHHMMString(date);
                            model.setDataValue(time);
                            model.setShowDataValue(time);
                            holder.setText(R.id.tv_item_Assess_dialog_calendar_time, time);
                        }
                    }).setType(new boolean[]{true, true, true, true, true, false})
                            .setDate(calendar)
                            .isDialog(true)
                            .build().show();
                }
            });
        }
    }
}
