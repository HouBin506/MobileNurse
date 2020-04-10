package com.herenit.mobilenurse.datastore.tempcache;

/**
 * author: HouBin
 * date: 2019/2/20 17:05
 * desc:公用的、常用的一些临时存储、管理
 */
public class CommonTemp {
    private static CommonTemp mInstance;

    private CommonTemp() {

    }

    public static CommonTemp getInstance() {
        if (mInstance == null)
            mInstance = new CommonTemp();
        return mInstance;
    }

    /**
     * 时间误差，记录服务器时间与客户端时间的差值，从而实现客户端时间于服务器时间同步
     */
    private long timeDeviation = 0;

    public long getTimeDeviation() {
        return timeDeviation;
    }

    public void setTimeDeviation(long timeDeviation) {
        this.timeDeviation = timeDeviation;
    }

}
