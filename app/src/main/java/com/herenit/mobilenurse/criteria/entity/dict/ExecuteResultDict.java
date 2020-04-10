package com.herenit.mobilenurse.criteria.entity.dict;

import android.text.TextUtils;

import com.herenit.mobilenurse.criteria.common.NameCode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author: HouBin
 * date: 2019/4/3 16:10
 * desc:执行结果字典类
 */
@Entity
public class ExecuteResultDict implements NameCode {
    @Id
    private Long id;

    /**
     * executeResultCode : 0
     * executeResultName : 未执⾏
     */
    private String executeResultCode;
    private String executeResultName;

    @Generated(hash = 1482254246)
    public ExecuteResultDict(Long id, String executeResultCode,
                             String executeResultName) {
        this.id = id;
        this.executeResultCode = executeResultCode;
        this.executeResultName = executeResultName;
    }

    @Generated(hash = 1065074209)
    public ExecuteResultDict() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExecuteResultCode() {
        return executeResultCode;
    }

    public void setExecuteResultCode(String executeResultCode) {
        this.executeResultCode = executeResultCode;
    }

    public String getExecuteResultName() {
        return executeResultName;
    }

    public void setExecuteResultName(String executeResultName) {
        this.executeResultName = executeResultName;
    }

    @Override
    public String getName() {
        return executeResultName;
    }

    @Override
    public String getCode() {
        return TextUtils.isEmpty(executeResultCode) ? executeResultName : executeResultCode;
    }
}
