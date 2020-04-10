package com.herenit.mobilenurse.custom.listener;

/**
 * author: HouBin
 * date: 2019/3/19 10:37
 * desc: 进度条监听器
 */
public interface OnProgressChangeListener {

    /**
     * 进度发生改变
     *
     * @param progress 当前进度
     * @return 根据当前进度，返回需要表达的数据，比如返回当前进度的百分数
     */
    String onProgressChanged(int progress);
}
