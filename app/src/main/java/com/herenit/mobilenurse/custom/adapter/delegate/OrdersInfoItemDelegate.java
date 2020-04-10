package com.herenit.mobilenurse.custom.adapter.delegate;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.arms.base.adapter.rv.base.ItemViewDelegate;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.view.MultiItemDelegate;
import com.herenit.mobilenurse.criteria.enums.ExecuteResultEnum;

/**
 * author: HouBin
 * date: 2019/3/13 10:58
 * desc: 单组医嘱列表的Adapter
 */
public class OrdersInfoItemDelegate implements ItemViewDelegate<MultiItemDelegate> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_orders;
    }

    @Override
    public boolean isForViewType(MultiItemDelegate item, int position) {
        return item instanceof Order;
    }

    @Override
    public void convert(ViewHolder holder, MultiItemDelegate multiItemDelegateDelegate, int position) {
        if (!(multiItemDelegateDelegate instanceof Order))
            return;
        Order item = (Order) multiItemDelegateDelegate;
        View divider = holder.getView(R.id.view_item_orders_divider);
        LinearLayout ll_group = holder.getView(R.id.ll_item_orders_group);
        CheckBox cb_selector = holder.getView(R.id.cb_item_orders_select);
        TextView tv_psResult = holder.getView(R.id.tv_item_orders_psResult);
        TextView tv_planDateTime = holder.getView(R.id.tv_item_orders_planDateTime);
        TextView tv_executeDataTime = holder.getView(R.id.tv_item_orders_executeDateTime);
        ImageView img_ExecuteResult = holder.getView(R.id.img_item_orders_executeResult);
        TextView tv_orderText = holder.getView(R.id.tv_item_orders_orderText);
        LinearLayout ll_orderDesc1 = holder.getView(R.id.ll_item_orders_desc1);
        TextView tv_orderDesc1 = holder.getView(R.id.tv_item_orders_desc1);
        TextView tv_orderDesc2 = holder.getView(R.id.tv_item_orders_desc2);
        TextView tv_orderDesc3 = holder.getView(R.id.tv_item_orders_desc3);
        TextView tv_orderDesc4 = holder.getView(R.id.tv_item_orders_desc4);
        LinearLayout ll_orderDesc2 = holder.getView(R.id.ll_item_orders_desc2);
        TextView tv_orderDesc5 = holder.getView(R.id.tv_item_orders_desc5);
        LinearLayout ll_startDateTime = holder.getView(R.id.ll_item_orders_startDateTime);
        TextView tv_startDateTime = holder.getView(R.id.tv_item_orders_startDateTime);
        Context context = holder.getConvertView().getContext();
        divider.setVisibility(View.GONE);
        cb_selector.setVisibility(View.GONE);
        int executeResult = item.getExecuteResult();
        Long planDateTime = item.getPlanDateTime();
        Long executeDateTime = item.getExecuteDateTime();
        Long startDateTime = item.getStartDateTime();
        if (item.isGroupStart()) {//一组医嘱的第一条
            ll_group.setVisibility(View.VISIBLE);
            ll_startDateTime.setVisibility(View.VISIBLE);
            if (planDateTime != null) {//计划时间
                tv_planDateTime.setText(TimeUtils.getHHMMString(item.getPlanDateTime()));
            } else {
                tv_planDateTime.setText("");
            }
            if (executeDateTime != null) {//执行时间
                tv_executeDataTime.setVisibility(View.VISIBLE);
                tv_executeDataTime.setText(TimeUtils.getYYYYMMDDHHMMSSString(item.getExecuteDateTime()));
            } else {
                tv_executeDataTime.setVisibility(View.GONE);
            }
            if (startDateTime != null)//开立时间
                tv_startDateTime.setText(TimeUtils.getYYYYMMDDHHMMString(startDateTime));
            else
                tv_startDateTime.setText("");
            if (ExecuteResultEnum.isExecuted(executeResult)) {//已执行的医嘱。“已执行”、“部分执行”的医嘱都属于已执行
                img_ExecuteResult.setBackgroundResource(R.mipmap.ic_finish_small);
            } else {
                img_ExecuteResult.setBackgroundResource(R.mipmap.ic_pencil_small);
            }
            tv_planDateTime.setTextColor(item.getPlanDateTimeColor(context));

            //皮试阴阳性的显示
            String psResult = item.getPerformResult();
            if (TextUtils.isEmpty(psResult)) {//没有皮试结果（不是皮试医嘱）
                tv_psResult.setVisibility(View.GONE);
            } else if (psResult.contains(CommonConstant.COMMON_VALUE_POSITIVE)) {//阳性
                tv_psResult.setVisibility(View.VISIBLE);
                tv_psResult.setTextColor(ArmsUtils.getColor(context, R.color.red));
                tv_psResult.setText(CommonConstant.COMMON_FLAG_POSITIVE);
            } else if (psResult.contains(CommonConstant.COMMON_VALUE_NEGATIVE)) {//阴性
                tv_psResult.setVisibility(View.VISIBLE);
                tv_psResult.setTextColor(ArmsUtils.getColor(context, R.color.bg_royalBlue));
                tv_psResult.setText(CommonConstant.COMMON_FLAG_NEGATIVE);
            } else {
                tv_psResult.setVisibility(View.GONE);
            }
        } else {//非一组医嘱的第一条
            ll_group.setVisibility(View.GONE);
            ll_startDateTime.setVisibility(View.GONE);
        }
        tv_startDateTime.setText(TimeUtils.getYYYYMMDDHHMMString(item.getStartDateTime()));
        tv_orderText.setText(item.getOrderText());
        ViewUtils.setViewMargin(ll_group, false, ArmsUtils.getDimens(context, R.dimen.view_horizontal_margin_screen), ArmsUtils.getDimens(context, R.dimen.dp_5), 0, 0);
        ViewUtils.setViewMargin(tv_orderText, false, ArmsUtils.getDimens(context, R.dimen.view_horizontal_margin_screen), ArmsUtils.getDimens(context, R.dimen.dp_5), 0, 0);
        ViewUtils.setViewMargin(ll_orderDesc1, false, ArmsUtils.getDimens(context, R.dimen.view_horizontal_margin_screen), ArmsUtils.getDimens(context, R.dimen.dp_5), 0, 0);
        ViewUtils.setViewMargin(ll_orderDesc2, false, ArmsUtils.getDimens(context, R.dimen.view_horizontal_margin_screen), ArmsUtils.getDimens(context, R.dimen.dp_5), 0, 0);
        tv_orderText.setText(item.getOrderText());
        //0，普通  1，高危
//        boolean isDanger = item.getDangerIndicator() == null ? false : item.getDangerIndicator() == 1;
        if (item.getIsDangerIndicator())//高危药设置
            tv_orderText.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_danger_sign_small, 0, 0, 0);
        else
            tv_orderText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        if (item.getIsDoubleSignature()) {//需要双签名的
            tv_orderText.setTextColor(ArmsUtils.getColor(context, R.color.red));
            tv_orderDesc1.setTextColor(ArmsUtils.getColor(context, R.color.red));
            tv_orderDesc2.setTextColor(ArmsUtils.getColor(context, R.color.red));
            tv_orderDesc3.setTextColor(ArmsUtils.getColor(context, R.color.red));
            tv_orderDesc4.setTextColor(ArmsUtils.getColor(context, R.color.red));
            tv_orderDesc5.setTextColor(ArmsUtils.getColor(context, R.color.red));
        } else {//不需要双签名的
            tv_orderText.setTextColor(ArmsUtils.getColor(context, R.color.light_black));
            tv_orderDesc1.setTextColor(ArmsUtils.getColor(context, R.color.gray));
            tv_orderDesc2.setTextColor(ArmsUtils.getColor(context, R.color.gray));
            tv_orderDesc3.setTextColor(ArmsUtils.getColor(context, R.color.gray));
            tv_orderDesc4.setTextColor(ArmsUtils.getColor(context, R.color.gray));
            tv_orderDesc5.setTextColor(ArmsUtils.getColor(context, R.color.gray));
        }

        //剂量
        String dosage = "";
        if (!TextUtils.isEmpty(item.getDosage())) {
            dosage = item.getDosage();
            if (!TextUtils.isEmpty(item.getDoseUnits()))
                dosage = dosage + item.getDoseUnits();
            tv_orderDesc1.setVisibility(View.VISIBLE);
            tv_orderDesc1.setText("剂量：" + dosage);
        } else {
            tv_orderDesc1.setVisibility(View.GONE);
        }
        //给药方式
        if (!TextUtils.isEmpty(item.getAdministration())) {
            tv_orderDesc2.setVisibility(View.VISIBLE);
            tv_orderDesc2.setText("方式：" + item.getAdministration());
        } else {
            tv_orderDesc2.setVisibility(View.GONE);
        }
        //频率
        if (!TextUtils.isEmpty(item.getFrequency())) {
            tv_orderDesc3.setVisibility(View.VISIBLE);
            tv_orderDesc3.setText("频率：" + item.getFrequency());
        } else {
            tv_orderDesc3.setVisibility(View.GONE);
        }
        //标本号
        if (!TextUtils.isEmpty(item.getSpecimanName())) {
            tv_orderDesc4.setVisibility(View.VISIBLE);
            tv_orderDesc4.setText("标本：" + item.getSpecimanName());
        } else {
            tv_orderDesc4.setVisibility(View.GONE);
        }

        if (ViewUtils.isVisibility(tv_orderDesc1) ||
                ViewUtils.isVisibility(tv_orderDesc2) ||
                ViewUtils.isVisibility(tv_orderDesc3) ||
                ViewUtils.isVisibility(tv_orderDesc4)) {
            ll_orderDesc1.setVisibility(View.VISIBLE);
        } else {
            ll_orderDesc1.setVisibility(View.GONE);
        }

        //用法
        if (!TextUtils.isEmpty(item.getUserDescription())) {
            tv_orderDesc5.setVisibility(View.VISIBLE);
            tv_orderDesc5.setText("用法：" + item.getUserDescription());
        } else {
            tv_orderDesc5.setVisibility(View.GONE);
        }
        if (ViewUtils.isVisibility(tv_orderDesc5)) {
            ll_orderDesc2.setVisibility(View.VISIBLE);
        } else {
            ll_orderDesc2.setVisibility(View.GONE);
        }
    }

}
