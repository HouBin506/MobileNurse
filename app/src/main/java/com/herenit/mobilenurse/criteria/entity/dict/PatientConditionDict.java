package com.herenit.mobilenurse.criteria.entity.dict;

import android.text.TextUtils;

import com.herenit.mobilenurse.criteria.common.NameCode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author: HouBin
 * date: 2019/4/3 16:09
 * desc:病情等级字典
 */
@Entity
public class PatientConditionDict implements NameCode {
    @Id
    private Long id;

    /**
     * patientConditionCode : null
     * patientConditionName : 危
     */

    private String patientConditionCode;
    private String patientConditionName;

    @Generated(hash = 444289804)
    public PatientConditionDict(Long id, String patientConditionCode,
                                String patientConditionName) {
        this.id = id;
        this.patientConditionCode = patientConditionCode;
        this.patientConditionName = patientConditionName;
    }

    @Generated(hash = 1950941801)
    public PatientConditionDict() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientConditionCode() {
        return patientConditionCode;
    }

    public void setPatientConditionCode(String patientConditionCode) {
        this.patientConditionCode = patientConditionCode;
    }

    public String getPatientConditionName() {
        return patientConditionName;
    }

    public void setPatientConditionName(String patientConditionName) {
        this.patientConditionName = patientConditionName;
    }

    @Override
    public String getName() {
        return patientConditionName;
    }

    @Override
    public String getCode() {
        return TextUtils.isEmpty(patientConditionCode) ? patientConditionName : patientConditionCode;
    }
}
