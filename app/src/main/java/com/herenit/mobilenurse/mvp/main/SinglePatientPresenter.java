package com.herenit.mobilenurse.mvp.main;

import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.arms.mvp.BasePresenter;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.custom.adapter.CommonTextAdapter;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * author: HouBin
 * date: 2019/3/1 13:38
 * desc: 单患者页面的Presenter层
 */
@ActivityScope
public class SinglePatientPresenter extends BasePresenter<SinglePatientContract.Model, SinglePatientContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    List<User.MnUserVsGroupVPOJOListBean> mGroupList;

    @Inject
    List<String> mGroupNameList;

    @Inject
    CommonTextAdapter mGroupNameAdapter;

    @Inject
    public SinglePatientPresenter(SinglePatientContract.Model model, SinglePatientContract.View rootView) {
        super(model, rootView);
    }

}
