package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herenit.arms.base.adapter.rv.CommonAdapter;
import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.enums.ExecuteResultEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author: HouBin
 * date: 2019/3/8 9:12
 * desc:医嘱列表适配
 */
public class OrdersAdapter extends CommonAdapter<Order> {

    /**
     * 是否处于编辑状态，如果处于编辑状态，则点击Item会选中，如果并非处于编辑状态，点击Item会跳转进入医嘱详情界面
     */
    private boolean mEditable = false;

    /**
     * 当前选中的医嘱列表
     */
    private List<Order> mSelectedOrders = new ArrayList<>();


    public OrdersAdapter(Context context, List<Order> datas) {
        super(context, R.layout.item_orders, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Order item, int position) {
        View divider = holder.getView(R.id.view_item_orders_divider);
        LinearLayout ll_group = holder.getView(R.id.ll_item_orders_group);
        CheckBox cb_selector = holder.getView(R.id.cb_item_orders_select);
        TextView tv_planDateTime = holder.getView(R.id.tv_item_orders_planDateTime);
        TextView tv_executeDataTime = holder.getView(R.id.tv_item_orders_executeDateTime);
        LinearLayout ll_startDateTime = holder.getView(R.id.ll_item_orders_startDateTime);
        TextView tv_startDateTime = holder.getView(R.id.tv_item_orders_startDateTime);
        TextView tv_psResult = holder.getView(R.id.tv_item_orders_psResult);
        ImageView img_ExecuteResult = holder.getView(R.id.img_item_orders_executeResult);
        TextView tv_orderText = holder.getView(R.id.tv_item_orders_orderText);
        LinearLayout ll_orderDesc1 = holder.getView(R.id.ll_item_orders_desc1);
        TextView tv_orderDesc1 = holder.getView(R.id.tv_item_orders_desc1);
        TextView tv_orderDesc2 = holder.getView(R.id.tv_item_orders_desc2);
        TextView tv_orderDesc3 = holder.getView(R.id.tv_item_orders_desc3);
        TextView tv_orderDesc4 = holder.getView(R.id.tv_item_orders_desc4);
        LinearLayout ll_orderDesc2 = holder.getView(R.id.ll_item_orders_desc2);
        TextView tv_orderDesc5 = holder.getView(R.id.tv_item_orders_desc5);
        int executeResult = item.getExecuteResult();
        String psResult = item.getPerformResult();
        Long planDateTime = item.getPlanDateTime();
        Long executeDateTime = item.getExecuteDateTime();
        Long startDateTime = item.getStartDateTime();
        if (isGroupStart(position)) {//一组医嘱的第一条
            divider.setVisibility(View.VISIBLE);
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
            //皮试阴阳性的显示
            if (TextUtils.isEmpty(psResult)) {//没有皮试结果（不是皮试医嘱）
                tv_psResult.setVisibility(View.GONE);
            } else if (psResult.contains(CommonConstant.COMMON_VALUE_POSITIVE)) {//阳性
                tv_psResult.setVisibility(View.VISIBLE);
                tv_psResult.setTextColor(ArmsUtils.getColor(mContext, R.color.red));
                tv_psResult.setText(CommonConstant.COMMON_FLAG_POSITIVE);
            } else if (psResult.contains(CommonConstant.COMMON_VALUE_NEGATIVE)) {//阴性
                tv_psResult.setVisibility(View.VISIBLE);
                tv_psResult.setTextColor(ArmsUtils.getColor(mContext, R.color.bg_royalBlue));
                tv_psResult.setText(CommonConstant.COMMON_FLAG_NEGATIVE);
            } else {
                tv_psResult.setVisibility(View.GONE);
            }

            if (ExecuteResultEnum.isExecuted(executeResult)) {//已执行的医嘱。“已执行”、“部分执行”的医嘱都属于已执行
                img_ExecuteResult.setBackgroundResource(R.mipmap.ic_finish_small);
            } else {
                img_ExecuteResult.setBackgroundResource(R.mipmap.ic_pencil_small);
            }
            tv_planDateTime.setTextColor(item.getPlanDateTimeColor(mContext));
        } else {//非一组医嘱的第一条
            divider.setVisibility(View.GONE);
            ll_group.setVisibility(View.GONE);
            ll_startDateTime.setVisibility(View.GONE);
        }
        if (item.isNewExecute()) {//刚刚新执行成功的医嘱，浅灰蓝色
            holder.getConvertView().setBackgroundColor(ArmsUtils.getColor(mContext, R.color.LightGreyBlue));
        } else {
            holder.getConvertView().setBackgroundColor(ArmsUtils.getColor(mContext, R.color.white));
        }
        tv_orderText.setText(item.getOrderText());
        //0，普通  1，高危
        if (item.getIsDangerIndicator())//高危药设置
            tv_orderText.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_danger_sign_small, 0, 0, 0);
        else
            tv_orderText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        if (item.getIsDoubleSignature()) {//需要双签名的
            tv_orderText.setTextColor(ArmsUtils.getColor(mContext, R.color.red));
            tv_orderDesc1.setTextColor(ArmsUtils.getColor(mContext, R.color.red));
            tv_orderDesc2.setTextColor(ArmsUtils.getColor(mContext, R.color.red));
            tv_orderDesc3.setTextColor(ArmsUtils.getColor(mContext, R.color.red));
            tv_orderDesc4.setTextColor(ArmsUtils.getColor(mContext, R.color.red));
            tv_orderDesc5.setTextColor(ArmsUtils.getColor(mContext, R.color.red));
        } else {//不需要双签名的
            tv_orderText.setTextColor(ArmsUtils.getColor(mContext, R.color.light_black));
            tv_orderDesc1.setTextColor(ArmsUtils.getColor(mContext, R.color.gray));
            tv_orderDesc2.setTextColor(ArmsUtils.getColor(mContext, R.color.gray));
            tv_orderDesc3.setTextColor(ArmsUtils.getColor(mContext, R.color.gray));
            tv_orderDesc4.setTextColor(ArmsUtils.getColor(mContext, R.color.gray));
            tv_orderDesc5.setTextColor(ArmsUtils.getColor(mContext, R.color.gray));
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
        //标本
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
        //根据是否处于编辑状态，决定选择框、以及Item显示调整
        if (!mEditable) {//不可编辑状态
            cb_selector.setVisibility(View.GONE);
            ViewUtils.setViewMargin(ll_group, false, ArmsUtils.getDimens(mContext, R.dimen.view_horizontal_margin_screen), ArmsUtils.getDimens(mContext, R.dimen.dp_5), 0, 0);
            ViewUtils.setViewMargin(tv_orderText, false, ArmsUtils.getDimens(mContext, R.dimen.view_horizontal_margin_screen), ArmsUtils.getDimens(mContext, R.dimen.dp_5), 0, 0);
            ViewUtils.setViewMargin(ll_orderDesc1, false, ArmsUtils.getDimens(mContext, R.dimen.view_horizontal_margin_screen), ArmsUtils.getDimens(mContext, R.dimen.dp_5), 0, 0);
            ViewUtils.setViewMargin(ll_orderDesc2, false, ArmsUtils.getDimens(mContext, R.dimen.view_horizontal_margin_screen), ArmsUtils.getDimens(mContext, R.dimen.dp_5), 0, 0);
        } else {//可编辑状态
            ViewUtils.setViewMargin(ll_group, false, 0, ArmsUtils.getDimens(mContext, R.dimen.dp_5), 0, 0);
            ViewUtils.setViewMargin(tv_orderText, false, ArmsUtils.getDimens(mContext, R.dimen.dp_25), ArmsUtils.getDimens(mContext, R.dimen.dp_5), 0, 0);
            ViewUtils.setViewMargin(ll_orderDesc1, false, ArmsUtils.getDimens(mContext, R.dimen.dp_25), ArmsUtils.getDimens(mContext, R.dimen.dp_5), 0, 0);
            ViewUtils.setViewMargin(ll_orderDesc2, false, ArmsUtils.getDimens(mContext, R.dimen.dp_25), ArmsUtils.getDimens(mContext, R.dimen.dp_5), 0, 0);
            if (ExecuteResultEnum.canExecute(executeResult)) {//未执行或者执行中，可选中之后继续执行
                cb_selector.setVisibility(View.VISIBLE);
                cb_selector.setChecked(item.isChecked());
            } else {
                cb_selector.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    protected void convert(ViewHolder holder, Map<String, Object> stringObjectMap, int position) {

    }


    /**
     * 根据医嘱列表的某条Item的Position，获取该组医嘱
     *
     * @param position
     * @return
     */
    public List<Order> getGroupOrdersByPosition(int position) {
        if (mDatas == null || mDatas.isEmpty())
            return null;
        List<Order> list = new ArrayList<>();
        String groupOrderId = mDatas.get(position).getRecordGroupId();
        int groupStart = -1;
        for (int x = 0; x < mDatas.size(); x++) {
            Order order = mDatas.get(x);
            if (groupOrderId.equals(order.getRecordGroupId())) {
                if (groupStart < 0)//该组医嘱的第一条
                    groupStart = x;
                list.add(order);
            } else {
                if (groupStart >= 0 && x > groupStart)//大于该组医嘱第一条，并且不属于同一组，则不必往下循环
                    break;
            }
        }
        return list;
    }

    /**
     * 判断当前医嘱是否为一组医嘱的开头
     *
     * @param position
     * @return
     */
    private boolean isGroupStart(int position) {
        if (mDatas == null || mDatas.isEmpty() || position >= mDatas.size())
            return false;
        Order order = mDatas.get(position);
        return position > 0 ? !(order.getRecordGroupId().equals(mDatas.get(position - 1).getRecordGroupId())) : true;
    }

    /**
     * 修改编辑状态
     *
     * @param editable
     */
    public void changeEditable(boolean editable) {
        mEditable = editable;
        clearDataListSelect();
        notifyDataSetChanged();
    }

    /**
     * 是否处于编辑状态
     *
     * @return
     */
    public boolean isEditable() {
        return mEditable;
    }

    /**
     * 清空所有选中条目
     */
    public void clearDataListSelect() {
        mSelectedOrders.clear();
        if (mDatas != null && !mDatas.isEmpty()) {
            for (Order order : mDatas) {
                order.setChecked(false);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 选中所有可执行的医嘱
     */
    public void selectAllCanExecuteOrders() {
        if (!mEditable || mDatas == null || mDatas.isEmpty())
            return;
        for (Order order : mDatas) {
            selectCanExecuteOrder(order);
            addSelectedOrder(order);
        }
        notifyDataSetChanged();
    }

    /**
     * 选中或者取消选中某条医嘱
     *
     * @param position
     */
    public void editOrderListBySelectPosition(int position) {
        if (mDatas == null || mDatas.isEmpty())
            return;
        String recordGroupId = mDatas.get(position).getRecordGroupId();
        int groupStartPosition = -1;
        for (int x = 0; x < mDatas.size(); x++) {
            Order order = mDatas.get(x);
            if (recordGroupId.equals(order.getRecordGroupId())) {//找到当次执行的同组医嘱
                if (!ExecuteResultEnum.canExecute(order.getExecuteResult()))//当前操作的医嘱，是不可被执行的，则直接返回不做任何操作
                    return;
                if (groupStartPosition < 0)
                    groupStartPosition = x;
                if (order.isChecked())
                    order.setChecked(false);
                else
                    order.setChecked(true);
                addSelectedOrder(order);
            } else {
                if (groupStartPosition >= 0 && x > groupStartPosition)
                    break;
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 想选中的医嘱列表中添加医嘱，当该条医嘱选中并且不存在则添加，当未选中并且存在则删除
     *
     * @param order
     */
    private void addSelectedOrder(Order order) {
        if (order.isChecked()) {
            if (!mSelectedOrders.contains(order))
                mSelectedOrders.add(order);
        } else {
            if (mSelectedOrders.contains(order))
                mSelectedOrders.remove(order);
        }
    }

    /**
     * 选中单条可执行的医嘱
     *
     * @param order
     */
    private void selectCanExecuteOrder(Order order) {
        int executeResult = order.getExecuteResult();
        if (ExecuteResultEnum.canExecute(executeResult)) {
            order.setChecked(true);
        } else {
            order.setChecked(false);
        }
    }

    /**
     * 当前是否处于全选状态，可执行的医嘱都被选中
     *
     * @return
     */
    public boolean isSelectedAll() {
        if (mDatas == null || mDatas.isEmpty())
            return false;
        boolean isSelectedAll = true;
        for (Order order : mDatas) {
            if (ExecuteResultEnum.canExecute(order.getExecuteResult()) && !order.isChecked()) {
                isSelectedAll = false;
                break;
            }
        }
        return isSelectedAll ? !mSelectedOrders.isEmpty() : isSelectedAll;
    }

    public List<Order> getSelectedOrders() {
        return mSelectedOrders;
    }
}
