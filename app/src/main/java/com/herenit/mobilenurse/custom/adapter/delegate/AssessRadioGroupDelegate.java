package com.herenit.mobilenurse.custom.adapter.delegate;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.MNUtils;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;
import com.herenit.mobilenurse.custom.adapter.AssessDialogAdapter;
import com.herenit.mobilenurse.custom.adapter.CommonTextAdapter;

import java.util.List;


/**
 * Created by HouBin on 2018/9/6.\
 * 下拉选择列表适配（评估用）
 */

public class AssessRadioGroupDelegate extends AssessBaseDelegate {

    public AssessRadioGroupDelegate(Context context, AssessDialogAdapter adapter) {
        super(context, adapter);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_assess_dialog_radiogroup;
    }

    @Override
    public boolean isForViewType(AssessViewItem item, int position) {
        return AssessViewItem.ASSESS_VIEW_RADIO_GROUP.equals(item.getViewName());
    }

    @Override
    public void convert(final ViewHolder holder, final AssessViewItem model, int position) {
        holder.setText(R.id.tv_item_assess_dialog_radioGroup_prefix, model.getText());
        if (!TextUtils.isEmpty(model.getUnit())) {
            holder.setVisible(R.id.tv_item_assess_dialog_radioGroup_suffix, true);
            holder.setText(R.id.tv_item_assess_dialog_radioGroup_suffix, model.getUnit());
        } else {
            holder.setVisible(R.id.tv_item_assess_dialog_radioGroup_suffix, false);
        }
        TextView tv_content = holder.getView(R.id.tv_item_assess_dialog_radioGroup_content);
        if (!TextUtils.isEmpty(model.getDataValue())) {
            tv_content.setText(model.getDataValue());
        } else {
            tv_content.setText("");
        }
        holder.setOnClickListener(R.id.tv_item_assess_dialog_radioGroup_content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> data = MNUtils.parseStringListByFormatString(model.getValueDataList());
                if (data == null || data.isEmpty())
                    return;
                ListPopupWindow mListPopupWindow = ViewUtils.createListPopupWindow(mContext, new CommonTextAdapter(mContext, data),
                        tv_content.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, tv_content, 0, 0);
                mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mListPopupWindow.dismiss();
                        String content = data.get(position);
                        model.setDataValue(content);
                        tv_content.setText(content);
                        model.setShowDataValue(content);
                    }
                });
                mListPopupWindow.show();
            }
        });
        //点击删除清空
        holder.setOnClickListener(R.id.img_item_assess_dialog_radioGroup_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_content.setText("");
                model.clearData();
            }
        });
    }

}
