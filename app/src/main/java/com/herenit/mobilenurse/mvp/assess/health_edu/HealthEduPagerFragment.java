package com.herenit.mobilenurse.mvp.assess.health_edu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.EventBusUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.MultiListMenuItem;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.adapter.MultiListMenuAdapter;
import com.herenit.mobilenurse.custom.adapter.MultiListPagerAdapter;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.custom.widget.dialog.NoticeDialog;
import com.herenit.mobilenurse.custom.widget.layout.MultiListViewPager;
import com.herenit.mobilenurse.di.component.DaggerHealthEduComponent;
import com.herenit.mobilenurse.mvp.base.BasicCommonFragment;
import com.herenit.mobilenurse.mvp.base.SwitchFragmentPagerPatientActivity;
import com.herenit.mobilenurse.mvp.assess.health_edu.content.HealthEduContentActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * author: HouBin
 * date: 2019/8/15 14:40
 * desc:健康宣教页面
 */
public class HealthEduPagerFragment extends BasicCommonFragment<HealthEduPresenter> implements HealthEduContract.View {

    @BindView(R2.id.srl_healthEduPager_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R2.id.btn_healthEduPager_nextStep)
    Button mBtn_nextStep;//下一步

    @BindView(R2.id.view_healthEduPager_rootView)
    LinearLayout mRootView;
    @BindView(R2.id.vp_healthEduPager_viewPager)
    MultiListViewPager mViewPager;//列表页面
    MultiListPagerAdapter mAdapter;//列表页面适配器


    View mView1, mView2, mView3;//一、二、三级菜单页面
    MultiListMenuAdapter mAdapter1;//一级菜单适配器
    MultiListMenuAdapter mAdapter2;//二级菜单适配器
    MultiListMenuAdapter mAdapter3;//三级菜单适配器
    List<MultiListMenuItem> mData1 = new ArrayList<>();
    List<MultiListMenuItem> mData2 = new ArrayList<>();
    List<MultiListMenuItem> mData3 = new ArrayList<>();
    ListView mListView1, mListView2, mListView3;//一、二、三级菜单列表

    List<View> mViews = new ArrayList<>();
    private LoadingDialog mLoadingDialog;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHealthEduComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);

    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_health_edu_pager, container, false);
        return contentView;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();
        mPresenter.getHealthEduItemList(false);
    }

    private void initView() {
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                mPresenter.getHealthEduItemList(true);
            }
        });
        //点击下一步
        mBtn_nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toHealthEduContent();
            }
        });

        //设置预加载两个页面
        mViewPager.setOffscreenPageLimit(2);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mView1 = inflater.inflate(R.layout.pager_common_listview, null);
        mView2 = inflater.inflate(R.layout.pager_common_listview, null);
        mView3 = inflater.inflate(R.layout.pager_common_listview, null);
        //多级列表加载
        mListView1 = mView1.findViewById(R.id.lv_pager_commonListView);
        mListView2 = mView2.findViewById(R.id.lv_pager_commonListView);
        mListView3 = mView3.findViewById(R.id.lv_pager_commonListView);
        //一级菜单数据绑定
        mAdapter1 = new MultiListMenuAdapter(mContext, mData1);
        mListView1.setAdapter(mAdapter1);
        //菜单列表界面添加到页面
        mViews.add(mView1);
        mViews.add(mView2);//当前是第三级菜单，所以前面已经存在第一，第二菜单了

        //触屏监听
        mRootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });
        //一级菜单点击监听
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mViews.contains(mView3)) {
                    mViews.remove(mView3);
                    mViewPager.getAdapter().notifyDataSetChanged();//立即更新adapter数据
                }
                mAdapter1.setCurrentPosition(position);
                List<MultiListMenuItem> data2 = mPresenter.getNextLevelList(mData1.get(position).getPath());
                mData2.clear();
                if (data2 != null)
                    mData2.addAll(data2);
                if (mAdapter2 == null) {
                    mAdapter2 = new MultiListMenuAdapter(mContext, mData2);
                    mListView2.setAdapter(mAdapter2);
                } else {
                    //清空当前选中的条目
                    mAdapter2.setCurrentPosition(-1);
                }
            }
        });
        //二级菜单点击监听
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mViews.contains(mView3))
                    mViews.remove(mView3);
                mAdapter2.setCurrentPosition(position);
                List<MultiListMenuItem> data3 = mPresenter.getNextLevelList(mData2.get(position).getPath());
                mData3.clear();
                if (data3 != null)
                    mData3.addAll(data3);
                if (mAdapter3 == null) {
                    mAdapter3 = new MultiListMenuAdapter(mContext, mData3);
                    mListView3.setAdapter(mAdapter3);
                } else {
                    mAdapter3.setCurrentPosition(-1);
                }
                mViews.add(mView3);
                mAdapter.notifyDataSetChanged();

                if (mViewPager.getCurrentItem() != 1) {
                    mViewPager.setCurrentItem(1, true);//选一个
                }
            }
        });
        //三级菜单点击监听
        mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /**
     * 进入健康宣教内容页面，该页面将进行宣教内容的编辑
     */
    private void toHealthEduContent() {
        List<MultiListMenuItem> selectItemList = mPresenter.getSelectItemList();
        if (selectItemList == null || selectItemList.isEmpty()) {
            //没有选择要宣教的项目
            NoticeDialog dialog = createWarningNoticeDialog(ArmsUtils.getString(mContext, R.string.title_dialog_warning),
                    ArmsUtils.getString(mContext, R.string.message_NoHealthEduItemSelected_sureToNextStep));
            dialog.setPositiveNegativeListener(new PositiveNegativeListener() {
                @Override
                public void onPositive() {//确定
                    dialog.dismiss();
                    //跳转进入宣教内容页面
                    Intent intent = new Intent(mContext, HealthEduContentActivity.class);
                    if (!selectItemList.isEmpty())
                        intent.putExtra(KeyConstant.NAME_EXTRA_SELECTED_ITEM_LIST, (Serializable) selectItemList);
                    intent.putExtra(KeyConstant.NAME_EXTRA_SICKBED, getCurrentSickbed());
                    launchActivity(intent);
                }

                @Override
                public void onNegative() {//取消
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            //跳转进入宣教内容页面
            Intent intent = new Intent(mContext, HealthEduContentActivity.class);
            if (!selectItemList.isEmpty())
                intent.putExtra(KeyConstant.NAME_EXTRA_SELECTED_ITEM_LIST, (Serializable) selectItemList);
            intent.putExtra(KeyConstant.NAME_EXTRA_SICKBED, getCurrentSickbed());
            launchActivity(intent);
        }
    }


    @Override
    public void setData(@Nullable Object data) {

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

    /**
     * 接收EventBus消息
     *
     * @param event
     */
    @Subscribe
    public void onReceiveEvent(Event event) {
        String id = event.getId();
        if (TextUtils.isEmpty(id))
            return;
        String intention = EventBusUtils.getPrivateEventIntention(this.toString(), id);
        if (!TextUtils.isEmpty(intention)) {//私有事件消费
        } else {//公共事件

        }
    }

    @Override
    public Sickbed getCurrentSickbed() {
        return ((SwitchFragmentPagerPatientActivity) getActivity()).currentSickbed();
    }

    /**
     * 加载健康宣教列表成功
     */
    @Override
    public void getHealthEduItemListSuccess() {
        mRefreshLayout.finishRefresh();
        List<MultiListMenuItem> data1 = mPresenter.getLevel1List();
        mData1.clear();
        mData2.clear();
        mData3.clear();
        if (data1 != null)
            mData1.addAll(data1);
        mAdapter1.setCurrentPosition(-1);
        if (mAdapter2 != null)
            mAdapter2.setCurrentPosition(-1);
        if (mAdapter3 != null)
            mAdapter3.setCurrentPosition(-1);
        if (mViews.contains(mView3))
            mViews.remove(mView3);
        if (mAdapter == null) {
            mAdapter = new MultiListPagerAdapter(mViews, mPresenter.getMaxLevelNum());
            mViewPager.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (mViews.contains(mView1)) {
            mViewPager.setCurrentItem(0, true);
        }
    }

    @Override
    public void onDestroyView() {
        if (mViewPager != null)
            mViewPager.removeAllViews();
        mAdapter = null;
        mViews.clear();
        mAdapter1 = null;
        mAdapter2 = null;
        mAdapter3 = null;
        mData1.clear();
        mData2.clear();
        mData3.clear();
        super.onDestroyView();
    }
}
