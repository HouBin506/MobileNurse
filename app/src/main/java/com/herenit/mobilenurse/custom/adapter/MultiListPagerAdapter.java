package com.herenit.mobilenurse.custom.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HouBin
 * date: 2019/9/9 14:57
 * desc:多级列表适配器
 */
public class MultiListPagerAdapter extends PagerAdapter {
    private List<View> mViews;
    /**
     * 最大页面数，每一个页面代表一级，多级列表则有多个页面。
     * 当列表的级数大于一，则表示多级列表，此时页面的宽度设为屏幕宽度的一半
     */
    private int mMaxPageCount = 1;

    public MultiListPagerAdapter(List<View> views, int maxPageCount) {
        mViews = views;
        mMaxPageCount = maxPageCount;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mViews.get(position);
        container.addView(view);
        return view;
    }

    /**
     * 当列表的级数大于一，则表示多级列表，此时页面的宽度设为屏幕宽度的一半
     */
    @Override
    public float getPageWidth(int position) {
        if (mMaxPageCount > 1)
            return (float) 0.5;
        return (float) 1;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
