package com.herenit.mobilenurse.mvp.assess.admission_assessment;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.AssessModel;
import com.herenit.mobilenurse.criteria.entity.submit.AssessParam;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.di.component.DaggerAdmAssessComponent;
import com.herenit.mobilenurse.mvp.base.SwitchFragmentPagerPatientActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/2/18 13:58
 * desc: “入院评估”功能 Activity
 */
public class AdmAssessActivity extends SwitchFragmentPagerPatientActivity<AdmAssessPresenter> implements AdmAssessContract.View {

    List<AdmAssessPagerFragment> mPagerList = new ArrayList<>();

    private LoadingDialog mLoadingDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAdmAssessComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.TV_TV_TV;
    }

    @Override
    protected void initData() {
//        setTitleBarRightText2(ArmsUtils.getString(mContext, R.string.title_module_admissionAssessment));
        setTitleBarLeftText(ArmsUtils.getString(mContext, R.string.title_module_admissionAssessment));
        setTitleBarLeftTextDrawableLeft(R.mipmap.ic_arrow_left_back);
        setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.btn_save));
        setTitleBarRightBackgroundResource1(R.drawable.shape_bg_text_transparent_solid_white_stroke);
        setTitleBarRightOnClickListener1(new View.OnClickListener() {//点击保存按钮
            @Override
            public void onClick(View v) {
                //点击保存按钮
                String id = EventBusUtils.obtainPrivateId(mPagerList.get(mViewPager.getCurrentItem()).toString(), CommonConstant.EVENT_INTENTION_ON_TITLEBAR_RIGHT_CLICK);
                EventBusUtils.post(id, null);
            }
        });
    }

    @Override
    protected List<AdmAssessPagerFragment> initFragmentPagerList(int pagerListSize) {
        mPagerList.clear();
        for (int x = 0; x < pagerListSize; x++) {
            mPagerList.add(new AdmAssessPagerFragment());
        }
        return mPagerList;
    }

    /**
     * 接收EventBus消息
     *
     * @param event
     */
    @Subscribe
    public void onReceiveEvent(Event event) {
        super.onReceiveEvent(event);
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (!TextUtils.isEmpty(intention)) {//私有事件消费
            if (CommonConstant.EVENT_INTENTION_LOAD_DATA.equals(intention)) {//加载数据
                boolean refresh = (boolean) event.getMessage();
                mPresenter.getAdmissionAssessModel(mSickbed.getPatientId(), mSickbed.getVisitId(), refresh);
            } else if (CommonConstant.EVENT_INTENTION_COMMIT_ADMISSION_ASSESSMENT_DATA.equals(intention)) {//提交入院评估数据
                AssessParam param = (AssessParam) event.getMessage();
                mPresenter.commitAdmissionAssessmentData(param);
            }
        } else {//公共事件

        }
    }

    /**
     * 获取入院-评估数据成功
     *
     * @param model
     */
    @Override
    public void getAdmissionAssessModelSuccess(AssessModel model) {
        String id = EventBusUtils.obtainPrivateId(mPagerList.get(mViewPager.getCurrentItem()).toString(), CommonConstant.EVENT_INTENTION_UPDATE_DATA);
        EventBusUtils.post(id, model);
    }

    /**
     * 保存入院评估数据成功
     */
    @Override
    public void saveAdmissionAssessDataSuccess() {
        showMessage(ArmsUtils.getString(mContext, R.string.message_saveSuccess));
        mPresenter.getAdmissionAssessModel(mSickbed.getPatientId(), mSickbed.getVisitId(), false);
    }

    @Override
    public void showError(@NonNull String message) {
        showErrorToast(message);
    }

    @Override
    public void showMessage(@NonNull String message) {
        showToast(message);
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
}
