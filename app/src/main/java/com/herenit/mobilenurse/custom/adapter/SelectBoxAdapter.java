package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.common.SelectNameCode;
import com.herenit.mobilenurse.custom.extra.MultipleChoiceExtra;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/5/17 14:46
 * desc:带有选择框的Adapter
 */
public class SelectBoxAdapter extends CommonAdapter<SelectNameCode> implements MultipleChoiceExtra {
    //默认选中的Item
    private List<Integer> mDefaultChoice = new ArrayList<>();
    //选中的结果
    private List<Integer> mChoiceResult = new ArrayList<>();

    public SelectBoxAdapter(Context context, List<SelectNameCode> datas) {
        super(context, datas, R.layout.item_common_select_box_text);
    }

    @Override
    protected void convert(ViewHolder holder, SelectNameCode item, int position) {
        CheckBox checkBox = holder.getView(R.id.cb_item_commonSelectBoxText);
        TextView name = holder.getView(R.id.tv_item_commonSelectBoxText);
        checkBox.setChecked(item.isChecked());
        name.setText(item.getName());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {//Item被点击
            @Override
            public void onClick(View v) {
                if (item.isChecked()) {
                    item.setChecked(false);
                    checkBox.setChecked(false);
                    mChoiceResult.remove(Integer.valueOf(position));
                } else {
                    item.setChecked(true);
                    checkBox.setChecked(true);
                    mChoiceResult.add(position);
                }
            }
        });
    }


    /**
     * 返回被选中的Item角标列表（包括默认选中项）
     *
     * @return
     */
    @Override
    public List<Integer> getChoiceResult() {
        if (mDatas == null)
            return null;
        return mChoiceResult;
    }

    /**
     * 设置默认选中状态的Item，被设为默认项的Item，不可操作，永远都处于选中状态
     *
     * @param defaultChoice
     */
    @Override
    public void setDefaultChoice(List<Integer> defaultChoice) {
        mDefaultChoice.clear();
        if (defaultChoice != null)
            mDefaultChoice.addAll(defaultChoice);
    }
}
