package com.herenit.mobilenurse.custom.widget.barview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * author: HouBin
 * date: 2019/1/31 11:16
 * desc: 状态栏占位，替代状态栏，解决4.4--5.0版版本沉浸式标题栏因为状态栏透明而上移，
 * 通过状态栏StatusBarView占位，可以让布局下移到原来位置
 */
public class StatusBarView extends View {
    public StatusBarView(Context context) {
        super(context);
    }

    public StatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StatusBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
