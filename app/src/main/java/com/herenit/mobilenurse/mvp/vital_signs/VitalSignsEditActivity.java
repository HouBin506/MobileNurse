package com.herenit.mobilenurse.mvp.vital_signs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.mvp.IView;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsViewItem;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.custom.adapter.VitalSignsItemAdapter;
import com.herenit.mobilenurse.custom.widget.layout.SpacesItemDecoration;
import com.herenit.mobilenurse.mvp.base.BasicBusinessActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/5/21 14:16
 * desc:
 */
public class VitalSignsEditActivity extends BasicBusinessActivity implements IView {

    private List<VitalSignsViewItem> mViewItemList = new ArrayList<>();
    /**
     * 体征列表
     */
    @BindView(R2.id.rv_vitalSigns_vitalSignsList)
    RecyclerView mRv_VitalSignsList;

    @BindView(R2.id.ll_item_vitalSignsRecord_group)
    LinearLayout ll_group;//表示组
    @BindView(R2.id.ll_item_vitalSignsRecord_item)
    LinearLayout ll_item;//表示条目
    @BindView(R2.id.view_item_vitalSignsRecord_divider)
    View view_divider;//分割线
    @BindView(R2.id.tv_item_vitalSignsRecord_groupName)
    TextView tv_groupName;//组名，比如“血压”、“体温”
    @BindView(R2.id.cb_item_vitalSignsRecord_itemName)
    CheckBox cb_itemName;//体征项目名
    @BindView(R2.id.tv_item_vitalSignsRecord_itemName)
    TextView tv_itemName;//体征项目名
    @BindView(R2.id.ll_item_vitalSignsRecord_value)
    LinearLayout ll_value;//体征项的值，客户端录入的
    @BindView(R2.id.ll_item_vitalSignsRecord_specialValue)
    LinearLayout ll_specialValue;//例外值，客户端录入
    @BindView(R2.id.img_item_vitalSignsRecord_chart)
    ImageView img_chart;//趋势图标记，有历史记录且值为数字的，可以画趋势图
    @BindView(R2.id.ll_item_vitalSignsRecord_fixedTimePoint)
    LinearLayout ll_fixedTimePoint;//使用固定时间点时。显示
    @BindView(R2.id.tv_item_vitalSignsRecord_timePoint)
    TextView tv_timePoint;//该条体征录入的时间点，默认为当前时间
    @BindView(R2.id.tv_item_vitalSignsRecord_fixedTimePoint)
    TextView tv_fixed;//固定时间点，针对某些医院要求录入固定时间点
    @BindView(R2.id.img_item_vitalSignsRecord_fixedTimeClear)
    ImageView img_fixedClean;//删除固定时间点
    @BindView(R2.id.ll_item_vitalSignsRecord_memo)
    LinearLayout ll_memo;//备注，有的医院需要录入备注信息
    @BindView(R2.id.et_item_vitalSignsRecord_memo)
    EditText et_memo;//备注，有的医院需要录入备注信息
    @BindView(R2.id.ll_item_vitalSignsRecord_desc)
    LinearLayout ll_desc;//描述，显示该体征项的一些额外描述，频次、特殊说明等
    @BindView(R2.id.tv_item_vitalSignsRecord_frequency)
    TextView tv_frequency;//显示该项体征相关的频次描述
    @BindView(R2.id.tv_item_vitalSignsRecord_desc)
    TextView tv_desc;//显示该项体征相关特殊说明

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_TV;
    }


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_vital_signs_edit;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initTitle();
        Intent intent = getIntent();
        if (intent != null) {
            VitalSignsViewItem viewItem = (VitalSignsViewItem) intent.getSerializableExtra(KeyConstant.NAME_EXTRA_VITAL_SIGNS_VIEW);
            if (viewItem != null)
                mViewItemList.add(viewItem);
        }
        if (mViewItemList == null || mViewItemList.isEmpty())
            return;
        initVitalSignsView();
    }

    /**
     * 构建体征项目编辑界面
     */
    private void initVitalSignsView() {
        VitalSignsItemAdapter adapter = new VitalSignsItemAdapter(mContext, mViewItemList, CommonConstant.OPERATION_TYPE_UPDATE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRv_VitalSignsList.setLayoutManager(layoutManager);
        mRv_VitalSignsList.setAdapter(adapter);
        mRv_VitalSignsList.addItemDecoration(new SpacesItemDecoration(ArmsUtils.getDimens(mContext, R.dimen.dp_5)));
    }

    /**
     * 加载标题
     */
    private void initTitle() {
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        setTitleBarCenterText(ArmsUtils.getString(mContext, R.string.title_activity_vital_signs_edit));
        setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.btn_sure));
        setTitleBarLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleBarRightOnClickListener1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editVitalSignsFinish();
            }
        });
    }

    private void editVitalSignsFinish() {
        VitalSignsViewItem viewItem = mViewItemList.get(0);
        Intent intent = new Intent();
        intent.putExtra(KeyConstant.NAME_EXTRA_VITAL_SIGNS_VIEW, viewItem);
        setResult(VitalSignsHistoryActivity.RESULT_CODE_VITAL_SIGHS_EDIT, intent);
        finish();
    }
}
