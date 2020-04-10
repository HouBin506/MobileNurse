package com.herenit.mobilenurse.mvp.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.constant.ValueConstant;
import com.herenit.mobilenurse.criteria.entity.dict.ModuleDict;
import com.herenit.mobilenurse.criteria.enums.ModuleEnum;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.datastore.tempcache.DictTemp;
import com.herenit.mobilenurse.datastore.tempcache.SickbedTemp;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;
import com.herenit.mobilenurse.di.component.DaggerSinglePatientComponent;
import com.herenit.mobilenurse.mvp.assess.admission_assessment.AdmAssessActivity;
import com.herenit.mobilenurse.mvp.base.BaseMainActivity;
import com.herenit.mobilenurse.mvp.exam.report.ExamReportActivity;
import com.herenit.mobilenurse.mvp.assess.health_edu.HealthEduActivity;
import com.herenit.mobilenurse.mvp.lab.report.LabReportActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/3/1 9:30
 * desc: 单患者模块管理界面，是“患者详情”、“医嘱执行”、“体征采集”等Fragment的载体
 */
public class SinglePatientActivity extends BaseMainActivity<SinglePatientPresenter> implements SinglePatientContract.View {

    @BindView(R2.id.dl_single_patient)
    DrawerLayout mDrawerLayout;
    @BindView(R2.id.ll_main_content_drawer)
    LinearLayout mLm_drawerView;

    @BindView(R2.id.tv_main_drawer_userName)
    TextView mTv_userName;
    @BindView(R2.id.tv_main_drawer_deptName)
    TextView mTv_deptName;

    @BindView(R2.id.lv_drawerSinglePatient_moduleList)
    ListView mLv_secondModule;

    /**
     * 加载数据
     */
    @Override
    protected void loadData() {

    }


    /**
     * 设置界面
     */
    @Override
    protected void setView() {
        showModuleView(ValueConstant.MODULE_TYPE_SINGLE_PATIENT);
        initDrawerView();
    }

    /**
     * 初始化抽屉界面
     */
    private void initDrawerView() {
        //TODO 关闭抽屉侧滑弹出模式，单患者页面侧滑菜单主要放一些功能模块，等功能足够多的时候在展示抽屉
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mTv_userName.setText(UserTemp.getInstance().getUserName());
        mTv_deptName.setText(UserTemp.getInstance().getGroupName());
        List<ModuleDict> secondModuleList = DictTemp.getInstance().getSecondShowModuleList(ValueConstant.MODULE_TYPE_SINGLE_PATIENT);
        mLv_secondModule.setAdapter(new CommonAdapter<ModuleDict>(mContext, secondModuleList, R.layout.item_app_module) {
            @Override
            protected void convert(ViewHolder holder, ModuleDict item, int position) {
                holder.setImageResource(R.id.img_appModule_icon, item.getIconRes());
                holder.setText(R.id.tv_appModule_name, item.getModuleName());
            }
        });
        mLv_secondModule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewUtils.closeDrawer(mDrawerLayout, mLm_drawerView);
                ModuleDict module = secondModuleList.get(position);
                intoSecondModule(module);
            }
        });
    }

    /**
     * 根据侧滑抽屉选择，进入对应的，模块
     *
     * @param module
     */
    private void intoSecondModule(ModuleDict module) {
        String moduleId = module.getModuleId();
        Intent intent = new Intent();
        if (ModuleEnum.ADMISSION_ASSESSMENT.id().equals(moduleId)) {//入院评估
            intent.setClass(mContext, AdmAssessActivity.class);
            intent.putExtra(KeyConstant.NAME_EXTRA_SICKBED, SickbedTemp.getInstance().getCurrentSickbed());
            launchActivity(intent);
        } else if (ModuleEnum.HEALTH_EDUCATION.id().equals(moduleId)) {//健康宣教
            intent.setClass(mContext, HealthEduActivity.class);
            intent.putExtra(KeyConstant.NAME_EXTRA_SICKBED, SickbedTemp.getInstance().getCurrentSickbed());
            launchActivity(intent);
        } else if (ModuleEnum.EXAM_REPORT.id().equals(moduleId)) {//检查报告
            intent.setClass(mContext, ExamReportActivity.class);
            intent.putExtra(KeyConstant.NAME_EXTRA_SICKBED, SickbedTemp.getInstance().getCurrentSickbed());
            launchActivity(intent);
        } else if (ModuleEnum.LAB_REPORT.id().equals(moduleId)) {//检验报告
            intent.setClass(mContext, LabReportActivity.class);
            intent.putExtra(KeyConstant.NAME_EXTRA_SICKBED, SickbedTemp.getInstance().getCurrentSickbed());
            launchActivity(intent);
        }
    }

    /**
     * 模块切换
     *
     * @param moduleId
     */
    @Override
    protected void onModuleSwitched(@NonNull String moduleId) {

    }

    /**
     * 返回标题栏类型
     *
     * @return
     */
    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.NONE;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSinglePatientComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_single_patient;
    }

    @Override
    public void showMessage(@NonNull String message) {

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

        } else {//公共事件消费

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
