package com.herenit.mobilenurse.mvp.vital_signs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.mvp.IView;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.mvp.base.SwitchPatientFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/4/10 10:02
 * desc: 体征Fragment
 */
public class VitalSignsRecordFragment extends SwitchPatientFragment<VitalSignsRecordPresenter> implements IView {
    List<VitalSignsRecordPagerFragment> mPagerList = new ArrayList<>();
    private FragmentStatePagerAdapter mPagerAdapter;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    protected void initData() {
        setTitleUI();
        mPagerList.clear();
        for (int x = 0; x < mSickbedList.size(); x++) {
            mPagerList.add(new VitalSignsRecordPagerFragment());
        }
        if (mSickbed != null) {
            int position = mSickbedList.indexOf(mSickbed);
            if (position != mViewPager.getCurrentItem()) {
                mViewPager.setCurrentItem(position, true);
            }
        }
        mPagerAdapter.notifyDataSetChanged();
    }

    /**
     * 设置标题的布局
     */
    private void setTitleUI() {
        setTitleBarRightBackgroundResource1(R.drawable.shape_bg_text_transparent_solid_white_stroke);
        setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.btn_save));
        //点击保存按钮，保存医嘱
        setTitleBarRightOnClickListener1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVitalSignsData();
            }
        });
    }

    /**
     * 点击保存体征
     */
    private void saveVitalSignsData() {
        if (mViewPager == null || mPagerList == null || mPagerList.size() == 0)
            return;
        VitalSignsRecordPagerFragment fragment = mPagerList.get(mViewPager.getCurrentItem());
        if (fragment != null) {
            fragment.saveVitalSignsData();
        }
    }

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_TV;
    }

    @Override
    protected void onSelectedPage(int position) {
        Sickbed sickbed = mSickbedList.get(position);
        switchPatient(sickbed);
    }

    /**
     * 切换患者
     *
     * @param sickbed
     */
    @Override
    protected void switchPatient(@NonNull Sickbed sickbed) {
        super.switchPatient(sickbed);
        int position = mSickbedList.indexOf(sickbed);
        //如果当前页面所处的角标就是要切换的患者的角标，则说明是同一个患者不需要切换
        //出现这种情况，说明是由Fragment自己左右滑动发出的Event消息调用，或者是SingleActivity患者列表选择了同一个人
        if (position != mViewPager.getCurrentItem())
            mViewPager.setCurrentItem(position, true);
    }


    @NonNull
    @Override
    protected PagerAdapter pagerAdapter() {
        if (mPagerAdapter == null) {
            mPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int i) {
                    return mPagerList.get(i);
                }

                @Override
                public int getCount() {
                    return mPagerList.size();
                }
            };
        }
        return mPagerAdapter;
    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
