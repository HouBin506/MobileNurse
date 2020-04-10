package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.herenit.arms.base.adapter.rv.CommonAdapter;
import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.NurseRoundItem;

import java.util.List;
import java.util.Map;

/**
 * author: HouBin
 * date: 2019/8/12 15:24
 * desc:护理巡视历史记录列表Adapter
 */
public class NurseRoundHistoryAdapter extends CommonAdapter<NurseRoundItem> {

    public NurseRoundHistoryAdapter(Context context, List<NurseRoundItem> datas) {
        super(context, R.layout.item_nurse_round_history, datas);

    }

    @Override
    protected void convert(ViewHolder holder, NurseRoundItem item, int position) {
        View view_divider = holder.getView(R.id.view_item_nurseRoundHistory_divider);
        RelativeLayout rl_title = holder.getView(R.id.rl_item_nurseRoundHistory_title);
        TextView tv_title = holder.getView(R.id.tv_item_nurseRoundHistory_title);
        TextView tv_timePoint = holder.getView(R.id.tv_item_nurseRoundHistory_timePoint);
        TextView tv_desc = holder.getView(R.id.tv_item_nurseRoundHistory_desc);
        LinearLayout ll_value = holder.getView(R.id.ll_item_nurseRoundHistory_value);
        TextView tv_itemName = holder.getView(R.id.tv_item_nurseRoundHistory_itemName);
        TextView tv_itemValue = holder.getView(R.id.tv_item_nurseRoundHistory_itemValue);
        if (CommonConstant.ITEM_TYPE_GROUP.equals(item.getItemType())) {//组
            view_divider.setVisibility(View.VISIBLE);
            rl_title.setVisibility(View.VISIBLE);
            ll_value.setVisibility(View.GONE);
            tv_title.setText(item.getItemName());
            tv_timePoint.setText(TimeUtils.getYYYYMMDDHHMMString(item.getTimePoint()));
            if (TextUtils.isEmpty(item.getItemDescription())) {
                tv_desc.setVisibility(View.GONE);
            } else {
                tv_desc.setVisibility(View.VISIBLE);
                String desc = item.getItemDescription();
                desc = desc.replaceAll("\\|", "\n");
                tv_desc.setText(desc);
            }
        } else {
            view_divider.setVisibility(View.GONE);
            rl_title.setVisibility(View.GONE);
            tv_desc.setVisibility(View.GONE);
            ll_value.setVisibility(View.VISIBLE);
            tv_itemName.setText(item.getItemName());
            tv_itemValue.setText(item.getItemValue());
        }
    }

    @Override
    protected void convert(ViewHolder holder, Map<String, Object> stringObjectMap, int position) {

    }
}
