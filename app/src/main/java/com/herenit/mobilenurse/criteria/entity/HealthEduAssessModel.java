package com.herenit.mobilenurse.criteria.entity;

import com.herenit.mobilenurse.criteria.common.CommonNameCode;

import java.io.Serializable;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/8/19 14:17
 * desc:评估实体类，内部封装了界面配置列表、数据列表、以及一些结果
 */
public class HealthEduAssessModel extends AssessModel implements Serializable {
    private static final long serialVersionUID = -8223860322046626177L;
    /**
     * 健康宣教项目（一般由客户端本地缓存中获取）
     */
    private List<MultiListMenuItem> selectedItemList;

    public List<MultiListMenuItem> getSelectedItemList() {
        return selectedItemList;
    }

    public void setSelectedItemList(List<MultiListMenuItem> selectedItemList) {
        this.selectedItemList = selectedItemList;
    }
}
