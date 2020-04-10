package com.herenit.mobilenurse.custom.widget.layout;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by HouBin on 2017/11/28.
 * 设置RecyclerView GridLayoutManager or StaggeredGridLayoutManager spacing
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        //TODO 这里去掉判断，否则会出现不对齐的情况
//        if (parent.getChildPosition(view) - 1 == 0)
        outRect.top = space;
    }

}
