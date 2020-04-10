package com.herenit.mobilenurse.mvp.vital_signs;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.mvp.IView;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.ScreenUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.submit.VitalSignsHistoryQuery;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsChartData;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.di.component.DaggerVitalSignsChartComponent;
import com.herenit.mobilenurse.mvp.base.BasicBusinessActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;


/**
 * author: HouBin
 * date: 2019/5/28 14:28
 * desc:体征趋势图页面
 */
public class VitalSignsChartActivity extends BasicBusinessActivity<VitalSignsChartPresenter> implements VitalSignsChartContract.View {

    //默认趋势图时间区间
    public static final String DEFAULT_TIME_INTERVAL_CODE = CommonConstant.TIME_CODE_IN_PAST_THREE_DAYS;

    @BindView(R2.id.ll_vitalSignsChart_chartContainer)
    LinearLayout mLL_container;

    private LoadingDialog mLoadingDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVitalSignsChartComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_NULL;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_vital_signs_chart;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        setTitleUI();
        Intent intent = getIntent();
        if (intent != null) {
            VitalSignsHistoryQuery.VitalItemID itemID = (VitalSignsHistoryQuery.VitalItemID) intent.getSerializableExtra(KeyConstant.NAME_EXTRA_VITAL_ITEM_ID);
            if (itemID != null) {
                mPresenter.loadVitalSignsChartData(itemID);
            }
        }
    }

    private void setTitleUI() {
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        setTitleBarLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleBarCenterText(ArmsUtils.getString(mContext, R.string.title_activity_vital_signs_chart));
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog == null)
            mLoadingDialog = createLoadingDialog();
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
    }

    @Override
    public void showError(@NonNull String message) {
        showErrorToast(message);
    }

    /**
     * 展示体征趋势图
     *
     * @param data
     */
    @Override
    public void showChart(VitalSignsChartData data) {
        if (data == null) {
            showToast(ArmsUtils.getString(mContext, R.string.message_emptyVitalData));
            return;
        }
        try {
            LineChart mChart = new LineChart(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mChart.setLayoutParams(layoutParams);
            mLL_container.addView(mChart);

//显示边界
            mChart.setDrawBorders(true);
            XAxis xAxis = mChart.getXAxis();//得到x轴
            //设置x轴的位置，默认在上方
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //设置x轴坐标之间的最小间距（因为此图有缩放功能，X轴Y轴设置可缩放）
            xAxis.setGranularity(1f);
            //设置X轴的刻度数量，第二个参数为true，则平均分配刻度为12份，false会根据实际的刻度数量分配
//        xAxis.setLabelCount(12, false);
            //设置x轴的最大最小刻度
            xAxis.setAxisMinimum((float) Math.floor(data.getMinimum()));
            xAxis.setAxisMaximum((float) Math.ceil(data.getMaximum()));
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return data.getxAxisValueList().get((int) value);
                }
            });
            mChart.setData(data.getLineData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
