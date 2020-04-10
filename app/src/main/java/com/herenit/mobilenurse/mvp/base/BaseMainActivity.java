package com.herenit.mobilenurse.mvp.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.herenit.arms.mvp.BasePresenter;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.dict.ModuleDict;
import com.herenit.mobilenurse.criteria.enums.ModuleEnum;
import com.herenit.mobilenurse.custom.widget.barview.BottomBar;
import com.herenit.mobilenurse.custom.widget.barview.BottomBarTab;
import com.herenit.mobilenurse.datastore.tempcache.DictTemp;
import com.herenit.mobilenurse.mvp.assess.admission_assessment.AdmAssessActivity;
import com.herenit.mobilenurse.mvp.monitor.MonitorFragment;
import com.herenit.mobilenurse.mvp.nursing_round.NurseRoundFragment;
import com.herenit.mobilenurse.mvp.operation.OperationScheduledFragment;
import com.herenit.mobilenurse.mvp.orders.OrderListFragment;
import com.herenit.mobilenurse.mvp.other.OtherFragment;
import com.herenit.mobilenurse.mvp.patient.PatientInfoFragment;
import com.herenit.mobilenurse.mvp.sickbed.SickbedListFragment;
import com.herenit.mobilenurse.mvp.vital_signs.VitalSignsRecordFragment;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/3/1 9:59
 * desc: 主界面的基类
 * 这里的主界面，指的是拥有选择科室和系统设置的抽屉，并且此Activity是Fragment功能模块的载体
 * 比如我们的MultiPatientActivity（多患者功能）、SinglePatientActivity（单患者功能）等就是集成此基类；
 * 封装了抽屉的设置，页面基本模块的加载等基本功能
 */
public abstract class BaseMainActivity<P extends BasePresenter> extends BasicBusinessActivity<P> {

    @BindView(R2.id.bb_main_bottomBar)
    BottomBar mBb_bottom;//底部栏

    @Inject
    FragmentManager mFragmentManager;

    private String mModuleId;


    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {//获取页面默认加载的模块
            mModuleId = intent.getStringExtra(KeyConstant.NAME_EXTRA_MODULE_ID);
        }
        setView();
        setBottomLayout();
        loadData();
    }

    /**
     * 加载数据
     */
    protected abstract void loadData();

    protected abstract void setView();

    /**
     * 模块切换，子类做出相应处理
     *
     * @param moduleId
     */
    protected abstract void onModuleSwitched(@NonNull String moduleId);

    /**
     * 设置底部布局
     */
    private void setBottomLayout() {
        //功能模块按钮点击切换监听
        mBb_bottom.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                String tag = (String) mBb_bottom.getItem(position).getTag();
                showModuleFragment(tag);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    /**
     * 显示模块Fragment页面
     *
     * @param moduleId 显示功能模块页面的对应Id
     */
    private void showModuleFragment(@NonNull String moduleId) {
        mModuleId = moduleId;
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        for (int x = 0; x < mBb_bottom.size(); x++) {
            String tag = (String) mBb_bottom.getItem(x).getTag();
            Fragment fragment = mFragmentManager.findFragmentByTag(tag);
            if (fragment != null && !moduleId.equals(tag))
                fragmentTransaction.hide(fragment);
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(moduleId);
        if (fragment == null || fragment.isDetached()) {
            if (moduleId.equals(ModuleEnum.SICKBED_LIST.id())) {//床位一览
                fragment = new SickbedListFragment();
            } else if (moduleId.equals(ModuleEnum.OPERATION_SCHEDULED.id())) {//手术安排
                fragment = new OperationScheduledFragment();
            } else if (moduleId.equals(ModuleEnum.OTHER.id())) {//其他
                fragment = new OtherFragment();
            } else if (moduleId.equals(ModuleEnum.PATIENT_INFO.id())) {//患者详情
                fragment = new PatientInfoFragment();
            } else if (moduleId.equals(ModuleEnum.ORDERS.id())) {//医嘱
                fragment = new OrderListFragment();
            } else if (moduleId.equals(ModuleEnum.VITAL_SIGNS.id())) {//体征
                fragment = new VitalSignsRecordFragment();
            } else if (moduleId.equals(ModuleEnum.NURSE_ROUND.id())) {//护理巡视
                fragment = new NurseRoundFragment();
            } else if (moduleId.equals(ModuleEnum.MONITOR.id())) {//监护仪绑定
                fragment = new MonitorFragment();
            }
            if (fragment != null)
                fragmentTransaction.add(R.id.fl_main_container_fragment, fragment, moduleId);
        } else {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commit();
        onModuleSwitched(moduleId);
    }

    /**
     * \初始化主页面要显示的模块
     *
     * @param moduleType 模块类型，比如是单患者的还是多患者的
     */
    protected void showModuleView(@NonNull String moduleType) {
        Map<String, ModuleDict> moduleMap = DictTemp.getInstance().getShowModule(moduleType);
        if (moduleMap == null || moduleMap.isEmpty())
            return;
        for (Map.Entry<String, ModuleDict> entry : moduleMap.entrySet()) {
            String id = entry.getKey();
            String name = entry.getValue().getModuleName();
            int iconRes = entry.getValue().getIconRes();
            BottomBarTab tab = new BottomBarTab(mContext, iconRes, name);
            tab.setTag(id);
            mBb_bottom.addItem(tab);
        }
        if (TextUtils.isEmpty(mModuleId) && mBb_bottom.size() > 0)
            mModuleId = (String) mBb_bottom.getItem(0).getTag();
        if (mBb_bottom.size() <= 1)
            mBb_bottom.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mModuleId)) {
            showModuleFragment(mModuleId);
        }
    }

}
