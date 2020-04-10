package com.herenit.mobilenurse.custom.listener;

/**
 * author: HouBin
 * date: 2019/3/18 17:23
 * desc: 针对一些未知的操作的响应监听
 */
public interface OnActionListener {
    /**
     * 操作之后的响应
     *
     * @param actionResult
     */
    void onAction(Object... actionResult);
}
