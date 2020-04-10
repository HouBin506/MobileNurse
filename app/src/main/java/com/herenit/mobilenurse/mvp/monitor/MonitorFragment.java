package com.herenit.mobilenurse.mvp.monitor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.View;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.mvp.IView;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.MonitorBind;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.di.component.DaggerMonitorComponent;
import com.herenit.mobilenurse.di.component.DaggerPatientInfoComponent;
import com.herenit.mobilenurse.mvp.base.SwitchPatientFragment;
import com.herenit.mobilenurse.mvp.orders.OrderListPagerFragment;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/3/4 10:06
 * desc: 监护仪绑定页面载体
 */
public class MonitorFragment extends SwitchPatientFragment implements IView {

    private List<MonitorPagerFragment> mPageList = new ArrayList<>();
    private FragmentStatePagerAdapter mPagerAdapter;

    /**
     * 切换了患者
     *
     * @param position
     */
    @Override
    protected void onSelectedPage(int position) {
        Sickbed sickbed = (Sickbed) mSickbedList.get(position);
        switchPatient(sickbed);
    }

    @NonNull
    @Override
    protected FragmentStatePagerAdapter pagerAdapter() {
        if (mPagerAdapter == null) {
            mPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int i) {
                    return mPageList.get(i);
                }

                @Override
                public int getCount() {
                    return mPageList.size();
                }
            };
        }
        return mPagerAdapter;
    }


    @Override
    protected void initData() {
        initTitle();
        mPageList.clear();
        for (int x = 0; x < mSickbedList.size(); x++) {
            mPageList.add(new MonitorPagerFragment());
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
     * 初始化标题
     */
    private void initTitle() {
        setTitleBarRightBackgroundResource1(R.drawable.shape_bg_text_transparent_solid_white_stroke);
        setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.btn_binding));
        setTitleBarRightOnClickListener1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击了绑定或者解绑
                MonitorPagerFragment fragment = mPageList.get(mViewPager.getCurrentItem());
                if (fragment != null) {
                    String id = EventBusUtils.obtainPrivateId(fragment.toString(), CommonConstant.EVENT_INTENTION_ON_TITLEBAR_RIGHT_CLICK);
                    EventBusUtils.post(id, null);
                }
            }
        });
    }


    @Override
    public void setData(@Nullable Object data) {

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

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_TV;
    }

    /**
     * 设置右上角按钮文本
     *
     * @param text
     */
    public void setTitleBarRightText(String text) {
        setTitleBarRightText1(text);
    }

    /**
     * EventBus接收消息
     *
     * @param event
     */
    @Subscribe
    public void onEventBusReceive(Event event) {
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (!TextUtils.isEmpty(intention)) {//私有消费

        } else {//公共事件消费

        }
    }
}
