package com.herenit.mobilenurse.custom.widget.layout;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;
import com.herenit.mobilenurse.custom.adapter.AssessAdapter;
import com.herenit.mobilenurse.custom.widget.dialog.assess.AssessDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/9/17 10:28
 * desc:移动护理特点的评估列表。可以展示入院评估、健康宣教等列表功能。并做评估操作
 */
public class MNAssessListView extends ExpandableListView {

    private Context mContext;
    private List<AssessViewItem> mAssessUIRawData = new ArrayList<>();//界面数据
    private AssessAdapter mAssessAdapter;
    private List<AssessViewItem> groupData = new ArrayList<>();//处理之后得到的组数据
    private List<List<AssessViewItem>> childData = new ArrayList<>();//处理之后得到的子数据

    public MNAssessListView(Context context) {
        super(context);
        init(context);
    }

    public MNAssessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MNAssessListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MNAssessListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * 初始话适配器等
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        mAssessAdapter = new AssessAdapter(context, groupData, childData);
        setAdapter(mAssessAdapter);
        setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                AssessViewItem assessModel = childData.get(groupPosition).get(childPosition);
                handleChildClick(assessModel);
                return true;
            }
        });
        setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (mAssessAdapter.getGroupType(groupPosition) == AssessAdapter.GROUP_TYPE_GROUP) {
                    return false;
                } else {
                    AssessViewItem model = groupData.get(groupPosition);
                    handleChildClick(model);
                    return true;
                }
            }
        });
    }

    /**
     * 评估项目列表某条目被点击
     *
     * @param model
     */
    private void handleChildClick(AssessViewItem model) {
        if (model == null || model.getSubModel() == null)
            return;
        AssessDialog dialog = new AssessDialog(mContext, model, new AssessDialog.AssessSureClickListener() {
            @Override
            public void onSureClick(AssessViewItem resultModel) {
                resultModel.setDataValueByFormula(mAssessUIRawData);
                mAssessAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelClick(AssessViewItem model) {
            }
        });
        dialog.showDialog();
    }

    /**
     * 数据改变通知
     */
    public void notifyDataSetChanged() {
        if (mAssessAdapter != null)
            mAssessAdapter.notifyDataSetChanged();
    }

    /**
     * 展示数据
     *
     * @param viewItemList
     * @param defaultDate
     */
    public void show(List<AssessViewItem> viewItemList, Date defaultDate) {
        mAssessUIRawData.clear();
        if (viewItemList != null)
            mAssessUIRawData.addAll(viewItemList);
        setDefaultDateTime(mAssessUIRawData, defaultDate);
        groupData.clear();
        childData.clear();
        if (viewItemList == null || viewItemList.size() == 0)
            return;
        for (int x = 0; x < viewItemList.size(); x++) {
            AssessViewItem model = viewItemList.get(x);
            groupData.add(model);
            if (AssessViewItem.MODEL_TYPE_GROUP.equals(model.getModelType())) {//成组
                List<AssessViewItem> groupChildData = model.getSubModel();
                childData.add(groupChildData);
            } else {
                childData.add(new ArrayList<AssessViewItem>());
            }
        }
        mAssessAdapter.notifyDataSetChanged();
        if (groupData != null && groupData.size() > 0)
            expandGroup(0);
    }

    /**
     * 给数据设置默认的时间数据
     *
     * @param modelList
     */
    private void setDefaultDateTime(List<AssessViewItem> modelList, Date defaultDate) {
        if (modelList == null)
            return;
        for (AssessViewItem model : modelList) {
            if (AssessViewItem.DATA_TYPE_DATE.equals(model.getDataType())) {
                if (TextUtils.isEmpty(model.getDataValue())) {
                    model.setDataValue(TimeUtils.getYYYYMMDDHHMM2String(defaultDate));
                    model.setShowDataValue(model.getDataValue());
                }
            }
            if (model.getSubModel() == null)
                continue;
            else
                setDefaultDateTime(model.getSubModel(), defaultDate);
        }
    }

    /**
     * 获取页面配置数据
     *
     * @return
     */
    public List<AssessViewItem> getViewDataList() {
        return mAssessUIRawData;
    }
}
