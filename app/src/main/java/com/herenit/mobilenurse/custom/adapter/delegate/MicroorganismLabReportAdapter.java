package com.herenit.mobilenurse.custom.adapter.delegate;

import android.content.Context;
import android.text.TextUtils;

import com.herenit.arms.base.adapter.rv.CommonAdapter;
import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.entity.lab.MicroorganismLabReport;

import java.util.List;
import java.util.Map;

/**
 * author: HouBin
 * date: 2019/1/22 15:12
 * desc: 微生物检验Adapter
 */
public class MicroorganismLabReportAdapter extends CommonAdapter<MicroorganismLabReport> {


    public MicroorganismLabReportAdapter(Context context, List<MicroorganismLabReport> datas) {
        super(context, R.layout.item_microorganism_lab_report, datas);
    }


    @Override
    protected void convert(ViewHolder holder, MicroorganismLabReport item, int position) {
        if (position == 0 || !mDatas.get(position - 1).getLabNo().equals(item.getLabNo())) {//某项检验的开头项
            holder.setVisible(R.id.view_item_microorganismLabReport_divider, true);
            holder.setVisible(R.id.tv_item_microorganismLabReport_itemName, true);
            holder.setVisible(R.id.ll_item_microorganismLabReport_itemTime, true);
            holder.setText(R.id.tv_item_microorganismLabReport_itemName, item.getSpecimen());
            if (item.getReqDateTime() != null)
                holder.setText(R.id.tv_item_microorganismLabReport_requestDateTime, TimeUtils.getYYYYMMDDHHMMString(item.getReqDateTime()));
            else
                holder.setText(R.id.tv_item_microorganismLabReport_requestDateTime, "");
            if (item.getExecuteDateTime() != null)
                holder.setText(R.id.tv_item_microorganismLabReport_labDateTime, TimeUtils.getYYYYMMDDHHMMString(item.getExecuteDateTime()));
            else
                holder.setText(R.id.tv_item_microorganismLabReport_labDateTime, "");
        } else {
            holder.setVisible(R.id.view_item_microorganismLabReport_divider, false);
            holder.setVisible(R.id.tv_item_microorganismLabReport_itemName, false);
            holder.setVisible(R.id.ll_item_microorganismLabReport_itemTime, false);
        }
        holder.setText(R.id.tv_item_microorganismLabReport_bioName, item.getBioName());
        if (!TextUtils.isEmpty(item.getResultType()))
            holder.setText(R.id.tv_item_microorganismLabReport_resultType, item.getResultType());
        else
            holder.setText(R.id.tv_item_microorganismLabReport_resultType, "");
        if (!TextUtils.isEmpty(item.getSpectrum()))
            holder.setText(R.id.tv_item_microorganismLabReport_spectrum, item.getSpectrum());
        else
            holder.setText(R.id.tv_item_microorganismLabReport_spectrum, "");
    }

    @Override
    protected void convert(ViewHolder holder, Map<String, Object> stringObjectMap, int position) {

    }
}
