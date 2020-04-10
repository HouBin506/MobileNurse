package com.herenit.mobilenurse.custom.extra;

/**
 * author: HouBin
 * date: 2019/1/10 15:40
 * desc: 组件控制器，widget中的类要实现此接口，便于额外控制
 */
public interface WidgetExtra {

    /**
     * 资源的释放、解绑操作
     */
    void release();
}
