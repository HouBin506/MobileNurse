package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.entity.MultiListMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/9/10 10:32
 * desc:多级列表菜单的Item适配器
 */
public class MultiListMenuAdapter extends CommonAdapter<MultiListMenuItem> {

    private int currentPosition = -1;

    public MultiListMenuAdapter(Context context, List<MultiListMenuItem> datas) {
        super(context, datas, R.layout.item_multi_list_menu);
    }

    @Override
    protected void convert(ViewHolder holder, MultiListMenuItem item, int position) {
        holder.setText(R.id.tv_item_multiListMenu_itemName, item.getName());
        CheckBox checkBox = holder.getView(R.id.cb_item_multiListMenu_selector);
        if (item.isSelectable()) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setChecked(isChecked);
                }
            });
            checkBox.setChecked(item.isChecked());
        } else {
            checkBox.setVisibility(View.GONE);
            checkBox.setChecked(false);
        }
        if (currentPosition == position) {
            holder.getConvertView().setBackgroundColor(ArmsUtils.getColor(mContext, R.color.bg_lightGrayB));
            holder.setTextColor(R.id.tv_item_multiListMenu_itemName, ArmsUtils.getColor(mContext, R.color.MediumSeaGreen));
        } else {
            holder.getConvertView().setBackgroundColor(ArmsUtils.getColor(mContext, R.color.white));
            holder.setTextColor(R.id.tv_item_multiListMenu_itemName, ArmsUtils.getColor(mContext, R.color.light_black));
        }
        if (item.isHasSubItem()) {
            holder.setVisibility(R.id.img_item_multiListMenu_subItem, View.VISIBLE);
        } else {
            holder.setVisibility(R.id.img_item_multiListMenu_subItem, View.GONE);
        }
    }

    public void setCurrentPosition(int position) {
        currentPosition = position;
        notifyDataSetChanged();
    }

    public void setData(List<MultiListMenuItem> data) {
        if (data == null)
            return;
        if (mDatas != null) {
            mDatas.clear();
            mDatas.addAll(data);
        } else {
            mDatas = data;
        }
    }

    public List<MultiListMenuItem> getSelectItemList() {
        if (mDatas == null || mDatas.isEmpty())
            return null;
        List<MultiListMenuItem> list = new ArrayList<>();
        for (MultiListMenuItem item : mDatas) {
            if (item.isChecked())
                list.add(item);
        }
        return list;
    }
}
