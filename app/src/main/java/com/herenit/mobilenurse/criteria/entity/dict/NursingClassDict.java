package com.herenit.mobilenurse.criteria.entity.dict;

import android.text.TextUtils;

import com.herenit.mobilenurse.criteria.common.NameCode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author: HouBin
 * date: 2019/4/3 16:08
 * desc: 护理等级字典
 */
@Entity
public class NursingClassDict implements NameCode {
    @Id
    private Long id;

    /**
     * nursingClassCode : null
     * nursingClassName : 重症护理
     */

    private String nursingClassCode;
    private String nursingClassName;

    @Generated(hash = 709974326)
    public NursingClassDict(Long id, String nursingClassCode,
                            String nursingClassName) {
        this.id = id;
        this.nursingClassCode = nursingClassCode;
        this.nursingClassName = nursingClassName;
    }

    @Generated(hash = 561059080)
    public NursingClassDict() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNursingClassCode() {
        return nursingClassCode;
    }

    public void setNursingClassCode(String nursingClassCode) {
        this.nursingClassCode = nursingClassCode;
    }

    public String getNursingClassName() {
        return nursingClassName;
    }

    public void setNursingClassName(String nursingClassName) {
        this.nursingClassName = nursingClassName;
    }

    @Override
    public String getName() {
        return nursingClassName;
    }

    @Override
    public String getCode() {
        return TextUtils.isEmpty(nursingClassCode) ? nursingClassName : nursingClassCode;
    }
}
