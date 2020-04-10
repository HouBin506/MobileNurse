package com.herenit.mobilenurse.custom.widget.layout;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

import com.herenit.mobilenurse.custom.listener.OnScrollListener;

/**
 * author: HouBin
 * date: 2019/2/26 15:08
 * desc:自定义的ScrollView，实现一些额外功能，例如滑动监听
 */
public class MNScrollView extends ScrollView {

    private int slop;
    private int touch;

    private OnScrollListener mOnScrollListener;

    public MNScrollView(Context context) {
        super(context);
        setSlop(context);
    }

    public MNScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSlop(context);
    }

    public MNScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSlop(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MNScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setSlop(context);
    }

    /**
     * 是否intercept当前的触摸事件
     *
     * @param ev 触摸事件
     * @return true：调用onMotionEvent()方法，并完成滑动操作
     */
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                //  保存当前touch的纵坐标值
//                touch = (int) ev.getRawY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //  滑动距离大于slop值时，返回true
//                if (Math.abs((int) ev.getRawY() - touch) > slop) return true;
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

    /**
     * 获取相应context的touch slop值（即在用户滑动之前，能够滑动的以像素为单位的距离）
     *
     * @param context ScrollView对应的context
     */
    private void setSlop(Context context) {
        slop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollListener != null)
            mOnScrollListener.onScroll(this, l, t);
    }


}
