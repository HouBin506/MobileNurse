package com.herenit.mobilenurse.criteria.entity;

import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;

import java.io.Serializable;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/8/19 14:17
 * desc:评估实体类，内部封装了界面配置列表、数据列表、以及一些结果
 */
public class AssessModel implements Serializable {
    private static final long serialVersionUID = -8223860322046626177L;

    /**
     * 界面配置列表
     */
    private List<AssessViewItem> viewList;
    /**
     * 数据列表
     */
    private List<SummaryDataModel> dataList;

    public List<AssessViewItem> getViewList() {
        return viewList;
    }

    public void setViewList(List<AssessViewItem> viewList) {
        this.viewList = viewList;
    }

    public List<SummaryDataModel> getDataList() {
        return dataList;
    }

    public void setDataList(List<SummaryDataModel> dataList) {
        this.dataList = dataList;
    }

}
