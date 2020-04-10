package com.herenit.mobilenurse.mvp.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.api.Api;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.constant.ValueConstant;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.custom.widget.dialog.SingleInputDialog;
import com.herenit.mobilenurse.datastore.sp.ConfigSp;
import com.herenit.mobilenurse.datastore.tempcache.DictTemp;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.custom.adapter.CommonTextAdapter;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.custom.widget.dialog.NoticeDialog;
import com.herenit.mobilenurse.di.component.DaggerMultiPatientComponent;
import com.herenit.mobilenurse.mvp.base.BaseMainActivity;
import com.herenit.mobilenurse.mvp.login.LoginActivity;
import com.herenit.mobilenurse.mvp.setting.BedRangeActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.herenit.arms.utils.Preconditions.checkNotNull;


public class MultiPatientActivity extends BaseMainActivity<MultiPatientPresenter> implements MultiPatientContract.View, View.OnClickListener {

    @BindView(R2.id.dl_main_drawer)
    DrawerLayout mDrawerLayout;//整体布局
    @BindView(R2.id.ll_main_content_drawer)
    LinearLayout mLl_drawer;//抽屉布局

    private LoadingDialog mLoadingDialog;

    @BindView(R2.id.tv_main_drawer_deptName)
    TextView mTv_deptName;//当前科室名称
    @BindView(R2.id.tv_main_drawer_userName)
    TextView mTv_userName;//用户名

    //设置
    @BindView(R2.id.tv_systemSetting_lock)
    TextView mTv_lock;//锁屏
    @BindView(R2.id.tv_systemSetting_switchUser)
    TextView mTv_switchUser;//切换账号
    @BindView(R2.id.tv_systemSetting_changPassWord)
    TextView mTv_changPassWord;//修改密码
    @BindView(R2.id.tv_systemSetting_bedRange)
    TextView mTv_bedRange;//床位范围
    @BindView(R2.id.tv_systemSetting_webServiceIP)
    TextView mTv_webServiceIp;//服务器地址
    @BindView(R2.id.rl_systemSetting_webServiceIp)
    RelativeLayout mRl_webService;
    @BindView(R2.id.cb_systemSetting_scanSetting)
    CheckBox mCb_scanSetting;//扫描设置

    /**
     * 选择科室或者病区列表Adapter
     */
    @Inject
    CommonTextAdapter mGroupListAdapter;
    private ListPopupWindow mLpw_group;//选择科室或者病区弹窗
    @Inject
    List<String> mGroupTextList;//选择科室或者病区的Adapter数据


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMultiPatientComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_multi_patient;
    }


    /**
     * 设置抽屉布局
     */
    private void setDrawerLayout() {
        setTitleBarLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.openDrawer(mDrawerLayout, mLl_drawer);
            }
        });
        mTv_deptName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectGroup();
            }
        });
        String address = ConfigSp.getInstance().getServiceAddress();
        address = TextUtils.isEmpty(address) ? Api.IP_PORT : address;
        mTv_webServiceIp.setText(address);
        mTv_lock.setOnClickListener(this);
        mTv_switchUser.setOnClickListener(this);
        mTv_changPassWord.setOnClickListener(this);
        mTv_bedRange.setOnClickListener(this);
        mRl_webService.setOnClickListener(this);
    }

    /**
     * 显示科室列表
     */
    private void showSelectGroup() {
        if (mLpw_group == null) {
            int width = ArmsUtils.getDimens(mContext, R.dimen.app_drawer_width) - mTv_deptName.getLeft();
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mLpw_group = createListPopupWindow(mGroupListAdapter, width, height, mTv_deptName, 0, 0, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mPresenter.switchGroup(position);
                }
            });
        }
        mLpw_group.show();
        mLpw_group.getListView().setSelection(getCurrentGroupPosition());
    }


    @Override
    protected void loadData() {
        mPresenter.getUser();
    }

    protected int getCurrentGroupPosition() {
        return mPresenter.getCurrentGroupPosition();
    }

    /**
     * 切换科室
     */
    @Override
    public void switchGroup() {
        mTv_deptName.setText(UserTemp.getInstance().getGroupName());
        int position = getCurrentGroupPosition();
        mGroupListAdapter.setSelectItem(position);
        mGroupListAdapter.notifyDataSetChanged();
        mLpw_group.dismiss();
        ViewUtils.closeDrawer(mDrawerLayout, mLl_drawer);
        EventBusUtils.post(EventIntentionEnum.SWITCH_GROUP.getId(), null);
    }

    @Override
    protected void setView() {
        setTitleBarLeftImage(R.mipmap.ic_title_list);
        setTitleBarRightImage1(R.mipmap.ic_title_scan);
        //设置抽屉布局
        setDrawerLayout();
    }

    /**
     * 模块发生切换
     *
     * @param moduleId
     */
    @Override
    protected void onModuleSwitched(@NonNull String moduleId) {
        String moduleName = DictTemp.getInstance().getModuleNameById(moduleId);
        setTitleBarCenterText(moduleName);
    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    /**
     * 设置主界面的标题栏为左、中、右：ImageView、TextView、ImageView
     *
     * @return
     */
    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_IMG;
    }

    /**
     * 返回键点击监听
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mLl_drawer)) {
            ViewUtils.closeDrawer(mDrawerLayout, mLl_drawer);
            return;
        }
        //将APP退到后台，但不关闭
//        moveTaskToBack(false);

        //返回键退出APP
        NoticeDialog dialog = createNoticeDialog(ArmsUtils.getString(mContext, R.string.title_dialog_exitApp),
                ArmsUtils.getString(mContext, R.string.message_sure_exitApp));
        dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
            @Override
            public void onPositive() {
                dialog.dismiss();
                //确定退出APP
                exitApp();
            }

            @Override
            public void onNegative() {
                //取消退出APP
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    /**
     * 界面展示
     */
    @Override
    public void showView() {
        setUserNameAndGroupName();
        showModuleView();//患者列表加载缓存完成，显示模块页面
        //TODO 暂时不加载患者列表
//        mPresenter.loadSickbedList(false);
    }

    protected void setUserNameAndGroupName() {
        mTv_userName.setText(UserTemp.getInstance().getUserName());
        mTv_deptName.setText(UserTemp.getInstance().getGroupName());
    }

    @Override
    public void showModuleView() {
        showModuleView(ValueConstant.MODULE_TYPE_MULTI_PATIENT);
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
     * 点击事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_systemSetting_lock) {//锁定
            onLockClick();
        } else if (id == R.id.tv_systemSetting_switchUser) {//切换用户
            onUserChanged();
        } else if (id == R.id.tv_systemSetting_changPassWord) {//修改密码
            onChangPassword();
        } else if (id == R.id.tv_systemSetting_bedRange) {//床位范围
            launchActivity(new Intent(mContext, BedRangeActivity.class));
        } else if (id == R.id.rl_systemSetting_webServiceIp) {//服务地址配置
            onSettingWebServiceIp();
        }
    }

    /**
     * 设置服务器端IP
     */
    private void onSettingWebServiceIp() {
        SingleInputDialog dialog = createSingInputDialog(ArmsUtils.getString(mContext, R.string.btn_serviceAddress),
                ArmsUtils.getString(mContext, R.string.message_inputServiceAddress), mTv_webServiceIp.getText().toString());
        dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
            @Override
            public void onPositive(Object... backData) {
                String address = (String) backData[0];
                if (!StringUtils.isNetworkAddress(address)) {
                    showError(ArmsUtils.getString(mContext, R.string.error_serviceAddress_formatError));
                } else {
                    dialog.dismiss();
                    mTv_webServiceIp.setText(address);
                    ConfigSp.getInstance().setServiceAddress(address);
                    restartApp();
                }
            }

            @Override
            public void onNegative() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showError(@NonNull String message) {
        showErrorToast(message);
    }

    /**
     * 修改密码
     */
    private void onChangPassword() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(KeyConstant.NAME_EXTRA_PAGE_TYPE, ValueConstant.VALUE_PAGE_TYPE_CHANGE_PASSWORD);
        launchActivity(intent);
    }

    /**
     * \锁屏
     */
    protected void onLockClick() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(KeyConstant.NAME_EXTRA_PAGE_TYPE, ValueConstant.VALUE_PAGE_TYPE_LOCK);
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    /**
     * 切换账号
     */
    protected void onUserChanged() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(KeyConstant.NAME_EXTRA_PAGE_TYPE, ValueConstant.VALUE_PAGE_TYPE_SWITCH_ACCOUNT);
        launchActivity(intent);
    }


    /**
     * 退出APP
     */
    protected void onLogoutClick() {
        NoticeDialog dialog = createNoticeDialog(ArmsUtils.getString(mContext, R.string.title_dialog_exitApp),
                ArmsUtils.getString(mContext, R.string.message_sure_exitApp));
        dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
            @Override
            public void onPositive() {
                dialog.dismiss();
                //确定退出APP
                exitApp();
            }

            @Override
            public void onNegative() {
                //取消退出APP
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
