package com.herenit.mobilenurse.mvp.nursing_round;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.View;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.NurseRoundItem;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewGroup;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.custom.widget.dialog.NoticeDialog;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.di.component.DaggerNurseRoundComponent;
import com.herenit.mobilenurse.mvp.base.SwitchPatientFragment;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/2/18 13:58
 * desc: “护理巡视”功能 Fragment
 */
public class NurseRoundFragment extends SwitchPatientFragment<NurseRoundPresenter> implements NurseRoundContract.View {

    List<NurseRoundPagerFragment> mPagerList = new ArrayList<>();
    private FragmentStatePagerAdapter mPagerAdapter;

    private LoadingDialog mLoadingDialog;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerNurseRoundComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    protected void initData() {
        setTitleUI();
        mPagerList.clear();
        for (int x = 0; x < mSickbedList.size(); x++) {
            mPagerList.add(new NurseRoundPagerFragment());
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
     * 设置标题栏UI
     */
    private void setTitleUI() {
        setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.btn_save));
        setTitleBarRightBackgroundResource1(R.drawable.shape_bg_text_transparent_solid_white_stroke);
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        setTitleBarRightOnClickListener1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NurseRoundPagerFragment fragment = (NurseRoundPagerFragment) mPagerAdapter.getItem(mViewPager.getCurrentItem());
                if (fragment != null) {
                    EventBusUtils.post(EventBusUtils.obtainPrivateId(fragment.toString(),
                            CommonConstant.EVENT_INTENTION_ON_TITLEBAR_RIGHT_CLICK), null);
                }
            }
        });
    }

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_TV;
    }

    @Override
    protected void onSelectedPage(int position) {
        Sickbed sickbed = SickbedTemp.getInstance().getSickbedList().get(position);
        switchPatient(sickbed);
    }

    @NonNull
    @Override
    protected FragmentStatePagerAdapter pagerAdapter() {
        if (mPagerAdapter == null) {
            mPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
                @Override
                public int getCount() {
                    return mPagerList.size();
                }

                @Override
                public Fragment getItem(int i) {
                    return mPagerList.get(i);
                }
            };
        }
        return mPagerAdapter;
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

    /**
     * 完成获取护理巡视数据列表
     *
     * @param nurseRoundView
     */
    @Override
    public void loadNurseRoundListSuccess(NurseRoundViewGroup nurseRoundView) {
        NurseRoundPagerFragment fragment = (NurseRoundPagerFragment) mPagerAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment != null) {
            EventBusUtils.post(EventBusUtils.obtainPrivateId(fragment.toString(),
                    CommonConstant.EVENT_INTENTION_UPDATE_NURSE_ROUND), nurseRoundView);
        }
    }

    /**
     * EventBus消息
     *
     * @param event
     */
    @Subscribe
    public void onEventReceive(Event event) {
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (TextUtils.isEmpty(intention)) {//未找到私有事件，故为共有消息事件
            if (id.equals(EventIntentionEnum.CODE_TYPE_ORDER.getId()) && mVisible) {//扫描到医嘱条码
                ScanResult result = (ScanResult) event.getMessage();
                String patientId = result.getPatientId();
                String visitId = result.getVisitId();
                if (!SickbedTemp.getInstance().isCurrentPatient(patientId, visitId)) {//不是当前患者的医嘱
                    showErrorToast(ArmsUtils.getString(mContext, R.string.message_notCurrentPatientOrders));
                    return;
                }
                //发送消息，通知具体的巡视页面去操作
                NurseRoundPagerFragment fragment = (NurseRoundPagerFragment) mPagerAdapter.getItem(mViewPager.getCurrentItem());
                if (fragment != null) {
                    EventBusUtils.post(EventBusUtils.obtainPrivateId(fragment.toString(),
                            CommonConstant.EVENT_INTENTION_INFUSION_ROUND), result);
                }
            }
        } else {
            if (intention.equals(CommonConstant.EVENT_INTENTION_LOAD_DATA)) {//加载数据
                boolean refresh = (boolean) event.getMessage();//是否刷新
                mPresenter.loadNurseRoundItemList(refresh);
            } else if (intention.equals(CommonConstant.EVENT_INTENTION_COMMIT_NURSE_ROUND)) {//提交护理巡视数据
                List<NurseRoundItem> nurseRoundDataList = (List<NurseRoundItem>) event.getMessage();
                mPresenter.commitNurseRoundData(nurseRoundDataList);
            }
        }
    }

    @Override
    public void showMessage(@NonNull String message) {
        showToast(message);
    }

    @Override
    public void showError(@NonNull String title, @Nullable String message) {
        NoticeDialog dialog = createErrorNoNegativeNoticeDialog(title, message);
        dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
            @Override
            public void onPositive() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
