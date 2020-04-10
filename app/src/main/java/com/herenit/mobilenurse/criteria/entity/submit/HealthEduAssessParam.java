package com.herenit.mobilenurse.criteria.entity.submit;

import com.herenit.mobilenurse.criteria.entity.MultiListMenuItem;
import com.herenit.mobilenurse.criteria.entity.submit.AssessParam;

import java.io.Serializable;
import java.util.List;

/**
 * 健康宣教评估传参（提交给服务器端的数据）
 * <p>
 * Created by HouBin on 2018/9/28.
 */

public class HealthEduAssessParam extends AssessParam implements Serializable {
    List<MultiListMenuItem> selectedItemList;

    public HealthEduAssessParam() {
        super();
    }

    public List<MultiListMenuItem> getSelectedItemList() {
        return selectedItemList;
    }

    public void setSelectedItemList(List<MultiListMenuItem> selectedItemList) {
        this.selectedItemList = selectedItemList;
    }

    @Override
    public void clear() {
        super.clear();
        if (selectedItemList != null)
            selectedItemList.clear();
        selectedItemList = null;
    }
}
