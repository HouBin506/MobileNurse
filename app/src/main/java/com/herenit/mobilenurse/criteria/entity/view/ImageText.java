package com.herenit.mobilenurse.criteria.entity.view;

import android.support.annotation.DrawableRes;

/**
 * author: HouBin
 * date: 2019/3/28 14:58
 * desc: 图片与字符串
 */
public class ImageText {
    @DrawableRes
    private int imageRes;

    private String text;

    public ImageText(@DrawableRes int imageRes, String text) {
        this.imageRes = imageRes;
        this.text = text;
    }

    @DrawableRes
    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(@DrawableRes int imageRes) {
        this.imageRes = imageRes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
