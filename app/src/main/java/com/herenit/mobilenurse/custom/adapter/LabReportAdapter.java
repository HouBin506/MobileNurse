package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;

import com.herenit.arms.base.adapter.rv.CommonAdapter;
import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.entity.lab.CommonLabReport;

import java.util.List;
import java.util.Map;

/**
 * author: HouBin
 * date: 2019/1/22 15:12
 * desc: 普通的带有展开子项选择的Item，内容只有一个TextView
 */
public class LabReportAdapter extends CommonAdapter<CommonLabReport> {


    public LabReportAdapter(Context context, List<CommonLabReport> datas) {
        super(context, R.layout.item_lab_report, datas);
    }


    @Override
    protected void convert(ViewHolder holder, CommonLabReport item, int position) {
        holder.setText(R.id.tv_labReport_iteName, item.getLabItemName());
        if (item.getReqDateTime() != null)
            holder.setText(R.id.tv_labReport_requestDateTime, TimeUtils.getYYYYMMDDHHMMString(item.getReqDateTime()));
        else
            holder.setText(R.id.tv_labReport_requestDateTime, "");
        if (item.getSpcmSampleDateTime() != null)
            holder.setText(R.id.tv_labReport_sampleDateTime, TimeUtils.getYYYYMMDDHHMMString(item.getSpcmSampleDateTime()));
        else
            holder.setText(R.id.tv_labReport_sampleDateTime, "");
        holder.setText(R.id.tv_labReport_labNo, item.getLabNo());
        if (item.getSpecimen() != null)
            holder.setText(R.id.tv_labReport_specimen, item.getSpecimen());
        else
            holder.setText(R.id.tv_labReport_specimen, "");
        if (item.getReqDeptName() != null)
            holder.setText(R.id.tv_labReport_requestDept, item.getReqDeptName());
        else
            holder.setText(R.id.tv_labReport_requestDept, "");
        if (item.getResultStatusName() != null)
            holder.setText(R.id.tv_labReport_resultStatus, item.getResultStatusName());
        else
            holder.setText(R.id.tv_labReport_resultStatus, "");
    }

    @Override
    protected void convert(ViewHolder holder, Map<String, Object> stringObjectMap, int position) {

    }
}
