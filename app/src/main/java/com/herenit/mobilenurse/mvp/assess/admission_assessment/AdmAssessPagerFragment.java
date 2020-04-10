package com.herenit.mobilenurse.mvp.assess.admission_assessment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.AssessModel;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.SummaryDataModel;
import com.herenit.mobilenurse.criteria.entity.submit.AssessParam;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.widget.layout.MNAssessListView;
import com.herenit.mobilenurse.mvp.assess.AssessHelper;
import com.herenit.mobilenurse.mvp.base.BasicCommonFragment;
import com.herenit.mobilenurse.mvp.base.SwitchFragmentPagerPatientActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/8/15 14:40
 * desc:入院评估页面
 */
public class AdmAssessPagerFragment extends BasicCommonFragment {

    @BindView(R2.id.srl_admAssess_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R2.id.tv_admAssess_recordTime)
    TextView mTv_recordTime;//记录时间

    @BindView(R2.id.elv_admAssess_listView)
    MNAssessListView mElv_assessList;//评估列表
    private Date mCurrentTime = TimeUtils.getCurrentDate();

    private Sickbed mSickbed;

    private AssessParam mAssessParam = new AssessParam();

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_adm_assess_pager, container, false);
        return contentView;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mSickbed = ((SwitchFragmentPagerPatientActivity) getActivity()).currentSickbed();
        initView();
        //加载所有数据
        String id = EventBusUtils.obtainPrivateId(getActivity().toString(), CommonConstant.EVENT_INTENTION_LOAD_DATA);
        EventBusUtils.post(id, false);
    }

    private void initView() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                //刷新要加载所有数据
                String id = EventBusUtils.obtainPrivateId(getActivity().toString(), CommonConstant.EVENT_INTENTION_LOAD_DATA);
                EventBusUtils.post(id, true);
            }
        });
        mTv_recordTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mCurrentTime);
                new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        mCurrentTime = date;
                        mTv_recordTime.setText(TimeUtils.getYYYYMMDDHHMMString(mCurrentTime));
                    }
                }).setType(new boolean[]{true, true, true, true, true, false})
                        .setDate(calendar)
                        .isDialog(false)
                        .build().show();
            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    /**
     * 数据展示
     *
     * @param model
     */
    private void showData(AssessModel model) {
        mCurrentTime = TimeUtils.getCurrentDate();
        mTv_recordTime.setText(TimeUtils.getYYYYMMDDHHMMString(mCurrentTime));
        mRefreshLayout.finishRefresh();
        List<AssessViewItem> viewList = model.getViewList();
        List<SummaryDataModel> dataList = model.getDataList();
        AssessHelper.resetAssessParam(mAssessParam, mSickbed, dataList);
        mElv_assessList.show(AssessHelper.buildAssessViewItemListBySummaryData(viewList, dataList), mCurrentTime);
    }


    /**
     * 保存入院评估数据
     */
    private void saveAdmissionAssessData() {
        //提交入院评估数据
        String id = EventBusUtils.obtainPrivateId(getActivity().toString(), CommonConstant.EVENT_INTENTION_COMMIT_ADMISSION_ASSESSMENT_DATA);
        AssessHelper.buildAssessParamByAssessViewList(mAssessParam, mElv_assessList.getViewDataList(), mCurrentTime);
        EventBusUtils.post(id, mAssessParam);
    }


    /**
     * 接收EventBus消息
     *
     * @param event
     */
    @Subscribe
    public void onReceiveEvent(Event event) {
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (!TextUtils.isEmpty(intention)) {//私有事件消费
            if (CommonConstant.EVENT_INTENTION_ON_TITLEBAR_RIGHT_CLICK.equals(intention)) {//点击了保存按钮
                //此处准备好数据，要保存了
                saveAdmissionAssessData();
            } else if (CommonConstant.EVENT_INTENTION_UPDATE_DATA.equals(intention)) {//更新数据
                showData((AssessModel) event.getMessage());
            }
        } else {//公共事件

        }
    }

}
