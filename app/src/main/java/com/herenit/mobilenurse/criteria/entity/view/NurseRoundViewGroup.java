package com.herenit.mobilenurse.criteria.entity.view;

import com.herenit.mobilenurse.criteria.entity.Order;

import java.io.Serializable;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/8/7 14:57
 * desc: 护理巡视界面配置包装类
 */
public class NurseRoundViewGroup implements Serializable {

    private static final long serialVersionUID = -6762288654491201806L;
    /**
     * 普通巡视列表
     */
    private List<NurseRoundViewItem> commonRoundList;
    /**
     * 输液巡视列表
     */
    private List<NurseRoundViewItem> infusionRoundList;
    /**
     * 当天正在执行的输液医嘱
     */
    private List<Order> orderList;

    public List<NurseRoundViewItem> getCommonRoundList() {
        return commonRoundList;
    }

    public void setCommonRoundList(List<NurseRoundViewItem> commonRoundList) {
        this.commonRoundList = commonRoundList;
    }

    public List<NurseRoundViewItem> getInfusionRoundList() {
        return infusionRoundList;
    }

    public void setInfusionRoundList(List<NurseRoundViewItem> infusionRoundList) {
        this.infusionRoundList = infusionRoundList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
