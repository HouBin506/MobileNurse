package com.herenit.mobilenurse.mvp.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.integration.AppManager;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.app.utils.ScreenUtils;
import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.criteria.common.ScanResult;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.constant.ValueConstant;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.dialog.ListDialog;
import com.herenit.mobilenurse.custom.widget.dialog.NoticeDialog;
import com.herenit.mobilenurse.custom.widget.progressbar.LoadingProgressBar;
import com.herenit.mobilenurse.di.component.DaggerLoginComponent;
import com.herenit.mobilenurse.criteria.entity.submit.Login;
import com.herenit.mobilenurse.custom.adapter.CommonTextImageAdapter;
import com.herenit.mobilenurse.mvp.base.BasicCommonActivity;
import com.herenit.mobilenurse.mvp.main.MultiPatientActivity;
import com.herenit.mobilenurse.mvp.setting.SystemSettingsActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/1/4 13:47
 * desc:登录、锁频、修改密码
 */
public class LoginActivity extends BasicCommonActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R2.id.til_login_userId)
    TextInputLayout mEt_userId;//账号
    @BindView(R2.id.til_login_password)
    TextInputLayout mEt_password;//密码
    @BindView(R2.id.til_login_passwordNew)
    TextInputLayout mEt_passwordNew;//新密码
    @BindView(R2.id.img_login_selectAccount)
    ImageView mImg_selectAccount;//选择账号
    @BindView(R2.id.cb_login_rememberPassword)
    CheckBox mCb_rememberPassword;//记住密码
    @BindView(R2.id.tv_login_clear)
    TextView mTv_clear;//清除输入框
    @BindView(R2.id.btn_login_login)
    Button mBtn_login;//登录
    @BindView(R2.id.pb_login_loading)
    LoadingProgressBar mPb_loading;//加载进度
    @BindView(R2.id.tv_login_systemSettings)
    TextView mTv_systemSettings;//系统设置
    @BindView(R2.id.rl_login_remember_and_clear)
    RelativeLayout mRL_rememberAndClear;//记住密码、清空父布局

    /**
     * 省人民登录时候先选择工作站
     */
    @BindView(R2.id.ll_login_selectDeptLayout)
    LinearLayout mLl_selectDeptLayout;
    @BindView(R2.id.ll_login_selectDept)
    LinearLayout mLl_selectDept;
    @BindView(R2.id.tv_login_selectDept)
    TextView mTv_selectDept;
    ListPopupWindow mSelectDeptWindow;
    @Inject
    List<User.MnUserVsGroupVPOJOListBean> mDeptList;
    @Inject
    CommonAdapter<User.MnUserVsGroupVPOJOListBean> mDeptAdapter;
    //当前准备登录的工作站
    private User.MnUserVsGroupVPOJOListBean mApplication;

    /**
     * 当前界面状态  登录、修改密码、锁屏
     */
    private String mPageType;
    @Inject
    List<String> mUserIdList;
    @Inject
    ArrayAdapter<String> mAutoCompleteAdapter;//输入账号输入框的自动补全弹出列表的Adapter

    @Inject
    List<Login> mLoginList;
    @Inject
    CommonTextImageAdapter<Login> mSelectAccountAdapter;//账号选择弹窗列表Adapter
    private ListDialog mSelectAccountDialog;

    @Inject
    AppManager mAppManager;

    /**
     * 是否为扫码登录，如果是，则不根据监控输入框来获取工作站列表
     * 否则，监控工作站获取工作站列表
     */
    private boolean isScanLogin = false;

    /**
     * 上一次输入框修改的时间
     */
    private long lastInputChanged = 0;
    /**
     * 上次输入的账号
     */
    private String lastInputUserId = "";
    /**
     * 启动定时任务，定时获取当前账号对应的工作站列表
     */
    private Timer mTimer;

    private final int MESSAGE_TIMER_LOGIN_INPUT = 10;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == MESSAGE_TIMER_LOGIN_INPUT) {
                String userId = mEt_userId.getEditText().getText().toString();
                if (TextUtils.isEmpty(userId)) {
                    setApplication(null);
                }
                if (TextUtils.isEmpty(userId) || !userId.equals(lastInputUserId)) //输入的账号为空，或者一秒钟之内，账号改变，则不去获取工作站列表
                    return;
                mPresenter.getApplicationList(userId, false);
            }
        }
    };

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent.builder()
                .view(this)
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        setUseStatusBarView(false);
        return R.layout.activity_login;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPageType = ValueConstant.VALUE_PAGE_TYPE_LOGIN;
        Intent intent = getIntent();
        if (intent != null && !TextUtils.isEmpty(intent.getStringExtra(KeyConstant.NAME_EXTRA_PAGE_TYPE))) {
            mPageType = intent.getStringExtra(KeyConstant.NAME_EXTRA_PAGE_TYPE);
        }
        switch (mPageType) {
            case ValueConstant.VALUE_PAGE_TYPE_SWITCH_ACCOUNT:
            case ValueConstant.VALUE_PAGE_TYPE_LOGIN://登录
                initLoginView();
                mPresenter.loadHistoryLogin();
                break;
            case ValueConstant.VALUE_PAGE_TYPE_CHANGE_PASSWORD://修改界面
                initChangePasswordView();
                break;
            case ValueConstant.VALUE_PAGE_TYPE_LOCK://锁屏界面
                initLockView();
                break;
        }
    }

    /**
     * 加载锁屏界面
     */
    private void initLockView() {
        mEt_userId.getEditText().setEnabled(false);
        mEt_userId.getEditText().setFocusable(false);
        mLl_selectDeptLayout.setVisibility(View.GONE);
        mEt_userId.setVisibility(View.VISIBLE);
        mEt_password.setVisibility(View.VISIBLE);
        mEt_passwordNew.setVisibility(View.INVISIBLE);
        mRL_rememberAndClear.setVisibility(View.INVISIBLE);
        mBtn_login.setVisibility(View.VISIBLE);
        mTv_systemSettings.setVisibility(View.INVISIBLE);
        mPb_loading.setVisibility(View.INVISIBLE);
        mBtn_login.setText(ArmsUtils.getString(mContext, R.string.btn_unlock));
        mEt_userId.getEditText().setText(UserTemp.getInstance().getUserId());
        //点击解锁按钮
        mBtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unlock();
            }
        });
    }

    /**
     * 解锁
     */
    private void unlock() {
        String userId = mEt_userId.getEditText().getText().toString().trim();
        String password = mEt_password.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(userId)) {
            showError(ArmsUtils.getString(mContext, R.string.error_login_emptyUserId));
            mEt_userId.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            showError(ArmsUtils.getString(mContext, R.string.error_login_emptyPassword));
            mEt_password.requestFocus();
            return;
        }
        mPresenter.unlock(userId, password);
    }

    /**
     * 加载修改密码界面
     */
    private void initChangePasswordView() {
        mEt_userId.getEditText().setEnabled(false);
        mEt_userId.getEditText().setFocusable(false);
        mEt_userId.setVisibility(View.VISIBLE);
        mEt_password.setVisibility(View.VISIBLE);
        mEt_passwordNew.setVisibility(View.VISIBLE);
        mRL_rememberAndClear.setVisibility(View.INVISIBLE);
        mImg_selectAccount.setVisibility(View.GONE);
        mBtn_login.setText(ArmsUtils.getString(mContext, R.string.btn_changePassword));
        mEt_passwordNew.getEditText().setHint(R.string.message_inputNewPassword);
        mPb_loading.setVisibility(View.INVISIBLE);
        mTv_systemSettings.setVisibility(View.GONE);
        mEt_userId.getEditText().setText(UserTemp.getInstance().getUserId());
        mLl_selectDeptLayout.setVisibility(View.GONE);
        //点击修改密码监听
        mBtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    /**
     * 修改密码
     */
    private void changePassword() {
        String userId = mEt_userId.getEditText().getText().toString();
        String oldPassword = mEt_password.getEditText().getText().toString();
        String newPassword = mEt_passwordNew.getEditText().getText().toString();
        if (TextUtils.isEmpty(userId)) {
            showError(ArmsUtils.getString(mContext, R.string.error_login_emptyUserId));
            return;
        } else if (TextUtils.isEmpty(oldPassword)) {
            showError(ArmsUtils.getString(mContext, R.string.error_login_emptyPassword));
            mEt_password.requestFocus();
            return;
        } else if (TextUtils.isEmpty(newPassword)) {
            showError(ArmsUtils.getString(mContext, R.string.error_login_emptyNewPassword));
            mEt_passwordNew.requestFocus();
            return;
        }
        mPresenter.changePassword(userId, oldPassword, newPassword);
    }

    /**
     * 加载登录界面
     */
    private void initLoginView() {
        mEt_userId.getEditText().setEnabled(true);
        mEt_userId.getEditText().setFocusable(true);
        mEt_userId.setVisibility(View.VISIBLE);
        mEt_password.setVisibility(View.VISIBLE);
        mEt_passwordNew.setVisibility(View.GONE);
        mImg_selectAccount.setVisibility(View.VISIBLE);
        mRL_rememberAndClear.setVisibility(View.VISIBLE);
        mPb_loading.setVisibility(View.INVISIBLE);
        mCb_rememberPassword.setChecked(mPresenter.isRememberPassword());
        mLl_selectDeptLayout.setVisibility(View.VISIBLE);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) mEt_userId.getEditText();
        autoCompleteTextView.setAdapter(mAutoCompleteAdapter);
        //账号自动补全列表Item点击监听
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userId = (String) parent.getItemAtPosition(position);
                autoCompleteLogin(mPresenter.getLoginByUserId(userId));
            }
        });
        //登录按钮点击监听
        mBtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.logout();
            }
        });
        //账号输入框右侧箭头点击监听
        mImg_selectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAccount();
            }
        });
        //点击清除按钮，清除掉输入框中的内容
        mTv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInput();
            }
        });
        //记住密码选项框选择监听
        mCb_rememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rememberPasswordChanged(isChecked);
            }
        });
        //点击系统设置按钮
        mTv_systemSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                systemSettings();
            }
        });
        //当手动输入账号时，则自动切换到非扫码登陆状态
        mEt_userId.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isScanLogin = false;
                return false;
            }
        });
        mEt_userId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            /**
             * 监控输入框内容，实时获取对应账号的工作站列表
             * @param s
             */
            @Override
            public void afterTextChanged(Editable s) {
                lastInputChanged = System.currentTimeMillis();
                lastInputUserId = mEt_userId.getEditText().getText().toString();
                if (isScanLogin)
                    return;
                closeLogin();//输入内容每次修改，都不允许马上点击登录，加载工作站列表之后在登陆
                if (mTimer == null)
                    mTimer = new Timer();
                mTimer.schedule(new TimerTask() {//启动定时任务，一秒钟之后调用一次
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(MESSAGE_TIMER_LOGIN_INPUT);
                    }
                }, 1000);
            }
        });
        //选择工作站
        mLl_selectDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectDeptWindow == null)
                    mSelectDeptWindow = createListPopupWindow(mDeptAdapter, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, mLl_selectDept,
                            0, 0, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSelectDeptWindow.dismiss();
                                    setApplication(mDeptList.get(position));
                                }
                            });
                mSelectDeptWindow.show();
            }
        });
    }

    /**
     * 系统设置
     */
    private void systemSettings() {
        Intent intent = new Intent(mContext, SystemSettingsActivity.class);
        intent.putExtra(KeyConstant.NAME_EXTRA_IS_LOGIN, false);
        launchActivity(intent);
    }

    /**
     * 是否记住密码
     *
     * @param remember
     */
    private void rememberPasswordChanged(boolean remember) {
        mPresenter.setRememberPassword(remember);
    }

    /**
     * 清除输入框
     */
    private void clearInput() {
        mEt_userId.getEditText().setText("");
        mEt_password.getEditText().setText("");
        mEt_userId.requestFocus();
    }


    /**
     * 单击输入框右侧向下箭头，弹出历史账号列表进行选择
     */
    private void selectAccount() {
        if (mSelectAccountDialog == null) {
            mSelectAccountDialog = createNoBottomListDialog(ArmsUtils.getString(mContext, R.string.title_dialog_selectUserId), mSelectAccountAdapter);
            mSelectAccountDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mSelectAccountDialog.dismiss();
                    autoCompleteLogin(mLoginList.get(position));
                }
            });
        }
        mSelectAccountDialog.show();
    }


    /**
     * 登录
     */
    private void login() {
        int height = mEt_password.getHeight();
        int dpHeight = ScreenUtils.px2dip(mContext, height);
        String userId = mEt_userId.getEditText().getText().toString().trim();
        String password = mEt_password.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(userId)) {
            showError(ArmsUtils.getString(mContext, R.string.error_login_emptyUserId));
            mEt_userId.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            showError(ArmsUtils.getString(mContext, R.string.error_login_emptyPassword));
            mEt_password.requestFocus();
            return;
        } else if (mApplication == null) {
            showError(ArmsUtils.getString(mContext, R.string.error_login_canNotFoundLoginApplication));
            return;
        }
        mPresenter.login(userId, password, mApplication.getGroupCode());
    }

    @Override
    public void showMessage(@NonNull String message) {
        showToast(message);
    }

    /**
     * 自动补全登录信息
     *
     * @param login
     */
    @Override
    public void autoCompleteLogin(Login login) {
        if (login == null)
            return;
        String userId = login.getUserId() == null ? "" : login.getUserId();
        String password = login.getPassword() == null ? "" : login.getPassword();
        EditText etUserId = mEt_userId.getEditText();
        EditText etPassword = mEt_password.getEditText();
        etUserId.setText(userId);
        etPassword.setText(password);
        mEt_password.requestFocus();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                etPassword.getWindowToken(), 0
        );
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    /**
     * 登录成功
     */
    @Override
    public void loginSuccess() {
        //非手术室登录
        launchActivity(new Intent(mContext, MultiPatientActivity.class));
        finish();
    }

    /**
     * 登出成功，在修改密码成功、切换账号时，需要将之前的账号登出
     */
    @Override
    public void logoutSuccess() {
        switch (mPageType) {
            case ValueConstant.VALUE_PAGE_TYPE_LOGIN://当前处于登录状态
            case ValueConstant.VALUE_PAGE_TYPE_SWITCH_ACCOUNT://切换账号与登录时一样的
                login();
                break;
            case ValueConstant.VALUE_PAGE_TYPE_CHANGE_PASSWORD://修改密码
                changePasswordSuccess();
                break;
        }
    }

    /**
     * 修改密码成功
     */
    private void changePasswordSuccess() {
        NoticeDialog dialog = createNoticeDialog(ArmsUtils.getString(mContext, R.string.title_dialog_changePasswordSuccess),
                ArmsUtils.getString(mContext, R.string.message_login_again));
        dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
            @Override
            public void onPositive() {
                dialog.dismiss();
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.putExtra(KeyConstant.NAME_EXTRA_PAGE_TYPE, ValueConstant.VALUE_PAGE_TYPE_LOGIN);
                launchActivity(intent);
                killMyself();
            }

            @Override
            public void onNegative() {
                exitApp();
            }
        });
        dialog.show();
        mAppManager.killAll(this.getClass());

    }

    /**
     * 解锁成功
     */
    @Override
    public void unlockSuccess() {
        killMyself();
    }

    /**
     * 打开登陆
     */
    @Override
    public void openLogin() {
        mBtn_login.setEnabled(true);
    }

    /**
     * 关闭登录
     */
    @Override
    public void closeLogin() {
        mBtn_login.setEnabled(false);
    }

    /**
     * 设置要登陆的工作站
     *
     * @param application
     */
    @Override
    public void setApplication(User.MnUserVsGroupVPOJOListBean application) {
        mApplication = application;
        if (application == null)
            mTv_selectDept.setText("");
        else
            mTv_selectDept.setText(application.getGroupName());
    }

    /**
     * 免密登录
     */
    @Override
    public void loginWithoutPassword() {
        String loginName = mEt_userId.getEditText().getText().toString();
        if (TextUtils.isEmpty(loginName)) {
            showError(ArmsUtils.getString(mContext, R.string.error_login_emptyUserId));
            mEt_userId.requestFocus();
            return;
        }
        if (mApplication == null) {//没有工作站
            showError(ArmsUtils.getString(mContext, R.string.error_login_canNotFoundLoginApplication));
            return;
        }
        mPresenter.login(loginName, "", mApplication.getGroupCode());
    }


    @Override
    public void showLoading() {
        mPb_loading.show();
    }

    @Override
    public void hideLoading() {
        mPb_loading.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void showError(@NonNull String message) {
        showErrorToast(message);
    }

    /**
     * 接收EventBus事件消费
     *
     * @param event
     */
    @Subscribe
    public void onEventBusReceive(Event event) {
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (!TextUtils.isEmpty(intention)) {//私有事件消费
            if (intention.equals(CommonConstant.EVENT_INTENTION_REMOVE_USER)) {//删除本地某账号
                int position = (int) event.getMessage();
                mPresenter.removeLogin(mLoginList.get(position));
            }
        } else {//公共事件消费

        }
    }

    @Override
    protected void onDestroy() {
        mPb_loading.release();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        super.onDestroy();
    }

    @Override
    public void killMyself() {
        finish();
    }

    /**
     * 登录界面按返回键，如果是修改密码或者是登录界面，则要先登出，如果是锁屏，则不允许返回
     */
    @Override
    public void onBackPressed() {
        if (mPageType.equals(ValueConstant.VALUE_PAGE_TYPE_LOCK)) {//锁频
            return;
        }
        super.onBackPressed();
    }

    /**
     * 扫描工牌
     *
     * @param event
     */
    @Subscribe
    public void onScanEmpCard(Event event) {
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (TextUtils.isEmpty(intention)) {//未找到私有事件，故为共有消息事件
            if (id.equals(EventIntentionEnum.CODE_TYPE_EMP_CARD.getId())) {//扫描到员工牌
                ScanResult scanResult = (ScanResult) event.getMessage();
                String loginName = scanResult.getLoginName();
                scanEmpCardLogin(loginName);
            } else {
                showErrorToast(ArmsUtils.getString(mContext, R.string.message_pleaseScanEmpCard));
            }
        }
    }

    /**
     * 扫描员工牌登录
     */
    private void scanEmpCardLogin(String loginName) {
        if (!ValueConstant.VALUE_PAGE_TYPE_LOGIN.equals(mPageType) && !ValueConstant.VALUE_PAGE_TYPE_SWITCH_ACCOUNT.equals(mPageType))
            return;
        clearInput();
        if (TextUtils.isEmpty(loginName)) {
            showErrorToast(ArmsUtils.getString(mContext, R.string.error_login_emptyUserId));
            return;
        }
        //切换到扫码登录状态
        isScanLogin = true;
        mEt_userId.getEditText().setText(loginName);
        mPresenter.getApplicationList(loginName, true);
    }
}

