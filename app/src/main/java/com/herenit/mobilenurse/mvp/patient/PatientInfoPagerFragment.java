package com.herenit.mobilenurse.mvp.patient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.PatientInfo;
import com.herenit.mobilenurse.criteria.enums.SickbedFlagEnum;
import com.herenit.mobilenurse.datastore.tempcache.DictTemp;
import com.herenit.mobilenurse.mvp.base.BasicCommonFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/3/4 15:06
 * desc: 患者详情页面
 */
public class PatientInfoPagerFragment extends BasicCommonFragment {

    @BindView(R2.id.tv_patientID)
    TextView tv_patientID;
    @BindView(R2.id.tv_sex)
    TextView tv_sex;
    @BindView(R2.id.tv_dateOfBirth)
    TextView tv_dateOfBirth;
    @BindView(R2.id.tv_identity)
    TextView tv_identity;
    @BindView(R2.id.tv_nation)
    TextView tv_nation;
    @BindView(R2.id.tv_inpNo)
    TextView tv_inpNo;
    @BindView(R2.id.tv_maritalStatus)
    TextView tv_maritalStatus;
    @BindView(R2.id.tv_age)
    TextView tv_age;
    @BindView(R2.id.tv_nextOfKinPhone)
    TextView tv_nextOfKinPhone;
    @BindView(R2.id.tv_phoneNumber)
    TextView tv_phoneNumber;
    @BindView(R2.id.tv_nextOfKin)
    TextView tv_nextOfKin;
    @BindView(R2.id.tv_idNo)
    TextView tv_idNo;
    @BindView(R2.id.tv_birthPlaceCity)
    TextView tv_birthPlaceCity;
    @BindView(R2.id.tv_chargeType)
    TextView tv_chargeType;
    @BindView(R2.id.tv_mailingAddressType)
    TextView tv_mailingAddressType;
    @BindView(R2.id.tv_zipCode)
    TextView tv_zipCode;
    @BindView(R2.id.tv_admissionDateTime)
    TextView tv_admissionDateTime;
    @BindView(R2.id.tv_admWardDateTime)
    TextView tv_admWardDateTime;
    @BindView(R2.id.tv_deptName)
    TextView tv_deptName;
    @BindView(R2.id.tv_isolationSign)
    TextView tv_isolationSign;
    @BindView(R2.id.tv_patientClass)
    TextView tv_patientClass;
    @BindView(R2.id.tv_nursingClass)
    TextView tv_nursingClass;
    @BindView(R2.id.tv_doctorInCharge)
    TextView tv_doctorInCharge;
    @BindView(R2.id.tv_chiefDoctor)
    TextView tv_chiefDoctor;
    @BindView(R2.id.tv_patientCondition)
    TextView tv_patientCondition;
    @BindView(R2.id.tv_fastingSign)
    TextView tv_fastingSign;
    @BindView(R2.id.tv_attendingDoctor)
    TextView tv_attendingDoctor;
    @BindView(R2.id.tv_byNurse)
    TextView tv_byNurse;
    @BindView(R2.id.tv_diagnosis)
    TextView tv_diagnosis;
    @BindView(R2.id.tv_info)
    TextView tv_info;

    @BindView(R2.id.srl_patientInfo_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_patient_info_pager, container, false);
        return contentView;
    }


    public void finishRefresh() {
        if (mRefreshLayout != null)
            mRefreshLayout.finishRefresh();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                //发送通知，加载患者详情
                String id = EventBusUtils.obtainPrivateId(getParentFragment().toString(), CommonConstant.EVENT_INTENTION_LOAD_DATA);
                EventBusUtils.post(id, true);
            }
        });
        //发送通知，加载患者详情
        String id = EventBusUtils.obtainPrivateId(getParentFragment().toString(), CommonConstant.EVENT_INTENTION_LOAD_DATA);
        EventBusUtils.post(id, false);
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {//Fragment可见

        }
    }

    /**
     * 显示患者详情
     *
     * @param patient
     */
    public void showPatientInfo(PatientInfo patient) {
        try {
            if (patient == null)
                return;
            tv_admissionDateTime.setText(TimeUtils.getYYYYMMDDHHMMString(patient.getVisitTime()));
            tv_admWardDateTime.setText(TimeUtils.getYYYYMMDDHHMMString(patient.getAdmWardTime()));
            tv_age.setText(patient.getPatientAge());
            tv_attendingDoctor.setText(patient.getAttendingDoctor());
            tv_birthPlaceCity.setText(patient.getBirthPlaceName());
            tv_byNurse.setText(patient.getByNurseName());
            tv_chargeType.setText(patient.getChargeType());
            tv_chiefDoctor.setText(patient.getChiefDoctor());
            tv_dateOfBirth.setText(TimeUtils.getYYYYMMDDString(patient.getBirthTime()));
            tv_deptName.setText(patient.getDeptName());
            tv_diagnosis.setText(patient.getDiagnosis());
            tv_doctorInCharge.setText(patient.getChargerDoctorName());
            String fastingSign = SickbedFlagEnum.isFastingSign(patient.getFastingSign()) ? "是" : "否";
            tv_fastingSign.setText(fastingSign);
            tv_identity.setText(patient.getPatientIdentity());
            tv_idNo.setText(patient.getIdNo());
            tv_inpNo.setText(patient.getInpNo());
            String isolationSign = SickbedFlagEnum.isIsolationSign(patient.getIsolationSign()) ? "是" : "否";
            tv_isolationSign.setText(isolationSign);
            tv_mailingAddressType.setText(patient.getPresentAddressName());
            tv_maritalStatus.setText(patient.getMaritalStatus());
            tv_nation.setText(patient.getNation());
            tv_nextOfKin.setText(patient.getNextOfKin());
            tv_nextOfKinPhone.setText(patient.getNextOfKinPhone());
            tv_nursingClass.setText(DictTemp.getInstance().getNursingClassNameByCode(patient.getNursingClass()));
            tv_patientClass.setText(patient.getPatientClass());
            tv_patientCondition.setText(DictTemp.getInstance().getPatientConditionNameByCode(patient.getPatientCondition()));
            tv_patientID.setText(patient.getPatientId());
            tv_phoneNumber.setText(patient.getPhoneNumber());
            tv_sex.setText(patient.getPatientSex());
            if (!TextUtils.isEmpty(patient.getZipCode()))
                tv_zipCode.setText(patient.getZipCode());
            else
                tv_zipCode.setText("");
            if (TextUtils.isEmpty(patient.getPoor())) {
                tv_info.setVisibility(View.GONE);
                tv_info.setText("");
            } else {
                tv_info.setText(patient.getPoor());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
