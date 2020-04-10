package com.herenit.mobilenurse.custom.adapter.delegate;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;
import com.herenit.mobilenurse.custom.adapter.AssessDialogAdapter;


/**
 * Created by HouBin on 2018/9/6.
 */

public class AssessCheckBoxDelegate extends AssessBaseDelegate {

    public AssessCheckBoxDelegate(Context context, AssessDialogAdapter adapter) {
        super(context, adapter);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_assess_dialog_checkbox;
    }

    @Override
    public boolean isForViewType(AssessViewItem item, int position) {
        return AssessViewItem.ASSESS_VIEW_CKECK_BOX.equals(item.getViewName());
    }

    @Override
    public void convert(final ViewHolder holder, final AssessViewItem model, int position) {
        holder.setText(R.id.cb_item_assess_dialog_checkBox, model.getText());
        //只有需要额外操作的时候，才需要显示右侧的展示箭头
        holder.setVisible(R.id.img_assess_dialog_addHandle, (model.getSubModel() != null && model.getSubModel().size() > 0));
        if (!model.isReadOnly()) {
            holder.setOnClickListener(R.id.cb_item_assess_dialog_checkBox, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.isChecked()) {
                        holder.setChecked(R.id.cb_item_assess_dialog_checkBox, false);
                        model.setChecked(false);
                        model.clearData();
                    } else {
                        holder.setChecked(R.id.cb_item_assess_dialog_checkBox, true);
                        model.setChecked(true);
                        model.setDataValue(AssessViewItem.DATA_VALUE_TRUE);
                        model.setShowDataValue(model.getText());
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getSubModel() != null && model.getSubModel().size() > 0) {//该选项的额外操作
                        if (!model.isChecked()) {
                            Toast.makeText(mContext, "请先选中 " + model.getText() + " 项", Toast.LENGTH_SHORT).show();
                        } else {
                            showAddHandle(model);
                        }
                    }
                }
            });
        }
        holder.setChecked(R.id.cb_item_assess_dialog_checkBox, model.isChecked());
        if (!TextUtils.isEmpty(model.getShowDataValue()) && !model.getShowDataValue().equals(model.getText())) {
            holder.setVisible(R.id.tv_assess_dialog_addHandleResult, true);
            holder.setText(R.id.tv_assess_dialog_addHandleResult, model.getShowDataValue());
        } else {
            holder.setVisible(R.id.tv_assess_dialog_addHandleResult, false);
        }
    }
}
