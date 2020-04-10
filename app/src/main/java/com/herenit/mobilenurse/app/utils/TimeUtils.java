package com.herenit.mobilenurse.app.utils;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.datastore.tempcache.CommonTemp;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * author: HouBin
 * date: 2019/2/20 15:07
 * desc: 时间工具类
 */
public class TimeUtils {

    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYYMMDDHHMMSS2 = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String FORMAT_YYYYMMDD2 = "yyyy/MM/dd";
    public static final String FORMAT_YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYYMMDDHHMM2 = "yyyy/MM/dd HH:mm";
    public static final String FORMAT_MMDDHHMM = "MM-dd HH:mm";
    public static final String FORMAT_YYMMDDHHMMSS_SSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static final SimpleDateFormat SDF_YYYYMMDDHHMMSS = new SimpleDateFormat(FORMAT_YYYYMMDDHHMMSS);
    public static final SimpleDateFormat SDF_YYYYMMDDHHMMSS2 = new SimpleDateFormat(FORMAT_YYYYMMDDHHMMSS2);
    public static final SimpleDateFormat SDF_YYYYMMDD = new SimpleDateFormat(FORMAT_YYYYMMDD);
    public static final SimpleDateFormat SDF_YYYYMMDD2 = new SimpleDateFormat(FORMAT_YYYYMMDD2);
    public static final SimpleDateFormat SDF_YYYYMMDDHHMM = new SimpleDateFormat(FORMAT_YYYYMMDDHHMM);
    public static final SimpleDateFormat SDF_YYYYMMDDHHMM2 = new SimpleDateFormat(FORMAT_YYYYMMDDHHMM2);
    public static final SimpleDateFormat SDF_YYMMDDHHMMSS_SSSZ = new SimpleDateFormat(FORMAT_YYMMDDHHMMSS_SSSZ);
    public static final SimpleDateFormat SDF_MMDDHHMM = new SimpleDateFormat(FORMAT_MMDDHHMM);


    private TimeUtils() {
    }


    /**
     * 设置系统时区
     *
     * @param timeZone
     */
    public static void setTimeZone(String timeZone) {
        final Calendar now = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        now.setTimeZone(tz);
    }


    /**
     * 时间校对
     *
     * @param serverTime 服务器时间
     * @return 如果本地时间不准确，则返回本地时间与服务器时间的差值；
     * 返回值不等于0 ，说明本地时间不准确
     */
    public static long proofTime(long serverTime) {
        long localTime = System.currentTimeMillis();
        long deviation = localTime - serverTime;
        if (Math.abs(deviation) > CommonConstant.TIME_ERROR_RANGE * 1000) {//时间差超出误差范围
            return deviation;
        }
        return 0;
    }

    /**
     * 以当前时间为准，获取与当前时间相差小时数为offHour的日期
     *
     * @param offHour 与当前时间相比，相差几小时 大于0，增加offHour天，小于0，减少offHour天，等于0为当前时间
     * @return
     */
    public static Date getOffsetHoursDate(int offHour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCurrentDate());
        calendar.add(Calendar.HOUR_OF_DAY, offHour);
        return calendar.getTime();
    }

    /**
     * 以当前时间为准，获取与当前时间相差天数为offDays的日期
     *
     * @param offDays 与当前时间相比，相差几天 大于0，增加offDays天，小于0，减少offDays天，等于0为当前时间
     * @return
     */
    public static Date getOffsetDaysDate(int offDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCurrentDate());
        calendar.add(Calendar.DAY_OF_YEAR, offDays);
        return calendar.getTime();
    }

    /**
     * 以当前时间为准，获取与当前时间相差月数为offDays的日期
     *
     * @param offMonth 与当前时间相比，相差几月 大于0，增加offMonth月，小于0，减少offMonth月，等于0为当前时间
     * @return
     */
    public static Date getOffsetMonthDate(int offMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCurrentDate());
        calendar.add(Calendar.MONTH, offMonth);
        return calendar.getTime();
    }

    /**
     * 以当前时间为准，获取与当前时间相差月数为offDays的日期
     *
     * @param offYear 与当前时间相比，相差几年 大于0，增加offMonth月，小于0，减少offMonth月，等于0为当前时间
     * @return
     */
    public static Date getOffsetYearDate(int offYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCurrentDate());
        calendar.add(Calendar.YEAR, offYear);
        return calendar.getTime();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getCurrentDate() {
        long deviation = CommonTemp.getInstance().getTimeDeviation();
        long now = System.currentTimeMillis() - deviation;
        if (now > 0)
            return new Date(now);
        return new Date();
    }

    /**
     * 根据{@link CommonConstant}中的TIME_CODE开头的数据，获取时间范围的开始点
     *
     * @param timeCode
     * @return
     */
    public static Date getStartDateTimeByTimeCode(String timeCode) {
        Date date = null;
        switch (timeCode) {
            case CommonConstant.TIME_CODE_IN_A_MONTH://一个月内
            case CommonConstant.TIME_CODE_IN_A_WEEK://一周内
            case CommonConstant.TIME_CODE_IN_THREE_DAYS://三日内
            case CommonConstant.TIME_CODE_TODAY://今天
                date = getFirstTimeOfDay(getCurrentDate());
                break;
            case CommonConstant.TIME_CODE_TWO_DAYS_LATER://后天
                date = getFirstTimeOfDay(getOffsetDaysDate(2));
                break;
            case CommonConstant.TIME_CODE_TOMORROW://明天
                date = getFirstTimeOfDay(getOffsetDaysDate(1));
                break;
            case CommonConstant.TIME_CODE_YESTERDAY://昨天
                date = getFirstTimeOfDay(getOffsetDaysDate(-1));
                break;
            case CommonConstant.TIME_CODE_IN_PAST_THREE_DAYS://过去的三天内
                date = getFirstTimeOfDay(getOffsetDaysDate(-2));
                break;
            case CommonConstant.TIME_CODE_IN_PAST_WEEK://过去的一周内
                date = getFirstTimeOfDay(getOffsetDaysDate(-6));
                break;
            case CommonConstant.TIME_CODE_IN_PAST_MONTH://过去的一个月内
                date = getFirstTimeOfDay(getOffsetMonthDate(-1));
                break;
            case CommonConstant.TIME_CODE_IN_PAST_THREE_MONTH://过去的三个月内
                date = getFirstTimeOfDay(getOffsetMonthDate(-3));
                break;
        }
        return date;
    }

    /**
     * 根据{@link CommonConstant}中的TIME_CODE开头的数据，获取时间范围的结束点
     *
     * @param timeCode
     * @return
     */
    public static Date getStopDateTimeByTimeCode(String timeCode) {
        Date date = null;
        switch (timeCode) {
            case CommonConstant.TIME_CODE_IN_A_MONTH://一个月内
                date = getLastTimeOfDay(getOffsetDaysDate(30));
                break;
            case CommonConstant.TIME_CODE_IN_A_WEEK://一周内
                date = getLastTimeOfDay(getOffsetDaysDate(6));
                break;
            case CommonConstant.TIME_CODE_IN_THREE_DAYS://三日内
            case CommonConstant.TIME_CODE_TWO_DAYS_LATER://后天
                date = getLastTimeOfDay(getOffsetDaysDate(2));
                break;
            case CommonConstant.TIME_CODE_TOMORROW://明天
                date = getLastTimeOfDay(getOffsetDaysDate(1));
                break;
            case CommonConstant.TIME_CODE_YESTERDAY://昨天
                date = getLastTimeOfDay(getOffsetDaysDate(-1));
                break;
            case CommonConstant.TIME_CODE_TODAY://今天
            case CommonConstant.TIME_CODE_IN_PAST_THREE_DAYS://过去的三天内
            case CommonConstant.TIME_CODE_IN_PAST_WEEK://过去的一周内
            case CommonConstant.TIME_CODE_IN_PAST_MONTH://过去的一个月内
            case CommonConstant.TIME_CODE_IN_PAST_THREE_MONTH://过去的三个月内
                date = getLastTimeOfDay(getCurrentDate());
                break;
        }
        return date;
    }

    /**
     * 根据{@link CommonConstant}中的TIME_CODE开头的数据，获取时间范围的开始点
     *
     * @param timeCode
     * @return
     */
    public static Long getStartDateTimeLongByTimeCode(String timeCode) {
        Date date = getStartDateTimeByTimeCode(timeCode);
        if (date != null)
            return date.getTime();
        return null;
    }

    /**
     * 根据{@link CommonConstant}中的TIME_CODE开头的数据，获取时间范围的结束点
     *
     * @param timeCode
     * @return
     */
    public static Long getStopDateTimeLongByTimeCode(String timeCode) {
        Date date = getStopDateTimeByTimeCode(timeCode);
        if (date != null)
            return date.getTime();
        return null;
    }

    /**
     * 根据时间格式，和时间字符串，获取某天的起始时间
     *
     * @param format
     * @param timeStr
     * @return
     */
    public static Date getStartDateTimeByFormat(String format, String timeStr) {
        Date date = getDateByFormat(format, timeStr);
        if (date != null)
            return getFirstTimeOfDay(date);
        return null;
    }

    /**
     * 根据时间格式，获取某天最后的时刻
     *
     * @param format  时间格式
     * @param timeStr 时间字符串
     * @return
     */
    public static Date getStopDateTimeByFormat(String format, String timeStr) {
        Date date = getDateByFormat(format, timeStr);
        if (date != null)
            return getLastTimeOfDay(date);
        return null;
    }

    /**
     * 根据给定的格式，获取Date对象
     *
     * @param format
     * @param timeStr
     * @return
     */
    public static Date getDateByFormat(@NonNull String format, @NonNull String timeStr) {
        Date date = null;
        if (format == null || TextUtils.isEmpty(timeStr))
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        if (dateFormat == null)
            return null;
        ParsePosition position = new ParsePosition(0);
        date = dateFormat.parse(timeStr, position);
        return date;
    }

    /**
     * 获取某天最后的时间
     *
     * @return
     */
    public static Date getLastTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取某天最后的时间
     *
     * @return
     */
    public static Date getFirstTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取格式为yyyy-MM-dd HH:mm的时间字符串
     *
     * @param date
     * @return
     */
    public static String getYYYYMMDDHHMMString(Date date) {
        String dateStr = "";
        if (date == null)
            return dateStr;
        dateStr = SDF_YYYYMMDDHHMM.format(date);
        return dateStr;
    }

    /**
     * 获取格式为yyyy-MM-dd的时间字符串
     *
     * @param date
     * @return
     */
    public static String getYYYYMMDDString(Date date) {
        String dateStr = "";
        if (date == null)
            return dateStr;
        dateStr = SDF_YYYYMMDD.format(date);
        return dateStr;
    }

    /**
     * 获取格式为yyyy-MM-dd HH:mm的时间字符串
     *
     * @param date
     * @return
     */
    public static String getYYYYMMDDHHMM2String(Date date) {
        String dateStr = "";
        if (date == null)
            return dateStr;
        dateStr = SDF_YYYYMMDDHHMM2.format(date);
        return dateStr;
    }


    /**
     * 获取格式为yyyy-MM-dd HH:mm的时间字符串
     *
     * @param timeMillis 时间毫秒
     * @return
     */
    public static String getYYYYMMDDHHMMString(long timeMillis) {
        Date date = new Date(timeMillis);
        return getYYYYMMDDHHMMString(date);
    }

    /**
     * 获取格式为yyyy-MM-dd的时间字符串
     *
     * @param timeMillis 时间毫秒
     * @return
     */
    public static String getYYYYMMDDString(long timeMillis) {
        Date date = new Date(timeMillis);
        return getYYYYMMDDString(date);
    }

    /**
     * 获取格式为yyyy-MM-dd HH:mm:ss的时间字符串
     *
     * @param timeMillis 时间毫秒
     * @return
     */
    public static String getYYYYMMDDHHMMSSString(long timeMillis) {
        Date date = new Date(timeMillis);
        return getYYYYMMDDHHMMSSString(date);
    }

    /**
     * 获取格式为yyyy-MM-dd HH:mm:ss的时间字符串
     *
     * @param date 时间date
     * @return
     */
    public static String getYYYYMMDDHHMMSSString(Date date) {
        String dateStr = "";
        if (date == null)
            return dateStr;
        dateStr = SDF_YYYYMMDDHHMMSS.format(date);
        return dateStr;
    }

    /**
     * 返回格式为“HH:mm”时间字符串
     *
     * @param timeMillis
     * @return
     */
    public static String getHHMMString(long timeMillis) {
        Date date = new Date(timeMillis);
        return getHHMMString(date);
    }

    /**
     * 返回格式为“HH:mm”时间字符串
     *
     * @param date
     * @return
     */
    public static String getHHMMString(Date date) {
        if (date == null)
            return "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return formatIntField2String(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + formatIntField2String(calendar.get(Calendar.MINUTE));
    }

    /**
     * 返回格式为“MM-dd HH:mm”时间字符串
     *
     * @param timeMillis
     * @return
     */
    public static String getMMDDHHMMString(long timeMillis) {
        Date date = new Date(timeMillis);
        return getMMDDHHMMString(date);
    }

    /**
     * /**
     * 返回格式为“MM-dd HH:mm”时间字符串
     *
     * @param date
     * @return
     */
    private static String getMMDDHHMMString(Date date) {
        String dateStr = "";
        if (date == null)
            return dateStr;
        dateStr = SDF_MMDDHHMM.format(date);
        return dateStr;
    }


    /**
     * 格式化一个时间字段的值，返回对应字符串，
     * 比如0格式化为“00”，8格式化为“08”
     *
     * @param fieldValue
     * @return
     */
    public static String formatIntField2String(int fieldValue) {
        if (fieldValue < 10)
            return "0" + fieldValue;
        return fieldValue + "";
    }

    /**
     * 根据给定的时间格式，获取时间字符串
     *
     * @param date   日期
     * @param format 格式
     * @return
     */
    public static String getDateStringByFormat(@NonNull Date date, @NonNull String format) {
        if (TextUtils.isEmpty(format) || date == null)
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        if (dateFormat == null)
            return null;
        return dateFormat.format(date);
    }

}
