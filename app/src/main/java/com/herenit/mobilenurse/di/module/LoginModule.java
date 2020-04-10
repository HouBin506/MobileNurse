package com.herenit.mobilenurse.di.module;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.arms.di.scope.ActivityScope;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.entity.User;
import com.herenit.mobilenurse.custom.adapter.CommonTextAdapter;
import com.herenit.mobilenurse.mvp.login.LoginContract;
import com.herenit.mobilenurse.mvp.login.LoginModel;
import com.herenit.mobilenurse.criteria.entity.submit.Login;
import com.herenit.mobilenurse.custom.adapter.CommonTextImageAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * author: HouBin
 * date: 2019/1/7 13:37
 * desc: Login模块的dagger ModuleManager
 */
@Module
public abstract class LoginModule {

    @Binds
    abstract LoginContract.Model bindLoginModel(LoginModel model);

    @ActivityScope
    @Provides
    static List<Login> provideLoginList() {
        return new ArrayList<>();
    }

    /**
     * 提供账号自动补全数据源
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<String> provideUserIdList() {
        return new ArrayList<>();
    }

    /**
     * 提供账号自动补全列表Adapter
     *
     * @return
     */
    @ActivityScope
    @Provides
    static ArrayAdapter<String> provideAutoCompleteAdapter(LoginContract.View view, List<String> userIdList) {
        return new ArrayAdapter<>(view.getActivity(), android.R.layout.simple_dropdown_item_1line, userIdList);
    }

    @ActivityScope
    @Provides
    static CommonTextImageAdapter<Login> provideSelectAccountAdapter(LoginContract.View view, List<Login> data) {
        return new CommonTextImageAdapter<>(view.getActivity(), data, view.toString());
    }

    /**
     * 提供科室列表
     *
     * @return
     */
    @ActivityScope
    @Provides
    static List<User.MnUserVsGroupVPOJOListBean> provideDeptList() {
        return new ArrayList<>();
    }

    /**
     * 提供科室列表
     *
     * @return
     */
    @ActivityScope
    @Provides
    static CommonAdapter<User.MnUserVsGroupVPOJOListBean> provideDeptListAdapter(LoginContract.View view, List<User.MnUserVsGroupVPOJOListBean> data) {
        return new CommonAdapter<User.MnUserVsGroupVPOJOListBean>((Activity) view, data, R.layout.item_common_text) {
            @Override
            protected void convert(ViewHolder holder, User.MnUserVsGroupVPOJOListBean item, int position) {
                holder.setText(R.id.tv_item_text_text, item.getGroupName());
            }
        };
    }
}
