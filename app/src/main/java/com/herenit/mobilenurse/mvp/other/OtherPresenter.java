package com.herenit.mobilenurse.mvp.other;

import com.herenit.arms.di.scope.FragmentScope;
import com.herenit.arms.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * author: HouBin
 * date: 2019/2/18 14:00
 * desc:“其它”功能  Presenter
 */
@FragmentScope
public class OtherPresenter extends BasePresenter<OtherContract.Model, OtherContract.View> {

    @Inject
    OtherPresenter(OtherContract.Model model, OtherContract.View view) {
        super(model, view);
    }
}
