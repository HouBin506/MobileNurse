package com.herenit.mobilenurse.di.module;

import com.herenit.mobilenurse.mvp.tool.ViewToolContract;
import com.herenit.mobilenurse.mvp.tool.ViewToolModel;


import dagger.Binds;
import dagger.Module;

/**
 * author: HouBin
 * date: 2019/1/11 16:36
 * desc:PD查看工具dagger
 */
@Module
public abstract class PDFViewModule {

    @Binds
    abstract ViewToolContract.Model bindPDFViewModel(ViewToolModel model);

}
