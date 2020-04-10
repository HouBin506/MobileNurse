package com.herenit.mobilenurse.mvp.vital_signs;

import android.graphics.Color;
import android.text.TextUtils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.app.utils.StringUtils;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.entity.VitalSignsItem;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsChartData;
import com.herenit.mobilenurse.criteria.entity.view.VitalSignsViewItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * author: HouBin
 * date: 2019/5/21 10:59
 * desc:针对体征的一些特殊处理帮助类
 */
public class VitalSignsHelper {

    private VitalSignsHelper() {
    }

    /**
     * 获取ItemCode和ClassCode集合，
     *
     * @param code
     * @return 返回长度为2的数组，[0]表示itemCode，[1]表示classCode
     */
    public static String[] getItemAndClassCode(String code) {
        String[] codeArr = new String[2];
        if (!TextUtils.isEmpty(code) && code.contains("|")) {
            String[] arr = code.split("\\|");
            if (arr != null && arr.length == 2) {
                codeArr[0] = arr[0];
                codeArr[1] = arr[1];
            } else {
                codeArr[0] = code;
                codeArr[1] = "";
            }
        } else {
            codeArr[0] = code;
            codeArr[1] = "";
        }
        return codeArr;
    }

    /**
     * 根据ItemCode和ClassCode组合成Code
     *
     * @param itemCode
     * @param classCode
     * @return
     */
    public static String getCodeByItemAndClassCode(String itemCode, String classCode) {
        String code;
        if (!TextUtils.isEmpty(itemCode) && !TextUtils.isEmpty(classCode)) {
            code = itemCode + "|" + classCode;
        } else if (TextUtils.isEmpty(classCode)) {
            code = itemCode;
        } else {
            code = "|" + classCode;
        }
        return code;
    }

    /**
     * 将体征数据赋到体征UI条目上面
     *
     * @param dataItem 表示体征数据的Item
     * @param viewItem 表示体征UI的Item
     */
    public static void convertDataItemToViewItem(VitalSignsItem dataItem, VitalSignsViewItem viewItem) {
        if (dataItem == null || viewItem == null)
            return;
        viewItem.setValue(dataItem.getItemValue());
        viewItem.setSpecialValue(dataItem.getSpecialValue());
        viewItem.setMemo(dataItem.getMemo());
        Long timePoint = dataItem.getTimePoint();
        //设置时间点
        viewItem.setTimePoint(timePoint);
        String fixedTimePointList = viewItem.getFixedTimePointList();
        if (TextUtils.isEmpty(fixedTimePointList)) {
            viewItem.setFixedTimePoint("");
        } else {
            String[] fixedArr = fixedTimePointList.split("\\|");
            if (fixedArr == null || fixedArr.length == 0) {
                viewItem.setFixedTimePoint("");
            } else {
                List fixedList = Arrays.asList(fixedArr);
                String HHMM = TimeUtils.getHHMMString(timePoint);
                if (!fixedList.contains(HHMM)) {
                    viewItem.setFixedTimePoint("");
                } else {
                    viewItem.setFixedTimePoint(HHMM);
                }
            }
        }
    }

    /**
     * 根据时间点、固定时间点，解析出真正的时间点
     *
     * @param timePoint
     * @param fixedTime
     * @return
     */
    public static Long getTimePoint(Long timePoint, String fixedTime) {
        if (TextUtils.isEmpty(fixedTime)) {
            return timePoint;//没有特殊时间点，直接返回本来的时间点
        } else {//有特殊时间点,返回时间点的“年 月 日”+特殊时间点 拼接
            String timePointStr = TimeUtils.getYYYYMMDDString(timePoint) + " " + fixedTime;
            Date timePointDate = TimeUtils.getDateByFormat(TimeUtils.FORMAT_YYYYMMDDHHMM, timePointStr);
            if (timePointDate == null)
                return timePoint;
            else
                return timePointDate.getTime();
        }
    }

    /**
     * 将体征数据列表，转化成体征趋势图数据
     *
     * @param itemList
     * @return
     */
    public static VitalSignsChartData convertVitalToChartData(List<VitalSignsItem> itemList) {
        if (itemList == null || itemList.isEmpty())
            return null;
        Collections.reverse(itemList);//话体征趋势图，要将体征数据倒序一下
        //获取体征列表中的最大时间和最小时间
        Long minDate = itemList.get(0).getTimePoint();
        Long maxDate = itemList.get(itemList.size() - 1).getTimePoint();
        if (maxDate == null || minDate == null || maxDate < minDate)
            return null;
        //获取当前系统要求的体征趋势图时间范围（比如三天内）最大值最小值
        Long startDate = TimeUtils.getStartDateTimeLongByTimeCode(VitalSignsChartActivity.DEFAULT_TIME_INTERVAL_CODE);
        Long stopDate = TimeUtils.getStopDateTimeLongByTimeCode(VitalSignsChartActivity.DEFAULT_TIME_INTERVAL_CODE);
        if (startDate == null || stopDate == null || startDate >= stopDate)
            return null;
        VitalSignsChartData data = new VitalSignsChartData();
        data.setxAxisValueList(getXAxisValueList(startDate, maxDate <= stopDate ? maxDate : stopDate));//如果结束时间超出允许的时间范围，则截掉
        List<Entry> entries = new ArrayList<>();
        //获取单位时间间隔（默认为每隔四小时为一个单位）
        long unitInterval = getUnitInterval();
        float test = (stopDate - startDate) / unitInterval;
        for (int x = 0; x < itemList.size(); x++) {
            VitalSignsItem item = itemList.get(x);
            Long timePoint = item.getTimePoint();
            if (timePoint == null || timePoint < startDate || timePoint > stopDate)//记录时间为空、记录时间超出了趋势图x轴的范围，则不要
                continue;
            Entry entry = getChartLineEntity(item.getItemValue(), timePoint, startDate, unitInterval);
            if (entry != null)
                entries.add(entry);
        }
        LineDataSet dataSet = new LineDataSet(entries, itemList.get(0).getItemName());//设置点坐标、趋势图名称
        dataSet.setCircleColor(ArmsUtils.getColor(MobileNurseApplication.getInstance(), R.color.red));//设置坐标点颜色
        dataSet.setColor(Color.MAGENTA);//设置折线的线条颜色
        dataSet.setHighLightColor(ArmsUtils.getColor(MobileNurseApplication.getInstance(), R.color.yellow));//设置选中某一点出现的横竖两条线的颜色
        LineData lineData = new LineData(dataSet);
        lineData.setValueTextColor(ArmsUtils.getColor(MobileNurseApplication.getInstance(), R.color.black));//设置坐标点上字的颜色
        //设置最小最大刻度
        data.setMinimum((float) Math.floor(entries.get(0).getX()));
        data.setMaximum((float) Math.ceil(entries.get(entries.size() - 1).getX()));
        data.setLineData(lineData);
        return data;
    }

    /**
     * 构建一条体征在体征趋势图对应的坐标
     *
     * @param itemValue    体征项数值
     * @param timePoint    体征项记录时间
     * @param startDate    X轴原点对应的时间
     * @param unitInterval 单位时间间隔，比如每4小时为一个单位
     * @return
     */
    private static Entry getChartLineEntity(String itemValue, Long timePoint, Long startDate, long unitInterval) {
        if (TextUtils.isEmpty(itemValue))
            return null;
        float fValue = Float.valueOf(itemValue);
        if (startDate == timePoint)
            return new Entry(0, fValue);
        float lX = (float) ((timePoint - startDate) / (double) unitInterval);
        if (lX < 0)
            return null;
        return new Entry(lX, fValue);
    }

    /**
     * 获取单位时间间隔，就是说每一个刻度表示多少毫秒。
     * 默认为每2小时一个单位
     *
     * @return
     */
    private static long getUnitInterval() {
        return 2 * 60 * 60 * 1000;
    }

    /**
     * 获取趋势图X轴上的值的集合（时间点集合）
     * 当事件区间小于一天，则时间点集合范围在一天之内
     * 当时间区间大于一天小于两天，则时间点集合范围在两天之内
     * .......当时间区间超出默认的时间区间，则截掉后面的部分
     *
     * @param minDate
     * @param maxDate
     * @return
     */
    private static List<String> getXAxisValueList(Long minDate, Long maxDate) {
        long interval = maxDate - minDate;
        int dayCount = (int) (interval / (24 * 60 * 60 * 1000)) + 1;
        List<String> result = new ArrayList<>();
        for (int x = 0; x < dayCount; x++) {
            result.add("第" + (x + 1) + "天" + " 00:00");
            result.add("02:00");
            result.add("04:00");
            result.add("06:00");
            result.add("08:00");
            result.add("10:00");
            result.add("12:00");
            result.add("14:00");
            result.add("16:00");
            result.add("18:00");
            result.add("20:00");
            result.add("22:00");
            if (x == dayCount - 1) {
                result.add("第" + (x + 2) + "天" + " 00:00");
            }
        }
        return result;
    }
}
