package com.herenit.mobilenurse.criteria.entity.view;

import com.herenit.arms.base.adapter.rv.base.ItemViewDelegate;
import com.herenit.mobilenurse.custom.adapter.OrdersInfoAdapter;

/**
 * author: HouBin
 * date: 2019/3/13 11:21
 * desc: 没有其他作用，为了适配多类型的RecyclerView的Item显示，
 * 如果当前的Adapter要显示多种类型的Item，不同种类的Item对应的实体bean类不相同，
 * 为了避免类型不同导致代码报错，统一继承{@link MultiItemDelegate} 到每个具体子Adapter
 * 中处理时，根据类型做判断显示
 */
public interface MultiItemDelegate {
}
