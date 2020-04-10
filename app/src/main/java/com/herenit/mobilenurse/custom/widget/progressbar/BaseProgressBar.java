package com.herenit.mobilenurse.custom.widget.progressbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.herenit.arms.utils.Preconditions;
import com.herenit.mobilenurse.custom.extra.WidgetExtra;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: HouBin
 * date: 2019/1/8 14:20
 * desc: ProgressBar 基类，提供一些基本的Java设置
 */
public abstract class BaseProgressBar extends LinearLayout implements WidgetExtra {
    protected Unbinder mUnbinder;
    protected Context mContext;

    public BaseProgressBar(Context context) {
        super(context);
        initLayout(context);
        mContext = context;
    }

    public BaseProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
        mContext = context;
    }

    public BaseProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    /**
     * 子类将布局文件返回，由父类初始化，返回0则不初始化
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化布局文件，并且绑定ButterKnife，子类需要自行使用@BindView的等注解获取控件
     *
     * @param context
     */
    private void initLayout(Context context) {
        View layout = LayoutInflater.from(context).inflate(Preconditions.checkIntGreaterThan(getLayoutId(), 0, "layoutId"), this);
        mUnbinder = ButterKnife.bind(this, layout);
        setView(layout);
    }

    /**
     * 调用此方法时，已经完成了页面加载，此方法主要设置ProgressBar的一些特性监听
     *
     * @param layout
     */
    public abstract void setView(View layout);

    public void show() {
        this.setVisibility(VISIBLE);
    }

    public void dismiss() {
        this.setVisibility(GONE);
    }

    @Override
    public void release() {
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
    }
}
