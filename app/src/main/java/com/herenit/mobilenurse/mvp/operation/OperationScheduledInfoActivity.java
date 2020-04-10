package com.herenit.mobilenurse.mvp.operation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.herenit.arms.mvp.IView;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.OperationScheduled;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.mvp.base.BasicBusinessActivity;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/5/23 14:04
 * desc:手术安排详情页面
 */
public class OperationScheduledInfoActivity extends BasicBusinessActivity implements IView {
    @BindView(R2.id.tv_bedLabel)
    TextView tv_bedLabel;
    @BindView(R2.id.tv_name)
    TextView tv_name;
    @BindView(R2.id.tv_scheduledDateTime)
    TextView tv_scheduledDateTime;
    @BindView(R2.id.tv_ackIndicator)
    TextView tv_ackIndicator;
    @BindView(R2.id.tv_operatorDoctor)
    TextView tv_operatorDoctor;
    @BindView(R2.id.tv_diagBeforeOperation)
    TextView tv_diagBeforeOperation;
    @BindView(R2.id.tv_operationName)
    TextView tv_operationName;
    @BindView(R2.id.tv_operatingRoomName)
    TextView tv_operatingRoomName;
    @BindView(R2.id.tv_operationSequence)
    TextView tv_operationSequence;
    @BindView(R2.id.tv_operatingScale)
    TextView tv_operatingScale;
    @BindView(R2.id.tv_enteredBy)
    TextView tv_enteredBy;
    @BindView(R2.id.tv_assistantNurse)
    TextView tv_assistantNurse;
    @BindView(R2.id.tv_anaesthesiaMethod)
    TextView tv_anaesthesiaMethod;
    @BindView(R2.id.tv_anesthesiaDoctor)
    TextView tv_anesthesiaDoctor;
    @BindView(R2.id.tv_bloodDoctor)
    TextView tv_bloodDoctor;
    @BindView(R2.id.tv_operationNurse)
    TextView tv_operationNurse;
    @BindView(R2.id.tv_supplyNurse)
    TextView tv_supplyNurse;
    @BindView(R2.id.tv_emergency)
    TextView tv_emergency;//急诊

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_NULL;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_operation_scheduled_info;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        Intent intent = getIntent();
        if (intent != null) {
            OperationScheduled operationScheduled = (OperationScheduled) intent.getSerializableExtra(KeyConstant.NAME_EXTRA_OPERATION_SCHEDULED);
            showOperationScheduledInfo(operationScheduled);
        }
    }

    /**
     * 设置标题栏
     */
    private void initTitleBar() {
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        setTitleBarCenterText(ArmsUtils.getString(mContext, R.string.title_operation_scheduled_info));
        setTitleBarLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 显示手术安排详细信息
     *
     * @param operationScheduled
     */
    private void showOperationScheduledInfo(OperationScheduled operationScheduled) {
        if (operationScheduled == null)
            return;
        tv_bedLabel.setText(operationScheduled.getBedLabel());
        tv_name.setText(operationScheduled.getName());
        if (operationScheduled.getScheduledDateTime() != null)
            tv_scheduledDateTime.setText(TimeUtils.getYYYYMMDDHHMMString(operationScheduled.getScheduledDateTime()));
        else
            tv_scheduledDateTime.setText("");
        tv_ackIndicator.setText(operationScheduled.getAckIndicator());
        tv_operatorDoctor.setText(operationScheduled.getOperatorDoctor());
        tv_diagBeforeOperation.setText(operationScheduled.getDiagBeforeOperation());
        tv_operationName.setText(operationScheduled.getOperationName());
        tv_operatingRoomName.setText(operationScheduled.getOperatingRoomName());
        tv_operationSequence.setText(operationScheduled.getOperationSequence());
        tv_operatingScale.setText(operationScheduled.getOperatingScale());
        tv_enteredBy.setText(operationScheduled.getEnteredBy());
        tv_assistantNurse.setText(operationScheduled.getAssistantNurse());
        tv_anaesthesiaMethod.setText(operationScheduled.getAnaesthesiaMethod());
        tv_anesthesiaDoctor.setText(operationScheduled.getAnesthesiaDoctor());
        tv_bloodDoctor.setText(operationScheduled.getBloodDoctor());
        tv_operationNurse.setText(operationScheduled.getOperationNurse());
        tv_supplyNurse.setText(operationScheduled.getSupplyNurse());
        tv_emergency.setText(operationScheduled.getEmergencyIndicator());

    }

}
