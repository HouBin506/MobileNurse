package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;

import java.util.List;


/**
 * Created by HouBin on 2018/9/5.
 */

public class AssessAdapter extends BaseExpandableListAdapter {

    public static final int GROUP_TYPE_GROUP = 0;
    public static final int GROUP_TYPE_ITEM = 1;

    private List<AssessViewItem> mGroupData;
    private List<List<AssessViewItem>> mChildData;
    private Context mContext;

    public AssessAdapter(Context context, List<AssessViewItem> groupData, List<List<AssessViewItem>> childData) {
        this.mContext = context;
        this.mGroupData = groupData;
        this.mChildData = childData;
    }

    @Override
    public int getGroupCount() {
        return mGroupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
     *
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getGroupTypeCount() {
        return 2;
    }

    @Override
    public int getGroupType(int groupPosition) {
        if (AssessViewItem.MODEL_TYPE_GROUP.equals(mGroupData.get(groupPosition).getModelType()))
            return GROUP_TYPE_GROUP;
        else
            return GROUP_TYPE_ITEM;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (getGroupType(groupPosition) == GROUP_TYPE_GROUP) {
            GroupViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_assess_admission_group, parent, false);
                viewHolder = new GroupViewHolder();
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_item_assess_admission_group_title);
                viewHolder.img_arrow = (ImageView) convertView.findViewById(R.id.img_item_assess_admission_group_arrow);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (GroupViewHolder) convertView.getTag();
            }
            String title = mGroupData.get(groupPosition).getText();
            if (!TextUtils.isEmpty(title)) {
                viewHolder.tv_title.setVisibility(View.VISIBLE);
                viewHolder.tv_title.setText(title);
            } else {
                viewHolder.tv_title.setVisibility(View.GONE);
            }
            if (isExpanded)
                viewHolder.img_arrow.setImageResource(R.mipmap.ic_arrow_sub_item_drop_down);
            else
                viewHolder.img_arrow.setImageResource(R.mipmap.ic_arrow_sub_item_right);
            return convertView;
        } else {
            return convertChildView(convertView, mGroupData.get(groupPosition), parent);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        AssessViewItem model = mChildData.get(groupPosition).get(childPosition);
        return convertChildView(convertView, model, parent);
    }

    public View convertChildView(View convertView, AssessViewItem model, ViewGroup parent) {
        ChildViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_assess_admission_child, parent, false);
            viewHolder = new ChildViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_assess_admission_child_name);
            viewHolder.tv_value = (TextView) convertView.findViewById(R.id.tv_item_assess_admission_child_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(model.getText());
        if (!TextUtils.isEmpty(model.getShowDataValue())) {
            viewHolder.tv_value.setVisibility(View.VISIBLE);
            viewHolder.tv_value.setText(model.getShowDataValue());
        } else {
            viewHolder.tv_value.setVisibility(View.GONE);
        }
        return convertView;
    }

    /**
     * 要想子Item点击响应，返回true
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        TextView tv_title;
        ImageView img_arrow;
    }

    class ChildViewHolder {
        TextView tv_name;
        TextView tv_value;
    }
}
