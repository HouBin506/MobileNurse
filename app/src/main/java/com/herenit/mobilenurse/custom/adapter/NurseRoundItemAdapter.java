package com.herenit.mobilenurse.custom.adapter;


import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herenit.arms.base.adapter.rv.CommonAdapter;
import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.MNUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewItem;
import com.herenit.mobilenurse.criteria.enums.IndicatorStatus;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.dialog.MNListPopupWindow;
import com.herenit.mobilenurse.custom.widget.input.MNInputView;
import com.herenit.mobilenurse.custom.widget.input.MNSingleInputView;
import com.herenit.mobilenurse.custom.widget.layout.MNSingleInputDialog;

import java.util.List;
import java.util.Map;

/**
 * author: HouBin
 * date: 2019/8/6 9:27
 * desc:护理巡视Adapter
 */
public class NurseRoundItemAdapter extends CommonAdapter<NurseRoundViewItem> {


    public NurseRoundItemAdapter(Context context, List<NurseRoundViewItem> datas) {
        super(context, R.layout.item_nurse_round, datas);
    }


    @Override
    protected void convert(ViewHolder holder, NurseRoundViewItem item, int position) {
        LinearLayout ll_group = holder.getView(R.id.ll_item_nurseRound_group);
        CheckBox cb_title = holder.getView(R.id.cb_item_nurseRound_title);
        LinearLayout ll_value = holder.getView(R.id.ll_item_nurseRound_value);
        LinearLayout ll_content = holder.getView(R.id.ll_item_nurseRound_content);
        TextView tv_valueName = holder.getView(R.id.tv_item_nurseRound_valueName);
        TextView tv_clear = holder.getView(R.id.tv_item_nurseRound_clear);
        if (item.isEditable()) {//可编辑状态
            holder.getConvertView().setBackgroundColor(ArmsUtils.getColor(mContext, R.color.white));
            cb_title.setTextColor(ArmsUtils.getColor(mContext, R.color.light_black));
        } else {//不可编辑
            holder.getConvertView().setBackgroundColor(ArmsUtils.getColor(mContext, R.color.bg_lightGrayB));
            cb_title.setTextColor(ArmsUtils.getColor(mContext, R.color.gray));
        }

        String itemType = item.getItemType();
        if (CommonConstant.ITEM_TYPE_GROUP.equals(itemType)) {//组
            ll_group.setVisibility(View.VISIBLE);
            ll_value.setVisibility(View.GONE);
            String itemName = item.getItemName();
            cb_title.setOnCheckedChangeListener(null);//先清空之前的监听，为了解决listView滚动时再次调用CheckBox的监听器的onCheckedChanged方法，导致选中状态被改变
            cb_title.setText(itemName);
            cb_title.setChecked(item.isChecked());
            cb_title.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setChecked(isChecked);
                    updateItemEditable(position);
                }
            });
            tv_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//点击清除按钮，清除填写的数据
                    clearGroupValue(position);
                }
            });
        } else {//值
            ll_group.setVisibility(View.GONE);
            ll_value.setVisibility(View.VISIBLE);
            tv_valueName.setText(item.getItemName());
            addValueView(ll_content, item);
        }
    }

    /**
     * 清除某一组巡视数据
     *
     * @param position
     */
    private void clearGroupValue(int position) {
        for (int x = position; x < mDatas.size(); x++) {
            NurseRoundViewItem item = mDatas.get(x);
            if (x > position && CommonConstant.ITEM_TYPE_GROUP.equals(item.getItemType()))//循环到了下一组，直接跳出循环
                break;
            item.setValue("");
        }
        notifyDataSetChanged();
    }


    @Override
    protected void convert(ViewHolder holder, Map<String, Object> stringObjectMap, int position) {

    }

    /**
     * 给巡视子项添加控件
     *
     * @param parent
     * @param item
     */
    private void addValueView(LinearLayout parent, NurseRoundViewItem item) {
        String valueViewType = item.getValueViewType();
        if (TextUtils.isEmpty(valueViewType)) {
            parent.setVisibility(View.GONE);
            return;
        }
        String[] viewDesc = MNUtils.parseStringArrayByStringFormat(item.getValueViewRule());
        if (CommonConstant.VIEW_TYPE_MN_SINGLE_INPUT_VIEW.equals(valueViewType)) {//输入框
            MNSingleInputView singleInputView = new MNSingleInputView.Builder(mContext)
                    .inputType(item.getValueDataType())
                    .onTextChangedListener(new MNInputView.OnTextChangedListener() {
                        @Override
                        public void onTextChanged(@Nullable IndicatorStatus status, String content) {
                            item.setValue(content);
                        }
                    }).content(item.getValue())
                    .prefix(viewDesc[0])
                    .suffix(viewDesc[1])
                    .useIndicatorStatus(false).build();
            singleInputView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            singleInputView.setGravity(Gravity.CENTER);
            parent.removeAllViews();
            parent.addView(singleInputView);
            singleInputView.setEnabled(item.isEditable());
        } else if (CommonConstant.VIEW_TYPE_MN_LIST_POPUP_WINDOW.equals(valueViewType)) {//下拉选择框
            List<String> dataList = MNUtils.parseStringListByFormatString(item.getValueDataList());
            if (dataList == null || dataList.isEmpty())
                return;
            MNListPopupWindow mnListPopupWindow = new MNListPopupWindow.Builder(mContext)
                    .adapter(new CommonTextAdapter(mContext, dataList))
                    .content(item.getValue())
                    .prefix(viewDesc[0])
                    .suffix(viewDesc[1])
                    .build();
            mnListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mnListPopupWindow.dismiss();
                    item.setValue(dataList.get(position));
                    notifyDataSetChanged();
                }
            });
            mnListPopupWindow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            mnListPopupWindow.setGravity(Gravity.CENTER);
            parent.removeAllViews();
            parent.addView(mnListPopupWindow);
            mnListPopupWindow.setEnabled(item.isEditable());
        } else if (CommonConstant.VIEW_TYPE_MN_SINGLE_INPUT_DIALOG.equals(valueViewType)) {//单输入框弹窗
            MNSingleInputDialog singleInputView = new MNSingleInputDialog.Builder(mContext)
                    .content(item.getValue())
                    .inputTitle(item.getItemName())
                    .prefix(viewDesc[0])
                    .suffix(viewDesc[1])
                    .build();
            singleInputView.setPositiveNegativeListener(new PositiveNegativeListener() {
                @Override
                public void onPositive(Object... backData) {
                    singleInputView.dismiss();
                    item.setValue((String) backData[0]);
                    notifyDataSetChanged();
                }

                @Override
                public void onNegative() {
                    singleInputView.dismiss();
                }
            });
            singleInputView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            singleInputView.setGravity(Gravity.CENTER);
            parent.removeAllViews();
            parent.addView(singleInputView);
            singleInputView.setEnabled(item.isEditable());
        }
    }


    /**
     * 跟新巡视列表的可编辑性，只有选中的
     *
     * @param position
     */
    private void updateItemEditable(int position) {
        boolean editable = mDatas.get(position).isChecked();
        for (int x = position; x < mDatas.size(); x++) {
            NurseRoundViewItem item = mDatas.get(x);
            if (x > position && CommonConstant.ITEM_TYPE_GROUP.equals(item.getItemType())) //已经循环进入另一组，此时停止循环
                break;
            item.setEditable(editable);//被选中了的项才可以编辑
        }
        notifyDataSetChanged();
    }
}
