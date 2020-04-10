package com.herenit.mobilenurse.app.utils;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.criteria.enums.ChoiceTypeEnum;
import com.herenit.mobilenurse.custom.widget.dialog.ListDialog;


/**
 * author: HouBin
 * date: 2019/1/28 13:31
 * desc: View的常规操作工具类
 */
public class ViewUtils {
    private ViewUtils() {
    }


    /**
     * 设置某个View的margin
     *
     * @param view   需要设置的view
     * @param isDp   需要设置的数值是否为DP
     * @param left   左边距
     * @param right  右边距
     * @param top    上边距
     * @param bottom 下边距
     * @return
     */
    public static ViewGroup.LayoutParams setViewMargin(View view, boolean isDp, int left, int right, int top, int bottom) {
        if (view == null) {
            return null;
        }
        int leftPx = left;
        int rightPx = right;
        int topPx = top;
        int bottomPx = bottom;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = null;
        //获取view的margin设置参数
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }

        //根据DP与PX转换计算值
        if (isDp) {
            leftPx = ScreenUtils.dip2px(view.getContext(), left);
            rightPx = ScreenUtils.dip2px(view.getContext(), right);
            topPx = ScreenUtils.dip2px(view.getContext(), top);
            bottomPx = ScreenUtils.dip2px(view.getContext(), bottom);
        }
        //设置margin
        marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);
        view.setLayoutParams(marginParams);
        return marginParams;
    }

    /**
     * 设置View 的显示、隐藏
     *
     * @param view
     * @param visibility true 则VISIBLE，false则GONE
     */
    public static void setViewVisibility(View view, boolean visibility) {
        if (visibility) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 设置ImageView的资源
     *
     * @param imageView
     * @param resId
     */
    public static void setImageResource(ImageView imageView, @DrawableRes int resId) {
        if (imageView != null)
            imageView.setImageResource(resId);
    }

    /**
     * 设置TextView的资源
     *
     * @param textView
     * @param text
     */
    public static void setText(TextView textView, String text) {
        if (textView != null)
            textView.setText(text);
    }

    /**
     * 设置TextView的资源
     *
     * @param textView
     * @param colorRes
     */
    public static void setTextColor(TextView textView, @ColorRes int colorRes) {
        if (textView != null && colorRes != 0)
            textView.setTextColor(ArmsUtils.getColor(textView.getContext(), colorRes));
    }

    /**
     * 给View设置点击事件
     *
     * @param view
     * @param onClickListener
     */
    public static void setOnClickListener(View view, View.OnClickListener onClickListener) {
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }

    /**
     * 打开抽屉
     *
     * @param drawer
     * @param drawerView
     */
    public static void openDrawer(DrawerLayout drawer, View drawerView) {
        if (drawer == null || drawerView == null)
            return;
        if (!drawer.isDrawerOpen(drawerView))
            drawer.openDrawer(drawerView);
    }

    /**
     * 关闭抽屉
     *
     * @param drawer
     * @param drawerView
     */
    public static void closeDrawer(DrawerLayout drawer, View drawerView) {
        if (drawer == null || drawerView == null)
            return;
        if (drawer.isDrawerOpen(drawerView))
            drawer.closeDrawer(drawerView);
    }

    /**
     * 判断View是否为可见 占位也算可见
     *
     * @param view
     * @return
     */
    public static boolean isVisibility(View view) {
        int visibility = view.getVisibility();
        return (visibility == View.VISIBLE) || (visibility == View.INVISIBLE);
    }

    public static void setBackgroundResource(View view, @DrawableRes int resId) {
        if (view != null && resId != 0) {
            view.setBackgroundResource(resId);
        }
    }

    /**
     * 创建下拉列表的PopupWindow
     *
     * @param adapter 列表的Adapter
     * @param width   弹框的宽
     * @param height  弹框的高
     * @param anchor  弹框基于anchor控件弹出
     * @param xoff    弹框横向偏移量
     * @param yoff    弹框纵向偏移量
     * @return
     */
    public static ListPopupWindow createListPopupWindow(Context context, ListAdapter adapter, int width, int height, View anchor,
                                                        int xoff, int yoff) {
        ListPopupWindow popupWindow = new ListPopupWindow(context);
        popupWindow.setAdapter(adapter);
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        popupWindow.setAnchorView(anchor);
        popupWindow.setHorizontalOffset(xoff);
        popupWindow.setVerticalOffset(yoff);
        return popupWindow;
    }

    /**
     * 弹出列表弹窗，无底部“确定”、“取消”按钮，默认为单选列表
     *
     * @param title   弹窗标题
     * @param adapter 列表适配器
     * @return
     */
    public static ListDialog createNoBottomListDialog(Context context, String title, BaseAdapter adapter) {
        ListDialog dialog = new ListDialog.Builder(context)
                .adapter(adapter)
                .bottomVisibility(false)
                .choiceType(ChoiceTypeEnum.SINGLE)
                .title(title)
                .build();
        return dialog;
    }
}
