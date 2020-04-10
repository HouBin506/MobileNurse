package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.MNUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.entity.HealthEduHistoryItem;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/5/16 10:02
 * desc: 健康宣教记录Adapter
 */
public class HealthEduHistoryAdapter extends CommonAdapter<HealthEduHistoryItem> {

    public HealthEduHistoryAdapter(Context context, List<HealthEduHistoryItem> datas) {
        super(context, datas, R.layout.item_health_edu_history);
    }

    @Override
    protected void convert(ViewHolder holder, HealthEduHistoryItem item, int position) {
        try {
            holder.setText(R.id.tv_item_healthEduHistory_time, TimeUtils.getYYYYMMDDHHMMString(item.getModifyTime()));//操作时间
            holder.setText(R.id.tv_item_healthEduHistory_name, item.getModifierName());//操作人
            if (TextUtils.isEmpty(item.getSelectedItemNameList())) {//宣教项目为空
                holder.setText(R.id.tv_item_healthEduHistory_item, "");
            } else {//宣教项目不为空
                holder.setText(R.id.tv_item_healthEduHistory_item, item.getSelectedItemNameList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
