package com.herenit.mobilenurse.mvp.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.herenit.arms.base.BaseFragment;
import com.herenit.arms.integration.AppManager;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.constant.ValueConstant;
import com.herenit.mobilenurse.criteria.enums.ChoiceTypeEnum;
import com.herenit.mobilenurse.criteria.enums.EventIntentionEnum;
import com.herenit.mobilenurse.criteria.enums.NoticeLevelEnum;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.criteria.message.eventbus.Event;
import com.herenit.mobilenurse.custom.extra.WidgetExtra;
import com.herenit.mobilenurse.custom.widget.dialog.ListDialog;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.custom.widget.dialog.NoticeDialog;
import com.herenit.mobilenurse.custom.widget.dialog.SingleInputDialog;
import com.herenit.mobilenurse.custom.widget.toast.NoticeToast;
import com.herenit.mobilenurse.mvp.launch.LoadingActivity;
import com.herenit.mobilenurse.mvp.login.LoginActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

import static com.herenit.arms.utils.Preconditions.checkNotNull;

/**
 * author: HouBin
 * date: 2019/1/28 13:44
 * desc: 封装一些APP特有功能，比如对Title的处理
 * 如果需要框架支持Fragment页面的TitleBar，则继承此类，并且界面要引入{@link R.layout#include_title}
 */
public abstract class BasicBusinessFragment<P extends BasePresenter> extends BasicCommonFragment<P> {
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitleBar(titleBarType());
        init(savedInstanceState);
    }

    /**
     * 初始化数据、界面
     *
     * @param savedInstanceState
     */
    protected abstract void init(Bundle savedInstanceState);

    /*****************************************标题栏***************************************************************/


    @BindView(R2.id.rl_title)
    RelativeLayout mRl_titleLayout;
    @BindView(R2.id.rl_title_left)
    RelativeLayout mRl_title_left;
    @BindView(R2.id.img_title_left)
    ImageView mImg_title_left;
    @BindView(R2.id.tv_title_left)
    TextView mTv_title_left;
    @BindView(R2.id.rl_title_center)
    RelativeLayout mRl_title_center;
    @BindView(R2.id.tv_title_center)
    TextView mTv_title_center;
    @BindView(R2.id.ll_title_right)
    LinearLayout mLl_title_right;
    @BindView(R2.id.rl_title_right1)
    RelativeLayout mRl_title_right1;
    @BindView(R2.id.img_title_right1)
    ImageView mImg_title_right1;
    @BindView(R2.id.tv_title_right1)
    TextView mTv_title_right1;
    @BindView(R2.id.rl_title_right2)
    RelativeLayout mRl_title_right2;
    @BindView(R2.id.img_title_right2)
    ImageView mImg_title_right2;
    @BindView(R2.id.tv_title_right2)
    TextView mTv_title_right2;
    @BindView(R2.id.rl_title_right3)
    RelativeLayout mRl_title_right3;
    @BindView(R2.id.img_title_right3)
    ImageView mImg_title_right3;
    @BindView(R2.id.tv_title_right3)
    TextView mTv_title_right3;

    protected TextView getTitleBarCenterTextView() {
        return mTv_title_center;
    }


    /**
     * 设置title左侧图标
     *
     * @param resId
     */

    protected void setTitleBarLeftImage(@DrawableRes int resId) {
        ViewUtils.setImageResource(mImg_title_left, resId);
    }

    /**
     * 设置title左边TextView
     *
     * @param text
     */
    protected void setTitleBarLeftText(@NonNull String text) {
        ViewUtils.setText(mTv_title_left, text);
    }

    /**
     * 设置title中间文字
     *
     * @param text
     */
    protected void setTitleBarCenterText(@NonNull String text) {
        ViewUtils.setText(mTv_title_center, text);
    }

    /**
     * 设置标题栏右侧Image1
     *
     * @param resId
     */
    protected void setTitleBarRightImage1(@DrawableRes int resId) {
        ViewUtils.setImageResource(mImg_title_right1, resId);
    }

    /**
     * 设置标题栏右侧Image3
     *
     * @param resId
     */
    protected void setTitleBarRightImage2(@DrawableRes int resId) {
        ViewUtils.setImageResource(mImg_title_right2, resId);
    }

    /**
     * 设置标题栏右侧Image3
     *
     * @param resId
     */
    protected void setTitleBarRightImage3(@DrawableRes int resId) {
        ViewUtils.setImageResource(mImg_title_right3, resId);
    }

    /**
     * 设置标题栏右侧TextView1
     *
     * @param text
     */
    protected void setTitleBarRightText1(@NonNull String text) {
        ViewUtils.setText(mTv_title_right1, text);
    }

    /**
     * 设置标题栏右侧TextView2
     *
     * @param text
     */
    protected void setTitleBarRightText2(@NonNull String text) {
        ViewUtils.setText(mTv_title_right2, text);
    }

    /**
     * 设置标题栏右侧TextView3
     *
     * @param text
     */
    protected void setTitleBarRightText3(@NonNull String text) {
        ViewUtils.setText(mTv_title_right3, text);
    }

    /**
     * 设置标题栏右侧TextView1
     *
     * @param colorRes
     */
    protected void setTitleBarRightTextColor1(@ColorRes int colorRes) {
        ViewUtils.setTextColor(mTv_title_right1, colorRes);
    }

    /**
     * 设置标题栏右侧TextView1
     *
     * @param colorRes
     */
    protected void setTitleBarRightTextColor2(@ColorRes int colorRes) {
        ViewUtils.setTextColor(mTv_title_right2, colorRes);
    }

    /**
     * 设置标题栏右侧TextView1
     *
     * @param colorRes
     */
    protected void setTitleBarRightTextColor3(@ColorRes int colorRes) {
        ViewUtils.setTextColor(mTv_title_right3, colorRes);
    }

    /**
     * 设置标题左边点击事件
     */
    protected void setTitleBarLeftOnClickListener(@NonNull View.OnClickListener onClickListener) {
        ViewUtils.setOnClickListener(mRl_title_left, onClickListener);
    }

    /**
     * 设置标题中间点击事件
     */
    protected void setTitleBarCenterOnClickListener(@NonNull View.OnClickListener onClickListener) {
        ViewUtils.setOnClickListener(mRl_title_center, onClickListener);
    }

    /**
     * 标题右边View1点击事件
     */
    protected void setTitleBarRightOnClickListener1(@NonNull View.OnClickListener onClickListener) {
        ViewUtils.setOnClickListener(mRl_title_right1, onClickListener);
    }

    /**
     * 设置标题右边View2点击事件
     */
    protected void setTitleBarRightOnClickListener2(@NonNull View.OnClickListener onClickListener) {
        ViewUtils.setOnClickListener(mRl_title_right2, onClickListener);
    }

    /**
     * 设置标题右边View3点击事件
     */
    protected void setTitleBarRightOnClickListener3(@NonNull View.OnClickListener onClickListener) {
        ViewUtils.setOnClickListener(mRl_title_right3, onClickListener);
    }

    /**
     * 设置背景
     *
     * @param resId
     */
    protected void setTitleBarRightBackgroundResource1(@DrawableRes int resId) {
        ViewUtils.setBackgroundResource(mTv_title_right1, resId);
        ViewUtils.setBackgroundResource(mImg_title_right1, resId);
    }

    /**
     * 设置背景
     *
     * @param resId
     */
    protected void setTitleBarRightBackgroundResource2(@DrawableRes int resId) {
        ViewUtils.setBackgroundResource(mTv_title_right2, resId);
        ViewUtils.setBackgroundResource(mImg_title_right2, resId);
    }

    /**
     * 设置背景
     *
     * @param resId
     */
    protected void setTitleBarRightBackgroundResource3(@DrawableRes int resId) {
        ViewUtils.setBackgroundResource(mTv_title_right3, resId);
        ViewUtils.setBackgroundResource(mImg_title_right3, resId);
    }

    /**
     * 加载标题栏,分为左、中、右三个位置，其中右边布局横向排列有三个子View，分别以1、2、3编号，每个子View可显示TextView或者ImageView
     * 左边和中间布局也可选择TextView或者ImageView显示。右边布局的三个子View，显示优先级由左到右依次降低，比如右边只显示一个TextView时，
     * View1显示，View2、View3隐藏
     */
    protected void setTitleBar(TitleBarTypeEnum titleBarType) {
        if (titleBarType == TitleBarTypeEnum.NONE) {//不使用标题栏
            if (mRl_titleLayout != null)
                mRl_titleLayout.setVisibility(View.GONE);
            return;
        } else if (titleBarType == TitleBarTypeEnum.TV_NULL_TV) {//左、中、右：TextView、无、TextView
            setTV_NULL_TV();
        } else if (titleBarType == TitleBarTypeEnum.TV_NULL_TVIMGTV) {//左、中、右：TextView、无、TextView+ImageView=TextView
            setTV_NULL_TVIMGTV();
        } else if (titleBarType == TitleBarTypeEnum.TV_TV_TV) {//左、中、右：TextView、TextView、TextView
            setTV_TV_TV();
        } else if (titleBarType == TitleBarTypeEnum.IMG_TV_IMG) {//左、中、右：ImageView、TextView、ImageView
            setIMG_TV_IMG();
        } else if (titleBarType == TitleBarTypeEnum.IMG_TV_TV) {//左、中、右：ImageView、TextView、TextView
            setIMG_TV_TV();
        } else if (titleBarType == TitleBarTypeEnum.IMG_TV_NULL) {//左、中、右：ImageView、TextView、无
            setIMG_TV_NULL();
        } else if (titleBarType == TitleBarTypeEnum.TV_TV_TVIMG) {//左、中、右：TextView、TextView、TextView+ImageView
            setTV_TV_TVIMG();
        } else if (titleBarType == TitleBarTypeEnum.TV_TV_IMG) {//左、中、右：TextView、TextView、ImageView
            setTV_TV_IMG();
        } else if (titleBarType == TitleBarTypeEnum.IMG_TV_TVIMG) {//左、中、右：ImageView、TextView、TextView+ImageView
            setIMG_TV_TVIMG();
        }
    }

    /**
     * 左、中、右：ImageView、TextView、TextView+ImageView
     */
    private void setIMG_TV_TVIMG() {
        mRl_title_left.setVisibility(View.VISIBLE);
        mTv_title_left.setVisibility(View.GONE);
        mImg_title_left.setVisibility(View.VISIBLE);
        mRl_title_center.setVisibility(View.VISIBLE);
        mTv_title_center.setVisibility(View.VISIBLE);
        mLl_title_right.setVisibility(View.VISIBLE);
        mRl_title_right1.setVisibility(View.VISIBLE);
        mRl_title_right2.setVisibility(View.VISIBLE);
        mRl_title_right3.setVisibility(View.GONE);
        mTv_title_right1.setVisibility(View.VISIBLE);
        mImg_title_right1.setVisibility(View.GONE);
        mTv_title_right2.setVisibility(View.GONE);
        mImg_title_right2.setVisibility(View.VISIBLE);
    }


    /**
     * 左、中、右：TextView、TextView、ImageView
     */
    private void setTV_TV_IMG() {
        mRl_title_left.setVisibility(View.VISIBLE);
        mTv_title_left.setVisibility(View.VISIBLE);
        mImg_title_left.setVisibility(View.GONE);
        mRl_title_center.setVisibility(View.VISIBLE);
        mTv_title_center.setVisibility(View.VISIBLE);
        mLl_title_right.setVisibility(View.VISIBLE);
        mRl_title_right1.setVisibility(View.VISIBLE);
        mRl_title_right2.setVisibility(View.GONE);
        mRl_title_right3.setVisibility(View.GONE);
        mTv_title_right1.setVisibility(View.GONE);
        mImg_title_right1.setVisibility(View.VISIBLE);
    }

    /**
     * 左、中、右：TextView、TextView、TextView+ImageView
     */
    private void setTV_TV_TVIMG() {
        mRl_title_left.setVisibility(View.VISIBLE);
        mTv_title_left.setVisibility(View.VISIBLE);
        mImg_title_left.setVisibility(View.GONE);
        mRl_title_center.setVisibility(View.VISIBLE);
        mTv_title_center.setVisibility(View.VISIBLE);
        mLl_title_right.setVisibility(View.VISIBLE);
        mRl_title_right1.setVisibility(View.VISIBLE);
        mRl_title_right2.setVisibility(View.VISIBLE);
        mRl_title_right3.setVisibility(View.GONE);
        mTv_title_right1.setVisibility(View.VISIBLE);
        mImg_title_right1.setVisibility(View.GONE);
        mTv_title_right2.setVisibility(View.GONE);
        mImg_title_right2.setVisibility(View.VISIBLE);
    }

    /**
     * 左、中、右：ImageView、TextView、无
     */
    private void setIMG_TV_NULL() {
        mRl_title_left.setVisibility(View.VISIBLE);
        mRl_title_center.setVisibility(View.VISIBLE);
        mLl_title_right.setVisibility(View.GONE);
        mTv_title_left.setVisibility(View.GONE);
        mImg_title_left.setVisibility(View.VISIBLE);
        mTv_title_center.setVisibility(View.VISIBLE);
    }

    /**
     * 左、中、右：ImageView、TextView、TextView
     */
    private void setIMG_TV_TV() {
        mRl_title_left.setVisibility(View.VISIBLE);
        mRl_title_center.setVisibility(View.VISIBLE);
        mLl_title_right.setVisibility(View.VISIBLE);
        mTv_title_left.setVisibility(View.GONE);
        mImg_title_left.setVisibility(View.VISIBLE);
        mTv_title_center.setVisibility(View.VISIBLE);
        mRl_title_right1.setVisibility(View.VISIBLE);
        mRl_title_right2.setVisibility(View.GONE);
        mRl_title_right3.setVisibility(View.GONE);
        mTv_title_right1.setVisibility(View.VISIBLE);
        mImg_title_right1.setVisibility(View.GONE);
    }

    /**
     * 左、中、右：ImageView、TextView、ImageView
     */
    private void setIMG_TV_IMG() {
        mRl_title_left.setVisibility(View.VISIBLE);
        mRl_title_center.setVisibility(View.VISIBLE);
        mLl_title_right.setVisibility(View.VISIBLE);
        mTv_title_left.setVisibility(View.GONE);
        mImg_title_left.setVisibility(View.VISIBLE);
        mTv_title_center.setVisibility(View.VISIBLE);
        mRl_title_right1.setVisibility(View.VISIBLE);
        mRl_title_right2.setVisibility(View.GONE);
        mRl_title_right3.setVisibility(View.GONE);
        mTv_title_right1.setVisibility(View.GONE);
        mImg_title_right1.setVisibility(View.VISIBLE);
    }

    /**
     * 左、中、右：TextView、TextView、TextView
     */
    private void setTV_TV_TV() {
        mRl_title_left.setVisibility(View.VISIBLE);
        mRl_title_center.setVisibility(View.VISIBLE);
        mLl_title_right.setVisibility(View.VISIBLE);
        mTv_title_left.setVisibility(View.VISIBLE);
        mImg_title_left.setVisibility(View.GONE);
        mTv_title_center.setVisibility(View.VISIBLE);
        mRl_title_right1.setVisibility(View.VISIBLE);
        mRl_title_right2.setVisibility(View.GONE);
        mRl_title_right3.setVisibility(View.GONE);
        mTv_title_right1.setVisibility(View.VISIBLE);
        mImg_title_right1.setVisibility(View.GONE);
    }

    /**
     * 左、中、右：TextView、无、TextView+ImageView+TextView
     */
    private void setTV_NULL_TVIMGTV() {
        mRl_title_left.setVisibility(View.VISIBLE);
        mTv_title_left.setVisibility(View.VISIBLE);
        mImg_title_left.setVisibility(View.GONE);
        mRl_title_center.setVisibility(View.GONE);
        mLl_title_right.setVisibility(View.VISIBLE);
        mRl_title_right1.setVisibility(View.VISIBLE);
        mRl_title_right2.setVisibility(View.VISIBLE);
        mRl_title_right3.setVisibility(View.VISIBLE);
        mTv_title_right1.setVisibility(View.VISIBLE);
        mImg_title_right1.setVisibility(View.GONE);
        mTv_title_right2.setVisibility(View.GONE);
        mImg_title_right2.setVisibility(View.VISIBLE);
        mTv_title_right3.setVisibility(View.VISIBLE);
        mImg_title_right3.setVisibility(View.GONE);
    }

    /**
     * 左、中、右：TextView、无、TextView
     */
    private void setTV_NULL_TV() {
        mRl_title_left.setVisibility(View.VISIBLE);
        mImg_title_left.setVisibility(View.GONE);
        mTv_title_left.setVisibility(View.VISIBLE);
        mRl_title_center.setVisibility(View.GONE);
        mLl_title_right.setVisibility(View.VISIBLE);
        mRl_title_right1.setVisibility(View.VISIBLE);
        mRl_title_right2.setVisibility(View.GONE);
        mRl_title_right3.setVisibility(View.GONE);
        mImg_title_right1.setVisibility(View.GONE);
        mTv_title_right1.setVisibility(View.VISIBLE);
    }

    /**
     * 如果当前Activity使用了框架的TitleBar，而Fragment又继承了BasicCommonFragment，
     * 则Fragment应该返回TitleBarTypeEnum.NOT
     *
     * @return 返回标题栏类型，每个页面对标题栏的加载，要看返回的TitleBarTypeEnum；
     */
    protected abstract TitleBarTypeEnum titleBarType();
}
