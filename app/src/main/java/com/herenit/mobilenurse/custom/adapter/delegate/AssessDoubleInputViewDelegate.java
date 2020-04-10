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

public class AssessDoubleInputViewDelegate implements ItemViewDelegate<AssessViewItem> {

    public static final String INTPUT_VIEW_FLAG_LEFT = "left";
    public static final String INTPUT_VIEW_FLAG_RIGHT = "right";

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_assess_dialog_doubleinputview;
    }

    @Override
    public boolean isForViewType(AssessViewItem item, int position) {
        return AssessViewItem.ASSESS_VIEW_DOUBLE_INPUT_VIEW.equals(item.getViewName());
    }

    @Override
    public void convert(ViewHolder holder, AssessViewItem model, int position) {
        String name = model.getText();
        String unit = model.getUnit();
        String unit1 = "";//第一个输入框后面的单位
        String unit2 = "";//第二个输入框后面的单位
        String connector = "";//两个输入框的值的连接符
        String[] units = splitDoubleValueUnit(unit);
        if (units != null && units.length == 3) {
            unit1 = units[0];
            connector = units[1];
            unit2 = units[2];
        }
        String[] values = splitDoubleValue(model.getDataValue(), connector);
        String value1 = "";
        String value2 = "";
        if (values != null) {
            value1 = values[0];
            value2 = values[1];
        }
        if (!TextUtils.isEmpty(name)) {
            holder.setVisible(R.id.tv_item_Assess_dialog_doubleInput_name, true);
            holder.setText(R.id.tv_item_Assess_dialog_doubleInput_name, name);
        } else {
            holder.setVisible(R.id.tv_item_Assess_dialog_doubleInput_name, false);
        }
        if (!TextUtils.isEmpty(value1)) {
            holder.setText(R.id.et_item_Assess_dialog_doubleInput_input1, value1);
        } else {
            holder.setText(R.id.et_item_Assess_dialog_doubleInput_input1, "");
        }
        if (!TextUtils.isEmpty(value2)) {
            holder.setText(R.id.et_item_Assess_dialog_doubleInput_input2, value2);
        } else {
            holder.setText(R.id.et_item_Assess_dialog_doubleInput_input2, "");
        }
        if (!TextUtils.isEmpty(unit1)) {
            holder.setVisible(R.id.tv_item_Assess_dialog_doubleInput_unit1, true);
            holder.setText(R.id.tv_item_Assess_dialog_doubleInput_unit1, unit1);
        } else {
            holder.setVisible(R.id.tv_item_Assess_dialog_doubleInput_unit1, false);
        }
        if (!TextUtils.isEmpty(unit2)) {
            holder.setVisible(R.id.tv_item_Assess_dialog_doubleInput_unit2, true);
            holder.setText(R.id.tv_item_Assess_dialog_doubleInput_unit2, unit2);
        } else {
            holder.setVisible(R.id.tv_item_Assess_dialog_doubleInput_unit2, false);
        }
        if (!TextUtils.isEmpty(connector)) {
            holder.setVisible(R.id.tv_item_Assess_dialog_doubleInput_connector, true);
            holder.setText(R.id.tv_item_Assess_dialog_doubleInput_connector, connector);
        } else {
            holder.setVisible(R.id.tv_item_Assess_dialog_doubleInput_connector, false);
        }


        EditText editText1 = holder.getView(R.id.et_item_Assess_dialog_doubleInput_input1);
        EditText editText2 = holder.getView(R.id.et_item_Assess_dialog_doubleInput_input2);
        Utils.setEditTextReadOnlyStatus(editText1, !model.isReadOnly());
        Utils.setEditTextReadOnlyStatus(editText2, !model.isReadOnly());
//        if (AssessViewItem.DATA_TYPE_NUMBER.equals(model.getDataType())) {
//            editText1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//            editText2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        }
        holder.addTextChangedListener(R.id.et_item_Assess_dialog_doubleInput_input1,
                new DoubleInputTextWatcher(model, connector, INTPUT_VIEW_FLAG_LEFT));
        holder.addTextChangedListener(R.id.et_item_Assess_dialog_doubleInput_input2,
                new DoubleInputTextWatcher(model, connector, INTPUT_VIEW_FLAG_RIGHT));
    }

    private String[] splitDoubleValue(String value, String connector) {
        String value1 = "";
        String value2 = "";
        if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(connector) && value.contains(connector)) {
            char[] values = value.toCharArray();
            int connectorIndex = value.indexOf(connector);
            for (int x = 0; x < values.length; x++) {
                char c = values[x];
                if (x < connectorIndex)
                    value1 += c;
                else if (x > connectorIndex)
                    value2 += c;
            }
        }
        if (TextUtils.isEmpty(value1) && TextUtils.isEmpty(value2))
            return null;
        String[] valueArr = new String[2];
        valueArr[0] = value1;
        valueArr[1] = value2;
        return valueArr;
    }

    private String[] splitDoubleValueUnit(String unit) {
        if (TextUtils.isEmpty(unit))
            return null;
        return unit.split(" ");
    }

    class DoubleInputTextWatcher implements TextWatcher {
        private String inputViewFlag;
        private AssessViewItem model;
        private String connector;


        public DoubleInputTextWatcher(AssessViewItem model, String connector, String inputViewFlag) {
            this.model = model;
            this.connector = connector;
            this.inputViewFlag = inputViewFlag;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = s.toString();
            String[] values = splitDoubleValue(model.getDataValue(), connector);
            String value1 = "";
            String value2 = "";
            if (values != null) {
                value1 = values[0];
                value2 = values[1];
            }
            if (INTPUT_VIEW_FLAG_LEFT.equals(inputViewFlag)) {
                value1 = content;
            } else if (INTPUT_VIEW_FLAG_RIGHT.equals(inputViewFlag)) {
                value2 = content;
            }

            String[] units = splitDoubleValueUnit(model.getUnit());
            String unit1 = "";
            String unit2 = "";
            if (units != null && units.length == 3) {
                unit1 = units[0];
                unit2 = units[2];
            }
            String value = "";
            String showValue = "";
            if (!TextUtils.isEmpty(value1) || !TextUtils.isEmpty(value2)) {
                value = value1 + connector + value2;
                showValue = (TextUtils.isEmpty(value1) ? "" : value1 + " " + unit1) + connector + (TextUtils.isEmpty(value2) ? "" : value2 + " " + unit2);
            }
            model.setDataValue(value);
            model.setShowDataValue(showValue);
        }
    }

}
