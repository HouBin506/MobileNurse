package com.herenit.mobilenurse.mvp.exam.report;

import android.support.v4.app.Fragment;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.mvp.base.SwitchFragmentPagerPatientActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/8/27 10:50
 * desc: 检查报告列表页面
 */
public class ExamReportActivity extends SwitchFragmentPagerPatientActivity {

    List<ExamReportPagerFragment> mPagerList = new ArrayList<>();

    @Override
    protected void initData() {
        initUI();
    }

    private void initUI() {
        setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.title_module_examReport));
    }


    @Override
    protected List<? extends Fragment> initFragmentPagerList(int pagerListSize) {
        mPagerList.clear();
        for (int x = 0; x < pagerListSize; x++) {
            mPagerList.add(new ExamReportPagerFragment());
        }
        return mPagerList;
    }

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_TV;
    }

}
