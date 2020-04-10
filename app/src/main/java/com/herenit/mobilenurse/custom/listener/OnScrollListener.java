package com.herenit.mobilenurse.custom.listener;

import android.view.View;

/**
 * author: HouBin
 * date: 2019/2/26 15:17
 * desc: 滑动监听器
 */
public interface OnScrollListener {

    /**
     * 滑动监听
     *
     * @param view    正在发生滑动的View
     * @param originX 截止当前 view所处的坐标位置x轴的值
     * @param originY 截止当前 view所处的坐标位置y轴的值
     */
    default void onScroll(View view, int originX, int originY) {

    }

}
