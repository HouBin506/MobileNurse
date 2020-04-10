package com.herenit.mobilenurse.criteria.entity.dict;

import android.text.TextUtils;

import com.herenit.mobilenurse.criteria.common.NameCode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author: HouBin
 * date: 2019/5/23 10:23
 * desc: 手术确认（手术状态）字典表
 */
@Entity
public class AckIndicatorDict implements NameCode {
    @Id
    private Long id;

    private String ackIndicatorName;
    private String ackIndicatorCode;

    @Generated(hash = 174154876)
    public AckIndicatorDict(Long id, String ackIndicatorName,
                            String ackIndicatorCode) {
        this.id = id;
        this.ackIndicatorName = ackIndicatorName;
        this.ackIndicatorCode = ackIndicatorCode;
    }

    @Generated(hash = 861811790)
    public AckIndicatorDict() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAckIndicatorName() {
        return ackIndicatorName;
    }

    public void setAckIndicatorName(String ackIndicatorName) {
        this.ackIndicatorName = ackIndicatorName;
    }

    public String getAckIndicatorCode() {
        return ackIndicatorCode;
    }

    public void setAckIndicatorCode(String ackIndicatorCode) {
        this.ackIndicatorCode = ackIndicatorCode;
    }

    @Override
    public String getName() {
        return ackIndicatorName;
    }

    @Override
    public String getCode() {
        return TextUtils.isEmpty(ackIndicatorCode) ? ackIndicatorName : ackIndicatorCode;
    }
}
