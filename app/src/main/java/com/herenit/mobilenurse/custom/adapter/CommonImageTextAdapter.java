package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;
import android.view.View;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.entity.view.ImageText;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/1/22 15:12
 * desc: 普通的Item为Text---Image布局的适配器
 */
@ActivityScope
public class CommonImageTextAdapter extends CommonAdapter<ImageText> {

    /**
     * @param context
     * @param data
     */
    public CommonImageTextAdapter(Context context, List<ImageText> data) {
        super(context, data, R.layout.item_common_image_text);
    }

    @Override
    protected void convert(ViewHolder holder, ImageText item, int position) {
        holder.setText(R.id.tv_item_imageText_text, item.getText());
        holder.setImageResource(R.id.img_item_imageText_image, item.getImageRes());
    }
}
