package com.herenit.mobilenurse.criteria.entity;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/9/27 10:19
 * desc: 健康宣教历史记录数据
 */
public class HealthEduHistoryItem {

    private String docId;

    /**
     * 操作者
     */
    private String modifierName;
    /**
     * 操作时间
     */
    private Long modifyTime;
    /**
     * 宣教项目名称
     */
    private String selectedItemNameList;
    /**
     * 宣教项目列表
     */
    private List<MultiListMenuItem> selectedItemList;

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getSelectedItemNameList() {
        return selectedItemNameList;
    }

    public void setSelectedItemNameList(String selectedItemNameList) {
        this.selectedItemNameList = selectedItemNameList;
    }

    public List<MultiListMenuItem> getSelectedItemList() {
        return selectedItemList;
    }

    public void setSelectedItemList(List<MultiListMenuItem> selectedItemList) {
        this.selectedItemList = selectedItemList;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
