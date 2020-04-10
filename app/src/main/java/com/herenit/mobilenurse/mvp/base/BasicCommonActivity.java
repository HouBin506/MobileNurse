package com.herenit.mobilenurse.mvp.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.herenit.arms.base.BaseActivity;
import com.herenit.arms.integration.AppManager;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.ScreenUtils;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.constant.ValueConstant;
import com.herenit.mobilenurse.criteria.enums.ChoiceTypeEnum;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.enums.NoticeLevelEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.adapter.CommonTextAdapter;
import com.herenit.mobilenurse.custom.extra.WidgetExtra;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.barview.StatusBarView;
import com.herenit.mobilenurse.custom.widget.dialog.AccountVerifyDialog;
import com.herenit.mobilenurse.custom.widget.dialog.DoubleInputDialog;
import com.herenit.mobilenurse.custom.widget.dialog.ListDialog;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.custom.widget.dialog.NoticeDialog;
import com.herenit.mobilenurse.custom.widget.dialog.SingleInputDialog;
import com.herenit.mobilenurse.custom.widget.toast.NoticeToast;
import com.herenit.mobilenurse.mvp.launch.LoadingActivity;
import com.herenit.mobilenurse.mvp.login.LoginActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.herenit.arms.utils.Preconditions.checkNotNull;

/**
 * author: HouBin
 * date: 2019/1/28 13:44
 * desc: 封装一些APP通用的功能
 */
public abstract class BasicCommonActivity<P extends BasePresenter> extends BaseActivity<P> {
    /**
     * 自定义组件集合，用于统一管理
     */
    private List<WidgetExtra> mWidgetList;
    protected Context mContext;
    //是否使用状态栏占位
    private boolean useStatusBarView = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        mWidgetList = new ArrayList<>();
        if (useStatusBarView)
            setStatusBarView();
    }

    /**
     * 沉浸式标题栏，设置状态栏的占位View以及其颜色，如果当前页面是全屏显示图片，则没必要使用此方法
     */
    private void setStatusBarView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0设置沉浸式状态栏
            getWindow().setStatusBarColor(ArmsUtils.getColor(mContext, R.color.bg_title));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4设置沉浸式状态栏
            //获取windowphone下的decorView
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            int count = decorView.getChildCount();
            //判断是否已经添加了statusBarView
            if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
                decorView.getChildAt(count - 1).setBackgroundColor(ArmsUtils.getColor(mContext, R.color.bg_title));
            } else {
                //新建一个和状态栏高宽的view
                StatusBarView statusView = ScreenUtils.createStatusBarView(mContext, ArmsUtils.getColor(mContext, R.color.bg_title));
                decorView.addView(statusView);
            }
            ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            //rootview不会为状态栏留出状态栏空间
            ViewCompat.setFitsSystemWindows(rootView, true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 设置是否使用状态栏占位，如果屏幕要全屏显示图片，则不需要。
     * m默认为true，使用状态栏占位，不会让标题栏太靠上被状态栏遮盖
     *
     * @param useStatusBarView
     */
    protected void setUseStatusBarView(boolean useStatusBarView) {
        this.useStatusBarView = useStatusBarView;
    }


    /**
     * 弹出NoticeDialog，级别为{@link NoticeLevelEnum#NORMAL},自定义title，隐藏message
     * 显示默认“确定、取消按钮”
     *
     * @param title 弹窗标题
     */
    protected NoticeDialog createNoticeDialog(@NonNull String title) {
        NoticeDialog dialog = new NoticeDialog.Builder(mContext)
                .title(title)
                .build();
        mWidgetList.add(dialog);
        return dialog;
    }

    /**
     * 弹出NoticeDialog，级别为{@link NoticeLevelEnum#NORMAL},自定义title、message，
     * 显示默认“确定、取消按钮”
     *
     * @param title   弹窗标题
     * @param message 弹窗内容
     */
    protected NoticeDialog createNoticeDialog(@NonNull String title, @NonNull String message) {
        NoticeDialog dialog = new NoticeDialog.Builder(mContext)
                .title(title).message(message)
                .build();
        mWidgetList.add(dialog);
        return dialog;
    }

    /**
     * 弹出NoticeDialog，级别为{@link NoticeLevelEnum#NORMAL},自定义title，隐藏message，
     * 只显示“确定”按钮
     *
     * @param title 弹窗标题
     */
    protected NoticeDialog createNoNegativeNoticeDialog(@NonNull String title) {
        NoticeDialog dialog = new NoticeDialog.Builder(mContext)
                .title(title)
                .negativeVisibility(false)
                .build();
        mWidgetList.add(dialog);
        return dialog;
    }

    /**
     * 弹出NoticeDialog，级别为{@link NoticeLevelEnum#NORMAL},自定义title、message，
     * 只显示“确定”按钮
     *
     * @param title   弹窗标题
     * @param message 弹窗内容
     */
    protected NoticeDialog createNoNegativeNoticeDialog(@NonNull String title, @NonNull String message) {
        NoticeDialog dialog = new NoticeDialog.Builder(mContext)
                .title(title)
                .message(message)
                .negativeVisibility(false)
                .build();
        mWidgetList.add(dialog);
        return dialog;
    }

    /**
     * 弹出NoticeDialog，级别为{@link NoticeLevelEnum#ERROR},自定义title，隐藏message，
     * 只显示“确定”按钮
     *
     * @param title 弹窗标题
     */
    protected NoticeDialog createErrorNoNegativeNoticeDialog(@NonNull String title) {
        NoticeDialog dialog = new NoticeDialog.Builder(mContext)
                .title(title).negativeVisibility(false)
                .level(NoticeLevelEnum.ERROR)
                .build();
        mWidgetList.add(dialog);
        return dialog;
    }

    /**
     * 弹出NoticeDialog，级别为{@link NoticeLevelEnum#ERROR},自定义title、message，
     * 只显示“确定”按钮
     *
     * @param title   弹窗标题
     * @param message 弹窗内容
     */
    protected NoticeDialog createErrorNoNegativeNoticeDialog(@NonNull String title, @NonNull String message) {
        NoticeDialog dialog = new NoticeDialog.Builder(mContext)
                .title(title).message(message)
                .negativeVisibility(false)
                .level(NoticeLevelEnum.ERROR)
                .build();
        mWidgetList.add(dialog);
        return dialog;
    }

    /**
     * 弹出NoticeDialog，级别为{@link NoticeLevelEnum#NORMAL},自定义title，message
     * 正负极按钮的Text自定义
     *
     * @param title        弹窗标题
     * @param message      弹窗内容
     * @param positiveText 正极按钮
     * @param negativeText 负极按钮
     * @return 返回创建好的NoticeDialog
     */
    protected NoticeDialog createNoticeDialog(@NonNull String title, String message, String positiveText, String negativeText) {
        NoticeDialog dialog = new NoticeDialog.Builder(mContext)
                .title(title)
                .message(message)
                .positiveText(positiveText)
                .negativeText(negativeText)
                .build();
        mWidgetList.add(dialog);
        return dialog;
    }

    /**
     * 弹出列表弹窗，无底部“确定”、“取消”按钮，默认为单选列表
     *
     * @param title   弹窗标题
     * @param adapter 列表适配器
     * @return
     */
    protected ListDialog createNoBottomListDialog(String title, BaseAdapter adapter) {
        ListDialog dialog = new ListDialog.Builder(mContext)
                .adapter(adapter)
                .bottomVisibility(false)
                .choiceType(ChoiceTypeEnum.SINGLE)
                .title(title)
                .build();
        mWidgetList.add(dialog);
        return dialog;
    }

    /**
     * 弹出列表弹窗，多选列表
     *
     * @param title   弹窗标题
     * @param adapter 列表适配器
     * @return
     */
    protected ListDialog createMultiChoiceListDialog(String title, BaseAdapter adapter) {
        ListDialog dialog = new ListDialog.Builder(mContext)
                .adapter(adapter)
                .bottomVisibility(true)
                .negativeVisibility(false)
                .positiveVisibility(true)
                .choiceType(ChoiceTypeEnum.MULTIPLE)
                .title(title)
                .build();
        mWidgetList.add(dialog);
        return dialog;
    }

    /**
     * 创建单输入框弹框，有“确定”、“取消”按钮，没有输入框左边的描述控件
     *
     * @param title   弹窗标题
     * @param hint    输入框的hint
     * @param content 输入框初始内容
     * @return
     */
    protected SingleInputDialog createSingInputDialog(String title, String hint, String content) {
        SingleInputDialog dialog = new SingleInputDialog.Builder(mContext)
                .descVisibility(false)
                .hint(hint)
                .title(title)
                .content(content)
                .build();
        mWidgetList.add(dialog);
        return dialog;
    }

    /**
     * 创建账号验证弹窗，双输入框弹窗
     *
     * @param message
     * @return
     */
    protected DoubleInputDialog createAccountVerify(String message) {
        String title = ArmsUtils.getString(mContext, R.string.title_accountVerify);
        boolean messageVisibility = TextUtils.isEmpty(message) ? false : true;
        DoubleInputDialog dialog = new DoubleInputDialog.Builder(mContext)
                .title(title)
                .message(message)
                .messageVisibility(messageVisibility)
                .hint1(ArmsUtils.getString(mContext, R.string.message_inputUserId))
                .hint2(ArmsUtils.getString(mContext, R.string.message_inputPassword))
                .build();
        mWidgetList.add(dialog);
        return dialog;
    }

    /**
     * 弹出普通的吐司
     *
     * @param message
     */
    protected void showToast(String message) {
        NoticeToast toast = new NoticeToast(mContext);
        mWidgetList.add(toast);
        toast.show(message, NoticeLevelEnum.NORMAL);
    }

    /**
     * 弹出错误提示的吐司
     *
     * @param message
     */
    protected void showErrorToast(String message) {
        NoticeToast toast = new NoticeToast(mContext);
        mWidgetList.add(toast);
        toast.show(message, NoticeLevelEnum.ERROR);
    }

    /**
     * 弹出普通吐司，不与Activity绑定，即Activity退出，吐司不会消失
     *
     * @param message
     */
    protected void showNotAttachToast(String message) {
        NoticeToast toast = new NoticeToast(mContext);
        toast.attachActivity(false);
        mWidgetList.add(toast);
        toast.show(message, NoticeLevelEnum.NORMAL);
    }

    /**
     * 弹出错误提示吐司，不与Activity绑定，即Activity退出，吐司不会消失
     *
     * @param message
     */
    protected void showNotAttachErrorToast(String message) {
        NoticeToast toast = new NoticeToast(mContext);
        toast.attachActivity(false);
        mWidgetList.add(toast);
        toast.show(message, NoticeLevelEnum.ERROR);
    }

    /**
     * 创建下拉列表的PopupWindow
     *
     * @param adapter             列表的Adapter
     * @param width               弹框的宽
     * @param height              弹框的高
     * @param anchor              弹框基于anchor控件弹出
     * @param xoff                弹框横向偏移量
     * @param yoff                弹框纵向偏移量
     * @param onItemClickListener 弹框列表条目点击监听
     * @return
     */
    protected ListPopupWindow createListPopupWindow(ListAdapter adapter, int width, int height, View anchor,
                                                    int xoff, int yoff, AdapterView.OnItemClickListener onItemClickListener) {
        ListPopupWindow popupWindow = new ListPopupWindow(mContext);
        popupWindow.setAdapter(adapter);
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        popupWindow.setAnchorView(anchor);
        popupWindow.setOnItemClickListener(onItemClickListener);
        popupWindow.setHorizontalOffset(xoff);
        popupWindow.setVerticalOffset(yoff);
        return popupWindow;
    }

    /**
     * 创建加载弹窗
     *
     * @return
     */
    public LoadingDialog createLoadingDialog() {
        return new LoadingDialog(mContext);
    }

    public DoubleInputDialog createDoubleInputDialog(String title, String message, String inputDesc1, String inputDesc2) {
        DoubleInputDialog dialog = new DoubleInputDialog.Builder(mContext)
                .title(title)
                .descVisibility1(true)
                .descVisibility2(true)
                .message(message)
                .messageVisibility(true)
                .inputDesc1(inputDesc1)
                .inputDesc2(inputDesc2)
                .build();
        mWidgetList.add(dialog);
        return dialog;
    }

    /**
     * 创建账号核对弹窗
     *
     * @param title
     * @param message
     * @return
     */
    public AccountVerifyDialog createAccountVerifyDialog(String title, String message) {
        AccountVerifyDialog dialog = new AccountVerifyDialog.Builder(mContext)
                .title(title)
                .message(message)
                .messageVisibility(true)
                .build();
        mWidgetList.add(dialog);
        return dialog;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseWidget();
    }

    /**
     * 释放掉自定义控件的一些资源，解绑等操作
     */
    private void releaseWidget() {
        Iterator<WidgetExtra> iterator = mWidgetList.iterator();
        while (iterator.hasNext()) {
            WidgetExtra widget = iterator.next();
            widget.release();
            iterator.remove();
            widget = null;
        }
        mWidgetList.clear();
        mWidgetList = null;
    }


    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 网络状态发生了改变
     *
     * @param event
     */
    @Subscribe
    public void onNetworkConnectivityChangeEventBus(Event event) {
        String id = event.getId();
        if (id.equals(EventIntentionEnum.CONNECTIVITY_CHANGE.getId())) {//网络状态发生了改变
            //TODO 根据网络状态做一些响应
        }
    }

    /**
     * 锁屏
     */
    protected void lock() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(KeyConstant.NAME_EXTRA_PAGE_TYPE, ValueConstant.VALUE_PAGE_TYPE_LOCK);
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    /**
     * 退出App
     */
    protected void exitApp() {
        ArmsUtils.exitApp();
    }

    /**
     * 重启APP
     */
    protected void restartApp() {
        Intent intent = new Intent(mContext, LoadingActivity.class);
        AppManager.getAppManager().startActivity(intent);
        exitApp();
    }
}
