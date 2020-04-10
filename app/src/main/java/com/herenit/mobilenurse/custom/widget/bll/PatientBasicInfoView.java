package com.herenit.mobilenurse.custom.widget.bll;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.entity.Newborn;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.SickbedFlagEnum;
import com.herenit.mobilenurse.custom.extra.WidgetExtra;
import com.herenit.mobilenurse.datastore.tempcache.DictTemp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 患者基本信息控键
 */
public class PatientBasicInfoView extends LinearLayout implements WidgetExtra {
    private Context mContext;
    private Unbinder mUnbinder;

    /**
     * 新生儿信息布局
     */
    @BindView(R2.id.ll_newbornBasicInfo)
    LinearLayout mLl_newbornBasicInfo;
    /**
     * 新生儿床表号+名称
     */
    @BindView(R2.id.tv_newbornBasicInfo_LabelAndName)
    TextView mTv_babyLabelAndName;
    /**
     * 新生儿ID
     */
    @BindView(R2.id.tv_newbornBasicInfo_babyId)
    TextView mTv_babyId;
    /**
     * 新生儿性别
     */
    @BindView(R2.id.tv_newbornBasicInfo_babySex)
    TextView mTv_babySex;
    /**
     * 新生儿出生时间
     */
    @BindView(R2.id.tv_newbornBasicInfo_birthDateTime)
    TextView mTv_babyBirthDateTime;


    //床标号+姓名
    @BindView(R2.id.tv_patientBasicInfo_LabelAndName)
    TextView mTv_labelAndName;
    //患者Id
    @BindView(R2.id.tv_patientBasicInfo_patientId)
    TextView mTv_patientId;
    //年龄
    @BindView(R2.id.tv_patientBasicInfo_age)
    TextView mTv_age;
    //性别
    @BindView(R2.id.tv_patientBasicInfo_sex)
    TextView mTv_sex;
    //费别
    @BindView(R2.id.tv_patientBasicInfo_chargeType)
    TextView mTv_chargeType;
    //入院
    @BindView(R2.id.tv_patientBasicInfo_admission)
    TextView mTv_admission;
    //身份
    @BindView(R2.id.tv_patientBasicInfo_identity)
    TextView mTv_identity;
    //产生费用
    @BindView(R2.id.tv_patientBasicInfo_totalCosts)
    TextView mTv_totalCost;
    //电话号码
    @BindView(R2.id.tv_patientBasicInfo_telephone)
    TextView mTv_telephone;
    //病区
    @BindView(R2.id.tv_patientBasicInfo_wardName)
    TextView mTv_wardName;
    //护理等级
    @BindView(R2.id.tv_patientBasicInfo_nursingClass)
    TextView mTv_nursingClass;
    //病情
    @BindView(R2.id.img_patientBasicInfo_patientCondition)
    ImageView mImg_patientCondition;

    public PatientBasicInfoView(Context context) {
        super(context);
        init(context);
    }

    public PatientBasicInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PatientBasicInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PatientBasicInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_patient_basic_info, this);
        mUnbinder = ButterKnife.bind(this, contentView);
    }

    /**
     * 设置患者
     *
     * @param patient 患者信息
     * @param newborn 患者新生儿信息，这里显示，某一个新生儿的信息
     */
    public void setPatient(PatientInfo patient, @Nullable Newborn newborn) {
        if (patient == null)
            return;
        try {
            if (newborn == null) {
                mLl_newbornBasicInfo.setVisibility(GONE);
            } else {//新生儿信息
                mLl_newbornBasicInfo.setVisibility(VISIBLE);
                mTv_babyLabelAndName.setText(patient.getBedLabel() + " " + newborn.getName());
                mTv_babyId.setText(newborn.getBabyId());
                mTv_babySex.setText(newborn.getSex());
                if (newborn.getDateOfBirth() != null)
                    mTv_babyBirthDateTime.setText(TimeUtils.getYYYYMMDDHHMMSSString(newborn.getDateOfBirth()));
            }


            //患者信息
            mTv_labelAndName.setText(patient.getBedLabel() + " " + patient.getPatientName());
            if (!TextUtils.isEmpty(patient.getPatientAge()))
                mTv_age.setText(patient.getPatientAge());
            String patientCondition = DictTemp.getInstance().getPatientConditionNameByCode(patient.getPatientCondition());
            if (TextUtils.isEmpty(patientCondition))
                mImg_patientCondition.setVisibility(View.GONE);
            else if (SickbedFlagEnum.isDangerCondition(patientCondition)) {//病危
                mImg_patientCondition.setVisibility(View.VISIBLE);
                mImg_patientCondition.setImageResource(R.mipmap.ic_bed_flag_danger);
            } else if (SickbedFlagEnum.isIntensiveCondition(patientCondition)) {//病重
                mImg_patientCondition.setVisibility(View.VISIBLE);
                mImg_patientCondition.setImageResource(R.mipmap.ic_bed_flag_intensive);
            } else {
                mImg_patientCondition.setVisibility(View.GONE);
            }
            //显示护理等级
            String nursingClass = DictTemp.getInstance().getNursingClassNameByCode(patient.getNursingClass());
            if (TextUtils.isEmpty(nursingClass)) {
                mTv_nursingClass.setVisibility(View.GONE);
            } else {
                @ColorRes int colorRes = SickbedFlagEnum.getNursingClassColorResByName(nursingClass);
                mTv_nursingClass.setText(nursingClass);
                mTv_nursingClass.setTextColor(SickbedFlagEnum.getNursingClassTextColorByName(nursingClass));
                if (colorRes == R.color.bg_light_gray) {
                    mTv_nursingClass.setBackgroundResource(R.drawable.shape_bg_text_light_gray);
                } else if (colorRes == R.color.nursingClass_intensive) {
                    mTv_nursingClass.setBackgroundResource(R.drawable.shape_bg_nursing_class_intensive);
                } else if (colorRes == R.color.nursingClass_special) {
                    mTv_nursingClass.setBackgroundResource(R.drawable.shape_bg_nursing_class_special);
                } else if (colorRes == R.color.nursingClass_first) {
                    mTv_nursingClass.setBackgroundResource(R.drawable.shape_bg_nursing_class_first);
                } else if (colorRes == R.color.nursingClass_second) {
                    mTv_nursingClass.setBackgroundResource(R.drawable.shape_bg_nursing_class_second);
                } else if (colorRes == R.color.nursingClass_third) {
                    mTv_nursingClass.setBackgroundResource(R.drawable.shape_bg_nursing_class_third);
                } else {
                    mTv_nursingClass.setVisibility(View.GONE);
                }
            }
            mTv_patientId.setText(patient.getPatientId());
            mTv_admission.setText(patient.getadmissionDateCount() + "");
            if (!TextUtils.isEmpty(patient.getChargeType()))
                mTv_chargeType.setText(patient.getChargeType());
            else
                mTv_chargeType.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(patient.getPatientIdentity()))
                mTv_identity.setText(patient.getPatientIdentity());
            else
                mTv_identity.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(patient.getPatientSex()))
                mTv_sex.setText(patient.getPatientSex());
            else
                mTv_sex.setVisibility(View.GONE);
            mTv_totalCost.setText("¥" + patient.getTotalCosts());
            if (!TextUtils.isEmpty(patient.getPhoneNumber())) {
                mTv_telephone.setText(patient.getPhoneNumber());
            } else {
                mTv_telephone.setVisibility(GONE);
            }
            if (!TextUtils.isEmpty(patient.getWardName())) {
                mTv_wardName.setText(patient.getWardName());
            } else {
                mTv_wardName.setVisibility(GONE);
            }
        } catch (Exception e) {
            ArmsUtils.snackbarText(e.getMessage());
        }
    }

    @Override
    public void release() {
        if (mUnbinder != null)
            mUnbinder.unbind();
    }
}
