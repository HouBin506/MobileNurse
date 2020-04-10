package com.herenit.mobilenurse.mvp.patient;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.di.component.DaggerPatientInfoComponent;
import com.herenit.mobilenurse.mvp.base.SwitchPatientFragment;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/3/4 10:06
 * desc: 患者详情页面载体
 */
public class PatientInfoFragment extends SwitchPatientFragment<PatientInfoPresenter> implements PatientInfoContract.View {

    private List<PatientInfoPagerFragment> mPageList = new ArrayList<>();
    private FragmentStatePagerAdapter mPagerAdapter;

    private LoadingDialog mLoadingDialog;


    /**
     * 切换了患者
     *
     * @param position
     */
    @Override
    protected void onSelectedPage(int position) {
        Sickbed sickbed = mSickbedList.get(position);
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
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerPatientInfoComponent
                .builder()
                .view(this)
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    protected void initData() {
        mPageList.clear();
        for (int x = 0; x < mSickbedList.size(); x++) {
            mPageList.add(new PatientInfoPagerFragment());
        }
        if (mSickbed != null) {
            int position = mSickbedList.indexOf(mSickbed);
            if (position != mViewPager.getCurrentItem()) {
                mViewPager.setCurrentItem(position, true);
            }
        }
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog == null)
            mLoadingDialog = createLoadingDialog();
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
    }

    @Override
    public void setData(@Nullable Object data) {

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
            if (intention.equals(CommonConstant.EVENT_INTENTION_LOAD_DATA)) {//加载数据
                boolean refresh = (boolean) event.getMessage();//是否为刷新
                mPresenter.loadPatientInfo(SickbedTemp.getInstance().getCurrentSickbed(), refresh);
            }
        } else {//公共事件消费
            if (id.equals(EventIntentionEnum.SWITCH_PATIENT.getId())) {//切换患者
                switchPatient((Sickbed) event.getMessage());
            }
        }
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

    /**
     * 显示患者详情
     *
     * @param patient
     */
    @Override
    public void showPatientInfo(PatientInfo patient) {
        PatientInfoPagerFragment page = mPageList.get(mViewPager.getCurrentItem());
        page.showPatientInfo(patient);
        page.finishRefresh();
    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_IMG;
    }

}
