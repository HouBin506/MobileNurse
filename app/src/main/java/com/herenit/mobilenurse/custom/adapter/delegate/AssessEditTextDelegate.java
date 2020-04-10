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

public class AssessEditTextDelegate implements ItemViewDelegate<AssessViewItem> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_assess_dialog_edittext;
    }

    @Override
    public boolean isForViewType(AssessViewItem item, int position) {
        return AssessViewItem.ASSESS_VIEW_EDIT_TEXT.equals(item.getViewName());
    }

    @Override
    public void convert(ViewHolder holder, final AssessViewItem model, int position) {
        String value = model.getDataValue();
        if (!TextUtils.isEmpty(value)) {
            holder.setText(R.id.et_item_assess_dialog_editText, value);
        }

//        if (AssessViewItem.DATA_TYPE_NUMBER.equals(model.getDataType())) {
//            ((EditText) holder.getView(R.id.et_item_assess_dialog_editText)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        }
        Utils.setEditTextReadOnlyStatus((EditText) holder.getView(R.id.et_item_assess_dialog_editText), !model.isReadOnly());
        holder.addTextChangedListener(R.id.et_item_assess_dialog_editText, new TextWatcher() {
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
                model.setShowDataValue(content);
            }
        });
    }
}
