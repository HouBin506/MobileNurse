package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.MNUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/5/16 10:02
 * desc: 体征历史记录Adapter
 */
public class VitalSignsHistoryAdapter extends CommonAdapter<VitalSignsItem> {

    public VitalSignsHistoryAdapter(Context context, List<VitalSignsItem> datas) {
        super(context, datas, R.layout.item_vital_signs_history);
    }

    @Override
    protected void convert(ViewHolder holder, VitalSignsItem item, int position) {
        try {
            holder.setText(R.id.tv_item_vitalSignsHistory_time, TimeUtils.getYYYYMMDDHHMMString(item.getTimePoint()));//录入时间
            holder.setText(R.id.tv_item_vitalSignsHistory_name, item.getItemName());//项目名
            if (!TextUtils.isEmpty(item.getUnit()))//单位
                holder.setText(R.id.tv_item_vitalSignsHistory_unit, item.getUnit());
            else
                holder.setText(R.id.tv_item_vitalSignsHistory_unit, "");
            if (!TextUtils.isEmpty(item.getMemo())) {//备注
                holder.setVisibility(R.id.ll_item_vitalSignsHistory_memo, View.VISIBLE);
                holder.setText(R.id.tv_item_vitalSignsHistory_memo, item.getMemo());
            } else {
                holder.setVisibility(R.id.ll_item_vitalSignsHistory_memo, View.GONE);
            }
            String value = item.getItemValue();
            TextView tv_value = holder.getView((R.id.tv_item_vitalSignsHistory_value));
            if (!TextUtils.isEmpty(value)) {//利用加载Html的办法，动态改变体征项数据的字体颜色，这样可以更好的显示出不同状态下的数据
                if (item.getItemName().equals(CommonConstant.VITAL_SIGNS_BP)) {//临夏州的血压特殊处理方法
                    String htmlValue = MNUtils.getLXHtmlFontBloodLimitValue(value, item.getUpperLimitValue(), item.getLowerLimitValue());
                    tv_value.setText(Html.fromHtml(htmlValue));
                } else {
                    String htmlValue = MNUtils.getHtmlFontValueByLimit(value, item.getInputUpperLimit(), item.getInputLowerLimit(), item.getUpperLimitValue(), item.getLowerLimitValue());
                    tv_value.setText(Html.fromHtml(htmlValue));
                }
            } else {
                //显示例外值
                String specialValue = item.getSpecialValue();
                if (!TextUtils.isEmpty(specialValue))
                    tv_value.setText(specialValue);
                else
                    tv_value.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
