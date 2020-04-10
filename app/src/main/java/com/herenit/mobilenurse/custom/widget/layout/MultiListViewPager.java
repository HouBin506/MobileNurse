package com.herenit.mobilenurse.custom.widget.layout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * author: HouBin
 * date: 2019/9/9 17:12
 * desc:
 */
public class MultiListViewPager extends ViewPager {

    public MultiListViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiListViewPager(@NonNull Context context) {
        super(context);
    }

    //判断menu在x,y的位置
    public void scrollTo(int x, int y) {
        if (getAdapter() == null || x > getWidth() * (getAdapter().getCount() - 2)) {
            return;
        }
        super.scrollTo(x, y);
    }
}
