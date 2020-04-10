package com.herenit.mobilenurse.mvp.assess.health_edu;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.MultiListMenuItem;
import com.herenit.mobilenurse.criteria.entity.view.MultiItemDelegate;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.mvp.assess.health_edu.history.HealthEduHistoryActivity;
import com.herenit.mobilenurse.mvp.base.SwitchFragmentPagerPatientActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/8/27 10:50
 * desc: 健康宣教页面
 */
public class HealthEduActivity extends SwitchFragmentPagerPatientActivity {
    private List<HealthEduPagerFragment> mPagerList = new ArrayList<>();
    //操作类型，新增、更新
    private int mOperationType = CommonConstant.OPERATION_TYPE_PERSIST;
    private List<MultiListMenuItem> mSelectedItemList = new ArrayList<>();

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            //默认为新增
            mOperationType = intent.getIntExtra(KeyConstant.NAME_EXTRA_OPERATION_TYPE, CommonConstant.OPERATION_TYPE_PERSIST);
            //如果是修改，则获取之前选择的项目列表
            List<MultiListMenuItem> selectedItemList = (List<MultiListMenuItem>) intent.getSerializableExtra(KeyConstant.NAME_EXTRA_SELECTED_ITEM_LIST);
            mSelectedItemList.clear();
            if (selectedItemList != null)
                mSelectedItemList.addAll(selectedItemList);
        }
        initView();
    }


    private void initView() {
        //TODO 因为涉及到多级列表滑动，所以禁止ViewPager滑动
        mViewPager.setCanScroll(false);
        setTitleBarLeftText(ArmsUtils.getString(mContext, R.string.title_module_healthEducation));
        setTitleBarLeftTextDrawableLeft(R.mipmap.ic_arrow_left_back);
        setTitleBarRightBackgroundResource1(R.drawable.shape_bg_text_transparent_solid_white_stroke);
        setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.btn_historyRecord));
        setTitleBarRightOnClickListener1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看历史记录
                Intent intent = new Intent(mContext, HealthEduHistoryActivity.class);
                intent.putExtra(KeyConstant.NAME_EXTRA_SICKBED, mSickbed);
                ArmsUtils.startActivity(intent);
            }
        });
        if (mOperationType == CommonConstant.OPERATION_TYPE_PERSIST) {//新增界面显示历史记录
            setTitleBarRightVisibility1(true);
        } else if (mOperationType == CommonConstant.OPERATION_TYPE_UPDATE) {//更新界面不可跳入历史记录
            setTitleBarRightVisibility1(false);
            setSickbedSwitchable(false);//设置不可切换患者
        }
    }

    @Override
    protected List<? extends Fragment> initFragmentPagerList(int pagerListSize) {
        mPagerList.clear();
        for (int x = 0; x < pagerListSize; x++) {
            mPagerList.add(new HealthEduPagerFragment());
        }
        return mPagerList;
    }

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.TV_TV_TV;
    }

    /**
     * 返货要修改的数据中，之前选择的宣教项目列表
     *
     * @return
     */
    public List<MultiListMenuItem> getOldSelectedItemList() {
        return mSelectedItemList;
    }

    /**
     * 返回当前的操作模式，是新增还是修改
     * 如果是新增，则选择完毕要进入新的宣教内容界面，
     * 如果是修改，要返回之前的宣教内容界面，并将重新选择的数据带入
     *
     * @return
     */
    public int getOperationType() {
        return mOperationType;
    }
}
