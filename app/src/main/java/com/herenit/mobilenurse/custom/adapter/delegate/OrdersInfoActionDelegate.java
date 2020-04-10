package com.herenit.mobilenurse.custom.adapter.delegate;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.arms.base.adapter.rv.base.ItemViewDelegate;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.view.RvController;
import com.herenit.mobilenurse.criteria.entity.view.MultiItemDelegate;
import com.herenit.mobilenurse.custom.listener.OnActionListener;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;

/**
 * author: HouBin
 * date: 2019/3/13 13:39
 * desc: 对医嘱的额外操作列表Adapter
 */
public class OrdersInfoActionDelegate implements ItemViewDelegate<MultiItemDelegate>, MultiItemDelegate {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_common_name_value_arrow_tool;
    }

    @Override
    public boolean isForViewType(MultiItemDelegate item, int position) {
        return item instanceof RvController;
    }

    @Override
    public void convert(ViewHolder holder, MultiItemDelegate multiItemDelegateDelegate, int position) {
        if (!(multiItemDelegateDelegate instanceof RvController))
            return;
        Context context = holder.getConvertView().getContext();
        RvController item = (RvController) multiItemDelegateDelegate;
        View itemLayout = holder.getConvertView();
        TextView tv_name = holder.getView(R.id.tv_item_commonNameValue_name);
        TextView tv_value = holder.getView(R.id.tv_item_commonNameValue_value);
        ImageView img_arrow = holder.getView(R.id.img_item_commonNameValue_arrow);
        tv_name.setText(item.getName());
        tv_value.setText(item.getValue());
        if (item.isReadOnly()) {//只读
            itemLayout.setBackgroundColor(ArmsUtils.getColor(context, R.color.white));
            img_arrow.setVisibility(View.INVISIBLE);
            itemLayout.setClickable(false);
            itemLayout.setEnabled(false);
        } else {
            img_arrow.setVisibility(View.VISIBLE);
            if (item.isEditable()) {//可编辑
                itemLayout.setBackgroundColor(ArmsUtils.getColor(context, R.color.white));
                itemLayout.setClickable(true);
                itemLayout.setEnabled(true);
            } else {//不可编辑
                itemLayout.setBackgroundColor(ArmsUtils.getColor(context, R.color.bg_lightGrayR));
                itemLayout.setClickable(false);
                itemLayout.setEnabled(false);
            }
        }
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.showAction(context, new OnActionListener() {
                    @Override
                    public void onAction(Object... actionResult) {
                        tv_name.setText(item.getName());
                        tv_value.setText(item.getValue());
                    }
                });
            }
        });
    }
}
