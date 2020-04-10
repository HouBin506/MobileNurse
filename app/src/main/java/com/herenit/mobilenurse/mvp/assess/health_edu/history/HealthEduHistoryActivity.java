package com.herenit.mobilenurse.mvp.assess.health_edu.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.ScreenUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.HealthEduHistoryItem;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.custom.adapter.HealthEduHistoryAdapter;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.di.component.DaggerHealthEduHistoryComponent;
import com.herenit.mobilenurse.mvp.assess.health_edu.content.HealthEduContentActivity;
import com.herenit.mobilenurse.mvp.base.BasicBusinessActivity;
import com.herenit.mobilenurse.mvp.base.SwitchFragmentPagerPatientActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/9/27 9:01
 * desc: 健康宣教历史记录
 */
public class HealthEduHistoryActivity extends BasicBusinessActivity<HealthEduHistoryPresenter> implements HealthEduHistoryContract.View {

    @BindView(R2.id.srl_healthEduHistory_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    //列表控件（带有侧滑删除按钮）
    @BindView(R2.id.lv_healthEduHistory_list)
    SwipeMenuListView mHistoryListView;
    //列表数据
    @Inject
    List<HealthEduHistoryItem> mHistoryItemList;
    //列表适配器
    @Inject
    HealthEduHistoryAdapter mHistoryAdapter;

    //当前患者
    private Sickbed mSickbed;
    private LoadingDialog mLoadingDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHealthEduHistoryComponent
                .builder()
                .view(this)
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_TV;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_health_edu_history;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null)
            mSickbed = (Sickbed) intent.getSerializableExtra(KeyConstant.NAME_EXTRA_SICKBED);
        initView();
        mPresenter.loadHealthEduHistory(mSickbed, false);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        setTitleBarLeftOnClickListener(new View.OnClickListener() {//返回
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleBarCenterText(ArmsUtils.getString(mContext, R.string.title_activity_health_edu_history));
        if (mSickbed != null) {
            setTitleBarRightText1(mSickbed.getPatientTitle());
        }
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                mPresenter.loadHealthEduHistory(mSickbed, true);
            }
        });

        mHistoryListView.setAdapter(mHistoryAdapter);
        mHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HealthEduHistoryItem item = mHistoryItemList.get(position);
                Intent intent = new Intent(mContext, HealthEduContentActivity.class);
                intent.putExtra(KeyConstant.NAME_EXTRA_SICKBED, mSickbed);
                intent.putExtra(KeyConstant.NAME_EXTRA_DOC_ID, item.getDocId());
                intent.putExtra(KeyConstant.NAME_EXTRA_OPERATION_TYPE, CommonConstant.OPERATION_TYPE_UPDATE);
                launchActivity(intent);
            }
        });
        //侧滑菜单设置
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //添加“删除”条目
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
                deleteItem.setBackground(R.color.red);
                deleteItem.setTitleColor(ArmsUtils.getColor(mContext, R.color.white));
                deleteItem.setTitle(ArmsUtils.getString(mContext, R.string.btn_delete));
                deleteItem.setTitleSize((int) ScreenUtils.px2sp(mContext, ArmsUtils.getDimens(mContext, R.dimen.text_content_level_3)));
                deleteItem.setWidth(ArmsUtils.getDimens(mContext, R.dimen.dp_70));
                menu.addMenuItem(deleteItem);
            }
        };
        mHistoryListView.setMenuCreator(creator);
        mHistoryListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                HealthEduHistoryItem item = mHistoryItemList.get(position);
                if (item == null)
                    return false;
                switch (index) {
                    case 0://删除
                        mPresenter.deleteHealthEduHistory(item.getDocId());
                        break;
                }
                return false;
            }
        });
        //设置侧滑方向
        mHistoryListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
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

    @Override
    public void showError(@NonNull String message) {
        showErrorToast(message);
    }

    @Override
    public Sickbed sickbed() {
        return mSickbed;
    }
}
