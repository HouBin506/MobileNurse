package com.herenit.mobilenurse.mvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.app.utils.ScreenUtils;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.adapter.SelectSickbedAdapter;
import com.herenit.mobilenurse.custom.widget.layout.NoCacheViewPager;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/8/15 10:42
 * desc:切换患者的Activity基类，封装了切换患者的一些方法.以Fragment为Pager左右滑动切换页面
 */
public abstract class SwitchFragmentPagerPatientActivity<T extends BasePresenter> extends BasicBusinessActivity<T> {
    //切换患者的下拉列表
    private ListPopupWindow mLpw_selectSickbed;
    //切换患者的Adapter
    SelectSickbedAdapter mSelectSickbedAdapter;
    protected List<Sickbed> mSickbedList;//患者列表
    protected Sickbed mSickbed;//当前患者

    @BindView(R2.id.vp_switchPatient)
    protected NoCacheViewPager mViewPager;

    private FragmentStatePagerAdapter mPagerAdapter;
    private List<? extends Fragment> mPagerList;

    /**
     * 患者是否可切换。某些特殊情况，不允许切换患者
     */
    private boolean mSwitchable = true;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_switch_patient;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        mSickbedList = SickbedTemp.getInstance().getSickbedList();
        if (mSickbedList == null || mSickbedList.isEmpty())
            return;
        mPagerList = initFragmentPagerList(mSickbedList.size());
        mSickbed = SickbedTemp.getInstance().getCurrentSickbed();
        setView();
        int position = SickbedTemp.getInstance().getSickbedPosition(mSickbed);
        if (position == 0)//因为ViewPager初始化默认0为第一项，所以这里只能手动调用切换患者
            switchPatient(mSickbed);
        else
            switchPager(position);
        initData();
    }

    /**
     * 提供给子类初始化数据
     */
    protected abstract void initData();

    protected void setView() {
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        int width = ScreenUtils.getScreenwidth(mContext) / 2;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (mSelectSickbedAdapter == null)
            mSelectSickbedAdapter = new SelectSickbedAdapter(mContext, mSickbedList);
        mLpw_selectSickbed = createListPopupWindow(mSelectSickbedAdapter, width, height, getTitleBarCenterTextView(), 0, 0, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//选择了患者
                switchPager(position);
                mLpw_selectSickbed.dismiss();
            }
        });
        getTitleBarCenterTextView().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_arrows_down_popup, 0);
        setTitleBarCenterOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//弹出下拉列表，让用户选择床位
                mLpw_selectSickbed.show();
                mLpw_selectSickbed.getListView().setSelection(mSelectSickbedAdapter.getSelectPosition());
            }
        });

        setTitleBarLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //ViewPager页面切换
        mPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mPagerList.get(i);
            }

            @Override
            public int getCount() {
                return mPagerList.size();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(new NoCacheViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchPatient(mSickbedList.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 切换页面
     *
     * @param position
     */
    protected void switchPager(int position) {
        mViewPager.setCurrentItem(position, true);
        mPagerAdapter.notifyDataSetChanged();
    }

    /**
     * 切换患者
     *
     * @param sickbed
     */
    protected void switchPatient(Sickbed sickbed) {
        mSelectSickbedAdapter.setSelectPosition(SickbedTemp.getInstance().getSickbedPosition(sickbed));
        mSickbed = sickbed;
        if (mSickbed != null)
            setTitleBarCenterText(mSickbed.getPatientTitle());
    }

    /**
     * 初始化页面
     *
     * @param pagerListSize
     * @return
     */
    protected abstract List<? extends Fragment> initFragmentPagerList(int pagerListSize);

    /**
     * EventBus消息
     *
     * @param event
     */
    @Subscribe
    public void onReceiveEvent(Event event) {
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (TextUtils.isEmpty(intention)) {//未找到私有事件，故为共有消息事件
            if (id.equals(EventIntentionEnum.CODE_TYPE_PATIENT.getId())) {//扫描结果
                if (!mSwitchable) {//当前模式不允许切换患者
                    ArmsUtils.snackbarText(ArmsUtils.getString(mContext, R.string.message_canNotSwitchPatient));
                    return;
                }
                ScanResult scanResult = (ScanResult) event.getMessage();
                Sickbed sickbed = SickbedTemp.getInstance().getSickbed(scanResult.getPatientId(), scanResult.getVisitId());
                if (sickbed == null) {//没找到患者
                    ArmsUtils.snackbarText(ArmsUtils.getString(mContext, R.string.message_notFoundPatient));
                } else {//切换患者
                    //切换页面
                    switchPager(SickbedTemp.getInstance().getSickbedPosition(sickbed));
                }
            }
        }
    }

    public Sickbed currentSickbed() {
        return mSickbed;
    }

    private void setTitleBarSwitchPatient() {
        setTitleBarCenterEnabled(mSwitchable);
        if (mSwitchable) {//患者可切换
            getTitleBarCenterTextView().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_arrows_down_popup, 0);
            setTitleBarCenterOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//弹出下拉列表，让用户选择床位
                    mLpw_selectSickbed.show();
                    mLpw_selectSickbed.getListView().setSelection(mSelectSickbedAdapter.getSelectPosition());
                }
            });
        } else {
            getTitleBarCenterTextView().setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    protected void setSickbedSwitchable(boolean switchable) {
        mSwitchable = switchable;
        setTitleBarSwitchPatient();
    }
}
