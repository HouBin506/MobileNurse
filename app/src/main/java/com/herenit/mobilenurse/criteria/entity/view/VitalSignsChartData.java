package com.herenit.mobilenurse.criteria.entity.view;

import com.github.mikephil.charting.data.LineData;

import java.util.List;

/**
 * author: HouBin
 * date: 2019/6/3 16:21
 * desc:体征项目趋势图实体类
 */
public class VitalSignsChartData {
    private LineData lineData;
    private List<String> xAxisValueList;
    //X轴最小刻度
    private float minimum;
    //x轴最大刻度
    private float maximum;

    public LineData getLineData() {
        return lineData;
    }

    public void setLineData(LineData lineData) {
        this.lineData = lineData;
    }

    public List<String> getxAxisValueList() {
        return xAxisValueList;
    }

    public void setxAxisValueList(List<String> xAxisValueList) {
        this.xAxisValueList = xAxisValueList;
    }

    public float getMinimum() {
        return minimum;
    }

    public void setMinimum(float minimum) {
        this.minimum = minimum;
    }

    public float getMaximum() {
        return maximum;
    }

    public void setMaximum(float maximum) {
        this.maximum = maximum;
    }
}
