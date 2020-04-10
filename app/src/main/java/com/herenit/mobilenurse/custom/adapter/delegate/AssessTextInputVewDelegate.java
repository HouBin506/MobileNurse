package com.herenit.mobilenurse.custom.adapter.delegate;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.arms.base.adapter.rv.base.ItemViewDelegate;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.Utils;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;


/**
 * Created by HouBin on 2018/9/6.
 */

public class AssessTextInputVewDelegate implements ItemViewDelegate<AssessViewItem> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_assess_dialog_textinputview;
    }

    @Override
    public boolean isForViewType(AssessViewItem item, int position) {
        return AssessViewItem.ASSESS_VIEW_TEXT_INPUT_VIEW.equals(item.getViewName());
    }

    @Override
    public void convert(ViewHolder holder, final AssessViewItem model, int position) {
        String name = model.getText();
        String value = model.getDataValue();
        String unit = model.getUnit();
        if (!TextUtils.isEmpty(name)) {
            holder.setVisible(R.id.tv_item_assess_dialog_textInputView_name, true);
            holder.setText(R.id.tv_item_assess_dialog_textInputView_name, name);
        } else {
            holder.setVisible(R.id.tv_item_assess_dialog_textInputView_name, false);
        }
        if (!TextUtils.isEmpty(value)) {
            holder.setText(R.id.et_item_assess_dialog_textInputView_content, value);
        }
        Utils.setEditTextReadOnlyStatus((EditText) holder.getView(R.id.et_item_assess_dialog_textInputView_content), !model.isReadOnly());
//        if (AssessViewItem.DATA_TYPE_NUMBER.equals(model.getDataType())) {
//            ((EditText) holder.getView(R.id.et_item_assess_dialog_textInputView_content)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        }
        holder.addTextChangedListener(R.id.et_item_assess_dialog_textInputView_content, new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                model.setDataValue(content);
                if (!TextUtils.isEmpty(content))
                    model.setShowDataValue(content + " " + model.getUnit());
                else
                    model.setShowDataValue("");
            }
        });
        if (!TextUtils.isEmpty(unit)) {
            holder.setVisible(R.id.tv_item_assess_dialog_textInputView_unit, true);
            holder.setText(R.id.tv_item_assess_dialog_textInputView_unit, unit);
        } else {
            holder.setVisible(R.id.tv_item_assess_dialog_textInputView_unit, false);
        }
    }
}
