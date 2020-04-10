package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.entity.OperationScheduled;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/5/22 16:44
 * desc: 手术安排列表适配器
 */
public class OperationScheduledAdapter extends CommonAdapter<OperationScheduled> {


    public OperationScheduledAdapter(Context context, List<OperationScheduled> datas) {
        super(context, datas, R.layout.item_operation_scheduled);
    }

    @Override
    protected void convert(ViewHolder holder, OperationScheduled item, int position) {
        holder.setText(R.id.tv_item_operationScheduled_bedAndName, item.getBedLabel() + "床 " + item.getName());
        if (item.getScheduledDateTime() != null) {
            holder.setText(R.id.tv_item_operationScheduled_operationTime, TimeUtils.getYYYYMMDDHHMMString(item.getScheduledDateTime()));
        } else {
            holder.setText(R.id.tv_item_operationScheduled_operationTime, "");
        }
        if (item.isEmergency() && item.isUnconfirmed()) {//紧急并且未确认的手术，标红
            holder.setTextColor(R.id.tv_item_operationScheduled_emergency, ArmsUtils.getColor(mContext, R.color.red));
            holder.setTextColor(R.id.tv_item_operationScheduled_operationStatus, ArmsUtils.getColor(mContext, R.color.red));
        } else {
            holder.setTextColor(R.id.tv_item_operationScheduled_emergency, ArmsUtils.getColor(mContext, R.color.light_black));
            holder.setTextColor(R.id.tv_item_operationScheduled_operationStatus, ArmsUtils.getColor(mContext, R.color.light_black));
        }
        holder.setText(R.id.tv_item_operationScheduled_emergency, item.getEmergencyIndicator());
        holder.setText(R.id.tv_item_operationScheduled_operationDoctor, item.getOperatorDoctor());
        holder.setText(R.id.tv_item_operationScheduled_diagnose, item.getDiagBeforeOperation());
        holder.setText(R.id.tv_item_operationScheduled_operationName, item.getOperationName());
        holder.setText(R.id.tv_item_operationScheduled_operationStatus, item.getAckIndicator());
    }
}
