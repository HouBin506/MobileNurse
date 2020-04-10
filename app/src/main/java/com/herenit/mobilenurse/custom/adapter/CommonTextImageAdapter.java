package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;
import android.view.View;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.common.NameCode;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/1/22 15:12
 * desc: 普通的Item为Text---Image布局的适配器
 */
@ActivityScope
public class CommonTextImageAdapter<T extends NameCode> extends CommonAdapter<T> {
    /**
     * EventBusId
     */
    private String tag;

    /**
     * @param context
     * @param data
     * @param tag
     */
    public CommonTextImageAdapter(Context context, List<T> data, String tag) {
        super(context, data, R.layout.item_common_text_image);
        this.tag = tag;
    }

    @Override
    protected void convert(ViewHolder holder, T item, int position) {
        holder.setText(R.id.tv_item_textImage_text, item.getName());
        holder.setOnClickListener(R.id.img_item_textImage_image, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventId = EventBusUtils.obtainPrivateId(tag, CommonConstant.EVENT_INTENTION_REMOVE_USER);
                EventBusUtils.post(eventId, position);
            }
        });
    }
}
