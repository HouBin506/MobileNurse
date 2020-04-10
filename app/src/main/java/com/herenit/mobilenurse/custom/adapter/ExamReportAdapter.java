package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herenit.arms.base.adapter.rv.CommonAdapter;
import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.entity.ExamReport;

import java.util.List;
import java.util.Map;

/**
 * author: HouBin
 * date: 2019/1/22 15:12
 * desc: 普通的带有展开子项选择的Item，内容只有一个TextView
 */
public class ExamReportAdapter extends CommonAdapter<ExamReport> {


    public ExamReportAdapter(Context context, List<ExamReport> datas) {
        super(context, R.layout.item_exam_report, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ExamReport item, int position) {
        View view_divider = holder.getView(R.id.view_examReport_divider);
        LinearLayout ll_group = holder.getView(R.id.ll_item_examReport_group);
        TextView tv_className = holder.getView(R.id.tv_examReport_className);//检查类别
        TextView tv_requestTime = holder.getView(R.id.tv_examReport_requestDateTime);//申请时间
        TextView tv_resultStatus = holder.getView(R.id.tv_examReport_resultStatus);//检查状态
        TextView tv_scheduledTime = holder.getView(R.id.tv_examReport_scheduledDateTime);//预约时间
        TextView tv_examNo = holder.getView(R.id.tv_examReport_examNo);//检查单号
        TextView tv_performDept = holder.getView(R.id.tv_examReport_performDept);//执行科室
        TextView tv_itemName = holder.getView(R.id.tv_examReport_itemName);//检查项目名称
        if (position == 0 || !mDatas.get(position - 1).getExamNo().equals(item.getExamNo())) {//一组检查的开头项
            view_divider.setVisibility(View.VISIBLE);
            ll_group.setVisibility(View.VISIBLE);
            tv_className.setText(item.getExamSubClass());
            tv_requestTime.setText(TimeUtils.getYYYYMMDDHHMMString(item.getReqDateTime()));
            tv_resultStatus.setText(item.getResultStatusName());
            tv_examNo.setText(item.getExamNo());
            tv_scheduledTime.setText(TimeUtils.getYYYYMMDDHHMMString(item.getScheduledDateTime()));
            tv_performDept.setText(item.getPerformDeptName());
        } else {
            view_divider.setVisibility(View.GONE);
            ll_group.setVisibility(View.GONE);
        }
        tv_itemName.setText(item.getExamItemName());
    }

    @Override
    protected void convert(ViewHolder holder, Map<String, Object> stringObjectMap, int position) {

    }
}
