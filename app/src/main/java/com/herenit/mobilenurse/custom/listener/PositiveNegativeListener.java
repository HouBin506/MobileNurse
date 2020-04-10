package com.herenit.mobilenurse.custom.listener;

/**
 * author: HouBin
 * date: 2019/1/9 17:53
 * desc: 正负极监听器，可以做确认取消的监听
 */
public interface PositiveNegativeListener {

    /**
     * 有参数回传的接口回调（正极事件监听，比如“确定”按钮的点击）
     *
     * @param backData
     */
    default void onPositive(Object... backData) {

    }

    /**
     * 有参数回传的接口回调（负极事件监听，比如“取消”按钮的点击）
     *
     * @param backData
     */
    default void onNegative(Object... backData) {

    }

    /**
     * 无参数回传的接口回调（正极事件监听，比如“确定”按钮的点击）
     */
    default void onPositive() {

    }

    /**
     * 无参数回传的接口回调（负极事件监听，比如“取消”按钮的点击）
     */
    default void onNegative() {

    }

}
