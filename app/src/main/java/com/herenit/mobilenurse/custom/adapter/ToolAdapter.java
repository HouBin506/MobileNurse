package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.enums.ModuleEnum;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/10/12 16:48
 * desc:工具
 */
public class ToolAdapter extends CommonAdapter<ModuleEnum> {

    public ToolAdapter(Context context, List<ModuleEnum> datas) {
        super(context, datas, R.layout.item_tool);
    }

    @Override
    protected void convert(ViewHolder holder, ModuleEnum item, int position) {
        holder.setImageResource(R.id.img_item_tool_image, item.iconRes());
        holder.setText(R.id.tv_item_tool_name, item.itemName());
    }
}
