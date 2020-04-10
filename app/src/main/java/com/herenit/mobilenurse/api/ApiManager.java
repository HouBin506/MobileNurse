package com.herenit.mobilenurse.api;

import android.text.TextUtils;

import com.herenit.mobilenurse.datastore.sp.ConfigSp;

/**
 * author: HouBin
 * date: 2019/8/28 17:15
 * desc:
 */
public class ApiManager {
    /**
     * 根据检查报告单号拼接检查报告页面地址
     *
     * @param examNo
     * @return
     */
    public static String getExamReportPagerUrl(String examNo) {
        return baseUrl() + Api.EXAM_REPORT_PAGER_PATH + "?examNo=" + examNo;
    }

    /**
     * 根据检查报告单号拼接检查报告页面地址
     *
     * @param labNo
     * @return
     */
    public static String getLabReportPagerUrl(String labNo) {
        return baseUrl() + Api.LAB_REPORT_PAGER_PATH + "?labNo=" + labNo;
    }

    /**
     * 根据检查报告单号拼接微生物检查报告页面地址
     *
     * @param labNo
     * @return
     */
    public static String getMicroorganismLabReportPagerUrl(String labNo) {
        return baseUrl() + Api.MICROORGANISM_LAB_REPORT_PAGER_PATH + "?labNo=" + labNo;
    }

    public static String baseUrl() {
        String serviceAddress = baseServiceAddress();
        return serviceAddress + Api.DOMAIN;
    }

    public static String baseServiceAddress() {
        String serviceAddress = ConfigSp.getInstance().getServiceAddress();
        if (TextUtils.isEmpty(serviceAddress))
            serviceAddress = Api.PROTOCOL + Api.IP_PORT;
        else
            serviceAddress = Api.PROTOCOL + serviceAddress;
        return serviceAddress;
    }
}
