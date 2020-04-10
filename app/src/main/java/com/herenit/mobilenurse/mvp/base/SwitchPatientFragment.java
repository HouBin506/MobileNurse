package com.herenit.mobilenurse.mvp.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.herenit.arms.integration.AppManager;
import com.herenit.arms.integration.EventBusManager;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.app.utils.ScreenUtils;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.enums.ModuleEnum;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.custom.adapter.SelectSickbedAdapter;
import com.herenit.mobilenurse.mvp.main.SinglePatientActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.http.POST;

/**
 * author: HouBin
 * date: 2019/3/19 16:14
 * desc:切换患者的Fragment基类，封装了切换患者的一些方法操作
 */
public abstract class SwitchPatientFragment<T extends BasePresenter> extends PagerSupportFragment<T> {
    private ListPopupWindow mLpw_selectSickbed;

    SelectSickbedAdapter mSelectSickbedAdapter;
    protected List<Sickbed> mSickbedList;
    protected Sickbed mSickbed;
    /**
     * 当前页面是否可见
     */
    protected boolean mVisible;

    /**
     * 因为左右滑动切换患者不太好监控,所以默认就是左右滑动切换。
     * 如果是扫描腕带或者手动选择患者切换，则修改成另外两种类别，当切换完之后要继续改回到滑动切换模式
     */
    protected int mSwitchType = CommonConstant.SWITCH_TYPE_SCROLL;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        mSickbedList = SickbedTemp.getInstance().getSickbedList();
        if (mSickbedList == null || mSickbedList.isEmpty())
            return;
        setView();
        switchPatient(SickbedTemp.getInstance().getCurrentSickbed());
        initData();
    }

    protected abstract void initData();

    protected void setView() {
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        getTitleBarCenterTextView().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_arrows_down_popup, 0);
        int width = ScreenUtils.getScreenwidth(mContext) / 2;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (mSelectSickbedAdapter == null)
            mSelectSickbedAdapter = new SelectSickbedAdapter(mContext, SickbedTemp.getInstance().getSickbedList());
        mLpw_selectSickbed = createListPopupWindow(mSelectSickbedAdapter, width, height, getTitleBarCenterTextView(), 0, 0, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//选择了患者
//                switchPatient(mSickbedList.get(position));
                mSwitchType = CommonConstant.SWITCH_TYPE_SELECT;
                Sickbed sickbed = mSickbedList.get(position);
                sickbed.setCurrentBabyIndex(-1);
                setCurrentSickbed(sickbed);
                mLpw_selectSickbed.dismiss();
            }
        });
        setTitleBarCenterOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//弹出下拉列表，让用户选择床位
                List<Sickbed> sickbedList = SickbedTemp.getInstance().getSickbedList();
                if (sickbedList == null || sickbedList.isEmpty())
                    return;
                mLpw_selectSickbed.show();
                mLpw_selectSickbed.getListView().setSelection(mSelectSickbedAdapter.getSelectPosition());
            }
        });

        setTitleBarLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void setCurrentSickbed(Sickbed sickbed) {
        Sickbed lastSickbed = SickbedTemp.getInstance().getCurrentSickbed();
        int lastPosition = SickbedTemp.getInstance().getSickbedPosition(lastSickbed);
        mSickbed = sickbed;
        SickbedTemp.getInstance().setCurrentSickbed(sickbed);
        int position = mSickbedList.indexOf(sickbed);
        mViewPager.setCurrentItem(position, true);
        //当扫码或者手动选择到同一个患者时候，或者是母婴关系时候，ViewPager不会去刷新，这时候我们需要手动重新加载数据
        if (lastPosition == position) {
            switchSameSickbed();
        }
    }

    /**
     * 切换到了同一个人
     */
    protected void switchSameSickbed() {

    }

    /**
     * 切换患者
     *
     * @param sickbed
     */
    protected void switchPatient(Sickbed sickbed) {
        mSelectSickbedAdapter.setSelectPosition(SickbedTemp.getInstance().getSickbedPosition(sickbed));
        mSickbed = sickbed;
        SickbedTemp.getInstance().setCurrentSickbed(sickbed);
        if (mSickbed != null)
            setTitleBarCenterText(mSickbed.getPatientTitle());
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mVisible = !hidden;
        if (!hidden) {
            switchPatient(SickbedTemp.getInstance().getCurrentSickbed());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mVisible = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mVisible = false;
    }

    /**
     * EventBus消息
     *
     * @param event
     */
    @Subscribe
    public void onSwitchPatientScan(Event event) {
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (TextUtils.isEmpty(intention)) {//未找到私有事件，故为共有消息事件
            if (id.equals(EventIntentionEnum.CODE_TYPE_PATIENT.getId())) {//扫描结果
                ScanResult scanResult = (ScanResult) event.getMessage();
                Sickbed sickbed = SickbedTemp.getInstance().getSickbed(scanResult.getPatientId(), scanResult.getVisitId());
                if (sickbed == null) {//没找到患者
                    Sickbed mother = SickbedTemp.getInstance().getMother(scanResult.getPatientId());
                    if (mother != null) {
                        mSwitchType = CommonConstant.SWITCH_TYPE_SCAN;
                        setCurrentSickbed(mother);
                        return;
                    }
                    //既不是患者也不是新生儿
                    ArmsUtils.snackbarText(ArmsUtils.getString(mContext, R.string.message_notFoundPatient));
                } else {//切换患者
//                    switchPatient(sickbed);
                    mSwitchType = CommonConstant.SWITCH_TYPE_SCAN;
                    sickbed.setCurrentBabyIndex(-1);
                    setCurrentSickbed(sickbed);
                }
            }
        }
    }

}
