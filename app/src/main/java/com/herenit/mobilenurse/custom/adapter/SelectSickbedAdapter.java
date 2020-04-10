package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.entity.Sickbed;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/3/1 16:31
 * desc: 选择患者的下拉列表Adapter
 */
public class SelectSickbedAdapter extends CommonAdapter<Sickbed> {
    private int selectPosition;

    public SelectSickbedAdapter(Context context, List<Sickbed> datas) {
        super(context, datas, R.layout.item_select_sickbed);
    }

    @Override
    protected void convert(ViewHolder holder, Sickbed item, int position) {
        holder.setText(R.id.tv_item_selectSickbed_bedLabel, item.getBedLabel());
        holder.setText(R.id.tv_item_selectSickbed_patientName, item.getPatientName());
        if (selectPosition == position)
            holder.getConvertView().setBackgroundColor(ArmsUtils.getColor(mContext, R.color.green_selected_bg));
        else
            holder.getConvertView().setBackgroundColor(ArmsUtils.getColor(mContext, R.color.white));
    }

    public void setSelectPosition(int position) {
        this.selectPosition = position;
    }

    public int getSelectPosition() {
        return selectPosition;
    }
}
