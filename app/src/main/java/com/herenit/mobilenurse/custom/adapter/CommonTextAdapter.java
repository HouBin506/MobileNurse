package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.common.NameCode;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/2/14 17:30
 * desc: 普通的TextView 为Item的Adapter
 */
public class CommonTextAdapter extends CommonAdapter<String> {

    private int mSelectPosition = -1;

    private Context mContext;

    public CommonTextAdapter(Context context, List datas) {
        super(context, datas, R.layout.item_common_text);
        mContext = context;
    }


    @Override
    protected void convert(ViewHolder holder, String item, int position) {
        holder.setText(R.id.tv_item_text_text, item);
        if (mSelectPosition == position)
            holder.getConvertView().setBackgroundColor(ArmsUtils.getColor(mContext, R.color.green_selected_bg));
        else
            holder.getConvertView().setBackgroundColor(ArmsUtils.getColor(mContext, R.color.white));
    }

    public void setSelectItem(int position) {
        mSelectPosition = position;
    }
}
