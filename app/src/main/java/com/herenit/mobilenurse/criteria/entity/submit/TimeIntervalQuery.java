package com.herenit.mobilenurse.criteria.entity.submit;

import java.util.Date;

/**
 * author: HouBin
 * date: 2019/2/18 17:08
 * desc: 通用的提交时间范围+科室代码（或者病区号），一般用于查询数据
 */
public class TimeIntervalQuery extends BaseSubmit {
    /**
     * 科室代码或者病区号
     */
    protected String groupCode;

    //开始时间
    protected Long startDateTime;

    //结束时间
    protected Long stopDateTime;

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Long getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        if (startDateTime != null)
            this.startDateTime = startDateTime.getTime();
        else
            this.startDateTime = null;
    }

    public Long getStopDateTime() {
        return stopDateTime;
    }

    public void setStopDateTime(Date stopDateTime) {
        if (stopDateTime != null)
            this.stopDateTime = stopDateTime.getTime();
        else
            this.stopDateTime = null;
    }
}
