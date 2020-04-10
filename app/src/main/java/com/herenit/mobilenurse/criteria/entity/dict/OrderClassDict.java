package com.herenit.mobilenurse.criteria.entity.dict;

import android.text.TextUtils;

import com.herenit.mobilenurse.criteria.common.NameCode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author: HouBin
 * date: 2019/4/3 16:10
 * desc: 医嘱类别字典
 */
@Entity
public class OrderClassDict implements NameCode {
    @Id
    private Long id;

    /**
     * orderClassCode : A
     * orderClassName : 药疗
     */

    private String orderClassCode;
    private String orderClassName;

    @Generated(hash = 229704965)
    public OrderClassDict(Long id, String orderClassCode, String orderClassName) {
        this.id = id;
        this.orderClassCode = orderClassCode;
        this.orderClassName = orderClassName;
    }

    @Generated(hash = 1914847328)
    public OrderClassDict() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderClassCode() {
        return orderClassCode;
    }

    public void setOrderClassCode(String orderClassCode) {
        this.orderClassCode = orderClassCode;
    }

    public String getOrderClassName() {
        return orderClassName;
    }

    public void setOrderClassName(String orderClassName) {
        this.orderClassName = orderClassName;
    }

    @Override
    public String getName() {
        return orderClassName;
    }

    @Override
    public String getCode() {
        return TextUtils.isEmpty(orderClassCode) ? orderClassName : orderClassCode;
    }
}
