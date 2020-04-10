package com.herenit.mobilenurse.custom.widget.toast;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.enums.NoticeLevelEnum;
import com.herenit.mobilenurse.custom.extra.WidgetExtra;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: HouBin
 * date: 2019/1/10 17:17
 * desc: 自定义Toast
 */
public class NoticeToast implements WidgetExtra {
    private Toast mToast;

    /**
     * 是否依附于Activity，即当前Activity退出时，是否消失吐司
     */
    private boolean attach = true;
    @BindView(R2.id.img_noticeToast_icon)
    ImageView mImg_notice;
    @BindView(R2.id.tv_noticeToast_content)
    TextView mTv_content;

    private Unbinder mUnbinder;

    public NoticeToast(Context context) {
        mToast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_notice, null);
        mUnbinder = ButterKnife.bind(this, view);
        mToast.setView(view);
        mToast.setDuration(Toast.LENGTH_SHORT);
    }

    /**
     * 设置当前Toast是否基于Activity
     *
     * @param attach
     */
    public void attachActivity(boolean attach) {
        this.attach = attach;
    }

    public void show(String message, NoticeLevelEnum level) {
        mTv_content.setText(message);
        if (level == NoticeLevelEnum.NORMAL) {
            mImg_notice.setImageResource(R.mipmap.ic_notice_normal);
        } else if (level == NoticeLevelEnum.WARNING) {
            mImg_notice.setImageResource(R.mipmap.ic_notice_warning);
        } else if (level == NoticeLevelEnum.ERROR) {
            mImg_notice.setImageResource(R.mipmap.ic_notice_error);
        }
        mToast.show();
    }

    @Override
    public void release() {
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        if (attach) {
            mToast.cancel();
            mToast = null;
        }

    }
}
