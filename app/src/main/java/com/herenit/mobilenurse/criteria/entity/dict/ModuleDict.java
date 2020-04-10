package com.herenit.mobilenurse.criteria.entity.dict;

import android.support.annotation.DrawableRes;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * author: HouBin
 * date: 2019/4/3 16:08
 * desc:模块字典，表示当前要显示的
 */
@Entity
public class ModuleDict {
    @Id
    private Long id;

    /**
     * moduleId : 0001
     * moduleName : 床位⼀览
     */

    private String moduleId;
    private String moduleName;
    @Transient
    @DrawableRes
    private int iconRes;



    @Generated(hash = 1235879777)
    public ModuleDict(Long id, String moduleId, String moduleName) {
        this.id = id;
        this.moduleId = moduleId;
        this.moduleName = moduleName;
    }

    @Generated(hash = 426871898)
    public ModuleDict() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(@DrawableRes int iconRes) {
        this.iconRes = iconRes;
    }
}
