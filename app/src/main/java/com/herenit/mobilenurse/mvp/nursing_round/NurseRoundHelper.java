package com.herenit.mobilenurse.mvp.nursing_round;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.NurseRoundItem;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.view.NurseRoundViewItem;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.mvp.orders.OrdersHelp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/8/12 9:14
 * desc:护理巡视帮助类
 */
public class NurseRoundHelper {

    private NurseRoundHelper() {
    }

    /**
     * 构建护理巡视数据列表
     *
     * @param roundList         护理巡视界面Item
     * @param timePoint         护理巡视时间点
     * @param infusionOrderList 输液医嘱列表，当输液巡视时，传入该列表
     * @return
     */
    public static List<NurseRoundItem> buildNurseRoundDataList(List<NurseRoundViewItem> roundList, Date timePoint, @Nullable List<Order> infusionOrderList) {
        if (roundList == null || roundList.isEmpty())
            return null;
        List<NurseRoundItem> dataList = new ArrayList<>();
        boolean groupChecked = false;
        for (int x = 0; x < roundList.size(); x++) {
            NurseRoundViewItem viewItem = roundList.get(x);
            if (CommonConstant.ITEM_TYPE_GROUP.equals(viewItem.getItemType())) {//组
                groupChecked = viewItem.isChecked();
                if (!groupChecked)//未选中，则不提交
                    continue;
                NurseRoundItem data = buildNurseRoundData(timePoint, viewItem, infusionOrderList);
                if (data != null)
                    dataList.add(data);
            } else if (groupChecked) {//非组，并且该组已被选中
                NurseRoundItem data = buildNurseRoundData(timePoint, viewItem, infusionOrderList);
                if (data != null)
                    dataList.add(data);
            }
        }
        return dataList;
    }

    /**
     * 构建巡视数据实体类
     *
     * @param timePoint         时间点
     * @param viewItem          巡视界面的某一条
     * @param infusionOrderList 输液医嘱列表
     * @return
     */
    private static NurseRoundItem buildNurseRoundData(Date timePoint, NurseRoundViewItem viewItem, List<Order> infusionOrderList) {
        if (timePoint == null || viewItem == null)
            return null;
        if (!CommonConstant.ITEM_TYPE_GROUP.equals(viewItem.getItemType()) && TextUtils.isEmpty(viewItem.getValue()))
            return null;
        NurseRoundItem data = new NurseRoundItem();
        data.setPatientId(SickbedTemp.getInstance().getCurrentSickbed().getPatientId());
        data.setVisitId(SickbedTemp.getInstance().getCurrentSickbed().getVisitId());
        data.setItemCode(viewItem.getItemCode());
        data.setItemName(viewItem.getItemName());
        data.setPath(viewItem.getPath());
        data.setItemType(viewItem.getItemType());
        data.setItemSortNo(viewItem.getItemSortNo());
        data.setTimePoint(timePoint.getTime());
        data.setNurse(UserTemp.getInstance().getUserId());
        if (CommonConstant.ITEM_TYPE_GROUP.equals(viewItem.getItemType())) {//组的时候，不设置值，如果是输液巡视，要赋值输液条目
            if (infusionOrderList == null || infusionOrderList.isEmpty())
                return data;
            data.setItemDescription(OrdersHelp.buildOrderTextByOrderList(infusionOrderList));
        } else {//非组，则设置值
            String dataRule = viewItem.getValueDataRule();
            if (!TextUtils.isEmpty(dataRule) && dataRule.contains("※")) {
                if (!TextUtils.isEmpty(viewItem.getValue()))
                    data.setItemValue(dataRule.replaceAll("※", viewItem.getValue()));
                else
                    data.setItemValue("");
            } else {
                data.setItemValue(viewItem.getValue());
            }
        }
        return data;
    }

}
