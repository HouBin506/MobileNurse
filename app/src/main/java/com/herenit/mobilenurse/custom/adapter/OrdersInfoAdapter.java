package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;

import com.herenit.arms.base.adapter.rv.MultiItemTypeAdapter;
import com.herenit.mobilenurse.custom.adapter.delegate.OrdersInfoItemDelegate;
import com.herenit.mobilenurse.custom.adapter.delegate.OrdersInfoActionDelegate;
import com.herenit.mobilenurse.criteria.entity.view.MultiItemDelegate;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/3/13 10:49
 * desc:医嘱详情界面列表 Adapter，包括了单组医嘱列表以及页面上医嘱列表下面的详情
 */
public class OrdersInfoAdapter extends MultiItemTypeAdapter<MultiItemDelegate> {


    public OrdersInfoAdapter(Context context, List<MultiItemDelegate> datas) {
        super(context, datas);
        addItemViewDelegate(0, new OrdersInfoItemDelegate());
        addItemViewDelegate(1, new OrdersInfoActionDelegate());
    }


}
