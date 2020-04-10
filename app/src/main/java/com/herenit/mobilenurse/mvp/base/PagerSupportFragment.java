package com.herenit.mobilenurse.mvp.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.herenit.arms.mvp.BasePresenter;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.custom.widget.layout.NoCacheViewPager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * author: HouBin
 * date: 2019/3/4 10:20
 * desc: 支持翻页的Fragment，用于加载页面较多Fragment载体
 */
public abstract class PagerSupportFragment<T extends BasePresenter> extends BasicBusinessFragment<T> {

    private Unbinder mUnbinder;
    /**
     * 页面切换
     */
//    @BindView(R2.id.vp_pager_support)
//    protected ViewPager mViewPager;
    @BindView(R2.id.vp_pager_support)
    protected NoCacheViewPager mViewPager;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_pager_support, container, false);
        mUnbinder = ButterKnife.bind(this, contentView);
        mViewPager.setAdapter(pagerAdapter());
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                onSelectedPage(position);//页面切换
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
        mViewPager.setOnPageChangeListener(new NoCacheViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                onSelectedPage(position);//页面切换
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return contentView;
    }


    /**
     * 页面滑动结束，显示和滑动前不同的页面
     *
     * @param position
     */
    protected abstract void onSelectedPage(int position);

    /**
     * 返回页面切换的Adapter
     *
     * @param <T>
     * @return
     */
    @NonNull
    protected abstract <T extends PagerAdapter> T pagerAdapter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null)
            mUnbinder.unbind();
    }
}
