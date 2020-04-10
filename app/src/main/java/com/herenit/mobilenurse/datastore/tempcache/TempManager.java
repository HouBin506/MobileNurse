package com.herenit.mobilenurse.datastore.tempcache;

/**
 * author: HouBin
 * date: 2019/5/24 10:38
 * desc: 临时存储管理类
 */
public class TempManager {


    /**
     * 清除临时存储
     */
    public static void clearTemp() {
        SickbedTemp.getInstance().clear();
        UserTemp.getInstance().clear();
    }
}
