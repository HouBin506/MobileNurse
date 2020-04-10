package com.herenit.mobilenurse.criteria.entity.dict;

import android.text.TextUtils;

import com.herenit.mobilenurse.criteria.common.NameCode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author: HouBin
 * date: 2019/5/23 10:18
 * desc: 紧急状态标识
 */
@Entity
public class EmergencyIndicatorDict implements NameCode {

    @Id
    private Long id;

    private String emergencyIndicatorName;
    private String emergencyIndicatorCode;


    @Generated(hash = 675280279)
    public EmergencyIndicatorDict(Long id, String emergencyIndicatorName,
                                  String emergencyIndicatorCode) {
        this.id = id;
        this.emergencyIndicatorName = emergencyIndicatorName;
        this.emergencyIndicatorCode = emergencyIndicatorCode;
    }

    @Generated(hash = 620008459)
    public EmergencyIndicatorDict() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmergencyIndicatorName() {
        return emergencyIndicatorName;
    }

    public void setEmergencyIndicatorName(String emergencyIndicatorName) {
        this.emergencyIndicatorName = emergencyIndicatorName;
    }

    public String getEmergencyIndicatorCode() {
        return emergencyIndicatorCode;
    }

    public void setEmergencyIndicatorCode(String emergencyIndicatorCode) {
        this.emergencyIndicatorCode = emergencyIndicatorCode;
    }

    @Override
    public String getName() {
        return emergencyIndicatorName;
    }

    @Override
    public String getCode() {
        return TextUtils.isEmpty(emergencyIndicatorCode) ? emergencyIndicatorName : emergencyIndicatorCode;
    }
}
