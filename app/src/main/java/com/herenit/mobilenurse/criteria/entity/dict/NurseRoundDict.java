package com.herenit.mobilenurse.criteria.entity.dict;

import com.herenit.mobilenurse.criteria.common.NameCode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author: HouBin
 * date: 2019/8/13 9:29
 * desc: 护理巡视项目字典
 */
@Entity
public class NurseRoundDict implements NameCode {

    @Id
    private Long id;

    private String itemName;

    private String itemCode;

    @Generated(hash = 1077882766)
    public NurseRoundDict(Long id, String itemName, String itemCode) {
        this.id = id;
        this.itemName = itemName;
        this.itemCode = itemCode;
    }

    @Generated(hash = 249382354)
    public NurseRoundDict() {
    }

    @Override
    public String getName() {
        return itemName;
    }

    @Override
    public String getCode() {
        return itemCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
}
