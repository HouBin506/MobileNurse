package com.herenit.mobilenurse.mvp.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.herenit.arms.base.BaseFragment;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.mvp.IView;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.enums.ModuleEnum;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.custom.adapter.ToolAdapter;
import com.herenit.mobilenurse.di.component.DaggerOtherComponent;
import com.herenit.mobilenurse.mvp.base.BasicBusinessFragment;
import com.herenit.mobilenurse.mvp.tool.StopwatchActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/2/18 13:58
 * desc: “其它”功能 Fragment
 */
public class OtherFragment extends BaseFragment<OtherPresenter> implements OtherContract.View {

    @BindView(R2.id.gv_other_list)
    GridView mGridView;

    //工具列表数据
    @Inject
    List<ModuleEnum> mToolList;
    //工具列表Adapter
    @Inject
    ToolAdapter mToolAdapter;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerOtherComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_other, container, false);
        return contentView;
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        mGridView.setAdapter(mToolAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModuleEnum tool = mToolList.get(position);
                if (ModuleEnum.TOOL_CALCULATOR.id().equals(tool.id())) {//计算器
                    Intent intent = new Intent();
                    intent.setClassName("com.android.calculator2",
                            "com.android.calculator2.Calculator");
                    startActivity(intent);
                } else if (ModuleEnum.TOOL_STOPWATCH.id().equals(tool.id())) {//秒表
                    launchActivity(new Intent(mContext, StopwatchActivity.class));
                }
            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
