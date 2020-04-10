package com.herenit.mobilenurse.mvp.orders;

import android.content.Intent;
import android.media.SoundPool;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.View;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.app.utils.SoundPlayUtils;
import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.criteria.common.CommonNameCode;
import com.herenit.mobilenurse.criteria.common.ConditionItem;
import com.herenit.mobilenurse.criteria.common.Conditions;
import com.herenit.mobilenurse.criteria.common.NameCode;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.Newborn;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.Result;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.submit.OrdersExecute;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.enums.ExecuteResultEnum;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.adapter.NameCodeAdapter;
import com.herenit.mobilenurse.custom.widget.dialog.AccountVerifyDialog;
import com.herenit.mobilenurse.custom.widget.dialog.ListPopupWindowDialog;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.custom.widget.dialog.NoticeDialog;
import com.herenit.mobilenurse.di.component.DaggerOrderListComponent;
import com.herenit.mobilenurse.mvp.base.SwitchPatientFragment;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * author: HouBin
 * date: 2019/3/6 15:46
 * desc:医嘱列表界面，ViewPager载体，详细数据在OrderListPagerFragment中
 */
public class OrderListFragment extends SwitchPatientFragment<OrderListPresenter> implements OrderListContract.View {
    private List<OrderListPagerFragment> mPageList = new ArrayList<>();

    @Inject
    List<Order> mOrderList;

    @Inject
    List<Conditions> mConditionList;

    private LoadingDialog mLoadingDialog;

    private FragmentStatePagerAdapter mPagerAdapter;
    /**
     * 表示当前页面状态是否处于编辑状态，当处于编辑状态，则可以手动选择多条医嘱，手动执行。
     * 处于费编辑状态，则不可选择
     */
    private boolean mEditable;

    /**
     * 核对弹窗
     */
    private AccountVerifyDialog mVerifyDialog;

    private NoticeDialog mErrorDialog;

    /**
     * 含有签名核对的执行列表
     */
    private List<OrdersExecute> mVerifyExecuteList = new ArrayList<>();
    /**
     * 需要签名核对的recordId列表
     */
    private List<String> mVerifyList = new ArrayList<>();


    /**
     * ViewPager页面发生切换
     *
     * @param position
     */
    @Override
    protected void onSelectedPage(int position) {
        Sickbed sickbed = mSickbedList.get(position);
        switchPatient(sickbed);
    }

    /**
     * 切换患者
     *
     * @param sickbed
     */
    @Override
    protected void switchPatient(@NonNull Sickbed sickbed) {
        super.switchPatient(sickbed);
        //如果是左右滑动切换患者，那么就不是显示新生儿医嘱
        if (mSwitchType == CommonConstant.SWITCH_TYPE_SCROLL) {
            sickbed.setCurrentBabyIndex(-1);
        }
        //因为无法区分是左右滑动还是手动选择还是扫码，所以切换完患者要马上修改成默认状态
        mSwitchType = CommonConstant.SWITCH_TYPE_SCROLL;
//        int position = mSickbedList.indexOf(sickbed);
//        //如果当前页面所处的角标就是要切换的患者的角标，则说明是同一个患者不需要切换
//        //出现这种情况，说明是由Fragment自己左右滑动发出的Event消息调用，或者是SingleActivity患者列表选择了同一个人
//        if (position != mViewPager.getCurrentItem())
//            mViewPager.setCurrentItem(position, true);
    }


    /**
     * 返回ViewPager的适配器
     *
     * @return
     */
    @NonNull
    @Override
    protected FragmentStatePagerAdapter pagerAdapter() {
        if (mPagerAdapter == null) {
            mPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
                @Override
                public int getCount() {
                    return mPageList.size();
                }

                @Override
                public Fragment getItem(int i) {
                    return mPageList.get(i);
                }
            };
        }
        return mPagerAdapter;
    }

    /**
     * 注册Dagger
     *
     * @param appComponent
     */
    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerOrderListComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    /**
     * 加载数据
     */
    @Override
    protected void initData() {
        setTitleBar();
//        List<Sickbed> sickbedList = SickbedTemp.getInstance().getSickbedList();
//        if (sickbedList == null || sickbedList.isEmpty())
//            return;
//        int size = sickbedList.size();
//        mPageList.clear();
//        for (int x = 0; x < size; x++) {
//            mPageList.add(new OrderListPagerFragment());
//        }
//        mViewPager.getAdapter().notifyDataSetChanged();
        mPageList.clear();
        for (int x = 0; x < mSickbedList.size(); x++) {
            mPageList.add(new OrderListPagerFragment());
        }
        if (mSickbed != null) {
            int position = mSickbedList.indexOf(mSickbed);
            if (position != mViewPager.getCurrentItem()) {
                mViewPager.setCurrentItem(position, true);
            }
        }
        mPagerAdapter.notifyDataSetChanged();
    }

    /**
     * 设置标题栏
     */
    private void setTitleBar() {
        setTitleBarLeftText((ArmsUtils.getString(mContext, R.string.title_module_ordersExecute)));
        setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.btn_edit));
        setTitleBarRightBackgroundResource1(R.drawable.shape_bg_text_transparent_solid_white_stroke);
        setTitleBarRightOnClickListener1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editableChanged(!mEditable);
            }
        });
    }

    /**
     * 界面是否可编辑状态发生改变
     *
     * @param editable 修改后页面的状态  true 编辑状态，false  不可编辑状态
     */
    private void editableChanged(boolean editable) {
        mEditable = editable;
        if (mEditable) {//可编辑状态，按钮为“取消”
            setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.btn_cancel));
        } else {//不可编辑状态，按钮为“编辑”
            setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.btn_edit));
        }
        OrderListPagerFragment fragment = mPageList.get(mViewPager.getCurrentItem());
        if (fragment == null)
            return;
        String id = EventBusUtils.getPrivateEventIntention(fragment.toString(), CommonConstant.EVENT_INTENTION_EDITABLE_CHANGED);
        if (TextUtils.isEmpty(id))
            id = EventBusUtils.obtainPrivateId(fragment.toString(), CommonConstant.EVENT_INTENTION_EDITABLE_CHANGED);
        EventBusUtils.post(id, mEditable);
    }

    /**
     * 对外提供当前界面是否处于编辑状态
     *
     * @return
     */
    public boolean isEditable() {
        return mEditable;
    }

    /**
     * 用来与别的类数据沟通
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showConditionUI() {
        OrderListPagerFragment fragment = (OrderListPagerFragment) mPagerAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment != null) {
            EventBusUtils.post(EventBusUtils.obtainPrivateId(fragment.toString(),
                    CommonConstant.EVENT_INTENTION_UPDATE_CONDITION), mConditionList);
            fragment.finishRefresh();
        }
    }

    @Override
    public void showOrderListUI() {
        editableChanged(false);
        OrderListPagerFragment fragment = (OrderListPagerFragment) mPagerAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment != null) {
            EventBusUtils.post(EventBusUtils.obtainPrivateId(fragment.toString(),
                    CommonConstant.EVENT_INTENTION_UPDATE_ORDERS), mOrderList);
            fragment.finishRefresh();
        }
    }


    /**
     * 皮试医嘱执行
     *
     * @param skinTestOrders
     */
    @Override
    public void executeSkinTestOrder(List<Order> skinTestOrders) {
        if (skinTestOrders == null || skinTestOrders.isEmpty())
            return;
        List<CommonNameCode> stResultList = new ArrayList<>();
        stResultList.add(new CommonNameCode(ExecuteResultEnum.SKIN_TEST_NEGATIVE.getName(), String.valueOf(ExecuteResultEnum.SKIN_TEST_NEGATIVE.getCode())));
        stResultList.add(new CommonNameCode(ExecuteResultEnum.SKIN_TEST_POSITIVE.getName(), String.valueOf(ExecuteResultEnum.SKIN_TEST_POSITIVE.getCode())));
        NameCodeAdapter adapter = new NameCodeAdapter(mContext, stResultList);
        ListPopupWindowDialog dialog = createListPopupWindowDialog(ArmsUtils.getString(mContext, R.string.common_STResult),
                skinTestOrders.get(0).getOrderText(), adapter);
        dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
            @Override
            public void onPositive(Object... backData) {
                dialog.dismiss();
                NameCode nameCode = (NameCode) backData[0];
                if (nameCode == null)
                    return;
                mPresenter.executeSkinTestOrder(skinTestOrders, Integer.parseInt(nameCode.getCode()));
            }

            @Override
            public void onNegative() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 清除核对医嘱缓存，防止扫描工牌重复核对
     */
    @Override
    public void clearVerifyList() {
        mVerifyExecuteList.clear();
        mVerifyList.clear();
        if (mVerifyDialog != null)
            mVerifyDialog.dismiss();
    }

    /**
     * 执行双签医嘱
     */
    @Override
    public void executeDoubleSignatureOrders(final List<OrdersExecute> executeList, final List<String> verifyList) {
        if (executeList == null || executeList.isEmpty())
            return;
        //双签名弹窗
        if (mVerifyDialog == null) {
            mVerifyDialog = createAccountVerifyDialog(ArmsUtils.getString(mContext, R.string.title_doubleSignatureOrder),
                    ArmsUtils.getString(mContext, R.string.message_verifyNotice));
            mVerifyDialog.setPositiveNegativeListener(new PositiveNegativeListener() {
                @Override
                public void onPositive(Object... backData) {
                    String loginName = (String) backData[0];
                    String password = (String) backData[1];
                    if (TextUtils.isEmpty(loginName)) {//账号为空
                        showErrorToast(ArmsUtils.getString(mContext, R.string.error_login_emptyUserId));
                        return;
                    } else if (TextUtils.isEmpty(password)) {//密码为空
                        showErrorToast(ArmsUtils.getString(mContext, R.string.error_login_emptyPassword));
                        return;
                    }
                    mVerifyDialog.dismiss();
                    mPresenter.executeDoubleSignatureOrders(mVerifyExecuteList, mVerifyList, loginName, StringUtils.toMD5(password));
                }

                @Override
                public void onNegative() {
                    mVerifyDialog.dismiss();
                }
            });
        }
        mVerifyDialog.show();
        //弹出签名核对弹窗时，要将核对执行的医嘱缓存，用于扫描工牌核对
        mVerifyExecuteList.clear();
        mVerifyList.clear();
        mVerifyExecuteList.addAll(executeList);
        mVerifyList.addAll(verifyList);
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog == null)
            mLoadingDialog = createLoadingDialog();
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
    }

    /**
     * 执行失败
     *
     * @param errorMessage
     */
    @Override
    public void executeFailed(String errorMessage) {
        showErrorVoice();
        if (mErrorDialog == null) {
            mErrorDialog = createErrorNoNegativeNoticeDialog(ArmsUtils.getString(mContext, R.string.error_executeError), errorMessage);
            mErrorDialog.setPositiveNegativeListener(new PositiveNegativeListener() {
                @Override
                public void onPositive() {
                    mErrorDialog.dismiss();
                }
            });
        } else {
            mErrorDialog.setMessage(errorMessage);
        }
        mErrorDialog.show();
    }

    @Override
    public void showSuccessVoice() {
        SoundPlayUtils.play(1);
    }

    @Override
    public void showErrorVoice() {
        SoundPlayUtils.play(2);
    }

    @Override
    public void showMessage(@NonNull String message) {
        showToast(message);
    }

    /**
     * 切换到了同一个人，因为View Pager不会重新加载，需要手动刷新数据
     */
    @Override
    protected void switchSameSickbed() {
        super.switchSameSickbed();
        mPresenter.loadData(SickbedTemp.getInstance().getCurrentSickbed(), false);
    }

    /**
     * EventBus接收消息
     *
     * @param event
     */
    @Subscribe
    public void onEventBusReceive(Event event) {
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (!TextUtils.isEmpty(intention)) {//私有消费
            if (intention.equals(CommonConstant.EVENT_INTENTION_LOAD_ORDER_LIST)) {//筛选条件发生改变
                checkBabyIndexChanged(SickbedTemp.getInstance().getCurrentSickbed(), mConditionList);
                mPresenter.loadPatientOrderList(SickbedTemp.getInstance().getCurrentSickbed(), false, false, false);
            } else if (intention.equals(CommonConstant.EVENT_INTENTION_EXECUTE_ORDERS)) {//执行医嘱
                List<Order> orderList = (List<Order>) event.getMessage();
                if (orderList != null && !orderList.isEmpty())
                    mPresenter.executeNotSkinTestOrders(orderList);
            } else if (intention.equals(CommonConstant.EVENT_INTENTION_LOAD_DATA)) {//加载数据
                boolean refresh = (boolean) event.getMessage();//是否刷新
                mPresenter.loadData(SickbedTemp.getInstance().getCurrentSickbed(), refresh);
            }
        } else {//公共事件消费
            if (id.equals(EventIntentionEnum.CODE_TYPE_ORDER.getId()) && mVisible) {//公共事件的执行医嘱，一般是由外界扫码调用
                //上次执行错误，还未点击确认，或者当前正在进行签名核对，则不允许执行
                if ((mErrorDialog != null && mErrorDialog.isShowing()) || (mVerifyDialog != null && mVerifyDialog.isShowing())) {
                    showErrorVoice();
                    return;
                }
                ScanResult scanResult = (ScanResult) event.getMessage();
                mPresenter.scanOrderExecute(scanResult);
            } else if (id.equals(EventIntentionEnum.CODE_TYPE_EMP_CARD.getId()) && mVisible) {//扫描员工牌
                ScanResult scanResult = (ScanResult) event.getMessage();
                if (mVerifyDialog != null && mVerifyDialog.isShowing() && !mVerifyExecuteList.isEmpty() && !mVerifyList.isEmpty()) {//当前正处于签名状态
                    String loginName = scanResult.getLoginName();
                    if (!TextUtils.isEmpty(loginName)) {
                        //将签名信息写入弹窗，同时执行签名方法
                        mVerifyDialog.setVerifyText(loginName, "");
                        mPresenter.executeDoubleSignatureOrders(mVerifyExecuteList, mVerifyList, loginName, "");
                    }
                }
            }
        }
    }

    /**
     * 检测当前条件选中的新生儿角标
     *
     * @param currentSickbed
     * @param conditionList
     */
    private void checkBabyIndexChanged(Sickbed currentSickbed, List<Conditions> conditionList) {
        if (conditionList == null || conditionList.isEmpty())
            return;
        for (Conditions conditions : conditionList) {
            if (!conditions.getConditionType().equals(CommonConstant.CONDITION_TYPE_MATERNAL_AND_INFANTS))
                continue;
            List<ConditionItem> itemList = conditions.getConditions();
            if (itemList == null || itemList.isEmpty())
                continue;
            for (int x = 0; x < itemList.size(); x++) {
                ConditionItem item = itemList.get(x);
                if (item.isChecked()) {//检测到当前患者有新生儿，并且查询到当前选中的新生儿，则设置期角标
                    currentSickbed.setCurrentBabyIndex(x - 1);
                }
            }
        }
    }

    @Override
    public void onSwitchPatientScan(Event event) {
        //扫描患者腕带之后的操作
        super.onSwitchPatientScan(event);//先继承父类做切换患者的操作
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (TextUtils.isEmpty(intention)) {//未找到私有事件，故为共有消息事件
            if (id.equals(EventIntentionEnum.CODE_TYPE_PATIENT.getId())) {//扫描结果
                if (!mVisible)//页面不可见，扫描不做处理
                    return;
                ScanResult scanResult = (ScanResult) event.getMessage();
                //针对皮试医嘱进行判断，正在皮试的医嘱，需要录入皮试结果，省人民皮试处置医嘱没有二维码，只能通过扫描患者腕带来执行
                if (mSickbed.getPatientId().equals(scanResult.getPatientId())) {
                    //上次执行错误，还未点击确认，或者当前正在进行签名核对，则不允许执行
                    if ((mErrorDialog != null && mErrorDialog.isShowing()) || (mVerifyDialog != null && mVerifyDialog.isShowing())) {
                        showErrorVoice();
                        return;
                    }
                    //先去查找需要录入皮试结果的皮试医嘱
                    mPresenter.loadPatientOrderList(SickbedTemp.getInstance().getCurrentSickbed(), false, true, false);
                } else {
                    Newborn newborn = mSickbed.getCurrentBaby();
                    if (newborn == null)
                        return;
                    if (newborn.getBabyId().equals(scanResult.getPatientId())) {//扫描到当前新生儿腕带，则可能是要执行当前新生儿的皮试医嘱
                        mPresenter.loadPatientOrderList(SickbedTemp.getInstance().getCurrentSickbed(), false, true, false);
                    }
                }
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //医嘱详情操作完毕，医嘱列表界面要刷新数据
        if (requestCode == CommonConstant.REQUEST_CODE_ORDER_INFO && resultCode == CommonConstant.RESULT_CODE_ORDER_INFO) {
            if (data != null) {
                List<OrdersExecute> executeList = (List<OrdersExecute>) data.getSerializableExtra(KeyConstant.NAME_EXTRA_EXECUTE_LIST);
                mPresenter.setCurrentOperation(executeList);
                mPresenter.loadPatientOrderList(SickbedTemp.getInstance().getCurrentSickbed(), false, false, true);
            } else {
                mPresenter.loadPatientOrderList(SickbedTemp.getInstance().getCurrentSickbed(), false, false, false);
            }
        }
    }

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_TV;
    }

}
