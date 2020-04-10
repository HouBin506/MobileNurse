package com.herenit.mobilenurse.custom.extra;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/1/18 16:13
 * desc:针对多选做额外提供的接口，用于对外提供获取多选结果等方法
 */
public interface MultipleChoiceExtra {
    /**
     * 获取多选选择结果,一般为position的集合
     *
     * @return
     */
    List<Integer> getChoiceResult();

    /**
     * 设置默认选项，一般为默认选中的position集合
     *
     * @param defaultChoice
     */
    void setDefaultChoice(List<Integer> defaultChoice);
}
